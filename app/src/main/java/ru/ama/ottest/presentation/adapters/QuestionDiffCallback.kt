package ru.ama.ottest.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.ama.ottest.domain.entity.TestInfoDomModel

object QuestionDiffCallback : DiffUtil.ItemCallback<TestInfoDomModel>() {

    override fun areItemsTheSame(oldItem: TestInfoDomModel, newItem: TestInfoDomModel): Boolean {
        return oldItem.testId == newItem.testId
    }

    override fun areContentsTheSame(oldItem: TestInfoDomModel, newItem: TestInfoDomModel): Boolean {
        return oldItem == newItem
    }
}
