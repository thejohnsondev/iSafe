package com.thejohnsondev.isafe.di

import android.app.Application
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.thejohnsondev.isafe.data.local_data_source.DataStore
import com.thejohnsondev.isafe.data.local_data_source.DataStoreImpl
import com.thejohnsondev.isafe.data.repositories.AuthRepositoryImpl
import com.thejohnsondev.isafe.data.repositories.GenerateKeyRepositoryImpl
import com.thejohnsondev.isafe.data.repositories.NotesRepositoryImpl
import com.thejohnsondev.isafe.data.repositories.PasswordsRepositoryImpl
import com.thejohnsondev.isafe.data.repositories.UserRepositoryImpl
import com.thejohnsondev.isafe.domain.repositories.AuthRepository
import com.thejohnsondev.isafe.domain.repositories.GenerateKeyRepository
import com.thejohnsondev.isafe.domain.repositories.NotesRepository
import com.thejohnsondev.isafe.domain.repositories.PasswordsRepository
import com.thejohnsondev.isafe.domain.repositories.UserRepository
import com.thejohnsondev.isafe.domain.use_cases.auth.CheckUserKeyCorrectUseCase
import com.thejohnsondev.isafe.domain.use_cases.auth.CheckUserKeyCorrectUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.auth.EmailValidateUseCase
import com.thejohnsondev.isafe.domain.use_cases.auth.EmailValidationUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.auth.GetFirstScreenRoute
import com.thejohnsondev.isafe.domain.use_cases.auth.GetFirstScreenRouteImpl
import com.thejohnsondev.isafe.domain.use_cases.auth.LogoutUseCase
import com.thejohnsondev.isafe.domain.use_cases.auth.LogoutUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.auth.PasswordValidationUseCase
import com.thejohnsondev.isafe.domain.use_cases.auth.PasswordValidationUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.auth.SignInUseCase
import com.thejohnsondev.isafe.domain.use_cases.auth.SignInUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.auth.SignUpUseCase
import com.thejohnsondev.isafe.domain.use_cases.auth.SignUpUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.combined.AuthUseCases
import com.thejohnsondev.isafe.domain.use_cases.key_gen.GenerateUserKeyUseCase
import com.thejohnsondev.isafe.domain.use_cases.key_gen.GenerateUserKeyUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.key_gen.SaveUserKeyUseCase
import com.thejohnsondev.isafe.domain.use_cases.key_gen.SaveUserKeyUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.notes.CreateNoteUseCase
import com.thejohnsondev.isafe.domain.use_cases.notes.CreateNoteUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.notes.DeleteNoteUseCase
import com.thejohnsondev.isafe.domain.use_cases.notes.DeleteNoteUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.notes.GetAllNotesUseCase
import com.thejohnsondev.isafe.domain.use_cases.notes.GetAllNotesUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.notes.UpdateNoteUseCase
import com.thejohnsondev.isafe.domain.use_cases.notes.UpdateNoteUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.user.CreateUserUseCase
import com.thejohnsondev.isafe.domain.use_cases.user.CreateUserUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.user.GetLocalUserDataUseCase
import com.thejohnsondev.isafe.domain.use_cases.user.GetLocalUserDataUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.user.GetRemoteUserDataUseCase
import com.thejohnsondev.isafe.domain.use_cases.user.GetRemoteUserDataUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.user.SaveUserDataUseCase
import com.thejohnsondev.isafe.domain.use_cases.user.SaveUserDataUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.user.SaveUserSecretUseCase
import com.thejohnsondev.isafe.domain.use_cases.user.SaveUserSecretUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.vault.CreatePasswordUseCase
import com.thejohnsondev.isafe.domain.use_cases.vault.CreatePasswordUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.vault.DeletePasswordUseCase
import com.thejohnsondev.isafe.domain.use_cases.vault.DeletePasswordUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.vault.GetAllPasswordsUseCase
import com.thejohnsondev.isafe.domain.use_cases.vault.GetAllPasswordsUseCaseImpl
import com.thejohnsondev.isafe.domain.use_cases.vault.UpdatePasswordUseCase
import com.thejohnsondev.isafe.domain.use_cases.vault.UpdatePasswordUseCaseImpl
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
    fun provideIsUserLoggedInUseCase(
        firebaseAuth: FirebaseAuth,
        dataStore: DataStore
    ): GetFirstScreenRoute =
        GetFirstScreenRouteImpl(firebaseAuth, dataStore)

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
        coroutineScope: CoroutineScope,
    ): UserRepository =
        UserRepositoryImpl(firebaseDatabase, coroutineScope)

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
    fun provideSaveUserKeyUseCase(
        dataStore: DataStore
    ): SaveUserKeyUseCase = SaveUserKeyUseCaseImpl(dataStore)

    @Singleton
    @Provides
    fun provideGetUserDataUseCase(
        userRepository: UserRepository
    ): GetRemoteUserDataUseCase = GetRemoteUserDataUseCaseImpl(userRepository)

    @Singleton
    @Provides
    fun provideLogoutUseCase(
        firebaseAuth: FirebaseAuth,
        coroutineScope: CoroutineScope,
        dataStore: DataStore
    ): LogoutUseCase = LogoutUseCaseImpl(
        firebaseAuth,
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

    @Singleton
    @Provides
    fun provideSaveUserSecretUseCase(
        userRepository: UserRepository
    ): SaveUserSecretUseCase = SaveUserSecretUseCaseImpl(userRepository)

    @Singleton
    @Provides
    fun provideGenerateUserKeyUseCase(
        generateKeyRepository: GenerateKeyRepository
    ): GenerateUserKeyUseCase = GenerateUserKeyUseCaseImpl(generateKeyRepository)

    @Singleton
    @Provides
    fun provideGetLocalUserDataUseCase(
        dataStore: DataStore
    ): GetLocalUserDataUseCase = GetLocalUserDataUseCaseImpl(dataStore)

    @Singleton
    @Provides
    fun provideAuthUseCases(
        signInUseCase: SignInUseCase,
        signUseCases: SignUpUseCase,
        emailValidateUseCase: EmailValidateUseCase,
        passwordValidationUseCase: PasswordValidationUseCase,
        createUserUseCase: CreateUserUseCase,
        saveUserDataUseCase: SaveUserDataUseCase,
        getRemoteUserDataUseCase: GetRemoteUserDataUseCase
    ): AuthUseCases =
        AuthUseCases(
            signUseCases,
            signInUseCase,
            emailValidateUseCase,
            passwordValidationUseCase,
            createUserUseCase,
            saveUserDataUseCase,
            getRemoteUserDataUseCase
        )


    @Singleton
    @Provides
    fun provideNotesRepository(
        firebaseDatabase: FirebaseDatabase,
        coroutineScope: CoroutineScope,
        dataStore: DataStore
    ): NotesRepository = NotesRepositoryImpl(
        firebaseDatabase,
        coroutineScope,
        dataStore
    )

    @Singleton
    @Provides
    fun provideCreateNoteUseCase(notesRepository: NotesRepository): CreateNoteUseCase =
        CreateNoteUseCaseImpl(notesRepository)

    @Singleton
    @Provides
    fun provideDeleteNoteUseCase(notesRepository: NotesRepository): DeleteNoteUseCase =
        DeleteNoteUseCaseImpl(notesRepository)

    @Singleton
    @Provides
    fun provideGetAllNotesUseCase(notesRepository: NotesRepository): GetAllNotesUseCase =
        GetAllNotesUseCaseImpl(notesRepository)

    @Singleton
    @Provides
    fun provideUpdateNoteUseCase(notesRepository: NotesRepository): UpdateNoteUseCase =
        UpdateNoteUseCaseImpl(notesRepository)


    @Singleton
    @Provides
    fun providePasswordsRepository(
        firebaseDatabase: FirebaseDatabase,
        coroutineScope: CoroutineScope,
        dataStore: DataStore
    ): PasswordsRepository = PasswordsRepositoryImpl(firebaseDatabase, coroutineScope, dataStore)

    @Singleton
    @Provides
    fun provideCreatePasswordUseCase(
        passwordsRepository: PasswordsRepository
    ): CreatePasswordUseCase = CreatePasswordUseCaseImpl(passwordsRepository)

    @Singleton
    @Provides
    fun provideDeletePasswordUseCase(
        passwordsRepository: PasswordsRepository
    ): DeletePasswordUseCase = DeletePasswordUseCaseImpl(passwordsRepository)


    @Singleton
    @Provides
    fun provideGetAllPasswordsUseCase(
        passwordsRepository: PasswordsRepository
    ): GetAllPasswordsUseCase = GetAllPasswordsUseCaseImpl(passwordsRepository)


    @Singleton
    @Provides
    fun provideUpdatePasswordUseCase(
        passwordsRepository: PasswordsRepository
    ): UpdatePasswordUseCase = UpdatePasswordUseCaseImpl(passwordsRepository)


}