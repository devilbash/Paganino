package it.bestapp.paganino.utility.connessione;


import java.util.List;

import org.apache.http.Header;
import org.apache.http.cookie.Cookie;


public class Login {

	private String userid,
				   password,
				   __EVENTVALIDATION,
				   __VIEWSTATE;
	
	
	private List<Cookie> cookies;
	private Header headers[];

	
	public Login(String userid, String psw) {
		this.userid   = userid;
		this.password = psw;
	} 

	public Login(String id, String psw, List<Cookie> c, Header[] h ) {
		this.password = psw;
		this.userid = id;
		this.cookies = c; 
		this.headers = h; 
	} 	

	public void setEVENTVALIDATION (String s){
		__EVENTVALIDATION = s;
	}
	
	public void setVIEWSTATE (String s){
		__VIEWSTATE = s;
	}

	public String getEVENTVALIDATION (){
		return __EVENTVALIDATION;
	}
	
	public String getVIEWSTATE (){
		return __VIEWSTATE ;
	}
	
	
	public String getUser() {
		return this.userid;
	}
	
	public String getPassword() {
		return this.password;	
	}
	
	public List<Cookie> getCookies() {
		return this.cookies;
	}
	
	public void setCookies(List<Cookie> c) {
		this.cookies = c;	
	}	
	
	public Header[] getHeaders() {
		return this.headers;
	}
	
	public void setHeaders(Header[] h) {
		this.headers = h;	
	}	
	
	public String getUserid() {
		return this.userid;
	}
	
	public void setUserid(String s) {
		this.userid = s;	
	}


}