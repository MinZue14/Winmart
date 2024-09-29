package com.example.winmart.Main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.winmart.Adapter.UserAdapter
import com.example.winmart.Database.DatabaseUsers
import com.example.winmart.Database.Users
import com.example.winmart.MainActivity
import com.example.winmart.R
import com.example.winmart.databinding.ActivityEmployeeBinding
import com.example.winmart.databinding.HeaderMenuBinding
import com.google.android.material.navigation.NavigationView
import java.util.Locale

class EmployeeActivity : AppCompatActivity() {
    lateinit var binding: ActivityEmployeeBinding
    lateinit var sharedPref: SharedPreferences
    lateinit var listUser: ArrayList<Users>
    lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeBinding.inflate(layoutInflater)
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
        val dbHelper = DatabaseUsers(this)
        // Truy vấn cơ sở dữ liệu để lấy danh sách người dùng
        listUser = dbHelper.getAllUsers()

        // Khởi tạo adapter và gán adapter cho ListView
        userAdapter = UserAdapter(this, listUser)
        binding.userList.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this@EmployeeActivity)
        }
        for (user in listUser) {
            Log.d("TAG", "User: ${user.username}, Email: ${user.email}, Address: ${user.address}")
        }

        // Xử lý sự kiện tìm kiếm người dùng
        binding.searchUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchUser.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchEmployee = newText?.toLowerCase(Locale.getDefault()) ?: ""
                filterUsers(searchEmployee)
                return true
            }
        })
    }

    private fun filterUsers(query: String) {
        val filteredList = if (query.isEmpty()) {
            listUser
        } else {
            listUser.filter {
                it.username.toLowerCase(Locale.getDefault()).contains(query) ||
                        it.email.toLowerCase(Locale.getDefault()).contains(query) ||
                        it.address.toLowerCase(Locale.getDefault()).contains(query)
            }
        }

        // Log the filtered list
        for (user in filteredList) {
            Log.d("TAG", "Lọc dữ liệu người dùng: ${user.username}, Email: ${user.email}, Address: ${user.address}")
        }

        userAdapter.apply {
            userList = ArrayList(filteredList)  // Update adapter's data
            notifyDataSetChanged()  // Notify adapter of data changes
        }
    }
}
