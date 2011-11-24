// CatfoOD 2011-11-24 上午08:51:28 yanming-sohu@sohu.com/@qq.com

package jym.qq.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import jym.qq.cmd.ICommand;
import jym.qq.console.IConsole;
import jym.qq.core.MiniQQClient.METHOD;


public abstract class CommandBase implements ICommand {
	
	private Core core;
	
	
	public void setCore(Core c) {
		core = c;
	}

	/** 返回所有命令 */
	protected Map<String, ICommand> getAllCommands() {
		return core.cmds;
	}
	
	/**
	 * 返回存储快捷码的map
	 */
	protected Map<Long, User> getQuicker() {
		return core.quick_id;
	}
	
	/**
	 * 返回登录信息, 如果未登录抛出NULL异常
	 */
	protected ILoginModel getLoginModel() {
		return core.getServer().getLoginModel();
	}
	
	/**
	 * 返回所有用户
	 */
	protected Collection<User> getAllUsers() {
		return core.getServer().allUsers().values();
	}
	
	/**
	 * 如果已经登录返回true,
	 * @param printMessage - true如果未登录则打印错误信息
	 */
	protected boolean alreadyLogin(boolean printMessage) {
		boolean run = core.isrun();
		if ((!run) && printMessage) {
			pl("lose server connection. must relogin.");
		}
		return run;
	}
	
	/**
	 * 重新刷新快捷码表, 如果未登录抛出NULL异常
	 */
	protected void makequick() {
		Map<Long, User> q = core.quick_id;
		q.clear();
		
		Iterator<User> it = getAllUsers().iterator();
		long i=0;
		
		while (it.hasNext()) {
			User u = it.next();
			i++; q.put(i, u);
		}
	}
	
	/**
	 * 从快捷码,qq码或者uin返回用户,如果未登录抛出NULL异常
	 */
	protected User getFriend(long id) {
		Map<Long, User> quick = core.quick_id;
		User u = quick.get(id);
		if (u==null) {
			u = core.getServer().getFriend(id);
		}
		return u;
	}
	
	/**
	 * 设定默认的聊天用户
	 */
	protected void setTarget(User u) {
		core.setTarget(u);
	}
	
	/**
	 * 返回默认的聊天用户
	 */
	protected User getTarget() {
		return core.getTarget();
	}
	
	/**
	 * 向用户发送消息, 如果未登录抛出NULL异常
	 */
	protected void sendMsg(User u, String msg) {
		core.getServer().sendMsg(u.getQq(), msg);
	}
	
	/**
	 * 失败返回null
	 */
	protected Integer toInt(String i) {
		try {
			return Integer.parseInt(i);
		} catch(Exception e) {}
		return null;
	}
	
	/**
	 * 读取数字, 返回null说明输入无效
	 */
	protected Integer readInt(String fmt, Object... av) {
		Integer i = null;
		String line = getConsole().readLine(fmt, av);
		try {
			i = Integer.parseInt(line);
		} catch(Exception e) {
		}
		return i;
	}
	
	/**
	 * 发送http请求, 如果未登录抛出NULL异常
	 * @param url
	 * @param contents - 如果为null,使用GET方法
	 * @return 失败返回null
	 */
	protected String sendHttpRequest(String url, String contents) {
		if (contents==null) {
			return core.getServer().sendHttpMessage(url, METHOD.GET, null);
		} else {
			return core.getServer().sendHttpMessage(url, METHOD.POST, contents);
		}
	}
	
	/**
	 * 登录系统
	 */
	protected void login(long id, String pw) {
		logout();
		IServerConn serv = new MiniQQClient(id, pw, core, core.mgspkg);
		core.setServer(serv);
		makequick();
	}
	
	/**
	 * 登出系统
	 */
	protected void logout() {
		IServerConn s = core.getServer();
		if (s!=null) s.stop();
	}
	
	protected IConsole getConsole() {
		return core.console;
	}
	
	protected IPrinter getPrinter() {
		return core;
	}
	
	/**
	 * 默认使用类名小写
	 */
	public String cmd() {
		return this.getClass().getSimpleName().toLowerCase();
	}
	
	public void pl(Object o) {
		core.println(o);
	}
}
