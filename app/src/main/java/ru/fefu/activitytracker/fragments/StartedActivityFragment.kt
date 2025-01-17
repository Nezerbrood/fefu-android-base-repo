package ru.fefu.activitytracker.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.osmdroid.config.Configuration
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import ru.fefu.activitytracker.App
import ru.fefu.activitytracker.enums.ActivityTypeEnum
import ru.fefu.activitytracker.R
import ru.fefu.activitytracker.databinding.StartedActivityFragmentBinding
import org.osmdroid.views.overlay.Polyline
import ru.fefu.activitytracker.services.TrackerService
import kotlin.math.roundToInt

class StartedActivityFragment(): Fragment() {
    private var _binding: StartedActivityFragmentBinding? = null
    private val binding get() = _binding!!
    private var distance = 0.0
    private var id_ = -1

    private val polyline by lazy {
        Polyline().apply {
            outlinePaint.color = ContextCompat.getColor(
                requireContext(),
                R.color.blue
            )
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.registerReceiver(updateTime, IntentFilter("timerUpdated"))
        _binding = StartedActivityFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val time = intent?.getDoubleExtra("timeExtra", 0.0)!!
            Log.d("timeFragment", time.toString())
            binding.time.text = DoubleToTime(time)
        }
    }

    private fun DoubleToTime(time: Double): String {
        val result = time.roundToInt()
        val hours = result % 86400 / 3600
        val minutes = result % 86400 % 3600 / 60
        val seconds = result % 86400 % 3600 % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id_ = arguments!!.getInt("id_")
        Configuration.getInstance().load(requireActivity(), activity?.getPreferences(Context.MODE_PRIVATE))
        showUserLocation()
        initMap()

        val activityUnfinished = App.INSTANCE.db.activityDao().getById(id_)
        var type = ActivityTypeEnum.values()[activityUnfinished.type].type
        binding.activityName.text = type
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.finishActivityButton.setOnClickListener {
            val cancelIntent = Intent(requireActivity(), TrackerService::class.java)
            cancelIntent.putExtra("activity_id", id_)
            cancelIntent.action = "stop_service"
            requireActivity().startService(cancelIntent)
        }
        App.INSTANCE.db.activityDao().getByIdLiveData(id_).observe(viewLifecycleOwner) {
            if (it.coordinates.isNotEmpty()) {
                polyline.addPoint(GeoPoint(it.coordinates.last().first, it.coordinates.last().second))
            }
            if (it.distance > 1000) {
                val distance = it.distance / 1000
                binding.distance.text = "%.1f км".format(distance)
            }
            else {
                binding.distance.text = "%.0f м".format(it.distance)
            }
        }
    }

    private fun initMap() {
        binding.mapView.minZoomLevel = 4.0
        binding.mapView.post {
            binding.mapView.zoomToBoundingBox(
                BoundingBox(
                    43.03527,
                    132.117062,
                    43.02375,
                    131.89345
                ),
                false
            )
        }
        val activityInstance = App.INSTANCE.db.activityDao().getById(id_)
        val coordinatesList: List<Pair<Double, Double>> = activityInstance.coordinates
        if (activityInstance.distance > 1000) {
            val distance = activityInstance .distance / 1000
            binding.distance.text = "%.1f км".format(distance)
        }
        else {
            binding.distance.text = "%.0f м".format(activityInstance .distance)
        }
        for(point in coordinatesList) {
            polyline.addPoint(GeoPoint(point.first, point.second))
        }
        binding.mapView.overlayManager.add(polyline)
    }

    private fun showUserLocation() {
        val locationOverlay = MyLocationNewOverlay(binding.mapView)
        val locationIcon = BitmapFactory.decodeResource(resources, R.drawable.marker2)
        locationOverlay.setDirectionArrow(locationIcon, locationIcon)
        locationOverlay.setPersonHotspot(locationIcon.width / 2f, locationIcon.height.toFloat())
        locationOverlay.enableMyLocation()
        binding.mapView.overlays.add(locationOverlay)
    }

    override fun onDestroyView() {
        activity?.unregisterReceiver(updateTime)
        _binding = null
        super.onDestroyView()
    }
}