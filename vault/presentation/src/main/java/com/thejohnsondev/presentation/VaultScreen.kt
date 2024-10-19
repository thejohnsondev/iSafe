package com.thejohnsondev.presentation

import android.content.ClipboardManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.core.content.ContextCompat.getSystemService
import com.thejohnsondev.common.R
import com.thejohnsondev.common.copyData
import com.thejohnsondev.common.copySensitiveData
import com.thejohnsondev.common.toast
import com.thejohnsondev.designsystem.AppTheme
import com.thejohnsondev.designsystem.Size16
import com.thejohnsondev.designsystem.Size32
import com.thejohnsondev.designsystem.Size72
import com.thejohnsondev.designsystem.getAppLogo
import com.thejohnsondev.model.BankAccountModel
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.OneTimeEvent
import com.thejohnsondev.model.PasswordModel
import com.thejohnsondev.ui.ConfirmAlertDialog
import com.thejohnsondev.ui.EmptyListPlaceHolder
import com.thejohnsondev.ui.FilterGroup
import com.thejohnsondev.ui.Loader
import com.thejohnsondev.ui.PasswordItem
import com.thejohnsondev.ui.ScaffoldConfig
import com.thejohnsondev.ui.SearchBar
import com.thejohnsondev.ui.bounceClick
import com.thejohnsondev.ui.scaffold.BottomNavItem
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun VaultScreen(
    viewModel: VaultViewModel,
    onAddNewPasswordClick: () -> Unit,
    onEditPasswordClick: (PasswordModel) -> Unit,
    setScaffoldConfig: (ScaffoldConfig) -> Unit
) {
    val context = LocalContext.current
    val clipboardManager: ClipboardManager =
        getSystemService(context, ClipboardManager::class.java) as ClipboardManager
    val keyboardController = LocalSoftwareKeyboardController.current
    val state = viewModel.state.collectAsState(VaultViewModel.State())
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val listState = rememberLazyListState()
    val expandedFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }
    val appLogo = getAppLogo()


    LaunchedEffect(true) {
        setScaffoldConfig(
            ScaffoldConfig(
                isTopAppBarVisible = true,
                isBottomNavBarVisible = true,
                topAppBarTitle = context.getString(R.string.vault),
                topAppBarIcon = appLogo,
                isFabVisible = true,
                fabTitle = context.getString(R.string.add),
                fabIcon = Icons.Default.Add,
                onFabClick = {
                    onAddNewPasswordClick()
                },
                isFabExpanded = expandedFab,
                snackBarHostState = snackBarHostState,
                bottomBarItemIndex = BottomNavItem.Vault.index
            )
        )
        viewModel.perform(VaultViewModel.Action.FetchVault)
        viewModel.getEventFlow().collect {
            when (it) {
                is OneTimeEvent.InfoToast -> context.toast(it.message)
                is OneTimeEvent.InfoSnackbar -> snackBarHostState.showSnackbar(
                    it.message,
                    duration = SnackbarDuration.Short
                )

                is OneTimeEvent.SuccessNavigation -> {}

            }
        }
    }
    VaultContent(
        state = state.value,
        lazyListState = listState,
        onCopyData = { data ->
            clipboardManager.copyData(data)
        },
        onCopySensitiveData = { data ->
            clipboardManager.copySensitiveData(data)
        },
        onEditPasswordClick = { password ->
            onEditPasswordClick(password)
        },
        onAction = { action ->
            viewModel.perform(action)
        },
        onHideKeyboard = {
            keyboardController?.hide()
        })
}

