package com.example.taskmanagertodo.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.widget.EditText
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanager.R
import com.example.taskmanager.databinding.ActivityMainBinding
import com.example.taskmanagertodo.data.Task
import com.example.taskmanagertodo.viewmodel.TaskViewModel
import com.example.taskmanagertodo.viewmodel.TaskViewModelFactory

class MainActivity : AppCompatActivity(), TaskAdapter.Listener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: TaskViewModel
    private lateinit var adapter: TaskAdapter

    // Activity result for add/edit - returns Task via intent extras
    private val addEditLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val mode = data?.getStringExtra(AddEditTaskActivity.EXTRA_MODE) ?: return@registerForActivityResult
            val title = data.getStringExtra(AddEditTaskActivity.EXTRA_TITLE) ?: ""
            val desc = data.getStringExtra(AddEditTaskActivity.EXTRA_DESC)
            if (title.isBlank()) {
                Toast.makeText(this, "Task title cannot be empty", Toast.LENGTH_SHORT).show()
                return@registerForActivityResult
            }
            if (mode == AddEditTaskActivity.MODE_ADD) {
                val task = Task(title = title, description = desc)
                viewModel.insert(task) { id ->
                    Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show()
                }
            } else if (mode == AddEditTaskActivity.MODE_EDIT) {
                val id = data.getLongExtra(AddEditTaskActivity.EXTRA_ID, -1)
                if (id == -1L) return@registerForActivityResult
                val task = Task(id = id, title = title, description = desc)
                viewModel.update(task) {
                    Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val factory = TaskViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory).get(TaskViewModel::class.java)

        adapter = TaskAdapter(emptyList(), this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Observe filtered tasks LiveData
        viewModel.filteredTasks.observe(this) { list ->
            adapter.updateList(list)
            if (list.isEmpty()) {
                Toast.makeText(this, "No tasks found", Toast.LENGTH_SHORT).show()
            }
        }

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddEditTaskActivity::class.java)
            intent.putExtra(AddEditTaskActivity.EXTRA_MODE, AddEditTaskActivity.MODE_ADD)
            addEditLauncher.launch(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        // SearchView setup
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "Search tasks"

        val searchEditTextId = androidx.appcompat.R.id.search_src_text
        val searchEditText = searchView.findViewById<EditText>(searchEditTextId)
        searchEditText?.apply {
            setTextColor(Color.WHITE)
            setHintTextColor(Color.parseColor("#B0FFFFFF"))
        }

        val searchIcon = searchView.findViewById<ImageView>(
            resources.getIdentifier("android:id/search_mag_icon", null, null)
        )
        searchIcon?.setColorFilter(Color.WHITE)

        val closeButton = searchView.findViewById<ImageView>(
            resources.getIdentifier("android:id/search_close_btn", null, null)
        )
        closeButton?.setColorFilter(Color.WHITE)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setSearchQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchQuery(newText)
                return true
            }
        })

        // Clear filter when closed
        searchView.setOnCloseListener {
            viewModel.setSearchQuery(null)
            false
        }

        return true
    }

    override fun onEdit(task: Task) {
        // Open AddEditTaskActivity in edit mode with existing data
        val intent = Intent(this, AddEditTaskActivity::class.java).apply {
            putExtra(AddEditTaskActivity.EXTRA_MODE, AddEditTaskActivity.MODE_EDIT)
            putExtra(AddEditTaskActivity.EXTRA_ID, task.id)
            putExtra(AddEditTaskActivity.EXTRA_TITLE, task.title)
            putExtra(AddEditTaskActivity.EXTRA_DESC, task.description)
        }
        addEditLauncher.launch(intent)
    }

    override fun onDelete(task: Task) {
        viewModel.delete(task) {
            Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show()
        }
    }
}