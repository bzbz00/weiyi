package com.example.app_02.method;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class RainbowView extends View {

    private Paint paint;

    public RainbowView(Context context) {
        super(context);
        init();
    }

    public RainbowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 设置颜色数组
        int[] colors = {0x66FF0000, 0x66FFA500, 0x66FFFF00, 0x6600FF00, 0x660000FF, 0x66800080};

        // 弧形半径和位置
        float radius = getWidth() / 2.0f;
        float centerX = getWidth() / 2.0f;
        float centerY = getHeight() * 0.8f; // 控制彩虹的垂直位置
        RectF rectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        // 绘制每一条彩虹弧线
        for (int i = 0; i < colors.length; i++) {
            paint.setColor(colors[i]);
            canvas.drawArc(rectF, 180, 180, false, paint);
            rectF.inset(30, 30); // 控制每层彩虹之间的间距
        }
    }
}
