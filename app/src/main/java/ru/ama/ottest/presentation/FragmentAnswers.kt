package ru.ama.ottest.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import ru.ama.ottest.R
import ru.ama.ottest.databinding.FragmentTestsAnswersBinding
import ru.ama.ottest.domain.entity.TestsResult
import ru.ama.ottest.presentation.adapters.AnswerAdapter
import ru.ama.ottest.presentation.adapters.ResultAdapter
import javax.inject.Inject

class FragmentAnswers : Fragment() {

    //private lateinit var binding: FragmentTestsAnswersBinding
    private var _binding: FragmentTestsAnswersBinding? = null
    private val binding: FragmentTestsAnswersBinding
        get() = _binding ?: throw RuntimeException("FragmentTestsAnswersBinding == null")
    private var testTitle: String=PARAMETER_EMPTY_STRING
    private var testId: Int=PARAMETER_MINUS_ODIN
	   private lateinit var viewModel: ViewModelAnswers
	  private val component by lazy {
        (requireActivity().application as MyApplication).component
    }
	@Inject
    lateinit var viewModelFactory: ViewModelFactory
	
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            goToStartGame()
            remove()
        }
    }
 override fun onAttach(context: Context) {
        component.inject(this)

        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTestsAnswersBinding.inflate(inflater, container, false)
        return binding.root
    }

  override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = testTitle
        (requireActivity() as AppCompatActivity).supportActionBar?.subtitle = null
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
        binding.buttonBack.setOnClickListener {
            goToStartGame()
            Toast.makeText(requireContext(),"sdsd",Toast.LENGTH_SHORT).show()
        }
		
		
        viewModel = ViewModelProvider(this, viewModelFactory)[ViewModelAnswers::class.java]
		viewModel.getTestAnswers(testId)
		viewModel.listOfAnswers.observe(viewLifecycleOwner){
           
            val adapter = AnswerAdapter(requireContext())
			binding.rvAnswersList.adapter = adapter
            adapter.submitList(it)
        
        }
		
       
    }



    private fun parseArgs() {
        val args = requireArguments()
        if (!args.containsKey(ARG_TEST_ANSWERS_TITLE)) {
            throw RuntimeException("$this must contain argument $ARG_TEST_ANSWERS_TITLE")
        }
        args.getString(ARG_TEST_ANSWERS_TITLE)?.let {
            testTitle = it
        }
        if (!args.containsKey(ARG_TEST_ANSWERS_ID)) {
            throw RuntimeException("$this must contain argument $ARG_TEST_ANSWERS_ID")
        }
        args.getInt(ARG_TEST_ANSWERS_ID)?.let {
            testId = it
        }
    }

    private fun goToStartGame() {
        activity?.supportFragmentManager?.popBackStack(
            TestsFragment.NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }




    companion object {

        private const val PARAMETER_MINUS_ODIN = -1
        private const val PARAMETER_EMPTY_STRING =""
        private const val ARG_TEST_ANSWERS_TITLE = "test_answers_title"
        private const val ARG_TEST_ANSWERS_ID = "test_answers_id"
        const val NAME = "FragmentAnswers"

        fun newInstance(testInfo: String, testId:Int): FragmentAnswers {
            return FragmentAnswers().apply {
                arguments = Bundle().apply {
                    putString(ARG_TEST_ANSWERS_TITLE, testInfo)
                    putInt(ARG_TEST_ANSWERS_ID, testId)
                }
            }
        }
    }
}