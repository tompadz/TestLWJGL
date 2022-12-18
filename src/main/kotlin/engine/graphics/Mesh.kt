package engine.graphics

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer

class Mesh(
    val vertices: Array<Vertex>,
    val indices : IntArray
) {
    var vAO = 0
        private set
    var pBO = 0
        private set
    var iBO = 0
        private set
    var cBO = 0
        private set

    fun create() {

        vAO = GL30.glGenVertexArrays()
        GL30.glBindVertexArray(vAO)

        val positionBuffer = MemoryUtil.memAllocFloat(vertices.size * 3) //3 because in position 3 values
        val positionData = FloatArray(vertices.size * 3)

        val colorBuffer = MemoryUtil.memAllocFloat(vertices.size * 3)
        val colorData = FloatArray(vertices.size * 3)

        for (i in vertices.indices) {
            positionData[i * 3] = vertices[i].position.x
            positionData[i * 3 + 1] = vertices[i].position.y
            positionData[i * 3 + 2] = vertices[i].position.z

            colorData[i * 3] = vertices[i].color.x
            colorData[i * 3 + 1] = vertices[i].color.y
            colorData[i * 3 + 2] = vertices[i].color.z
        }

        positionBuffer.put(positionData).flip()
        pBO = storeData(positionBuffer, 0, 3)

        colorBuffer.put(colorData).flip()
        cBO = storeData(colorBuffer, 1, 3)

        val indicesBuffer = MemoryUtil.memAllocInt(indices.size)
        indicesBuffer.put(indices).flip()

        iBO = GL15.glGenBuffers()

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iBO)
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0)
    }

    private fun storeData(buffer: FloatBuffer, index: Int, size: Int): Int {
        val bufferID = GL15.glGenBuffers()
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferID)
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW)
        GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, 0, 0)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
        return bufferID
    }

    fun destroy() {
        GL15.glDeleteBuffers(pBO)
        GL15.glDeleteBuffers(cBO)
        GL15.glDeleteBuffers(iBO)
        GL30.glDeleteVertexArrays(vAO)
    }

}