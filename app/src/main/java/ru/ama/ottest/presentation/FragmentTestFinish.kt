package ru.ama.ottest.presentation

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.ama.ottest.R
import ru.ama.ottest.databinding.FragmentTestsFinishedBinding
import ru.ama.ottest.domain.entity.TestsResult
import ru.ama.ottest.presentation.adapters.ResultAdapter

class FragmentTestFinish : Fragment() {

    private var _binding: FragmentTestsFinishedBinding? = null
    private val binding: FragmentTestsFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentTestsFinishedBinding == null")
    private lateinit var testsResult: TestsResult
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
        _binding = FragmentTestsFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

  override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = getString(R.string.frgmnt_result_ab_title) //"Разбор ответов"
        (requireActivity() as AppCompatActivity).supportActionBar?.subtitle = null
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
        binding.buttonRetry.setOnClickListener {
            goToStartGame()
        }
        with(testsResult) {
			
            val adapter = ResultAdapter(requireContext())
            binding.rvResultList.setHasFixedSize(false)
            binding.rvResultList.isNestedScrollingEnabled = false
        binding.rvResultList.adapter = adapter
          adapter.submitList(answerOfTest)

            val emojiResId = if (winner) {
                binding.tvZacet.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.result_zacet))
                getString(R.string.frgmnt_result_passed)
            } else {
                binding.tvZacet.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.result_nezacet))
                getString(R.string.frgmnt_result_missed)
            }
            binding.tvZacet.text=emojiResId
            binding.tvScoreAnswers.text = String.format(
                getString(R.string.score_answers),
				countOfAnswers,
				timeForTest,
				countOfRightAnswers,
				countOfQuestions,
				percentageOfRightAnswers,
                minPercentOfRightAnswers
            )
        }
    }

    private fun parseArgs() {
        val args = requireArguments()
        if (!args.containsKey(ARG_GAME_RESULT)) {
            throw RuntimeException("$this must contain argument $ARG_GAME_RESULT")
        }
        args.getParcelable<TestsResult>(ARG_GAME_RESULT)?.let {
            testsResult = it
        }
    }

    private fun goToStartGame() {
        activity?.supportFragmentManager?.popBackStack(
            FragmentTestProcess.NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }


 override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_frgmnt_result, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_share -> {
			val shareBody=
			String.format(getString(R.string.frgmnt_menu_share_body),
			if (!testsResult.winner) getString(R.string.frgmnt_menu_share_ne) else getString(R.string.frgmnt_menu_share_success) ,
			testsResult.title,
			testsResult.timeForTest,
			testsResult.countOfRightAnswers,
			testsResult.countOfQuestions,
			testsResult.percentageOfRightAnswers
			)+"\n\n"+getString(R.string.app_url)


				sharetext(getString(R.string.frgmnt_menu_share_title),shareBody,false)
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

            if (isEmail) {
                sharingIntent.putExtra(
                    Intent.EXTRA_EMAIL,
                    arrayOf(getString(R.string.frgmnt_menu_share_mail))
                )
                sharingIntent.type = SHARE_MAIL_TYPE
            } else
                sharingIntent.type = SHARE_TEXT_TYPE
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
                getString(R.string.frgmnt_menu_share_use)
            )
            requireActivity().startActivity(d)
        }

    companion object {

        private const val ARG_GAME_RESULT = "game_result"
        private const val SHARE_MAIL_TYPE = "message/rfc822"
        private const val SHARE_TEXT_TYPE = "text/plain"

        fun newInstance(testsResult: TestsResult): FragmentTestFinish {
            return FragmentTestFinish().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_GAME_RESULT, testsResult)
                }
            }
        }
    }
}
