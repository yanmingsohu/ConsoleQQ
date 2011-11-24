// CatfoOD 2011-11-24 上午08:54:43 yanming-sohu@sohu.com/@qq.com

package jym.qq.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import jym.qq.cmd.ICommand;
import jym.qq.console.DosConsole;
import jym.qq.console.IConsole;
import jym.qq.console.WinConsole;


public class Core implements IPrinter {

	protected final Map<String, ICommand> cmds;
	protected final Map<Long, User> quick_id;
	
	protected final IConsole console;
	private IServer server = null;
	private User target = null;
	private boolean waitInput = false;
	private ReentrantLock printLock;
	
	
	public Core() {
		printLock = new ReentrantLock();
		console = createConsole();

		StringBuilder out = new StringBuilder();
		Message.welcome(out);
		println(out);

		quick_id = new LinkedHashMap<Long, User>();
		cmds = new HashMap<String, ICommand>();
		CommandList.createCommands(this, cmds);
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
		return server!=null && server.running();
	}
	
	private String getCursor() {
		return (target==null) ? "->" : ("[" + target.getNick() + "]->");
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

	protected IServer getServer() {
		return server;
	}

	protected User getTarget() {
		return target;
	}

	protected void setServer(IServer server) {
		this.server = server;
	}

	protected void setTarget(User target) {
		this.target = target;
	}
}
