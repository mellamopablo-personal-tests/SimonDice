package io.github.mellamopablo.simondice.support

import io.github.mellamopablo.simondice.GameButton
import io.github.mellamopablo.simondice.extensions.randomElement

fun randomGameButton() = listOf(
	GameButton.GREEN,
	GameButton.RED,
	GameButton.YELLOW,
	GameButton.BLUE
).randomElement