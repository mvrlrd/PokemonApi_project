package ru.mvrlrd.pokeapi.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.mvrlrd.pokeapi.data.Pokemon


@Database(entities = [Pokemon::class], version = 1)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}