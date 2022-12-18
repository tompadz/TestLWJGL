package engine.graphics

import engine.uilts.FileUtil
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20

class Shader(
    var vertexFile : String,
    var fragmentFile : String,
) {

    private var programID = 0
    private var vertexID = 0
    private var fragmentID = 0

    init {
        vertexFile = FileUtil().loadAsString(vertexFile)
        fragmentFile = FileUtil().loadAsString(fragmentFile)

    }

    fun create() {

        programID = GL20.glCreateProgram()

        //vertex

        vertexID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER)

        GL20.glShaderSource(vertexID, vertexFile)
        GL20.glCompileShader(vertexID)

        val isVertexCompile = GL20.glGetShaderi(vertexID, GL20.GL_COMPILE_STATUS) == GL11.GL_TRUE
        if (!isVertexCompile) {
            System.err.println("vertex shader ${GL20.glGetShaderInfoLog(vertexID)}")
            return
        }

        //fragment

        fragmentID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER)

        GL20.glShaderSource(fragmentID, fragmentFile)
        GL20.glCompileShader(fragmentID)

        val isFragmentCompile = GL20.glGetShaderi(fragmentID, GL20.GL_COMPILE_STATUS) == GL11.GL_TRUE
        if (!isFragmentCompile) {
            System.err.println("fragment shader ${GL20.glGetShaderInfoLog(fragmentID)}")
            return
        }

        GL20.glAttachShader(programID, vertexID)
        GL20.glAttachShader(programID, fragmentID)

        GL20.glLinkProgram(programID)
        val isProgramCompile = GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == GL11.GL_TRUE
        if (!isProgramCompile) {
            System.err.println("program linking ${GL20.glGetProgramInfoLog(fragmentID)}")
            return
        }

        GL20.glValidateProgram(programID)
        val isValidateCompile = GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == GL11.GL_TRUE
        if (!isValidateCompile) {
            System.err.println("program validate ${GL20.glGetProgramInfoLog(programID)}")
            return
        }

        GL20.glDetachShader(programID, vertexID)
        GL20.glDetachShader(programID, fragmentID)
    }

    fun bind() {
        GL20.glUseProgram(programID)
    }

    fun unbind() {
        GL20.glUseProgram(0)
    }

    fun destroy() {
        GL20.glDeleteProgram(programID)
    }
}