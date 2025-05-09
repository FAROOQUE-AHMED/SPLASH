package com.example.splash.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.splash.model.SplashModel
import com.example.splash.model.UserModel
import com.example.splash.utils.SharedPref
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.storage
import java.util.UUID

class SearchViewModel: ViewModel() {

    private val db=FirebaseDatabase.getInstance()
    val users =db.getReference("users")


    private var _users =MutableLiveData<List< UserModel>>()
    val usersList:LiveData<List< UserModel>> = _users

    init {
        fetchUsers {
            _users.value=it
        }
    }


    private fun fetchUsers(onResult:(List<UserModel>)-> Unit){
        users.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<UserModel>()
                for (splashSnapsort in snapshot.children){
                    val splash=splashSnapsort.getValue(UserModel::class.java)
                    result.add(splash!!)

                }

                onResult(result)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun fetchUserFromSplash(splash: SplashModel,onResult: (UserModel)-> Unit){
        db.getReference("users").child(splash.userId)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user=snapshot.getValue(UserModel::class.java)
                    user?.let(onResult)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }


}



