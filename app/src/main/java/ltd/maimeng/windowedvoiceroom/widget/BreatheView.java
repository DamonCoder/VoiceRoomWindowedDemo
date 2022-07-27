package ltd.maimeng.windowedvoiceroom.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.Nullable;
import ltd.maimeng.windowedvoiceroom.R;

/**
 * <pre>
 *     author : chenzimeng
 *     e-mail : 1989583040@qq.com
 *     time   : 2020/12/31
 *     desc   : 呼吸灯
 * </pre>
 */
public class BreatheView extends View implements ValueAnimator.AnimatorUpdateListener {

    private Paint mPaint;
    private ValueAnimator mAnimator;
    private Handler mHandler;

    /**
     * 每隔多久闪烁一次
     */
    private static final long HEART_BEAT_RATE = 1000;
    private static final long ANIMATION_DURATION = 1000;

    /**
     * 是否正在扩散中
     */
    private boolean mIsDiffuse = false;

    /***
     * 初始透明度
     */
    private int color = 255;

    /**
     * 扩散圆圈颜色
     */
    private int mBreatheColor = getResources().getColor(R.color.Green);
    /**
     * 圆圈中心颜色
     */
    private int mCoreColor = getResources().getColor(R.color.Yellow);

    /**
     * 变化因子
     */
    private float mFraction;

    /**
     * 中心圆半径
     */
    private float mCoreRadius = 30f;

    /**
     * 整个绘制面的最大宽度
     */
    private float mBreatheWidth = 40f;

    // 圆圈中心坐标
    private float circleX;
    private float circleY;

    public BreatheView(Context context) {
        this(context, null);
    }

    public BreatheView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BreatheView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BreatheView, 0, 0);
        mCoreColor = a.getColor(R.styleable.BreatheView_color_core, Color.parseColor("#0ba62c"));
        mBreatheColor = a.getColor(R.styleable.BreatheView_color_breathe, Color.parseColor("#0ba62c"));
        mCoreRadius = a.getDimension(R.styleable.BreatheView_width_core, 10);
        mBreatheWidth = a.getDimension(R.styleable.BreatheView_width_breathe, 5);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth((float) 3.0);// 线宽
        mPaint.setStyle(Paint.Style.STROKE);// 空心
        mAnimator = ValueAnimator.ofFloat(0, 1f).setDuration(ANIMATION_DURATION);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addUpdateListener(this);
        if (null == mHandler) {
            mHandler = new Handler();
        }
    }

    // @Override
    // public void invalidate() {
    //     if (hasWindowFocus()) {
    //         super.invalidate();
    //     }
    // }

    // @Override
    // public void onWindowFocusChanged(boolean hasWindowFocus) {
    //     super.onWindowFocusChanged(hasWindowFocus);
    //     if (hasWindowFocus) {
    //         invalidate();
    //     }
    // }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        circleX = w / 2;
        circleY = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // if (mIsDiffuse) {
        // 绘制扩散圆
        mPaint.setStyle(Paint.Style.STROKE);// 空心
        mPaint.setColor(mBreatheColor);
        if (mFraction < 1.0f) {
            mPaint.setAlpha((int) (color - color * mFraction * 0.3f));
            canvas.drawCircle(circleX, circleY, mCoreRadius + mBreatheWidth * mFraction * 0.3f, mPaint);
        }
        mPaint.setAlpha((int) (color - color * mFraction));
        canvas.drawCircle(circleX, circleY, mCoreRadius + mBreatheWidth * mFraction, mPaint);
        // 绘制中心圆
        mPaint.setStyle(Paint.Style.FILL);// 实心
        mPaint.setAlpha(255);
        mPaint.setColor(mCoreColor);
        canvas.drawCircle(circleX, circleY, mCoreRadius, mPaint);
        // }
        invalidate();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        mFraction = (float) valueAnimator.getAnimatedValue();
    }

    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            if (mIsDiffuse) {
                start();
                mHandler.postDelayed(this, HEART_BEAT_RATE);
            } else {
                invalidate();
                mHandler.removeCallbacks(heartBeatRunnable);
            }
        }
    };

    private void start() {
        mIsDiffuse = true;
        mAnimator.start();
        invalidate();
    }

    public void onConnected() {
        mHandler.removeCallbacks(heartBeatRunnable);
        mHandler.post(heartBeatRunnable);
        mIsDiffuse = true;
    }

    public void onDestroy() {
        mIsDiffuse = false;
    }

    public boolean isDiffuse() {
        return mIsDiffuse;
    }
}
