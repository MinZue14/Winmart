package com.example.winmart.Adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.winmart.Database.DatabaseProducts
import com.example.winmart.Object.Invoices
import com.example.winmart.R

class InvoiceAdapter(val context: Context, var invoiceList: ArrayList<Invoices>) :
    RecyclerView.Adapter<InvoiceAdapter.InvoiceViewHolder>() {

    inner class InvoiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val invoiceDate: TextView = itemView.findViewById(R.id.invoiceDate)
        val invoiceProducts: TextView = itemView.findViewById(R.id.invoiceProducts)
        val invoiceTotalPrice: TextView = itemView.findViewById(R.id.invoiceTotalPrice)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val invoice = invoiceList[position]
                    showInvoiceDetailsDialog(invoice)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_invoice, parent, false)
        return InvoiceViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        val invoice = invoiceList[position]
        holder.invoiceDate.text = invoice.date
        holder.invoiceProducts.text = invoice.products
        holder.invoiceTotalPrice.text = invoice.totalPrice.toString()
    }

    override fun getItemCount(): Int {
        return invoiceList.size
    }

    private fun showInvoiceDetailsDialog(invoice: Invoices) {
        // Tạo dialog để hiển thị chi tiết hóa đơn
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_detail_invoice, null)
        val productListTextView = dialogView.findViewById<TextView>(R.id.productList)

        // Giả định rằng bạn có một lớp DatabaseProducts để truy vấn cơ sở dữ liệu sản phẩm
        val dbHelper = DatabaseProducts(context)

        // Lấy danh sách sản phẩm dựa trên các barcode trong hóa đơn
        val productBarcodes = invoice.productBarcode.split(", ")
        val productList = dbHelper.getProductsByBarcodes(productBarcodes)

        // Tạo danh sách sản phẩm dưới dạng chuỗi
        val productListString = productList.joinToString(separator = "\n") {
            "${it.productName} - ${it.productBarcode} - ${it.productQuantity} - ${it.productPrice}"
        }
        productListTextView.text = productListString

        AlertDialog.Builder(context)
            .setTitle("Chi tiết hóa đơn")
            .setView(dialogView)
            .setPositiveButton("Đóng") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
