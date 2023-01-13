package com.salikhdev.chat_app_websocet.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MessageViewModel : ViewModel() {


    val itemList = MutableLiveData<List<String>>()

    private val datas = ArrayList<String>()


    fun getHeroes(): LiveData<List<String>> {
        return itemList
    }

    fun addItem(modelmess: String) {
        datas.add(modelmess)
    }

    fun setItemList() {
        itemList.value = datas
    }

    fun setItem(dataes2: List<String>) {
        itemList.value = dataes2
    }

}