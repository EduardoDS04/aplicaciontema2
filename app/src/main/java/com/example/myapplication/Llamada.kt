package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Llamada : AppCompatActivity() {
    private val REQUEST_PHONE_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.llamada_main)

        val botonLlamada2 = findViewById<Button>(R.id.llamarConfirmar)

        botonLlamada2.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_PHONE_PERMISSION)
                } else {
                    llamar()
                }
            } else {
                llamar()
            }
        }
    }
    private fun llamar() {
        val numeroTelefono = "tel:+34 682878911"
        val intentLlamada = Intent(Intent.ACTION_CALL, Uri.parse(numeroTelefono))
        if (intentLlamada.resolveActivity(packageManager) != null) {
            startActivity(intentLlamada)
        } else {
            enCasoDeError()
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PHONE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                llamar()
            } else {
                enCasoDeError()
            }
        }
    }


    private fun enCasoDeError() {
        Toast.makeText(this, "No tiene permisos", Toast.LENGTH_SHORT).show()
    }
}