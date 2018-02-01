package cn.hfiti.toiletapp.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import cn.hfiti.toiletapp.R;
import cn.hfiti.toiletapp.bluetooth.BluetoothLeService;
import cn.hfiti.toiletapp.entity.UserInfo;
import cn.hfiti.toiletapp.util.CustomProgressDialog;
import cn.hfiti.toiletapp.util.Define;
import cn.hfiti.toiletapp.util.SharedTool;
import cn.hfiti.toiletapp.view.CustomActionBar;

public class BodyCompositionActivity extends Activity implements OnClickListener{

    private final static String TAG = BodyCompositionActivity.class.getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    public static final int FAT_PROGRESS = 100;
    public static final int FAT_END = 101;

    private TextView username, height, weight, age, sex, bmi, bmr, body_fat;
    private CustomActionBar body_fat_actionbar;
    private SharedTool sharedTool;

    protected BluetoothLeService mBluetoothLeService;
    private String mDeviceName;
    protected String mDeviceAddress;

    private CustomProgressDialog progressDialog;

    private long sendBytes;

    private boolean mConnected = false;

    int sendIndex = 0;
    int sendDataLen = 0;
    byte[] sendBuf;

    private UserInfo info;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FAT_PROGRESS:
                    DecimalFormat format = new DecimalFormat("##0.0");
                    body_fat.setText(format.format(count));
                    break;

