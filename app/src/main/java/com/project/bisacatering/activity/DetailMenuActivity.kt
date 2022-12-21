package com.project.bisacatering.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.gson.Gson
import com.project.bisacatering.R
import com.project.bisacatering.databinding.ActivityDetailMenuBinding
import com.project.bisacatering.model.Menu
import java.text.NumberFormat
import java.util.*

class DetailMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailMenuBinding
    private lateinit var dataMenu: Menu

    private val gson = Gson()
    private val locale = Locale("in", "ID")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        binding = ActivityDetailMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras

        if (extras != null) {
            dataMenu = gson.fromJson(extras.getString("DATA_MENU", ""), Menu::class.java)

            val formatted = NumberFormat.getCurrencyInstance(locale).format(dataMenu.price)
            val priceFormatted = formatted.substringBefore(",")

            binding.tvTitle.text = dataMenu.name
            binding.tvPrice.text = priceFormatted
            binding.tvPorsi.text = dataMenu.person
            binding.tvProses.text = dataMenu.process

            binding.btnPesan.setOnClickListener {
                val intent = Intent(this, PaymentActivity::class.java)
                intent.putExtra("DATA_MENU", extras.getString("DATA_MENU", ""))
                startActivity(intent)
            }
        }
    }
}