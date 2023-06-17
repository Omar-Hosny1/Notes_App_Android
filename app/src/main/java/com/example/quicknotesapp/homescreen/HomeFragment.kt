package com.example.quicknotesapp.homescreen

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quicknotesapp.R
import com.example.quicknotesapp.database.NoteDatabase
import com.example.quicknotesapp.database.NoteDatabaseDao
import com.example.quicknotesapp.databinding.FragmentHomeScreenBinding
import com.example.quicknotesapp.model.NotesViewModel
import com.example.quicknotesapp.model.NotesViewModelFactory

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeScreenBinding
    private var dialog: Dialog? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Binding Object Configuration
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_screen, container, false)
        binding.lifecycleOwner = this

        // Preparing The Global ViewModelFactory Arguments
        val application = requireNotNull(this.activity).application
        val dataSource = NoteDatabase.getInstance(application).noteDatabaseDao

        // Global ViewModel Configuration
        val viewModelFactory = NotesViewModelFactory(dataSource, application)
        val sharedViewModel: NotesViewModel by activityViewModels { viewModelFactory }

        // ViewModel Configuration
        val viewModel: HomeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        // Bind The ViewModels To the Views
        binding.viewModel = viewModel
        binding.globalViewModel = sharedViewModel

        // On Navigating to NoteFormFragment
        viewModel.navigateToNoteForm.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeScreenToNoteForm(-1))
                viewModel.doneNavigatingToNoteForm()
            }
        })

        // Recycler View Configuration
        val gridManager = GridLayoutManager(activity, 2)
        binding.noteList.layoutManager = gridManager
        val adapter = NoteAdapter(NoteListener {
            // Handle The Click Listener
            showDialog(
                {
                    this.findNavController()
                        .navigate(HomeFragmentDirections.actionHomeScreenToNoteForm(it))
                }, {
                    sharedViewModel.removeNote(it)
                })
        })

        // Bind The Recycler View Adapter with the Adapter
        binding.noteList.adapter = adapter
        sharedViewModel.notes.observe(viewLifecycleOwner, Observer {
            println("!!!!!!!!!!!!!!!!!!!!!!!!!!! Submitted The New List !!!!!!!!!!!!!!!!!!!!!!!!!!!")
            adapter.submitList(ArrayList(it))
        })

        return binding.root
    }

    private fun showDialog(
        updateCallback: () -> Unit,
        removeCallback: () -> Unit
    ) {
        dialog = Dialog(this.requireContext())
        dialog?.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_bottom_sheet)
            val editLayout = findViewById<LinearLayout>(R.id.layout_edit)
            val removeLayout = findViewById<LinearLayout>(R.id.layout_remove)

            editLayout.setOnClickListener {
                dismissDialog()
                updateCallback()
            }


            removeLayout.setOnClickListener {
                removeCallback()
                dismissDialog()
            }

            show()
            window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window!!.setGravity(Gravity.BOTTOM)
        }
    }

    private fun dismissDialog() {
        dialog?.dismiss()
        dialog = null
    }

    override fun onPause() {
        super.onPause()
        dismissDialog()
    }
}
