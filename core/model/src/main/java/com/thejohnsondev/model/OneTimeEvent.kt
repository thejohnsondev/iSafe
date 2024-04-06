package com.thejohnsondev.model

sealed class OneTimeEvent {
    class InfoToast(val message: String): OneTimeEvent()
    class InfoSnackbar(val message: String): OneTimeEvent()
    class SuccessNavigation(val message: String? = null) : OneTimeEvent()
}
