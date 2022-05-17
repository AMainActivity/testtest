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
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
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
        binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title =testInfo.title

  
	  
        viewModel = ViewModelProvider(this, viewModelFactory)[GameViewModel::class.java]

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
                }
                is CurrentNoOfQuestion -> {	
if (it.value<viewModel.testInfo.countOfQuestions)
      setActionBarSubTitle("${it.value+1}/${viewModel.testInfo.countOfQuestions} ")
                     Toast.makeText(requireContext(), "${it.value+1}/${viewModel.testInfo.countOfQuestions} ",Toast.LENGTH_SHORT).show() 
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
        viewModel.question.observe(viewLifecycleOwner) {
            with(binding) {
            val s=it.imageUrl
              //  if (s?.length!!>0) Picasso.get().load(s).into(ivQuestion)
						val isImage=s?.endsWith(".png")!! && s?.length!!>0
                 if (isImage)
				 {Picasso.get().load(s)/*.placeholder(R.drawable.ic_brain)*/.into(ivQuestion)
					 ivQuestion.visibility=View.VISIBLE}
				 else
					 ivQuestion.visibility=View.GONE
                tvQuestion.text = "${it.number}. ${it.question}"
				val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,
				it.answers )
				lvAnswers.adapter = adapter
            }
        }
        viewModel.gameResult.observe(viewLifecycleOwner) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, GameFinishedFragment.newInstance(it))
                .addToBackStack(null)
                .commit()
        }
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

        fun newInstance(testId:TestInfo): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_TEST_INFO, testId)
                }
            }
        }
    }
}
