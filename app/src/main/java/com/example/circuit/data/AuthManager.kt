package com.example.circuit.data

import com.example.circuit.CircuitApp
import com.example.circuit.data.models.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

val gson = Gson()

object AuthManager {
    private val preferenceHandler = CircuitApp.instance.getSharedPreferences("auth_details", 0)

    private var registeredUsers: List<User>
        get() {
            val usersString = preferenceHandler.getString(USERS, "[]")
            val listType = object : TypeToken<List<User>>() {}.type
            return gson.fromJson(usersString, listType)
        }
        set(value) {
            preferenceHandler.edit().putString(USERS, gson.toJson(value)).apply()
        }

    private var loggedInUser: User?
        get() {
            val userString = preferenceHandler.getString(LOGGED_IN_USER, null)
            if (userString != null) {
                return gson.fromJson(userString, User::class.java)
            }
            return null
        }
        set(value) = preferenceHandler.edit().putString(LOGGED_IN_USER, gson.toJson(value)).apply()

    val isLoggedIn: Boolean
        get() = loggedInUser != null

    fun login(email: String, password: String): Result<Unit> {
        val users = registeredUsers
        val foundUser = users.find { it.email == email }

        if (foundUser == null || foundUser.password != password) {
            return Result.failure(InvalidCredentialsException())
        }

        registeredUsers = users + User(email, password)
        loggedInUser = User(email, password)

        return Result.success(Unit)
    }

    fun register(email: String, password: String): Result<Unit> {
        val users = registeredUsers
        val foundUser = users.find { it.email == email }

        if (foundUser != null) {
            return Result.failure(AccountExistsException())
        }

        registeredUsers = users + User(email, password)
        loggedInUser = User(email, password)

        return Result.success(Unit)
    }

    fun logout() {
        loggedInUser = null
    }

    private const val LOGGED_IN_USER = "logged_in_user"
    private const val USERS = "registered_users"
}

