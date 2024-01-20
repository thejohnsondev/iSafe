package com.thejohnsondev.presentation

import android.content.ClipboardManager
import android.content.res.Configuration
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
import androidx.compose.material.icons.filled.Security
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.getSystemService
import com.thejohnsondev.common.R
import com.thejohnsondev.common.copyData
import com.thejohnsondev.common.copySensitiveData
import com.thejohnsondev.common.toast
import com.thejohnsondev.designsystem.ISafeTheme
import com.thejohnsondev.designsystem.Size16
import com.thejohnsondev.designsystem.Size32
import com.thejohnsondev.designsystem.Size72
import com.thejohnsondev.model.BankAccountModel
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.OneTimeEvent
import com.thejohnsondev.model.PasswordModel
import com.thejohnsondev.ui.EmptyListPlaceHolder
import com.thejohnsondev.ui.FilterGroup
import com.thejohnsondev.ui.FullScreenLoading
import com.thejohnsondev.ui.PasswordItem
import com.thejohnsondev.ui.ScaffoldConfig
import com.thejohnsondev.ui.SearchBar
import com.thejohnsondev.ui.bounceClick
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@OptIn(ExperimentalComposeUiApi::class)
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

    LaunchedEffect(true) {
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
    setScaffoldConfig(
        ScaffoldConfig(
            isTopAppBarVisible = true,
            isBottomNavBarVisible = true,
            topAppBarTitle = stringResource(R.string.your_vault),
            topAppBarIcon = Icons.Default.Security,
            isFabVisible = true,
            fabTitle = stringResource(R.string.add),
            fabIcon = Icons.Default.Add,
            onFabClick = {
                onAddNewPasswordClick()
            },
            isFabExpanded = expandedFab,
            snackBarHostState = snackBarHostState
        )
    )
    VaultContent(
        state = state.value,
        lazyListState = listState,
        onCopyData = { data ->
            clipboardManager.copyData(data)
        },
        onCopySensitiveData = { data ->
            clipboardManager.copySensitiveData(data)
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
        },
        onSaveReorderClick = {
            viewModel.perform(VaultViewModel.Action.SaveNewOrderedList)
        })
}

