package jym.qq.console;
// CatfoOD 2011-11-23 上午08:35:17 yanming-sohu@sohu.com/@qq.com

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPasswordField;

import acm.io.IOConsole;


public class WinConsole implements IConsole {
	/**
	 * http://jtf.acm.org/javadoc/student/acm/io/IOConsole.html
	 */
	private IOConsole con;
	private final Frame win;
	
	
	public WinConsole() {
		con = new IOConsole();
		con.setInputColor(Color.LIGHT_GRAY);
		con.setBackground(Color.BLACK);
		con.setForeground(Color.ORANGE);
		
		System.setOut( new Bridge() );
		
		win = new Frame();
		win.setTitle("console. -= CatfoOD =-");
		win.setBackground(Color.BLACK);
		win.add(con);
		win.setSize(600, 300);
		center(win);
		win.setVisible(true);
		
		win.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				win.dispose();
				System.exit(0);
			}
		});
	}
	
	public static void center(Container c) {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int w = c.getWidth();
		int h = c.getHeight();
		int x = (d.width - w) / 2;
		int y = (d.height- h) / 2;
		c.setBounds(x, y, w, h);
	}

	@Override
	public String readLine() {
		return con.readLine();
	}

	@Override
	public String readLine(String fmt, Object... args) {
		return con.readLine(String.format(fmt, args));
	}

	@Override
	public char[] readPassword() {
		Password p = new Password();
		char[] pw = p.get();
		con.println();
		return pw;
	}

	@Override
	public char[] readPassword(String fmt, Object... args) {
		con.print(String.format(fmt, args));
		return readPassword();
	}

	
	private class Bridge extends PrintStream {
		private PrintWriter pw = con.getWriter();
		
		Bridge() {
			super(new NULL_OUT());
		}
		/* 拦截对write的调用 */
		public void write(byte[] buf, int off, int len) {
			pw.write(new String(buf, off, len));
		}

		public void write(int b) {
			pw.write(b);
		}
	}
	
	private class NULL_OUT extends OutputStream {
		public void write(int b) throws IOException {
		}
	}
	
	private class Password extends JDialog {
		private static final long serialVersionUID = 5914329747921502915L;
		JPasswordField pf = new JPasswordField();
		
		Password() {
			super(win, true);
			setTitle("input password.");
			setSize(300, 80);
			setLayout(new GridLayout(2,1));
			
			pf = new JPasswordField();
			add(pf);
			JButton ok = new JButton("OK");
			add(ok);
			center(this);
			
			ok.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					over();
				}
			});
			
			pf.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode()==10) {
						over();
					}
				}
			});
		}
		
		void over() {
			setVisible(false);
			dispose();
		}
		
		char[] get() {
			setVisible(true);
			return pf.getPassword();
		}
	}
}
