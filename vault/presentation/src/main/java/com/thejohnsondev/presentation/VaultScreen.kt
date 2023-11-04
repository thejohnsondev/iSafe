package com.thejohnsondev.presentation

import android.content.ClipboardManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.ContextCompat.getSystemService
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.thejohnsondev.common.copyData
import com.thejohnsondev.common.copySensitiveData
import com.thejohnsondev.common.toast
import com.thejohnsondev.designsystem.Size16
import com.thejohnsondev.designsystem.Size32
import com.thejohnsondev.designsystem.Size48
import com.thejohnsondev.designsystem.Size86
import com.thejohnsondev.model.BankAccountModel
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.OneTimeEvent
import com.thejohnsondev.model.PasswordModel
import com.thejohnsondev.ui.EmptyListPlaceHolder
import com.thejohnsondev.ui.FilterGroup
import com.thejohnsondev.ui.FullScreenLoading
import com.thejohnsondev.ui.PasswordItem
import com.thejohnsondev.ui.SearchBar
import com.thejohnsondev.ui.bounceClick

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun VaultScreen(
    viewModel: VaultViewModel,
    onAddNewPasswordClick: () -> Unit,
    onEditPasswordClick: (PasswordModel) -> Unit
) {
    val context = LocalContext.current
    val clipboardManager: ClipboardManager =
        getSystemService(context, ClipboardManager::class.java) as ClipboardManager
    val keyboardController = LocalSoftwareKeyboardController.current
    val state = viewModel.state.collectAsState(VaultViewModel.State())
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val listState = rememberLazyListState()
    val expandedFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }
    StatusBarColor()
    LaunchedEffect(true) {
        viewModel.perform(VaultViewModel.Action.FetchVault)
        viewModel.getEventFlow().collect {
            when (it) {
                is OneTimeEvent.InfoToast -> context.toast(it.message)
                is OneTimeEvent.InfoSnackbar -> snackbarHostState.showSnackbar(
                    it.message,
                    duration = SnackbarDuration.Short
                )

                is OneTimeEvent.SuccessNavigation -> {}

            }
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    modifier = Modifier
                        .padding(bottom = Size86, start = Size16, end = Size16),
                ) {
                    Text(text = data.visuals.message)
                }
            }
        },
        topBar = {
            AnimatedVisibility(visible = !state.value.isSearching) {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(com.thejohnsondev.common.R.string.your_vault),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    navigationIcon = {
                        Icon(
                            modifier = Modifier.size(Size48),
                            painter = painterResource(id = com.thejohnsondev.designsystem.R.drawable.i_safe_foreground),
                            contentDescription = ""
                        )
                    }
                )
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .bounceClick(),
                onClick = {
                    onAddNewPasswordClick()
                },
                expanded = expandedFab,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(com.thejohnsondev.common.R.string.add)
                    )
                },
                text = {
                    Text(text = stringResource(com.thejohnsondev.common.R.string.add))
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        },
        floatingActionButtonPosition = FabPosition.End,
    ) {
        VaultContent(
            modifier = Modifier.padding(it),
            state = state.value,
            lazyListState = listState,
            clipboardManager = clipboardManager,
            onPasswordClick = {

            },
            onBankAccountClick = {

            },
            onDeletePasswordClick = { password ->
                viewModel.perform(VaultViewModel.Action.DeletePassword(password))
            },
            onEditPasswordClick = { password ->
                onEditPasswordClick(password)
            },
            onSearchQueryEntered = { query ->
                viewModel.perform(VaultViewModel.Action.Search(query))
            },
            onStopSearching = {
                keyboardController?.hide()
                viewModel.perform(VaultViewModel.Action.StopSearching)
            })
    }
}

