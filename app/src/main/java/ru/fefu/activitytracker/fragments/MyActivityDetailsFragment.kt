package ru.fefu.activitytracker.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import ru.fefu.activitytracker.App
import ru.fefu.activitytracker.R
import ru.fefu.activitytracker.adapters.ActivitiesListRecyclerAdapter
import ru.fefu.activitytracker.databinding.MyActivityDetailsBinding
import ru.fefu.activitytracker.dataclasses.ActivityData
import ru.fefu.activitytracker.dataclasses.DateData
import ru.fefu.activitytracker.enums.ActivityTypeEnum
import ru.fefu.activitytracker.room.Activity
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import android.text.Editable
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.getSystemService








class MyActivityDetailsFragment: Fragment() {
    private var _binding: MyActivityDetailsBinding? = null
    private val binding get() = _binding!!
    private val map = mapOf(1 to "Январь", 2 to "Февраль", 3 to "Март",
        4 to "Апрель", 5 to "Май", 6 to "Июнь",
        7 to "Июль", 8 to "Август", 9 to "Сентябрь",
        10 to "Октябрь", 11 to "Ноябрь", 12 to "Декабрь")
    companion object {
        fun newInstance(): MyActivityDetailsFragment {
            return MyActivityDetailsFragment()
        }
    }
    private fun fillDate() {
        val activity_id = arguments!!.getInt("ActivityID")
        val activity_from_db: Activity = App.INSTANCE.db.activityDao().getById(activity_id).get(0)!!
        val startDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(activity_from_db.dateStart), ZoneId.systemDefault())
        val endDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(activity_from_db.dateEnd), ZoneId.systemDefault())
        val type = ActivityTypeEnum.values()[activity_from_db.type].type
        val distance = (1..20).random().toString() + " км"
        val cur = LocalDateTime.now()
        binding.distance.text = distance
        binding.toolbar.title = type
        binding.myActivityCommentEdit.setText(activity_from_db.comment)
        val duration_ = Duration.between(endDate, startDate);
        var seconds: Long = Math.abs(duration_.getSeconds())
        val hours = seconds / 3600
        seconds -= hours * 3600
        val minutes = seconds / 60
        val hours_ = ActivitiesListRecyclerAdapter.getNoun(hours, "час", "часа", "часов")
        val minutes_ = ActivitiesListRecyclerAdapter.getNoun(minutes, "минута", "минуты", "минут")
        binding.duration.text = hours.toString() + ' ' +hours_ + ' ' +minutes.toString() + ' '+ minutes_
        if (LocalDateTime.now().year == endDate.year &&
            LocalDateTime.now().monthValue == endDate.monthValue &&
            LocalDateTime.now().dayOfMonth == endDate.dayOfMonth) {
            binding.date.text = Duration.between(endDate, LocalDateTime.now()).toHours().toString() +
                    ActivitiesListRecyclerAdapter.getNoun(
                        Duration.between(
                            endDate,
                            LocalDateTime.now()
                        ).toHours(), " час", " часа", " часов"
                    ) +
                    " назад"
        }
        else binding.date.text = endDate.dayOfMonth.toString() + '.'+
                endDate.monthValue.toString() + '.' + endDate.year.toString()
        binding.startTime.text = startDate.hour.toString()+":"+startDate.minute.toString()
        binding.finishTime.text = endDate.hour.toString()+":"+endDate.minute.toString()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MyActivityDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity_id = arguments!!.getInt("ActivityID")
        fillDate()
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.myActivityCommentEdit.addTextChangedListener( object :TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                App.INSTANCE.db.activityDao().setCommentById(activity_id,binding.myActivityCommentEdit.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        binding.mainConstraint.setOnClickListener {
            binding.myActivityCommentEdit.clearFocus()
            closeKeyboard(binding.comment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun closeKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}