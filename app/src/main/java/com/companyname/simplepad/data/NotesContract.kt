package com.companyname.simplepad.data

import com.companyname.simplepad.data.NotesContract.NoteTable.CREATED_AT
import com.companyname.simplepad.data.NotesContract.NoteTable.IS_PINNED
import com.companyname.simplepad.data.NotesContract.NoteTable.TEXT
import com.companyname.simplepad.data.NotesContract.NoteTable.UPDATED_AT
import com.companyname.simplepad.data.NotesContract.NoteTable._ID
import com.companyname.simplepad.data.NotesContract.NoteTable._TABLE_NAME

object NotesContract {

    //NOTE: Kotlin will need to define `_id` as opposed to Java.
    object NoteTable {
        val _ID = "_id"
        val _TABLE_NAME = "notes"
        val TEXT = "text"
        val IS_PINNED = "is_pinned"
        val CREATED_AT = "created_at"
        val UPDATED_AT = "updated_at"
    }

    val SQL_CREATE_ENTRIES = """CREATE TABLE $_TABLE_NAME (
            $_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            $TEXT TEXT, 
            $IS_PINNED INTEGER, 
            $CREATED_AT INTEGER, 
            $UPDATED_AT INTEGER
    )"""

    val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $_TABLE_NAME"

    //NOTE: Be sure to verify variable names in SQL tables are converted correctly after running autoconvert!
    val SQL_QUERY_ALL =
            "SELECT * FROM $_TABLE_NAME ORDER BY $CREATED_AT DESC"
}
