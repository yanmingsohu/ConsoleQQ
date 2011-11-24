// CatfoOD 2011-11-24 上午09:08:33 yanming-sohu@sohu.com/@qq.com

package jym.qq.cmd;

import jym.qq.core.CommandBase;
import jym.qq.core.User;


public class To extends CommandBase {

	public void exec(String[] args) {
		if (!alreadyLogin(true)) return;
		
		String id;
		if (args.length>1) {
			id = args[1];
		} else {
			id = getConsole().readLine("to: ");
		}
		
		long qq = Long.parseLong(id);
		User user = getFriend(qq);
		if (user==null) {
			pl("user '" + id + "' is invalid id number.");
		} else {
			setTarget(user);
		}
	}
	
	public String help() {
		return "指定目标用户";
	}
}
