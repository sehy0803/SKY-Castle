package cyc;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ksh.MainFrame;
import ksh.RoundedButton;

public class UserInfoFrame extends JFrame implements ActionListener{

	private Font fontA = new Font("맑은 고딕", Font.BOLD, 20);
	private Font fontB = new Font("", Font.PLAIN, 15);
    private JPanel panel;
    private RoundedButton btnUserinfo;
	private RoundedButton btnUnRegister;
	private RoundedButton btnBack;

	public UserInfoFrame() {
    	setTitle("스카이캐슬");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 800);
		setResizable(false);
		setLocationRelativeTo(null);

        backgroundImage();
        setLogo();
        
        setButton();
        setbackButton();



        setVisible(true);
    }

	 private void setbackButton() {
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
	
	//버튼 설정
	private void setButton() {
		btnUserinfo = new RoundedButton("정보 보기");
		btnUserinfo.setFont(fontA);
		btnUserinfo.addActionListener(this);
		btnUserinfo.setBounds(85, 400, 200, 100);

		btnUnRegister = new RoundedButton("회원탈퇴");
		btnUnRegister.setFont(fontA);
		btnUnRegister.addActionListener(this);
		btnUnRegister.setBounds(300, 400, 200, 100);
		
		panel.add(btnUserinfo);
		panel.add(btnUnRegister);
	}
    public static void main(String[] args) {
    	UserInfoFrame basicFrame = new UserInfoFrame();
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj == btnUserinfo) {
			new InfoFrame();
			setVisible(false);
		}
		else if(obj == btnUnRegister) {
			
		}
		else if(obj == btnBack) {			
	    		new MainFrame();
	    		setVisible(false);
		}
	}
}
