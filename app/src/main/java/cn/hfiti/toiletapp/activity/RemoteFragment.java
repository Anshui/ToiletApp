package cn.hfiti.toiletapp.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import cn.hfiti.toiletapp.R;

public class RemoteFragment extends Fragment {
	
	private MainActivity mActivity;
	private View view;
	private GridView mGridView;
	private TextView mTextSeatTemp;
	private TextView mTextWeight;
    private List<RemoteBean> list;
	private GridViewAdapter gridViewAdapter;
	
    public static final int RECEIVE_WEIGHT_DATA = 100;
    public static final int RECEIVE_WEIGHT_DATA_FAILED = 101;
    
	private List<Map<String, Object>> mDataList;
	private SimpleAdapter remoteAdapter;
	private int[] icon = {
			R.drawable.c_xb,
			R.drawable.c_fx,
			R.drawable.c_ptfw,
			R.drawable.c_hg,
			R.drawable.c_ydcs,
			R.drawable.c_ydcs,
			R.drawable.c_cs,
			R.drawable.c_fgdk,
			R.drawable.c_yd_off,
			R.drawable.cs,
			R.drawable.tz,
			R.drawable.search_temp,
			R.drawable.power,
			R.drawable.baby_icon,
			R.drawable.bath,
			R.drawable.massage,
			R.drawable.water_temp_green,
			R.drawable.wind_temp_green,
			R.drawable.water_press_green,
			R.drawable.seat_temp_green
	};
	private int[] icon_yd = {
			R.drawable.c_yd_on,
			R.drawable.c_yd_off
	};
	private int[] icon_wind_temp = {
			R.drawable.wind_temp,
			R.drawable.wind_temp_blue,
			R.drawable.wind_temp_green,
			R.drawable.wind_temp_red
	};
	private int[] icon_water_temp = {
			R.drawable.water_temp,
			R.drawable.water_temp_blue,
			R.drawable.water_temp_green,
			R.drawable.water_temp_red
	};
	private int[] icon_water_press = {
			R.drawable.water_press,
			R.drawable.water_press_blue,
			R.drawable.water_press_green,
			R.drawable.water_press_red
	};
	private int[] icon_seat_temp = {
			R.drawable.seat_temp,
			R.drawable.seat_temp_blue,
			R.drawable.seat_temp_green,
			R.drawable.seat_temp_red
	};
	private String[] iconName = {
			"ͨ臀部清洗",
			"女用清洗",
			"喷头自洁",
			"烘干",
			"喷头前移",
			"喷头后移",
			"冲水",
			"翻盖",
			"夜灯",
			"功能设置",
			"停止",
			"查询座温",
			"电源 ",
			"儿童清洗",
			"坐浴",
			"按摩",
			"水温",
			"风温",
			"水压",
			"座温"
	};
	private int waterTempFlag = 3;
	private int windTempFlag = 3;
	private int waterPressFlag = 3;
	private int seatTempFlag = 3;
	private boolean lightFlag = false;
	
	private String mWeightData;
	
