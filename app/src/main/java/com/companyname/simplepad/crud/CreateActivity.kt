package com.companyname.simplepad.crud

import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter.EXTRA_DATA
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.companyname.simplepad.R
import com.companyname.simplepad.data.DataStore
import com.companyname.simplepad.data.Note
import com.companyname.simplepad.util.DateSerializer
import kotlinx.android.synthetic.main.activity_create.*

import java.util.Date

class CreateActivity : AppCompatActivity() {

    private var isNewNote: Boolean = true
    private var note: Note? = Note()

    companion object {
        operator fun get(context: Context): Intent {
            return Intent(context, CreateActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        // Get the Intent that started this activity and extract the Int with default 0
        val noteId = intent.getIntExtra(EXTRA_MESSAGE, 0)
        if(noteId!=0) isNewNote = false; getNote(noteId) //only need to update UI if not a new note
    }

    private fun updateUI() {
        edit_text.setText(note?.text, TextView.BufferType.EDITABLE)
        edit_text.setSelection(edit_text.text.length) //put selectable cursor at end of line
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_accept, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_accept -> {
                save()
                finish()
            }
            R.id.action_delete -> {
                delete()
                finish()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun delete() {
        DataStore.execute(Runnable {
            if(!isNewNote) note?.let { DataStore.notes.delete(it) }
        })
    }

    private fun save() {
        DataStore.execute(Runnable {
            val note = updateNote()
            if(isNewNote) {
                DataStore.notes.insert(note)
            } else {
                DataStore.notes.update(note)
            }
        })
    }

    private fun updateNote(): Note {
        val updatedNote = Note()
        updatedNote.text = edit_text.text.toString()
        updatedNote.updatedAt = Date()
        if(!isNewNote) {
            updatedNote.id = note!!.id
            updatedNote.createdAt = note!!.createdAt
            updatedNote.isPinned = note!!.isPinned
        }
        return updatedNote
    }

    private fun getNote(noteId: Int) {
        DataStore.execute(Runnable {
            val notes = DataStore.notes.getAll()
            note = notes.find { it.id == noteId }
            updateUI()
        })
    }
}
