package com.project.bisacatering.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.bisacatering.R
import com.project.bisacatering.activity.DetailMenuActivity
import com.project.bisacatering.activity.RocaActivity
import com.project.bisacatering.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pilihMenuManual.setOnClickListener {
            val item = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).menu.findItem(R.id.navigation_menu)
            NavigationUI.onNavDestinationSelected(item, view.findNavController())
        }

        binding.pilihRoca.setOnClickListener {
            val intent = Intent(context, RocaActivity::class.java)
            startActivity(intent)
        }

        binding.laris1.setOnClickListener {
            val intent = Intent(context, DetailMenuActivity::class.java)
            startActivity(intent)
        }

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