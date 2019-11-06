package com.companyname.simplepad.recycler

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.companyname.simplepad.R
import com.companyname.simplepad.crud.CreateActivity
import com.companyname.simplepad.data.DataStore
import com.companyname.simplepad.data.Note
import com.companyname.simplepad.util.layoutInflater
import kotlinx.android.synthetic.main.item_note.view.*
import android.content.Intent
import android.view.View
import android.widget.TextView

import java.util.ArrayList

class NotesAdapter(private val context: Context) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    private var notes: List<Note> = ArrayList()
    private var isRefreshing = false

    init {
        setHasStableIds(true)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        refresh()
    }

    override fun getItemId(position: Int): Long {
        return notes[position].id.toLong()
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        //this particular extension function literally saves us from typing one extra `.from` in each adapter...
        val view = context.layoutInflater!!.inflate(R.layout.item_note, parent, false)
        return NotesViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = notes[position]
        holder.text.text = note.text
        holder.bind(notes[position])
    }

    fun refresh() {
        if (isRefreshing) return
        isRefreshing = true
        DataStore.execute {
            val notes = DataStore.notes.getAll()
            Handler(Looper.getMainLooper()).post {
                this@NotesAdapter.notes = notes
                notifyDataSetChanged()
                isRefreshing = false
            }
        }
    }

    class NotesViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var text: TextView = itemView.text
        var id = itemView.id

        fun bind(note: Note) {
            itemView.content.setOnClickListener(View.OnClickListener {
                //Start CreateActivity but with extra id to Edit a Note instead of create a new one
                val intent = Intent(itemView.context, CreateActivity::class.java).apply {
                    putExtra(EXTRA_MESSAGE, note.id)
                }
                startActivity(itemView.context, intent, null)
            })
        }

    }

}
