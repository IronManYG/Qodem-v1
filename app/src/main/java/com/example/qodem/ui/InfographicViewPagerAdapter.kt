package com.example.qodem.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.qodem.databinding.ItemInfographicBinding

class InfographicViewPagerAdapter : RecyclerView.Adapter<InfographicViewPagerAdapter.InfographicViewHolder>() {

    inner class InfographicViewHolder(val binding: ItemInfographicBinding): RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var infographics: List<Int>
        get() = differ.currentList
        set(value) { differ.submitList(value) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfographicViewHolder {
        return InfographicViewHolder(
            ItemInfographicBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: InfographicViewHolder, position: Int) {
        holder.binding.apply {
            imageInfographic.setImageResource(infographics[position])
        }
    }

    override fun getItemCount(): Int {
       return infographics.size
    }
}