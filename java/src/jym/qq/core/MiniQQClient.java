package jym.qq.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import jym.qq.message.MessagePackage;
import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONObject;

/**
 * QQ MINI 客户端
 * 协议:	http://blog.coxxs.com/105-webqq.html
 * 		http://dev.qq.com/wiki/index.php?title=API
 * 
 * @author mrlans E-mail:mrlans@qq.com
 * @editor CatfoOD E-mail:yanming-sohu@sohu.com [time 2011-11]
 * @version create Time：Dec 11, 2010 8:54:38 PM
 */
public class MiniQQClient implements IServerConn {
	
	private final String HOST_D = "http://d.web2.qq.com";
	private final String HOST_B = "http://web2-b.qq.com";
	private final String HOST_S = "http://s.web2.qq.com";
	private final String CHARSET = "UTF-8";
	
	private boolean run = false;
	private int clientid = 73937875;
	private long qq_id;
	private IPrinter prt;
	
	private String password;
	private String psessionid = "";
	private String ptwebqq;
	private String vfwebqq;
	private String skey;
	private String refer = HOST_D + "/proxy.html?v=20101025002";
	private String cookie = "";

	private MessagePackage message;
	private PollMessageThread poll = new PollMessageThread();
	private Map<Long, User>  firends = new HashMap<Long, User>();
	private Map<Long, Group> groups = new HashMap<Long, Group>();
	/** key is 'uin', value is 'qqid' */
	private Map<Long, Long> uincatch = new HashMap<Long, Long>();
	public enum METHOD { GET, POST }

	
	/** 不可复用 */
	public MiniQQClient(long qq, String password, IPrinter p, MessagePackage mp) {
		this.prt = p;
		this.qq_id = qq;
		this.password = password;
		message = mp;

		try {
			boolean login = login();
			if (login) {
				// fetchAllOnlineFriends();
				fetchAllFriends();
				fetchAllGroups();
				run = true;
				poll.start();
				print("now running.......");
			}
		} catch (Exception e) {
			print("发生异常退出, 重新登录.");
			run = false;
		}
	}
	
	/** 必须使用该方法取得用户,不要直接访问firends */
	public User getFriend(long qq$uid) {
		User u = firends.get(qq$uid);
		if (u==null) {
			qq$uid = getQQWithUin(qq$uid);
			if (qq$uid>0) {
				u = firends.get(qq$uid);
			}
		}
		return u;
	}
	
	public Map<Long, User> allUsers() {
		return firends;
	}
	
	
	@Override
	public Group getGroup(long code,long senduin) {
		
		Group g = getGGWithUin(code,senduin);
		return g;
	}

	@Override
	public Map<Long, Group> allGroups() {
		return groups;
	}
	
	public boolean running() {
		return run;
	}
	
	public void stop() {
		run = false;
		try {
			poll.join();
		} catch (InterruptedException e) {
		}
	}

