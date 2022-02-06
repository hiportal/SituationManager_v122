package com.ex.situationmanager.util;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.ex.situationmanager.BaseActivity;
import com.ex.situationmanager.dto.TowBscode;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

/**
 * Asset Database 접근 관리
 *
 * @author JSJ
 * 2015-09-09
 */
public class DBAdapter {

    private String TAG = "DBAdapter";
    private static SQLiteDatabase sqliteDatabase;
    private final String package_name = "com.ex.situationmanager";

    /**
     * 생성자
     */
    public DBAdapter() {

    }

    public void init() {
        File sdcard = Environment.getExternalStorageDirectory();
        File dbpath = new File(sdcard.getAbsolutePath() + File.separator);
        Log.d(TAG, "DB directory. " + dbpath.getAbsolutePath());

        Log.d("process", "db create");

        String dbfile = "/data/data/" + package_name + "/databases/exApp.db";

        try {
            Log.d(TAG, "OPEN DB : " + dbfile);
            Log.d("process", "open db1");

            sqliteDatabase = SQLiteDatabase.openDatabase(dbfile, null, SQLiteDatabase.OPEN_READWRITE);

        } catch (Exception e) {
            Log.d(TAG, "Exception : DB is not exist. ");
        }
    }


    /**
     * 노선 정보
     *
     * @return Cursor
     */
    public Cursor fetchRange(Double latitude, Double longitude, String ns_code) {

        StringBuffer sql = new StringBuffer();

        sql.append("   SELECT nscode,													\n");
        sql.append("          nsmyeong,                                                 \n");
        sql.append("          MIN (ijeong),                                             \n");
        sql.append("          MIN (xgap+xgap2) as xgap,                                 \n");
        sql.append("          '0'  icmyeong,                                            \n");
        sql.append("          rampid                                            		\n");
        sql.append("     FROM (SELECT nscode,                                         \n");
        sql.append("                  nsmyeong,                            				\n");
        sql.append("                  ijeong,                                 			\n");
        sql.append("                  kyungdo,                                         \n");
        sql.append("                  wido,                                            \n");
        sql.append("                  ABS (kyungdo - " + longitude + ") xgap,             \n");
        sql.append("                  ABS (wido - " + latitude + ") xgap2,             \n");
        sql.append("                  rampid             								\n");
        sql.append("             FROM nscode a                                        \n");
        sql.append("            WHERE     1 = 1                                        \n");
        sql.append("                  AND kyungdo + 0.0015 > " + longitude + "\n");
        sql.append("                  AND kyungdo - 0.0015 < " + longitude + "\n");
        sql.append("                  AND wido + 0.0015 > " + latitude + "\n");
        sql.append("                  AND wido - 0.0015 < " + latitude + ")                \n");
        sql.append(" GROUP BY nscode, nsmyeong                                         \n");
        sql.append(" ORDER BY MIN (xgap) DESC                                           \n");

        Cursor cursor = sqliteDatabase.rawQuery(sql.toString(), null);
        Log.d("process", "query db1");
//		Log.d("process", "sql.toString() : "+sql.toString());
        return cursor;
    }

    //좌표 거리 계산
    public double calDistance(double lat1, double lon1, double lat2, double lon2) {

        double theta, dist;
        theta = lon1 - lon2;
        dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);

        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;    // 단위 mile 에서 km 변환.
        dist = dist * 1000.0;      // 단위  km 에서 m 로 변환