@Composable
fun VaultContent(
    modifier: Modifier = Modifier,
    state: VaultViewModel.State,
    lazyListState: LazyListState,
    clipboardManager: ClipboardManager,
    onPasswordClick: (PasswordModel) -> Unit,
    onBankAccountClick: (BankAccountModel) -> Unit,
    onDeletePasswordClick: (PasswordModel) -> Unit,
    onEditPasswordClick: (PasswordModel) -> Unit,
    onSearchQueryEntered: (String) -> Unit,
    onStopSearching: () -> Unit
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        if (state.loadingState is LoadingState.Loading) {
            FullScreenLoading()
            return@Surface
        }
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            ItemsList(
                passwordsList = state.passwordsList,
                bankAccountsList = state.bankAccountsList,
                isSearching = state.isSearching,
                lazyListState = lazyListState,
                clipboardManager = clipboardManager,
                onDeletePasswordClick = onDeletePasswordClick,
                onEditPasswordClick = onEditPasswordClick,
                onSearchQueryEntered = onSearchQueryEntered,
                onStopSearching = onStopSearching
            )
        }

    }
}

@Composable
fun Filters(
    onAllClick: () -> Unit,
    onPasswordsClick: () -> Unit,
    onBankAccountsClick: () -> Unit,
) {
    val filterAll = stringResource(id = com.thejohnsondev.common.R.string.all)
    val filterPasswords = stringResource(id = com.thejohnsondev.common.R.string.passwords)
    val filterBankAccounts = stringResource(id = com.thejohnsondev.common.R.string.bank_accounts)
    val filters = listOf(
        filterAll,
        filterPasswords,
        filterBankAccounts
    )
    FilterGroup(filters = filters, onFilterClick = {
        when (it) {
            filterAll -> onAllClick()
            filterPasswords -> onPasswordsClick()
            filterBankAccounts -> onBankAccountsClick()
        }
    }, defaultSelected = filterAll)
}

@Composable
fun ItemsList(
    passwordsList: List<PasswordModel>,
    bankAccountsList: List<BankAccountModel>,
    lazyListState: LazyListState,
    clipboardManager: ClipboardManager,
    onDeletePasswordClick: (PasswordModel) -> Unit,
    onEditPasswordClick: (PasswordModel) -> Unit,
    onSearchQueryEntered: (String) -> Unit,
    onStopSearching: () -> Unit,
    isSearching: Boolean
) {
    LazyColumn(state = lazyListState, modifier = Modifier.fillMaxWidth()) {
        item {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(Size16),
                onQueryEntered = { query ->
                    onSearchQueryEntered(query)
                },
                onQueryClear = {
                    onStopSearching()
                })
        }
        item {
            AnimatedVisibility(visible = !isSearching) {
                Filters(
                    onAllClick = {

                    }, onPasswordsClick = {

                    }, onBankAccountsClick = {

                    }
                )
            }
        }
        item {
            AnimatedVisibility(
                visible = passwordsList.isEmpty() && bankAccountsList.isEmpty(),
            ) {
                EmptyListPlaceHolder(
                    modifier = Modifier.fillMaxSize()
                        .padding(Size32)

                )
            }
        }
        if (passwordsList.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(id = com.thejohnsondev.common.R.string.passwords),
                    modifier = Modifier.padding(Size16),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            items(passwordsList) {
                PasswordItem(
                    item = it,
                    onClick = {},
                    onCopySensitiveClick = { password ->
                        clipboardManager.copySensitiveData(password)
                    },
                    onCopyClick = { title ->
                        clipboardManager.copyData(title)
                    },
                    onDeleteClick = { password ->
                        onDeletePasswordClick(password)
                    },
                    onEditClick = { password ->
                        onEditPasswordClick(password)
                    })
            }
        }
        if (bankAccountsList.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(Size16))
                Text(
                    text = stringResource(id = com.thejohnsondev.common.R.string.bank_accounts),
                    modifier = Modifier.padding(Size16),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            items(bankAccountsList) {

            }
        }


    }

}

@Composable
fun StatusBarColor() {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(MaterialTheme.colorScheme.background)
}