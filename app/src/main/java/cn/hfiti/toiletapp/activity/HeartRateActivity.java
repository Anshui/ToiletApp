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

import cn.hfiti.toiletapp.R;
import cn.hfiti.toiletapp.bluetooth.BleSppActivity;
import cn.hfiti.toiletapp.bluetooth.BluetoothLeService;
import cn.hfiti.toiletapp.util.CustomProgressDialog;
import cn.hfiti.toiletapp.util.Define;
import cn.hfiti.toiletapp.view.CustomActionBar;

public class HeartRateActivity extends Activity implements OnClickListener{
	
	private final static String TAG = BleSppActivity.class.getSimpleName();
	
	public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    
    private static final int GET_HEART_RATE = 102;
    private static final int TIME_OUT = 103;

    static long recv_cnt = 0;

    private String mDeviceName;
    private String mDeviceAddress;
    
    private String mHeartRate;
    
    private boolean mConnected = false;

	
	private Button searchHeartRate;
	private CustomActionBar heartRateActionBar;
	private CustomProgressDialog progressDialog;
	private TextView heartRate;
	protected BluetoothLeService mBluetoothLeService;
	
	private long recvBytes=0;
    private long lastSecondBytes=0;
    private long sendBytes;
    private StringBuilder mData;

    int sendIndex = 0;
    int sendDataLen=0;
    byte[] sendBuf;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.heart_rate_activity);
		initView();
		initData();
		final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_HEART_RATE:
				mHeartRate = (String) msg.obj;
				if(Integer.parseInt(mHeartRate) > 40) {
					heartRate.setText(mHeartRate);
					progressDialog.dismiss();
				}
				
				break;

			case TIME_OUT:
				if(progressDialog.isShowing()) {
					progressDialog.dismiss();
					Toast.makeText(HeartRateActivity.this, "操作失误，请重新查询！", 1).show();
				}
			default:
				break;
			}
		};
	};
	
	// Code to manage Service lifecycle.
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
            }else if (BluetoothLeService.ACTION_GATT_SERVICES_NO_DISCOVERED.equals(action)){
                mBluetoothLeService.connect(mDeviceAddress);
            }else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
//                final byte[] data = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
//                final StringBuilder stringBuilder = new StringBuilder();
//                 for(byte byteChar : data)
//                      stringBuilder.append(String.format("%02X ", byteChar));
//                Log.v("log",stringBuilder.toString());
                displayData(intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA));
            }else if (BluetoothLeService.ACTION_WRITE_SUCCESSFUL.equals(action)) {
//                mSendBytes.setText(sendBytes + " ");
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
    
    //查询心率
    public String searchHeartRate() {
    	getSendBuf(Define.SEARCH_HEART_RATE);
        onSendBtnClicked();
        String heartRate = "";
        heartRate = getReturnValue(mData.toString());
		return heartRate;
    }
    
    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               // mConnectionState.setText(resourceId);
            }
        });
    }
    
    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(mGattUpdateReceiver);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unbindService(mServiceConnection);
        mBluetoothLeService = null;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
        	Log.d("yuhao", "connect-------------------4");
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d("yuhao", "Connect request result=" + result);
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

    
    private void onSendBtnClicked() {
		// TODO Auto-generated method stub
		Log.d("yuhao", "onSendBtnClicked-------------------");
        if (sendDataLen>20) {
            sendBytes += 20;
            final byte[] buf = new byte[20];
           // System.arraycopy(buffer, 0, tmpBuf, 0, writeLength);
            for (int i=0;i<20;i++)
            {
                buf[i] = sendBuf[sendIndex+i];
                Log.d("yuhao", "buf[i]------0-----="+buf[i]);
            }
            sendIndex+=20;
            Log.d("yuhao", "buf-----0---="+buf);
            mBluetoothLeService.writeData(buf);
            sendDataLen -= 20;
        }
        else {
            sendBytes += sendDataLen;
            final byte[] buf = new byte[sendDataLen];
            for (int i=0;i<sendDataLen;i++)
            {
                buf[i] = sendBuf[sendIndex+i];
                Log.d("yuhao", "buf[i]------1-----="+buf[i]);
            }
            Log.d("yuhao", "buf-----1---="+buf);
            mBluetoothLeService.writeData(buf);
            sendDataLen = 0;
            sendIndex = 0;
        }
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

	//获取输入框十六进制格式
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

	private void getSendBuf(String command) {
		// TODO Auto-generated method stub
		Log.d("yuhao", "getSendBuf---------------------");
        sendIndex = 0;
        sendBuf = stringToBytes(getHexString(command));
        Log.d("yuhao", "sendBuf=--------------"+sendBuf);
        sendDataLen = sendBuf.length;
	}
    
    private String getReturnValue(String string) {
		// TODO Auto-generated method stub
		Log.d("yuhao", "string=----------------"+string);
		char a1 = 0;
		char a2 = 0;
		char a3 = 0;
		char a4 = 0;
		char a5 = 0;
		char a6 = 0;
		StringBuilder data = new StringBuilder();
		Log.d("yuhao", "string.length()=--------"+string.length());
		for (int i = 0; i < string.length(); i++) {
			a1 = string.charAt(0);
			a2 = string.charAt(1);
			a3 = string.charAt(3);
			a4 = string.charAt(4);
			a5 = string.charAt(string.length()-3);
			a6 = string.charAt(string.length()-2);
		}
		Log.d("yuhao", "a1=-----------"+a1);
		Log.d("yuhao", "a2=-----------"+a2);
		Log.d("yuhao", "a3=-----------"+a3);
		Log.d("yuhao", "a4=-----------"+a4);
		int a = 0;
		if (a1=='5' && a2=='5' && a3=='0' && a4=='4' && a5=='A' && a6=='A') {
			data.append(Integer.parseInt(string.substring(6,8), 16));
//			data.append(string.substring(9,11));
			Log.d("yuhao", "data.toString()=--------"+data.toString());
			a = Integer.parseInt(data.toString());
			Log.d("yuhao", "a=-----------------"+a);
			return a+"";
		}
		else {
//			Toast.makeText(MainActivity.this, "查询失败,请重新查询！", Toast.LENGTH_SHORT).show();
			return "0";
		}
		
		
	}
    
    protected void displayData(byte[] buf) {
		// TODO Auto-generated method stub
    	String s = bytesToString(buf);
    	mData.append(s);
		String heartRate = "";
		heartRate = getReturnValue(mData.toString());
        if(heartRate != "") {
        	Message msg = new Message();
        	msg.what = GET_HEART_RATE;
        	msg.obj = heartRate;
        	handler.sendMessage(msg);
        }
        mData = mData.delete(0, mData.length());
	}
    
    private String bytesToString(byte[] bytes) {
		// TODO Auto-generated method stub
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

	private void initData() {
		searchHeartRate.setOnClickListener(this);
		heartRateActionBar.setTitleClickListener(this);
		progressDialog.setCanceledOnTouchOutside(false);
		
		mData = new StringBuilder();
	}

	private void initView() {
		searchHeartRate = (Button) findViewById(R.id.search_heart_rate);
		heartRateActionBar = (CustomActionBar) findViewById(R.id.heart_rate_action_bar);
		heartRate = (TextView) findViewById(R.id.heart_rate);
		progressDialog = new CustomProgressDialog(this, "正在查询心率...", "BallSpinFadeLoaderIndicator");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_heart_rate:
			searchHeartRate();
			progressDialog.show();
			handler.sendEmptyMessageDelayed(TIME_OUT, 12000);
			
			break;

		case R.id.action_bar_left_button:
			finish();
			
			break;
		default:
			break;
		}
	}

}
