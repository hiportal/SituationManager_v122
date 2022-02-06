package com.ex.situationmanager.dto;

import com.ex.situationmanager.util.Common;

import java.io.Serializable;

public class TunnelNameVo implements Serializable {

    String tunnelName;
    String fclts_intg_id;
    String gubun;

    public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }

    public String getFclts_intg_id() {
        return fclts_intg_id;
    }

    public void setFclts_intg_id(String fclts_intg_id) {
        this.fclts_intg_id = fclts_intg_id;
    }

    public String getGubun() {
        return gubun;
    }

    public void setGubun(String gubun) {
        this.gubun = gubun;
    }



}
