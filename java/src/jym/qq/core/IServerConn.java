// CatfoOD 2011-11-24 上午09:51:23 yanming-sohu@sohu.com/@qq.com

package jym.qq.core;

import java.util.Map;

import jym.qq.core.MiniQQClient.METHOD;


public interface IServerConn {

	/** 必须使用该方法取得用户,不要直接访问firends */
	User getFriend(long qq$uid);
	
	Group getGroup(long code);
	/**查询群消息来源信息*/
	Group getGroup(long code, long senduin);
	
	Map<Long, User> allUsers();
	
	Map<Long, Group> allGroups();

	/** 已经连接到服务器返回true */
	boolean running();

	/** 停止系统, 并等待直到完全退出 */
	void stop();

	/** 取QQ好友, 会清除之前的信息 */
	void fetchAllFriends();

	/** 输入uin 返回qq */
	long getQQWithUin(long uid);

	/** 取群组, 会清除之前的信息 */
	void fetchAllGroups();
	
	/** 在线用户 */
	void fetchAllOnlineFriends();

	/** 发送聊天信息 */
	boolean sendMsg(long toQQ, String message);

	/** HTTP 消息发送, 出错返回null */
	String sendHttpMessage(String url, METHOD method, String contents);
	
	/** 返回登录信息,用于拼装URL */
	ILoginModel getLoginModel();



}
