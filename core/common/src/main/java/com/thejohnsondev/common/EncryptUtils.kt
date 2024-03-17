package com.thejohnsondev.common

import com.thejohnsondev.model.AdditionalField
import com.thejohnsondev.model.NoteModel
import com.thejohnsondev.model.PasswordModel


fun AdditionalField.encryptModel(key: ByteArray): AdditionalField {
    return this.copy(
        title = this.title.encrypt(key),
        value = this.value.encrypt(key)
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
        organization = this.organization.encrypt(key),
        organizationLogo = this.organizationLogo?.encrypt(key),
        title = this.title.encrypt(key),
        password = this.password.encrypt(key),
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
        title = this.title.encrypt(key),
        description = this.description.encrypt(key)
    )
}

fun NoteModel.decryptModel(key: ByteArray): NoteModel {
    return this.copy(
        title = this.title.decrypt(key),
        description = this.description.decrypt(key)
    )
}