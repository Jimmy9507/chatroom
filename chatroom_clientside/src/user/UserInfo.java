package user;
import java.util.ArrayList;
/**
 *  这是一个记录了用户的名称，IP地址，好友列表的类
 * @author jimmy
 *
 */
public class UserInfo {
         String username;
	  String nickname;
	  String ip;
	   int level;
	   ArrayList<FriendInfo> friendsInfos;
	   
	   public UserInfo()
	   {
		   
	   }
	   public UserInfo(String nickname,int level,ArrayList<FriendInfo> friendsInfos)
	   {  
		 this.nickname=nickname;
		 this.level=level;
		 this.friendsInfos=friendsInfos;
	   }
	   //不能随意调用
	   public  void setUserNickname(String nickname)
	   {
		   this.nickname=nickname;
	   }
	   public void setUserLevel(int level)
	   {
		   this.level=level;
	   }
	   public String getUserNickname()
	   {
		   return nickname;
	   }
	   public int getUserLevel()
	   {
		   return level;
	   }
	   public void setUserFriendInfo(ArrayList<FriendInfo> friendsInfos)
	   {    
		   this.friendsInfos=friendsInfos;
		   
	   }
	   public ArrayList<FriendInfo> getUserFriendInfo()
	   {
		   return friendsInfos;
	   }
	   public String getIP()
	   {
		   return ip;
	   }
	   public void setIP(String ip)
	   {
		   this.ip=ip;
	   }
	   public void setUsername(String username)
	   {
		   this.username=username;
	   }
	   public String getUsername()
	   {
		   return username;
	   }
	   
	
	
}
