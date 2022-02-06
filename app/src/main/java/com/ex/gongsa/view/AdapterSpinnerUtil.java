package com.ex.gongsa.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ex.gongsa.Common;
import com.ex.situationmanager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.ex.gongsa.Common.nullCheck;
import static com.ex.gongsa.Configuration.SERVER_URL;
import static com.ex.gongsa.view.WorkPlanRegisterActivity.codeMap;

public class AdapterSpinnerUtil extends ArrayAdapter<String> implements AdapterView.OnItemSelectedListener {

    private Context context;

    private Spinner spinner;



    private int resources;


    private Map<String, Object[]> viewCollection;

    private JSONArray jsonArray;
    private JSONObject jsonObject;

    static Integer maxCharo = null;
    private Map<String, String> jisaMap;
    private Map<String, String> nosunMap;
    private Map<String, String> workTypeMap;
    private List<Map<String, String>> workTypeMapList;


    private Map<String, Map<String, String>> itemKeyValueMap;
    private String jsonJisaResult;
    //2021.07 작업유형(대분류)
    private String workTypeJsonResult;
    private List<String> workTypeList; //작업유형 리스트
    Intent intent = null;

    Map<String, String> cityMap = new HashMap<>();
    private BaseActivity baseActivity = new BaseActivity() {
        @Override
        public String onActionPost(String primitive, String date) {
            return null;
        }
    };


