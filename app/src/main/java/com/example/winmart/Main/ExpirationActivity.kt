package com.example.winmart.Main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.winmart.Adapter.ProductAdapter
import com.example.winmart.Database.DatabaseProducts
import com.example.winmart.MainActivity
import com.example.winmart.R
import com.example.winmart.databinding.ActivityClaimBinding
import com.example.winmart.databinding.ActivityExpirationBinding
import com.example.winmart.databinding.HeaderMenuBinding
import com.google.android.material.navigation.NavigationView

class ExpirationActivity : AppCompatActivity() {
    lateinit var binding: ActivityExpirationBinding
    lateinit var sharedPref: SharedPreferences
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpirationBinding.inflate(layoutInflater)
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
        recyclerView = findViewById(R.id.ListExpiraton)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Lấy danh sách sản phẩm sắp hết hạn (trong 30 ngày tới)
        val dbProducts = DatabaseProducts(this)
        val productList = ArrayList(dbProducts.getProductsExpiringSoon(30))  // Chuyển List thành ArrayList
        productAdapter = ProductAdapter(this, productList)
        recyclerView.adapter = productAdapter


    }
}