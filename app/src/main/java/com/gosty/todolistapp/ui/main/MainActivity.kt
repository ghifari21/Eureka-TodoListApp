package com.gosty.todolistapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.gosty.todolistapp.R
import com.gosty.todolistapp.data.models.Book
import com.gosty.todolistapp.data.ui.BookListAdapter
import com.gosty.todolistapp.databinding.ActivityMainBinding
import com.gosty.todolistapp.ui.add.AddActivity
import com.gosty.todolistapp.ui.detail.DetailActivity
import com.gosty.todolistapp.utils.Result
import com.kennyc.view.MultiStateView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val adapter = BookListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    /***
     * This method to initialize the view for MainActivity.
     * @author Ghifari Octaverin
     * @since Sept 15th, 2023
     */
    private fun initView() {
        viewModel.getAllBooks().observe(this) {
            when (it) {
                is Result.Loading -> {
                    binding.stateView.viewState = MultiStateView.ViewState.LOADING
                }

                is Result.Success -> {
                    if (it.data.isNotEmpty()) {
                        binding.apply {
                            stateView.viewState = MultiStateView.ViewState.CONTENT
                            val content = stateView.getView(MultiStateView.ViewState.CONTENT)
                            if (content != null) {
                                val layoutManager = LinearLayoutManager(this@MainActivity)
                                rvBooks.adapter = adapter
                                rvBooks.layoutManager = layoutManager
                                rvBooks.setHasFixedSize(true)
                            }
                        }
                        adapter.submitList(it.data)

                        adapter.setOnItemClickCallback(
                            object : BookListAdapter.OnItemClickCallback {
                                override fun onItemClicked(book: Book) {
                                    val intent =
                                        Intent(this@MainActivity, DetailActivity::class.java)
                                    intent.putExtra(DetailActivity.EXTRA_DATA, book.id)
                                    startActivity(intent)
                                }
                            }
                        )

                        binding.fabAdd.setOnClickListener {
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    AddActivity::class.java
                                )
                            )
                        }
                    } else {
                        binding.stateView.viewState = MultiStateView.ViewState.EMPTY
                        val empty = binding.stateView.getView(MultiStateView.ViewState.EMPTY)
                        if (empty != null) {
                            val btn: FloatingActionButton = empty.findViewById(R.id.fab_add_empty)
                            btn.setOnClickListener {
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        AddActivity::class.java
                                    )
                                )
                            }
                        }
                    }
                }

                is Result.Error -> {
                    binding.stateView.viewState = MultiStateView.ViewState.ERROR
                    val errorView = binding.stateView.getView(MultiStateView.ViewState.ERROR)
                    if (errorView != null) {
                        val btn: Button = errorView.findViewById(R.id.btn_retry)
                        btn.setOnClickListener {
                            finish()
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}