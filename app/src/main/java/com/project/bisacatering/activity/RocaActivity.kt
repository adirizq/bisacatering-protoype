package com.project.bisacatering.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.project.bisacatering.R
import com.project.bisacatering.databinding.ActivityRocaBinding
import java.text.NumberFormat
import java.util.*


class RocaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRocaBinding
    private var currStep: Int = 1

    private lateinit var rg1: RadioGroup
    private lateinit var rg2: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        binding = ActivityRocaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvRoca.setDelay(50);
        stepZero()

        binding.recommendation1.setOnClickListener {
            val intent = Intent(this, DetailMenuActivity::class.java)
            startActivity(intent)
        }

        binding.btnContinue.setOnClickListener {
            clear()
            binding.tvRoca.removeAnimation()
            if(currStep == 1){
                stepOne()
                currStep++
            }else if(currStep == 2){
                stepTwo()
                currStep++
            }else if(currStep == 3){
                stepThree()
                currStep++
            }else if(currStep == 4){
                stepFour()
                currStep++
            }else if(currStep == 5){
                stepFive()
                currStep++
            }else if(currStep == 7){
                finish()
            }
        }
    }

    private fun stepZero(){
        binding.tvRoca.animateText("Halo, perkenalkan saya RoCa." +
                "\nDisini saya akan membantu kamu memilih menu dan paket catering melalui rekomendasi dari aku, Roca si Robot Caterting. " +
                "\nTenang semua pasti BisaCatering.")

    }

    private fun stepOne(){
        binding.tvRoca.animateText("Pertama, aku perlu tahu nih. Menu apa aja nih yang kamu sukai atau yang kamu ingin tambahkan ke dalam catering kamu?")
        binding.stepOne.visibility = View.VISIBLE
    }

    private fun stepTwo(){
        binding.tvRoca.animateText("Mantab, kira-kira catering kamu untuk acara apa nih?")
        binding.stepTwo.visibility = View.VISIBLE

        rg1 = binding.rgJenis1
        rg2 = binding.rgJenis2

        rg1.clearCheck()
        rg2.clearCheck()

        rg1.setOnCheckedChangeListener(listener1);
        rg2.setOnCheckedChangeListener(listener2);
    }

    private fun stepThree(){
        binding.tvRoca.animateText("Sip, rencananya untuk berapa orang nih catering yang kamu pesen?")
        binding.stepThree.visibility = View.VISIBLE
    }

    private fun stepFour(){
        binding.tvRoca.animateText("Terakhir nih, berapa budget yang kamu sediain untuk catering ini?")
        binding.stepFour.visibility = View.VISIBLE

        var currentMin = ""
        binding.edtBudgetMin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun afterTextChanged(s: Editable) {
                val stringText = s.toString()

                if(stringText != currentMin && stringText.isNotEmpty()) {
                    binding.edtBudgetMin.removeTextChangedListener(this)

                    val locale = Locale("in", "ID")
                    val cleanString = stringText.replace("[Rp,.]".toRegex(), "")

                    if(cleanString.isNotEmpty()){
                        val parsed = cleanString.toInt()
                        val formatted = NumberFormat.getCurrencyInstance(locale).format(parsed)
                        val normal = formatted.substringBefore(",")

                        currentMin = normal
                        binding.edtBudgetMin.setText(normal)
                        binding.edtBudgetMin.setSelection(normal.length)
                        binding.edtBudgetMin.addTextChangedListener(this)
                    } else {
                        currentMin = ""
                        binding.edtBudgetMin.setText("")
                        binding.edtBudgetMin.addTextChangedListener(this)
                    }

                }
            }
        })

        var currentMax = ""
        binding.edtBudgetMax.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun afterTextChanged(s: Editable) {
                val stringText = s.toString()

                if(stringText != currentMax && stringText.isNotEmpty()) {
                    binding.edtBudgetMax.removeTextChangedListener(this)

                    val locale = Locale("in", "ID")
                    val cleanString = stringText.replace("[Rp,.]".toRegex(), "")

                    if(cleanString.isNotEmpty()){
                        val parsed = cleanString.toInt()
                        val formatted = NumberFormat.getCurrencyInstance(locale).format(parsed)
                        val normal = formatted.substringBefore(",")

                        currentMin = normal
                        binding.edtBudgetMax.setText(normal)
                        binding.edtBudgetMax.setSelection(normal.length)
                        binding.edtBudgetMax.addTextChangedListener(this)
                    } else {
                        currentMax = ""
                        binding.edtBudgetMax.setText("")
                        binding.edtBudgetMax.addTextChangedListener(this)
                    }
                }
            }
        })
    }

    private fun stepFive(){
        var loading = false
        val zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        val zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoom_out)

        binding.loadingRoca.animation = zoomIn
        binding.loadingRoca.animation = zoomOut

        zoomIn.setAnimationListener(object: AnimationListener {
            override fun onAnimationStart(p0: Animation?) {}

            override fun onAnimationRepeat(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {
                if(loading){
                    binding.loadingRoca.startAnimation(zoomOut)
                }
            }

        })

        zoomOut.setAnimationListener(object: AnimationListener {
            override fun onAnimationStart(p0: Animation?) {}

            override fun onAnimationRepeat(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {
                if(loading){
                    binding.loadingRoca.startAnimation(zoomIn)
                }
            }

        })

        val timer = object: CountDownTimer(6000, 1000) {
            override fun onTick(p0: Long) {}

            override fun onFinish() {
                loading = false
                binding.stepFive.visibility = View.GONE
                stepSix()
            }
        }

        loading = true
        binding.stepFive.visibility = View.VISIBLE
        binding.loadingRoca.startAnimation(zoomIn)
        timer.start()
    }

    private fun stepSix(){
        binding.btnContinue.text = "Finish"
        currStep++

        binding.tvRoca.animateText("Ini rekomendasi menu dan paket dari RoCa, semoga dapat membantu kamu ya.")
        binding.stepSix.visibility = View.VISIBLE
    }

    private fun clear(){
        binding.stepOne.visibility = View.GONE
        binding.stepTwo.visibility = View.GONE
        binding.stepThree.visibility = View.GONE
        binding.stepFour.visibility = View.GONE
        binding.stepFive.visibility = View.GONE
    }

    private val listener1: RadioGroup.OnCheckedChangeListener =
        RadioGroup.OnCheckedChangeListener { _, checkedId ->
            if (checkedId != -1) {
                rg2.setOnCheckedChangeListener(null)
                rg2.clearCheck()
                rg2.setOnCheckedChangeListener(listener2)
            }
        }

    private val listener2: RadioGroup.OnCheckedChangeListener =
        RadioGroup.OnCheckedChangeListener { _, checkedId ->
            if (checkedId != -1) {
                rg1.setOnCheckedChangeListener(null)
                rg1.clearCheck()
                rg1.setOnCheckedChangeListener(listener1)
            }
        }
}