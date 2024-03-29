package com.example.tinderview_demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
	int windowwidth;
	int screenCenter;
	int x_cord, y_cord, x, y, dx, dy;
	int Likes = 0;
	RelativeLayout parentView;
	float alphaValue = 0;
	private Context m_context;

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mainlayout);
		m_context = MainActivity.this;

		parentView = (RelativeLayout) findViewById(R.id.layoutview);
		windowwidth = getWindowManager().getDefaultDisplay().getWidth();
		//Log.d("myApp", Float.toString(windowwidth));
		screenCenter = windowwidth / 2;
		//int[] myImageList = new int[] { R.drawable.cats, R.drawable.baby1, R.drawable.sachin,
		//		R.drawable.cats, R.drawable.puppy };
		int [] myImageList = new int[] { R.drawable.cats, R.drawable.baby1, R.drawable.sachin};

		
		for (int i = 0; i < 3; i++) {
			LayoutInflater inflate = (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final View m_view = inflate.inflate(R.layout.custom_layout, null);
			//final View m_view = inflate.inflate(R.layout.fragment_main, null);
			ImageView m_image = (ImageView) m_view.findViewById(R.id.sp_image);
			LinearLayout m_topLayout = (LinearLayout) m_view.findViewById(R.id.sp_color);
			LinearLayout m_bottomLayout = (LinearLayout) m_view.findViewById(R.id.sp_linh);
			// final RelativeLayout myRelView = new RelativeLayout(this);
			m_view.setLayoutParams(new LayoutParams((windowwidth - 80), windowwidth));
			m_view.setX(40);
			m_view.setY(40);
			m_view.setTag(i);
			m_image.setBackgroundResource(myImageList[i]);
			
			
			// ADD dynamically like button on image.
			final Button imageLike = new Button(m_context);
			imageLike.setLayoutParams(new LayoutParams(100, 50));
			imageLike.setBackgroundDrawable(getResources().getDrawable(R.drawable.like));
			imageLike.setX(20);
			imageLike.setY(-250);
			imageLike.setAlpha(alphaValue);
			m_topLayout.addView(imageLike);

			// ADD dynamically dislike button on image.
			final Button imagePass = new Button(m_context);
			imagePass.setLayoutParams(new LayoutParams(100, 50));
			imagePass.setBackgroundDrawable(getResources().getDrawable(R.drawable.dislike));

			imagePass.setX(260);
			imagePass.setY(-300);
			imagePass.setAlpha(alphaValue);
			m_topLayout.addView(imagePass);

			// Click listener on the bottom layout to open the details of the
			// image.
			m_bottomLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					startActivity(new Intent(m_context, DetailsActivity.class));

				}
			});

			// Touch listener on the image layout to swipe image right or left.
			m_topLayout.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					x_cord = (int) event.getRawX();
					y_cord = (int) event.getRawY();

					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						x = (int) event.getX();
						y = (int) event.getY();
						Log.v("On touch", x + " " + y);
						break;
					case MotionEvent.ACTION_MOVE:
						x_cord = (int) event.getRawX(); // Updated for more smoother animation.
						y_cord = (int) event.getRawY();
						dx = x_cord - x;
						dy = y_cord - y;
						m_view.setX(dx);
						m_view.setY(dy - 80);
						if (dx >= 5) {
							m_view.setRotation((float) ((x_cord - screenCenter) * (Math.PI / 32)));
							//like image fade
							if (x_cord > (screenCenter + (screenCenter / 2))) {
								imageLike.setAlpha(1);
								if (x_cord > (windowwidth - (screenCenter / 4))) {
									Likes = 2;
								} else {
									Likes = 0;
								}
							} else {
								Likes = 0;
								imageLike.setAlpha(0);
							}
							imagePass.setAlpha(0);
						} else if ((dx < -5)){
							m_view.setRotation((float) ((x_cord - screenCenter) * (Math.PI / 32)));
							//pass image fade
							if (x_cord < (screenCenter / 2)) {
								imagePass.setAlpha(1);
								if (x_cord < screenCenter / 4) {
									Likes = 1;
								} else {
									Likes = 0;
								}
							} else {
								Likes = 0;
								imagePass.setAlpha(0);
							}
							imageLike.setAlpha(0);
						}
						else{};

						break;
					case MotionEvent.ACTION_UP:
						//x_cord = (int) event.getRawX();
						//y_cord = (int) event.getRawY();

						Log.e("X Point", "" + x_cord + " , Y " + y_cord);
						imagePass.setAlpha(0);
						imageLike.setAlpha(0);

						if (Likes == 0) {
							// Log.e("Event Status", "Nothing");
							m_view.setX(40);
							m_view.setY(40);
							m_view.setRotation(0);
						} else if (Likes == 1) {
							// Log.e("Event Status", "Passed");
							parentView.removeView(m_view);

						} else if (Likes == 2) {

							// Log.e("Event Status", "Liked");
							parentView.removeView(m_view);
						}
						break;
					default:
						break;
					}
					return true;
				}
			});

			parentView.addView(m_view);

		
		}
		
	}
	

}
