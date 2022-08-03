package ru.netology.nmedia.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityAppBinding
import ru.netology.nmedia.ui.NewPostFragment.Companion.textArg


class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return@let
            }

            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if (text.isNullOrBlank()) {
                Snackbar.make(binding.root, "Content can`t be empty", Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok) {
                        finish()
                    }.show()
                return@let
            }
//            val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment  //альтернативный вариант
//            fragment.navController.navigate(R.id.newPostFragment,Bundle().apply { textArg = text })
            findNavController(R.id.nav_host_fragment).navigate(
                    R.id.newPostFragment,
                    Bundle().apply { textArg = text })
        }
    }
}