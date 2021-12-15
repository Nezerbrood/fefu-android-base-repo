package ru.fefu.activitytracker.fragments

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.fefu.activitytracker.App
import ru.fefu.activitytracker.dataclasses.ActivityData
import ru.fefu.activitytracker.dataclasses.DateData
import ru.fefu.activitytracker.R
import ru.fefu.activitytracker.adapters.ActivitiesListRecyclerAdapter
import ru.fefu.activitytracker.databinding.FragmentMyActivitiesBinding
import ru.fefu.activitytracker.enums.ActivityTypeEnum
import ru.fefu.activitytracker.room.Activity
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.random.Random
class MyActivitiesFragment : Fragment(R.layout.fragment_my_activities) {
    private var _binding: FragmentMyActivitiesBinding? = null
    private val binding get() = _binding!!
    private val dataActivities = mutableListOf<Any>()
    private val adapter = ActivitiesListRecyclerAdapter(dataActivities)
    val activities = mutableListOf<ActivityData>()
    private val map = mapOf(1 to "Январь", 2 to "Февраль", 3 to "Март",
        4 to "Апрель", 5 to "Май", 6 to "Июнь",
        7 to "Июль", 8 to "Август", 9 to "Сентябрь",
        10 to "Октябрь", 11 to "Ноябрь", 12 to "Декабрь")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyActivitiesBinding.inflate(inflater, container, false)
        return binding.root
    }
    private fun fillDate(activities: List<ActivityData>) {
        val cur = LocalDateTime.now()
        var date = DateData("")
        for (activity in activities) {
            if (cur.year == activity.endDate.year &&
                cur.monthValue == activity.endDate.monthValue &&
                cur.dayOfMonth == activity.endDate.dayOfMonth) {
                if (date.Date != "Сегодня") {
                    date = DateData("Сегодня")
                    dataActivities.add(date)
                }
            }
            else {
                if (date.Date != map[activity.endDate.monthValue] + ' ' + activity.endDate.year.toString()  + "года") {
                    date = DateData(map[activity.endDate.monthValue] + ' '+activity.endDate.year.toString() + "года")
                    dataActivities.add(date)
                }
            }
            Log.d("TAG", cur.hour.toString())
            dataActivities.add(activity)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        for (i in 1..6) {
            var locations = mutableListOf<Pair<Double,Double>>()
            for (k in 1..5) {
                var loc:Pair<Double,Double> = Pair(Random.nextDouble(43.566651, 43.626951),Random.nextDouble(131.987517, 131.998417))
                locations.add(loc)
            }
            var new_act = Activity(
                0,
                (0..2).random(),
                (1639590928000..1639593300000).random().toLong(),
                (1639500000000..1639503300000).random().toLong(),
                locations,
                ""
            )
            //Log.d("fsdf", "latitude:"+(new_act.coordinates[0].latitude.toString()))
            //Log.d("fsdf", "longitude:"+(new_act.coordinates[0].longitude.toString()))

            App.INSTANCE.db.activityDao().insert(new_act)   //добавляю в бд новую активность
        }
        App.INSTANCE.db.activityDao().getAll().observe(viewLifecycleOwner) {
            activities.clear()
            dataActivities.clear()
            for(activity in it) {
                val startDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(activity.dateStart), ZoneId.systemDefault())
                val endDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(activity.dateEnd), ZoneId.systemDefault())
                Log.d("fsdf",endDate.toString())
                val type = ActivityTypeEnum.values()[activity.type].type
                var distance_m = 0
                var distance = ""
                for(i in 1 until activity.coordinates.size){
                    var loc1 = Location("");
                    loc1.setLatitude(activity.coordinates[i].first);
                    loc1.setLongitude(activity.coordinates[i].second);

                    var loc2 = Location("");
                    loc2.setLatitude(activity.coordinates[i-1].first);
                    loc2.setLongitude(activity.coordinates[i-1].second);
                    distance_m += loc1.distanceTo(loc2).toInt()
                }
                distance = if(distance_m>=1000){
                    (distance_m/1000).toString()+" км"
                }else{
                    (distance_m).toString()+" м"
                }
                activities.add(ActivityData(activity.id, distance, type, startDate, endDate))
            }
            fillDate(activities)
            adapter.notifyDataSetChanged() //todo Переделать?...
        }
        fillDate(activities)
        val recycleView = binding.recyclerView
        recycleView.layoutManager = LinearLayoutManager(requireContext())
        recycleView.adapter = adapter
        adapter.setItemClickListener {
            val bundle = Bundle()
            bundle.putInt("ActivityID",activities[it].id )
            arguments = bundle
            findNavController().navigate(R.id.action_workoutFragment_to_myActivityDetailsFragment,arguments)
        }
        binding.startActivity.setOnClickListener {
            findNavController().navigate(R.id.action_workoutFragment_to_newActivityFragment)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
