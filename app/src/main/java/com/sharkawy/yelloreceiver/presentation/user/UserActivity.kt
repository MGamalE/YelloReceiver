package com.sharkawy.yelloreceiver.presentation.user

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.sharkawy.yelloreceiver.databinding.ActivityMainBinding
import com.sharkawy.yelloreceiver.entities.Status
import com.sharkawy.yelloreceiver.entities.user.User
import com.sharkawy.yelloreceiver.presentation.core.ReceiverApplication
import com.sharkawy.yelloreceiver.presentation.core.getNetworkIp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.ServerSocket
import java.net.Socket


class UserActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    private val viewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as ReceiverApplication).repository)
    }

    private var user: User? = null
    private lateinit var progress: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    override fun onStart() {
        super.onStart()
        setupObservers()
        fireUsersRefreshing()
        fireClickListener()
        readFromClientServer()
    }

    private fun fireClickListener() {
        binding?.displayUserBtn?.setOnClickListener {
            setupObservers()
        }
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
                        Log.w("STATUS", "SUCCESS")
                        binding?.swipeRefresh?.isRefreshing = false
                        resource.data?.let { user ->
                            writeFromClientServer()
                            binding?.userVg?.visibility = VISIBLE
                            binding?.userNameTv?.text = user.username
                            binding?.userPhoneTv?.text = user.phone
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

    private fun readFromClientServer() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val socket = Socket(getNetworkIp(this@UserActivity), 8888)
                val inStream = DataInputStream(socket.getInputStream())
                val serverMessage = inStream.readUTF()

                Handler(Looper.getMainLooper()).post {
                    Log.e("User", " ${serverMessage}")
                    user = Gson().fromJson(serverMessage, User::class.java)
                    viewModel.insertUser(
                        User(
                            user?.username,
                            user?.phone
                        )
                    )
                }

                inStream.close()
                socket.close()

            } catch (e: Exception) {
                Log.e("EXCEPTION", e.toString())
            }
        }

        if (user != null) {
            writeFromClientServer()
        }
    }

    private fun writeFromClientServer() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                Log.e("SERVER", " Create socket connection")
                val server = ServerSocket(9999)
                server.reuseAddress = true
                val serverClient: Socket = server.accept()
                val outStream = DataOutputStream(serverClient.getOutputStream())

                outStream.writeUTF(
                    "OK"
                )
                outStream.flush()

                outStream.close()
                serverClient.close()
                server.close()
            } catch (e: Exception) {
                Log.e("EXCEPTION", e.toString())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}