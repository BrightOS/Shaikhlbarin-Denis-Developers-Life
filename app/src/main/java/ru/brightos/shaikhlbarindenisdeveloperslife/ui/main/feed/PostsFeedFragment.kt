package ru.brightos.shaikhlbarindenisdeveloperslife.ui.main.feed

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_post_feed.*
import kotlinx.android.synthetic.main.fragment_post_feed.random_error
import kotlinx.android.synthetic.main.fragment_random.*
import kotlinx.coroutines.*
import ru.brightos.shaikhlbarindenisdeveloperslife.R
import ru.brightos.shaikhlbarindenisdeveloperslife.epoxy.PostsEpoxyController
import ru.brightos.shaikhlbarindenisdeveloperslife.util.EndlessRecyclerViewScrollListener
import ru.brightos.shaikhlbarindenisdeveloperslife.util.ErrorHandler
import kotlin.properties.Delegates

@AndroidEntryPoint
class PostsFeedFragment : Fragment(R.layout.fragment_post_feed) {

    val postsFeedFragmentViewModel: PostsFeedFragmentViewModel by viewModels()

    lateinit var postController: PostsEpoxyController
    var currentPage by Delegates.notNull<Int>()
    lateinit var feedType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null)
            feedType = requireArguments().getString("type").toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentPage = 0

        posts_refresh?.setOnRefreshListener {
            posts_refresh?.isRefreshing = false
        }

        postsFeedFragmentViewModel.apply {

            innerContext = context
            type = feedType

            error.observe(viewLifecycleOwner) {
                if (it == ErrorHandler.LOAD_ERROR) {
                    random_error?.visibility = View.VISIBLE
                }
            }

            postsList.observe(viewLifecycleOwner) {
                postController.hasMoreToLoad = hasMoreToLoad
                postController.setData(it)
            }
        }

        firstLoadPosts()
    }

    private fun firstLoadPosts() {
        postController = PostsEpoxyController(postsFeedFragmentViewModel.getPostsRepository())
        val layoutManager = LinearLayoutManager(context)

        val loadMoreScrollListener =
            object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int) {
                    if (postController.hasMoreToLoad) {
                        currentPage++
                        GlobalScope.launch(Dispatchers.IO) {
                            postsFeedFragmentViewModel.loadPosts(page - 1, 20)
                        }
                    }
                }
            }

        posts_recycler?.apply {
            setHasFixedSize(false)
            setLayoutManager(layoutManager)
            clearOnScrollListeners()
            adapter = postController.adapter
        }

        currentPage++
        GlobalScope.launch(Dispatchers.IO) {
            postsFeedFragmentViewModel.loadPosts(currentPage - 1, 20)
            posts_recycler?.addOnScrollListener(loadMoreScrollListener)
        }
    }

    companion object {
        fun newInstance(type: String?): PostsFeedFragment {
            val fragment = PostsFeedFragment()
            val bundle = Bundle()
            bundle.putString("type", type)
            fragment.arguments = bundle
            return fragment
        }
    }
}
