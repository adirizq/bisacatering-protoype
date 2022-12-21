package com.project.bisacatering.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.project.bisacatering.R
import com.project.bisacatering.activity.DetailMenuActivity
import com.project.bisacatering.activity.RocaActivity
import com.project.bisacatering.adapter.MenuAdapter
import com.project.bisacatering.databinding.FragmentHomeBinding
import com.project.bisacatering.model.Menu
import org.checkerframework.checker.units.qual.A

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var menuLarisAdapter: MenuAdapter
    private var db = Firebase.firestore
    private var menuLaris = ArrayList<Menu>()

    private val TAG = "MAIN_TAG"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuLarisAdapter = MenuAdapter(requireContext(), menuLaris)
        binding.rvLaris.adapter = menuLarisAdapter
        binding.loadingLaris.visibility = View.VISIBLE

        db.collection("menu_laris")
            .get()
            .addOnSuccessListener { documents ->
                menuLaris.clear()
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")

                    menuLaris.add(
                        Menu(
                            document.data["name"] as String,
                            (document.data["price"] as Long).toInt(),
                            document.data["person"] as String,
                            document.data["process"] as String,
                        )
                    )
                }
                menuLarisAdapter.notifyDataSetChanged()
                binding.loadingLaris.visibility = View.GONE
            }
            .addOnFailureListener { exception ->
                binding.loadingLaris.visibility = View.GONE
                Toast.makeText(requireContext(), exception.toString(), Toast.LENGTH_SHORT).show()
                Log.w(TAG, "Error getting documents: ", exception)
            }

        binding.pilihMenuManual.setOnClickListener {
            val item = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).menu.findItem(R.id.navigation_menu)
            NavigationUI.onNavDestinationSelected(item, view.findNavController())
        }

        binding.pilihRoca.setOnClickListener {
            val intent = Intent(context, RocaActivity::class.java)
            startActivity(intent)
        }

//        binding.laris1.setOnClickListener {
//            val intent = Intent(context, DetailMenuActivity::class.java)
//            startActivity(intent)
//        }

        val promoList = ArrayList<SlideModel>()
        promoList.add(SlideModel(R.drawable.promo_1))
        promoList.add(SlideModel(R.drawable.promo_2))
        promoList.add(SlideModel(R.drawable.promo_3))

        val blogList = ArrayList<SlideModel>()
        blogList.add(SlideModel(R.drawable.blog_1))
        blogList.add(SlideModel(R.drawable.blog_2))
        blogList.add(SlideModel(R.drawable.blog_3))

        val promoSlider = binding.promoSlider
        promoSlider.setImageList(promoList, ScaleTypes.CENTER_CROP)

        val blogSlider = binding.blogSlider
        blogSlider.setImageList(blogList, ScaleTypes.CENTER_CROP)
    }

}