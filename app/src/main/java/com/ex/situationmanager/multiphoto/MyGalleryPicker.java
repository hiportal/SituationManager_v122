package com.ex.situationmanager.multiphoto;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ex.situationmanager.PatrolMainActivity;
import com.ex.situationmanager.R;
import com.ex.situationmanager.util.Common;
import com.ex.situationmanager.util.Configuration;

import android.R.bool;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class MyGalleryPicker extends FragmentActivity implements OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {
	private static final String TAG = "Collage";
	public static final String COL_WIDTH_KEY = "COL_WIDTH";
	public static final String FLURRY_EVENT_ADD_MULTIPLE_IMAGES = "Add multiple images";

	private static final int DEFAULT_COLUMN_WIDTH = 120;

	public static final int NOLIMIT = -1;
	public static final String MAX_IMAGES_KEY = "MAX_IMAGES";

	private  ImageAdapter imgAdapter;

	private Cursor imagecursor, actualimagecursor;
	private int image_column_index, actual_image_column_index;
	private int colWidth;

	private static final int CURSORLOADER_THUMBS = 0;
	private static final int CURSORLOADER_REAL = 1;

	private Set<String> fileNames = new HashSet<String>();

	private SparseBooleanArray checkStatus = new SparseBooleanArray();

	private Button confirm;
	private TextView freeLabel = null;
	private int maxImages;
	private boolean unlimitedImages = false;
	private GridView gridView;

	//	private ExecutorService executor;
	//threadpool Threads count  only use 1 else Image is sometimes dissappear
	final ExecutorService threadPool = Executors.newFixedThreadPool(1);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiselectorgrid);

		Intent i = getIntent();
		String MainType = i.getStringExtra("MainType");//0003
		if(!"0003".equals(Common.nullCheck(MainType))){
			Common.DeleteDir(Configuration.directoryName);
		}
		refreshSD();
//		executor = Executors.newCachedThreadPool();

		fileNames.clear();

		maxImages = getIntent().getIntExtra(MAX_IMAGES_KEY, NOLIMIT);
		maxImages = 5;

		unlimitedImages = maxImages == NOLIMIT;
		if (!unlimitedImages) {
			freeLabel = (TextView) findViewById(R.id.label_images_left);
			freeLabel.setVisibility(View.VISIBLE);
			updateLabel();
		}

		confirm = (Button) findViewById(R.id.confirm);
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectClicked(v);
			}
		});

		colWidth = getIntent().getIntExtra(COL_WIDTH_KEY, DEFAULT_COLUMN_WIDTH);

		Display display = getWindowManager().getDefaultDisplay();
		@SuppressWarnings("deprecation")
		int width = display.getWidth();
		int testColWidth = width / 3;

		if (testColWidth > colWidth) {
			colWidth = width / 4;
		}

		int bgColor = getIntent().getIntExtra("BG_COLOR", Color.BLACK);

		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setColumnWidth(colWidth);
		gridView.setOnItemClickListener(this);
		gridView.setBackgroundColor(bgColor);


		imgAdapter = new ImageAdapter(this);
		gridView.setAdapter(imgAdapter);
		LoaderManager.enableDebugLogging(false);


		getSupportLoaderManager().initLoader(CURSORLOADER_THUMBS, null, this);
		getSupportLoaderManager().initLoader(CURSORLOADER_REAL, null, this);
