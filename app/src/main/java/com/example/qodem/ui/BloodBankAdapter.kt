package com.example.qodem.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.qodem.R
import com.example.qodem.databinding.ItemBloodBankBinding
import com.example.qodem.model.BloodBank

class BloodBankAdapter(private val listener: OnBloodBankItemClickListener) :
    ListAdapter<BloodBank, BloodBankAdapter.BloodBankViewHolder>(DiffCallBack()) {

    inner class BloodBankViewHolder(private val binding: ItemBloodBankBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val bloodBank = getItem(position)
                        listener.onItemClick(bloodBank)
                        notifyDataSetChanged()
                    }
                }
                imagePhoneNumber.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val bloodBank = getItem(position)
                        listener.onPhoneNumberImageClick(bloodBank)
                    }
                }
                imageBloodBankPlace.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val bloodBank = getItem(position)
                        listener.onBloodBankPlaceImageClick(bloodBank)
                    }
                }
            }
        }

        fun bind(bloodBank: BloodBank) {
            binding.apply {
                textBloodBank.text = bloodBank.name_en
                imagePhoneNumber.visibility = View.VISIBLE
                textAddTime.visibility = View.GONE
                if (bloodBank.isSelected) {
                    cardView.strokeColor =
                        ContextCompat.getColor(cardView.context, R.color.secondaryColor)
                    cardView.strokeWidth = 5
                } else {
                    cardView.setBackgroundColor(
                        ContextCompat.getColor(
                            cardView.context,
                            R.color.white
                        )
                    )
                    cardView.strokeColor = ContextCompat.getColor(cardView.context, R.color.white)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BloodBankViewHolder {
        val binding =
            ItemBloodBankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BloodBankViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BloodBankViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class DiffCallBack : DiffUtil.ItemCallback<BloodBank>() {
        override fun areItemsTheSame(oldItem: BloodBank, newItem: BloodBank): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BloodBank, newItem: BloodBank): Boolean {
            return oldItem == newItem
        }
    }

    interface OnBloodBankItemClickListener {
        fun onItemClick(bloodBank: BloodBank)
        fun onPhoneNumberImageClick(bloodBank: BloodBank)
        fun onBloodBankPlaceImageClick(bloodBank: BloodBank)
    }
}