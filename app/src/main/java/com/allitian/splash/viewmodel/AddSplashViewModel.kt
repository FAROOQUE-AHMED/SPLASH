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

class AddSplashViewModel: ViewModel() {

    private val db=FirebaseDatabase.getInstance()
    val userRef=db.getReference("splashs")


    private val storageRef= Firebase.storage.reference
    private val imageRef=storageRef.child("splashs/${UUID.randomUUID()}.jpg")

    private val _isPosted=MutableLiveData<Boolean>()
    val isPosted:LiveData<Boolean> = _isPosted



    fun saveImage(splash:String,userId:String,imageUri:Uri){
        val uploadTask =imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(splash,userId,it.toString() )
            }
        }

    }
    fun saveData(splash:String,userId:String,image:String) {
        val splashData = SplashModel(splash,image,userId,System.currentTimeMillis().toString())

        userRef.child(userRef.push().key!!).setValue(splashData)
            .addOnSuccessListener {
                _isPosted.postValue(true)
            }
            .addOnFailureListener {
                _isPosted.postValue(false)
            }
    }


}



