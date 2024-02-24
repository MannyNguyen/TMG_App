package prosoft.android.utility;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.android.activity.RegisterActivity;
import com.ctyprosoft.tmg.FlashActivity;

public class API {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	static String token = RegisterActivity.mRegId;
	static String currentDateandTime = sdf.format(new Date());
	public static String http = "http://tmg.ctyprosoft.com:81/api/";
	public static String register = "account/register.aspx";
	public static String cplist = "Coupon/GetCouponlist.aspx";
	public static String cpDetail = "Coupon/GetCouponDetail.aspx";
	public static String cpMemCard = "account/GetAccountDetail.aspx";
	public static String login = "account/login.aspx";
	public static String loginFB = "account/loginfb.aspx";
	public static String checkFbAcc = "account/checkloginfb.aspx";
	public static String newListBrandById = "News/GetNewsList.aspx";
	public static String infoGame = "Games/SpinSlotGame.aspx";
	public static String Voucher = "Games/WinSlotGame.aspx";
	public static String DeleteVoucher = "Games/GetDelete.aspx";
	public static String GetBrandList = http + "Brand/GetBrandList.aspx";
	public static String cpListUsed = "History/GetUsedDate.aspx";
	public static String cpListExpration = "History/GetExpiration.aspx";
	public static String purchaseList = "PurchaseLog/GetPurchase.aspx";
	public static String notifiList = "PushLog/PushNotiﬁcation.aspx";
	public static String friendList = "FB/GetFBFriendList.aspx";
	public static String updatefbID = "Account/GetUpdateFBID.aspx";
	public static String updateFriendInvited = "FB/InviteFriend.aspx";
	public static String updateCLickedNotification = "http://tmg.ctyprosoft.com:82/push/Android/?id=";
	
	public static String registerString(String name, String email,
			String phone, String gender, String birthday, String deviceid,
			String system, String token) {
		String string = http + register + "?name=" + URLEncoder.encode(name) + "&email=" + URLEncoder.encode(email)
				+ "&phone=" + URLEncoder.encode(phone) + "&gender=" + gender + "&birthday="
				+ URLEncoder.encode(birthday) + "&deviceid=" + deviceid + "&system=" + system
				+ "&token=" + token;
		return string;
	}

	public static String getCPListString(String email,String page) {
		String string = http + cplist + "?email=" + email + "&page=" +page;
		return string;
	}
	public static String getCPListUsed(String email,String page) {
		String string = http + cpListUsed + "?email=" + email + "&page="+page;
		return string;
	}
	public static String getCPListExpration(String email,String page) {
		String string = http + cpListExpration + "?email=" + email+ "&page="+page;
		return string;
	}
	public static String getCPDetailString(String id) {
		String string = http + cpDetail + "?id=" + id;
		return string;
	}
	public static String getMemCard(String email) {
		String string = http + cpMemCard + "?email=" + email;
		return string;
	}
	public static String checkLogin(String email,String phone,String deviceId,String mToken) {
		String string = http + login + "?email=" + email + "&phone=" + phone+ "&DeviceId="+ deviceId +"&token=" + mToken;
		return string;
	}
	public static String checkFBAccount(String faceID,String deviceId,String mToken) {
		String string = http + checkFbAcc + "?IDFB=" + faceID + "&deviceid="+ deviceId +"&token=" + mToken ;
		return string;
	}
	public static String getNewListBandById(String page,String brandid) {
		String string = http + newListBrandById + "?page=" + page + "&brandid=" + brandid ;
		return string;
	}
	public static String getInfoGame(String email) {
		String string = http + infoGame + "?email=" + email  ;
		return string;
	}
	public static String getVoucher(String id,String gameid,String email) {
		String string = http + Voucher + "?id=" + id + "&gameid=" + gameid + "&email=" + email  ;
		return string;
	}
	public static String deleteVoucher(String gameid,String email) {
		String string = http + DeleteVoucher+ "?gameid=" + gameid  + "&email=" + email  ;
		return string;
	}
	public static String getPCList(String cardno,String page) {
		String string = http + purchaseList+ "?cardno=" + cardno  + "&page=" + page  ;
		return string;
	}
	public static String getNotiList(String email,String page) {
		String string = http + notifiList+ "?email=" + email  + "&page=" + page  ;
		return string;
	}
	public static String getFBFriendList(String fbid,String access_token,String page) {
		String string = http + friendList+ "?fbid=" + fbid  + "&access_token=" + access_token
				+"&page="+page;
		return string;
	}
	public static String getupdatefbID(String email,String avatar) {
		String string = http + updatefbID+ "?email=" +email+ "&avatar=" +avatar;
		return string;
	}
	public static String getupdateFriendInvited(String fbid,String fbname,String email) {
		String string = http + updateFriendInvited+ "?fbid=" + fbid  + "&fbname=" +
				URLEncoder.encode(fbname)+ "&email=" +email;
		return string;
	}
	public static String checkLoginFB(String IDFB,String imgpath,String name,String email
			,String phone,String gender,String birthday,String deviceID,String system,String token) {
		String string = http + loginFB + "?IDFB=" + IDFB + "&imgpath=" + URLEncoder.encode(imgpath)+  "&name=" + URLEncoder.encode(name)
				+  "&email=" + URLEncoder.encode(email)
				+  "&phone=" + URLEncoder.encode(phone)+  "&gender=" +URLEncoder.encode(gender)+ 
				"&birthday=" +URLEncoder.encode(birthday)+  "&deviceID=" + URLEncoder.encode(deviceID)
				+  "&system=" + URLEncoder.encode(system)+  "&token=" + URLEncoder.encode(token);
		System.out.println("url="+string);
		return string;
	}
}