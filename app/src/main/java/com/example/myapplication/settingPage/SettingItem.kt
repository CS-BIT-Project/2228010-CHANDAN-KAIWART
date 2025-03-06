package com.example.myapplication.settingPage

sealed class SettingItem {
    data class Header(val title: String) : SettingItem()
    data class Switch(val title: String, val icon: Int, var isChecked: Boolean) : SettingItem()
    data class ThemeColors(val title: String, val options: List<String>) : SettingItem()
    data class FontSize(val size: Float) : SettingItem()
    data class Navigation(val title: String, val icon: Int) : SettingItem()
    data class Dropdown(val title: String, val icon: Int, val options: List<String>) : SettingItem()
    data class Version(val version: String) : SettingItem()
    object Logout : SettingItem()
}
