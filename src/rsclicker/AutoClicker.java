package rsclicker;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JTextField;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.JPanel;

public class AutoClicker {

	private static List<PointType> clicks = new ArrayList<PointType>();
	private static Robot r;
	private JFrame frame;
	public static JTextField textField;
	public static JPanel panelColor;
	private static final Random RANDOM = new Random();
	private static boolean clicking = false;
	public static JCheckBox bxOnce;
	public static JTextField textField_1;
	public static boolean shiftDown = false;
	public static JTextField textField_2;
	public static String colorSearch;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		Thread thread = new Thread() {
			public void run() {
				int x = 0;
				int y = 0;
				while (true) {
					try {
						sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (clicking) {
						try {
							for(PointType p : clicks) {
								if (!clicking)
									break;
								int offset = 10;
								String offsetS = textField_1.getText();
								if (offsetS != null && !offsetS.equals(""))
									offset = Integer.parseInt(offsetS);
								int delay = 100;
								String text = textField.getText();
								if (text != null && !text.equals(""))
									delay = Integer.parseInt(text);
								if (offset > 0) {
									x = (int) p.getPoint().getX() - (offset / 2) + random(offset);
									y = (int) p.getPoint().getY() - (offset / 2) + random(offset);
									r.mouseMove(x, y);
									sleep(10);
									r.mouseMove(x + 1, y + 1);
									sleep(10);
									r.mouseMove(x, y);
									
								}
								int type = p.getType();
								if (p.isNoDelay()) {
									delay = 30;
								}
								r.mousePress(type);
								sleep(1);
								r.mouseRelease(type);
								//sleep(random(120000, 300000));
								sleep(delay + random(1, 70));
							}
							if (bxOnce.isSelected())
								clicking = false;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		thread.start();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AutoClicker window = new AutoClicker();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		try {
			r = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		try {
			Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
			logger.setLevel(Level.OFF);
			logger.setUseParentHandlers(false);
			GlobalScreen.registerNativeHook();

			GlobalScreen.addNativeKeyListener(new NativeKeyListener() {

				@Override
				public void nativeKeyPressed(NativeKeyEvent arg0) {
					String key = NativeKeyEvent.getKeyText(arg0.getKeyCode());
					if (key.equalsIgnoreCase("Left Shirt"))
						shiftDown = true;
				}

				@Override
				public void nativeKeyReleased(NativeKeyEvent arg0) {
					String key = NativeKeyEvent.getKeyText(arg0.getKeyCode());
					System.out.println(key);
					if (key.equalsIgnoreCase("Left Shirt"))
						shiftDown = false;
					if (key.equalsIgnoreCase("Right Control")) {
						clicking = !clicking;
					} else if(key.equalsIgnoreCase("Right Alt") || key.equalsIgnoreCase("Back Slash")) {
						Point p = MouseInfo.getPointerInfo().getLocation();
						Point p2 = new Point();
						p2.setLocation(p.getX(), p.getY());
						PointType pt = new PointType();
						pt.setPoint(p2);
						if (key.equalsIgnoreCase("Right Alt"))
							pt.setType(InputEvent.BUTTON1_MASK);
						else
							pt.setType(InputEvent.BUTTON3_MASK);
						if (shiftDown)
							pt.setNoDelay(true);
						else
							pt.setNoDelay(false);
						clicks.add(pt);
					} else if(key.equalsIgnoreCase("Right Shift")) {
						clicks.clear();
					} else if (key.equalsIgnoreCase("End")) {
						String color = "#" + getColorFromCoords();
						panelColor.setBackground(Color.decode(color));
						textField_2.setText(color);
						colorSearch = color;
					}
				}

				@Override
				public void nativeKeyTyped(NativeKeyEvent arg0) {
					
				}
				
			});
		} catch (NativeHookException ex) {
			System.exit(1);
		}
	}

	public static String getColorFromCoords() {
		Point a = MouseInfo.getPointerInfo().getLocation();
		String color = getPixelColor((int) a.getX(), (int) a.getY());
		System.out.println(color);
		return color;
	}
	
	public static String getPixelColor(int x, int y) {
		String color = "";
		BufferedImage image = r.createScreenCapture(new Rectangle(x, y, x, y));
		int rgba = image.getRGB(0, 0);
		Color col = new Color(rgba, true);
		color = String.format("%06x", col.getRGB() & 0x00FFFFFF);
		return color;
	}
	
	/**
	 * Create the application.
	 */
	public AutoClicker() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 300, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Auto Clicker");
		frame.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setText("2400");
		textField.setBounds(89, 11, 86, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		bxOnce = new JCheckBox("Run Once");
		bxOnce.setBounds(181, 10, 97, 23);
		frame.getContentPane().add(bxOnce);
		
		JLabel lblNewLabel = new JLabel("Click Delay ms");
		lblNewLabel.setBounds(10, 14, 69, 14);
		frame.getContentPane().add(lblNewLabel);
		
		textField_1 = new JTextField();
		textField_1.setText("10");
		textField_1.setColumns(10);
		textField_1.setBounds(89, 42, 86, 20);
		frame.getContentPane().add(textField_1);
		
		JLabel lblClickOffset = new JLabel("click offset");
		lblClickOffset.setBounds(10, 45, 69, 14);
		frame.getContentPane().add(lblClickOffset);
		
		panelColor = new JPanel();
		panelColor.setBounds(10, 70, 57, 51);
		frame.getContentPane().add(panelColor);
		
		textField_2 = new JTextField();
		textField_2.setBounds(10, 132, 86, 20);
		frame.getContentPane().add(textField_2);
		textField_2.setColumns(10);
	}
	
	public static final int random(int min, int max) {
		max = max + 1;
		final int n = Math.abs(max - min);
		return Math.min(min, max) + (n == 0 ? 0 : random(n));
	}
	
	public static final int random(int maxValue) {
		if(maxValue <= 0)
			return  0;
		return RANDOM.nextInt(maxValue);
	}
}
