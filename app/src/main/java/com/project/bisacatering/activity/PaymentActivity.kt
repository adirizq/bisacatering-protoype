package com.project.bisacatering.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.project.bisacatering.R
import com.project.bisacatering.databinding.ActivityPaymentBinding

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPesan.setOnClickListener {
            val intent = Intent(this, SuccessActivity::class.java)
            intent.putExtra("TITLE", "Pemesanan Berhasil!")
            intent.putExtra("DESC", "Pesanan kamu langsung kami proses")
            startActivity(intent)
        }
    }
}