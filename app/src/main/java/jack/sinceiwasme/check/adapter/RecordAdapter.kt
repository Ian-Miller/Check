package jack.sinceiwasme.check.adapter

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import jack.sinceiwasme.check.R
import jack.sinceiwasme.check.data.Record

class RecordAdapter : PagedListAdapter<Record, RecordAdapter.Holder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context), parent, R.layout.item_record)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class Holder(inflater: LayoutInflater, parent: ViewGroup, id: Int) :
            RecyclerView.ViewHolder(inflater.inflate(id, parent, false)) {
        val recordView = itemView.findViewById<TextView>(R.id.record)
        val labelView = itemView.findViewById<TextView>(R.id.label)
        fun bind(record: Record?) {
            if (record == null){
                clear()
            } else {
                recordView.text = Record.getFormattedNumber(record.record)
                labelView.text = record.label
            }
        }
        private fun clear(){
            recordView.text = "null"
            labelView.text = "null"
        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Record>() {

            override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
                return oldItem.record == newItem.record && oldItem.label == newItem.label && oldItem.time == newItem.time
            }
        }
    }

}
