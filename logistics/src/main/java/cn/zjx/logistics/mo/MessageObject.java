package cn.zjx.logistics.mo;


public class MessageObject {
	private  Integer code; 
	private  String msg;
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "MessageObject [code=" + code + ", msg=" + msg + "]";
	}
	public MessageObject(Integer code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}
	
	
}
