package com.ex.gongsa.vo;

import java.util.ArrayList;

public class KyoTongResultVo {

    private String drctClssCd;//방향
    private String trfcLimtStatCd;//처리상태
    private String mtnofCd;//지사코드
    private String routeCd;//차선코드
    private String rmrkCtnt;//비고란 (본부,작업유형,고순대번호, 장비목록;
    private String routeNm;
    private String mtnofNm;

    private String cnstnCtnt;//공사내용
    private String blcStrtDttm;//시작날짜
    private String blcRevocDttm;//종료날짜
    private String strtBlcPntVal;//시작구간
    private String endBlcPntVal;//종료구간
    private String mvblBlcYn;//이동차단 여부
    private String mtnofPrchEmno;//사원번호(지사 감독원)
    private String totCrgwCnt;//총 차로수
    private String trfcLimtCrgwClssCd;    //교통제한 차로 구분코드


    private ArrayList<String> cmmdNm;//장비이름
    private String hdqrNm;//본부이름
    private String workType;//작업유형

    private String humanCnt;
    private String gosundaeNo;

    public String getDrctClssCd() {
        return drctClssCd;
    }

    public void setDrctClssCd(String drctClssCd) {
        this.drctClssCd = drctClssCd;
    }

    public String getTrfcLimtStatCd() {
        return trfcLimtStatCd;
    }

    public void setTrfcLimtStatCd(String trfcLimtStatCd) {
        this.trfcLimtStatCd = trfcLimtStatCd;
    }

    public String getMtnofCd() {
        return mtnofCd;
    }

    public void setMtnofCd(String mtnofCd) {
        this.mtnofCd = mtnofCd;
    }

    public String getRouteCd() {
        return routeCd;
    }

    public void setRouteCd(String routeCd) {
        this.routeCd = routeCd;
    }

    public String getRmrkCtnt() {
        return rmrkCtnt;
    }

    public void setRmrkCtnt(String rmrkCtnt) {
        this.rmrkCtnt = rmrkCtnt;
    }

    public String getRouteNm() {
        return routeNm;
    }

    public void setRouteNm(String routeNm) {
        this.routeNm = routeNm;
    }

    public String getMtnofNm() {
        return mtnofNm;
    }

    public void setMtnofNm(String mtnofNm) {
        this.mtnofNm = mtnofNm;
    }

    public String getCnstnCtnt() {
        return cnstnCtnt;
    }

    public void setCnstnCtnt(String cnstnCtnt) {
        this.cnstnCtnt = cnstnCtnt;
    }

    public String getBlcStrtDttm() {
        return blcStrtDttm;
    }

    public void setBlcStrtDttm(String blcStrtDttm) {
        this.blcStrtDttm = blcStrtDttm;
    }

    public String getBlcRevocDttm() {
        return blcRevocDttm;
    }

    public void setBlcRevocDttm(String blcRevocDttm) {
        this.blcRevocDttm = blcRevocDttm;
    }

    public String getStrtBlcPntVal() {
        return strtBlcPntVal;
    }

    public void setStrtBlcPntVal(String strtBlcPntVal) {
        this.strtBlcPntVal = strtBlcPntVal;
    }

    public String getEndBlcPntVal() {
        return endBlcPntVal;
    }

    public void setEndBlcPntVal(String endBlcPntVal) {
        this.endBlcPntVal = endBlcPntVal;
    }

    public String getMvblBlcYn() {
        return mvblBlcYn;
    }

    public void setMvblBlcYn(String mvblBlcYn) {
        this.mvblBlcYn = mvblBlcYn;
    }

    public String getMtnofPrchEmno() {
        return mtnofPrchEmno;
    }

    public void setMtnofPrchEmno(String mtnofPrchEmno) {
        this.mtnofPrchEmno = mtnofPrchEmno;
    }

    public String getTotCrgwCnt() {
        return totCrgwCnt;
    }

    public void setTotCrgwCnt(String totCrgwCnt) {
        this.totCrgwCnt = totCrgwCnt;
    }

    public String getTrfcLimtCrgwClssCd() {
        return trfcLimtCrgwClssCd;
    }

    public void setTrfcLimtCrgwClssCd(String trfcLimtCrgwClssCd) {
        this.trfcLimtCrgwClssCd = trfcLimtCrgwClssCd;
    }

    public ArrayList<String> getCmmdNm() {
        return cmmdNm;
    }

    public void setCmmdNm(ArrayList<String> cmmdNm) {
        this.cmmdNm = cmmdNm;
    }

    public String getHdqrNm() {
        return hdqrNm;
    }

    public void setHdqrNm(String hdqrNm) {
        this.hdqrNm = hdqrNm;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getHumanCnt() {
        return humanCnt;
    }

    public void setHumanCnt(String humanCnt) {
        this.humanCnt = humanCnt;
    }

    public String getGosundaeNo() {
        return gosundaeNo;
    }

    public void setGosundaeNo(String gosundaeNo) {
        this.gosundaeNo = gosundaeNo;
    }

    @Override
    public String toString() {
        return "KyoTongResultVo{" +
                "drctClssCd='" + drctClssCd + '\'' +
                ", trfcLimtStatCd='" + trfcLimtStatCd + '\'' +
                ", mtnofCd='" + mtnofCd + '\'' +
                ", routeCd='" + routeCd + '\'' +
                ", rmrkCtnt='" + rmrkCtnt + '\'' +
                ", routeNm='" + routeNm + '\'' +
                ", mtnofNm='" + mtnofNm + '\'' +
                ", cnstnCtnt='" + cnstnCtnt + '\'' +
                ", blcStrtDttm='" + blcStrtDttm + '\'' +
                ", blcRevocDttm='" + blcRevocDttm + '\'' +
                ", strtBlcPntVal='" + strtBlcPntVal + '\'' +
                ", endBlcPntVal='" + endBlcPntVal + '\'' +
                ", mvblBlcYn='" + mvblBlcYn + '\'' +
                ", mtnofPrchEmno='" + mtnofPrchEmno + '\'' +
                ", totCrgwCnt='" + totCrgwCnt + '\'' +
                ", trfcLimtCrgwClssCd='" + trfcLimtCrgwClssCd + '\'' +
                ", cmmdNm=" + cmmdNm +
                ", hdqrNm='" + hdqrNm + '\'' +
                ", workType='" + workType + '\'' +
                ", humanCnt='" + humanCnt + '\'' +
                ", gosundaeNo='" + gosundaeNo + '\'' +
                '}';
    }
}
