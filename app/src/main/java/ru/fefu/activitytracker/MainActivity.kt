package ru.fefu.activitytracker
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val workoutFragment = WorkoutFragment()
        val profileFragment = ProfileFragment()
        supportFragmentManager.beginTransaction().apply {
            add(R.id.fragment_container,
                profileFragment,
                "First Fragment")
            add(
                R.id.fragment_container,
                workoutFragment,
                "First Fragment"
            )
            hide(profileFragment)
            addToBackStack("First Fragment")
            commit()
        }


        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        bottomNavigation.setOnItemSelectedListener {
            if(it.itemId==R.id.navigation_profile) {
                if (!workoutFragment.isHidden && profileFragment.isHidden) {
                    supportFragmentManager.beginTransaction().apply {
                        hide(workoutFragment)
                        show(profileFragment)
                        commit()
                    }
                }
            }
            else if (it.itemId==R.id.navigation_workout) {
                if(workoutFragment.isHidden && !profileFragment.isHidden) {
                    supportFragmentManager.beginTransaction().apply {
                        hide(profileFragment)
                        show(workoutFragment)
                        commit()
                    }
                }
            }
            true
        }
    }
}