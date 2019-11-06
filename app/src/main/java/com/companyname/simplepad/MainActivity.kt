package com.companyname.simplepad

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.companyname.simplepad.crud.CreateActivity
import com.companyname.simplepad.recycler.NotesAdapter
import com.companyname.simplepad.util.SpaceItemDecoration
//Allows kotlin to auto find view ids to make declarations and assignments easier. (var name is view id)
//This might get in the way of instant run feature, if crashes when doing so try a clean build
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        //b/c of Kotlin's SAM conversion any simple interface with one method can be changed to a simple lambda expression
        fab.setOnClickListener { startActivity(CreateActivity.get(this@MainActivity)) }

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.addItemDecoration(SpaceItemDecoration(this, R.dimen.margin_small))
        recycler.adapter = NotesAdapter(this)
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    public override fun onDestroy() {
        super.onDestroy()
        recycler!!.adapter = null
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) { //when statements in kotlin can be returnable expressions
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun refresh() {
        (recycler!!.adapter as NotesAdapter).refresh()
    }

}
