package com.project.bisacatering.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.text.bold
import androidx.core.text.color
import com.google.firebase.auth.FirebaseAuth
import com.project.bisacatering.R
import com.project.bisacatering.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(firebaseAuth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finishAffinity()
        }

        val s = SpannableStringBuilder()
            .append("Dengan masuk atau mendaftar, kamu menyetujui ")
            .color(ContextCompat.getColor(applicationContext, R.color.yellow), { bold { append("Ketentuan Layanan") } })
            .append(" dan ")
            .color(ContextCompat.getColor(applicationContext, R.color.yellow), { bold { append("Kebijakan Privasi") } })

        binding.btmDesc.text = s

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}