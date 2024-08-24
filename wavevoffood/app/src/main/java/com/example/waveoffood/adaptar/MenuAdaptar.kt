package com.example.waveoffood.adaptar

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.waveoffood.DetailActivity
import com.example.waveoffood.databinding.MenuItemBinding
import com.example.waveoffood.model.MenuItem

class MenuAdaptar(
    private val menuItems: List<MenuItem>,
    private val requireContext: Context
): RecyclerView.Adapter<MenuAdaptar.MenuViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }
    override fun getItemCount(): Int = menuItems.size


    inner class MenuViewHolder (private val binding: MenuItemBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener{
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION){
                    openDetailsActivity(position)
                }
            }
        }
        private fun openDetailsActivity(position: Int) {
            val menuItem = menuItems[position]
            val intent = Intent(requireContext, DetailActivity::class.java).apply {
                putExtra("MenuItemName", menuItem.foodName)
                putExtra("MenuItemPrice", menuItem.foodPrice)
                putExtra("MenuItemDescription", menuItem.foodDescription)
                putExtra("MenuItemIngredients", menuItem.foodIngredient)
                putExtra("MenuItemImage", menuItem.foodImage)
            }
            //start the details Activity
            requireContext.startActivity(intent)
        }
        fun bind(position: Int) {
            val menuitem = menuItems[position]
            binding.apply {
                MenuFoodName.text = menuitem.foodName
                MenuPrice.text = menuitem.foodPrice
                val Uri = Uri.parse(menuitem.foodImage)
                Glide.with(requireContext).load(Uri).into(foodImage)
            }
        }
    }
}
