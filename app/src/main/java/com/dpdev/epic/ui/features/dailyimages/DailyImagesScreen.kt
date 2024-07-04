package com.dpdev.epic.ui.features.dailyimages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dpdev.core.model.Day
import com.dpdev.epic.R
import com.dpdev.epic.ui.icon.EpicIcons
import com.dpdev.epic.ui.theme.EpicTheme

@Composable
fun DailyImagesRoute(
    modifier: Modifier = Modifier,
    onDayClick: (date: String) -> Unit,
    viewModel: DailyImagesViewModel = hiltViewModel()
) {
    val dailyImagesUiState: DailyImagesUiState by viewModel.uiState.collectAsStateWithLifecycle()

    DailyImagesScreen(
        modifier = modifier,
        dailyImagesUiState = dailyImagesUiState,
        onClick = onDayClick
    )
}

@Composable
fun DailyImagesScreen(
    modifier: Modifier = Modifier,
    dailyImagesUiState: DailyImagesUiState,
    onClick: (String) -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        DailyImagesToolbar()
        when (dailyImagesUiState) {
            DailyImagesUiState.Error -> Unit
            DailyImagesUiState.Loading -> Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .testTag("dailyImages:loading")
                        .align(alignment = Alignment.Center)
                )
            }

            is DailyImagesUiState.Success -> LazyColumn(
                state = rememberLazyListState(),
                contentPadding = PaddingValues(all = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items = dailyImagesUiState.days, key = { it.date }) { day ->
                    DayCard(day = day, onClick = { onClick(day.date) })
                }
                item {
                    Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
                }
            }
        }
    }
}

@Composable
private fun DayCard(
    modifier: Modifier = Modifier,
    day: Day,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .testTag("dailyImages:day${day.date}")
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(weight = 1f),
                verticalArrangement = Arrangement.spacedBy(space = 4.dp)
            ) {
                Text(
                    text = day.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = day.date,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
            DayStatusContainer(status = day.status)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                modifier = Modifier.align(Alignment.CenterVertically),
                imageVector = EpicIcons.ChevronRight,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun RowScope.DayStatusContainer(
    modifier: Modifier = Modifier,
    status: Day.Status
) {
    val statusColor = when (status) {
        is Day.Status.Completed -> colorResource(id = R.color.support_success)
        is Day.Status.InProgress -> colorResource(id = R.color.support_warning)
        is Day.Status.Pending -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
    }

    Row(
        modifier = modifier
            .clip(shape = RoundedCornerShape(size = 16.dp))
            .background(color = MaterialTheme.colorScheme.background)
            .align(alignment = Alignment.CenterVertically)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 4.dp)
    ) {
        if (status !is Day.Status.Pending) {
            Icon(
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .size(size = 16.dp)
                    .testTag("dailyImages:statusIcon"),
                imageVector = if (status is Day.Status.Completed) {
                    EpicIcons.IconCheck
                } else {
                    EpicIcons.IconSync
                },
                tint = statusColor,
                contentDescription = null
            )
        }

        Text(
            modifier = modifier.align(alignment = Alignment.CenterVertically),
            text = when (status) {
                is Day.Status.Completed -> stringResource(
                    id = R.string.item_daily_images_status_format,
                    status.totalCount,
                    status.totalCount
                )

                is Day.Status.InProgress -> stringResource(
                    id = R.string.item_daily_images_status_format,
                    status.completedCount,
                    status.totalCount
                )

                is Day.Status.Pending -> stringResource(
                    id = R.string.item_daily_images_status_pending
                )
            },
            color = statusColor,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
private fun DailyImagesToolbar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.daily_images_screen_name),
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
@Preview
private fun DailyImages() {
    EpicTheme {
        Surface {
            DailyImagesScreen(
                dailyImagesUiState = DailyImagesUiState.Success(
                    days = listOf(
                        Day(
                            date = "12/31/2022",
                            name = "Monday",
                            status = Day.Status.InProgress(completedCount = 3, totalCount = 12)
                        ),
                        Day(
                            date = "12/30/2022",
                            name = "Sunday",
                            status = Day.Status.Pending
                        ),
                        Day(
                            date = "12/29/2022",
                            name = "Saturday",
                            status = Day.Status.Completed(totalCount = 12)
                        )
                    )
                )
            ) {}
        }
    }
}

@Composable
@Preview
private fun DailyImagesLoading() {
    EpicTheme {
        Surface {
            DailyImagesScreen(
                dailyImagesUiState = DailyImagesUiState.Loading,
                onClick = {}
            )
        }
    }
}
