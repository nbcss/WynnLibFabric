package io.github.nbcss.wynnlib.items.equipments

import io.github.nbcss.wynnlib.data.MajorId
import net.minecraft.text.Text

data class MajorIdContainer(val majorId: MajorId,
                            val tooltip: List<Text>){
    interface Holder {
        fun getMajorIdContainers(): List<MajorIdContainer>
    }
}