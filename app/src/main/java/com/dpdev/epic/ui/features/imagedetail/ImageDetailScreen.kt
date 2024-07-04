package com.dpdev.epic.ui.features.imagedetail

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dpdev.core.model.Image
import com.dpdev.epic.R
import com.dpdev.epic.ui.icon.EpicIcons

@Composable
fun ImageDetailRoute(
    onBackClick: () -> Unit,
    viewModel: ImageDetailViewModel = hiltViewModel()
) {
    val imageDetailUiState: ImageDetailUiState by viewModel.imageUiState.collectAsStateWithLifecycle()

    ImageDetailScreen(
        uiState = imageDetailUiState,
        onBackClick = onBackClick
    )
}

@Composable
private fun ImageDetailScreen(
    modifier: Modifier = Modifier,
    uiState: ImageDetailUiState,
    onBackClick: () -> Unit
) {
    var showSheet by remember { mutableStateOf(false) }
    val toggleSheet: () -> Unit = { showSheet = !showSheet }

    Column(modifier = modifier.fillMaxSize()) {
        ImageDetailToolbar(
            onBackClick = onBackClick,
            onMoreClick = toggleSheet
        )
        when (uiState) {
            ImageDetailUiState.Loading -> Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(alignment = Alignment.Center))
            }

            is ImageDetailUiState.Success -> {
                ZoomableImage(
                    modifier = modifier,
                    uri = (uiState.image.status as Image.Status.Downloaded).path
                )
                if (showSheet) {
                    ImageDetailBottomSheet(
                        image = uiState.image,
                        onDismiss = toggleSheet
                    )
                }
            }
        }
    }
}


@Composable
private fun ZoomableImage(
    modifier: Modifier = Modifier,
    uri: String
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offset += offsetChange
    }
    Box(
        modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        AsyncImage(
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
                .transformable(state = state)
                .align(alignment = Alignment.Center),
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(data = uri)
                .build(),
            contentDescription = null
        )
    }
}


@Composable
private fun ImageDetailToolbar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onMoreClick: () -> Unit
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
            modifier = Modifier.clickable { onBackClick() },
            imageVector = EpicIcons.ChevronLeft,
            contentDescription = null
        )
        Icon(
            modifier = Modifier.clickable { onMoreClick() },
            imageVector = EpicIcons.Info,
            contentDescription = null
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageDetailBottomSheet(
    modifier: Modifier = Modifier,
    image: Image,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = { onDismiss() },
        sheetState = rememberModalBottomSheetState(),
        dragHandle = { BottomSheetDefaults.DragHandle() },
        windowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.image_detail_info_title),
                    style = MaterialTheme.typography.headlineSmall
                )
                Icon(
                    modifier = Modifier.clickable { onDismiss() },
                    imageVector = EpicIcons.Close,
                    contentDescription = null
                )
            }
            Column {
                Label(textResId = R.string.image_detail_info_number)
                Value(text = image.id)
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(weight = 1f)) {
                    Label(textResId = R.string.image_detail_info_capture)
                    Value(text = image.date)
                }
                Column(modifier = Modifier.weight(weight = 1f)) {
                    Label(textResId = R.string.image_detail_info_latitude)
                    Value(text = image.coordinates.latitude.toString())
                }
                Column(modifier = Modifier.weight(weight = 1f)) {
                    Label(textResId = R.string.image_detail_info_longitude)
                    Value(text = image.coordinates.longitude.toString())
                }
            }
            Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
        }
    }
}

@Composable
private fun Label(@StringRes textResId: Int) {
    Text(
        text = stringResource(id = textResId),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
    )
}

@Composable
private fun Value(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Medium
    )
}
