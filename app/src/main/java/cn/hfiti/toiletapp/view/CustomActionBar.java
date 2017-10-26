package cn.hfiti.toiletapp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.hfiti.toiletapp.R;

public class CustomActionBar extends RelativeLayout {

	private ImageView actionBarLeftBtn;
	private Button actionBarRightBtn;
	private TextView actionBarTitle;

	public CustomActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.custom_action_bar, this, true);
		actionBarLeftBtn = (ImageView) findViewById(R.id.action_bar_left_button);
		actionBarRightBtn = (Button) findViewById(R.id.action_bar_right_button);
		actionBarTitle = (TextView) findViewById(R.id.action_bar_title);

		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomActionBar);
		if (attributes != null) {
			// 处理titleBar背景色
			int titleBarBackGround = attributes.getResourceId(R.styleable.CustomActionBar_title_background_color,
					Color.GREEN);
			setBackgroundResource(titleBarBackGround);
			// 先处理左边按钮
			// 获取是否要显示左边按钮
			boolean leftButtonVisible = attributes.getBoolean(R.styleable.CustomActionBar_left_button_visible, true);
			if (leftButtonVisible) {
				actionBarLeftBtn.setVisibility(View.VISIBLE);
			} else {
				actionBarLeftBtn.setVisibility(View.INVISIBLE);
			}

			// 设置左边图片icon 这里是二选一 要么只能是文字 要么只能是图片
			int leftButtonDrawable = attributes.getResourceId(R.styleable.CustomActionBar_left_button_drawable,
					R.drawable.back);
			if (leftButtonDrawable != -1) {
				actionBarLeftBtn.setBackgroundResource(leftButtonDrawable);
			}

			// 处理标题
			// 先获取标题是否要显示图片icon
			int titleTextDrawable = attributes.getResourceId(R.styleable.CustomActionBar_title_text_drawable, -1);
			if (titleTextDrawable != -1) {
				actionBarTitle.setBackgroundResource(titleTextDrawable);
			} else {
				// 如果不是图片标题 则获取文字标题
				String titleText = attributes.getString(R.styleable.CustomActionBar_title_text);
				if (!TextUtils.isEmpty(titleText)) {
					actionBarTitle.setText(titleText);
				}
				// 获取标题显示颜色
				int titleTextColor = attributes.getColor(R.styleable.CustomActionBar_title_text_color, Color.WHITE);
				actionBarTitle.setTextColor(titleTextColor);
			}

			// 先处理右边按钮
			// 获取是否要显示右边按钮
			boolean rightButtonVisible = attributes.getBoolean(R.styleable.CustomActionBar_right_button_visible, true);
			if (rightButtonVisible) {
				actionBarRightBtn.setVisibility(View.VISIBLE);
			} else {
				actionBarRightBtn.setVisibility(View.INVISIBLE);
			}
			// 设置右边按钮的文字
			String rightButtonText = attributes.getString(R.styleable.CustomActionBar_right_button_text);
			if (!TextUtils.isEmpty(rightButtonText)) {
				actionBarRightBtn.setText(rightButtonText);
				// 设置右边按钮文字颜色
				int rightButtonTextColor = attributes.getColor(R.styleable.CustomActionBar_right_button_text_color,
						Color.WHITE);
				actionBarRightBtn.setTextColor(rightButtonTextColor);
			} else {
				// 设置右边图片icon 这里是二选一 要么只能是文字 要么只能是图片
				int rightButtonDrawable = attributes.getResourceId(R.styleable.CustomActionBar_right_button_drawable,
						-1);
				if (rightButtonDrawable != -1) {
					actionBarRightBtn.setBackgroundResource(rightButtonDrawable);
				}
			}
			attributes.recycle();
		}
	}

	public void setTitleClickListener(OnClickListener onClickListener) {
		if (onClickListener != null) {
			actionBarLeftBtn.setOnClickListener(onClickListener);
			actionBarRightBtn.setOnClickListener(onClickListener);
		}
	}

	public ImageView getTitleBarLeftBtn() {
		return actionBarLeftBtn;
	}

	public Button getTitleBarRightBtn() {
		return actionBarRightBtn;
	}

	public TextView getTitleBarTitle() {
		return actionBarTitle;
	}
}
