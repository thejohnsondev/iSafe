package com.thejohnsondev.isafe.presentation.screens.passwords.list

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
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.thejohnsondev.isafe.R
import com.thejohnsondev.isafe.domain.models.BankAccountModel
import com.thejohnsondev.isafe.domain.models.LoadingState
import com.thejohnsondev.isafe.domain.models.OneTimeEvent
import com.thejohnsondev.isafe.domain.models.PasswordModel
import com.thejohnsondev.isafe.presentation.components.EmptyListPlaceHolder
import com.thejohnsondev.isafe.presentation.components.FilterGroup
import com.thejohnsondev.isafe.presentation.components.FullScreenLoading
import com.thejohnsondev.isafe.presentation.components.PasswordItem
import com.thejohnsondev.isafe.presentation.components.SearchBar
import com.thejohnsondev.isafe.presentation.navigation.Screens
import com.thejohnsondev.isafe.utils.FILTER_ALL
import com.thejohnsondev.isafe.utils.FILTER_BANK_ACCOUNTS
import com.thejohnsondev.isafe.utils.FILTER_PASSWORDS
import com.thejohnsondev.isafe.utils.Size16
import com.thejohnsondev.isafe.utils.Size32
import com.thejohnsondev.isafe.utils.Size48
import com.thejohnsondev.isafe.utils.Size86
import com.thejohnsondev.isafe.utils.copySensitiveData
import com.thejohnsondev.isafe.utils.toJson
import com.thejohnsondev.isafe.utils.toast

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun VaultScreen(
    navController: NavHostController,
    viewModel: VaultViewModel,
) {
    val context = LocalContext.current
    val clipboardManager: ClipboardManager =
        getSystemService(context, ClipboardManager::class.java) as ClipboardManager
    val keyboardController = LocalSoftwareKeyboardController.current
    val state = viewModel.state.collectAsState(VaultState())
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
        viewModel.perform(VaultAction.FetchVault)
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
                            text = stringResource(R.string.your_vault),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    navigationIcon = {
                        Icon(
                            modifier = Modifier.size(Size48),
                            painter = painterResource(id = R.drawable.i_safe_foreground),
                            contentDescription = ""
                        )
                    }
                )
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate(Screens.AddEditPassword.name)
                },
                expanded = expandedFab,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add)
                    )
                },
                text = {
                    Text(text = stringResource(R.string.add))
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
                viewModel.perform(VaultAction.DeletePassword(password))
            },
            onEditPasswordClick = { password ->
                navController.navigate(
                    "${Screens.AddEditPassword.name}/${password.toJson()}")
            },
            onSearchQueryEntered = { query ->
                viewModel.perform(VaultAction.Search(query))
            },
            onStopSearching = {
                keyboardController?.hide()
                viewModel.perform(VaultAction.StopSearching)
            })
    }
}

@Composable
fun VaultContent(
    modifier: Modifier = Modifier,
    state: VaultState,
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
    val filterAll = stringResource(id = FILTER_ALL)
    val filterPasswords = stringResource(id = FILTER_PASSWORDS)
    val filterBankAccounts = stringResource(id = FILTER_BANK_ACCOUNTS)
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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Size32)
                )
            }
        }
        if (passwordsList.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(id = R.string.passwords),
                    modifier = Modifier.padding(Size16),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            items(passwordsList) {
                PasswordItem(
                    item = it,
                    onClick = {},
                    onCopyClick = { password ->
                        clipboardManager.copySensitiveData(password)
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
                    text = stringResource(id = R.string.bank_accounts),
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