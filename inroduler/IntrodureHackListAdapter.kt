package com.applock.lock.apps.fingerprint.password.inroduler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.applock.lock.apps.fingerprint.password.R
import com.applock.lock.apps.fingerprint.password.view.Hack
import com.bumptech.glide.Glide

class IntrodureHackListAdapter(val ctx: Context, var hackList: List<Hack>) :
    RecyclerView.Adapter<IntrodureHackListAdapter.ViewHolder>(){

    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onIntrodureHackItemClick(position: Int, app: String, path: String, timestamp: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTimeStamp: TextView = itemView.findViewById(R.id.tvTimeStamp)
        private val ivHackThumb: ImageView = itemView.findViewById(R.id.ivHackThumb)

        fun bind(position: Int)
        {
            if (hackList[position].timestamp.isNotEmpty()) {
                tvTimeStamp.text=hackList[position].timestamp
            }
            if (hackList[position].path.isNotEmpty()) {
                Glide.with(ctx).load(hackList[position].path).into(ivHackThumb)
            }

            // Click listener for the item view
            itemView.setOnClickListener {
                itemClickListener?.onIntrodureHackItemClick(position,hackList[position].app,hackList[position].path,hackList[position].timestamp)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_introdure_hack, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return hackList.size
    }

}
