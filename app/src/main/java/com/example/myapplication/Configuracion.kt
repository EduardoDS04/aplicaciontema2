package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class Configuracion : AppCompatActivity() {

    private lateinit var EDnombreJugador: EditText
    private lateinit var EDnTiradas: EditText
    private lateinit var switchStatic: Switch
    private lateinit var btGuardarConfiguracion: Button
    private lateinit var spinnerGustoJuego: Spinner
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes)

        EDnombreJugador = findViewById(R.id.nombreJugador)
        EDnTiradas = findViewById(R.id.numeroTiradas)
        switchStatic = findViewById(R.id.switchCartaONumero)
        btGuardarConfiguracion = findViewById(R.id.btnGuardarConfiguracion)
        spinnerGustoJuego = findViewById(R.id.spinnerGustoJuego)

        sharedPreferences = getSharedPreferences("configuracion", MODE_PRIVATE)
        val nombreJugador = sharedPreferences.getString("Jugador", "") ?: ""
        val numeroTiradas = sharedPreferences.getInt("Tiradas", 5)
        val DadosEstaticosActivo = sharedPreferences.getBoolean("estatico", false)

        EDnombreJugador.setText(nombreJugador)
        EDnTiradas.setText(numeroTiradas.toString())
        switchStatic.isChecked = DadosEstaticosActivo

        // Configura el ArrayAdapter para el Spinner con las opciones mencionadas
        val opcionesGustoJuego = arrayOf("Me encanta", "No", "Podria ser mejor")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesGustoJuego)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGustoJuego.adapter = adapter

        btGuardarConfiguracion.setOnClickListener {
            val nuevoNombreJugador = EDnombreJugador.text.toString()
            val nuevoNumeroTiradas = EDnTiradas.text.toString().toIntOrNull() ?: 5

            if (nuevoNumeroTiradas > 0) {
                val estatico = switchStatic.isChecked
                val gustoJuegoSeleccionado = spinnerGustoJuego.selectedItem.toString()

                // Guarda la configuración en SharedPreferences
                guardarConfiguracion(
                    nuevoNombreJugador,
                    nuevoNumeroTiradas,
                    estatico,
                    gustoJuegoSeleccionado
                )
            } else {
            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun guardarConfiguracion(
        nombreJugador: String,
        numeroTiradas: Int,
        estatico: Boolean,
        gustoJuego: String
    ) {
        val editor = sharedPreferences.edit()
        editor.putString("Jugador", nombreJugador)
        editor.putInt("Tiradas", numeroTiradas)
        editor.putBoolean("estatico", estatico)
        editor.putString("GustoJuego", gustoJuego)
        editor.apply()
    }
}
