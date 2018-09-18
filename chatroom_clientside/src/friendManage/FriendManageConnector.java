package friendManage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.FileInputStream;
import java.io.InputStream;
import client.Client;
import java.util.Properties;
import java.io.IOException;
import java.io.FileNotFoundException;
/*导入UserInfo,FriendInfo数据结构*/

/**
 * 直接连接远程数据库（非socket通信）
 * @author jimmy
 *
 */
public class FriendManageConnector {
    
	/*连接数据库所需的信息*/
	  String driverClass=null;
	  String jdbcUrl=null;
	  String databaseUsername=null;
	  String databasePassword=null;
	  InputStream in;
	  Connection connection ;
	  String username=null;

	  public FriendManageConnector(String username)
	  {
		  in=getClass().getResourceAsStream("jdbc.properties");
	  
	  
	  Properties info=new Properties();
		try{
		  info.load(in);
		}
		catch(IOException e)
		{e.printStackTrace();}
		driverClass=info.getProperty("driverClass");
		jdbcUrl=info.getProperty("jdbcUrl");
		databaseUsername=info.getProperty("username");
		databasePassword=info.getProperty("password");
		this.username=username;
	  }
	
	  public void getFriendManageConnection()
	  {  //加载驱动程序
		 try{
		  Class.forName(driverClass);
		 }
		 catch(ClassNotFoundException e)
		 {
			 e.printStackTrace();
		 }
		 try{
		  connection=  DriverManager.getConnection(jdbcUrl,databaseUsername,databasePassword);
		 }
		 catch(SQLException e)
		 {
			 e.printStackTrace();
		 }
		  
		 
	  }
	  //加好友进入数据库
	  public boolean addFriendToDatabase(String usernameToAdd,int userLevelToAdd)
		{   int count=0;
			this.getFriendManageConnection();
		  String pw=new String();
			try{
		    Statement stm=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		    String sqlStr="INSERT INTO "+username+"FriendInfo (friendName,level) VALUES ( '"+usernameToAdd+"','"+userLevelToAdd+"');";
		    count=stm.executeUpdate(sqlStr);
	  
		    stm.close();
			   connection.close();
		}
		   catch (SQLException e){
			   e.printStackTrace();
		   }
		   if (count==1)
			   return true;
		   else return false;
        }
	  //从数据库中删好友
	  public boolean removeFriendToDatabase(String usernameToDel)
		{   int count=0;
			this.getFriendManageConnection();
		  String pw=new String();
			try{
		    Statement stm=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		    String sqlStr="DELETE  FROM "+username+"FriendInfo WHERE friendName='"+usernameToDel+"'";
		    count=stm.executeUpdate(sqlStr);
		   
		    stm.close();
			   connection.close();
		}
		   catch (SQLException e){
			   e.printStackTrace();
		   }
		   if (count==1)
			   return true;
		   else return false;
      }

}
