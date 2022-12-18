package engine.io

import org.joml.Vector3f
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil

private const val DEFAULT_WINDOW_WIDTH = 1280
private const val DEFAULT_WINDOW_HEIGHT = 720

//TODO add resize callback
//TODO add window position callback
class Window(
    val title: String,
    private val width: Int = DEFAULT_WINDOW_WIDTH,
    private val height: Int = DEFAULT_WINDOW_HEIGHT,
    private val showFpsInTitle: Boolean = true,
) {

    lateinit var input: Input

    private var window = 0L
    private var createTime = 0.0

    private var nsFrames = 0

    private var _frames = 0
    val frames get() = _frames

    private var background = Vector3f(0f, 0f, 0f)

    fun create(): Window {

        // Set up an error callback. The default implementation
        GLFWErrorCallback.createPrint(System.err).set()

        //init
        check(glfwInit()) { "Unable to initialize GLFW" }

        //options
        glfwDefaultWindowHints() // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE) // According to Apple docs, non-core profiles are limited to version 2.1.
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE) // Should be true for macOS, according to GLFW docs, to get core profile.

        // Create the window
        window = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL)
        if (window == MemoryUtil.NULL) throw RuntimeException("Failed to create the GLFW window")

        //create input
        input = Input()

        MemoryStack.stackPush().use { stack ->

            val pWidth = stack.mallocInt(1)
            val pHeight = stack.mallocInt(1)

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight)

            val monitor = glfwGetPrimaryMonitor()
            val videoMode = glfwGetVideoMode(monitor) ?: throw RuntimeException("Failed to create video mode")

            val xPos = (videoMode.width() - pWidth[0]) / 2
            val yPos = (videoMode.height() - pHeight[0]) / 2

            // Center the window
            glfwSetWindowPos(window, xPos, yPos)
        }

        glfwMakeContextCurrent(window) // Make the OpenGL context current
        GL.createCapabilities()
        glfwSwapInterval(1) // Enable v-sync

        //enable test state for gl
        glEnable(GL_DEPTH_TEST)

        //set input callback
        glfwSetKeyCallback(window, input.keyboardCallback)
        glfwSetMouseButtonCallback(window, input.mouseButtonCallback)
        glfwSetCursorPosCallback(window, input.cursorPositionCallback)
        glfwSetScrollCallback(window, input.scrollCallback)

        //show window
        glfwShowWindow(window)

        createTime = glfwGetTime()

        return this
    }

    fun changeTitle(title: String) {
        glfwSetWindowTitle(window, title)
    }


    fun setBackground(background : Vector3f) {
        this.background = background
    }

    /**
     * Poll for window events. The key callback above will only be
     * invoked during this call.
     */
    fun update() {
        changeBackground(background)
        glfwPollEvents()
        calculateFPS()
        showFpsInTitle()
    }

    fun swapBuffers() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)  // clear the framebuffer
        glfwSwapBuffers(window) // swap the color buffers
    }

    fun destroy() {
        // Free the window callbacks and destroy the window
        Callbacks.glfwFreeCallbacks(window)
        glfwDestroyWindow(window)
        // Terminate GLFW and free the error callback
        glfwTerminate()
        glfwSetErrorCallback(null)!!.free()
    }

    private fun calculateFPS() {
        val currentTime = glfwGetTime()
        nsFrames++
        if (currentTime - createTime >= 1) {
            _frames = nsFrames
            nsFrames = 0
            createTime += 1
        }
    }

    private fun showFpsInTitle() {
        if (!showFpsInTitle) return
        val title = "$title | $frames FPS"
        changeTitle(title)
    }

    fun get(): Long = window

    private fun changeBackground(vector3f: Vector3f) {
        glClearColor(
            vector3f.x,
            vector3f.y,
            vector3f.z,
            1f
        )
    }
}