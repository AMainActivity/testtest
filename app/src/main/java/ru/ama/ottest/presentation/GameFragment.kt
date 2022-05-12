package ru.ama.ottest.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.ama.ottest.R
import ru.ama.ottest.databinding.FragmentGameBinding
import ru.ama.ottest.domain.entity.TestInfo
import javax.inject.Inject

class GameFragment : Fragment() {

   // @Inject
   private lateinit var testInfo: TestInfo
    private lateinit var viewModel: GameViewModel
	  private val component by lazy {
        (requireActivity().application as MyApplication).component
    }
	@Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: FragmentGameBinding
	/*  private val binding by lazy {
        FragmentGameBinding.inflate(layoutInflater)
    }*/

    //private lateinit var level: Level
   // private var optionsTextViews = mutableListOf<TextView>()

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
        binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      
	  /*
	   val adapter = QuestionsAdapter(this)
        adapter.onQuestionClickListener = object : QuestionsAdapter.OnQuestionClickListener {
            override fun onQuestionClick(coinPriceInfo: TestQuestion) {
             
            }
        }
        binding.rvCoinPriceList.adapter = adapter
        binding.rvCoinPriceList.itemAnimator = null
        viewModel = ViewModelProvider(this, viewModelFactory)[CoinViewModel::class.java]
        viewModel.coinInfoList.observe(this) {
            adapter.submitList(it)
        }
       viewModel.testInfo?.value.let{ viewModel.testInfo?.observe(this){
            Log.e("testInfo",it.toString())
        }
	  */
	  
	  
        viewModel = ViewModelProvider(this, viewModelFactory)[GameViewModel::class.java]
	  //  viewModel = ViewModelProvider(this)[GameViewModel::class.java]
      
        setupClickListenersToOptions()
		viewModel.setParams(testInfo)
        observeViewModel()
        /*if (savedInstanceState == null) {
            viewModel.startGame()
        }*/
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
   // val element = adapter.getItemAtPosition(position)
     viewModel.chooseAnswer(position)
}      
    }

    private fun observeViewModel() {
		 viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is ReadyStart -> {
                    viewModel.startGame()
                    //binding.progressBar.max=viewModel.testInfo.countOfQuestions
                }
               /* is CurrentNoOfQuestion -> {
                     Toast.makeText(requireContext(), "${it.value+1}/${viewModel.testInfo.countOfQuestions} ",Toast.LENGTH_SHORT).show() 
                }*/
                is MinPercentOfRightAnswers -> {
                    binding.progressBar.secondaryProgress =  it.value
                }
                /*is PercentOfRightAnswers -> {
                         binding.progressBar.setProgress( it.value, true)
                }*/
                is LeftFormattedTime -> {
                      binding.tvTimer.text = it.value
                }
               /* is Question -> {
                     with(binding) {
                tvQuestion.text = "${it.value.number} ${it.value.question}"
				val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,
				it.value.answers )
				lvAnswers.adapter = adapter
            } 
                }
                is GameResult -> {
                     requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, GameFinishedFragment.newInstance(it.value))
                .addToBackStack(null)
                .commit()
                }*/
            }
        }
							
		/*
		
        viewModel.readyStart.observe(viewLifecycleOwner){
            viewModel.startGame()
        }
*/
        viewModel.question.observe(viewLifecycleOwner) {
            with(binding) {
                tvQuestion.text = "${it.number} ${it.question}"
				val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,
				it.answers )
				lvAnswers.adapter = adapter
            }
        }
        viewModel.currentNoOfQuestion.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "${it+1}/${viewModel.testInfo.countOfQuestions} ",Toast.LENGTH_SHORT).show()
        }/*
        viewModel.leftFormattedTime.observe(viewLifecycleOwner) {
            binding.tvTimer.text = it
        }*/
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
		/*
        viewModel.minPercentOfRightAnswers.observe(viewLifecycleOwner) {
            binding.progressBar.secondaryProgress = it
        }*/
    }

   

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

        private const val ARG_TEST_INFO = "testInfo"
        const val NAME = "GameFragment"

        fun newInstance(testInfo:TestInfo): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_TEST_INFO, testInfo)
                }
            }
        }
    }
}
