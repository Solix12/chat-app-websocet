package com.salikhdev.chat_app_websocet.ViewModel

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salikhdev.chat_app_websocet.databinding.ItemMessageBinding

class MessageAdapter() : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    private var list: ArrayList<String> = ArrayList()
    private var userName = ""

    constructor(list: List<String>, userName: String) : this() {
        this.list = list as ArrayList<String>
        this.userName = userName
    }

    inner class ViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(data: String) {

            var kindUser = ""
            var message = ""

            for (i in data.indices) {
                if (data[i] == ':') {
                    kindUser = data.substring(0, i)
                    message = data.substring(i + 1, data.length)
                    break
                }
            }

            if (kindUser == "first") {
                binding.RightLayout.visibility = GONE
                binding.textLeft.visibility = GONE
                binding.textNameleft.text = message
            } else {
                if (kindUser != userName) {
                    binding.RightLayout.visibility = GONE
                    binding.left.visibility = VISIBLE
                    binding.textNameleft.text = "$kindUser:"
                    binding.textLeft.text = message
                } else {
                    binding.RightLayout.visibility = VISIBLE
                    binding.left.visibility = GONE
                    binding.textRight.text = message
                }
            }


        }

    }

    fun setData(list: List<String>) {

        if (list != null) {
            this.list.clear()
            this.list.addAll(list)
            println(list)
        } else {
            this.list = this.list
        }

    }

    fun getData(): List<String> {
        return this.list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindData(data = list[position])

    }


}