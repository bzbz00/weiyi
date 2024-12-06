package service;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class ChatGPTService {
    private static final String API_URL = "https://api.chatanywhere.tech/v1/chat/completions";
    private static final String API_KEY = "sk-wgeT8ePrVNBtB70mar7VcS2TccKQYnCsc09BKv4Qd2nt4Pxb";

    // 可用模型列表
    public static final String[] AVAILABLE_MODELS = {
        "gpt-3.5-turbo",
        "gpt-4",
        "gpt-4-32k"
    };

    private String currentModel = AVAILABLE_MODELS[0];
    private final OkHttpClient client = new OkHttpClient();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface ChatCallback {
        void onResponse(String response);
        void onError(String error);
    }

    public void setModel(String model) {
        this.currentModel = model;
    }

    public String getCurrentModel() {
        return currentModel;
    }

    public void sendMessage(String message, ChatCallback callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("model", currentModel);
            jsonBody.put("temperature", 0.7);
            jsonBody.put("max_tokens", 2000);
            
            JSONArray messages = new JSONArray();
            JSONObject messageObj = new JSONObject();
            messageObj.put("role", "user");
            messageObj.put("content", message);
            messages.put(messageObj);
            
            jsonBody.put("messages", messages);

            Log.d("ChatGPT", "Preparing request: " + jsonBody.toString());

            RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonBody.toString()
            );

            Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("ChatGPT", "Request failed", e);
                    mainHandler.post(() -> callback.onError("网络请求失败: " + e.getMessage()));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String responseBody = response.body().string();
                        Log.d("ChatGPT", "Response: " + responseBody);

                        if (!response.isSuccessful()) {
                            mainHandler.post(() -> callback.onError("API请求失败: " + response.code()));
                            return;
                        }

                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONArray choices = jsonResponse.getJSONArray("choices");
                        JSONObject choice = choices.getJSONObject(0);
                        JSONObject message = choice.getJSONObject("message");
                        String content = message.getString("content");
                        
                        mainHandler.post(() -> callback.onResponse(content.trim()));
                    } catch (Exception e) {
                        Log.e("ChatGPT", "Parse response failed", e);
                        mainHandler.post(() -> callback.onError("解析响应失败: " + e.getMessage()));
                    }
                }
            });
        } catch (Exception e) {
            Log.e("ChatGPT", "Build request failed", e);
            callback.onError("请求构建失败: " + e.getMessage());
        }
    }
} 