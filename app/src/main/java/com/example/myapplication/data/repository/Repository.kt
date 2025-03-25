package com.example.myapplication.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.data.model.Recipe
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class Repository {

    private val firestore = FirebaseFirestore.getInstance()
    private val savedRecipesCollection = firestore.collection("saved_recipes")

    fun saveRecipe(userId: String, recipe: Recipe) {
        savedRecipesCollection
            .document(userId)
            .collection("recipes")
            .document(recipe.id.toString())
            .set(recipe)
            .addOnSuccessListener { Log.d("Repository", "Recipe saved successfully") }
            .addOnFailureListener { e -> Log.e("Repository", "Failed to save recipe", e) }
    }

    fun getSavedRecipes(userId: String): LiveData<List<Recipe>> {
        val liveData = MutableLiveData<List<Recipe>>()

        savedRecipesCollection
            .document(userId)
            .collection("recipes")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Repository", "Error fetching recipes", error)
                    liveData.value = emptyList()
                    return@addSnapshotListener
                }

                val recipeList = snapshot?.documents?.mapNotNull { it.toObject<Recipe>() } ?: emptyList()
                liveData.value = recipeList
            }

        return liveData
    }

    fun deleteRecipe(userId: String, recipeId: Int) {
        savedRecipesCollection
            .document(userId)
            .collection("recipes")
            .document(recipeId.toString())
            .delete()
            .addOnSuccessListener { Log.d("Repository", "Recipe deleted successfully") }
            .addOnFailureListener { e -> Log.e("Repository", "Failed to delete recipe", e) }
    }
}
