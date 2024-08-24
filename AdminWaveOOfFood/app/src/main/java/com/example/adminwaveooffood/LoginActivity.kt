package com.example.adminwaveooffood

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.ActivityChooserView
import com.example.adminwaveooffood.databinding.ActivityLoginBinding
import com.example.adminwaveooffood.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class LoginActivity : AppCompatActivity() {

    private var userName: String? = null
    private var nameOfRestaurant: String? = null
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInCilent: GoogleSignInClient

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.app_name)).requestEmail().build()
        //initalize Firebase Auth
        auth = Firebase.auth
        //initalize firebase databse
        database = Firebase.database.reference
        //Google signin
        googleSignInCilent = GoogleSignIn.getClient(this, googleSignInOption)

        binding.loginButton.setOnClickListener {
            //get text from edit text
            email = binding.Email.text.toString().trim()
            password = binding.Password.text.toString().trim()
            if(email.isBlank() || password.isBlank()){
                Toast.makeText(this, "Please fill all detail", Toast.LENGTH_SHORT).show()
            }
            else{
                createUseraccount(email, password)
            }
        }

        binding.googleButton.setOnClickListener {
            val signIntent:Intent = googleSignInCilent.signInIntent
            launcher.launch(signIntent)
        }

        binding.DontHaveAccountButton.setOnClickListener {
            val intent = Intent(this,SinghUpAvtivity::class.java)
            startActivity(intent)
        }
    }

    private fun createUseraccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                val user: FirebaseUser? = auth.currentUser
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                updateUi(user)
            }
            else{
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        val user: FirebaseUser? = auth.currentUser
                        Toast.makeText(this, "Create User & Login Successful", Toast.LENGTH_SHORT).show()
                        saveUserData()
                        updateUi(user)
                    }
                    else{
                        Toast.makeText(this , "Authentication Faild", Toast.LENGTH_SHORT).show()
                        Log.d("Account", "CreateUSerAccount: Authentication faild", task.exception)
                    }
                }
            }
        }
    }

    private fun saveUserData() {
        email = binding.Email.text.toString().trim()
        password = binding.Password.text.toString().trim()
        val user = UserModel(userName, nameOfRestaurant, email, password)
        val userID = FirebaseAuth.getInstance().currentUser?.uid
        userID.let {
            if (it != null) {
                database.child("user").child(it).setValue(user)
            }
        }
    }


    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if(result.resultCode == Activity.RESULT_OK){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if(task.isSuccessful){
                val account: GoogleSignInAccount = task.result
                val credential:AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                    if(authTask.isSuccessful){
                        //successfuly sign in with google
                        Toast.makeText(this,"Successfuly Sign-in With Google", Toast.LENGTH_SHORT).show()
                        updateUi(null)
                    }
                    else{
                        Toast.makeText(this,"Google Sign-in Faild", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                Toast.makeText(this,"Google Sign-in Faild", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //Check if user is alredy login
    override fun onStart() {
        super.onStart()
        val currentUser: FirebaseUser? = auth.currentUser
        if(currentUser!=null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}