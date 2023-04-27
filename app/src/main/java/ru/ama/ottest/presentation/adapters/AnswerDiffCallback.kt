package ru.ama.ottest.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.ama.ottest.domain.entity.QuestionDomModel

object AnswerDiffCallback : DiffUtil.ItemCallback<QuestionDomModel>() {

    override fun areItemsTheSame(oldItem: QuestionDomModel, newItem: QuestionDomModel): Boolean {
        return oldItem.number == newItem.number
    }

    override fun areContentsTheSame(oldItem: QuestionDomModel, newItem: QuestionDomModel): Boolean {
        return oldItem == newItem
    }
}
