package com.project.bisacatering.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.project.bisacatering.activity.DetailMenuActivity
import com.project.bisacatering.adapter.MenuAdapter
import com.project.bisacatering.databinding.FragmentFavoriteBinding
import com.project.bisacatering.model.Menu

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding

    private val db = Firebase.firestore
    private val TAG = "MAIN_TAG"

    private var menuFavorite = ArrayList<Menu>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.loadingFavorite.visibility = View.VISIBLE

        val menuFavoriteAdapter = MenuAdapter(requireContext(), menuFavorite)
        binding.rvFavorite.adapter = menuFavoriteAdapter

        db.collection("wishlist")
            .get()
            .addOnSuccessListener { documents ->
                menuFavorite.clear()
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")

                    menuFavorite.add(
                        Menu(
                            document.data["name"] as String,
                            (document.data["price"] as Long).toInt(),
                            document.data["person"] as String,
                            document.data["process"] as String,
                        )
                    )
                }
                menuFavoriteAdapter.notifyDataSetChanged()
                binding.loadingFavorite.visibility = View.GONE
            }
            .addOnFailureListener { exception ->
                binding.loadingFavorite.visibility = View.GONE
                Toast.makeText(requireContext(), exception.toString(), Toast.LENGTH_SHORT).show()
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }
}