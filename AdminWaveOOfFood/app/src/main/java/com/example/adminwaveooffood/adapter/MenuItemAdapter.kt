package com.example.adminwaveooffood.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminwaveooffood.databinding.ItemItemBinding
import com.example.adminwaveooffood.model.AllMenu
import com.google.firebase.database.DatabaseReference

class MenuItemAdapter(
    private val context: Context,
    private val menuList: ArrayList<AllMenu>,
    databaseReference: DatabaseReference,
    private val onDeleteClickListener: (position:Int) ->Unit
): RecyclerView.Adapter<MenuItemAdapter.AddItemViewHolder>() {
    private val itemQuantity = IntArray(menuList.size) {1}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
        val binding = ItemItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
        holder.bind(position)
    }
    override fun getItemCount(): Int = menuList.size
    inner class AddItemViewHolder(private val binding: ItemItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantity[position]
                val menuItem = menuList[position]
                val uriString = menuItem.foodImage
                val uri = Uri.parse(uriString)
                foodName.text = menuItem.foodName
                foodPrice.text = menuItem.foodPrice
                Glide.with(context).load(uri).into(foodImageUri)
                TotalPay.text = quantity.toString()

                Minusbutton.setOnClickListener {
                    decreaseQuantity(position)
                }
                PlusButton.setOnClickListener {
                    increaseQunatity(position)
                }
                DeleteButton.setOnClickListener {
                    onDeleteClickListener(position)
                }
            }
        }

        private fun deleteQunatity(position: Int) {
            menuList.removeAt(position)
            menuList.removeAt(position)
            menuList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, menuList.size)
    }
        private fun increaseQunatity(position: Int) {
            if (itemQuantity[position] < 10) {
                itemQuantity[position] ++
                binding.TotalPay.text = itemQuantity[position].toString()
            }
        }

        private fun decreaseQuantity(position: Int) {
            if (itemQuantity[position] > 1) {
                itemQuantity[position]--
                binding.TotalPay.text = itemQuantity[position].toString()
            }
        }
    }
}
