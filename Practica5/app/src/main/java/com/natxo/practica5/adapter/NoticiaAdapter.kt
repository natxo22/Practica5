package com.natxo.practica5.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.natxo.practica5.NoticiasApplication.Companion.prefs
import com.natxo.practica5.R
import com.natxo.practica5.database.entity.NoticiaEntity


class NoticiaAdapter(
    private var noticiaList: MutableList<NoticiaEntity>,
    val onFavorite: (NoticiaEntity) -> Unit,
    val onEdit: (NoticiaEntity) -> Unit,
    val onDelete: (NoticiaEntity) -> Unit

) : RecyclerView.Adapter<NoticiaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticiaViewHolder {
        val layoutInflater = LayoutInflater.from (parent.context)
        return NoticiaViewHolder(layoutInflater.inflate(R.layout.item_noticia, parent, false))
    }

    override fun getItemCount(): Int {
        return noticiaList.size
    }

    override fun onBindViewHolder(holder: NoticiaViewHolder, position: Int) {
        val item = noticiaList[position]

        holder.render(item)

        val icon = if (item.esFavorita) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border_24
        holder.binding.btnFav.setImageResource(icon)

        holder.binding.card.setOnClickListener{
            //Cargar imagenes desde los items del adaptador
            // (en mi windows 10 daba pantallazo azul, en el ordenador de clase funciona perfectamente)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.noticiaUrl))
            holder.itemView.context.startActivity(intent)

            prefs.setLastClickedTitle(item.titulo)

        }

        holder.binding.btnDelete.setOnClickListener {
            onDelete(item)
        }

        holder.binding.btnEdit.setOnClickListener{
            onEdit(item)
        }

        holder.binding.btnFav.setOnClickListener{
            onFavorite(item)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getNoticiaAdapter(noticias: MutableList<NoticiaEntity>) {
        this.noticiaList = noticias
        notifyDataSetChanged()
    }

    fun updateNoticiaAdapter(noticia: NoticiaEntity) {
        val indice = noticiaList.indexOfFirst { it.id == noticia.id }

        if (indice != -1) {
            noticiaList[indice] = noticia
            notifyItemChanged(indice)
        }
    }

    fun deleteNoticiaAdapter(noticia: NoticiaEntity) {
        val indice = noticiaList.indexOf(noticia)

        if (indice != -1) {
            noticiaList.removeAt(indice)
            notifyItemRemoved(indice)
        }
    }


}