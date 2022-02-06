package ru.brightos.shaikhlbarindenisdeveloperslife.ui.main.liked

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ru.brightos.shaikhlbarindenisdeveloperslife.data.repository.LikedPostsRepository
import ru.brightos.shaikhlbarindenisdeveloperslife.models.PostModel
import javax.inject.Inject

@HiltViewModel
class LikedPostsFragmentViewModel @Inject constructor(
    private val postsRepository: LikedPostsRepository
) : ViewModel() {

    private val _postsList = MutableLiveData(ArrayList<PostModel>())
    val postsList: LiveData<ArrayList<PostModel>>
        get() = _postsList

    fun getPostsRepository() = postsRepository

    fun getAllPosts() {
        GlobalScope.launch(Dispatchers.IO) {
            val newList = postsRepository.getAllLikedPosts() as ArrayList
            MainScope().launch {
                _postsList.value = newList
            }
        }
    }
}
