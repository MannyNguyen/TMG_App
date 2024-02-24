package object;

public class Stores {
	private String name;
	private String addr;
	private double distance;
	private String logo;
	public Stores(String name, String addr, double distance, String logo) {
		super();
		this.name = name;
		this.addr = addr;
		this.distance = distance;
		this.logo = logo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	
}
