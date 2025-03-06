package com.example.myapplication.recipes

import android.os.Bundle
import androidx.fragment.app.Fragment

class FeaturedRecipeFragment : Fragment() {

    companion object {
        fun newInstance(recipe: Recipe): FeaturedRecipeFragment {
            val fragment = FeaturedRecipeFragment()
            val args = Bundle().apply {
                putInt("id", recipe.id)
                putString("title", recipe.title)
                putString("image", recipe.image)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
