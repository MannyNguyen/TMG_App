package object;

public class InviteFB {
	private boolean check = false;
	private String fb_id = "";
	private String fb_name = "";
	private String img_avarta= "";
	public InviteFB()
	{
		super();
	}
	public String getFb_id() {
		return fb_id;
	}
	public void setFb_id(String fb_id) {
		this.fb_id = fb_id;
	}
	public String getFb_name() {
		return fb_name;
	}
	public void setFb_name(String fb_name) {
		this.fb_name = fb_name;
	}
	public String getImg_avarta() {
		return img_avarta;
	}
	public void setImg_avarta(String img_avarta) {
		this.img_avarta = img_avarta;
	}
	public boolean isCheck() {
		return check;
	}
	public void setCheck(boolean check) {
		this.check = check;
	}
}
	