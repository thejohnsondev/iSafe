package com.thejohnsondev.domain.di

import com.thejohnsondev.data.AuthRepository
import com.thejohnsondev.data.GenerateKeyRepository
import com.thejohnsondev.data.UserRepository
import com.thejohnsondev.datastore.DataStore
import com.thejohnsondev.domain.CreateUserUseCase
import com.thejohnsondev.domain.CreateUserUseCaseImpl
import com.thejohnsondev.domain.EmailValidateUseCase
import com.thejohnsondev.domain.EmailValidationUseCaseImpl
import com.thejohnsondev.domain.GenerateUserKeyUseCase
import com.thejohnsondev.domain.GenerateUserKeyUseCaseImpl
import com.thejohnsondev.domain.GetFirstScreenRouteUseCase
import com.thejohnsondev.domain.GetFirstScreenRouteUseCaseImpl
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
import com.thejohnsondev.domain.SaveUserKeyUseCase
import com.thejohnsondev.domain.SaveUserKeyUseCaseImpl
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
    fun provideIsUserLoggedInUseCase(
        authRepository: AuthRepository
    ): GetFirstScreenRouteUseCase =
        GetFirstScreenRouteUseCaseImpl(authRepository)

    @Singleton
    @Provides
    fun provideSaveUserKeyUseCase(
        dataStore: DataStore
    ): SaveUserKeyUseCase = SaveUserKeyUseCaseImpl(dataStore)

    @Singleton
    @Provides
    fun provideGenerateUserKeyUseCase(
        generateKeyRepository: GenerateKeyRepository
    ): GenerateUserKeyUseCase = GenerateUserKeyUseCaseImpl(generateKeyRepository)

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
}