package ru.ama.ottest.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import com.squareup.picasso.Picasso
import ru.ama.ottest.R
import ru.ama.ottest.databinding.ItemTestInfoBinding
import ru.ama.ottest.domain.entity.TestInfo
import ru.ama.ottest.domain.entity.TestQuestion

class QuestionsAdapter(
    private val context: Context
) : ListAdapter<TestInfo, QuestionViewHolder>(TestDiffCallback) {

    var onQuestionClickListener: OnQuestionClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = ItemTestInfoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val test = getItem(position)
        /*holder.binding.frgmntSotrButMap.setOnClickListener {
            Toast.makeText(holder.binding.frgmntSotrButMap.context,test.title,Toast.LENGTH_SHORT).show()
        }*/
        with(holder.binding) {
            with(test) {
                val testInfoTemplate = context.resources.getString(R.string.test_info)
                tvTitle.text =title
                tvInfo.text=String.format(
                    testInfoTemplate,
                    countOfQuestions.toString(),
                    (testTimeInSeconds / 60).toString(),
                    minPercentOfRightAnswers.toString()
                )
				val isImage=(mainImageUrl?.endsWith(".png")!! && mainImageUrl?.length!!>0)
                 if (isImage)
				 {Picasso.get().load(mainImageUrl).into(ivLogoTest)			 
					 ivLogoTest.visibility= View.VISIBLE}
				 else
					 ivLogoTest.visibility=View.GONE
                root.setOnClickListener {
                    onQuestionClickListener?.onQuestionClick(this)
                }
            }
        }
    }

    interface OnQuestionClickListener {
        fun onQuestionClick(testInfo: TestInfo)
    }
}
