package com.yk.color;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.StateSet;

/**
 * Created by Silence on 2018/3/2.
 */

public class DarkColorDrawable extends Drawable {

    Paint paint;
    private static final int[] PRESS_SET = new int[]{android.R.attr.state_pressed};
    private static final int[] SELECTED_SET = new int[]{android.R.attr.state_selected};
    private int r;
    private final int pressColor;
    private boolean press = false;
    private int pressR;
    private int normalColor;
    private float darkAreaRatio = 1.0f;

    /**
     * @param normalColor   正常颜色
     * @param darkRatio     加深度0-1.0
     * @param darkAreaRatio 按下时深颜色占整个Drawable的大小
     */
    public DarkColorDrawable(int normalColor, int darkRatio, float darkAreaRatio) {
        paint = new Paint();
        paint.setColor(normalColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        this.normalColor = normalColor;
        this.darkAreaRatio = darkAreaRatio;
        float[] hsv = new float[3];
        Color.colorToHSV(normalColor, hsv);
        hsv[1] += darkRatio;
        pressColor = Color.HSVToColor(hsv);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        paint.setColor(normalColor);
        canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), r, paint);
        if (press) {
            paint.setColor(pressColor);
            canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), pressR, paint);
        }
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        int size = Math.min(getBounds().width(), getBounds().height());
        r = size / 2;
        pressR = (int) (r * darkAreaRatio);
    }

    /**
     * 支持不同状态
     *
     * @return
     */
    @Override
    public boolean isStateful() {
        return true;
    }

    @Override
    protected boolean onStateChange(int[] state) {
        if (StateSet.stateSetMatches(SELECTED_SET, state) ||
                StateSet.stateSetMatches(PRESS_SET, state)) {
            press = true;
        } else {
            press = false;
        }
        invalidateSelf();
        return true;
    }
}
