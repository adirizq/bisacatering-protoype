package com.project.bisacatering.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.bisacatering.R
import com.project.bisacatering.activity.DetailMenuActivity
import com.project.bisacatering.databinding.FragmentHomeBinding
import com.project.bisacatering.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {

    private lateinit var binding: FragmentMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMenuBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.laris1.setOnClickListener {
            val intent = Intent(context, DetailMenuActivity::class.java)
            startActivity(intent)
        }
    }
}