// CatfoOD 2011-11-24 上午11:00:54 yanming-sohu@sohu.com/@qq.com

package jym.qq.core;

import java.util.Map;

import jym.qq.cmd.Exit;
import jym.qq.cmd.Help;
import jym.qq.cmd.ICommand;
import jym.qq.cmd.Info;
import jym.qq.cmd.Login;
import jym.qq.cmd.Logout;
import jym.qq.cmd.Send;
import jym.qq.cmd.To;
import jym.qq.cmd.Users;
import jym.qq.cmd.State;


public final class CommandList {
	
	public final static Class<?>[] COMMAND_LIST = new Class<?>[] {
		Exit.class,
		Help.class,
		Info.class,
		Login.class,
		Send.class,
		To.class,
		Users.class,
		Logout.class,
		State.class,
	};

	public static void createCommands(Core c, Map<String, ICommand> cmds) {
		for (int i=0; i<COMMAND_LIST.length; ++i) {
			try {
				CommandBase cb = (CommandBase) COMMAND_LIST[i].newInstance();
				cb.setCore(c);
				cmds.put(cb.cmd(), cb);
			} catch (Exception e) {
				System.out.println("创建命令失败: " + COMMAND_LIST[i] + e);
			}
		}
	}
}
