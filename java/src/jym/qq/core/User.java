package jym.qq.core;

public class User {

	private long uin;
	private long qq;
	private int face;
	private long flag;
	private String nick;
	private String markname;


	public User() {
		super();
	}

	public User(long uin, String nick, int face, long flag) {
		super();
		this.uin = uin;
		this.nick = nick;
		this.face = face;
		this.flag = flag;
		this.qq = uin;
	}

	public long getUin() {
		return uin;
	}

	public void setUin(long uin) {
		this.uin = uin;
	}

	public long getQq() {
		return qq;
	}

	public void setQq(long qq) {
		this.qq = qq;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public int getFace() {
		return face;
	}

	public void setFace(int face) {
		this.face = face;
	}

	public long getFlag() {
		return flag;
	}

	public void setFlag(long flag) {
		this.flag = flag;
	}

	public String toString() {
		String user = this.uin + "\t\t" + this.qq + "\t\t" + this.nick + "\t\t" + this.flag;
		return user;
	}

	public String getMarkname() {
		return markname;
	}
	
	public void setMarkname(String markname) {
		this.markname = markname;
	}
}
