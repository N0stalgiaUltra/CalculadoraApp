package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.textInputServiceFactory
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculator.ui.theme.CalculatorTheme
import com.example.calculator.ui.theme.NewCyan
import com.example.calculator.ui.theme.NewRed

sealed class CalculatorAction{
    data class Number(val number: Int): CalculatorAction()
    object Clear: CalculatorAction()
    object Decimal: CalculatorAction()
    object Calculate: CalculatorAction()

    data class Operation(val operation: CalculatorOperation): CalculatorAction()
}

sealed class CalculatorOperation(val symbol: String ){
    object Add: CalculatorOperation("+")
    object Sub: CalculatorOperation("-")
    object Mul: CalculatorOperation("*")
    object Div: CalculatorOperation("/")
}

data class CalculatorState(
    val num1: String = "",
    val num2: String = "",
    val operation: CalculatorOperation? = null
)

//View Model serve para atualizar os estados
class CalculatorViewModel: ViewModel(){
    var state by mutableStateOf(CalculatorState())
    private set

    fun onAction(action: CalculatorAction){
        when(action) {
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Operation -> enterOperation(action.operation)
            is CalculatorAction.Decimal -> enterDecimal()
            is CalculatorAction.Calculate -> enterCalculate()
            is CalculatorAction.Clear-> state = CalculatorState() //reseta o estado
        }
    }

    private fun enterCalculate() {
        val number1 = state.num1.toDoubleOrNull()
        val number2 = state.num2.toDoubleOrNull()

        if(number1 != null && number2 != null){
            val result = when(state.operation){
                is CalculatorOperation.Add -> number1 + number2
                is CalculatorOperation.Sub -> number1 - number2
                is CalculatorOperation.Mul -> number1 * number2
                is CalculatorOperation.Div -> number1 / number2
                null -> return
            }
            state = state.copy(
                num1 = result.toString().take(15),
                num2 = "",
                operation = null
            )
        }


    }

    private fun enterDecimal() {
        if(state.operation == null &&
            !state.num1.contains(",")
        ){
            if(state.num1.isBlank()){
                state = state.copy(
                    num1 = "0"+","
                )
            }
            else{
                state = state.copy(
                    num1 = state.num1 + ","
                )
            }
            return
        }

        if(!state.num2.contains(",")){
            if(state.num2.isBlank()){
                state = state.copy(
                    num2 = "0"+","
                )
            }
            else{
                state = state.copy(
                    num2 = state.num2 + ","
                )
            }
            return

        }
    }

    private fun enterOperation(operation: CalculatorOperation) {
        println("Enter Operation Clicado")

        if(state.num1.isNotBlank()){
            //atualiza o estado gerando uma copia com a nova operação
            state = state.copy(operation = operation)
        }
    }

    private fun enterNumber(number: Int) {
       if(state.operation == null)
       {
           if(state.num1.length >= 8)
                return

           state = state.copy(
               num1 = state.num1 + number
           )
           return
       }
        if(state.num2.length >= 8)
            return
        state = state.copy(
            num2 = state.num2 + number
        )
    }
}
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                val viewModel = viewModel<CalculatorViewModel>()
                val state = viewModel.state
                val buttonSpacing = 8.dp

                Calculator(state, modifier = Modifier, buttonSpacing, viewModel)
            }
        }
    }

}

@Composable
private fun Calculator(
    state: CalculatorState,
    modifier: Modifier = Modifier,
    buttonSpacing: Dp = 8.dp,
    viewModel: CalculatorViewModel
){

    Box(modifier = Modifier.fillMaxSize()
        .background(Color.DarkGray)
        .padding(16.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            Text(
                text = state.num1 + (state.operation?.symbol ?: "") + state.num2,
                //text = "TEXT",
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                color = Color.White,
                fontWeight = FontWeight.Light,
                maxLines = 2,
                fontSize = 80.sp
            )

            SetButtons(viewModel)
        }
    }
}

@Composable
private fun SetHeader() {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(Color.Green)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Result of the Calculation"
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Text(text = "$")
        }
    }
}



