package io.github.nbcss.wynnlib.data

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.registry.Registry
import io.github.nbcss.wynnlib.utils.Keyed

data class MajorId(val id: String,
                   val displayName: String): Keyed, Translatable {
    constructor(json: JsonObject) : this(
        json.get("name").asString,
        json.get("displayName").asString,
    )

    companion object: Registry<MajorId>() {
        private const val RESOURCE = "assets/wynnlib/data/MajorID.json"

        override fun getFilename(): String = RESOURCE

        override fun read(data: JsonObject): MajorId {
            return MajorId(data)
        }
    }

    override fun getKey(): String = id

    override fun getTranslationKey(label: String?): String {
        //todo description
        return "wynnlib.major_id.name.${id.lowercase()}"
    }
}