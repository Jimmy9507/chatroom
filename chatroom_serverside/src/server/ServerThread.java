package server;
import java.net.ServerSocket;
import java.io.IOException;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Vector;
import java.net.UnknownHostException;
import clients.Client;
public class ServerThread extends Thread {
	private  ServerSocket chatroomServerSocket;
	private ServerSocket onlineUpdateServerSocket;
	private ServerSocket friendManageServerSocket;
	private  InetAddress myIPAddress;
   private static final int CHATROOMPORT=10745;
   private static final int ONLINEPORT=10746;
   private static final int FRIENDMANAGEPORT=10747;
    public Vector<Client> clients;
   public  Vector<String> messages;
   private BroadCastMessage broadCastMessage;
   private BroadCastOnline broadCastOnline;
   private String ip;
   private CleanDeadThread cleanDeadThread;
   private SingleChatServerThread singleChatServerThread;
   
//开启一个服务器线程
   public ServerThread()
   {    //创建两个集合分别存放客户端，信息。
	     clients=new Vector<Client>();
	     messages=new Vector<String>();
	     try 
	     {
	    	 chatroomServerSocket=new ServerSocket(CHATROOMPORT);
	    	 onlineUpdateServerSocket=new ServerSocket(ONLINEPORT);
	    	 friendManageServerSocket=new ServerSocket(FRIENDMANAGEPORT);
	     }
	     catch(IOException e)
	     {
	    	 e.printStackTrace();
	     }
	     
	     try
	     {
	    	myIPAddress=InetAddress.getLocalHost(); 
	     }	 
	     catch(UnknownHostException e)
	     {
	    	 e.printStackTrace();
	    	 
	    	 
	     }
	      ip=String.valueOf(myIPAddress);
	     
	     broadCastMessage=new BroadCastMessage(this);
	     broadCastMessage.start();
	     broadCastOnline=new BroadCastOnline(this);
	     broadCastOnline.start();
	     singleChatServerThread=new SingleChatServerThread(this);
	     singleChatServerThread.start();
	     cleanDeadThread=new CleanDeadThread(this);
	     cleanDeadThread.start();
	    
   }
   
   public String getServerIp()
   {
	   return ip;
   }
   public String getServerPort()
   {
	   return String.valueOf(chatroomServerSocket.getLocalPort());
   }
	public void run ()
        {
		//监听新的客户端接入
		while(true)
		  {
			try
			{  //接收一个负责群聊功能的socket
				Socket chatroomSocket=chatroomServerSocket.accept();
			  // 接收一个负责刷新聊天室在线人员功能的socket
				Socket onlineUpdateSocket=onlineUpdateServerSocket.accept();
				//接收一个负责管理客观端添删好友的socket
				Socket friendManageSocket=friendManageServerSocket.accept();
				
				Client client=new Client();
        	  ChatroomClientThread chatroomClientThread=new ChatroomClientThread(this,chatroomSocket,client); 
        	  chatroomClientThread.start();
        	 
        	  OnlineUpdateClientThread onlineUpdateClientThread=new OnlineUpdateClientThread(this,onlineUpdateSocket,client);
        	 
        	  FriendManageClientThread friendManageClientThread=new FriendManageClientThread(this,friendManageSocket,client);
        	  friendManageClientThread.start();
        	//利用线程同步向clients集合中添加socket
        	if(chatroomSocket!=null&&onlineUpdateSocket!=null)
        	{
             synchronized(clients){
        			clients.addElement(client);	      
        	 }
        	}
			}
           catch(IOException e)
            	{
            	   System.out.println("发生异常"+e);
            	   System.out.println("建立客户端联机失败");
            	   System.exit(2);
            	}
        
		  }
        	
        }
        
   public void finalize()
   {   try
      {  chatroomServerSocket.close();
         onlineUpdateServerSocket.close();
      
      }
       catch(IOException e)
       {e.printStackTrace();
	   chatroomServerSocket=null;
       onlineUpdateServerSocket=null;
       }
   }
		 
   }
	

