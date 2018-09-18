package client;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import javax.swing.Box;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import friendManage.FriendManageThread;
import friendManage.FriendManageConnector;
/*引入两个数据模型*/
import user.FriendInfo;
import user.UserInfo;
public class Client extends JFrame implements ActionListener

{  /*声明线程*/
	Thread thread;
   /*设置四个大面板*/
	JPanel topPanel;
	JPanel middleLeftPanel;
	JPanel middleRightPanel;
    /*(第三个面板中的一个小按钮面板)*/
    JPanel friendManagePanel;
	JPanel bottomPanel;
	/*设置滚动条面板*/
	JScrollPane showMessagePane;
	JScrollPane showOnlinePane;
	JScrollPane showFriendListPane;
    /*设置按钮*/
	JButton enterChatroom;
	JButton exitChatroom;
	JButton addFriendButton;
	JButton delFriendButton;
	JButton singleChatButton;
	JButton sendMessage;
	/*设置标签*/
	JLabel welcomeLabel;
	JLabel onlineLabel;
	JLabel friendLabel;
    /*设置文本框*/

	JTextField messageInput;
	/*设置两个表格*/
	JTable onlineTable;
	JTable friendTable;
	/*设置表格模型*/
	static DefaultTableModel onlineTableModel;
	public  DefaultTableModel friendListTableModel;
	/*设置聊天信息显示框*/
	static JTextArea showMessageArea;
	//设置进入聊天室的布尔值
	static boolean isOpened=false;
	/*设置各个面板的位置*/
	
	//设置一个监听聊天室发送信息的线程
	 ClientSideThread clientSideThread;
	 //设置一个监听在线用户列表改变的线程
	 OnlineUpdateClientThread onlineUpdateClientThread;
	 //设置一个监听添加删除好友的线程
	 FriendManageThread friendManageThread;
	//设置主窗口的位置
	Dimension dimension=Toolkit.getDefaultToolkit().getScreenSize();
	
	//定义一个确定是哪个按钮的集合
	 ArrayList<JButton>jButtons=new ArrayList<JButton>();
	
	
	private  final int WINDOW_X=dimension.width/4-100;
	private  final int WINDOW_Y=dimension.height/4-100;
	private final int WINDOW_WIDTH=dimension.width/2;
	private final int WINDOW_HEIGHT=dimension.height/2;
	//设置上层组件panel的位置
	private final int TOPPANEL_X=0;
	private final int TOPPANEL_Y=0;
	private final int TOPPANEL_WIDTH=WINDOW_WIDTH;
	private final int TOPPANEL_HEIGHT=(int)(WINDOW_HEIGHT*(1/8.0));
	//设置中层左部组件的位置
	private final int MIDDLE_LX=0;
	private final int MIDDLE_LY=TOPPANEL_HEIGHT;
	private final int MIDDLE_LWIDTH=(int)(WINDOW_WIDTH*(3/5.0));
	private final int MIDDLE_LHEIGHT=(int)(WINDOW_HEIGHT*(6/8.0));
	//设置中层右部组件的的位置
	private final int MIDDLE_RX=MIDDLE_LWIDTH;
	private final int MIDDLE_RY=TOPPANEL_HEIGHT;
	private final int MIDDLE_RWIDTH=(int)(WINDOW_WIDTH*(2/5.0));
	private final int MIDDLE_RHEIGHT=(int)(WINDOW_HEIGHT*(6/8.0));
	//设置下层组件的位置
	private final int BOTTOM_X=0;
	private final int BOTTOM_Y= TOPPANEL_HEIGHT+MIDDLE_RHEIGHT;
	private final int BOTTOM_WIDTH=WINDOW_WIDTH;
	private final int BOTTOM_HEIGHT=(int)(WINDOW_HEIGHT*(1/8.0));
	
