package id.kamus.toraya

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentReference

class InputViewModel : ViewModel() {
    // MutableLiveData untuk mengamati hasil operasi
    val result: MutableLiveData<Boolean> = MutableLiveData()

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun simpanData(kataToraja: String, terjemahan: String) {
        val entry = hashMapOf(
            "kataToraja" to kataToraja,
            "terjemahan" to terjemahan
        )

        firestore.collection("kamus")
            .add(entry)
            .addOnCompleteListener { task: Task<DocumentReference> ->
                if (task.isSuccessful) {
                    // Jika berhasil, set nilai MutableLiveData menjadi true
                    result.value = true
                } else {
                    // Jika gagal, set nilai MutableLiveData menjadi false
                    result.value = false
                }
            }
            .addOnFailureListener { e ->
                // Handle kesalahan jika ada
                result.value = false
                // Di sini Anda bisa menambahkan logging atau pengiriman laporan kesalahan
            }
    }

    override fun onCleared() {
        super.onCleared()
        // Pastikan Anda membersihkan referensi Firestore saat ViewModel tidak lagi digunakan
        firestore.clearPersistence()
    }
}
