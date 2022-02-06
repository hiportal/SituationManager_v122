package com.ex.situationmanager;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.entity.ContentType;
import org.xmlpull.v1.XmlPullParserException;

import com.ex.situationmanager.dto.CodeNameVo;
import com.ex.situationmanager.dto.DirectionVo;
import com.ex.situationmanager.dto.Sisul;
import com.ex.situationmanager.service.Parameters;
import com.ex.situationmanager.service.SituationService;
import com.ex.situationmanager.service.XMLData;
import com.ex.situationmanager.util.Common;
import com.ex.situationmanager.util.Configuration;
import com.ex.situationmanager.util.FileUpload;
import com.ex.situationmanager.util.Image;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DialogJochi extends BaseActivity implements OnClickListener {
    private final String TAG = "DialogJochi";

    ImageView btn_pic, btn_confirm, btn_cancel;
    Spinner sp_01, sp_02, sp_03, sp_04, sp_05, sp_06;
    EditText ed_move_place;

    EditText ed_01, ed_02, ed_03, ed_04;

    String selected_sp_01;//차량종류
    String selected_sp_02;//보험
    String selected_sp_03;//견인업체
    String selected_sp_04;//지원내용
    String selected_sp_05;//지원유형
    String selected_sp_06;//견인사유
//	String selected_sp_07;//이동장소

    String selected_ed_01;//차량번호
    String selected_ed_02;//고객명
    String selected_ed_03;//고객연락처
    String selected_ed_04;//이동거리

    LinearLayout ll_sp_03, ll_sp_06, ll_sp_07, ll_ed_04;

    List<CodeNameVo> sp_01_items = new ArrayList<CodeNameVo>();
    List<CodeNameVo> sp_02_items = new ArrayList<CodeNameVo>();
    List<CodeNameVo> sp_03_items = new ArrayList<CodeNameVo>();
    List<CodeNameVo> sp_04_items = new ArrayList<CodeNameVo>();
    List<CodeNameVo> sp_05_items = new ArrayList<CodeNameVo>();
    List<CodeNameVo> sp_06_items = new ArrayList<CodeNameVo>();
//	List<Sisul> sp_07_items = new ArrayList<Sisul>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        //주변 검정으로 변하지않게
//		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //다이얼로그 테두리 제거
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_jochi);

        Intent receiveIntent = getIntent();
        selected_ed_03 = receiveIntent.getStringExtra("psn_tel_no");
        ed_01 = (EditText) findViewById(R.id.ed_01);
        ed_02 = (EditText) findViewById(R.id.ed_02);
        ed_03 = (EditText) findViewById(R.id.ed_03);
        ed_04 = (EditText) findViewById(R.id.ed_04);

        ed_move_place = (EditText) findViewById(R.id.ed_move_place);

        btn_pic = (ImageView) findViewById(R.id.btn_pic);
        btn_confirm = (ImageView) findViewById(R.id.btn_confirm);
        btn_cancel = (ImageView) findViewById(R.id.btn_cancel);
        btn_pic.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        ll_sp_03 = (LinearLayout) findViewById(R.id.ll_sp_03);
        LinearLayout ll_sp_06 = (LinearLayout) findViewById(R.id.ll_sp_06);
        LinearLayout ll_sp_07 = (LinearLayout) findViewById(R.id.ll_sp_07);
        LinearLayout ll_ed_04 = (LinearLayout) findViewById(R.id.ll_ed_04);

        if (Configuration.User.getUser_type().equals(SituationService.USER_TYPE_TOW)) {
//			ll_sp_03.setVisibility(View.GONE);	
        }


        setSp_01();
        setSp_02();
        setSp_03();ll_sp_03.setVisibility(View.GONE);
        setSp_04();
        setSp_05();
        setSp_06();
