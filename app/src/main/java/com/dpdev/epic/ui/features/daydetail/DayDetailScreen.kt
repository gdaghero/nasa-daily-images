package com.dpdev.epic.ui.features.daydetail

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dpdev.core.model.Coordinates
import com.dpdev.core.model.Day
import com.dpdev.core.model.Image
import com.dpdev.epic.R
import com.dpdev.epic.ui.icon.EpicIcons
import com.dpdev.epic.ui.theme.EpicTheme
import kotlinx.coroutines.flow.flowOf

@Composable
fun DayDetailRoute(
    modifier: Modifier = Modifier,
    viewModel: DayDetailViewModel = hiltViewModel(),
    onBackPress: () -> Unit,
    onImageClick: (String) -> Unit
) {
    val imagesUiState: ImagesUiState by viewModel.imagesUiState.collectAsStateWithLifecycle()
    val dayUiState: DayUiState by viewModel.dayUiState.collectAsStateWithLifecycle()

    DayDetailScreen(
        modifier = modifier,
        imagesUiState = imagesUiState,
        dayUiState = dayUiState,
        onBackPress = onBackPress,
        onImageClick = onImageClick
    )
}

@Composable
fun DayDetailScreen(
    modifier: Modifier = Modifier,
    imagesUiState: ImagesUiState,
    dayUiState: DayUiState,
    onBackPress: () -> Unit,
    onImageClick: (String) -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        DayDetailToolbar(
            onBackPress = onBackPress,
            dayUiState = dayUiState
        )

        when (imagesUiState) {
            ImagesUiState.Error -> {
                Toast.makeText(
                    LocalContext.current,
                    "Something went wrong, please try again.",
                    Toast.LENGTH_SHORT
                ).show()
                onBackPress()
            }

            ImagesUiState.Loading -> Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(alignment = Alignment.Center)
                )
            }

            is ImagesUiState.Success -> {
                DaySyncStatusButton(uiState = dayUiState)
                ImagesGrid(
                    images = imagesUiState.images,
                    onImageClick = onImageClick
                )
            }
        }
    }
}

@Composable
private fun ImagesGrid(
    modifier: Modifier = Modifier,
    images: List<Image>,
    onImageClick: (String) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(count = 2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
    ) {
        items(items = images, key = { it.id }) {
            Card(modifier = Modifier
                .aspectRatio(1f)
                .border(
                    width = 1.dp,
                    color = colorResource(id = R.color.item_daily_image_card_border),
                    shape = RoundedCornerShape(size = 8.dp)
                )
                .clickable { onImageClick(it.id) }
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    when (it.status) {
                        is Image.Status.Downloaded -> {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data((it.status as Image.Status.Downloaded).path)
                                    .build(),
                                contentDescription = null
                            )
                            Column(
                                modifier = Modifier
                                    .background(
                                        Brush.verticalGradient(
                                            0f to Color.Transparent,
                                            .5f to Color.Black.copy(alpha = 0.9F),
                                            1f to Color.Black.copy(alpha = 1F)
                                        )
                                    )
                                    .fillMaxWidth()
                                    .align(alignment = Alignment.BottomStart)
                                    .padding(all = 16.dp)
                            ) {
                                Text(
                                    text = it.id,
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = 1
                                )
                                Text(
                                    text = it.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1
                                )
                            }
                        }

                        is Image.Status.Downloading -> {
                            val progress by (it.status as Image.Status.Downloading).progress
                                .collectAsStateWithLifecycle(initialValue = 0)
                            CircularProgressIndicator(
                                modifier = Modifier.align(alignment = Alignment.Center),
                                progress = progress.toFloat() / 100
                            )
                        }
                    }
                }
            }
        }
        item {
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
        }
    }
}

@Composable
private fun DayDetailToolbar(
    modifier: Modifier = Modifier,
    dayUiState: DayUiState,
    onBackPress: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            modifier = Modifier.clickable { onBackPress() },
            imageVector = EpicIcons.ChevronLeft,
            contentDescription = null
        )
        when (dayUiState) {
            DayUiState.Loading -> Unit
            is DayUiState.Success -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(space = 16.dp)
                ) {
                    Text(
                        text = dayUiState.day.name,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = dayUiState.day.date,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.size(size = 24.dp))
    }
}

@Composable
private fun DaySyncStatusButton(
    modifier: Modifier = Modifier,
    uiState: DayUiState
) {
    val context = LocalContext.current
    when (uiState) {
        DayUiState.Loading -> Unit
        is DayUiState.Success -> {
            val status = uiState.day.status
            val isDownloading = status is Day.Status.InProgress
            Button(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp),
                onClick = {
                    // TODO:
                    Toast.makeText(
                        context,
                        "Not implemented yet :(",
                        Toast.LENGTH_LONG
                    )
                        .show()
                },
                shape = MaterialTheme.shapes.small,
                enabled = status is Day.Status.Completed
            ) {
                Text(
                    text = stringResource(
                        id = if (isDownloading) {
                            R.string.day_detail_button_status_downloading
                        } else {
                            R.string.day_detail_button_status_complete
                        }
                    )
                )
                if (isDownloading) {
                    Spacer(modifier = Modifier.width(width = 8.dp))
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(size = 24.dp)
                            .align(alignment = Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DayDetailScreenPreview() {
    EpicTheme {
        Surface {
            DayDetailScreen(
                imagesUiState = ImagesUiState.Success(
                    images = listOf(
                        Image(
                            id = "2023123101202323",
                            caption = "",
                            name = "image name 1",
                            version = "",
                            date = "",
                            coordinates = Coordinates(0.0, 0.0),
                            status = Image.Status.Downloaded(path = "")
                        ), Image(
                            id = "id2",
                            caption = "",
                            name = "image name 2",
                            version = "",
                            date = "",
                            coordinates = Coordinates(0.0, 0.0),
                            status = Image.Status.Downloading(progress = flowOf(30))
                        )
                    )
                ),
                dayUiState = DayUiState.Success(
                    day = Day(
                        date = "12/12/2023",
                        name = "Sunday",
                        status = Day.Status.InProgress(completedCount = 2, totalCount = 3)
                    )
                ),
                onBackPress = {},
                onImageClick = {}
            )
        }
    }
}


@Preview
@Composable
private fun DayDetailScreenLoading() {
    EpicTheme {
        Surface {
            DayDetailScreen(
                imagesUiState = ImagesUiState.Loading,
                dayUiState = DayUiState.Loading,
                onBackPress = {},
                onImageClick = {}
            )
        }
    }
}
