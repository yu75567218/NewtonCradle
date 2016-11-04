package com.example.newtoncradle.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.newtoncradle.R;

/**
 * 描述：牛顿摆
 * 作者: hy
 * 日期：2016/11/1
 */

public class NewtonCradle extends View {

    private static final int LOCATION_LEFT   = -1; // 左边
    private static final int LOCATION_MIDDLE = 0; // 中间
    private static final int LOCATION_RIGHT  = 1; //右边

    private Paint linePaint; // 画线
    private Paint ballPaint; // 画圆
    private int lineLength = 100; // 线长
    // 自定义属性
    private int lineColor; // 线颜色
    private int ballColor; // 球颜色
    private float lineWidth; // 线宽
    private float radius; // 球半径
    private int initAngle; // 初始角度
    private boolean isLeftSwing = true; // 是否向左摆动
    private int location = 1; // 位置 -1：左边 0：中间 1：右边

    public NewtonCradle(Context context) {
        this(context, null);
    }

    public NewtonCradle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewtonCradle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NewtonCradle, defStyleAttr, 0);
        lineColor = typedArray.getColor(R.styleable.NewtonCradle_lineColor, Color.BLACK);
        ballColor = typedArray.getColor(R.styleable.NewtonCradle_ballColor, Color.BLACK);
        lineWidth = typedArray.getDimension(R.styleable.NewtonCradle_lineWidth, 1f);
        radius = typedArray.getDimension(R.styleable.NewtonCradle_radius, 10f);
        initAngle = typedArray.getInt(R.styleable.NewtonCradle_initAngle, 10);
        swingAngle = initAngle; // 初始偏移角度
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        int widthSize;
        int widthMode;
        int heightSize;
        int heightMode;
        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        widthMode = MeasureSpec.getMode(widthMeasureSpec);
        heightSize = MeasureSpec.getSize(heightMeasureSpec);
        heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = (int) (2 * lineLength + 10 * radius);
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = (int) (lineLength + 2 * radius);
        }
        // 根据宽高获取符合的最小线长
        lineLength = (int) Math.min((width-10*radius)/2, height-2*radius);
        setMeasuredDimension(width, height);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCradle(canvas);
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setStyle(Paint.Style.FILL);

        ballPaint = new Paint();
        ballPaint.setColor(ballColor);
        ballPaint.setStyle(Paint.Style.FILL);
    }

    int swingAngle; // 摆动角度
    double pSswingAngle; // 摆动角度对应计算角度
    int ballX, ballY; // 小球的中心坐标
    long cosInitAngle = (long) Math.cos(initAngle);
    long delay;
    long g = (long) 9.8;
    private void drawCradle(Canvas canvas) {
        int width = getWidth();
        // 画中间的圆
        canvas.drawLine(width/2, 0, width/2, lineLength, linePaint);
        canvas.drawCircle(width/2, lineLength, radius, ballPaint);
        // 画左一的圆
        canvas.drawLine(width/2-2*radius, 0, width/2-2*radius, lineLength, linePaint);
        canvas.drawCircle(width/2-2*radius, lineLength, radius, ballPaint);
        // 画右一的圆
        canvas.drawLine(width/2+2*radius, 0, width/2+2*radius, lineLength, linePaint);
        canvas.drawCircle(width/2+2*radius, lineLength, radius, ballPaint);

        pSswingAngle = Math.toRadians(swingAngle);
        switch (location) {
            case LOCATION_LEFT:
                // 画右二的圆
                canvas.drawLine(width/2+4*radius, 0, width/2+4*radius, lineLength, linePaint);
                canvas.drawCircle(width/2+4*radius, lineLength, radius, ballPaint);
                // 画左二的圆
                ballX = (int) (width/2-4*radius - lineLength*Math.sin(pSswingAngle));
                ballY = (int) (lineLength * Math.cos(pSswingAngle));
                canvas.drawLine(width/2-4*radius, 0, ballX, ballY, linePaint);
                canvas.drawCircle(ballX, ballY, radius, ballPaint);
                delay = (long) Math.sqrt(((long)lineLength) / (2*g*(Math.cos(swingAngle - cosInitAngle))));
                if (isLeftSwing) {
                    swingAngle ++;
                    if (swingAngle >= initAngle) {
                        swingAngle = initAngle;
                        isLeftSwing = false;
                    }
                } else {
                    swingAngle --;
                    if (swingAngle <= 0) {
                        swingAngle = 0;
                        location = LOCATION_RIGHT;
                    }
                }
                break;
            case LOCATION_MIDDLE:
                // 画左二的圆
                canvas.drawLine(width/2-4*radius, 0, width/2-4*radius, lineLength, linePaint);
                canvas.drawCircle(width/2-4*radius, lineLength, radius, ballPaint);
                // 画右二的圆
                canvas.drawLine(width/2+4*radius, 0, width/2+4*radius, lineLength, linePaint);
                canvas.drawCircle(width/2+4*radius, lineLength, radius, ballPaint);
                if (isLeftSwing)
                    location = LOCATION_LEFT;
                else
                    location = LOCATION_RIGHT;
                delay = 200; // 中间停顿毫秒数
                break;
            case LOCATION_RIGHT:
                // 画左二的圆
                canvas.drawLine(width/2-4*radius, 0, width/2-4*radius, lineLength, linePaint);
                canvas.drawCircle(width/2-4*radius, lineLength, radius, ballPaint);
                // 画右二的圆
                ballX = (int) (width/2+4*radius + lineLength*Math.sin(pSswingAngle));
                ballY = (int) (lineLength * Math.cos(pSswingAngle));
                canvas.drawLine(width/2+4*radius, 0, ballX, ballY, linePaint);
                canvas.drawCircle(ballX, ballY, radius, ballPaint);
                delay = (long) Math.sqrt(((long)lineLength) / (2*g*(Math.cos(swingAngle - cosInitAngle))));
                if (isLeftSwing) {
                    swingAngle --;
                    if (swingAngle <= 0) {
                        swingAngle = 0;
                        location = LOCATION_MIDDLE;
                    }
                } else {
                    swingAngle ++;
                    if (swingAngle >= initAngle) {
                        swingAngle = initAngle;
                        isLeftSwing = true;
                    }
                }
                break;
        }
        postInvalidateDelayed(delay);
    }

}
