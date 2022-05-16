package ru.ama.ottest.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import com.squareup.picasso.Picasso
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
        holder.binding.frgmntSotrButMap.setOnClickListener {
            Toast.makeText(holder.binding.frgmntSotrButMap.context,test.title,Toast.LENGTH_SHORT).show()
        }
        with(holder.binding) {
            with(test) {
              //  val symbolsTemplate = context.resources.getString(R.string.symbols_template)
//                tvSymbols.text = question //String.format(symbolsTemplate, fromSymbol, toSymbol)
                tvId.text = testId.toString()
                tvTitle.text =title//String.format(lastUpdateTemplate, lastUpdate)
                tvInfo.text="Время теста: ${(testTimeInSeconds/60).toString()}мин, вопросов: ${countOfQuestions}, процент сдачи: $minPercentOfRightAnswers"
                mainImageUrl?.let{ if (mainImageUrl?.length!!>0) Picasso.get().load(mainImageUrl).into(ivLogoTest)}
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
