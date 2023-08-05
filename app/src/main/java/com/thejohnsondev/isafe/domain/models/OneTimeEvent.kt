package com.thejohnsondev.isafe.domain.models

sealed class OneTimeEvent {
    class InfoToast(val message: String): OneTimeEvent()
    class InfoSnackbar(val message: String): OneTimeEvent()
    object SuccessNavigation : OneTimeEvent()
}
