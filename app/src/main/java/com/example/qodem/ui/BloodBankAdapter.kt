package com.example.qodem.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.qodem.data.remote.BloodBank
import com.example.qodem.databinding.ItemBloodBankBinding

class BloodBankAdapter() : RecyclerView.Adapter<BloodBankAdapter.BloodBankViewHolder>() {

    inner class BloodBankViewHolder(val binding: ItemBloodBankBinding): RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<BloodBank>() {
        override fun areItemsTheSame(oldItem: BloodBank, newItem: BloodBank): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BloodBank, newItem: BloodBank): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var bloodBanks: List<BloodBank>
        get() = differ.currentList
        set(value) { differ.submitList(value) }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BloodBankViewHolder {
        return BloodBankViewHolder(ItemBloodBankBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        ))
    }

    override fun onBindViewHolder(holder: BloodBankViewHolder, position: Int) {
        holder.binding.apply {
            val bloodBank = bloodBanks[position]
            textBloodBank.text = bloodBank.name_en
        }
    }

    override fun getItemCount(): Int {
        return bloodBanks.size
    }
}