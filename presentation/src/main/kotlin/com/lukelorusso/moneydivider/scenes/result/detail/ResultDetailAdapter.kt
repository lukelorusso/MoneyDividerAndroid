package com.lukelorusso.moneydivider.scenes.result.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lukelorusso.moneydivider.R
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_result.view.*

class ResultDetailAdapter : RecyclerView.Adapter<ResultDetailAdapter.ViewHolder>() {

    val intentItemLoad = PublishSubject.create<String>()

    var data: List<String> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var historyMap: MutableMap<String, String> = mutableMapOf()

    var situationMap: MutableMap<String, String> = mutableMapOf()

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
            val history = historyMap[sender]
            val situation = situationMap[sender]
            if (situation == null || history == null)
                intentItemLoad.onNext(sender)
            else {
                val even = position % 2 == 0
                val context = it.context
                val colorRes = if (even) R.color.background_evens else R.color.background_odds
                it.content.setBackgroundColor(ContextCompat.getColor(context, colorRes))
                val description = "$sender $situation"
                it.itemResultSituation.text = description
                it.itemResultHistory.text = history
            }
        }
    }

    override fun getItemCount() = data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}