package com.example.gamepadtest

import com.example.gamepadtest.gamepad.GamePadKeyCode
import com.example.gamepadtest.gamepad.GamepadManager
import com.example.gamepadtest.gamepad.GamepadManagerListener
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class GamePadTestApplication : Application(), GamepadManagerListener {
    private var onFrameTimer: AnimationTimer? = null
    private val gamePadManager: GamepadManager = GamepadManager(this)
    companion object {
        lateinit var fieldController: FieldController
    }

    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(GamePadTestApplication::class.java.getResource("field.fxml"))
        val scene = Scene(fxmlLoader.load(), 320.0, 240.0)
        stage.title = "GamePad test"
        stage.scene = scene
        stage.show()

        // Set the initial state of the field
        fieldController.initialize()

        onFrameTimer = object : AnimationTimer() {
            override fun handle(now: Long) {
                fieldController.onFrame()
                gamePadManager.update() // Update the incoming parameters of connected controllers
            }
        }
        onFrameTimer?.start()
    }

    /**
     * Receive an event from the gamepad manager and give it to the controller
     */
    override fun gamePadButtonEvent(keyCode: GamePadKeyCode, pressed: Boolean, padId: Int) {
        fieldController.gamePadButtonEvent(keyCode, pressed, padId)
    }

    override fun gamePadStickEvent(x: Float, y: Float, padId: Int) {
        fieldController.gamePadStickEvent(x, y, padId)
    }

    override fun stop() {
        super.stop()
        onFrameTimer?.stop()
        gamePadManager.destroy()
    }
}

