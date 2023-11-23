package com.thejohnsondev.presentation

import android.content.ClipboardManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.thejohnsondev.common.R
import com.thejohnsondev.common.copyData
import com.thejohnsondev.common.copySensitiveData
import com.thejohnsondev.common.toast
import com.thejohnsondev.designsystem.Size16
import com.thejohnsondev.designsystem.Size32
import com.thejohnsondev.designsystem.Size48
import com.thejohnsondev.designsystem.Size72
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
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

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
                            text = stringResource(R.string.your_vault),
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
            },
            reorder = { from, to ->
                viewModel.perform(VaultViewModel.Action.Reorder(from, to))
            },
            onToggleReordering = {
                viewModel.perform(VaultViewModel.Action.ToggleReordering)
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
    onStopSearching: () -> Unit,
    reorder: (Int, Int) -> Unit,
    onToggleReordering: () -> Unit,
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
            if (state.isReordering) {
                ReorderingItemsList(
                    passwordsList = state.passwordsList,
                    bankAccountsList = state.bankAccountsList,
                    isSearching = state.isSearching,
                    isReordering = state.isReordering,
                    clipboardManager = clipboardManager,
                    onDeletePasswordClick = onDeletePasswordClick,
                    onEditPasswordClick = onEditPasswordClick,
                    onSearchQueryEntered = onSearchQueryEntered,
                    onStopSearching = onStopSearching,
                    reorder = reorder,
                    onToggleReordering = onToggleReordering,
                )
            } else {
                ItemsList(
                    passwordsList = state.passwordsList,
                    bankAccountsList = state.bankAccountsList,
                    isSearching = state.isSearching,
                    isReordering = state.isReordering,
                    lazyListState = lazyListState,
                    clipboardManager = clipboardManager,
                    onDeletePasswordClick = onDeletePasswordClick,
                    onEditPasswordClick = onEditPasswordClick,
                    onSearchQueryEntered = onSearchQueryEntered,
                    onStopSearching = onStopSearching,
                    onToggleReordering = onToggleReordering
                )
            }
        }

    }
}

@Composable
fun Filters(
    onAllClick: () -> Unit,
    onPasswordsClick: () -> Unit,
    onBankAccountsClick: () -> Unit,
) {
    val filterAll = stringResource(id = R.string.all)
    val filterPasswords = stringResource(id = R.string.passwords)
    val filterBankAccounts = stringResource(id = R.string.bank_accounts)
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
    onToggleReordering: () -> Unit,
    isReordering: Boolean,
    isSearching: Boolean
) {
    LazyColumn(state = lazyListState, modifier = Modifier.fillMaxWidth()) {
        item {
            SearchBarItem(onSearchQueryEntered, onStopSearching)
        }
        item {
            FiltersItem(isSearching)
        }
        item {
            EmptyListPlaceholder(passwordsList, bankAccountsList)
        }
        if (passwordsList.isNotEmpty()) {
            item {
                PasswordsTitleItem(onToggleReordering, isReordering)
            }
            items(passwordsList) {
                PasswordItem(
                    item = it,
                    isReordering = isReordering,
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
                BankAccountsTitleItem()
            }
            items(bankAccountsList) {

            }
        }
        item {
            Spacer(modifier = Modifier.padding(bottom = Size72))
        }


    }
}

@Composable
fun ReorderingItemsList(
    passwordsList: List<PasswordModel>,
    bankAccountsList: List<BankAccountModel>,
    clipboardManager: ClipboardManager,
    onDeletePasswordClick: (PasswordModel) -> Unit,
    onEditPasswordClick: (PasswordModel) -> Unit,
    onSearchQueryEntered: (String) -> Unit,
    onStopSearching: () -> Unit,
    onToggleReordering: () -> Unit,
    isSearching: Boolean,
    isReordering: Boolean,
    reorder: (Int, Int) -> Unit
) {
    // TODO: add updating the reordered list on the database
    val state = rememberReorderableLazyListState(onMove = { from, to ->
        reorder(from.index, to.index)
    })

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            SearchBarItem(onSearchQueryEntered, onStopSearching)
        }
        item {
            FiltersItem(isSearching)
        }
        item {
            EmptyListPlaceholder(passwordsList, bankAccountsList)
        }

        if (passwordsList.isNotEmpty()) {
            item {
                PasswordsTitleItem(onToggleReordering, isReordering)
            }
            item {
                LazyColumn(
                    state = state.listState,
                    modifier = Modifier
                        .fillParentMaxHeight()
                        .reorderable(state)
                        .detectReorderAfterLongPress(state)
                ) {
                    items(passwordsList, key = { it.id }) {
                        ReorderableItem(reorderableState = state, key = it.id) { isDragging ->
                            PasswordItem(
                                item = it,
                                isReordering = isReordering,
                                isDragging = isDragging,
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
                }
            }
        }
        if (bankAccountsList.isNotEmpty()) {
            item {
                BankAccountsTitleItem()
            }
            item {
                LazyColumn(
                    modifier = Modifier
                        .fillParentMaxHeight()
                ) {
                    items(bankAccountsList) {

                    }
                }
            }
            item {
                Spacer(modifier = Modifier.padding(bottom = Size72))
            }
        }
    }
}

@Composable
fun SearchBarItem(
    onSearchQueryEntered: (String) -> Unit,
    onStopSearching: () -> Unit,
) {
    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Size16),
        onQueryEntered = { query ->
            onSearchQueryEntered(query)
        },
        onQueryClear = {
            onStopSearching()
        })
}

@Composable
fun FiltersItem(
    isSearching: Boolean
) {
    AnimatedVisibility(visible = !isSearching) {
        Filters(
            onAllClick = {

            }, onPasswordsClick = {

            }, onBankAccountsClick = {

            }
        )
    }
}

@Composable
fun EmptyListPlaceholder(
    passwordsList: List<PasswordModel>,
    bankAccountsList: List<BankAccountModel>
) {
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

@Composable
fun PasswordsTitleItem(
    onToggleReordering: () -> Unit,
    isReordering: Boolean,
) {
    var isDropDownMenuExpanded by remember {
        mutableStateOf(false)
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Size16)
    ) {
        Text(
            text = stringResource(id = R.string.passwords),
            modifier = Modifier.padding(vertical = Size16),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        // TODO: add save button 
        Box {
            IconButton(modifier = Modifier
                .padding(vertical = Size16),
                onClick = {
                    isDropDownMenuExpanded = !isDropDownMenuExpanded
                }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                )
            }
            DropdownMenu(
                expanded = isDropDownMenuExpanded,
                onDismissRequest = {
                    isDropDownMenuExpanded = false
                },
            ) {
                DropdownMenuItem(onClick = {
                    onToggleReordering()
                }, text = {
                    Text(text = stringResource(id = R.string.reorder))
                }, leadingIcon = {
                    Icon(imageVector = Icons.Default.Reorder, contentDescription = null)
                }, colors = MenuDefaults.itemColors(
                    textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    leadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ))
            }
        }
    }
}

@Composable
fun BankAccountsTitleItem() {
    Spacer(modifier = Modifier.height(Size16))
    Text(
        text = stringResource(id = R.string.bank_accounts),
        modifier = Modifier.padding(horizontal = Size16),
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground
    )
}


@Composable
fun StatusBarColor() {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(MaterialTheme.colorScheme.background)
}