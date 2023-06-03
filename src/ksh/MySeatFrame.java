package ksh;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;

import ksh.RoundedButton;

public class MySeatFrame extends JFrame implements ActionListener {
    private Font fontA = new Font("맑은 고딕", Font.BOLD, 18);
    private Font fontB = new Font("맑은 고딕", Font.PLAIN, 15);
    private Font fontD = new Font("맑은 고딕", Font.BOLD, 30);
    private Font fontH = new Font("맑은 고딕", Font.PLAIN, 18);
    private JPanel panel;
    private RoundedButton btnBack;
	private JPanel whitePanel;
	private JLabel lblMySeat;
	private JLabel lblReservationTime;
	private JLabel lblReservationStartTime;
	private JLabel lblReservationEndTime;
	private JLabel infoMySeat;
	private JLabel infoReservationTime;
	private JLabel infoReservationStartTime;
	private JLabel infoReservationEndTime;
    
    private static String loggedInUserId; // 로그인한 사용자의 아이디
    
    private static final String URL = "jdbc:mysql://localhost/studycafe";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public MySeatFrame(String userId) {
        this.loggedInUserId = userId;
        setTitle("스카이캐슬");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 800);
        setResizable(false);
        setLocationRelativeTo(null);

        backgroundImage();
        setLogo();
        setButton();
        setWhitePanel();
        setLabel();
        setLabel();
        showSeatInformation();
        
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
 	private void setLabel() { 
 		JLabel mySeat = new JLabel("내 좌석");
    	mySeat.setFont(fontD);
    	mySeat.setBounds(60, 12, 150, 30);
    	mySeat.setForeground(new Color(125, 83, 154));
    	panel.add(mySeat);
    	
 		lblMySeat = new JLabel("좌석 번호");
 		lblReservationTime = new JLabel("예약 시간");
 		lblReservationStartTime = new JLabel("시작 시간");
 		lblReservationEndTime = new JLabel("종료 시간");


 		lblMySeat.setFont(fontA);
 		lblReservationTime.setFont(fontA);
 		lblReservationStartTime.setFont(fontA);
 		lblReservationEndTime.setFont(fontA);


 		lblMySeat.setBounds(30, 30, 100, 30);
 		lblReservationTime.setBounds(30, 90, 100, 30);
 		lblReservationStartTime.setBounds(30, 150, 100, 30);
 		lblReservationEndTime.setBounds(30, 210, 100, 30);


 		whitePanel.add(lblMySeat);
 		whitePanel.add(lblReservationTime);
 		whitePanel.add(lblReservationStartTime);
 		whitePanel.add(lblReservationEndTime);

 		
 		infoMySeat = new JLabel();
 		infoReservationTime = new JLabel();
 		infoReservationStartTime = new JLabel();
 		infoReservationEndTime = new JLabel();


 		infoMySeat.setFont(fontH);
 		infoReservationTime.setFont(fontH);
 		infoReservationStartTime.setFont(fontH);
 		infoReservationEndTime.setFont(fontH);
 		
 		infoMySeat.setForeground(new Color(197, 132, 243));
 		infoReservationTime.setForeground(new Color(197, 132, 243));
 		infoReservationStartTime.setForeground(new Color(197, 132, 243));
 		infoReservationEndTime.setForeground(new Color(197, 132, 243));

 		infoMySeat.setBounds(150, 30, 200, 30);
 		infoReservationTime.setBounds(150, 90, 200, 30);
 		infoReservationStartTime.setBounds(150, 150, 200, 30);
 		infoReservationEndTime.setBounds(150, 210, 200, 30);


 		whitePanel.add(infoMySeat);
 		whitePanel.add(infoReservationTime);
 		whitePanel.add(infoReservationStartTime);
 		whitePanel.add(infoReservationEndTime);
 	}

	// 버튼 설정
	private void setButton() {
        btnBack = new RoundedButton("◀");
        btnBack.setFont(fontB);
        btnBack.addActionListener(this);
        btnBack.setBounds(10, 10, 35, 35);
        panel.add(btnBack);
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
        setContentPane(panel);
        return panel;
    }

    public static void main(String[] args) {
    	MySeatFrame mySeatFrame = new MySeatFrame(loggedInUserId);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == btnBack) {
			new MainFrame(loggedInUserId);
			setVisible(false);
		}
	}
	
	// 좌석 정보 표시
	private void showSeatInformation() {
	    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
	        String selectReservationSql = "SELECT * FROM reservations WHERE uId = ?";
	        PreparedStatement selectReservationStatement = conn.prepareStatement(selectReservationSql);
	        selectReservationStatement.setString(1, loggedInUserId);
	        ResultSet reservationRs = selectReservationStatement.executeQuery();

	        if (reservationRs.next()) {
	            int seatNumber = reservationRs.getInt("seatNumber");
	            int reservationTime = reservationRs.getInt("reservationTime");
	            Timestamp reservationStartTime = reservationRs.getTimestamp("reservationStartTime");
	            Timestamp reservationEndTime = reservationRs.getTimestamp("reservationEndTime");
	            
	            infoMySeat.setText(String.valueOf(seatNumber));
	            infoReservationTime.setText(String.valueOf(reservationTime));
	            infoReservationStartTime.setText(reservationStartTime.toString());
	            infoReservationEndTime.setText(reservationEndTime.toString());
	            
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}

}
