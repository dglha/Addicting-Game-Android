package com.dlha.addictinggame.ui.activities


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dlha.addictinggame.R
import com.dlha.addictinggame.api.ApiClient
import com.dlha.addictinggame.api.AuthService
import com.dlha.addictinggame.model.Message
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        changeStatusBarColor()
    }
    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.register_bk_color)
        }
    }
    public fun onLoginClickView(view : View) {
        startActivity(Intent(this,LoginActivity::class.java))
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
    }
    public fun onRegisterClick(view : View) {
        val username : String = findViewById<EditText>(R.id.editTextUsername).text.toString()
        val password : String = findViewById<EditText>(R.id.editTextPassword).text.toString()
        val name  : String = findViewById<EditText>(R.id.editTextName).text.toString()
        if(name.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()) {
            register(name,username,password)
        } else {
            Toast.makeText(this,"Nhập đủ 3 trường",Toast.LENGTH_SHORT).show()
        }
    }
    private fun register(name : String,username : String,password : String) {
        val registerService = ApiClient.getRetrofit().create(AuthService::class.java)

        val call : Call<Message> = registerService.userRegister(name,username,password)

        call.enqueue(object : Callback<Message> {
            override fun onFailure(call: Call<Message>, t: Throwable) {
                Toast.makeText(this@RegisterActivity,"Xem lại kết nối mạng",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Message>, response: Response<Message>) {
                val message : Message?= response?.body()
                if (message?.code == 201) {
                    Toast.makeText(this@RegisterActivity,message.message,Toast.LENGTH_SHORT).show()
                    val intent : Intent = Intent(this@RegisterActivity,LoginActivity::class.java)
                    intent.putExtra("username", "$username")
                    Thread.sleep(1000)
                    startActivity(intent)
                } else if(message?.code==200) {
                    Toast.makeText(this@RegisterActivity,"Tên tài khoản đã tồn tại",Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this@RegisterActivity,"Error",Toast.LENGTH_SHORT).show()
                }
            }
        })

    }
}