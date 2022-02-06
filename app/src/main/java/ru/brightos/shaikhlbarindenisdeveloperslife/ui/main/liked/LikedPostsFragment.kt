package ru.brightos.shaikhlbarindenisdeveloperslife.ui.main.liked

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_post_feed.*
import ru.brightos.shaikhlbarindenisdeveloperslife.R
import ru.brightos.shaikhlbarindenisdeveloperslife.epoxy.PostsEpoxyController

@AndroidEntryPoint
class LikedPostsFragment: Fragment(R.layout.fragment_post_feed) {

    val likedPostsFragmentViewModel: LikedPostsFragmentViewModel by viewModels()

    lateinit var postController: PostsEpoxyController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postController = PostsEpoxyController(likedPostsFragmentViewModel.getPostsRepository())

        posts_recycler?.apply {
            layoutManager = layoutManager
            clearOnScrollListeners()
            adapter = postController.adapter
            setHasFixedSize(false)
        }

        likedPostsFragmentViewModel.postsList.observe(viewLifecycleOwner) {
            postController.setData(it)
            Log.e("SIIIZE", "${it.size}")
            posts_refresh?.isRefreshing = false
        }

        posts_refresh?.setOnRefreshListener {
            likedPostsFragmentViewModel.getAllPosts()
        }

        likedPostsFragmentViewModel.getAllPosts()
    }
}
