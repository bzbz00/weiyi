package com.example.app_02.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_02.R;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    // 定义控件
    private EditText etAccount, etPassword, etConfirmPassword; // 定义账号、密码和确认密码输入框
    private Button btnRegister; // 定义注册按钮
    private ImageButton img; // 定义图片按钮（用于logo）

    @SuppressLint("MissingInflatedId") // 忽略缺失的 ID 警告
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // 设置活动对应的布局文件为 activity_register.xml

        // 初始化控件，绑定布局中的各个控件
        etAccount = findViewById(R.id.et_account); // 获取账号输入框
        etPassword = findViewById(R.id.et_password); // 获取密码输入框
        etConfirmPassword = findViewById(R.id.et_confirm_password); // 获取确认密码输入框
        btnRegister = findViewById(R.id.btn_register); // 获取注册按钮
        img = findViewById(R.id.registerlogo); // 获取图片按钮（logo）
        ImageView btnBack = findViewById(R.id.btn_back);
        // 设置返回按钮的点击事件
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 关闭当前Activity，返回到上一个界面
            }
        });

        // 设置注册按钮点击事件
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取用户输入的账号、密码和确认密码
                String account = etAccount.getText().toString().trim(); // 获取并去除两端空格的账号
                String password = etPassword.getText().toString().trim(); // 获取并去除两端空格的密码
                String confirmPassword = etConfirmPassword.getText().toString().trim(); // 获取并去除两端空格的确认密码

                // 验证用户输入的有效性
                if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this, "请输入账号或密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) { // 如果密码和确认密码不一致，提示用户密码不匹配
                    Toast.makeText(RegisterActivity.this, "密码不匹配", Toast.LENGTH_SHORT).show();
                    return; // 返回，终止后续代码执行
                }

                // 调用 registerUser 方法，将账号和密码发送到服务器进行注册
                registerUser(account, password);
            }
        });
    }

    // 使用 Volley 库发送账号和密码到服务器
    private void registerUser(final String account, final String password) {
        String url = "http://www.zhangang.top:10000/register"; // 服务器地址，替换为实际服务器的 URL

        // 创建请求队列，用于管理所有的网络请求
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // 创建一个 POST 请求，向服务器发送注册信息
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // 处理服务器返回的响应
                if (response.contains("fail: 用户已存在")) { // 如果返回的响应包含 "用户已存在"
                    Toast.makeText(RegisterActivity.this, "该用户已存在，请使用其他账号", Toast.LENGTH_SHORT).show();
                } else if (response.contains("success: 用户注册成功")) { // 如果返回的响应包含 "注册成功"
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                } else {
                    // 如果返回的响应不是预期的消息，提示注册失败
                    Toast.makeText(RegisterActivity.this, "注册失败，请稍后再试", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() { // 错误监听器，处理网络请求中的错误
            @Override
            public void onErrorResponse(VolleyError error) {
                // 如果网络请求失败，显示网络错误提示
                Toast.makeText(RegisterActivity.this, "网络请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // 定义发送给服务器的参数（账号和密码）
                Map<String, String> params = new HashMap<>();
                params.put("account", account); // 将账号放入参数
                params.put("password", password); // 将密码放入参数
                return params; // 返回参数
            }
        };

        // 将请求添加到请求队列中，Volley 会自动执行请求
        requestQueue.add(stringRequest);
    }
}
