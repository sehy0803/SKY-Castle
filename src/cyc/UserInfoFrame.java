package cyc;

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
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ksh.LoginFrame;
import ksh.MainFrame;
import ksh.RoundedButton;
import ksh.User;

public class UserInfoFrame extends JFrame implements ActionListener {

	private Font fontA = new Font("맑은 고딕", Font.BOLD, 18);
	private Font fontB = new Font("", Font.PLAIN, 15);
	private JPanel panel;
	private RoundedButton btnUnRegister;
	private RoundedButton btnBack;
	private JLabel lblName;
	private JLabel lblId;
	private JLabel lblPw;
	private JLabel lblSeat;
	private JLabel lblTime;
	private JLabel lblUname;
	private JLabel tfID;
	private JLabel tfPW;
	private JLabel lblReservedSeat;
	private JPanel whitePanel;
	private JLabel lblPhone;
	private User user;
	
	private static String loggedInUserId; // 로그인한 사용자의 아이디
	
	public UserInfoFrame(String userId) {
		this.loggedInUserId = userId;
		setTitle("스카이캐슬");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 800);
		setResizable(false);
		setLocationRelativeTo(null);

		backgroundImage();
		setLogo();
		setWhitePanel();
		setInfoTitle();
		setButton();

		user = getUserInfo(); // 회원 정보 가져오기

        setUserInfo(); // 회원 정보 설정
        
		setVisible(true);
	}

	// 패널 설정
	private void setWhitePanel() {
		whitePanel = new JPanel();
		whitePanel.setBackground(Color.WHITE);
		whitePanel.setBounds(105, 250, 380, 390);
		whitePanel.setLayout(null);
		panel.add(whitePanel);
	}

	// 라벨 설정
	private void setInfoTitle() {
		lblName = new JLabel("이름");
		lblId = new JLabel("아이디");
		lblPw = new JLabel("비밀번호");
		lblPhone = new JLabel("전화번호");
		lblSeat = new JLabel("좌석번호");
		lblTime = new JLabel("예약시간");

		lblName.setFont(fontA);
		lblId.setFont(fontA);
		lblPw.setFont(fontA);
		lblPhone.setFont(fontA);
		lblSeat.setFont(fontA);
		lblTime.setFont(fontA);

		lblName.setBounds(30, 30, 100, 30);
		lblId.setBounds(30, 90, 100, 30);
		lblPw.setBounds(30, 150, 100, 30);
		lblPhone.setBounds(30, 210, 100, 30);
		lblSeat.setBounds(30, 270, 100, 30);
		lblTime.setBounds(30, 330, 100, 30);

		whitePanel.add(lblName);
		whitePanel.add(lblId);
		whitePanel.add(lblPw);
		whitePanel.add(lblPhone);
		whitePanel.add(lblSeat);
		whitePanel.add(lblTime);
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

	// 버튼 설정
	private void setButton() {
		btnUnRegister = new RoundedButton("회원탈퇴");
		btnUnRegister.setFont(fontA);
		btnUnRegister.addActionListener(this);
		btnUnRegister.setBounds(420, 660, 120, 60);

		btnBack = new RoundedButton("◀");
		btnBack.setFont(fontB);
		btnBack.addActionListener(this);
		btnBack.setBounds(10, 10, 35, 35);
		panel.add(btnBack);

		panel.add(btnUnRegister);
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnUnRegister) {
			int confirm = JOptionPane.showConfirmDialog(null, "정말로 회원탈퇴하시겠습니까?", "회원탈퇴", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
	            doUnRegister();
	        }
		} else if (e.getSource() == btnBack) {
			new MainFrame(loggedInUserId);
			setVisible(false);
		}
	}
	
	public static void main(String[] args) {
		new UserInfoFrame(loggedInUserId);
	}
	
	// 회원정보 가져오기
	public User getUserInfo() {
        String name = ""; // 이름
        String userId = loggedInUserId; // 로그인한 사용자의 아이디
        String password = ""; // 비밀번호
        String phone = ""; // 전화번호
        int seatNumber = 0; // 좌석번호
        String reservedTime = ""; // 예약시간

        try {
            // 데이터베이스 연결 설정
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/studycafe", "root", "1234");

            // 쿼리 실행
            String query = "SELECT uName, uId, uPw, uPhone, uSeat, reservation_time FROM users WHERE uId = '" + userId + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // 결과 처리
            if (resultSet.next()) {
                name = resultSet.getString("uName");
                password = resultSet.getString("uPw");
                phone = resultSet.getString("uPhone");
                seatNumber = resultSet.getInt("uSeat");
                reservedTime = resultSet.getString("reservation_time");
            }

            // 연결 및 리소스 해제
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new User(name, userId, password, phone, seatNumber, reservedTime);
    }
	
	// 회원 정보 설정
    public void setUserInfo() {
        lblUname = new JLabel(user.getName());
        lblUname.setFont(fontA);
        lblUname.setBounds(150, 30, 200, 30);
        whitePanel.add(lblUname);

        tfID = new JLabel(user.getId());
        tfID.setFont(fontA);
        tfID.setBounds(150, 90, 200, 30);
        whitePanel.add(tfID);

        tfPW = new JLabel(user.getPassword());
        tfPW.setFont(fontA);
        tfPW.setBounds(150, 150, 200, 30);
        whitePanel.add(tfPW);

        lblPhone = new JLabel(user.getPhone());
        lblPhone.setFont(fontA);
        lblPhone.setBounds(150, 210, 200, 30);
        whitePanel.add(lblPhone);

        lblReservedSeat = new JLabel(Integer.toString(user.getSeatNumber()));
        lblReservedSeat.setFont(fontA);
        lblReservedSeat.setBounds(150, 270, 200, 30);
        whitePanel.add(lblReservedSeat);

        lblTime = new JLabel(user.getReservationTime());
        lblTime.setFont(fontA);
        lblTime.setBounds(150, 330, 200, 30);
        whitePanel.add(lblTime);
    }
   
    // 회원탈퇴 기능
    private void doUnRegister() {
        try {
            // 데이터베이스 연결 설정
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/studycafe", "root", "1234");

            // 쿼리 실행
            String query = "DELETE FROM users WHERE uId = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, loggedInUserId);
            int rowsAffected = pstmt.executeUpdate();

            // 연결 및 리소스 해제
            pstmt.close();
            connection.close();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "회원탈퇴가 완료되었습니다.", "회원탈퇴", JOptionPane.INFORMATION_MESSAGE);
                setVisible(false);
                new LoginFrame();
            } else {
                JOptionPane.showMessageDialog(this, "회원탈퇴에 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "데이터베이스 오류", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

}
