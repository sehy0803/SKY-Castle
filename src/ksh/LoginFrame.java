package ksh;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginFrame extends JFrame implements ActionListener {
	private Font fontA = new Font("맑은 고딕", Font.BOLD, 20);
    private JPanel panel;
	private RoundedButton btnLogin;
	private RoundedButton btnRegister;
	private JTextField tfID;
	private JTextField tfPW;

	public LoginFrame() {
    	setTitle("스카이캐슬");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 800);
		setResizable(false);
		setLocationRelativeTo(null);

        backgroundImage();
        setLogo();
        setLabel();
        setInput();
        setButton();

        setVisible(true);
    }

	// 버튼 설정
	private void setButton() {
		btnLogin = new RoundedButton("로그인");
		btnLogin.setBounds(165, 560, 250, 40);
		btnLogin.setFont(fontA);
		btnLogin.addActionListener(this);
		panel.add(btnLogin);

		btnRegister = new RoundedButton("회원가입");
		btnRegister.setBounds(165, 605, 250, 40);
		btnRegister.setFont(fontA);
		btnRegister.addActionListener(this);
		panel.add(btnRegister);
	}

	// 아이콘 설정
	private void setLabel() {
		ImageIcon icID = new ImageIcon("images/userIcon.png");
		JLabel lblID = new JLabel(icID);
		lblID.setBounds(120, 480, 35, 35);
		panel.add(lblID);

		ImageIcon icPW = new ImageIcon("images/passIcon.png");
		JLabel lblPW = new JLabel(icPW);
		lblPW.setBounds(120, 520, 35, 35);
		panel.add(lblPW);
	}


	// Input 설정
	private void setInput() {
		tfID = new JTextField();
		tfID.setBounds(165, 480, 250, 35);
		tfID.setFont(fontA);
		panel.add(tfID);

		tfPW = new JTextField();
		tfPW.setBounds(165, 520, 250, 35);
		tfPW.setFont(fontA);
		panel.add(tfPW);
	}

	// 로고 이미지 설정
	private void setLogo() {
		JPanel logoPanel = new JPanel();
		ImageIcon logo = new ImageIcon("images/logoL.png");
		JLabel logoLabel = new JLabel(logo);
		logoPanel.setOpaque(false);
		logoPanel.add(logoLabel);
		logoPanel.setBounds(0, 0, 570, 600);
		panel.add(logoPanel);
	}

	// 배경 이미지 설정
	private JPanel backgroundImage() {
        panel = new JPanel() {
        private Image background = new ImageIcon("images/background.png").getImage();

        @Override
        protected void paintComponent(Graphics g) {
        	super.paintComponent(g);
        	g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);
        setContentPane(panel); // JFrame의 컨텐트 팬을 JPanel으로 설정
		return panel;
	}

	// 로그인 기능
	private void DoLogin() {
		final String URL = "jdbc:mysql://localhost/studycafe";
		final String USER = "root";
		final String PASSWORD = "1234";

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

        String enteredID = tfID.getText();
        String enteredPW = tfPW.getText();

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = conn.createStatement();

            String query = "SELECT * FROM user WHERE uID='" + enteredID + "' AND uPW='" + enteredPW + "'";
            rs = stmt.executeQuery(query);

            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "로그인 성공");
                MainFrame mf = new MainFrame();
				dispose();
            } else {
                JOptionPane.showMessageDialog(null, "아이디 또는 비밀번호가 올바르지 않습니다.");
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "로그인 중 오류가 발생했습니다: " + ex.getMessage());
        }
    }

	
	
    public static void main(String[] args) {
    	LoginFrame loginFrame = new LoginFrame();
    }


    
    // 액션 리스너
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == btnLogin) {
			DoLogin();
		} else if (obj == btnRegister) {
			new RegisterFrame();
			setVisible(false);
		}
	}
}