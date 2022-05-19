package ru.ama.ottest.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
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
        setHasOptionsMenu(true)
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
            binding.rvResultList.isNestedScrollingEnabled = false
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
				countOfAnswers,
				timeForTest,
				countOfRightAnswers,
				countOfQuestions,
				percentageOfRightAnswers,
                gameSettings.minPercentOfRightAnswers
            )
            /* binding.tvRequiredAnswers.text = String.format(
                 getString(R.string.required_score),
                 gameSettings.minCountOfRightAnswers
             )*/
           /* binding.tvRequiredPercentage.text = String.format(
                getString(R.string.required_percentage),
                gameSettings.minPercentOfRightAnswers
            )
            binding.tvScorePercentage.text = String.format(
                getString(R.string.score_percentage),
                percentageOfRightAnswers
            )*/
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


 override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_frgmnt_result, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_share -> {
			val shareBody="я ${if (!gameResult.winner) "не " else "успешно " } прошел тест \"${gameResult.title}\" за ${gameResult.timeForTest}минут, верно ответив на ${gameResult.countOfRightAnswers} вопросов из ${gameResult.countOfQuestions} (${gameResult.percentageOfRightAnswers}% верных ответов)"
				sharetext("Поделиться",shareBody,false)
                return true
            }


            else -> return super.onOptionsItemSelected(item)
        }
    }


private fun sharetext(
            textZagol: String,
            textBody: String,
            isEmail: Boolean
        ) {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            //sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)

            if (isEmail) {
                sharingIntent.putExtra(
                    Intent.EXTRA_EMAIL,
                    arrayOf("10ama@mail.ru")
                )
                sharingIntent.type = "message/rfc822"
            } else
                sharingIntent.type = "text/plain"
				sharingIntent.putExtra(
                android.content.Intent.EXTRA_SUBJECT,
                textZagol
            )
            sharingIntent.putExtra(
                android.content.Intent.EXTRA_TEXT, 
                textBody
            )
            val d = Intent.createChooser(
                sharingIntent,
                "использовать"
            )
            requireActivity().startActivity(d)
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