@Composable
fun VaultContent(
    modifier: Modifier = Modifier,
    state: VaultViewModel.State,
    lazyListState: LazyListState,
    onCopyData: (String) -> Unit,
    onCopySensitiveData: (String) -> Unit,
    onEditPasswordClick: (PasswordModel) -> Unit,
    onHideKeyboard: () -> Unit,
    onAction: (VaultViewModel.Action) -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (state.loadingState is LoadingState.Loading) {
            Loader()
            return@Surface
        }
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            if (state.isReordering) {
                ReorderingItemsList(
                    state = state,
                    passwordsList = state.passwordsList,
                    bankAccountsList = state.bankAccountsList,
                    isSearching = state.isSearching,
                    isReordering = state.isReordering,
                    onCopyData = onCopyData,
                    onCopySensitiveData = onCopySensitiveData,
                    onEditPasswordClick = onEditPasswordClick,
                    onHideKeyboard = onHideKeyboard,
                    onAction = onAction
                )
            } else {
                ItemsList(
                    state = state,
                    passwordsList = state.passwordsList,
                    bankAccountsList = state.bankAccountsList,
                    isSearching = state.isSearching,
                    isReordering = state.isReordering,
                    lazyListState = lazyListState,
                    onCopyData = onCopyData,
                    onCopySensitiveData = onCopySensitiveData,
                    onEditPasswordClick = onEditPasswordClick,
                    onHideKeyboard = onHideKeyboard,
                    onAction = onAction
                )
            }
        }
        Dialogs(state, onAction)
    }
}

@Composable
fun Filters(
    onAllClick: () -> Unit,
    onPasswordsClick: () -> Unit,
    onBankAccountsClick: () -> Unit,
    onNotesClick: () -> Unit,
) {
    // TODO: Uncomment rest of filters once the items types are added
    val filterAll = stringResource(id = R.string.all)
    val filterPasswords = stringResource(id = R.string.passwords)
//    val filterBankAccounts = stringResource(id = R.string.bank_accounts)
//    val filterNotes = stringResource(id = R.string.notes)
    val filters = listOf(
        filterAll,
        filterPasswords,
//        filterBankAccounts,
//        filterNotes
    )
    FilterGroup(filters = filters, onFilterClick = {
        when (it) {
            filterAll -> onAllClick()
            filterPasswords -> onPasswordsClick()
//            filterBankAccounts -> onBankAccountsClick()
//            filterNotes -> onNotesClick()
        }
    }, defaultSelected = filterAll)
}

