package com.example.gamepadtest.gamepad

/**
 * Interface for the class that will listen to the GamepadManager
 */
interface GamepadManagerListener {
    fun gamePadButtonEvent(keyCode: GamePadKeyCode, pressed: Boolean, padId: Int)
    fun gamePadStickEvent(x: Float, y: Float, padId: Int)
}