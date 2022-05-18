package ru.ama.ottest.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.ama.ottest.R
import ru.ama.ottest.databinding.FragmentGameFinishedBinding
import ru.ama.ottest.domain.entity.GameResult
import ru.ama.ottest.presentation.adapters.ResultAdapter

class GameFinishedFragment : Fragment() {

    private lateinit var binding: FragmentGameFinishedBinding
    private lateinit var gameResult: GameResult
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            goToStartGame()
            remove()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Разбор ответов"
        (requireActivity() as AppCompatActivity).supportActionBar?.subtitle = null
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
        binding.buttonRetry.setOnClickListener {
            goToStartGame()
        }
        with(gameResult) {
            Log.e("resultOfTest", resultOfTest.toString())
       
			
			//********** tvResultQuestion tvResultAnswers
            val adapter = ResultAdapter(requireContext())
        /*adapter.onResultClickListener = object : ResultAdapter.onResultClickListener {
            override fun onResultClick(resultOfTest: ResultOfTest) {
                Log.e("tInfo",resultOfTest.toString())
            }
        }*/
            binding.rvResultList.setHasFixedSize(false)
            binding.rvResultList.setNestedScrollingEnabled(false)
        binding.rvResultList.adapter = adapter
       // binding.rvResultList.itemAnimator = null
          adapter.submitList(resultOfTest)        
            
			
		
			
			//**********

            val emojiResId = if (winner) {
                binding.tvZacet.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.result_zacet))
                "Тест сдан"
            } else {
                binding.tvZacet.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.result_nezacet))
                "Тест не сдан"
            }
            binding.tvZacet.text=emojiResId
            binding.tvScoreAnswers.text = String.format(
                getString(R.string.score_answers),
                countOfRightAnswers,
                countOfAnswers,
                timeForTest
            )
            /* binding.tvRequiredAnswers.text = String.format(
                 getString(R.string.required_score),
                 gameSettings.minCountOfRightAnswers
             )*/
            binding.tvRequiredPercentage.text = String.format(
                getString(R.string.required_percentage),
                gameSettings.minPercentOfRightAnswers
            )
            binding.tvScorePercentage.text = String.format(
                getString(R.string.score_percentage),
                percentageOfRightAnswers
            )
        }
    }

    private fun parseArgs() {
        val args = requireArguments()
        if (!args.containsKey(ARG_GAME_RESULT)) {
            throw RuntimeException("$this must contain argument $ARG_GAME_RESULT")
        }
        args.getParcelable<GameResult>(ARG_GAME_RESULT)?.let {
            gameResult = it
        }
    }

    private fun goToStartGame() {
        activity?.supportFragmentManager?.popBackStack(
            GameFragment.NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    companion object {

        private const val ARG_GAME_RESULT = "game_result"

        fun newInstance(gameResult: GameResult): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_GAME_RESULT, gameResult)
                }
            }
        }
    }
}
