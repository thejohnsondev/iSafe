package com.thejohnsondev.isafe.presentation.screens.feat.notes.add_note

sealed class AddNoteAction{
    class EnterTitle(val title: String): AddNoteAction()
    class EnterDescription(val description: String): AddNoteAction()
    object SaveNote: AddNoteAction()
}
