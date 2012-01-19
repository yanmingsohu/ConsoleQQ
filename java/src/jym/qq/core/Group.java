package jym.qq.core;

import java.util.HashMap;
import java.util.Map;


public class Group {
	private long gid;
	private long flag;
	private long code;
	private String name;
	private long guick;
	
	private Map<Long,String> group_name = new HashMap<Long,String>();
	private Map<Long,String> send_username = new HashMap<Long,String>();
	
	
	
	
	public Map<Long, String> getGroup_name() {
		return group_name;
	}

	public void setGroup_name(Map<Long, String> group_name) {
		this.group_name = group_name;
	}

	public Map<Long, String> getSend_username() {
		return send_username;
	}

	public void setSend_username(Map<Long, String> send_username) {
		this.send_username = send_username;
	}

	public Group(){
		super();
	}
	
	public Group(long flag, String name, long gid, long code){
		super();
		this.flag = flag;
		this.name = name;
		this.gid = gid;
		this.code = code;
	}
	
	public long getGuick() {
		return guick;
	}

	public void setGuick(long guick) {
		this.guick = guick;
	}

	public long getGid() {
		return gid;
	}
	public void setGid(long gid) {
		this.gid = gid;
	}
	public long getFlag() {
		return flag;
	}
	public void setFlag(long flag) {
		this.flag = flag;
	}
	public long getCode() {
		return code;
	}
	public void setCode(long code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
