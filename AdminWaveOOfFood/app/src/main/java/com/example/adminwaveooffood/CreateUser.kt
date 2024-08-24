package com.example.adminwaveooffood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.adminwaveooffood.databinding.ActivityCreateUserBinding

class CreateUser : AppCompatActivity() {
    private val binding: ActivityCreateUserBinding by lazy {
        ActivityCreateUserBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.BackButton.setOnClickListener {
            finish()
        }
    }
}