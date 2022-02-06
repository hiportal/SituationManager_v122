package com.ex.situationmanager.multiphoto;

import java.io.File;
import java.util.ArrayList;

import com.ex.situationmanager.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;

public class GalleryListMain extends Activity implements OnClickListener{
	ProgressDialog mLoagindDialog;
	GridView mGvImageList;
	ImageAdapter mListAdapter;
	ArrayList<GalleryItem> mThumbImageInfoList;
	Button btnSelectCancel, btnSelectOk;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery_list);

		mThumbImageInfoList = new ArrayList<GalleryItem>();
		mGvImageList = (GridView) findViewById(R.id.gvImageList);

		btnSelectCancel = (Button) findViewById(R.id.btnSelectCancel);
		btnSelectOk = (Button) findViewById(R.id.btnSelectOk);
		btnSelectCancel.setOnClickListener(this);
		btnSelectOk.setOnClickListener(this);
		
		
		new DoFindImageList().execute();
	}

	private long findThumbList() {
		long returnValue = 0;

		// Select 하고자 하는 컬럼
		String[] projection = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };

		// 쿼리 수행
		Cursor imageCursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Images.Media.DATE_ADDED + " desc ");

		if (imageCursor != null && imageCursor.getCount() > 0) {
			// 컬럼 인덱스
			int imageIDCol = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
			int imageDataCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);

			// 커서에서 이미지의 ID와 경로명을 가져와서 ThumbImageInfo 모델 클래스를 생성해서
			// 리스트에 더해준다.
			while (imageCursor.moveToNext()) {
				GalleryItem thumbInfo = new GalleryItem();

				thumbInfo.setId(imageCursor.getString(imageIDCol));
				thumbInfo.setData(imageCursor.getString(imageDataCol));

				String path = imageCursor.getString(imageDataCol);
				BitmapFactory.Options option = new BitmapFactory.Options();

				if (new File(path).length() > 100000)
					option.inSampleSize = 4;
				else
					option.inSampleSize = 2;
				
				Bitmap bmp = BitmapFactory.decodeFile(path, option);
				thumbInfo.setBitmap(bmp);

				thumbInfo.setCheckedState(false);

				mThumbImageInfoList.add(thumbInfo);
				returnValue++;
			}
		}
		imageCursor.close();
		return returnValue;
	}

	// 화면에 이미지들을 뿌려준다.
	private void updateUI() {
		mListAdapter = new ImageAdapter(this, R.layout.gallery_cell, mThumbImageInfoList);
		mGvImageList.setAdapter(mListAdapter);
	}

	// *****************************************************************************************
	// //
	// Image Adapter Class
	// *****************************************************************************************
	// //
	static class ImageViewHolder {
		ImageView ivImage;
		CheckBox chkImage;
	}

	private class ImageAdapter extends BaseAdapter {
		private Context mContext;
		private int mCellLayout;
		private LayoutInflater mLiInflater;

		public ImageAdapter(Context c, int cellLayout, ArrayList<GalleryItem> thumbImageInfoList) {
			mContext = c;
			mCellLayout = cellLayout;
//			mArrData = thumbImageInfoList;

			mLiInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return mThumbImageInfoList.size();
		}

		public Object getItem(int position) {
			return mThumbImageInfoList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mLiInflater.inflate(mCellLayout, parent, false);
				ImageViewHolder holder = new ImageViewHolder();

				holder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
				holder.chkImage = (CheckBox) convertView.findViewById(R.id.chkImage);

				convertView.setTag(holder);
			}

			final ImageViewHolder holder = (ImageViewHolder) convertView.getTag();

			if (((GalleryItem) mThumbImageInfoList.get(position)).getCheckedState())
				holder.chkImage.setChecked(true);
			else
				holder.chkImage.setChecked(false);
			
			
			holder.ivImage.setImageBitmap(mThumbImageInfoList.get(position).getBitmap());
			
			setProgressBarIndeterminateVisibility(false);

			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					if (mThumbImageInfoList.get(position).getCheckedState() == true) {
						mThumbImageInfoList.get(position).setCheckedState(false);
					} else {
						mThumbImageInfoList.get(position).setCheckedState(true);
					}
					mListAdapter.notifyDataSetChanged();
				}
			});

			return convertView;
		}
	}

	// *****************************************************************************************
	// //
	// Image Adapter Class End
	// *****************************************************************************************
	// //

	// *****************************************************************************************
	// //
	// AsyncTask Class
	// *****************************************************************************************
	// //
	private class DoFindImageList extends AsyncTask<String, Integer, Long> {
		@Override
		protected void onPreExecute() {
			mLoagindDialog = ProgressDialog.show(GalleryListMain.this, null, "이미지 로딩중...", true, true);
			super.onPreExecute();
		}

		@Override
		protected Long doInBackground(String... arg0) {
			long returnValue = 0;
			returnValue = findThumbList();
			return returnValue;
		}

		@Override
		protected void onPostExecute(Long result) {
			updateUI();
			mLoagindDialog.dismiss();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
	}
	// *****************************************************************************************
	// //
	// AsyncTask Class End
	// *****************************************************************************************
	// //

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSelectOk:
			for (int i = 0; i < mThumbImageInfoList.size(); i++) {
				Log.d("", "GalleryListMain onclick checked list check ### position = " + i);
				Log.d("", ""+mThumbImageInfoList.get(i).getId());
				Log.d("", ""+mThumbImageInfoList.get(i).getData());//경로
				Log.d("", ""+mThumbImageInfoList.get(i).getCheckedState());//선택여부
			}
			Intent i = getIntent();
//			i.putExtra("imgName", mFileimageRoute + f + ".jpg");
			setResult(RESULT_OK, i);
			finish();
			break;
		case R.id.btnSelectCancel:
			finish();
			break;
		default:
			break;
		}
		
	}
}