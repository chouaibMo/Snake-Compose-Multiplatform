package snake.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.painterResource
import snake.composeapp.generated.resources.Res
import snake.composeapp.generated.resources.ic_down
import snake.composeapp.generated.resources.ic_left
import snake.composeapp.generated.resources.ic_pause
import snake.composeapp.generated.resources.ic_play
import snake.composeapp.generated.resources.ic_right
import snake.composeapp.generated.resources.ic_up
import snake.theme.Green

@Composable
fun SnakeScreen(viewModel: SnakeViewModel = viewModel { SnakeViewModel() }) {

    val uiState = viewModel.uiState.value

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 60.dp, bottom = 30.dp)
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
    val gridSize = uiState.grid.size

    LazyVerticalGrid(
        columns = GridCells.Fixed(gridSize),
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(4.dp, Color.Black, RoundedCornerShape(4.dp))
    ) {
        items(uiState.grid.flatten().size) { index ->
            val row = index / gridSize
            val column = index % gridSize
            val cell = Pair(row, column)

            val snakeSize = uiState.snake.size
            val headPosition = uiState.snake.first()

            val minAlphaValue = 0.4f
            val currentAlphaValue = (snakeSize - uiState.snake.indexOf(cell)).toFloat() / snakeSize
            val alpha = maxOf(currentAlphaValue, minAlphaValue)

            val color = when {
                cell == headPosition -> Color.Black
                uiState.snake.contains(cell) -> Color.Black.copy(alpha = alpha)
                uiState.apple == cell -> Green
                else -> Color.White
            }
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
            )
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
            painter = painterResource(Res.drawable.ic_up),
            contentDescription = "Up",
            modifier = Modifier
                .size(70.dp)
                .clickable { viewModel.onDirectionChanged(Direction.UP) }
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(-8.dp)
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_left),
                contentDescription = "Left",
                modifier = Modifier
                    .size(70.dp)
                    .clickable { viewModel.onDirectionChanged(Direction.LEFT) }
            )
            Image(
                painter = painterResource(if (viewModel.uiState.value.isPaused) Res.drawable.ic_play else Res.drawable.ic_pause),
                contentDescription = "Play/Pause",
                modifier = Modifier
                    .size(70.dp)
                    .clickable {
                        viewModel.togglePause()
                    }
            )
            Image(
                painter = painterResource(Res.drawable.ic_right),
                contentDescription = "Right",
                modifier = Modifier
                    .size(70.dp)
                    .clickable {
                        viewModel.onDirectionChanged(Direction.RIGHT)
                    }
            )
        }

        Image(
            painter = painterResource(Res.drawable.ic_down),
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