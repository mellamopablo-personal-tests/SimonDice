package io.github.mellamopablo.simondice

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.GridLayout
import io.github.mellamopablo.simondice.extensions.clicks
import io.github.mellamopablo.simondice.extensions.flash
import io.github.mellamopablo.simondice.extensions.snackbar
import io.github.mellamopablo.simondice.support.randomGameButton
import io.github.mellamopablo.simondice.support.setTimeout
import io.reactivex.Observable

class MainActivity : AppCompatActivity() {

	private lateinit var root: GridLayout

	private lateinit var greenButton: Button
	private lateinit var redButton: Button
	private lateinit var yellowButton: Button
	private lateinit var blueButton: Button

	private lateinit var buttonClicks: Observable<GameButton>

	private var sequence = mutableListOf<GameButton>()
	private var currentIndex = 0
	private var sequenceIsPlaying = false

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		setupViews()
		setupEvents()

		setTimeout(500) {
			Log.i("INFO", "El juego comienza")
			generateNextSequence()
		}
	}

	private fun setupViews() {
		root = findViewById(R.id.root)

		greenButton = findViewById(R.id.greenButton)
		redButton = findViewById(R.id.redButton)
		yellowButton = findViewById(R.id.yellowButton)
		blueButton = findViewById(R.id.blueButton)
	}

	private fun setupEvents() {
		buttonClicks = Observable.merge(
			greenButton.clicks.map { GameButton.GREEN },
			redButton.clicks.map { GameButton.RED },
			yellowButton.clicks.map { GameButton.YELLOW },
			blueButton.clicks.map { GameButton.BLUE }
		)

		buttonClicks.subscribe(this::handleUserInput)
	}

	private fun handleUserInput(clickedColour: GameButton) {
		if (sequenceIsPlaying) {
			// Descartamos clicks del usuario mientras se reproduce la secuencia
			return
		}

		Log.i("INFO", "El usuario ha pulsado $clickedColour")
		flashButton(clickedColour, 300)

		if (sequence.isEmpty()) {
			// El juego no ha empezado todavía
			return
		}

		val correctColour = sequence[currentIndex]

		if (clickedColour == correctColour) {
			if (currentIndex == sequence.size - 1) {
				handleCorrectSequence()
			} else {
				currentIndex++
			}
		} else {
			handleIncorrectSequence()
		}
	}

	private fun handleCorrectSequence() {
		Log.i("INFO", "El usuario ha acertado")

		setTimeout(500) {
			generateNextSequence()
		}
	}

	private fun handleIncorrectSequence() {
		val score = sequence.size

		Log.i("INFO", "El usuario ha fallado. Puntuación final: $score")

		sequence = mutableListOf()
		currentIndex = 0

		root.snackbar("¡Has perdido! Puntuación final: $score") {
			Log.i("INFO", "El juego comienza de nuevo")
			generateNextSequence()
		}
	}

	private fun generateNextSequence() {
		sequence.add(randomGameButton())
		currentIndex = 0

		Log.i("INFO", "Se ha generado una nueva secuencia: $sequence")

		playSequence()
	}

	private fun playSequence() {
		sequenceIsPlaying = true

		if (currentIndex < sequence.size) {
			val duration = Math.max(
				(600 - (sequence.size * 25)).toLong(),
				100
			)
			flashButton(sequence[currentIndex], duration)
			setTimeout(duration + 100) {
				currentIndex++
				playSequence()
			}
		} else {
			currentIndex = 0
			sequenceIsPlaying = false
		}
	}

	private fun flashButton(colour: GameButton, duration: Long) = when (colour) {
		GameButton.GREEN -> greenButton.flash(duration)
		GameButton.RED -> redButton.flash(duration)
		GameButton.YELLOW -> yellowButton.flash(duration)
		GameButton.BLUE -> blueButton.flash(duration)
	}

}
