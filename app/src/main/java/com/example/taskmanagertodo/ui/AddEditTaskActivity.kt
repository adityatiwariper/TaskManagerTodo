package com.example.taskmanagertodo.ui

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanager.databinding.ActivityAddEditTaskBinding

class AddEditTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditTaskBinding

    companion object {
        const val MODE_ADD = "mode_add"
        const val MODE_EDIT = "mode_edit"
        const val EXTRA_MODE = "extra_mode"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_DESC = "extra_desc"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mode = intent.getStringExtra(EXTRA_MODE) ?: MODE_ADD

        if (mode == MODE_EDIT) {
            title = "Edit Task"
            val titleStr = intent.getStringExtra(EXTRA_TITLE) ?: ""
            val desc = intent.getStringExtra(EXTRA_DESC) ?: ""
            binding.etTitle.setText(titleStr)
            binding.etDesc.setText(desc)
        } else {
            title = "Add Task"
        }

        binding.btnSave.setOnClickListener {
            saveAndFinish(mode)
        }

        binding.etDesc.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveAndFinish(mode)
                true
            } else false
        }
    }

    private fun saveAndFinish(mode: String) {
        val title = binding.etTitle.text?.toString()?.trim() ?: ""
        val desc = binding.etDesc.text?.toString()?.trim()

        // validation: title required
        if (title.isEmpty()) {
            binding.tilTitle.error = "Title required"
            binding.etTitle.requestFocus()
            return
        } else {
            binding.tilTitle.error = null
        }

        // Prepare result
        val result = intent
        result.putExtra(EXTRA_MODE, mode)
        result.putExtra(EXTRA_TITLE, title)
        result.putExtra(EXTRA_DESC, desc)
        if (mode == MODE_EDIT) {
            val id = intent.getLongExtra(EXTRA_ID, -1)
            if (id != -1L) result.putExtra(EXTRA_ID, id)
        }

        setResult(Activity.RESULT_OK, result)
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
        finish()
    }
}