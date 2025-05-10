package com.allitian.splash.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.allitian.splash.model.UserModel
import com.allitian.splash.utils.SharedPref
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.util.UUID

class AuthViewModel: ViewModel() {
    val auth=FirebaseAuth.getInstance()
    private val db=FirebaseDatabase.getInstance()
    val userRef=db.getReference("users")
    private val storageRef= Firebase.storage.reference
    private val imageRef=storageRef.child("user/${UUID.randomUUID()}.jpg")

    private val _firebaseUser=MutableLiveData<FirebaseUser?>()
    val firebaseUser:LiveData<FirebaseUser> = _firebaseUser as LiveData<FirebaseUser>

    private val _error=MutableLiveData<String>()
    val error:LiveData<String> = _error

    init {

        _firebaseUser.value=auth.currentUser

    }
    fun login(email: String, password: String, context: Context) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _firebaseUser.postValue(auth.currentUser)
                    getData(auth.currentUser!!.uid, context)
                } else {
                    _error.postValue("something went wrong!")
                }
            }
    }

    private fun getData(uid: String?, context: Context){



        userRef.child(uid!!).addListenerForSingleValueEvent(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData=snapshot.getValue(UserModel::class.java)
                SharedPref.storeData(userData!!.name, userData.email, userData.bio, userData.username, "", context)

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    fun signup(email: String, password: String, name: String, bio: String, username: String, imageUri: Uri, context: Context) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _firebaseUser.postValue(auth.currentUser)

                    // Skip image upload
                    val uid = auth.currentUser?.uid
                    saveData(email, password, name, bio, username, "", uid, context)

                } else {
                    _error.postValue(it.exception!!.message)
                }
            }
    }

    private fun saveImage(email:String,password:String,name:String,bio:String,username:String,imageUri:Uri,uid:String?,context: Context){
        val uploadTask =imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(email,password,name,bio,username,it.toString(),uid, context )
            }
        }

    }
    private fun saveData(email: String, password: String, name: String, bio: String, username: String, imageUrl: String, uid: String?, context: Context) {

        val firestoreDb= Firebase.firestore
        val followersRef= firestoreDb.collection("followers").document(uid!!)
        val followingRef= firestoreDb.collection("following").document(uid!!)

        followingRef.set(mapOf("followingIds" to listOf<String>())) // empty list of Strings
        followersRef.set(mapOf("followerIds" to listOf<String>()))


        val userData = UserModel(email, password, name, bio, username, imageUrl, uid ?: "")
        userRef.child(uid!!).setValue(userData)
            .addOnSuccessListener {
                SharedPref.storeData(name, email, bio, username, imageUrl, context)
            }
            .addOnFailureListener {
                _error.postValue("Data save failed: ${it.message}")
            }
    }

    fun logout(){
        auth.signOut()
        _firebaseUser.postValue(null)

    }




}



