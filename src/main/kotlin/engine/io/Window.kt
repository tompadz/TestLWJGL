package engine.io

import engine.uilts.WindowListener
import org.joml.Vector3f
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import utils.SystemUtil

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

    private var window = 0L

    //frames
    private var createTime = 0.0
    private var nsFrames = 0
    var frames = 0
        private set

    private var background = Vector3f(0f, 0f, 0f)
    private val input: Input = Input()
    private var windowListener : WindowListener? = null

    fun create() {

        // Set up an error callback. The default implementation
        GLFWErrorCallback.createPrint(System.err).set()

        //init
        check(glfwInit()) { "Unable to initialize GLFW" }
        setWindowHints()

        // Create the window
        window = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL)
        if (window == MemoryUtil.NULL) throw RuntimeException("Failed to create the GLFW window")

        setWindowSizeAndPos()
        setContextAndSettingOfWindow()
        initCallbacks()

        glfwShowWindow(window) //show window

        createTime = glfwGetTime()
    }

    fun get(): Long = window

    fun changeTitle(title: String) {
        glfwSetWindowTitle(window, title)
    }

    fun setBackground(background : Vector3f) {
        this.background = background
    }

    fun addWindowListener(listener: WindowListener) {
        windowListener = listener
    }

    /**
     * Poll for window events. The key callback above will only be
     * invoked during this call.
     */
    fun update() {
        changeBackground(background)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)  // clear the framebuffer
        glfwPollEvents()
        calculateFPS()
        showFpsInTitle()
        windowListener?.onUpdate()
    }

    fun swapBuffers() {
        glfwSwapBuffers(window) // swap the color buffers
        windowListener?.onSwapBuffer()
    }

    fun destroy() {
        windowListener?.onDestroy()
        // Free the window callbacks and destroy the window
        Callbacks.glfwFreeCallbacks(window)
        glfwDestroyWindow(window)
        // Terminate GLFW and free the error callback
        glfwTerminate()
        glfwSetErrorCallback(null)!!.free()
    }

    private fun initCallbacks() {
        glfwSetKeyCallback(window, input.keyboardCallback)
        glfwSetMouseButtonCallback(window, input.mouseButtonCallback)
        glfwSetCursorPosCallback(window, input.cursorPositionCallback)
        glfwSetScrollCallback(window, input.scrollCallback)
    }

    private fun setWindowHints() {

        glfwDefaultWindowHints() // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable

        if (SystemUtil().getOS() == SystemUtil.OS.MAC) {
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE) // According to Apple docs, non-core profiles are limited to version 2.1.
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE) // Should be true for macOS, according to GLFW docs, to get core profile.
        }
        else if (SystemUtil().getOS() == SystemUtil.OS.LINUX) {
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
        }
    }

    private fun setWindowSizeAndPos() {
        MemoryStack.stackPush().use { stack ->
            val pWidth = stack.mallocInt(1)
            val pHeight = stack.mallocInt(1)
            glfwGetWindowSize(window, pWidth, pHeight) // Get the window size passed to glfwCreateWindow
            val monitor = glfwGetPrimaryMonitor()
            val videoMode = glfwGetVideoMode(monitor) ?: throw RuntimeException("Failed to create video mode")
            val xPos = (videoMode.width() - pWidth[0]) / 2
            val yPos = (videoMode.height() - pHeight[0]) / 2
            glfwSetWindowPos(window, xPos, yPos) // Center the window
        }
    }

    private fun setContextAndSettingOfWindow() {
        glfwMakeContextCurrent(window) // Make the OpenGL context current
        GL.createCapabilities()
        glEnable(GL_DEPTH_TEST)
        glfwSwapInterval(1) // Enable v-sync
    }

    private fun calculateFPS() {
        val currentTime = glfwGetTime()
        nsFrames++
        if (currentTime - createTime >= 1) {
            frames = nsFrames
            nsFrames = 0
            createTime += 1
        }
    }

    private fun showFpsInTitle() {
        if (!showFpsInTitle) return
        val title = "$title | $frames FPS"
        changeTitle(title)
    }


    private fun changeBackground(vector3f: Vector3f) {
        glClearColor(
            vector3f.x,
            vector3f.y,
            vector3f.z,
            1f
        )
    }
}