package cn.hfiti.toiletapp.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.hfiti.toiletapp.R;
import cn.hfiti.toiletapp.bluetooth.BluetoothLeService;
import cn.hfiti.toiletapp.util.CustomProgressDialog;
import cn.hfiti.toiletapp.view.CustomActionBar;

public class UrineTestActivity extends Activity implements OnClickListener{
	
	private final static String TAG = UrineTestActivity.class.getSimpleName();
	
	public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
	
	private Button startTest;
	private TextView testResult,lifeAdvice,mDataRecvFormat,mRecvBytes,
					 mSendBytes;
	private CustomActionBar urineTest;
	
	static long recv_cnt = 0;
	
	private boolean mConnected = false;
	
	protected BluetoothLeService mBluetoothLeService;
	
	private CustomProgressDialog progressDialog;
	
	private String mDeviceName;
	protected String mDeviceAddress;
	
	private long recvBytes=0;
    private long lastSecondBytes=0;
    private long sendBytes;
    private StringBuilder mData;
    
    int sendIndex = 0;
    int sendDataLen=0;
    byte[] sendBuf;
    
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

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
	}

	private void intiData() {
		startTest.setOnClickListener(this);
		urineTest.setTitleClickListener(this);
	}

	private void initView() {
        startTest = findViewById(R.id.start_test);
        urineTest = findViewById(R.id.urine_test_action_bar);
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
                mSendBytes.setText(sendBytes + " ");
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
    
    private void displayData(byte[] buf) {
        recvBytes += buf.length;
        recv_cnt += buf.length;

        if (recv_cnt>=1024)
        {
            recv_cnt = 0;
            mData.delete(0,mData.length()/2); //UI界面只保留512个字节，免得APP卡顿
        }

//        if (mDataRecvFormat.getText().equals("Ascii")) {
            String s =asciiToString(buf);
            mData.append(s);
//        } else {
//            String s = bytesToString(buf);
//            mData.append(s);
//        }
        testResult.setText(mData.toString());
//        mRecvBytes.setText(recvBytes + " ");
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
