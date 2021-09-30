package com.nineone.verificationcode.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nineone.verificationcode.activity.FiveActivity
import com.nineone.verificationcode.activity.KotlinActivity
import com.nineone.verificationcode.utils.SimpleData

class UserViewModel : ViewModel() {


    private val users: MutableLiveData<List<SimpleData>> by lazy {
        MutableLiveData<List<SimpleData>>().apply {
            loadUsers();
        }
    }

    fun getUserList() = users;
    private fun loadUsers() {

    }

}