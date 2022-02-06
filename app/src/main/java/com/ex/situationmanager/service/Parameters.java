package com.ex.situationmanager.service;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.util.Log;

public class Parameters implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String primitive = "";
	private Map<String, Object> params = new HashMap<String, Object>();

	@SuppressWarnings("unused")
    public Parameters() { };
	
	public Parameters(String primitive) {
		this.primitive = primitive;
	}
	
	public boolean containsKey(String key) {
		return params.containsKey(key);
	}
	
	public void put(String key, String value) {
		params.put(key, value);
	}
	
	public void put(String key, String[] values) {
		params.put(key, values);
	}
	
	public String get(String key) {
		Object value = params.get(key);
		if(value instanceof String) {
			return (String)params.get(key);
		} else {
			return value.toString();
		}
	}

	public String[] getArray(String key) {
		Object value = params.get(key);
		if(value instanceof String[]) {
			return (String[])params.get(key);
		} else if(value instanceof String) {
			return new String[] { (String)value };
		} else {
			return null;
		}
	}
	
	private Object getObject(String key) {
		return params.get(key);
	}

	public void append(Parameters p) {
		Iterator<String> it = p.iterator();
		while(it.hasNext()) {
			String key   = it.next();
			Object value = p.getObject(key);
			params.put(key, value);
		}
	}

	public void remove(String key) {
		params.remove(key);
	}

	public Iterator<String> iterator() {
		return params.keySet().iterator();
	}

	public void setPrimitive(String primitive){
		this.primitive = primitive;
	}
	
	public String getPrimitive() {
		return primitive;
	}
	
	public String toString() {
		return toString(false);
	}
	
	public String toString(boolean postFlag) {
		StringBuffer buf = new StringBuffer();
		buf.append("primitive=" + getPrimitive());
		Iterator<String> it = iterator();
		while(it.hasNext()) {
			String key   = it.next();
			Object value = params.get(key);
			if(value == null) {
				buf.append("&" + key + "=");
				System.out.println("KEY[=" + key + "] is null");
				continue;
			}

			String[] values = null;
			if(value instanceof String) {
				values = new String[] {(String)value};
			} else if(value instanceof String[]) {
				values = (String[])value;
			}

			for(int i=0; i<values.length; i++) {
				if(values[i]==null || values[i].length()==0) {
					System.out.println("KEY[=" + key + "] is null");
					values[i] = "";
				} else {
					try {
						System.out.println("encoding check = " + values[i]);
					} catch (ArrayIndexOutOfBoundsException | NumberFormatException | NullPointerException e) {
						Log.e("에러","예외");
					}
				}

				//운영 배포시
				buf.append("&" + key + "=" + values[i]);


				//최창유 로컬 테스트용

			/*	try{
					if(!key.equals("hp_no")){
						buf.append("&" + key + "=" + values[i]);
						Log.i("이게 과연 뭘까?",values[i]);
					}else{
						Log.i("이게 과연 뭘까?",values[i]);
						buf.append("&" + key + "=" + "010-7152-0043");
					}
				}catch (Exception e){
					e.printStackTrace();
				}*/

			//	Log.i("키밸류 확인","키:"+key+", "+values[i]);
			}
		}
		return buf.toString();
	}
	

}
