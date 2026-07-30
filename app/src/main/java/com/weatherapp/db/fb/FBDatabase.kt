package com.weatherapp.db.fb

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

class FBDatabase {

    private val auth = Firebase.auth

    private val db = Firebase.firestore

    val user: Flow<FBUser>
        get() {
            val currentUser = auth.currentUser ?: return emptyFlow()

            return db
                .collection("users")
                .document(currentUser.uid)
                .snapshots()
                .map { snapshot ->
                    snapshot.toObject(FBUser::class.java) ?: FBUser()
                }
        }

    val cities: Flow<List<FBCity>>
        get() {
            val currentUser = auth.currentUser ?: return emptyFlow()

            return db
                .collection("users")
                .document(currentUser.uid)
                .collection("cities")
                .snapshots()
                .map { snapshot ->
                    snapshot.toObjects(FBCity::class.java)
                }
        }

    fun add(city: FBCity) {
        auth.currentUser?.let { user ->
            db.collection("users")
                .document(user.uid)
                .collection("cities")
                .document(city.name!!)
                .set(city)
        }
    }

    fun update(city: FBCity) {
        auth.currentUser?.let { user ->
            db.collection("users")
                .document(user.uid)
                .collection("cities")
                .document(city.name!!)
                .set(city)
        }
    }

    fun register(user: FBUser) {
        auth.currentUser?.let { firebaseUser ->
            db.collection("users")
                .document(firebaseUser.uid)
                .set(user)
        }
    }

    fun remove(city: FBCity) {
        auth.currentUser?.let { user ->
            db.collection("users")
                .document(user.uid)
                .collection("cities")
                .document(city.name!!)
                .delete()
        }
    }
}