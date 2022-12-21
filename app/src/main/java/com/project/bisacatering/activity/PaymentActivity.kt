package com.project.bisacatering.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.project.bisacatering.R
import com.project.bisacatering.databinding.ActivityPaymentBinding
import com.project.bisacatering.model.Menu
import java.text.NumberFormat
import java.util.*
import kotlin.math.round

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding

    private lateinit var dataMenu: Menu

    private val db = Firebase.firestore
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val gson = Gson()
    private val locale = Locale("in", "ID")
    private val TAG = "MAIN_TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras

        if (extras != null) {
            dataMenu = gson.fromJson(extras.getString("DATA_MENU", ""), Menu::class.java)

            val shipment = 35000
            val price = dataMenu.price
            val tax = round(dataMenu.price * 0.11).toInt()
            val totalPrice = price + tax + shipment

            binding.tvMenuName.text = "1x " + dataMenu.name
            binding.tvTotalPrice.text = formatPrice(totalPrice)
            binding.tvPrice.text = formatPrice(price)
            binding.tvShipment.text = formatPrice(shipment)
            binding.tvTax.text = formatPrice(tax)
            binding.tvTotalPriceDetail.text = formatPrice(totalPrice)

            binding.btnPesan.setOnClickListener {
                var type: String? = null

                if (binding.rgPengataran.checkedRadioButtonId != -1){
                    type = findViewById<RadioButton>(binding.rgPengataran.checkedRadioButtonId).text.toString()
                }

                val year = binding.datePicker.year.toString()
                val month = (binding.datePicker.month + 1).toString()
                val dayofMonth = binding.datePicker.dayOfMonth.toString()

                if(type == null || TextUtils.isEmpty(year) or TextUtils.isEmpty(month) or TextUtils.isEmpty(dayofMonth)){
                    Toast.makeText(this, "Harap isi semua informasi", Toast.LENGTH_SHORT).show()
                }else {
                    val date = "$dayofMonth-$month-$year"
                    saveOrder(dataMenu.name, totalPrice, type, date)
                }
            }
        }
    }

    private fun formatPrice(price: Int): String {
        val formatted = NumberFormat.getCurrencyInstance(locale).format(price)
        return formatted.substringBefore(",")
    }

    private fun saveOrder(name: String, totalPrice: Int, type: String, date: String) {

        val orderData = hashMapOf(
            "name" to name,
            "total_price" to totalPrice,
            "type" to type,
            "date" to date
        )

        val user = firebaseAuth.currentUser

        binding.loading.visibility = View.VISIBLE
        binding.btnPesan.isEnabled = false
        binding.btnPesan.visibility = View.GONE

        db.collection("users").document(user!!.uid)
            .collection("orders")
            .add(orderData)
            .addOnSuccessListener {
                binding.loading.visibility = View.GONE
                binding.btnPesan.isEnabled = true
                binding.btnPesan.visibility = View.VISIBLE

                val intent = Intent(this, SuccessActivity::class.java)
                intent.putExtra("TITLE", "Pemesanan Berhasil!")
                intent.putExtra("DESC", "Pesanan kamu langsung kami proses")
                intent.putExtra("ORDER", true)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                Log.w(TAG, "Error writing document", e)

                binding.loading.visibility = View.GONE
                binding.btnPesan.isEnabled = true
                binding.btnPesan.visibility = View.VISIBLE
            }
    }
}