import engine.graphics.*
import engine.io.Window
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL

private const val WINDOW_TITLE = "MainWindow"

class Main  {

    private lateinit var window: Window
    private lateinit var renderer: Renderer
    private lateinit var shader: Shader

    private val  mesh = Mesh(
        arrayOf(
            Vertex(Vector3f(-0.5f, 0.5f, 0.0f), Vector3f(1.0f, 0.0f, 0.0f)),
            Vertex(Vector3f(-0.5f, -0.5f, 0.0f), Vector3f(0.0f, 1.0f, 0.0f)),
            Vertex(Vector3f(0.5f, -0.5f, 0.0f), Vector3f(0.0f, 0.0f, 1.0f)),
            Vertex(Vector3f(0.5f, 0.5f, 0.0f), Vector3f(1.0f, 1.0f, 0.0f))
        ), intArrayOf(
            0, 1, 2,
            0, 3, 2
        )
    )

    private fun init() {
        window = Window(title = WINDOW_TITLE)
        shader = Shader(
            vertexPath = "/Users/jj/Documents/_intelliJ/TestLWJGL/src/main/resources/shaders/mainVertex.glsl",
            fragmentPath =  "/Users/jj/Documents/_intelliJ/TestLWJGL/src/main/resources/shaders/mainFragment.glsl"
        )
        renderer = Renderer(shader)
        window.create()
        mesh.create()
        shader.create()
    }

    fun run() {
        init()
        loop()
        destroy()
    }

    private fun loop() {
        GL.createCapabilities()
        while (!GLFW.glfwWindowShouldClose(window.get())) {
            update()
            render()
        }
    }

    private fun update() {
        window.update()
    }

    private fun render() {
        renderer.renderMesh(mesh)
        window.swapBuffers()
    }

    private fun destroy() {
        window.destroy()
        mesh.destroy()
        shader.destroy()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Main().run()
        }
    }
}
