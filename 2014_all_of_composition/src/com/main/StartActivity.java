package com.main;
/* 첫번째 엑티비티 */



import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class StartActivity extends Activity {

	int count = 0;
	
	private ImageView fadeView;
	
	LinearLayout linear1;
	LinearLayout linear2;
	

	RelativeLayout rel1;
	RelativeLayout rel2;
	
	//AnimationDrawable frameAnimation;
	ImageView view1;
	ImageView view2;
	TextView text;
	TextView tv;
	TextView tv1;
	TextView tv2;
	
	Intent intent;
	

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.activity_start); 
		
		tv = (TextView)findViewById(R.id.logotext);
		tv1 = (TextView)findViewById(R.id.logotext1);
		tv2 = (TextView)findViewById(R.id.touchscreen);
		Typeface face = Typeface.createFromAsset(getAssets(), "fonts/second.otf");
		tv.setTypeface(face);
		tv1.setTypeface(face);
		tv2.setTypeface(face);
		
		intent = new Intent(this, MainActivity.class); //intent 실행되면 menu01activity 실행.
		
		Background();
		
	}
	public void Background(){
		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setAnimationListener(  // Animation에 리스너를 등록한다
				new AnimationListener(){  // AnimationListener가 파라미터로 필요하다. 또한, AnimationListener는 추상메소드(abstract메소드)를 가지고있으므로
				 // 그 세가지 메소드를 반드시 구현해줘야 한다.
				//  그 세가지 메소드가 바로 onAnimationStart, onAnimationRepead, onAnimationEnd이다.
					public void onAnimationStart(Animation animation){
						Handler handler = new Handler();
						handler.postDelayed(new Runnable(){  // postDelayed 의 인자 800은 밀리세컨드이다. 즉, 0.8초후에 run()메소드가 실행된다
							public void run(){
								createUI(); //ui 생성
							}
						} , 800);
				 
					}
				 
					public void onAnimationRepead(Animation animation){
					
					}
				 
					public void onAnimationEnd(Animation arg0){
				 
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
					
					}
				});
				animation.setDuration(2000);  // 애니메이션이 지속될 시간이다. 즉 2초 동안 서서히 이미지가 나타난다.
				fadeView = (ImageView)findViewById(R.id.mainlogoimage);
				fadeView.startAnimation(animation);  // 애니메이션을 시작한다.
				
	}
	public void createUI(){
	
		
		Handler handler1=new Handler() {
			@Override
			public void handleMessage(Message msg) {
				tv1.setVisibility(View.VISIBLE);
			}
		};
		handler1.sendEmptyMessageDelayed(0, 1300);
		Handler handler2=new Handler() {
			@Override
			public void handleMessage(Message msg) {
				tv.setVisibility(View.VISIBLE);
			}
		};
		handler2.sendEmptyMessageDelayed(0, 2000);
		Handler handler=new Handler() {
			@Override
			public void handleMessage(Message msg) {
				tv2.setText("Touch the screen");
			}
		};
		handler.sendEmptyMessageDelayed(0, 3000); //3초 지속
		
		
		linear1 = (LinearLayout)findViewById(R.id.mainlayout);
		
		linear1.setOnTouchListener(new OnTouchListener() {//클릭 리스너
	         @Override
	         public boolean onTouch(View v, MotionEvent event) {
	            // TODO Auto-generated method stub
	            if(event.getAction()==MotionEvent.ACTION_DOWN)
	            {
	            }
	            if(event.getAction()==MotionEvent.ACTION_UP)
	            {
	               if(tv2.getText().equals("Touch the screen"))
	               {
	                  final Handler handler = new Handler();
	                  runOnUiThread(new Runnable() {
	                          
	                          public void run() {
	                              // TODO Auto-generated method stub
	                              final ProgressDialog dialog = ProgressDialog.show(
	                                    StartActivity.this, "Loading...", "DB 다운로드 중");
	                               
	                              handler.post(new Runnable() {
	                                   
	                                  public void run() {
	                                     try {
	                                     
	                                     startActivity(intent);
	                                     finish();//menu01 실행
	                                     }
	                                     catch(Exception e) {
	                                          Log.e("Error", e.getMessage());
	                                      } finally{
	                                          dialog.dismiss();
	                                      }
	                                     
	                                  }
	                              });
	                              
	                          }
	                  
	                  });
	               }
	         
	            }
	            else
	            {}
	            if(event.getAction()==MotionEvent.ACTION_MOVE)
	            {}
	         return true;

	        }
	         
	      });

			
		linear2 = (LinearLayout)findViewById(R.id.imagelayout);
		//linear2.setBackgroundResource(R.drawable.frame_animation_list);
		//frameAnimation = (AnimationDrawable) linear2.getBackground(); /*배경화면에 움직이는 xml 실행*/

	}
	
	
		
	
}
	
	