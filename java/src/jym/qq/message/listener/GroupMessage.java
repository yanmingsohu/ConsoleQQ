// CatfoOD 2011-11-24 下午02:59:10 yanming-sohu@sohu.com/@qq.com

package jym.qq.message.listener;

import jym.qq.core.Group;
import jym.qq.message.IMessageListener;
import jym.qq.message.MsgEvent;
import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONObject;


public class GroupMessage implements IMessageListener {

	private JSONObject root;
	private JSONArray result;
	private JSONObject value;
	
	@Override
	public String pollType() {
		return GROUP_MESSAGE;
	}

	@Override
	public void process(MsgEvent e) throws Exception {
//		e.log("接受到群消息");
		root = e.getRoot();
		result = root.getJSONArray("result");
		value = result.getJSONObject(0).getJSONObject("value");
		
		String content = value.getJSONArray("content").getString(1);
		long group_code = value.getLong("group_code");
		long send_uin = value.getLong("send_uin");
		Group g = e.getGroup(group_code,send_uin);
		String groupName = g.getGroup_name().get(group_code);
		String sendUser = g.getSend_username().get(send_uin);
		StringBuilder sb = new StringBuilder("[");
		sb.append(groupName+" ");
		sb.append("("+sendUser+")");
		sb.append("]  ");
		sb.append(content);
		
		
		e.log(sb.toString());
	}

}
