package ru.fefu.activitytracker.fragments
import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.MapEventsOverlay
import ru.fefu.activitytracker.App
import ru.fefu.activitytracker.R
import ru.fefu.activitytracker.databinding.NewActivityFragmentBinding
import ru.fefu.activitytracker.adapters.NewActivityTypeListAdapter
import ru.fefu.activitytracker.room.ActivityRoom
import kotlin.random.Random
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import ru.fefu.activitytracker.services.TrackerService

class NewActivityFragment:Fragment() {
    private var _binding: NewActivityFragmentBinding? = null
    private val binding get() = _binding!!
    private val adapter = NewActivityTypeListAdapter()
    //private lateinit var serviceIntent: IntentService
    companion object {
        fun newInstance(): NewActivityFragment {
            return NewActivityFragment()
        }
        const val tag = "new_activity"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NewActivityFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycleView = binding.newActivityList
        recycleView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recycleView.adapter = adapter
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonContinue.setOnClickListener {
            if(adapter.selected!=-1)
            {
                val endDate = System.currentTimeMillis()
                val startDate = System.currentTimeMillis()
                val id_ = App.INSTANCE.db.activityDao().insert ( // хз как это работает...
                    ActivityRoom (
                        0,
                        adapter.selected,
                        startDate,
                        endDate,
                        mutableListOf<Pair<Double,Double>>(),
                        0,
                        0.0,
                        "",
                    )
                )
                val all = App.INSTANCE.db.activityDao().getAllList()
                val serviceIntent = Intent(this.requireActivity(), TrackerService::class.java)
                serviceIntent.putExtra("activity_id", id_.toInt())
                serviceIntent.action = "start_service"
                serviceIntent.putExtra("timeExtra", 0.0)
                this.requireActivity().startService(serviceIntent)
                val bundle = Bundle()
                bundle.putInt("id_",id_.toInt())
                arguments = bundle
                findNavController().popBackStack()
                findNavController().navigate(R.id.action_workoutFragment_to_startedActivityFragment,arguments)
            }
        }
        Configuration.getInstance().load(requireActivity(), activity?.getPreferences(Context.MODE_PRIVATE))
        initMap()
    }
    private fun initMap() {
        binding.mapView.minZoomLevel = 4.0
        binding.mapView.post {
            binding.mapView.zoomToBoundingBox(
                BoundingBox(
                    43.232111,
                    132.117062,
                    42.968866,
                    131.768039
                ),
                false
            )
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }
}