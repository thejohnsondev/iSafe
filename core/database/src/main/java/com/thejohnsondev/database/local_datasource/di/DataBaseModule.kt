package com.thejohnsondev.database.local_datasource.di

import android.content.Context
import androidx.room.Room
import com.thejohnsondev.database.local_datasource.LocalDataSource
import com.thejohnsondev.database.local_datasource.room.RoomLocalDataSourceImpl
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
    fun provideLocalDataSource(dataBase: ISafeDataBase): LocalDataSource =
        RoomLocalDataSourceImpl(dataBase)

}