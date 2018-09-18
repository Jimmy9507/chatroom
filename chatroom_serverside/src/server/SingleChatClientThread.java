package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import clients.Client;
import org.json.JSONException;
import org.json.JSONObject;

public class SingleChatClientThread extends Thread {
    
	ServerThread serverThread;
	 Socket socket;
	  public DataInputStream in;
	  public DataOutputStream out;
	  String otherSideUsername;
	  String thisSideUsername;
	  SingleChatClientThread otherSingleClientThread;
	  //判断对方是否已退出
	  public boolean otherIsLeft;
	 public SingleChatClientThread(ServerThread serverThread,Socket socket)
	 {  otherIsLeft=false;
		 try{
		this.serverThread=serverThread;
		 this.socket=socket;
		 in=new DataInputStream(socket.getInputStream());
		 out=new DataOutputStream(socket.getOutputStream());
	     String otherSideInfoStr=in.readUTF();  
	     JSONObject otherSideInfo=new JSONObject(otherSideInfoStr);
	    otherSideUsername=otherSideInfo.getString("otherSideUsername");
	    thisSideUsername=otherSideInfo.getString("thisSideUsername");
	    //测试代码
	    System.out.println(otherSideUsername);
	    System.out.println(thisSideUsername);
	    
	  
	    //将这个私聊线程放进Client信息里
	    synchronized(serverThread.clients)
	    {    for(int i=0;i<serverThread.clients.size();i++)
	    {
	    	Client client=serverThread.clients.elementAt(i);
	    	if(client.getUsername().equals(thisSideUsername))
	    	{
	    		client.otherSideThreadMap.put(otherSideUsername, this);
	    	}
	    }
	    }
	   
	    
	    //获取对方的私聊进程
	    synchronized(serverThread.clients)
	    {    for(int i=0;i<serverThread.clients.size();i++)
	    {
	    	Client client=serverThread.clients.elementAt(i);
	    	if(client.getUsername().equals(otherSideUsername))
	    	{
	    		otherSingleClientThread=client.otherSideThreadMap.get(thisSideUsername);
	    	}
	    }
	    }
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
	 public void run()
	 {
		while(true)
		{  
			
			try{
				if(otherSingleClientThread==null&&otherIsLeft==false) 
				{	//获取对方的私聊进程
			    synchronized(serverThread.clients)
			    {    for(int i=0;i<serverThread.clients.size();i++)
			    {
			    	Client client=serverThread.clients.elementAt(i);
			    	if(client.getUsername().equals(otherSideUsername))
			    	{   
			    		otherSingleClientThread=client.otherSideThreadMap.get(thisSideUsername);
			    	}
			    }
			    }
			          continue;
				}
			String messageGet=in.readUTF();
			out.writeUTF(messageGet);
			otherSingleClientThread.out.writeUTF(messageGet);
			}
			catch(IOException e)
			{
				try{
					socket.close();
					socket=null;
					in.close();
				in=null;
				out.close();
				out=null;}
				catch(IOException e1)
				{
					e1.printStackTrace();
				}
				e.printStackTrace();
				break;
			}
			
			
			
			
		}
		 
		 
		 
	 }
	
	
	
}
