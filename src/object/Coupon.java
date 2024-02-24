package object;

public class Coupon {
	private String id;
	private String fbID;
	private String accessToken;
	private String cpNo;
	private String img_path;
	private String cp_type;
	private String cp_value;
	private String cp_expired;
	private String cp_phone;
	private String cp_birthday;
	private String cp_gender;
	private String cp_Email;
	public Coupon(String img_path,String cp_type,String cp_value, String cp_expired)
	{
		super();
		this.cp_expired = cp_expired;
		this.img_path= img_path;
		this.cp_type= cp_type;
		this.cp_value= cp_value;
		
	}
	public Coupon()
	{
		super();
		
	}
	public String getImg_path() {
		return img_path;
	}
	public void setImg_path(String img_path) {
		this.img_path = img_path;
	}
	public String getCp_type() {
		return cp_type;
	}
	public void setCp_type(String cp_type) {
		this.cp_type = cp_type;
	}
	public String getCp_value() {
		return cp_value;
	}
	public void setCp_value(String cp_value) {
		this.cp_value = cp_value;
	}
	public String getCp_expired() {
		return cp_expired;
	}
	public void setCp_expired(String cp_expired) {
		this.cp_expired = cp_expired;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCpNo() {
		return cpNo;
	}
	public void setCpNo(String cpNo) {
		this.cpNo = cpNo;
	}
	public String getCp_phone() {
		return cp_phone;
	}
	public void setCp_phone(String cp_phone) {
		this.cp_phone = cp_phone;
	}
	public String getCp_birthday() {
		return cp_birthday;
	}
	public void setCp_birthday(String cp_birthday) {
		this.cp_birthday = cp_birthday;
	}
	public String getCp_gender() {
		return cp_gender;
	}
	public void setCp_gender(String cp_gender) {
		this.cp_gender = cp_gender;
	}
	public String getFbID() {
		return fbID;
	}
	public void setFbID(String fbID) {
		this.fbID = fbID;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getCp_Email() {
		return cp_Email;
	}
	public void setCp_Email(String cp_Email) {
		this.cp_Email = cp_Email;
	}
}
