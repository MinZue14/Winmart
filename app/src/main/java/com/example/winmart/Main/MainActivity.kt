package com.example.winmart

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.winmart.Main.ClaimActivity
import com.example.winmart.Main.EmployeeActivity
import com.example.winmart.Main.InvoiceActivity
import com.example.winmart.Main.LoginActivity
import com.example.winmart.Main.ResearchActivity
import com.example.winmart.databinding.ActivityMainBinding
import com.example.winmart.databinding.HeaderMenuBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//menu
        // Khai báo drawerLayout và navigationView
        val drawer = findViewById<DrawerLayout>(R.id.users)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)

        // Khởi tạo SharedPreferences
        sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE)

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
    }
}