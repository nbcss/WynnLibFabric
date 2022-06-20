package io.github.nbcss.wynnlib.registry

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.AbilityTree
import io.github.nbcss.wynnlib.data.CharacterClass
import java.util.*
import kotlin.collections.HashSet

object AbilityRegistry: Registry<Ability>() {
    private val treeMap: MutableMap<CharacterClass, AbilityTree> = EnumMap(CharacterClass::class.java)
    init {
        CharacterClass.values().forEach { treeMap[it] = AbilityTree(it) }
    }

    override fun read(data: JsonObject): Ability? = try {
        Ability(data)
    }catch (e: Exception){
        e.printStackTrace()
        null
    }

    override fun reload(array: JsonArray){
        super.reload(array)
        //keep a cache map from character to all its ability
        val map: MutableMap<CharacterClass, MutableSet<Ability>> = EnumMap(CharacterClass::class.java)
        CharacterClass.values().forEach {map[it] = HashSet()}
        itemMap.values.forEach { map[it.getCharacter()]!!.add(it) }
        //update abilities for all ability trees
        CharacterClass.values().forEach { fromCharacter(it).setAbilities(map[it]!!) }
    }

    fun fromCharacter(character: CharacterClass): AbilityTree = treeMap[character]!!
}