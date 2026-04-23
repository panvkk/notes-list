package com.example.noteslist.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.noteslist.data.dto.NoteEntity
import com.example.noteslist.data.local.dao.NotesDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [NoteEntity::class], version = 1)
abstract class NotesListDatabase : RoomDatabase() {
    abstract fun notesDao() : NotesDao

    companion object {
        private var INSTANCE: NotesListDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope) : NotesListDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesListDatabase::class.java, "noteslist-db.db"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        scope.launch(Dispatchers.IO) {
                            val dao = getDatabase(context, scope).notesDao()

                            val testNotes = listOf(
                                NoteEntity(1, "универ", "завтра надо приехать к 4 паре (хотя бы приехать)", false, "15.03.2026"),
                                NoteEntity(2, "поесть", "пельменей можно", true, "15.03.2026"),
                                NoteEntity(5, "универ", "завтра надо приехать к 4 паре (хотя бы приехать)", false, "15.03.2026"),
                                NoteEntity(6, "Hilt", "Поправить циклическую зависимость в модуле Data", false, "15.03.2026"),
                                NoteEntity(7, "Зал", "Тяжелая тренировка: спина и бицепс", false, "15.03.2026"),
                                NoteEntity(8, "БГУ", "Забрать справку в деканате (если не забуду)", false, "16.03.2026"),
                                NoteEntity(9, "Android", "Custom View: разобраться с onMeasure и StaticLayout", false, "16.03.2026"),
                                NoteEntity(3, "Зал", "Легкое кардио, чисто размяться перед неделей", true, "15.03.2026"),
                                NoteEntity(4, "Планы", "Распихать все лабы по дедлайнам нахуй", false, "15.03.2026")
                            )
                            testNotes.forEach { dao.putNote(it) }
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}