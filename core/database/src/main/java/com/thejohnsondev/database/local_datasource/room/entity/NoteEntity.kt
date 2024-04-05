package com.thejohnsondev.database.local_datasource.room.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.thejohnsondev.model.NoteModel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: String,
    @ColumnInfo("title")
    val title: String,
    @ColumnInfo("description")
    val description: String,
    @ColumnInfo("lastEdit")
    val lastEdit: String
): Parcelable {

    companion object {

        fun NoteEntity.toModel(): NoteModel =
            NoteModel(
                id = id,
                title = title,
                description = description,
                lastEdit = lastEdit
            )

        fun NoteModel.toEntity(): NoteEntity =
            NoteEntity(
                id = id ?: throw IllegalArgumentException("Note id cannot be null"),
                title = title,
                description = description,
                lastEdit = lastEdit
            )

    }

}
