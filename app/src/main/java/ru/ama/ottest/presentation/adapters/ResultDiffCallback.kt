package ru.ama.ottest.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.ama.ottest.domain.entity.AnswerOfTest

object ResultDiffCallback : DiffUtil.ItemCallback<AnswerOfTest>() {

    override fun areItemsTheSame(oldItem: AnswerOfTest, newItem: AnswerOfTest): Boolean {
        return oldItem.number == newItem.number
    }

    override fun areContentsTheSame(oldItem: AnswerOfTest, newItem: AnswerOfTest): Boolean {
        return oldItem == newItem
    }
}
