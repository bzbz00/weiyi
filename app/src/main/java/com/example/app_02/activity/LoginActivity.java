package com.example.app_02.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.example.app_02.method.RainbowView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity"; // 日志标签
    private static final String LOGIN_URL = "http://www.zhangang.top:10000/login"; // 登录请求的URL
    private static final String SUCCESS_RESPONSE = "success: 登录成功"; // 成功响应
    private static final long RAINBOW_GENERATE_INTERVAL = 10000; // 彩虹生成间隔10秒
    private static final long RAINBOW_LIFE_TIME = 5000; // 彩虹持续时间5秒

    private EditText etAccount, etPassword; // 账号和密码输入框
    private ImageView btnLogin, handpoint, btnBack; // 登录按钮、手指图标、返回按钮
    private ImageButton logo; // Logo按钮
    private RequestQueue requestQueue; // Volley请求队列
    private BiometricPrompt biometricPrompt; // 指纹认证
    private BiometricPrompt.PromptInfo promptInfo; // 指纹认证提示信息
    private ObjectAnimator logoAnimator; // Logo动画
    private boolean isAnimating = false; // 动画状态标志

    private Handler rainbowHandler; // 彩虹处理Handler
    private Runnable rainbowRunnable; // 彩虹生成Runnable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // 设置布局

        initializeViews(); // 初始化视图组件
        requestQueue = Volley.newRequestQueue(this); // 创建Volley请求队列

        rainbowHandler = new Handler(); // 初始化Handler
        startRainbowGeneration(); // 开始生成彩虹

        // 注册按钮点击事件，跳转到注册页面
        findViewById(R.id.register).setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));

        // 返回按钮点击事件
        btnBack.setOnClickListener(v -> {
            rotateBackButton(); // 旋转返回按钮
            finish(); // 关闭当前Activity
        });

        // Logo按钮点击事件，切换旋转动画
        logo.setOnClickListener(v -> toggleLogoRotation());

        // 设置登录按钮初始位置
        btnLogin.setX(-200);
        handpoint.setX(170);
        handpoint.setY(20);
        btnLogin.setOnClickListener(v -> handleLogin()); // 登录按钮点击事件

        setupFingerprintAuthentication(); // 设置指纹认证
    }

    private void initializeViews() {
        // 初始化视图组件
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        handpoint = findViewById(R.id.hand_login);
        logo = findViewById(R.id.logo);
        btnBack = findViewById(R.id.btn_back);
    }

    private void startRainbowGeneration() {
        // 开始生成彩虹的Runnable
        rainbowRunnable = new Runnable() {
            @Override
            public void run() {
                RainbowView rainbowView = new RainbowView(LoginActivity.this); // 创建彩虹视图
                ((ViewGroup) findViewById(R.id.main_view)).addView(rainbowView); // 添加彩虹视图

                // 逐渐显示彩虹
                AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
                fadeIn.setDuration(1000); // 显示持续1秒
                rainbowView.startAnimation(fadeIn); // 开始动画

                // 3秒后逐渐隐藏并移除彩虹
                rainbowHandler.postDelayed(() -> {
                    AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
                    fadeOut.setDuration(1000); // 隐藏持续1秒
                    fadeOut.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(android.view.animation.Animation animation) {}

                        @Override
                        public void onAnimationEnd(android.view.animation.Animation animation) {
                            ((ViewGroup) findViewById(R.id.main_view)).removeView(rainbowView); // 移除彩虹视图
                        }

                        @Override
                        public void onAnimationRepeat(android.view.animation.Animation animation) {}
                    });
                    rainbowView.startAnimation(fadeOut); // 开始隐藏动画
                }, RAINBOW_LIFE_TIME); // 延迟5秒

                // 定时生成彩虹
                rainbowHandler.postDelayed(this, RAINBOW_GENERATE_INTERVAL); // 每10秒调用一次
            }
        };
        rainbowHandler.post(rainbowRunnable); // 启动Runnable
    }

    private void handleLogin() {
        // 直接跳转到主界面
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
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
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this,MainActivity.class)); // 跳转到下一个页面
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

        // 长按手指图标进行指纹认证
        handpoint.setOnLongClickListener(v -> {
            biometricPrompt.authenticate(promptInfo); // 开始指纹认证
            return true; // 返回true表示事件已处理
        });
    }

    private void rotateBackButton() {
        // 旋转返回按钮动画
        ObjectAnimator animator = ObjectAnimator.ofFloat(btnBack, "rotation", 0f, 360f); // 创建动画
        animator.setDuration(1000); // 动画持续1秒
        animator.start(); // 启动动画
    }

    private void toggleLogoRotation() {
        // 切换Logo旋转动画
        if (!isAnimating) {
            logoAnimator = ObjectAnimator.ofFloat(logo, "rotation", 0f, 360f); // 创建动画
            logoAnimator.setDuration(1000); // 动画持续1秒
            logoAnimator.setRepeatCount(ObjectAnimator.INFINITE); // 无限重复
            logoAnimator.start(); // 启动动画
            isAnimating = true; // 设置为正在动画状态
        } else {
            logoAnimator.cancel(); // 取消动画
            isAnimating = false; // 设置为非动画状态
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rainbowHandler.removeCallbacks(rainbowRunnable); // 移除彩虹生成的Runnable
    }
}
