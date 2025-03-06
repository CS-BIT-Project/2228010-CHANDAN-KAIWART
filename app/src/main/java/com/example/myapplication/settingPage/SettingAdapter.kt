package com.example.myapplication.settingPage

import android.R
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.*
import com.example.myapplication.databinding.ItemSettingHeaderBinding
import com.example.myapplication.databinding.ItemSettingSwitchBinding
import com.example.myapplication.databinding.ItemSettingThemeColorsBinding
import com.example.myapplication.databinding.ItemSettingFontSizeBinding
import com.example.myapplication.databinding.ItemSettingNavigationBinding
import com.example.myapplication.databinding.ItemSettingVersionBinding
import com.example.myapplication.databinding.ItemSettingLogoutBinding


class SettingAdapter : ListAdapter<SettingItem, RecyclerView.ViewHolder>(SettingDiffCallback()) {

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is SettingItem.Header -> VIEW_TYPE_HEADER
        is SettingItem.Switch -> VIEW_TYPE_SWITCH
        is SettingItem.ThemeColors -> VIEW_TYPE_THEME_COLORS
        is SettingItem.FontSize -> VIEW_TYPE_FONT_SIZE
        is SettingItem.Navigation -> VIEW_TYPE_NAVIGATION
        is SettingItem.Dropdown -> VIEW_TYPE_DROPDOWN
        is SettingItem.Version -> VIEW_TYPE_VERSION
        is SettingItem.Logout -> VIEW_TYPE_LOGOUT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            VIEW_TYPE_HEADER -> HeaderViewHolder(ItemSettingHeaderBinding.inflate(inflater, parent, false))
            VIEW_TYPE_SWITCH -> SwitchViewHolder(ItemSettingSwitchBinding.inflate(inflater, parent, false))
            VIEW_TYPE_THEME_COLORS -> ThemeColorViewHolder(ItemSettingThemeColorsBinding.inflate(inflater, parent, false))
            VIEW_TYPE_FONT_SIZE -> FontSizeViewHolder(ItemSettingFontSizeBinding.inflate(inflater, parent, false))
            VIEW_TYPE_NAVIGATION -> NavigationViewHolder(ItemSettingNavigationBinding.inflate(inflater, parent, false))
            VIEW_TYPE_DROPDOWN -> DropdownViewHolder(ItemSettingDropdownBinding.inflate(inflater, parent, false))
            VIEW_TYPE_VERSION -> VersionViewHolder(ItemSettingVersionBinding.inflate(inflater, parent, false))
            VIEW_TYPE_LOGOUT -> LogoutViewHolder(ItemSettingLogoutBinding.inflate(inflater, parent, false))
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is SettingItem.Header -> (holder as HeaderViewHolder).bind(item)
            is SettingItem.Switch -> (holder as SwitchViewHolder).bind(item)
            is SettingItem.ThemeColors -> (holder as ThemeColorViewHolder).bind(item)
            is SettingItem.FontSize -> (holder as FontSizeViewHolder).bind(item)
            is SettingItem.Navigation -> (holder as NavigationViewHolder).bind(item)
            is SettingItem.Dropdown -> (holder as DropdownViewHolder).bind(item)
            is SettingItem.Version -> (holder as VersionViewHolder).bind(item)
            is SettingItem.Logout -> (holder as LogoutViewHolder).bind()
            is SettingItem.Dropdown -> TODO()
        }
    }

    class HeaderViewHolder(private val binding: ItemSettingHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SettingItem.Header) {
            binding.headerTitle.text = item.title
        }
    }

    class SwitchViewHolder(private val binding: ItemSettingSwitchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SettingItem.Switch) {
            binding.switchTitle.text = item.title
            binding.switchIcon.setImageResource(item.icon)
            binding.settingSwitch.isChecked = item.isChecked
        }
    }

    class ThemeColorViewHolder(private val binding: ItemSettingThemeColorsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SettingItem.ThemeColors) {
            binding.themeLabel.text = item.title// Ensure ID matches XML
        }
    }

    class FontSizeViewHolder(private val binding: ItemSettingFontSizeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SettingItem.FontSize) {
            binding.fontSizeValue.text = item.size.toString()
        }
    }

    class DropdownViewHolder(private val binding: ItemSettingDropdownBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SettingItem.Dropdown) {
            binding.dropdownTitle.text = item.title
            binding.dropdownIcon.setImageResource(item.icon)
            val adapter = ArrayAdapter(binding.root.context, R.layout.simple_spinner_item, item.options)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.dropdownSpinner.adapter = adapter
        }
    }


    class NavigationViewHolder(private val binding: ItemSettingNavigationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SettingItem.Navigation) {
            binding.navTitle.text = item.title
            binding.navIcon.setImageResource(item.icon)
        }
    }


    class VersionViewHolder(private val binding: ItemSettingVersionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SettingItem.Version) {
            binding.versionText.text = "Version ${item.version}"
        }
    }

    class LogoutViewHolder(binding: ItemSettingLogoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {}
    }

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_SWITCH = 1
        private const val VIEW_TYPE_THEME_COLORS = 2
        private const val VIEW_TYPE_FONT_SIZE = 3
        private const val VIEW_TYPE_NAVIGATION = 4
        private const val VIEW_TYPE_DROPDOWN = 5
        private const val VIEW_TYPE_VERSION = 6
        private const val VIEW_TYPE_LOGOUT = 7
    }
}
