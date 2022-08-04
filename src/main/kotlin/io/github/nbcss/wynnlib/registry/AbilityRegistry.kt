package io.github.nbcss.wynnlib.registry

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.AbilityTree
import io.github.nbcss.wynnlib.abilities.properties.info.StatusReferenceProperty
import io.github.nbcss.wynnlib.data.CharacterClass
import java.util.*
import kotlin.collections.HashSet

object AbilityRegistry: Registry<Ability>() {
    private const val RESOURCE = "assets/wynnlib/data/Abilities.json"
    private val treeMap: MutableMap<CharacterClass, AbilityTree> = EnumMap(CharacterClass::class.java)
    private val nameMap: MutableMap<String, MutableSet<Ability>> = mutableMapOf()
    private val statusNameMap: MutableMap<String, Ability> = mutableMapOf()
    init {
        CharacterClass.values().forEach { treeMap[it] = AbilityTree(it) }
    }

    override fun getFilename(): String = RESOURCE

    override fun read(data: JsonObject): Ability? = try {
        Ability(data)
    }catch (e: Exception){
        e.printStackTrace()
        null
    }

    override fun put(item: Ability) {
        super.put(item)
        StatusReferenceProperty.from(item)?.let {
            statusNameMap[it.getFooterName()] = item
        }
        item.getName()?.let { name ->
            var items = nameMap[name]
            if (items == null){
                items = mutableSetOf()
                nameMap[name] = items
            }
            items.add(item)
        }
    }

    override fun reload(array: JsonArray){
        nameMap.clear()
        statusNameMap.clear()
        super.reload(array)
        //keep a cache map from character to all its ability
        val map: MutableMap<CharacterClass, MutableSet<Ability>> = EnumMap(CharacterClass::class.java)
        CharacterClass.values().forEach {map[it] = HashSet()}
        itemMap.values.forEach { map[it.getCharacter()]!!.add(it) }
        //update abilities for all ability trees
        CharacterClass.values().forEach { fromCharacter(it).setAbilities(map[it]!!) }
    }

    fun fromCharacter(character: CharacterClass): AbilityTree = treeMap[character]!!

    fun fromStatusName(name: String): Ability? {
        return statusNameMap[name]
    }

    fun fromDisplayName(name: String): Collection<Ability> {
        return nameMap[name] ?: emptySet()
    }
}