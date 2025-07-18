package com.allitian.splash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.allitian.splash.model.SplashModel
import com.allitian.splash.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

class UserViewModel: ViewModel() {

    private val db=FirebaseDatabase.getInstance()
    val splashRef=db.getReference("splashs")
    val userRef=db.getReference("users")

    private val _splash = MutableLiveData(listOf<SplashModel>())
    val splash:LiveData<List<SplashModel>> get()= _splash

    private val _followerList = MutableLiveData(listOf<String>())
    val followerList:LiveData<List<String>> get()= _followerList

    private val _followingList = MutableLiveData(listOf<String>())
    val followingList:LiveData<List<String>> get()= _followingList

    private val _user = MutableLiveData<UserModel>()
    val user: LiveData<UserModel> get() = _user




    fun fetchUser(uid: String){
        userRef.child(uid).addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user=snapshot.getValue(UserModel::class.java)

                if (user != null) {
                    _user.postValue(user)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun fetchSplash(uid: String){
        splashRef.orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val splashList=snapshot.children.mapNotNull {
                    it.getValue(SplashModel::class.java)

                }

                _splash.postValue(splashList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


    val firestoreDb= Firebase.firestore
    fun followUsers(userId: String, currentUserId: String){

        val ref = firestoreDb.collection("following").document(currentUserId)
        val followersRef = firestoreDb.collection("followers").document(userId)

        ref.update("followingIds", FieldValue.arrayUnion(userId))
        followersRef.update("followerIds", FieldValue.arrayUnion(currentUserId))

    }

    fun getFollowers(userId: String){

        firestoreDb.collection("followers").document(userId)
            .addSnapshotListener { value,error ->

                val followerIds =value?.get("followerIds") as? List<String>?:listOf()
                _followerList.postValue(followerIds)
            }

    }

    fun getFollowing(userId: String){
        firestoreDb.collection("following").document(userId)
            .addSnapshotListener { value,error ->

                val followerIds =value?.get("followingIds") as? List<String>?:listOf()
                _followingList.postValue(followerIds)
            }

    }





}



