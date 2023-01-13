package com.example.gamepadtest.gamepad

import kotlinx.coroutines.*
import net.java.games.input.Controller
import net.java.games.input.ControllerEnvironment
import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Level
import java.util.logging.Logger

/**
 * GamepadManager running in a separate thread
 */
class GamepadManager(private val listener: Listener) : CoroutineScope {
    override val coroutineContext = Dispatchers.IO
    private val gamepadControllers = ConcurrentHashMap<Int, GamepadController>() // all connected controllers
    private val updateAvailableJob: Job

    init {
        /* The jinput library displays a message if it cannot recognize the device,
           we remove it so that it does not spam the console */
        Logger.getLogger(ControllerEnvironment::class.java.getPackage().name).level = Level.OFF

        updateAvailableJob = launch {
            while (true) {
                updateAvailableControllers()
                delay(1000)
            }
        }
    }

    /**
     * Due to the fact that the jinput library does not notify us in any way about the connected controllers,
     * we have to independently poll it for the fact that a new device is connected
     */
    private fun updateAvailableControllers() {
        val environment = getDefaultEnvironment()
        val controllers = environment.controllers
        val gamepads = ArrayList<Controller>()
        for (controller in controllers) {
            if (isGamepad(controller))
                gamepads.add(controller)
        }

        if (gamepads.size > gamepadControllers.size)
            rebindGamepads(gamepads)
    }

    /**
     * In the jinput library, scanning the system for connected devices occurs once,
     * during the initialization of the DefaultControllerEnvironment (singleton) class.
     * Because of this, while the application is running, when the device is turned off, it will not work again.
     * To be able to connect and disconnect the controller at any time during the application,
     * you need to call the constructor of the DefaultControllerEnvironment class using reflection
     */
    private fun getDefaultEnvironment(): ControllerEnvironment {
        ControllerEnvironment.getDefaultEnvironment()
        val constructor = Class.forName("net.java.games.input.DefaultControllerEnvironment").declaredConstructors[0]
        constructor.isAccessible = true
        return constructor.newInstance() as ControllerEnvironment
    }

    /**
     * Checking the correct type of the controller
     */
    private fun isGamepad(controller: Controller): Boolean {
        return controller.type === Controller.Type.STICK
                || controller.type === Controller.Type.GAMEPAD
                || controller.type === Controller.Type.WHEEL
                || controller.type === Controller.Type.FINGERSTICK
    }

    /**
     * If the device is disconnected or connected, we rebind the device with its handler and assign it a specific id
     */
    private fun rebindGamepads(gamepads: ArrayList<Controller>) {
        gamepadControllers.clear()

        for ((id, gamepad) in gamepads.withIndex()) {
            val gamepadController = GamepadController(gamepad, object : GamepadController.Listener {
                override fun onGamePadButton(keyCode: GamePadKeyCode, pressed: Boolean) {
                    listener.gamePadButtonEvent(keyCode, pressed, id)
                }
                override fun onGamePadStick(x: Float, y: Float) {
                    listener.gamePadStickEvent(x, y, id)
                }
            })
            gamepadControllers[id] = gamepadController
        }

        onGamepadConnected()
    }

    /**
     * Get the current state of the controller, if the controller is not available, remove it from the listening list
     */
    fun update() {
        val ids = gamepadControllers.keys
        for (id in ids) {
            val controller = gamepadControllers[id] ?: continue

            if (!controller.available()) {
                gamepadControllers.remove(id)
                onGamepadDisconnected(controller)
                continue
            }

            controller.update()
        }
    }

    private fun onGamepadConnected() {
        println("Gamepad connected: ${gamepadControllers.values}")
    }

    private fun onGamepadDisconnected(controller: GamepadController) {
        println("Gamepad disconnected: $controller")
    }

    fun destroy() {
        updateAvailableJob.cancel()
    }

    /**
     * Interface for the class that will listen to the GamepadManager
     */
    interface Listener {
        fun gamePadButtonEvent(keyCode: GamePadKeyCode, pressed: Boolean, padId: Int)
        fun gamePadStickEvent(x: Float, y: Float, padId: Int)
    }
}