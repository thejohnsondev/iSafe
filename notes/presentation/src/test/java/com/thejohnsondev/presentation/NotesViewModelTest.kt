import arrow.core.Either
import com.thejohnsondev.common.key_utils.FakeKeyUtils
import com.thejohnsondev.data.NotesRepository
import com.thejohnsondev.datastore.FakeDataStore
import com.thejohnsondev.domain.CreateNoteUseCase
import com.thejohnsondev.domain.DeleteNoteUseCase
import com.thejohnsondev.domain.GetAllNotesUseCase
import com.thejohnsondev.domain.NotesUseCases
import com.thejohnsondev.domain.UpdateNoteUseCase
import com.thejohnsondev.model.HttpError
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.NoteModel
import com.thejohnsondev.model.OneTimeEvent
import com.thejohnsondev.presentation.list.NotesViewModel
import com.thejohnsondev.test.MainDispatcherRule
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@ExperimentalCoroutinesApi
class NotesViewModelTest {

    @get:Rule
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val mockNotesRepository = Mockito.mock(NotesRepository::class.java)

    private val notesUseCases: NotesUseCases = NotesUseCases(
        CreateNoteUseCase(mockNotesRepository),
        DeleteNoteUseCase(mockNotesRepository),
        GetAllNotesUseCase(mockNotesRepository),
        UpdateNoteUseCase(mockNotesRepository)
    )

    private lateinit var viewModel: NotesViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = NotesViewModel(notesUseCases, FakeDataStore(), FakeKeyUtils())
    }

    @Test
    fun `fetchNotes updates state with decrypted notes`() = runTest {
        val fakeNotes = listOf(NoteModel("1", "Title", "Content", "1234567890"))
        `when`(mockNotesRepository.getNotes()).thenReturn(flowOf(Either.Right(fakeNotes)))

        viewModel.perform(NotesViewModel.Action.FetchNotes)
        val state = viewModel.state.first()

        assert(state.notesList == fakeNotes)
        assert(state.loadingState == LoadingState.Loaded)
    }

    @Test
    fun `fetchNotes updates state on error`() = runTest {
        val errorMessage = "Error fetching notes"
        `when`(mockNotesRepository.getNotes()).thenReturn(
            flowOf(
                Either.Left(
                    HttpError(
                        500,
                        errorMessage
                    )
                )
            )
        )

        viewModel.perform(NotesViewModel.Action.FetchNotes)

        assertTrue(viewModel.getEventFlow().value is OneTimeEvent.InfoToast)
    }


}