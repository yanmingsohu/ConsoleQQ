// CatfoOD 2011-11-24 上午09:00:06 yanming-sohu@sohu.com/@qq.com

package jym.qq.cmd;

import jym.qq.core.CommandBase;


public class Login extends CommandBase {

	public void exec(String[] args) {
		if (alreadyLogin(false)) {
			pl("already login.");
			return;
		}

		String name = getConsole().readLine("input id:");
		if (name==null) {
			pl("name not null.");
			return;
		}
		int id = 0;
		try {
			id = Integer.parseInt(name);
		} catch(Exception e) {
			pl(name + " is invalid id number.");
			return;
		}
		
		char[] pass = getConsole().readPassword("input password:");
		if (pass==null) {
			pl("password not null.");
			return;
		}
		String pw = new String(pass);
		login(id, pw);
	}
	
	public String help() {
		return "登录帐号";
	}
}
