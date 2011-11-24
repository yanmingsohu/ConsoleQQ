// CatfoOD 2011-11-24 下午03:55:56 yanming-sohu@sohu.com/@qq.com

package jym.qq.cmd;

import jym.qq.core.CommandBase;


public class Time extends CommandBase {

	@Override
	public void exec(String[] args) {
		Integer i = readInt("[0:不打印时间, 1:打印时间]: ");
		if (i!=null) {
			getPrinter().setDispTime(i==1);
		}
	}

	@Override
	public String help() {
		return "设置是否打印时间";
	}

}
