// CatfoOD 2011-11-24 上午08:55:20 yanming-sohu@sohu.com/@qq.com

package jym.qq.cmd;

import java.util.Iterator;
import java.util.Map;

import jym.qq.core.CommandBase;


public class Help extends CommandBase {
	
	public void exec(String[] args) {
		Map<String, ICommand> cmds = getAllCommands();
		
		Iterator<String> key = cmds.keySet().iterator();
		while (key.hasNext()) {
			String k = key.next();
			pl(k + "\t\t" + cmds.get(k).help());
		}
	}
	
	public String help() {
		return "打印帮助";
	}
}
