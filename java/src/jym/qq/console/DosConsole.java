package jym.qq.console;
import java.io.Console;
import java.io.IOException;

// CatfoOD 2011-11-23 上午08:53:44 yanming-sohu@sohu.com/@qq.com

public class DosConsole implements IConsole {
	
	private Console con;
	
	
	public DosConsole() throws IOException {
		con = System.console();
		if (con==null) {
			throw new IOException("create system console fail.");
		}
	}

	@Override
	public String readLine() {
		return con.readLine();
	}

	@Override
	public String readLine(String fmt, Object... args) {
		return con.readLine(fmt, args);
	}

	@Override
	public char[] readPassword() {
		return con.readPassword();
	}

	@Override
	public char[] readPassword(String fmt, Object... args) {
		return con.readPassword(fmt, args);
	}
}
