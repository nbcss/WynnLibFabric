package io.github.nbcss.wynnlib.registry

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.builder.AbilityBuild

object AbilityBuildStorage: SavingStorage<AbilityBuild>() {
    private const val PATH = "config/WynnLib/AbilityBuilds.json"

    override fun read(data: JsonObject): AbilityBuild? {
        return AbilityBuild.fromData(data)
    }

    override fun getKey(): String = "ABILITY_BUILD"

    override fun getData(item: AbilityBuild): JsonObject {
        return item.getData()
    }

    override fun getSavePath(): String = PATH
}