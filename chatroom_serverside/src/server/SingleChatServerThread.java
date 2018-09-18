package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
//管理所有私聊的线程
public class SingleChatServerThread extends Thread{
      ServerSocket singleChatServerSocket;
      ServerThread serverThread;
      static final int PORT=10748; 
      DataInputStream in;
      DataOutputStream out;
      //创立一个MAP映射表，来反映出是往具体的哪个线程中输数据
   
      public SingleChatServerThread(ServerThread serverThread)  
      { try{
    	 singleChatServerSocket=new ServerSocket(PORT);
          }
         catch(IOException e)
         {
        	 e.printStackTrace();
         }
        this.serverThread=serverThread;
      }
      public void run()
      {
    	  while(true)
    	  {
    		  
    		  try{
    	     Socket socket=singleChatServerSocket.accept();
    	       SingleChatClientThread singleChatClientThread=new SingleChatClientThread(serverThread,socket);
    	       singleChatClientThread.start();
    		  }
    		  catch(IOException e)
    		  {e.printStackTrace();}
    	  
    	  
    	  
      }
      }
	
	
}
