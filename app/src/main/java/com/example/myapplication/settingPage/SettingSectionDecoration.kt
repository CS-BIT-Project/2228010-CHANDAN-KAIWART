package com.example.myapplication.settingPage

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SettingSectionDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) return

        outRect.bottom = spaceHeight
    }
}