        return dist;
    }

    // 주어진 도(degree) 값을 라디언으로 변환  
    private double deg2rad(double deg) {
        return (double) (deg * Math.PI / (double) 180d);
    }

    // 주어진 라디언(radian) 값을 도(degree) 값으로 변환
    private double rad2deg(double rad) {
        return (double) (rad * (double) 180d / Math.PI);
    }


    /**
     * 터널내 좌표수신 불가시 다음 이정값 가져오기
     *
     * @param ns_code
     * @param ijung
     * @param bhcode
     * @return Cursor
     */
    public Cursor fetchNextRange(String ns_code, String ijung, String bhcode) {
        if ("".equals(Common.nullCheck(ijung))) {
            return null;
        }
        Log.d("", TAG + " fetchNextRange curIjeong = " + ijung);

        double curIjeongS = Common.nullCheckDouble(ijung);
        double nextIjeongS = Math.round((curIjeongS - 0.04) * 10000d) / 10000d;
        double nnextIjeongS = Math.round((nextIjeongS - 0.06) * 10000d) / 10000d;

        double curIjeongE = Common.nullCheckDouble(ijung);
        double nextIjeongE = Math.round((curIjeongE + 0.04) * 10000d) / 10000d;
        double nnextIjeongE = Math.round((nextIjeongE + 0.06) * 10000d) / 10000d;


        StringBuffer sql = new StringBuffer();

        sql.append("	SELECT * FROM(                             \n");
        sql.append("	SELECT nscode,                             \n");
        sql.append("			nsmyeong,                          \n");
        sql.append("			ijeong,                            \n");
        sql.append("			ijeong,                            \n");
        sql.append("			'0' icmyeong,                      \n");
        sql.append("			rampid,                            \n");
        sql.append("			kyungdo,                           \n");
        sql.append("			wido	                           \n");
        sql.append("	FROM                                       \n");
        sql.append("		nscode a                               \n");
        sql.append("	WHERE     1 = 1                            \n");
        if ("E".equals(bhcode)) {
            sql.append("	AND CAST (ijeong AS DOUBLE) = " + nextIjeongE + "\n");
            sql.append("	OR CAST (ijeong AS DOUBLE) = " + ijung + "\n");
            sql.append("	OR CAST (ijeong AS DOUBLE) = " + nnextIjeongE + "\n");
            Log.d("", TAG + " fetchNextRange nextIjeong = " + nextIjeongE);
        } else {
            sql.append("	AND CAST (ijeong AS DOUBLE) = " + nextIjeongS + "\n");
            sql.append("	OR CAST (ijeong AS DOUBLE) = " + ijung + "\n");
            sql.append("	OR CAST (ijeong AS DOUBLE) = " + nnextIjeongS + "\n");
            Log.d("", TAG + " fetchNextRange nextIjeong = " + nextIjeongS);
        }
        sql.append("	)WHERE nscode = '" + ns_code + "'     			\n");
        sql.append("	AND rampid IS NULL     			   			\n");
        if ("E".equals(bhcode)) {
            sql.append("	ORDER BY ijeong ASC     			   \n");
        } else {
            sql.append("	ORDER BY ijeong DESC    			   \n");
        }


        Cursor cursor = sqliteDatabase.rawQuery(sql.toString(), null);
        Log.d("process", "query db1");
//		Log.d("process", "sql.toString() : "+sql.toString());

        return cursor;
    }


    /**
     * 노선 내  시설물 검색
     * 조회범위 약 (160m)
     *
     * @param nsCode    노선코드
     * @param latitude  위도
     * @param longitude 경도
     * @return rtnStr 시설물 명
     * 우선순위
     * IC 1
     * JCT 2
     * TG 3
     * 휴게소 4
     * 졸음쉼터 5
     * 터널 6
     * 교량 7
     */
    public String fetchSisulAll(String nsCode, double latitude, double longitude, String bhCode) {
        String rtnStr = "";
        StringBuffer sql = new StringBuffer();


        sql.append("	SELECT code,                                                      	\n");
        sql.append("	xgap,                                                             	\n");
        sql.append("	ijung,                                                             	\n");
        sql.append("	gubun                                                             	\n");
        sql.append("	FROM(                                                             	\n");
        sql.append("	SELECT code,                                                      	\n");
        sql.append("	ABS (longitude -" + Math.abs(longitude) + " + latitude -" + Math.abs(latitude) + ") as xgap,	\n");
        sql.append("	bhcode,                                                           	\n");
        sql.append("	ijung,                                                             	\n");
        sql.append("	gubun                                                             	\n");
        sql.append("	FROM sisul                                                        	\n");
        sql.append("  WHERE     1 = 1                                   					\n");
        sql.append("        AND longitude + 0.0050 > " + longitude + "						\n");
        sql.append("        AND longitude - 0.0050 < " + longitude + "						\n");
        sql.append("        AND latitude + 0.0050 >  " + latitude + "							\n");
        sql.append("        AND latitude - 0.0050 < " + latitude + "							\n");
        sql.append("        and latitude !='0'												\n");
        sql.append("       ) WHERE bhcode ='" + bhCode + "'    									\n");
        sql.append("        OR bhcode = 'O'			   										\n");
        sql.append("        OR bhcode IS NULL    											\n");
        sql.append("  ORDER BY gubun DESC, xgap DESC                      					\n");

        Cursor cursor = sqliteDatabase.rawQuery(sql.toString(), null);

//		Log.d("process", "fetchSisulAll sql= " + sql.toString());
        Log.d("process", "query fetchSisulAll !");
        String resultIjeong = "";
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                try {
                    resultIjeong = String.valueOf(Common.nullCheckDouble(cursor.getString(2)) * 1000);
                    resultIjeong = resultIjeong.substring(0, resultIjeong.indexOf("."));
//					rtnStr = cursor.getString(0)+"_"+Double.parseDouble(resultIjeong)/1000d;
                    rtnStr = cursor.getString(0);

                } catch (Exception e) {
                    Log.e("예외", "예외발생");
                }
            }
        }

        return rtnStr;
    }

    public String[] fetchSisulAllInfo(String nsCode, double latitude, double longitude, String bhCode) {
        String[] rtnList = new String[3];
        String rtnStr = "";
        StringBuffer sql = new StringBuffer();


        sql.append("	SELECT code,                                                      	\n");
        sql.append("	xgap,                                                             	\n");
        sql.append("	ijung,                                                             	\n");
        sql.append("	gubun,                                                             	\n");
        sql.append("	nscode                                                             	\n");
        sql.append("	FROM(                                                             	\n");
        sql.append("	SELECT code,                                                      	\n");
        sql.append("	ABS (longitude -" + Math.abs(longitude) + " + latitude -" + Math.abs(latitude) + ") as xgap,	\n");
        sql.append("	bhcode,                                                           	\n");
        sql.append("	ijung,                                                             	\n");
        sql.append("	gubun,                                                             	\n");
        sql.append("	nscode                                                             	\n");
        sql.append("	FROM sisul                                                        	\n");
        sql.append("  WHERE     1 = 1                                   					\n");
        sql.append("        AND longitude + 0.0050 > " + longitude + "						\n");
        sql.append("        AND longitude - 0.0050 < " + longitude + "						\n");
        sql.append("        AND latitude + 0.0050 >  " + latitude + "							\n");
        sql.append("        AND latitude - 0.0050 < " + latitude + "							\n");
        sql.append("        and latitude !='0'												\n");
        sql.append("       ) WHERE bhcode ='" + bhCode + "'    									\n");
        sql.append("        OR bhcode = 'O'			   										\n");
        sql.append("        OR bhcode IS NULL    											\n");
        sql.append("  ORDER BY gubun DESC, xgap DESC                      					\n");

        Cursor cursor = sqliteDatabase.rawQuery(sql.toString(), null);

//		Log.d("process", "fetchSisulAll sql= " + sql.toString());
        Log.d("process", "query fetchSisulAll !");
        String resultIjeong = "";
        String resultNscode = "";
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                try {
                    resultIjeong = Common.nullCheck(cursor.getString(2));
                    resultIjeong = resultIjeong.substring(0, resultIjeong.indexOf(".") + 2);
                    Log.d("", "resultIjeong1 " + resultIjeong);
                    Log.d("", "resultIjeong2 " + resultIjeong);
                    resultNscode = cursor.getString(4);
//					rtnStr = cursor.getString(0)+"_"+Double.parseDouble(resultIjeong)/1000d;
                    rtnStr = cursor.getString(0);

                } catch (Exception e) {
                    Log.e("예외", "예외발생");
                }
            }
        }
        rtnList[0] = resultNscode;
        rtnList[1] = resultIjeong;
        rtnList[2] = rtnStr;

        return rtnList;
    }

    /**
     * 노선 외  램프 RAMP정보 조회
     * 조회범위 약 (10m)
     *
     * @param latitude  위도
     * @param longitude 경도
     * @return rtnStr 램프정보 name+rampid+ramp_sta
     */
    public String fetchRamp(double latitude, double longitude) {
        String rtnStr = "";
        StringBuffer sql = new StringBuffer();

        sql.append("	SELECT name, ramp_id, ramp_sta,               \n");
        sql.append("	MIN(ABS (wido-" + longitude + ")) as xgap       \n");
        sql.append("	FROM ramp                                     \n");
        sql.append("	WHERE     1 = 1                               \n");
        sql.append("	      AND kyungdo+ 0.00001 > " + longitude + "  \n");
        sql.append("	      AND kyungdo- 0.00001 < " + longitude + "  \n");
        sql.append("	      AND wido + 0.00001 >  " + latitude + "    \n");
        sql.append("	      AND wido - 0.00001 < " + latitude + "     \n");
        sql.append("	ORDER BY xgap DESC                            \n");


        Cursor cursor = sqliteDatabase.rawQuery(sql.toString(), null);
        Log.d("process", "query fetchSisulAll !");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                rtnStr = cursor.getString(0);
            }
        }
        return rtnStr;
    }

    /**
     * 노선 외 톨게이트(TG) 조회
     *
     * @param latitude  위도
     * @param longitude 경도
     * @return rtnStr 톨게이트 명
     */
    public String fetchTG(double latitude, double longitude) {
        String rtnStr = "";
        StringBuffer sql = new StringBuffer();

        sql.append("	SELECT name,                                    \n");
        sql.append("	MIN(ABS (longitude-" + longitude + ")) as xgap          \n");
        sql.append("	FROM tolgate                                    \n");
        sql.append("	WHERE     1 = 1                                 \n");
        sql.append("	      AND longitude+ 0.0016 > " + longitude + "         \n");
        sql.append("	      AND longitude - 0.0016 < " + longitude + "        \n");
        sql.append("	      AND latitude + 0.0016 >  " + latitude + "         \n");
        sql.append("	      AND latitude - 0.0016 < " + latitude + "          \n");
        sql.append("	ORDER BY xgap DESC                              \n");

        Cursor cursor = sqliteDatabase.rawQuery(sql.toString(), null);
        Log.d("process", "query fetchSisulAll !");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                rtnStr = cursor.getString(0);
            }
        }
        return rtnStr;
    }

    //터널. 중요 **************************************
    public String fetchBridge(String nsCode, String ijung, double longitude, double latitude, String bhCode) {

        String rtnStr = "";

        StringBuffer sql;
        sql = new StringBuffer();
        sql.append("	SELECT ftr_name, start_wgs84_lon, start_wgs84_lat, end_wgs84_lon, end_wgs84_lat                     \n");
        sql.append("	         FROM (SELECT                                                                               \n");
        sql.append("	                    ftr_name, start_wgs84_lon, start_wgs84_lat, end_wgs84_lon, end_wgs84_lat,       \n");
        sql.append("			    ABS (start_wgs84_lon - 127.26427000) xgap                                               \n");
        sql.append("	            FROM bridge                                                                             \n");
        sql.append("	           WHERE     nosun_no = '0010'                                                              \n");
        sql.append("	                 AND start_wgs84_lon + 0.0008 > 127.05730                                           \n");
        sql.append("	                 AND start_wgs84_lon - 0.0008 < 127.05730                                           \n");
        sql.append("	                 AND start_wgs84_lat + 0.0008 > 35.90629                                            \n");
        sql.append("	                 AND start_wgs84_lat - 0.0008 < 35.90629                                            \n");
        sql.append("	                 AND end_wgs84_lon + 0.0008 > 127.05730                                             \n");
        sql.append("	                 AND end_wgs84_lon - 0.0008 < 127.05730                                             \n");
        sql.append("	                 AND end_wgs84_lat + 0.0008 > 35.90629                                              \n");
        sql.append("	                 AND end_wgs84_lat - 0.0008 < 35.90629                                              \n");
        sql.append("	                 AND direction_code = 'S'                                              				\n");
        sql.append("	                )                                                                                   \n");
        sql.append("	GROUP BY ftr_name                                                                                   \n");
        sql.append("	ORDER BY MIN (xgap) DESC                                                                            \n");


        Cursor cursorSisul = sqliteDatabase.rawQuery(sql.toString(), null);
        if (cursorSisul.getCount() > 0) {
            while (cursorSisul.moveToLast()) {
//				rtnStr =  cursorSisul.getString(0);
                String a1 = fetcnBridgeSE(Common.nullCheckDouble(cursorSisul.getString(1)), Common.nullCheckDouble(cursorSisul.getString(2)));
                String a2 = fetcnBridgeSE(Common.nullCheckDouble(cursorSisul.getString(3)), Common.nullCheckDouble(cursorSisul.getString(4)));
            }
        }

        return rtnStr;
    }

    public String fetcnBridgeSE(double longitude, double latitude) {
        String rtnStr = "";
        StringBuffer sql = new StringBuffer();

        sql.append("	  SELECT nscode,								                   \n");
        sql.append("	         nsmyeong,                                                 \n");
        sql.append("	         MIN (ijeong),                                             \n");
        sql.append("	         MIN (xgap),                                               \n");
        sql.append("	         '0'  icmyeong                                             \n");
        sql.append("	    FROM (SELECT nscode,                                           \n");
        sql.append("	                 nsmyeong,                                         \n");
        sql.append("	                 ijeong,                                           \n");
        sql.append("	                 kyungdo,                                          \n");
        sql.append("	                 wido,                                             \n");
        sql.append("	                 ABS (kyungdo - " + longitude + ") xgap            \n");
        sql.append("	            FROM nscode a                                          \n");
        sql.append("	           WHERE     1 = 1                                         \n");
        sql.append("	                 AND kyungdo + 0.0008 > 128.13340                  \n");
        sql.append("	                 AND kyungdo - 0.0008 < 128.13340                  \n");
        sql.append("	                 AND wido + 0.0008 > 36.12469                      \n");
        sql.append("	                 AND wido - 0.0008 < 36.12469)                     \n");
        sql.append("	GROUP BY nscode, nsmyeong                                          \n");
        sql.append("	ORDER BY MIN (xgap) DESC                                           \n");


        Cursor cursorSE = sqliteDatabase.rawQuery(sql.toString(), null);
        if (cursorSE.getCount() > 0) {
            while (cursorSE.moveToNext()) {
                rtnStr = cursorSE.getString(2);//이정값
            }
        }

        return rtnStr;
    }


    //TG
    public String fetchTolgate(String nsCode, String ijung, double longitude, double latitude, String bhCode) {

        String rntStr = "";

        StringBuffer sql;
        sql = new StringBuffer();

        sql.append(" select ftr_name                   \n");
        sql.append("from tolgate                                 \n");
        sql.append("          WHERE 1=1                 													\n");
        sql.append("          AND wgs84_lon + 0.0016 > " + longitude + "                  													\n");
        sql.append("          AND wgs84_lon - 0.0016 < " + longitude + "                  													\n");
        sql.append("          AND wgs84_lat + 0.0016 > " + latitude + "                      														\n");
        sql.append("          AND wgs84_lat - 0.0016 < " + latitude + "                      														\n");
//		Log.d("","tolgate sql = " + sql.toString());
        Cursor cursorSisul = sqliteDatabase.rawQuery(sql.toString(), null);
        if (cursorSisul.getCount() > 0) {
            while (cursorSisul.moveToNext()) {
                rntStr = cursorSisul.getString(0);
                Log.d("", "cursor Tolgate Sisul = " + cursorSisul.getString(0));
            }
        }
        return rntStr;
    }


    /**
     * 방향 조회
     *
     * @return Cursor
     */
    public Cursor fetchBangHyang(String nsCode) {

        StringBuffer sql = new StringBuffer();

        sql.append(" SELECT nscode,      												\n");
        sql.append("        nsmyeong,                           							\n");
        sql.append("        gjmyeong,                            							\n");
        sql.append("        jjmyeong                             							\n");
        sql.append("   FROM banghyang                            						\n");
        sql.append("  WHERE nscode = '" + nsCode + "'    						\n");
        sql.append("  ORDER BY nscode					    							\n");

//		Log.d("process", "sql.toString() : "+sql.toString());

        Cursor cursor = sqliteDatabase.rawQuery(sql.toString(), null);
        return cursor;
    }


    /**
     * @param nsCode
     * @param bh_code
     * @param bscode
     * @return
     */
    public String fetchBanghyang_sub(String nsCode, String bh_code, String rpt_bscode, String ijeong) {

        String rtnStr = "";
        StringBuffer sql = new StringBuffer();

        sql.append("	SELECT code      					                      				");
        sql.append("	FROM banghyang_se                                         				");
        sql.append("	WHERE                                    								");
        sql.append("		ns_code = '" + nsCode + "'                                    			");
        sql.append("	AND bh_code = '" + bh_code + "'                                        		");
        sql.append("	AND bscode =                              								");
        sql.append("		(SELECT A.BSCODE                             						");
        sql.append("			FROM                                                     		");
        sql.append("				(SELECT  BSCODE, YEONDO                                    	");
        sql.append("					FROM JISAINFO                                           ");
        sql.append("					WHERE                                     				");
        sql.append("					NSCODE ='" + nsCode + "'                                    ");
        sql.append("					AND CAST('" + ijeong + "' AS DOUBLE)   						");
        sql.append("						BETWEEN CAST(MIN AS DOUBLE)+0.1						");
        sql.append("					AND CAST(MAX AS DOUBLE)            		                ");
        sql.append("					ORDER BY YEONDO ASC                                     ");
        sql.append("				) A                                                     	");
        sql.append("				GROUP BY A.BSCODE                                       	");
        sql.append("		)                                                         			");
        sql.append("		ORDER BY ns_code	                                      			");
        Cursor cursor = sqliteDatabase.rawQuery(sql.toString(), null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                rtnStr = cursor.getString(0);

            }
        }
        return rtnStr;
    }


    /**
     * 공구 조회
     *
     * @return Cursor
     */
    public String fetchGonggu(String nsCode, String ijung) {
        String rtnStr = "";

        StringBuffer sql = new StringBuffer();

        sql.append(" SELECT gonggu      																					\n");
        sql.append("    FROM gonggu                            																\n");
        sql.append("   WHERE nscode = '" + nsCode + "' 																\n");
        sql.append("   AND CAST('" + ijung + "' AS DOUBLE) BETWEEN CAST(minkm AS DOUBLE) 	\n");
        sql.append("   AND CAST(maxkm AS DOUBLE)-0.1															\n");
        sql.append(" union      																									\n");
        sql.append(" SELECT gonggu      																					\n");
        sql.append("    FROM gonggu                            																\n");
        sql.append("   WHERE nscode = '" + nsCode + "' 																\n");
        sql.append("   AND CAST('" + ijung + "' AS DOUBLE) BETWEEN CAST(maxkm AS DOUBLE) \n");
        sql.append("   AND CAST(minkm AS DOUBLE)-0.1															\n");


//		Log.d("process", "fetchgonggu sql.toString() : "+sql.toString());

        Cursor cursor = sqliteDatabase.rawQuery(sql.toString(), null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                rtnStr = cursor.getString(0);
            }
        }

        return rtnStr;

    }

    // Data Select
    public List<String> selectNoseonName() {
        // TODO Auto-generated method stub
        String sql = " select ns_code, ns_name from banghyang_se group by ns_code, ns_name order by ns_name ";

        List<String> list = new ArrayList<String>();
        //Log.e("selectData(): ", sql);
        try {
            Cursor cur = sqliteDatabase.rawQuery(sql, null);
            list.add("노선외");
            while (cur.moveToNext()) {

                list.add(cur.getString(1));
            }

        } catch (SQLException se) {
            // TODO: handle exception
            Log.e("selectData()Error! : ", se.toString());
        }

        return list;
    }

    // Data Select
    public List<String> selectBanghyangName(String nsName, String nsCode) {
        // TODO Auto-generated method stub
        String sql = "";
        if (nsCode != null) {
            sql = " select bh_code, bh_name from banghyang_se where ns_code = '"
                    + nsCode + "'  group by bh_code order by bh_code desc";
        } else {
            sql = " select bh_code, bh_name from banghyang_se where ns_name = '"
                    + nsName + "' group by bh_code order by bh_code desc ";
        }

        List<String> list = new ArrayList<String>();
        try {
            Cursor cur = sqliteDatabase.rawQuery(sql, null);
            while (cur.moveToNext()) {

                list.add(cur.getString(1));
            }

        } catch (SQLException se) {
            Log.e("selectData()Error! : ", se.toString());
        }

        return list;
    }

    /**
     * 노선코드 조회
     *
     * @param nsName
     * @return
     */
    public String getNoseonCode(String nsName) {
        // TODO Auto-generated method stub
        String sql = "select ns_code from banghyang_se where ns_name = '" + nsName
                + "'; ";
        String nsCode = "";
        //Log.e("selectData(): ", sql);

        // System.out.println("============================ sql : " + sql);

        try {
            Cursor cur = sqliteDatabase.rawQuery(sql, null);
            cur.moveToFirst();
            if (cur.getCount() > 0) {
                nsCode = cur.getString(0);
            }
        } catch (SQLException se) {
            // TODO: handle exception
            Log.e("selectData()Error! : ", se.toString());
        }

        return nsCode;
    }


    /**
     * 노선의 최대 이정값 구하기
     *
     * @param nsCode
     * @return
     */
    public String maxIjung(String nsCode) {
        String maxIjung = "";
        String sql = "select max(cast( ijeong as double )) aa from nscode where nscode='" + nsCode + "'";
        Cursor cursor = sqliteDatabase.rawQuery(sql.toString(), null);

        cursor = sqliteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            maxIjung = cursor.getString(0);
        }
