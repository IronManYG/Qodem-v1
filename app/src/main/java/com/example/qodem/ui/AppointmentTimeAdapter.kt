package com.example.qodem.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.qodem.databinding.ItemTimeDateBinding
import com.example.qodem.model.AppointmentDay
import com.example.qodem.model.AppointmentTime


class AppointmentTimeAdapter (private val listener: OnItemClickListener) : RecyclerView.Adapter<AppointmentTimeAdapter.TimeViewHolder>(){

    inner class TimeViewHolder(val binding: ItemTimeDateBinding): RecyclerView.ViewHolder(binding.root),
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
                            listener.onTimeItemClick(position)
                            notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<AppointmentTime>() {
        override fun areItemsTheSame(oldItem: AppointmentTime, newItem: AppointmentTime): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AppointmentTime, newItem: AppointmentTime): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var appointmentTimes: List<AppointmentTime>
        get() = differ.currentList
        set(value) { differ.submitList(value) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        return TimeViewHolder(
            ItemTimeDateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,))
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        holder.binding.apply {

        }
    }

    override fun getItemCount(): Int {
        return appointmentTimes.count()
    }

    interface OnItemClickListener {
        fun onTimeItemClick(position: Int)
    }
}