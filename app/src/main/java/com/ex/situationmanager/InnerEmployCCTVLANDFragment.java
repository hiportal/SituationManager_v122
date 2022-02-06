package com.ex.situationmanager;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;



public class InnerEmployCCTVLANDFragment  extends Fragment {



    public InnerEmployCCTVLANDFragment(){

    }

    public String cur_cctv_url = "";

    public String getCur_cctv_url() {
        return cur_cctv_url;
    }

    public void setCur_cctv_url(String cur_cctv_url) {
        this.cur_cctv_url = cur_cctv_url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){


        View view = inflater.inflate(R.layout.inner_employee_cctv_land, container, false);
        InnerEmployActivity.currentFragment="InnerEmployCCTVFragment_LAND";

        VideoView cctv_videoView = (VideoView) view.findViewById(R.id.cctv_videoView);
        view =inflater.inflate(R.layout.inner_employee_cctv_land, container, false);
        cctv_videoView = (VideoView) view.findViewById(R.id.cctv_videoView);
        if(this.getCur_cctv_url()!=null){
            cctv_videoView.setVideoURI(Uri.parse(this.getCur_cctv_url()));

        }else{
            cctv_videoView.setVideoURI(Uri.parse(getArguments().get("cur_cctv").toString()));
        }

        cctv_videoView.start();
        return view;
    }
}
