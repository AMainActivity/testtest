package ru.ama.ottest.presentation.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.ListAdapter
import com.squareup.picasso.Picasso
import ru.ama.ottest.R
import ru.ama.ottest.databinding.ItemResultBinding
import ru.ama.ottest.databinding.ItemTestInfoBinding
import ru.ama.ottest.domain.entity.ResultOfTest

class ResultAdapter(
    private val context: Context
) : ListAdapter<ResultOfTest, ResultViewHolder>(ResultDiffCallback) {

    var onResultClickListener: OnResultClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ItemResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val res = getItem(position)

        with(holder.binding) {
            with(res) {
				     var html = ""
            answers.forEach {
                html = "<b>${number.toString()}. ${question}</b>"
                var ans = ""
                for ((index, element) in answers.withIndex()) {
                    val ss=(if (index==0) "" else "<br>")
                    ans = ans+ss+ when (index) {
                        indexOfUserAnswer ->
                            "<font color=#c10000>- ${element}</font>"
                        indexOfCorrect ->
                            "<font color=#009f00>- ${element}</font>"
                        else ->
                            "<font color=gray>- ${element}</font>"
                    }
                    //println("$index: $element")
                }
               // html = "$html <br> $ans"
                //  "<font color=red>${answer}</font> <br><br><font color=green>${correct}</font>"
                tvResultQuestion.text =  HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
                tvResultAnswers.text =  HtmlCompat.fromHtml(ans, HtmlCompat.FROM_HTML_MODE_LEGACY)

            }				
				Log.e("ResultAdapterURL",imageUrl!!)
				val isImage=(imageUrl?.endsWith(".png")!! && imageUrl?.length!!>0)
                 if (isImage)
				 {Picasso.get().load(imageUrl).placeholder(R.drawable.preload).into(ivResultQuestion)
                     ivResultQuestion.visibility= View.VISIBLE}
				 else
                     ivResultQuestion.visibility=View.GONE
                root.setOnClickListener {
                    onResultClickListener?.onResultClick(this)
                }
            }
        }
    }

    interface OnResultClickListener {
        fun onResultClick(resultOfTest: ResultOfTest)
    }
}
