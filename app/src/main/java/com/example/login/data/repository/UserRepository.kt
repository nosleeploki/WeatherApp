package com.example.login.data.repository

import com.example.login.data.local.DatabaseHelper
import com.example.login.data.model.User

class UserRepository(private val dbHelper: DatabaseHelper) {
    //Add
    fun registerUser(name: String, username: String, phone: String, password: String) : Boolean{
        if (dbHelper.isValid(username, phone)){
            return false
        }
        return dbHelper.insertUser(name, username, phone, password)
    }

    //Check isValid
    fun loginUser(username: String, password: String): User? {
        return dbHelper.getUser(username, password)
    }

    //Kiểm tra tính hợp lệ của username và số điện thoại
    fun isValid(username: String, phone: String): Boolean{
        return dbHelper.isValid(username, phone)
    }
}