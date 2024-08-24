package com.example.waveoffood.adaptar

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.waveoffood.databinding.CartItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartAdaptar(
    private val context: Context,
    private val CartItems: MutableList<String>,
    private val CartItemsPrices: MutableList<String>,
    private var CartDescriptions: MutableList<String>,
    private var CartImages: MutableList<String>,
    private val CartQuantity: MutableList<Int>,
    private var CartIngredient: MutableList<String>

): RecyclerView.Adapter<CartAdaptar.CartViewHolder>() {

    //initialize Firebase
    private val auth = FirebaseAuth.getInstance()
    init {
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid ?:""
        val cartItemNumber = CartItems.size

        //initialize Firebase
        itemQuantities = IntArray(cartItemNumber){ 1 }//cartItems.size
        cartItemReference = database.reference.child("user").child(userId).child("CartItem")
    }
        companion object{
        private var itemQuantities:IntArray = intArrayOf()
        private lateinit var cartItemReference:DatabaseReference
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }
    override fun getItemCount(): Int = CartItems.size
    //get updated quantity
    fun getUpdatedItemsQuantities(): MutableList<Int> {
        val itemQuantity = mutableListOf<Int>()
        itemQuantity.addAll(CartQuantity)
        return itemQuantity
    }
    inner class CartViewHolder(private val binding: CartItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            binding.apply {
                val quantity = itemQuantities[position]
                CartFoodName.text = CartItems[position]
                CartItemPrice.text = CartItemsPrices[position]
                //load Image using Glide
                val uriString = CartImages[position]
                Log.d("image", "food Url: $uriString")
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).listener(object :RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d("Glide","onLoadFailed: Image loading Faild")
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d("Glide","onLoadFailed: Image loading Successful")
                        return false
                    }
                }
                ).into(CartImage)

                cartQuantityyy.text = quantity.toString()

                MinusButton.setOnClickListener {
                    deceaseQuantity(position)
                }
                PlusButton.setOnClickListener {
                    increaseQuantity(position)
                }

                DeleteButton.setOnClickListener {
                    val itemPosition = adapterPosition
                    if(itemPosition != RecyclerView.NO_POSITION){
                        deleteItem((itemPosition))
                    }
                }
            }
        }
        private fun increaseQuantity(position: Int){
            if(itemQuantities[position] <10 ){
                itemQuantities[position] ++
                CartQuantity[position] = itemQuantities[position]
                binding.cartQuantityyy.text = itemQuantities[position].toString()
            }
        }
        private fun deceaseQuantity(position: Int){
            if(itemQuantities[position] > 1){
                itemQuantities[position] --
                CartQuantity[position] = itemQuantities[position]
                binding.cartQuantityyy.text = itemQuantities[position].toString()
            }
        }
        private fun deleteItem(position: Int){
            val positionRetrieve = position
            getUniqueKeyAtPosition(positionRetrieve){uniqueKey ->
                if(uniqueKey != null){
                    removeItem(position, uniqueKey)
                }
            }
        }
        private fun removeItem(position: Int, uniqueKey: String) {
            if(uniqueKey != null){
                cartItemReference.child(uniqueKey).removeValue().addOnSuccessListener {
                    CartItems.removeAt(position)
                    CartImages.removeAt(position)
                    CartDescriptions.removeAt(position)
                    CartQuantity.removeAt(position)
                    CartItemsPrices.removeAt(position)
                    CartIngredient.removeAt(position)
                    Toast.makeText(context,"Item Delete", Toast.LENGTH_SHORT).show()
                    //update item quanitites
                    itemQuantities = itemQuantities.filterIndexed { index, i -> index != position}.toIntArray()
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, CartItems.size)
                }.addOnFailureListener{
                    Toast.makeText(context,"Faild to Delete", Toast.LENGTH_SHORT).show()
                }
            }
        }
        private fun getUniqueKeyAtPosition(positionRetrieve: Int, onComplete:(String?) ->Unit) {
            cartItemReference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var uniqueKey:String? = null
                    snapshot.children.forEachIndexed{index, dataSnapshot ->
                        if(index == positionRetrieve){
                            uniqueKey = dataSnapshot.key
                            return@forEachIndexed
                        }
                    }
                    onComplete(uniqueKey)
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
}