//		setSp_07();
        if (!SituationService.rpt_reg_type.equals("0007")) {//긴급견인
//			setSp_06();
//			setSp_07();
//			ll_sp_06.setVisibility(View.GONE);
//			ll_sp_07.setVisibility(View.GONE);
//			ll_ed_04.setVisibility(View.GONE);
        }

        Parameters params = new Parameters(ONECLICK_TOW_ACTION_SELECT);
        params.put("user_type", Common.nullCheck(SituationService.conf.User.getUser_type()).replace(" ", ""));
        params.put("rpt_id", SituationService.selectedRpt_id);
//		params.put("rpt_id", "10000893");

        params.put("bscode", Configuration.User.getBscode_list().get(0));
        params.put("hp_no", Configuration.User.getHp_no());

        new Action(ONECLICK_TOW_ACTION_SELECT, params).execute();
    }


    /**
     * 차량종류
     */
    private void setSp_01() {
        List<String> spinItems = new ArrayList<String>();
        for (int i = 0; i < sp_01_items.size(); i++) {
            spinItems.add(sp_01_items.get(i).getName());
        }

        int UserPosition = 0;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setText(getItem(getCount()));
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount();
            }

        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_01 = (Spinner) findViewById(R.id.sp_01);
        sp_01.setAdapter(adapter);
        sp_01.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_sp_01 = sp_01_items.get(position).getCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }

        });
    }

    /**
     * 보험
     */
    private void setSp_02() {
        List<String> spinItems = new ArrayList<String>();
        for (int i = 0; i < sp_02_items.size(); i++) {
            spinItems.add(sp_02_items.get(i).getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setText(getItem(getCount()));
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount();
            }

        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_02 = (Spinner) findViewById(R.id.sp_02);
        sp_02.setAdapter(adapter);
        sp_02.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_sp_02 = sp_02_items.get(position).getCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }

        });
    }

    /**
     * 견인업체
     */
    private void setSp_03() {
        List<String> spinItems = new ArrayList<String>();
        for (int i = 0; i < sp_03_items.size(); i++) {
            spinItems.add(sp_03_items.get(i).getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setText(getItem(getCount()));
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount();
            }

        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_03 = (Spinner) findViewById(R.id.sp_03);
        sp_03.setAdapter(adapter);
        sp_03.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_sp_03 = sp_03_items.get(position).getCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }

        });
    }

    /**
     * 지원내용
     */
    private void setSp_04() {
        List<String> spinItems = new ArrayList<String>();
        spinItems.add("긴급견인");
        spinItems.add("타이어교체");
        spinItems.add("밧데리충전");
        spinItems.add("냉각수보충");
        spinItems.add("견인협조");
        spinItems.add("서비스연락");
        spinItems.add("엔진고장");

        CodeNameVo item1 = new CodeNameVo();
        item1.setCode("01");
        item1.setName("긴급견인");
        CodeNameVo item2 = new CodeNameVo();
        item2.setCode("02");
        item2.setName("타이어교체");
        CodeNameVo item3 = new CodeNameVo();
        item3.setCode("03");
        item3.setName("밧데리충전");
        CodeNameVo item4 = new CodeNameVo();
        item4.setCode("04");
        item4.setName("냉각수보충");
        CodeNameVo item5 = new CodeNameVo();
        item5.setCode("05");
        item5.setName("견인협조");
        CodeNameVo item6 = new CodeNameVo();
        item6.setCode("06");
        item6.setName("서비스연락");
        CodeNameVo item7 = new CodeNameVo();
        item7.setCode("07");
        item7.setName("엔진고장");

        sp_04_items.add(item1);
        sp_04_items.add(item2);
        sp_04_items.add(item3);
        sp_04_items.add(item4);
        sp_04_items.add(item5);
        sp_04_items.add(item6);
        sp_04_items.add(item7);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setText(getItem(getCount()));
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount();
            }

        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_04 = (Spinner) findViewById(R.id.sp_04);
        sp_04.setAdapter(adapter);
        sp_04.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_sp_04 = sp_04_items.get(position).getCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }

        });
    }

    /**
     * 지원유형
     */
    private void setSp_05() {
        List<String> spinItems = new ArrayList<String>();

        spinItems.add("로봇신호사용");
        spinItems.add("신호탄사용");
        spinItems.add("라바콘설치");


        CodeNameVo item1 = new CodeNameVo();
        item1.setCode("01");
        item1.setName("로봇신호사용");
        CodeNameVo item2 = new CodeNameVo();
        item2.setCode("02");
        item2.setName("신호탄사용");
        CodeNameVo item3 = new CodeNameVo();
        item3.setCode("03");
        item3.setName("라바콘설치");
        sp_05_items.add(item1);
        sp_05_items.add(item2);
        sp_05_items.add(item3);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setText(getItem(getCount()));
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount();
            }

        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_05 = (Spinner) findViewById(R.id.sp_05);
        sp_05.setAdapter(adapter);
        sp_05.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_sp_05 = sp_05_items.get(position).getCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }

        });
    }

    /**
     * 견인사유
     */
    private void setSp_06() {
        List<String> spinItems = new ArrayList<String>();
        spinItems.add("타이어문제");
        spinItems.add("연료부족");
        spinItems.add("엔진고장");
        spinItems.add("방전");
        spinItems.add("사고");
        spinItems.add("잡물피해");
        spinItems.add("냉각수부족");
        spinItems.add("미션고장");
        spinItems.add("연료호스");

        CodeNameVo item1 = new CodeNameVo();
        item1.setCode("01");
        item1.setName("타이어문제");
        CodeNameVo item2 = new CodeNameVo();
        item2.setCode("02");
        item2.setName("연료부족");
        CodeNameVo item3 = new CodeNameVo();
        item3.setCode("03");
        item3.setName("엔진고장");
        CodeNameVo item4 = new CodeNameVo();
        item4.setCode("04");
        item4.setName("방전");
        CodeNameVo item5 = new CodeNameVo();
        item5.setCode("05");
        item5.setName("사고");
        CodeNameVo item6 = new CodeNameVo();
        item6.setCode("06");
        item6.setName("잡물피해");
        CodeNameVo item7 = new CodeNameVo();
        item7.setCode("07");
        item7.setName("냉각수부족");
        CodeNameVo item8 = new CodeNameVo();
        item8.setCode("08");
        item8.setName("미션고장");
        CodeNameVo item9 = new CodeNameVo();
        item8.setCode("09");
        item8.setName("미션고장");

        sp_06_items.add(item1);
        sp_06_items.add(item2);
        sp_06_items.add(item3);
        sp_06_items.add(item4);
        sp_06_items.add(item5);
        sp_06_items.add(item6);
        sp_06_items.add(item7);
        sp_06_items.add(item8);
        sp_06_items.add(item9);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setText(getItem(getCount()));
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount();
            }

        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_06 = (Spinner) findViewById(R.id.sp_06);
        sp_06.setAdapter(adapter);
        sp_06.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_sp_06 = sp_06_items.get(position).getCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }

        });
    }

    /**
     * 이동장소
     */
    private void setSp_07() {
		/*List<String> spinItems = new ArrayList<String>();
		Cursor cursor = db.selectICandRest(SituationService.latitude, SituationService.longitude);
		if(null != cursor){
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				Sisul item = new Sisul();
				String code = cursor.getString(0);
				String name = cursor.getString(1);
				String ns_code = cursor.getString(2);
				item.setCode(code);
				item.setName(name);
				item.setNs_code(ns_code);
				sp_07_items.add(item);
				spinItems.add(name);
			}
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinItems) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);
				if (position == getCount()) {
					((TextView) v.findViewById(android.R.id.text1)).setText("");
					((TextView) v.findViewById(android.R.id.text1)).setText(getItem(getCount()));
				}
				return v;
			}
			
			@Override
			public int getCount() {
				return super.getCount();
			}

		};
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_07 = (Spinner)findViewById(R.id.sp_07);
		sp_07.setAdapter(adapter);
		sp_07.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				selected_sp_07 = sp_07_items.get(position).getCode();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
			
		});*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pic:
                runCamera();
                break;
            case R.id.btn_confirm:
                if (ReadSdCardSize() > 0) {
                    sendValue(true);
                } else {
                    sendValue(false);
                }

                String sysCd = "OCK";
                String storgSeq = "29";
                String drtySeq = "1";
                Parameters params = new Parameters(ONECLICK_FILE_SEND);
                params.put("rpt_id", SituationService.selectedRpt_id);
                params.put("nscode", ns_code);
                params.put("nsname", ns_name);
                params.put("bhcode", bhCode);
                params.put("bhname", banghyang);
                params.put("ijung", currentIjung);
                if (com.ex.situationmanager.util.Configuration.User.getBscode_list() != null) {
                    if (com.ex.situationmanager.util.Configuration.User.getBscode_list().size() > 0) {
                        params.put("bscode", com.ex.situationmanager.util.Configuration.User.getBscode_list().get(0));
                    }
                }
                params.put("patcar_id", com.ex.situationmanager.util.Configuration.User.getPatcar_id());
                params.put("isClose", "Y");

//			params.put("SS_USER_ID", SituationService.conf.User.getPatcar_id());
//			params.put("sysCd", sysCd );
//			params.put("storgSeq", storgSeq);
//			params.put("drtySeq", drtySeq);

                executeJob(params, DialogJochi.this);

                break;
            case R.id.btn_cancel:
                finish();
                break;

            default:
                break;
        }
    }

    /**
     * 입력내용 전송
     */
    public void sendValue(boolean sendImg) {
//		String selected_sp_01;//차량종류
//		String selected_sp_02;//보험
//		String selected_sp_03;//견인업체
//		String selected_sp_04;//지원내용
//		String selected_sp_05;//지원유형
//		String selected_sp_06;//견인사유
//		String selected_sp_07;//이동장소
//		String selected_ed_01;//차량번호
//		String selected_ed_02;//고객명
//		String selected_ed_03;//고객연락처
//		String selected_ed_04;//이동거리

//		selected_sp_01 = Common.nullCheck(sp_01.getSelectedItem().toString());
//		selected_sp_02 = Common.nullCheck(sp_02.getSelectedItem().toString());
//		selected_sp_03 = Common.nullCheck(sp_03.getSelectedItem().toString());
//		selected_sp_04 = Common.nullCheck(sp_04.getSelectedItem().toString());
//		selected_sp_05 = Common.nullCheck(sp_05.getSelectedItem().toString());
//		selected_sp_06 = Common.nullCheck(sp_06.getSelectedItem().toString());
//		selected_sp_07 = Common.nullCheck(sp_07.getSelectedItem().toString());

        selected_ed_01 = Common.nullCheck(ed_01.getText().toString().replace(" ", ""));
        selected_ed_02 = Common.nullCheck(ed_02.getText().toString().replace(" ", ""));
        selected_ed_03 = Common.nullCheck(ed_03.getText().toString().replace(" ", ""));
        selected_ed_04 = Common.nullCheck(ed_04.getText().toString().replace(" ", ""));

        Parameters params = new Parameters(ONECLICK_TOW_ACTION_INSERT);
        try {
//			params.put("car_number", URLEncoder.encode(Common.nullCheck(selected_ed_01).replace(" ", ""), "utf-8"));
//			params.put("psn_name", URLEncoder.encode(Common.nullCheck(selected_ed_02).replace(" ", ""), "utf-8"));
//			params.put("car_number", Common.nullCheck(selected_ed_01).replace(" ", ""));
//			params.put("psn_name", Common.nullCheck(selected_ed_02).replace(" ", ""));

            byte[] hp_noByte = seed.encrypt(selected_ed_01 + "", szKey);
            String enchp_noByte = seed.renameSpecificChar(new String(Base64.encodeBase64(hp_noByte)));
            byte[] hp_noByte2 = seed.encrypt(selected_ed_02 + "", szKey);
            String enchp_noByte2 = seed.renameSpecificChar(new String(Base64.encodeBase64(hp_noByte2)));

            byte[] moveplaceByte = seed.encrypt(selected_ed_02 + "", szKey);
            String moveplaceStr = seed.renameSpecificChar(new String(Base64.encodeBase64(moveplaceByte)));

            params.put("car_number", enchp_noByte);
            params.put("psn_name", enchp_noByte2);

            params.put("psn_tel_no", Common.nullCheck(selected_ed_03).replace(" ", ""));
            params.put("move_km", Common.nullCheck(selected_ed_04).replace(" ", ""));
            params.put("rpt_id", Common.nullCheck(SituationService.selectedRpt_id).replace(" ", ""));
            params.put("hp_no", Common.nullCheck(Configuration.User.getHp_no()).replace(" ", ""));
            params.put("user_type", Common.nullCheck(SituationService.conf.User.getUser_type()).replace(" ", ""));
            params.put("car_code", Common.nullCheck(selected_sp_01).replace(" ", ""));
            params.put("insurance_code", Common.nullCheck(selected_sp_02).replace(" ", ""));
            params.put("tow_code", Common.nullCheck(selected_sp_03).replace(" ", ""));
            params.put("help_code", Common.nullCheck(selected_sp_04).replace(" ", ""));
            params.put("help_sort", Common.nullCheck(selected_sp_05).replace(" ", ""));
            params.put("tow_reason", Common.nullCheck(selected_sp_06).replace(" ", ""));
            params.put("move_place", moveplaceStr);
        } catch (Exception e) {
            //e.printStackTrace();
            Log.e("예외", "예외발생");
        }


        if (sendImg) {
            sendValueParent(new Action(ONECLICK_TOW_ACTION_INSERT, params));
        } else {
            new Action(ONECLICK_TOW_ACTION_INSERT, params).execute("");
        }


    }

    public void runCamera() {
        Common.DeleteDir(Common.FILE_DIR);
        Intent cameraIntent = new Intent(getApplicationContext(), CameraActivity2.class);
        cameraIntent.putExtra("directSendYN", false);
        startActivityForResult(cameraIntent, Configuration.IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Configuration.IMAGE_CAPTURE:
                Log.d("", TAG + " file full path = ");
                if (resultCode == RESULT_OK) {
                    //파일 전송.
                    mUriSet = data.getData();
                    String[] filenames = {
                            data.getStringExtra("imgName")
                    };
//				/storage/emulated/0/DCIM/Camera/situationmanager/2017102619083662_.jpg
                    Log.d("", TAG + " filenames " + filenames[0]);
//				FileUpload fUpload = new FileUpload();
//				fUpload.main(files, "", "", "");
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void setActivityViewEdit(Activity activity) throws IOException {
        // TODO Auto-generated method stub
    }


    @Override
    protected void onActionPost(String primitive, XMLData result, Exception e) throws IOException {
        try {
            if (e == null) {
                if (null != result) {
                    String rtnResultCode = result.get("result");
                    System.out.println("onActionPost resultCode = " + rtnResultCode);
//					if ("1000".equals(rtnResultCode)) {
                    if (primitive.equals(ONECLICK_TOW_ACTION_INSERT)) {
                        Parameters params = new Parameters(ONECLICK_FILE_SEND);
                        params.put("rpt_id", Common.nullCheck(SituationService.selectedRpt_id));
                        params.put("nscode", Common.nullCheck(SituationService.ns_code));
                        params.put("nsname", Common.nullCheck(SituationService.ns_name));
                        params.put("bhcode", Common.nullCheck(SituationService.bhCode));
                        params.put("bhname", Common.nullCheck(SituationService.banghyang));
                        params.put("ijung", Common.nullCheck(SituationService.currentIjung));

                        if (com.ex.situationmanager.util.Configuration.User.getBscode_list() != null) {
                            if (com.ex.situationmanager.util.Configuration.User.getBscode_list().size() > 0) {
                                params.put("bscode", com.ex.situationmanager.util.Configuration.User.getBscode_list().get(0));
                            }
                        }

                        if (Configuration.User.getBscode_list() != null) {
                            if (Configuration.User.getBscode_list().size() > 0) {
                                params.put("bscode", Configuration.User.getBscode_list().get(0));
                            }
                        }
                        params.put("patcar_id", Configuration.User.getPatcar_id());

                        executeJob(params, DialogJochi.this);

                        if ("1000".equals(rtnResultCode)) {
                            Toast.makeText(DialogJochi.this, "조치사항을 등록하였습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DialogJochi.this, "조치사항 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    } else if (ONECLICK_TOW_ACTION_SELECT.equals(primitive)) {
                        String car_number = Common.nullCheck(result.get("car_number"));
                        String psn_name = Common.nullCheck(result.get("psn_name"));
                        String psn_tel_no = Common.nullCheck(result.get("psn_tel_no"));
                        ed_01.setText(car_number);
                        ed_02.setText(psn_name);
                        ed_03.setText(psn_tel_no);

                        result.setList("list");

                        for (int i = 0; i < result.size(); i++) {
                            if (i == 0) {

                                XMLData data1 = result.getChild(i);
                                data1.setList("entity");
                                for (int j = 0; j < data1.size(); j++) {
                                    CodeNameVo item = new CodeNameVo();
                                    String car_code = data1.get(j, "car_code");
                                    String car_name = data1.get(j, "car_name");
                                    item.setCode(car_code);
                                    item.setName(car_name);
                                    sp_01_items.add(item);
                                    Log.d("", "ddddddddddddddddddd car = " + car_code + car_name);
                                }
                            } else if (i == 1) {
                                XMLData data1 = result.getChild(i);
                                data1.setList("entity");
                                for (int j = 0; j < data1.size(); j++) {
                                    CodeNameVo item = new CodeNameVo();
                                    String insurance_code = data1.get(j, "insurance_code");
                                    String insurance_name = data1.get(j, "insurance_name");
                                    item.setCode(insurance_code);
                                    item.setName(insurance_name);
                                    sp_02_items.add(item);
                                    Log.d("", "ddddddddddddddddddd car = " + insurance_code + insurance_name);
                                }
                            } else if (i == 2) {
                                XMLData data1 = result.getChild(i);
                                data1.setList("entity");
                                for (int j = 0; j < data1.size(); j++) {
                                    CodeNameVo item = new CodeNameVo();
                                    String tow_code = data1.get(j, "tow_code");
                                    String tow_name = data1.get(j, "tow_name");
                                    item.setCode(tow_code);
                                    item.setName(tow_name);
                                    sp_03_items.add(item);
                                    Log.d("", "ddddddddddddddddddd car = " + tow_code + tow_name);
                                }

                                if (data1.size() > 0) {
                                    ll_sp_03.setVisibility(View.GONE);
                                }
                            }
                        }
                        setSp_01();
                        setSp_02();
                        if (Configuration.User.getUser_type().equals(SituationService.USER_TYPE_PATROL)) {
                            setSp_03();
                        }
									
									/*result.setList("entity");
							for (int i = 0; i < result.size(); i++) {
								DirectionVo item = new DirectionVo();
								String direct_code = Common.nullCheck(result.get(i,"direct_code"));
								String direct_name = Common.nullCheck(result.get(i,"direct_name"));
								
								item.setDirect_code(direct_code);
								item.setDirect_name(direct_name);
								
								items.add(item);
							}*/
                    }
//					}
                }
            }
        } catch (XmlPullParserException e1) {
            Log.e("에러", "예외");
        }
    }
}

