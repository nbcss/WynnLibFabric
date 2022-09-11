package io.github.nbcss.wynnlib.registry

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.github.nbcss.wynnlib.utils.FileUtils
import io.github.nbcss.wynnlib.utils.Keyed
import io.github.nbcss.wynnlib.utils.Version
import java.io.InputStreamReader

abstract class Registry<T: Keyed>: Storage<T>() {
    private var version: Version? = null

    protected open fun getFilename(): String? = null

    override fun load() {
        getFilename()?.let {
            val reader = InputStreamReader(FileUtils.getResource(it)!!, "utf-8")
            reload(JsonParser.parseReader(reader).asJsonObject)
        }
    }

    override fun reload(json: JsonObject){
        val ver = Version(json["version"].asString)
        //skip reload if currently have newer version
        if(version != null && version!! > ver) return
        super.reload(json)
        version = ver
    }
}