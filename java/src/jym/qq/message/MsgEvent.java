// CatfoOD 2011-11-24 下午01:48:59 yanming-sohu@sohu.com/@qq.com

package jym.qq.message;


import jym.qq.core.Group;
import jym.qq.core.IPrinter;
import jym.qq.core.IServerConn;
import jym.qq.core.User;
import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;


public class MsgEvent {
	
	public final IServerConn server;
	public final IPrinter print;
	
	private JSONObject root;
	private JSONArray result;
	private JSONObject value;
	
	
	public MsgEvent(JSONObject _root, IServerConn _server, IPrinter _print) throws JSONException {
		root = _root;
		server = _server;
		print = _print;
		
		result = root.getJSONArray("result");
		value = result.getJSONObject(0).getJSONObject("value");
	}

	public JSONObject getRoot() {
		return root;
	}

	public JSONArray getResult() {
		return result;
	}

	public JSONObject getValue() {
		return value;
	}
	
	/** 输出使用该方法 */
	public void log(Object o) {
		print.println(o);
	}
	
	public User getFriend(long id) {
		return server.getFriend(id);
	}
	
	public Group getGroup(long code,long senduin) {
		return server.getGroup(code,senduin);
	}
}
