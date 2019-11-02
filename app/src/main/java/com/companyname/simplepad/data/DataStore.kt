package com.companyname.simplepad.data

import android.content.Context
import org.jetbrains.anko.doAsync

import java.util.concurrent.Executor
import java.util.concurrent.Executors

object DataStore {

    val EXEC: Executor = Executors.newSingleThreadExecutor()

    //lateinit lets compiler know we structure code to be initialized before using it
    //allows us to define it as non-null for cleaner and safer code
    @JvmStatic //helps mix kotlin with java by allowing static-ness representation at the JVM level
    lateinit var notes: NoteDatabase
        private set

    fun init(context: Context) {
        notes = NoteDatabase(context)
    }

    fun execute(runnable: Runnable) {
        execute { runnable.run() } //a way Anko helps make an alternative to AsyncTask
    }

    fun execute(fn: () -> Unit) {
        doAsync { fn() }
    }
}
