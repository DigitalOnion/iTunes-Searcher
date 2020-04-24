package com.example.itunessearcher.view

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.itunessearcher.App
import com.example.itunessearcher.R
import com.example.itunessearcher.model.DatabaseName
import com.example.itunessearcher.model.DbPersistence
import com.example.itunessearcher.model.MusicTrack
import com.example.itunessearcher.viewmodel.MusicViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    private val handler: Handler = Handler(Looper.getMainLooper())

    private val viewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MusicViewModel() as T
            }
        }).get(MusicViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.itunessearcher.R.layout.activity_main)

        rv_search_results.layoutManager = LinearLayoutManager(this@MainActivity)

        setTextChangedListener(viewModel)
        viewModel.searchNetwork.observe(this, getListMusicUIObserver())
        viewModel.searchNetwork.observe(this, getListMusicDbObserver())
        viewModel.networkError.observe(this, getNetworkErrorObserver())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.empty_database -> emptyDatabase();
        }
        return super.onOptionsItemSelected(item)
    }

    private fun emptyDatabase() {
        AlertDialog.Builder(this)
            .setTitle(R.string.empty_db_title)
            .setMessage(R.string.empty_db_message)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.ok,
                DialogInterface.OnClickListener { dialog, id ->
                    DbPersistence.clearCache()
                    App.context.deleteDatabase(DatabaseName)
                })
            .create().show()
    }

    private fun getListMusicUIObserver() : Observer<List<MusicTrack>> {
            return Observer { musicTrackList ->
                musicTrackList?.let {
                    rv_search_results.adapter = SearchListAdapter(musicTrackList)
                                { trackData: MusicTrack -> itemClicked(trackData) }
                }
            }
    }

    private fun getListMusicDbObserver() : Observer<List<MusicTrack>> {
            return Observer { musicTrackList ->
                DbPersistence.persist(viewModel.searchedTerm.value, musicTrackList)
            }
    }

    private fun getNetworkErrorObserver() : Observer<Int> {
            return Observer {
                Toast.makeText(App.context, R.string.network_error, Toast.LENGTH_LONG).show()
            }
    }

    /*TextWatcher with delay to prevent unnecessary network calls */
    private fun setTextChangedListener(viewModel: MusicViewModel) {
        edit_search.addTextChangedListener(object : TextWatcher {
            var timer = Timer()
            override fun afterTextChanged(s: Editable?) {
                viewModel.searchedTerm.value = s.toString()
                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        GlobalScope.launch {
                            viewModel.getTracks(s.toString().trim().toLowerCase(Locale.getDefault()), handler)
                        }
                    }
                }, 600)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                timer.cancel()
            }
        })
    }

    private fun itemClicked(trackData: MusicTrack) {

        val detailViewIntent = Intent(this, SongDetailActivity::class.java).apply {
            putExtra("track", trackData)
        }
        startActivity(detailViewIntent)
    }
}
