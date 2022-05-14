package ru.ama.ottest.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.ama.ottest.R
import ru.ama.ottest.databinding.FragmentGameFinishedBinding
import ru.ama.ottest.domain.entity.GameResult

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
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
        binding.buttonRetry.setOnClickListener {
            goToStartGame()
        }
        with(gameResult) {
			Log.e("resultOfTest",resultOfTest.toString())
            var html=""
              resultOfTest.forEach{
                  html="$html  <br><br><br>  <b>${it.number.toString()}. ${it.question}</b><br>"
                  var ans=""
                  for( (index, element) in it.answers.withIndex()) {
                      ans = ans +"<br>"+ when (index) {
                      it.indexOfUserAnswer ->
                       "<font color=red>- ${element}</font>"
                      it.indexOfCorrect->
                       "<font color=green>- ${element}</font>"
                      else ->
                       "<font color=gray>- ${element}</font>"
                  }
                  //println("$index: $element")
                  }
                  html="$html <br> $ans"
                //  "<font color=red>${it.answer}</font> <br><br><font color=green>${it.correct}</font>"
                  binding.tvResQuestions.text=HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)

              }

            val emojiResId = if (winner) {
                R.drawable.ic_smile
            } else {
                R.drawable.ic_sad
            }
            binding.emojiResult.setImageResource(emojiResId)
            binding.tvScoreAnswers.text = String.format(
                getString(R.string.score_answers),
                countOfRightAnswers
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
