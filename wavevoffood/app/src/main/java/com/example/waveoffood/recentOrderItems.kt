package com.example.waveoffood

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.waveoffood.adaptar.RecentBuyAdapter
import com.example.waveoffood.databinding.ActivityRecentOrderItemsBinding
import com.example.waveoffood.model.OrderDetails

class recentOrderItems : AppCompatActivity() {

    private val binding: ActivityRecentOrderItemsBinding by lazy {
        ActivityRecentOrderItemsBinding.inflate(layoutInflater)
    }


    private lateinit var allFoodNames:ArrayList<String>
    private lateinit var allFoodImages:ArrayList<String>
    private lateinit var allFoodPrices:ArrayList<String>
    private lateinit var allFoodQuantities:ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        val recentOrderItems = intent.getSerializableExtra("RecentBuyOrderItems") as? ArrayList<OrderDetails>
        recentOrderItems?.let { orderDetails ->
            if(orderDetails.isNotEmpty()){
                val recentOrderItems = orderDetails[0]
                allFoodNames = recentOrderItems.foodName as ArrayList<String>
                allFoodImages = recentOrderItems.foodImage as ArrayList<String>
                allFoodPrices = recentOrderItems.foodPrice as ArrayList<String>
                allFoodQuantities = recentOrderItems.cartQuantity as ArrayList<Int>
            }
        }
        setAdapter()
    }
    private fun setAdapter() {
        //val rv = binding.buyRecyclerView
        binding.buyRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = RecentBuyAdapter(this, allFoodNames, allFoodImages, allFoodPrices, allFoodQuantities)
        binding.buyRecyclerView.adapter = adapter
    }
}