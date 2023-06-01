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

import ksh.RoundedButton;

public class SeatReserveFrame extends JFrame implements ActionListener {
    private Font fontA = new Font("맑은 고딕", Font.BOLD, 20);
    private Font fontB = new Font("", Font.PLAIN, 15);
    private JPanel panel;
    private RoundedButton btnBack;
    private JButton[][] seatButtons;
    private boolean[][] seatStatus;
    
    private static String loggedInUserId; // 로그인한 사용자의 아이디
    
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
        setSeatButtons();

        setVisible(true);
    }

    private void setButton() {
        btnBack = new RoundedButton("◀");
        btnBack.setFont(fontB);
        btnBack.addActionListener(this);
        btnBack.setBounds(10, 10, 35, 35);
        panel.add(btnBack);
    }

    private void setSeatButtons() {
        seatButtons = new JButton[5][5];
        seatStatus = new boolean[5][5];

        JPanel seatPanel = new JPanel();
        seatPanel.setLayout(new GridLayout(5, 5, 10, 10));
        seatPanel.setOpaque(false);
        seatPanel.setBounds(50, 350, 500, 400);

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                JButton seatButton = new JButton((row * 5 + col + 1) + "번");
                seatButton.setFont(fontB);
                seatButton.addActionListener(this);
                seatPanel.add(seatButton);
                seatButtons[row][col] = seatButton;
                seatStatus[row][col] = false;
            }
        }

        panel.add(seatPanel);
        loadReservedSeats(); // 예약된 좌석을 로드하여 표시
    }

    private void setLogo() {
        JPanel logoPanel = new JPanel();
        ImageIcon logo = new ImageIcon("images/logoS.png");
        JLabel logoLabel = new JLabel(logo);
        logoPanel.setOpaque(false);
        logoPanel.add(logoLabel);
        logoPanel.setBounds(0, 0, 600, 300);
        panel.add(logoPanel);
    }

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
        SeatReserveFrame seatReserveFrame = new SeatReserveFrame(loggedInUserId);
    }

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
                        if (seatStatus[row][col]) {
                            int confirmation = JOptionPane.showConfirmDialog(this, "해당 좌석의 예약을 취소하시겠습니까?", "예약 취소", JOptionPane.YES_NO_OPTION);
                            if (confirmation == JOptionPane.YES_OPTION) {
                                seatStatus[row][col] = false;
                                seatButtons[row][col].setBackground(null); // 예약이 취소된 좌석은 배경색을 초기화
                                cancelReservationFromDatabase(row, col); // 데이터베이스에서 예약 정보 삭제

                                // 예약 정보를 MainFrame으로 전달
                                MainFrame mainFrame = new MainFrame(loggedInUserId);
                                
                            }
                        } else {
                            int confirmation = JOptionPane.showConfirmDialog(this, "해당 좌석을 예약하시겠습니까?", "좌석 예약", JOptionPane.YES_NO_OPTION);
                            if (confirmation == JOptionPane.YES_OPTION) {
                                seatStatus[row][col] = true;
                                seatButtons[row][col].setBackground(Color.RED); // 예약된 좌석은 빨간색으로 표시
                                reserveSeatInDatabase(row, col); // 데이터베이스에 예약 정보 저장

                                // 예약 정보를 MainFrame으로 전달
                                MainFrame mainFrame = new MainFrame(loggedInUserId);
                                
                            }
                        }
                        break;
                    }
                }
            }
        }
    }
    
    // 좌석 상태 가져오기
    private void loadReservedSeats() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT seatNumber FROM reservations WHERE uId = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, loggedInUserId);

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                int seatNumber = result.getInt("seatNumber");
                int row = (seatNumber - 1) / 5;
                int col = (seatNumber - 1) % 5;
                seatStatus[row][col] = true;
                seatButtons[row][col].setBackground(Color.RED);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // 좌석 상태 저장
    private void reserveSeatInDatabase(int row, int col) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO reservations (uId, reservationDate, seatNumber) VALUES (?, NOW(), ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, loggedInUserId);
            statement.setInt(2, row * 5 + col + 1);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // 예약 취소
    private void cancelReservationFromDatabase(int row, int col) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "DELETE FROM reservations WHERE uId = ? AND seatNumber = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, loggedInUserId);
            statement.setInt(2, row * 5 + col + 1);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
