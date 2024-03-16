package com.thejohnsondev.presentation

import com.thejohnsondev.common.base.BaseViewModel
import com.thejohnsondev.domain.ToolsUseCases
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.tools.ToolModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class ToolsViewModel @Inject constructor(
    private val useCases: ToolsUseCases
) : BaseViewModel() {

    private val _toolsList = MutableStateFlow<List<ToolModel>>(emptyList())

    val state = combine(
        _loadingState,
        _toolsList,
        ::mergeSources
    )

    private fun mergeSources(
        loadingState: LoadingState,
        toolsList: List<ToolModel>
    ) = State(loadingState, toolsList)

    fun perform(action: Action) {
        when (action) {
            is Action.FetchTools -> fetchTools()
        }
    }

    private fun fetchTools() = launch {
        useCases.getTools().first().fold(
            ifLeft = ::handleError,
            ifRight = ::handleToolsList
        )
    }

    private fun handleToolsList(list: List<ToolModel>) {
        _toolsList.value = list
    }

    sealed class Action {
        object FetchTools : Action()
    }

    data class State(
        val loadingState: LoadingState = LoadingState.Loaded,
        val toolsList: List<ToolModel> = emptyList()
    )

}