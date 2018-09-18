package server;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;



public class Server extends JFrame implements ActionListener
{  
	
	JButton openServer;
	JButton closeServer;
	JPanel topPanel;
	JPanel bottomPanel;
	JScrollPane showMessagePane;
	static JTextArea showMessageArea;
	//线程（登录注册线程，聊天室进程,无效线程清除线程）
	ServerThread serverThread;

	CleanDeadThread cleanDeadThread;
	private static boolean isOpened=false; 
	//设置一个装有JButton 的ArrayList();
	private ArrayList<JButton>jButtons=new ArrayList<JButton>();
	//设置窗口大小
	Dimension dimension=Toolkit.getDefaultToolkit().getScreenSize();
	private  final int WINDOW_X=dimension.width/4-100;
	private  final int WINDOW_Y=dimension.height/4-100;
	private final int WINDOW_HEIGHT=dimension.height/2;
	private final int WINDOW_WIDTH=dimension.width/2;
	//上部面板大小
	private final int TOPPANEL_X=0;
	private final int TOPPANEL_Y=0;
	private final int TOPPANEL_HEIGHT=(int)(WINDOW_HEIGHT*(1/8.0));
	private final int TOPPANEL_WIDTH=WINDOW_WIDTH;
	//底部面板大小
	private final int BOTTOM_X=0;
	private final int BOTTOM_Y=TOPPANEL_HEIGHT;
	private final int BOTTOM_HEIGHT=(int)(WINDOW_HEIGHT*(7/8.0));
	private final int BOTTOM_WIDTH=WINDOW_WIDTH;
	public Server()
	{  
		Container container=this.getContentPane();
		container.setLayout(null);
		topPanel=new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		topPanel.setBounds(TOPPANEL_X,TOPPANEL_Y,TOPPANEL_WIDTH,TOPPANEL_HEIGHT);
		openServer=new JButton("启动服务器");
		
		openServer.addActionListener(this);
		jButtons.add(openServer);
		
		closeServer=new JButton("关闭服务器");
		closeServer.addActionListener(this);
		jButtons.add(closeServer);
		topPanel.add(openServer);
		topPanel.add(closeServer);
		container.add(topPanel);
		/*绘制第二个面板*/
		showMessagePane=new JScrollPane();
	    bottomPanel=new JPanel();
		showMessageArea=new JTextArea();
		bottomPanel.setLayout(new BorderLayout());
		showMessagePane.getViewport().add(showMessageArea);
		bottomPanel.add(showMessagePane,BorderLayout.CENTER);
		bottomPanel.setBounds(new Rectangle(BOTTOM_X,BOTTOM_Y,BOTTOM_WIDTH,BOTTOM_HEIGHT));
		container.add(bottomPanel);
		this.setVisible(true);
		this.setBounds(WINDOW_X,WINDOW_Y,WINDOW_WIDTH,WINDOW_HEIGHT);
	}
	public void actionPerformed(ActionEvent e)
	{
	   switch(jButtons.indexOf((JButton)e.getSource()))
	   {
	   case 0:
		   if(isOpened)
			   JOptionPane.showMessageDialog(this,"服务器已经启动了！");
		   else
		   {   
			   serverThread=new ServerThread();
			   serverThread.start();
			   
			   cleanDeadThread=new CleanDeadThread(serverThread);
			     cleanDeadThread.start();
			   JOptionPane.showMessageDialog(this,"开启服务器成功");
			   showMessageArea.append("服务器地址："+serverThread.getServerIp()+"："+serverThread.getServerPort()+"\n");
		       isOpened=true;
		   }
			   break;
	   case 1:
		   if(!isOpened)
		      JOptionPane.showMessageDialog(this, "服务器并未启动！");
		   else
			   {
			   JOptionPane.showMessageDialog(this,"退出服务器成功");
			   serverThread.finalize();
			   isOpened=false;
			   }
			   
	   }
	}
	
	
	public static void main(String[] args)
	{  
		Server server=new Server();
		server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	

}
