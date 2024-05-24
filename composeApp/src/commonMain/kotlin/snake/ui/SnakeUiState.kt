package snake.ui


data class SnakeUiState(
    val grid: MutableList<MutableList<Boolean>> = MutableList(SnakeViewModel.GRID_SIZE) { MutableList(
        SnakeViewModel.GRID_SIZE
    ) { false } },
    val snake: MutableList<Pair<Int, Int>> = mutableListOf(Pair(7,6) , Pair(7, 7)),
    val apple: Pair<Int, Int> = Pair(4, 4),
    val score: Int = 0,
    val highScore: Int = 0,
    val isFinished: Boolean = false,
    val isPaused: Boolean = false,
    val direction: Direction = Direction.RIGHT
)