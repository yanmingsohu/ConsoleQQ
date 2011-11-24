// CatfoOD 2011-11-24 上午10:59:55 yanming-sohu@sohu.com/@qq.com

package jym.qq.cmd;

import jym.qq.core.CommandBase;
import jym.qq.core.ILoginModel;


public class State extends CommandBase {
	
	private final static String[] STATE = new String[] {
		"hidden",	// 隐身
		"online", 	// 我在线上
		"callme",	// Q我吧
		"away",		// 离开
		"busy",		// 忙碌
		"silent",	// 请勿打扰
		"offline",	// 离线
	};
	
	private final static String INFO = 
		"[0-隐身  1-我在线上  2-Q我吧  3-离开  4-忙碌  5-请勿打扰  6-离线 ]\n输入状态码:";

	
	@Override
	public void exec(String[] args) {
		if (!alreadyLogin(true)) return;
		
		Integer i = null;
		if (args.length>1) {
			i = toInt(args[1]);
		}

		if (i==null) i = readInt(INFO);
		
		if (i!=null) {
			if (i>=0 && i<STATE.length) {
				sendRequest(STATE[i]);
				return;
			}
		}
		pl("invalid state number.");
	}

	@Override
	public String help() {
		return "切换登录状态";
	}

	private void sendRequest(String state) {
		ILoginModel m = getLoginModel();
		String url = "http://d.web2.qq.com/channel/change_status2?newstatus=" + state
				+ "&clientid=" + m.clientid() + "&psessionid=" + m.session() + "&t=" + m.t();
		
		String ret = sendHttpRequest(url, null);
		if (ret.indexOf("ok")>0) {
			pl( "change state: " + state );
		} else {
			pl( "change state fail. ");
		}
	}
}
