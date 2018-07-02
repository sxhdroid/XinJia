package com.hnxjgou.xinjia.widget;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.hnxjgou.xinjia.R;

public class MyMarkerView extends MarkerView {

    private MPPointF mOffset2 = new MPPointF();

    private TextView tvContent;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = findViewById(R.id.tv_chart_info);
    }

    @Override
    public MPPointF getOffset() {
        // 往右边显示
        tvContent.setBackgroundResource(R.drawable.bg_chart_markerview_left);
        return new MPPointF(0, -getHeight());
    }

    /*
    添加接口，解决MarkView显示问题
     */
    public MPPointF getOffsetRight() {
        // 往左边显示
        tvContent.setBackgroundResource(R.drawable.bg_chart_markerview);
        return new MPPointF(-getWidth(), -getHeight());
    }

    @Override
    public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {

        MPPointF offset = getOffset();
        mOffset2.x = offset.x;
        mOffset2.y = offset.y;

        Chart chart = getChartView();

        float width = getWidth();
        float height = getHeight();
        //添加当点解右边往左显示
        if (chart != null && posX + width + mOffset2.x > chart.getWidth() || posX >= width) {
            offset = getOffsetRight();
            mOffset2.x = offset.x;
        }

        if (posX + mOffset2.x < 0) {
            mOffset2.x = -posX;
        }
        //修改解决显示问题
//        else if (chart != null && posX + width + mOffset2.x > chart.getWidth()) {
//            mOffset2.x = chart.getWidth() - posX - width;
//        }

        if (posY + mOffset2.y < 0) {
            mOffset2.y = -posY;
        } else if (chart != null && posY + height + mOffset2.y > chart.getHeight()) {
            mOffset2.y = chart.getHeight() - posY - height;
        }

        return mOffset2;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setText(e.getY() + "");
        super.refreshContent(e, highlight);
    }
}
