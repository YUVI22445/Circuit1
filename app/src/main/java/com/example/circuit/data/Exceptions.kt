package com.example.circuit.data

class InvalidCredentialsException : Exception("Incorrect Email/Password, please try again or register an account")
class AccountExistsException: Exception("An Account Already exists with this Email, try logging in instead!")