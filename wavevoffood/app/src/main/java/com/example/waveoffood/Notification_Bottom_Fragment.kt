package com.example.waveoffood

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.waveoffood.adaptar.NotificationAdapter
import com.example.waveoffood.databinding.FragmentNotificationBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class Notification_Bottom_Fragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNotificationBottomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationBottomBinding.inflate(layoutInflater,container,false)
        val notification = listOf("Your order has been canceled Successfully", "Your order has been taken by driver", "Congrate your order place")
        val notificationImage = listOf(R.drawable.sademoji, R.drawable.icon_3,R.drawable.illustration)
        val adapter = NotificationAdapter(ArrayList(notification), ArrayList(notificationImage))
        binding.NotificationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.NotificationRecyclerView.adapter = adapter
        return binding.root
    }

    companion object {

    }
}