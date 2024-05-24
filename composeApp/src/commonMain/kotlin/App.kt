import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import snake.theme.SnakeTypography
import snake.ui.SnakeScreen

@Composable
@Preview
fun App() {
    MaterialTheme(typography = SnakeTypography()) {
        SnakeScreen()
    }
}