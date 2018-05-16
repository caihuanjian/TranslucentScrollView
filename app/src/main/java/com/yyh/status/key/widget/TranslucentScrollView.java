package com.yyh.status.key.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ScrollView;

import com.yyh.status.key.R;
import com.yyh.status.key.utils.ScreenUtils;
import com.yyh.status.key.utils.SizeUtils;


/**
 * 类功能描述：</br>
 * Android沉浸式状态栏 + scrollView顶部伸缩 + actionBar渐变
 * 博客地址：http://blog.csdn.net/androidstarjack
 * 公众号：终端研发部
 *
 * @author yuyahao
 * @version 1.0 </p> 修改时间：</br> 修改备注：</br>
 */
public class TranslucentScrollView extends ScrollView {

    static final String TAG = "TranslucentScrollView";
    private final static float DEFAULT_DAMP = 0.75f;

    //伸缩视图
    private View zoomView;
    //伸缩视图初始高度
    private int zoomViewInitHeight = 0;
    private int zoomViewInitWidth = 0;
    // 记录首次按下位置
    private float mFirstPosition = 0;
    // 是否正在放大
    private Boolean mScaling = false;

    //渐变的视图
    private View transView;
    //渐变颜色
    private int transColor = Color.WHITE;
    //渐变开始位置
    private int transStartY = 50;
    //渐变结束位置
    private int transEndY = 300;

    //渐变开始默认位置，Y轴，50dp
    private final int DFT_TRANSSTARTY = 50;
    //渐变结束默认位置，Y轴，300dp
    private final int DFT_TRANSENDY = 300;
    private Interpolator mInterpor = new AccelerateInterpolator();

    private TranslucentScrollView.TranslucentChangedListener translucentChangedListener;
    private int mSreenHeight;

    public interface TranslucentChangedListener {
        /**
         * 透明度变化，取值范围0-255
         *
         * @param transAlpha
         */
        void onTranslucentChanged(int transAlpha);
    }

    public TranslucentScrollView(Context context) {
        this(context, null);
    }

    public TranslucentScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TranslucentScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mSreenHeight = ScreenUtils.getScreenHeight(getContext());
    }

    public void setTranslucentChangedListener(TranslucentScrollView.TranslucentChangedListener translucentChangedListener) {
        this.translucentChangedListener = translucentChangedListener;
    }

    /**
     * 设置伸缩视图
     *
     * @param zoomView
     */
    public void setPullZoomView(View zoomView) {
        this.zoomView = zoomView;
        zoomViewInitHeight = zoomView.getHeight();
        zoomViewInitWidth = zoomView.getWidth();
        if (zoomViewInitWidth <= 0 || zoomViewInitHeight <= 0) {
            zoomView.post(new Runnable() {
                @Override
                public void run() {
                    zoomViewInitHeight = TranslucentScrollView.this.zoomView.getHeight();
                    zoomViewInitWidth = TranslucentScrollView.this.zoomView.getWidth();
                }
            });
        }
    }

    /**
     * 设置渐变视图
     *
     * @param transView 渐变的视图
     */
    public void setTransView(View transView) {
        setTransView(transView, getResources().getColor(R.color.colorPrimary), SizeUtils.dip2px(getContext(), DFT_TRANSSTARTY), SizeUtils.dip2px(getContext(), DFT_TRANSENDY));
    }

    /**
     * 设置渐变视图
     *
     * @param transView  渐变的视图
     * @param transColor 渐变颜色
     * @param transEndY  渐变结束位置
     */
    public void setTransView(View transView, @ColorInt int transColor, int transStartY, int transEndY) {
        this.transView = transView;
        //初始视图-透明
        this.transView.setBackgroundColor(ColorUtils.setAlphaComponent(transColor, 0));
        this.transStartY = transStartY;
        this.transEndY = transEndY;
        this.transColor = transColor;
        if (transStartY > transEndY) {
            throw new IllegalArgumentException("transStartY 不得大于 transEndY .. ");
        }
    }

    /**
     * 获取透明度
     *
     * @return
     */
    private int getTransAlpha() {
        float scrollY = getScrollY();
        if (transStartY != 0) {
            if (scrollY <= transStartY) {
                return 0;
            } else if (scrollY >= transEndY) {
                return 255;
            } else {
                return (int) ((scrollY - transStartY) / (transEndY - transStartY) * 255);
            }
        } else {
            if (scrollY >= transEndY) {
                return 255;
            }
            return (int) ((transEndY - scrollY) / transEndY * 255);
        }
    }

    /**
     * 重置ZoomView
     */
    private void resetZoomView() {
        final MarginLayoutParams lp = (MarginLayoutParams) zoomView.getLayoutParams();
        final float h = lp.height;// ZoomView当前高度
        final int w = lp.width;
        final int leftMargin = lp.leftMargin;

        // 设置动画
        ValueAnimator anim = ObjectAnimator.ofFloat(0.0F, 1.0F).setDuration(200);

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                lp.width = (int) (w - (w - zoomViewInitWidth) * cVal);
                lp.height = (int) (h - (h - zoomViewInitHeight) * cVal);
                lp.leftMargin = (int) (leftMargin - leftMargin * cVal);
                zoomView.setLayoutParams(lp);
            }
        });
        anim.start();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        int transAlpha = getTransAlpha();

        if (transView != null) {
            Log.d(TAG, "[onScrollChanged .. in ], 透明度 == " + transAlpha);
            transView.setBackgroundColor(ColorUtils.setAlphaComponent(transColor, transAlpha));
        }
        if (translucentChangedListener != null) {
            translucentChangedListener.onTranslucentChanged(transAlpha);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (zoomView != null) {
            MarginLayoutParams params = (MarginLayoutParams) zoomView.getLayoutParams();
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    //手指离开后恢复图片
                    mScaling = false;
                    resetZoomView();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!mScaling) {
                        if (getScrollY() == 0) {
                            mFirstPosition = event.getY();
                        } else {
                            break;
                        }
                    }
                    float temp = event.getY() - mFirstPosition;
                    float damp = DEFAULT_DAMP - mInterpor.getInterpolation(temp / mSreenHeight);//阻尼系数
                    int distance = (int) ((event.getY() - mFirstPosition) * damp);
                    if (distance < 0) {
                        break;
                    }
                    mScaling = true;
                    params.height = zoomViewInitHeight + distance;
//                    zoomView.animate().scaleX((zoomViewInitWidth + distance) * 1.0f / zoomViewInitWidth).start();
                    params.width = zoomViewInitWidth + distance;
                    params.leftMargin = -distance / 2;

                    Log.d(TAG, "params.height == " + params.height + ", zoomViewInitHeight == " + zoomViewInitHeight + ", distance == " + distance);
                    zoomView.setLayoutParams(params);
                    return true;
            }
        }

        return super.onTouchEvent(event);
    }

}
