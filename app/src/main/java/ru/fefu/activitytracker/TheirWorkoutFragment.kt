package ru.fefu.activitytracker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.fefu.activitytracker.R.layout.fragment_their_workout
import ru.fefu.activitytracker.databinding.FragmentTheirWorkoutBinding
import java.time.LocalDateTime

class TheirWorkoutFragment : Fragment() {
    private var _binding: FragmentTheirWorkoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var items: MutableList<ActivityData>
    val activities = listOf<UserActivityData>(
        UserActivityData(
            "1000 м",
            "Серфинг",
            LocalDateTime.now(),
            LocalDateTime.now(),
            "@nagibator228"
        ),
        UserActivityData(
            "1000 м",
            "Серфинг",
            LocalDateTime.of(2021, 10, 27, 11, 22),
            LocalDateTime.of(2021, 10, 28, 12, 40),
            "@hatersgonnahate"
        ),
        UserActivityData(
            "1000 м",
            "Серфинг",
            LocalDateTime.of(2021, 10, 27, 11, 22),
            LocalDateTime.of(2021, 10, 28, 12, 40),
            "@Jusus"
        ),
        UserActivityData(
            "1000 м",
            "Серфинг",
            LocalDateTime.of(2021, 10, 27, 11, 22),
            LocalDateTime.of(2021, 10, 28, 12, 40),
            "@gagaved"
        ),
    )

    val map = mapOf(1 to "Январь", 2 to "Февраль", 3 to "Март",
        4 to "Апрель", 5 to "Май", 6 to "Июнь",
        7 to "Июль", 8 to "Август", 9 to "Сентябрь",
        10 to "Октябрь", 11 to "Ноябрь", 12 to "Декабрь")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTheirWorkoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    val data_activities = mutableListOf<Any>()
    private fun fill_date(activities: List<UserActivityData>) {
        val cur = LocalDateTime.now()
        var date = DateData("")
        for (activity in activities) {
            if (cur.year == activity.endDate.year &&
                cur.monthValue == activity.endDate.monthValue &&
                cur.dayOfMonth == activity.endDate.dayOfMonth) {
                if (date.Date != "Сегодня") {
                    date = DateData("Сегодня")
                    data_activities.add(date)
                }
            }
            else {
                if (date.Date != map.get(activity.endDate.monthValue) + ' ' + activity.endDate.year.toString()  + "года") {
                    date = DateData(map.get(activity.endDate.monthValue) + ' '+activity.endDate.year.toString() + "года")
                    data_activities.add(date)
                }
            }
            Log.d("TAG", cur.hour.toString())
            data_activities.add(activity)
        }
    }

    private val adapter = RecyclerAdapter(data_activities)

    /*private fun changeFragment(position: Int) {
        if (position in data_activities.indices) {
            val manager = activity?.supportFragmentManager?.findFragmentByTag(ActivityTabs.tag)?.childFragmentManager
            manager?.beginTransaction()?.apply {
                manager.fragments.forEach(::hide)
                add (
                    R.id.activity_fragment_container,
                    MyActivityInfo.newInstance(),
                    MyActivityInfo.tag,
                )
                addToBackStack(null)
                commit()
            }
        }
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fill_date(activities)
        val recycleView = binding.theirRecyclerView
        recycleView.layoutManager = LinearLayoutManager(requireContext())
        recycleView.adapter = adapter
        adapter.setItemClickListener {position: Int ->
            findNavController().navigate(R.id.action_workoutFragment_to_theirActivityDetailsFragment)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}