    public AdapterSpinnerUtil(final Context context, int resources, Spinner spinner, List<String> list/*, Map<String, String> codeMap*/) {
        super(context, resources, list);
        this.context = context;
        this.resources = resources;
        this.spinner = spinner;
        spinner.clearFocus();
        spinner.setOnItemSelectedListener(this);
        // spinner.setOnItemClickListener(AdapterSpinnerUtil.class);
        /*this.codeMap = codeMap;*/
        System.out.println("------------------------------- 순서 5 -----------------------------------------");
        this.resources = resources;
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(this);
        setDropDownViewResource(resources);
        Log.i("실시간 맵 체크", codeMap.toString());
        //--
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }//onItemSelected

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }//onNothingSelected
        });//setOnItemSelectedListener
        //spinner.setOnItemSelectedListener(this);


    }//생성자

       /*     Constructor
            Spinner 클릭 이벤트에 따른 ajax 동작 구현을 위한 Constructor*/

    public AdapterSpinnerUtil(final Context context, int resources, Spinner spinner, List<String> list,/*  Map<String, String> codeMap,*/ Map<String, Map<String, String>> itemKeyValueMap, Map<String, Object[]> viewCollection) {
        super(context, resources, list);
        this.spinner = spinner;
        /*this.codeMap = codeMap;*/
        this.viewCollection = viewCollection;
        this.context = context;
        Log.i("itemKeyVlaueMap", itemKeyValueMap.toString());//모든 기값을 가지고있는 Map
        //viewCollection /// 각각의 메뉴에 대한 view들의 모음
        Log.i("viewCollection", viewCollection.toString());
        this.itemKeyValueMap = itemKeyValueMap;
        spinner.setOnItemSelectedListener(this);
        setDropDownViewResource(resources);
        jisaMap = new HashMap<String, String>();
        nosunMap = new HashMap<String, String>();


        /*workTypeMapList = new ArrayList<Map<String, String>>();
        workTypeList = new ArrayList<>();*/
        System.out.println("--------------------workTypeList ---------------------------  " +    list     );

        //   spinner.setSelection(0);
    }//생성자

    int bonbuPosition = -1;


    @Override
    @SuppressWarnings("unchecked")
    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

        switch (parent.getId()) {
            case R.id.bonbuSpinner:
                try {
                    parent.setSelection(position);
                    jsonJisaResult = nullCheck(baseActivity.new Action("get", SERVER_URL + "/WorkPlan/jisaItem.do", itemKeyValueMap.get("bonbuList").get(parent.getSelectedItem().toString()), context).execute().get());
                    Log.i("JsonJisaResult", jsonJisaResult);//MTNOF_CD -- 지사 코드, MTNOF_NM -- 지사 이름
                    //  codeMap.put("HDQR_CD",itemKeyValueMap.get("bonbuList").get(parent.getSelectedItem()));
                    //지사에대한 제이슨 파싱
                    codeMap.put("HDQR_NM",parent.getSelectedItem().toString());
                    jsonArray = new JSONArray(jsonJisaResult);
                    ((TextView) (viewCollection.get("jisaView")[1])).clearComposingText();
                    ((List<String>) (viewCollection.get("jisaView")[2])).clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        ((List<String>) (viewCollection.get("jisaView")[2])).add(jsonObject.get("MTNOF_NM").toString());
                        jisaMap.put(jsonObject.get("MTNOF_NM").toString(), jsonObject.get("MTNOF_CD").toString());
                        jisaMap.put(jsonObject.get("MTNOF_NM").toString(), jsonObject.get("MTNOF_CD").toString());
                        itemKeyValueMap.put("jisaList", jisaMap);

                    }
                    ((Spinner) (viewCollection.get("jisaView")[0])).setSelection(position);
                    ((TextView) (viewCollection.get("jisaView")[1])).setText(((Spinner) (viewCollection.get("jisaView")[0])).getSelectedItem().toString());
                } catch (JSONException  e) {
                   Log.e("에러","익셉션발생");
                }  catch ( InterruptedException  e) {
                    Log.e("에러","익셉션발생");
                }  catch ( ExecutionException e) {
                    Log.e("에러","익셉션발생");
                }   catch ( Exception e) {
                    Log.e("에러","익셉션발생");
                }finally {

                   ((Spinner) viewCollection.get("bonbuView")[0]).setSelection(position);
                    ((TextView) viewCollection.get("bonbuView")[1]).clearComposingText();
                    ((TextView) viewCollection.get("bonbuView")[1]).setText(((Spinner) viewCollection.get("bonbuView")[0]).getSelectedItem().toString());//본부의 텍스트뷰에 대해서 선택된 값이 셋팅되는 작업
                    WorkPlanRegisterActivity.codeMap.put("HDQR_CD", itemKeyValueMap.get("bonbuList").get(((TextView) viewCollection.get("bonbuView")[1]).getText()));
                    Log.e("ddddddddddddd", WorkPlanRegisterActivity.codeMap.toString());
                }

            case R.id.jisaSpinner:
                try {

                    if (position < ((List<String>) viewCollection.get("jisaView")[2]).size()) {
                        ((Spinner) viewCollection.get("jisaView")[0]).setSelection(position);
                        ((TextView) viewCollection.get("jisaView")[1]).clearComposingText();

                        ((TextView) viewCollection.get("jisaView")[1]).setText(((Spinner) viewCollection.get("jisaView")[0]).getSelectedItem().toString());//본부의 텍스트뷰에 대해서 선택된 값이 셋팅되는 작업
                    } else {
                        ((Spinner) viewCollection.get("jisaView")[0]).setSelection(0);
                    }
                    jsonObject = new JSONObject();
                    jsonObject.accumulate("HDQR_CD", itemKeyValueMap.get("bonbuList").get(((TextView) viewCollection.get("bonbuView")[1]).getText()));
                    // Log.i("dddd",(itemKeyValueMap.get("jisaList").get(((TextView)viewCollection.get("jisaView")[1]).getText())));
                    jsonObject.accumulate("MTNOF_CD", (itemKeyValueMap.get("jisaList").get(((TextView) viewCollection.get("jisaView")[1]).getText())));
                    jsonObject.accumulate("MTNOF_NM", ((TextView) viewCollection.get("jisaView")[1]).getText());
                    String JisaJsonresult = nullCheck(baseActivity.new Action("get", SERVER_URL + "/WorkPlan/nosunItem.do", jsonObject.toString(), context).execute().get());


                    jsonArray = new JSONArray(JisaJsonresult);
                    ((List<String>) viewCollection.get("nosunView")[2]).clear();
                    ((TextView) viewCollection.get("nosunView")[1]).clearComposingText();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        Log.i("JisaJsonresult", jsonObject.get("USE_ROTNM").toString());
                        ((List<String>) viewCollection.get("nosunView")[2]).add(jsonObject.get("USE_ROTNM").toString());
                        nosunMap.put(jsonObject.get("USE_ROTNM").toString(), jsonObject.get("ROUTE_CD").toString());
                        itemKeyValueMap.put("nosunList", nosunMap);
                    }

                    try{
                        ((Spinner) viewCollection.get("nosunView")[0]).setSelection(0);


                        ((TextView) viewCollection.get("nosunView")[1]).setText(((List<String>) viewCollection.get("nosunView")[2]).get(0));
                        Log.i("itemKeyVal123ueMap", itemKeyValueMap.toString());
                        Log.i("itemKeyVal123123ueMap", ((Spinner) viewCollection.get("nosunView")[0]).getSelectedItem().toString());
                    }catch (ArrayIndexOutOfBoundsException e){
                       Log.e("에러","익셉션발생");
                        ((TextView) viewCollection.get("nosunView")[1]).clearComposingText();
                    }catch ( NullPointerException  e){
                        Log.e("에러","익셉션발생");
                        ((TextView) viewCollection.get("nosunView")[1]).clearComposingText();
                    }catch ( StringIndexOutOfBoundsException e){
                        Log.e("에러","익셉션발생");
                        ((TextView) viewCollection.get("nosunView")[1]).clearComposingText();
                    }catch (Exception e){
                        Log.e("에러","익셉션발생");
                        ((TextView) viewCollection.get("nosunView")[1]).clearComposingText();
                    }

                    WorkPlanRegisterActivity.codeMap.put("MTNOF_CD", itemKeyValueMap.get("jisaList").get(((Spinner) viewCollection.get("jisaView")[0]).getSelectedItem().toString()));
                    WorkPlanRegisterActivity.codeMap.put("MTNOF_NM", ((TextView) viewCollection.get("jisaView")[1]).getText().toString());
                } catch ( ExecutionException e) {
                   Log.e("에러","익셉션발생");
                }catch (ArrayIndexOutOfBoundsException  e) {
                    Log.e("에러","익셉션발생");
                }catch ( NullPointerException  e) {
                    Log.e("에러","익셉션발생");
                }catch (StringIndexOutOfBoundsException   e) {
                    Log.e("에러","익셉션발생");
                }catch ( JSONException e) {
                    Log.e("에러","익셉션발생");
                }catch ( InterruptedException  e) {
                    Log.e("에러","익셉션발생");
                }catch (Exception e) {
                    Log.e("에러","익셉션발생");
                }

            case R.id.nosunSpinner:
                //Toast.makeText(context,parent.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                if (position < ((List<String>) viewCollection.get("nosunView")[2]).size()) {
                    ((Spinner) viewCollection.get("nosunView")[0]).setSelection(position);
                    ((TextView) viewCollection.get("nosunView")[1]).clearComposingText();
                    ((TextView) viewCollection.get("nosunView")[1]).setText(((Spinner) viewCollection.get("nosunView")[0]).getSelectedItem().toString());
                    codeMap.put("ROUTE_CD", itemKeyValueMap.get("nosunList").get(((TextView) viewCollection.get("nosunView")[1]).getText().toString()));
                } else {
                    ((Spinner) viewCollection.get("nosunView")[0]).setSelection(0);
                    codeMap.put("ROUTE_CD", itemKeyValueMap.get("nosunList").get(((TextView) viewCollection.get("nosunView")[1]).getText().toString()));
                }
                try {
                    WorkPlanRegisterActivity.codeMap.put("ROUTE_CD", itemKeyValueMap.get("nosunList").get(((((TextView) viewCollection.get("nosunView")[1]).getText()))));
                    //String charoJsonResult = nullCheck(baseActivity.new Action("get", SERVER_URL + "/WorkPlan/charo.do", itemKeyValueMap.get("nosunList").get(((((TextView) viewCollection.get("nosunView")[1]).getText()))), context).execute("").get());
                    //Log.i("Json MaxCharo", charoJsonResult);
                 /*   if (Integer.parseInt(charoJsonResult) <= 6) {

                        ((TextView) viewCollection.get("charo")[1]).clearComposingText();
                        WorkPlanRegisterActivity.codeMap.put("MTNOF_CD", itemKeyValueMap.get("jisaList").get(((Spinner) viewCollection.get("jisaView")[0]).getSelectedItem().toString()));
                        ((TextView) viewCollection.get("charo")[1]).setText("총 " + maxCharo + "차로 중 0차로 차단 ");
                        WorkPlanRegisterActivity.codeMap.put("TOT_CRGW_CNT", Integer.toString(maxCharo));
                    } else {
                        maxCharo = 6;
                        ((TextView) viewCollection.get("charo")[1]).clearComposingText();
                        WorkPlanRegisterActivity.codeMap.put("MTNOF_CD", itemKeyValueMap.get("jisaList").get(((Spinner) viewCollection.get("jisaView")[0]).getSelectedItem().toString()));

                    }*/
                    maxCharo =0;
                //    ((TextView) viewCollection.get("charo")[1]).setText("총 " + 0 + "차로 중 0차로 차단 ");
                  //  WorkPlanRegisterActivity.codeMap.put("TOT_CRGW_CNT", Integer.toString(0));
                   // Log.i("charoJsonResult", charoJsonResult);

                   try{
                       ((EditText) viewCollection.get("gugan")[0]).clearComposingText();
                       ((EditText) viewCollection.get("gugan")[1]).clearComposingText();
                   }catch (ArrayIndexOutOfBoundsException  e){
                      Log.e("에러","익셉션발생");
                   }catch (  StringIndexOutOfBoundsException e){
                       Log.e("에러","익셉션발생");
                   }catch ( NullPointerException e){
                       Log.e("에러","익셉션발생");
                   }catch (Exception e){
                       Log.e("에러","익셉션발생");
                   }
                    Log.e("이거 뭔데 ", codeMap.toString());


                    jsonObject = new JSONObject(codeMap);
                    //   Log.i("dnnnnnnnndd", jsonObject.toString());
                    String guganMinMaxValue = URLDecoder.decode(Common.nullCheck(baseActivity.new Action("get", SERVER_URL + "/WorkPlan/guganIjung.do", jsonObject.toString(), context).execute().get()), "UTF-8");
                    Log.v("guganMinMaxValue", guganMinMaxValue);

                    jsonObject = new JSONObject(guganMinMaxValue);
                    ((EditText) viewCollection.get("gugan")[0]).setText(jsonObject.get("minMtnofStpntDstnc").toString());
                    ((EditText) viewCollection.get("gugan")[1]).setText(jsonObject.get("maxMtnofEdpntDstnc").toString());

                    WorkPlanRegisterActivity.startLimitGugan = Common.doubleNullCheck(Double.parseDouble(jsonObject.get("minMtnofStpntDstnc").toString()));
                    WorkPlanRegisterActivity.endLimitGugan = Common.doubleNullCheck(Double.parseDouble(jsonObject.get("maxMtnofEdpntDstnc").toString()));
                    WorkPlanRegisterActivity.startLimitEtValue = Common.doubleNullCheck(Double.parseDouble(((EditText) viewCollection.get("gugan")[0]).getText().toString()));
                    WorkPlanRegisterActivity.endLimitEtValue = Common.doubleNullCheck(Double.parseDouble(((EditText) viewCollection.get("gugan")[1]).getText().toString()));

                } catch (ArrayIndexOutOfBoundsException e) {
                   Log.e("에러","익셉션발생");
                }catch ( NullPointerException  e) {
                    Log.e("에러","익셉션발생");
                }catch ( StringIndexOutOfBoundsException  e) {
                    Log.e("에러","익셉션발생");
                }catch ( JSONException e) {
                    Log.e("에러","익셉션발생");
                }catch (InterruptedException  e) {
                    Log.e("에러","익셉션발생");
                }catch ( ExecutionException  e) {
                    Log.e("에러","익셉션발생");
                }catch (UnsupportedEncodingException e) {
                    Log.e("에러","익셉션발생");
                }catch (Exception e) {
                    Log.e("에러","익셉션발생");
                }
             //이상무
            case R.id.direnctionSpinner:
                //   ((Spinner) viewCollection.get("direction")[0]).setSelection(position);
                /*codeMap.put("bonbuCd",codeMap.get("HDQR_CD"));
                codeMap.put("mtnofCd",codeMap.get("MTNOF_CD"));*/
                //Log.e("다이렉션 확인",itemKeyValueMap.get("directCity").toString());

                try{
                    ((List<String>) viewCollection.get("direction")[2]).clear();
                }catch (ArrayIndexOutOfBoundsException   e){
                    Log.e("에러","익셉션발생");
                }catch (  StringIndexOutOfBoundsException e){
                    Log.e("에러","익셉션발생");
                }catch ( NullPointerException  e){
                    Log.e("에러","익셉션발생");
                }catch (Exception e){
                    Log.e("에러","익셉션발생");
                }
                //jsonObject.put(codeMap);
                try {

                    jsonObject = new JSONObject(codeMap);
                    // jsonObject.accumulate("HDQR_CD", itemKeyValueMap.get("bonbuList").get(((TextView) viewCollection.get("bonbuView")[1]).getText()));
                    // jsonObject.accumulate("MTNOF_CD", (itemKeyValueMap.get("jisaList").get(((TextView) viewCollection.get("jisaView")[1]).getText())));
                    jsonObject.accumulate("MTNOF_NM", ((TextView) viewCollection.get("jisaView")[1]).getText());
                    //  jsonObject.accumulate("ROUTE_CD", codeMap.get("ROUTE_CD"));
                    Log.i("파람 확인",jsonObject.toString());

                    String directionCity = URLDecoder.decode(Common.nullCheck(baseActivity.new Action("get", SERVER_URL + "/WorkPlan/getDirectionCityNm.do", jsonObject.toString(), context).execute().get()), "UTF-8");
                    Log.i("dnnnnnnnndd", directionCity);



                    jsonArray = new JSONArray(directionCity);
                    try{
                        ((List<String>) viewCollection.get("direction")[2]).clear();

                    }catch (ArrayIndexOutOfBoundsException    e){
                        Log.e("에러","익셉션");
                    }catch ( NullPointerException   e){
                        Log.e("에러","익셉션");
                    }catch (  StringIndexOutOfBoundsException  e){
                        Log.e("에러","익셉션");
                    }catch (Exception e){
                        Log.e("에러","익셉션");
                    }
                    ((TextView)viewCollection.get("direction")[1]).clearComposingText();
                    Log.i("방향 결과123",jsonArray.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Log.i("루프 확인", "----------------------");
                        jsonObject = jsonArray.getJSONObject(i);


                        nosunMap.put("directionCityNm", jsonObject.get("directionCityNm").toString());
                        nosunMap.put("directionCityCd",jsonObject.get("directionCityCd").toString());


                        cityMap.put(jsonObject.get("directionCityNm").toString(),  jsonObject.get("directionCityCd").toString());


                        ((List<String>) viewCollection.get("direction")[2]).add(  jsonObject.get("directionCityNm").toString());
                    }

                    try{

                        ((TextView)viewCollection.get("direction")[1]).clearComposingText();
                        if(position <( (List<String>)viewCollection.get("direction")[2]).size()){
                            Log.i("여기 확인","if");
                            ( (Spinner)viewCollection.get("direction")[0]).setSelection(position);
                            /*       ((TextView)viewCollection.get("direction")[1]).clearComposingText();*/
                            /*  ((TextView)viewCollection.get("direction")[1]).setText(  ( (Spinner)viewCollection.get("direction")[0]).getSelectedItem().toString());*/
                            ((TextView)viewCollection.get("direction")[1]).setText(  ( (Spinner)viewCollection.get("direction")[0]).getSelectedItem().toString());

                        }else{


                            //  ((TextView)viewCollection.get("direction")[1]).setText("sdf");
                            try{
                                if(  (( (List<String>)viewCollection.get("direction")[2]).size()!=0)){
                                    ( (Spinner)viewCollection.get("direction")[0]).setSelection(0);
                                    ((TextView)viewCollection.get("direction")[1]).setText(  (( (List<String>)viewCollection.get("direction")[2]).get( ( (Spinner)viewCollection.get("direction")[0]).getSelectedItemPosition())));
                                }else{
                                    Log.i("여기 확인","ㅇㅇ");
                                    ((TextView)viewCollection.get("direction")[1]).setText("");
                                }

                            }catch (ArrayIndexOutOfBoundsException e){
                               Log.e("에러","익셉션발생");
                            }catch ( NullPointerException  e){
                                Log.e("에러","익셉션발생");
                            }catch (StringIndexOutOfBoundsException e){
                                Log.e("에러","익셉션발생");
                            }catch (Exception e){
                                Log.e("에러","익셉션발생");
                            }


                        }
                        codeMap.put("ROUTE_DRCT_CD",itemKeyValueMap.get("directCity").get( ((TextView)viewCollection.get("direction")[1]).getText().toString()));
                        //   codeMap.put("ROUTE_DRCT_CD",jsonArray.getJSONObject(( (Spinner)viewCollection.get("direction")[0]).getSelectedItemPosition()).get("directionCityCd").toString());
                        //               ((TextView) viewCollection.get("direction")[1]).clearComposingText();
                        //             ((TextView) viewCollection.get("direction")[1]).setText(((Spinner) viewCollection.get("direction")[0]).getSelectedItem().toString());
                    }catch (ArrayIndexOutOfBoundsException   e){
                        Log.e("에러","익셉션발생");
//                    ((TextView)viewCollection.get("direction")[1]).clearComposingText();
                    }catch ( NullPointerException   e){
                        Log.e("에러","익셉션발생");
//                    ((TextView)viewCollection.get("direction")[1]).clearComposingText();
                    }catch ( StringIndexOutOfBoundsException  e){
                        Log.e("에러","익셉션발생");
//                    ((TextView)viewCollection.get("direction")[1]).clearComposingText();
                    }catch (Exception  e){
                        Log.e("에러","익셉션발생");
//                    ((TextView)viewCollection.get("direction")[1]).clearComposingText();
                    }

                    Log.i("방향까지 체크된 코드맵 ",codeMap.toString());
                    itemKeyValueMap.put("directCity",cityMap);
                }catch (ArrayIndexOutOfBoundsException | NullPointerException | StringIndexOutOfBoundsException | JSONException | InterruptedException | ExecutionException | UnsupportedEncodingException e) {
                   Log.e("에러","익셉션발생");
                }

                if(parent.getId()==R.id.nosunSpinner || parent.getId()==R.id.direnctionSpinner){
                    break;
                }

            case R.id.gamdokSpinner:
                try {

                    //감독
                    jsonObject = new JSONObject();
                    jsonObject.accumulate("HDQR_CD", codeMap.get("HDQR_CD"));
                    jsonObject.accumulate("MTNOF_CD", (itemKeyValueMap.get("jisaList").get(((TextView) viewCollection.get("jisaView")[1]).getText())));
                    jsonObject.accumulate("MTNOF_NM", ((TextView) viewCollection.get("jisaView")[1]).getText());
                    jisaMap.put(jsonObject.get("MTNOF_NM").toString(), jsonObject.get("MTNOF_CD").toString());

                    String gamddkJsonResult = nullCheck(baseActivity.new Action("get", SERVER_URL + "/WorkPlan/gamdokwon.do", jsonObject.toString(), context).execute().get());
                    WorkPlanRegisterActivity.gamdokArrayItem=gamddkJsonResult;
                    Log.println(Log.ASSERT,"감독원",gamddkJsonResult);
                    Log.i("gamdkJsonResult", gamddkJsonResult);

                    try{
                        ((List<String>) viewCollection.get("gamdokwon")[2]).clear();

                        jsonArray = new JSONArray(URLDecoder.decode(gamddkJsonResult,"UTF-8"));
                        try{
                            ((TextView) viewCollection.get("gamdokwon")[1]).setText(jsonArray.getJSONObject(0).get("EMNM").toString());//USER_EMNO

                            try{
                                codeMap.put("MTNOF_PRCH_EMNO", jsonArray.getJSONObject(0).get("USER_EMNO").toString());
                                String teno="";
                                String[] tenoArray=null;
                                String reusltTelNo="";
                                try{
                                    tenoArray =    jsonArray.getJSONObject(0).get("TEL_NO").toString().split("-");
                                    reusltTelNo=tenoArray[0]+tenoArray[1]+tenoArray[2];
                                }catch (ArrayIndexOutOfBoundsException e){
                                    reusltTelNo=jsonArray.getJSONObject(0).get("TEL_NO").toString();
                                }
                                codeMap.put("MTNOF_PRCH_EMNO_TELNO",reusltTelNo);
                            }catch (ArrayIndexOutOfBoundsException | NullPointerException | StringIndexOutOfBoundsException | JSONException  e){
                                codeMap.put("MTNOF_PRCH_EMNO","\n");
                                String teno="";
                                String[] tenoArray=null;
                                String reusltTelNo="";
                                try{
                                    tenoArray =    jsonArray.getJSONObject(0).get("TEL_NO").toString().split("-");
                                    reusltTelNo=tenoArray[0]+tenoArray[1]+tenoArray[2];
                                }catch (ArrayIndexOutOfBoundsException ee){
                                    Log.e("에러","Exception");
                                    reusltTelNo=jsonArray.getJSONObject(0).get("TEL_NO").toString();
                                }
                                codeMap.put("MTNOF_PRCH_EMNO_TELNO",reusltTelNo);
                            }
                          //  codeMap.put("MTNOF_PRCH_EMNO", jsonArray.getJSONObject( ((Spinner) viewCollection.get("gamdokwon")[0]).getSelectedItemPosition()).get("USER_EMNO").toString());
                        }catch (ArrayIndexOutOfBoundsException e){
                            ((TextView) viewCollection.get("gamdokwon")[1]).setText("조회된 감독원이 없습니다.");
                        }catch ( NullPointerException  e){
                            Log.e("에러","익셉션발생2");
                            ((TextView) viewCollection.get("gamdokwon")[1]).setText("조회된 감독원이 없습니다.");
                        }catch ( StringIndexOutOfBoundsException e){
                            Log.e("에러","익셉션발생3");
                            ((TextView) viewCollection.get("gamdokwon")[1]).setText("조회된 감독원이 없습니다.");
                        }catch ( JSONException e){
                            Log.e("에러","익셉션발생4");
                            ((TextView) viewCollection.get("gamdokwon")[1]).setText("조회된 감독원이 없습니다.");
                        }catch (Exception e){
                            Log.e("에러","익셉션발생5");
                            ((TextView) viewCollection.get("gamdokwon")[1]).setText("조회된 감독원이 없습니다.");
                        }

                    }catch (ArrayIndexOutOfBoundsException  e){
                       Log.e("에러","익셉션발생ㅁ");
                        ((TextView) viewCollection.get("gamdokwon")[1]).setText("");
                    }catch (  NullPointerException  e){
//                        ((List<String>) viewCollection.get("gamdokwon")[2]).add("");
                        //e.printStackTrace();
                        Log.e("에러","익셉션발생ㄴ");
                        ((TextView) viewCollection.get("gamdokwon")[1]).setText("");
                    }catch ( StringIndexOutOfBoundsException   e){
                        Log.e("에러","익셉션발생ㄷ");
                        ((TextView) viewCollection.get("gamdokwon")[1]).setText("");
                    }catch (JSONException   e){
                       // e.printStackTrace();
                        Log.e("에러","익셉션발생");
                        ((TextView) viewCollection.get("gamdokwon")[1]).setText("");
                    }catch ( UnsupportedEncodingException e){
                        Log.e("에러","익ㄷ셉션발생");
                        ((TextView) viewCollection.get("gamdokwon")[1]).setText("");
                    }catch ( Exception e){
                        Log.e("에러","익셉션발ㄹ생");
                        ((TextView) viewCollection.get("gamdokwon")[1]).setText("");
                    }



                    try{
                        //((TextView) viewCollection.get("gamdokwon")[1]).setText("끵!");
                        Log.i("ㅇㅇㄹㄴㅇㄹ","durl dho 안와");
                        Log.i("if 직전","402라인");
                        Log.println(Log.ASSERT,"각각의 감독원 이름","if 직전");

                  /*      if (position < ((List<String>) viewCollection.get("gamdokwon")[2]).size()) {
                            Log.println(Log.ASSERT,"각각의 감독원 이름","if 안");
                            Log.println(Log.ASSERT,"이크","공사관리"+ "else");
                            Log.println(Log.ASSERT,"이크","공사관리"+ "if");
                            ((Spinner) viewCollection.get("gamdokwon")[0]).setSelection(position);

                            //((TextView) viewCollection.get("gamdokwon")[1]).clearComposingText();
                            ((TextView) viewCollection.get("gamdokwon")[1]).setText(((Spinner) viewCollection.get("gamdokwon")[0]).getSelectedItem().toString());

                           // ((TextView) viewCollection.get("gamdokwon")[1]).setText(Common.nullCheck(((List<String>) viewCollection.get("gamdokwon")[2]).get(0)));

                        } else {
                            Log.println(Log.ASSERT,"이크","공사관리"+ "else");
                            if( ((List<String>) viewCollection.get("gamdokwon")[2]).size()!=0){
                                Log.println(Log.ASSERT,"이크","공사관리"+ "else");
                                ((Spinner) viewCollection.get("gamdokwon")[0]).setSelection(0);
                                //  ((TextView) viewCollection.get("gamdokwon")[1]).clearComposingText();

                                ((TextView) viewCollection.get("gamdokwon")[1]).setText(  ((Spinner) viewCollection.get("gamdokwon")[0]).getSelectedItem().toString());

                            }else{
                                Log.println(Log.ASSERT,"이크","공사관리"+ "else");
                                ((TextView) viewCollection.get("gamdokwon")[1]).clearComposingText();
                                ((List<String>) viewCollection.get("gamdokwon")[2]).clear();
                            }

                        }*/

                    /*    try{
                            ((TextView) viewCollection.get("gamdokwon")[1]).setText(Common.nullCheck(((Spinner) viewCollection.get("gamdokwon")[0]).getSelectedItem().toString()));
                        }catch (Exception e){
                           Log.e("에러","익셉션발생");
                            *//*((TextView) viewCollection.get("gamdokwon")[1]).setText("");*//*
                            ((TextView) viewCollection.get("gamdokwon")[1]).setText(((Spinner) viewCollection.get("gamdokwon")[0]).getSelectedItem().toString());
                        }*/
                    }catch (ArrayIndexOutOfBoundsException    e){
                      // Log.e("에러","익셉션발생");
                       Log.e("에러","익셉션발생b");
                    }catch ( NullPointerException   e){
                        // Log.e("에러","익셉션발생");
                        Log.e("에러","익셉션발생c");
                    }catch (  StringIndexOutOfBoundsException  e){
                        // Log.e("에러","익셉션발생");
                        Log.e("에러","익셉션발생d");
                    }catch (  Exception  e){
                        // Log.e("에러","익셉션발생");
                        Log.e("에러","익셉션발생f");
                    }
                    //감독원 종료



/*

                    ((EditText) viewCollection.get("gugan")[0]).clearComposingText();
                    ((EditText) viewCollection.get("gugan")[1]).clearComposingText();
                    Log.e("이거 뭔데 ", codeMap.toString());



                    jsonObject = new JSONObject(codeMap);
                    jsonObject.accumulate("HDQR_CD", itemKeyValueMap.get("bonbuList").get(((TextView) viewCollection.get("bonbuView")[1]).getText()));
                    jsonObject.accumulate("MTNOF_CD", (itemKeyValueMap.get("jisaList").get(((TextView) viewCollection.get("jisaView")[1]).getText())));
                    jsonObject.accumulate("MTNOF_NM", ((TextView) viewCollection.get("jisaView")[1]).getText());
                    jsonObject.accumulate("ROUTE_CD", codeMap.get("ROUTE_CD"));
                  //  jisaMap.put(jsonObject.get("MTNOF_NM").toString(), jsonObject.get("MTNOF_CD").toString());
                   // Log.i("dnnnnnnnndd", jsonObject.toString());
                    //     String guganMinMaxValue = URLDecoder.decode(Common.nullCheck(baseActivity.new Action("get", SERVER_URL+"/WorkPlan/guganIjung.do", jsonObject.toString(),context).execute().get()), "UTF-8");
                    Log.v("guganMinMaxValue", guganMinMaxValue);

                    jsonObject = new JSONObject(guganMinMaxValue);
                    ((EditText) viewCollection.get("gugan")[0]).setText(jsonObject.get("minMtnofStpntDstnc").toString());
                    ((EditText) viewCollection.get("gugan")[1]).setText(jsonObject.get("maxMtnofEdpntDstnc").toString());

                    WorkPlanRegisterActivity.startLimitGugan = Common.doubleNullCheck(Double.parseDouble(jsonObject.get("minMtnofStpntDstnc").toString()));
                    WorkPlanRegisterActivity.endLimitGugan = Common.doubleNullCheck(Double.parseDouble(jsonObject.get("maxMtnofEdpntDstnc").toString()));
                    WorkPlanRegisterActivity.startLimitEtValue = Common.doubleNullCheck(Double.parseDouble(((EditText) viewCollection.get("gugan")[0]).getText().toString()));
                    WorkPlanRegisterActivity.endLimitEtValue = Common.doubleNullCheck(Double.parseDouble(((EditText) viewCollection.get("gugan")[1]).getText().toString()));

*/

                } catch (ArrayIndexOutOfBoundsException e) {
                   Log.e("에러","익셉션발생;;");
                }catch ( JSONException  e) {
                    Log.e("에러","익셉션발생;;;");
                }catch ( InterruptedException e) {
                    Log.e("에러","익셉션발생;;;;");
                }catch (ExecutionException e) {
                    Log.e("에러","익셉션발생;;;;;");
                }catch (Exception e) {
                 //   e.printStackTrace();
                    Log.e("에러","익셉션발생;;;;;;");
                }

                /*    try{
                 *//*  jsonObject.accumulate("HDQR_CD", codeMap.get("HDQR_CD"));
                    jsonObject.accumulate("mtnofCd", (itemKeyValueMap.get("jisaList").get(((TextView) viewCollection.get("jisaView")[1]).getText())));
                    jsonObject.accumulate("MTNOF_NM", ((TextView) viewCollection.get("jisaView")[1]).getText());
                 //   jsonObject.accumulate("ROUTE_CD", ((TextView) viewCollection.get("jisaView")[1]).getText());
                    jisaMap.put(jsonObject.get("MTNOF_NM").toString(), jsonObject.get("MTNOF_CD").toString());*//*
                }catch (Exception e){
                   Log.e("에러","익셉션발생");
                }*/
            //    Log.i("dnnnnnnnndd", jsonObject.toString());


                if(parent.getId()==R.id.gamdokSpinner){
                    break;
                }
                //방향
                break;
            case R.id.inwonJangbiSpinner:
                if (parent.getSelectedItem().equals("작업원")) {
                    inwonEditText();

                } else if (parent.getSelectedItem().equals("사인카")) {
                    signCarSetting();
                } else if (parent.getSelectedItem().equals("작업차")) {
                    workCarSetting();
                }
                parent.setSelection(0);
                break;

            case R.id.startHourSpinner:
                ((Spinner) viewCollection.get("startHour")[0]).setSelection(position);
                ((TextView) viewCollection.get("startHour")[1]).clearComposingText();
                String startHourStr = ((Spinner) viewCollection.get("startHour")[0]).getSelectedItem().toString();
                if (startHourStr.length() == 1) startHourStr = "0" + startHourStr;
                ((TextView) viewCollection.get("startHour")[1]).setText(startHourStr);
                break;
            case R.id.startMinuteSpinner:
                ((Spinner) viewCollection.get("startMinute")[0]).setSelection(position);
                ((TextView) viewCollection.get("startMinute")[1]).clearComposingText();
                String startMinStr = ((Spinner) viewCollection.get("startMinute")[0]).getSelectedItem().toString();
                if (startMinStr.length() == 1) startMinStr = "0" + startMinStr;
                ((TextView) viewCollection.get("startMinute")[1]).setText(startMinStr);
                break;

            case R.id.endHourSpinner:
                ((Spinner) viewCollection.get("endHour")[0]).setSelection(position);
                ((TextView) viewCollection.get("endHour")[1]).clearComposingText();
                String endHourStr = ((Spinner) viewCollection.get("endHour")[0]).getSelectedItem().toString();
                if (endHourStr.length() == 1) endHourStr = "0" + endHourStr;
                ((TextView) viewCollection.get("endHour")[1]).setText(endHourStr);
                break;

            case R.id.endMinuteSpinner:
                ((Spinner) viewCollection.get("endMinute")[0]).setSelection(position);
                ((TextView) viewCollection.get("endMinute")[1]).clearComposingText();
                String endMinStr = ((Spinner) viewCollection.get("endMinute")[0]).getSelectedItem().toString();
                if (endMinStr.length() == 1) endMinStr = "0" + endMinStr;
                ((TextView) viewCollection.get("endMinute")[1]).setText(endMinStr);
                break;

            case R.id.workTypeCATSpinner:       //2021.07 작업유형(대분류)
                try {
                    workTypeJsonResult = nullCheck(baseActivity.new Action("get", SERVER_URL + "/WorkPlan/workTypeCode.do", itemKeyValueMap.get("workTypeCATList").get(parent.getSelectedItem().toString()), context).execute().get());
                    System.out.println("--------------------- workTypeJsonResult ======= " + workTypeJsonResult);
                    JSONArray workTypeJsonArray = new JSONArray(workTypeJsonResult);
                    workTypeMapList = new ArrayList<Map<String, String>>();
                    workTypeList = new ArrayList<>();
                    for (int i = 0; i < workTypeJsonArray.length(); i++) {
                        jsonObject = workTypeJsonArray.getJSONObject(i);
                        workTypeMap = new HashMap<String, String>();
                        workTypeMap.put("cmmnCdNm",jsonObject.get("cmmnCdNm").toString() );
                        workTypeMap.put("cmmnCd",jsonObject.get("cmmnCd").toString() );
                        workTypeMapList.add(workTypeMap);
                        workTypeList.add((String)jsonObject.get("cmmnCdNm"));

                        itemKeyValueMap.put("workType",workTypeMap);
                        ((List<Map<String, String>>) viewCollection.get("workType")[2]).set(i,workTypeMap);
                    }
                    /*for(int s = 0; s < workTypeList.size(); s++){
                        ((Spinner) viewCollection.get("workType")[0]).setSelection(s);
                    }*/
                    itemKeyValueMap.put("workType",workTypeMap);
                    ((Spinner) viewCollection.get("workType")[0]).clearAnimation();
                    new AdapterSpinnerUtil(context, R.layout.z_spinner_item, ((Spinner) viewCollection.get("workType")[0]), workTypeList, itemKeyValueMap, viewCollection);
                    ((TextView) viewCollection.get("workTypeCATView")[1]).setText(parent.getSelectedItem().toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;

            case R.id.workTypeSpinner:
                //2021.07 작업유형(대분류)
                parent.setSelection(position);
                ((TextView) viewCollection.get("workType")[1]).clearComposingText();
                ((TextView) viewCollection.get("workType")[1]).setText(parent.getSelectedItem().toString());
                System.out.println("------------------------workTypeSpinner workTypeMap 1======= " + parent.getSelectedItem().toString());
                //System.out.println("------------------------workTypeSpinner workTypeMap 2======= " + (workTypeList.get(1)).toString());
                System.out.println("------------------------workTypeSpinner workTypeMap 3======= " );

                //String workTypeCd = ((List<String>) viewCollection.get("workType")[2]).get(position);
                String workTypeNm = ((List<Map<String, String>>) viewCollection.get("workType")[2]).get(position).get("cmmnCdNm");
                String workTypeCd = ((List<Map<String, String>>) viewCollection.get("workType")[2]).get(position).get("cmmnCd");

                System.out.println("-----------------------------------------------------workTypeCd ======= " + workTypeCd);
                System.out.println("-----------------------------------------------------workTypeNm ======= " + workTypeNm);

                codeMap.put("cmmnCd", workTypeCd);
                codeMap.put("cmmnCdNm", workTypeNm);

                /*codeMap.put("cmmnCd", workTypeCd);
                codeMap.put("cmmnCdNm", workTypeNm);*/


                /*String workTypeCd = ((List<String>) viewCollection.get("workType")[2]).get(position)
                String workTypeNm = ((List<Map<String, String>>) viewCollection.get("workType")[2]).get(position).get("cmmnCdNm");*/

               /* System.out.println("-----------------------------------------------------workTypeCd ======= " + workTypeCd);
                System.out.println("-----------------------------------------------------workTypeNm ======= " + workTypeNm);

                codeMap.put("cmmnCd", workTypeCd);
                codeMap.put("cmmnCdNm", workTypeNm);*/
                break;
            case R.id.charoSpinner:

//viewCollection.put("charo",new Object[]{charoSpinner,roadlimitTextView,charoList});

                int n = maxCharo;
                parent.setSelection(position);
                ((TextView) viewCollection.get("charo")[1]).clearComposingText();
                ;
                ((TextView) viewCollection.get("charo")[1]).setText(parent.getSelectedItem().toString());
                ((ArrayList<String>) viewCollection.get("charo")[2]).clear();
                for (int i = 0; i < maxCharo; i++) {
                    ((ArrayList<String>) viewCollection.get("charo")[2]).add(new Integer(i + 1) + "차로");
                }
                ((ArrayList<String>) viewCollection.get("charo")[2]).add("진입램프");
                ((ArrayList<String>) viewCollection.get("charo")[2]).add("진출램프");
                ((ArrayList<String>) viewCollection.get("charo")[2]).add("교량하부");
                ((ArrayList<String>) viewCollection.get("charo")[2]).add("법면");
                ((ArrayList<String>) viewCollection.get("charo")[2]).add("회차로");
                ((ArrayList<String>) viewCollection.get("charo")[2]).add("이동차단");
                ((ArrayList<String>) viewCollection.get("charo")[2]).add("갓길");

                String charCurretSelItem = parent.getSelectedItem().toString();
                if (charCurretSelItem.equals("1차로")) {
                    codeMap.put("TRFC_LIMT_CTGW_CLSS_CD", "01");
                } else if (charCurretSelItem.equals("2차로")) {
                    codeMap.put("TRFC_LIMT_CTGW_CLSS_CD", "02");
                } else if (charCurretSelItem.equals("3차로")) {
                    codeMap.put("TRFC_LIMT_CTGW_CLSS_CD", "03");
                } else if (charCurretSelItem.equals("4차로")) {
                    codeMap.put("TRFC_LIMT_CTGW_CLSS_CD", "04");
                } else if (charCurretSelItem.equals("5차로")) {
                    codeMap.put("TRFC_LIMT_CTGW_CLSS_CD", "05");
                }  else if (charCurretSelItem.equals("6차로")) {
                    codeMap.put("TRFC_LIMT_CTGW_CLSS_CD", "06");
                }else if (charCurretSelItem.equals("진입램프")) {
                    codeMap.put("TRFC_LIMT_CTGW_CLSS_CD", "07");
                } else if (charCurretSelItem.equals("진출램프")) {
                    codeMap.put("TRFC_LIMT_CTGW_CLSS_CD", "08");
                } else if (charCurretSelItem.equals("교량하부")) {
                    codeMap.put("TRFC_LIMT_CTGW_CLSS_CD", "09");
                } else if (charCurretSelItem.equals("법면")) {
                    codeMap.put("TRFC_LIMT_CTGW_CLSS_CD", "10");
                } else if (charCurretSelItem.equals("회차로")) {
                    codeMap.put("TRFC_LIMT_CTGW_CLSS_CD", "11");
                } else if (charCurretSelItem.equals("이동차단")) {
                    codeMap.put("TRFC_LIMT_CTGW_CLSS_CD", "20");
                } else if (charCurretSelItem.equals("갓길")) {
                    codeMap.put("TRFC_LIMT_CTGW_CLSS_CD", "30");
                }
                break;

        }
    }//onItemSelected

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

   /* @Override
    public void onClick(View v ){
        Toast.makeText(context,"여긴옴",Toast.LENGTH_LONG).show();

        *//*switch (v.getId()){
            case  R.id.gamdokSpinner:
                Toast.makeText(context,"감독스피너",Toast.LENGTH_LONG).show();
                break;*//*
           // case  R.id.gamdokTextView:
                Toast.makeText(context,"감독티비",Toast.LENGTH_LONG).show();
               // ((Spinner) viewCollection.get("gamdokwon")[0]).performClick();
                gamokAlertDialog( gamdokArrayItem,viewCollection);
            //    break;
        //}
    }*/

    //다중 감독원



    @Override
    public int getCount() {
        return super.getCount();
    }//getCount()

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try{
            View v = super.getView(position, convertView, parent);
            Log.i("position ",position +"");
            Log.i("getCount() ",getCount() +"");
            if (position == getCount()) {
            }//if
            return v;
        }catch (NullPointerException e){
           Log.e("에러","익셉션발생");
        }
      return null;
    }//getView

    @Override
    public void setDropDownViewResource(int resources) {
        super.setDropDownViewResource(resources);
        spinner.setAdapter(this);

    }//setDropDownViewResource

    public void inwonEditText() {


        final EditText editText = new EditText(context);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("인원을 입력해주세요");
        builder.setView(editText);
        editText.clearComposingText();
        builder.setPositiveButton("입력",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String[] inwonTextViewStr = ((TextView) viewCollection.get("inwon")[3]).getText().toString().split(", ");
                        if(editText.getText().length()==0){
                            editText.setText("0");
                        }
                        inwonTextViewStr[0] = "인원 " + editText.getText() + "명, ";

                        ((TextView) viewCollection.get("inwon")[3]).setText(inwonTextViewStr[0] + inwonTextViewStr[1] + ", " + inwonTextViewStr[2]);
                        //   codeMap.put("humanCnt", editText.getText().toString());

                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create();
        builder.show();
    }


    public void signCarSetting() {


        final EditText editText = new EditText(context);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("사용 사인카의 수량을 입력해주세요");
        builder.setView(editText);
        editText.clearComposingText();
        builder.setPositiveButton("입력",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String[] inwonTextViewStr = ((TextView) viewCollection.get("inwon")[3]).getText().toString().split(", ");
                        if(editText.getText().length()==0){
                            editText.setText("0");
                        }
                        inwonTextViewStr[1] = "사인카 " + editText.getText() + "대, ";

                        ((TextView) viewCollection.get("inwon")[3]).setText(inwonTextViewStr[0] + ", " + inwonTextViewStr[1] + inwonTextViewStr[2]);
                        //     codeMap.put("humanCnt", editText.getText().toString());

                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create();
        builder.show();
    }


    public void workCarSetting() {


        final EditText editText = new EditText(context);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("인원을 입력해주세요");
        builder.setView(editText);
        editText.clearComposingText();
        builder.setPositiveButton("입력",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ;

                        String[] inwonTextViewStr = ((TextView) viewCollection.get("inwon")[3]).getText().toString().split(", ");
                        if(editText.getText().length()==0){
                            editText.setText("0");
                        }
                        inwonTextViewStr[2] = "작업차 " + editText.getText() + "대";

                        ((TextView) viewCollection.get("inwon")[3]).setText(inwonTextViewStr[0] + ", " + inwonTextViewStr[1] + ", " + inwonTextViewStr[2]);
                        // codeMap.put("humanCnt", editText.getText().toString());

                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create();
        builder.show();
    }










    /*



     *
     * DB에서 가져온 장비를 셋팅하는 메소드
     */


    public void settingjangbi() {
        try {
            ((List<String>) viewCollection.get("inwon")[4]).clear();
            String jangbiJsonResult = nullCheck(baseActivity.new Action("get", SERVER_URL + "/WorkPlan/jangbiItem.do", null).execute().get());
            jsonArray = new JSONArray(jangbiJsonResult);
            final boolean[] itemBoolean = new boolean[jsonArray.length() - 1];

            final String[] items = new String[jsonArray.length()];
            for (int i = 1; i < jsonArray.length(); i++) {

                jsonObject = jsonArray.getJSONObject(i);
                items[i - 1] = jsonObject.get("CMMN_CD_NM").toString();


            }

            final StringBuffer cmmnCdNmStrArray = new StringBuffer();
            final StringBuffer cmmnCdStrArray = new StringBuffer();
            ((List<String>) viewCollection.get("inwon")[4]).clear();
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);

            dialog.setTitle("장비를 선택하여 주세요")
                    .setMultiChoiceItems(items,
                            itemBoolean,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                                    //사인카 강제로 지정하였음

                                    // ((List<String>) viewCollection.get("inwon")[4]).set(0,items[0]);


                                    if (which == 0) return;

                                    if (isChecked) {
                                        ((List<String>) viewCollection.get("inwon")[4]).add(items[which]);

                                        try {
                                            cmmnCdNmStrArray.append(", " + items[which]);
                                            cmmnCdStrArray.append(", " + jsonArray.getJSONObject(which).get("CMMN_CD").toString());


                                            codeMap.put("janbiNm", cmmnCdNmStrArray.substring(2));//장비이름
                                            codeMap.put("janbiCd", cmmnCdStrArray.substring(2));//장비코드

                                        } catch (JSONException|ArrayIndexOutOfBoundsException e) {
                                           Log.e("에러","익셉션발생");
                                        }

                                    } else {
                                        ((List<String>) viewCollection.get("inwon")[4]).remove(items[which]);
                                    }
                                }
                            }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    if (((List<String>) viewCollection.get("inwon")[4]).size() != 0) {
                        codeMap.put("janbiNm", "장비");
                    }
                    if (codeMap.get("humanCnt") != null) {
                        ((TextView) viewCollection.get("inwon")[3]).setText(codeMap.get("humanCnt") + "명" + ", 사인카" + " 1대, " + (((List<String>) viewCollection.get("inwon")[4]).get(0)) + " 외" + new Integer(((List<String>) viewCollection.get("inwon")[4]).size() - 1) + "대");
                        codeMap.put("inwonJangbi", ((TextView) viewCollection.get("inwon")[3]).getText().toString());
                    } else {
                        Toast.makeText(context, "작업인원부터 선택하여 주세요", Toast.LENGTH_LONG).show();
                    }
                }

            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            dialog.show();
        } catch (NullPointerException|JSONException|InterruptedException|ExecutionException e) {
           Log.e("에러","익셉션발생");
        }
    }


}//class AdapterSpinnerUtil
