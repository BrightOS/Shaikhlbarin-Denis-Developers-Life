package ru.brightos.shaikhlbarindenisdeveloperslife.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.ImageViewCompat
import com.google.android.material.appbar.AppBarLayout
import ru.brightos.shaikhlbarindenisdeveloperslife.R
import ru.brightos.shaikhlbarindenisdeveloperslife.util.getColorFromAttributes


class ShaiDAppBar constructor(
    cont: Context,
    attrs: AttributeSet?
) : AppBarLayout(cont, attrs) {

    fun setStartButtonOnClickListener(p0: (View) -> Unit) {
        findViewById<ImageView>(R.id.start_button).setOnClickListener(p0)
    }

    fun setEndButtonOnClickListener(p0: (View) -> Unit) {
        findViewById<ImageView>(R.id.end_button).setOnClickListener(p0)
    }

    fun hideEndButton() {
        findViewById<ImageView>(R.id.end_button).visibility = View.INVISIBLE
    }

    fun showEndButton() {
        findViewById<ImageView>(R.id.end_button).visibility = View.VISIBLE
    }

    val startButton: ImageView
        get() = findViewById(R.id.start_button)

    val endButton: ImageView
        get() = findViewById(R.id.end_button)

    init {
        View.inflate(context, R.layout.layout_appbar, this)

        elevation = 0f
        background = ColorDrawable(getColorFromAttributes(cont, android.R.attr.colorPrimaryDark))

        addOnOffsetChangedListener(object : OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                this@ShaiDAppBar.findViewById<TextView>(R.id.appbar_title)
                    ?.setTextSize(
                        TypedValue.COMPLEX_UNIT_DIP,
                        28f + verticalOffset / (appBarLayout.totalScrollRange.toFloat() / 4)
                    )
            }
        })

        val text = findViewById<View>(R.id.appbar_title) as TextView
        text.isSelected = true

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.ShaiDAppBar)

            findViewById<ImageView>(R.id.start_button).setImageResource(
                typedArray.getResourceId(
                    R.styleable.ShaiDAppBar_startIcon,
                    R.drawable.ic_arrow_left
                )
            )

            typedArray.getResourceId(
                R.styleable.ShaiDAppBar_endIcon,
                0
            ).let {
                if (it != 0)
                    findViewById<ImageView>(R.id.end_button).setImageResource(it)
            }

            if (typedArray.getBoolean(R.styleable.ShaiDAppBar_startIconAutoTint, true))
                ImageViewCompat.setImageTintList(
                    findViewById(R.id.start_button),
                    ColorStateList.valueOf(
                        getColorFromAttributes(
                            context,
                            android.R.attr.colorAccent
                        )
                    )
                )

            if (typedArray.getBoolean(R.styleable.ShaiDAppBar_endIconAutoTint, true))
                ImageViewCompat.setImageTintList(
                    findViewById(R.id.end_button),
                    ColorStateList.valueOf(
                        getColorFromAttributes(
                            context,
                            android.R.attr.colorAccent
                        )
                    )
                )

            findViewById<TextView>(R.id.appbar_title).setText(
                typedArray.getText(
                    R.styleable.ShaiDAppBar_titleText
                )
            )

            typedArray.recycle()
        }
    }
}