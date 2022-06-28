package com.example.qodem.ui.appointment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.qodem.R
import com.example.qodem.databinding.ItemDayDateBinding
import com.example.qodem.model.AppointmentDay
import java.util.*

class AppointmentDayAdapter(private val listener: OnItemClickListener) :
    ListAdapter<AppointmentDay, AppointmentDayAdapter.DayViewHolder>(DiffCallBack()) {

    inner class DayViewHolder(val binding: ItemDayDateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val appointmentDay = getItem(position)
                    listener.onDayItemClick(appointmentDay)
                    notifyDataSetChanged()
                }
            }
        }

        fun bind(appointmentDay: AppointmentDay) {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            binding.apply {
                calendar.time = Date(appointmentDay.dayInMilli)
                val dayOfWeekString =
                    calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH)
                val monthString =
                    calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)
                textWeekDay.text = dayOfWeekString
                textMonthDay.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
                textMonth.text = monthString
                if (appointmentDay.isSelected) {
                    cardViewWeekDay.strokeColor =
                        ContextCompat.getColor(cardView.context, R.color.primaryColor)
                    cardViewWeekDay.setBackgroundColor(
                        ContextCompat.getColor(cardView.context, R.color.primaryLightColor)
                    )
                    cardView.strokeColor =
                        ContextCompat.getColor(cardView.context, R.color.primaryColor)
                } else {
                    cardViewWeekDay.strokeColor =
                        ContextCompat.getColor(cardView.context, R.color.secondaryColor)
                    cardViewWeekDay.setBackgroundColor(
                        ContextCompat.getColor(cardView.context, R.color.secondaryLightColor)
                    )
                    cardView.strokeColor =
                        ContextCompat.getColor(cardView.context, R.color.secondaryColor)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val binding =
            ItemDayDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class DiffCallBack : DiffUtil.ItemCallback<AppointmentDay>() {
        override fun areItemsTheSame(oldItem: AppointmentDay, newItem: AppointmentDay): Boolean {
            return oldItem.dayInMilli == newItem.dayInMilli
        }

        override fun areContentsTheSame(oldItem: AppointmentDay, newItem: AppointmentDay): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemClickListener {
        fun onDayItemClick(appointmentDay: AppointmentDay)
    }
}