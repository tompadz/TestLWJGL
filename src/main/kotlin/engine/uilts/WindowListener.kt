package engine.uilts

interface WindowListener {

    fun onUpdate() {}
    fun onSwapBuffer() {}
    fun onDestroy() {}
    fun onWindowSizeChange(width:Int, height:Int) {}
    fun onWindowPoseChange(x:Int, y:Int)
}