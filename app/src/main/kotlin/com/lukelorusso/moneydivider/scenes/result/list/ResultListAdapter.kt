package com.lukelorusso.moneydivider.scenes.result.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lukelorusso.moneydivider.R
import com.lukelorusso.moneydivider.mapper.HistoryMapper
import com.lukelorusso.moneydivider.models.Constant
import com.lukelorusso.moneydivider.models.Transaction
import kotlinx.android.synthetic.main.item_result.view.*

class ResultListAdapter(
    private val listPattern: String = "- %s",
    private val giveSuffix: String = Constant.Message.YOU_OWE,
    private val takeSuffix: String = Constant.Message.YOU_GET
) :
    RecyclerView.Adapter<ResultListAdapter.ViewHolder>() {

    private val historyMapper = HistoryMapper(giveSuffix, takeSuffix)

    var transactionList: List<Transaction> = emptyList()
    var data: List<String> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_result,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.also {
            val sender = data[position]
            it.itemResultSender.text = sender
            it.itemResultHistory.text = historyMapper.map(
                sender,
                transactionList
            )?.joinToString("\n") { line ->
                String.format(
                    listPattern,
                    line.substring(9, line.length)
                )
            }
        }
    }

    override fun getItemCount() = data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}