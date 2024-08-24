package com.example.adminwaveooffood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.util.Log
import com.example.adminwaveooffood.databinding.ActivityMainBinding
import com.example.adminwaveooffood.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var completeOrderReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.AddMenu.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }
        binding.AllItemMenu.setOnClickListener {
            val intent = Intent(this, AllItemActivity::class.java)
            startActivity(intent)
        }
        binding.OutForDeliveryItemButton.setOnClickListener {
            val intent = Intent(this, OutForDeliveryActivity::class.java)
            startActivity(intent)
        }
        binding.Profile.setOnClickListener {
            val intent = Intent(this, AdminProfile::class.java)
            startActivity(intent)
        }
        binding.CreateUSer.setOnClickListener {
            val intent = Intent(this, CreateUser::class.java)
            startActivity(intent)
        }
        binding.PendingOrderTextView.setOnClickListener {
            val intent = Intent(this, PendingOrder::class.java)
            startActivity(intent)
        }

//        binding.logOut.setOnClickListener {
//            auth.signOut()
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()
//        }
        auth = FirebaseAuth.getInstance()
        binding.logOut.setOnClickListener {
            try {
                auth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } catch (e: Exception) {
                Log.e("Logout", "Error during logout", e)
            }
        }



        pendingOrders()
        completedOrders()
        wholeTimeEarning()

    }

    private fun wholeTimeEarning() {
        var listOfTotalPay = mutableListOf<Int>()
        completeOrderReference = FirebaseDatabase.getInstance().reference.child("CompletedOrder")
        completeOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children){
                    var completeOrder = orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.totalPrice?.replace("$", "")?.toIntOrNull()
                        ?.let { i ->
                            listOfTotalPay.add(i)
                        }
                }
                //binding.wholeTimeEarning.text = listOfTotalPay.sum().toString() + "&"
                binding.wholeTimeEarning.text = listOfTotalPay.sum().toString() + "$"
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun completedOrders() {
        database = FirebaseDatabase.getInstance()
        var completedOrderReference = database.reference.child("CompletedOrder")
        var completedOrderItemCount = 0
        completedOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                completedOrderItemCount = snapshot.childrenCount.toInt()
                binding.completeOrder.text = completedOrderItemCount.toString()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun pendingOrders() {
        database = FirebaseDatabase.getInstance()
        var pendingOrderReference = database.reference.child("OrderDetails")
        var pendingOrderItemCount = 0
        pendingOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                pendingOrderItemCount = snapshot.childrenCount.toInt()
                binding.pendingOrder.text = pendingOrderItemCount.toString()
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}