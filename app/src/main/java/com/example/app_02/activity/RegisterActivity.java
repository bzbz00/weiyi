package com.example.app_02.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_02.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    // 定义控件
    private EditText etAccount, etPassword, etConfirmPassword; // 定义账号、密码和确认密码输入框
    private Button btnRegister, btnGetCode; // 定义注册按钮和获取验证码按钮
    private EditText etNickname, etPhone, etVerificationCode; // 定义昵称、手机号和验证码输入框
    private CheckBox checkBoxAgreement; // 定义用户协议复选框
    private ImageButton buttonBack; // 定义返回按钮
    private String generatedCode; // 存储生成的验证码

    @SuppressLint("MissingInflatedId") // 忽略缺失的 ID 警告
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // 设置活动对应的布局文件为 activity_register.xml

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        // 绑定新布局中的视图
        etNickname = findViewById(R.id.editTextNickname);
        etPhone = findViewById(R.id.editTextPhone);
        etVerificationCode = findViewById(R.id.editTextVerificationCode);
        etPassword = findViewById(R.id.editTextPassword);
        etConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        btnRegister = findViewById(R.id.buttonRegister);
        btnGetCode = findViewById(R.id.buttonGetCode);
        checkBoxAgreement = findViewById(R.id.checkBoxAgreement);
        buttonBack = findViewById(R.id.buttonBack);
    }

    private void setupListeners() {
        // 返回按钮点击事件
        buttonBack.setOnClickListener(v -> finish());

        // 获取验证码按钮点击事件
        btnGetCode.setOnClickListener(v -> {
            String phone = etPhone.getText().toString().trim();
            if (phone.isEmpty()) {
                Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            if (phone.length() != 11) {
                Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            generateAndShowVerificationCode();
        });

        btnRegister.setOnClickListener(v -> {
            if (!checkBoxAgreement.isChecked()) {
                Toast.makeText(this, "请先同意用户协议和隐私政策", Toast.LENGTH_SHORT).show();
                return;
            }

            String nickname = etNickname.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String verificationCode = etVerificationCode.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (TextUtils.isEmpty(nickname) || TextUtils.isEmpty(phone) || 
                TextUtils.isEmpty(verificationCode) || TextUtils.isEmpty(password)) {
                Toast.makeText(RegisterActivity.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                return;
            }

            // 验证验证码
            if (!verificationCode.equals(generatedCode)) {
                Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "密码不匹配", Toast.LENGTH_SHORT).show();
                return;
            }

            registerUser(phone, password);
        });
    }

    // 生成并显示验证码
    private void generateAndShowVerificationCode() {
        // 生成4位随机数字
        Random random = new Random();
        generatedCode = String.format("%04d", random.nextInt(10000));

        // 创建并显示对话框
        new AlertDialog.Builder(this)
            .setTitle("验证码")
            .setMessage("您的验证码是：" + generatedCode)
            .setPositiveButton("确定", (dialog, which) -> {
                // 启动倒计时
                startCountDown();
            })
            .setCancelable(false)
            .show();
    }

    // 倒计时功能
    private void startCountDown() {
        btnGetCode.setEnabled(false);
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnGetCode.setText(millisUntilFinished / 1000 + "秒后重试");
            }

            @Override
            public void onFinish() {
                btnGetCode.setEnabled(true);
                btnGetCode.setText("获取验证码");
            }
        }.start();
    }

    // 使用 Volley 库发送账号和密码到服务器
    private void registerUser(final String account, final String password) {
        String url = "http://47.120.48.211:10000/register";

        Log.d("RegisterActivity", "Attempting to connect to: " + url);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("RegisterActivity", "Response: " + response);
                    if (response.contains("fail: 用户已存在")) {
                        Toast.makeText(RegisterActivity.this, "该用户已存在，请使用其他账号", Toast.LENGTH_SHORT).show();
                    } else if (response.contains("success: 用户注册成功")) {
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(() -> {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }, 1000);
                    } else {
                        Toast.makeText(RegisterActivity.this, "注册失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("RegisterActivity", "Error: " + error.toString(), error);
                    if (error.networkResponse != null) {
                        Log.e("RegisterActivity", "Status Code: " + error.networkResponse.statusCode);
                        Log.e("RegisterActivity", "Data: " + new String(error.networkResponse.data));
                    }
                    String errorMessage;
                    if (error.networkResponse != null) {
                        errorMessage = "服务器错误: " + error.networkResponse.statusCode;
                    } else if (error.toString().contains("NoConnectionError")) {
                        errorMessage = "无法连接到服务器，请检查网络";
                    } else {
                        errorMessage = "网络请求失败: " + error.getMessage();
                    }
                    Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("account", account);
                params.put("password", password);
                Log.d("RegisterActivity", "Params: " + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Accept", "*/*");
                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
            30000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }
}
