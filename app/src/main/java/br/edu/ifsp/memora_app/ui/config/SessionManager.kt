package br.edu.ifsp.memora_app.ui.config

import android.content.Context
import android.content.SharedPreferences
import br.edu.ifsp.memora_app.data.remote.api.ApiClient
import br.edu.ifsp.memora_app.domain.user.User

object SessionManager {
    private const val PREFS_NAME = "memora_session"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_PASSWORD = "password"

    private var currentUser: User? = null
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val userId = prefs.getString(KEY_USER_ID, null)
        val userName = prefs.getString(KEY_USER_NAME, null)
        val userEmail = prefs.getString(KEY_USER_EMAIL, null)
        val password = prefs.getString(KEY_PASSWORD, null)

        if (userId != null && userName != null && userEmail != null && password != null) {
            currentUser = User(id = userId, name = userName, email = userEmail)
            ApiClient.setBasicAuth(userEmail, password)
        }
    }

    fun login(user: User, email: String, password: String) {
        currentUser = user
        ApiClient.setBasicAuth(email, password)

        // Persist to SharedPreferences
        prefs.edit().apply {
            putString(KEY_USER_ID, user.id)
            putString(KEY_USER_NAME, user.name)
            putString(KEY_USER_EMAIL, user.email)
            putString(KEY_PASSWORD, password)
            apply()
        }
    }

    fun logout() {
        currentUser = null
        ApiClient.clearAuth()

        prefs.edit().clear().apply()
    }

    fun getCurrentUser(): User? = currentUser

    fun requireUser(): User = currentUser
        ?: throw IllegalStateException("User not logged in")

    fun requireUserId(): String = currentUser?.id
        ?: throw IllegalStateException("User not logged in")
}
