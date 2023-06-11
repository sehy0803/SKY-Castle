package cyc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ksh.LoginFrame;
import ksh.MainFrame;
import ksh.RoundedButton;
import ksh.User;
// 회원정보 프레임
public class UserInfoFrame extends JFrame implements ActionListener, MouseListener {

	private Font fontA = new Font("맑은 고딕", Font.BOLD, 18);
	private Font fontB = new Font("맑은 고딕", Font.PLAIN, 15);
	private Font fontD = new Font("맑은 고딕", Font.BOLD, 30);
	private Font fontH = new Font("맑은 고딕", Font.PLAIN, 18);
	private JPanel panel;
	
	private RoundedButton btnUnRegister;
	private RoundedButton btnBack;
	
	private JLabel lblId;
	private JLabel lblPw;
	private JLabel lblName;
	private JLabel lblPhone;

	
	private JLabel infoId;
	private JLabel infoPw;
	private JLabel infoName;
	private JLabel infoPhone;

	
	private JPanel whitePanel;
	
	private User user;
	private JLabel lblLogout;
	
	// JDBC 연결 정보
    private static final String URL = "jdbc:mysql://localhost/studycafe";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";
	
	private static String loggedInUserId; // 로그인한 사용자의 아이디
	
	public UserInfoFrame(String userId) {
		this.loggedInUserId = userId;
		setTitle("스카이캐슬");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 800);
		setResizable(false);
		setLocationRelativeTo(null);

		backgroundImage();
		setLogo();
		setWhitePanel();
		setInfoLabel();
		setButton();

		user = getUserInfo(); // 회원정보 가져오기

        setUserInfo();
        
