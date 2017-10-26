package cn.hfiti.toiletapp.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.hfiti.toiletapp.R;
import cn.hfiti.toiletapp.bluetooth.BluetoothLeService;
import cn.hfiti.toiletapp.bluetooth.DeviceScanActivity;
import cn.hfiti.toiletapp.db.DBManager;
import cn.hfiti.toiletapp.util.CustomProgressDialog;
import cn.hfiti.toiletapp.util.Define;

public class MainActivity extends FragmentActivity implements OnClickListener{
	
	private ImageView mImgWeight;
	private ImageView mImgConnect;
	private ImageView mImgQuit;
	private ImageView mImgRemote;
	private ImageView mImgUser;
	private ImageView mImgHome;
	private ImageView mXuanchuan;
	private TextView mTextConnect;
	private CustomProgressDialog progressDialog;
	
	private FragmentTransaction beginTransaction;
	private FragmentManager mFragmrentManager;
	private RemoteFragment mRemoteFragment;
	
	private String mDeviceName;
    private String mDeviceAddress;
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    private int sendIndex = 0;
    private int sendDataLen=0;
    private byte[] sendBuf;
    private boolean mConnected = false; //实际连接状态标志位
    private boolean connectFlag = false; //是否手动断开或连接标志位
    private BluetoothLeService mBluetoothLeService;
    private long sendBytes;
    private StringBuilder mData , mWeightData,mTempData;
    
    private Handler handler ;
    
    public static MainActivity mMainActivity;
    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
//                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            Log.d("yuhao", "connect------------------1");
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
            	Log.d("yuhao", "ACTION_GATT_CONNECTED-------------------");

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                invalidateOptionsMenu();
                Log.d("yuhao", "ACTION_GATT_DISCONNECTED-------------------");
                if (!connectFlag) {
                	Log.d("yuhao", "ACTION_GATT_DISCONNECTED---------connect----------");
                	mBluetoothLeService.connect(mDeviceAddress);
				}                
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                //特征值找到才代表连接成功
            	Log.d("yuhao", "ACTION_GATT_SERVICES_DISCOVERED-------------------");
                mConnected = true;
                invalidateOptionsMenu();
                updateConnectionState(R.string.connected);
            }else if (BluetoothLeService.ACTION_GATT_SERVICES_NO_DISCOVERED.equals(action)){
            	Log.d("yuhao", "ACTION_GATT_SERVICES_NO_DISCOVERED-------------------");
                mBluetoothLeService.connect(mDeviceAddress);
            }else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
            	Log.d("yuhao", "ACTION_DATA_AVAILABLE--------------------");
