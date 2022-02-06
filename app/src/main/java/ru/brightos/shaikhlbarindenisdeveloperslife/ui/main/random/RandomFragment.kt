package ru.brightos.shaikhlbarindenisdeveloperslife.ui.main.random

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_random.*
import kotlinx.android.synthetic.main.item_post.*
import kotlinx.android.synthetic.main.layout_internet_error.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.brightos.shaikhlbarindenisdeveloperslife.R
import ru.brightos.shaikhlbarindenisdeveloperslife.util.ErrorHandler
import ru.brightos.shaikhlbarindenisdeveloperslife.util.ErrorHandler.*
import ru.brightos.shaikhlbarindenisdeveloperslife.util.getColorFromAttributes

@AndroidEntryPoint
class RandomFragment : Fragment(R.layout.fragment_random) {

    val randomFragmentViewModel: RandomFragmentViewModel by viewModels()
    private var canReturnPreviousPost = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retry?.setOnClickListener {
            random_error?.visibility = View.INVISIBLE
            loading_indicator?.visibility = View.VISIBLE
            image?.alpha = 0f
            randomFragmentViewModel.loadRandomPost()
        }

        next?.setOnClickListener {
            randomFragmentViewModel.loadRandomPost()
        }

        back?.setOnClickListener {
            if (canReturnPreviousPost)
                randomFragmentViewModel.loadPreviousPost()
        }

        like?.setOnClickListener {
            randomFragmentViewModel.currentPost.value?.let {
                if (randomFragmentViewModel.isCurrentPostLiked.value!!)
                    randomFragmentViewModel.removePostFromLiked(it.id)
                else
                    randomFragmentViewModel.addPostToLiked(it)
            }
        }

        randomFragmentViewModel.apply {
            innerContext = context

            loadRandomPost()

            canReturnPreviousPost.observe(viewLifecycleOwner) {
                this@RandomFragment.canReturnPreviousPost = it
                if (!it)
                    back?.alpha = 0.7f
                else
                    back?.alpha = 1f
            }

            error.observe(viewLifecycleOwner) {
                if (it == LOAD_ERROR) {
                    random_error?.visibility = View.VISIBLE
                }
            }

            currentPost.observe(viewLifecycleOwner) {
                it?.let {
                    Glide.with(requireActivity())
                        .asDrawable()
                        .load(it.previewURL)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(object : CustomTarget<Drawable>(){
                            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                                image?.setImageDrawable(resource)
                                Glide.with(requireActivity())
                                    .asGif()
                                    .load(it.gifURL)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(resource)
                                    .into(image)
                                loading_indicator?.visibility = View.INVISIBLE
                                image?.alpha = 1f
                            }
                            override fun onLoadCleared(placeholder: Drawable?) {
                            }
                        })

                    title?.text = it.description
                    author?.text = "By ${it.author}"
                    rating?.text = "${it.votes}"
                    like?.imageTintList = if (it.liked)
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                requireContext(),
                                android.R.color.holo_red_dark
                            )
                        )
                    else
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                requireContext(),
                                android.R.color.black
                            )
                        )
                }
            }

            isCurrentPostLoaded.observe(viewLifecycleOwner) {
                if (!it) {
                    loading_indicator?.visibility = View.VISIBLE
                    image?.alpha = 0f
                }
            }

            isCurrentPostLiked.observe(viewLifecycleOwner) {
                like?.imageTintList = if (it)
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            android.R.color.holo_red_dark
                        )
                    )
                else
                    ColorStateList.valueOf(
                        getColorFromAttributes(
                            context,
                            android.R.attr.colorAccent
                        )
                    )
            }
        }
    }

    private fun animateAlpha(view: View?, startValue: Float, endValue: Float) {
        val va = ValueAnimator.ofFloat(startValue, endValue)
        va.duration = 300
        va.addUpdateListener { animation -> view?.alpha = animation.animatedValue as Float }
        va.repeatCount = 1
        va.start()
    }
}