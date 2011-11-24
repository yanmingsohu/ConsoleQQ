// CatfoOD 2011-11-24 下午01:21:23 yanming-sohu@sohu.com/@qq.com

package jym.qq.message;


/**
 * retcode==0, 的消息监听器 
 */
public interface IMessageListener extends IMessagePollTypes {
	
	/** 
	 * 监听器感兴趣的事件类型, 协议中的poll_type域 
	 */
	String pollType();
	
	/**
	 * 处理该事件
	 * @param e - 事件数据, 可以被前一个监听器修改
	 */
	void process(MsgEvent e) throws Exception ;
}
