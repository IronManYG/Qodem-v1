package com.example.qodem.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.qodem.R
import com.example.qodem.databinding.ItemDayDateBinding
import com.example.qodem.model.AppointmentDay
import java.util.*

class AppointmentDayAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<AppointmentDayAdapter.DayViewHolder>() {

    inner class DayViewHolder(val binding: ItemDayDateBinding): RecyclerView.ViewHolder(binding.root),
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
                            listener.onDayItemClick(position)
                            notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<AppointmentDay>() {
        override fun areItemsTheSame(oldItem: AppointmentDay, newItem: AppointmentDay): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AppointmentDay, newItem: AppointmentDay): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var appointmentDays: List<AppointmentDay>
        get() = differ.currentList
        set(value) { differ.submitList(value) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        return DayViewHolder(ItemDayDateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,))
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        //
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        holder.binding.apply {
            val selectedDay = appointmentDays[position]
            calendar.time = Date(selectedDay.dayInMilli)
            val dayOfWeekString =
                calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH)
            val monthString =
                calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)
            textWeekDay.text = dayOfWeekString
            textMonthDay.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
            textMonth.text = monthString
            if(appointmentDays[position].isSelected){
                cardViewWeekDay.strokeColor = ContextCompat.getColor(cardView.context, R.color.primaryColor)
                cardViewWeekDay.setBackgroundColor(ContextCompat.getColor(cardView.context, R.color.primaryLightColor))
                cardView.strokeColor = ContextCompat.getColor(cardView.context, R.color.primaryColor)
            } else {
                cardViewWeekDay.strokeColor = ContextCompat.getColor(cardView.context, R.color.secondaryColor)
                cardViewWeekDay.setBackgroundColor(ContextCompat.getColor(cardView.context, R.color.secondaryLightColor))
                cardView.strokeColor = ContextCompat.getColor(cardView.context, R.color.secondaryColor)
            }
        }
    }

    override fun getItemCount(): Int {
        return appointmentDays.count()
    }

    interface OnItemClickListener {
        fun onDayItemClick(position: Int)
    }
}