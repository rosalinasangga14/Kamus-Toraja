package id.kamus.toraya

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import id.kamus.toraya.R

class InputActivity : AppCompatActivity() {
    private lateinit var editTextKataToraja: EditText
    private lateinit var editTextTerjemahan: EditText
    private lateinit var switchLanguage: Switch

    private val viewModel: InputViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        editTextKataToraja = findViewById(R.id.editTextKataToraja)
        editTextTerjemahan = findViewById(R.id.editTextTerjemahan)
        switchLanguage = findViewById(R.id.switchLanguage)

        switchLanguage.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Switch ke Bahasa Indonesia
                editTextTerjemahan.hint = "Terjemahan Bahasa Indonesia"
            } else {
                // Switch ke Bahasa Toraja
                editTextTerjemahan.hint = "Terjemahan Bahasa Toraja"
            }
        }

        val buttonSimpan: Button = findViewById(R.id.buttonSimpan)
        buttonSimpan.setOnClickListener {
            val kataToraja = editTextKataToraja.text.toString()
            val terjemahan = editTextTerjemahan.text.toString()

            viewModel.simpanData(kataToraja, terjemahan)
        }

        // Observer untuk menanggapi hasil operasi yang dilakukan di latar belakang
        viewModel.result.observe(this, Observer { result ->
            if (result) {
                Toast.makeText(this, "Data berhasil disimpan di Firebase.", Toast.LENGTH_SHORT).show()

                val resultIntent = Intent()
                setResult(Activity.RESULT_OK, resultIntent)

                editTextKataToraja.text.clear()
                editTextTerjemahan.text.clear()
            } else {
                Toast.makeText(this, "Gagal menyimpan data di Firebase.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
