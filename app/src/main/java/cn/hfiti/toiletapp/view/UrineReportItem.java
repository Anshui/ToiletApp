package cn.hfiti.toiletapp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.hfiti.toiletapp.R;

/**
 * Created by static on 2017/12/08.
 * 尿液检测报告自定义控件
 */

public class UrineReportItem extends LinearLayout {

    private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10, tv11;
    private ImageView iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9, iv10, iv11;

    public UrineReportItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.urine_report_item, this, true);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        tv6 = findViewById(R.id.tv6);
        tv7 = findViewById(R.id.tv7);
        tv8 = findViewById(R.id.tv8);
        tv9 = findViewById(R.id.tv9);
        tv10 = findViewById(R.id.tv10);
        tv11 = findViewById(R.id.tv11);
        iv1 = findViewById(R.id.iv1);
        iv2 = findViewById(R.id.iv2);
        iv3 = findViewById(R.id.iv3);
        iv4 = findViewById(R.id.iv4);
        iv5 = findViewById(R.id.iv5);
        iv6 = findViewById(R.id.iv6);
        iv7 = findViewById(R.id.iv7);
        iv8 = findViewById(R.id.iv8);
        iv9 = findViewById(R.id.iv9);
        iv10 = findViewById(R.id.iv10);
        iv11 = findViewById(R.id.iv11);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.Urine_report_item);

        String tv1_text = attributes.getString(R.styleable.Urine_report_item_tv1_text);
        tv1.setText(tv1_text);
        int tv1_text_colot = attributes.getColor(R.styleable.Urine_report_item_tv1_text_color, Color.BLACK);
        tv1.setTextColor(tv1_text_colot);
        String tv2_text = attributes.getString(R.styleable.Urine_report_item_tv2_text);
        tv2.setText(tv2_text);
        int tv2_text_colot = attributes.getColor(R.styleable.Urine_report_item_tv2_text_color, Color.BLACK);
        tv2.setTextColor(tv2_text_colot);
        String tv3_text = attributes.getString(R.styleable.Urine_report_item_tv3_text);
        tv3.setText(tv3_text);
        int tv3_text_colot = attributes.getColor(R.styleable.Urine_report_item_tv3_text_color, Color.BLACK);
        tv3.setTextColor(tv3_text_colot);
        String tv4_text = attributes.getString(R.styleable.Urine_report_item_tv4_text);
        tv4.setText(tv4_text);
        int tv4_text_colot = attributes.getColor(R.styleable.Urine_report_item_tv4_text_color, Color.BLACK);
        tv4.setTextColor(tv4_text_colot);
        String tv5_text = attributes.getString(R.styleable.Urine_report_item_tv5_text);
        tv5.setText(tv5_text);
        int tv5_text_colot = attributes.getColor(R.styleable.Urine_report_item_tv5_text_color, Color.BLACK);
        tv5.setTextColor(tv5_text_colot);
        String tv6_text = attributes.getString(R.styleable.Urine_report_item_tv6_text);
        tv6.setText(tv6_text);
        int tv6_text_colot = attributes.getColor(R.styleable.Urine_report_item_tv6_text_color, Color.BLACK);
        tv6.setTextColor(tv6_text_colot);
        String tv7_text = attributes.getString(R.styleable.Urine_report_item_tv7_text);
        tv7.setText(tv7_text);
        int tv7_text_colot = attributes.getColor(R.styleable.Urine_report_item_tv7_text_color, Color.BLACK);
        tv7.setTextColor(tv7_text_colot);
        String tv8_text = attributes.getString(R.styleable.Urine_report_item_tv8_text);
        tv8.setText(tv8_text);
        int tv8_text_colot = attributes.getColor(R.styleable.Urine_report_item_tv8_text_color, Color.BLACK);
        tv8.setTextColor(tv8_text_colot);
        String tv9_text = attributes.getString(R.styleable.Urine_report_item_tv9_text);
        tv9.setText(tv9_text);
        int tv9_text_colot = attributes.getColor(R.styleable.Urine_report_item_tv9_text_color, Color.BLACK);
        tv9.setTextColor(tv9_text_colot);
        String tv10_text = attributes.getString(R.styleable.Urine_report_item_tv10_text);
        tv10.setText(tv10_text);
        int tv10_text_colot = attributes.getColor(R.styleable.Urine_report_item_tv10_text_color, Color.BLACK);
        tv10.setTextColor(tv10_text_colot);
        String tv11_text = attributes.getString(R.styleable.Urine_report_item_tv11_text);
        tv11.setText(tv11_text);
        int tv11_text_colot = attributes.getColor(R.styleable.Urine_report_item_tv11_text_color, Color.BLACK);
        tv11.setTextColor(tv11_text_colot);

        int iv1_drawable = attributes.getResourceId(R.styleable.Urine_report_item_iv1_drawable, -1);
        if (iv1_drawable != -1)
            iv1.setImageResource(iv1_drawable);
        int iv2_drawable = attributes.getResourceId(R.styleable.Urine_report_item_iv2_drawable, -1);
        if (iv2_drawable != -1)
            iv2.setImageResource(iv2_drawable);
        int iv3_drawable = attributes.getResourceId(R.styleable.Urine_report_item_iv3_drawable, -1);
        if (iv3_drawable != -1)
            iv3.setImageResource(iv3_drawable);
        int iv4_drawable = attributes.getResourceId(R.styleable.Urine_report_item_iv4_drawable, -1);
        if (iv4_drawable != -1)
            iv4.setImageResource(iv4_drawable);
        int iv5_drawable = attributes.getResourceId(R.styleable.Urine_report_item_iv5_drawable, -1);
        if (iv5_drawable != -1)
            iv5.setImageResource(iv5_drawable);
        int iv6_drawable = attributes.getResourceId(R.styleable.Urine_report_item_iv6_drawable, -1);
        if (iv6_drawable != -1)
            iv6.setImageResource(iv6_drawable);
        int iv7_drawable = attributes.getResourceId(R.styleable.Urine_report_item_iv7_drawable, -1);
        if (iv7_drawable != -1)
            iv7.setImageResource(iv7_drawable);
        int iv8_drawable = attributes.getResourceId(R.styleable.Urine_report_item_iv8_drawable, -1);
        if (iv8_drawable != -1)
            iv8.setImageResource(iv8_drawable);
        int iv9_drawable = attributes.getResourceId(R.styleable.Urine_report_item_iv9_drawable, -1);
        if (iv9_drawable != -1)
            iv9.setImageResource(iv9_drawable);
        int iv10_drawable = attributes.getResourceId(R.styleable.Urine_report_item_iv10_drawable, -1);
        if (iv10_drawable != -1)
            iv10.setImageResource(iv10_drawable);
        int iv11_drawable = attributes.getResourceId(R.styleable.Urine_report_item_iv11_drawable, -1);
        if (iv11_drawable != -1)
            iv11.setImageResource(iv11_drawable);

        attributes.recycle();
    }

    public TextView getTv1() {
        return tv1;
    }

    public TextView getTv2() {
        return tv2;
    }

    public TextView getTv3() {
        return tv3;
    }

    public TextView getTv4() {
        return tv4;
    }

    public TextView getTv5() {
        return tv5;
    }

    public TextView getTv6() {
        return tv6;
    }

    public TextView getTv7() {
        return tv7;
    }

    public TextView getTv8() {
        return tv8;
    }

    public TextView getTv9() {
        return tv9;
    }

    public TextView getTv10() {
        return tv10;
    }

    public TextView getTv11() {
        return tv11;
    }

    public ImageView getIv1() {
        return iv1;
    }

    public ImageView getIv2() {
        return iv2;
    }

    public ImageView getIv3() {
        return iv3;
    }

    public ImageView getIv4() {
        return iv4;
    }

    public ImageView getIv5() {
        return iv5;
    }

    public ImageView getIv6() {
        return iv6;
    }

    public ImageView getIv7() {
        return iv7;
    }

    public ImageView getIv8() {
        return iv8;
    }

    public ImageView getIv9() {
        return iv9;
    }

    public ImageView getIv10() {
        return iv10;
    }

    public ImageView getIv11() {
        return iv11;
    }
}
