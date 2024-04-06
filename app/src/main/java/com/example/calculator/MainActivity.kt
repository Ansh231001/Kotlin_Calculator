package com.example.calculator
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.calculator.ui.theme.CalculatorTheme
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import kotlinx.android.synthetic.main.activity_main.editText
import kotlinx.android.synthetic.main.activity_main.textView
import org.w3c.dom.Text

class MainActivity : ComponentActivity() {
    private var canAddOperations = false
    private var decimalPlace = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun onClear(view: View) {
        editText.text = ""
    }

    fun onBS(view: View) {
        val length: Int = editText.length()
        if (length > 0)
            editText.text = editText.text.dropLast(1)
    }

    fun numberClick(view: View) {
        if (view is Button) {
            if (view.text == ".") {
                if (decimalPlace)
                    editText.append(view.text)
                decimalPlace = false
            } else
                editText.append(view.text)
            canAddOperations = true
        }
    }

    fun operation(view: View) {
        if (view is Button && canAddOperations) {
            editText.append(view.text)
            canAddOperations = false
            decimalPlace = true
        }
    }

    fun equalTo(view: View) {
        textView.text = calRes()
    }

    private fun calRes(): String{
        val digitsOp = digit()
        if(digitsOp.isEmpty())
            return ""

        val mulDiv = muldiv(digitsOp)
        if(mulDiv.isEmpty())
            return ""

        val result = addSub(mulDiv)
        return result.toString()
    }

    private fun addSub(mulDiv: MutableList<Any>): Float {
        var res = mulDiv[0] as Float
        for(i in mulDiv.indices){
            if(mulDiv[i] is Char && i != mulDiv.lastIndex)
            {
                val op = mulDiv[i]
                val nextDig = mulDiv[i+1] as Float
                if(op == '+')
                    res += nextDig
                if(op == '-')
                    res -= nextDig
            }
        }
        return res
    }

    private fun muldiv(digitsOp: MutableList<Any>): MutableList<Any> {
        var list = digitsOp
        while(list.contains('x') || list.contains('/')){
            list = calDiv(list)
        }
        return list
    }

    private fun calDiv(list: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var resIndex = list.size
        for(i in list.indices)
        {
            if(list[i] is Char && i != list.lastIndex && i < resIndex)
            {
                val op = list[i]
                val nextdig = list[i+1] as Float
                val prevdig = list[i-1] as Float
                when(op){
                    'x'-> {
                        newList.add(prevdig*nextdig)
                        resIndex = i+1
                    }
                    '/'->{
                        newList.add(prevdig/nextdig)
                        newList.add(op)
                    }
                }
            }
            if(i > resIndex)
                newList.add(list[i])
        }
        return newList
    }


    private fun digit():MutableList<Any>{
        val list = mutableListOf<Any>()
        var currDig = ""
        for(char in editText.text){
            if(char.isDigit() || char == '.')
                currDig += char
            else
            {
                list.add(currDig.toFloat())
                currDig = ""
                list.add(char)
            }
        }
        if(currDig != "")
            list.add(currDig.toFloat())
        return list
    }

}


