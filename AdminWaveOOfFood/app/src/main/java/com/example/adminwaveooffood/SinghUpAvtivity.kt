package com.example.adminwaveooffood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.adminwaveooffood.databinding.ActivitySinghUpAvtivityBinding
import com.example.adminwaveooffood.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SinghUpAvtivity : AppCompatActivity() {

    private lateinit var userName: String
    private lateinit var nameOfRestaurant: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth : FirebaseAuth
    private lateinit var database: DatabaseReference

    private val binding: ActivitySinghUpAvtivityBinding by lazy {
        ActivitySinghUpAvtivityBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //intialization Firebase Auth
        auth = Firebase.auth
        //initialize Firebase database
        database = Firebase.database.reference

        binding.createUserButton.setOnClickListener {
            //gat text from edti text
            userName = binding.name.text.toString().trim()
            nameOfRestaurant = binding.restaurantName.text.toString().trim()
            email = binding.emailOrPhone.text.toString().trim()
            password = binding.password.text.toString().trim()

            if(userName.isBlank() || nameOfRestaurant.isBlank() || email.isBlank() || password.isBlank()){
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            }
            else{
                createAccount(email, password)
            }
        }

        binding.AlreadyHaveAccountButton.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        val LocaleList = arrayListOf("Alomra", "Nainital", "Pithoragarh", "Bageshwer", "Dehradun", "Haridwar")
        val adaptar = ArrayAdapter(this, android.R.layout.simple_list_item_1,LocaleList)
        val autoCompleteTextView = binding.ListOfLocation
        autoCompleteTextView.setAdapter(adaptar)
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(this, "Account Created Successfuly", Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this, "Account Creation Faild", Toast.LENGTH_SHORT).show()
                Log.d("Accoount", "CreateAccount: Failure", task.exception)
            }
        }
    }
    //save data into database
    private fun saveUserData() {
        userName = binding.name.text.toString().trim()
        nameOfRestaurant = binding.restaurantName.text.toString().trim()
        email = binding.emailOrPhone.text.toString().trim()
        password = binding.password.text.toString().trim()
        val user = UserModel(userName, nameOfRestaurant, email, password)
        val userID: String = FirebaseAuth.getInstance().currentUser!!.uid
        //save user data Firebase
        database.child("user").child(userID).setValue(user)

    }
}