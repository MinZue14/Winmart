package com.example.winmart.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.winmart.Database.Users
import com.example.winmart.R

class UserAdapter(val context: Context, var userList: ArrayList<Users>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewUsername: TextView = itemView.findViewById(R.id.invoiceProducts)
        val textViewEmail: TextView = itemView.findViewById(R.id.TextViewUsermail)
        val textViewAddress: TextView = itemView.findViewById(R.id.TextViewUserAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.table_user, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.textViewUsername.text = user.username
        holder.textViewEmail.text = user.email
        holder.textViewAddress.text = user.address
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}
