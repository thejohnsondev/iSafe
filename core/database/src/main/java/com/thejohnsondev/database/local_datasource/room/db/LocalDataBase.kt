package com.thejohnsondev.database.local_datasource.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.thejohnsondev.database.local_datasource.room.dao.NotesDao
import com.thejohnsondev.database.local_datasource.room.dao.PasswordAdditionalFieldDao
import com.thejohnsondev.database.local_datasource.room.dao.PasswordsDao
import com.thejohnsondev.database.local_datasource.room.entity.AdditionalFieldEntity
import com.thejohnsondev.database.local_datasource.room.entity.NoteEntity
import com.thejohnsondev.database.local_datasource.room.entity.PasswordEntity

@Database(
    entities = [PasswordEntity::class, NoteEntity::class, AdditionalFieldEntity::class],
    version = 1
)
abstract class LocalDataBase: RoomDatabase() {

    abstract fun getPasswordsDao(): PasswordsDao
    abstract fun getAdditionalFieldsDao(): PasswordAdditionalFieldDao
    abstract fun getNotesDao(): NotesDao

    companion object {
        const val NAME_BD = "i_safe.db"
    }

}