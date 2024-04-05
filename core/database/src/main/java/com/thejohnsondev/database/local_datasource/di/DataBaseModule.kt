package com.thejohnsondev.database.local_datasource.di

import android.content.Context
import androidx.room.Room
import com.thejohnsondev.database.local_datasource.LocalDataSource
import com.thejohnsondev.database.local_datasource.room.RoomLocalDataSourceImpl
import com.thejohnsondev.database.local_datasource.room.dao.NotesDao
import com.thejohnsondev.database.local_datasource.room.dao.PasswordAdditionalFieldDao
import com.thejohnsondev.database.local_datasource.room.dao.PasswordsDao
import com.thejohnsondev.database.local_datasource.room.db.ISafeDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Singleton
    @Provides
    fun provideDataBase(applicationContext: Context): ISafeDataBase {
        return Room.databaseBuilder(
            applicationContext,
            ISafeDataBase::class.java,
            ISafeDataBase.NAME_BD
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideLocalDataSource(
        passwordsDao: PasswordsDao,
        additionalFieldDao: PasswordAdditionalFieldDao,
        notesDao: NotesDao
    ): LocalDataSource =
        RoomLocalDataSourceImpl(
            passwordsDao,
            additionalFieldDao,
            notesDao
        )

    @Singleton
    @Provides
    fun providePasswordsDao(dataBase: ISafeDataBase) = dataBase.getPasswordsDao()

    @Singleton
    @Provides
    fun provideAdditionalFieldsDao(dataBase: ISafeDataBase) = dataBase.getAdditionalFieldsDao()

    @Singleton
    @Provides
    fun provideNotesDao(dataBase: ISafeDataBase) = dataBase.getNotesDao()

}