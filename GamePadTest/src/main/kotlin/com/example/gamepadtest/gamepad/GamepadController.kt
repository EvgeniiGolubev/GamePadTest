package com.example.gamepadtest.gamepad

import net.java.games.input.Component
import net.java.games.input.Controller

/**
 * Controller handler. With this class, we get the state of the controller.
 * Button pressed or released, stick position, etc.
 */
class GamepadController(private val controller: Controller, private var listener: Listener) {
    private var crossLeft = false
    private var crossRight = false
    private var crossUp = false
    private var crossDown = false
    private var xAxis = 0f
    private var yAxis = 0f
    private var buttonX = 0f
    private var buttonA = 0f
    private var buttonY = 0f
    private var buttonB = 0f
    private var buttonBack = 0f
    private var buttonStart = 0f


    fun update() {
        controller.poll()
        val components = controller.components

        for (component in components) {
            //X BUTTON
            if (component.identifier == Component.Identifier.Button._2) {
                val data = component.pollData
                if (buttonX != data) {
                    listener.onGamePadButton(GamePadKeyCode.X, data > 0)
                    buttonX = data
                }
                continue
            }

            //A BUTTON
            if (component.identifier == Component.Identifier.Button._0) {
                val data = component.pollData
                if (buttonA != data) {
                    listener.onGamePadButton(GamePadKeyCode.A, data > 0)
                    buttonA = data
                }
                continue
            }

            //Y BUTTON
            if (component.identifier == Component.Identifier.Button._3) {
                val data = component.pollData
                if (buttonY != data) {
                    listener.onGamePadButton(GamePadKeyCode.Y, data > 0)
                    buttonY = data
                }
                continue
            }

            //B BUTTON
            if (component.identifier == Component.Identifier.Button._1) {
                val data = component.pollData
                if (buttonB != data) {
                    listener.onGamePadButton(GamePadKeyCode.B, data > 0)
                    buttonB = data
                }
                continue
            }

            //BACK BUTTON
            if (component.identifier == Component.Identifier.Button._6) {
                val data = component.pollData
                if (buttonBack != data) {
                    listener.onGamePadButton(GamePadKeyCode.SELECT, data > 0)
                    buttonBack = data
                }
                continue
            }

            //START BUTTON
            if (component.identifier == Component.Identifier.Button._7) {
                val data = component.pollData
                if (buttonStart != data) {
                    listener.onGamePadButton(GamePadKeyCode.START, data > 0)
                    buttonStart = data
                }
                continue
            }

            // CROSS
            if (component.identifier == Component.Identifier.Axis.POV) {
                handleCross(component.pollData)
                continue
            }

            // X AXIS
            if (component.identifier == Component.Identifier.Axis.X) {
                val data = component.pollData
                val x = when (data) {
                    -1.5258789E-5f -> 0f
                    else -> data
                }

                if (x != xAxis) {
                    listener.onGamePadStick(x, yAxis)
                    xAxis = x
                }
                continue
            }

            // Y AXIS
            if (component.identifier == Component.Identifier.Axis.Y) {
                val data = component.pollData
                val y = when (data) {
                    -4.5776367E-5f -> 0f
                    else -> data
                }
                if (y != yAxis) {
                    listener.onGamePadStick(xAxis, y)
                    yAxis = y
                }
                continue
            }
        }
    }

    /**
     * CROSS
     * left top = 0.125
     * right top = 0.375
     * right bottom = 0.625
     * left bottom = 0.875
     * left = 1.0
     * top = 0.25
     * right = 0.5
     * bottom = 0.75
     */
    private fun handleCross(data: Float) {
        val x: Int = when (data) {
            0.125f, 0.875f, 1.0f -> -1
            0.375f, 0.625f, 0.5f -> 1
            else -> 0
        }
        val y: Int = when (data) {
            0.25f, 0.125f, 0.375f -> -1
            0.75f, 0.625f, 0.875f -> 1
            else -> 0
        }
        val left = x == -1
        val right = x == 1
        val up = y == -1
        val down = y == 1

        if (left != crossLeft) {
            listener.onGamePadButton(GamePadKeyCode.CROSS_LEFT, left)
            crossLeft = left
        }

        if (right != crossRight) {
            listener.onGamePadButton(GamePadKeyCode.CROSS_RIGHT, right)
            crossRight = right
        }

        if (up != crossUp) {
            listener.onGamePadButton(GamePadKeyCode.CROSS_UP, up)
            crossUp = up
        }

        if (down != crossDown) {
            listener.onGamePadButton(GamePadKeyCode.CROSS_DOWN, down)
            crossDown = down
        }
    }

    fun available(): Boolean {
        return controller.poll()
    }

    override fun toString(): String = controller.name

    /**
     *Interface for GamepadManager, using it GamepadManager gets the current state of the controller
     */
    interface Listener {
        fun onGamePadButton(keyCode: GamePadKeyCode, pressed: Boolean)
        fun onGamePadStick(x: Float, y: Float)
    }
}