package clients;
import java.util.Map;
import java.util.HashMap;
import server.ChatroomClientThread;
import server.OnlineUpdateClientThread;
import server.FriendManageClientThread;
import server.SingleChatClientThread;
/**
 * 一个数据结构，其中包含用户的进程，用户名，等级，IP地址
 * @author jimmy
 *
 */
public class Client {
	ChatroomClientThread clientThread;
	OnlineUpdateClientThread onlineUpdateClientThread;
	FriendManageClientThread friendManageClientThread;
	
	//关于私聊对象的一个MAP映射
	public Map<String,SingleChatClientThread>otherSideThreadMap;
	String username;
	int level;
	String ipAddress;
	public Client()
	{   
		otherSideThreadMap=new HashMap<String,SingleChatClientThread>();
	}
	public void setUsername(String username)
	{
		this.username=username;
	}
	public void setUserLevel(int level)
	{
		this.level=level;
	}
	public void setIPAddress(String ipAddress)
	{
		this.ipAddress=ipAddress;
	}
	public void setClientThread(ChatroomClientThread clientThread)
	{
		this.clientThread=clientThread;
	}
	public String getUsername()
	{
		return username;
	}
	public int  getUserLevel()
	{
		return level;
	}
	public String getIPAddress()
	{
		return ipAddress;
	}
	public ChatroomClientThread getClientThread()
	{
		return  clientThread;
	}
	public void setOUClientThread(OnlineUpdateClientThread onlineUpdateClientThread)
	
	{
	  this.	onlineUpdateClientThread=onlineUpdateClientThread;
	}
	public OnlineUpdateClientThread getOUClientThread()
	{
		return  onlineUpdateClientThread;
	}
public void setFriendManageClientThread(FriendManageClientThread friendManageClientThread)
	
	{
	  this.	friendManageClientThread=friendManageClientThread;
	}
	public FriendManageClientThread getFriendManageClientThread()
	{
		return  friendManageClientThread;
	}
	public Map<String,SingleChatClientThread> getOtherSideThreadMap()
	{
		return otherSideThreadMap;
	}
	public void setOtherSideThreadMap(Map<String,SingleChatClientThread> otherSideThreadMap)
	{
		this.otherSideThreadMap=otherSideThreadMap;
	}
	
}