@Composable
fun ItemsList(
    state: VaultViewModel.State,
    passwordsList: List<PasswordModel>,
    bankAccountsList: List<BankAccountModel>,
    lazyListState: LazyListState,
    onCopyData: (String) -> Unit,
    onCopySensitiveData: (String) -> Unit,
    onEditPasswordClick: (PasswordModel) -> Unit,
    isReordering: Boolean,
    isSearching: Boolean,
    onHideKeyboard: () -> Unit,
    onAction: (VaultViewModel.Action) -> Unit
) {
    LazyColumn(state = lazyListState, modifier = Modifier.fillMaxSize()) {
        item {
            SearchBarItem(state, onAction, onHideKeyboard)
        }
        item {
            FiltersItem(isSearching)
        }
        item {
            EmptyListPlaceholder(passwordsList, bankAccountsList)
        }
        if (passwordsList.isNotEmpty()) {
            item {
                PasswordsTitleItem(onAction, isReordering)
            }
            items(passwordsList) {
                PasswordItem(
                    item = it,
                    isReordering = isReordering,
                    onClick = {},
                    onCopySensitiveClick = { password ->
                        onCopySensitiveData(password)
                    },
                    onCopyClick = { title ->
                        onCopyData(title)
                    },
                    onDeleteClick = { password ->
                        onAction(VaultViewModel.Action.ShowHideConfirmDelete(Pair(true, password)))
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
private fun Dialogs(
    state: VaultViewModel.State,
    onAction: (VaultViewModel.Action) -> Unit
) {
    if (state.deletePasswordPair.first) {
        ConfirmAlertDialog(
            title = stringResource(R.string.delete_password),
            message = stringResource(R.string.delete_password_message),
            confirmButtonText = stringResource(R.string.delete),
            cancelButtonText = stringResource(R.string.cancel),
            onConfirm = {
                state.deletePasswordPair.second?.let {
                    onAction(VaultViewModel.Action.ShowHideConfirmDelete(Pair(false, null)))
                    onAction(VaultViewModel.Action.DeletePassword(it))
                }
            },
            onCancel = {
                onAction(VaultViewModel.Action.ShowHideConfirmDelete(Pair(false, null)))
            }
        )
    }
}

@Composable
fun ReorderingItemsList(
    state: VaultViewModel.State,
    passwordsList: List<PasswordModel>,
    bankAccountsList: List<BankAccountModel>,
    onCopyData: (String) -> Unit,
    onCopySensitiveData: (String) -> Unit,
    onEditPasswordClick: (PasswordModel) -> Unit,
    isSearching: Boolean,
    isReordering: Boolean,
    onHideKeyboard: () -> Unit,
    onAction: (VaultViewModel.Action) -> Unit
) {
    // TODO: add updating the reordered list on the database
    val reorderState = rememberReorderableLazyListState(onMove = { from, to ->
        onAction(VaultViewModel.Action.Reorder(from.index, to.index))
    })

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            SearchBarItem(state, onAction, onHideKeyboard)
        }
        item {
            FiltersItem(isSearching)
        }
        item {
            EmptyListPlaceholder(passwordsList, bankAccountsList)
        }

        if (passwordsList.isNotEmpty()) {
            item {
                PasswordsTitleItem(onAction, isReordering)
            }
            item {
                LazyColumn(
                    state = reorderState.listState,
                    modifier = Modifier
                        .fillParentMaxHeight()
                        .reorderable(reorderState)
                        .detectReorderAfterLongPress(reorderState)
                ) {
                    items(passwordsList, key = { it.id.orEmpty() }) {
                        ReorderableItem(
                            reorderableState = reorderState,
                            key = it.id
                        ) { isDragging ->
                            PasswordItem(
                                item = it,
                                isReordering = isReordering,
                                isDragging = isDragging,
                                onClick = {},
                                onCopySensitiveClick = { password ->
                                    onCopySensitiveData(password)
                                },
                                onCopyClick = { title ->
                                    onCopyData(title)
                                },
                                onDeleteClick = { password ->
                                    onAction(VaultViewModel.Action.DeletePassword(password))
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
    state: VaultViewModel.State,
    onAction: (VaultViewModel.Action) -> Unit,
    onHideKeyboard: () -> Unit,
) {
    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Size16),
        onQueryEntered = { query ->
            onAction(VaultViewModel.Action.Search(query, state.isDeepSearchEnabled))
        },
        onQueryClear = {
            onHideKeyboard()
            onAction(VaultViewModel.Action.StopSearching)
        })
}

@Composable
fun FiltersItem(
    isSearching: Boolean
) {
    // TODO: made invisible for now
    AnimatedVisibility(visible = false) {
        Filters(
            onAllClick = {

            }, onPasswordsClick = {

            }, onBankAccountsClick = {

            }, onNotesClick = {

            }
        )
    }
}

@Composable
fun EmptyListPlaceholder(
    passwordsList: List<PasswordModel>,
    bankAccountsList: List<BankAccountModel>
) {
    if (passwordsList.isEmpty() && bankAccountsList.isEmpty()) {
        EmptyListPlaceHolder(
            modifier = Modifier
                .fillMaxSize()
                .padding(Size32)
        )
    }
}

@Composable
fun PasswordsTitleItem(
    onAction: (VaultViewModel.Action) -> Unit,
    isReordering: Boolean,
) {
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
        Row(
            horizontalArrangement = Arrangement.End
        ) {
            if (isReordering) {
                Button(
                    modifier = Modifier
                        .padding(start = Size16, top = Size16, bottom = Size16)
                        .bounceClick(),
                    onClick = {
                        onAction(VaultViewModel.Action.SaveNewOrderedList)
                    }) {
                    Text(text = stringResource(R.string.save))
                }
            }
            IconButton(modifier = Modifier
                .padding(vertical = Size16),
                onClick = {
                    onAction(VaultViewModel.Action.ToggleReordering)
                }
            ) {
                Icon(
                    imageVector = if (isReordering) Icons.Default.Cancel else Icons.Default.Reorder,
                    contentDescription = stringResource(R.string.reorder),
                )
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
@PreviewLightDark
fun VaultScreenPreviewWithPasswords() {
    AppTheme {
        VaultContent(
            state = VaultViewModel.State(
                passwordsList = listOf(
                    PasswordModel(
                        "11",
                        "org11",
                        null,
                        "title11",
                        "password11",
                        "1711195873"
                    ),
                    PasswordModel(
                        "12",
                        "org12",
                        null,
                        "title12",
                        "password12",
                        "1711195873"
                    ),
                    PasswordModel(
                        "13",
                        "org13",
                        null,
                        "title13",
                        "password13",
                        "1711195873"
                    )
                )
            ),
            lazyListState = rememberLazyListState(),
            onCopyData = {},
            onCopySensitiveData = {},
            onEditPasswordClick = {},
            onHideKeyboard = {},
            onAction = {}
        )
    }
}


@Composable
@PreviewLightDark
fun VaultScreenPreviewEmpty() {
    AppTheme {
        VaultContent(
            state = VaultViewModel.State(),
            lazyListState = rememberLazyListState(),
            onCopyData = {},
            onCopySensitiveData = {},
            onEditPasswordClick = {},
            onHideKeyboard = {},
            onAction = {}
        )
    }
}


@Composable
@PreviewLightDark
fun VaultScreenPreviewLoading() {
    AppTheme {
        VaultContent(
            state = VaultViewModel.State(
                loadingState = LoadingState.Loading
            ),
            lazyListState = rememberLazyListState(),
            onCopyData = {},
            onCopySensitiveData = {},
            onEditPasswordClick = {},
            onHideKeyboard = {},
            onAction = {}
        )
    }
}

@Composable
@PreviewLightDark
fun VaultScreenPreviewSearching() {
    AppTheme {
        VaultContent(
            state = VaultViewModel.State(
                isSearching = true,
                passwordsList = listOf(
                    PasswordModel(
                        "11",
                        "org11",
                        null,
                        "title11",
                        "password11",
                        "1711195873"
                    ),
                    PasswordModel(
                        "12",
                        "org12",
                        null,
                        "title12",
                        "password12",
                        "1711195873"
                    ),
                    PasswordModel(
                        "13",
                        "org13",
                        null,
                        "title13",
                        "password13",
                        "1711195873"
                    )
                )
            ),
            lazyListState = rememberLazyListState(),
            onCopyData = {},
            onCopySensitiveData = {},
            onEditPasswordClick = {},
            onHideKeyboard = {},
            onAction = {}
        )
    }
}

@Composable
@PreviewLightDark
fun VaultScreenPreviewReordering() {
    AppTheme {
        VaultContent(
            state = VaultViewModel.State(
                isReordering = true,
                passwordsList = listOf(
                    PasswordModel(
                        "11",
                        "org11",
                        null,
                        "title11",
                        "password11",
                        "1711195873"
                    ),
                    PasswordModel(
                        "12",
                        "org12",
                        null,
                        "title12",
                        "password12",
                        "1711195873"
                    ),
                    PasswordModel(
                        "13",
                        "org13",
                        null,
                        "title13",
                        "password13",
                        "1711195873"
                    )
                )
            ),
            lazyListState = rememberLazyListState(),
            onCopyData = {},
            onCopySensitiveData = {},
            onEditPasswordClick = {},
            onHideKeyboard = {},
            onAction = {}
        )
    }
}

