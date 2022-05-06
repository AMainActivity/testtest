package ru.ama.ottest.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.ama.ottest.domain.entity.TestQuestion
import com.squareup.picasso.Picasso
import ru.ama.ottest.databinding.ItemCoinInfoBinding

class QuestionsAdapter(
    private val context: Context
) : ListAdapter<TestQuestion, QuestionViewHolder>(TestDiffCallback) {

    var onQuestionClickListener: OnQuestionClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = ItemCoinInfoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val coin = getItem(position)
        with(holder.binding) {
            with(coin) {
              //  val symbolsTemplate = context.resources.getString(R.string.symbols_template)
//                tvSymbols.text = question //String.format(symbolsTemplate, fromSymbol, toSymbol)
                tvPrice.text = number.toString()
                tvLastUpdate.text =number.toString()//String.format(lastUpdateTemplate, lastUpdate)
               /* Picasso.get().load(imageUrl).into(ivLogoCoin)
                root.setOnClickListener {
                    onQuestionClickListener?.onQuestionClick(this)
                }*/
            }
        }
    }

    interface OnQuestionClickListener {
        fun onQuestionClick(testQuestion: TestQuestion)
    }
}