	private Timer timer;
    private TimerTask task;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);		
	}

	private class GridViewAdapter extends BaseAdapter {
		
	    private LayoutInflater mInflater;
	    private Context mContext;
	    private List<RemoteBean> list;

	    public GridViewAdapter(Context context,List<RemoteBean> list) {
	    	mContext = context;
	    	mInflater = LayoutInflater.from(context);
	    	this.list = list;
	    }
	    
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return iconName.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return iconName[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Log.d("yuhao", "getView---------------------------");
			ItemViewTag viewTag;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.remote_item, null);
				// construct an item tag  
	            viewTag = new ItemViewTag((ImageView) convertView.findViewById(R.id.remote_image),
	            		(TextView) convertView.findViewById(R.id.remote_text));
	            convertView.setTag(viewTag);
			}
			else {
				viewTag = (ItemViewTag) convertView.getTag();
			}
			
			// set name
			viewTag.mName.setText(iconName[position]);
	        // set icon
	        viewTag.mIcon.setImageResource(icon[position]);
			Log.d("yuhao", "position=----------------------"+position);
			switch (position) {
			case 8:
				if (lightFlag) {
					Log.d("yuhao", "YD_STATE-----------------ON");
					viewTag.mIcon.setImageResource(icon_yd[0]);
				}
				else {
					viewTag.mIcon.setImageResource(icon_yd[1]);
				}
				break;
			case 16:
				Log.d("yuhao", "waterTempFlag=------------------"+waterTempFlag);
				switch (waterTempFlag) {
				case 1:
					viewTag.mIcon.setImageResource(icon_water_temp[0]);
					break;
				case 2:
					viewTag.mIcon.setImageResource(icon_water_temp[1]);
					break;
				case 3:
					viewTag.mIcon.setImageResource(icon_water_temp[2]);
					break;
				case 4:
					viewTag.mIcon.setImageResource(icon_water_temp[3]);
					break;
				default:
					break;
				}
				break;
			case 17:
				Log.d("yuhao", "windTempFlag=------------------"+windTempFlag);
				switch (windTempFlag) {
				case 1:
					viewTag.mIcon.setImageResource(icon_wind_temp[0]);
					break;
				case 2:
					viewTag.mIcon.setImageResource(icon_wind_temp[1]);
					break;
				case 3:
					viewTag.mIcon.setImageResource(icon_wind_temp[2]);
					break;
				case 4:
					viewTag.mIcon.setImageResource(icon_wind_temp[3]);
					break;

				default:
					break;
				}
				break;
			case 18:
				Log.d("yuhao", "waterPressFlag=------------------"+waterPressFlag);
				switch (waterPressFlag) {
				case 1:
					viewTag.mIcon.setImageResource(icon_water_press[0]);
					break;
				case 2:
					viewTag.mIcon.setImageResource(icon_water_press[1]);
					break;
				case 3:
					viewTag.mIcon.setImageResource(icon_water_press[2]);
					break;
				case 4:
					viewTag.mIcon.setImageResource(icon_water_press[3]);
					break;

				default:
					break;
				}
				break;
			case 19:
				Log.d("yuhao", "seatTempFlag=------------------"+seatTempFlag);
				switch (seatTempFlag) {
				case 1:
					viewTag.mIcon.setImageResource(icon_seat_temp[0]);
					break;
				case 2:
					viewTag.mIcon.setImageResource(icon_seat_temp[1]);
					break;
				case 3:
					viewTag.mIcon.setImageResource(icon_seat_temp[2]);
					break;
				case 4:
					viewTag.mIcon.setImageResource(icon_seat_temp[3]);
					break;

				default:
					break;
				}
				break;

			default:
				break;
			}
			return convertView;
		}
		
	}
	class ItemViewTag  
    {  
        protected ImageView mIcon;
        protected TextView mName; 
        public ItemViewTag(ImageView icon, TextView name)  
        {  
            this.mName = name;
            this.mIcon = icon;
        }  
    }
	private void init() {
		// TODO Auto-generated method stub
		Log.d("yuhao", "init----------RemoteFragment-----------------");
		mGridView = (GridView) view.findViewById(R.id.remote_grid);
		mTextSeatTemp = (TextView) view.findViewById(R.id.seat_temp_value);
		mTextWeight = (TextView) view.findViewById(R.id.weight_value);
		
		mDataList = new ArrayList<Map<String, Object>>();
		getData();
		String [] from ={"image","text"};
		int [] to = {R.id.remote_image,R.id.remote_text};
		remoteAdapter = new SimpleAdapter(getActivity(), mDataList, R.layout.remote_item, from, to);
//		mGridView.setAdapter(remoteAdapter);
		
		list = new ArrayList<RemoteBean>();
		gridViewAdapter = new GridViewAdapter(getActivity(), list);
		mGridView.setAdapter(gridViewAdapter);
				
	}

	class RemoteBean{
		String name;
		int redid;
	}
	
	public List<Map<String, Object>> getData(){
		for(int i=0;i<icon.length;i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", icon[i]);
			map.put("text", iconName[i]);
			mDataList.add(map);
		}
		return mDataList;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mActivity = (MainActivity) activity;
		mActivity.setHandler(mhandler);
	}
	
	

	private Handler mhandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RECEIVE_WEIGHT_DATA:
				mWeightData = (String) msg.obj;
				mTextWeight.setText(mWeightData);
//				mWeightData = null;
				break;
			case RECEIVE_WEIGHT_DATA_FAILED:
				Toast.makeText(mActivity, "查询失败,请重新查询！", Toast.LENGTH_SHORT).show();
			default:
				break;
			}
		};
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.d("yuhao", "onCreateView--------------------------");
		view = inflater.inflate(R.layout.fragment_remote, container, false);
		init();
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					mActivity.hipClean();
					break;
				case 1:
					mActivity.ladyClean();
					break;
				case 2:
					mActivity.nozzleClean();
					break;
				case 3:
					mActivity.dry();
					break;
				case 4:
					mActivity.nFront();
					break;
				case 5:
					mActivity.nBack();
					break;
				case 6:
					mActivity.flush();
					break;
				case 7:
					mActivity.cover();
					break;
				case 8:
					ImageView img = (ImageView)view.findViewById(R.id.remote_image);
					if (lightFlag == false) {
						mActivity.lightOn();
						lightFlag = true;
						img.setImageDrawable(getResources().getDrawable(R.drawable.c_yd_on));
					}
					else {
						mActivity.lightOff();
						lightFlag = false;
						img.setImageDrawable(getResources().getDrawable(R.drawable.c_yd_off));
					}
					break;
				case 9:
					Intent intent = new Intent(getActivity(),FunSettingsActivity.class);
					startActivity(intent);
					break;
				case 10:
					View v = mGridView.getChildAt(8);
					ImageView img1 = (ImageView)v.findViewById(R.id.remote_image);
					mActivity.stopAll();
					img1.setImageDrawable(getResources().getDrawable(R.drawable.c_yd_off));
					lightFlag = false;
					break;
				case 11:
					String seatTemp = mActivity.searchSeatTemp();
