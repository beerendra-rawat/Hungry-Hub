package com.example.waveoffood

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.waveoffood.adaptar.MenuAdaptar
import com.example.waveoffood.databinding.FragmentMenuBottomSheetBinding
import com.example.waveoffood.model.MenuItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MenuBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentMenuBottomSheetBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBottomSheetBinding.inflate(inflater, container, false)

        binding.ButtonBack.setOnClickListener {
            dismiss()
        }
        retriveMenuItems()
        return binding.root

    }

    private fun retriveMenuItems() {
        database = FirebaseDatabase.getInstance()
        val foodRef :DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()
        foodRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(foodSnapshot in snapshot.children){
                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let{menuItems.add(it)}
                }
                Log.d("ITEMS", "onDataChange: Data Received")
                setAdapter()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    private fun setAdapter() {
        if(menuItems.isNotEmpty()){
            val adapter = MenuAdaptar(menuItems, requireContext())
            binding.MenuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.MenuRecyclerView.adapter = adapter
            Log.d("Item", "setAdapter: data set")
        }
        else{
            Log.d("Item", "setAdapter: data not set")
        }
    }
}