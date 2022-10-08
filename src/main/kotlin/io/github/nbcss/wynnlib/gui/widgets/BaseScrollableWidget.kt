package io.github.nbcss.wynnlib.gui.widgets

abstract class BaseScrollableWidget(private val posX: Int,
                                    private val posY: Int): ScrollElement {
    private var interactable: Boolean = true
    private var x: Int = -99999
    private var y: Int = -99999

    override fun updateState(x: Int, y: Int, active: Boolean) {
        this.x = posX + x
        this.y = posY + y
        this.interactable = active
    }

    fun getX(): Int = x
    fun getY(): Int = y
    fun isInteractable(): Boolean = interactable
}