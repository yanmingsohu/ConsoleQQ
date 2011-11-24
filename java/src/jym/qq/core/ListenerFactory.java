// CatfoOD 2011-11-24 下午03:01:28 yanming-sohu@sohu.com/@qq.com

package jym.qq.core;

import jym.qq.message.IMessageListener;
import jym.qq.message.MessagePackage;
import jym.qq.message.listener.BuddiesStatusChange;
import jym.qq.message.listener.GroupMessage;
import jym.qq.message.listener.SystemMessage;
import jym.qq.message.listener.TalkMessage;


public final class ListenerFactory {

	public static final Class<?>[] LISTENER_CLASS = new Class<?>[] {
			BuddiesStatusChange.class,
			GroupMessage.class,
			SystemMessage.class,
			TalkMessage.class,
	};
	
	
	public static MessagePackage create(IPrinter print) {
		MessagePackage mp = new MessagePackage(print);
		for (int i=0; i<LISTENER_CLASS.length; ++i) {
			try {
				IMessageListener l = (IMessageListener) LISTENER_CLASS[i].newInstance();
				mp.addListener(l);
			} catch (Exception e) {
				print.println(">>> 创建监听器失败: " + e);
			}
		}
		return mp;
	}
	
}
