package jym.qq.cmd;

import java.util.Iterator;
import java.util.Map;

import jym.qq.core.CommandBase;
import jym.qq.core.Group;

public class Groups extends CommandBase {

	@Override
	public void exec(String[] args) {
		if (!alreadyLogin(true)) return;
		
		Map<Long, Group> q = getGrouper();
		Iterator<Long> it = q.keySet().iterator();
		
		StringBuilder out = new StringBuilder("快捷号码\n");
		
		while (it.hasNext()) {
			Long i = it.next();
			Group u = q.get(i);
			
			out.append(i).append('\t');
			out.append(u.getGid()).append('\t');
			out.append(u.getName()).append('\t');
			out.append('\n');
		}
		pl(out);
	}

	@Override
	public String help() {
		return "显示所有群组";
	}

}
