package com.example.itunessearcher.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.itunessearcher.model.MusicList
import com.example.itunessearcher.model.POKOMusicTrack
import com.example.itunessearcher.viewmodel.MusicViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.itunessearcher.R.layout.activity_main)

        val viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MusicViewModel() as T
            }
        }).get(MusicViewModel::class.java)

        search_words.addTextChangedListener(getTextChangedListener(viewModel))
        observeViewModel(viewModel)
    }

    private fun observeViewModel(viewModel: MusicViewModel) {
        viewModel.getSearchResults()
            .observe(this, Observer<MusicList> { t ->
                search_recycler.layoutManager = LinearLayoutManager(
                    this@MainActivity
                )
                t?.let {
                    search_recycler.adapter = SearchListAdapter(t) { trackData: POKOMusicTrack -> itemClicked(trackData) }
                }
            })
    }

    // TextWatcher with a delay prevents useless API calls while the user is typing
    private fun getTextChangedListener(viewModel : MusicViewModel) : TextWatcher {
        return object : TextWatcher {
            var timer = Timer()
            override fun afterTextChanged(s: Editable?) {
                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        GlobalScope.launch {
                            viewModel.getTracksFromAPI(s.toString())
                        }
                    }
                }, 600) 
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                timer.cancel()
            }
        }
    }

    private fun itemClicked(trackData: POKOMusicTrack) {
        val detailViewIntent = Intent(this, SongDetailActivity::class.java)
            .apply { putExtra("track", trackData) }
        startActivity(detailViewIntent)
    }
}
