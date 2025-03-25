package com.example.myapplication.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ProfilePageBinding
import com.example.myapplication.loginActivity.LoginFragment
import com.example.myapplication.settingPage.SettingActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class ProfileFragment : Fragment() {

    private var _binding: ProfilePageBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private val TAG = "ProfileFragment"

    // Activity Result launcher for image selection
    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val imageUri = result.data?.data
            if (imageUri != null) {
                uploadImageToFirebase(imageUri)
                // Update the profile image immediately for better user experience
                binding.profileImageView.setImageURI(imageUri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfilePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase components
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Configure Firestore for better offline support
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()
        firestore.firestoreSettings = settings

        storage = FirebaseStorage.getInstance()

        // Set up UI interactions
        setupClickListeners()

        // Initial UI setup with "loading" state
        showLoading(true)
        binding.noSavedRecipesTextView.visibility = View.GONE

        // Load user data
        loadUserProfile()
    }

    override fun onResume() {
        super.onResume()
        // Reload user data when returning to the fragment
        if (::auth.isInitialized && ::firestore.isInitialized) {
            loadUserProfile()
        }
    }


    private fun logoutUser() {
        auth.signOut()
        navigateToLogin()
    }

    private fun loadUserProfile() {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            // User is not logged in, navigate to login screen
            Log.d(TAG, "No current user, navigating to login")
            navigateToLogin()
            return
        }

        // Show loading state
        showLoading(true)

        // Get user document with offline capabilities
        firestore.collection("users").document(currentUser.uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Document exists, use the data
                    Log.d(TAG, "User document exists: ${document.data}")

                    // Get user data
                    val username = document.getString("username") ?: currentUser.displayName ?: "User"
                    val profileImageUrl = document.getString("profileImageUrl")
                    val followersCount = document.getLong("followersCount") ?: 0
                    val followingCount = document.getLong("followingCount") ?: 0
                    val email = document.getString("email") ?: currentUser.email ?: ""

                    // Update UI with user data
                    updateUI(username, email, profileImageUrl, followersCount, followingCount)

                    // Handle saved recipes (if we have them)
                    val savedRecipes = document.get("savedRecipes") as? List<*>
                    if (savedRecipes != null && savedRecipes.isNotEmpty()) {
                        // We have saved recipes, hide the "no recipes" message
                        binding.noSavedRecipesTextView.visibility = View.GONE
                        // Future: implement recycler view adapter to show saved recipes
                    } else {
                        // No saved recipes, show the message
                        binding.noSavedRecipesTextView.visibility = View.VISIBLE
                    }

                    // Hide loading
                    showLoading(false)
                } else {
                    // Document doesn't exist, create it with data from Firebase Auth
                    Log.d(TAG, "No user document exists, creating one")

                    // Get data from Firebase Auth
                    val username = currentUser.displayName ?: "User"
                    val email = currentUser.email ?: ""
                    val profileImageUrl = currentUser.photoUrl?.toString()

                    // Create a user document in Firestore
                    createUserInFirestore(currentUser.uid, username, email)

                    // Update UI with Auth data
                    updateUI(username, email, profileImageUrl, 0, 0)

                    // Show empty state for recipes
                    binding.noSavedRecipesTextView.visibility = View.VISIBLE

                    // Hide loading
                    showLoading(false)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting user document", exception)

                // Handle offline/error case - use cached data from Auth
                val username = currentUser.displayName ?: "User"
                val email = currentUser.email ?: ""
                val profileImageUrl = currentUser.photoUrl?.toString()

                // Update UI with Auth data
                updateUI(username, email, profileImageUrl, 0, 0)

                // Show empty state for recipes
                binding.noSavedRecipesTextView.visibility = View.VISIBLE

                // Hide loading
                showLoading(false)

                // Show error toast
                Toast.makeText(context, "Failed to load profile: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUI(username: String, email: String, profileImageUrl: String?, followersCount: Long, followingCount: Long) {
        try {
            // Set username
            binding.usernameTextView.text = username

            // Set email
            binding.emailTextView.text = email

            // Set followers and following counts
            binding.followersCountTextView.text = followersCount.toString()
            binding.followingCountTextView.text = followingCount.toString()

            // Load profile image with Glide
            if (!profileImageUrl.isNullOrEmpty()) {
                Glide.with(requireContext())
                    .load(profileImageUrl)
                    .placeholder(R.drawable.ic_user)
                    .error(R.drawable.ic_user)
                    .circleCrop()
                    .into(binding.profileImageView)
            } else {
                binding.profileImageView.setImageResource(R.drawable.ic_user)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating UI", e)
        }
    }

    private fun createUserInFirestore(userId: String, username: String, email: String) {
        // Create a user object in Firestore
        val user = hashMapOf(
            "username" to username,
            "email" to email,
            "profileImageUrl" to "",
            "followersCount" to 0,
            "followingCount" to 0,
            "savedRecipes" to arrayListOf<String>(),
            "createdAt" to Timestamp.now()
        )

        // Add a new document with the user ID
        firestore.collection("users").document(userId)
            .set(user)
            .addOnSuccessListener {
                Log.d(TAG, "User document created in Firestore")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error creating user document", e)
            }
    }

    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        selectImageLauncher.launch(intent)
    }

    private fun uploadImageToFirebase(imageUri: Uri) {
        // Show loading state
        showLoading(true)

        val currentUser = auth.currentUser ?: return

        // Create a storage reference
        val storageRef = storage.reference
        val imageRef = storageRef.child("profile_images/${currentUser.uid}/${UUID.randomUUID()}")

        // Upload the file
        imageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // Get the download URL
                imageRef.downloadUrl
                    .addOnSuccessListener { downloadUri ->
                        // Update the profile image URL in Firestore
                        updateProfileImageUrl(downloadUri.toString())
                    }
                    .addOnFailureListener { exception ->
                        showLoading(false)
                        Log.e(TAG, "Failed to get download URL", exception)
                        Toast.makeText(context, "Failed to update profile image: ${exception.message}",
                            Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { exception ->
                showLoading(false)
                Log.e(TAG, "Failed to upload image", exception)
                Toast.makeText(context, "Failed to upload image: ${exception.message}",
                    Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateProfileImageUrl(imageUrl: String) {
        val currentUser = auth.currentUser ?: return

        // Update Firestore document
        firestore.collection("users").document(currentUser.uid)
            .update("profileImageUrl", imageUrl)
            .addOnSuccessListener {
                showLoading(false)
                Log.d(TAG, "Profile image URL updated")

                // Load the new image using Glide
                context?.let { ctx ->
                    Glide.with(ctx)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_user)
                        .error(R.drawable.ic_user)
                        .circleCrop()
                        .into(binding.profileImageView)
                }
            }
            .addOnFailureListener { exception ->
                showLoading(false)
                Log.e(TAG, "Failed to update profile image URL", exception)
                Toast.makeText(context, "Failed to update profile image: ${exception.message}",
                    Toast.LENGTH_SHORT).show()
            }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginFragment::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupClickListeners() {
        // Settings button
        binding.settingsButton.setOnClickListener {
            startActivity(Intent(requireContext(), SettingActivity::class.java))
        }

        // Profile image click (to change profile picture)
        binding.profileImageView.setOnClickListener {
            selectImageFromGallery()
        }

        // Edit profile button
        binding.editProfileButton.setOnClickListener {
            Toast.makeText(context, "Edit profile coming soon", Toast.LENGTH_SHORT).show()
            // Future implementation: navigate to edit profile screen
        }

    }

}