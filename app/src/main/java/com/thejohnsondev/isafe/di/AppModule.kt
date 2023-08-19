package com.thejohnsondev.isafe.di

import android.app.Application
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.thejohnsondev.isafe.data.local_data_source.DataStore
import com.thejohnsondev.isafe.data.local_data_source.DataStoreImpl
import com.thejohnsondev.isafe.data.repositories.AuthRepositoryImpl
import com.thejohnsondev.isafe.data.repositories.GenerateKeyRepositoryImpl
import com.thejohnsondev.isafe.data.repositories.RemoteDbRepositoryImpl
import com.thejohnsondev.isafe.domain.repositories.AuthRepository
import com.thejohnsondev.isafe.domain.repositories.GenerateKeyRepository
import com.thejohnsondev.isafe.domain.repositories.RemoteDbRepository
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
import com.thejohnsondev.isafe.domain.use_cases.key_gen.GenerateUserKeyUseCase
import com.thejohnsondev.isafe.domain.use_cases.key_gen.GenerateUserKeyUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.user.CreateUserUseCase
import com.thejohnsondev.isafe.domain.use_cases.user.CreateUserUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.user.GetUserDataUseCase
import com.thejohnsondev.isafe.domain.use_cases.user.GetUserDataUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.user.GetUserSecretUseCase
import com.thejohnsondev.isafe.domain.use_cases.user.GetUserSecretUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.user.SaveUserDataUseCase
import com.thejohnsondev.isafe.domain.use_cases.user.SaveUserDataUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.user.SaveUserKeyUseCase
import com.thejohnsondev.isafe.domain.use_cases.user.SaveUserKeyUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.user.SaveUserSecretUseCase
import com.thejohnsondev.isafe.domain.use_cases.user.SaveUserSecretUseCaseImpl
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
    fun provideApplicationContext(application: Application): Context =
        application.applicationContext

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
    fun provideDataStore(context: Context): DataStore = DataStoreImpl(context)

    @Singleton
    @Provides
    fun provideIsUserLoggedInUseCase(firebaseAuth: FirebaseAuth): IsUserLoggedInUseCase =
        IsUserLoggedInUseCaseImpl(firebaseAuth)

    @Singleton
    @Provides
    fun provideGenerateKeyRepository(
        coroutineScope: CoroutineScope,
        applicationContext: Context
    ): GenerateKeyRepository = GenerateKeyRepositoryImpl(coroutineScope, applicationContext)

    @Singleton
    @Provides
    fun provideAuthRepository(coroutineScope: CoroutineScope): AuthRepository =
        AuthRepositoryImpl(coroutineScope)

    @Singleton
    @Provides
    fun provideRemoteDbRepository(
        firebaseDatabase: FirebaseDatabase,
        coroutineScope: CoroutineScope
    ): RemoteDbRepository =
        RemoteDbRepositoryImpl(firebaseDatabase, coroutineScope)

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
        remoteDbRepository: RemoteDbRepository
    ): CreateUserUseCase = CreateUserUseCaseImpl(remoteDbRepository)

    @Singleton
    @Provides
    fun provideSaveUserDataUseCase(
        dataStore: DataStore
    ): SaveUserDataUseCase = SaveUserDataUseCaseImpl(dataStore)

    @Singleton
    @Provides
    fun provideSaveUserKeyUseCase(
        dataStore: DataStore
    ): SaveUserKeyUseCase = SaveUserKeyUseCaseImpl(dataStore)

    @Singleton
    @Provides
    fun provideGetUserDataUseCase(
        remoteDbRepository: RemoteDbRepository
    ): GetUserDataUseCase = GetUserDataUseCaseImpl(remoteDbRepository)

    @Singleton
    @Provides
    fun provideGetUserSecretUseCase(
        dataStore: DataStore
    ): GetUserSecretUseCase = GetUserSecretUseCaseImpl(dataStore)

    @Singleton
    @Provides
    fun provideSaveUserSecretUseCase(
        dataStore: DataStore,
        remoteDbRepository: RemoteDbRepository
    ): SaveUserSecretUseCase = SaveUserSecretUseCaseImpl(dataStore, remoteDbRepository)

    @Singleton
    @Provides
    fun provideGenerateUserKeyUseCase(
        generateKeyRepository: GenerateKeyRepository
    ): GenerateUserKeyUseCase = GenerateUserKeyUseCaseImpl(generateKeyRepository)

    @Singleton
    @Provides
    fun provideAuthUseCases(
        signInUseCase: SignInUseCase,
        signUseCases: SignUpUseCase,
        emailValidateUseCase: EmailValidateUseCase,
        passwordValidationUseCase: PasswordValidationUseCase,
        createUserUseCase: CreateUserUseCase,
        saveUserDataUseCase: SaveUserDataUseCase,
        getUserDataUseCase: GetUserDataUseCase
    ): AuthUseCases =
        AuthUseCases(
            signUseCases,
            signInUseCase,
            emailValidateUseCase,
            passwordValidationUseCase,
            createUserUseCase,
            saveUserDataUseCase,
            getUserDataUseCase
        )

}