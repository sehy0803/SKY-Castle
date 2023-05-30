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

public class InfoFrame extends JFrame implements ActionListener {

	private Font fontA = new Font("맑은 고딕", Font.BOLD, 20);
	private Font fontB = new Font("", Font.PLAIN, 15);
    private JPanel panel;
    private RoundedButton btnBack;
	private JLabel idlbl;
	private JLabel seatlbl;
	private JLabel timelbl;
	private JLabel namelbl;

	public InfoFrame() {
    	setTitle("스카이캐슬");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 800);
		setResizable(false);
		setLocationRelativeTo(null);

        backgroundImage();
        setLogo();
        setInfo();
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

	private void setInfo() {
		namelbl = new JLabel("이름 : ");
		namelbl.setFont(fontA);
		namelbl.setBounds(85, 300, 100, 100);
		
		idlbl = new JLabel("아이디 : ");
		idlbl.setFont(fontA);
		idlbl.setBounds(85, 400, 100, 100);
		
		seatlbl = new JLabel("좌석 : ");
		seatlbl.setFont(fontA);
		seatlbl.setBounds(85, 500, 100, 100);
		
		timelbl = new JLabel("예약시간 : ");
		timelbl.setFont(fontA);
		timelbl.setBounds(85, 600, 100, 100);
		
		panel.add(namelbl);
		panel.add(idlbl);
		panel.add(seatlbl);
		panel.add(timelbl);
		
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
    	InfoFrame basicFrame = new InfoFrame();
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
    	if(obj==btnBack) {
    		new UserInfoFrame();
    		setVisible(false);
    	}		
	}
}
