// CatfoOD 2011-11-24 上午09:12:32 yanming-sohu@sohu.com/@qq.com

package jym.qq.cmd;

import jym.qq.core.CommandBase;


public class Send extends CommandBase {
	
	public void exec(String[] args) {
		if (!alreadyLogin(true)) return;
		
		if (getTarget()==null) {
			pl("use 'to' command, to specify target user.");
			return;
		}
		
		String message;
		if (args.length>1) {
			message = args[1];
		} else {
			message = getConsole().readLine("message: ");
		}
		
		sendMsg(getTarget(), message);
	}
	
	public String help() {
		return "发送信息";
	}
}
