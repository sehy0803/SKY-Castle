package ksh;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ksh.RoundedButton;

public class SeatMovementFrame extends JFrame implements ActionListener {
    private Font fontA = new Font("맑은 고딕", Font.BOLD, 20);
    private Font fontB = new Font("맑은 고딕", Font.PLAIN, 15);
    private Font fontC = new Font("맑은 고딕", Font.BOLD, 25);
    private Font fontD = new Font("맑은 고딕", Font.BOLD, 30);
    private Font fontE = new Font("맑은 고딕", Font.BOLD, 20);
    private JPanel panel;
    private RoundedButton btnBack;
    private RoundedButton[][] seatButtons;
    private boolean[][] seatStatus;
    
    private static String loggedInUserId; // 로그인한 사용자의 아이디
    
    private static final String URL = "jdbc:mysql://localhost/studycafe";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";
    
    private int previousRow; // 이전 좌석의 행
    private int previousCol; // 이전 좌석의 열
    
    public SeatMovementFrame(String userId, int previousRow, int previousCol) {
        this.loggedInUserId = userId;
        this.previousRow = previousRow;
        this.previousCol = previousCol;
        setTitle("스카이캐슬");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 800);
        setResizable(false);
        setLocationRelativeTo(null);

        backgroundImage();
        setLogo();
        setButton();
        setLabel();
        setSeatButtons();

        setVisible(true);
    }
    
    private void setLabel() {
    	JLabel lblSeatStatus = new JLabel("좌석이동");
    	lblSeatStatus.setFont(fontD);
    	lblSeatStatus.setBounds(60, 12, 150, 30);
    	lblSeatStatus.setForeground(new Color(125, 83, 154));
    	panel.add(lblSeatStatus);
	}

	// 버튼 설정
    private void setButton() {
        btnBack = new RoundedButton("◀");
        btnBack.setFont(fontB);
        btnBack.addActionListener(this);
        btnBack.setBounds(10, 10, 35, 35);
        panel.add(btnBack);
    }
    
    // 좌석 설정
    private void setSeatButtons() {
        seatButtons = new RoundedButton[5][5];
        seatStatus = new boolean[5][5];

        JPanel seatPanel = new JPanel();
        seatPanel.setLayout(new GridLayout(5, 5, 10, 10));
        seatPanel.setOpaque(false);
        seatPanel.setBounds(45, 250, 500, 500);

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                RoundedButton seatButton = new RoundedButton((row * 5 + col + 1) + "번");
                seatButton.setFont(fontE);
                seatButton.addActionListener(this);
                seatPanel.add(seatButton);
                seatButtons[row][col] = seatButton;
            }
        }

        panel.add(seatPanel);
        loadSeatStatus();
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

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == btnBack) {
            new MainFrame(loggedInUserId);
            setVisible(false);
        } else {
            // 좌석 버튼 클릭 시 동작
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 5; col++) {
                    if (obj == seatButtons[row][col]) {
                        int confirmation = JOptionPane.showConfirmDialog(this, "해당 좌석으로 이동하시겠습니까?", "좌석이동", JOptionPane.YES_NO_OPTION);
                        if (confirmation == JOptionPane.YES_OPTION) {
                            if (seatStatus[row][col]) {
                                int seatNumber = row * 5 + col + 1;
                                updateSeatStatus(seatNumber, "예약 불가능");
                                updatePreviousSeatStatus(previousRow, previousCol);
                                JOptionPane.showMessageDialog(this, "좌석이 이동되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                                setVisible(false);
                                new MainFrame(loggedInUserId);
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    // 이동한 좌석 상태 업데이트
    private void updateSeatStatus(int seatNumber, String status) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
        	// seats 테이블 업데이트
        	String updateSeatStatusSql = "UPDATE seats SET seatStatus = ? WHERE seatNumber = ?";
        	PreparedStatement updateSeatStatusStatement = conn.prepareStatement(updateSeatStatusSql);
        	updateSeatStatusStatement.setString(1, status);
        	updateSeatStatusStatement.setInt(2, seatNumber);
        	updateSeatStatusStatement.executeUpdate();
        	
            // reservations 테이블 업데이트
            String updateReservationSeatSql = "UPDATE reservations SET seatNumber = ? WHERE uId = ?";
            PreparedStatement updateReservationSeatStatement = conn.prepareStatement(updateReservationSeatSql);
            updateReservationSeatStatement.setInt(1, seatNumber);
            updateReservationSeatStatement.setString(2, loggedInUserId);
            updateReservationSeatStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    
    // 이전 좌석 상태 업데이트
    private void updatePreviousSeatStatus(int row, int col) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String updateSeatStatusSql = "UPDATE seats SET seatStatus = ? WHERE seatNumber = ?";
            PreparedStatement updateSeatStatusStatement = conn.prepareStatement(updateSeatStatusSql);
            updateSeatStatusStatement.setString(1, "예약 가능");
            int seatNumber = row * 5 + col + 1;
            updateSeatStatusStatement.setInt(2, seatNumber);
            updateSeatStatusStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    
    // 좌석 상태 가져오기
    private void loadSeatStatus() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String loadSeatsSql = "SELECT seatNumber, seatStatus FROM seats";
            Statement stmt = conn.createStatement();
            ResultSet seatsResult = stmt.executeQuery(loadSeatsSql);

            while (seatsResult.next()) {
                int seatNumber = seatsResult.getInt("seatNumber");
                int row = (seatNumber - 1) / 5;
                int col = (seatNumber - 1) % 5;

                String seatStatusStr = seatsResult.getString("seatStatus");
                seatStatus[row][col] = seatStatusStr.equals("예약 가능");

                String seatStatusText = seatStatus[row][col] ? Integer.toString(seatNumber) : "예약 불가능";
                if(seatStatusText.equals("예약 불가능")) {
                	seatButtons[row][col].setEnabled(false);
                }
                seatButtons[row][col].setText(seatStatusText);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
