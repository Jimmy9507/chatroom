package friendManage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;
public class FriendChatThread extends Thread{
       Socket socket;
       static final int PORT=10748;
       DataInputStream in;
       DataOutputStream out;
       String otherSideUsername;
       String thisSideUsername;
       SingleChatDialog singleChatDialog;
       public FriendChatThread(String thisSideUsername,String otherSideUsername,SingleChatDialog singleChatDialog)
       {  
    	   try{
    	   socket=new Socket("112.74.19.32",PORT);
    	   in=new DataInputStream(socket.getInputStream());
    	   out=new DataOutputStream(socket.getOutputStream());
          
          this.otherSideUsername=otherSideUsername;
          this.thisSideUsername=thisSideUsername;
          JSONObject otherSideInfo=new JSONObject();
          otherSideInfo.put("otherSideUsername", otherSideUsername);
          otherSideInfo.put("thisSideUsername", thisSideUsername);
          String otherSideInfoStr=otherSideInfo.toString();
         this.singleChatDialog=singleChatDialog;
          out.writeUTF(otherSideInfoStr);
       }
       catch(IOException e)
       { 
     	  e.printStackTrace();
       }
       catch(JSONException e)
       {    e.printStackTrace();
        }
	
	
}
       public void run()
       {
    	   while(true)
    	   {  //如果线程挂起则将通信关闭
    		   if(isInterrupted())
    		   {
    		   try{
    		   socket.close();
    		   socket=null;
    		   in.close();
    		   out.close();
    		   in=null;
    		   out=null;}
    		   catch(IOException e)
    		   {
    			   e.printStackTrace();
    		   }
    		   
    		   break;
    		   
    		   }
    		   try{
    		   String messageGet=in.readUTF();
    		   //测试代码
    		   System.out.println(messageGet);
    		   singleChatDialog.showMessageArea.append(messageGet+"\n");
    	       }
    	   catch(IOException e)
    	   { try{ 
    		   socket.close();
    		   socket=null;
    		   in.close();
    	     out.close();
    	     out=null;
    	       in=null;}
    	   catch(IOException e1)
              {e1.printStackTrace(); }
              break;
    	   }
    	   
    	   }
    	   
       }
       public void sendMessage(String username,String message)
       {  if(socket.isClosed())
    	   singleChatDialog.showMessageArea.append("系统提示：对方已退出\n");
    	   try{
    		   out.writeUTF(username+"说："+message+'\n');
    		   
    	   }
    	
    	   catch(IOException e)
    	   { 
    		   
    		   e.printStackTrace();
    	   }
    	   
    	   
       }
}