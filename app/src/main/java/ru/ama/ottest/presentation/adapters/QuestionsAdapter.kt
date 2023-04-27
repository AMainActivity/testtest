package ru.ama.ottest.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.squareup.picasso.Picasso
import ru.ama.ottest.R
import ru.ama.ottest.databinding.ItemTestInfoBinding
import ru.ama.ottest.domain.entity.TestInfoDomModel

class QuestionsAdapter(
    private val context: Context
) : ListAdapter<TestInfoDomModel, QuestionViewHolder>(QuestionDiffCallback) {

    var onQuestionClickListener: OnQuestionClickListener? = null
    var onButtonAnswersClickListener: OnButtonAnswersClickListener? = null

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
        with(holder.binding) {
            with(test) {
                val testInfoTemplate = context.resources.getString(R.string.test_info)
                tvTitle.text = title
                tvInfo.text = String.format(
                    testInfoTemplate,
                    countOfQuestions.toString(),
                    (testTimeInSeconds / SECONDS_IN_MINUTE).toString(),
                    minPercentOfRightAnswers.toString()
                )
                mainImageUrl?.let {
                    val isImage = (it.endsWith(IMAGE_ENDS) && it.length > 0)
                    if (isImage) Picasso.get().load(mainImageUrl).placeholder(R.drawable.preload)
                        .into(ivLogoTest)
                    ivLogoTest.visibility = if (isImage) View.VISIBLE else View.GONE
                }

                frgmntTestAnswers.setOnClickListener {
                    onButtonAnswersClickListener?.onButtonAnswersClick(title, testId)
                }

                root.setOnClickListener {
                    onQuestionClickListener?.onQuestionClick(this)
                }
            }
        }
    }

    companion object {

        private const val IMAGE_ENDS = ".png"
        private const val SECONDS_IN_MINUTE = 60
    }

    interface OnButtonAnswersClickListener {
        fun onButtonAnswersClick(testInfo: String, testId: Int)
    }

    interface OnQuestionClickListener {
        fun onQuestionClick(testInfoDomModel: TestInfoDomModel)
    }
}
