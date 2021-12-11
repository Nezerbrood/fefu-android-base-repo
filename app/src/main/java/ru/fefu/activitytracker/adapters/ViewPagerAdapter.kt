package ru.fefu.activitytracker.adapters

import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.fefu.activitytracker.fragments.MyActivitiesFragment
import ru.fefu.activitytracker.fragments.TheirActivitiesFragment
import ru.fefu.activitytracker.fragments.MainActivitiesFragment
import androidx.fragment.app.Fragment as Fragment

class ViewPagerAdapter(fragment: MainActivitiesFragment):FragmentStateAdapter(fragment) {
    private val mFragments: Array<Fragment> = arrayOf(
        MyActivitiesFragment(),
        TheirActivitiesFragment()
    )
    val mFragmentNames: Array<String> = arrayOf(
        "Моя",
        "Пользователей"
    )
    override fun getItemCount(): Int {
        return 2
    }
    override fun createFragment(position: Int): Fragment {
        return mFragments[position]
    }
}