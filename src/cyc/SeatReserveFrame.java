package cyc;

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

import ksh.MainFrame;
import ksh.NewReserveFrame;
import ksh.RoundedButton;
// 좌석예약 프레임
public class SeatReserveFrame extends JFrame implements ActionListener {
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
    
    // JDBC 연결 정보
    private static final String URL = "jdbc:mysql://localhost/studycafe";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public SeatReserveFrame(String userId) {
        this.loggedInUserId = userId;
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
    
    // 라벨 설정
    private void setLabel() {
    	JLabel lblSeatReserve = new JLabel("좌석예약");
    	lblSeatReserve.setFont(fontD);
    	lblSeatReserve.setBounds(60, 12, 150, 30);
    	lblSeatReserve.setForeground(new Color(125, 83, 154));
    	panel.add(lblSeatReserve);
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
    	// 좌석번호와 상태를 저장할 배열 생성
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
    
    // 액션 리스너
    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();

        if (obj == btnBack) {
            MainFrame mainFrame = new MainFrame(loggedInUserId);
            setVisible(false);
        } else {
            // 좌석 버튼 클릭 시 동작
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 5; col++) {
                    if (obj == seatButtons[row][col]) {
                        if (!canReserveMoreSeats()) { // 더 이상 예약 가능한 좌석이 없는 경우
                            JOptionPane.showMessageDialog(this, "더 이상 예약할 수 없습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        int confirmation = JOptionPane.showConfirmDialog(this, "해당 좌석을 예약하시겠습니까?", "좌석 예약", JOptionPane.YES_NO_OPTION);
                        if (confirmation == JOptionPane.YES_OPTION) {
                            openReservationFrame(row, col);
                            return;
                        }
                        break;
                    }
                }
            }

        }
    }
    
    // 예약한 좌석이 없는 경우
    private boolean canReserveMoreSeats() {
        int reservedSeatCount = 0;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
        	// 예약 테이블에서 회원의 예약 수를 reservedSeats 변수에 저장
            String loadUserReservationsSql = "SELECT COUNT(*) AS reservedSeats FROM reservations WHERE uId = ?";
            PreparedStatement pstmt = conn.prepareStatement(loadUserReservationsSql);
            pstmt.setString(1, loggedInUserId);
            ResultSet userReservationsResult = pstmt.executeQuery();

            if (userReservationsResult.next()) {
                reservedSeatCount = userReservationsResult.getInt("reservedSeats");
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return reservedSeatCount < 1; // 예약한 좌석의 수가 1 이하일 때. 즉 예약이 없는 경우
    }

    // 좌석 선택 후 결제 프레임으로 이동
    private void openReservationFrame(int row, int col) {
        NewReserveFrame newReserveFrame = new NewReserveFrame(loggedInUserId, row, col);
        setVisible(false);
        return;
    }
    
    // 좌석 상태 가져오기
    private void loadSeatStatus() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
        	// 좌석 테이블에서 좌석번호와 좌석상태 가져오기
            String loadSeatsSql = "SELECT seatNumber, seatStatus FROM seats";
            Statement stmt = conn.createStatement();
            ResultSet seatsResult = stmt.executeQuery(loadSeatsSql);

            while (seatsResult.next()) {
                int seatNumber = seatsResult.getInt("seatNumber");
                int row = (seatNumber - 1) / 5;
                int col = (seatNumber - 1) % 5;

                String seatStatusStr = seatsResult.getString("seatStatus");
                seatStatus[row][col] = seatStatusStr.equals("예약 가능");

                // 예약 가능한 좌석은 좌석번호(숫자)로, 예약된 좌석은 "사용중"(문자)으로 표시
                String seatStatusText = seatStatus[row][col] ? Integer.toString(seatNumber) : "사용중";
                if(seatStatusText.equals("사용중")) {
                	seatButtons[row][col].setEnabled(false); // 예약된 좌석은 비활성화
                }
                seatButtons[row][col].setText(seatStatusText);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}