		setVisible(true);
	}

	// 패널 설정
	private void setWhitePanel() {
		whitePanel = new JPanel();
		whitePanel.setBackground(Color.WHITE);
		whitePanel.setBounds(105, 250, 380, 270);
		whitePanel.setLayout(null);
		panel.add(whitePanel);
	}
	
	// 라벨 설정
	private void setInfoLabel() {
		lblLogout = new JLabel("로그아웃");
		lblLogout.setBounds(260, 680, 70, 30);
		lblLogout.setFont(fontB);
		lblLogout.setForeground(Color.gray);
		lblLogout.addMouseListener(this);
		panel.add(lblLogout);
		
		
		JLabel lblUserInfo = new JLabel("회원정보");
		lblUserInfo.setFont(fontD);
		lblUserInfo.setBounds(60, 12, 150, 30);
		lblUserInfo.setForeground(new Color(125, 83, 154));
    	panel.add(lblUserInfo);
    	
		lblName = new JLabel("이름");
		lblId = new JLabel("아이디");
		lblPw = new JLabel("비밀번호");
		lblPhone = new JLabel("전화번호");


		lblName.setFont(fontA);
		lblId.setFont(fontA);
		lblPw.setFont(fontA);
		lblPhone.setFont(fontA);


		lblName.setBounds(30, 30, 100, 30);
		lblId.setBounds(30, 90, 100, 30);
		lblPw.setBounds(30, 150, 100, 30);
		lblPhone.setBounds(30, 210, 100, 30);


		whitePanel.add(lblName);
		whitePanel.add(lblId);
		whitePanel.add(lblPw);
		whitePanel.add(lblPhone);

		
		infoName = new JLabel();
		infoId = new JLabel();
		infoPw = new JLabel();
		infoPhone = new JLabel();


		infoName.setFont(fontH);
		infoId.setFont(fontH);
		infoPw.setFont(fontH);
		infoPhone.setFont(fontH);

		infoName.setForeground(new Color(197, 132, 243));
		infoId.setForeground(new Color(197, 132, 243));
		infoPw.setForeground(new Color(197, 132, 243));
		infoPhone.setForeground(new Color(197, 132, 243));

		infoName.setBounds(150, 30, 200, 30);
		infoId.setBounds(150, 90, 200, 30);
		infoPw.setBounds(150, 150, 200, 30);
		infoPhone.setBounds(150, 210, 200, 30);


		whitePanel.add(infoName);
		whitePanel.add(infoId);
		whitePanel.add(infoPw);
		whitePanel.add(infoPhone);
	}

	// 로고 이미지 설정
	private void setLogo() {
		JPanel logoPanel = new JPanel();
		ImageIcon logo = new ImageIcon("images/logoS.png");
		JLabel logoLabel = new JLabel(logo);
		logoPanel.setOpaque(false);
		logoPanel.add(logoLabel);
		logoPanel.setBounds(0, 0, 600, 300);
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

	// 버튼 설정
	private void setButton() {
		btnUnRegister = new RoundedButton("회원탈퇴");
		btnUnRegister.setFont(fontA);
		btnUnRegister.addActionListener(this);
		btnUnRegister.setBounds(295, 550, 190, 60);
		
		btnBack = new RoundedButton("◀");
		btnBack.setFont(fontB);
		btnBack.addActionListener(this);
		btnBack.setBounds(10, 10, 35, 35);
		panel.add(btnBack);

		panel.add(btnUnRegister);
	}

	// 액션 리스너
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnUnRegister) {
			int confirm = JOptionPane.showConfirmDialog(null, "정말로 회원탈퇴하시겠습니까?", "회원탈퇴", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
	            doUnRegister();
	        }
		} else if (e.getSource() == btnBack) {
			new MainFrame(loggedInUserId);
			setVisible(false);
		}
	}
	
	// 회원정보 가져오기
	public User getUserInfo() {
        String name = ""; // 이름
        String userId = loggedInUserId; // 로그인한 사용자의 아이디
        String password = ""; // 비밀번호
        String phone = ""; // 전화번호

        try {
            // 데이터베이스 연결
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            String query = "SELECT uName, uId, uPw, uPhone FROM users WHERE uId = '" + userId + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                name = resultSet.getString("uName");
                password = resultSet.getString("uPw");
                phone = resultSet.getString("uPhone");
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return new User(name, userId, password, phone);
    }
	
	// 회원 정보 설정
	public void setUserInfo() {
		infoName.setText(user.getName());
		infoId.setText(user.getId());
		infoPw.setText(user.getPw());
		infoPhone.setText(user.getPhone());
	}
   
	// 회원탈퇴 기능
	private void doUnRegister() {
	    String userId = infoId.getText(); // 회원 아이디 가져오기

	    try {
	        // JDBC 드라이버 로드
	        Class.forName("com.mysql.cj.jdbc.Driver");

	        // 데이터베이스 연결
	        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
	        
	        // 외래 키 제약 조건 해제
	        String disableForeignKeyCheck = "SET FOREIGN_KEY_CHECKS=0";
	        PreparedStatement disableFkStmt = conn.prepareStatement(disableForeignKeyCheck);
	        disableFkStmt.executeUpdate();

	        // 회원 삭제
	        String deleteUserQuery = "DELETE FROM users WHERE uId = ?";
	        PreparedStatement deleteUserStmt = conn.prepareStatement(deleteUserQuery);
	        deleteUserStmt.setString(1, userId);
	        int rowsAffected = deleteUserStmt.executeUpdate();

	        // 예약 정보 삭제
	        String deleteReservationQuery = "DELETE FROM reservations WHERE uId = ?";
	        PreparedStatement deleteReservationStmt = conn.prepareStatement(deleteReservationQuery);
	        deleteReservationStmt.setString(1, userId);
	        deleteReservationStmt.executeUpdate();

	        // 좌석 상태 변경
	        String updateSeatStatusQuery = "UPDATE seats SET seatStatus = '예약 가능' WHERE seatNumber IN (SELECT seatNumber FROM reservations WHERE uId = ?)";
	        PreparedStatement updateSeatStatusStmt = conn.prepareStatement(updateSeatStatusQuery);
	        updateSeatStatusStmt.setString(1, userId);
	        updateSeatStatusStmt.executeUpdate();

	        // 외래 키 제약 조건 복원
	        String enableForeignKeyCheck = "SET FOREIGN_KEY_CHECKS=1";
	        PreparedStatement enableFkStmt = conn.prepareStatement(enableForeignKeyCheck);
	        enableFkStmt.executeUpdate();

	        if (rowsAffected > 0) {
	            JOptionPane.showMessageDialog(this, "회원탈퇴가 완료되었습니다.");
	            dispose();
	            new LoginFrame();
	        } else {
	            JOptionPane.showMessageDialog(this, "회원탈퇴에 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
	        }

	        disableFkStmt.close();
	        deleteUserStmt.close();
	        deleteReservationStmt.close();
	        updateSeatStatusStmt.close();
	        enableFkStmt.close();
	        conn.close();
	    } catch (ClassNotFoundException | SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(this, "데이터베이스 오류", "오류", JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	// 마우스 리스너
	@Override
	public void mouseClicked(MouseEvent e) {
		Object obj = e.getSource();
		if(obj == lblLogout) {
			int confirm = JOptionPane.showConfirmDialog(null, "정말로 로그아웃하시겠습니까?", "로그아웃", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
	            setVisible(false);
	            new LoginFrame();
	        }
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}




}