package com.project.bisacatering.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.project.bisacatering.activity.DetailMenuActivity
import com.project.bisacatering.databinding.ItemMenuBinding
import com.project.bisacatering.model.Menu
import org.checkerframework.checker.units.qual.s
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class MenuAdapter(private val context: Context, private val menuList: ArrayList<Menu>): RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    private val gson = Gson()
    private val locale = Locale("in", "ID")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            val formatted = NumberFormat.getCurrencyInstance(locale).format(menuList[position].price)
            val priceFormatted = formatted.substringBefore(",")

            binding.tvTitle.text = menuList[position].name
            binding.tvPrice.text = priceFormatted

            binding.menu.setOnClickListener {
                val json = gson.toJson(menuList[position])
                val intent = Intent(context, DetailMenuActivity::class.java)
                intent.putExtra("DATA_MENU", json)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    class ViewHolder(val binding: ItemMenuBinding): RecyclerView.ViewHolder(binding.root)
}