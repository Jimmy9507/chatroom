package client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import user.UserInfo;
import java.util.ArrayList;
//引入JSON包
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class OnlineUpdateClientThread extends Thread{
   Socket socket;
   final static int PORT=10746;
   DataInputStream in;
   DataOutputStream out;
   String sendUserInfo;
   ArrayList<UserInfo> usersInfos;
   UserInfo userInfo;
   private Object threadControl;
   private boolean suspend;
   public  OnlineUpdateClientThread(UserInfo Info)
   {  usersInfos=new ArrayList<UserInfo>();
   userInfo=Info;
	   try{
   
	   socket=new Socket("112.74.19.32",PORT);
	   in=new DataInputStream(socket.getInputStream());
	    out=new DataOutputStream(socket.getOutputStream());
	    
    }
  
    catch(UnknownHostException e)
    {
    	e.printStackTrace();
    }
    catch(IOException e)
    {
 	   e.printStackTrace();
    }
 
  //获取本地ip
    try{
    String ip=String.valueOf(socket.getInetAddress().getLocalHost());
    userInfo.setIP(ip);
    }
    catch(UnknownHostException e)
    {e.printStackTrace();}
    //利用JSON传输账户的名字，等级和IP地址
	JSONObject userObject=new JSONObject();
	
	try{
	userObject.put("username",userInfo.getUserNickname() );
	userObject.put("level",userInfo.getUserLevel());
	userObject.put("ip",userInfo.getIP());
	sendUserInfo=userObject.toString();
	}
	catch(JSONException e){e.printStackTrace();}
	try{
	out.writeUTF(sendUserInfo);
	}
	catch(IOException e)
	{ e.printStackTrace();
   }
	//控制线程是否挂起的布尔值
	suspend=false;
	//创建线程控制对象
	threadControl=new Object();
   
	
}
   public void run()
   {
	  
	   
		   while (true) 
		   {
			   try{
				   sleep(300);
				   }
				   catch(InterruptedException e)
				   {
					   e.printStackTrace();
				   }
			   
			   synchronized (threadControl) {
	                if (suspend) {
	                    try {
	                        threadControl.wait();
	                    } catch (InterruptedException e) {
	                        e.printStackTrace();
	                    }
	                }
	            }
		 try{  //接收用户信息
		   String receiveUsersInfos=in.readUTF();
		   JSONObject json=new JSONObject(receiveUsersInfos);
		   JSONArray usersInfosArray=json.getJSONArray("users");
		   //同步一下usersInfos
		   synchronized(usersInfos)
		   {
		   usersInfos.clear();
		   for(int i=0;i<usersInfosArray.length();i++)
		   {  //获取从服务器端传来的信息
			   JSONObject userInfoObject=(JSONObject)usersInfosArray.get(i);
			   String username =(String)userInfoObject.get("username");
			   int level=Integer.parseInt(String.valueOf(userInfoObject.get("level")));
			  
			   String ip=(String)userInfoObject.get("ip");
			   UserInfo userInfo=new UserInfo();
			   userInfo.setUserNickname(username);
			   userInfo.setUserLevel(level);
			   userInfo.setIP(ip);
			   usersInfos.add(userInfo);
		   }
		   }
		 }
		 catch(IOException e)
		 {e.printStackTrace();
		 try{  
		 in.close();
		 out.close();
		 if(!socket.isClosed())
		  socket.close();
		  socket=null;
		 }
		 catch(IOException e1)
		 {
			 e1.printStackTrace();
		 }
		 }
		 catch(JSONException e)
		 {e.printStackTrace();
		 }
		 if(socket!=null)
		   updateOnlineFriendTable(usersInfos);	  		   
	   }
		
   }
   public void finalize()
   { 
   	try{
   		if(socket!=null)
   		{	socket.close();
   		in.close();
   		out.close();}
   	}	
   catch(IOException e)
   {  e.printStackTrace();}
   socket=null;
   in=null;
   out=null;
   }
   
   
   //设置线程挂起
   public void setSuspend(boolean suspend)
   {  //唤醒
	   if(!suspend)
	   { 
		   try{
			   socket=new Socket("localhost",PORT);
			   in=new DataInputStream(socket.getInputStream());
			    out=new DataOutputStream(socket.getOutputStream());
			    JSONObject userObject=new JSONObject();
				//使用JSON再次传用户信息过去
			    System.out.println(userInfo.getUserNickname());
				userObject.put("username",userInfo.getUserNickname() );
				userObject.put("level",userInfo.getUserLevel());
				userObject.put("ip",userInfo.getIP());
				sendUserInfo=userObject.toString();	
				out.writeUTF(sendUserInfo);	
		   }
		   catch(IOException e)
		   {
			   e.printStackTrace();
		   }catch(JSONException e){e.printStackTrace();}
			
		          
		   synchronized(threadControl)
	      { threadControl.notifyAll();}
	
	   }
	   else 
		   {try{socket.close();
		   in.close();
		   out.close();
		   }
		   catch(IOException e){e.printStackTrace();}
		   in=null;
		   out=null;
		   socket=null;
		   }
		   
	   this.suspend=suspend;
	   
		   
   }
   void updateOnlineFriendTable(ArrayList<UserInfo> usersInfos)
   {  //清空表格列表
	   Client.onlineTableModel.setRowCount(0);
	   for(int i=0;i<usersInfos.size();i++)
	   {
		   UserInfo userInfo=usersInfos.get(i);
		   String username=userInfo.getUserNickname();
		   int level=userInfo.getUserLevel();
		   Client.onlineTableModel.addRow(new Object[]{username,level});
	   }
	 
	   
   }
   boolean  selectOnlineFriend(String userNickname)
   {    boolean isExist=false;
	  //同步
   synchronized(usersInfos)
	   {
	   for(int i=0;i<usersInfos.size();i++)
	     if(userNickname.equals(usersInfos.get(i).getUserNickname()))
	     {	 isExist=true;
         break;}
	   }
   return isExist;
	   
   }
}
