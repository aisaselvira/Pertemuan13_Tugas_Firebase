package com.example.pertemuan13_tugas_firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.pertemuan13_tugas_firebase.databinding.ActivityUpdateBinding
import com.google.firebase.firestore.FirebaseFirestore

class UpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val complaintCollectionRef = firestore.collection("complaints")
    private var updateId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            val id = intent.getStringExtra(ComplaintActivity.EXTRA_ID)
            if (id.isNullOrEmpty()) {
                Log.d("UpdateActivity", "Invalid ID: $id")
                finish()
            }

            val name = intent.getStringExtra(ComplaintActivity.EXTRA_NAME)
            val title = intent.getStringExtra(ComplaintActivity.EXTRA_TITLE)
            val detail = intent.getStringExtra(ComplaintActivity.EXTRA_DETAIL)

            txtName.setText(name)
            txtTitle.setText(title)
            txtDetail.setText(detail)

            btnUpdate.setOnClickListener {
                val updatedData = Complaint(
                    id = id.toString(),
                    name = txtName.text.toString(),
                    title = txtTitle.text.toString(),
                    details = txtDetail.text.toString()
                )
                updateData(updatedData)
            }

            btnDelete.setOnClickListener {
                val complaintToDelete = Complaint(id = id.toString(), "", "", "")
                deleteData(complaintToDelete)
            }

            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun updateData(complaint: Complaint) {
        complaintCollectionRef.document(complaint.id).set(complaint)
            .addOnSuccessListener {
                Log.d("UpdateActivity", "The data has been updated successfully")
                finish()
            }
            .addOnFailureListener {
                Log.d("UpdateActivity", "An error occurred while updating a complaint", it)
            }
    }

    private fun deleteData(complaint: Complaint) {
        if (complaint.id.isEmpty()) {
            Log.d("UpdateActivity", "An error occurred while deleting empty ID data")
            return
        }

        complaintCollectionRef.document(complaint.id).delete()
            .addOnSuccessListener {
                Log.d("UpdateActivity", "The data has been deleted successfully")
                finish()
            }
            .addOnFailureListener {
                Log.d("UpdateActivity", "An error occurred while deleting complaint data", it)
            }
    }
}
