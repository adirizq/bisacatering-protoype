package com.project.bisacatering.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.project.bisacatering.R
import com.project.bisacatering.activity.DetailMenuActivity
import com.project.bisacatering.adapter.MenuAdapter
import com.project.bisacatering.databinding.FragmentHomeBinding
import com.project.bisacatering.databinding.FragmentMenuBinding
import com.project.bisacatering.model.Menu

class MenuFragment : Fragment() {

    private lateinit var binding: FragmentMenuBinding

    private val db = Firebase.firestore
    private val TAG = "MAIN_TAG"

    private var menuLaris = ArrayList<Menu>()
    private var menuPaket1 = ArrayList<Menu>()
    private var menuPaket2 = ArrayList<Menu>()
    private var menuPaket3 = ArrayList<Menu>()
    private var menuAyam = ArrayList<Menu>()
    private var menuSapi = ArrayList<Menu>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMenuBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.loadingLaris.visibility = View.VISIBLE
        binding.loadingPaket1.visibility = View.VISIBLE
        binding.loadingPaket2.visibility = View.VISIBLE
        binding.loadingPaket3.visibility = View.VISIBLE
        binding.loadingAyam.visibility = View.VISIBLE
        binding.loadingSapi.visibility = View.VISIBLE

        val menuLarisAdapter = MenuAdapter(requireContext(), menuLaris)
        binding.rvLaris.adapter = menuLarisAdapter

        fetchData(binding.rvLaris, binding.loadingLaris, "menu_laris", menuLaris, menuLarisAdapter)


        val menuPaket1Adapter = MenuAdapter(requireContext(), menuPaket1)
        binding.rvPaket1.adapter = menuPaket1Adapter

        fetchData(binding.rvPaket1, binding.loadingPaket1, "menu_paket_1", menuPaket1, menuPaket1Adapter)


        val menuPaket2Adapter = MenuAdapter(requireContext(), menuPaket2)
        binding.rvPaket2.adapter = menuPaket2Adapter

        fetchData(binding.rvPaket2, binding.loadingPaket2, "menu_paket_2", menuPaket2, menuPaket2Adapter)


        val menuPaket3Adapter = MenuAdapter(requireContext(), menuPaket3)
        binding.rvPaket3.adapter = menuPaket3Adapter

        fetchData(binding.rvPaket3, binding.loadingPaket3, "menu_paket_3", menuPaket3, menuPaket3Adapter)


        val menuAyamAdapter = MenuAdapter(requireContext(), menuAyam)
        binding.rvAyam.adapter = menuAyamAdapter

        fetchData(binding.rvAyam, binding.loadingAyam, "menu_ayam", menuAyam, menuAyamAdapter)


        val menuSapiAdapter = MenuAdapter(requireContext(), menuSapi)
        binding.rvSapi.adapter = menuSapiAdapter

        fetchData(binding.rvSapi, binding.loadingSapi, "menu_sapi", menuSapi, menuSapiAdapter)


    }

    private fun fetchData(rv: RecyclerView, loading: CircularProgressIndicator, path: String, list: ArrayList<Menu>, adapter: MenuAdapter){
        db.collection(path)
            .get()
            .addOnSuccessListener { documents ->
                list.clear()
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")

                    list.add(
                        Menu(
                            document.data["name"] as String,
                            (document.data["price"] as Long).toInt(),
                            document.data["person"] as String,
                            document.data["process"] as String,
                        )
                    )
                }
                adapter.notifyDataSetChanged()
                loading.visibility = View.GONE
            }
            .addOnFailureListener { exception ->
                loading.visibility = View.GONE
                Toast.makeText(requireContext(), exception.toString(), Toast.LENGTH_SHORT).show()
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }
}