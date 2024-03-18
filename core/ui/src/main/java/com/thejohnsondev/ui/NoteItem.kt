package com.thejohnsondev.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.thejohnsondev.common.NOTE_TIME_FORMAT
import com.thejohnsondev.common.getTimeFormatted
import com.thejohnsondev.designsystem.EqualRounded
import com.thejohnsondev.designsystem.Size16
import com.thejohnsondev.designsystem.Size8
import com.thejohnsondev.model.NoteModel

@Composable
fun NoteItem(
    modifier: Modifier = Modifier, note: NoteModel, onNoteClicked: (NoteModel) -> Unit = {}
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = Size16, bottom = Size16, end = Size16),
        shape = EqualRounded.medium,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier.clickable {
                onNoteClicked(note)
            }, horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Top
        ) {
            Text(
                modifier = Modifier.padding(
                    top = Size16,
                    start = Size16,
                    bottom = Size8,
                    end = Size16
                ),
                text = note.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
            Text(
                modifier = Modifier.padding(start = Size16, end = Size16),
                text = note.description,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Surface(
                modifier = Modifier
                    .padding(top = Size8)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                color = MaterialTheme.colorScheme.secondary
            ) {
                Text(
                    modifier = Modifier.padding(Size16),
                    text = stringResource(
                        id = com.thejohnsondev.common.R.string.last_edit,
                        0L.toLong().getTimeFormatted(NOTE_TIME_FORMAT) // TODO: replace with lastEdit param
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }

}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NoteItemPreview() {
    NoteItem(
        note = NoteModel(
            "1",
            "Note 1 title",
            "Note 1 description (long), Note 1 description (long) Note 1 description (long) Note 1 description (long) Note 1 description (long), Note 1 description (long), Note 1 description (long) Note 1 description (long) Note 1 description (long) Note 1 description (long)",
        )
    )
}