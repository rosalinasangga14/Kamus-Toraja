package id.kamus.toraya

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import id.kamus.toraya.R
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {
    private lateinit var dataManager: FirebaseDictionaryDataManager
    private lateinit var adapter: DictionaryDataAdapter
    private lateinit var searchResultAdapter: ArrayAdapter<String>

    private lateinit var editTextKataToraja: EditText
    private lateinit var tvSearchResult: TextView
    private lateinit var btnCloseResult: Button
    private lateinit var listViewKamus: ListView
    private lateinit var switchLanguage: SwitchMaterial

    private val INPUT_ACTIVITY_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataManager = FirebaseDictionaryDataManager()

        editTextKataToraja = findViewById(R.id.editTextKataToraja)
        tvSearchResult = findViewById(R.id.tvSearchResult)
        btnCloseResult = findViewById(R.id.btnCloseResult)
        listViewKamus = findViewById(R.id.listViewKamus)
        switchLanguage = findViewById(R.id.switchLanguage)

        val tambahKataButton: Button = findViewById(R.id.tambahKataButton)
        tambahKataButton.setOnClickListener {
            val intent = Intent(this, InputActivity::class.java)
            startActivityForResult(intent, INPUT_ACTIVITY_REQUEST_CODE)
        }

        // Menyembunyikan tombol "Tambah Kata"
        tambahKataButton.visibility = View.GONE


        switchLanguage.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Switch dalam mode "Bahasa Toraja ke Indonesia"
                switchLanguage.text = "Toraja-Indonesia"
                editTextKataToraja.hint = "Masukkan Kata Dalam Bahasa Toraja"
                updateDictionaryData(true)
            } else {
                // Switch dalam mode "Indonesia ke Bahasa Toraja"
                switchLanguage.text = "Indonesia-Toraja"
                editTextKataToraja.hint = "Masukkan Kata Dalam Bahasa Indonesia"
                updateDictionaryData(false)
            }
        }

        val btnSearch: Button = findViewById(R.id.btnSearch)
        btnSearch.setOnClickListener {
            val kataPencarian = editTextKataToraja.text.toString()
            if (switchLanguage.isChecked) {
                // Switch dalam mode "Bahasa Toraja ke Indonesia"
                dataManager.getTranslation(kataPencarian) { hasilPencarian: String? ->
                    if (hasilPencarian != null) {
                        tvSearchResult.text = "Hasil Pencarian:\n$hasilPencarian"
                    } else {
                        tvSearchResult.text = "Kata Toraja tidak ditemukan."
                    }
                    // Setelah tombol cari ditekan, baru tampilkan hasil
                    tvSearchResult.visibility = View.VISIBLE
                    btnCloseResult.visibility = View.VISIBLE
                    listViewKamus.visibility = View.GONE
                }
            } else {
                // Switch dalam mode "Indonesia ke Bahasa Toraja"
                dataManager.getTranslationToToraja(kataPencarian) { hasilTerjemahan: String? ->
                    if (hasilTerjemahan != null) {
                        tvSearchResult.text = "Hasil Terjemahan:\n$hasilTerjemahan"
                    } else {
                        tvSearchResult.text = "Kata Indonesia tidak ditemukan."
                    }
                    // Setelah tombol cari ditekan, baru tampilkan hasil
                    tvSearchResult.visibility = View.VISIBLE
                    btnCloseResult.visibility = View.VISIBLE
                    listViewKamus.visibility = View.GONE
                }
            }
        }

        btnCloseResult.setOnClickListener {
            tvSearchResult.visibility = View.GONE
            btnCloseResult.visibility = View.GONE
            listViewKamus.visibility = View.VISIBLE
        }

        dataManager.getAllWords { kamus ->
            adapter = DictionaryDataAdapter(this, kamus)
            listViewKamus.adapter = adapter
        }
    }

    private fun updateDictionaryData(fromTorajaToIndonesia: Boolean) {
        dataManager.getAllWords { kamus ->
            adapter.updateData(kamus)
        }
    }

}
