package loginWindowInfo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.io.IOException;
import java.io.FileNotFoundException;
/*导入UserInfo,FriendInfo数据结构*/
import user.FriendInfo;
import user.UserInfo;
/**
 * 直接连接远程数据库（非socket通信）
 * @author jimmy
 *
 */
public class LoginConnector {
    
	/*连接数据库所需的信息*/
	  String driverClass=null;
	  String jdbcUrl=null;
	  String databaseUsername=null;
	  String databasePassword=null;
	  InputStream in;
	  Connection connection ;
	  String username=null;
	  String password=null;
	  public LoginConnector(String username,String password)
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
		this.password=password;
	  }
	
	  public void getLoginConnection()
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
	  public boolean verifyLogin()
		{   
			this.getLoginConnection();
		  String pw=new String();
			try{
		    Statement stm=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		    String sqlStr="SELECT password  FROM userLoginInfo WHERE username='"+username+"'";
		    ResultSet rs=stm.executeQuery(sqlStr);
		    if(rs.next())
		    pw=rs.getString(1).trim();
		    //测试代码
		    System.out.println("the password from database is "+pw);
		    System.out.println("the password we input is "+password);
		    System.out.println(pw.equals(password));
		    stm.close();
			   connection.close();
		}
		   catch (SQLException e){
			   e.printStackTrace();
		   }
		   if (pw.equals(password))
			   return true;
		   else return false;
        }
	  
	    public UserInfo getLoginUserInfo()
	    {   UserInfo userInfo=new UserInfo();
	       ArrayList<FriendInfo> friendsInfos=new ArrayList<FriendInfo>();
	    	this.getLoginConnection();
	    	String getUserInfoSql=new String("SELECT * FROM userLoginInfo WHERE username='"+username+"';");
	    	String getFriendInfoSql=new String("SELECT * FROM "+username+"FriendInfo;");
	    	try{
	    	Statement stm=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
	    	   //分别获取该用户的用户信息，和该用户的“朋友”列表信息
	    	  
	    	 
	    	  ResultSet friendInfoRS=stm.executeQuery(getFriendInfoSql);
	    	
	    	 //逐项获取该用户的朋友列表信息
	    	  while(friendInfoRS.next())
	    	  {  
	    		          String friendName=friendInfoRS.getString(2);
	    		          //测试代码
	    		          System.out.println(friendName);
	    		          
	    		          int friendLevel=friendInfoRS.getInt(3);
	    		          FriendInfo friendInfo=new FriendInfo(friendName,friendLevel);
	    		          friendsInfos.add(friendInfo);
	    		  
	    	  }
	    	//逐项获取该用户的信息
	    	  ResultSet userInfoRS=stm.executeQuery(getUserInfoSql);
	    	  while(userInfoRS.next())
	    	  {
	    		  String userNickname=userInfoRS.getString(3);
	    		  int level =userInfoRS.getInt(5);
	    		  System.out.println(level);
	    		  userInfo.setUsername(username);
	    		 userInfo.setUserNickname(userNickname);
	    		 userInfo.setUserLevel(level);
	    		
	    	  }
	    	  userInfo.setUserFriendInfo(friendsInfos);
	    	  stm.close();
	    	  connection.close();
	    	}
	    	catch(SQLException e)
	    	{
	    		e.printStackTrace();
	    	}
	    	return userInfo;
	    	
	 }
}
