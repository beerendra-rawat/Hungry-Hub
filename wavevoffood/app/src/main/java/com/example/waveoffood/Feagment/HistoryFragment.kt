package com.example.waveoffood.Feagment

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.waveoffood.adaptar.BuyAgainAdaptar
import com.example.waveoffood.databinding.FragmentHistoryBinding
import com.example.waveoffood.model.OrderDetails
import com.example.waveoffood.recentOrderItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAgainAdaptar

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private var listOfOderItem: ArrayList<OrderDetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        //Retrieve and display the user order history
        retrieveBuyHistory()
        binding.recentBuyItem.setOnClickListener {
            seeItemsRecentBuy()
        }
        binding.ReceivedButton.setOnClickListener {
            updateOrderStatus()
        }
        return binding.root
    }

    private fun updateOrderStatus() {
        val itemPushKey = listOfOderItem[0].itemPushKey
        val completeOrderReference = database.reference.child("CompletedOrder").child(itemPushKey!!)
        completeOrderReference.child("paymentReceived").setValue(true)
    }

    private fun seeItemsRecentBuy() {
        listOfOderItem.firstOrNull()?.let { recentBuy ->
            val intent = Intent(requireContext(), recentOrderItems::class.java)
            intent.putExtra("RecentBuyOrderItems", listOfOderItem)
            startActivity(intent)
        }
    }

    private fun retrieveBuyHistory() {
        binding.recentBuyItem.visibility = View.INVISIBLE
        userId = auth.currentUser?.uid ?: ""
        val buyItemReference: DatabaseReference =
            database.reference.child("user").child(userId).child("BuyHistory")
        val shortingQuery = buyItemReference.orderByChild("currentTime")

        shortingQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (buySnapshot in snapshot.children) {
                    val buyHistoryItem = buySnapshot.getValue(OrderDetails::class.java)
                    buyHistoryItem?.let {
                        listOfOderItem.add(it)
                    }
                }
                listOfOderItem.reverse()
                if (listOfOderItem.isNotEmpty()) {
                    setDataInRecentBuyItem()
                    setPreviousBuyItemsRecyclerView()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun setDataInRecentBuyItem() {
        binding.recentBuyItem.visibility = View.VISIBLE
        val recentOderItem = listOfOderItem.firstOrNull()
        recentOderItem?.let {
            with(binding) {
                buyAgainFoodName.text = it.foodName?.firstOrNull() ?: ""
                buyAgainFoodPrice.text = it.foodPrice?.firstOrNull() ?: ""
                val image = it.foodImage?.firstOrNull() ?: ""
                val uri = Uri.parse(image)
                Glide.with(requireContext()).load(uri).into(buyAgainFoodImage)

                val isOrderIsAccepted = listOfOderItem[0].orderAccepted
              //  Log.d("TAG", "setDataInRecentBuyItem: &isOrderIsAccepted")
                if (isOrderIsAccepted!!){
                    OrderStatus.background.setTint(Color.GREEN)
                    ReceivedButton.visibility = View.VISIBLE
                }

                if (listOfOderItem.isNotEmpty()) {
                }
            }
        }
    }

    private fun setPreviousBuyItemsRecyclerView() {
        val buyAgainFoodName = mutableListOf<String>()
        val buyAgainFoodPrice = mutableListOf<String>()
        val buyAgainFoodImage = mutableListOf<String>()
        for (i in 0 until listOfOderItem.size) {////1
            listOfOderItem[i].foodName?.firstOrNull()?.let {
                buyAgainFoodName.add(it)
                listOfOderItem[i].foodPrice?.firstOrNull()?.let {
                    buyAgainFoodPrice.add(it)
                    listOfOderItem[i].foodImage?.firstOrNull()?.let {
                        buyAgainFoodImage.add(it)
                    }
                }
                val rv = binding.BuyAgainRecyclerView
                rv.layoutManager = LinearLayoutManager(requireContext())
                buyAgainAdapter = BuyAgainAdaptar(
                    buyAgainFoodName,
                    buyAgainFoodPrice,
                    buyAgainFoodImage,
                    requireContext()
                )
                rv.adapter = buyAgainAdapter
            }
        }
    }
}