                case FAT_END:
                    body_fat.setText(rate);
                    break;
                default:
                    break;
            }
        }
    };
    private double count;
    private String rate;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.body_fat);

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
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_NO_DISCOVERED.equals(action)) {
                mBluetoothLeService.connect(mDeviceAddress);
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
//                final byte[] data = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
//                final StringBuilder stringBuilder = new StringBuilder();
//                 for(byte byteChar : data)
//                      stringBuilder.append(String.format("%02X ", byteChar));
//                Log.v("log",stringBuilder.toString());
                displayResult(intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA));
            } else if (BluetoothLeService.ACTION_WRITE_SUCCESSFUL.equals(action)) {
                if (sendDataLen > 0) {
                    Log.v("log", "Write OK,Send again");
                    onSendBtnClicked();
                } else {
                    Log.v("log", "Write Finish");
                }
            }

        }
    };

    private void displayResult(byte[] byteArrayExtra) {
        String result_value = bytesToString(byteArrayExtra);
        double fat = getResult(result_value);
        setBodyFat(fat);
    }

    private void setBodyFat(double fat) {
        double ffm;
        if (info.getUserSex().equals("男")) {
            ffm = 13.19699 + 0.30845 * Math.pow(info.getUserHeight(), 2) / fat + 0.427884 * info.getUserWeight() - 0.09817 * getAge(info.getUserBrithday()) - 3.5943 * 0;
        } else {
            ffm = 13.19699 + 0.30845 * Math.pow(info.getUserHeight(), 2) / fat + 0.427884 * info.getUserWeight() - 0.09817 * getAge(info.getUserBrithday()) - 3.5943 * 1;
        }
        final double fatRate = ((info.getUserWeight() - ffm) / info.getUserWeight()) * 100;

        DecimalFormat format = new DecimalFormat("##0.0");
        rate = format.format(fatRate);

        count = 0;

        final Thread textProgress = new Thread(new Runnable() {
            @Override
            public void run() {
                while (count < fatRate) {
                    count += 0.1;
                    handler.sendEmptyMessage(FAT_PROGRESS);
                    try {
                        Thread.sleep(6);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(FAT_END);
            }
        });
        textProgress.start();

//        body_fat.setText(rate);
    }

    private double getResult(String result) {
        Log.d("zzh", "string=----------------" + result);
        char a1 = 0;
        char a2 = 0;
        char a3 = 0;
        char a4 = 0;
        char a5 = 0;
        char a6 = 0;
        char a7 = 0;
        char a8 = 0;
        StringBuilder data = new StringBuilder();
        Log.d("zzh", "string.length()=--------" + result.length());
        for (int i = 0; i < result.length(); i++) {
            a1 = result.charAt(0);
            a2 = result.charAt(1);
            a3 = result.charAt(3);
            a4 = result.charAt(4);
            a5 = result.charAt(result.length() - 6);
            a6 = result.charAt(result.length() - 5);
            a7 = result.charAt(result.length() - 3);
            a8 = result.charAt(result.length() - 2);
        }
        if (a1 == 'A' && a2 == 'A' && a3 == '0' && a4 == '4' && a5 == '0' && a6 == 'D' && a7 == '0' && a8 == 'A') {
            data.append(result.substring(6, 8));
            data.append(result.substring(9, 11));
        }
        double body_fat = Integer.parseInt(data.toString(), 16);
        Log.d("zzh", "body_fat:" + body_fat);
        return body_fat;
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
                    Toast.makeText(BodyCompositionActivity.this, "连接成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BodyCompositionActivity.this, "断开！", Toast.LENGTH_SHORT).show();
                }

            }
        });
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
        if (sendDataLen > 20) {
            sendBytes += 20;
            final byte[] buf = new byte[20];
            // System.arraycopy(buffer, 0, tmpBuf, 0, writeLength);
            for (int i = 0; i < 20; i++) {
                buf[i] = sendBuf[sendIndex + i];
            }
            sendIndex += 20;
            mBluetoothLeService.writeData(buf);
            sendDataLen -= 20;
        } else {
            sendBytes += sendDataLen;
            final byte[] buf = new byte[sendDataLen];
            for (int i = 0; i < sendDataLen; i++) {
                buf[i] = sendBuf[sendIndex + i];
            }
            mBluetoothLeService.writeData(buf);
            sendDataLen = 0;
            sendIndex = 0;
        }
    }

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

	private void intiData() {
        if (sharedTool.getSharedBoolean("auto_login", false)) {
            String userIdName = sharedTool.getSharedString("userIdName", null);
            ArrayList<UserInfo> list = Define.dbManager.searchData(userIdName);
            info = list.get(0);

            height.setText(info.getUserHeight() + "");
            weight.setText(info.getUserWeight() + "");
            sex.setText(info.getUserSex());
            username.setText(info.getUserName());
            age.setText(getAge(info.getUserBrithday()) + "");

            DecimalFormat format = new DecimalFormat("##0.0");
            bmi.setText(format.format(info.getUserWeight() / Math.pow(info.getUserHeight() / 100, 2)));
            bmr.setText(format.format(getBMR(info)));
        } else {
            Toast.makeText(this, "尚未登录！", Toast.LENGTH_LONG).show();
        }
    }

    private double getBMR(UserInfo info) {
        double bmr_ = 0.0;
        if (info.getUserSex().equals("男")) {
            bmr_ = 67 + 13.73 * info.getUserWeight() + 5 * info.getUserHeight() - 6.9 * getAge(info.getUserBrithday());
        } else {
            bmr_ = 661 + 9.6 * info.getUserWeight() + 1.72 * info.getUserHeight() - 4.7 * getAge(info.getUserBrithday());
        }
        return bmr_;
    }

    private int getAge(String date) {
        int age_ = 0;
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
        try {
            Date birth = format.parse(date);
            Date now = new Date();

            age_ = now.getYear() - birth.getYear() - 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return age_;
    }

    private void initView() {
        username = findViewById(R.id.user_name);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        age = findViewById(R.id.age);
        sex = findViewById(R.id.sex);
        bmi = findViewById(R.id.body_fat_bmi);
        bmr = findViewById(R.id.body_fat_bmr);
        body_fat = findViewById(R.id.body_fat);

        body_fat_actionbar = findViewById(R.id.body_fat_actionbar);
        body_fat_actionbar.setTitleClickListener(this);

        sharedTool = new SharedTool(this);
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.action_bar_left_button:
			finish();
			
			break;

		default:
			break;
		}
	}
}
