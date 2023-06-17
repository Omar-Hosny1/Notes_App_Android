package com.example.quicknotesapp.homescreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quicknotesapp.databinding.NoteItemBinding
import com.example.quicknotesapp.database.Note

class NoteAdapter(val clickListener: NoteListener) :
    ListAdapter<Note, NoteAdapter.ViewHolder>(NoteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val noteItem = getItem(position)
        holder.bind(noteItem, clickListener)
    }

    class ViewHolder(val binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Note, clickListener: NoteListener) {
            binding.note = item
            binding.clickListenr = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NoteItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}

class NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        // check the equality by checking the all vars in this data class
        return oldItem == newItem
    }
}

class NoteListener(val clickListener: (noteId: Long) -> Unit) {
    fun onClick(note: Note) = clickListener(note.id)
}