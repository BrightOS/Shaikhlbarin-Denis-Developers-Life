package ru.brightos.shaikhlbarindenisdeveloperslife.ui.main.feed

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
import ru.brightos.shaikhlbarindenisdeveloperslife.util.api.latest.LatestPosts
import ru.brightos.shaikhlbarindenisdeveloperslife.util.api.random.RandomPost
import ru.brightos.shaikhlbarindenisdeveloperslife.util.api.top.TopPosts
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class PostsFeedFragmentViewModel @Inject constructor(
    private val postsRepository: LikedPostsRepository
) : ViewModel() {

    var innerContext: Context? = null
    var type: String = "newest"
    var hasMoreToLoad = true

    private val _error: MutableLiveData<ErrorHandler> =
        MutableLiveData<ErrorHandler>(ErrorHandler.currentError)
    val error: LiveData<ErrorHandler?>
        get() = _error

    private val _postsList = MutableLiveData(ArrayList<PostModel>())
    val postsList: LiveData<ArrayList<PostModel>>
        get() = _postsList

    fun getPostsRepository() = postsRepository

    fun loadPosts(page: Int, offset: Int) {
        if (!hasMoreToLoad)
            return

        val newList = ArrayList<PostModel>()
        newList.addAll(_postsList.value!!)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val array = JSONObject(
                    if (type == "newest")
                        LatestPosts(
                            innerContext!!,
                            page,
                            offset
                        ).get().body
                    else
                        TopPosts(
                            innerContext!!,
                            page,
                            offset
                        ).get().body
                ).getJSONArray("result")

                if (array.length() < offset)
                    hasMoreToLoad = false

                var postObject: JSONObject
                for (postIndex in 0 until array.length()) {
                    postObject = array.get(postIndex) as JSONObject

                    if (!postObject.getString("type").contains("gif"))
                        continue

                    PostModel(
                        postObject.getLong("id"),
                        postObject.getString("description"),
                        postObject.getLong("votes"),
                        postObject.getString("author"),
                        postObject.getString("date"),
                        postObject.getString("gifURL"),
                        postObject.getLong("gifSize"),
                        postObject.getString("previewURL"),
                        postObject.getString("type"),
                        postsRepository.getLikedPostByID(postObject.getLong("id")) != null
                    ).let {
                        newList.add(it)
                    }
                }
                MainScope().launch {
                    _postsList.value = newList
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