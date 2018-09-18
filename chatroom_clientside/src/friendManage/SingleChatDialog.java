package friendManage;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import client.Client;
public class SingleChatDialog extends JDialog implements ActionListener{
      JButton sendMessageButton;
      JButton exitButton;
      JScrollPane showMessagePane;
      JTextArea showMessageArea;
      FriendChatThread friendChatThread;
      JTextField messageInput;
      JPanel buttonPanel;
      JPanel showMessagePanel;
      String otherSideUsername;
      String thisSideUsername;
      /*设置对话框的大小尺寸*/
	  Dimension dimension=Toolkit.getDefaultToolkit().getScreenSize();
	    final int WINDOW_X=dimension.width/3;
	    final int WINDOW_Y=dimension.height/3;
	    final int WINDOW_WIDTH=dimension.width/3;
	    final int WINDOW_HEIGHT=dimension.height/4;
      public SingleChatDialog(String thisSideUsername,String otherSideUsername)
      {   friendChatThread=new FriendChatThread(thisSideUsername,otherSideUsername,this);
          friendChatThread.start();
    	  initView();
    	  this.setBounds(WINDOW_X,WINDOW_Y,WINDOW_WIDTH,WINDOW_HEIGHT);
    	  
    	  this.setVisible(true);
    	  this.otherSideUsername=otherSideUsername;
    	  this.thisSideUsername=thisSideUsername;
    	  this.setTitle("与"+otherSideUsername+"的聊天");
    	  this.addWindowListener(new MyWindowListener(this));
    
      }
      public void initView()
      {  
    	  Container container=this.getContentPane();
    	  container.setLayout(null);
    	  //配置消息面板
    	  showMessagePanel=new JPanel();
    	  showMessagePanel.setLayout(new BorderLayout());
    	  showMessageArea=new JTextArea();
          showMessagePane=new JScrollPane();
          showMessagePane.getViewport().add(showMessageArea);
          showMessagePanel.add(showMessagePane,BorderLayout.CENTER);
          showMessagePanel.setBounds(0,0,WINDOW_WIDTH,(int)(WINDOW_HEIGHT*4/6.0));
          
    	  //按钮面板
          messageInput=new JTextField(25);
    	  buttonPanel=new JPanel();
          buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
          buttonPanel.setBounds(0,(int)(WINDOW_HEIGHT*4/6.0),WINDOW_WIDTH,(int)(WINDOW_HEIGHT*2/6.0));
    	  sendMessageButton=new JButton("发送");
    	  sendMessageButton.addActionListener(this);
    	  exitButton=new JButton("退出");
    	  exitButton.addActionListener(this);
    	  buttonPanel.add(messageInput);
    	  buttonPanel.add(sendMessageButton);
    	  buttonPanel.add(exitButton); 
    	  
    	  this.getContentPane().add(showMessagePanel);
    	  this.getContentPane().add(buttonPanel);
      }
      public void actionPerformed(ActionEvent e)
      {
    	  if((JButton)e.getSource()==sendMessageButton)
    	  {  
    		  
    		  String message=messageInput.getText().trim();
    	   friendChatThread.sendMessage(Client.userInfo.getUserNickname(),message);
    	
    	      messageInput.setText("");  
    	        }
    	 
    	  
    	  if((JButton)e.getSource()==exitButton)
    	  {
    		  friendChatThread.interrupt();
    		  this.dispose();
    	  }
    	  //消息类
    
      }
      private static class MyWindowListener extends WindowAdapter{
    	  SingleChatDialog singleChatDialog;
    	  public MyWindowListener(SingleChatDialog singleChatDialog)
    	  {
    		  this.singleChatDialog=singleChatDialog;
    	  }
    	  public void WindowClosing(WindowEvent e)
    	  {
    		  singleChatDialog.friendChatThread.interrupt();
    		  singleChatDialog.dispose();
    	  }
    	  
    	  
    }
      
      
	 
}

