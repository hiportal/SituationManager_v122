package com.ex.situationmanager.dto;

public class BonBuVo {

    //내부직원용 Activity에서 사용될 본부 리스트 VO
    private String bonbuCode="";//본부코드
    private String bonbuName="";//지사코드

    public String getBonbuCode() {return bonbuCode; }//본
    public void setBonbuCode(String bonbuCode) { this.bonbuCode = bonbuCode; }
    public String getBonbuName() { return bonbuName; }
    public void setBonbuName(String bonbuName) { this.bonbuName = bonbuName; }
}
