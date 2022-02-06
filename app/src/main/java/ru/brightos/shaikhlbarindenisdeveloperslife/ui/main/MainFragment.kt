package ru.brightos.shaikhlbarindenisdeveloperslife.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.*
import ru.brightos.shaikhlbarindenisdeveloperslife.R

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var mainPagerAdapter: MainPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainPagerAdapter = MainPagerAdapter(requireContext(), childFragmentManager)

        pages?.apply {
            offscreenPageLimit = 3
            adapter = mainPagerAdapter
            tabs?.setupWithViewPager(this)
        }
    }
}
