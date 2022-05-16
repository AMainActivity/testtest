package ru.ama.ottest.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.ama.ottest.R
import ru.ama.ottest.databinding.FragmentChooseLevelBinding
import ru.ama.ottest.domain.entity.TestInfo
import ru.ama.ottest.presentation.adapters.QuestionsAdapter
import javax.inject.Inject

class ChooseTestFragment : Fragment() {

    private lateinit var binding: FragmentChooseLevelBinding
  private lateinit var viewModel: TestsViewModel

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
        binding = FragmentChooseLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.title ="Список тестов"
        /*viewModel = ViewModelProvider(this, viewModelFactory)[TestsViewModel::class.java]
            binding.buttonLevelTest.setOnClickListener {
                launchGameFragment()
            }*/
            val adapter = QuestionsAdapter(requireContext())
        adapter.onQuestionClickListener = object : QuestionsAdapter.OnQuestionClickListener {
            override fun onQuestionClick(tInfo: TestInfo) {
                Log.e("tInfo",tInfo.toString())
                launchGameFragment(tInfo)
            }
        }
        binding.rvTestsList.adapter = adapter
        binding.rvTestsList.itemAnimator = null
        viewModel = ViewModelProvider(this, viewModelFactory)[TestsViewModel::class.java]
        viewModel.testInfo.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        
    }

    private fun launchGameFragment(tInfo: TestInfo) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameFragment.newInstance(tInfo))
            .addToBackStack(GameFragment.NAME)
            .commit()
    }

    companion object {

        fun newInstance(): ChooseTestFragment {
            return ChooseTestFragment()
        }
    }
}