	private boolean login() {
		print("Login...");
		// login 1
		String checkQQUrl = "http://ptlogin2.qq.com/check?appid=1003903&uin=" + qq_id;
		String result = sendHttpMessage(checkQQUrl, METHOD.GET, null);
		
		Pattern p = Pattern.compile("\\,\\'([!\\w]+)\\'");
		Matcher m = p.matcher(result);
		
		String checkType = "";
		if (m.find()) {
			checkType = m.group(1);
		}
		
		String checkImg = "http://captcha.qq.com/getimage?aid=1003903&uin=" + qq_id + "&vc_type=" + checkType;
		String check = "";
		if (!checkType.startsWith("!")) {
			// 生成图片验证码
			print("需要图片验证:" + checkImg);
		}
		else {
			check = checkType;
		}

		// login 2
		String loginUrl = "http://ptlogin2.qq.com/login?u=" + qq_id + "&" + "p=" + encodePass(this.password, check)
				+ "&verifycode=" + check + "&webqq_type=40&"+"&remember_uin=1&aid=1003903"
				+ "&u1=http%3A%2F%2Fweb2.qq.com%2Floginproxy.html%3Fstrong%3Dtrue"
				+ "&h=1&ptredirect=0&ptlang=2052&from_ui=1&pttype=1&dumy=&fp=loginerroralert";

		result = sendHttpMessage(loginUrl, METHOD.GET, null);

		p = Pattern.compile("登录成功！");
		m = p.matcher(result);
		if (m.find()) {
			print("Welcome QQ : " + qq_id + " Login Success！");
		}
		else {
			print("login failure.");
			return false;
		}

		// 从cookie中提取ptwebqq,skey
		p = Pattern.compile("ptwebqq=(\\w+);");
		m = p.matcher(cookie);
		if (m.find()) {
			this.ptwebqq = m.group(1);
		}
		p = Pattern.compile("skey=(@\\w+);");
		m = p.matcher(cookie);
		if (m.find()) {
			this.skey = m.group(1);
			print("skey: " + skey);
		}
		// log("ptwebqq="+ptwebqq+",skey="+skey);

		// login 3
		String channelLoginUrl = this.HOST_D + "/channel/login2";
		String content = "{\"status\":\"\",\"ptwebqq\":\"" + ptwebqq + "\",\"passwd_sig\":\"\",\"clientid\":\""
				+ clientid + "\"}";
		try {
			content = URLEncoder.encode(content, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		
		content = "r=" + content;// post的数据
		result = sendHttpMessage(channelLoginUrl, METHOD.POST, content);

		p = Pattern.compile("\"vfwebqq\":\"(\\w+)\"");
		m = p.matcher(result);
		
		if (m.find())
			this.vfwebqq = m.group(1);
		
		p = Pattern.compile("\"psessionid\":\"(\\w+)\"");
		m = p.matcher(result);
		
		if (m.find())
			psessionid = m.group(1);
		// log("vwebqq="+vfwebqq);
		// log("psessionid="+psessionid);

		return true;
	}

	/** 
	 * 取QQ好友, 会清除之前的信息
	 */
	public void fetchAllFriends() {
		String getFriendsurl2 = HOST_B + "/api/get_user_friends";
		String resultJson = fetchAllFriends(getFriendsurl2);
		getFriendInfo(resultJson);
	}
	
	private String fetchAllFriends(String getFriendsurl) {
		// {"h":"hello","vfwebqq":"7fe84931db23dc5a0351d759905642bcf5d09632e001bbfc8822809067538431d4da9dd1e8e653a0"}
		String content = "{\"h\":\"hello\",\"vfwebqq\":\"" + vfwebqq + "\"}";
		try {
			content = URLEncoder.encode(content, "UTF-8");
			content = "r=" + content;
			String result = sendHttpMessage(getFriendsurl, METHOD.POST, content);
			// log("AllFriends= "+result);
			return result;
		} catch (Exception e) {
			print("fetchAllFriends failure.............\t" + e);
			return null;
		}
	}
	
	/** 
	 * 取QQ好友
	 */
	@SuppressWarnings("unchecked")
	private void getFriendInfo(String result) {
		try {
			JSONObject retJson = new JSONObject(result);
			if (retJson.getInt("retcode") == 0) {
				JSONObject jresult = retJson.getJSONObject("result");
				JSONArray infos = jresult.getJSONArray("info");
				JSONArray marknames = jresult.getJSONArray("marknames");
				
				Map<Long, String> markns = new HashMap<Long, String>();
				for (ListIterator<JSONObject> it = marknames.listIterator(); it.hasNext();) {
					JSONObject obj = it.next();
					markns.put(obj.getLong("uin"), obj.getString("markname"));
				}
				
				firends.clear();
				for (ListIterator<JSONObject> it = infos.listIterator(); it.hasNext();) {
					JSONObject obj = it.next();
					User user = new User(obj.getLong("uin"), 
										obj.getString("nick"), 
										obj.getInt("face"), 
										obj.getLong("flag"));
					
					user.setMarkname(markns.get(user.getUin()));
					firends.put(user.getQq(), user);
				}
			}
		} catch (Exception e) {
			print("getFriendInfo failure:" + e);
		}
	}
	
	
	/** 
	 * 取QQ群, 会清除之前的信息
	 */
	
	
	@Override
	public void fetchAllGroups() {
		String getGroupsurl2 = HOST_S + "/api/get_group_name_list_mask2";
		String resultJson = fetchAllGroups(getGroupsurl2);
		getGroupInfo(resultJson);
	}
	
	private String fetchAllGroups(String getGroupurl) {
		// {"h":"hello","vfwebqq":"7fe84931db23dc5a0351d759905642bcf5d09632e001bbfc8822809067538431d4da9dd1e8e653a0"}
		String content = "{\"h\":\"hello\",\"vfwebqq\":\"" + vfwebqq + "\"}";
		try {
			content = URLEncoder.encode(content, "UTF-8");
			content = "r=" + content;
			String result = sendHttpMessage(getGroupurl, METHOD.POST, content);
			// log("AllFriends= "+result);
			return result;
		} catch (Exception e) {
			print("fetchAllFriends failure.............\t" + e);
			return null;
		}
	}
	
	/** 
	 * 取QQ群
	 */
	@SuppressWarnings("unchecked")
	private void getGroupInfo(String result) {
		try {
			JSONObject retJson = new JSONObject(result);
			if (retJson.getInt("retcode") == 0) {
				JSONObject jresult = retJson.getJSONObject("result");
				JSONArray infos = jresult.getJSONArray("gnamelist");
				
				Map<Long, String> markns = new HashMap<Long, String>();
				for (ListIterator<JSONObject> it = infos.listIterator(); it.hasNext();) {
					JSONObject obj = it.next();
					markns.put(obj.getLong("gid"), obj.getString("name"));
				}
				
				groups.clear();
				for (ListIterator<JSONObject> it = infos.listIterator(); it.hasNext();) {
					JSONObject obj = it.next();
					Group group = new Group(obj.getLong("flag"), 
										obj.getString("name"), 
										obj.getLong("gid"), 
										obj.getLong("code"));
					groups.put(group.getGid(), group);
				}
			}
		} catch (Exception e) {
			print("getFriendInfo failure:" + e);
		}
	}
	
	
	
	/** 输入uin 返回qq */
	public long getQQWithUin(long uid) {
		Long qq = uincatch.get(uid);
		if (qq!=null) return qq;
		
		try {
			/* type==【好友-1，群-4】 */
			String url = HOST_S + "/api/get_friend_uin2?tuin=" + uid +
					"&verifysession=&type=&code=&vfwebqq=" + vfwebqq + "&t=";
			String ret = sendHttpMessage(url, METHOD.GET, null);
			
			JSONObject retJ = new JSONObject(ret);
			JSONObject jresult = retJ.getJSONObject("result");
			qq = jresult.getLong("account");
			
			uincatch.put(uid, qq);
			return qq;
		} catch(Exception e) {
			print("get uin fail.");
		}
		return -1;
	}
	
	
	/** 输入gid 返回gg  
	 * @param senduin */
	public Group getGGWithUin(long gcode, long senduin) {
		Group g = new Group();
		try {
			/* type==【好友-1，群-4】 */
			//http://s.web2.qq.com/api/get_group_info_ext2?gcode=2870913606&vfwebqq=026582eb1c7ac6fa4aa71daadb87a62c0d0bef367506e7bc75aed9debfdb481664370cc14777e6de&t=1326779989664
			String url = HOST_S + "/api/get_group_info_ext2?gcode=" + gcode +
					"&verifysession=&type=&code=&vfwebqq=" + vfwebqq + "&t=";
			String ret = sendHttpMessage(url, METHOD.GET, null);
			
			JSONObject retJ = new JSONObject(ret);
			Map<Long,String> nameMap = new HashMap<Long,String>();
			Map<Long,String> userMap = new HashMap<Long,String>();
			
			JSONObject jresult = retJ.getJSONObject("result");
			//获取所有群成员
			JSONArray minfo = jresult.getJSONArray("minfo");
		
			for(int i=0;i<minfo.size();i++){
				long userqq = minfo.getJSONObject(i).getLong("uin");
				String username = minfo.getJSONObject(i).getString("nick");
				userMap.put(userqq, username);
			}
			
			//获取群名字以及code
			JSONObject ginfo = jresult.getJSONObject("ginfo");
			nameMap.put(ginfo.getLong("code"), ginfo.getString("name"));
			
			g.setGroup_name(nameMap);
			g.setSend_username(userMap);
			
		} catch(Exception e) {
			print("get uin fail.");
			//e.printStackTrace();
		}
		return g;
	}


	// 在线用户
	public void fetchAllOnlineFriends() {
		String onlineUserURL = HOST_D + "/channel/get_online_buddies2"
							+ "?clientid=" + clientid + "&psessionid=" + psessionid;
		String result = sendHttpMessage(onlineUserURL, METHOD.GET, null);
		System.out.println(result);
	}

	public boolean sendMsg(long toQQ, String message) {
		try {
			print("sending message...");
			JSONObject json = new JSONObject();
			json.put("to", toQQ);// 要发送的人
			json.put("face", 330);

			JSONArray msg = new JSONArray();
			msg.add(message);
			JSONArray font = new JSONArray();
			font.add("font");

			JSONObject font1 = new JSONObject().put("name", "宋体").put("size", "10");

			JSONArray style = new JSONArray();
			style.add(0);
			style.add(0);
			style.add(0);
			font1.put("style", style);
			font1.put("color", "000000");

			font.add(font1);
			msg.add(font);

			json.put("content", msg.toString());
			json.put("msg_id", new Random().nextInt(10000000));
			json.put("clientid", this.clientid);
			json.put("psessionid", this.psessionid);// 需要这个才能发送
			
			String sendMsgUrl = HOST_D + "/channel/send_msg";
			String content = json.toString();

			try {
				content = URLEncoder.encode(content, "UTF-8");
			} catch (UnsupportedEncodingException e) {
			}// 他要需要编码
			
			content = "r=" + content;
			// 发送
			String res = sendHttpMessage(sendMsgUrl, METHOD.POST, content);
			// 不出意外，这是返回结果：{"retcode":0,"result":"ok"}

			if (null != res) {
				JSONObject rh = new JSONObject(res);
				if ("ok".equals(rh.getString("result"))) {
					print("send ok.");
					return true;
				}
			}
			
			print("send fail.");
			return false;
			
		} catch (Exception e) {
			print("send message to " + toQQ + " failure......\n" + e.getMessage());
		}
		return false;
	}

	/** HTTP 消息发送, 出错返回null */
	public String sendHttpMessage(String url, METHOD method, String contents) {
		HttpURLConnection conn = null;
		try {
			//log("request=" + url);
			
			URL serverUrl = new URL(url);
			conn = (HttpURLConnection) serverUrl.openConnection();
			conn.setConnectTimeout(20000);
			conn.setRequestMethod(method.name());// "POST" ,"GET"
			if (null != refer)
				conn.addRequestProperty("Referer", refer);

			conn.addRequestProperty("Cookie", 			cookie			);
			conn.addRequestProperty("Connection",		"Keep-Alive"	);
			conn.addRequestProperty("Accept-Language",	"zh-cn"			);
			conn.addRequestProperty("Accept-Encoding",	"gzip, deflate"	);
			conn.addRequestProperty("Cache-Control",	"no-cache"		);
			conn.addRequestProperty("Accept-Charset",	CHARSET + ";"	);
			conn.addRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727)");
			
			if (method == METHOD.GET) {
				conn.connect();
			}
			else if (method == METHOD.POST) {
				conn.setDoOutput(true);
				conn.connect();
				OutputStream out = conn.getOutputStream();
				out.write(contents.getBytes());
				out.close();
			}
			else {
				throw new RuntimeException("your method is not implement");
			}

			if (conn.getHeaderFields().get("Set-Cookie") != null) {
				StringBuilder out = new StringBuilder();
				for (String s : conn.getHeaderFields().get("Set-Cookie")) {
					out.append(s);
				}
				cookie = out.toString();
			}

			InputStream ins = conn.getInputStream();

			// 处理GZIP压缩的
			if (null != conn.getHeaderField("Content-Encoding")
					&& conn.getHeaderField("Content-Encoding").equals("gzip")) {
				
				ins = new GZIPInputStream(ins);
			}

			InputStreamReader inr = new InputStreamReader(ins, CHARSET);
			BufferedReader br = new BufferedReader(inr);

			StringBuffer sb = new StringBuffer();
			String line = br.readLine();
			while (line!=null) {
				sb.append(line);
				line = br.readLine();
			}
			br.close();
			//log("response=" + sb);
			
			return sb.toString();
			
		} catch (MalformedURLException e) {
			print("sendHttpMessage error1:" + e);
			return null;
			
		} catch (SocketTimeoutException e) {
			print("send http message timeout, please retry.");
			return null;
			
		} catch (IOException e) {
			print("sendHttpMessage error2:" + e);
			return null;
			
		} finally {
			if (conn!=null) {
				conn.disconnect();
			}
		}
	}

	// 加密密码
	private String encodePass(String pass, String code) {
		try {
			ScriptEngineManager m = new ScriptEngineManager();
			ScriptEngine se = m.getEngineByName("javascript");
		
			URL url = this.getClass().getClassLoader().getResource("pw.js");
			se.eval(new InputStreamReader(url.openStream()));
			
			Object t = se.eval("md5(md5_3(\"" + pass + "\")+\"" + code.toUpperCase() + "\");");
			return t.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	private class PollMessageThread extends Thread {

		public void run() {
			String pollUrl = HOST_D + "/channel/poll?clientid=" + clientid + "&psessionid=" + psessionid;
			
			while (run) {
				try {
					String ret = sendHttpMessage(pollUrl, METHOD.GET, null);
					
					JSONObject retJ = new JSONObject(ret);
					int retcode = retJ.getInt("retcode");
				
					if (retcode == 0) {
						JSONArray result = retJ.getJSONArray("result");
						String poll_type = result.getJSONObject(0).getString("poll_type");
						
						message.sendMessage(MiniQQClient.this, poll_type, retJ);
					}
					else if (retcode == 121) {
						run = false;
						print("QQ已经在别处登录！");
					}
					else {
						sleep(3000);
					}
				} catch (Exception e) {
					print("Response PollMessage failure .");
				}
			}
		}
	}
	
	private void print(Object o) {
		prt.println(o);
	}

	public ILoginModel getLoginModel() {
		return new ILoginModel() {
			public String cookie() 		{ return cookie; }
			public String ptwebqq() 	{ return ptwebqq; }
			public String session() 	{ return psessionid; }
			public String skey() 		{ return skey; }
			public String vfwebqq() 	{ return vfwebqq; }
			public String clientid()	{ return String.valueOf(clientid); }
			public long   t()			{ return System.currentTimeMillis(); }
		};
	}

	@Override
	public Group getGroup(long code) {
		// 此处无用
		return null;
	}


}
