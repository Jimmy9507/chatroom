package client;
import java.net.InetAddress;
import java.net.Socket;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.UnknownHostException;
import user.UserInfo;
/**
 * 
 * @author jimmy
 * 这是一个管理客户端连接方法的类
 *
 */
public class ClientSideThread extends Thread{
	

	  private static Socket socket;
	  private static DataInputStream in;
	  private static DataOutputStream out;
      private static final int PORT=10745;
	  private static String message;
	  boolean suspend;
	  Object threadControl;
    public ClientSideThread()
    {
   
    	try{
    		/*测试代码*/

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
		threadControl=new Object();
		suspend=false;
   
    }
    public void finalize()
    { 
    	try{
    	socket.close();
    	in.close();
    	out.close();
    	}			
    catch(IOException e)
    {  e.printStackTrace();}
       in=null;
       out=null;
       socket=null;
    }
    
    public void sendMessageToServer(String name,String message)
    {
    	try
    	{    		
    	out.writeUTF(name+"对大家说："+message+"\n");
    	/*测试代码*/
    	System.out.println(name+"对大家说："+message+"\n");
    	}
    	catch(IOException e){
    		e.printStackTrace();    	}
    }
    public void run()
    {
    	while(true)
    		
    		{  synchronized (threadControl) {
                if (suspend) {
                    try {
                        threadControl.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
    		try{
    		  message=in.readUTF();
    		 Client.showMessageArea.append(message+'\n');
    		}
    		catch(IOException e)
    		{  
    			e.printStackTrace();
    		     in=null;
    		
    	}
    
    }
    }
    public void setSuspend(boolean suspend)
    {  //唤醒
 	   if(!suspend)
 	    {  try{
 	 	   socket=new Socket("localhost",PORT);
 	 	 in=new DataInputStream(socket.getInputStream());
		    out=new DataOutputStream(socket.getOutputStream());
 		  
 	 	   }
 	   catch(IOException e){e.printStackTrace();}
 		   synchronized(threadControl)
 	      { threadControl.notifyAll();}
 	        
 	  
 	   
    }
 	   else
 	   {  try{
 		   socket.close();
 		   in.close();
 		   out.close();}
 	      catch(IOException e){e.printStackTrace();}
 		   socket=null;
 		   in=null;
 		   out=null;
 	   }
 	  this.suspend=suspend;
 		   
    }

}
