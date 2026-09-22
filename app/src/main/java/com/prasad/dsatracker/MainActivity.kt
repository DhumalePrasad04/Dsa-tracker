package com.prasad.dsatracker

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    // Mirrors the 12-week / 90-day DSA + System Design plan
    private val weeks = listOf(
        "Week 1: Arrays, Strings, Two Pointers, Sliding Window, Hashing",
        "Week 2: Linked Lists, Stacks/Queues",
        "Week 3: Trees (traversals, BST, LCA)",
        "Week 4: Heaps, Recursion/Backtracking",
        "Week 5: Graphs (BFS/DFS, topological sort)",
        "Week 6: Graphs (Dijkstra, Union-Find) + DP intro",
        "Week 7: DP 2D (knapsack, LCS, LIS)",
        "Week 8: DP hard patterns + LLD (SOLID)",
        "Week 9: LLD problems (parking lot, rate limiter, chat system)",
        "Week 10: HLD basics (load balancing, caching, DB, API design)",
        "Week 11: HLD (CAP theorem, sharding) + timed mixed sets",
        "Week 12: Mock interviews + applying"
    )

    private val prefs by lazy { getSharedPreferences("dsa_tracker", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= 33) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
        }

        val dayLabel = findViewById<TextView>(R.id.dayLabel)
        val weekLabel = findViewById<TextView>(R.id.weekLabel)
        val streakLabel = findViewById<TextView>(R.id.streakLabel)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val doneButton = findViewById<Button>(R.id.doneButton)
        val remindButton = findViewById<Button>(R.id.remindButton)

        fun refresh() {
            val day = prefs.getInt("day", 1)
            val streak = prefs.getInt("streak", 0)
            val week = ((day - 1) / 7).coerceIn(0, weeks.size - 1)
            dayLabel.text = "Day $day of 90"
            weekLabel.text = weeks[week]
            streakLabel.text = "\uD83D\uDD25 Streak: $streak days"
            progressBar.max = 90
            progressBar.progress = day
        }
        refresh()

        doneButton.setOnClickListener {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
            val lastDone = prefs.getString("lastDone", "")
            if (lastDone != today) {
                val day = prefs.getInt("day", 1)
                val streak = prefs.getInt("streak", 0)
                prefs.edit()
                    .putInt("day", (day + 1).coerceAtMost(90))
                    .putInt("streak", streak + 1)
                    .putString("lastDone", today)
                    .apply()
                refresh()
            }
        }

        remindButton.setOnClickListener { scheduleDailyReminder() }
    }

    private fun scheduleDailyReminder() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 20)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) add(Calendar.DAY_OF_YEAR, 1)
        }
        val intent = Intent(this, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}
