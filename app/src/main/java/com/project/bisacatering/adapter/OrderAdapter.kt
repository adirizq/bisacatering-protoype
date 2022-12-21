package com.project.bisacatering.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.project.bisacatering.R
import com.project.bisacatering.activity.DetailMenuActivity
import com.project.bisacatering.databinding.ItemMenuBinding
import com.project.bisacatering.databinding.ItemOrderBinding
import com.project.bisacatering.model.Menu
import com.project.bisacatering.model.Order
import java.text.DateFormatSymbols
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderAdapter(private val context: Context, private val orderList: ArrayList<Order>): RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    private val locale = Locale("in", "ID")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            val formatted = NumberFormat.getCurrencyInstance(locale).format(orderList[position].total_price)
            val priceFormatted = formatted.substringBefore(",")

            val dates: List<String> = orderList[position].date.split("-")
            val month = DateFormatSymbols().months[dates[1].toInt()-1]

            binding.tvName.text = orderList[position].name
            binding.tvTotalPrice.text = priceFormatted
            binding.tvType.text = orderList[position].type
            binding.tvDate.text = "${dates[0]} $month ${dates[2]}"

            if(orderList[position].type == "Ambil Sendiri"){
                binding.icTitle.setImageResource(R.drawable.ic_round_inventory_2_24)
            }
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    class ViewHolder(val binding: ItemOrderBinding): RecyclerView.ViewHolder(binding.root)
}