package com.mindorks.example.shimmer

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUI()
        setupAPICall()
    }

    private fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(arrayListOf())
        recyclerView.addItemDecoration(
                DividerItemDecoration(
                        recyclerView.context,
                        (recyclerView.layoutManager as LinearLayoutManager).orientation
                )
        )
        recyclerView.adapter = adapter
    }

    private fun setupAPICall() {
        AndroidNetworking.initialize(applicationContext)
        AndroidNetworking.get("https://5e510330f2c0d300147c034c.mockapi.io/users")
                .build()
                .getAsObjectList(User::class.java, object : ParsedRequestListener<List<User>> {
                    override fun onResponse(users: List<User>) {
                        shimmerFrameLayout.stopShimmerAnimation()
                        shimmerFrameLayout.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        adapter.addData(users)
                        adapter.notifyDataSetChanged()
                    }

                    override fun onError(anError: ANError) {
                        shimmerFrameLayout.visibility = View.GONE
                        Toast.makeText(this@MainActivity, "Something Went Wrong", Toast.LENGTH_LONG).show()
                    }

                })
    }

    override fun onResume() {
        super.onResume()
        shimmerFrameLayout.startShimmerAnimation()
    }

    override fun onPause() {
        shimmerFrameLayout.stopShimmerAnimation()
        super.onPause()
    }
}
