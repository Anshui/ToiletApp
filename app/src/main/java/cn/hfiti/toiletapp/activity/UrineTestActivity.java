package cn.hfiti.toiletapp.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

import cn.hfiti.toiletapp.R;
import cn.hfiti.toiletapp.bluetooth.BluetoothLeService;
import cn.hfiti.toiletapp.util.CustomProgressDialog;
import cn.hfiti.toiletapp.util.Define;
import cn.hfiti.toiletapp.view.CustomActionBar;
import cn.hfiti.toiletapp.view.UrineReportItem;

public class UrineTestActivity extends Activity implements OnClickListener{
	
	private final static String TAG = UrineTestActivity.class.getSimpleName();
	
	public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private TextView advice;
    private UrineReportItem name, testResult;
    private CustomActionBar urineTest;
	
	static long recv_cnt = 0;
	
	private boolean mConnected = false;
	
	protected BluetoothLeService mBluetoothLeService;
	
	private CustomProgressDialog progressDialog;
	
	private String mDeviceName;
	protected String mDeviceAddress;

    public static final int RESULT_SET = 100;

    private long recvBytes=0;
    private long lastSecondBytes=0;
    private long sendBytes;
    private StringBuilder lifeAdvice;
    private int overproofCount = 0;
    private int greatOverproofCount = 0;
    private int[] result = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    
    int sendIndex = 0;
    int sendDataLen=0;
    byte[] sendBuf;

    private Handler handler;
    
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.urine_test);

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        initView();
        intiData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private void intiData() {
		urineTest.setTitleClickListener(this);
        lifeAdvice = new StringBuilder();
        name.getTv6().setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        name.getTv7().setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case RESULT_SET:
                        screenshot();
                        break;
                }
            }
        };
    }

	private void initView() {
        urineTest = findViewById(R.id.urine_test_action_bar);
        name = findViewById(R.id.name);
        testResult = findViewById(R.id.test_result);
        advice = findViewById(R.id.life_advice);
        advice.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
            case R.id.action_bar_left_button:
                finish();
                break;
            case R.id.start_test:
                getSendBuf(Define.HIP_CLEAN);
                onSendBtnClicked();

		default:
			break;
		}
		
	}

    private void getSendBuf(String command) {
        // TODO Auto-generated method stub
        Log.d("yuhao", "getSendBuf---------------------");
        sendIndex = 0;
        sendBuf = stringToBytes(getHexString(command));
        Log.d("yuhao", "sendBuf=--------------" + sendBuf);
        sendDataLen = sendBuf.length;
    }

    private byte[] stringToBytes(String s) {
        byte[] buf = new byte[s.length() / 2];
        for (int i = 0; i < buf.length; i++) {
            try {
                buf[i] = (byte) Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return buf;
    }

    private String getHexString(String command) {
        String s = command;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (('0' <= c && c <= '9') || ('a' <= c && c <= 'f') ||
                    ('A' <= c && c <= 'F')) {
                sb.append(c);
            }
        }
        if ((sb.length() % 2) != 0) {
            sb.deleteCharAt(sb.length());
        }
        return sb.toString();
    }


    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                invalidateOptionsMenu();
                mBluetoothLeService.connect(mDeviceAddress);
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                //特征值找到才代表连接成功
                mConnected = true;
                invalidateOptionsMenu();
                updateConnectionState(R.string.connected);
            }else if (BluetoothLeService.ACTION_GATT_SERVICES_NO_DISCOVERED.equals(action)){
                mBluetoothLeService.connect(mDeviceAddress);
            }else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
//                final byte[] data = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
//                final StringBuilder stringBuilder = new StringBuilder();
//                 for(byte byteChar : data)
//                      stringBuilder.append(String.format("%02X ", byteChar));
//                Log.v("log",stringBuilder.toString());
                displayResult(intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA));
            }else if (BluetoothLeService.ACTION_WRITE_SUCCESSFUL.equals(action)) {
                if (sendDataLen>0)
                {
                    Log.v("log","Write OK,Send again");
                    onSendBtnClicked();
                }
                else {
                    Log.v("log","Write Finish");
                }
            }

        }
    };
    
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }
    
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.ACTION_WRITE_SUCCESSFUL);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_NO_DISCOVERED);
        return intentFilter;
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }
    
    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               // mConnectionState.setText(resourceId);
            	Log.d("yuhao", "updateConnectionState----------------");
