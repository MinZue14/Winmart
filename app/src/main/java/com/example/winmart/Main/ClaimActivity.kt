package com.example.winmart.Main

import android.animation.ValueAnimator
import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.winmart.Adapter.ProductAdapter
import com.example.winmart.Adapter.UserAdapter
import com.example.winmart.Database.DatabaseProducts
import com.example.winmart.Database.DatabaseUsers
import com.example.winmart.MainActivity
import com.example.winmart.Object.Products
import com.example.winmart.R
import com.example.winmart.databinding.ActivityClaimBinding
import com.example.winmart.databinding.ActivityEmployeeBinding
import com.example.winmart.databinding.HeaderMenuBinding
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ClaimActivity : AppCompatActivity() {
    lateinit var binding: ActivityClaimBinding
    lateinit var sharedPref: SharedPreferences
    lateinit var listProduct: ArrayList<Products>
    lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClaimBinding.inflate(layoutInflater)
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
        val dbHelper = DatabaseProducts(this)

        // Truy vấn cơ sở dữ liệu để lấy danh sách sản phẩm
        listProduct = dbHelper.getAllProducts()

        // Khởi tạo adapter và gán adapter cho ListView
        productAdapter = ProductAdapter(this, listProduct)
        binding.productList.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(this@ClaimActivity)
        }
        for (product in listProduct) {
            Log.d(
                "TAG",
                "du lieu trong csdl: ten: ${product.productName}, so luong: ${product.productQuantity}, DVT: ${product.productUnit}" +
                        ", barcode: ${product.productBarcode}, gia tien: ${product.productPrice}, trang thai: ${product.productStatus}" +
                        ", NSX: ${product.productManufacturing}, HSD: ${product.productExpiration}"
            )
        }

        val myCalendar = Calendar.getInstance()
        val datePickerManu = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            PickDateManufact(myCalendar)
        }
        val datePickerExpi = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            PickDateExpi(myCalendar)
        }

        // Thêm sản phẩm vào cơ sở dữ liệu
        binding.btnAdd.setOnClickListener {
            val barcode = binding.edtBarcode.text.toString()
            val name = binding.edtName.text.toString()
            val quantity = binding.edtQuantity.text.toString()
            val unit = binding.edtUnit.text.toString()
            val price = binding.edtPrice.text.toString()
            val status = binding.edtStatus.text.toString()
            val manufactString = binding.edtManufac.text.toString()
            val expirationString = binding.edExpiration.text.toString()

            if (name.isEmpty() || quantity.isEmpty() || unit.isEmpty() || barcode.isEmpty() || price.isEmpty() ||
                status.isEmpty() || manufactString.isEmpty() || expirationString.isEmpty()
            ) {
                showErrorDialog("Vui lòng nhập đầy đủ thông tin!")
                return@setOnClickListener
            }

            val success = dbHelper.insert(name,quantity,unit,barcode,price,status,manufactString,expirationString)
            if (success) {
                showResultDialog("Thêm sản phẩm thành công!")
                listProduct.clear()  // Xóa dữ liệu cũ
                listProduct.addAll(dbHelper.getAllProducts())  // Lấy dữ liệu mới
                productAdapter.notifyDataSetChanged()  // Thông báo adapter cập nhật lại danh sách
            } else {
                showResultDialog("Thêm sản phẩm thất bại!")
            }
        }


        binding.edtManufac.setOnClickListener {
            DatePickerDialog(this, datePickerManu, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.edExpiration.setOnClickListener {
            DatePickerDialog(this, datePickerExpi, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

//SHOW PHẦN NHẬP SẢN PHẨM SAU KHI QUÉT BARCODE
        val drawerClaim = findViewById<DrawerLayout>(R.id.claimOpenDrawer)

        // Xử lý sự kiện khi người dùng nhấn nút mở Drawer
        binding.claimOpen.setOnClickListener {
            val currentHeight = drawerClaim.layoutParams.height
            val targetHeight = if (currentHeight == 0) dpToPx(280) else 0

            val animator = ValueAnimator.ofInt(currentHeight, targetHeight)
            animator.addUpdateListener { animation ->
                val value = animation.animatedValue as Int
                val layoutParams = drawerClaim.layoutParams
                layoutParams.height = value
                drawerClaim.layoutParams = layoutParams
            }
            animator.duration = 300 // Thời gian của animation tính bằng mili giây
            animator.start()
        }
    }

    // Hàm dpToPx được sử dụng để chuyển đổi đơn vị dp thành pixel vì ValueAnimator yêu cầu giá trị pixel.
    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    private fun PickDateManufact(myCalendar: Calendar){
        val myFomart = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFomart, Locale.UK)
        binding.edtManufac.setText(sdf.format(myCalendar.time))
    }
    private fun PickDateExpi(myCalendar: Calendar){
        val myFomart = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFomart, Locale.UK)
        binding.edExpiration.setText(sdf.format(myCalendar.time))
    }

    // Hàm hiển thị dialog kết quả
    private fun showResultDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
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