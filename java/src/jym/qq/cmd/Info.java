// CatfoOD 2011-11-24 上午09:18:34 yanming-sohu@sohu.com/@qq.com

package jym.qq.cmd;

import jym.qq.core.CommandBase;
import jym.qq.core.ILoginModel;
import jym.qq.core.User;


public class Info extends CommandBase {
	
	public void exec(String[] args) {
		if (!alreadyLogin(true)) return;
		
		if (getTarget()==null) {
			pl("use 'to' command, to specify target user.");
			return;
		}
		pl(getInfo(getTarget()));
	}
	
	public String getInfo(User u) {
		ILoginModel m = getLoginModel();
		
		String url = "http://s.web2.qq.com/api/get_friend_info2?" +
				"tuin="+ u.getQq() +"&verifysession=" + m.session() + "&code=" +
				"&vfwebqq=" + m.vfwebqq() + "&t=" + m.t();
		
		String info = sendHttpRequest(url, null);
		return info;
	}
	
	public String help() {
		return "查看个人资料(测试)";
	}
}
