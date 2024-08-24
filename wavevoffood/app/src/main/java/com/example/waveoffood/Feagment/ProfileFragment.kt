package com.example.waveoffood.Feagment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.waveoffood.databinding.FragmentFrofileBinding
import com.example.waveoffood.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentFrofileBinding
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFrofileBinding.inflate(inflater, container, false)
        setUserData()

        binding.apply {
            name.isEnabled = false
            email.isEnabled = false
            address.isEnabled =false
            phone.isEnabled = false
        binding.editButton.setOnClickListener {
                name.isEnabled =! name.isEnabled
                email.isEnabled =! email.isEnabled
                address.isEnabled =! address.isEnabled
                phone.isEnabled =! phone.isEnabled
            }
        }

        binding.saveInfoButton.setOnClickListener {
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val phone = binding.phone.text.toString()
            val address = binding.address.text.toString()

            updateUserData(name, email, phone, address)
        }
        return binding.root
    }

    private fun updateUserData(name: String, email: String, phone: String, address: String) {
        val userId  = auth.currentUser?.uid
        if(userId != null){
            val userReference = database.getReference("user").child(userId)
            val userData = hashMapOf(
                "name" to name,
                "email" to email,
                "phone" to phone,
                "address" to address
            )
            userReference.setValue(userData).addOnSuccessListener {
                Toast.makeText(requireContext(),"Profile update Successfuly", Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener{
                    Toast.makeText(requireContext(),"Profile update Failed", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun setUserData(){
        val userId = auth.currentUser?.uid
        if(userId != null){
            val userRefrence = database.getReference("user").child(userId)
            userRefrence.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val userProfile = snapshot.getValue(UserModel::class.java)
                        if(userProfile != null){
                            binding.name.setText(userProfile.name).toString()
                            binding.address.setText(userProfile.address).toString()
                            binding.email.setText(userProfile.email).toString()
                            binding.phone.setText(userProfile.phone).toString()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }
}