package com.example.appconversao

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    private lateinit var resultado: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultado =  findViewById<TextView>(R.id.txtResultado)
        val btnCalcular = findViewById<Button>(R.id.btnCalcular)

        btnCalcular.setOnClickListener{
            converter()
        }
    }
    private fun converter(){
        val rdngroup = findViewById<RadioGroup>(R.id.rdnGroup)
        val checked = rdngroup.checkedRadioButtonId

        val moeda = when(checked){
            R.id.rdnDolar -> "USD"
            else -> "EUR"
        }

        val txtValor = findViewById<EditText>(R.id.txtValor)
        val valor = txtValor.text.toString()

        if(valor.isEmpty()) {
            return
        }

        resultado.text = valor

        Thread{
            val url = URL("https://free.currconv.com/api/v7/convert?q=${moeda}_BRL&compact=ultra&apiKey=91946959beeec572ee1c")
            val conn = url.openConnection() as HttpsURLConnection

            try{
                val data = conn.inputStream.bufferedReader().readText()

                val obj = JSONObject(data)

                runOnUiThread {
                    val res = obj.getDouble("${moeda}_BRL")
                    resultado.text = "R$ ${"%.4f".format(valor.toDouble() * res)}"
                }

            }finally {
                conn.disconnect()
            }
        }.start()
    }
}