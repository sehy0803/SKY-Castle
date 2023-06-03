package ksh;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

public class RoundedButton extends JButton {
	public RoundedButton() { super(); decorate(); } 
	public RoundedButton(String text) { super(text); decorate(); } 
	public RoundedButton(Action action) { super(action); decorate(); } 
	public RoundedButton(Icon icon) { super(icon); decorate(); } 
	public RoundedButton(String text, Icon icon) { super(text, icon); decorate(); } 
	protected void decorate() { setBorderPainted(false); setOpaque(false); }
	@Override
	protected void paintComponent(Graphics g) {
	    Color c = new Color(255, 255, 255); // 배경색 결정
	    Color o = new Color(197, 132, 243); // 글자색 결정
	    Color h = new Color(250, 242, 255); // 배경색 결정
	    Color d = new Color(245, 230, 255); // 배경색 결정
	    int width = getWidth();
	    int height = getHeight();
	    Graphics2D graphics = (Graphics2D) g;
	    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    if (getModel().isArmed()) {
	        graphics.setColor(d); // 마우스를 눌렀을 때
	    } else if (getModel().isRollover()) {
	        graphics.setColor(h); // 마우스를 올렸을 때
	    } else {
	        graphics.setColor(c);
	    }
	    graphics.fillRoundRect(0, 0, width, height, 10, 10);
	    FontMetrics fontMetrics = graphics.getFontMetrics();
	    Rectangle stringBounds = fontMetrics.getStringBounds(this.getText(), graphics).getBounds();
	    int textX = (width - stringBounds.width) / 2;
	    int textY = (height - stringBounds.height) / 2 + fontMetrics.getAscent();
	    graphics.setColor(o);
	    graphics.setFont(getFont());
	    graphics.drawString(getText(), textX, textY);
	    graphics.dispose();
	    super.paintComponent(g);
	}

}
