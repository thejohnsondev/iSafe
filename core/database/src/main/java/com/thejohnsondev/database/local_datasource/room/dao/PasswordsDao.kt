package com.thejohnsondev.database.local_datasource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.thejohnsondev.database.local_datasource.room.entity.PasswordEntity

@Dao
interface PasswordsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPassword(password: PasswordEntity)

    @Update
    suspend fun updatePassword(password: PasswordEntity)

    @Query("DELETE FROM passwords WHERE id = :passwordId")
    suspend fun deletePasswordById(passwordId: String)

    @Query("SELECT * FROM passwords")
    suspend fun getAllPasswords(): List<PasswordEntity>

    @Query("DELETE FROM passwords")
    suspend fun deleteAllPasswords()

}