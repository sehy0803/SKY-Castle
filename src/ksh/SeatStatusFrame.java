package ksh;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SeatStatusFrame extends JFrame implements ActionListener {
	private Font fontA = new Font("맑은 고딕", Font.BOLD, 20);
	private Font fontB = new Font("", Font.PLAIN, 15);
    private JPanel panel;
    private JButton[][] seats;
	private RoundedButton btnBack;

	private static String loggedInUserId; // 로그인한 사용자의 아이디
    
    public SeatStatusFrame(String userId) {
    	this.loggedInUserId = userId;
        setTitle("스카이캐슬");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 800);
        setResizable(false);
        setLocationRelativeTo(null);
        
        backgroundImage();
        setLogo();
        setButton();

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

    public static void main(String[] args) {
    	SeatStatusFrame seatStatusFrame = new SeatStatusFrame(loggedInUserId);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	Object obj = e.getSource();
    	if(obj == btnBack) {
    		new MainFrame(loggedInUserId);
    		setVisible(false);
    	}
    }
}