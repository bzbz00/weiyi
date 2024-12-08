package service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.app.AlertDialog;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.util.Log;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.app_02.R;

import java.util.List;

public class FloatBallService extends Service {

    private WindowManager windowManager;
    private View floatBallView;
    private StringBuilder chatHistory = new StringBuilder();
    private ChatGPTService chatGPTService;
    private static final String TAG = "FloatBallService";
    private static final String APP_PACKAGE_NAME = "com.example.app_02";
    private boolean isFloatBallVisible = false;
    private Handler handler;
    private Runnable checkAppStateRunnable;

    @Override
    public void onCreate() {
        super.onCreate();
        setupFloatingBall();
        chatGPTService = new ChatGPTService();
        handler = new Handler();
        startAppStateCheck();
    }

    private void startAppStateCheck() {
        checkAppStateRunnable = new Runnable() {
            @Override
            public void run() {
                boolean isAppInForeground = isAppInForeground();
                updateFloatBallVisibility(isAppInForeground);
                handler.postDelayed(this, 1000); // 每秒检查一次
            }
        };
        handler.post(checkAppStateRunnable);
    }

    private boolean isAppInForeground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && appProcess.processName.equals(APP_PACKAGE_NAME)) {
                return true;
            }
        }
        return false;
    }

    private void updateFloatBallVisibility(boolean shouldBeVisible) {
        if (floatBallView != null && windowManager != null) {
            if (shouldBeVisible && !isFloatBallVisible) {
                try {
                    windowManager.addView(floatBallView, getLayoutParams());
                    isFloatBallVisible = true;
                } catch (Exception e) {
                    Log.e(TAG, "Error showing float ball", e);
                }
            } else if (!shouldBeVisible && isFloatBallVisible) {
                try {
                    windowManager.removeView(floatBallView);
                    isFloatBallVisible = false;
                } catch (Exception e) {
                    Log.e(TAG, "Error hiding float ball", e);
                }
            }
        }
    }

    private WindowManager.LayoutParams getLayoutParams() {
        int layoutFlag;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutFlag = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutFlag = WindowManager.LayoutParams.TYPE_PHONE;
        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = getScreenWidth() - 150;
        params.y = getScreenHeight() - 800;

        return params;
    }

    private int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    private int getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private void setupFloatingBall() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        floatBallView = LayoutInflater.from(this).inflate(R.layout.float_ball, null);
        updateFloatBallVisibility(true);

        floatBallView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            private boolean isMoving = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isMoving = false;
                        initialX = getLayoutParams().x;
                        initialY = getLayoutParams().y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        int deltaX = (int) (event.getRawX() - initialTouchX);
                        int deltaY = (int) (event.getRawY() - initialTouchY);

                        if (Math.abs(deltaX) > 5 || Math.abs(deltaY) > 5) {
                            isMoving = true;
                            getLayoutParams().x = initialX + deltaX;
                            getLayoutParams().y = initialY + deltaY;
                            windowManager.updateViewLayout(floatBallView, getLayoutParams());
                        }
                        return true;

                    case MotionEvent.ACTION_UP:
                        if (!isMoving) {
                            showAiDialog();
                        }
                        return true;
                }
                return false;
            }
        });
    }

    private void showAiDialog() {
        try {
            Log.d(TAG, "开始显示对话框");
            
            Context dialogContext = new ContextThemeWrapper(getApplicationContext(), R.style.Theme_App_02);
            
            View dialogView = LayoutInflater.from(dialogContext).inflate(R.layout.dialog_ai_chat, null);

            TextView chatHistory = dialogView.findViewById(R.id.chat_history);
            EditText inputMessage = dialogView.findViewById(R.id.input_message);
            Button sendButton = dialogView.findViewById(R.id.send_button);
            Spinner modelSpinner = dialogView.findViewById(R.id.model_spinner);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                dialogContext,
                android.R.layout.simple_spinner_item,
                ChatGPTService.AVAILABLE_MODELS
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            modelSpinner.setAdapter(adapter);

            modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedModel = ChatGPTService.AVAILABLE_MODELS[position];
                    chatGPTService.setModel(selectedModel);
                    
                    String modelInfo;
                    if (selectedModel.equals("gpt-3.5-turbo")) {
                        modelInfo = "当前使用: GPT-3.5\n适合日常对话，响应速度快";
                    } else if (selectedModel.equals("gpt-4")) {
                        modelInfo = "当前使用: GPT-4\n⚠️ 注意：高级模型，每次对话消耗更多额度\n优点：更强的理解能力和创造力";
                    } else {
                        modelInfo = "当前使用: GPT-4-32K\n⚠️ 注意：高级模型，每次对话消耗更多额度\n优点：支持超长对话，记忆力更强";
                    }
                    
                    chatHistory.append("系统: " + modelInfo + "\n------------------------\n");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            sendButton.setOnClickListener(v -> {
                String message = inputMessage.getText().toString().trim();
                if (!message.isEmpty()) {
                    chatHistory.append("用户: " + message + "\nAI: 正在思考中...\n");
                    inputMessage.setText("");
                    
                    chatGPTService.sendMessage(message, new ChatGPTService.ChatCallback() {
                        @Override
                        public void onResponse(String response) {
                            String currentText = chatHistory.getText().toString();
                            chatHistory.setText(currentText.replace("正在思考中...", response));
                        }

                        @Override
                        public void onError(String error) {
                            String currentText = chatHistory.getText().toString();
                            chatHistory.setText(currentText.replace("正在思考中...", 
                                "抱歉，发生错误：" + error + "\n请检查网络连接或稍后重试"));
                        }
                    });
                }
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext, R.style.DialogTheme);
            builder.setView(dialogView);
            
            final AlertDialog dialog = builder.create();
            if (dialog.getWindow() != null) {
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                dialog.getWindow().setAttributes(params);
            }

            dialog.show();
            Log.d(TAG, "对话框显示成功");

        } catch (Exception e) {
            Log.e(TAG, "showAiDialog 发生错误", e);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(checkAppStateRunnable);
        if (floatBallView != null && isFloatBallVisible) {
            windowManager.removeView(floatBallView);
            isFloatBallVisible = false;
        }
    }
}
