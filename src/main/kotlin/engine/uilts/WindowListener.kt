package engine.uilts

interface WindowListener {

    fun onUpdate() {}
    fun onSwapBuffer() {}
    fun onDestroy()
}