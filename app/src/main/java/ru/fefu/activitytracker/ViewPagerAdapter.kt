package ru.fefu.activitytracker

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.fragment.app.Fragment as Fragment

class ViewPagerAdapter(fragment: WorkoutFragment):FragmentStateAdapter(fragment) {
    private val mFragments: Array<Fragment> = arrayOf(
        MyWorkoutFragment(),
        TheirWorkoutFragment()
    )
    val mFragmentNames: Array<String> = arrayOf(
        "Мои",
        "Пользователей"
    )
    override fun getItemCount(): Int {
        return 2
    }
    override fun createFragment(position: Int): Fragment {
        return mFragments[position]
    }
}