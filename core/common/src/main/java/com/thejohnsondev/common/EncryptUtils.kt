package com.thejohnsondev.common

import com.thejohnsondev.model.AdditionalField
import com.thejohnsondev.model.NoteModel
import com.thejohnsondev.model.PasswordModel


fun AdditionalField.encryptModel(key: ByteArray): AdditionalField {
    return this.copy(
        title = this.title.trim().encrypt(key),
        value = this.value.trim().encrypt(key)
    )
}

fun AdditionalField.decryptModel(key: ByteArray): AdditionalField {
    return this.copy(
        title = this.title.decrypt(key),
        value = this.value.decrypt(key)
    )
}

fun PasswordModel.encryptModel(key: ByteArray): PasswordModel {
    return this.copy(
        organization = this.organization.trim().encrypt(key),
        organizationLogo = this.organizationLogo?.trim()?.encrypt(key),
        title = this.title.trim().encrypt(key),
        password = this.password.trim().encrypt(key),
        additionalFields = this.additionalFields.map { it.encryptModel(key) }
    )
}

fun PasswordModel.decryptModel(key: ByteArray): PasswordModel {
    return this.copy(
        organization = this.organization.decrypt(key),
        organizationLogo = this.organizationLogo?.decrypt(key),
        title = this.title.decrypt(key),
        password = this.password.decrypt(key),
        additionalFields = this.additionalFields.map { it.decryptModel(key) }
    )
}

fun NoteModel.encryptModel(key: ByteArray): NoteModel {
    return this.copy(
        title = this.title.trim().encrypt(key),
        description = this.description.trim().encrypt(key)
    )
}

fun NoteModel.decryptModel(key: ByteArray): NoteModel {
    return this.copy(
        title = this.title.decrypt(key),
        description = this.description.decrypt(key)
    )
}