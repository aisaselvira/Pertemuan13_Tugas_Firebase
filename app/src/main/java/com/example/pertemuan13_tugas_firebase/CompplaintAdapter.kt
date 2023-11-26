package com.example.pertemuan13_tugas_firebase

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pertemuan13_tugas_firebase.databinding.ComplaintListBinding

class CompplaintAdapter (private val context: Context, private val listData: List<Complaint>, private val onClickData: (Complaint) -> Unit) :
    RecyclerView.Adapter<CompplaintAdapter.ItemDataViewHolder>() {

    inner class ItemDataViewHolder(private val binding: ComplaintListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Complaint) {
            with(binding) {
                nameList.text = "Name : " + data.name
                titleList.text = "Title Complaint : " + data.title
                detailList.text = "Detail Complaint : " + data.details
                itemView.setOnClickListener {
                    onClickData(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDataViewHolder {
        val binding = ComplaintListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ItemDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemDataViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size
}
