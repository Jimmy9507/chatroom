package loginWindowInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.omg.CORBA.portable.UnknownException;


public class RegisterConnector {
         /*判断用户名是否已存在*/
	     boolean isUsernameValid=false;
	     boolean isNicknameValid=false;
	    
		/*连接数据库所需的信息*/
		  String driverClass=null;
		  String jdbcUrl=null;
		  String databaseUsername=null;
		  String databasePassword=null;
		  InputStream in;
		  Connection connection ;
		  String username=null;
		  String password=null;
		  String registerNickname=null;
		  public RegisterConnector()
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
		
		  }
		
		  public void getRegisterConnection()
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
		  //判断注册的用户名是否可用
		  public boolean verifyRegisterName(String username,String password)
			{   
				 this.getRegisterConnection();
				this.username=username;
				this.password=password;
				try{
			    Statement stm=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			    String sqlStr="SELECT username FROM userLoginInfo WHERE username='"+username+"';";
			    ResultSet rs=stm.executeQuery(sqlStr);
			    if(rs.next())
			     isUsernameValid=false;
			    else 
			     isUsernameValid=true;
			    //测试代码
			 
			    stm.close();
				   connection.close();
			}
			   catch (SQLException e){
				   e.printStackTrace();
			   }
			  
			
			   if (isUsernameValid)
				   return true;
			   else return false;
			
	}
		  //判断注册的用户昵称是否可用
		 public boolean verifyRegisterNickname(String registerNickname)
		 {	  this.getRegisterConnection();
			 try{
			   this.registerNickname=registerNickname   ;
			 Statement stm=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			    String sqlStr1="SELECT nickname FROM userLoginInfo WHERE nickname='"+this.registerNickname+"';";
			    ResultSet rs=stm.executeQuery(sqlStr1);
			    
			    if(rs.next())
			     isNicknameValid=false;
			    else 
			     isNicknameValid=true;
			    //测试代码
			  
			    if(isNicknameValid)
			    {
			      String sqlStr2="CREATE TABLE "+username+"friendInfo (id INT not null auto_increment ," +
			      	                                         "friendName VARCHAR(20) not null ,"+
			      	                                         "level INT," +
			      	                                         "primary key(id,friendName) );";
			        int count =stm.executeUpdate(sqlStr2);
			    }		    
			    stm.close();
				   connection.close();
			}
			   catch (SQLException e){
				   e.printStackTrace();
			   }
			  
			
			   if (isNicknameValid)
				   return true;
			   else return false;
			 
			 
			 
		 }
		 //加入新的注册账户
		 public boolean createNewAccount(String username,String password,String nickname)
		 {  boolean isRegisterSuccess=false;  
		   this.getRegisterConnection();
			 try{
			 
			 Statement stm=connection.createStatement();
			 String sql="INSERT INTO userLoginInfo (username,nickname,password,level) VALUES ('"+username
			 +"','"+nickname+"','"+password+"','0');";
		     int count= stm.executeUpdate(sql);
			 if(count==1)
				 isRegisterSuccess=true;
			 else 
				 isRegisterSuccess=false;	 
			 stm.close();
			 connection.close();
		 }
		 catch(SQLException e)
		 {e.printStackTrace();
      	}
		 return isRegisterSuccess;
		 }
		 
	 }
