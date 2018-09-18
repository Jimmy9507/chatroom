package friendManage;
import java.net.Socket;
import java.util.List;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import user.FriendInfo;
import user.UserInfo;
import javax.swing.JOptionPane;
import client.Client;

import org.json.JSONException;
import org.json.JSONObject;
public class FriendManageThread extends Thread
{    static final int PORT=10747;
	     Socket socket;
	     DataInputStream in;
	     DataOutputStream out;
	     Client client;
	     FriendManageConnector friendManageConnector;
	     Object threadControl;
	     boolean suspend;
	     public FriendManageThread(Client client)
	      {  try{
	    	 socket=new Socket("112.74.19.32",PORT);
	    	 in=new DataInputStream(socket.getInputStream());
	    	 out=new DataOutputStream(socket.getOutputStream());
	      }	
	    	 catch(IOException e)
	    	 {e.printStackTrace();}
	    	 this.client=client;
	    	 friendManageConnector=new FriendManageConnector(client.userInfo.getUsername());
	    threadControl=new Object();
	    suspend=false;
	      }
	     public void run()
	     {    //接收好友发来的添加好友请求
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
	    		 try{
	    			 String receiveStr=in.readUTF();
	    			 //解析JSON
	    			 JSONObject jsonReceive=new JSONObject(receiveStr);
	    			 String operation=jsonReceive.getString("information");
	    			 //请求加你为好友
	    			 if(operation.equals("addRequest"))
	    			 {   String userNickname=jsonReceive.getString("requesterNickname");
	    				 int i=JOptionPane.showConfirmDialog(client, userNickname+"请求加你为好友，是否同意?","添加好友",JOptionPane.YES_NO_OPTION);
	    				 if(i==0)
	    					 {     int level=jsonReceive.getInt("level");
	    					   //添加对方进自己的数据库表
	    					 //测试代码
	    					 System.out.println(userNickname);
	    					   if(friendManageConnector.addFriendToDatabase(userNickname, level))
	    					   {
	    			            //同意，并把把自己有关信息发送过去
	    					     JSONObject  jsonSendTrue=new JSONObject();
	    					     String myInformation="addResponseTrue";
	    					     String myNickname=client.userInfo.getUserNickname();
	    					     int myLevel=client.userInfo.getUserLevel();
	    					     jsonSendTrue.put("information", myInformation);
	    					     jsonSendTrue.put("requesterNickname", userNickname);
	    					     jsonSendTrue.put("responserNickname", myNickname);
	    					     jsonSendTrue.put("level", myLevel);
	    					     String jsonSendStr=jsonSendTrue.toString();
	    					     
	    					     out.writeUTF(jsonSendStr);
	    					     
	    					     JOptionPane.showMessageDialog(client, "添加好友成功！");
	    					   FriendInfo friendInfo=new FriendInfo(userNickname,level);
	    					   client.userInfo.getUserFriendInfo().add(friendInfo);
	    					   client.friendListTableModel.addRow(new Object[]{userNickname,level});
	    					   }
	    					 } 
	    				        //不同意对方的请求
	    					   else
	    					   {  JSONObject  jsonSendFalse=new JSONObject();
	    					       String myInformation="addResponseFalse";
	    					       jsonSendFalse.put("information", myInformation);
	    					       jsonSendFalse.put("requesterNickname",userNickname);
	    					       String jsonSendStr=jsonSendFalse.toString();
	    					       out.writeUTF(jsonSendStr);
	    						   JOptionPane.showMessageDialog(client, "添加好友失败");
	    					   }
	    					 									 
	    			 }
	    			 //你请求别人加你为好友的回复
	    			 if(operation.equals("addResponseTrue"))
	    			 {   String username=jsonReceive.getString("responserNickname");
	    				int level=jsonReceive.getInt("level");
	    					   if(friendManageConnector.addFriendToDatabase(username, level))
	    					   {JOptionPane.showMessageDialog(client, "添加好友成功！");
	    					   FriendInfo friendInfo=new FriendInfo(username,level);
	    					   client.userInfo.getUserFriendInfo().add(friendInfo);
	    					   client.friendListTableModel.addRow(new Object[]{username,level});
	    					   }
	    			 }
	    		     if(operation.equals("addResponseFalse")) 
	    						   JOptionPane.showMessageDialog(client, "添加好友失败");
	    			//接收私聊请求		 								 
	    			 if(operation.equals("chatRequest"))
	    			 {
	    				 String username=jsonReceive.getString("requesterNickname");
	    				 
	    				int i=JOptionPane.showConfirmDialog(client,username+"想和你进行聊天，是否同意？","聊天",JOptionPane.YES_NO_OPTION);
	    				  //同意 
	    				if(i==0)
	    				 {  String information="chatResponseTrue";
	    					JSONObject jsonSend=new JSONObject();
	    					jsonSend.put("information",information);
	    					jsonSend.put("requesterNickname", username);
	    					jsonSend.put("responserNickname",Client.userInfo.getUserNickname());
	    					jsonSend.toString();
	    					String jsonSendStr=jsonSend.toString();
	    					out.writeUTF(jsonSendStr);
	    					//创建聊天框
	    					SingleChatDialog singleChatDialog=new SingleChatDialog(Client.userInfo.getUserNickname(),username);
	    					
	    				 }//不同意
	    				else
	    				{
	    					String information="chatResponseFalse";
	    					JSONObject jsonSend=new JSONObject();
	    					jsonSend.put("information", information);
	    					jsonSend.put("requesterNickname",username);
	    					String jsonSendStr=jsonSend.toString();
	    					out.writeUTF(jsonSendStr);
	    				}
	    				 
	    			 }
	    			 //接收回复
	    			 if(operation.equals("chatResponseTrue"))
	    			 {    //获得对方名字
	    				   String username= jsonReceive.getString("responserNickname");
	    				
	    				 SingleChatDialog singleChatDialog=new SingleChatDialog(Client.userInfo.getUserNickname(),username);
	    				
	    				 JOptionPane.showMessageDialog(client,"聊天请求成功");
	    			 }
	    			 if(operation.equals("chatResponseFalse"))
	    			 {
	    				 JOptionPane.showMessageDialog(client,"聊天请求失败");
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
	    				 }}
	    		  catch(JSONException e)
	    		  {e.printStackTrace();}
	    	 }
	    		 
	     }
	  
	 public    boolean removeFriend(String userNicknameToDel)
	     {   
	    	 if(friendManageConnector.removeFriendToDatabase(userNicknameToDel))
	    	 {   List<FriendInfo> friendsInfos=Client.userInfo.getUserFriendInfo();
	    		 for(int i=0;i<friendsInfos.size();i++)
	    		 {
	    			 FriendInfo friendInfo=friendsInfos.get(i);
	    			 if(friendInfo.getFriendName().equals(userNicknameToDel))
	    			 {
	    				 friendsInfos.remove(i);
	    			 }
	    		 }
	    		 return true;
	    	 }
	    		 else 
	    		 return false; 
	     }
	    public void sendAddFriendRequest(String userNicknameToAdd)
	     {  //传输自己的添加请求到对方处   
	    	JSONObject sendAddRequest=new JSONObject();
	    	String information="addRequest";
	    	String myNickname=client.userInfo.getUserNickname();
	    	int level=client.userInfo.getUserLevel();
	    	try{
	    	sendAddRequest.put("information",information);
	    	sendAddRequest.put("responserNickname", userNicknameToAdd);
	    	sendAddRequest.put("requesterNickname",myNickname);
	    	sendAddRequest.put("level", level);
	    	String sendAddRequestStr=sendAddRequest.toString();
	    	out.writeUTF(sendAddRequestStr);
	    	}
	    	catch(IOException e)
	    	{
	    		e.printStackTrace();
	    	}
	    	catch(JSONException e)
	    	{
	    		e.printStackTrace();
	    	}
	    	
	     }
	    
	    
	    
	    public void sendChatRequest(String userNicknameToChat)
	    {   
	    	 //传输自己的私聊请求到对方处   
	    	JSONObject sendChatRequest=new JSONObject();
	    	String information="chatRequest";
	    	String myNickname=client.userInfo.getUserNickname();
	    	int level=client.userInfo.getUserLevel();
	    	//测试代码
	    	System.out.println(myNickname);
	    	System.out.println(level);
	    	try{
	    	sendChatRequest.put("information",information);
	    	sendChatRequest.put("responserNickname", userNicknameToChat);
	    	sendChatRequest.put("requesterNickname",myNickname);
	    	sendChatRequest.put("level", level);
	    	String sendChatRequestStr=sendChatRequest.toString();
	    	out.writeUTF(sendChatRequestStr);
	    	}
	    	catch(IOException e)
	    	{
	    		e.printStackTrace();
	    	}
	    	catch(JSONException e)
	    	{
	    		e.printStackTrace();
	    	}
	    	
	    	
	    	
	    	
	    	
	    	
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
					
				   }
				   catch(IOException e)
				   {
					   e.printStackTrace();
				   }
				    
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
	

}
