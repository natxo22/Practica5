package com.natxo.practica5.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.natxo.practica5.database.dao.FavoritoDao
import com.natxo.practica5.database.dao.NoticiaDao
import com.natxo.practica5.database.dao.UsuarioDao
import com.natxo.practica5.database.entity.FavoritoEntity
import com.natxo.practica5.database.entity.NoticiaEntity
import com.natxo.practica5.database.entity.UsuarioEntity


@Database(entities = [NoticiaEntity::class, UsuarioEntity::class, FavoritoEntity::class],
    version = 1)
abstract class NoticiaDatabase: RoomDatabase() {
    abstract fun noticiaDao(): NoticiaDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun favoritoDao(): FavoritoDao
}
