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

class BloodBankAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<BloodBankAdapter.BloodBankViewHolder>() {

    inner class BloodBankViewHolder(val binding: ItemBloodBankBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
            binding.imagePhoneNumber.setOnClickListener(this)
            binding.imageBloodBankPlace.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                if (v != null) {
                    when (v.id) {
                        itemView.id -> {
                            listener.onItemClick(position)
                            notifyDataSetChanged()
                        }
                        binding.imagePhoneNumber.id -> {
                            listener.onPhoneNumberImageClick(position)
                        }
                        binding.imageBloodBankPlace.id -> {
                            listener.onBloodBankPlaceImageClick(position)
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BloodBankViewHolder {
        return BloodBankViewHolder(
            ItemBloodBankBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )
    }

    override fun onBindViewHolder(holder: BloodBankViewHolder, position: Int) {
        holder.binding.apply {
            val bloodBank = bloodBanks[position]
            textBloodBank.text = bloodBank.name_en
            imagePhoneNumber.visibility = View.VISIBLE
            textAddTime.visibility = View.GONE
            if (bloodBanks[position].isSelected) {
                cardView.strokeColor = ContextCompat.getColor(cardView.context, R.color.secondaryColor)
                cardView.strokeWidth = 5
            } else {
                cardView.setBackgroundColor(ContextCompat.getColor(cardView.context, R.color.white))
                cardView.strokeColor = ContextCompat.getColor(cardView.context, R.color.white)
            }
        }
    }

    override fun getItemCount(): Int {
        return bloodBanks.count()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onPhoneNumberImageClick(position: Int)
        fun onBloodBankPlaceImageClick(position: Int)
    }
}