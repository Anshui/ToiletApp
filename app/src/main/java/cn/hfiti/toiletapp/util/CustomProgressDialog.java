package cn.hfiti.toiletapp.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import cn.hfiti.toiletapp.R;

public class CustomProgressDialog extends ProgressDialog {
	private Context mContext;
	private AVLoadingIndicatorView loading_progress;
	private String mLoadingTip;
	private TextView mLoadingTv;
	private String type;

	public CustomProgressDialog(Context context, String content, String type) {
		super(context);
		this.mContext = context;
		this.mLoadingTip = content;
		this.type = type;
		setCanceledOnTouchOutside(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initData() {
		loading_progress.setIndicator(type);
		mLoadingTv.setText(mLoadingTip);
		jumping();
	}

	public void setContent(String str) {
		mLoadingTv.setText(str);
		jumping();
	}
	
	private void jumping(){
		JumpingBeans.with(mLoadingTv)
		.makeTextJump(0, mLoadingTv.getText().length()).setIsWave(true)
		.setLoopDuration(2000).build();
	}

	private void initView() {
		setContentView(R.layout.progress_dialog);
		mLoadingTv = (TextView) findViewById(R.id.loadingTv);
		loading_progress = (AVLoadingIndicatorView) findViewById(R.id.loading_progress);
	}

	private final String[] INDICATORS = new String[] { "BallPulseIndicator",
			"BallGridPulseIndicator", "BallClipRotateIndicator",
			"BallClipRotatePulseIndicator", "SquareSpinIndicator",
			"BallClipRotateMultipleIndicator", "BallPulseRiseIndicator",
			"BallRotateIndicator", "CubeTransitionIndicator",
			"BallZigZagIndicator", "BallZigZagDeflectIndicator",
			"BallTrianglePathIndicator", "BallScaleIndicator",
			"LineScaleIndicator", "LineScalePartyIndicator",
			"BallScaleMultipleIndicator", "BallPulseSyncIndicator",
			"BallBeatIndicator", "LineScalePulseOutIndicator",
			"LineScalePulseOutRapidIndicator", "BallScaleRippleIndicator",
			"BallScaleRippleMultipleIndicator", "BallSpinFadeLoaderIndicator",
			"LineSpinFadeLoaderIndicator", "TriangleSkewSpinIndicator",
			"PacmanIndicator", "BallGridBeatIndicator",
			"SemiCircleSpinIndicator", "com.wang.avi.sample.MyCustomIndicator" };
}
