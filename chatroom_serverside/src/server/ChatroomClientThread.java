package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import clients.Client;
public class ChatroomClientThread extends Thread{
	
	private ServerThread serverThread;
	private Socket socket;
	public  DataInputStream in;
	public  DataOutputStream out;
	String message;
  
	private String ipInString;
	public static int clientConnectionNumber=0;
	public ChatroomClientThread(ServerThread serverThread,Socket socket,Client client)
	{   
		
		this.serverThread=serverThread;
		this.socket=socket;
		clientConnectionNumber++;
		try{
		in=new DataInputStream(socket.getInputStream());
		out=new DataOutputStream(socket.getOutputStream());
		}
		catch(IOException e)
		{
			System.out.println("建立服务器与客户端间建立I/O通道失败！,IP:"+String.valueOf(socket.getLocalAddress()));
		}
		client.setClientThread(this);
		String str="欢迎进入聊天室！";
		try{
		out.writeUTF(str);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	   try{
			 ipInString= String.valueOf(socket.getInetAddress().getLocalHost());
		}
		catch(UnknownHostException e)
		{
			e.printStackTrace();
		}
	
		
	}
	public void run()
	{   while(true)
	    {  try
		  {
			Thread.sleep(300);
		  }
	      catch(InterruptedException e){}
	      if(socket==null)
	       break;
	       try
	     	{
	    	  
	    	    message=in.readUTF();
	    	    synchronized(serverThread.messages)
		  	       {
	    	    	if(message!=null)
	     	   {serverThread.messages.addElement(message);
	     	      Server.showMessageArea.append(message+'\n');
	     	   }
	     	      /*测试代码*/
	     	   System.out.println(message);
		  	       }
	     	}
	        catch(IOException e)
	       { e.printStackTrace();
	          if(socket!=null)
	        	{  try{socket.close();
	        	   socket=null;}
	        	catch(IOException e1){e1.printStackTrace();}
	        	}
	        break;
	        	}
	     
	     }
	}
	public void finalize()
	{ 
	}
	public String getIP()
	{  
        return ipInString;
	}
}
	


