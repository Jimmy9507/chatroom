package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import clients.Client;

import org.json.JSONException;
import org.json.JSONObject;
public class FriendManageClientThread extends Thread{
      ServerThread serverThread;

       Socket socket;
      Client client;
    //设为公共的
      public DataInputStream in;
      public DataOutputStream out;
	public FriendManageClientThread(ServerThread serverThread,Socket socket,Client client)
	{
	    this.serverThread=serverThread;
		this.socket=socket;
		this.client=client;
		try{
		in=new DataInputStream(socket.getInputStream());
		out=new DataOutputStream(socket.getOutputStream());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		client.setFriendManageClientThread(this);

	}
	public void run()
	{
		while(true)
			{//判断信息是添加好友申请，还是添加好友回复，还是私聊请求
			try{    
			String strReceive=in.readUTF();
			
			    JSONObject jsonReceive=new JSONObject(strReceive);
			   String operation= jsonReceive.getString("information");
		     //添加请求，那必须找到响应者的线程
			   if(operation.equals("addRequest"))
		      {
		    	 String userNickname= jsonReceive.getString("responserNickname");
		    	 synchronized(serverThread.clients)
		    	 { for(int i=0;i<serverThread.clients.size();i++)
		    	 {
		    		 Client client=serverThread.clients.elementAt(i);
		    		 //如果和某个用户的昵称相同，向其线程发送信息
		    		 if(client.getUsername().equals(userNickname))
		    			 {//测试代码
		    			 System.out.println(client.getUsername().equals(userNickname));
		    			 client.getFriendManageClientThread().out.writeUTF(strReceive);
		    			 }
		    			 continue;
		    		   
		    	 }
		    	 }
		      }
			   //请求回复，那必须找到请求者的线程
		      if(operation.equals("addResponseTrue")||operation.equals("addResponseFalse"))
		      {
		    	  String userNickname= jsonReceive.getString("requesterNickname");
		    	  synchronized(serverThread.clients)
		    	  { for(int i=0;i<serverThread.clients.size();i++)
			    	 {
			    		 Client client=serverThread.clients.elementAt(i);
			    		 //如果
			    		 if(client.getUsername().equals(userNickname))
			    		 { 
			    			
			    				client.getFriendManageClientThread().out.writeUTF(strReceive);
			    				continue;
			    		 }
			    			 
			    		   
			    	 }
		    	  }
		      }
		      if(operation.equals("chatRequest"))
		      {
		    	 String userNickname= jsonReceive.getString("responserNickname");
		    	 //测试代码:
		    	 System.out.println(userNickname);
		    	 synchronized(serverThread.clients)
		    	 { for(int i=0;i<serverThread.clients.size();i++)
		    	 {
		    		//测试代码
		    		 System.out.println(client.getUsername()==userNickname);
		    		 Client client=serverThread.clients.elementAt(i);
		    		 //如果和某个用户的昵称相同，向其线程发送信息
		    		 if(client.getUsername().equals(userNickname))
		    			 {
		    				
		    			 client.getFriendManageClientThread().out.writeUTF(strReceive);
		    			  continue;
		    			 }
		    			 
		    		   
		    	 }
		    	 }
		      }
		      if(operation.equals("chatResponseTrue")||operation.equals("chatResponseFalse"))
		      {
		    	  String userNickname= jsonReceive.getString("requesterNickname");
		    	  synchronized(serverThread.clients)
		    	  { for(int i=0;i<serverThread.clients.size();i++)
			    	 {
			    		 Client client=serverThread.clients.elementAt(i);
			    		 //如果
			    		 if(client.getUsername().equals(userNickname))
			    			 client.getFriendManageClientThread().out.writeUTF(strReceive);
			    			 
			    		   continue;
			    	 }
		    	  }
		      }
			}
			catch(IOException e)
			{  try{
				in.close();
				break;
			}
			catch(IOException e1)
			{e1.printStackTrace();
			  
			}
				e.printStackTrace();
			}
			catch(JSONException e)
			{
				e.printStackTrace();
			}
	 
			}
	
}

}
