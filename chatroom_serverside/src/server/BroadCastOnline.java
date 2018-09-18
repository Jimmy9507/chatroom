package server;

import java.io.IOException;
import clients.Client;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
public class BroadCastOnline extends Thread{

	private ServerThread serverThread;
	private String str;
	private OnlineUpdateClientThread newClientThread;
	public BroadCastOnline(ServerThread serverThread)
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
			synchronized(serverThread.clients)
			{   //负责分发聊天室信息
				JSONObject jsonObject=new JSONObject();
				JSONArray clientInfoArray=new JSONArray();
				try{
				for(int i=0;i<serverThread.clients.size();i++)
				{
					Client client=serverThread.clients.get(i);
					String username=client.getUsername();
					int level=client.getUserLevel();
					String ip=client.getIPAddress();
					JSONObject clientObject=new JSONObject();
					clientObject.put("username", username);
					clientObject.put("level", level);
					clientObject.put("ip", ip);
					clientInfoArray.put(clientObject);
				}
				jsonObject.put("users",clientInfoArray);
				}
				catch(JSONException e)
				{
					e.printStackTrace();
				}
				String str=jsonObject.toString();
				for(int i=0;i<serverThread.clients.size();i++)
				{
				 newClientThread=serverThread.clients.elementAt(i).getOUClientThread();
				 if(newClientThread.socket!=null)
				try {
					newClientThread.out.writeUTF(str);
				}
				catch(IOException e)
				{   
					
					try
					{
				      newClientThread.in.close();
				      newClientThread.in=null;
				      newClientThread.out.close();
				      newClientThread.out=null;
					}
					catch(IOException e1)
					{e1.printStackTrace();
					}
				
					e.printStackTrace();
					
				}
			
				}
			
			}
		}
		}
	}
		
		
		
	
