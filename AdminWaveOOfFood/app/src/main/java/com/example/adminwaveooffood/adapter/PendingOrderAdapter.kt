package com.example.adminwaveooffood.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminwaveooffood.databinding.PendingorderitemBinding

class PendingOrderAdapter(
    private val context: Context,
    private val customerName: MutableList<String>,
    private val totalPrice: MutableList<String>,
    private val foodImage: MutableList<String>,
    private val itemClicked: OnItemClicked,
): RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>() {
    interface OnItemClicked{
        fun onItemClickedListener(position: Int)
        fun onItemAcceptClickedListener(position: Int)
        fun onItemDispatchClickedListener(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderViewHolder {
        val binding = PendingorderitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PendingOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PendingOrderViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerName.size
    inner class PendingOrderViewHolder(private val binding: PendingorderitemBinding): RecyclerView.ViewHolder(binding.root) {
        private var isAccepted = false
        fun bind(position: Int) {
            binding.apply {

                Customername.text = customerName[position]
                TotalPay.text = totalPrice[position]

                var uriString = foodImage[position]
                var uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(OrderFoodImage)
                OrderAccept.apply {
                    if (!isAccepted){
                        text = "Accept"
                    }
                    else{
                        text = "Dispatch"
                    }
                    setOnClickListener {
                        if(!isAccepted){
                            text =  "Dispatch"
                            isAccepted = true
                            showToast("Order is Accepted")
                            itemClicked.onItemAcceptClickedListener(position)
                        }
                        else{
                            customerName.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            showToast("Order is Dispatch")
                            itemClicked.onItemDispatchClickedListener(position)
                        }
                    }
                }
                itemView.setOnClickListener {
                    itemClicked.onItemClickedListener(position)
                }
            }
        }
        private fun showToast(message: String){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}