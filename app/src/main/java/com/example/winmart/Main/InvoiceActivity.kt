package com.example.winmart.Main

import android.animation.ValueAnimator
import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.winmart.Adapter.InvoiceAdapter
import com.example.winmart.Adapter.ProductAdapter
import com.example.winmart.Database.DatabaseInvoices
import com.example.winmart.Database.DatabaseProducts
import com.example.winmart.MainActivity
import com.example.winmart.Object.Invoices
import com.example.winmart.Object.Products
import com.example.winmart.R
import com.example.winmart.databinding.ActivityClaimBinding
import com.example.winmart.databinding.ActivityInvoiceBinding
import com.example.winmart.databinding.HeaderMenuBinding
import com.google.android.material.navigation.NavigationView
import com.google.zxing.integration.android.IntentIntegrator
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class InvoiceActivity : AppCompatActivity() {
    lateinit var binding: ActivityInvoiceBinding
    lateinit var sharedPref: SharedPreferences
    private lateinit var dbHelper: DatabaseProducts
    private lateinit var dbInvoices: DatabaseInvoices
    private lateinit var listProduct: ArrayList<Products>
    private lateinit var productAdapter: ProductAdapter
    private lateinit var invoiceAdapter: InvoiceAdapter
    private var scannedProducts: ArrayList<Products> = ArrayList() // Danh sách các sản phẩm đã quét
    private var invoiceList: ArrayList<Invoices> = ArrayList()

    private var totalQuantity = 0
    private var totalPrice = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvoiceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//menu
        // Khai báo drawerLayout và navigationView
        val drawer = findViewById<DrawerLayout>(R.id.users)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)

        // Khởi tạo SharedPreferences
        sharedPref = getSharedPreferences("user_data", MODE_PRIVATE)

        // Lấy thông tin người dùng từ SharedPreferences
        val username = sharedPref.getString("username", "") ?: ""
        val email = sharedPref.getString("email", "") ?: ""
        val address = sharedPref.getString("address", "") ?: ""

        // Gán headerLayoutBinding cho navigationView menu
        val headerLayoutBinding = HeaderMenuBinding.bind(navigationView.getHeaderView(0))
        headerLayoutBinding.menuUsername.text = username
        headerLayoutBinding.menuUsermail.text = email
        headerLayoutBinding.menuUserAddress.text = address

        // Thiết lập listener cho các mục menu trong navigationView
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navTraCuu -> {
                    val intent = Intent(this, ResearchActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.navNhanVien -> {
                    val intent = Intent(this, EmployeeActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.navNhapHang -> {
                    val intent = Intent(this, ClaimActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.navHoaDon -> {
                    val intent = Intent(this, InvoiceActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.navHuyHang -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.navHSD -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.navLogOut -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    true
                }


                else -> false
            }
        }

        // Xử lý sự kiện khi người dùng nhấn nút mở Drawer
        binding.btnOpenDrawer.setOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }

//main
        dbHelper = DatabaseProducts(this)
        dbInvoices = DatabaseInvoices(this)

        // Truy vấn cơ sở dữ liệu để lấy danh sách sản phẩm
        listProduct = dbHelper.getAllProducts()

        // Khởi tạo adapter và gán adapter cho ListView
        binding.productList.apply {
            layoutManager = LinearLayoutManager(this@InvoiceActivity)
            invoiceAdapter = InvoiceAdapter(this@InvoiceActivity, invoiceList)
            adapter = invoiceAdapter
        }
        for (product in listProduct) {
            Log.d(
                "TAG",
                "du lieu trong csdl: ten: ${product.productName}, so luong: ${product.productQuantity}, DVT: ${product.productUnit}" +
                        ", barcode: ${product.productBarcode}, gia tien: ${product.productPrice}, trang thai: ${product.productStatus}" +
                        ", NSX: ${product.productManufacturing}, HSD: ${product.productExpiration}"
            )
        }

        // Mở trình quét barcode khi nhấn vào nút ScanOpen
        binding.ScanOpen.setOnClickListener {
            startBarcodeScanner()
        }
    }

    // Hàm khởi động trình quét barcode
    private fun startBarcodeScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        integrator.setPrompt("Scan a barcode")
        integrator.setCameraId(0)  // Sử dụng camera sau
        integrator.setBeepEnabled(true)
        integrator.setBarcodeImageEnabled(true)
        integrator.setOrientationLocked(false)
        integrator.initiateScan()
    }

    // Xử lý kết quả quét barcode
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                // Người dùng đã hủy quét
                showErrorDialog("Quét bị hủy")
            } else {
                try {
                    // Lấy nội dung barcode
                    val barcode = result.contents
                    val products = dbHelper.getProductsByBarcodes(listOf(barcode))
                    if (products.isNotEmpty()) {
                        // Hiển thị thông tin sản phẩm và nhập số lượng
                        showProductDialog(products)
                    } else {
                        // Thông báo rằng sản phẩm không tồn tại
                        showErrorDialog("Sản phẩm không có trong kho")
                    }
                } catch (e: Exception) {
                    // Xử lý ngoại lệ khi truy vấn hoặc hiển thị thông tin
                    showErrorDialog("Đã xảy ra lỗi: ${e.message}")
                    e.printStackTrace()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    // Hàm hiển thị dialog nhập số lượng và thông tin sản phẩm
    private fun showProductDialog(products: List<Products>) {
        // Inflate layout của dialog
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_invoice, null)
        val quantityEditText = dialogView.findViewById<EditText>(R.id.quantity)
        val productNameTextView = dialogView.findViewById<TextView>(R.id.name)
        val productBarcodeTextView = dialogView.findViewById<TextView>(R.id.barcode)
        val productPriceTextView = dialogView.findViewById<TextView>(R.id.price)
        val totalQuantityTextView = dialogView.findViewById<TextView>(R.id.totalQuantity)
        val totalPriceTextView = dialogView.findViewById<TextView>(R.id.totalPrice)
        val productListTextView = dialogView.findViewById<TextView>(R.id.productList)

        // Hiển thị danh sách sản phẩm
        val productDetails = products.joinToString(separator = "\n") { product ->
            "${product.productName} - ${product.productBarcode} - ${product.productPrice}"
        }
        productListTextView.text = productDetails

        // Hiển thị thông tin sản phẩm đầu tiên (nếu cần)
        if (products.isNotEmpty()) {
            val firstProduct = products[0]
            productNameTextView.text = firstProduct.productName
            productBarcodeTextView.text = firstProduct.productBarcode
            productPriceTextView.text = firstProduct.productPrice.toString()
        }

        // Cập nhật thông tin tổng cộng
        updateSummary(totalQuantityTextView, totalPriceTextView, productListTextView)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Thông tin sản phẩm")
        builder.setView(dialogView)
        builder.setPositiveButton("Thêm") { dialog, _ ->
            val quantityStr = quantityEditText.text.toString()
            if (quantityStr.isNotEmpty()) {
                val quantity = quantityStr.toInt()
                // Giả định rằng bạn thêm số lượng cho sản phẩm đầu tiên trong danh sách
                val selectedProduct = products[0]
                selectedProduct.productQuantity = quantity.toString()
                scannedProducts.add(selectedProduct)

                // Cập nhật tổng số lượng và tổng giá tiền
                updateSummary(totalQuantityTextView, totalPriceTextView, productListTextView)
            }
            dialog.dismiss()
            startBarcodeScanner() // Mở lại trình quét sau khi thêm sản phẩm
        }
        builder.setNegativeButton("Hủy") { dialog, _ ->
            dialog.dismiss()
        }
        builder.setNeutralButton("Xuất hóa đơn") { dialog, _ ->
            // Logic xuất hóa đơn
            exportInvoice()
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun updateSummary(totalQuantityTextView: TextView, totalPriceTextView: TextView, productListTextView: TextView) {
        var totalQuantity = 0
        var totalPrice = 0.0

        // Cập nhật danh sách sản phẩm trong dialog
        val productList = StringBuilder()
        for (product in scannedProducts) {
            val quantity = product.productQuantity.toIntOrNull() ?: 0
            val price = product.productPrice.toDoubleOrNull() ?: 0.0
            totalQuantity += quantity
            totalPrice += price * quantity
            productList.append("${product.productName} - ${product.productBarcode} - $quantity - $price\n")
        }

        // Hiển thị tổng số lượng và tổng giá tiền
        totalQuantityTextView.text = "Tổng số lượng: $totalQuantity"
        totalPriceTextView.text = "Tổng giá tiền: $totalPrice"
        productListTextView.text = productList.toString()
    }


    private fun exportInvoice() {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
        val products = scannedProducts.joinToString(separator = ", ") { "${it.productName} - ${it.productBarcode}" }
        val productBarcodes = scannedProducts.joinToString(separator = ", ") { it.productBarcode }
        val quantity = totalQuantity
        val totalPrice = this.totalPrice

        // Giả sử phương thức addInvoice đã tự động tạo ID hóa đơn
        val result = dbInvoices.addInvoice(currentDate, products, productBarcodes, quantity, totalPrice)

        if (result != -1L) {
            Toast.makeText(this, "Hóa đơn đã được thêm thành công!", Toast.LENGTH_SHORT).show()
            updateInvoiceListByDate(currentDate)
        } else {
            Toast.makeText(this, "Thêm hóa đơn thất bại!", Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateInvoiceListByDate(invoiceDate: String) {
        // Lấy danh sách hóa đơn từ cơ sở dữ liệu theo ngày
        val invoices = dbInvoices.getInvoicesByDate(invoiceDate)

        // Chuyển đổi từ List<Invoices> thành ArrayList<Invoices>
        val arrayListInvoices = ArrayList(invoices)

        // Thiết lập LayoutManager và Adapter cho RecyclerView
        binding.productList.apply {
            layoutManager = LinearLayoutManager(this@InvoiceActivity)
            adapter = InvoiceAdapter(this@InvoiceActivity, arrayListInvoices)
        }
    }

    // Hàm hiển thị dialog thông báo lỗi
    private fun showErrorDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }
}
