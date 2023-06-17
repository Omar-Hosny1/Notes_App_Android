package com.example.quicknotesapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


// exportSchema => saves the database schema in a folder
@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDatabaseDao: NoteDatabaseDao

    companion object {
        @Volatile // this means that the variable is always up to date and the same to the all execution thread
        private var INSTANCE: NoteDatabase? = null

        // we here provide the database via database builder and it requires a context
        fun getInstance(context: Context): NoteDatabase {
            // only one thread can enter this block of code
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "notes_database"
                )
                    // when changing the database wipe and build again
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }
    }
}
