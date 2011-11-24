// CatfoOD 2011-11-24 上午09:22:33 yanming-sohu@sohu.com/@qq.com

package jym.qq.core;


public class Message {
	
	public static final String Version = "v0.04";
	
	
	public static void welcome(StringBuilder o) {
		o.append("[-----------------------------------------------------------------------------]\n");
		o.append("[---- CatfoOD 2011 Console qq                                             ----]\n");
		o.append("[---- Type 'help' can print help list                                     ----]\n");
		o.append("[---- Qq:412475540                                  yanming-sohu@sohu.com ----]\n");
		o.append("[------------------------------------------------------------------["
																	+ Version +     "]----]\n");
		o.append("[ 使用login登入账号,用users列出用户,使用to指定对话的用户(可以用快捷号) \n");
		o.append("[ send发送信息(或者直接录入信息), help打印帮助 <<<\n");
	}
}
