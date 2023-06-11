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
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;

import ksh.RoundedButton;
// 결제 프레임
public class NewReserveFrame extends JFrame implements ActionListener {
    private Font fontA = new Font("맑은 고딕", Font.BOLD, 20);
    private Font fontB = new Font("맑은 고딕", Font.PLAIN, 15);
    private Font fontC = new Font("맑은 고딕", Font.BOLD, 25);
    private Font fontD = new Font("맑은 고딕", Font.BOLD, 30);
    private JPanel panel;
    private RoundedButton btnBack;
    private JComboBox<String> timeComboBox;
	private RoundedButton btnPay;
	private JTable table;
    
    private static String loggedInUserId; // 로그인한 사용자의 아이디
    
    private static int row; // 사용자가 선택한 좌석 - 행
    private static int col; // 사용자가 선택한 좌석 - 열
    
    // JDBC 연결 정보
    private static final String URL = "jdbc:mysql://localhost/studycafe";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public NewReserveFrame(String userId, int row, int col) {
        this.loggedInUserId = userId;
        this.row = row;
        this.col = col;
        setTitle("스카이캐슬");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 800);
        setResizable(false);
        setLocationRelativeTo(null);

        backgroundImage();
        setLogo();
        setButton();
        setLabel();
        createTable();
        setTimeComboBox();
        
        setVisible(true);
    }
    
    // 라벨 설정
    private void setLabel() {
    	JLabel payList = new JLabel("결제");
    	payList.setFont(fontD);
    	payList.setBounds(60, 12, 150, 30);
    	payList.setForeground(new Color(125, 83, 154));
    	panel.add(payList);
	}

	// 가격표 설정
    private void createTable() {
        String[] columnNames = {"시간", "가격"};
        String[][] data = {
                {"2시간", "4,000원"},
                {"4시간", "6,000원"},
                {"6시간", "7,000원"},
                {"12시간", "10,000원"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        table = new JTable(model);
        table.setEnabled(false); // 테이블 비활성화

        // 테이블 스타일 설정
        table.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        
        // 테이블 외곽선
        table.setBorder(BorderFactory.createLineBorder(new Color(197, 132, 243), 2));
        
        // 시간 배경색 설정
        DefaultTableCellRenderer timeRenderer = new DefaultTableCellRenderer();
        timeRenderer.setHorizontalAlignment(JLabel.CENTER);
        timeRenderer.setBackground(new Color(250, 242, 255)); // 배경색 설정
        table.getColumnModel().getColumn(0).setCellRenderer(timeRenderer);
        
        // 글자를 중앙에 배치
        DefaultTableCellRenderer rendererCenter = new DefaultTableCellRenderer();
        rendererCenter.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, rendererCenter);
        
        table.setRowHeight(60);
        table.setBounds(105, 310, 380, 240);
        
        panel.add(table);
    }
    
    // 콤보박스 설정
    private void setTimeComboBox() {
        String[] times = {"2시간", "4시간", "6시간", "12시간"};
        timeComboBox = new JComboBox<>(times);
        timeComboBox.setFont(fontA);
        timeComboBox.setBounds(105, 550, 190, 60);
        timeComboBox.setBackground(Color.white);
        
        // Renderer를 사용하여 텍스트를 중앙에 배치하고 배경색 설정
        timeComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                JLabel label = (JLabel) component;
                label.setHorizontalAlignment(JLabel.CENTER); // 텍스트를 중앙에 배치

                if (isSelected) {
                    label.setBackground(new Color(245, 230, 255)); // 선택된 항목의 배경색 변경
                } else {
                    label.setBackground(Color.WHITE); // 선택되지 않은 항목의 배경색 초기화
                }

                return label;
            }
        });

        panel.add(timeComboBox);
    }
	
	// 버튼 설정
	private void setButton() {
        btnBack = new RoundedButton("◀");
        btnBack.setFont(fontB);
        btnBack.addActionListener(this);
        btnBack.setBounds(10, 10, 35, 35);
        panel.add(btnBack);
        
        btnPay = new RoundedButton("결제");
        btnPay.setFont(fontC);
        btnPay.addActionListener(this);
        btnPay.setBounds(295, 550, 190, 60);
        panel.add(btnPay);
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

        } else if (obj == btnPay) {
            String selectedTime = (String) timeComboBox.getSelectedItem();
            int selectedHours = getSelectedHours(selectedTime);

            if (selectedHours != -1) {
                int confirmation = JOptionPane.showConfirmDialog(this, selectedTime + " 예약하시겠습니까?", "예약", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    int totalPrice = calculateTotalPrice(selectedHours);
                    boolean paymentSuccessful = performPayment(totalPrice);
                    if (paymentSuccessful) {
                        reserveSeatInDatabase(row, col, selectedHours); // 예약 시간 전달
                        JOptionPane.showMessageDialog(this, "예약이 완료되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                        new MainFrame(loggedInUserId);
                        setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(this, "예약에 실패했습니다.", "알림", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
    
    // 예약 시간
    private int getSelectedHours(String selectedTime) {
        if (selectedTime.equals("2시간")) {
            return 2;
        } else if (selectedTime.equals("4시간")) {
            return 4;
        } else if (selectedTime.equals("6시간")) {
            return 6;
        } else if (selectedTime.equals("12시간")) {
            return 12;
        } else {
            return -1;
        }
    }
    
    // 이용요금
    private int calculateTotalPrice(int selectedHours) {
        if (selectedHours == 2) {
            return 4000;
        } else if (selectedHours == 4) {
            return 6000;
        } else if (selectedHours == 6) {
            return 7000;
        } else if (selectedHours == 12) {
            return 10000;
        } else {
            return 0;
        }
    }
    
    // 결제 과정
    private boolean performPayment(int totalPrice) {
        return true; // 일단 무조건 결제 성공
    }

    // 데이터베이스에 정보 저장
    private void reserveSeatInDatabase(int row, int col, int reservationTime) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // 예약 정보 저장
            String insertReservationSql = "INSERT INTO reservations (uId, seatNumber, reservationTime, reservationStartTime, reservationEndTime) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertReservationStatement = conn.prepareStatement(insertReservationSql, Statement.RETURN_GENERATED_KEYS);
            insertReservationStatement.setString(1, loggedInUserId);
            insertReservationStatement.setInt(2, row * 5 + col + 1);
            insertReservationStatement.setInt(3, reservationTime);

            // 시작 시간 계산
            Timestamp reservationStartTime = new Timestamp(System.currentTimeMillis());
            insertReservationStatement.setTimestamp(4, reservationStartTime);

            // 종료 시간 계산
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(reservationStartTime.getTime());
            cal.add(Calendar.HOUR, reservationTime);
            Timestamp reservationEndTime = new Timestamp(cal.getTime().getTime());
            insertReservationStatement.setTimestamp(5, reservationEndTime);

            insertReservationStatement.executeUpdate();

            // 예약된 좌석의 상태를 "사용중"으로 업데이트
            String updateSeatsSql = "UPDATE seats SET seatStatus = '사용중' WHERE seatNumber = ?";
            PreparedStatement updateSeatsStatement = conn.prepareStatement(updateSeatsSql);
            updateSeatsStatement.setInt(1, row * 5 + col + 1);
            updateSeatsStatement.executeUpdate();

            // 예약 정보 출력
            System.out.println("선택한 예약시간: " + reservationTime);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            System.out.println("예약 시작 시간: " + dateFormat.format(reservationStartTime));
            System.out.println("예약 종료 시간: " + dateFormat.format(reservationEndTime));

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }



}
