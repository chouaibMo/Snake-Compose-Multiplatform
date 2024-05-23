package com.chouaibmo.pathfinder.snake

import android.provider.ContactsContract.CommonDataKinds.Im
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chouaibmo.pathfinder.R

@Composable
fun SnakeScreen(viewModel: SnakeViewModel = viewModel()) {

    val uiState = viewModel.uiState.value

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 40.dp)
    ) {


        if (uiState.isFinished) {
            GameOver(
                score = uiState.score,
                highScore = uiState.highScore,
                onRetryClicked = { viewModel.restartGame() }
            )
        } else {
            Text(text = "Snake", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text(
                text = "Move the snake to eat the apple.\nAvoid running into the walls or yourself.",
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))
            Row {
                Text("Score: ${uiState.score}")
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "High Score: ${uiState.highScore}")
            }

            Spacer(modifier = Modifier.height(8.dp))
            GameGrid(uiState)
            Spacer(modifier = Modifier.weight(1f))
            DirectionButtons(viewModel)
        }
    }
}

@Composable
fun GameGrid(uiState: SnakeUiState) {
    val padding = 16.dp
    val spacing = 1.dp


    val screenWidth = LocalConfiguration.current.screenWidthDp.dp - (padding * 2)
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp - (padding * 2)
    val totalSpacing = spacing * (uiState.grid.size - 1)
    val availableWidth = screenWidth - totalSpacing
    val availableHeight = screenHeight - totalSpacing
    val squareSize = min(availableWidth, availableHeight) / uiState.grid.size

    BoxWithConstraints {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing),
            modifier = Modifier
                .fillMaxWidth()
                .border(4.dp, Color.Black, RoundedCornerShape(4.dp))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing)
            ) {
                uiState.grid.forEachIndexed { x, row ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(spacing)
                    ) {
                        row.forEachIndexed { y, _ ->
                            val cell = Pair(x, y)
                            val snakeSize = uiState.snake.size
                            val headPosition = uiState.snake.first()

                            val minAlpha = 0.3f
                            val alpha = maxOf((snakeSize - uiState.snake.indexOf(cell)).toFloat() / snakeSize, minAlpha)



                            val color = when {
                                Pair(x, y) == headPosition -> Color.Black
                                uiState.snake.contains(Pair(x, y)) -> Color.Black.copy(alpha = alpha)
                                uiState.apple == cell -> Color(0xFF4287f5) // TODO: move to colors file
                                else -> Color.White
                            }
                            Box(
                                modifier = Modifier
                                    .size(squareSize)
                                    //.shadow(6.dp, RoundedCornerShape(2.dp))
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(color)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DirectionButtons(viewModel: SnakeViewModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(-8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxHeight()
    ) {


        Image(
            painter = painterResource(id = R.drawable.ic_up),
            contentDescription = "Up",
            modifier = Modifier
                .size(70.dp)
                .clickable {
                    viewModel.onDirectionChanged(Direction.UP)
                }
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(-8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_left),
                contentDescription = "Left",
                modifier = Modifier
                    .size(70.dp)
                    .clickable {
                        viewModel.onDirectionChanged(Direction.LEFT)
                    }
            )
            Image(
                painter = painterResource(id = if (viewModel.uiState.value.isPaused) R.drawable.ic_play else R.drawable.ic_pause),
                contentDescription = "Play/Pause",
                modifier = Modifier
                    .size(70.dp)
                    .clickable {
                        viewModel.togglePause()
                    }
            )
            Image(
                painter = painterResource(id = R.drawable.ic_right),
                contentDescription = "Right",
                modifier = Modifier
                    .size(70.dp)
                    .clickable {
                        viewModel.onDirectionChanged(Direction.RIGHT)
                    }
            )
        }

        Image(
            painter = painterResource(id = R.drawable.ic_down),
            contentDescription = "Down",
            modifier = Modifier
                .size(70.dp)
                .clickable {
                    viewModel.onDirectionChanged(Direction.DOWN)
                }
        )
    }

}

@Composable
fun GameOver(score: Int, highScore: Int, onRetryClicked: () -> Unit) {
    val text = if (score == highScore) "New High Score: $score" else "Score: $score"
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Game Over!", fontSize = 40.sp, fontWeight = FontWeight.Black)
        Text(text, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onRetryClicked() },
            modifier = Modifier.padding(8.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(Color.Black)
        ) {
            Text("Retry", color = Color.White)
        }
    }
}