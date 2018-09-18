package server;
import java.net.Socket;
import clients.Client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

public class OnlineUpdateClientThread {
     ServerThread serverThread;
     Socket socket;
     Client client;
	 DataInputStream in;
	 DataOutputStream out;
	 String readJson;
	 String[] response;
	public OnlineUpdateClientThread(ServerThread serverThread,Socket socket,Client client)
	{
		this.serverThread=serverThread;
		this.socket=socket;
		this.client=client;
		try{
		in=new DataInputStream(socket.getInputStream());
		out=new DataOutputStream(socket.getOutputStream());
		}
		catch(IOException e)
		{e.printStackTrace();}
		
		try{  //接收用户信息
			   String receiveUsersInfos=in.readUTF();
			   JSONObject jsonClientInfo=new JSONObject(receiveUsersInfos);
			  String username =(String)jsonClientInfo.get("username");
				 int level=Integer.parseInt(String.valueOf(jsonClientInfo.get("level")));
				String ip=(String)jsonClientInfo.get("ip");
				
				   client.setUsername(username);
				   client.setUserLevel(level);
				   client.setIPAddress(ip);

			   }
			 catch(IOException e)
			 {e.printStackTrace();
			 }
			 catch(JSONException e)
			 {e.printStackTrace();
			 }
			 client.setOUClientThread(this);
	}

	
	
}
