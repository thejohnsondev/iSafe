package com.thejohnsondev.database.local_datasource.room.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.thejohnsondev.model.PasswordModel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "passwords")
data class PasswordEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: String,
    @ColumnInfo("organization")
    val organization: String,
    @ColumnInfo("organizationLogo")
    val organizationLogo: String? = null,
    @ColumnInfo("title")
    val title: String,
    @ColumnInfo("password")
    val password: String,
    @ColumnInfo("lastEdit")
    val lastEdit: String,
) : Parcelable {

    companion object {

        fun PasswordEntity.toModel(): PasswordModel =
            PasswordModel(
                id = id,
                organization = organization,
                organizationLogo = organizationLogo,
                title = title,
                password = password,
                lastEdit = lastEdit
            )

        fun PasswordModel.toEntity(): PasswordEntity =
            PasswordEntity(
                id = id ?: throw IllegalArgumentException("Password id cannot be null"),
                organization = organization,
                organizationLogo = organizationLogo,
                title = title,
                password = password,
                lastEdit = lastEdit
            )

    }

}