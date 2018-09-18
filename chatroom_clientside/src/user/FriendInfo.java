package user;

/**
 * 这是记录每个用户的朋友信息的类
 * @author jimmy
 *
 */
public class FriendInfo {
   
	/*朋友的名字*/
	   String friendName;
	    int level;
	    public FriendInfo(String friendName,int level)
	    {
	    	   this.friendName=friendName;
	    	   this.level=level;
	    	   
	    }
	    //不能被外界调用
	    private void setFriendName(String friendName)
	    {
	    	this.friendName=friendName;
	    }
	    private void setFriendLevel(int level)
	    {
	    	this.level=level;
	    }
	    public String getFriendName()
	    {
	    	return friendName;
	    }
	    public int getFriendLevel()
	    {
	    	return level;
	    }
	    
}
