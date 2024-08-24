package com.example.waveoffood

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.waveoffood.databinding.ActivityDetailBinding
import com.example.waveoffood.model.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var foodName:String? = null
    private var foodPrice:String? = null
    private var foodDescription:String? = null
    private var foodIngredient:String? = null
    private var foodImage:String? = null
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize Firebase Auth
        auth = FirebaseAuth.getInstance()


        foodName = intent.getStringExtra("MenuItemName")
        foodPrice = intent.getStringExtra("MenuItemPrice")
        foodDescription = intent.getStringExtra("MenuItemDescription")
        foodIngredient = intent.getStringExtra("MenuItemIngredients")
        foodImage = intent.getStringExtra("MenuItemImage")

        with(binding){
            DetailFoodName.text = foodName
            DescriptionTextView.text =foodDescription
            IngredientTextView.text = foodIngredient
            Glide.with(this@DetailActivity).load(Uri.parse(foodImage)).into(DetailFoodImage)
        }

        binding.imageButton.setOnClickListener {
            finish()
        }
        binding.addItemButton.setOnClickListener {
            addItemToCart()
        }
    }

    private fun addItemToCart() {
        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid?:""
        //create a cartItem object
        val cartItem = CartItems(
            foodName.toString(),
            foodPrice.toString(),
            foodDescription.toString(),
            foodImage.toString(),foodIngredient.toString(),1)

        //save data to cartItem to firebase
        database.child("user").child(userId).child("CartItem").push().setValue(cartItem).addOnSuccessListener {
            Toast.makeText(this, "Item added into cart Successful", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this, "Item not added", Toast.LENGTH_SHORT).show()
        }
    }
}