package com.example.aeon.ui.payments

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.aeon.domain.model.local.Payment
import com.example.aeon.ui.payments.view.PaymentCell

class PaymentsAdapter : RecyclerView.Adapter<PaymentsAdapter.ViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Payment>() {

        override fun areItemsTheSame(oldItem: Payment, newItem: Payment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Payment, newItem: Payment): Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer: AsyncListDiffer<Payment> = AsyncListDiffer(this, diffCallback)

    fun submitList(payments: List<Payment>) = asyncListDiffer.submitList(payments)

    inner class ViewHolder(cell: PaymentCell) : RecyclerView.ViewHolder(cell)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cell = PaymentCell(parent.context)
        return ViewHolder(cell)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val payment = asyncListDiffer.currentList[position]
        (holder.itemView as PaymentCell).payment = payment
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size
}