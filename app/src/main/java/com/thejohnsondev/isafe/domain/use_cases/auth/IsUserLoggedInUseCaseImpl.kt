package com.thejohnsondev.isafe.domain.use_cases.auth

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class IsUserLoggedInUseCaseImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): IsUserLoggedInUseCase {
    override operator fun invoke(): Boolean {
        return !firebaseAuth.currentUser?.email.isNullOrBlank()
    }
}