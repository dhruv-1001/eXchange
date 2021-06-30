package com.exchange.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.exchange.R
import com.exchange.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class UserLoginActivity : AppCompatActivity() {

    private lateinit var googleSignInClient:GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private val userReference = FirebaseDatabase.getInstance().reference.child("users")
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        val googleLogin = findViewById<LinearLayout>(R.id.googleLogin)
        val facebookLogin = findViewById<LinearLayout>(R.id.facebookLogin)
        val mobileLogin = findViewById<LinearLayout>(R.id.mobileLogin)
        auth = Firebase.auth

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("270051838599-lejnid7fk470mav1cknadib57h5ph8at.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        progressBar = findViewById(R.id.progressBar)

        googleLogin.setOnClickListener { signInWithGoogle() }
        facebookLogin.setOnClickListener { signInWithFacebook() }
        mobileLogin.setOnClickListener { signInWithMobile() }


        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    private fun signInWithMobile(){
        TODO()
    }

    private fun signInWithFacebook(){
        TODO()
    }

    private fun signInWithGoogle() {
        progressBar.visibility = View.VISIBLE
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 10)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 10) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.i("error found",e.toString())

            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                progressBar.visibility = View.INVISIBLE
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    addUser()
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Login Failed",Toast.LENGTH_SHORT).show()
                }
            }
    }


    // adds new user if not already present in the database
    private fun addUser(){

        if (userPresent()) return

        Log.d("Updating database", "New Firebase user")

        val user = User()
        val currentUser = auth.currentUser
        user.userName = currentUser?.displayName.toString()
        user.userEmail = currentUser?.email.toString()
        user.userUid = currentUser?.uid.toString()

        userReference.child(user.userUid).setValue(user)

    }


    // function returns true if user is present in the database
    private fun userPresent(): Boolean{

        var present = false

        userReference
            .child(auth.currentUser?.uid.toString())
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) present = true
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        return present
    }

}