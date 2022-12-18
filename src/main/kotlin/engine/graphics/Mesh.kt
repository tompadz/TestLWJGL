package engine.graphics

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import org.lwjgl.system.MemoryUtil

class Mesh(
    val vertex: Array<Vertex>,
    val indices : IntArray
) {

    private var _vertexArraysObjects = 0
    private var _positionBufferObjects = 0
    private var _indicesBufferObject = 0

    val vertexArraysObjects get() = _vertexArraysObjects
    val positionBufferObjects get() = _positionBufferObjects
    val indicesBufferObject get() = _indicesBufferObject

    fun create() : Mesh {

        _vertexArraysObjects = GL30.glGenVertexArrays()
        GL30.glBindVertexArray(_vertexArraysObjects)

        val positionsBuffer = MemoryUtil.memAllocFloat(vertex.size * 3) //3 because in position 3 values
        val positionData = FloatArray(vertex.size * 3)

        vertex.forEachIndexed { index, _ ->
            positionData[index * 3] = vertex[index].position.x
            positionData[index * 3 + 1] = vertex[index].position.y
            positionData[index * 3 + 2] = vertex[index].position.z
        }
        positionsBuffer.put(positionData).flip()

        _positionBufferObjects = GL15.glGenBuffers()

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, positionBufferObjects)
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, positionsBuffer, GL15.GL_STATIC_DRAW)
        GL20.glVertexAttribPointer(0, 3, GL11.GL_UNSIGNED_INT, false, 0, 0)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)

        val indicesBuffer = MemoryUtil.memAllocInt(indices.size )
        indicesBuffer.put(indices).flip()

        _indicesBufferObject = GL15.glGenBuffers()

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, _indicesBufferObject)
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0)


        return this
    }
}