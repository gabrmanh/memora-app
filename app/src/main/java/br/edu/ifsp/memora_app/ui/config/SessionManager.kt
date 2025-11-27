package br.edu.ifsp.memora_app.ui.config

import br.edu.ifsp.memora_app.data.remote.api.ApiClient
import br.edu.ifsp.memora_app.domain.user.User

object SessionManager {
    private var currentUser: User? = null

    fun login(user: User, email: String, password: String) {
        currentUser = user
        ApiClient.setBasicAuth(email, password)
    }

    fun logout() {
        currentUser = null
        ApiClient.clearAuth()
    }

    fun getCurrentUser(): User? = currentUser

    fun requireUser(): User = currentUser
        ?: throw IllegalStateException("User not logged in")

    fun requireUserId(): String = currentUser?.id
        ?: throw IllegalStateException("User not logged in")
}
