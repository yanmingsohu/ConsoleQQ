// CatfoOD 2011-11-24 上午08:59:12 yanming-sohu@sohu.com/@qq.com

package jym.qq.cmd;

import jym.qq.core.CommandBase;


public class Exit extends CommandBase {
	
	public void exec(String[] args) {
		System.exit(0);
	}
	
	public String help() {
		return "退出程序";
	}
}
