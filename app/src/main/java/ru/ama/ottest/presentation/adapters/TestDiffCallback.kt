package ru.ama.ottest.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.ama.ottest.domain.entity.TestInfo
import ru.ama.ottest.domain.entity.TestQuestion

object TestDiffCallback : DiffUtil.ItemCallback<TestInfo>() {

    override fun areItemsTheSame(oldItem: TestInfo, newItem: TestInfo): Boolean {
        return oldItem.testId == newItem.testId
    }

    override fun areContentsTheSame(oldItem: TestInfo, newItem: TestInfo): Boolean {
        return oldItem == newItem
    }
}
