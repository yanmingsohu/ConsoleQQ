// CatfoOD 2011-11-24 上午09:14:40 yanming-sohu@sohu.com/@qq.com

package jym.qq.cmd;

import java.util.Iterator;
import java.util.Map;

import jym.qq.core.CommandBase;
import jym.qq.core.User;


public class Users extends CommandBase {
	
	public void exec(String[] args) {
		if (!alreadyLogin(true)) return;
		
		Map<Long, User> q = getQuicker();
		Iterator<Long> it = q.keySet().iterator();
		
		StringBuilder out = new StringBuilder("快捷号码\n");
		
		while (it.hasNext()) {
			Long i = it.next();
			User u = q.get(i);
			
			out.append(i).append('\t');
			out.append(u.getQq()).append('\t');
			out.append(u.getNick()).append('\t');
			if (u.getMarkname()!=null) {
				out.append(" [ ").append(u.getMarkname()).append(" ]");
			}
			out.append('\n');
		}
		pl(out);
	}
	
	public String help() {
		return "显示所有用户";
	}
}
