package com.project.bisacatering.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.opengl.Visibility
import android.os.Build.VERSION_CODES.S
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.project.bisacatering.R
import com.project.bisacatering.databinding.ActivityRegisterBinding
import java.util.Calendar
import java.util.concurrent.TimeUnit

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var context: Context
    private var currStep: Int = 1

    private var forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null

    private var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    private var mVerificationId: String? = null
    private var userId: String? = null

    private lateinit var firebaseAuth: FirebaseAuth
    private val db = Firebase.firestore

    private val TAG = "MAIN_TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context = this

        firebaseAuth = FirebaseAuth.getInstance()
        mCallbacks = object : OnVerificationStateChangedCallbacks() {
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
                forceResendingToken = token
                binding.loading.visibility = View.GONE
                binding.btnContinue.isEnabled = true
                binding.btnContinue.visibility = View.VISIBLE

                Toast.makeText(context, "OTP Send Successfully", Toast.LENGTH_SHORT).show()

                clear()
                stepTwo()
                currStep++
            }
        }

        stepOne()
        currStep++

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
                        verifyOTP(mVerificationId!!, binding.otp.text.toString())
                    }
                }
                4->{
                    val name = binding.edtName.text.toString()
                    val email = binding.edtEmail.text.toString()
                    val birthYear = binding.datePicker.year.toString()
                    val birthMonth = (binding.datePicker.month + 1).toString()
                    val birthDayofMonth = binding.datePicker.dayOfMonth.toString()
                    val gender = findViewById<RadioButton>(binding.rgGender.checkedRadioButtonId)?.text.toString()

                    if(TextUtils.isEmpty(name) or TextUtils.isEmpty(email) or TextUtils.isEmpty(birthYear) or TextUtils.isEmpty(birthMonth) or TextUtils.isEmpty(birthDayofMonth) or TextUtils.isEmpty(gender)){
                        Toast.makeText(context, "Harap isi semua informasi", Toast.LENGTH_SHORT).show()
                    }else {
                        val birth = "$birthDayofMonth-$birthMonth-$birthYear"
                        saveProfilData(name, email, birth, gender)
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

    private fun stepThree(){
        val c = Calendar.getInstance();
        val year = c.get(Calendar.YEAR);
        val month = c.get(Calendar.MONTH);
        val day = c.get(Calendar.DAY_OF_MONTH);

        binding.datePicker.init(year, month, day, null)

        binding.btnContinue.text = "Register"
        binding.stepThree.visibility = View.VISIBLE
    }

    private fun clear(){
        binding.loading.visibility = View.GONE
        binding.stepOne.visibility = View.GONE
        binding.stepTwo.visibility = View.GONE
        binding.stepThree.visibility = View.GONE
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
            .setCallbacks(mCallbacks!!)
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

                    userId = user?.uid.toString()
                    checkUid(userId!!)

                    clear()
                    stepThree()
                    currStep++
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

    private fun saveProfilData(name: String, email: String, birth: String, gender: String) {

        val userData = hashMapOf(
            "name" to name,
            "email" to email,
            "birth" to birth,
            "gender" to gender
        )

        binding.loading.visibility = View.VISIBLE
        binding.btnContinue.isEnabled = false
        binding.btnContinue.visibility = View.GONE

        db.collection("users").document(userId!!)
            .set(userData)
            .addOnSuccessListener {
                binding.loading.visibility = View.GONE
                binding.btnContinue.isEnabled = true
                binding.btnContinue.visibility = View.VISIBLE

                val intent = Intent(this, SuccessActivity::class.java)
                intent.putExtra("TITLE", "Pendaftaran Berhasil!")
                intent.putExtra("DESC", "Selamat kamu telah berhasil mendaftar di BisaCatering. Sesaat lagi kamu akan diarahkan ke halaman utama kami. Kami senang dapat bertemu dengan mu.")
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
                Log.w(TAG, "Error writing document", e)

                binding.loading.visibility = View.GONE
                binding.btnContinue.isEnabled = true
                binding.btnContinue.visibility = View.VISIBLE
            }
    }

    private fun checkUid(uid: String){
        val docRef = db.collection("users").document(uid)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    Log.d("MAIN_ACTIVITY", "TRUE")
                    firebaseAuth.signOut()
                    Toast.makeText(context, "Anda sudah memiliki akun, Silahkan pilih menu masuk", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.d("MAIN_ACTIVITY", "FALSE")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }
}