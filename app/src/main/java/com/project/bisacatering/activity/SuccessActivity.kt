package com.project.bisacatering.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.project.bisacatering.R
import com.project.bisacatering.databinding.ActivitySuccessBinding

class SuccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        binding = ActivitySuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras

        if (extras != null) {
            binding.tvTitle.text = extras.getString("TITLE")
            binding.tvDesc.text = extras.getString("DESC")
        }

        val zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in_success)
        val zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoom_out_success)

        binding.icSuccess.animation = zoomIn
        binding.icSuccess.animation = zoomOut

        val timer = object: CountDownTimer(2000, 1000) {
            override fun onTick(p0: Long) {}

            override fun onFinish() {
                val intent = Intent(this@SuccessActivity, MainActivity::class.java)
                intent.flags =  Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        zoomIn.setAnimationListener(object: Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {}

            override fun onAnimationRepeat(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {
                binding.icSuccess.startAnimation(zoomOut)
            }

        })

        zoomOut.setAnimationListener(object: Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {}

            override fun onAnimationRepeat(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {
                timer.start()
            }

        })

        binding.icSuccess.startAnimation(zoomIn)
    }
}