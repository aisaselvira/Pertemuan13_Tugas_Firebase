package com.example.pertemuan13_tugas_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.pertemuan13_tugas_firebase.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val complaintCollectionRef = firestore.collection("complaints")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnAdd.setOnClickListener {
                val nama = txtName.text.toString()
                val titles = txtTitle.text.toString()
                val detail = txtDetail.text.toString()
                val newComplaint = Complaint(
                    name = nama,
                    title = titles,
                    details = detail,
                )
                addDataComplaint(newComplaint)
                startActivity(Intent(this@MainActivity, ComplaintActivity::class.java))
            }
            btnBack.setOnClickListener {
                startActivity(Intent(this@MainActivity, ComplaintActivity::class.java))
            }
        }
    }

    private fun getAllComplaints() {
        trackComplaintChanges()
    }

    private fun trackComplaintChanges() {
        complaintCollectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.d("MainActivity", "An error occurred while changing the complaint: ", error)
                return@addSnapshotListener
            }
            val complaints = snapshot?.toObjects(Complaint::class.java)
            if (complaints != null) {
                Log.d("MainActivity", "Complaints: $complaints")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getAllComplaints()
    }

    private fun addDataComplaint(complaint: Complaint) {
        complaintCollectionRef.add(complaint)
            .addOnSuccessListener { docRef ->
                val createComplaintId = docRef.id
                complaint.id = createComplaintId
                docRef.set(complaint)
                    .addOnFailureListener{
                        Log.d("MainActivity", "An error occurred while updating a complaint id", it)
                    }
                resetForm()
            }
            .addOnFailureListener{
                Log.d("MainActivity", "An error occurred while adding a complaint", it)
            }
    }

    private fun resetForm() {
        with(binding) {
            txtName.setText("")
            txtTitle.setText("")
            txtDetail.setText("")
        }
    }
}
