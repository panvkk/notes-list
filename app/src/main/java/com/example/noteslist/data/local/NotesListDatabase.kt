package com.example.noteslist.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.noteslist.data.dto.NoteEntity
import com.example.noteslist.data.local.dao.NotesDao

@Database(entities = [NoteEntity::class], version = 2)
abstract class NotesListDatabase : RoomDatabase() {
    abstract fun notesDao() : NotesDao

    companion object {
        private var INSTANCE: NotesListDatabase? = null
        private fun generateInitialData(db: SupportSQLiteDatabase) {
            db.execSQL("INSERT INTO notes_table (id, title, description, is_important, date, is_read) VALUES (1, 'универ', 'завтра надо приехать к 4 паре (хотя бы приехать)', 0, '15.03.2026', 0)")
            db.execSQL("INSERT INTO notes_table (id, title, description, is_important, date, is_read) VALUES (2, 'поесть', 'пельменей можно', 1, '15.03.2026', 0)")
            db.execSQL("INSERT INTO notes_table (id, title, description, is_important, date, is_read) VALUES (5, 'универ', 'завтра надо приехать к 4 паре (хотя бы приехать)', 0, '15.03.2026', 0)")
            db.execSQL("INSERT INTO notes_table (id, title, description, is_important, date, is_read) VALUES (6, 'Hilt', 'Поправить циклическую зависимость в модуле Data', 0, '15.03.2026', 0)")
            db.execSQL("INSERT INTO notes_table (id, title, description, is_important, date, is_read) VALUES (7, 'Зал', 'Тяжелая тренировка: спина и бицепс', 0, '15.03.2026', 0)")
            db.execSQL("INSERT INTO notes_table (id, title, description, is_important, date, is_read) VALUES (8, 'БГУ', 'Забрать справку в деканате (если не забуду)', 0, '16.03.2026', 0)")
            db.execSQL("INSERT INTO notes_table (id, title, description, is_important, date, is_read) VALUES (9, 'Android', 'Custom View: разобраться с onMeasure и StaticLayout', 0, '16.03.2026', 0)")
            db.execSQL("INSERT INTO notes_table (id, title, description, is_important, date, is_read) VALUES (3, 'Зал', 'Легкое кардио, чисто размяться перед неделей', 1, '15.03.2026', 0)")
            db.execSQL("INSERT INTO notes_table (id, title, description, is_important, date, is_read) VALUES (4, 'Планы', 'Распихать все лабы по дедлайнам', 0, '15.03.2026', 0)")

        }

        private val Migration_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                generateInitialData(db)
            }
        }

        fun getDatabase(context: Context) : NotesListDatabase {
            return INSTANCE ?: synchronized(this) {
                var instance: NotesListDatabase? = null
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesListDatabase::class.java, "noteslist-db.db"
                )
                    .addCallback(object: Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            generateInitialData(db)
                        }
                    })
                    .addMigrations(Migration_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}