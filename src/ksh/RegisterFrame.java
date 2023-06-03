package ksh;

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

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RegisterFrame extends JFrame implements ActionListener {
    private Font fontA = new Font("맑은 고딕", Font.BOLD, 20);
    private Font fontB = new Font("맑은 고딕", Font.PLAIN, 15);
    private JPanel panel;
    private JPanel whitePanel;
    private RoundedButton btnRegister;
    private RoundedButton btnBack;
    private JTextField tfId;
    private JTextField tfPw;
    private JTextField tfName;
    private JTextField tfPhone;

    // JDBC 연결 정보
    private static final String URL = "jdbc:mysql://localhost/studycafe";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public RegisterFrame() {
        setTitle("스카이캐슬");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 800);
        setResizable(false);
        setLocationRelativeTo(null);

        backgroundImage();
        setWhitePanel();
        setLogo();
        setLabel();
        setInput();
        setButton();

        setVisible(true);
    }

    // 버튼 설정
    private void setButton() {
        btnRegister = new RoundedButton("회원가입");
        btnRegister.setFont(fontA);
        btnRegister.addActionListener(this);
        btnRegister.setBounds(105, 510, 380, 60);
        panel.add(btnRegister);

        btnBack = new RoundedButton("◀");
        btnBack.setFont(fontB);
        btnBack.addActionListener(this);
        btnBack.setBounds(10, 10, 35, 35);
        panel.add(btnBack);
    }

    // 패널 설정
    private void setWhitePanel() {
        whitePanel = new JPanel();
        whitePanel.setBackground(Color.WHITE);
        whitePanel.setBounds(105, 250, 380, 250);
        whitePanel.setLayout(null);
        panel.add(whitePanel);
    }

    // Input 설정
    private void setInput() {
        tfId = new JTextField();
        tfId.setBounds(160, 30, 180, 30);
        whitePanel.add(tfId);

        tfPw = new JTextField();
        tfPw.setBounds(160, 80, 180, 30);
        whitePanel.add(tfPw);

        tfName = new JTextField();
        tfName.setBounds(160, 130, 180, 30);
        whitePanel.add(tfName);

        tfPhone = new JTextField();
        tfPhone.setBounds(160, 180, 180, 30);
        whitePanel.add(tfPhone);
    }

    // Label 설정
    private void setLabel() {
        JLabel lblId = new JLabel("아이디");
        lblId.setFont(fontA);
        lblId.setBounds(30, 30, 100, 30);
        whitePanel.add(lblId);

        JLabel lblPw = new JLabel("비밀번호");
        lblPw.setFont(fontA);
        lblPw.setBounds(30, 80, 100, 30);
        whitePanel.add(lblPw);

        JLabel lblName = new JLabel("이름");
        lblName.setFont(fontA);
        lblName.setBounds(30, 130, 100, 30);
        whitePanel.add(lblName);

        JLabel lblPhone = new JLabel("전화번호");
        lblPhone.setFont(fontA);
        lblPhone.setBounds(30, 180, 100, 30);
        whitePanel.add(lblPhone);
    }

    // 로고 이미지 설정
    private void setLogo() {
        JPanel logoPanel = new JPanel();
        ImageIcon logo = new ImageIcon("images/logoS.png");
        JLabel logoLabel = new JLabel(logo);
        logoPanel.setOpaque(false);
        logoPanel.add(logoLabel);
        logoPanel.setBounds(140, 0, 300, 300);
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

    // 액션 리스너
    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == btnRegister) {
            doRegister();
        } else if (obj == btnBack) {
            new LoginFrame();
            setVisible(false);
        }
    }

    // 회원가입 기능
    private void doRegister() {
        String id = tfId.getText();
        String password = tfPw.getText();
        String name = tfName.getText();
        String phone = tfPhone.getText();

        if (id.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "빈 칸을 모두 입력해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // JDBC 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 데이터베이스 연결
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // 중복된 아이디 확인
            String checkQuery = "SELECT * FROM users WHERE uId = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, id);
            ResultSet resultSet = checkStmt.executeQuery();
            
            if (resultSet.next()) {
                JOptionPane.showMessageDialog(this, "이미 존재하는 아이디입니다.", "오류", JOptionPane.ERROR_MESSAGE);
                resultSet.close();
                checkStmt.close();
                conn.close();
                return;
            }

            // 쿼리 작성 및 실행
            String query = "INSERT INTO users (uId, uPw, uName, uPhone) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, id);
            pstmt.setString(2, password);
            pstmt.setString(3, name);
            pstmt.setString(4, phone);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "회원가입 성공!");
                new LoginFrame();
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "회원가입 실패", "회원가입 실패", JOptionPane.ERROR_MESSAGE);
            }

            // 연결 종료
            resultSet.close();
            checkStmt.close();
            pstmt.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        RegisterFrame registerFrame = new RegisterFrame();
    }
}
