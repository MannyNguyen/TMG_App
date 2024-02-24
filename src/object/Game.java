package object;

public class Game {
	private String GameID = "GameID";
	private String Win = "Win";
	private String ID = "ID";
	private String CpValue = "CpValue";
	private String Credits = "Credits";
	private String Total_wins = "Total_wins";
	private String ThumbImg = "ThumbImg";
	public Game(String GameID ,String Win,String ID,String CpValue,String Credits,String Total_wins)
	{
		super();
		this.setGameID(GameID);
		this.setWin(Win);
		this.setID(ID);
		this.setCpValue(CpValue);
		this.setCredits(Credits);
		this.setTotal_wins(Total_wins);
	}
	public Game()
	{
		super();
	}
	public String getGameID() {
		return GameID;
	}
	public void setGameID(String gameID) {
		GameID = gameID;
	}
	public String getWin() {
		return Win;
	}
	public void setWin(String win) {
		Win = win;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getCpValue() {
		return CpValue;
	}
	public void setCpValue(String cpValue) {
		CpValue = cpValue;
	}
	public String getCredits() {
		return Credits;
	}
	public void setCredits(String credits) {
		Credits = credits;
	}
	public String getTotal_wins() {
		return Total_wins;
	}
	public void setTotal_wins(String total_wins) {
		Total_wins = total_wins;
	}
	public String getThumbImg() {
		return ThumbImg;
	}
	public void setThumbImg(String thumbImg) {
		ThumbImg = thumbImg;
	}
}