@Composable
fun VaultContent(
    modifier: Modifier = Modifier,
    state: VaultViewModel.State,
    lazyListState: LazyListState,
    onCopyData: (String) -> Unit,
    onCopySensitiveData: (String) -> Unit,
    onDeletePasswordClick: (PasswordModel) -> Unit,
    onEditPasswordClick: (PasswordModel) -> Unit,
    onSearchQueryEntered: (String) -> Unit,
    onStopSearching: () -> Unit,
    reorder: (Int, Int) -> Unit,
    onToggleReordering: () -> Unit,
    onSaveReorderClick: () -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxSize(),
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
                    onCopyData = onCopyData,
                    onCopySensitiveData = onCopySensitiveData,
                    onDeletePasswordClick = onDeletePasswordClick,
                    onEditPasswordClick = onEditPasswordClick,
                    onSearchQueryEntered = onSearchQueryEntered,
                    onStopSearching = onStopSearching,
                    reorder = reorder,
                    onToggleReordering = onToggleReordering,
                    onSaveReorderClick = onSaveReorderClick,
                )
            } else {
                ItemsList(
                    passwordsList = state.passwordsList,
                    bankAccountsList = state.bankAccountsList,
                    isSearching = state.isSearching,
                    isReordering = state.isReordering,
                    lazyListState = lazyListState,
                    onCopyData = onCopyData,
                    onCopySensitiveData = onCopySensitiveData,
                    onDeletePasswordClick = onDeletePasswordClick,
                    onEditPasswordClick = onEditPasswordClick,
                    onSearchQueryEntered = onSearchQueryEntered,
                    onStopSearching = onStopSearching,
                    onToggleReordering = onToggleReordering,
                    onSaveReorderClick = onSaveReorderClick
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
    onCopyData: (String) -> Unit,
    onCopySensitiveData: (String) -> Unit,
    onDeletePasswordClick: (PasswordModel) -> Unit,
    onEditPasswordClick: (PasswordModel) -> Unit,
    onSearchQueryEntered: (String) -> Unit,
    onStopSearching: () -> Unit,
    onToggleReordering: () -> Unit,
    onSaveReorderClick: () -> Unit,
    isReordering: Boolean,
    isSearching: Boolean
) {
    LazyColumn(state = lazyListState, modifier = Modifier.fillMaxSize()) {
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
                PasswordsTitleItem(onToggleReordering, onSaveReorderClick, isReordering)
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
    onCopyData: (String) -> Unit,
    onCopySensitiveData: (String) -> Unit,
    onDeletePasswordClick: (PasswordModel) -> Unit,
    onEditPasswordClick: (PasswordModel) -> Unit,
    onSearchQueryEntered: (String) -> Unit,
    onStopSearching: () -> Unit,
    onToggleReordering: () -> Unit,
    onSaveReorderClick: () -> Unit,
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
                PasswordsTitleItem(onToggleReordering, onSaveReorderClick, isReordering)
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
                                    onCopySensitiveData(password)
                                },
                                onCopyClick = { title ->
                                    onCopyData(title)
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
    onToggleReordering: () -> Unit,
    onSaveReorderClick: () -> Unit,
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
                        onSaveReorderClick()
                    }) {
                    Text(text = stringResource(R.string.save))
                }
            }
            IconButton(modifier = Modifier
                .padding(vertical = Size16),
                onClick = {
                    onToggleReordering()
                }
            ) {
                Icon(
                    imageVector = if (isReordering) Icons.Default.Cancel else Icons.Default.Reorder,
                    contentDescription = null,
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
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
fun VaultScreenPreviewWithPasswordsLight() {
    ISafeTheme {
        VaultContent(
            state = VaultViewModel.State(
                passwordsList = listOf(
                    PasswordModel(
                        "11",
                        "org11",
                        null,
                        "title11",
                        "password11",
                    ),
                    PasswordModel(
                        "12",
                        "org12",
                        null,
                        "title12",
                        "password12",
                    ),
                    PasswordModel(
                        "13",
                        "org13",
                        null,
                        "title13",
                        "password13",
                    )
                )
            ),
            lazyListState = rememberLazyListState(),
            onCopyData = {},
            onCopySensitiveData = {},
            onDeletePasswordClick = {},
            onEditPasswordClick = {},
            onSearchQueryEntered = {},
            onStopSearching = { },
            reorder = { _, _ -> },
            onToggleReordering = { }) {
        }
    }
}

@Composable
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
fun VaultScreenPreviewWithPasswordsDark() {
    ISafeTheme {
        VaultContent(
            state = VaultViewModel.State(
                passwordsList = listOf(
                    PasswordModel(
                        "11",
                        "org11",
                        null,
                        "title11",
                        "password11",
                    ),
                    PasswordModel(
                        "12",
                        "org12",
                        null,
                        "title12",
                        "password12",
                    ),
                    PasswordModel(
                        "13",
                        "org13",
                        null,
                        "title13",
                        "password13",
                    )
                )
            ),
            lazyListState = rememberLazyListState(),
            onCopyData = {},
            onCopySensitiveData = {},
            onDeletePasswordClick = {},
            onEditPasswordClick = {},
            onSearchQueryEntered = {},
            onStopSearching = { },
            reorder = { _, _ -> },
            onToggleReordering = { }) {
        }
    }
}

@Composable
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
fun VaultScreenPreviewEmptyLight() {
    ISafeTheme {
        VaultContent(
            state = VaultViewModel.State(),
            lazyListState = rememberLazyListState(),
            onCopyData = {},
            onCopySensitiveData = {},
            onDeletePasswordClick = {},
            onEditPasswordClick = {},
            onSearchQueryEntered = {},
            onStopSearching = { },
            reorder = { _, _ -> },
            onToggleReordering = { }) {
        }
    }
}

@Composable
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
fun VaultScreenPreviewEmptyDark() {
    ISafeTheme {
        VaultContent(
            state = VaultViewModel.State(),
            lazyListState = rememberLazyListState(),
            onCopyData = {},
            onCopySensitiveData = {},
            onDeletePasswordClick = {},
            onEditPasswordClick = {},
            onSearchQueryEntered = {},
            onStopSearching = { },
            reorder = { _, _ -> },
            onToggleReordering = { }) {
        }
    }
}

@Composable
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
fun VaultScreenPreviewLoadingLight() {
    ISafeTheme {
        VaultContent(
            state = VaultViewModel.State(
                loadingState = LoadingState.Loading
            ),
            lazyListState = rememberLazyListState(),
            onCopyData = {},
            onCopySensitiveData = {},
            onDeletePasswordClick = {},
            onEditPasswordClick = {},
            onSearchQueryEntered = {},
            onStopSearching = { },
            reorder = { _, _ -> },
            onToggleReordering = { }) {
        }
    }
}

@Composable
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
fun VaultScreenPreviewLoadingDark() {
    ISafeTheme {
        VaultContent(
            state = VaultViewModel.State(
                loadingState = LoadingState.Loading
            ),
            lazyListState = rememberLazyListState(),
            onCopyData = {},
            onCopySensitiveData = {},
            onDeletePasswordClick = {},
            onEditPasswordClick = {},
            onSearchQueryEntered = {},
            onStopSearching = { },
            reorder = { _, _ -> },
            onToggleReordering = { }) {
        }
    }
}

@Composable
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
fun VaultScreenPreviewSearchingLight() {
    ISafeTheme {
        VaultContent(
            state = VaultViewModel.State(
                isSearching = true
            ),
            lazyListState = rememberLazyListState(),
            onCopyData = {},
            onCopySensitiveData = {},
            onDeletePasswordClick = {},
            onEditPasswordClick = {},
            onSearchQueryEntered = {},
            onStopSearching = { },
            reorder = { _, _ -> },
            onToggleReordering = { }) {
        }
    }
}

@Composable
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
fun VaultScreenPreviewSearchingDark() {
    ISafeTheme {
        VaultContent(
            state = VaultViewModel.State(
                isSearching = true
            ),
            lazyListState = rememberLazyListState(),
            onCopyData = {},
            onCopySensitiveData = {},
            onDeletePasswordClick = {},
            onEditPasswordClick = {},
            onSearchQueryEntered = {},
            onStopSearching = { },
            reorder = { _, _ -> },
            onToggleReordering = { }) {
        }
    }
}

@Composable
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
fun VaultScreenPreviewReorderingLight() {
    ISafeTheme {
        VaultContent(
            state = VaultViewModel.State(
                isReordering = true
            ),
            lazyListState = rememberLazyListState(),
            onCopyData = {},
            onCopySensitiveData = {},
            onDeletePasswordClick = {},
            onEditPasswordClick = {},
            onSearchQueryEntered = {},
            onStopSearching = { },
            reorder = { _, _ -> },
            onToggleReordering = { }) {
        }
    }
}

@Composable
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
fun VaultScreenPreviewReorderingDark() {
    ISafeTheme {
        VaultContent(
            state = VaultViewModel.State(
                isReordering = true
            ),
            lazyListState = rememberLazyListState(),
            onCopyData = {},
            onCopySensitiveData = {},
            onDeletePasswordClick = {},
            onEditPasswordClick = {},
            onSearchQueryEntered = {},
            onStopSearching = { },
            reorder = { _, _ -> },
            onToggleReordering = { }) {
        }
    }
}

