package com.example.waveoffood.adaptar

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.waveoffood.DetailActivity
import com.example.waveoffood.databinding.PopularitemBinding

class PopularAdaptar (private  val items:List<String>, private val price:List<String>, private val image:List<Int>, private val requireContext:Context) : RecyclerView.Adapter<PopularAdaptar.PopularViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        return PopularViewHolder(PopularitemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }



    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        val item = items[position]
        val price = price[position]
        val images = image[position]
        holder.bind(item, price, images)

        holder.itemView.setOnClickListener {
            val intent = Intent(requireContext, DetailActivity::class.java)
            intent.putExtra("MenuItemName", item)
            intent.putExtra("MenuImage", images)
            requireContext.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }
    class PopularViewHolder (private  val binding: PopularitemBinding) : RecyclerView.ViewHolder(binding.root){
        private val imagesView = binding.imageView5
        fun bind(item: String, price: String, images: Int) {
            binding.Foodnamepopular.text = item
            binding.Pricepopular.text = price
            imagesView.setImageResource(images)
        }

    }

}