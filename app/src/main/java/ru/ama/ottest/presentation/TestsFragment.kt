package ru.ama.ottest.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import ru.ama.ottest.R
import ru.ama.ottest.databinding.FragmentTestsBinding
import ru.ama.ottest.domain.entity.TestInfo
import javax.inject.Inject

class TestsFragment : Fragment() {

   private lateinit var testInfo: TestInfo
    private lateinit var viewModel: TestProcessViewModel
	  private val component by lazy {
        (requireActivity().application as MyApplication).component
    }
	@Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: FragmentTestsBinding


private fun setActionBarSubTitle(txt:String)
{
    (requireActivity() as AppCompatActivity).supportActionBar?.subtitle =txt
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title =testInfo.title

  
	  
        viewModel = ViewModelProvider(this, viewModelFactory)[TestProcessViewModel::class.java]

        setupClickListenersToOptions()
		viewModel.setParams(testInfo)
        observeViewModel()
     
    }

   private fun parseArgs() {
        val args = requireArguments()
        if (!args.containsKey(ARG_TEST_INFO)) {
            throw RuntimeException("Required param TestInfo is absent")
        }
        args.getParcelable<TestInfo>(ARG_TEST_INFO)?.let {
            testInfo = it
        }
    }


    private fun setupClickListenersToOptions() {
		binding.lvAnswers.setOnItemClickListener { parent, view, position, id ->
     viewModel.chooseAnswer(position)
}      
    }

    private fun observeViewModel() {
		 viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is ReadyStart -> {
                    viewModel.startGame()
                }
                is QuestionState -> {
                    with(binding) {
                        val s=it.value.imageUrl
                        val isImage=s?.endsWith(".png")!! && s?.length!!>0
                        if (isImage)
                        {Picasso.get().load(s).placeholder(R.drawable.preload).into(ivQuestion)
                            ivQuestion.visibility=View.VISIBLE}
                        else
                            ivQuestion.visibility=View.GONE
                        tvQuestion.text = "${it.value.question}"
                        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,
                            it.value.answers )
                        lvAnswers.adapter = adapter
                    }
                }
                is GameResultState -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, TestsFinishedFragment.newInstance(it.value))
                        .addToBackStack(null)
                        .commit()
                }
                is CurrentNoOfQuestion -> {	
if (it.value<viewModel.testInfo.countOfQuestions)
      setActionBarSubTitle("${it.value+1}/${viewModel.testInfo.countOfQuestions} ")
                }
                is LeftFormattedTime -> {
                      binding.tvTimer.text = it.value
                }
            }
        }
							
		/*
		
        viewModel.readyStart.observe(viewLifecycleOwner){
            viewModel.startGame()
        }
*/
       /* viewModel.question.observe(viewLifecycleOwner) {
            with(binding) {
            val s=it.imageUrl
              //  if (s?.length!!>0) Picasso.get().load(s).into(ivQuestion)
						val isImage=s?.endsWith(".png")!! && s?.length!!>0
                 if (isImage)
				 {Picasso.get().load(s).placeholder(R.drawable.preload).into(ivQuestion)
					 ivQuestion.visibility=View.VISIBLE}
				 else
					 ivQuestion.visibility=View.GONE
                tvQuestion.text = "${it.question}"
				val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,
				it.answers )
				lvAnswers.adapter = adapter
            }
        }*/
      /*  viewModel.gameResult.observe(viewLifecycleOwner) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, GameFinishedFragment.newInstance(it))
                .addToBackStack(null)
                .commit()
        }*/
        viewModel.percentOfRightAnswersStr.observe(viewLifecycleOwner) {
				binding.tvPercentOfRight.text=it.toString()
        }
        viewModel.enoughPercentage.observe(viewLifecycleOwner) {
            setupProgressColorByState(it)
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
        const val NAME = "GameFragment"

        fun newInstance(testId:TestInfo): TestsFragment {
            return TestsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_TEST_INFO, testId)
                }
            }
        }
    }
}