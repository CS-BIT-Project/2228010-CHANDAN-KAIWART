package com.example.myapplication.settingPage

import androidx.recyclerview.widget.DiffUtil

class SettingDiffCallback : DiffUtil.ItemCallback<SettingItem>() {
    override fun areItemsTheSame(oldItem: SettingItem, newItem: SettingItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: SettingItem, newItem: SettingItem): Boolean {
        return oldItem == newItem
    }
}
