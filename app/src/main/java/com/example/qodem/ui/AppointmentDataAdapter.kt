package com.example.qodem.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.qodem.databinding.ItemDayDateBinding
import com.example.qodem.model.BloodBank

class AppointmentDataAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<AppointmentDataAdapter.DateViewHolder>() {

    inner class DateViewHolder(val binding: ItemDayDateBinding): RecyclerView.ViewHolder(binding.root),
        View.OnClickListener{

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                if (v != null) {
                    when (v.id){
                        itemView.id -> {
                            listener.onItemClick(position)
                            notifyDataSetChanged()
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
        set(value) { differ.submitList(value) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        return DateViewHolder(ItemDayDateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,))
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        holder.binding.apply {
            val bloodBank = bloodBanks[position]
            textWeekDay.text = bloodBank.name_en
        }
    }

    override fun getItemCount(): Int {
        return bloodBanks.count()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}