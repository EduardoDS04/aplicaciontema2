package com.example.myapplication

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityDadosBinding
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class Dados : AppCompatActivity() {

    private lateinit var bindingDados: ActivityDadosBinding
    private lateinit var sharedPreferences: SharedPreferences

    private var resultado: Int = 0
    private var numeroMaximo: Int = 6 // Valor predeterminado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingDados = ActivityDadosBinding.inflate(layoutInflater)
        setContentView(bindingDados.root)
        initEvent()

        sharedPreferences = getSharedPreferences("configuracion", MODE_PRIVATE)
        val btnAtras = findViewById<Button>(R.id.atras)
        btnAtras.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initEvent() {
        bindingDados.resultadoText.visibility = View.INVISIBLE
        bindingDados.imageButton.setOnClickListener {
            bindingDados.resultadoText.visibility = View.VISIBLE
            game()
        }
    }

    private fun game() {
        val nombreJugador = sharedPreferences.getString("nombreJugador", "") ?: ""
        val numeroTiradas = sharedPreferences.getInt("Tiradas",5 )
        val estatico = sharedPreferences.getBoolean("estatico", false)

        numeroMaximo = sharedPreferences.getInt("numeroMaximoSeleccionado", 6)

        scheduleRun(nombreJugador, numeroTiradas, estatico)
    }

    private fun scheduleRun(nombreJugador: String, numeroTiradas: Int, estatico: Boolean) {
        val schedulerExecutor = Executors.newSingleThreadScheduledExecutor()
        val msc = 1000
        for (i in 1..numeroTiradas) {
            schedulerExecutor.schedule(
                {
                    throwDadoInTime(estatico)
                },
                msc * i.toLong(), TimeUnit.MILLISECONDS
            )
        }
        schedulerExecutor.schedule({
            viewResult(nombreJugador)
        }, msc * (numeroTiradas + 1).toLong(), TimeUnit.MILLISECONDS)

        schedulerExecutor.shutdown()
    }

    private fun throwDadoInTime(estatico: Boolean) {
        val numDados = Array(3) { Random.nextInt(1, numeroMaximo + 1) }
        val imageViews: Array<ImageView> = arrayOf(
            bindingDados.imagviewDado1,
            bindingDados.imagviewDado2,
            bindingDados.imagviewDado3
        )

        resultado = numDados.sum()
        for (i in 0 until 3) {
            selectView(imageViews[i], numDados[i], estatico)
        }
    }

    private fun selectView(imgV: ImageView, imagenes: Int, estatico: Boolean) {
        if (estatico) {

        } else {
            when (imagenes) {
                1 -> imgV.setImageResource(R.drawable.dado1)
                2 -> imgV.setImageResource(R.drawable.dado2)
                3 -> imgV.setImageResource(R.drawable.dado3)
                4 -> imgV.setImageResource(R.drawable.dado4)
                5 -> imgV.setImageResource(R.drawable.dado5)
                6 -> imgV.setImageResource(R.drawable.dado6)
            }
        }
    }

    private fun viewResult(nombreJugador: String) {
        val resultadoTextView: TextView = findViewById(R.id.resultadoText)
        resultadoTextView.text = "$nombreJugador tienes $resultado"
        println(resultado)
    }
}