@Composable
private fun SetButtons(viewModel: CalculatorViewModel) {
        Row(modifier = Modifier.padding(8.dp)) {
            CreateButtons(text = "C",
                modifier = Modifier
                    .background(NewCyan)
                    .aspectRatio(2f)
                    .weight(2f),
                onClick = {

                    viewModel.onAction(CalculatorAction.Clear)
                }
            )
            CreateButtons(text = "=",
                modifier = Modifier
                    .background(NewCyan)
                    .aspectRatio(2f)
                    .weight(2f),
                onClick = {
                    viewModel.onAction(CalculatorAction.Calculate)
                }
            )
        }
        //Numeros de 1 a 3
        Row(modifier = Modifier.padding(8.dp)) {
            CreateButtons(text = "1",
                modifier = Modifier
                    .background(NewCyan)
                    .aspectRatio(1f)
                    .weight(1f),
                onClick = {
                    viewModel.onAction(CalculatorAction.Number(1))
                }
            )
            CreateButtons(text = "2",
                modifier = Modifier
                    .background(NewCyan)
                    .aspectRatio(1f)
                    .weight(1f),
                onClick = {
                    viewModel.onAction(CalculatorAction.Number(2))
                }
            )
            CreateButtons(text = "3",
                modifier = Modifier
                    .background(NewCyan)
                    .aspectRatio(1f)
                    .weight(1f),
                onClick = {
                    viewModel.onAction(CalculatorAction.Number(3))
                }
            )
            CreateButtons(text = "+",
                modifier = Modifier
                    .background(NewRed)
                    .aspectRatio(1f)
                    .weight(1f),
                onClick = {
                    viewModel.onAction(CalculatorAction.Operation(CalculatorOperation.Add))
                }
            )
        }
        Row(modifier = Modifier.padding(8.dp)) {
            CreateButtons(text = "4",
                modifier = Modifier
                    .background(NewCyan)
                    .aspectRatio(1f)
                    .weight(1f),
                onClick = {
                    viewModel.onAction(CalculatorAction.Number(4))
                }
            )
            CreateButtons(text = "5",
                modifier = Modifier
                    .background(NewCyan)
                    .aspectRatio(1f)
                    .weight(1f),
                onClick = {
                    viewModel.onAction(CalculatorAction.Number(5))
                }
            )
            CreateButtons(text = "6",
                modifier = Modifier
                    .background(NewCyan)
                    .aspectRatio(1f)
                    .weight(1f),
                onClick = {
                    viewModel.onAction(CalculatorAction.Number(6))
                }
            )
            CreateButtons(text = "-",
                modifier = Modifier
                    .background(NewRed)
                    .aspectRatio(1f)
                    .weight(1f),
                onClick = {
                    viewModel.onAction(CalculatorAction.Operation(CalculatorOperation.Sub))
                }
            )
        }
        Row(modifier = Modifier.padding(8.dp)) {
            CreateButtons(text = "7",
                modifier = Modifier
                    .background(NewCyan)
                    .aspectRatio(1f)
                    .weight(1f),
                onClick = {
                    viewModel.onAction(CalculatorAction.Number(7))
                }
            )
            CreateButtons(text = "8",
                modifier = Modifier
                    .background(NewCyan)
                    .aspectRatio(1f)
                    .weight(1f),
                onClick = {
                    viewModel.onAction(CalculatorAction.Number(8))
                }
            )
            CreateButtons(text = "9",
                modifier = Modifier
                    .background(NewCyan)
                    .aspectRatio(1f)
                    .weight(1f),
                onClick = {
                    viewModel.onAction(CalculatorAction.Number(9))
                }
            )
            CreateButtons(text = "*",
                modifier = Modifier
                    .background(NewRed)
                    .aspectRatio(1f)
                    .weight(1f),
                onClick = {
                    viewModel.onAction(CalculatorAction.Operation(CalculatorOperation.Mul))
                }
            )
        }
        Row(modifier = Modifier.padding(8.dp)) {
            CreateButtons(text = "0",
                modifier = Modifier
                    .background(NewCyan)
                    .aspectRatio(2f)
                    .weight(2f),
                onClick = {
                    viewModel.onAction(CalculatorAction.Number(0))
                }
            )
            CreateButtons(text = ".",
                modifier = Modifier
                    .background(NewCyan)
                    .aspectRatio(1f)
                    .weight(1f),
                onClick = {
                    viewModel.onAction(CalculatorAction.Decimal)
                }
            )
            CreateButtons(text = "/",
                modifier = Modifier
                    .background(NewRed)
                    .aspectRatio(1f)
                    .weight(1f),
                onClick = {
                    viewModel.onAction(CalculatorAction.Operation(CalculatorOperation.Div))
                }
            )
        }

}

@Composable
private fun CreateButtons(text: String, modifier: Modifier, onClick: ()-> Unit){
    Box(
        contentAlignment = Alignment.Center,
        //Não passa o modifier do param pois existem botões com shapes diferentes
        modifier = Modifier
            .clip(CircleShape)
            .clickable { onClick() }
            .then(modifier) //aplica as mudanças ao modifier do param
    ){
        Text(
            text = text,
            fontSize = 36.sp,
            color = Color.Black

        )
    }
}

