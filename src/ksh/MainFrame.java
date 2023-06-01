package ksh;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cyc.UserInfoFrame;

public class MainFrame extends JFrame implements ActionListener {
    private Font fontA = new Font("맑은 고딕", Font.BOLD, 20);
    private JPanel panel;
    private RoundedButton btnReserve;
    private RoundedButton btnMovement;
    private RoundedButton btnLeave;
    private RoundedButton btnMySeat;
    private RoundedButton btnUserInfo;
    private RoundedButton btnStatus;

    private String loggedInUserId; // 로그인한 사용자의 아이디

    public MainFrame(String userId) {
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
        btnReserve = new RoundedButton("좌석예약");
        btnReserve.setFont(fontA);
        btnReserve.addActionListener(this);
        btnReserve.setBounds(85, 300, 200, 100);

        btnMovement = new RoundedButton("좌석이동");
        btnMovement.setFont(fontA);
        btnMovement.addActionListener(this);
        btnMovement.setBounds(300, 300, 200, 100);

        btnStatus = new RoundedButton("좌석현황");
        btnStatus.setFont(fontA);
        btnStatus.addActionListener(this);
        btnStatus.setBounds(85, 415, 200, 100);

        btnLeave = new RoundedButton("퇴실");
        btnLeave.setFont(fontA);
        btnLeave.addActionListener(this);
        btnLeave.setBounds(300, 415, 200, 100);

        btnMySeat = new RoundedButton("내 자리");
        btnMySeat.setFont(fontA);
        btnMySeat.addActionListener(this);
        btnMySeat.setBounds(85, 530, 200, 100);

        btnUserInfo = new RoundedButton("회원정보");
        btnUserInfo.setFont(fontA);
        btnUserInfo.addActionListener(this);
        btnUserInfo.setBounds(300, 530, 200, 100);

        panel.add(btnReserve);
        panel.add(btnMovement);
        panel.add(btnStatus);
        panel.add(btnLeave);
        panel.add(btnMySeat);
        panel.add(btnUserInfo);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == btnReserve) {
            // 좌석예약
            new SeatReserveFrame(loggedInUserId);
            setVisible(false);
        } else if (obj == btnMovement) {
            // 좌석이동
        } else if (obj == btnStatus) {
            // 좌석현황
            new SeatStatusFrame(loggedInUserId);
            setVisible(false);
        } else if (obj == btnLeave) {
            // 퇴실
            new LoginFrame();
            setVisible(false);
        } else if (obj == btnMySeat) {
            // 내 좌석
        } else if (obj == btnUserInfo) {
            // 회원정보
            new UserInfoFrame(loggedInUserId);
            setVisible(false);
        }
    }
    
}
