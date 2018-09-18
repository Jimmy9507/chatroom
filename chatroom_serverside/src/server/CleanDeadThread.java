package server;

import java.util.Map;
import java.util.Iterator;

import clients.Client;


public class CleanDeadThread extends Thread{
	
	private  ServerThread serverThread;
	private  ChatroomClientThread clientThread;
    private  Map<String,SingleChatClientThread> otherSideThreadMap;
     
    String thisSideName;
    String otherSideName;
	public CleanDeadThread(ServerThread serverThread)
	{
		this.serverThread=serverThread;
		
	}
	
	public void run()
 {
			
		while(true)
			{
			try{
				sleep(1000);
			    }
		    catch(InterruptedException e){ e.printStackTrace();}
		    //判断聊天室里是否有线程退出
		    synchronized(serverThread.clients){
		   for(int i=0;i<serverThread.clients.size();i++)
		   {
			   clientThread=(ChatroomClientThread)serverThread.clients.elementAt(i).getClientThread();
			   String ip=clientThread.getIP(); 
		       if(!clientThread.isAlive())
		       {  
		    	    Server.showMessageArea.append("ip:"+ip+"已经离开了聊天室\n");
		    	   ChatroomClientThread.clientConnectionNumber--;
		    	 serverThread.clients.removeElementAt(i);
		    	 
		        }
		      
		   }
		   //判断私聊中是否有线程退出
		   for(int i=0;i<serverThread.clients.size();i++)
		   {   Client client=serverThread.clients.elementAt(i);
		       thisSideName=client.getUsername();
			   otherSideThreadMap=client.getOtherSideThreadMap();
			   Iterator<Map.Entry<String,SingleChatClientThread>> iter=otherSideThreadMap.entrySet().iterator();
			   while(iter.hasNext())
			   {
				   Map.Entry<String,SingleChatClientThread> entry= iter.next();
				   otherSideName=(String)entry.getKey();
				  //测试代码
				   System.out.println(otherSideName);
				   SingleChatClientThread singleChatClientThread=(SingleChatClientThread) entry.getValue();
				   if(!singleChatClientThread.isAlive())
			       {    
					   for(int j=0;j<serverThread.clients.size();j++)
					   {
						   Client clientToRemind=serverThread.clients.elementAt(j);
						   if(clientToRemind.getUsername().equals(otherSideName))
							   {Map<String,SingleChatClientThread> map=clientToRemind.getOtherSideThreadMap();
						        if(map.get(thisSideName)!=null)
						        		map.get(thisSideName).otherIsLeft=true;
							   }
					   }
			    	    Server.showMessageArea.append(thisSideName+"和"+otherSideName+"的聊天已经结束\n");
			    	 otherSideThreadMap.remove(otherSideName);
			    	 
			    	 
				   
			   }
			   
		      
		       
		   } 
		
		   
	        }
		
		 
		    }
	   }
}
}
