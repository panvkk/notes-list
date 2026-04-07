package com.example.noteslist.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.noteslist.R
import com.example.noteslist.databinding.ActivityMainBinding
import com.example.noteslist.presentation.fragment.NoteDetailsFragment
import com.example.noteslist.presentation.viewmodel.GlobalViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val globalViewModel: GlobalViewModel by viewModels()
    private var onBackPressedCallback: OnBackPressedCallback? = null
    private var isTablet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        isTablet = findViewById<View>(R.id.detail_container) != null
        setupBackPressedCallback()

        if(isTablet) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.detail_container, NoteDetailsFragment())
                .commit()
        }
    }

    private fun setupBackPressedCallback() {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!isTablet) {
                    val navHostFragment = supportFragmentManager
                        .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
                    val navController = navHostFragment.findNavController()

                    if (navController.previousBackStackEntry != null) {
                        if(globalViewModel.hasUnsavedChanges)
                            showUnsavedChangesAlert { navController.popBackStack() }
                        else navController.popBackStack()
                    } else {
                        showOnExitAlert()
                    }
                } else {
                    if(globalViewModel.hasUnsavedChanges)
                        showUnsavedChangesAlert { globalViewModel.selectNote(null)}
                    else {
                        if(globalViewModel.selectedNoteId.value == null)
                            showOnExitAlert()
                        else
                            globalViewModel.selectNote(null)
                    }
                }
            }
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback!!)
    }

    private fun showOnExitAlert() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Выйти?")
            .setMessage("Вы уверены, что хотите выйти из приложения?")
            .setPositiveButton("Да", { _, _ -> this.finish() })
            .setNegativeButton("Нет", null)
            .show()
    }

    private fun showUnsavedChangesAlert(onConfirm: () -> Unit) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Назад?")
            .setMessage("Несохранённые данные будут потеряны.")
            .setPositiveButton("Да", { _, _ -> onConfirm() })
            .setNegativeButton("Нет",  null)
            .show()
    }

}