package ru.fefu.activitytracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
class WorkoutFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_workout, container, false)
    }
    private fun setupNavigation(){

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mViewPager = view.findViewById<ViewPager2>(R.id.workout_view_pager2)
        mViewPager.adapter=(ViewPagerAdapter(this))
        val tabLayout = view.findViewById<TabLayout>(R.id.workout_tab_layout)
        TabLayoutMediator(tabLayout,mViewPager

        ) { tab, position ->
            tab.text =
                ((mViewPager.adapter) as ViewPagerAdapter?)!!.mFragmentNames[position] //Sets tabs names as mentioned in ViewPagerAdapter fragmentNames array, this can be implemented in many different ways.
        }.attach()
    }
}