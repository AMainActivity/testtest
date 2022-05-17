package ru.ama.ottest.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.ama.ottest.domain.entity.ResultOfTest
import ru.ama.ottest.domain.entity.TestQuestion

object ResultDiffCallback : DiffUtil.ItemCallback<ResultOfTest>() {

    override fun areItemsTheSame(oldItem: ResultOfTest, newItem: ResultOfTest): Boolean {
        return oldItem.number == newItem.number
    }

    override fun areContentsTheSame(oldItem: ResultOfTest, newItem: ResultOfTest): Boolean {
        return oldItem == newItem
    }
}
