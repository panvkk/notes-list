package com.example.noteslist.data

class NotesRepository {
    fun getNotes() = listOf(
        ViewTyped.Note("универ", "завтра надо приехать к 4 паре (хотя бы приехать)", false, "15.03.2026"),
        ViewTyped.Note("универ", "завтра надо приехать к 4 паре (хотя бы приехать)", false, "15.03.2026"),
        ViewTyped.Note("поесть", "пельменей можно", true, "15.03.2026"),
        ViewTyped.Note("Зал", "Легкое кардио, чисто размяться перед неделей", true, "15.03.2026"),
        ViewTyped.Note("Планы", "Распихать все лабы по дедлайнам нахуй", false, "15.03.2026"),

        ViewTyped.Note("универ", "завтра надо приехать к 4 паре (хотя бы приехать)", false, "16.03.2026"),
        ViewTyped.Note("Hilt", "Поправить циклическую зависимость в модуле Data", false, "16.03.2026"),
        ViewTyped.Note("Зал", "Тяжелая тренировка: спина и бицепс", false, "16.03.2026"),
        ViewTyped.Note("БГУ", "Забрать справку в деканате (если не забуду)", false, "16.03.2026"),
        ViewTyped.Note("Android", "Custom View: разобраться с onMeasure и StaticLayout", false, "16.03.2026"),

        ViewTyped.Note("универ", "завтра надо приехать к 4 паре (хотя бы приехать)", false, "17.03.2026"),
        ViewTyped.Note("Code", "Оптимизировать DiffUtil в основном списке", true, "17.03.2026"),
        ViewTyped.Note("Шмотки", "Чекать Wildberries на предмет оверсайз худи", false, "17.03.2026"),
        ViewTyped.Note("Аптека", "Витамины и креатин забрать", false, "17.03.2026"),
        ViewTyped.Note("ФПМИ", "Сдать лабу по алгоритмам (графы)", false, "17.03.2026"),

        ViewTyped.Note("Новая Forza", "Завтра выходит Forza Horizon 6 хз надо поиграть наверное придётся удалить танки", true, "18.03.2026"),
        ViewTyped.Note("Стоп", "Пока хватит, потом допишу остальные.", true, "18.03.2026"),
        ViewTyped.Note("Зал", "День ног (приседания до талого)", false, "18.03.2026"),
        ViewTyped.Note("Pet-project", "Spotify-style dark theme: подобрать HEX цвета фона", false, "18.03.2026"),
        ViewTyped.Note("Уборка", "Помыть посуду, а то завалило всё нахуй", false, "18.03.2026"),

        ViewTyped.Note("Экзамены", "Если замтека видна, то сесси уже близко...", false, "19.03.2026"),
        ViewTyped.Note("Forza 6", "Поставить на закачку с утра, чтобы к вечеру была", false, "19.03.2026"),
        ViewTyped.Note("Собес", "Повторить Coroutines: разница между launch и async", false, "19.03.2026"),
        ViewTyped.Note("Танки", "Удалить всё-таки эту помойку, место нужно", true, "19.03.2026"),

        ViewTyped.Note("универ", "завтра надо приехать к 4 паре (хотя бы приехать)", false, "20.03.2026"),
        ViewTyped.Note("Flow", "StateFlow vs SharedFlow: дописать доку в Notion", false, "20.03.2026"),
        ViewTyped.Note("Зал", "Грудь и трицепс. Жать от души", false, "20.03.2026"),
        ViewTyped.Note("Проект", "Добавить анимацию перехода через MotionLayout", false, "20.03.2026"),
        ViewTyped.Note("Вечер", "Пиво или отдых? Наверное, отдых", true, "20.03.2026"),

        ViewTyped.Note("Учеба", "Посмотреть лекцию, которую проспал во вторник", false, "21.03.2026"),
        ViewTyped.Note("GitHub", "Запушить последние фиксы в репу Конструктора Печей", true, "21.03.2026"),
        ViewTyped.Note("Forza", "Ночной дрифт на новых картах", false, "21.03.2026"),
        ViewTyped.Note("Питание", "Закупиться едой на неделю вперед", false, "21.03.2026"),
        ViewTyped.Note("Сон", "Выспаться хотя бы до 11", false, "21.03.2026"),

        ViewTyped.Note("Рефактор", "Вынести константы в отдельный файл", false, "22.03.2026"),
        ViewTyped.Note("Зал", "Растяжка и бассейн", true, "22.03.2026"),
        ViewTyped.Note("ФПМИ", "Подготовить отчет по практике", false, "22.03.2026"),
        ViewTyped.Note("Отдых", "Чисто залипнуть в ютуб под вечер", false, "22.03.2026")
    )

    fun getExpandedNotes() : List<ViewTyped.Note> {
        val data = getNotes()
        val expandedData: MutableList<ViewTyped.Note> = mutableListOf()
        for(i in 1..100) {
            expandedData.addAll(data.toList())
        }
        return expandedData
    }
}