package object;

public class Register {
	private String Faceid;
	private String name;
	private String img_path;
	private String email;
	private String phone;
	private String gender;
	private String birthday;
	private String deviceID;
	private String system;
	private String token;
	private String accesstoken;
	public Register(String id,String img_path,String name,String email, String phone,String gender,String birthday,String deviceId
			,String system,String token,String accesstoken)
	{
		super();
		this.setFaceid(id);
		this.email= email;
		this.phone= phone;
		this.gender= gender;
		this.birthday = birthday;
		this.deviceID= deviceId;
		this.system= system;
		this.token= token;
		this.name = name;
		this.img_path= img_path;
		this.accesstoken= accesstoken;
		
	}
	public Register()
	{
		super();
		
	}
	public String getImg_path() {
		return img_path;
	}
	public void setImg_path(String img_path) {
		this.img_path = img_path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getAccesstoken() {
		return accesstoken;
	}
	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}
	public String getFaceid() {
		return Faceid;
	}
	public void setFaceid(String faceid) {
		Faceid = faceid;
	}
}
