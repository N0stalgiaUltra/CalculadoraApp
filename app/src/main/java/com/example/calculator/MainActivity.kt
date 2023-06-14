package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.textInputServiceFactory
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                //SetHeader()
                SetButtons()
                //Calculate(1f,2f,"+")
            }
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
private fun Calculate(value1: Float, value2: Float, op: String) {
    //atualizar o estado de returnValue
    //entregar o valor de return value no header
    //assim como recuperar o valor dos elementos a serem somados no header
    var returnValue = 0f

    if (op.equals("+"))
        returnValue = value1 + value2
    else if(op.equals("-"))
        returnValue = value1 - value2
    else if(op.equals("*"))
        returnValue = value1 * value2
    else if(op.equals("/"))
        returnValue = value1 / value2
    else
        returnValue = 0f

    Text(text = "A soma deu $returnValue")
}

@Composable
private fun SetButtons() {
    //4Colunas
    // +, -, *, /, =
    //   1, 2, 3
    //   4, 5, 6
    //   7, 8, 9
    //      0

    //cada clique, concatena string
    // ao apertar algum operador, converte a string p/float
    var firstVal = ""
    var secondVal = ""

    Column(modifier = Modifier.padding(16.dp)) {
        //Operadores Aritméticos
        Row(modifier = Modifier.padding(8.dp)) {
            CreateButtons(text = "+")
            CreateButtons(text = "-")
            CreateButtons(text = "*")
            CreateButtons(text = "/")
            CreateButtons(text = "=")
        }
        //Numeros de 1 a 3
        Row(modifier = Modifier.padding(8.dp)) {
            CreateButtons(text = "1")
            CreateButtons(text = "2")
            CreateButtons(text = "3")
        }
        Row(modifier = Modifier.padding(8.dp)) {
            CreateButtons(text = "4")
            CreateButtons(text = "5")
            CreateButtons(text = "6")
        }
        Row(modifier = Modifier.padding(8.dp)) {
            CreateButtons(text = "7")
            CreateButtons(text = "8")
            CreateButtons(text = "9")
        }
        Row(modifier = Modifier.padding(8.dp)) {
            CreateButtons(text = "0")
        }

    }

}

@Composable
private fun CreateButtons(text: String){
    Button(onClick = { PrintValue(text) }) {
        Text(text = "$text")
    }
}

private fun PrintValue(value: String){
    println(value)
}