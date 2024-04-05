package com.thejohnsondev.database.local_datasource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.thejohnsondev.database.local_datasource.room.entity.AdditionalFieldEntity

@Dao
interface PasswordAdditionalFieldDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdditionalField(additionalField: AdditionalFieldEntity)

    @Update
    suspend fun updateAdditionalField(additionalField: AdditionalFieldEntity)

    @Query("DELETE FROM additionalFields WHERE id = :additionalFieldId")
    suspend fun deletePasswordAdditionalFieldById(additionalFieldId: String?)

    @Query("SELECT * FROM additionalFields WHERE passwordId = :passwordId")
    suspend fun getAdditionalFieldsByPasswordId(passwordId: String?): List<AdditionalFieldEntity>

    @Query("DELETE FROM additionalFields")
    suspend fun deleteAllAdditionalFields()

}