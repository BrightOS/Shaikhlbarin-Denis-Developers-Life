package ru.brightos.shaikhlbarindenisdeveloperslife.epoxy

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ru.brightos.shaikhlbarindenisdeveloperslife.R
import ru.brightos.shaikhlbarindenisdeveloperslife.data.repository.LikedPostsRepository
import ru.brightos.shaikhlbarindenisdeveloperslife.models.PostModel
import ru.brightos.shaikhlbarindenisdeveloperslife.util.KotlinHolder

@EpoxyModelClass(layout = R.layout.item_post)
abstract class PostEpoxyModel(
    private val postsRepository: LikedPostsRepository
) : EpoxyModelWithHolder<PostEpoxyModel.Holder>() {

    @EpoxyAttribute
    lateinit var postModel: PostModel

    @EpoxyAttribute
    var likedPage = false

    override fun bind(holder: Holder) {
        holder.title.text = postModel.description
        holder.author.text = "By ${postModel.author}"
        holder.rating.text = "${postModel.votes}"

        Glide.with(holder.image.context)
            .asDrawable()
            .load(postModel.previewURL)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    holder.image.setImageDrawable(resource)
                    Glide.with(holder.image.context)
                        .asGif()
                        .load(postModel.gifURL)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(resource)
                        .into(holder.image)
                    holder.loadingIndicator.visibility = View.INVISIBLE
                    holder.image.alpha = 1f
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })

        var liked: Boolean
        GlobalScope.launch {
            liked = postsRepository.getLikedPostByID(postModel.id) != null
            MainScope().launch {
                holder.like.imageTintList =
                    if (liked) ColorStateList.valueOf(
                        ContextCompat.getColor(
                            holder.image.context,
                            android.R.color.holo_red_dark
                        )
                    )
                    else
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                holder.image.context,
                                android.R.color.black
                            )
                        )
            }
        }

        holder.like.setOnClickListener {
            GlobalScope.launch {
                if (postsRepository.getLikedPostByID(postModel.id) != null) {
                    postsRepository.removePostFromLiked(postModel.id)
                    MainScope().launch {
                        holder.like.imageTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                holder.image.context,
                                android.R.color.black
                            )
                        )
                    }
                } else {
                    postsRepository.addLikedPost(postModel)
                    MainScope().launch {
                        holder.like.imageTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                holder.image.context,
                                android.R.color.holo_red_dark
                            )
                        )
                    }
                }
            }
        }
        super.bind(holder)
    }

    inner class Holder : KotlinHolder() {
        val author by bind<TextView>(R.id.author)
        val root by bind<MaterialCardView>(R.id.post_root)
        val title by bind<TextView>(R.id.title)
        val rating by bind<TextView>(R.id.rating)
        val like by bind<FloatingActionButton>(R.id.like)
        val image by bind<ImageView>(R.id.image)
        val loadingIndicator by bind<CircularProgressIndicator>(R.id.loading_indicator)
    }
}
