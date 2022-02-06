package com.ex.situationmanager.dto;

import java.util.ArrayList;

public class Userinfo {

	//공통
	static String user_type = "";//사용자타입 0001:순찰, 0002:견인, 0003:대국민
	static ArrayList<String> bscode_list = new ArrayList<String>();//지사코드
	static ArrayList<String> bsname_list = new ArrayList<String>();//지사명


	//2020.12 터널화재pdf
	static String tunnelImp = ""; // 터널화재 접근가능

	public static String getTunnelImp() {
		return tunnelImp;
	}

	public static void setTunnelImp(String tunnelImp) {
		Userinfo.tunnelImp = tunnelImp;
	}


	//견인
	static ArrayList<String> crdns_id_list = new ArrayList<String>();//견인차량, 지사코드 의 종합적인 pk  
	static String reg_post = "";//업체명
	static String reg_part = "";//업무종류(견인)
	static String reg_name = "";//사용자이름
	static ArrayList<String> rcv_yn_list = new ArrayList<String>();//주접보지사여부 YN
	static ArrayList<String> rcv_bs_list = new ArrayList<String>();//접보받을 지사 목록 N00441|N00147 구분자 |
	static String tel_no = "";//지사 전화번호
	
	//순찰.
	static String patcar_id = "";//순찰차 일련번호
	static String car_nm = "";//순찰차명
	static String hp_no = "";//전화번호
	static String group_id;//그룹 아이디 ( 견인  사황목록 조회시 사용된다)



	public static String getGroup_id() {
		return group_id;
	}
	public static void setGroup_id(String group_id) {
		Userinfo.group_id = group_id;
	}
	public String getTel_no() {
		return tel_no;
	}
	public void setTel_no(String tel_no) {
		this.tel_no = tel_no;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	public ArrayList<String> getBscode_list() {
		return bscode_list;
	}
	public void setBscode_list(ArrayList<String> bscode_list) {
		this.bscode_list = bscode_list;
	}
	public ArrayList<String> getBsname_list() {
		return bsname_list;
	}
	public void setBsname_list(ArrayList<String> bsname_list) {
		this.bsname_list = bsname_list;
	}
	public String getHp_no() {
		return hp_no;
	}
	public void setHp_no(String hp_no) {
		this.hp_no = hp_no;
	}
	public ArrayList<String> getCrdns_id_list() {
		return crdns_id_list;
	}
	public void setCrdns_id_list(ArrayList<String> crdns_id_list) {
		this.crdns_id_list = crdns_id_list;
	}
	public String getReg_post() {
		return reg_post;
	}
	public void setReg_post(String reg_post) {
		this.reg_post = reg_post;
	}
	public String getReg_part() {
		return reg_part;
	}
	public void setReg_part(String reg_part) {
		this.reg_part = reg_part;
	}
	public String getReg_name() {
		return reg_name;
	}
	public void setReg_name(String reg_name) {
		this.reg_name = reg_name;
	}
	public ArrayList<String> getRcv_yn_list() {
		return rcv_yn_list;
	}
	public void setRcv_yn_list(ArrayList<String> rcv_yn_list) {
		this.rcv_yn_list = rcv_yn_list;
	}
	public ArrayList<String> getRcv_bs_list() {
		return rcv_bs_list;
	}
	public void setRcv_bs_list(ArrayList<String> rcv_bs_list) {
		this.rcv_bs_list = rcv_bs_list;
	}
	public String getPatcar_id() {
		return patcar_id;
	}
	public void setPatcar_id(String patcar_id) {
		this.patcar_id = patcar_id;
	}
	public String getCar_nm() {
		return car_nm;
	}
	public void setCar_nm(String car_nm) {
		this.car_nm = car_nm;
	}


	
}
