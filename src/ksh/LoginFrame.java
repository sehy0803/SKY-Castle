package ksh;

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

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginFrame extends JFrame implements ActionListener {
    private Font fontA = new Font("맑은 고딕", Font.BOLD, 20);
    private JPanel panel;
    private RoundedButton btnLogin;
    private RoundedButton btnRegister;
    private JTextField tfId;
    private JPasswordField tfPw;

    // JDBC 연결 정보
    private static final String URL = "jdbc:mysql://localhost/studycafe";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";
    
    public LoginFrame() {
        setTitle("스카이캐슬");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 800);
        setResizable(false);
        setLocationRelativeTo(null);

        backgroundImage();
        setLogo();
        setLabel();
        setInput();
        setButton();

        setVisible(true);
    }

    // 버튼 설정
    private void setButton() {
        btnLogin = new RoundedButton("로그인");
        btnLogin.setBounds(165, 560, 250, 40);
        btnLogin.setFont(fontA);
        btnLogin.addActionListener(this);
        panel.add(btnLogin);

        btnRegister = new RoundedButton("회원가입");
        btnRegister.setBounds(165, 605, 250, 40);
        btnRegister.setFont(fontA);
        btnRegister.addActionListener(this);
        panel.add(btnRegister);
    }

    // 아이콘 설정
    private void setLabel() {
        ImageIcon icID = new ImageIcon("images/userIcon.png");
        JLabel lblID = new JLabel(icID);
        lblID.setBounds(120, 480, 35, 35);
        panel.add(lblID);

        ImageIcon icPW = new ImageIcon("images/passIcon.png");
        JLabel lblPW = new JLabel(icPW);
        lblPW.setBounds(120, 520, 35, 35);
        panel.add(lblPW);
    }

    // Input 설정
    private void setInput() {
        tfId = new JTextField();
        tfId.setBounds(165, 480, 250, 35);
        tfId.setFont(fontA);
        panel.add(tfId);

        tfPw = new JPasswordField();
        tfPw.setBounds(165, 520, 250, 35);
        tfPw.setFont(fontA);
        panel.add(tfPw);
    }

    // 로고 이미지 설정
    private void setLogo() {
        JPanel logoPanel = new JPanel();
        ImageIcon logo = new ImageIcon("images/logoL.png");
        JLabel logoLabel = new JLabel(logo);
        logoPanel.setOpaque(false);
        logoPanel.add(logoLabel);
        logoPanel.setBounds(0, 0, 570, 600);
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
        setContentPane(panel); // JFrame의 컨텐트 팬을 JPanel으로 설정
        return panel;
    }

    public static void main(String[] args) {
        LoginFrame loginFrame = new LoginFrame();
    }

    // 액션 리스너
    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == btnLogin) {
            doLogin();
        } else if (obj == btnRegister) {
            new RegisterFrame();
            setVisible(false);
        }
    }

    // 로그인 기능
    private void doLogin() {
        String id = tfId.getText();
        String password = tfPw.getText();

        if (id.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 모두 입력해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // JDBC 드라이버 로드
        	Class.forName("com.mysql.cj.jdbc.Driver");

            // 데이터베이스 연결
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // 쿼리 작성 및 실행
            String query = "SELECT * FROM users WHERE uId = ? AND uPw = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, id);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // 로그인 성공
                String userId = rs.getString("uId"); // 회원 아이디 가져오기
                JOptionPane.showMessageDialog(this, "로그인 성공!");
                
                dispose();
                
                MainFrame mainFrame = new MainFrame(userId); // 아이디를 전달하여 MainFrame 생성
                mainFrame.setVisible(true);
            } else {
                // 로그인 실패
                JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호가 올바르지 않습니다.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
            }

            // 자원 해제
            rs.close();
            pstmt.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "데이터베이스 오류", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
}
