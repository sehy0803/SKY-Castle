package ksh;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import ksh.RoundedButton;

public class SeatStatusFrame extends JFrame implements ActionListener {
	private Font fontA = new Font("맑은 고딕", Font.BOLD, 20);
	private Font fontB = new Font("", Font.PLAIN, 15);
    private JPanel panel;
    private JButton[][] seats;
    private boolean[][] reserved;  // 좌석 예약 상태를 저장할 배열
	private RoundedButton btnBack;

    public SeatStatusFrame() {
        setTitle("스카이캐슬");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 800);
        setResizable(false);
        setLocationRelativeTo(null);

        backgroundImage();
        setLogo();
        setButton();
        seatLayout();

        setVisible(true);
    }

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
        setContentPane(panel); // JFrame의 컨텐트팬을 JPanel로 설정
        return panel;
    }

    // 좌석배치도 설정
    private void seatLayout() {
        int rows = 5;
        int columns = 5;

        JPanel seatPanel = new JPanel();
        seatPanel.setOpaque(false);
        seatPanel.setBounds(23, 240, 540, 500);
        seatPanel.setLayout(new GridLayout(rows, columns, 10, 10));
        panel.add(seatPanel);

        // 좌석배치도 버튼 생성
        seats = new RoundedButton[rows][columns];
        reserved = new boolean[rows][columns];  // 좌석 예약 상태 배열 초기화

        int seatNumber = 1; // 좌석 번호

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                RoundedButton seatButton = new RoundedButton(String.valueOf(seatNumber));
                seats[row][col] = seatButton;
                seatButton.setFont(fontA);
                seatButton.addActionListener(this);
                seatPanel.add(seatButton);

                seatNumber++;
            }
        }
        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SeatStatusFrame());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	Object obj = e.getSource();
    	if(obj==btnBack) {
    		new MainFrame();
    		setVisible(false);
    	}
    	
        JButton seatButton = (JButton) e.getSource();
        int row = -1;
        int col = -1;

        // 선택된 좌석의 인덱스(row, col) 찾기
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                if (seats[i][j] == seatButton) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }

        if (row >= 0 && col >= 0) {
            if (reserved[row][col]) {
                // 이미 예약된 좌석을 선택한 경우, 예약 해제
                reserved[row][col] = false;
                seatButton.setText(String.valueOf(row * seats[row].length + col + 1));
            } else {
                // 예약되지 않은 좌석을 선택한 경우, 예약
                int choice = JOptionPane.showConfirmDialog(SeatStatusFrame.this,
                        "해당 좌석을 예약하시겠습니까?", "좌석 예약", JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    reserved[row][col] = true;
                    seatButton.setText("예약됨");
                }
            }
        }
    }


}
