package com.example.quicknotesapp.note_form

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.quicknotesapp.database.Note
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.quicknotesapp.R
import com.example.quicknotesapp.database.NoteDatabase
import com.example.quicknotesapp.databinding.FragmentNoteFormBinding
import com.example.quicknotesapp.generateNewId
import com.example.quicknotesapp.model.NotesViewModel
import com.example.quicknotesapp.model.NotesViewModelFactory

class NoteFormFragment : Fragment() {
    lateinit var binding: FragmentNoteFormBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Preparing The Global ViewModelFactory Arguments
        val application = requireNotNull(this.activity).application
        val dataSource = NoteDatabase.getInstance(application).noteDatabaseDao

        val sharedViewModel: NotesViewModel by activityViewModels {
            NotesViewModelFactory(
                dataSource,
                application
            )
        }
        val noteId = NoteFormFragmentArgs.fromBundle(requireArguments()).updatedNoteId

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_note_form, container, false)
        binding.lifecycleOwner = this


        val viewModel = ViewModelProvider(this).get(FormViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.navigateToHome.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController()
                    .navigate(NoteFormFragmentDirections.actionNoteFormToHomeScreen())
                viewModel.doneNavigatingToHome()
            }
        })

        if (noteId == -1L) {
            binding.addNoteButton.setOnClickListener {
                val enteredTitle = binding.noteTitleEditText.text.trim().toString()
                if (enteredTitle.isNotEmpty()) {
                    sharedViewModel.addNote(
                        Note(
                            generateNewId(),
                            enteredTitle
                        )
                    )
                    this.findNavController()
                        .navigate(NoteFormFragmentDirections.actionNoteFormToHomeScreen())
                } else {
                    Toast.makeText(context, "Enter a text pls", Toast.LENGTH_SHORT).show()

                }
            }
        } else {
            binding.fragmentTitle.text = getString(R.string.update_note_text)
            binding.addNoteButton.text = getString(R.string.update_note_text)
            val editedNote = sharedViewModel.getNote(noteId)
            binding.noteTitleEditText.setText(editedNote.title)
            binding.addNoteButton.setOnClickListener {
                val enteredTitle = binding.noteTitleEditText.text.trim().toString()
                if (enteredTitle.isNotEmpty()) {
                    sharedViewModel.updateNote(Note(noteId, enteredTitle))
                    this.findNavController()
                        .navigate(NoteFormFragmentDirections.actionNoteFormToHomeScreen())
                } else {
                    Toast.makeText(context, "Enter a text pls", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }

}