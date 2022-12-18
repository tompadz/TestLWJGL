package engine.io

import org.lwjgl.glfw.*

class Input {

    lateinit var keyboardCallback : GLFWKeyCallback
    lateinit var cursorPositionCallback : GLFWCursorPosCallback
    lateinit var mouseButtonCallback : GLFWMouseButtonCallback
    lateinit var scrollCallback : GLFWScrollCallback

    private var keyboardKeys = BooleanArray(GLFW.GLFW_KEY_LAST)
    private var mouseButtons = BooleanArray(GLFW.GLFW_MOUSE_BUTTON_LAST)
    private var _cursorX = 0.0
    private var _cursorY = 0.0
    private var _scrollOffsetX = 0.0
    private var _scrollOffsetY = 0.0

    val cursorX get() = _cursorX
    val cursorY get() = _cursorY
    val scrollOffsetX get() = _scrollOffsetX
    val scrollOffsetY get() = _scrollOffsetY

    init {
        initKeyboardCallback()
        initCursorCallback()
        initMouseButtonCallback()
        initScrollCallback()
    }

    fun isKeyDown(key: Int) = keyboardKeys[key]
    fun isMouseButtonDown(button:Int) = mouseButtons[button]

    private fun initKeyboardCallback() {
        keyboardCallback = object : GLFWKeyCallback() {
            override fun invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
                keyboardKeys[key] = (action != GLFW.GLFW_RELEASE)
            }
        }
    }

    private fun initCursorCallback() {
        cursorPositionCallback = object : GLFWCursorPosCallback() {
            override fun invoke(window: Long, xpos: Double, ypos: Double) {
                _cursorX = xpos
                _cursorY = ypos
            }
        }
    }

    private fun initMouseButtonCallback() {
        mouseButtonCallback = object : GLFWMouseButtonCallback() {
            override fun invoke(window: Long, button: Int, action: Int, mods: Int) {
                mouseButtons[button] = (action != GLFW.GLFW_RELEASE)
            }
        }
    }

    private fun initScrollCallback() {
        scrollCallback = object : GLFWScrollCallback() {
            override fun invoke(window: Long, xoffset: Double, yoffset: Double) {
                _scrollOffsetX = xoffset
                _scrollOffsetY = xoffset
            }
        }
    }

}