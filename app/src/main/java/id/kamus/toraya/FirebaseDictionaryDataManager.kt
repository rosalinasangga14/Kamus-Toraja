package id.kamus.toraya

import com.google.firebase.firestore.FirebaseFirestore

class FirebaseDictionaryDataManager {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun addWord(kataToraja: String, terjemahan: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val entry = hashMapOf(
            "kataToraja" to kataToraja,
            "terjemahan" to terjemahan
        )

        firestore.collection("kamus")
            .add(entry)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun getTranslation(kataToraja: String, callback: (String?) -> Unit) {
        // Referensi koleksi "kamus" di Firestore
        val kamusCollection = firestore.collection("kamus")

        // Mencari dokumen dengan kataToraja yang sesuai
        kamusCollection.whereEqualTo("kataToraja", kataToraja)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    // Jika dokumen tidak ditemukan, panggil callback dengan null
                    callback(null)
                } else {
                    // Jika dokumen ditemukan, ambil terjemahan dari dokumen pertama
                    val terjemahan = result.documents[0].getString("terjemahan")
                    callback(terjemahan)
                }
            }
            .addOnFailureListener { exception ->
                // Handle kesalahan jika ada
                callback(null)
            }
    }

    fun getTranslationToToraja(kataIndonesia: String, callback: (String?) -> Unit) {
        // Referensi koleksi "kamus" di Firestore
        val kamusCollection = firestore.collection("kamus")

        // Mencari dokumen dengan terjemahan yang sesuai dengan kata Indonesia
        kamusCollection.whereEqualTo("terjemahan", kataIndonesia)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    // Jika dokumen tidak ditemukan, panggil callback dengan null
                    callback(null)
                } else {
                    // Jika dokumen ditemukan, ambil kata Toraja dari dokumen pertama
                    val kataToraja = result.documents[0].getString("kataToraja")
                    callback(kataToraja)
                }
            }
            .addOnFailureListener { exception ->
                // Handle kesalahan jika ada
                callback(null)
            }
    }

    fun getAllWords(callback: (MutableList<DictionaryEntry>) -> Unit) {
        // Referensi koleksi "kamus" di Firestore
        val kamusCollection = firestore.collection("kamus")

        // Mendapatkan seluruh dokumen dari koleksi, diurutkan berdasarkan kataToraja
        kamusCollection.orderBy("kataToraja").get()
            .addOnSuccessListener { result ->
                val kamusList = mutableListOf<DictionaryEntry>()

                for (document in result) {
                    // Mapping dokumen Firestore ke objek DictionaryEntry
                    val kataToraja = document.getString("kataToraja") ?: ""
                    val terjemahan = document.getString("terjemahan") ?: ""

                    val entry = DictionaryEntry(kataToraja, terjemahan)
                    kamusList.add(entry)
                }

                // Panggil callback dengan daftar kamus yang diurutkan
                callback(kamusList)
            }
            .addOnFailureListener { exception ->
                // Handle kesalahan jika ada
                callback(mutableListOf())  // atau callback(null) tergantung kebutuhan
            }
    }
}
