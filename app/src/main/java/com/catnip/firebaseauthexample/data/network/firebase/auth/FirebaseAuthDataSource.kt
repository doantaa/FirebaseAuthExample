package com.catnip.firebaseauthexample.data.network.firebase.auth

import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.tasks.await

interface FirebaseAuthDataSource {
    fun isLoggedIn() : Boolean
    fun getCurrentUser(): FirebaseUser?
    fun doLogout(): Boolean
    suspend fun doRegister(fullName: String, email: String, password: String) : Boolean
    suspend fun doLogin(email: String, password: String) : Boolean
}

class FirebaseAuthDataSourceImpl(private val firebaseAuth: FirebaseAuth): FirebaseAuthDataSource {
    override fun isLoggedIn(): Boolean {
        //currentUser -> user yang sedang login pada saat tersebut
        return firebaseAuth.currentUser != null
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override fun doLogout(): Boolean {
        firebaseAuth.signOut()
        return true
    }

    override suspend fun doRegister(fullName: String, email: String, password: String): Boolean {
        val registerResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        registerResult.user?.updateProfile(
            userProfileChangeRequest {
                displayName = fullName
                photoUri = "https://www.91-cdn.com/hub/wp-content/uploads/2023/09/New-Android-logo-2023.jpg".toUri()
            }
        )?.await()
        return registerResult.user != null
    }

    //use callback
//    override suspend fun doRegister(fullName: String, email: String, password: String): Boolean {
//        val registerResult = firebaseAuth.createUserWithEmailAndPassword(email,password)
//    }

    override suspend fun doLogin(email: String, password: String): Boolean {
        val loginResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return loginResult.user != null
    }
}