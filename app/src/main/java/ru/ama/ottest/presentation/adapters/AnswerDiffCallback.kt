package ru.ama.ottest.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.ama.ottest.domain.entity.TestInfo
import ru.ama.ottest.domain.entity.TestQuestion

object AnswerDiffCallback : DiffUtil.ItemCallback<TestQuestion>() {

    override fun areItemsTheSame(oldItem: TestQuestion, newItem: TestQuestion): Boolean {
        return oldItem.number == newItem.number
    }

    override fun areContentsTheSame(oldItem: TestQuestion, newItem: TestQuestion): Boolean {
        return oldItem == newItem
    }
}
