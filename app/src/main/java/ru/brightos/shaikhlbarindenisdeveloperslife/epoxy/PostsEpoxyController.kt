package ru.brightos.shaikhlbarindenisdeveloperslife.epoxy

import androidx.fragment.app.Fragment
import com.airbnb.epoxy.EpoxyController
import ru.brightos.shaikhlbarindenisdeveloperslife.data.repository.LikedPostsRepository
import ru.brightos.shaikhlbarindenisdeveloperslife.models.PostModel
import javax.inject.Inject

class PostsEpoxyController(
    private val postsRepository: LikedPostsRepository
) : EpoxyController() {
    val data = ArrayList<PostModel>()
    var hasMoreToLoad = true
    var isLikedPage = false

    fun setData(data: MutableList<PostModel>) {
        this.data.clear()
        this.data.addAll(data)
        requestModelBuild()
    }

    fun addData(data: MutableList<PostModel>) {
        this.data.addAll(data)
        requestModelBuild()
    }

    override fun buildModels() {
        val list = data.toList()
        val k = isLikedPage

        list.forEach {
            post(postsRepository) {
                id(it.id)
                postModel(it)
                likedPage(k)
            }
        }

        if (list.size < 1) {
            empty {
                id(-1)
                text("Здесь как-то... Пусто?")
                code(404)
            }
        }
    }
}