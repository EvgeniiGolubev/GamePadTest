package com.example.gamepadtest

import com.example.gamepadtest.gamepad.GamePadKeyCode
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color

/**
 * In this class the figure is drawn on the field
 */
class FieldController {
    @FXML private lateinit var figure: Canvas
    private var translateX = 0.0
    private var translateY = 0.0

    init {
        GamePadTestApplication.fieldController = this
    }

    fun initialize() {
        drawSquare()
    }

    /**
     * Drawing figure motion animation
     */
    fun onFrame() {
        figure.translateX = figure.translateX + translateX
        figure.translateY = figure.translateY + translateY
    }

    /**
     * Processing the button presses
     */
    fun gamePadButtonEvent(keyCode: GamePadKeyCode, pressed: Boolean, padId: Int) {
        if (pressed) {
            when (keyCode) {
                GamePadKeyCode.A -> drawX()
                GamePadKeyCode.X -> drawSquare()
                GamePadKeyCode.Y -> drawTriangle()
                GamePadKeyCode.B -> drawOval()
                else -> {}
            }
        }

        when(keyCode) {
            GamePadKeyCode.CROSS_DOWN -> translateY = if(pressed) 1.0 else 0.0
            GamePadKeyCode.CROSS_UP -> translateY = if(pressed) -1.0 else 0.0
            GamePadKeyCode.CROSS_LEFT -> translateX = if(pressed) -1.0 else 0.0
            GamePadKeyCode.CROSS_RIGHT -> translateX = if(pressed) 1.0 else 0.0
            else -> {}
        }
    }

    /**
     * Processing the position of the stick
     */
    fun gamePadStickEvent(x: Float, y: Float, padId: Int) {
        translateX = x.toDouble()
        translateY = y.toDouble()
    }

    private fun drawSquare() {
        clearFigure()
        val context = figure.graphicsContext2D
        context.fill = Color.BLUE
        context.fillRect(0.0, 0.0, 30.0, 30.0)
    }

    private fun drawOval() {
        clearFigure()
        val context = figure.graphicsContext2D
        context.fill = Color.RED
        context.fillOval(0.0, 0.0, 28.0, 28.0)
    }

    private fun drawX() {
        clearFigure()
        val context = figure.graphicsContext2D
        context.lineWidth = 5.0
        context.stroke = Color.GREEN
        context.strokeLine(0.0, 0.0, 30.0, 30.0)
        context.strokeLine(0.0, 30.0, 30.0, 0.0)
    }

    private fun drawTriangle() {
        clearFigure()
        val context = figure.graphicsContext2D
        context.fill = Color.ORANGE
        context.fillPolygon(doubleArrayOf(15.0, 0.0, 30.0), doubleArrayOf(0.0, 30.0, 30.0), 3)
    }

    private fun clearFigure() {
        figure.graphicsContext2D.clearRect(0.0, 0.0, figure.width, figure.height)
    }
}