package com.example.login.data.cache

import android.content.Context
import com.example.login.data.local.DatabaseHelper
import com.example.login.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow

class AuthCache(
    private val context: Context,
    private val databaseHelper: DatabaseHelper
    ) {

    private val _userProfileFlow = MutableStateFlow<User?>(null)

    private fun User?.tryEmit(){
        this?.let {
            _userProfileFlow.tryEmit(this)
        }
    }

    fun getUserProfile(): User? {
        return _userProfileFlow.value
    }

    fun saveUserProfile(user: User){
        _userProfileFlow.tryEmit(user)
    }

    //Nhan thong tin tu dtb
    private fun getUserProfileFromDb(): User? {
        return databaseHelper.getUser(username = "someUsername", password = "somePassword")
    }

    fun clear(){
        _userProfileFlow.tryEmit(null)
    }

    fun isLoggedIn() = _userProfileFlow.value != null

    fun isValidUser(username: String, phone: String): Boolean{
        return databaseHelper.isValid(username, phone)
    }

    fun addFavoriteLocation(userId: Int, locationName: String, latitude: Double, longitude: Double): Boolean{
        return databaseHelper.addFavoriteLocation(userId, locationName, latitude, longitude)
    }

    fun getFavoriteLocation(userId: Int): List<DatabaseHelper.FavoriteLocation>{
        return databaseHelper.getFavoriteLocations(userId)
    }

    fun removeFavoriteLocation(userId: Int, locationName: String): Boolean{
        return databaseHelper.removeFavoriteLocation(userId, locationName)
    }
}