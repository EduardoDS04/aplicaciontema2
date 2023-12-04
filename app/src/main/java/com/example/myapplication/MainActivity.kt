package com.example.myapplication

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.AlarmClock
import android.speech.tts.TextToSpeech
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var progressBar: ProgressBar

    private var siHabla = false

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val llamada = findViewById<Button>(R.id.btllamar)
        val Botonconfiguracion = findViewById<Button>(R.id.btconfiguracion)
        val juegoDados = findViewById<Button>(R.id.juegoDados)
        val botonTiktok = findViewById<Button>(R.id.btTiktok)
        val botonAlarma = findViewById<Button>(R.id.btalarma)
        val botonChiste = findViewById<Button>(R.id.btchiste)
        imageView = findViewById(R.id.FotoPrincipal)
        progressBar = findViewById(R.id.pbChiste)

        configureTextToSpeech()

        llamada.setOnClickListener {
            val llamadaIntent = Intent(this, Llamada::class.java)
            startActivity(llamadaIntent)
        }
        Botonconfiguracion.setOnClickListener {
            val llamadaIntent = Intent(this, Configuracion::class.java)
            startActivity(llamadaIntent)
        }
        juegoDados.setOnClickListener {
            val llamadaIntent = Intent(this, Dados::class.java)
            startActivity(llamadaIntent)
        }
        botonTiktok.setOnClickListener {
            val enlace = "https://www.tiktok.com"
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(enlace))
            startActivity(webIntent)
        }
        botonAlarma.setOnClickListener {
            val calendar = java.util.Calendar.getInstance()
            val horaActual = calendar.get(java.util.Calendar.HOUR_OF_DAY)
            val minutosActuales = calendar.get(java.util.Calendar.MINUTE)

            val minutos = minutosActuales + 11
            val hora = horaActual + minutos / 60
            val minutosDespues = minutos % 60

            try {
                val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                    putExtra(AlarmClock.EXTRA_HOUR, hora)
                    putExtra(AlarmClock.EXTRA_MINUTES, minutosDespues)
                    putExtra(AlarmClock.EXTRA_SKIP_UI, true)
                    putExtra(AlarmClock.EXTRA_MESSAGE, "Alarma")
                    putExtra(AlarmClock.EXTRA_VIBRATE, true)
                    putExtra(AlarmClock.EXTRA_RINGTONE, "content://settings/system/alarm_alert")
                }

                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error al configurar la alarma", Toast.LENGTH_SHORT).show()
            }
        }
        botonChiste.setOnClickListener {
            if (!siHabla) {
                siHabla = true

                progressBar.visibility = View.VISIBLE

                startProgressBarAnimation()

                val listaChistes = resources.getStringArray(R.array.listaChistes)
                val randomIndex = Random.nextInt(listaChistes.size)
                val chisteSeleccionado = listaChistes[randomIndex]
                speakMeDescription(chisteSeleccionado)

                GlobalScope.launch(Dispatchers.Main) {
                    delay(500)
                    siHabla = false

                    stopProgressBarAnimation()

                    GlobalScope.launch(Dispatchers.Main) {
                        delay(5000)
                        progressBar.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    private fun configureTextToSpeech() {
        textToSpeech = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
            if (it != TextToSpeech.ERROR) {
                textToSpeech.language = Locale.getDefault()
            }
        })
    }

    private fun speakMeDescription(s: String) {
        textToSpeech.speak(s, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun captureScreen(): Bitmap? {
        val vista = window.decorView.rootView
        vista.isDrawingCacheEnabled = true
        val captura = Bitmap.createBitmap(vista.drawingCache)
        vista.isDrawingCacheEnabled = false
        return captura
    }

    private fun startProgressBarAnimation() {
        progressBar.rotation = 0f
        val rotate = ObjectAnimator.ofFloat(progressBar, "rotation", 360f)
        rotate.duration = 600
        rotate.interpolator = LinearInterpolator()
        rotate.start()
    }

    private fun stopProgressBarAnimation() {
        progressBar.clearAnimation()
    }

    override fun onDestroy() {
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }
}
