package ltd.maimeng.windowedvoiceroom.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.animation.LinearInterpolator;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import ltd.maimeng.core.ui.widget.CircleImageView;
import ltd.maimeng.windowedvoiceroom.R;

/**
 * @ClassName EnFloatingView
 * @Description 悬浮窗
 * @Author Yunpeng Li
 * @Creation 2018/3/15 下午5:04
 * @Mender Yunpeng Li
 * @Modification 2018/3/15 下午5:04
 */
public class EnFloatingView extends FloatingMagnetView {

    private final CircleImageView ivFloatView;

    public EnFloatingView(@NonNull Context context) {
        this(context, R.layout.widget_float_view);
    }

    public EnFloatingView(@NonNull Context context, @LayoutRes int resource) {
        super(context, null);
        inflate(context, resource, this);
        BreatheView bvFloatView = findViewById(R.id.bv_float_view);
        ivFloatView = findViewById(R.id.iv_float_view);

        bvFloatView.onConnected();
        if (!bvFloatView.isDiffuse()) {
            bvFloatView.onConnected();
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(ivFloatView, "rotation", 0.0F, 360.0F);
        animator.setDuration(6000);
        animator.setRepeatCount(-1);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    public void setIconImage(@DrawableRes int resId) {
        ivFloatView.setImageResource(resId);
    }
}
