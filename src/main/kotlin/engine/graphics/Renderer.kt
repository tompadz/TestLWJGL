package engine.graphics

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL30

class Renderer(
    private val shader: Shader
) {

    fun renderMesh(mesh: Mesh) {

        GL30.glBindVertexArray(mesh.vertexArraysObjects)
        GL30.glEnableVertexAttribArray(0)
        GL30.glEnableVertexAttribArray(1)

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.indicesBufferObject)

        shader.bind()
        GL11.glDrawElements(
            GL11.GL_TRIANGLES,
            mesh.indices.size,
            GL11.GL_UNSIGNED_INT,
            0
        )
        shader.unbind()

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0)
        GL30.glDisableVertexAttribArray(0)
        GL30.glDisableVertexAttribArray(1)
        GL30.glBindVertexArray(0)
    }
}