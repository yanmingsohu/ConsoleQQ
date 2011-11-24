// CatfoOD 2011-11-23 上午08:48:55 yanming-sohu@sohu.com/@qq.com

package jym.qq.console;

public interface IConsole {
	String readLine();
	String readLine(String fmt, Object... args);
	char[] readPassword();
	char[] readPassword(String fmt, Object... args);
}