//					mTextSeatTemp.setText(seatTemp);
					break;
//				case 12:
//					mWeightData = mActivity.searchWeight();
//					Toast.makeText(mActivity, "正在查询，请稍后...", Toast.LENGTH_LONG).show();
//
//					break;
				case 12:
					mActivity.powerControl();
					break;
				case 13:
					mActivity.childrenClean();
					break;
				case 14:
					mActivity.hipBath();
					break;
				case 15:
					mActivity.hipMassage();
					break;
				case 16:
					ImageView img17 = (ImageView)view.findViewById(R.id.remote_image);
					if (waterTempFlag == 3) {
						img17.setImageDrawable(getResources().getDrawable(R.drawable.water_temp_red));
						waterTempFlag = 4;
						mActivity.setWaterTemp_4();
					}
					else if (waterTempFlag == 4) {
						img17.setImageDrawable(getResources().getDrawable(R.drawable.water_temp));
						waterTempFlag = 1;
						mActivity.setWaterTemp_1();
					}
					else if (waterTempFlag == 1) {
						img17.setImageDrawable(getResources().getDrawable(R.drawable.water_temp_blue));
						waterTempFlag = 2;
						mActivity.setWaterTemp_2();
					}
					else if (waterTempFlag == 2) {
						img17.setImageDrawable(getResources().getDrawable(R.drawable.water_temp_green));
						waterTempFlag = 3;
						mActivity.setWaterTemp_3();
					}
					
					break;
				case 17:
					ImageView img18 = (ImageView)view.findViewById(R.id.remote_image);
					if (windTempFlag == 3) {
						img18.setImageDrawable(getResources().getDrawable(R.drawable.wind_temp_red));
						windTempFlag = 4;
						mActivity.setDryTemp_4();
					}
					else if (windTempFlag == 4) {
						img18.setImageDrawable(getResources().getDrawable(R.drawable.wind_temp));
						windTempFlag = 1;
						mActivity.setDryTemp_1();
					}
					else if (windTempFlag == 1) {
						img18.setImageDrawable(getResources().getDrawable(R.drawable.wind_temp_blue));
						windTempFlag = 2;
						mActivity.setDryTemp_2();
					}
					else if (windTempFlag == 2) {
						img18.setImageDrawable(getResources().getDrawable(R.drawable.wind_temp_green));
						windTempFlag = 3;
						mActivity.setDryTemp_3();
					}
					break;
				case 18:
					ImageView img19 = (ImageView)view.findViewById(R.id.remote_image);
					if (waterPressFlag == 3) {
						img19.setImageDrawable(getResources().getDrawable(R.drawable.water_press_red));
						waterPressFlag = 4;
						mActivity.setWaterPress_4();
					}
					else if (waterPressFlag == 4) {
						img19.setImageDrawable(getResources().getDrawable(R.drawable.water_press));
						waterPressFlag = 1;
						mActivity.setWaterPress_1();
					}
					else if (waterPressFlag == 1) {
						img19.setImageDrawable(getResources().getDrawable(R.drawable.water_press_blue));
						waterPressFlag = 2;
						mActivity.setWaterPress_2();
					}
					else if (waterPressFlag == 2) {
						img19.setImageDrawable(getResources().getDrawable(R.drawable.water_press_green));
						waterPressFlag = 3;
						mActivity.setWaterPress_3();
					}
					break;
				case 19:
					ImageView img20 = (ImageView)view.findViewById(R.id.remote_image);
					if (seatTempFlag == 3) {
						img20.setImageDrawable(getResources().getDrawable(R.drawable.seat_temp_red));
						seatTempFlag = 4;
						mActivity.setSeatTemp_4();
					}
					else if (seatTempFlag == 4) {
						img20.setImageDrawable(getResources().getDrawable(R.drawable.seat_temp));
						seatTempFlag = 1;
						mActivity.setSeatTemp_1();
					}
					else if (seatTempFlag == 1) {
						img20.setImageDrawable(getResources().getDrawable(R.drawable.seat_temp_blue));
						seatTempFlag = 2;
						mActivity.setSeatTemp_2();
					}
					else if (seatTempFlag == 2) {
						img20.setImageDrawable(getResources().getDrawable(R.drawable.seat_temp_green));
						seatTempFlag = 3;
						mActivity.setSeatTemp_3();
					}
					break;

				default:
					break;
				}
			}
		});
		return view;
	}

}
