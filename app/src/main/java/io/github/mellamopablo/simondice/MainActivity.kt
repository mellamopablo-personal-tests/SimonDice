package io.github.mellamopablo.simondice

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import io.github.mellamopablo.simondice.extensions.clicks
import io.github.mellamopablo.simondice.extensions.flash
import io.reactivex.Observable

class MainActivity : Activity() {

	private lateinit var greenButton: Button
	private lateinit var redButton: Button
	private lateinit var yellowButton: Button
	private lateinit var blueButton: Button

	private lateinit var buttonClicks: Observable<GameButton>

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		setupViews()
		setupEvents()

		greenButton.clicks.subscribe { greenButton.flash(300) }
		redButton.clicks.subscribe { redButton.flash(300) }
		yellowButton.clicks.subscribe { yellowButton.flash(300) }
		blueButton.clicks.subscribe { blueButton.flash(300) }
	}

	private fun setupViews() {
		greenButton = findViewById(R.id.greenButton)
		redButton = findViewById(R.id.redButton)
		yellowButton = findViewById(R.id.yellowButton)
		blueButton = findViewById(R.id.blueButton)
	}

	private fun setupEvents() {
		buttonClicks = Observable.concat(
			greenButton.clicks.map { GameButton(GameButtonColour.GREEN) },
			redButton.clicks.map { GameButton(GameButtonColour.RED) },
			yellowButton.clicks.map { GameButton(GameButtonColour.RED) },
			blueButton.clicks.map { GameButton(GameButtonColour.RED) }
		)
	}

}
