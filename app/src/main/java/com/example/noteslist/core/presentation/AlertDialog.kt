package com.example.noteslist.core.presentation

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun showOnExitAlert(
    context: Context,
    exitActivity: () -> Unit
) {
    MaterialAlertDialogBuilder(context)
        .setTitle("Выйти?")
        .setMessage("Вы уверены, что хотите выйти из приложения?")
        .setPositiveButton("Да") { _, _ -> exitActivity() }
        .setNegativeButton("Нет", null)
        .show()
}

fun showUnsavedChangesAlert(
    context: Context,
    onConfirm: () -> Unit
) {
    MaterialAlertDialogBuilder(context)
        .setTitle("Назад?")
        .setMessage("Несохранённые данные будут потеряны.")
        .setPositiveButton("Да") { _, _ -> onConfirm() }
        .setNegativeButton("Нет",  null)
        .show()
}