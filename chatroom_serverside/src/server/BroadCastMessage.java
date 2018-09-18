package server;

import java.io.IOException;
import clients.Client;
public class BroadCastMessage extends Thread{

	private ServerThread serverThread;
	private String str;
	private ChatroomClientThread newClientThread;
	public BroadCastMessage(ServerThread serverThread)
	{
		this.serverThread=serverThread;
		
	}
	public void run()
	{
		while(true)
		{
			try
			{
				Thread.sleep(300);
			}
			catch(InterruptedException e)
			{
				 e.printStackTrace();
			}
			synchronized(serverThread.messages)
			{
				if(serverThread.messages.isEmpty()){continue;}
			    str=(String)serverThread.messages.firstElement();
			    serverThread.messages.removeElement(str);
			}
			synchronized(serverThread.clients)
			{   //负责分发聊天室信息
				for(int i=0;i<serverThread.clients.size();i++)
				{
				 newClientThread=(ChatroomClientThread)serverThread.clients.elementAt(i).getClientThread();
			
				try {
					newClientThread.out.writeUTF(str);
				}
				catch(IOException e)
				{
					
				}
			
				}
			
			}
		}
		}
		
		
		
	}

