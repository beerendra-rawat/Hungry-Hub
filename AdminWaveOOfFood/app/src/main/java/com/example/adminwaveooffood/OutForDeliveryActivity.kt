package com.example.adminwaveooffood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwaveooffood.adapter.DeliveryAdapter
import com.example.adminwaveooffood.databinding.ActivityOutForDeliveryBinding
import com.example.adminwaveooffood.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OutForDeliveryActivity : AppCompatActivity() {
    private val binding: ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }

    private lateinit var database: FirebaseDatabase
    private var listOfCompleteOderList: ArrayList<OrderDetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.BackButton.setOnClickListener {
            finish()
        }

        //retrieve and dispatchedCompleted Order
        retrieveCompletedOrderDetails()


    }

    private fun retrieveCompletedOrderDetails() {
        //initialize firebase database
        database = FirebaseDatabase.getInstance()
        val completedOrderReference = database.reference.child("CompletedOrder")
            .orderByChild("currentTime")
        completedOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear the list before populating it with new data
                listOfCompleteOderList.clear()

               for (orderSnapshot in snapshot.children){
                   val completeOrder = orderSnapshot.getValue(OrderDetails::class.java)
                   completeOrder?.let{
                       listOfCompleteOderList.add(it)
                   }
               }
                //reverse the list to display order first
                listOfCompleteOderList.reverse()
                setDataIntoRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    private fun setDataIntoRecyclerView() {
        //initialize list to hold customers name and payment status
        val customerName = mutableListOf<String>()
        val moneyStatus = mutableListOf<Boolean>()

        for (order in listOfCompleteOderList){
            order.userName?.let{
                customerName.add(it)
            }
            moneyStatus.add(order.paymentReceived!!)
        }
        val adapter = DeliveryAdapter(customerName, moneyStatus)
        binding.DeliveryRecyclerView.adapter = adapter
        binding.DeliveryRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}