package com.example.pertemuan13_tugas_firebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pertemuan13_tugas_firebase.databinding.ActivityComplaintBinding
import com.google.firebase.firestore.FirebaseFirestore

class ComplaintActivity : AppCompatActivity() {
    private lateinit var binding: ActivityComplaintBinding
    private lateinit var itemAdapter: CompplaintAdapter
    private val listViewData = ArrayList<Complaint>()
    private val firestore = FirebaseFirestore.getInstance()
    private val complaintCollectionRef = firestore.collection("complaints")
    private var updateId = ""
    private val complaintListLiveData: MutableLiveData<List<Complaint>> by lazy {
        MutableLiveData<List<Complaint>>()
    }

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DETAIL = "extra_detail"
        const val EXTRA_ID = "extra_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComplaintBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        configureButtonClickHandlers()
        updateComplaintListOnTracking()
        getAllComplaints()
    }

    private fun setupRecyclerView() {
        itemAdapter = CompplaintAdapter(this, listViewData) { item ->
            updateId = item.id
            val intentToForm = Intent(this, UpdateActivity::class.java)
                .apply {
                    putExtra(EXTRA_NAME, item.name)
                    putExtra(EXTRA_TITLE, item.title)
                    putExtra(EXTRA_DETAIL, item.details)
                    putExtra(EXTRA_ID, item.id)
                }
            startActivity(intentToForm)
        }

        binding.rvComplaint.apply {
            layoutManager = LinearLayoutManager(this@ComplaintActivity)
            adapter = itemAdapter
        }
    }

    private fun configureButtonClickHandlers() {
        binding.btnAdd.setOnClickListener {
            val intentToForm = Intent(this@ComplaintActivity, MainActivity::class.java)
            startActivity(intentToForm)
        }
    }

    private fun getAllComplaints() {
        trackComplaintChanges()
    }

    private fun trackComplaintChanges() {
        complaintCollectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.d("ComplaintActivity", "An error occurred while changing the complaint: ", error)
                return@addSnapshotListener
            }
            val complaints = snapshot?.toObjects(Complaint::class.java)
            if (complaints != null) {
                complaintListLiveData.postValue(complaints)
            }
        }
    }

    private fun updateComplaintListOnTracking() {
        complaintListLiveData.observe(this) { complaints ->
            listViewData.clear()
            listViewData.addAll(complaints)
            itemAdapter.notifyDataSetChanged()

            Log.d("ComplaintActivity", "Number of complaints: ${complaints.size}")
        }
    }

    override fun onResume() {
        super.onResume()
        getAllComplaints()
    }
}
