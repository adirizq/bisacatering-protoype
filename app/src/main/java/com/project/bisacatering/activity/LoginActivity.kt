package com.project.bisacatering.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.project.bisacatering.R
import com.project.bisacatering.databinding.ActivityLoginBinding
import com.project.bisacatering.databinding.ActivityRegisterBinding
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var mVerificationId: String

    private val firebaseAuth = FirebaseAuth.getInstance()
    private var currStep: Int = 1

    private val TAG = "MAIN_TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val context = this

        stepOne()
        currStep++

        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
                Log.d("TAG", e.toString())
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(id, token)

                mVerificationId = id

                binding.loading.visibility = View.GONE
                binding.btnContinue.isEnabled = true
                binding.btnContinue.visibility = View.VISIBLE

                Toast.makeText(context, "OTP Send Successfully", Toast.LENGTH_SHORT).show()

                clear()
                stepTwo()
                currStep++
            }
        }

        binding.btnContinue.setOnClickListener {
            when(currStep){
                2->{
                    if(TextUtils.isEmpty(binding.edtPhoneNumber.text.toString().trim())){
                        Toast.makeText(context, "Harap isi Nomor HP", Toast.LENGTH_SHORT).show()
                    }else{
                        getOTP()
                    }
                }
                3->{
                    if(TextUtils.isEmpty(binding.otp.text.toString())){
                        Toast.makeText(context, "Harap isi OTP", Toast.LENGTH_SHORT).show()
                    }else{
                        verifyOTP(mVerificationId, binding.otp.text.toString())
                    }
                }
            }
        }
    }

    private fun stepOne(){
        binding.btnContinue.text = "Get OTP"
        binding.stepOne.visibility = View.VISIBLE
    }

    private fun stepTwo(){
        binding.btnContinue.text = "Verify"
        binding.stepTwo.visibility = View.VISIBLE
    }

    private fun clear(){
        binding.stepOne.visibility = View.GONE
        binding.stepTwo.visibility = View.GONE
    }

    private fun getOTP(){
        binding.loading.visibility = View.VISIBLE
        binding.btnContinue.isEnabled = false
        binding.btnContinue.visibility = View.GONE

        val phoneNumber = "+62" + binding.edtPhoneNumber.text.toString()

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mCallbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyOTP(id: String, code: String){
        binding.loading.visibility = View.VISIBLE
        binding.btnContinue.isEnabled = false
        binding.btnContinue.visibility = View.GONE

        val credential = PhoneAuthProvider.getCredential(id, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user

                    binding.loading.visibility = View.GONE
                    binding.btnContinue.isEnabled = true
                    binding.btnContinue.visibility = View.VISIBLE

                    val intent = Intent(this, SuccessActivity::class.java)
                    intent.putExtra("TITLE", "Berhasil Masuk!")
                    intent.putExtra("DESC", "Senang dapat bertemu dengamu lagi.")
                    startActivity(intent)
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        binding.loading.visibility = View.GONE
                        binding.btnContinue.isEnabled = true
                        binding.btnContinue.visibility = View.VISIBLE

                        Toast.makeText(this, "Kode OTP Salah" + task.exception, Toast.LENGTH_SHORT).show()
                    }
                    // Update UI
                }
            }
    }
}