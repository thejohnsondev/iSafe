package com.thejohnsondev.isafe.presentation.screens.passwords.add_edit_password

import com.thejohnsondev.isafe.domain.models.PasswordModel

sealed class AddEditPasswordAction {
    class EnterOrganization(val organization: String): AddEditPasswordAction()
    class EnterTitle(val title: String): AddEditPasswordAction()
    class EnterPassword(val password: String): AddEditPasswordAction()
    class AddAdditionalField(val timeStamp: String): AddEditPasswordAction()
    class EnterAdditionalFieldTitle(val timeStamp: String, val title: String): AddEditPasswordAction()
    class EnterAdditionalFieldValue(val timeStamp: String, val value: String): AddEditPasswordAction()
    object SavePassword: AddEditPasswordAction()
    class SetPasswordModelForEdit(val passwordModel: PasswordModel): AddEditPasswordAction()
}