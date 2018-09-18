package loginWindowInfo;

import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Container;
import javax.swing.Box;

import client.Client;

import java.awt.Dimension;

import java.net.URL;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import user.UserInfo;

public class LoginWindow extends JFrame implements ActionListener {

	/* 设置标签 */
	JLabel usrnameLabel;
	JLabel passwordLabel;

	/* 设置文本框 */
	JTextField usrnameInput;
	JPasswordField passwordInput;;

	/* 按钮集合 */
	ArrayList<JButton> jButtons;

	/* 按钮 */
	JButton loginButton;
	JButton registerButton;
	/* 设置面板 */
	JPanel usrnamePanel;
	JPanel passwordPanel;
	JPanel buttonPanel;
	ImagePanel imagePanel;

	/* 设置各组件大小 */
	Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	final int WINDOW_X = dimension.width / 3;
	final int WINDOW_Y = dimension.height / 3;
	final int WINDOW_WIDTH = dimension.width / 3;
	final int WINDOW_HEIGHT = dimension.height / 3;
	/* 设置一个连接验证类 */
	LoginConnector loginConnector;
	// 设置一个聊天室窗口
	Client client;

	// 定义一个内部类用来绘制图像
	class ImagePanel extends JPanel {
		private ImageIcon imageIcon;
		private Image image;

		public ImagePanel() {
			URL url = getClass().getResource("momo.png");
			this.imageIcon = new ImageIcon(url);
			this.image = imageIcon.getImage();
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
		}

	}

	public LoginWindow() {

		this.setLayout(null);
		initView();
		this.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setTitle("陌陌");
		// 居中显示
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
		this.setBackground(new Color(255, 255, 255));

	}

	private void initView() {
		Container container = this.getContentPane();

		jButtons = new ArrayList<JButton>();
		// 创建一个图片面板

		imagePanel = new ImagePanel();
		imagePanel.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT / 2);

		// 创建一个用户名面板
		usrnamePanel = new JPanel();
		usrnamePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		// 创建一个密码面板
		passwordPanel = new JPanel();
		passwordPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		// 配置usrnamePanel面板
		usrnameLabel = new JLabel("用户名:");
		usrnamePanel.add(usrnameLabel);
		usrnameInput = new JTextField(20);
		usrnamePanel.add(usrnameInput);
		usrnamePanel.setBounds(0, WINDOW_Y / 2, WINDOW_WIDTH,
				(int) (WINDOW_HEIGHT * (1 / 7.0)));

		// 配置passwordPanel面板；
		passwordLabel = new JLabel("密码:   ");
		passwordPanel.add(passwordLabel);
		passwordInput = new JPasswordField(20);
		passwordPanel.add(passwordInput);
		passwordPanel.setBounds(0, WINDOW_Y / 2
				+ (int) (WINDOW_HEIGHT * (1 / 7.0)), WINDOW_WIDTH,
				(int) (WINDOW_HEIGHT * (1 / 7.0)));
		// 配置buttonPanel面板
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		loginButton = new JButton("登录");
		jButtons.add(loginButton);
		loginButton.addActionListener(this);

		registerButton = new JButton("注册");
		jButtons.add(registerButton);
		registerButton.addActionListener(this);
		buttonPanel.add(loginButton);
		buttonPanel.add(registerButton);
		buttonPanel.setBounds(0, WINDOW_Y / 2 + 2
				* (int) (WINDOW_HEIGHT * (1 / 7.0)), WINDOW_WIDTH,
				(int) (WINDOW_HEIGHT * (1 / 6.0)));
		container.add(imagePanel);
		container.add(usrnamePanel);
		container.add(passwordPanel);
		container.add(buttonPanel);

	}

	public void actionPerformed(ActionEvent e) {
		switch (jButtons.indexOf(e.getSource())) {
		case 0:
			String usrname = usrnameInput.getText().trim();
			String password = new String(passwordInput.getPassword()).trim();

			if (loginVerify(usrname, password)) {
				UserInfo userInfo = loginConnector.getLoginUserInfo();
				this.dispose();
				client = new Client();
				client.getInformationFromLogin(userInfo);
				client.initView();
			} else {
				JOptionPane.showMessageDialog(this, "您输入的账号密码有误！");
			}
			break;
		case 1:
			JDialog registerDialog = new RegisterDialog();

		}

	}

	boolean loginVerify(String usrname, String password) {
		loginConnector = new LoginConnector(usrname, password);

		if (loginConnector.verifyLogin())
			return true;
		else
			return false;
	}

	public static void main(String[] args) {

		LoginWindow window = new LoginWindow();

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
