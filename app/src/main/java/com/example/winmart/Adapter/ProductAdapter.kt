package com.example.winmart.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.winmart.Object.Products
import com.example.winmart.R

class ProductAdapter (val context: Context, var productList: ArrayList<Products>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.TextViewName)
        val textViewQuantity: TextView = itemView.findViewById(R.id.TextViewQuantity)
        val textViewUnit: TextView = itemView.findViewById(R.id.TextViewUnit)
        val textViewStatus: TextView = itemView.findViewById(R.id.TextViewStatus)
        val textViewPrice: TextView = itemView.findViewById(R.id.TextViewPrice)
        val textViewBarcode: TextView = itemView.findViewById(R.id.TextViewBarcode)
        val textViewManufact: TextView = itemView.findViewById(R.id.TextViewManufact)
        val textViewExpiration: TextView = itemView.findViewById(R.id.TextViewExpiration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.table_product, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductAdapter.ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.textViewName.text = product.productName
        holder.textViewQuantity.text = product.productQuantity
        holder.textViewUnit.text = product.productUnit
        holder.textViewBarcode.text = product.productBarcode
        holder.textViewStatus.text = product.productStatus
        holder.textViewPrice.text = product.productPrice
        holder.textViewManufact.text = product.productManufacturing.toString()
        holder.textViewExpiration.text = product.productExpiration.toString()
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}
