package cn.hfiti.toiletapp.entity;

public class WeightInfo {
	
	public String userIdName;
	public String userName;
	public float userWeight;
	public String time;
	public long gTime;
	
	public String getUserIdName() {
		return userIdName;
	}
	public void setUserIdName(String userIdName) {
		this.userIdName = userIdName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public float getUserWeight() {
		return userWeight;
	}
	public void setUserWeight(float userWeight) {
		this.userWeight = userWeight;
	}
	public String getTime() {
		return time;
	}
	public void setCompleteTime(String time) {
		this.time = time;
	}
	
	public long getGTime(){
		return gTime;		
	}
	public void setGTime(long gTime){
		this.gTime = gTime;
	}
	
	public WeightInfo() {
		super();
	}

}
