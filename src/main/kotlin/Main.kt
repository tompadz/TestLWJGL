import engine.graphics.*
import engine.io.Window
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL

private const val WINDOW_TITLE = "MainWindow"

class Main  {

    private lateinit var shader: Shader
    private lateinit var renderer: Renderer
    private lateinit var window: Window

    private var mesh = Mesh(
        vertex = arrayOf(
            Vertex(
                Vector3f(-0.5f, 0.5f, 0f),
                Vector3f(1.0f, 0.0f, 0.0f)
            ),
            Vertex(
                Vector3f(-0.5f, -0.5f, 0f),
                Vector3f(0.0f, 1.0f, 0.0f)
            ),
            Vertex(
                Vector3f(0.5f, -0.5f, 0f),
                Vector3f(1.0f, 0.0f, 0.0f)
            ),
            Vertex(
                Vector3f(0.5f, 0.5f, 0f),
                Vector3f(1.0f, 1.0f, 0.0f)
            ),
        ),
        indices = intArrayOf(
            0, 1, 2,
            0, 3, 2
        )
    )

    private fun init() {
        println("initialize application")
        shader = Shader(
            vertexFile = "/Users/jj/Documents/_intelliJ/TestLWJGL/src/main/resources/shaders/mainVertex.glsl",
            fragmentFile =  "/Users/jj/Documents/_intelliJ/TestLWJGL/src/main/resources/shaders/mainFragment.glsl"
        )
        renderer = Renderer(shader)
        window = Window(title = WINDOW_TITLE).create()
        mesh.create()
        shader.create()
    }

    fun run() {
        init()
        loop()
        destroy()
    }

    private fun destroy() {
        window.destroy()
        mesh.destroy()
        shader.destroy()
    }

    private fun loop() {

        /**
         * This line is critical for LWJGL's interoperation with GLFW's
         * OpenGL context, or any context that is managed externally.
         * LWJGL detects the context that is current in the current thread,
         * creates the GLCapabilities instance and makes the OpenGL
         * bindings available for use.
         */

        GL.createCapabilities()

        /**
         * Run the rendering loop until the user has attempted to close
         * the window or has pressed the ESCAPE key.
         */
        while (!GLFW.glfwWindowShouldClose(window.get())) {
            window.update()
            renderer.renderMesh(mesh)
            window.swapBuffers()
            if (window.input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
                return
            }
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Main().run()
        }
    }
}
