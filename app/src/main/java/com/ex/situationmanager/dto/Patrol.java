package com.ex.situationmanager.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Patrol implements Serializable {



	//상황일지 번호  
	String rpt_id = "";
	//부서코드
	String bscode = "";
	//기록일자
	String reg_date = "";
	//시간
	String reg_time = "";
	//상황유형 ex) 0001:기타, 0002:잡물, 0003:차단작업, 0004:고장차량, 0005:사고발생, 0006:긴급견인, 0007:동물, 0008:도로진입제한, 0009:지정체, 0010:불량차량, 0011::노면(시설물)파손 
	String reg_type = "";
	//접보자 정보 ex) 콜센터, 본부
	String reg_info = "";
	//내용
	String reg_data = "";
	//상황일지 입력값
	String inp_val = "";
	//노선코드
	String local_way = "";
	//이정
	String start_km = "";
	//차선 ex) 0001:1차로, 0002:2차로, 0003:3차로, 0004:4차로, 0005:5차로
	String lane_num = "";
	//당사자전화번호
	String psn_tel_no = "";
	//제보자전화번호
	String reg_tel_no = "";
	//결과 ex) 0001:접보, 0002:순찰차확인, 0003:기타, 0004:본부전달완료, 0005:완료
	String r_result = "";
	//지사명
	String local_nm = "";
	//방향명
	String direction_name = "";
	//출동수
	String startcount = "";
	
	//좌표정보.
	String rpt_lo = "";
	String rpt_la = "";
	
	//조치중N, 조치완료Y
	String end_yn = "";
	//업체전화번호.
	String etc = "";
	//조치완료 시간.
	String end_time = "";
	
	//사요여부.Y,N
	String use_yn = "";
	String update_cartime = "";
	
	String modegubun = "";//real or train : 실제상황 or 훈련상황



	// 2020.12 터널명 조회시 필요한 코
	String stpnt_ic_fclts_intg_id = "";

	//원클릭 내부직원 사용할 vo 추가
	/*
	    2019/12/09
	    최창유
	 */

	//cctv fronURL
	private String front_cctv_url="";
	private String rear_cctv_url="";
	private String end_km;
	private String acdnt_id="";//상황조회를 위한 파라미터


	public String getRoute_no() {
		return route_no;
	}

	public void setRoute_no(String route_no) {
		this.route_no = route_no;
	}

	private String route_no;

	public String getRoad_id() {
		return road_id;
	}

	public void setRoad_id(String road_id) {
		this.road_id = road_id;
	}

	private  String road_id;
	//private String route_no;

	//private String roadNo;
	public String getRoadNo() {
		return roadNo;
	}

	public void setRoadNo(String roadNo) {
		this.roadNo = roadNo;
	}

	private  String roadNo;;



	public String getEnd_km() {
		return end_km;
	}

	public void setEnd_km(String end_km) {
		this.end_km = end_km;
	}

	public String getFront_cctv_url() {
		return front_cctv_url;
	}

	public void setFront_cctv_url(String front_cctv_url) {
		this.front_cctv_url = front_cctv_url;
	}

	public String getRear_cctv_url() {
		return rear_cctv_url;
	}

	public void setRear_cctv_url(String rear_cctv_url) {
		this.rear_cctv_url = rear_cctv_url;
	}

	public String getAcdnt_id() {
		return acdnt_id;
	}

	public void setAcdnt_id(String acdnt_id) {
		this.acdnt_id = acdnt_id;
	}



	
	public String getModegubun() {
		return modegubun;
	}
	public void setModegubun(String modegubun) {
		this.modegubun = modegubun;
	}
	public String getUpdate_cartime() {
		return update_cartime;
	}
	public void setUpdate_cartime(String update_cartime) {
		this.update_cartime = update_cartime;
	}
	public String getUse_yn() {
		return use_yn;
	}
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getEtc() {
		return etc;
	}
	public void setEtc(String etc) {
		this.etc = etc;
	}
	public String getEnd_yn() {
		return end_yn;
	}
	public void setEnd_yn(String end_yn) {
		this.end_yn = end_yn;
	}
	public String getRpt_lo() {
		return rpt_lo;
	}
	public void setRpt_lo(String rpt_lo) {
		this.rpt_lo = rpt_lo;
	}
	public String getRpt_la() {
		return rpt_la;
	}
	public void setRpt_la(String rpt_la) {
		this.rpt_la = rpt_la;
	}
	public String getRpt_id() {
		return rpt_id;
	}
	public void setRpt_id(String rpt_id) {
		this.rpt_id = rpt_id;
	}
	public String getBscode() {
		return bscode;
	}
	public void setBscode(String bscode) {
		this.bscode = bscode;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public String getReg_time() {
		return reg_time;
	}
	public void setReg_time(String reg_time) {
		this.reg_time = reg_time;
	}
	public String getReg_type() {
		return reg_type;
	}
	public void setReg_type(String reg_type) {
		this.reg_type = reg_type;
	}
	public String getReg_info() {
		return reg_info;
	}
	public void setReg_info(String reg_info) {
		this.reg_info = reg_info;
	}
	public String getReg_data() {
		return reg_data;
	}
	public void setReg_data(String reg_data) {
		this.reg_data = reg_data;
	}
	public String getInp_val() {
		return inp_val;
	}
	public void setInp_val(String inp_val) {
		this.inp_val = inp_val;
	}
	public String getLocal_way() {
		return local_way;
	}
	public void setLocal_way(String local_way) {
		this.local_way = local_way;
	}
	public String getStart_km() {
		return start_km;
	}
	public void setStart_km(String start_km) {
		this.start_km = start_km;
	}
	public String getLane_num() {
		return lane_num;
	}
	public void setLane_num(String lane_num) {
		this.lane_num = lane_num;
	}
	public String getPsn_tel_no() {
		return psn_tel_no;
	}
	public void setPsn_tel_no(String psn_tel_no) {
		this.psn_tel_no = psn_tel_no;
	}
	public String getReg_tel_no() {
		return reg_tel_no;
	}
	public void setReg_tel_no(String reg_tel_no) {
		this.reg_tel_no = reg_tel_no;
	}
	public String getR_result() {
		return r_result;
	}
	public void setR_result(String r_result) {
		this.r_result = r_result;
	}
	public String getLocal_nm() {
		return local_nm;
	}
	public void setLocal_nm(String local_nm) {
		this.local_nm = local_nm;
	}
	public String getDirection_name() {
		return direction_name;
	}
	public void setDirection_name(String direction_name) {
		this.direction_name = direction_name;
	}
	public String getStartcount() {
		return startcount;
	}
	public void setStartcount(String startcount) {
		this.startcount = startcount;
	}

	//2020.12 터널pdf
	public String getStpnt_ic_fclts_intg_id() {
		return stpnt_ic_fclts_intg_id;
	}

	public void setStpnt_ic_fclts_intg_id(String stpnt_ic_fclts_intg_id) {
		this.stpnt_ic_fclts_intg_id = stpnt_ic_fclts_intg_id;
	}


    @Override
    public String toString() {
        return "Patrol{" +
                "rpt_id='" + rpt_id + '\'' +
                ", bscode='" + bscode + '\'' +
                ", reg_date='" + reg_date + '\'' +
                ", reg_time='" + reg_time + '\'' +
                ", reg_type='" + reg_type + '\'' +
                ", reg_info='" + reg_info + '\'' +
                ", reg_data='" + reg_data + '\'' +
                ", inp_val='" + inp_val + '\'' +
                ", local_way='" + local_way + '\'' +
                ", start_km='" + start_km + '\'' +
                ", lane_num='" + lane_num + '\'' +
                ", psn_tel_no='" + psn_tel_no + '\'' +
                ", reg_tel_no='" + reg_tel_no + '\'' +
                ", r_result='" + r_result + '\'' +
                ", local_nm='" + local_nm + '\'' +
                ", direction_name='" + direction_name + '\'' +
                ", startcount='" + startcount + '\'' +
                ", rpt_lo='" + rpt_lo + '\'' +
                ", rpt_la='" + rpt_la + '\'' +
                ", end_yn='" + end_yn + '\'' +
                ", etc='" + etc + '\'' +
                ", end_time='" + end_time + '\'' +
                ", use_yn='" + use_yn + '\'' +
                ", update_cartime='" + update_cartime + '\'' +
                ", modegubun='" + modegubun + '\'' +
                ", front_cctv_url='" + front_cctv_url + '\'' +
                ", rear_cctv_url='" + rear_cctv_url + '\'' +
                ", end_km='" + end_km + '\'' +
                ", acdnt_id='" + acdnt_id + '\'' +
                ", route_no='" + route_no + '\'' +
                ", road_id='" + road_id + '\'' +
                ", roadNo='" + roadNo + '\'' +
				", stpnt_ic_fclts_intg_id='" + stpnt_ic_fclts_intg_id + '\'' +
                '}';
    }



}
