package com.edoctor.presentation.app.medcard

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.edoctor.presentation.app.events.EventsFragment
import com.edoctor.presentation.app.parameters.ParametersFragment

class MedcardPagerAdapter(
    fragmentManager: FragmentManager,
    private val pageTitles: List<String>
) : FragmentPagerAdapter(fragmentManager) {

    companion object {
        const val PAGES_COUNT = 2
    }

    init {
        require(pageTitles.size == PAGES_COUNT)
    }

    override fun getItem(position: Int): Fragment? =
        when (position) {
            0 -> EventsFragment()
            1 -> ParametersFragment()
            else -> null
        }

    override fun getPageTitle(position: Int) = pageTitles[position]

    override fun getCount(): Int = PAGES_COUNT

}