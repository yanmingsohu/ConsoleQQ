// CatfoOD 2011-11-24 上午09:07:34 yanming-sohu@sohu.com/@qq.com

package jym.qq.cmd;

import jym.qq.core.CommandBase;


public class Logout extends CommandBase {
	
	public void exec(String[] args) {
		logout();
	}
	
	public String help() {
		return "退出登录";
	}
}
