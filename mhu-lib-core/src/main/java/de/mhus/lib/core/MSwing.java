package de.mhus.lib.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;

import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.UIManager;

public class MSwing {

	/**
	 * Convert string information to color.
	 * 
	 * @param string
	 * @param def
	 * @return the color
	 */
	public static Color toColor(String string, Color def) {
		Color out = null;
		if (string == null)
			return def;
		if ("blue".equals(string))
			return Color.BLUE;
		if ("green".equals(string))
			return Color.GREEN;
		if ("grey".equals(string))
			return Color.GRAY;
		if ("yellow".equals(string))
			return Color.YELLOW;
		if ("light_grey".equals(string))
			return Color.LIGHT_GRAY;
		if ("red".equals(string))
			return Color.RED;

		out = Color.getColor(string);
		if (out == null)
			out = def;
		return out;
	}
	
	/**
	
	 * 
	 * Cast a Image to a Buffered Image. The new image is a
	 * BufferedImage.TYPE_3BYTE_BGR type. Perhaps some informations of the
	 * origin image will be lost.
	 * @param _in 
	 * @return  the image
	 */

	public static BufferedImage toBufferedImage(Image _in) {

		BufferedImage out = new BufferedImage(_in.getWidth(null), _in
				.getHeight(null), BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = out.getGraphics();
		g.drawImage(_in, 0, 0, null);
		_in.flush();

		return out;
	}

	//
	public static BufferedImage toBufferedImage(Image _in, int _width,
			int _height) {

		BufferedImage out = new BufferedImage(_width, _height,
				BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = out.getGraphics();
		g.drawImage(_in, 0, 0, _width, _height, null);
		_in.flush();

		return out;
	}

	/**
	 * Convert Color information to string. Do not store alpha value.
	 * 
	 * @param _in
	 * @return color as string
	 */
	public static String toString(Color _in) {
		return "#" + MCast.toHex2String(_in.getRed()) + MCast.toHex2String(_in.getGreen())
				+ MCast.toHex2String(_in.getBlue());
	}

	/**
	 * Convert Font information to string.
	 * 
	 * @param _in
	 * @return the font as string
	 */
	public static String toString(Font _in) {
		return _in.getFamily() + "-"
				+ (_in.getStyle() == Font.BOLD ? "BOLD" : "")
				+ (_in.getStyle() == Font.ITALIC ? "ITALIC" : "") + "-"
				+ _in.getSize();
	}

	static public String getSelectedPart(JTextArea text) {
		String s = text.getSelectedText();
		if (s == null) {
			s = text.getText();
			int start = s.lastIndexOf("\n\n", text.getCaretPosition());
			if (start < 0)
				start = 0;
			else
				start += 2;
			int end = s.indexOf("\n\n", text.getCaretPosition());
			if (end < 0)
				end = s.length();
			s = s.substring(start, end);
			if (s == null)
				s = text.getText();
		}
		return s;
	}

	static public void halfFrame(Window _frame) {

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int) screenSize.getWidth();
		int screenHeight = (int) screenSize.getHeight();

		_frame.setSize(screenWidth / 2, screenHeight / 2);

	}

	static public void tribleFrame(Window _frame) {

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int) screenSize.getWidth();
		int screenHeight = (int) screenSize.getHeight();

		_frame.setSize(screenWidth * 2 / 3, screenHeight * 2 / 3);

	}

	static public void centerFrame(Window _frame) {

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int) screenSize.getWidth();
		int screenHeight = (int) screenSize.getHeight();

		int width = (int) _frame.getSize().getWidth();
		int height = (int) _frame.getSize().getHeight();

		int x = (screenWidth - width) / 2;
		int y = (screenHeight - height) / 2;

		if (x < 0)
			x = 0;
		if (y < 0)
			y = 0;

		_frame.setLocation(x, y);

	}

	static public void centerDialog(Window _frame, JDialog _dialog) {

		int x, y;

		if (_frame != null) {
			Point frameLoc = _frame.getLocation();
			Dimension frameSize = _frame.getSize();
			Dimension diaSize = _dialog.getSize();

			x = (int) (frameLoc.getX() + frameSize.getWidth() / 2 - diaSize
					.getWidth() / 2);
			y = (int) (frameLoc.getY() + frameSize.getHeight() / 2 - diaSize
					.getHeight() / 2);

		} else {

			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int screenWidth = (int) screenSize.getWidth();
			int screenHeight = (int) screenSize.getHeight();
			Dimension diaSize = _dialog.getSize();

			x = (int) ((screenWidth - diaSize.getWidth()) / 2);
			y = (int) ((screenHeight - diaSize.getHeight()) / 2);

		}

		if (x < 0)
			x = 0;
		if (y < 0)
			y = 0;

		_dialog.setLocation(x, y);

	}

	static public String[] getLookAndFeels() {

		String[] o = new String[7];
		o[0] = "System Look And Feel";
		o[1] = "Windows Style";
		o[2] = "Unix (Motif) Style";
		o[3] = "Metal Style";
		o[4] = "Plastic XP Style";
		o[5] = "Plastic 3D Style";
		o[6] = "Plastic Style";

		return o;
	}

	static public void setLookAndFeel(int _nr) {

		String luf = null;

		try {
			switch (_nr) {
			case 0:
				luf = UIManager.getSystemLookAndFeelClassName();
				break;
			case 1:
				luf = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
				break;
			case 2:
				luf = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
				break;
			case 3:
				luf = "javax.swing.plaf.metal.MetalLookAndFeel";
				break;
			}

			System.out.println("Set LuF: " + luf + " " + _nr);
			if (luf != null)
				UIManager.setLookAndFeel(luf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
