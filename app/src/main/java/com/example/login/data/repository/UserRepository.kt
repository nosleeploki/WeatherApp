package com.example.login.data.repository

import com.example.login.data.cache.AuthCache
import com.example.login.data.local.DatabaseHelper
import com.example.login.data.model.User

class UserRepository(
    private val dbHelper: DatabaseHelper,
    private val authCache: AuthCache) {
    //Add
    fun registerUser(name: String, username: String, phone: String, password: String) : Boolean{
        if (dbHelper.isValid(username, phone)){
            return false
        }
        return dbHelper.insertUser(name, username, phone, password)
    }

    //dang nhap va luu vao cache
    fun loginUser(username: String, password: String): User? {
        val user = dbHelper.getUser(username, password)
        user?.let {
            authCache.saveUserProfile(it)
        }
        return user
    }

    //Kiểm tra tính hợp lệ của username và số điện thoại
    fun isValid(username: String, phone: String): Boolean{
        return dbHelper.isValid(username, phone)
    }
}