package ksh;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RegisterFrame extends JFrame implements ActionListener {
	private Font fontA = new Font("맑은 고딕", Font.BOLD, 20);
	private Font fontB = new Font("", Font.PLAIN, 15);
    private JPanel panel;
	private JPanel whitePanel;
	private RoundedButton btnRegister;
	private RoundedButton btnBack;
	private JTextField tfID;
	private JTextField tfPW;
	private JTextField tfName;
	private JTextField tfTel;

	public RegisterFrame() {
    	setTitle("스카이캐슬");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 800);
		setResizable(false);
		setLocationRelativeTo(null);

        backgroundImage();
        setWhitePanel();
        setLogo();
        setLabel();
        setInput();
        setButton();

        setVisible(true);
    }

	// 버튼 설정
	private void setButton() {
		btnRegister = new RoundedButton("회원가입");
		btnRegister.setFont(fontA);
		btnRegister.addActionListener(this);
		btnRegister.setBounds(105, 510, 380, 60);
		panel.add(btnRegister);

		btnBack = new RoundedButton("◀");
		btnBack.setFont(fontB);
		btnBack.addActionListener(this);
		btnBack.setBounds(10, 10, 35, 35);
		panel.add(btnBack);
	}

	// 패널 설정
	private void setWhitePanel() {
		whitePanel = new JPanel();
		whitePanel.setBackground(Color.WHITE);
		whitePanel.setBounds(105, 250, 380, 250);
		whitePanel.setLayout(null);
		panel.add(whitePanel);
	}

	// Input 설정
	private void setInput() {
		tfID = new JTextField();
		tfID.setBounds(160, 30, 180, 30);
		whitePanel.add(tfID);

		tfPW = new JTextField();
		tfPW.setBounds(160, 80, 180, 30);
		whitePanel.add(tfPW);

		tfName = new JTextField();
		tfName.setBounds(160, 130, 180, 30);
		whitePanel.add(tfName);

		tfTel = new JTextField();
		tfTel.setBounds(160, 180, 180, 30);
		whitePanel.add(tfTel);
	}

	// Label 설정
	private void setLabel() {
		JLabel lblID = new JLabel("아이디");
		lblID.setFont(fontA);
		lblID.setBounds(30, 30, 100, 30);
		whitePanel.add(lblID);

		JLabel lblPW = new JLabel("비밀번호");
		lblPW.setFont(fontA);
		lblPW.setBounds(30, 80, 100, 30);
		whitePanel.add(lblPW);

		JLabel lblNAME = new JLabel("이름");
		lblNAME.setFont(fontA);
		lblNAME.setBounds(30, 130, 100, 30);
		whitePanel.add(lblNAME);

		JLabel lblTEL = new JLabel("전화번호");
		lblTEL.setFont(fontA);
		lblTEL.setBounds(30, 180, 100, 30);
		whitePanel.add(lblTEL);
	}

	// 로고 이미지 설정
	private void setLogo() {
		JPanel logoPanel = new JPanel();
		ImageIcon logo = new ImageIcon("images/logoS.png");
		JLabel logoLabel = new JLabel(logo);
		logoPanel.setOpaque(false);
		logoPanel.add(logoLabel);
		logoPanel.setBounds(140, 0, 300, 300);
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
        setContentPane(panel); // JFrame의 컨텐트팬을 JPanel로 설정
		return panel;
	}
	
	// 데이터베이스 연동
	private void Database() {
		final String URL = "jdbc:mysql://localhost/studycafe";
		final String USER = "root";
		final String PASSWORD = "1234";

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = conn.createStatement();
            System.out.println("MySQL 서버 연동 성공");
        } catch (Exception e) {
            System.out.println("MySQL 서버 연동 실패 : " + e.toString());
        }
    }
	
	// 회원가입 기능
	private void insertUser(String id, String password, String name, String tel) {
		final String URL = "jdbc:mysql://localhost/studycafe";
		final String USER = "root";
		final String PASSWORD = "1234";
		Connection conn = null;
	    try {
	        // Connection 객체 생성 및 초기화
	        conn = DriverManager.getConnection(URL, USER, PASSWORD);

	        String query = "INSERT INTO user (uID, uPW, uName, uTel) VALUES (?, ?, ?, ?)";
	        PreparedStatement pstmt = conn.prepareStatement(query);
	        pstmt.setString(1, id);
	        pstmt.setString(2, password);
	        pstmt.setString(3, name);
	        pstmt.setString(4, tel);
	        pstmt.executeUpdate();
	        System.out.println("회원가입 성공");
	        LoginFrame loginFrame = new LoginFrame();
	        setVisible(false);
	    } catch (SQLException e) {
	        System.out.println("회원가입 실패: " + e.toString());
	    } finally {
	        // Connection 객체를 닫아줌
	        if (conn != null) {
	            try {
	                conn.close();
	            } catch (SQLException e) {
	                System.out.println("Connection 닫기 실패: " + e.toString());
	            }
	        }
	    }
	}


    public static void main(String[] args) {
    	RegisterFrame registerFrame = new RegisterFrame();
    }
    
    
    
    // 액션 리스너
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == btnRegister) {
			String id = tfID.getText();
            String pw = tfPW.getText();
            String name = tfName.getText();
            String tel = tfTel.getText();

            insertUser(id, pw, name, tel);
		} else if (obj == btnBack){
			new LoginFrame();
			setVisible(false);
		}
	}
}