//                final byte[] data = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
//                final StringBuilder stringBuilder = new StringBuilder();
//                 for(byte byteChar : data)
//                      stringBuilder.append(String.format("%02X ", byteChar));
//                Log.v("log",stringBuilder.toString());
//                displayTempData(intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA));
                displayWeight(intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA));
            }else if (BluetoothLeService.ACTION_WRITE_SUCCESSFUL.equals(action)) {
            	Log.d("yuhao", "ACTION_WRITE_SUCCESSFUL--------------------");
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) { 
	        Log.d("yuhao", "onKeyDown-------------------");
//	        new AlertDialog.Builder(this).setTitle("确认退出？")
//            .setIcon(android.R.drawable.ic_dialog_info) 
//            .setPositiveButton("确定", new DialogInterface.OnClickListener() {            	
//                @Override 
//                public void onClick(DialogInterface dialog, int which) {
//                    MainActivity.this.finish();
//                    clearData();
//                }
//            })
//            .setNegativeButton("返回", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                }
//            }).show();
	    } 
		return super.onKeyDown(keyCode, event);
	}
	
	public void setHandler(Handler handler) {
		this.handler = handler ;
		
	}

	protected void displayWeight(byte[] buf) {
		// TODO Auto-generated method stub
		String s = bytesToString(buf);
		mWeightData.append(s);
		String weightValue = "";
        weightValue = getWeightValue(mWeightData.toString());
        if(weightValue != "") {
        	Message msg = new Message();
        	msg.what = mRemoteFragment.RECEIVE_WEIGHT_DATA;
        	msg.obj = weightValue;
        	handler.sendMessage(msg);
        }
        mWeightData = mWeightData.delete(0, mWeightData.length());
	}

	protected void displayTempData(byte[] buf) {
		// TODO Auto-generated method stub
		String s = bytesToString(buf);
		mTempData.append(s);
		String tempValue = "";
		tempValue = getTempValue(mWeightData.toString());
        if(tempValue != "") {
        	Message msg = new Message();
        	msg.what = mRemoteFragment.RECEIVE_WEIGHT_DATA;
        	msg.obj = tempValue;
        	handler.sendMessage(msg);
        }
        mTempData = mTempData.delete(0, mTempData.length());
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		init();
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

	private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               // mConnectionState.setText(resourceId);
            	Log.d("yuhao", "updateConnectionState----------------");
//            	mTextConnect.setText(resourceId);
            	if (mConnected) {
            		progressDialog.dismiss();
            		mImgConnect.setImageResource(R.drawable.bluetooth_icon);
            		Toast.makeText(MainActivity.this, "连接成功！", Toast.LENGTH_SHORT).show();
				}
            	else {
            		mImgConnect.setImageResource(R.drawable.bluetooth_icon1);
            		Toast.makeText(MainActivity.this, "断开！", Toast.LENGTH_SHORT).show();
				}
				
            }
        });
    }

	private void init() {
		// TODO Auto-generated method stub
		mImgWeight = (ImageView) findViewById(R.id.bn_health);
		mImgConnect = (ImageView) findViewById(R.id.bn_connect);
		mImgRemote = (ImageView) findViewById(R.id.bn_remote);
		mImgQuit = (ImageView) findViewById(R.id.bn_quit);
		mImgUser = (ImageView) findViewById(R.id.img_user);
		mImgHome = (ImageView) findViewById(R.id.img_home1);
		mXuanchuan = (ImageView) findViewById(R.id.xuanchuan);
		mTextConnect = (TextView) findViewById(R.id.text_connect);
		mRemoteFragment = new RemoteFragment();
		mFragmrentManager = getSupportFragmentManager();
		Define.dbManager = new DBManager(this);
		mMainActivity = this;
		progressDialog = new CustomProgressDialog(this, "正在连接....", "BallSpinFadeLoaderIndicator");
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		mData = new StringBuilder();
		mWeightData = new StringBuilder();
		
		mImgWeight.setOnClickListener(this);
		mImgConnect.setOnClickListener(this);
		mImgRemote.setOnClickListener(this);
		mImgQuit.setOnClickListener(this);
		mImgUser.setOnClickListener(this);
		mImgHome.setOnClickListener(this);
		
		//获取蓝牙的名字和地址
        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bn_health:
			Log.d("yuhao", "bn_weight----Define.LOGIN_SUCCESS-="+Define.LOGIN_SUCCESS);
			if (mConnected) {
				if (Define.LOGIN_SUCCESS) {
					Log.d("yuhao", "bn_weight---switchToUser---");
					switchToHealth();
				}
				else {
					Log.d("yuhao", "bn_weight---switchToLogin---");
					switchToLogin();
				}
			}
			else {
				Toast.makeText(MainActivity.this, "请先连接马桶！", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.bn_connect:
			Log.d("yuhao", "mConnected=------0-------"+mConnected);
			if (mConnected) {
				mBluetoothLeService.disconnect();
				connectFlag = true;
			}
			else {
				progressDialog.show();
				mBluetoothLeService.connect(mDeviceAddress);
				connectFlag = false;
			}
			Log.d("yuhao", "mConnected=------1-------"+mConnected);
			break;
		case R.id.bn_remote:
			mXuanchuan.setVisibility(View.GONE);
			switchToRemote();
			break;
		case R.id.bn_quit:
			new AlertDialog.Builder(this).setTitle("确认退出？")
	            .setIcon(android.R.drawable.ic_dialog_info) 
	            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	                @Override 
	                public void onClick(DialogInterface dialog, int which) {
	                    MainActivity.this.finish();
	                    DeviceScanActivity.instance.finish();
	                    clearData();
	                }
	            })
	            .setNegativeButton("返回", new DialogInterface.OnClickListener() {         
	                @Override
	                public void onClick(DialogInterface dialog, int which) {
	
	                }
	            }).show();
			break;
		case R.id.img_user:
			Log.d("yuhao", "img_user----Define.LOGIN_SUCCESS-="+Define.LOGIN_SUCCESS);
			if (Define.LOGIN_SUCCESS) {
				switchToUser();
			}
			else {
				switchToLogin();
			}
			break;
		case R.id.img_home1:
			mXuanchuan.setVisibility(View.VISIBLE);
			break;
		
		default:
			break;
		}
	}

	private void switchToHealth() {
		Intent intent = new Intent(this,HealthActivity.class);
		startActivity(intent);
	}

	protected void clearData() {
		// TODO Auto-generated method stub
		Define.SET_SEAT_TEMP = 2;
		Define.SET_WATER_TEMP = 2;
		Define.SET_WATER_STH = 2;
		Define.SET_DRY_TEMP = 2;
	}

	public static MainActivity getMainActivity(){
		return mMainActivity;		
	}
	
	private void switchToUser() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(MainActivity.this,UserActivity.class);
		startActivity(intent);
	}

	private void switchToRemote() {
		// TODO Auto-generated method stub
		beginTransaction = mFragmrentManager.beginTransaction();
		beginTransaction.replace(R.id.main_frag, mRemoteFragment);
		beginTransaction.commit();
	}

	private void switchToLogin() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(MainActivity.this,LoginActivity.class);
		startActivity(intent);
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

	//臀洗
	public void hipClean() {
		Log.d("yuhao", "hipClean--------------------");
		getSendBuf(Define.HIP_CLEAN);
        onSendBtnClicked();
	}
	//妇洗
	public void ladyClean() {
		Log.d("yuhao", "ladyClean--------------------");
		getSendBuf(Define.LADY_CLEAN);
        onSendBtnClicked();
	}
	//喷头自洁
	public void nozzleClean() {
		Log.d("yuhao", "nozzleClean--------------------");
		getSendBuf(Define.NOZZLE_CLEAN);
        onSendBtnClicked();
	}
	//烘干
	public void dry() {
		Log.d("yuhao", "dry--------------------");
		getSendBuf(Define.DRY);
        onSendBtnClicked();
	}
	//喷头前移
	public void nFront() {
		Log.d("yuhao", "nFront--------------------");
		getSendBuf(Define.N_FRONT);
        onSendBtnClicked();
	}
	//喷头后移
	public void nBack() {
		Log.d("yuhao", "nBack--------------------");
		getSendBuf(Define.N_BACK);
        onSendBtnClicked();
	}
	//自动冲水
	public void flush() {
		Log.d("yuhao", "flush--------------------");
		getSendBuf(Define.FLUSH);
        onSendBtnClicked();
	}
	//自动翻盖
	public void cover() {
		Log.d("yuhao", "cover--------------------");
		getSendBuf(Define.COVER);
        onSendBtnClicked();
	}
	//夜灯开
	public void lightOn() {
		Log.d("yuhao", "lightOn--------------------");
		getSendBuf(Define.LIGHT_ON);
        onSendBtnClicked();
	}
	//夜灯关
	public void lightOff() {
		Log.d("yuhao", "lightOff--------------------");
		getSendBuf(Define.LIGHT_OFF);
        onSendBtnClicked();
	}
	//设置座温1
	public void setSeatTemp_1() {
		Log.d("yuhao", "setSeatTemp_1--------------------");
		getSendBuf(Define.SET_SEAT_TEMP_1);
        onSendBtnClicked();
	}
	//设置座温2
	public void setSeatTemp_2() {
		Log.d("yuhao", "setSeatTemp_2--------------------");
		getSendBuf(Define.SET_SEAT_TEMP_2);
        onSendBtnClicked();
	}
	//设置座温3
	public void setSeatTemp_3() {
		Log.d("yuhao", "setSeatTemp_3--------------------");
		getSendBuf(Define.SET_SEAT_TEMP_3);
        onSendBtnClicked();
	}
	//设置座温4
		public void setSeatTemp_4() {
			Log.d("yuhao", "setSeatTemp_4--------------------");
			getSendBuf(Define.SET_SEAT_TEMP_4);
	        onSendBtnClicked();
		}
	//设置水温1
	public void setWaterTemp_1() {
		Log.d("yuhao", "setWaterTemp_1--------------------");
		getSendBuf(Define.SET_WATER_TEMP_1);
        onSendBtnClicked();
	}
	//设置水温2
	public void setWaterTemp_2() {
		Log.d("yuhao", "setWaterTemp_2--------------------");
		getSendBuf(Define.SET_WATER_TEMP_2);
        onSendBtnClicked();
	}
	//设置水温3
	public void setWaterTemp_3() {
		Log.d("yuhao", "setWaterTemp_3--------------------");
		getSendBuf(Define.SET_WATER_TEMP_3);
        onSendBtnClicked();
	}
	//设置水温4
	public void setWaterTemp_4() {
		Log.d("yuhao", "setWaterTemp_4--------------------");
		getSendBuf(Define.SET_WATER_TEMP_4);
        onSendBtnClicked();
	}
	//设置水压1
	public void setWaterPress_1() {
		Log.d("yuhao", "setWaterPress_1--------------------");
		getSendBuf(Define.SET_WATER_STH_1);
        onSendBtnClicked();
	}
	//设置水压2
	public void setWaterPress_2() {
		Log.d("yuhao", "setWaterPress_2--------------------");
		getSendBuf(Define.SET_WATER_STH_2);
        onSendBtnClicked();
	}
	//设置水压3
	public void setWaterPress_3() {
		Log.d("yuhao", "setWaterPress_3--------------------");
		getSendBuf(Define.SET_WATER_STH_3);
        onSendBtnClicked();
	}
	//设置水压4
		public void setWaterPress_4() {
			Log.d("yuhao", "setWaterPress_4--------------------");
			getSendBuf(Define.SET_WATER_STH_4);
	        onSendBtnClicked();
		}
	//设置烘干温度1
	public void setDryTemp_1() {
		Log.d("yuhao", "setDryTemp_1--------------------");
		getSendBuf(Define.SET_DRY_TEMP_1);
        onSendBtnClicked();
	}
	//设置烘干温度2
	public void setDryTemp_2() {
		Log.d("yuhao", "setDryTemp_2--------------------");
		getSendBuf(Define.SET_DRY_TEMP_2);
        onSendBtnClicked();
	}
	//设置烘干温度3
	public void setDryTemp_3() {
		Log.d("yuhao", "setDryTemp_3--------------------");
		getSendBuf(Define.SET_DRY_TEMP_3);
        onSendBtnClicked();
	}
	//设置烘干温度4
	public void setDryTemp_4() {
		Log.d("yuhao", "setDryTemp_4--------------------");
		getSendBuf(Define.SET_DRY_TEMP_4);
        onSendBtnClicked();
	}
	//停止
	public void stopAll() {
		Log.d("yuhao", "stopAll--------------------");
		getSendBuf(Define.END_INQ);
        onSendBtnClicked();
	}
	public String searchSeatTemp(){
		Log.d("yuhao", "searchSeatTemp--------------------");
		getSendBuf(Define.SEARCH_SEAT_TEMP);
        onSendBtnClicked();
        Log.d("yuhao", "seatTemp=----"+mData.toString());
        String seatTempValue = "";
        seatTempValue = getTempValue(mData.toString());
		return seatTempValue;
	}	
	//解析帧头帧尾，提取数据
	private String getWeightValue(String string) {
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
		double a = 0;
		if (a1=='5' && a2=='5' && a3=='0' && a4=='2' && a5=='A' && a6=='A') {
			data.append(Integer.parseInt(string.substring(6,8), 16));
			int z = Integer.parseInt(string.substring(6,8), 16);
			data.append(Integer.parseInt(string.substring(9,11), 16));
			int x = Integer.parseInt(string.substring(9,11), 16);
			Log.d("yuhao", "data.toString()=--------"+data.toString());
			a = z+x/10.0;
			Log.d("yuhao", "a=-----------------"+a);
			return a+"";
		}
		else {
//			Toast.makeText(MainActivity.this, "查询失败,请重新查询！", Toast.LENGTH_SHORT).show();
			return "0";
		}
		
	}
	
	private String getTempValue(String string) {
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
		double a = 0;
		if (a1=='5' && a2=='5' && a3=='0' && a4=='2' && a5=='A' && a6=='A') {
			data.append(string.substring(6,8));
			data.append(string.substring(9,11));
			Log.d("yuhao", "data.toString()=--------"+data.toString());
			a = Integer.parseInt(data.toString())/100.0;
		}
		else {
//			Toast.makeText(MainActivity.this, "查询失败,请重新查询！", Toast.LENGTH_SHORT).show();
		}
		Log.d("yuhao", "a=-----------------"+a);
		return a+"";
	}
	//查询体重
	public String searchWeight(){
		Log.d("yuhao", "searchWeight--------------------");
		getSendBuf(Define.SEARCH_WEIGHT);
        onSendBtnClicked();
        Log.d("yuhao", "weight=----"+mWeightData.toString());
        String weightValue = "";
        weightValue = getWeightValue(mWeightData.toString());
		return weightValue;
	}
	//电源控制
	public void powerControl(){
		Log.d("yuhao", "powerControl--------------------");
		getSendBuf(Define.POWER_CONTROL);
        onSendBtnClicked();
	}
	//儿童清洗
	public void childrenClean(){
		Log.d("yuhao", "childrenClean--------------------");
		getSendBuf(Define.CHILDREN_CLEAN);
        onSendBtnClicked();
	}
	//坐浴
	public void hipBath(){
		Log.d("yuhao", "hipBath--------------------");
		getSendBuf(Define.HIP_BATH);
        onSendBtnClicked();
	}
	//按摩
	public void hipMassage(){
		Log.d("yuhao", "hipMassage--------------------");
		getSendBuf(Define.HIP_MASSAGE);
        onSendBtnClicked();
	}
	//自动烘干开
	public void autoDryOpen(){
		Log.d("yuhao", "autoDryOpen--------------------");
		getSendBuf(Define.AUTO_DRY_OPEN);
        onSendBtnClicked();
	}
	//自动烘干关
	public void autoDryClose(){
		Log.d("yuhao", "autoDryClose--------------------");
		getSendBuf(Define.AUTO_DRY_CLOSE);
        onSendBtnClicked();
	}
	//自动冲水开
	public void autoFlushOpen(){
		Log.d("yuhao", "autoFlushOpen--------------------");
		getSendBuf(Define.AUTO_FLUSH_OPEN);
        onSendBtnClicked();
	}
	//自动冲水关
	public void autoFlushClose(){
		Log.d("yuhao", "autoFlushClose--------------------");
		getSendBuf(Define.AUTO_FLUSH_CLOSE);
        onSendBtnClicked();
	}
	//自动翻盖开
	public void autoCoverOpen(){
		Log.d("yuhao", "autoCoverOpen--------------------");
		getSendBuf(Define.AUTO_COVER_OPEN);
        onSendBtnClicked();
	}
	//自动翻盖关
	public void autoCoverClose(){
		Log.d("yuhao", "autoCoverClose--------------------");
		getSendBuf(Define.AUTO_COVER_CLOSE);
        onSendBtnClicked();
	}
	//省电开
	public void autoPowerOpen(){
		Log.d("yuhao", "autoPowerOpen--------------------");
		getSendBuf(Define.AUTO_POWER_OPEN);
        onSendBtnClicked();
	}
	//省电关
	public void autoPowerClose(){
		Log.d("yuhao", "autoPowerClose--------------------");
		getSendBuf(Define.AUTO_POWER_CLOSE);
        onSendBtnClicked();
	}
	//自动除臭开
	public void autoSmellOpen(){
		Log.d("yuhao", "autoSmellOpen--------------------");
		getSendBuf(Define.AUTO_SMELL_OPEN);
        onSendBtnClicked();
	}
	//自动除臭关
	public void autoSmellClose(){
		Log.d("yuhao", "autoSmellClose--------------------");
		getSendBuf(Define.AUTO_SMELL_CLOSE);
        onSendBtnClicked();
	}
}
