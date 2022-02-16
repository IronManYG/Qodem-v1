package com.example.qodem.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.qodem.R
import com.example.qodem.databinding.ItemBloodBankBinding
import com.example.qodem.model.BloodBank

class CampaignBloodBankAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<CampaignBloodBankAdapter.CampaignBloodBankViewHolder>() {

    inner class CampaignBloodBankViewHolder(val binding: ItemBloodBankBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                if (v != null) {
                    when (v.id) {
                        itemView.id -> {
                            listener.onItemClick(position)
                        }
                    }
                }
            }
        }
    }

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
        set(value) {
            differ.submitList(value)
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampaignBloodBankViewHolder {
        return CampaignBloodBankViewHolder(
            ItemBloodBankBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )
    }

    override fun onBindViewHolder(holder: CampaignBloodBankViewHolder, position: Int) {
        holder.binding.apply {
            val bloodBank = bloodBanks[position]
            textBloodBank.text = bloodBank.name_en
            imagePhoneNumber.visibility = View.GONE
            textAddTime.visibility = View.VISIBLE
            cardView.strokeColor = ContextCompat.getColor(cardView.context, R.color.primaryDarkColor)
            cardView.strokeWidth = 5
        }
    }

    override fun getItemCount(): Int {
        return bloodBanks.size
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}