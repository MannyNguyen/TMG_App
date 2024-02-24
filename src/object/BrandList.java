package object;


public class BrandList{
	/**
	 * 
	 */
	
	private int ID;
	private String Name;
	private String FBPage;
	private String Logo;

	public BrandList(int iD, String name, String fBPage, String logo) {
		super();
		ID = iD;
		Name = name;
		FBPage = fBPage;
		Logo = logo;
	}

	public BrandList()
	{
		super();
	}
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getFBPage() {
		return FBPage;
	}

	public void setFBPage(String fBPage) {
		FBPage = fBPage;
	}

	public String getLogo() {
		return Logo;
	}

	public void setLogo(String logo) {
		Logo = logo;
	}

}
