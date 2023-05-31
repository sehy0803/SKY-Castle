package cyc;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ksh.LoginFrame;
import ksh.MainFrame;
import ksh.RegisterFrame;
import ksh.RoundedButton;

public class UserInfoFrame extends JFrame implements ActionListener{

	private Font fontA = new Font("맑은 고딕", Font.BOLD, 20);
	private Font fontB = new Font("", Font.PLAIN, 15);
    private JPanel panel;
	private RoundedButton btnUnRegister;
	private RoundedButton btnBack;
	private JLabel lblName;
	private JLabel lblId;
	private JLabel lblPw;
	private JLabel lblSeat;
	private JLabel lblTime;
	private JPanel whitePanel;
	private JLabel lblUname;

	public UserInfoFrame() {
    	setTitle("스카이캐슬");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 800);
		setResizable(false);
		setLocationRelativeTo(null);
		
        backgroundImage();
        setLogo();
        setWhitePanel();
        setInfoTitle();
        setInfo();
        setButton();




        setVisible(true);
    }
	

	// 패널 설정
	private void setWhitePanel() {
		whitePanel = new JPanel();
		whitePanel.setBackground(Color.WHITE);
		whitePanel.setBounds(105, 250, 380, 375);
		whitePanel.setLayout(null);
		panel.add(whitePanel);
	}
	
	// 회원 정보 설정
	private void setInfo() {
		// 회원 정보 들어갈 예시
		lblUname = new JLabel("스카이");
		lblUname.setFont(fontA);
		lblUname.setForeground(new Color(197, 132, 243));
		lblUname.setBounds(150, 30, 100, 30);
		
		whitePanel.add(lblUname);
	}
	
	// 라벨 설정
	private void setInfoTitle() {
		 lblName = new JLabel("이름");
		 lblId = new JLabel("아이디");
		 lblPw = new JLabel("비밀번호");
		 lblSeat = new JLabel("좌석번호");
		 lblTime = new JLabel("예약시간");
		 
		 lblName.setFont(fontA);
		 lblId.setFont(fontA);
		 lblPw.setFont(fontA);
		 lblSeat.setFont(fontA);
		 lblTime.setFont(fontA);
		 
		 lblName.setBounds(30, 30, 100, 30);
		 lblId.setBounds(30, 100, 100, 30);
		 lblPw.setBounds(30, 170, 100, 30);
		 lblSeat.setBounds(30, 240, 100, 30);
		 lblTime.setBounds(30, 310, 100, 30);
		
		 
		 whitePanel.add(lblName);
		 whitePanel.add(lblId);
		 whitePanel.add(lblPw);
		 whitePanel.add(lblSeat);
		 whitePanel.add(lblTime);
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
		btnUnRegister.setBounds(420, 660, 120, 60);
		
		btnBack = new RoundedButton("◀");
		btnBack.setFont(fontB);
		btnBack.addActionListener(this);
		btnBack.setBounds(10, 10, 35, 35);
		panel.add(btnBack);
		
		panel.add(btnUnRegister);
	}
	
	// tfID, tfPW 가져오기
	public void getMethod() {
        String id = RegisterFrame.tfID.getText();
        String password = RegisterFrame.tfPW.getText();
    }
	
	// 회원정보 가져오기
	private void getUserInfo(String id) {
	    final String URL = "jdbc:mysql://localhost/studycafe";
	    final String USER = "root";
	    final String PASSWORD = "1234";
	    Connection conn = null;
	    try {
	        conn = DriverManager.getConnection(URL, USER, PASSWORD);

	        String query = "SELECT uName, uID, uPW, uSeat, uTime FROM user WHERE uID = ?";
	        PreparedStatement pstmt = conn.prepareStatement(query);
	        pstmt.setString(1, id);
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            String name = rs.getString("uName");
	            String userID = rs.getString("uID");
	            String password = rs.getString("uPW");
	            String seat = rs.getString("uSeat");
	            String time = rs.getString("uTime");

	            // 가져온 회원 정보를 화면에 표시
	            lblUname.setText(name);
	            lblId.setText(userID);
	            lblPw.setText(password);
	            lblSeat.setText(seat);
	            lblTime.setText(time);
	        }

	        rs.close();
	        pstmt.close();
	    } catch (SQLException e) {
	        System.out.println("회원 정보 조회 실패: " + e.toString());
	    } finally {
	        if (conn != null) {
	            try {
	                conn.close();
	            } catch (SQLException e) {
	                System.out.println("Connection 닫기 실패: " + e.toString());
	            }
	        }
	    }
	}

	// 회원탈퇴 기능
	private void doUnregister() {
	    final String URL = "jdbc:mysql://localhost/studycafe";
	    final String USER = "root";
	    final String PASSWORD = "1234";

	    Connection conn = null;
	    Statement stmt = null;

	    String enteredID = tfID.getText();
	    String enteredPW = tfPW.getText();

	    try {
	        conn = DriverManager.getConnection(URL, USER, PASSWORD);
	        stmt = conn.createStatement();

	        String query = "DELETE FROM user WHERE uID='" + enteredID + "' AND uPW='" + enteredPW + "'";
	        int rowsAffected = stmt.executeUpdate(query);

	        if (rowsAffected > 0) {
	            JOptionPane.showMessageDialog(null, "회원탈퇴 완료");
	            dispose();
	            new LoginFrame();
	        } else {
	            JOptionPane.showMessageDialog(null, "아이디 또는 비밀번호가 올바르지 않습니다.");
	        }
	        stmt.close();
	        conn.close();
	    } catch (SQLException ex) {
	        JOptionPane.showMessageDialog(null, "회원탈퇴 중 오류가 발생했습니다: " + ex.getMessage());
	    }
	}

    public static void main(String[] args) {
    	UserInfoFrame userInfoFrame = new UserInfoFrame();
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == btnUnRegister) {
			setVisible(false);
		}
		else if(obj == btnBack) {
			new MainFrame();
	    	setVisible(false);
		}
	}
}
