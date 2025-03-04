package com.natxo.practica5.dataclass

data class Noticia(
    val id: Int,
    val titulo: String,
    val resumen: String,
    val fecha: String,
    val imagenPortada: String,
    val enlace: String
)