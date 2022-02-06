package com.ex.situationmanager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ex.situationmanager.TowMainActivity.TowListAdapter;
import com.ex.situationmanager.dto.Patrol;
import com.ex.situationmanager.dto.TowBscode;
import com.ex.situationmanager.dto.TowRow;
import com.ex.situationmanager.service.Parameters;
import com.ex.situationmanager.service.XMLData;
import com.ex.situationmanager.util.Common;
import com.ex.situationmanager.util.Configuration;
import com.ex.situationmanager.util.DBAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

public class

DialogTowSelectJisaActivity extends BaseActivity {

    private final String TAG = "DialogTowSelectJisaActivity";
    ArrayList<TowRow> itemList;

    ArrayList<String> userBsList;
    ListView towPopListView;
    ImageView btnSave, btnClose;
    ArrayList<TowBscode> items = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("oncreate", TAG + " oncreate Call!!");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        //주변 검정으로 변하지않게
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //다이얼로그 테두리 제거
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        setContentView(R.layout.popupscreen_tow);

        userBsList = Configuration.User.getBscode_list();

        for (int i = 0; i < userBsList.size(); i++) {
            Log.d("", "adfasdfasdfa = " + userBsList.get(i));
        }

        towPopListView = (ListView) findViewById(R.id.towPopListView);
        btnClose = (ImageView) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSave = (ImageView) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                db.updateBBBsCodeClean();
                for (int i = 0; i < items.size(); i++) {
                    if (Common.nullCheck(items.get(i).getUseyn()).equals("Y")) {
                        db.updateBBBsCode(items.get(i).getBscode(), "Y");
                    }
                }

                String rtnStr = db.fetchBBBsCodeSelected();
                Parameters params = new Parameters(ONECLICK_RCVJISALIST_UPDATE);
                if (Configuration.User.getCrdns_id_list() != null) {
                    if (Configuration.User.getCrdns_id_list().size() > 0) {
                        params.put("crdns_id", Configuration.User.getCrdns_id_list().get(0));
                    }
                }

                params.put("rcv_bs", rtnStr);
                new Action(ONECLICK_RCVJISALIST_UPDATE, params).execute("");
                setResult(RESULT_OK);
                //action
                finish();
            }
        });
        items = new ArrayList<TowBscode>();

        Cursor cursor = db.fetchBBBsCode();
        TowBscode item;

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            item = new TowBscode();
            String bbcode = cursor.getString(0);
            String bbname = cursor.getString(1);
            String bscode = cursor.getString(2);
            String bsname = cursor.getString(3);
            String useyn = cursor.getString(4);

            item.setBbcode(bbcode);
            item.setBbname(bbname);
            item.setBscode(bscode);
            item.setBsname(bsname);
            item.setUseyn(useyn);

            for (int j = 0; j < userBsList.size(); j++) {
                if (bscode.equals(userBsList.get(j))) {
                    items.add(item);

                    Log.e("개같1", item.getBbcode());
                    Log.e("개같2", item.getBbname());
                    Log.e("개같3", item.getBscode());
                    Log.e("개같4", item.getBsname());
                    Log.e("개같5", item.getUseyn());
                }
            }
        }

        //dbSearch후 displaygallery실행
        displayGallery();
    }


    public void displayGallery() {

        final TowPopListAdapter adapter = new TowPopListAdapter(this, items);

        towPopListView.setAdapter(adapter);
        towPopListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                adapter.setItemImage(position);
            }
        });
    }


    public class TowPopListAdapter extends ArrayAdapter<TowBscode> {
        private Context mContext;
        private LayoutInflater mInflater;

        public TowPopListAdapter(Context context, List<TowBscode> objects) {
            super(context, 0, objects);

            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mContext = context;

            if (objects.size() == 0) {
                Toast.makeText(getApplicationContext(), "검색된 결과가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }

        public void setItemImage(int position) {

        }

        public View getView(int position, View convertView, ViewGroup parent) {
        	final int posi = position;
            TextView txtJisaName;
            final CheckBox chkJisa;

            View view = convertView;
            if (convertView == null) {
                view = mInflater.inflate(R.layout.popupscreen_tow_row, null);
            }

            final TowBscode item = this.getItem(position);
            Log.e("테스트", "123123: " + this.getItem(position).getBbname());

            if (item != null) {
                txtJisaName = (TextView) view.findViewById(R.id.txtJisaName);
                txtJisaName.setText(item.getBbname() + " " + item.getBsname());
                chkJisa = (CheckBox) view.findViewById(R.id.chkJisa);

                if (Common.nullCheck(items.get(position).getUseyn()).equals("Y")) {
                    chkJisa.setChecked(true);
                    Log.i("select ", "true");

                } else {
                    chkJisa.setChecked(false);
                    Log.i("select ", "false");
                }

                chkJisa.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (chkJisa.isChecked() == true) {
                            items.get(posi).setUseyn("Y");
                            Log.d("", "checked true = " + posi);
                        } else {
                            items.get(posi).setUseyn("N");
                            Log.d("", "checked false = " + posi);
                        }
                    }
                });
            }

            return view;
        }
    }

    @Override
    protected void onActionPost(String primitive, XMLData result, Exception e) throws IOException {
        if (e == null) {
            try {
                String rtnResultCode = result.get("result");

                if ("1000".equals(rtnResultCode)) {
                    if (primitive.equals(ONECLICK_RCVJISALIST_UPDATE)) {
                        Log.d(TAG, TAG + "connection success!!");
                    }
                }

            } catch (XmlPullParserException e2) {
                // TODO: handle exception
                Log.e("에러", "예외발생");
            }

        } else {
            Toast.makeText(this, "서버와의 통신에 실패하였습니다.", Toast.LENGTH_LONG).show();
//			settingJeopboJisa();
        }
    }

    @Override
    protected void setActivityViewEdit(Activity activity) throws IOException {
        // TODO Auto-generated method stub
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
