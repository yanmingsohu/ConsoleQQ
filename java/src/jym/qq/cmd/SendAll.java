// CatfoOD 2012-4-19 下午03:20:47 yanming-sohu@sohu.com/@qq.com

package jym.qq.cmd;

import java.util.Iterator;

import jym.qq.core.CommandBase;
import jym.qq.core.User;


public class SendAll extends CommandBase {

	@Override
	public void exec(String[] args) {
		if (!alreadyLogin(true)) return;
		
		String message;
		if (args.length>1) {
			message = args[1];
		} else {
			message = getConsole().readLine("message: ");
		}

		sendInThread(message);
	}
	
	private void sendInThread(final String message) {
		Thread th = new Thread() {
			public void run() {
				Iterator<User> all = getAllUsers().iterator();
				while (all.hasNext()) {
					User user = all.next();
					sendMsg(user, message);
					
					try {
						Thread.sleep(3000 + (long)(Math.random() * 2000));
					} catch (InterruptedException e) {
					}
				}
				pl("send all over." + message.substring(0, Math.min(10, message.length())));
			}
		};
		th.setDaemon(true);
		th.setPriority(Thread.MIN_PRIORITY);
		th.start();
	}

	@Override
	public String help() {
		return "向所有用户发送消息";
	}

}
