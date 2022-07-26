package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityCreateOrEditPostBinding
class NewPostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCreateOrEditPostBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.edit.requestFocus()
        binding.edit.setText(intent.getStringExtra(Intent.EXTRA_TEXT))

        binding.ok.setOnClickListener {
            onOkButtonClicked(binding.edit.text?.toString() )
        }
    }

    private fun onOkButtonClicked(postContent : String?) {
        val intent = Intent()

        if(postContent.isNullOrBlank()) setResult(Activity.RESULT_CANCELED,intent)
        else {
            intent.putExtra(POST_CONTENT_EXTRA_KEY,postContent)
            setResult(Activity.RESULT_OK,intent)
        }
        finish()
    }

    private companion object {
        const val POST_CONTENT_EXTRA_KEY = "postContent"
    }

    object ResultContractCreateEdit : ActivityResultContract<String,String?>() {
        override fun createIntent(context: Context, input: String): Intent =
            Intent(context,NewPostActivity::class.java).apply {
                putExtra(Intent.EXTRA_TEXT,input)
            }

        override fun parseResult(resultCode: Int, intent: Intent?): String? {
            if (resultCode != RESULT_OK) return null
            intent ?: return null

            return intent.getStringExtra(POST_CONTENT_EXTRA_KEY)
        }
    }

}