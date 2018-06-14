package com.hnxjgou.xinjia.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

import com.hnxjgou.xinjia.R;

/**
 * 实现图片和文字的上下显示，默认水平居中。
 * Created by apple on 2018/5/16.
 */

@SuppressLint("AppCompatCustomView")
public class ImageTextView extends ImageButton {

    private final static String TAG = "ImageTextView";

    private Paint mPaint;
    private Bitmap imageBitmap;

    private int mBitmapW;
    private int mBitmapH;
    private int textColor;
    private int space;
    private int mTextSize = 12;

    private float textWidth;
    private float textHeight;
    private float ratio;// 图片长宽比

    private String text;

    public ImageTextView(Context context) {
        this(context, null);
    }

    public ImageTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageTextView);
        text = a.getString(R.styleable.ImageTextView_text);
        mTextSize = a.getDimensionPixelSize(R.styleable.ImageTextView_textSize, mTextSize);
        textColor = a.getColor(R.styleable.ImageTextView_textColor, Color.BLACK);
        space = a.getDimensionPixelSize(R.styleable.ImageTextView_space, 5);

        Drawable background = getBackground();
        imageBitmap = ((BitmapDrawable) background).getBitmap();
        mBitmapW = imageBitmap.getWidth();
        mBitmapH = imageBitmap.getHeight();
        ratio = mBitmapW * 1.0f / mBitmapH; // 计算图片宽高比
        setBackgroundDrawable(null);// 清除设置的背景
        a.recycle();

//        LogUtil.i(TAG, "图片宽度: " + mBitmapW);
//        LogUtil.i(TAG, "图片高度: " + mBitmapH);

        init();
    }

    /**
     * 初始化数据
     */
    private void init() {
        mPaint = new Paint();
        mPaint.setColor(textColor);
        mPaint.setTextSize(mTextSize);
        mPaint.setAntiAlias(true); // 打开抗矩齿

        textWidth = mPaint.measureText(text);
        textHeight = getFontHeight();

//        LogUtil.i(TAG, "文字宽度: " + textWidth);
//        LogUtil.i(TAG, "文字高度: " + textHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = measureWidth(widthMeasureSpec);//测量原始控件所占宽度
        int h = measureHeight(heightMeasureSpec);
        //设置最终控件尺寸
        setMeasuredDimension(w, h);
    }

    // 测量控件所占宽度
    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            if (TextUtils.isEmpty(text)) result = getPaddingLeft() + getPaddingRight();
            else result = (int)textWidth + getPaddingLeft() + getPaddingRight();
            result = Math.max(result, mBitmapW);
//            LogUtil.e(TAG, "宽度不确定值: " + result);
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by
                // measureSpec
                result = Math.min(result, specSize);// 60,480
            }
        }
        return result;
    }

    // 测量控件所占高度
    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            if (TextUtils.isEmpty(text)) result = getPaddingTop() + getPaddingBottom() + mBitmapH;
            else
                result = getPaddingTop() + getPaddingBottom() + mBitmapH + space + (int) textHeight;
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by
                // measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG));

        int w = getWidth(); //获取控件宽度
        int h = getHeight(); //获取控件高度
        if (h < mBitmapH + textHeight + space) {
            // 高度不够显示全部内容
            mBitmapH = (int) (h - textHeight - space);
            mBitmapW = (int) (mBitmapH / ratio);
        }else if (w < mBitmapW) {
            mBitmapW = w;
            mBitmapH = (int) (mBitmapW * ratio);
        }

        int left = (int) ((w - mBitmapW) * 0.5f); // 水平居中
        Rect rect = new Rect(left, 0, mBitmapW + left, mBitmapH);
        canvas.drawBitmap(imageBitmap, null, rect, mPaint);
        if (!TextUtils.isEmpty(text)) {
            canvas.drawText(text, (w - textWidth) * .5f, rect.height() + textHeight + space - mPaint.descent() , mPaint);
        }
//        setBackgroundColor(Color.RED);
    }

    /**
     * 获取字体的高度
     *
     * @return
     */
    private int getFontHeight() {
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent);
    }

    /**
     * 改变字体颜色
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mPaint.setColor(Color.BLUE);
//                break;
//            case MotionEvent.ACTION_UP:
//                mPaint.setColor(Color.BLACK);
//                break;
//            default:
//                break;
//        }
//        invalidate();
        return super.onTouchEvent(event);
    }
}
