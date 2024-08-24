package com.example.adminwaveooffood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwaveooffood.adapter.OrderDetailsAdapter
import com.example.adminwaveooffood.databinding.ActivityOrderDetailesBinding
import com.example.adminwaveooffood.model.OrderDetails

class OrderDetailesActivity : AppCompatActivity() {
    private val binding: ActivityOrderDetailesBinding by lazy {
        ActivityOrderDetailesBinding.inflate(layoutInflater)
    }
    private var userName: String? =  null
    private var address: String? =  null
    private var phonenumber: String? =  null
    private var totalPrice: String? =  null
    private var foodName : ArrayList<String> = arrayListOf()
    private var foodImage : ArrayList<String> = arrayListOf()
    private var foodQuantity : ArrayList<Int> = arrayListOf()
    private var foodPrice : ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }
        getDataFromIntent()
    }

    private fun getDataFromIntent() {
        val receivedOrderDetails = intent.getSerializableExtra("UserOrderDetails") as OrderDetails
        receivedOrderDetails?.let {orderDetails ->
                userName = receivedOrderDetails.userName
                foodName = receivedOrderDetails.foodName as ArrayList<String>
                foodImage = receivedOrderDetails.foodImage as ArrayList<String>
                foodQuantity = receivedOrderDetails.cartQuantity as ArrayList<Int>
                address = receivedOrderDetails.address
                phonenumber = receivedOrderDetails.phoneNumber
                foodPrice = receivedOrderDetails.foodPrice as ArrayList<String>
                totalPrice = receivedOrderDetails.totalPrice

                setUserDetails()
                setAdapter()
        }
    }


    private fun setUserDetails() {
        binding.name.text = userName
        binding.address.text = address
        binding.phone.text = phonenumber
        binding.totalPay.text = totalPrice
    }

    private fun setAdapter() {
        binding.orderDetailsRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = OrderDetailsAdapter(this, foodName, foodImage, foodQuantity, foodPrice)
        binding.orderDetailsRecyclerView.adapter = adapter
    }
}