package ru.ama.ottest.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.ama.ottest.domain.entity.TestInfo

object QuestionDiffCallback : DiffUtil.ItemCallback<TestInfo>() {

    override fun areItemsTheSame(oldItem: TestInfo, newItem: TestInfo): Boolean {
        return oldItem.testId == newItem.testId
    }

    override fun areContentsTheSame(oldItem: TestInfo, newItem: TestInfo): Boolean {
        return oldItem == newItem
    }
}
