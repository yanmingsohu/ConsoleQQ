// CatfoOD 2011-11-24 上午08:51:53 yanming-sohu@sohu.com/@qq.com

package jym.qq.cmd;


public interface ICommand {
	/** 
	 * args[0]: 命令本身 
	 */
	void exec(String[] args);
	
	/**
	 * 返回帮助信息
	 */
	String help();
	
	/**
	 * 返回命令代码
	 */
	String cmd();
}
