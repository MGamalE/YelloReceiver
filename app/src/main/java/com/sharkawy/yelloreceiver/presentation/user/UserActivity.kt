package com.sharkawy.yelloreceiver.presentation.user

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.sharkawy.yelloreceiver.databinding.ActivityMainBinding
import com.sharkawy.yelloreceiver.entities.Status
import com.sharkawy.yelloreceiver.entities.user.User
import com.sharkawy.yelloreceiver.presentation.core.ReceiverApplication

class UserActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    private val viewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as ReceiverApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    override fun onStart() {
        super.onStart()
        setupObservers()
        fireUsersRefreshing()
        viewModel.insertUser(
            User(
                1,
                "Mohamed",
                "123598345394"
            )
        )
    }

    private fun fireUsersRefreshing() {
        binding?.swipeRefresh?.setOnRefreshListener {
            binding?.swipeRefresh?.isRefreshing = false
        }
    }

    private fun setupObservers() {
        viewModel.getUser().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        binding?.swipeRefresh?.isRefreshing = true
                        Log.w("STATUS", "LOADING")
                    }
                    Status.SUCCESS -> {
                        binding?.swipeRefresh?.isRefreshing = false
                        resource.data?.let { user ->
                            Log.e("STATUS", "{${user.phone}}")
                        }
                    }
                    Status.ERROR -> {
                        binding?.swipeRefresh?.isRefreshing = false
                        Log.w("STATUS", "ERROR")
                        Log.w("STATUS", it.message.toString())
                    }

                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}