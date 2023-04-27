package ru.ama.ottest.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.ListAdapter
import com.squareup.picasso.Picasso
import ru.ama.ottest.R
import ru.ama.ottest.databinding.ItemResultBinding
import ru.ama.ottest.domain.entity.QuestionDomModel

class AnswerAdapter(
    private val context: Context
) : ListAdapter<QuestionDomModel, AnswerViewHolder>(AnswerDiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val binding = ItemResultBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AnswerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        val res = getItem(position)

        with(holder.binding) {
            with(res) {
                answers.forEach {
                    var ans = EMPTY_STRING
                    for ((index, element) in answers.withIndex()) {
                        val ss = (if (index == 0) EMPTY_STRING else PERENOS_STROKI_STRING)
                        ans = ans + ss + when (index) {
                            in correct -> String.format(
                                context.getString(R.string.questin_user_correct_answer_id),
                                (index + 1).toString(),
                                element
                            )
                            else -> String.format(
                                context.getString(R.string.questin_user_another_id),
                                (index + 1).toString(),
                                element
                            )
                        }
                    }

                    tvResultQuestion.text = HtmlCompat.fromHtml(
                        String.format(
                            context.getString(R.string.questin_formatted),
                            number.toString(),
                            question
                        ), HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                    tvResultAnswers.text =
                        HtmlCompat.fromHtml(ans, HtmlCompat.FROM_HTML_MODE_LEGACY)

                }
                val isImage = (imageUrl?.endsWith(IMAGE_ENDS)!! && imageUrl?.length!! > 0)
                if (isImage) {
                    Picasso.get().load(imageUrl).placeholder(R.drawable.preload)
                        .into(ivResultQuestion)
                    ivResultQuestion.visibility = View.VISIBLE
                } else ivResultQuestion.visibility = View.GONE

            }
        }
    }

    companion object {

        private const val EMPTY_STRING = ""
        private const val PERENOS_STROKI_STRING = "<br>"
        private const val IMAGE_ENDS = ".png"
    }


}
