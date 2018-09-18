package loginWindowInfo;

import javax.swing.*;

import client.Client;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegisterDialog extends JDialog implements ActionListener {

	/* 设置一个注册的数据库连接器 */
	RegisterConnector registerConnector;
	/* 设置三个面板 */
	JPanel usernamePanel;
	JPanel passwordPanel;
	JPanel buttonPanel;
	/* 设置两个标签 */
	JLabel usernameLabel;
	JLabel passwordLabel;
	/* 设置两个输入框 */
	JTextField usernameInput;
	JPasswordField passwordInput;
	/* 设置两个按钮 */
	JButton registerButton;
	JButton cancelButton;
	/* 设置对话框的大小尺寸 */
	Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	final int WINDOW_X = dimension.width / 3;
	final int WINDOW_Y = dimension.height / 3;
	final int WINDOW_WIDTH = dimension.width / 3;
	final int WINDOW_HEIGHT = dimension.height / 5;

	public RegisterDialog() {
		this.setLayout(null);
		/* 创建三个面板 */
		usernamePanel = new JPanel();
		usernamePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		usernamePanel
				.setBounds(0, 0, WINDOW_WIDTH, (int) (WINDOW_HEIGHT / 3.0));
		passwordPanel = new JPanel();
		passwordPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		passwordPanel.setBounds(0, (int) (WINDOW_HEIGHT / 3.0), WINDOW_WIDTH,
				(int) (WINDOW_HEIGHT / 3.0));
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setBounds(0, 2 * (int) (WINDOW_HEIGHT / 3.0), WINDOW_WIDTH,
				(int) (WINDOW_HEIGHT / 3.0));
		/* 配置username面板 */
		usernameLabel = new JLabel("用户名:");
		usernameInput = new JTextField(20);
		usernamePanel.add(usernameLabel);
		usernamePanel.add(usernameInput);
		/* 配置password面板 */
		passwordLabel = new JLabel("密码：");
		passwordInput = new JPasswordField(20);
		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordInput);
		/* 配置button面板 */
		registerButton = new JButton("注册");
		cancelButton = new JButton("取消");
		registerButton.addActionListener(this);
		cancelButton.addActionListener(this);
		buttonPanel.add(registerButton);
		buttonPanel.add(cancelButton);
		Container container = this.getContentPane();
		container.add(usernamePanel);
		container.add(passwordPanel);
		container.add(buttonPanel);
		this.setBounds(WINDOW_X, WINDOW_Y, WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setVisible(true);
		this.setTitle("注册");
		this.setResizable(false);

	}

	public void actionPerformed(ActionEvent e) {
		if ((JButton) e.getSource() == registerButton) {
			String usrname = usernameInput.getText().trim();
			String password = new String(passwordInput.getPassword()).trim();

			if (registerVerify(usrname, password)) {

				JOptionPane.showMessageDialog(this, "您已注册成功");
			}

		}
		if ((JButton) e.getSource() == cancelButton)
			this.dispose();

	}

	boolean registerVerify(String username, String password) {
		boolean registerSuccess = false;
		registerConnector = new RegisterConnector();

		// 判断用户名
		if (registerConnector.verifyRegisterName(username, password)) {
			String nickname = JOptionPane.showInputDialog(this, "请输入您要注册的昵称：")
					.trim();
			// 判断昵称名

			if (registerConnector.verifyRegisterNickname(nickname)) {
				registerConnector
						.createNewAccount(username, password, nickname);
				registerSuccess = true;
			} else {
				JOptionPane.showMessageDialog(this, "您注册的昵称已被使用！");
				registerSuccess = false;
			}
		} else
			JOptionPane.showMessageDialog(this, "您注册的用户名已被使用！");
		return registerSuccess;
	}
}
