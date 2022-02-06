package ru.brightos.shaikhlbarindenisdeveloperslife.ui.main

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ru.brightos.shaikhlbarindenisdeveloperslife.R
import ru.brightos.shaikhlbarindenisdeveloperslife.ui.main.feed.PostsFeedFragment
import ru.brightos.shaikhlbarindenisdeveloperslife.ui.main.liked.LikedPostsFragment
import ru.brightos.shaikhlbarindenisdeveloperslife.ui.main.random.RandomFragment

class MainPagerAdapter(
    private val context: Context,
    manager: FragmentManager?
) : FragmentPagerAdapter(manager!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = when (position) {
        1 -> PostsFeedFragment.newInstance("newest")
        2 -> PostsFeedFragment.newInstance("top")
        3 -> LikedPostsFragment()
        else -> RandomFragment()
    }

    override fun getPageTitle(position: Int) = when (position) {
        1 -> context.getString(R.string.latest)
        2 -> context.getString(R.string.top)
        3 -> context.getString(R.string.liked)
        else -> context.getString(R.string.random)
    }

    override fun getCount(): Int {
        return 4
    }
}
