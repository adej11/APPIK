package com.adej.appik.model

import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.adej.appik.R
import com.adej.appik.activity.PreviewClaim
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.text.SimpleDateFormat


class ClaimAdapter  constructor(private val context: Context, private val datas: ArrayList<mClaim>)
    : RecyclerView.Adapter<ViewHolderData>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderData {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_task, parent, false)
        return ViewHolderData(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolderData, position: Int) = holder.bindItem(datas[position], context)
}

class ViewHolderData(view: View) : RecyclerView.ViewHolder(view) {

    private val date: TextView = view.find(R.id.follow_up_date)
    private val customerName: TextView = view.find(R.id.customer_name)
    private val phoneNumber: TextView = view.find(R.id.phone_number)
    private val type: TextView = view.find(R.id.type)
    private val cardView: CardView = view.find(R.id.card_view)

    fun bindItem(datas: mClaim, context: Context) {
        date.text =  datas.description
        customerName.text = datas.policyNumber
       // val df = SimpleDateFormat("dd-yyyy-MM")
       //  df.format(datas.incidentDate)
        phoneNumber.text = datas.reportDate
        type.text = datas.causeOfIncident
        cardView.onClick {
                var intent = Intent(context, PreviewClaim::class.java)
                intent.putExtra("data", datas)
                intent.putExtra("type", "view")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)

        }
    }

}