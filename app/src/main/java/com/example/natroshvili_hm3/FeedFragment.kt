package com.example.natroshvili_hm3

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FeedFragment : Fragment(R.layout.fragment_feed) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Tweets")

        getData()
    }

    private fun getData() {
        reference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<Tweets>()
                for (data in snapshot.children) {
                    val model = data.getValue(Tweets::class.java)
                    list.add(model as Tweets)
                }
                if (list.size > 0) {
                    val adapter = RecyclerViewTweetAdapter(list)
                    recyclerView.adapter = adapter
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

}