//		 Log.d("","execsql maxIjung: " + sql.toString());
        return maxIjung;
    }


    //tm 좌표 구하기
    public Cursor fetchTMXY2(String nscode, String ijeong) {
        StringBuffer sql = new StringBuffer();

        sql.append("   SELECT tm_x, tm_y													\n");
        sql.append("     FROM nscode               			       							\n");
        sql.append(" WHERE nscode='" + nscode + "'               						\n");
        sql.append(" AND CAST (ijeong as double) = CAST ('" + ijeong + "' as double)		            \n");

        Cursor cursor = sqliteDatabase.rawQuery(sql.toString(), null);

//		Log.d("","execsql fetchGPSXY: " + sql.toString());
        return cursor;
    }


    /**
     * 노선명 조회
     *
     * @param nsCode
     * @return
     */
    public String getNoseonName(String nsCode) {
        // TODO Auto-generated method stub
        String sql = "select ns_name from banghyang_se where ns_code = '" + nsCode
                + "'; ";
        String nsName = "";
        //Log.e("selectData(): ", sql);

        // System.out.println("============================ sql : " + sql);

        try {
            Cursor cur = sqliteDatabase.rawQuery(sql, null);
            cur.moveToFirst();
            if (cur.getCount() > 0) {
                nsName = cur.getString(0);
            }
        } catch (SQLException se) {
            // TODO: handle exception
            Log.e("selectData()Error! : ", se.toString());
        }

        return nsName;
    }

    //터널내 N초후 다음좌표값 가져오기.
    public String[] getNextLocGps(String nsCode, String ijeong, String bhCode) {
        String[] arr = null;
        if (!"".equals(Common.nullCheck(nsCode))) {
            double nextIjeong;
            if ("E".equals(bhCode)) {
                nextIjeong = Common.nullCheckDouble(ijeong) + 0.1;//이정 + 100미터 S E에 따라 다름
            } else {
                nextIjeong = Common.nullCheckDouble(ijeong) - 0.1;//이정 + 100미터 S E에 따라 다름
            }
            StringBuffer sql = new StringBuffer();

            sql.append(" SELECT kyungdo, wido                                        \n");
            sql.append(" FROM nscode                                                 \n");
            sql.append(" WHERE nscode ='" + nsCode + "'                                  \n");
            sql.append(" AND CAST(ijeong AS DOUBLE) = " + nextIjeong + "        		 \n");

            Cursor cursor = sqliteDatabase.rawQuery(sql.toString(), null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    arr[0] = cursor.getString(0);
                    arr[1] = cursor.getString(1);
                }
                return arr;
            }
        }
        return null;
    }

    /**
     * 방향코드 조회
     *
     * @param nsCode
     * @param bhName
     * @return
     */
    public String getBanghyangCode(String nsCode, String bhName) {
        // TODO Auto-generated method stub
        String sql = "select bh_code from banghyang_se where ns_code = '" + nsCode
                + "' and bh_name = '" + bhName + "'; ";
        String bhCode = "";
        // Log.e("selectData(): ", sql);

        // System.out.println("============================ sql : " + sql);

        try {
            Cursor cur = sqliteDatabase.rawQuery(sql, null);
            cur.moveToFirst();
            if (cur.getCount() > 0) {
                bhCode = cur.getString(0);
            }
        } catch (SQLException se) {
            // TODO: handle exception
            Log.e("selectData()Error! : ", se.toString());
        }

        return bhCode;
    }


    /**
     * bsinfo 테이블 정보 조회
     *
     * @return 목록 리턴
     */
    public Cursor fetchBBBsCode() {
        String rtnStr = "";
        StringBuffer sql = new StringBuffer();

        sql.append("	SELECT bbcode, bbname, bscode, bsname, useyn from BSINFO      \n");

        Cursor cursor = sqliteDatabase.rawQuery(sql.toString(), null);


        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            if (i == 0) {
                rtnStr += cursor.getString(0);
            } else {
                rtnStr += "|" + cursor.getString(0);
            }
        }
        Log.d("process", "query fetchBBBsCode? !" + rtnStr);

        return cursor;
    }

    /**
     * bsinfo 테이블 정보 조회
     *
     * @return 목록 리턴
     */
    public String fetchBBBsCodeSelected() {
        String rtnStr = "";
        StringBuffer sql = new StringBuffer();

        sql.append("	SELECT bscode from BSINFO where useyn='Y'     \n");


        Cursor cursor = sqliteDatabase.rawQuery(sql.toString(), null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            if (i == 0) {
                rtnStr += cursor.getString(0);
            } else {
                rtnStr += "|" + cursor.getString(0);
            }
        }
        Log.d("process", "query fetchBBBsCode !" + rtnStr);

        return rtnStr;
    }

    /**
     * 부서코드 useyn update
     *
     * @param date
     * @return
     */
    public String updateBBBsCode(String bscode, String useyn) {
        sqliteDatabase.beginTransaction();

        String sql = "UPDATE BSINFO SET useyn = '" + useyn + "' where bscode='" + bscode + "'";

        try {
            sqliteDatabase.execSQL(sql);
            Log.d("", "setdataMngUpdate sql = " + sql);
        } catch (SQLException e) {
            Log.e("selectData()Error! : ", e.toString());
        } finally {
            sqliteDatabase.setTransactionSuccessful();
            sqliteDatabase.endTransaction();
        }
        return null;
    }

    /**
     * 접보지사 선택 코드 초기화
     *
     * @return
     */
    public String updateBBBsCodeClean() {
        sqliteDatabase.beginTransaction();

        String sql = "UPDATE BSINFO SET useyn = 'N' where 1=1 ";

        try {
            sqliteDatabase.execSQL(sql);
            Log.d("", "setdataMngUpdate sql = " + sql);
        } catch (SQLException e) {
            Log.e("selectData()Error! : ", e.toString());
        } finally {
            sqliteDatabase.setTransactionSuccessful();
            sqliteDatabase.endTransaction();
        }
        return null;
    }

    /**
     * 부서코드 테이블 초기화
     *
     * @param bscode
     * @param useyn
     * @return
     */
    public String updateBBBsCodeInit() {
        sqliteDatabase.beginTransaction();

        String sql = "UPDATE BSINFO SET useyn = 'N' where 1=1";

        try {
            sqliteDatabase.execSQL(sql);
            Log.d("", "setdataMngUpdate sql = " + sql);
        } catch (SQLException e) {
            Log.e("selectData()Error! : ", e.toString());
        } finally {
            sqliteDatabase.setTransactionSuccessful();
            sqliteDatabase.endTransaction();
        }
        return null;
    }

    /**
     * 사용자가 상세 데이타 확인한 값 저장.(알람용)
     *
     * @param rpt_id
     * @param reg_date
     */
    public void insertRptId(String rpt_id, String reg_date) {
        sqliteDatabase.beginTransaction();

        String sql = "INSERT INTO RPTID (rpt_id, reg_date) values('" + rpt_id + "','" + reg_date + "')";

        try {
            sqliteDatabase.execSQL(sql);
            Log.d("", "setdataMngUpdate sql = " + sql);
        } catch (SQLException e) {
            Log.e("selectData()Error! : ", e.toString());
        } finally {
            sqliteDatabase.setTransactionSuccessful();
            sqliteDatabase.endTransaction();
        }
    }


    /**
     * 사용자가 확인한 데이타 목록에 있는지 확인 (알람용)
     *
     * @param rpt_id
     * @return true=데이타 있음, false=데이타 없음
     */
    public boolean selectRptId(String rpt_id) {

        String rtnStr = "";
        StringBuffer sql = new StringBuffer();

        sql.append("	SELECT rpt_id               \n");
        sql.append("	FROM RPTID 					\n");
        sql.append("	WHERE rpt_id='" + rpt_id + "'	\n");


        Cursor cursor = sqliteDatabase.rawQuery(sql.toString(), null);
        Log.d("process", "query selectRptId !");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Log.d("", TAG + " isplay uhuh selectRptId = " + cursor.getString(0));
                return true;
            }
        }

        return false;
    }


    /**
     * 일주일 전의 데이타 삭제
     *
     * @param ymdhm 년 월 일 시 분
     */
    public void deleteRptId() {
        String rtnStr = "";
        StringBuffer sql = new StringBuffer();

        sql.append("delete from rptid WHERE DATE(STRFTIME('%Y-%m-%d', SUBSTR('now', 0, 11))) > DATE(reg_date, '+7 day')");

        sqliteDatabase.execSQL(sql.toString());
        Log.d("process", "query selectRptId !");

    }


    public Cursor selectICandRest(double latitude, double longitude) {
        sqliteDatabase.beginTransaction();
        String rtnStr = "";
        StringBuffer sql = new StringBuffer();

        sql.append("	SELECT code,                                                      	\n");
        sql.append("	name,                                                             	\n");
        sql.append("	nscode,                                                             	\n");
        sql.append("	xgap, gubun                                                             	\n");
        sql.append("	FROM(                                                             	\n");
        sql.append("	SELECT code,                                                      	\n");
        sql.append("	ABS (longitude -" + Math.abs(longitude) + " + latitude -" + Math.abs(latitude) + ") as xgap,	\n");
        sql.append("	name,                                                           	\n");
        sql.append("	nscode,                                                             	\n");
        sql.append("	gubun                                                             	\n");
        sql.append("	FROM sisul                                                        	\n");
        sql.append("  WHERE     1 = 1                                   					\n");
        sql.append("        AND longitude + 0.3000 > " + longitude + "						\n");
        sql.append("        AND longitude - 0.3000 < " + longitude + "						\n");
        sql.append("        AND latitude + 0.3000 >  " + latitude + "							\n");
        sql.append("        AND latitude - 0.3000 < " + latitude + "							\n");
        sql.append("       ) WHERE 1=1    																\n");
        sql.append("  ORDER BY gubun DESC, xgap DESC                      					\n");
        Log.d(TAG, TAG + "selectICandRest sql= " + sql.toString());
        Cursor cursor = null;
        try {
            cursor = sqliteDatabase.rawQuery(sql.toString(), null);
        } catch (SQLException e) {
            /*e.printStackTrace();*/
            Log.e("에러", "예외");
        } finally {
            sqliteDatabase.setTransactionSuccessful();
            sqliteDatabase.endTransaction();
        }
        return cursor;
    }


    /**
     * DB 닫기
     *
     * @return
     */
    public void close() {
        if (sqliteDatabase != null) {
            sqliteDatabase.close();
        }
    }


}
