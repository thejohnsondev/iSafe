package com.thejohnsondev.isafe.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.thejohnsondev.isafe.data.repositories.AuthRepositoryImpl
import com.thejohnsondev.isafe.domain.repositories.AuthRepository
import com.thejohnsondev.isafe.domain.use_cases.auth.EmailValidateUseCase
import com.thejohnsondev.isafe.domain.use_cases.auth.EmailValidationUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.auth.IsUserLoggedInUseCase
import com.thejohnsondev.isafe.domain.use_cases.auth.IsUserLoggedInUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.auth.PasswordValidationUseCase
import com.thejohnsondev.isafe.domain.use_cases.auth.PasswordValidationUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.auth.SignInUseCase
import com.thejohnsondev.isafe.domain.use_cases.auth.SignInUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.auth.SignUpUseCase
import com.thejohnsondev.isafe.domain.use_cases.auth.SignUpUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.combined.AuthUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideIOCoroutineScope(): CoroutineScope = CoroutineScope(Dispatchers.IO)

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

    @Singleton
    @Provides
    fun provideIsUserLoggedInUseCase(firebaseAuth: FirebaseAuth): IsUserLoggedInUseCase =
        IsUserLoggedInUseCaseImpl(firebaseAuth)

    @Singleton
    @Provides
    fun provideAuthRepository(coroutineScope: CoroutineScope): AuthRepository =
        AuthRepositoryImpl(coroutineScope)

    @Singleton
    @Provides
    fun provideSignInUseCase(authRepository: AuthRepository): SignInUseCase =
        SignInUseCaseImpl(authRepository)

    @Singleton
    @Provides
    fun provideSignUpUseCase(authRepository: AuthRepository): SignUpUseCase =
        SignUpUseCaseImpl(authRepository)

    @Singleton
    @Provides
    fun provideEmailValidateUseCase(): EmailValidateUseCase = EmailValidationUseCaseImpl()

    @Singleton
    @Provides
    fun providePasswordValidateUseCase(): PasswordValidationUseCase =
        PasswordValidationUseCaseImpl()

    @Singleton
    @Provides
    fun provideAuthUseCases(
        signInUseCase: SignInUseCase,
        signUseCases: SignUpUseCase,
        emailValidateUseCase: EmailValidateUseCase,
        passwordValidationUseCase: PasswordValidationUseCase
    ): AuthUseCases =
        AuthUseCases(
            signUseCases,
            signInUseCase,
            emailValidateUseCase,
            passwordValidationUseCase
        )

}