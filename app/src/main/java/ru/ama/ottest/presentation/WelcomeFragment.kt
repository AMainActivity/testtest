package ru.ama.ottest.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.ama.ottest.R
import ru.ama.ottest.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonUnderstand.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, ChooseTestFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    companion object {

        fun newInstance(): WelcomeFragment {
            return WelcomeFragment()
        }
    }
}
