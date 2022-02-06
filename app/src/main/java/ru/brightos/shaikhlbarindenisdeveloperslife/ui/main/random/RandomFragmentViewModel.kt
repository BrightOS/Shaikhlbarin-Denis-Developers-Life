package ru.brightos.shaikhlbarindenisdeveloperslife.ui.main.random

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import ru.brightos.shaikhlbarindenisdeveloperslife.data.repository.LikedPostsRepository
import ru.brightos.shaikhlbarindenisdeveloperslife.models.PostModel
import ru.brightos.shaikhlbarindenisdeveloperslife.util.ErrorHandler
import ru.brightos.shaikhlbarindenisdeveloperslife.util.api.random.RandomPost
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class RandomFragmentViewModel @Inject constructor(
    private val postsRepository: LikedPostsRepository
) : ViewModel() {

    @SuppressLint("StaticFieldLeak")
    var innerContext: Context? = null

    private val _currentPost: MutableLiveData<PostModel?> = MutableLiveData(null)
    val currentPost: LiveData<PostModel?>
        get() = _currentPost

    private val _error: MutableLiveData<ErrorHandler> =
        MutableLiveData<ErrorHandler>(ErrorHandler.currentError)
    val error: LiveData<ErrorHandler?>
        get() = _error

    private val _isCurrentPostLoaded = MutableLiveData(false)
    val isCurrentPostLoaded: LiveData<Boolean>
        get() = _isCurrentPostLoaded

    private val _isCurrentPostLiked = MutableLiveData(false)
    val isCurrentPostLiked: LiveData<Boolean>
        get() = _isCurrentPostLiked

    private val _canReturnPreviousPost = MutableLiveData(false)
    val canReturnPreviousPost: LiveData<Boolean>
        get() = _canReturnPreviousPost

    private val postsList: MutableList<PostModel> = ArrayList()

    fun addPostToLiked(postModel: PostModel) {
        viewModelScope.launch(Dispatchers.IO) {
            postsRepository.addLikedPost(postModel)
            val isPostLiked = isPostLiked(postsList.last().id)
            MainScope().launch {
                postsList.last().let {
                    postsList.add(
                        PostModel(
                            it.id,
                            it.description,
                            it.votes,
                            it.author,
                            it.date,
                            it.gifURL,
                            it.gifSize,
                            it.previewURL,
                            it.type,
                            isPostLiked
                        )
                    )
                    _isCurrentPostLiked.value = isPostLiked
                    var k = 0
                    postsList.removeAll { post -> post.id == it.id && k++ == 0 }
                }
            }
        }
    }

    fun removePostFromLiked(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            postsRepository.removePostFromLiked(id)
            val isPostLiked = isPostLiked(postsList.last().id)
            MainScope().launch {
                postsList.last().let {
                    postsList.add(
                        PostModel(
                            it.id,
                            it.description,
                            it.votes,
                            it.author,
                            it.date,
                            it.gifURL,
                            it.gifSize,
                            it.previewURL,
                            it.type,
                            isPostLiked
                        )
                    )
                    _isCurrentPostLiked.value = isPostLiked
                    var k = 0
                    postsList.removeAll { post -> post.id == it.id && k++ == 0 }
                }
            }
        }
    }

    fun loadPreviousPost() {
        _currentPost.value = postsList[postsList.lastIndex - 1]
        postsList.removeLast()
        _canReturnPreviousPost.value = postsList.size > 1
    }

    fun isPostLiked(id: Long) =
        postsRepository.getLikedPostByID(id) != null

    fun loadRandomPost() {
        _isCurrentPostLoaded.value = false
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var response: JSONObject
                do {
                    response = JSONObject(RandomPost(innerContext!!).get().body)
                } while (response.getString("type").contains("coub"))

                PostModel(
                    response.getLong("id"),
                    response.getString("description"),
                    response.getLong("votes"),
                    response.getString("author"),
                    response.getString("date"),
                    response.getString("gifURL"),
                    response.getLong("gifSize"),
                    response.getString("previewURL"),
                    response.getString("type"),
                    postsRepository.getLikedPostByID(response.getLong("id")) != null
                ).let {
                    MainScope().launch {
                        postsList.add(it)
                        _isCurrentPostLiked.value = it.liked
                        _currentPost.value = it
                        _isCurrentPostLoaded.value = true
                        _canReturnPreviousPost.value = postsList.size > 1
                    }
                }
            } catch (e: Exception) {
                MainScope().launch {
                    _error.value = ErrorHandler.LOAD_ERROR
                }
                e.printStackTrace()
            }
        }
    }
}