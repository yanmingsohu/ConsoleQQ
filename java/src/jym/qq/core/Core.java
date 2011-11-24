// CatfoOD 2011-11-24 上午08:54:43 yanming-sohu@sohu.com/@qq.com

package jym.qq.core;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import jym.qq.cmd.ICommand;
import jym.qq.console.DosConsole;
import jym.qq.console.IConsole;
import jym.qq.console.WinConsole;
import jym.qq.message.MessagePackage;


public class Core implements IPrinter {

	private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
	
	protected final Map<String, ICommand> cmds;
	protected final Map<Long, User> quick_id;
	
	protected final IConsole console;
	protected final MessagePackage mgspkg;
	private IServerConn serverConn = null;
	private User target = null;
	private ReentrantLock printLock;
	
	private boolean waitInput = false;
	private boolean logtime = true;
	
	
	public Core() {
		printLock = new ReentrantLock();
		console = createConsole();
		mgspkg = ListenerFactory.create(new MessagePrinter());

		StringBuilder out = new StringBuilder();
		Version.welcome(out);
		println(out);

		quick_id = new LinkedHashMap<Long, User>();
		cmds = CommandFactory.create(this);
		loop();
	}
	
	private IConsole createConsole() {
		try {
			return new DosConsole();
		} catch (IOException e) {
			return new WinConsole();
		}
	}

	private void loop() {
		String line = null;
		
		do {
			waitInput = true;
			do {
				line = console.readLine(getCursor());
			} while (line==null);
			waitInput = false;
			
			String[] args = line.split("\\s+", 0);
			
			if (args!=null && args.length>0) {
				if (args[0].length()==0) continue;
				
				ICommand c = cmds.get(args[0]);
				if (c!=null) {
					try {
						printLock.lock();
						c.exec(args);
					} catch(Exception e) {
						println(e.getMessage());
						println("error : " + e);
					} finally {
						printLock.unlock();
					}
				} else {
					if (isrun() && target!=null) {
						/* 默认使用send发送信息 */
						cmds.get("send").exec(new String[]{"send", args[0]});
					} else {
						println("unknow command '" + args[0] + "'");
					}
				}
			}
		} while (true);
	}
	
	protected boolean isrun() {
		return serverConn!=null && serverConn.running();
	}
	
	private String getCursor() {
		return (target==null) ? "->" : ("[" + target.getNick() + "]->");
	}
	
	public void setDispTime(boolean b) {
		logtime = b;
	}
	
	public void println(Object o) {
		printLock.lock();
		if (waitInput) {
			System.out.println();
		}
		
		System.out.println(o);
		
		if (waitInput) {
			System.out.print(getCursor());
		}
		printLock.unlock();
	}

	protected IServerConn getServer() {
		return serverConn;
	}

	protected User getTarget() {
		return target;
	}

	protected void setServer(IServerConn serverConn) {
		if (serverConn!=null) {
			this.serverConn = serverConn;
		}
	}

	protected void setTarget(User target) {
		this.target = target;
	}
	
	private class MessagePrinter implements IPrinter {
		
		@Override
		public void println(Object o) {
			if (logtime) {
				Core.this.println(sdf.format(new Date()) + " " + o);
			} else {
				Core.this.println(o);
			}
		}

		@Override
		public void setDispTime(boolean b) {
			logtime = b;
		}
		
	}
}
