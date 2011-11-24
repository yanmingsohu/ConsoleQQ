// CatfoOD 2011-11-24 下午02:37:16 yanming-sohu@sohu.com/@qq.com

package jym.qq.message.listener;

import atg.taglib.json.util.JSONObject;
import jym.qq.core.User;
import jym.qq.message.IMessageListener;
import jym.qq.message.MsgEvent;


public class TalkMessage implements IMessageListener {

	@Override
	public String pollType() {
		return TALK_MESSAGE;
	}

	@Override
	public void process(MsgEvent e) throws Exception {
		JSONObject value = e.getValue();
		String content = value.getJSONArray("content").getString(1);
		long from_uin = value.getLong("from_uin");
		long reply_ip = value.getLong("reply_ip");
	
		StringBuilder out = new StringBuilder("[ ");
		User u = e.getFriend(from_uin);
		if (null == u) {
			out.append(from_uin);
		} else {
			out.append(u.getNick());
		}
		
		out.append(" ]: ");
		out.append(content);
		out.append(" (");
		numToIp(out, reply_ip);
		out.append(")");
		
		e.log(out.toString());
	}
	
	private void numToIp(StringBuilder out, Long num) {
		String ip = Long.toHexString(num);
		out.append( Integer.parseInt(ip.substring(0, 2), 16) ).append('.');
		out.append( Integer.parseInt(ip.substring(2, 4), 16) ).append('.');
		out.append( Integer.parseInt(ip.substring(4, 6), 16) ).append('.');
		out.append( Integer.parseInt(ip.substring(6)   , 16) );
	}
}
