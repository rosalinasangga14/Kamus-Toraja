package id.kamus.toraya

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import id.kamus.toraya.R

class DictionaryDataAdapter(private val context: Context, private val kamus: MutableList<DictionaryEntry>) : BaseAdapter() {

    override fun getCount(): Int {
        return kamus.size
    }

    override fun getItem(position: Int): Any {
        return kamus[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val entry = getItem(position) as DictionaryEntry

        viewHolder.kataTorajaTextView.text = entry.kataToraja
        viewHolder.terjemahanTextView.text = entry.terjemahan

        return view
    }

    fun updateData(newData: List<DictionaryEntry>) {
        kamus.clear()
        kamus.addAll(newData)
        notifyDataSetChanged()
    }

    private class ViewHolder(view: View) {
        val kataTorajaTextView: TextView = view.findViewById(R.id.textViewKataToraja)
        val terjemahanTextView: TextView = view.findViewById(R.id.textViewTerjemahan)
    }
}

