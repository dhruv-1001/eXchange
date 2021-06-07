package com.exchange.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.exchange.R
import com.google.firebase.auth.FirebaseAuth

class AccountFragment : Fragment() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        auth.signOut()

        return inflater.inflate(R.layout.fragment_account, container, false)
    }


}