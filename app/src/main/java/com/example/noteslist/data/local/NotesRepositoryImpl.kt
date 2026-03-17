package com.example.noteslist.data.local

import com.example.noteslist.core.presentation.toLocalDate
import com.example.noteslist.data.dto.NoteDto
import com.example.noteslist.domain.model.NoteModel
import com.example.noteslist.domain.repository.NotesRepository

class NotesRepositoryImpl : NotesRepository {
    private fun fetchNotes() = listOf(
        NoteDto(1, "универ", "завтра надо приехать к 4 паре (хотя бы приехать)", false, "15.03.2026"),
        NoteDto(2, "поесть", "пельменей можно", true, "15.03.2026"),
        NoteDto(3, "Зал", "Легкое кардио, чисто размяться перед неделей", true, "15.03.2026"),
        NoteDto(4, "Планы", "Распихать все лабы по дедлайнам нахуй", false, "15.03.2026"),

        NoteDto(5, "универ", "завтра надо приехать к 4 паре (хотя бы приехать)", false, "16.03.2026"),
        NoteDto(6, "Hilt", "Поправить циклическую зависимость в модуле Data", false, "16.03.2026"),
        NoteDto(7, "Зал", "Тяжелая тренировка: спина и бицепс", false, "16.03.2026"),
        NoteDto(8, "БГУ", "Забрать справку в деканате (если не забуду)", false, "16.03.2026"),
        NoteDto(9, "Android", "Custom View: разобраться с onMeasure и StaticLayout", false, "16.03.2026"),

        NoteDto(10, "универ", "завтра надо приехать к 4 паре (хотя бы приехать)", false, "17.03.2026"),
        NoteDto(11, "Code", "Оптимизировать DiffUtil в основном списке", true, "17.03.2026"),
        NoteDto(12, "Шмотки", "Чекать Wildberries на предмет оверсайз худи", false, "17.03.2026"),
        NoteDto(13, "Аптека", "Витамины и креатин забрать", false, "17.03.2026"),
        NoteDto(14, "ФПМИ", "Сдать лабу по алгоритмам (графы)", false, "17.03.2026"),

        NoteDto(15, "Новая Forza", "Завтра выходит Forza Horizon 6 хз надо поиграть наверное придётся удалить танки", true, "18.03.2026"),
        NoteDto(16, "Стоп", "Пока хватит, потом допишу остальные.", true, "18.03.2026"),
        NoteDto(17, "Зал", "День ног (приседания до талого)", false, "18.03.2026"),
        NoteDto(18, "Pet-project", "Spotify-style dark theme: подобрать HEX цвета фона", false, "18.03.2026"),
        NoteDto(19, "Уборка", "Помыть посуду, а то завалило всё нахуй", false, "18.03.2026"),

        NoteDto(20, "Экзамены", "Если замтека видна, то сесси уже близко...", false, "19.03.2026"),
        NoteDto(21, "Forza 6", "Поставить на закачку с утра, чтобы к вечеру была", false, "19.03.2026"),
        NoteDto(22, "Собес", "Повторить Coroutines: разница между launch и async", false, "19.03.2026"),
        NoteDto(23, "Танки", "Удалить всё-таки эту помойку, место нужно", true, "19.03.2026"),

        NoteDto(24, "универ", "завтра надо приехать к 4 паре (хотя бы приехать)", false, "20.03.2026"),
        NoteDto(25, "Flow", "StateFlow vs SharedFlow: дописать доку в Notion", false, "20.03.2026"),
        NoteDto(26, "Зал", "Грудь и трицепс. Жать от души", false, "20.03.2026"),
        NoteDto(27, "Проект", "Добавить анимацию перехода через MotionLayout", false, "20.03.2026"),
        NoteDto(28, "Вечер", "Пиво или отдых? Наверное, отдых", true, "20.03.2026"),

        NoteDto(29, "Учеба", "Посмотреть лекцию, которую проспал во вторник", false, "21.03.2026"),
        NoteDto(30, "GitHub", "Запушить последние фиксы в репу Конструктора Печей", true, "21.03.2026"),
        NoteDto(31, "Forza", "Ночной дрифт на новых картах", false, "21.03.2026"),
        NoteDto(32, "Питание", "Закупиться едой на неделю вперед", false, "21.03.2026"),
        NoteDto(33, "Сон", "Выспаться хотя бы до 11", false, "21.03.2026"),

        NoteDto(34, "Рефактор", "Вынести константы в отдельный файл", false, "22.03.2026"),
        NoteDto(35, "Зал", "Растяжка и бассейн", true, "22.03.2026"),
        NoteDto(36, "ФПМИ", "Подготовить отчет по практике", false, "22.03.2026"),
        NoteDto(37, "Отдых", "Чисто залипнуть в ютуб под вечер", false, "22.03.2026")
    )

    override fun getNotes() = fetchNotes().map { noteDto -> noteDto.toDomain() }

    fun NoteDto.toDomain() = NoteModel(
        title = title,
        description = description,
        isImportant  = isImportant,
        date = date.toLocalDate(),
        isRead = isRead
    )
}