// CatfoOD 2011-11-24 上午11:00:54 yanming-sohu@sohu.com/@qq.com

package jym.qq.core;

import java.util.HashMap;
import java.util.Map;

import jym.qq.cmd.Exit;
import jym.qq.cmd.Groups;
import jym.qq.cmd.Help;
import jym.qq.cmd.ICommand;
import jym.qq.cmd.Info;
import jym.qq.cmd.Login;
import jym.qq.cmd.Logout;
import jym.qq.cmd.Send;
import jym.qq.cmd.State;
import jym.qq.cmd.Time;
import jym.qq.cmd.To;
import jym.qq.cmd.Users;


public final class CommandFactory {
	
	public final static Class<?>[] COMMAND_LIST = new Class<?>[] {
		Exit.class,
		Help.class,
		Info.class,
		Login.class,
		Send.class,
		To.class,
		Users.class,
		Groups.class,
		Logout.class,
		State.class,
		Time.class,
	};

	
	public static Map<String, ICommand> create(Core c) {
		Map<String, ICommand> cmds = new HashMap<String, ICommand>();
		for (int i=0; i<COMMAND_LIST.length; ++i) {
			try {
				create(c, cmds, COMMAND_LIST[i]);
			} catch (Exception e) {
				System.out.println(">>> 创建命令失败: " + COMMAND_LIST[i] + e);
			}
		}
		__testCommand(c, cmds);
		return cmds;
	}
	
	private static void __testCommand(Core core, Map<String, ICommand> cmds) {
		try {
			Class<?> c = Class.forName("jym.qq.cmd._Test");
			create(core, cmds, c);
		} catch (Exception e) {
		}
	}
	
	private static void create(Core core, Map<String, ICommand> cmds, Class<?> c) 
	throws InstantiationException, IllegalAccessException {
		CommandBase cb = (CommandBase) c.newInstance();
		cb.setCore(core);
		cmds.put(cb.cmd(), cb);
	}
}
