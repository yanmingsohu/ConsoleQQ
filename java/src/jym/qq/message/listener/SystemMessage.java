// CatfoOD 2011-11-24 下午02:59:49 yanming-sohu@sohu.com/@qq.com

package jym.qq.message.listener;

import jym.qq.message.IMessageListener;
import jym.qq.message.MsgEvent;


public class SystemMessage implements IMessageListener {

	@Override
	public String pollType() {
		return SYSTEM_MESSAGE;
	}

	@Override
	public void process(MsgEvent e) throws Exception {
		e.log("接受到系统消息,忽略...");
	}

}
