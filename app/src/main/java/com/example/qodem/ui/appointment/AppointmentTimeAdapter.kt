package com.example.qodem.ui.appointment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.qodem.R
import com.example.qodem.databinding.ItemTimeDateBinding
import com.example.qodem.model.AppointmentTime
import java.util.*

class AppointmentTimeAdapter(private val listener: OnItemClickListener) :
    ListAdapter<AppointmentTime, AppointmentTimeAdapter.TimeViewHolder>(DiffCallBack()) {

    inner class TimeViewHolder(val binding: ItemTimeDateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val appointmentTime = getItem(position)
                    listener.onTimeItemClick(appointmentTime)
                    notifyDataSetChanged()
                }
            }
        }

        fun bind(appointmentTime: AppointmentTime) {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            binding.apply {
                calendar.time = Date(appointmentTime.timeInMilli)
                val amPmString =
                    calendar.getDisplayName(Calendar.AM_PM, Calendar.LONG, Locale.ENGLISH)
                val hour = if (calendar.get(Calendar.HOUR) == 0) {
                    12
                } else {
                    calendar.get(Calendar.HOUR)
                }
                val donationTime = "${String.format("%02d", hour)}:" +
                        String.format("%02d", calendar.get(Calendar.MINUTE))
                Log.d("hereTime", "Time: $donationTime")
                textTime.text = donationTime
                textAmPm.text = amPmString
                if (appointmentTime.isSelected) {
                    cardViewTime.strokeColor =
                        ContextCompat.getColor(cardViewTime.context, R.color.primaryColor)
                } else {
                    cardViewTime.strokeColor =
                        ContextCompat.getColor(cardViewTime.context, R.color.secondaryColor)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val binding =
            ItemTimeDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class DiffCallBack : DiffUtil.ItemCallback<AppointmentTime>() {
        override fun areItemsTheSame(oldItem: AppointmentTime, newItem: AppointmentTime): Boolean {
            return oldItem.timeInMilli == newItem.timeInMilli
        }

        override fun areContentsTheSame(
            oldItem: AppointmentTime,
            newItem: AppointmentTime
        ): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemClickListener {
        fun onTimeItemClick(appointmentTime: AppointmentTime)
    }
}