package snake.ui

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    fun isOpposite(other: Direction): Boolean {
        return when (this) {
            UP -> other == DOWN
            DOWN -> other == UP
            LEFT -> other == RIGHT
            RIGHT -> other == LEFT
        }
    }
}