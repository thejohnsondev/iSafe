package com.thejohnsondev.domain.di

import com.thejohnsondev.data.AuthRepository
import com.thejohnsondev.data.UserRepository
import com.thejohnsondev.datastore.DataStore
import com.thejohnsondev.domain.CheckUserKeyCorrectUseCase
import com.thejohnsondev.domain.CheckUserKeyCorrectUseCaseImpl
import com.thejohnsondev.domain.CreateUserUseCase
import com.thejohnsondev.domain.CreateUserUseCaseImpl
import com.thejohnsondev.domain.EmailValidateUseCase
import com.thejohnsondev.domain.EmailValidationUseCaseImpl
import com.thejohnsondev.domain.GetLocalUserDataUseCase
import com.thejohnsondev.domain.GetLocalUserDataUseCaseImpl
import com.thejohnsondev.domain.GetRemoteUserDataUseCase
import com.thejohnsondev.domain.GetRemoteUserDataUseCaseImpl
import com.thejohnsondev.domain.LogoutUseCase
import com.thejohnsondev.domain.LogoutUseCaseImpl
import com.thejohnsondev.domain.PasswordValidationUseCase
import com.thejohnsondev.domain.PasswordValidationUseCaseImpl
import com.thejohnsondev.domain.SaveUserDataUseCase
import com.thejohnsondev.domain.SaveUserDataUseCaseImpl
import com.thejohnsondev.domain.SaveUserSecretUseCase
import com.thejohnsondev.domain.SaveUserSecretUseCaseImpl
import com.thejohnsondev.domain.SignInUseCase
import com.thejohnsondev.domain.SignInUseCaseImpl
import com.thejohnsondev.domain.SignUpUseCase
import com.thejohnsondev.domain.SignUpUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthDomainModule {

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
    fun provideCreateUserUseCase(
        userRepository: UserRepository
    ): CreateUserUseCase = CreateUserUseCaseImpl(userRepository)

    @Singleton
    @Provides
    fun provideSaveUserDataUseCase(
        dataStore: DataStore
    ): SaveUserDataUseCase = SaveUserDataUseCaseImpl(dataStore)

    @Singleton
    @Provides
    fun provideGetUserDataUseCase(
        userRepository: UserRepository
    ): GetRemoteUserDataUseCase = GetRemoteUserDataUseCaseImpl(userRepository)

    @Singleton
    @Provides
    fun provideGetLocalUserDataUseCase(
        dataStore: DataStore
    ): GetLocalUserDataUseCase = GetLocalUserDataUseCaseImpl(dataStore)

    @Singleton
    @Provides
    fun provideSaveUserSecretUseCase(
        userRepository: UserRepository
    ): SaveUserSecretUseCase = SaveUserSecretUseCaseImpl(userRepository)

    @Singleton
    @Provides
    fun provideLogoutUseCase(
        authRepository: AuthRepository,
        coroutineScope: CoroutineScope,
        dataStore: DataStore
    ): LogoutUseCase = LogoutUseCaseImpl(
        authRepository,
        coroutineScope,
        dataStore
    )

    @Singleton
    @Provides
    fun provideCheckKeyCorrectUseCase(
        localUserDataUseCase: GetLocalUserDataUseCase,
        remoteUserDataUseCase: GetRemoteUserDataUseCase,
        coroutineScope: CoroutineScope
    ): CheckUserKeyCorrectUseCase = CheckUserKeyCorrectUseCaseImpl(
        localUserDataUseCase,
        remoteUserDataUseCase,
        coroutineScope
    )
}