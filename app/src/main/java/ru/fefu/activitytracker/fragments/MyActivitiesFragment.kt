package ru.fefu.activitytracker.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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
import ru.fefu.activitytracker.room.ActivityRoom
import java.time.Instant
import android.provider.Settings
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import java.time.LocalDateTime
import java.time.ZoneId
import java.lang.Exception


class MyActivitiesFragment : Fragment(R.layout.fragment_my_activities) {
    private var _binding: FragmentMyActivitiesBinding? = null
    private val binding get() = _binding!!
    private val dataActivities = mutableListOf<Any>()
    private val adapter = ActivitiesListRecyclerAdapter(dataActivities)
    val activities = mutableListOf<ActivityData>()
    private val map = mapOf(
        1 to "Январь", 2 to "Февраль", 3 to "Март",
        4 to "Апрель", 5 to "Май", 6 to "Июнь",
        7 to "Июль", 8 to "Август", 9 to "Сентябрь",
        10 to "Октябрь", 11 to "Ноябрь", 12 to "Декабрь"
    )

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
                cur.dayOfMonth == activity.endDate.dayOfMonth
            ) {
                if (date.Date != "Сегодня") {
                    date = DateData("Сегодня")
                    dataActivities.add(date)
                }
            } else {
                if (date.Date != map[activity.endDate.monthValue] + ' ' + activity.endDate.year.toString() + "года") {
                    date =
                        DateData(map[activity.endDate.monthValue] + ' ' + activity.endDate.year.toString() + "года")
                    dataActivities.add(date)
                }
            }
            Log.d("TAG", cur.hour.toString())
            dataActivities.add(activity)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /* for (i in 1..6) {
            var locations = mutableListOf<Pair<Double,Double>>()
            for (k in 1..5) {
                var loc:Pair<Double,Double> = Pair(Random.nextDouble(43.566651, 43.626951),Random.nextDouble(131.987517, 131.998417))
                locations.add(loc)
            }
            var new_act = ActivityRoom(
                0,
                (0..2).random(),
                (1639590928000..1639593300000).random().toLong(),
                (1639500000000..1639503300000).random().toLong(),
                locations,
                ""
            )

            App.INSTANCE.db.activityDao().insert(new_act)   //добавляю в бд новую активность
        }
        */
        App.INSTANCE.db.activityDao().getAll().observe(viewLifecycleOwner) {
            activities.clear()
            dataActivities.clear()
            for (activity in it) {
                val startDate = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(activity.dateStart),
                    ZoneId.systemDefault()
                )
                val endDate = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(activity.dateEnd),
                    ZoneId.systemDefault()
                )
                val type = ActivityTypeEnum.values()[activity.type].type
                var distance_m = 0
                var distance = ""
                for (i in 1 until activity.coordinates.size) {
                    var loc1 = Location("");
                    loc1.latitude = activity.coordinates[i].first;
                    loc1.longitude = activity.coordinates[i].second;

                    var loc2 = Location("");
                    loc2.latitude = activity.coordinates[i - 1].first;
                    loc2.longitude = activity.coordinates[i - 1].second;
                    distance_m += loc1.distanceTo(loc2).toInt()
                }
                distance = if (distance_m >= 1000) {
                    (distance_m / 1000).toString() + " км"
                } else {
                    (distance_m).toString() + " м"
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
            bundle.putInt("id_", (dataActivities[it] as ActivityData).id)
            arguments = bundle
            if(App.INSTANCE.db.activityDao().getById((dataActivities[it]as ActivityData).id).finished==1){
                findNavController().navigate(
                    R.id.action_workoutFragment_to_myActivityDetailsFragment,
                    arguments)
            }else{
                findNavController().navigate(R.id.action_workoutFragment_to_startedActivityFragment,
                    arguments)
            }

        }
        binding.startActivity.setOnClickListener{
            requestLocationPermissionAndLocate()
        }
    }

    private val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions[Manifest.permission.ACCESS_FINE_LOCATION]?.let {
                if (it) {
                    if (isGoogleServiceAvailable()) {
                        checkGpsEnabled(
                            {
                                val unfinished = App.INSTANCE.db.activityDao().getUnfinished()
                                if (unfinished !== null) {
                                    switchToUnfinishedActivity(unfinished.id)
                                } else {
                                    switchToNewActivity()
                                }
                            },
                            {
                                if (it is ResolvableApiException) {
                                    it.startResolutionForResult(this.requireActivity(), 2)
                                }
                            })
                    }
                } else {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        showPermissionBlocked()
                    }
                }
            }
        }

    private fun requestLocationPermissionAndLocate() {


        /*val bundle = Bundle()
        val unfinishedActivityRoom = App.INSTANCE.db.activityDao().getUnfinished()
        if(unfinishedActivityRoom !== null){
            bundle.putInt("id",unfinishedActivityRoom.id)
            arguments = bundle
            findNavController().navigate(
                R.id.action_workoutFragment_to_startedActivityFragment,
                arguments)
        }else{
            findNavController().navigate(R.id.action_workoutFragment_to_newActivityFragment)
        }
    }*/

        when {
            ContextCompat.checkSelfPermission(
                this.requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED -> {
                if (isGoogleServiceAvailable()) {
                    checkGpsEnabled(
                        {
                            val unfinished = App.INSTANCE.db.activityDao().getUnfinished()
                            if (unfinished !== null) {
                                switchToUnfinishedActivity(unfinished.id)
                            } else {
                                switchToNewActivity()
                            }
                        },
                        {
                            if (it is ResolvableApiException) {
                                it.startResolutionForResult(this.requireActivity(), 2)
                            }
                        })
                }
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                showRationale()
            }
            else -> locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                )
            )
        }
    }

    private fun showRationale() {
        AlertDialog.Builder(this.requireContext())
            .setTitle("Permission required")
            .setMessage("Нужно местоположение для работы карты")
            .setPositiveButton("Proceed") { _, _ ->
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                    )
                )
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .show()
    }

    private fun showPermissionBlocked() {
        AlertDialog.Builder(this.requireContext())
            .setTitle("Permission denied")
            .setMessage("Измените найстройки геолокации приложения")
            .setPositiveButton("Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", "ru.fefu.activitytracker", null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .show()
    }

    private fun isGoogleServiceAvailable(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val result = googleApiAvailability.isGooglePlayServicesAvailable(this.requireActivity())
        if (result == ConnectionResult.SUCCESS) {
            return true
        }
        if (googleApiAvailability.isUserResolvableError(result)) {
            googleApiAvailability.getErrorDialog(
                this.requireActivity(),
                result,
                1
            )?.show()
        } else {
            Toast.makeText(this.requireActivity(), "Сервисы гугл недоступны", Toast.LENGTH_SHORT)
                .show()
        }
        return false
    }

    private fun checkGpsEnabled(success: () -> Unit, error: (Exception) -> Unit) {
        LocationServices.getSettingsClient(this.requireActivity())
            .checkLocationSettings(
                LocationSettingsRequest.Builder()
                    .addLocationRequest(
                        LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    )
                    .build()
            )
            .addOnSuccessListener {
                success.invoke()
            }
            .addOnFailureListener {
                error.invoke(it)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            switchToNewActivity()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun switchToNewActivity() {
        findNavController().navigate(R.id.action_workoutFragment_to_newActivityFragment)
    }

    private fun switchToUnfinishedActivity(id: Int) {
        var bundle = Bundle()
        bundle.putInt("id_", id)
        arguments = bundle
        findNavController().navigate(R.id.action_workoutFragment_to_startedActivityFragment,arguments)
    }
}