//		ImageLoaderHandler handler = new ImageLoaderHandler();
//		handler.sendEmptyMessage(0);
	}

	//	class ImageLoaderHandler extends Handler{
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//
//		}
//	}
	private void updateLabel() {

		if (freeLabel != null) {
			String text = String.format(getString(R.string.app_name), maxImages);
			freeLabel.setText(text);
			if (maxImages == 0) {
				freeLabel.setTextColor(Color.RED);
			} else {
				freeLabel.setTextColor(Color.WHITE);
			}
		}
	}

	public class ImageAdapter extends BaseAdapter {
		private final Matrix mMatrix = new Matrix();
		private Canvas canvas;
		private final Bitmap mPlaceHolderBitmap;
		private LayoutInflater mInflater;
		private Context mContext;

		public ImageAdapter(Context context) {
			Bitmap tmpHolderBitmap = BitmapFactory.decodeResource( getResources(), R.drawable.loading2);
			mPlaceHolderBitmap = Bitmap.createScaledBitmap(tmpHolderBitmap, colWidth, colWidth, false);
			if (tmpHolderBitmap != mPlaceHolderBitmap) {
				tmpHolderBitmap.recycle();
				tmpHolderBitmap = null;
			}
			this.mContext = context;
			mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			if (imagecursor != null) {
				return imagecursor.getCount();
			} else {
				return 0;
			}
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int pos, View convertView, ViewGroup parent) {
			View view = convertView;

			Log.d("", "re Call GetView Check !! " + pos);
			if (convertView == null) {
				view = mInflater.inflate(R.layout.multiselectorgrid_item, null);
			}
			final int position = pos;
			final ImageView imageView = (ImageView) view.findViewById(R.id.img_multiitem);
			final CheckBox chk = (CheckBox) view.findViewById(R.id.chk_multiitem);
			if (isChecked(position)) {
				chk.setChecked(true);
			}else {
				chk.setChecked(false);
			}

			chk.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String name = getImageName(position);
					if (chk.isChecked()) {
						if (fileNames.add(name)) {
							maxImages--;
						}
					} else {
						if (fileNames.remove(name)) {
							maxImages++;
						}
					}

					setChecked(position, chk.isChecked());
					confirm.setEnabled(fileNames.size() != 0);

					updateLabel();

					if(fileNames.size() > 5){
						chk.setChecked(false);
						String name2 = getImageName(position);
						if (chk.isChecked()) {
							if (fileNames.add(name2)) {
								maxImages--;
							}
						} else {
							if (fileNames.remove(name2)) {
								maxImages++;
							}
						}

						setChecked(position, chk.isChecked());
						confirm.setEnabled(fileNames.size() != 0);
						updateLabel();
						makeDialog();
					}
				}
			});


			imageView.setBackgroundColor(Color.TRANSPARENT);

			if (!imagecursor.moveToPosition(position)) {
				Log.d("", "getview return check");
				return imageView;
			}

			if (image_column_index == -1) {
				Log.d("", "getview return check");
				return imageView;
			}


			imagecursor.moveToPosition(position);
			final int cursorId = imagecursor.getInt(image_column_index);
			imageView.setImageBitmap(mPlaceHolderBitmap);
			final WeakReference<ImageView> ivRef = new WeakReference<ImageView>(imageView);

			Runnable theRunnable = new Runnable() {
				private void setInvisible() {
					if (ivRef.get() == null) {
						return;
					} else {
						final ImageView iv = (ImageView) ivRef.get();
						MyGalleryPicker.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								ivRef.get().setVisibility(View.GONE);
								ivRef.get().setClickable(false);
								ivRef.get().setEnabled(false);
							}
						});
					}
				}

				@SuppressLint("NewApi")
				@Override
				public  void run() {

					Bitmap thumb = MediaStore.Images.Thumbnails.getThumbnail( getContentResolver(), cursorId, MediaStore.Images.Thumbnails.MICRO_KIND, null);
//					actualimagecursor.moveToPosition(position);
//					BitmapFactory.Options option = new BitmapFactory.Options();
//					option.inSampleSize = 4;
//					Log.d("", "Runnable run = "+actualimagecursor.getString(actualimagecursor.getColumnIndex(MediaStore.Images.Media.DATA)));
//					Bitmap thumb = BitmapFactory.decodeFile(actualimagecursor.getString(actualimagecursor.getColumnIndex(MediaStore.Images.Media.DATA)), option);

					if (thumb == null) {
						setInvisible();
						return;
					} else {
						final Bitmap mutable = Bitmap.createBitmap(colWidth, colWidth, thumb.getConfig());
						if (mutable == null) {
							setInvisible();
							return;
						}
						canvas = new Canvas(mutable);
						if (canvas == null) {
							setInvisible();
							return;
						}

						RectF src = new RectF(0, 0, thumb.getWidth(), thumb.getHeight());
						RectF dst = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
						mMatrix.reset();
						mMatrix.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);
						canvas.drawBitmap(thumb, mMatrix, null);

						thumb.recycle();
						thumb = null;

						MyGalleryPicker.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (ivRef.get() == null) {
									return;
								} else {
									final ImageView iv = (ImageView) ivRef.get();
									iv.setImageBitmap(mutable);
								}
							}
						});
					}
				}
			};
			threadPool.execute(theRunnable);
			return view;
		}
	}


	private void makeDialog(){
		AlertDialog.Builder ad = new AlertDialog.Builder(MyGalleryPicker.this);
		ad.setMessage("")
				.setTitle("확인")
				.setMessage("* 이미지는 최대 5개까지 선택 가능합니다.")
				.setCancelable(false)
				.setPositiveButton("확인",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,int which) {
							}
						});
		ad.show();
	}
	private String getImageName(int position) {
		actualimagecursor.moveToPosition(position);
		String name = null;

		try {
			name = actualimagecursor.getString(actual_image_column_index);
		} catch (NullPointerException e) {
			return null;
		}
		return name;
	}

	private void setChecked(int position, boolean b) {
		checkStatus.put(position, b);
	}

	public boolean isChecked(int position) {
		boolean ret = checkStatus.get(position);
		return ret;
	}

	public void cancelClicked(View ignored) {
		setResult(RESULT_CANCELED);
		finish();
	}

	public void selectClicked(View ignored) {
		Common common = new Common();


		DecimalFormat decimalFormat = new DecimalFormat("00");// decimalformat
		DecimalFormat NumFormat = new DecimalFormat("0000");// 4
		Calendar rightNow = Calendar.getInstance();//
		int year = rightNow.get(Calendar.YEAR);
		int month = rightNow.get(Calendar.MONTH) + 1;
		int date = rightNow.get(Calendar.DATE);//
		int hour = rightNow.get(Calendar.HOUR_OF_DAY);
		int minute = rightNow.get(Calendar.MINUTE);
		int second = rightNow.get(Calendar.SECOND);
		//int rnd = (int) (Math.random() * 99);//이전 코드

		Random r = new Random();
		r.setSeed(new Date().getTime());
		int rnd = Math.abs((int) (r.nextInt() %99)+1);
		String result = Common.FILE_TAG+decimalFormat.format(year)
				+ decimalFormat.format(month) + decimalFormat.format(date)
				+ decimalFormat.format(hour) + decimalFormat.format(minute)
				+ decimalFormat.format(second) + decimalFormat.format(rnd);

		ArrayList<String> al = new ArrayList<String>();
		al.addAll(fileNames);
		for (int i = 0; i < al.size(); i++) {
			File file = new File(al.get(i));
			String tempName = file.getName();
			String ext = tempName.substring(tempName.indexOf("."));
			tempName = result+"_"+i+ext;

			Log.d("", "fileNames = " + al.get(i));
			Log.d("", "fileNames = " + file.getName());
			common.fileCopyToSituation(al.get(i), tempName, "N");
			file = null;
		}
		refreshSD();
		Intent data = getIntent();
		data.putStringArrayListExtra("imagelist", al);
		this.setResult(RESULT_OK, data);
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		String name = getImageName(position);

		if (name == null) {
			return;
		}
		boolean isChecked = !isChecked(position);
		// PhotoMix.Log("DAVID", "Posicion " + position + " isChecked: " +
		// isChecked);
		if (!unlimitedImages && maxImages == 0 && isChecked) {
			// PhotoMix.Log("DAVID", "Aquí no debería entrar...");
			isChecked = false;
		}

		if (isChecked) {
			// Solo se resta un slot si hemos introducido un
			// filename de verdad...
			if (fileNames.add(name)) {
				maxImages--;
				view.setBackgroundColor(Color.RED);
			}
		} else {
			if (fileNames.remove(name)) {
				// Solo incrementa los slots libres si hemos
				// "liberado" uno...
				maxImages++;
				view.setBackgroundColor(Color.TRANSPARENT);
			}
		}

		setChecked(position, isChecked);
		confirm.setEnabled(fileNames.size() != 0);
		updateLabel();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int cursorID, Bundle arg1) {
		CursorLoader cl = null;

		ArrayList<String> img = new ArrayList<String>();
		switch (cursorID) {

			case CURSORLOADER_THUMBS:
				img.add(MediaStore.Images.Media._ID);
				break;
			case CURSORLOADER_REAL:
				img.add(MediaStore.Images.Thumbnails.DATA);
				break;
			default:
				break;
		}

//		MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection                                       , null, null, MediaStore.Images.Media.DATE_ADDED + " desc ");
		cl = new CursorLoader(MyGalleryPicker.this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, img.toArray(new String[img.size()]), null, null, MediaStore.Images.Media.DATE_ADDED + " desc ");
		return cl;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if (cursor == null) {
			return;
		}

		switch (loader.getId()) {
			case CURSORLOADER_THUMBS:
				imagecursor = cursor;
				image_column_index = imagecursor.getColumnIndex(MediaStore.Images.Media._ID);
				//android 4.4.2 에서 문제 발생
//			ia.notifyDataSetChanged();
				break;
			case CURSORLOADER_REAL:
				actualimagecursor = cursor;
				actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				actualimagecursor.moveToFirst();
				break;
			default:
				getSupportLoaderManager().destroyLoader(0);
				break;
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (loader.getId() == CURSORLOADER_THUMBS) {
			imagecursor = null;
		} else if (loader.getId() == CURSORLOADER_REAL) {
			actualimagecursor = null;
		}
	}

	public void refreshSD() {

		sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
				Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
	}
}