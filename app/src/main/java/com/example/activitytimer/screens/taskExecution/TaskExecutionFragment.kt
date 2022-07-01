package com.example.activitytimer.screens.taskExecution

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.activitytimer.R
import com.example.activitytimer.databinding.FragmentTaskExecutionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class TaskExecutionFragment : Fragment() {
    private lateinit var binding: FragmentTaskExecutionBinding
    private lateinit var binder: CountDownTimerService.LocalBinder
    private lateinit var mService: CountDownTimerService
    private var mBound: Boolean = false

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to Service, cast the IBinder and get Service instance
            mBound = true
            binder = service as CountDownTimerService.LocalBinder
            mService = binder.getService()

            setListeners()
        }

        override fun onServiceDisconnected(name: ComponentName?) { }
    }

    private lateinit var intent: Intent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskExecutionBinding.inflate(inflater, container, false)

        intent = Intent(requireContext(), CountDownTimerService::class.java).also { intent ->
            intent.putExtra("taskId",
                TaskExecutionFragmentArgs.fromBundle(requireArguments()).taskId)
            requireContext().startService(intent)
            requireContext().bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    private fun adaptFontSize(textLength: Int) {
        when {
            textLength < 4f -> binding.taskTxvTime.textSize = 50f
            textLength < 7f -> binding.taskTxvTime.textSize = 40f
            else -> binding.taskTxvTime.textSize = 360f / textLength
        }
    }

    private fun setListeners() {
        binder.allTasksDone.observe(viewLifecycleOwner) {
            if(it) {
                findNavController().navigate(R.id.action_TaskExecution_to_TaskDone)
            }
        }

        binder.skippedAllTasks.observe(viewLifecycleOwner) {
            if(it) {
                findNavController().popBackStack()
            }
        }

        binder.currentSubtask.observe(viewLifecycleOwner) {
            it?.let{
                binding.taskTxvName.text = it.name
                adaptFontSize(it.duration.seconds.toString().length)
                binding.executionProgressBarTimer.progress =
                    binder.durationInSeconds.value!!.toInt()
                // binding.taskTxvSetsNumber.text = resources.getString(R.string.reps_count_of_all, viewModel.subtaskRepCount, it.count)
            }
        }

        binding.taskBtnStart.setOnClickListener {
            mService.play()
        }

        binding.taskBtnSkip.setOnClickListener {
            mService.skip()
        }

        binding.taskBtnFinish.setOnClickListener{
            mService.finish()
        }

        binder.progressMax.observe(viewLifecycleOwner) {
            binding.executionProgressBarTimer.max = it.toInt()
        }

        binder.durationInSeconds.observe(viewLifecycleOwner) {
            binding.executionProgressBarTimer.progress = it.toInt()
            adaptFontSize(it.seconds.toString().length)
        }

        binding.viewModel = binder
    }

    override fun onStop() {
        super.onStop()
        if (mBound) {
            requireContext().unbindService(connection)
        }
    }

}