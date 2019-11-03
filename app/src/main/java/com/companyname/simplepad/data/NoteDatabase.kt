package com.companyname.simplepad.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import java.util.ArrayList
import java.util.Date

import android.provider.BaseColumns._ID
import com.companyname.simplepad.data.NotesContract.NoteTable.CREATED_AT
import com.companyname.simplepad.data.NotesContract.NoteTable.IS_PINNED
import com.companyname.simplepad.data.NotesContract.NoteTable.TEXT
import com.companyname.simplepad.data.NotesContract.NoteTable.UPDATED_AT
import com.companyname.simplepad.data.NotesContract.NoteTable._TABLE_NAME
import org.jetbrains.anko.db.transaction

class NoteDatabase(context: Context) {

    private val helper: NotesOpenHelper = NotesOpenHelper(context)

    //change back into function despite kotlin's change to variable to keep readability and consistency
    fun getAll(): List<Note> {
            val cursor = helper.readableDatabase.query(_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                    CREATED_AT)
        //lambda based function .use streamlines the use of closable resources and helps prevent memory leaks
        return cursor.use(this::allFromCursor) //.use auto closes cursor
        }

    fun loadAllByIds(vararg ids: Int): List<Note> {
        //kotlin's functional language extensions help simplify string builders here
        val questionMarks = ids.map { "?" }.joinToString { ", " }
        var args = ids.map { it.toString() }
        val selection = "$_ID IN ($questionMarks)"
        val cursor = helper.readableDatabase.query(_TABLE_NAME,
            null,
                selection,
                args.toTypedArray(),
            null,
            null,
                CREATED_AT)
        //lambda based function .use streamlines the use of closable resources and helps prevent memory leaks
        return cursor.use(this::allFromCursor) //.use auto closes cursor
    }

    fun insert(vararg notes: Note) {
        val values = fromNotes(notes)
        val db = helper.writableDatabase
        //From Anko that wraps simple lambda and wraps all of its operations inside a database transaction
        //thus handles the try/catch for us and allows cleaner looking code
        db.transaction {
            for (value in values) {
                insert(_TABLE_NAME, null, value)
            }
        }
    }

    fun update(note: Note) {
        val values = fromNote(note)
        helper.writableDatabase.update(_TABLE_NAME,
                values,
                "$_ID = ?",
                arrayOf(Integer.toString(note.id)))
    }

    fun delete(note: Note) {
        helper.writableDatabase.delete(_TABLE_NAME,
                "$_ID = ?",
                arrayOf(Integer.toString(note.id)))
    }

    private fun fromCursor(cursor: Cursor): Note {
        var col = 0
        //.apply here let's us inline create and return our object from cursor db object
        return Note().apply {
            id = cursor.getInt(col++)
            text = cursor.getString(col++)
            isPinned = cursor.getInt(col++) != 0
            createdAt = Date(cursor.getLong(col++))
            updatedAt = Date(cursor.getLong(col))
        }
    }

    private fun allFromCursor(cursor: Cursor): List<Note> {
        val retval = ArrayList<Note>()
        while (cursor.moveToNext()) {
            retval.add(fromCursor(cursor))
        }
        return retval
    }

    private fun fromNote(note: Note): ContentValues {
        //.apply here let's us inline return our content values from object passed in
        return ContentValues().apply {
            val noteId = note.id
            if (noteId != -1) {
                put(_ID, noteId)
            }
            put(TEXT, note.text)
            put(IS_PINNED, note.isPinned)
            put(CREATED_AT, note.createdAt.time)
            put(UPDATED_AT, note.updatedAt!!.time)
        }
    }

    //NOTE: sometimes to deal with incorrect TypeVariance adding `out` helps
    private fun fromNotes(notes: Array<out Note>): List<ContentValues> {
        val values = ArrayList<ContentValues>()
        for (note in notes) {
            values.add(fromNote(note))
        }
        return values
    }
}
