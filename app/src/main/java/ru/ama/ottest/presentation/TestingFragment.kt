package ru.ama.ottest.presentation

import android.content.Context
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import ru.ama.ottest.R
import ru.ama.ottest.databinding.FragmentTestsBinding
import ru.ama.ottest.domain.entity.TestInfoDomModel
import javax.inject.Inject

class TestingFragment : Fragment() {

    private lateinit var testInfoDomModel: TestInfoDomModel
    private lateinit var viewModel: TestingViewModel
    private val component by lazy {
        (requireActivity().application as MyApplication).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private var _binding: FragmentTestsBinding? = null
    private val binding: FragmentTestsBinding
        get() = _binding ?: throw RuntimeException("FragmentTestsBinding == null")


    private fun setActionBarSubTitle(txt: String) {
        (requireActivity() as AppCompatActivity).supportActionBar?.subtitle = txt
    }

    override fun onAttach(context: Context) {
        component.inject(this)

        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = testInfoDomModel.title
        viewModel = ViewModelProvider(this, viewModelFactory)[TestingViewModel::class.java]
        setupClickListenersForItems()
        viewModel.setParams(testInfoDomModel)
        observeViewModel()
        binding.buttonSetAnswer.isEnabled = false
    }

    private fun parseArgs() {
        val args = requireArguments()
        if (!args.containsKey(ARG_TEST_INFO)) {
            throw RuntimeException("Required param TestInfo is absent")
        }
        args.getParcelable<TestInfoDomModel>(ARG_TEST_INFO)?.let {
            testInfoDomModel = it
        }
    }

    private fun setupClickListenersForItems() {
        binding.lvAnswers.setOnItemClickListener { parent, view, position, id ->
            binding.buttonSetAnswer.isEnabled = true
        }

        binding.buttonSetAnswer.setOnClickListener {
            val rList: MutableList<Int> = mutableListOf()
            val r = binding.lvAnswers.checkedItemPosition
            when (binding.lvAnswers.choiceMode) {
                ListView.CHOICE_MODE_SINGLE -> {
                    rList.clear()
                    rList.add(r)
                }
                ListView.CHOICE_MODE_MULTIPLE -> {
                    val sbArray: SparseBooleanArray = binding.lvAnswers.checkedItemPositions
                    rList.clear()
                    for (i in 0 until sbArray.size()) {
                        val key = sbArray.keyAt(i)
                        if (sbArray[key]) {
                            rList.add(key)
                        }
                    }
                }
            }
            viewModel.chooseAnswer(rList)


            binding.buttonSetAnswer.isEnabled = false
        }
    }

    private fun getAdapter(correct: List<Int>, ar: List<String>): ArrayAdapter<String> {

        if (correct.size == 1) binding.lvAnswers.choiceMode = ListView.CHOICE_MODE_SINGLE
        else binding.lvAnswers.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        return if (correct.size == 1) ArrayAdapter(
            requireContext(), R.layout.item_single_choice, ar
        )
        else ArrayAdapter(
            requireContext(), R.layout.item_multi_choice, ar
        )
    }

    private fun observeViewModel() {

        viewModel.readyStart.observe(viewLifecycleOwner) {
            viewModel.startGame()
        }

        viewModel.question.observe(viewLifecycleOwner) {
            with(binding) {
                val s = it.imageUrl
                val isImage = s?.endsWith(".png")!! && s?.length!! > 0
                if (isImage) {
                    Picasso.get().load(s).placeholder(R.drawable.preload).into(ivQuestion)
                    ivQuestion.visibility = View.VISIBLE
                } else ivQuestion.visibility = View.GONE
                tvQuestion.text = "${it.question}"
                lvAnswers.adapter = getAdapter(it.correct, it.answers)
            }
        }
        viewModel.gameResult.observe(viewLifecycleOwner) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, TestFinishFragment.newInstance(it))
                .addToBackStack(null).commit()
        }
        viewModel.percentOfRightAnswersStr.observe(viewLifecycleOwner) {
            binding.tvPercentOfRight.text = it.toString()
        }
        viewModel.enoughPercentage.observe(viewLifecycleOwner) {
            setupProgressColorByState(it)
        }

        viewModel.curNumOfQuestion.observe(viewLifecycleOwner) {
            if (it < viewModel.testInfoDomModel.countOfQuestions) setActionBarSubTitle("${it + 1}/${viewModel.testInfoDomModel.countOfQuestions} ")
        }

        viewModel.leftFormattedTime.observe(viewLifecycleOwner) {
            binding.tvTimer.text = it
        }
    }


    private fun setupProgressColorByState(enoughPercentage: Boolean) {
        val colorResId = if (enoughPercentage) {
            android.R.color.holo_green_light
        } else {
            android.R.color.holo_red_light
        }
        val color = ContextCompat.getColor(requireContext(), colorResId)
        binding.tvPercentOfRight.setBackgroundColor((color))
    }

    companion object {

        private const val ARG_TEST_INFO = "testInfo"
        const val NAME = "TestFragment"

        fun newInstance(testId: TestInfoDomModel): TestingFragment {
            return TestingFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_TEST_INFO, testId)
                }
            }
        }
    }
}
