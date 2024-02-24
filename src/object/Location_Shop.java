package object;

public class Location_Shop {

	private int mID, mBrandID, mCityID, mValid;
	double mDistance,mGeoX, mGeoY;
	private String mBrandName, mStoreName, mAddress, mPhone, mCityName, 
			mLogo;
	public Location_Shop(int mID, int mBrandID, int mCityID, int mValid,
			double mDistance, double mGeoX, double mGeoY, String mBrandName,
			String mStoreName, String mAddress, String mPhone,
			String mCityName, String mLogo) {
		super();
		this.mID = mID;
		this.mBrandID = mBrandID;
		this.mCityID = mCityID;
		this.mValid = mValid;
		this.mDistance = mDistance;
		this.mGeoX = mGeoX;
		this.mGeoY = mGeoY;
		this.mBrandName = mBrandName;
		this.mStoreName = mStoreName;
		this.mAddress = mAddress;
		this.mPhone = mPhone;
		this.mCityName = mCityName;
		this.mLogo = mLogo;
	}
	public int getmID() {
		return mID;
	}
	public void setmID(int mID) {
		this.mID = mID;
	}
	public int getmBrandID() {
		return mBrandID;
	}
	public void setmBrandID(int mBrandID) {
		this.mBrandID = mBrandID;
	}
	public int getmCityID() {
		return mCityID;
	}
	public void setmCityID(int mCityID) {
		this.mCityID = mCityID;
	}
	public int getmValid() {
		return mValid;
	}
	public void setmValid(int mValid) {
		this.mValid = mValid;
	}
	public double getmDistance() {
		return mDistance;
	}
	public void setmDistance(double mDistance) {
		this.mDistance = mDistance;
	}
	public double getmGeoX() {
		return mGeoX;
	}
	public void setmGeoX(double mGeoX) {
		this.mGeoX = mGeoX;
	}
	public double getmGeoY() {
		return mGeoY;
	}
	public void setmGeoY(double mGeoY) {
		this.mGeoY = mGeoY;
	}
	public String getmBrandName() {
		return mBrandName;
	}
	public void setmBrandName(String mBrandName) {
		this.mBrandName = mBrandName;
	}
	public String getmStoreName() {
		return mStoreName;
	}
	public void setmStoreName(String mStoreName) {
		this.mStoreName = mStoreName;
	}
	public String getmAddress() {
		return mAddress;
	}
	public void setmAddress(String mAddress) {
		this.mAddress = mAddress;
	}
	public String getmPhone() {
		return mPhone;
	}
	public void setmPhone(String mPhone) {
		this.mPhone = mPhone;
	}
	public String getmCityName() {
		return mCityName;
	}
	public void setmCityName(String mCityName) {
		this.mCityName = mCityName;
	}
	public String getmLogo() {
		return mLogo;
	}
	public void setmLogo(String mLogo) {
		this.mLogo = mLogo;
	}
	
	
	
	
}
