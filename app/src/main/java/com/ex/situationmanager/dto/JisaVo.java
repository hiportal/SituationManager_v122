package com.ex.situationmanager.dto;

public class JisaVo {
    /*
       내부직원용 Activity에서 사용될 지사 VO
     */
    private String jisaName="";//지사 이름
    private String jisaCode = "";//지사 코드

    public String getJisaName() { return jisaName; }
    public void setJisaName(String jisaName) { this.jisaName = jisaName; }
    public String getJisaCode() { return jisaCode; }
    public void setJisaCode(String jisaCode) { this.jisaCode = jisaCode; }
}
