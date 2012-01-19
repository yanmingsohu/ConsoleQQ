// CatfoOD 2011-11-24 下午02:57:40 yanming-sohu@sohu.com/@qq.com

package jym.qq.message.listener;

import atg.taglib.json.util.JSONObject;
import jym.qq.core.User;
import jym.qq.message.IMessageListener;
import jym.qq.message.MsgEvent;


public class BuddiesStatusChange implements IMessageListener {

	@Override
	public String pollType() {
		return BUDDIES_STATUS_CHANGE;
	}

	@Override
	public void process(MsgEvent e) throws Exception {
		JSONObject value = e.getValue();
		
		long from_uin = value.getLong("uin");
		String status = value.getString("status");

		User u = e.getFriend(from_uin);
		if (u==null) {
			e.log("用户：" + from_uin + "\t" + status);
		} else {
			e.log("用户：" + u.getNick() + "(" + u.getQuick()+ ")"+"\t" + status);
		}
	}

}
