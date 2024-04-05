package com.thejohnsondev.database.local_datasource.room.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.thejohnsondev.model.AdditionalField
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "additionalFields")
data class AdditionalFieldEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: String,
    @ColumnInfo("passwordId")
    val passwordId: String?,
    @ColumnInfo("title")
    val title: String,
    @ColumnInfo("value")
    val value: String,
): Parcelable {

    companion object {

        fun AdditionalFieldEntity.toModel(): AdditionalField =
            AdditionalField(
                id = id,
                title = title,
                value = value,
            )

        fun AdditionalField.toEntity(passwordId: String?): AdditionalFieldEntity =
            AdditionalFieldEntity(
                id = id.orEmpty(),
                passwordId = passwordId,
                title = title,
                value = value,
            )

    }

}