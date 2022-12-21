package com.project.bisacatering.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil.setContentView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.project.bisacatering.R
import com.project.bisacatering.adapter.MenuAdapter
import com.project.bisacatering.adapter.OrderAdapter
import com.project.bisacatering.databinding.ActivityOrderListBinding
import com.project.bisacatering.model.Menu
import com.project.bisacatering.model.Order

class OrderListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderListBinding

    private val db = Firebase.firestore
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val TAG = "MAIN_TAG"

    private var orders = ArrayList<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        binding = ActivityOrderListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = firebaseAuth.currentUser

        val orderAdapter = OrderAdapter(this, orders)
        binding.rvOrder.adapter = orderAdapter

        db.collection("users").document(user!!.uid).collection("orders")
            .get()
            .addOnSuccessListener { documents ->
                orders.clear()
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")

                    orders.add(
                        Order(
                            document.data["name"] as String,
                            (document.data["total_price"] as Long).toInt(),
                            document.data["type"] as String,
                            document.data["date"] as String,
                        )
                    )
                }
                orderAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

}