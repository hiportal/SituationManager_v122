package com.ex.gongsa;

public class Configuration {


    /**
     * 개발 서버
     */
/* public static final String DEFAULT_DOMAIN="http://192.168.1.2";
   public static final String DEFAULT_PORT="8080";*/
   /*public static final String DEFAULT_DOMAIN="http://192.168.1.24";
   public static final String DEFAULT_PORT="8081";*/

    //공사관리 운영 서버 정보
    public static final String DEFAULT_DOMAIN = "http://workm.ex.co.kr";
    public static final String DEFAULT_PORT = "5004";
    public static final String SERVER_URL = DEFAULT_DOMAIN + ":" + DEFAULT_PORT;

    public static final String JEGUBUN_JAKUPBOGO_SELECT = "JEGUBUN_JAKUPBOGO_SELECT";
    public static final String JEGUBUN_JAKUPBOGOGUBUN_INSERT = "JEGUBUN_JAKUPBOGOGUBUN_INSERT";
}
