package com.example.app_02.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_02.R;
import com.example.app_02.manager.UserManager;
import com.example.app_02.method.RainbowView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity"; // 日志标签
    private static final String LOGIN_URL = "http://www.zhangang.top:10000/login"; // 登录请求的URL
    private static final String SUCCESS_RESPONSE = "success: 登录成功"; // 成功响应

    private EditText etAccount, etPassword; // 账号和密码输入框
    private Button btnLogin; // 登录按钮
    private TextView register; // 注册文本
    private CheckBox checkBox; // 用户协议复选框
    private RequestQueue requestQueue; // Volley请求队列
    private BiometricPrompt biometricPrompt; // 指纹认证
    private BiometricPrompt.PromptInfo promptInfo; // 指纹认证提示信息
    private ObjectAnimator logoAnimator; // Logo动画
    private boolean isAnimating = false; // 动画状态标志
    private ImageButton fingerprint; // 添加指纹按钮变量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // 设置布局

        initializeViews(); // 初始化视图组件
        requestQueue = Volley.newRequestQueue(this); // 创建Volley请求队列

        // 设置登录按钮点击事件
        btnLogin.setOnClickListener(v -> {
            if (!checkBox.isChecked()) {
                Toast.makeText(LoginActivity.this, "请先同意用户协议和隐私政策", Toast.LENGTH_SHORT).show();
                return;
            }
            handleLogin();
        });

        // 设置注册文本点击事件
        register.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        setupFingerprintAuthentication(); // 设置指纹认证
    }

    private void initializeViews() {
        // 初始化视图组件
        etAccount = findViewById(R.id.editTextText); // 新的手机号输入框ID
        etPassword = findViewById(R.id.editTextText5); // 新的密码输入框ID
        btnLogin = findViewById(R.id.button); // 新的登录按钮ID
        register = findViewById(R.id.textViewRegister); // 新的注册文本ID
        checkBox = findViewById(R.id.checkBox); // 新的用户协议复选框ID
        fingerprint = findViewById(R.id.imageButton4); // 绑定指纹按钮
    }

    private void handleLogin() {
        // 获取输入的账号和密码
        String account = etAccount.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // 验证输入是否有效
        if (!isInputValid(account, password)) {
            Toast.makeText(LoginActivity.this, "请输入手机号和密码", Toast.LENGTH_SHORT).show();
            return;
        }

        // 发送登录请求到服务器
        sendLoginRequest(account, password);
    }

    private boolean isInputValid(String account, String password) {
        // 校验账号和密码是否有效
        return !account.isEmpty() && !password.isEmpty(); // 非空校验
    }

    private void sendLoginRequest(String account, String password) {
        // 发送登录请求
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                this::handleResponse, this::handleError) {
            @Override
            protected Map<String, String> getParams() {
                // 设置请求参数
                Map<String, String> params = new HashMap<>();
                params.put("account", account); // 账号
                params.put("password", password); // 密码
                return params; // 返回参数
            }
        };

        requestQueue.add(stringRequest); // 添加请求到队列
    }

    private void handleResponse(String response) {
        // 处理服务器响应
        Log.d(TAG, "Response: " + response);
        if (response.equals(SUCCESS_RESPONSE)) {
            // 登录成功
            UserManager.getInstance().login(etAccount.getText().toString());
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            finish(); // 关闭当前页面
        } else {
            // 登录失败
            Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleError(VolleyError error) {
        // 处理请求错误
        Log.e(TAG, "Network request failed", error);
        Toast.makeText(LoginActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
    }

    private void setupFingerprintAuthentication() {
        // 设置指纹认证
        Executor executor = ContextCompat.getMainExecutor(this); // 获取主线程执行器
        biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "指纹认证失败: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "指纹认证成功", Toast.LENGTH_SHORT).show();
                handleLogin(); // 成功后调用登录
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "指纹认证未通过", Toast.LENGTH_SHORT).show();
            }
        });

        // 设置指纹认证提示信息
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("指纹认证") // 标题
                .setSubtitle("使用指纹登录") // 副标题
                .setNegativeButtonText("取消") // 取消按钮文本
                .build();

        // 设置指纹按钮点击事件
        fingerprint.setOnClickListener(v -> {
            biometricPrompt.authenticate(promptInfo);
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
