package ru.ama.ottest.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.ama.ottest.R
import ru.ama.ottest.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var binding: FragmentGameBinding

    //private lateinit var level: Level
   // private var optionsTextViews = mutableListOf<TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        getTextViewsOptions()
        setupClickListenersToOptions()
        observeViewModel()
        if (savedInstanceState == null) {
            viewModel.startGame()
        }
    }

   /* private fun parseArgs() {
        val args = requireArguments()
        if (!args.containsKey(ARG_LEVEL)) {
            throw RuntimeException("Required param level is absent")
        }
        args.getParcelable<Level>(ARG_LEVEL)?.let {
            level = it
        }
    }*/

    private fun getTextViewsOptions() {
		
      /*  with(binding) {
            optionsTextViews.add(tvOption1)
            optionsTextViews.add(tvOption2)
            optionsTextViews.add(tvOption3)
            optionsTextViews.add(tvOption4)
            optionsTextViews.add(tvOption5)
            optionsTextViews.add(tvOption6)
        }*/
    }

    private fun setupClickListenersToOptions() {
		binding.lvAnswers.setOnItemClickListener { parent, view, position, id ->
   // val element = adapter.getItemAtPosition(position)
     viewModel.chooseAnswer(position)
}
        /*for (textView in optionsTextViews) {
            textView.setOnClickListener {
                viewModel.chooseAnswer(textView.text.toString().toInt())
            }
        }*/
    }

    private fun observeViewModel() {
        viewModel.question.observe(viewLifecycleOwner) {
            with(binding) {
                tvQuestion.text = it.question
				val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,
				it.answers )
				lvAnswers.adapter = adapter
                //setupTextToOptions(it.answers)
            }
        }
        viewModel.leftFormattedTime.observe(viewLifecycleOwner) {
            binding.tvTimer.text = it
        }
        viewModel.gameResult.observe(viewLifecycleOwner) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, GameFinishedFragment.newInstance(it))
                .addToBackStack(null)
                .commit()
        }
        viewModel.percentOfRightAnswers.observe(viewLifecycleOwner) {
            binding.progressBar.setProgress(it, true)
        }
        viewModel.enoughPercentage.observe(viewLifecycleOwner) {
            setupProgressColorByState(it)
        }
        viewModel.minPercentOfRightAnswers.observe(viewLifecycleOwner) {
            binding.progressBar.secondaryProgress = it
        }
    }

    /*private fun setupTextToOptions(answers: List<Int>) {
        for (i in answers.indices) {
            optionsTextViews[i].text = answers[i].toString()
        }
    }*/

    private fun setupProgressColorByState(enoughPercentage: Boolean) {
        val colorResId = if (enoughPercentage) {
            android.R.color.holo_green_light
        } else {
            android.R.color.holo_red_light
        }
        val color = ContextCompat.getColor(requireContext(), colorResId)
        binding.progressBar.progressTintList = ColorStateList.valueOf(color)
    }

    companion object {

        private const val ARG_LEVEL = "level"
        const val NAME = "GameFragment"

        fun newInstance(): GameFragment {
            return GameFragment()/*.apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_LEVEL, level)
                }
            }*/
        }
    }
}
