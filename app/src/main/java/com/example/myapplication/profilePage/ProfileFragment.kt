package com.example.myapplication.profilePage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.profilePage.UploadRecipeFragment
import com.example.myapplication.settingPage.SettingActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage

class ProfileFragment : Fragment() {

    private lateinit var profileImage: ImageView
    private lateinit var userName: TextView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var imageUri: Uri? = null
    private var userDocRef: DocumentReference? = null
    private var profileListener: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.profile_page, container, false)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        profileImage = view.findViewById(R.id.profile_image)
        userName = view.findViewById(R.id.user_name)
        val settingsButton = view.findViewById<ImageView>(R.id.settings_button)
        val profileEditButton = view.findViewById<ImageView>(R.id.profile_image)

        val uploadRecipeButton = view.findViewById<Button>(R.id.btn_upload_recipe)
        uploadRecipeButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, UploadRecipeFragment()) // Ensure fragment_container exists
                .addToBackStack(null)
                .commit()
        }

        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            userDocRef = firestore.collection("users").document(userId)
            listenToProfileUpdates()
        }

        settingsButton.setOnClickListener {
            val intent = Intent(requireContext(), SettingActivity::class.java)
            startActivity(intent)
        }

        profileImage.setOnClickListener {
            selectProfileImage()
        }

        profileEditButton.setOnClickListener {
            updateUserName()
        }

        return view
    }

    private fun listenToProfileUpdates() {
        userDocRef?.let { docRef ->
            profileListener = docRef.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Toast.makeText(requireContext(), "Error fetching profile!", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val name = snapshot.getString("username")
                    val profileUrl = snapshot.getString("profileImageUrl")

                    userName.text = name ?: "User"
                    profileUrl?.let {
                        Glide.with(this).load(it).into(profileImage)
                    }
                }
            }
        }
    }

    private fun selectProfileImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            imageUri?.let {
                profileImage.setImageURI(it)
                uploadProfileImage(it)
            }
        }
    }

    private fun uploadProfileImage(uri: Uri) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val storageRef = storage.reference.child("profile_images/$userId.jpg")

        storageRef.putFile(uri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                userDocRef?.update("profileImageUrl", downloadUri.toString())
                    ?.addOnSuccessListener {
                        Toast.makeText(requireContext(), "Profile Updated", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun updateUserName() {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val newName = "New User Name" // Replace with user input

        userDocRef?.update("username", newName)
            ?.addOnSuccessListener {
                userName.text = newName
                Toast.makeText(requireContext(), "Name Updated", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        profileListener?.remove()
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1001
    }
}
