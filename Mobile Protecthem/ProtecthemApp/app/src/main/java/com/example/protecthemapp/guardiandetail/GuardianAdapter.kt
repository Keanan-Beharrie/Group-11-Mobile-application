package com.example.protecthemapp.guardiandetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.protecthemapp.R
import com.example.protecthemapp.database.Guardian
import com.example.protecthemapp.databinding.ListViewBinding

class GuardianAdapter(private val guardians: List<Guardian>) :
    RecyclerView.Adapter<GuardianAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val guardian = guardians[position]
        holder.bind(guardian)
    }

    override fun getItemCount(): Int {
        return guardians.size
    }

    inner class ViewHolder(private val binding: ListViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(guardian: Guardian) {
            binding.textName.text = guardian.guardianName
            binding.textRelation.text = guardian.guardianRelation
            binding.textPhone.text = guardian.guardianPhoneNo
            binding.textEmail.text = guardian.guardianEmail
        }
    }
}




