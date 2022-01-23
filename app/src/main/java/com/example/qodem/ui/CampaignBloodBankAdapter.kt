package com.example.qodem.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.qodem.databinding.ItemBloodBankBinding
import com.example.qodem.model.BloodBank

class CampaignBloodBankAdapter : RecyclerView.Adapter<CampaignBloodBankAdapter.CampaignBloodBankViewHolder>() {

    inner class CampaignBloodBankViewHolder(val binding: ItemBloodBankBinding): RecyclerView.ViewHolder(binding.root)

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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampaignBloodBankViewHolder {
        return CampaignBloodBankViewHolder(
            ItemBloodBankBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        ))
    }

    override fun onBindViewHolder(holder: CampaignBloodBankViewHolder, position: Int) {
        holder.binding.apply {
            val bloodBank = bloodBanks[position]
            textBloodBank.text = bloodBank.name_en
            imagePhoneNumber.visibility = View.GONE
            textAddTime.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return bloodBanks.size
    }
}