	/*一个数据模型*/
	public static UserInfo userInfo;
	public Client()
	{
		
		userInfo=new UserInfo();
	    this.setVisible(true);
	    this.setTitle("陌陌");
	    this.setBounds(WINDOW_X,WINDOW_Y,WINDOW_WIDTH,WINDOW_HEIGHT);
	
	}
	public void initView()
	{
	  /*加入第一个面板*/
	Container container= this.getContentPane();
   container.setLayout(null);
     topPanel=new JPanel();
     topPanel.setLayout(null);
     topPanel.setBounds(TOPPANEL_X,TOPPANEL_Y,TOPPANEL_WIDTH,TOPPANEL_HEIGHT);
	container.add(topPanel);
	welcomeLabel=new JLabel("欢迎您："+userInfo.getUserNickname());
	welcomeLabel.setFont(new Font("TimesRoman",Font.ITALIC,25));
	welcomeLabel.setBounds(0,0,WINDOW_WIDTH/2,(int)(WINDOW_HEIGHT*(1/8.0)));
	enterChatroom=new JButton("进入聊天室");
    enterChatroom.addActionListener(this);
    enterChatroom.setBounds((int)(WINDOW_WIDTH*3/5.0),(int)(WINDOW_Y*1/12.0),WINDOW_WIDTH/5,(int)(WINDOW_HEIGHT*1/12.0));
    jButtons.add(enterChatroom);
	exitChatroom=new JButton("退出聊天室");
	exitChatroom.addActionListener(this);
	exitChatroom.setBounds((int)(WINDOW_WIDTH*4/5.0),(int)(WINDOW_Y*1/12.0),WINDOW_WIDTH/5,(int)(WINDOW_HEIGHT*1/12.0));
	jButtons.add(exitChatroom);
	
	topPanel.add(welcomeLabel);
	topPanel.add(enterChatroom);
	topPanel.add(exitChatroom);
	
	
    /*加入第二个面板 */
	middleLeftPanel=new JPanel();
    middleLeftPanel.setLayout(new BorderLayout());
    showMessagePane=new JScrollPane();

   showMessageArea=new JTextArea();
	showMessagePane.getViewport().add(showMessageArea);
	middleLeftPanel.add(showMessagePane,BorderLayout.CENTER);
 	middleLeftPanel.setBounds(new Rectangle(MIDDLE_LX,MIDDLE_LY,MIDDLE_LWIDTH,MIDDLE_LHEIGHT));
      container.add(middleLeftPanel);
      
      
    /*加入第三个面板*/
    middleRightPanel=new JPanel();
    middleRightPanel.setLayout(null);
    middleRightPanel.setBounds(MIDDLE_RX,MIDDLE_RY,MIDDLE_RWIDTH,MIDDLE_RHEIGHT);
  
    onlineLabel=new JLabel("聊天室在线：");
    onlineLabel.setBounds(0,0,MIDDLE_RWIDTH,(int)(MIDDLE_RHEIGHT*1/9.0));
    showOnlinePane=new JScrollPane();
    showOnlinePane.setBounds(0,(int)(MIDDLE_RHEIGHT*1/9.0),MIDDLE_RWIDTH,(int)(MIDDLE_RHEIGHT*3/9.0));
    onlineTable=createOnlineTable();
    showOnlinePane.getViewport().setLayout(null);
    showOnlinePane.getViewport().setBackground(Color.WHITE);
    showOnlinePane.getViewport().add(onlineTable);
    friendLabel=new JLabel("好友列表：");
    friendLabel.setBounds(0,(int)(MIDDLE_RHEIGHT*4/9.0),MIDDLE_RWIDTH,(int)(MIDDLE_RHEIGHT*1/9.0));
    showFriendListPane=new JScrollPane();
    showFriendListPane.setBounds(0,(int)(MIDDLE_RHEIGHT*5/9.0),MIDDLE_RWIDTH,(int)(MIDDLE_RHEIGHT*3/9.0));
    friendTable=createFriendListTable();
    showFriendListPane.getViewport().setLayout(null);
    showFriendListPane.getViewport().setBackground(Color.WHITE);
    showFriendListPane.getViewport().add(friendTable);
    friendManagePanel=new JPanel();
    friendManagePanel.setBounds(0,(int)(MIDDLE_RHEIGHT*8/9.0),MIDDLE_RWIDTH,(int)(MIDDLE_RHEIGHT*1/9.0));
    friendManagePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    addFriendButton=new JButton("添加好友");
    addFriendButton.addActionListener(this);
    jButtons.add(addFriendButton);
    delFriendButton=new JButton("删除好友");
    delFriendButton.addActionListener(this);
    jButtons.add(delFriendButton);
    singleChatButton=new JButton("私聊");
    singleChatButton.addActionListener(this);
    jButtons.add(singleChatButton);
    friendManagePanel.add(addFriendButton);
    friendManagePanel.add(delFriendButton);
    friendManagePanel.add(singleChatButton);
    middleRightPanel.add(onlineLabel);
    middleRightPanel.add(showOnlinePane);
    middleRightPanel.add(friendLabel);
    middleRightPanel.add(showFriendListPane);
    middleRightPanel.add(friendManagePanel);
   
    container.add(middleRightPanel);
    /*加入第4个面板*/
    bottomPanel=new JPanel();
    bottomPanel.setBounds(BOTTOM_X,BOTTOM_Y,BOTTOM_WIDTH,BOTTOM_HEIGHT);
    bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    messageInput=new JTextField(28);
    sendMessage=new JButton("发送");
    sendMessage.addActionListener(this);
    sendMessage.setEnabled(false);
    jButtons.add(sendMessage);
    bottomPanel.add(messageInput);
    bottomPanel.add(sendMessage);
    container.add(bottomPanel);
   /*更新好友列表*/
    updateFriendList();
	}
	/*按钮的事件响应*/
	    public void actionPerformed(ActionEvent e)
	    {
	    
	    	switch(jButtons.indexOf((JButton)e.getSource()))
	   {   //加入聊天室按钮
	    	case 0:
	    		
	    			if(isOpened)
	    	    JOptionPane.showMessageDialog(this, "您已经在聊天室了！");
	    		else
	    			{JOptionPane.showMessageDialog(this,"进入聊天室成功");
	    		    sendMessage.setEnabled(true);
	    		    //开启用户聊天室线程
	    		    if (clientSideThread==null)
	    		    { clientSideThread=new ClientSideThread();
	    		    	clientSideThread.start();}
	    		    else
	    		    	clientSideThread.setSuspend(false);
	    		    //开启聊天室在线用户监听线程
	    		    if(onlineUpdateClientThread==null)
	    		    {onlineUpdateClientThread=new OnlineUpdateClientThread(userInfo);
	    		    onlineUpdateClientThread.start();
	    		    }
	    		    else 
	    		    	onlineUpdateClientThread.setSuspend(false);
	    		    //开启添加删除用户监听线程
	    		      if(friendManageThread==null)
		    		    {friendManageThread=new FriendManageThread(this);
		    		    friendManageThread.start();
		    		    }
		    		    else 
		    		    	friendManageThread.setSuspend(false);
	    		      sendMessage.setEnabled(true);
	    		      addFriendButton.setEnabled(true);
	    		      delFriendButton.setEnabled(true);
	    		      singleChatButton.setEnabled(true);
	    		    isOpened=true;
	    			}
	    		
	    		break;
	    	//退出聊天室按钮
	    	case 1:
	    		if(!isOpened)
	    			JOptionPane.showMessageDialog(this,"您已经不在聊天室了");
	    		else  
	    		{JOptionPane.showMessageDialog(this,"退出聊天室成功");
	 
                 clientSideThread.setSuspend(true);
                 onlineUpdateClientThread.setSuspend(true);
                 friendManageThread.setSuspend(true);
                 onlineTableModel.setRowCount(0);
                 //sendMessage.setEnabled(false);
                 sendMessage.setEnabled(false);
   		      addFriendButton.setEnabled(false);
   		      delFriendButton.setEnabled(false);
   		      singleChatButton.setEnabled(false);
                 isOpened=false;
	    	         
	    	      }
	    		break;
	    
	    			
	    	case 2:
	    		String usernameToAdd=JOptionPane.showInputDialog(this,"请输入你想添加的好友(必须在线)").trim();
	    		if(onlineUpdateClientThread.selectOnlineFriend(usernameToAdd))
	    	    {int i=JOptionPane.showConfirmDialog(this,"你确定将"+usernameToAdd+"加为好友吗？","添加好友",JOptionPane.YES_NO_OPTION);
	    	       if(i==0)
	    	       {
	    	    	   friendManageThread.sendAddFriendRequest(usernameToAdd); 
	    	    	   JOptionPane.showMessageDialog(this,"请求已发送");
	    	          }
	    	    }
	    		break;
	    	case 3:
	    		String usernameToDel=friendListTableModel.getValueAt(friendTable.getSelectedRow(),0).toString();
	    		  if(usernameToDel!=null)
	    		{int i=JOptionPane.showConfirmDialog(this,"你确定要删除"+usernameToDel+"好友吗","删除好友",JOptionPane.YES_NO_OPTION);
	    		if(i==0)		
	    		{   if(friendManageThread.removeFriend(usernameToDel))  
	    			    {JOptionPane.showMessageDialog(this, "删除成功");
	    			    friendListTableModel.removeRow(friendTable.getSelectedRow());
	    			    }
	    			      
	    			    
	            else
	    			JOptionPane.showMessageDialog(this, "删除失败");
	    		}
	    		}
	    	    break;

	    	case 4:
		    		String usernameToChat=JOptionPane.showInputDialog(this,"请输入您想聊天的用户(必须在线)").trim();
		    		int i=JOptionPane.showConfirmDialog(this,"你确定要和"+usernameToChat+"进行聊天吗","聊天",JOptionPane.YES_NO_OPTION);
		    		  if(i==0)
		    		  { friendManageThread.sendChatRequest(usernameToChat);
		    		     JOptionPane.showMessageDialog(this,"请求已发送");
		    		  }
		    		  break;
	    		//发送按钮  
	    	case 5:
	    		
	    	    String name=userInfo.getUserNickname();
	    		String message=messageInput.getText().trim();
	    		if(!name.equals("")&&!message.equals(""))
	    		{clientSideThread.sendMessageToServer(name,message);
	    		  messageInput.setText("");
	    		}
	    		break;
	   
	    		  
	    	    	
	    	    	
	   }	
	    }
	    /*初始化窗口前的信息获取*/
	    public void getInformationFromLogin(UserInfo userInfo)
	    {     this.userInfo.setUsername(userInfo.getUsername());
	    	  this.userInfo.setUserNickname(userInfo.getUserNickname());
	    	  this.userInfo.setUserLevel(userInfo.getUserLevel());
	    	  System.out.println(userInfo.getUserLevel());
	    	  this.userInfo.setUserFriendInfo(userInfo.getUserFriendInfo());
	    }
	    /*创建在线成员表格*/
	    private JTable createOnlineTable()
	    {   Object[] title=new Object[]{"用户昵称","用户等级"};
	    	onlineTableModel=new DefaultTableModel(null,title);
	    	JTable  onlineTable =new JTable(onlineTableModel);
	    	onlineTable.setBounds(0,0,MIDDLE_RWIDTH,(int)(MIDDLE_RHEIGHT*3/9.0));
	    	return onlineTable;
	    	
	    }
	   private JTable createFriendListTable()
	   {
		    Object[] title=new Object[]{"好友昵称","用户等级"};
		    friendListTableModel=new DefaultTableModel(null,title);
		    JTable friendListTable=new JTable(friendListTableModel);
		    friendListTable.setBounds(0,0,MIDDLE_RWIDTH,(int)(MIDDLE_RHEIGHT*3/9.0));
		    return friendListTable;
		    
		   
	   }
	   private void updateFriendList()
	   {   if(userInfo.getUserFriendInfo().size()==0)
		       return ;
	      ArrayList<FriendInfo> friendsInfos=userInfo.getUserFriendInfo();
	     for(int i=0;i<friendsInfos.size();i++)
	     { String friendName=friendsInfos.get(i).getFriendName();
	       int friendLevel=friendsInfos.get(i).getFriendLevel();
           friendListTableModel.addRow(new Object[]{friendName,friendLevel});
	     }
	    
	     
	}
}
	
	
	
	
	
	

