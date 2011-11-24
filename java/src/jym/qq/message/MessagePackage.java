// CatfoOD 2011-11-24 下午01:21:09 yanming-sohu@sohu.com/@qq.com

package jym.qq.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jym.qq.core.IPrinter;
import jym.qq.core.IServerConn;

import atg.taglib.json.util.JSONObject;

/**
 * 消息处理器
 */
public class MessagePackage {

	private Map<String, List<IMessageListener>> pmliser;
	private IPrinter print;
	
	
	public MessagePackage(IPrinter _print) {
		if (_print==null) throw new NullPointerException();
		pmliser = new HashMap<String, List<IMessageListener>>();
		print = _print;
	}
	
	public void addListener(IMessageListener ml) {
		List<IMessageListener> list = pmliser.get(ml.pollType());
		if (list==null) {
			list = new ArrayList<IMessageListener>();
			pmliser.put(ml.pollType(), list);
		}
		list.add(ml);
	}
	
	/**
	 * 删除事件监听器,成功返回true
	 */
	public boolean removeListener(IMessageListener ml) {
		if (ml==null) return false;
		
		List<IMessageListener> list = pmliser.get(ml.pollType());
		if (list!=null) {
			Iterator<IMessageListener> it = list.iterator();
			boolean find = false;
			
			while (it.hasNext()) {
				IMessageListener tmp = it.next();
				if (tmp == ml) {
					it.remove();
					find = true;
				}
			}
			return find;
		}
		
		return false;
	}
	
	/**
	 * 发送从服务器接收到的数据, retcode == 0
	 * @param server - 到服务器的连接
	 * @param poll_type - 事件类型
	 * @param root - json数据的根节点
	 */
	public void sendMessage(IServerConn server, String poll_type, JSONObject root) {
		List<IMessageListener> list = pmliser.get(poll_type);
		if (list!=null) {
			try {
				MsgEvent e = new MsgEvent(root, server, print);
				Iterator<IMessageListener> it = list.iterator();
				while (it.hasNext()) {
					it.next().process(e);
				}
			} catch(Exception e) {
				print.println(">> 处理消息出错: " + e);
			}
		} 
		else {
			print.println(">> 未处理的消息类型: " + root);
		}
	}
}
