package ru.ama.ottest.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.ama.ottest.domain.entity.UserAnswerDomModel

object ResultDiffCallback : DiffUtil.ItemCallback<UserAnswerDomModel>() {

    override fun areItemsTheSame(oldItem: UserAnswerDomModel, newItem: UserAnswerDomModel): Boolean {
        return oldItem.number == newItem.number
    }

    override fun areContentsTheSame(oldItem: UserAnswerDomModel, newItem: UserAnswerDomModel): Boolean {
        return oldItem == newItem
    }
}
