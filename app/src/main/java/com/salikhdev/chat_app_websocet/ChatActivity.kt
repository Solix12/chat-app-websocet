package com.salikhdev.chat_app_websocet

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.salikhdev.chat_app_websocet.ViewModel.MessageAdapter
import com.salikhdev.chat_app_websocet.ViewModel.MessageViewModel
import com.salikhdev.chat_app_websocet.databinding.ActivityChatBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class ChatActivity : AppCompatActivity() {


    private val binding by viewBinding(ActivityChatBinding::bind)
    private val model: MessageViewModel by viewModels()

    private lateinit var client: OkHttpClient
    private lateinit var listener: EchoWebSocketListener

    private var nameUser: String? = null

    private var ws: WebSocket? = null

    private var sizeData = 0

    private lateinit var data: List<String>


    private var adapter: MessageAdapter? = null


    private inner class EchoWebSocketListener : WebSocketListener() {

        private val NORMAL_CLOSURE_STATUS = 1000

        override fun onOpen(webSocket: WebSocket, response: Response) {
            webSocket.send("Admin : Hello $nameUser")
            output("Admin : Hello $nameUser")

        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            output(text)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            // output("Receiving bytes : " + bytes.hex());
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null)
            output("Closing : $code / $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            output("Error : " + t.message)
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        setState()
        setWeb()

    }

    private fun setWeb() {

        data = ArrayList()
        nameUser = intent.getStringExtra("Name")
        binding.userName.text = "Your nick : $nameUser"

        adapter = MessageAdapter(data, nameUser!!)

        model.getHeroes().observe(this) {

            adapter!!.setData(it)
            adapter!!.notifyDataSetChanged()
            sizeData = adapter!!.getData().size

        }

        binding.recycler.adapter = adapter
        client = OkHttpClient()
        start()

    }

    private fun setState() {

        binding.recycler.setHasFixedSize(true)
        binding.recycler.layoutManager = LinearLayoutManager(this)

    }

    private fun start() {
        val request: Request = Request.Builder().url(
            "wss://connect.websocket.in/v2/1998?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjUyMmY0NGI3MWQzZmYzOTk5NGZmZjg3NDhjN2U3Yjk0NGMxOWE1NTkzMWRmOGFhZDk0N2EyMjYzYTc4NGYzMzA2MGI1Mzc5YmE1ZDlhMWU4In0.eyJhdWQiOiI4IiwianRpIjoiNTIyZjQ0YjcxZDNmZjM5OTk0ZmZmODc0OGM3ZTdiOTQ0YzE5YTU1OTMxZGY4YWFkOTQ3YTIyNjNhNzg0ZjMzMDYwYjUzNzliYTVkOWExZTgiLCJpYXQiOjE1ODI5Njk1MjEsIm5iZiI6MTU4Mjk2OTUyMSwiZXhwIjoxNjE0NTkxOTIxLCJzdWIiOiI2MDMiLCJzY29wZXMiOltdfQ.hxNCiERXZrDgPbN4GtPqpUAOeEKqdwIDaCkznEUDuLD2lChmBsFpqOYwIXUKexp262MsflCeJvZcV7n6iccE93emV-6b6YvFmwCkMzEd0ekHO-XgP0xIA7CTqxMh38GH-OowKer9APoynkMco1iEVADhiy7O5D-qhnxcTMRrr5Ui3uy36SbBRfkDS09Cmuo-pk4eRfMQtQkCGV7IQluv4wbxKlwCjRd_Xhx2sQ2g6PIJvSttuany8ewdYb4HKLnrPX3KcthlP3gdkrYOLIoMoeTXqaN-VO6Gwperkkxrnk8wYNEAZS0T5FJcnrqsMcV0flSj_mdwkAvLZ-__XjezJVpj2orykqaZTlua1sUHMPSEtHZnVhiN9nJlKHJ66l5cyBOzSHPgup-pUVRQcH89DkLpRDsn2pg5bJIk0goEZdrQRKzuaDAXuSsdQrRk2MwYR-hQG51cOqAnUyDhJMMBoEu2fJI_-dIKrMAnzjkhzqaOkzHenbF-vwbkKsIrq5GOkswR7a_sAidjBECYC_te63BXboLXJ8YvICBStkFy1Zwx6YAId9NhUXa05EoiZswELYSxQK7JEUI30tUltS2D5845rXeKOyREexBcnonS2z-5FT_uyA7R7L0BVToflF0hKfeY5NJY_FpEVYbpHZOaeF5OWhR2wKSWycDCYmnUUw4"
        ).build()

        listener = EchoWebSocketListener()
        ws = client.newWebSocket(request, listener)

        binding.send.setOnClickListener {
            val messaged = binding.messageadd

            if (messaged.text.toString().trim() == "") {
                Toast.makeText(this, " ", Toast.LENGTH_SHORT).show()
            } else {
                ws!!.send(nameUser + ":" + messaged.text.toString())
                output(nameUser + ":" + messaged.text.toString())
                messaged.setText("")

            }

        }

    }

    private fun output(text: String) {

        runOnUiThread {

            if (sizeData != 0) {
                binding.recycler.smoothScrollToPosition(sizeData)
            }

            model.addItem(text)
            model.setItemList()

        }

    }


}