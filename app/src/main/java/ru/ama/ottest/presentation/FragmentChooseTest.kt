package ru.ama.ottest.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.ama.ottest.R
import ru.ama.ottest.databinding.FragmentChooseTestsBinding
import ru.ama.ottest.domain.entity.TestInfo
import ru.ama.ottest.presentation.adapters.QuestionsAdapter
import javax.inject.Inject

class FragmentChooseTest : Fragment() {

    private var _binding: FragmentChooseTestsBinding? = null
    private val binding: FragmentChooseTestsBinding
        get() = _binding ?: throw RuntimeException("FragmentChooseTestsBinding == null")
    private lateinit var viewModel: ViewModelTestList

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val component by lazy {
        (requireActivity().application as MyApplication).component
    }

    override fun onAttach(context: Context) {
        component.inject(this)

        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseTestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            getString(R.string.frgmnt_list_test_ab_title)
        (requireActivity() as AppCompatActivity).supportActionBar?.subtitle = null

        val adapter = QuestionsAdapter(requireContext())
        adapter.onQuestionClickListener = object : QuestionsAdapter.OnQuestionClickListener {
            override fun onQuestionClick(tInfo: TestInfo) {
                launchGameFragment(tInfo)
            }
        }
        adapter.onButtonAnswersClickListener = object :
            QuestionsAdapter.OnButtonAnswersClickListener {
            override fun onButtonAnswersClick(testInfo: String, testId: Int) {
                launchFragmentAnswers(testInfo, testId)
            }
        }

        binding.rvTestsList.adapter = adapter
        binding.rvTestsList.itemAnimator = null
        viewModel = ViewModelProvider(this, viewModelFactory)[ViewModelTestList::class.java]
        viewModel.testInfo?.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

    }

    private fun launchFragmentAnswers(testInfo: String, testId: Int) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, FragmentAnswers.newInstance(testInfo, testId))
            .addToBackStack(null)
            .commit()
    }


    private fun launchGameFragment(tInfo: TestInfo) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, FragmentTestProcess.newInstance(tInfo))
            .addToBackStack(FragmentTestProcess.NAME)
            .commit()
    }

    companion object {

        fun newInstance(): FragmentChooseTest {
            return FragmentChooseTest()
        }
    }
}
