package cn.hfiti.toiletapp.entity;

public class UserInfo {
	
//	public int _id;
	public String userIdName;
	public String userName;
	public String userPwd;
	public String userSex;
	public String userBrithday;
	public float userHeight;
	public float userWeight;
	public String userImage;
	private String time;
	
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
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public String getUserSex() {
		return userSex;
	}
	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}
	public float getUserHeight() {
		return userHeight;
	}
	public void setUserHeight(float userHeight) {
		this.userHeight = userHeight;
	}
	public float getUserWeight() {
		return userWeight;
	}
	public void setUserWeight(float userWeight) {
		this.userWeight = userWeight;
	}
	public String getUserBrithday() {
		return userBrithday;
	}
	public void setUserBrithday(String userBrithday) {
		this.userBrithday = userBrithday;
	}
	public String getUserImage() {
		return userImage;
	}
	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}
	public UserInfo(String userIdName, String userPwd, String userName, String userSex, int userHeight,
			float userWeight, String userBrithday) {
		super();
//		this._id = _id;
		this.userIdName = userIdName;
		this.userName = userName;
		this.userPwd = userPwd;
		this.userSex = userSex;
		this.userBrithday = userBrithday;
		this.userHeight = userHeight;
		this.userWeight = userWeight;
//		this.userImage = userImage;
	}
	public UserInfo() {
		super();
	}
}
