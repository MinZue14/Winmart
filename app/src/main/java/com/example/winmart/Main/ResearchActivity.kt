package com.example.winmart.Main

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.winmart.Database.DatabaseProducts
import com.example.winmart.MainActivity
import com.example.winmart.Object.Products
import com.example.winmart.R
import com.example.winmart.databinding.ActivityResearchBinding
import com.example.winmart.databinding.HeaderMenuBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import com.google.zxing.integration.android.IntentIntegrator

class ResearchActivity : AppCompatActivity() {
    lateinit var binding: ActivityResearchBinding
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResearchBinding.inflate(layoutInflater)
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

                R.id.navHSD -> {
                    val intent = Intent(this, ExpirationActivity::class.java)
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
        binding.ScannerOpen.setOnClickListener {
            val scanner = IntentIntegrator(this)
            scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            scanner.setBeepEnabled(false)
            scanner.initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(this, "Đã hủy", Toast.LENGTH_LONG).show()
                } else {
                    val barcode = result.contents
                    val db = DatabaseProducts(this)
                    val product = db.getProductByBarcode(barcode)

                    if (product != null) {
                        // Nếu sản phẩm có trong cơ sở dữ liệu, hiển thị dialog
                        showProductDialog(product)
                    } else {
                        // Nếu không có sản phẩm, hiển thị thông báo
                        Toast.makeText(this, "Không có sản phẩm trong kho", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun showProductDialog(product: Products) {
        val dialogView = layoutInflater.inflate(R.layout.table_product, null)
        val dialog = BottomSheetDialog(this)

        dialog.setContentView(dialogView)


        // Thiết lập dữ liệu cho các trường trong table_product.xml
        dialogView.findViewById<TextView>(R.id.TextViewBarcode).text = product.productBarcode
        dialogView.findViewById<TextView>(R.id.TextViewName).text = product.productName
        dialogView.findViewById<TextView>(R.id.TextViewQuantity).text = product.productQuantity.toString()
        dialogView.findViewById<TextView>(R.id.TextViewPrice).text = product.productPrice.toString()
        dialogView.findViewById<TextView>(R.id.TextViewUnit).text = product.productUnit
        dialogView.findViewById<TextView>(R.id.TextViewStatus).text = product.productStatus
        dialogView.findViewById<TextView>(R.id.TextViewManufact).text = product.productManufacturing.toString()
        dialogView.findViewById<TextView>(R.id.TextViewExpiration).text = product.productExpiration.toString()

        val layoutParams = dialog.window?.attributes
        layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams?.height = WindowManager.LayoutParams.WRAP_CONTENT // Hoặc tùy chỉnh chiều cao nếu cần
        dialog.window?.attributes = layoutParams

        // Hiển thị dialog
        dialog.show()
    }
}