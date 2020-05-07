package com.example.notificationscheduler

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var mScheduler: JobScheduler? = null

    //Switches for setting job options
    private var mDeviceIdleSwitch: Switch? = null
    private var mDeviceChargingSwitch: Switch? = null

    //Override deadline seekbar
    private var mSeekBar: SeekBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scheduleBtn.setOnClickListener { scheduleJob() }
        cancelBtn.setOnClickListener { cancelJobs() }

        mDeviceIdleSwitch = findViewById(R.id.idleSwitch)
        mDeviceChargingSwitch = findViewById(R.id.chargingSwitch)

        mSeekBar = findViewById(R.id.seekBar)
        mSeekBar!!.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            /**
             * The second argument is the current value of the seek bar
             */
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // meaning a value has been set by the user
                if (progress > 0) {
                    // set the seek bar's progress label to indicate seconds
                    seekBarProgress.text = "$progress s";
                } else {
                    seekBarProgress.text = getString(R.string.not_set);
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //
            }

        })
    }

    fun scheduleJob() {
        val networkOptions: RadioGroup = findViewById(R.id.networkOptions)
        val selectedNetworkID = networkOptions.checkedRadioButtonId
        var selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE

        when (selectedNetworkID) {
            R.id.noNetwork -> selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE
            R.id.anyNetwork -> selectedNetworkOption = JobInfo.NETWORK_TYPE_ANY
            R.id.wifiNetwork -> selectedNetworkOption = JobInfo.NETWORK_TYPE_UNMETERED
        }

        mScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler

        // The ComponentName is used to associate the JobService with the JobInfo object.
        val serviceName = ComponentName(
            packageName,
            NotificationJobService::class.java.name
        )
        val builder = JobInfo.Builder(JOB_ID, serviceName)
            .setRequiredNetworkType(selectedNetworkOption)
            // set constraints based on the user selection in the Switch views
            .setRequiresDeviceIdle(mDeviceIdleSwitch!!.isChecked)
            .setRequiresCharging(mDeviceChargingSwitch!!.isChecked)


        // The override deadline should only be set if the integer value of the SeekBar is greater than 0.
        // create an int to store the seek bar's progress
        val seekBarInteger = mSeekBar!!.progress

        // create a boolean variable that's true if the seek bar has an integer value greater than 0.
        val seekBarSet = seekBarInteger > 0

        if (seekBarSet) {
            builder.setOverrideDeadline((seekBarInteger * 1000).toLong())
        }

        // true if the selected network option is not the default JobInfo.NETWORK_TYPE_NONE,
        // among other new/possible options
        val constraintSet = (selectedNetworkOption != JobInfo.NETWORK_TYPE_NONE
                || mDeviceChargingSwitch!!.isChecked || mDeviceIdleSwitch!!.isChecked)
                || seekBarSet

        if (constraintSet) {
            // Schedule the job and notify the user
            val myJobInfo = builder.build()
            mScheduler!!.schedule(myJobInfo)

            // let the user know the job was scheduled
            Toast.makeText(this, "Job Scheduled, job will run when the constraints are met.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Please set at least one constraint", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Check whether the JobScheduler object is null.
     * If not, call cancelAll() on the object to remove all pending jobs.
     * Also reset the JobScheduler to null and show a toast message to tell the user that the job was canceled.
     */
    fun cancelJobs() {
        if (mScheduler!=null){
            mScheduler!!.cancelAll()
            mScheduler = null
            Toast.makeText(this, "Jobs cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}