//            	mTextConnect.setText(resourceId);
            	if (mConnected) {
            		progressDialog.dismiss();
            		Toast.makeText(UrineTestActivity.this, "连接成功！", Toast.LENGTH_SHORT).show();
				}
            	else {
            		Toast.makeText(UrineTestActivity.this, "断开！", Toast.LENGTH_SHORT).show();
				}

            }
        });
    }

    private void displayResult(byte[] buf) {
        String result_value = bytesToString(buf);
        result = getTestResult(result_value);
        setTestResult();
    }

    private void setTestResult() {
        setpHResult();
        setNITResult();
        setGLUResult();
        setVCResult();
        setSGReault();
        setBLDResult();
        setPROResult();
        setBILResult();
        setUROResult();
        setKETResult();
        setWBCResult();
        giveAdvice();
        giveAdviceReason();
        saveScreen();
    }

    private void giveAdviceReason() {
        if (result[2] > 3) {
            lifeAdvice.append(Define.GLU);
        }
        if (result[3] > 3) {
            lifeAdvice.append(Define.VC);
        }
        if (result[4] < 3) {
            lifeAdvice.append(Define.SG);
        }
        if (result[5] > 3) {
            lifeAdvice.append(Define.BLD);
        }
        if (result[6] > 3) {
            lifeAdvice.append(Define.PRO);
        }
        if (result[7] > 2) {
            lifeAdvice.append(Define.BIL);
        }
        if (result[8] > 2) {
            lifeAdvice.append(Define.URO);
        }
        if (result[9] > 3) {
            lifeAdvice.append(Define.KET);
        }
        if (result[10] > 3) {
            lifeAdvice.append(Define.WBC);
        }
        advice.setText(lifeAdvice);
        lifeAdvice = new StringBuilder();
    }

    private void saveScreen() {
        if (!TextUtils.isEmpty(advice.getText())) {
            handler.sendEmptyMessageDelayed(RESULT_SET, 100);
        }
    }


    private void screenshot() {
        // 获取屏幕
        View dView = this.getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bmp = dView.getDrawingCache();
        Log.d(TAG, "screenshot: " + bmp);
        if (bmp != null) {
            try {
                // 获取内置SD卡路径
                String sdCardPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
                // 图片文件路径
                String filePath = sdCardPath + File.separator + "Screenshots" + File.separator + System.currentTimeMillis() + ".png";
                Log.d(TAG, "screenshot: " + filePath);

                File file = new File(filePath);
                FileOutputStream os = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void giveAdvice() {
        Log.d(TAG, "giveAdvice: " + overproofCount);
        Log.d(TAG, "giveAdvice: " + greatOverproofCount);
        if (greatOverproofCount == 0) {
            if (overproofCount <= 2) {
                lifeAdvice.append("    尿检结果未出现明显异常，健康状况基本良好!\n\n");
            } else if (overproofCount > 2 && overproofCount <= 5) {
                lifeAdvice.append("    部分指标存在一些异常，注意观察身体状况变化!\n\n");
            } else if (overproofCount > 5) {
                lifeAdvice.append("    多项指标出现异常，请多次测试，并依据具体情况就医复查!\n\n");
            }
        } else {
            if (result[2] > 4) {
                lifeAdvice.append("    葡萄糖检测结果异常，请多次测试，并依据具体情况就医复查!\n\n");
            }
            if (result[3] > 4) {
                lifeAdvice.append("    维生素C检测结果异常，请多次测试，并依据具体情况就医复查!\n\n");
            }
            if (result[4] < 2) {
                lifeAdvice.append("    尿比重检测结果异常，请多次测试，并依据具体情况就医复查!\n\n");
            }
            if (result[5] == 5) {
                lifeAdvice.append("    隐血检测结果异常，请多次测试，并依据具体情况就医复查!\n\n");
            }
            if (result[6] == 5) {
                lifeAdvice.append("    蛋白质检测结果异常，请多次测试，并依据具体情况就医复查!\n\n");
            }
            if (result[7] == 4) {
                lifeAdvice.append("    胆红素检测结果异常，请多次测试，并依据具体情况就医复查!\n\n");
            }
            if (result[8] == 4) {
                lifeAdvice.append("    尿胆原检测结果异常，请多次测试，并依据具体情况就医复查!\n\n");
            }
            if (result[9] == 5) {
                lifeAdvice.append("    酮体检测结果异常，请多次测试，并依据具体情况就医复查!\n\n");
            }
            if (result[10] == 5) {
                lifeAdvice.append("    白细胞检测结果异常，请多次测试，并依据具体情况就医复查!\n\n");
            }
        }
    }

    private void setWBCResult() {
        switch (result[10]) {
            case 1:
                testResult.getTv11().setText("—");
                testResult.getIv11().setImageResource(R.drawable.keep);
                break;
            case 2:
                testResult.getTv11().setText("±");
                testResult.getIv11().setImageResource(R.drawable.keep);
                break;
            case 3:
                testResult.getTv11().setText("+");
                testResult.getIv11().setImageResource(R.drawable.up1);
                overproofCount++;
                break;
            case 4:
                testResult.getTv11().setText("++");
                testResult.getIv11().setImageResource(R.drawable.up2);
                overproofCount++;
                break;
            case 5:
                testResult.getTv11().setText("+++");
                testResult.getIv11().setImageResource(R.drawable.up3);
                greatOverproofCount++;
                break;
            default:
                break;
        }
    }

    private void setKETResult() {
        switch (result[9]) {
            case 1:
                testResult.getTv10().setText("—");
                testResult.getIv10().setImageResource(R.drawable.keep);
                break;
            case 2:
                testResult.getTv10().setText("±");
                testResult.getIv10().setImageResource(R.drawable.keep);
                break;
            case 3:
                testResult.getTv10().setText("+");
                testResult.getIv10().setImageResource(R.drawable.up1);
                overproofCount++;
                break;
            case 4:
                testResult.getTv10().setText("++");
                testResult.getIv10().setImageResource(R.drawable.up2);
                overproofCount++;
                break;
            case 5:
                testResult.getTv10().setText("+++");
                testResult.getIv10().setImageResource(R.drawable.up3);
                greatOverproofCount++;
                break;
            default:
                break;
        }
    }

    private void setUROResult() {
        switch (result[8]) {
            case 1:
                testResult.getTv9().setText("—");
                testResult.getIv9().setImageResource(R.drawable.keep);
                break;
            case 2:
                testResult.getTv9().setText("+");
                testResult.getIv9().setImageResource(R.drawable.up1);
                overproofCount++;
                break;
            case 3:
                testResult.getTv9().setText("++");
                testResult.getIv9().setImageResource(R.drawable.up2);
                overproofCount++;
                break;
            case 4:
                testResult.getTv9().setText("+++");
                testResult.getIv9().setImageResource(R.drawable.up3);
                greatOverproofCount++;
                break;
            default:
                break;
        }
    }

    private void setBILResult() {
        switch (result[7]) {
            case 1:
                testResult.getTv8().setText("—");
                testResult.getIv8().setImageResource(R.drawable.keep);
                break;
            case 2:
                testResult.getTv8().setText("+");
                testResult.getIv8().setImageResource(R.drawable.up1);
                overproofCount++;
                break;
            case 3:
                testResult.getTv8().setText("++");
                testResult.getIv8().setImageResource(R.drawable.up2);
                overproofCount++;
                break;
            case 4:
                testResult.getTv8().setText("+++");
                testResult.getIv8().setImageResource(R.drawable.up3);
                greatOverproofCount++;
                break;
            default:
                break;

        }
    }

    private void setPROResult() {
        switch (result[6]) {
            case 1:
                testResult.getTv7().setText("—");
                testResult.getIv7().setImageResource(R.drawable.keep);
                break;
            case 2:
                testResult.getTv7().setText("±");
                testResult.getIv7().setImageResource(R.drawable.keep);
                break;
            case 3:
                testResult.getTv7().setText("+");
                testResult.getIv7().setImageResource(R.drawable.up1);
                overproofCount++;
                break;
            case 4:
                testResult.getTv7().setText("++");
                testResult.getIv7().setImageResource(R.drawable.up2);
                overproofCount++;
                break;
            case 5:
                testResult.getTv7().setText("+++");
                testResult.getIv7().setImageResource(R.drawable.up3);
                greatOverproofCount++;
                break;
            default:
                break;
        }
    }

    private void setBLDResult() {
        switch (result[5]) {
            case 1:
                testResult.getTv6().setText("—");
                testResult.getIv6().setImageResource(R.drawable.keep);
                break;
            case 2:
                testResult.getTv6().setText("±");
                testResult.getIv6().setImageResource(R.drawable.keep);
                break;
            case 3:
                testResult.getTv6().setText("溶血 +");
                testResult.getIv6().setImageResource(R.drawable.up1);
                overproofCount++;
                break;
            case 4:
                testResult.getTv6().setText("溶血 ++");
                testResult.getIv6().setImageResource(R.drawable.up2);
                overproofCount++;
                break;
            case 5:
                testResult.getTv6().setText("溶血 +++");
                testResult.getIv6().setImageResource(R.drawable.up3);
                greatOverproofCount++;
                break;
            case 6:
                testResult.getTv6().setText("非溶血 +");
                testResult.getIv6().setImageResource(R.drawable.up1);
                overproofCount++;
                break;
            case 7:
                testResult.getTv6().setText("非溶血 ++");
                testResult.getIv6().setImageResource(R.drawable.up2);
                overproofCount++;
                break;
        }
    }

    private void setSGReault() {
        switch (result[4]) {
            case 1:
                testResult.getTv5().setText("1.000");
                testResult.getIv5().setImageResource(R.drawable.down3);
                greatOverproofCount++;
                break;
            case 2:
                testResult.getTv5().setText("1.005");
                testResult.getIv5().setImageResource(R.drawable.down2);
                overproofCount++;
                break;
            case 3:
                testResult.getTv5().setText("1.010");
                testResult.getIv5().setImageResource(R.drawable.down1);
                overproofCount++;
                break;
            case 4:
                testResult.getTv5().setText("1.015");
                testResult.getIv5().setImageResource(R.drawable.keep);
                break;
            case 5:
                testResult.getTv5().setText("1.020");
                testResult.getIv5().setImageResource(R.drawable.keep);
                break;
            case 6:
                testResult.getTv5().setText("1.025");
                testResult.getIv5().setImageResource(R.drawable.keep);
                break;
            case 7:
                testResult.getTv5().setText("1.030");
                testResult.getIv5().setImageResource(R.drawable.up1);
                overproofCount++;
                break;
            default:
                break;
        }
    }

    private void setVCResult() {
        switch (result[3]) {
            case 1:
                testResult.getTv4().setText("—");
                testResult.getIv4().setImageResource(R.drawable.keep);
                break;
            case 2:
                testResult.getTv4().setText("±");
                testResult.getIv4().setImageResource(R.drawable.keep);
                break;
            case 3:
                testResult.getTv4().setText("+");
                testResult.getIv4().setImageResource(R.drawable.up1);
                overproofCount++;
                break;
            case 4:
                testResult.getTv4().setText("++");
                testResult.getIv4().setImageResource(R.drawable.up2);
                overproofCount++;
                break;
            case 5:
                testResult.getTv4().setText("+++");
                testResult.getIv4().setImageResource(R.drawable.up3);
                greatOverproofCount++;
                break;
            default:
                break;
        }
    }

    private void setGLUResult() {
        switch (result[2]) {
            case 1:
                testResult.getTv3().setText("—");
                testResult.getIv3().setImageResource(R.drawable.keep);
                break;
            case 2:
                testResult.getTv3().setText("±");
                testResult.getIv3().setImageResource(R.drawable.keep);
                break;
            case 3:
                testResult.getTv3().setText("+");
                testResult.getIv3().setImageResource(R.drawable.up1);
                overproofCount++;
                break;
            case 4:
                testResult.getTv3().setText("++");
                testResult.getIv3().setImageResource(R.drawable.up2);
                overproofCount++;
                break;
            case 5:
                testResult.getTv3().setText("+++");
                testResult.getIv3().setImageResource(R.drawable.up3);
                greatOverproofCount++;
                break;
            case 6:
                testResult.getTv3().setText("++++");
                testResult.getIv3().setImageResource(R.drawable.up4);
                greatOverproofCount++;
                break;
            default:
                break;

        }
    }

    private void setNITResult() {
        switch (result[1]) {
            case 1:
                testResult.getTv2().setText("—");
                testResult.getIv2().setImageResource(R.drawable.keep);
                break;
            case 2:
                testResult.getTv2().setText("+");
                testResult.getIv2().setImageResource(R.drawable.up1);
                overproofCount++;
                break;
            default:
                break;
        }
    }

    private void setpHResult() {
        testResult.getTv1().setText(String.valueOf(result[0] + 4));
        if (result[0] > 4) {
            testResult.getIv1().setImageResource(R.drawable.up1);
            overproofCount++;
        } else {
            testResult.getIv1().setImageResource(R.drawable.keep);
        }
    }

    private int[] getTestResult(String string) {
        // TODO Auto-generated method stub
        Log.d("zzh", "string=----------------" + string);
        char a1 = 0;
        char a2 = 0;
        char a3 = 0;
        char a4 = 0;
        char a5 = 0;
        char a6 = 0;
        char a7 = 0;
        char a8 = 0;
        StringBuilder data = new StringBuilder();
        Log.d("zzh", "string.length()=--------" + string.length());
        for (int i = 0; i < string.length(); i++) {
            a1 = string.charAt(0);
            a2 = string.charAt(1);
            a3 = string.charAt(3);
            a4 = string.charAt(4);
            a5 = string.charAt(string.length() - 6);
            a6 = string.charAt(string.length() - 5);
            a7 = string.charAt(string.length() - 3);
            a8 = string.charAt(string.length() - 2);
        }
        Log.d("zzh", "a1=-----------" + a1);
        Log.d("zzh", "a2=-----------" + a2);
        Log.d("zzh", "a3=-----------" + a3);
        Log.d("zzh", "a4=-----------" + a4);
        if (a1 == 'A' && a2 == 'A' && a3 == '0' && a4 == '2' && a5 == '0' && a6 == 'D' && a7 == '0' && a8 == 'A') {
            result[0] = Integer.parseInt(string.substring(6, 8), 16);
            result[1] = Integer.parseInt(string.substring(9, 11), 16);
            result[2] = Integer.parseInt(string.substring(12, 14), 16);
            result[3] = Integer.parseInt(string.substring(15, 17), 16);
            result[4] = Integer.parseInt(string.substring(18, 20), 16);
            result[5] = Integer.parseInt(string.substring(21, 23), 16);
            result[6] = Integer.parseInt(string.substring(24, 26), 16);
            result[7] = Integer.parseInt(string.substring(27, 29), 16);
            result[8] = Integer.parseInt(string.substring(30, 32), 16);
            result[9] = Integer.parseInt(string.substring(33, 35), 16);
            result[10] = Integer.parseInt(string.substring(36, 38), 16);
        }
        Log.d(TAG, "zzh: 数组 result " + Arrays.toString(result));
        return result;
    }
    
    public String asciiToString(byte[] bytes) {
        char[] buf = new char[bytes.length];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (char) bytes[i];
            sb.append(buf[i]);
        }
        return sb.toString();
    }

    public String bytesToString(byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = hexArray[v >>> 4];
            hexChars[i * 2 + 1] = hexArray[v & 0x0F];

            sb.append(hexChars[i * 2]);
            sb.append(hexChars[i * 2 + 1]);
            sb.append(' ');
        }
        return sb.toString();
    }
    
    private void onSendBtnClicked() {
        if (sendDataLen>20) {
            sendBytes += 20;
            final byte[] buf = new byte[20];
           // System.arraycopy(buffer, 0, tmpBuf, 0, writeLength);
            for (int i=0;i<20;i++)
            {
                buf[i] = sendBuf[sendIndex+i];
            }
            sendIndex+=20;
            mBluetoothLeService.writeData(buf);
            sendDataLen -= 20;
        }
        else {
            sendBytes += sendDataLen;
            final byte[] buf = new byte[sendDataLen];
            for (int i=0;i<sendDataLen;i++)
            {
                buf[i] = sendBuf[sendIndex+i];
            }
            mBluetoothLeService.writeData(buf);
            sendDataLen = 0;
            sendIndex = 0;
        }
    }
}
