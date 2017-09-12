package com.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;

import com.google.android.youtube.player.YouTubeIntents;
import com.sheet.ClefSymbol;
import com.sheet.MidiFile;
import com.sheet.MidiOptions;
import com.sheet.MidiPlayer;
import com.sheet.Piano;
import com.sheet.SheetMusic;
import com.sheet.TimeSigSymbol;
import com.sqlitedb.DBHandler;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class MainActivity extends Activity  {

	// 디자인0. 기본 메뉴 (시작,정지,뷰전환,재생바,설정)
	TextView start, stop, select_view;

	// 디자인1. 코드 생성/수정 뷰
	TextView mc1_1, mc1_2, mc2, mc4_1, mc4_2;
	LinearLayout make_chord, mc1, mc3, mc4;
	String textarray, chordresult;
	static ArrayList<String> singerlist, titlelist, chordlist;
	private ArrayList<String> chordsearch, RandomText, newRandomText;
	ArrayList<String> y_result = new ArrayList<String>();
	private DefaultSetting def_set = new DefaultSetting();
	private Button menu_button, codeBt[];
	
	String song_result;
	Intent youtube;
	static String USER_ID = "";
	enum IntentType {
	    PLAY_VIDEO, OPEN_PLAYLIST, PLAY_PLAYLIST, OPEN_USER,  OPEN_SEARCH, UPLOAD_VIDEO;
	  }
	
	String Temp;
	String help = "1q2w3e";
	FrameLayout main;
	LinearLayout lower;
	private boolean back_Check = false;
	private Handler back_Handler;
	

	// 디자인2. 트랙/채널 뷰
	TextView exp_cnt;
	LinearLayout ll_channel, ll_tracks;
	ScrollView cnt;
	int s_c = -1, v_t_cs = 0, v_t_ts = 0, indexop = 0, n_time = 0, cnttxt=0;
	int[] all_setting;
	String[] inf_ch, inf_ps, inf_ts, inf_oc;

	ArrayList<String> Track_Color = new ArrayList<String>();
	ArrayList<String> Hash_Order = new ArrayList<String>();
	ArrayList<String> Inst_Order = new ArrayList<String>();
	ArrayList<String> Vol_Order = new ArrayList<String>();
	HashMap<String, ArrayList<String>> listMap = new HashMap<String, ArrayList<String>>();

	private CharSequence[] Play = {"반주 없음", 
			"Arpegio1", "Arpegio2", "Arpegio3", "Arpegio4", "Arpegio5", 
			"Arpegio6", "Arpegio7", "Arpegio8", "Arpegio9", "Arpegio10", 
			"Hammered1", "Hammered2", "Hammered3", "Hammered4", "Hammered5",
			"Hammered6", "Hammered7", "Hammered8", "Hammered9", "Hammered10"};
	private CharSequence[] Drum = {"반주 없음", 
			"Drum1", "Drum2", "Drum3", "Drum4", "Drum5", 
			"Drum6", "Drum7", "Drum8", "Drum9", "Drum10", 
			"Drum11", "Drum12", "Drum13", "Drum14", "Drum15", 
			"Drum16", "Drum17", "Drum18", "Drum19", "Drum20" };
	private CharSequence[] Time = {"0/4","1/4","2/4","3/4","4/4","5/4",
			"6/4","7/4","8/4","9/4","10/4",
			"11/4","12/4","13/4","14/4","15/4",
			"16/4",};
	private CharSequence[] Oct = {"+0 옥타브", "+1 옥타브", "+2 옥타브", "+3 옥타브"};
	private CharSequence[] Inst = { "Drum", "Acoustic Grand Piano",
			"Bright Acoustic Piano", "Electric Grand Piano",
			"Honky-tonk Piano", "Electric Piano 1", "Electric Piano 2",
			"Harpsichord", "Clavi", "Celesta", "Glockenspiel", "Music Box",
			"Vibraphone", "Marimba", "Xylophone", "Tubular Bells", "Dulcimer",
			"Drawbar Organ", "Percussive Organ", "Rock Organ", "Church Organ",
			"Reed Organ", "Accordion", "Harmonica", "Tango Accordion",
			"Acoustic Guitar (nylon)", "Acoustic Guitar (steel)",
			"Electric Guitar (jazz)", "Electric Guitar (clean)",
			"Electric Guitar (muted)", "Overdriven Guitar",
			"Distortion Guitar", "Guitar harmonics", "Acoustic Bass",
			"Electric Bass (finger)", "Electric Bass (pick)", "Fretless Bass",
			"Slap Bass 1", "Slap Bass 2", "Synth Bass 1", "Synth Bass 2",
			"Violin", "Viola", "Cello", "Contrabass", "Tremolo Strings",
			"Pizzicato Strings", "Orchestral Harp", "Timpani",
			"String Ensemble 1", "String Ensemble 2", "Synth Strings 1",
			"Synth Strings 2", "Choir Aahs", "Voice Oohs", "Synth Voice",
			"Orchestra Hit", "Trumpet", "Trombone", "Tuba", "Muted Trumpet",
			"French Horn", "Brass Section", "Synth Brass 1", "Synth Brass 2",
			"Soprano Sax", "Alto Sax", "Tenor Sax", "Baritone Sax", "Oboe",
			"English Horn", "Bassoon", "Clarinet", "Piccolo", "Flute",
			"Recorder", "Pan Flute", "Blown Bottle", "Shakuhachi", "Whistle",
			"Ocarina", "Lead 1 (square)", "Lead 2 (sawtooth)",
			"Lead 3 (calliope)", "Lead 4 (chiff)", "Lead 5 (charang)",
			"Lead 6 (voice)", "Lead 7 (fifths)", "Lead 8 (bass-lead)",
			"Pad 1 (new age)", "Pad 2 (warm)", "Pad 3 (polysynth)",
			"Pad 4 (choir)", "Pad 5 (bowed)", "Pad 6 (metallic)",
			"Pad 7 (halo)", "Pad 8 (sweep)", "FX 1 (rain)",
			"FX 2 (soundtrack)", "FX 3 (crystal)", "FX 4 (atmosphere)",
			"FX 5 (brightness)", "FX 6 (goblins)", "FX 7 (echoes)",
			"FX 8 (sci-fi)", "Sitar", "Banjo", "Shamisen", "Koto", "Kalimba",
			"Bag pipe", "Fiddle", "Shanai", "Tinkle Bell", "Agogo",
			"Steel Drum Sound", "Woodblock", "Taiko Drum Sound", "Melodic Tom",
			"Synth Drum Sound", "Reverse Cymbal", "Guitar Fret Noise",
			"Breath Noise", "Seashore", "Bird Tweet", "Telephone Ring",
			"Helicopter", "Applause", "Gunshot" };
	private CharSequence[] Vol = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
			, "10", "11", "12" };
	private CharSequence[] Tempo = { "매우 느리게", "조금 느리게", "보통", "조금 빠르게",
			"매우 빠르게" };
	private CharSequence[] All_T = {"-32 (매우 느리게)","-31", "-30","-29", "-28", "-27", 
			"-26", "-25", "-24", "-23", "-22", "-21","-20", "-19", "-18", "-17", 
			"-16 (조금 느리게)", "-15", "-14", "-13", "-12", "-11", "-10", "-9", 
			"-8", "-7", "-6", "-5","-4", "-3", "-2", "-1", "0 (보통)", 
			"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
			"11", "12", "13", "14", "15", "16 (조금 빠르게)", "17", "18", "19", "20",
			"21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
			"31", "32 (매우 빠르게)"};
	private CharSequence[] Key = {"-11 (C키 기준 C#)","-10 (C키 기준 D)",
			"-9 (C키 기준 D#)","-8 (C키 기준 E)","-7 (C키 기준 F)",
			"-6 (C키 기준 F#)","-5 (C키 기준 G)","-4 (C키 기준 G#)",
			"-3 (C키 기준 A)","-2 (C키 기준 A#)","-1 (C키 기준 B)",
			"0 (C키 기준 C)","1 (C키 기준 C#)","2 (C키 기준 D)",
			"3 (C키 기준 D#)","4 (C키 기준 E)","5 (C키 기준 F)",
			"6 (C키 기준 F#)","7 (C키 기준 G)","8 (C키 기준 G#)",
			"9 (C키 기준 A)","10 (C키 기준 A#)","11 (C키 기준 B)",};
	private CharSequence[] Loop = {"반복 없음", "1번", "2번", "3번"};
	
	// 디자인3. 코드편집 뷰
	GridLayout edit_chord;
	TextView shall, shchn, shtrk, shap, op1, op2, op3, op4, op5, left_op1,
			left_op2, left_op3, left_op4, left_op5, value_op1, value_op2,
			value_op3, value_op4, value_op5, right_op1, right_op2, right_op3,
			right_op4, right_op5, confirm_t, close;

	// 디자인4. 메뉴 뷰
	LinearLayout menu;
	private Button save_proj, del_proj, load_proj, file_midi, file_pdf;

	// 기능1. MIDI, 악보
	private MediaPlayer mp;
	private SeekBar sb;
	private PlayStyle ps = new PlayStyle();
	private MakeMidi fs = new MakeMidi(MainActivity.this);
	private MidiPlayer player; /* The play/stop/rewind toolbar */
	private Piano piano; /* The piano at the top */
	private SheetMusic sheet; /* The sheet music */
	private LinearLayout layout; /* THe layout */
	private MidiFile midifile; /* The midi file to play */
	private MidiOptions options; /* The options for sheet music and sound */

	// 기능2. DB
	ArrayList<String> DB = new ArrayList<String>();
	int DB_Count = 0;
	String str, contents;
	String Now_P = "무제";

	// 기타_01. 각종 카운트 변수
	private int Count = 0, Channel_Pressed = 0, Channel_State = 0,
			Track_State = 0, Track_Pressed = 0, View_State = 0,
			Hash_Key = 0, Menu_State = 0, Score = 0;

	// 기타_02. 터치 이벤트
	Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 디00. 기본 메뉴 (시작,정지,뷰전환,재생바,설정)
		select_view = (TextView) findViewById(R.id.select_view);
		start = (TextView) findViewById(R.id.start);
		stop = (TextView) findViewById(R.id.stop);
		menu_button = (Button) findViewById(R.id.menu_button);
		main = (FrameLayout)findViewById(R.id.upper);
		lower = (LinearLayout)findViewById(R.id.lower);

		// 디01. 코드 설정/편집뷰
		make_chord = (LinearLayout) findViewById(R.id.make_chord);
		mc1 = (LinearLayout) findViewById(R.id.mc1);
		mc1_1 = (TextView) findViewById(R.id.mc1_1);
		mc1_2 = (TextView) findViewById(R.id.mc1_2);
		mc2 = (TextView) findViewById(R.id.mc2);
		mc3 = (LinearLayout) findViewById(R.id.mc3);
		mc4 = (LinearLayout) findViewById(R.id.mc4);
		mc4_1 = (TextView) findViewById(R.id.mc4_1);
		mc4_2 = (TextView) findViewById(R.id.mc4_2);

		// 디02. 트랙/채널 뷰
		exp_cnt = (TextView) findViewById(R.id.exp_cnt);
		cnt = (ScrollView) findViewById(R.id.channelntrack);
		ll_channel = (LinearLayout) findViewById(R.id.ll_channel);
		ll_tracks = (LinearLayout) findViewById(R.id.ll_tracks);

		// 디03. 코드 편집 뷰
		edit_chord = (GridLayout) findViewById(R.id.edit_chord);
		shall = (TextView) findViewById(R.id.shall);
		shchn = (TextView) findViewById(R.id.shchn);
		shtrk = (TextView) findViewById(R.id.shtrk);
		shap = (TextView) findViewById(R.id.shap);
		op1 = (TextView) findViewById(R.id.op1);
		op2 = (TextView) findViewById(R.id.op2);
		op3 = (TextView) findViewById(R.id.op3);
		op4 = (TextView) findViewById(R.id.op4);
		op5 = (TextView) findViewById(R.id.op5);
		left_op1 = (TextView) findViewById(R.id.left_op1);
		left_op2 = (TextView) findViewById(R.id.left_op2);
		left_op3 = (TextView) findViewById(R.id.left_op3);
		left_op4 = (TextView) findViewById(R.id.left_op4);
		left_op5 = (TextView) findViewById(R.id.left_op5);
		value_op1 = (TextView) findViewById(R.id.value_op1);
		value_op2 = (TextView) findViewById(R.id.value_op2);
		value_op3 = (TextView) findViewById(R.id.value_op3);
		value_op4 = (TextView) findViewById(R.id.value_op4);
		value_op5 = (TextView) findViewById(R.id.value_op5);
		right_op1 = (TextView) findViewById(R.id.right_op1);
		right_op2 = (TextView) findViewById(R.id.right_op2);
		right_op3 = (TextView) findViewById(R.id.right_op3);
		right_op4 = (TextView) findViewById(R.id.right_op4);
		right_op5 = (TextView) findViewById(R.id.right_op5);
		confirm_t = (TextView) findViewById(R.id.confirm_t);
		close = (TextView) findViewById(R.id.close);

		// 디04. 메뉴 뷰
		menu = (LinearLayout) findViewById(R.id.menu);
		save_proj = (Button) findViewById(R.id.save_proj);
		load_proj = (Button) findViewById(R.id.load_proj);
		del_proj = (Button) findViewById(R.id.del_proj);
		file_midi = (Button) findViewById(R.id.file_midi);
		file_pdf = (Button) findViewById(R.id.file_pdf);

		// 기01. MIDI
		sb = (SeekBar) findViewById(R.id.seekBar);
		mp = new MediaPlayer();


		setDB();
		setXML();
		setReset();
		setClick();
		setButton();
	}

	private void setDB() {

		DBHandler dbhandler = DBHandler.open(this);
		Cursor c = dbhandler.selectAll();

		while (c.moveToNext()) {
			// int id = c.getInt(c.getColumnIndex("_id"));
			String name = c.getString(c.getColumnIndex("_name"));
			// String contents = c.getString(c.getColumnIndex("_contents"));
			DB.add(name);
			DB_Count++;
		}

		dbhandler.close();
	}

	private void setXML() {
		runOnUiThread(new Runnable() {
			public void run() {
				try {
					URL url = new URL(
							"http://cvsale.dothome.co.kr:80/Books/search.php");
					url.openStream();

					singerlist = def_set.getXmlDataList("searchresult.xml",
							"singer");
					titlelist = def_set.getXmlDataList("searchresult.xml",
							"title");
					chordlist = def_set.getXmlDataList("searchresult.xml",
							"chord");
					chordsearch = def_set.getXmlDataList("searchresult.xml",
							"num");
					RandomText = def_set.getXmlDataList("searchresult.xml",
							"num");
					newRandomText = def_set.getXmlDataList("searchresult.xml",
							"num");
					
				} catch (Exception e) {
					Log.e("Error", e.getMessage());
				}
			}
		});
	}

	private void setReset() {
		
		
		// 디01.코드 생성/편집 뷰
		Clear_Chord();

		

		textarray = "";
		chordresult = "";
		y_result.clear();
		//chordsearch.clear();
		//RandomText.clear();
		//newRandomText.clear();
		// 디02. 트랙/채널 뷰
		cnt.setVisibility(View.GONE);
		exp_cnt.setVisibility(View.GONE);
		Track_Color.clear();
		Hash_Order.clear();
		Inst_Order.clear();
		Vol_Order.clear();
		cnttxt = 0;

		all_setting = new int[3];
		for (int i = 0; i < 3; i++)
			all_setting[i] = 0;

		new PlaybarMonitor(sb, mp);
		mp.reset();
		fs = new MakeMidi(MainActivity.this);


		// 디04. 메뉴 뷰
		menu.setVisibility(View.GONE);

		// 기타 변수들
		Count = 0;
		Channel_Pressed = 0;
		Channel_State = 0;
		Track_State = 0;
		Track_Pressed = 0;
		View_State = 0;
		Hash_Key = 0;
		Menu_State = 0;

	}

	private void setClick() {

		// 디00. 기본 메뉴 (시작,정지,뷰전환,재생바,설정)
		start.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
			if(n_time == 0){
				mp.reset();
				Full_Sound();
			}

			else
				Caution_Time_Signature();

		
			}
		});

		stop.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mp.reset();
				sb.setProgress((0));
			}
		});

		select_view.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (View_State == 0 && Count == 5) {
					make_chord.setVisibility(View.GONE);

					if (ll_channel.getChildCount() == 0)
						exp_cnt.setVisibility(View.VISIBLE);

					cnt.setVisibility(View.VISIBLE);
					select_view.setText("채널/트랙");

					View_State = 1;

				}

				else if (View_State == 1 && Count == 5) {
					cnt.setVisibility(View.GONE);
					exp_cnt.setVisibility(View.GONE);
					make_chord.setVisibility(View.VISIBLE);

					select_view.setText("코드");

					View_State = 0;
				}

				else if (Count != 5) {
					Toast.makeText(getBaseContext(), "코드 생성을 완료해주세요",
							Toast.LENGTH_LONG).show();
				}

			}

		});

		// 디01. 코드 생성/수정 뷰

		mc1_1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Count = 5;
				mc1_1.setVisibility(View.GONE);
				mc1_2.setVisibility(View.VISIBLE);

				// YouTube 코드
				mc4_1.setVisibility(View.GONE);
				mc4_2.setVisibility(View.VISIBLE);

				// 만약 코드 수정후의 생성이면 (채널이 하나라도 있다면)
				if (ll_channel.getChildCount() > 0) {

					for (int i = 0; i < ll_channel.getChildCount(); i++) {
						ArrayList<String> list = listMap.get(Hash_Order.get(i));
						for (int t = 0; t < list.size(); t++) {
							if (t % 10 == 0)
								list.set(t, chordresult);
						}
					}
				}

			}
		});

		mc1_2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Caution_Clear_Chord();
				y_result.clear();
			}
		});

		mc4_1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Clear_Chord();
			}
		});
		
		mc4_2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int r;
				Random random = new Random();
				r = random.nextInt(y_result.size());
				USER_ID = y_result.get(r);
				You_Tube();
		        startActivity(youtube);
			}
		});


		// 디02. 채널/트랙 뷰
		exp_cnt.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// make track and channel
				if (Count == 5) {
					Add_Channel(0);
					exp_cnt.setVisibility(View.GONE);
				}

				else {
					Toast toast = Toast.makeText(MainActivity.this,
							"먼저 코드를 생성해주세요", Toast.LENGTH_SHORT);
					toast.show();
				}
			}
		});

		value_op2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				String key_s = String.valueOf(Hash_Order.get(v_t_cs));
				ArrayList<String> list = listMap.get(key_s);
				
				if(op2.getText().equals("반주")){
					
					if(Inst_Order.get(v_t_cs).equals("0"))
						Click_Op(value_op2,"반주 설정", Drum, list, v_t_ts+1,0);
					
					else
						Click_Op(value_op2,"반주 설정", Play, list, v_t_ts+1,0);
					
				}
				
				else if(op2.getText().equals("악기"))
					Click_Op(value_op2,"악기 설정",Inst, Inst_Order, v_t_cs,0);
				
				else if(op2.getText().equals("빠르기"))
					Click_Op(value_op2,"빠르기 설정",Tempo, list , v_t_ts+4,2);
				
				else if(op2.getText().equals("전체 빠르기"))
					Click_Op(value_op2,"전체 빠르기 설정",All_T, null , all_setting[0],32);

			}
		});
		
		value_op3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				String key_s = String.valueOf(Hash_Order.get(v_t_cs));
				ArrayList<String> list = listMap.get(key_s);
				
				if(op3.getText().equals("박자"))
					Click_Op(value_op3,"박자 설정", Time, list, v_t_ts+2, 0);
				
				else if(op3.getText().equals("전조"))
					Click_Op(value_op3,"전조 설정", Key, list, v_t_ts+5 ,11);
				
				else if(op3.getText().equals("음량"))
					Click_Op(value_op3,"음량 설정",Vol, Vol_Order , v_t_cs, 0);
				
				else if(op3.getText().equals("전체 전조"))
					Click_Op(value_op3,"전체 전조 설정", Key, null , all_setting[1],11);

			}
		});
		
		value_op4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				String key_s = String.valueOf(Hash_Order.get(v_t_cs));
				ArrayList<String> list = listMap.get(key_s);
				
				if(op4.getText().equals("옥타브"))
					Click_Op(value_op4,"옥타브 설정", Oct, list, v_t_ts+3, 0);
				
				else if(op4.getText().equals("반복"))
					Click_Op(value_op4,"반복 설정", Loop, list, v_t_ts+6 ,0);
				
				else if(op4.getText().equals("전체 반복"))
					Click_Op(value_op4,"전체 반복 설정",Loop, null , all_setting[2], 0);

				else if(op4.getText().equals("텍스트")){
					if(value_op4.getText().equals("채널/트랙")){
						value_op4.setText("악기/정보");
						cnttxt = 1;
						F5cnt(cnttxt);
					}
					else if(value_op4.getText().equals("악기/정보")){
						value_op4.setText("채널/트랙");
						cnttxt = 0;
						F5cnt(cnttxt);
					}
				}
					
			}
		});


		// 디03. 코드 편집 뷰
		left_op2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				String key_s = String.valueOf(Hash_Order.get(v_t_cs));
				ArrayList<String> list = listMap.get(key_s);

				if (op2.getText().equals("반주") && INT(inf_ps[s_c]) > 0) {
					int n_v_i = Integer.parseInt(inf_ps[s_c]) - 1;
					String n_v_t = String.valueOf(n_v_i);
					inf_ps[s_c] = n_v_t;

					String n_v_s = "";
					for (int i = 0; i < 4; i++) {
						n_v_s += inf_ps[i];
						if (i < 3)
							n_v_s += "-";
					}

					list.set(v_t_ts + 1, n_v_s);
					
					if(Inst_Order.get(v_t_cs).equals("0"))
						value_op2.setText(Drum[n_v_i]);
					
					else
						value_op2.setText(Play[n_v_i]);
					
					Paint_Track(list);
				}

				else if (op2.getText().equals("빠르기")
						&& INT(list.get(v_t_ts + 4)) > -2) {
					int l = Integer.parseInt(list.get(v_t_ts + 6));
					int n_v_i = Integer.parseInt(list.get(v_t_ts + 4)) - 1;
					String n_v_t = String.valueOf(n_v_i);
					list.set(v_t_ts + 4, n_v_t);
					value_op2.setText(Tempo[n_v_i + 2]);
					Change_Size_Track(v_t_cs, v_t_ts / 10, n_v_i, l);
				}

				else if (op2.getText().equals("악기")
						&& INT(Inst_Order.get(v_t_cs)) > 0) {
					int n_v_i = Integer.parseInt(Inst_Order.get(v_t_cs)) - 1;

					for (int i = 0; i < Inst_Order.size(); i++) {
						if (n_v_i == INT(Inst_Order.get(i))) {
							n_v_i--;
						}
					}

					if (n_v_i != -1) {
						String n_v_t = String.valueOf(n_v_i);
						Inst_Order.set(v_t_cs, n_v_t);
						value_op2.setText(Inst[n_v_i]);
					}
				}

				else if (op2.getText().equals("전체 빠르기")&& all_setting[0] > -32) {
					
					if(all_setting[0] <= 0)
						fs.header[13] --;
					
					else{
						fs.header[13] -= 2;
					}
					
					all_setting[0]--;
					value_op2.setText(All_T[all_setting[0]+32]);
				}
				

				mp.reset();
				Option_Sound(v_t_cs, v_t_ts/10);
			}
		});

		left_op3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				String key_s = String.valueOf(Hash_Order.get(v_t_cs));
				ArrayList<String> list = listMap.get(key_s);

				if (op3.getText().equals("박자") && INT(inf_ts[s_c]) > 0) {
					int n_v_i = Integer.parseInt(inf_ts[s_c]) - 1;
					String n_v_t = String.valueOf(n_v_i);
					inf_ts[s_c] = n_v_t;
					String n_v_s = "";
					for (int i = 0; i < 4; i++) {
						n_v_s += inf_ts[i];
						if (i < 3)
							n_v_s += "-";
					}
					list.set(v_t_ts + 2, n_v_s);
					Exception_Time_Signature();
					value_op3.setText(Time[n_v_i]);
				}

				else if (op3.getText().equals("전조")
						&& INT(list.get(v_t_ts + 5)) > -11) {
					int n_v_i = Integer.parseInt(list.get(v_t_ts + 5)) - 1;
					String n_v_t = String.valueOf(n_v_i);
					list.set(v_t_ts + 5, n_v_t);
					value_op3.setText(Key[n_v_i + 11]);
				}

				else if (op3.getText().equals("음량")
						&& INT(Vol_Order.get(v_t_cs)) > 0) {
					int n_v_i = Integer.parseInt(Vol_Order.get(v_t_cs)) - 1;
					String n_v_t = String.valueOf(n_v_i);
					Vol_Order.set(v_t_cs, n_v_t);
					value_op3.setText(Vol[n_v_i]);
				}

				else if (op3.getText().equals("전체 전조") && all_setting[1] > -11) {
					all_setting[1]--;
					value_op3.setText(Key[all_setting[1]+11]);
					
					
					
				}

				Option_Sound(v_t_cs, v_t_ts/10);
			}
		});

		left_op4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				String key_s = String.valueOf(Hash_Order.get(v_t_cs));
				ArrayList<String> list = listMap.get(key_s);

				if (op4.getText().equals("옥타브") && INT(inf_oc[s_c]) > 0) {
					int n_v_i = Integer.parseInt(inf_oc[s_c]) - 1;
					String n_v_t = String.valueOf(n_v_i);
					inf_oc[s_c] = n_v_t;
					String n_v_s = "";
					for (int i = 0; i < 4; i++) {
						n_v_s += inf_oc[i];
						if (i < 3)
							n_v_s += "-";
					}
					list.set(v_t_ts + 3, n_v_s);
					value_op4.setText(Oct[n_v_i]);
				}

				else if (op4.getText().equals("반복")
						&& INT(list.get(v_t_ts + 6)) > 0) {
					int t = Integer.parseInt(list.get(v_t_ts + 4));
					int n_v_i = Integer.parseInt(list.get(v_t_ts + 6)) - 1;
					String n_v_t = String.valueOf(n_v_i);
					list.set(v_t_ts + 6, n_v_t);
					value_op4.setText(Loop[n_v_i]);
					Change_Size_Track(v_t_cs, v_t_ts / 10, t, n_v_i);
				}

				else if (op4.getText().equals("전체 반복") && all_setting[2] > 0) {
					all_setting[2]--;
					value_op4.setText(Loop[all_setting[2]]);
				}

				Option_Sound(v_t_cs, v_t_ts/10);
			}
		});

		right_op2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				String key_s = String.valueOf(Hash_Order.get(v_t_cs));
				ArrayList<String> list = listMap.get(key_s);

				if (op2.getText().equals("반주") && INT(inf_ps[s_c]) < 20) {
					int n_v_i = Integer.parseInt(inf_ps[s_c]) + 1;
					String n_v_t = String.valueOf(n_v_i);
					inf_ps[s_c] = n_v_t;

					String n_v_s = "";
					for (int i = 0; i < 4; i++) {
						n_v_s += inf_ps[i];
						if (i < 3)
							n_v_s += "-";
					}

					list.set(v_t_ts + 1, n_v_s);
					

					if(Inst_Order.get(v_t_cs).equals("0"))
						value_op2.setText(Drum[n_v_i]);
					
					else
						value_op2.setText(Play[n_v_i]);
					
					// Paint_Track(list);
				}

				else if (op2.getText().equals("빠르기")
						&& INT(list.get(v_t_ts + 4)) < 2) {
					int l = Integer.parseInt(list.get(v_t_ts + 6));
					int n_v_i = Integer.parseInt(list.get(v_t_ts + 4)) + 1;
					String n_v_t = String.valueOf(n_v_i);
					list.set(v_t_ts + 4, n_v_t);
					value_op2.setText(Tempo[n_v_i + 2]);
					Change_Size_Track(v_t_cs, v_t_ts / 10, n_v_i, l);
				}

				else if (op2.getText().equals("악기")
						&& INT(Inst_Order.get(v_t_cs)) < 128) {
					int n_v_i = Integer.parseInt(Inst_Order.get(v_t_cs)) + 1;

					for (int i = 0; i < Inst_Order.size(); i++) {
						if (n_v_i == INT(Inst_Order.get(i))) {
							n_v_i++;
						}
					}

					if (n_v_i != 129) {
						String n_v_t = String.valueOf(n_v_i);
						Inst_Order.set(v_t_cs, n_v_t);
						value_op2.setText(Inst[n_v_i]);
					}

				}

				else if (op2.getText().equals("전체 빠르기") && all_setting[0] < 32) {
					
					if(all_setting[0] >= 0){
						fs.header[13] += 2;
					}
					else
						fs.header[13]++;
					
					all_setting[0]++;
					value_op2.setText(All_T[all_setting[0] + 32]);
				}

				mp.reset();
				Option_Sound(v_t_cs, v_t_ts/10);
			}
		});

		right_op3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				String key_s = String.valueOf(Hash_Order.get(v_t_cs));
				ArrayList<String> list = listMap.get(key_s);

				if (op3.getText().equals("박자") && INT(inf_ts[s_c]) < 16) {
					int n_v_i = Integer.parseInt(inf_ts[s_c]) + 1;
					String n_v_t = String.valueOf(n_v_i);
					inf_ts[s_c] = n_v_t;
					String n_v_s = "";
					for (int i = 0; i < 4; i++) {
						n_v_s += inf_ts[i];
						if (i < 3)
							n_v_s += "-";
					}
					list.set(v_t_ts + 2, n_v_s);
					Exception_Time_Signature();
					value_op3.setText(Time[n_v_i]);
				}

				else if (op3.getText().equals("전조")
						&& INT(list.get(v_t_ts + 5)) < 11) {
					int n_v_i = Integer.parseInt(list.get(v_t_ts + 5)) + 1;
					String n_v_t = String.valueOf(n_v_i);
					list.set(v_t_ts + 5, n_v_t);
					value_op3.setText(Key[n_v_i + 11]);
				}

				else if (op3.getText().equals("음량")
						&& INT(Vol_Order.get(v_t_cs)) < 12) {
					int n_v_i = Integer.parseInt(Vol_Order.get(v_t_cs)) + 1;
					String n_v_t = String.valueOf(n_v_i);
					Vol_Order.set(v_t_cs, n_v_t);
					value_op3.setText(Vol[n_v_i]);
				}

				else if (op3.getText().equals("전체 전조") && all_setting[1] < 11) {
					all_setting[1]++;
					value_op3.setText(Key[all_setting[1] + 11]);
				}

				Option_Sound(v_t_cs, v_t_ts/10);
			}
		});

		right_op4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				String key_s = String.valueOf(Hash_Order.get(v_t_cs));
				ArrayList<String> list = listMap.get(key_s);

				if (op4.getText().equals("옥타브") && INT(inf_oc[s_c]) < 3) {
					int n_v_i = Integer.parseInt(inf_oc[s_c]) + 1;
					String n_v_t = String.valueOf(n_v_i);
					inf_oc[s_c] = n_v_t;
					String n_v_s = "";
					for (int i = 0; i < 4; i++) {
						n_v_s += inf_oc[i];
						if (i < 3)
							n_v_s += "-";
					}
					list.set(v_t_ts + 3, n_v_s);
					value_op4.setText(Oct[n_v_i]);
				}

				else if (op4.getText().equals("반복")
						&& INT(list.get(v_t_ts + 6)) < 3) {
					int t = Integer.parseInt(list.get(v_t_ts + 4));
					int n_v_i = Integer.parseInt(list.get(v_t_ts + 6)) + 1;
					String n_v_t = String.valueOf(n_v_i);
					list.set(v_t_ts + 6, n_v_t);
					value_op4.setText(Loop[n_v_i]);
					Change_Size_Track(v_t_cs, v_t_ts / 10, t, n_v_i);
				}

				else if (op4.getText().equals("전체 반복") && all_setting[2] < 3) {
					all_setting[2]++;
					value_op4.setText(Loop[all_setting[2]]);
				}

				Option_Sound(v_t_cs, v_t_ts/10);
			}
		});

		confirm_t.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				confirm_t.setVisibility(View.VISIBLE);
				String key_s = String.valueOf(Hash_Order.get(v_t_cs));
				ArrayList<String> list = listMap.get(key_s);
				String all_ps = "";
				String all_oc = "";

				for (int i = 0; i < 4; i++) {
					all_ps += inf_ps[s_c];
					all_oc += inf_oc[s_c];
					if (i < 3) {
						all_ps += "-";
						all_oc += "-";
					}
				}

				list.set(v_t_ts + 1, all_ps);
				list.set(v_t_ts + 2, "4-4-4-4");
				list.set(v_t_ts + 3, all_oc);
				inf_ps = list.get(v_t_ts + 1).split("-");
				inf_ts = list.get(v_t_ts + 2).split("-");
				inf_oc = list.get(v_t_ts + 3).split("-");
				Paint_Track(list);
				value_op3.setText("4/4");
				Exception_Time_Signature();
			}
		});

		shap.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Shadow_CO(shap);
				confirm_t.setVisibility(View.VISIBLE);
				s_c = (s_c + 1) % 4;
				shap.setText((inf_ch[s_c].replaceAll("/", "")).replaceAll("0",
						""));

				value_op2.setTextSize(25);
				value_op3.setTextSize(25);
				left_op4.setText("◁");
				right_op4.setText("▷");

				op2.setText("반주");
				op3.setText("박자");
				op4.setText("옥타브");
				
				if(Inst_Order.get(v_t_cs).equals("0"))
					value_op2.setText(Drum[INT(inf_ps[s_c])]);
				
				else
					value_op2.setText(Play[INT(inf_ps[s_c])]);
					
				
				value_op3.setText(Time[INT(inf_ts[s_c])]);
				value_op4.setText(Oct[INT(inf_oc[s_c])]);
				select_view.setText("코드 설정");

			}
		});

		shtrk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				String key_s = String.valueOf(Hash_Order.get(v_t_cs));
				ArrayList<String> list = listMap.get(key_s);

				if (n_time == 0) {
					Shadow_CO(shtrk);
					confirm_t.setVisibility(View.INVISIBLE);

					s_c = -1;
					shap.setText("코드");

					value_op2.setTextSize(25);
					value_op3.setTextSize(20);
					left_op4.setText("◁");
					right_op4.setText("▷");

					op2.setText("빠르기");
					op3.setText("전조");
					op4.setText("반복");

					value_op2.setText(Tempo[INT(list.get(v_t_ts + 4)) + 2]);
					value_op3.setText(Key[INT(list.get(v_t_ts + 5)) + 11]);
					value_op4.setText(Loop[INT(list.get(v_t_ts + 6))]);
					select_view.setText("트랙 설정");
				}

				else
					Caution_Time_Signature();

			}
		});

		shchn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (n_time == 0) {
					Shadow_CO(shchn);
					confirm_t.setVisibility(View.INVISIBLE);
					s_c = -1;
					shap.setText("코드");

					value_op2.setTextSize(15);
					value_op3.setTextSize(25);
					left_op4.setText("");
					right_op4.setText("");
					
					if(cnttxt == 0)
						value_op4.setText("채널/트랙");

					else if(cnttxt == 1)
						value_op4.setText("악기/정보");

					op2.setText("악기");
					op3.setText("음량");
					op4.setText("텍스트");
					value_op2.setText(Inst[INT(Inst_Order.get(v_t_cs))]);
					value_op3.setText(Vol[INT(Vol_Order.get(v_t_cs))]);
					select_view.setText("채널 설정");
				}

				else
					Caution_Time_Signature();
			}
		});

		shall.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (n_time == 0) {
					Shadow_CO(shall);
					confirm_t.setVisibility(View.INVISIBLE);
					s_c = -1;
					shap.setText("코드");

					value_op2.setTextSize(20);
					value_op3.setTextSize(20);
					left_op4.setText("◁");
					right_op4.setText("▷");

					op2.setText("전체 빠르기");
					op3.setText("전체 전조");
					op4.setText("전체 반복");

					value_op2.setText(All_T[all_setting[0] + 32]);
					value_op3.setText(Key[all_setting[1] + 11]);
					value_op4.setText(Loop[all_setting[2]]);
					select_view.setText("전체 설정");
				}

				else
					Caution_Time_Signature();
			}
		});

		close.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (n_time == 0) {
					Shadow_CO(null);
					confirm_t.setVisibility(View.VISIBLE);
					value_op2.setTextSize(25);
					value_op3.setTextSize(25);
					left_op4.setText("◁");
					right_op4.setText("▷");
					cnt.setVisibility(View.VISIBLE);
					edit_chord.setVisibility(View.GONE);
					select_view.setText("채널/트랙");
					View_State = 1;
					s_c = -1;
					
					if(cnttxt == 1)
						F5cnt(1);
					
				} else
					Caution_Time_Signature();
			}
		});

		// 디04. 메뉴 뷰
		menu_button.setOnClickListener(new View.OnClickListener() {
			int Menu_Count;

			public void onClick(View v) {

				if (n_time == 0) {

					if (Menu_State == 0) {
						make_chord.setVisibility(View.GONE);
						exp_cnt.setVisibility(View.GONE);
						cnt.setVisibility(View.GONE);
						edit_chord.setVisibility(View.GONE);
						menu.setVisibility(View.VISIBLE);
						select_view.setText("메뉴");
						Menu_Count = View_State;
						View_State = 3;
						Menu_State++;
					}

					else {
						menu.setVisibility(View.GONE);

						if (Menu_Count == 0) {
							View_State = Menu_Count;
							make_chord.setVisibility(View.VISIBLE);
							select_view.setText("코드");
						} else if (Menu_Count == 1) {
							View_State = Menu_Count;
							if (ll_channel.getChildCount() == 0)
								exp_cnt.setVisibility(View.VISIBLE);
							cnt.setVisibility(View.VISIBLE);
							select_view.setText("채널/트랙");
						} else if (Menu_Count == 2) {
							View_State = Menu_Count;
							edit_chord.setVisibility(View.VISIBLE);
							
							if(op2.getText().equals("반주"))
								select_view.setText("코드 설정");
							if(op2.getText().equals("빠르기"))
								select_view.setText("트랙 설정");
							if(op2.getText().equals("악기"))
								select_view.setText("채널 설정");
							if(op2.getText().equals("전체 빠르기"))
								select_view.setText("전체 설정");
						} 
						Menu_State = 0;
					}
				}

				else
					Caution_Time_Signature();

			}
		});

		save_proj.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (ll_channel.getChildCount() == 0) {
					Toast.makeText(getBaseContext(), "채널/트랙을 생성해주세요",
							Toast.LENGTH_LONG).show();
				} else
					Db_Save();
			}
		});

		load_proj.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (DB.size() == 0) {
					Toast.makeText(getBaseContext(), "불러올 프로젝트가 없습니다",
							Toast.LENGTH_LONG).show();
				} else
					Db_Contents();
			}
		});

		del_proj.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (DB.size() == 0) {
					Toast.makeText(getBaseContext(), "삭제할 프로젝트가 없습니다",
							Toast.LENGTH_LONG).show();
				} else
					Db_Delete();
			}
		});

		file_midi.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (ll_channel.getChildCount() == 0) {
					Toast.makeText(getBaseContext(), "채널/트랙을 생성해주세요",
							Toast.LENGTH_LONG).show();
				} else
					Export_Midi();

			}
		});

		file_pdf.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (ll_channel.getChildCount() == 0) {
					Toast.makeText(getBaseContext(), "채널/트랙을 생성해주세요",
							Toast.LENGTH_LONG).show();
				} else {
					Make_Score();
				}
			}
		});
	}

	// MIDI

	private void Make_Sound(int start) {

		fs = new MakeMidi(MainActivity.this);
		int end_for_drum = 0;
		int c = 0;
		int first = 0, last = ll_channel.getChildCount();
		int []max_track = Sort();

		int ts =0;
		
		if(start != -1){
			first = start;
			last = start+1;
		}
		
		for (int all_loop = 0; all_loop < all_setting[2]+1; all_loop++) {
		
			for (c = first; c <  last; c++) {

				if (((TextView) ((ViewGroup) ((ViewGroup) ll_channel
						.getChildAt(c)).getChildAt(0)).getChildAt(1)).getText()
						.equals("ON")) {

					int pc = Integer.parseInt(Inst_Order.get(c));
					int chn = c;
					if(c==9)
						chn = 10;
					int ds = 0;
					if (pc == 0) {
						end_for_drum = 9;
						chn = 9;
						ds = 100;
					} else
						pc--;
					 //채널의 트랙 길이 합
					ts =0;
					
					fs.progChange(pc, chn);
					String key_s = String.valueOf(Hash_Order.get(c));
					ArrayList<String> list = listMap.get(key_s);

					for (int t = 0; t < list.size() / 10; t++) {

						String[] chord = list.get(0 + (10 * t)).split("-");
						String[] play_style = list.get(1 + (10 * t)).split("-");
						String[] time_signature = list.get(2 + (10 * t)).split(
								"-");
						String[] octave = list.get(3 + (10 * t)).split("-");
						int tempo = Integer.parseInt(list.get(4 + (10 * t)));
						int key = Integer.parseInt(list.get(5 + (10 * t)));
						int loop = Integer.parseInt(list.get(6 + (10 * t)));
						
						int tmp=0;
						
						if(tempo == -2)
							tmp = 40;
						else if(tempo == -1)
							tmp = 30;
						else if(tempo == 0)
							tmp = 20;
						else if(tempo == 1)
							tmp = 15;
						else if(tempo == 2)
							tmp = 10;
						
						ts += tmp * (loop+1); 

						for (int l = 0; l < loop + 1; l++) {
							for (int i = 0; i < chord.length; i++) {

								int p_s = INT(play_style[i]);
								int t_s = INT(time_signature[i]);
								int oct = INT(octave[i]);
								int vol = INT(Vol_Order.get(c));

								int[] data = new int[7];
								data[0] = p_s + ds;
								data[1] = t_s;
								data[2] = oct * 12;
								data[3] = -tempo;
								data[4] = (key + all_setting[1]) % 12;
								data[5] = vol * 10;
								data[6] = 0;

								if (Integer.parseInt(time_signature[i]) > 4) {

									for (int j = 0; j < (t_s / 4); j++) {
										data[1] = 4;
										ps.style(fs, chn, chord[i], data);
									}

									data[1] = t_s % 4;

									ps.style(fs, chn, chord[i], data);
								}

								else
									ps.style(fs, chn, chord[i], data);
							}
						}

					}
					//트랙까지 다돈 후
					if(ts < max_track[0]){
						int add = max_track[0] - ts;

						int[] data = { 0, 4, 0, 0, 0, 0, 0 };
						for(int n = 0; n < add/5; n++){
						ps.style(fs, chn, "A/0/0/0/0", data);
						}
						
					}
				}
			}
		}

		if(end_for_drum == 9)
			fs.end(9);
		
			fs.end(max_track[1]);
	}

	private void Individual_Sound(String A) {

		if (mp.isPlaying())
			mp.pause();

		mp.reset();
		MakeMidi is = new MakeMidi(MainActivity.this);
		//플레이, 박자, 옥타브, 빠르기, 음정, 음량, 전체음정
		int[] data = { -1, 4, 0, 0, 0, 0, 0 };
		ps.style(is, 0, A, data);

		String file = "indiv";
		String dir = getDir("", MODE_PRIVATE).getAbsolutePath();
		String soundPath = dir + File.separator + file;

		try {
			is.writeToFile(file, 0);
			mp.setDataSource(soundPath);
			mp.prepare();
			mp.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void Option_Sound(int c, int t){


		if (mp.isPlaying())
			mp.pause();

		mp.reset();
		MakeMidi is = new MakeMidi(MainActivity.this);
		
		int pc = Integer.parseInt(Inst_Order.get(c));
		int chn = c;
		if(c==9)
			chn = 10;
		int ds = 0;
		int end_for_drum = 0;
		
		if (pc == 0) {
			end_for_drum = 9;
			chn = 9;
			ds = 100;
		} else
			pc--;

		is.progChange(pc, chn);

		String key_s = String.valueOf(Hash_Order.get(c));
		ArrayList<String> list = listMap.get(key_s);
		
		String[] chord = list.get(0 + (10 * t)).split("-");
		String[] play_style = list.get(1 + (10 * t)).split("-");
		String[] time_signature = list.get(2 + (10 * t)).split(
				"-");
		String[] octave = list.get(3 + (10 * t)).split("-");
		int tempo = Integer.parseInt(list.get(4 + (10 * t)));
		int key = Integer.parseInt(list.get(5 + (10 * t)));
		int loop = Integer.parseInt(list.get(6 + (10 * t)));

		for (int l = 0; l < loop + 1; l++) {
			for (int i = 0; i < chord.length; i++) {

				int p_s = INT(play_style[i]);
				int t_s = INT(time_signature[i]);
				int oct = INT(octave[i]);
				int vol = INT(Vol_Order.get(c));

				int[] data = new int[7];
				data[0] = p_s + ds;
				data[1] = t_s;
				data[2] = oct * 12;
				data[3] = -tempo;
				data[4] = (key + all_setting[1]) % 12;
				data[5] = vol * 10;
				data[6] =0;
				

				if(s_c == -1){
				if (Integer.parseInt(time_signature[i]) > 4) {

					for (int j = 0; j < (t_s / 4); j++) {
						data[1] = 4;
						ps.style(is, chn, chord[i], data);
					}

					data[1] = t_s % 4;

					ps.style(is, chn, chord[i], data);
				}

				else
					ps.style(is, chn, chord[i], data);
				}
				
				else if (l == 0 && s_c == i ){
					if (Integer.parseInt(time_signature[s_c]) > 4) {

						for (int j = 0; j < (t_s / 4); j++) {
							data[1] = 4;
							ps.style(is, chn, chord[s_c], data);
						}

						data[1] = t_s % 4;

						ps.style(is, chn, chord[s_c], data);
					}

					else
						ps.style(is, chn, chord[s_c], data);
				}
				
				
			}
		}
		
		
		if(end_for_drum == 9)
			is.end(9);
		
			is.end(chn);
		
		
		//플레이, 박자, 옥타브, 빠르기, 음정, 음량, 전체음정
		String file = "indiv";
		String dir = getDir("", MODE_PRIVATE).getAbsolutePath();
		String soundPath = dir + File.separator + file;

		try {
			is.writeToFile(file, 0);
			mp.setDataSource(soundPath);
			mp.prepare();
			mp.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		//	mp.seekTo(dur);
		
		
	}
	
	private void Full_Sound() {

		// 템포를 조절하는 명령어

		if (mp.isPlaying())
			mp.pause();
		mp.reset();

		Make_Sound(-1);

		String Filename = "full";
		String dir = getDir("", MODE_PRIVATE).getAbsolutePath();
		String soundPath = dir + File.separator + Filename;

		try {
			fs.writeToFile(Filename, 0);
			mp.setDataSource(soundPath);
			mp.prepare();
			mp.start();
			//Toast.makeText(getBaseContext(), dur + "", Toast.LENGTH_LONG)
			//		.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void Export_Midi() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("파일 출력");
		alert.setMessage("곡 제목을 입력해주세요");
		String[] seperate = Now_P.split("/");

		final EditText input = new EditText(this);
		input.setText(seperate[0]);

		alert.setView(input);

		alert.setNegativeButton("확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				String name = input.getText().toString();

				if (name.equals("")) {
					Toast.makeText(getBaseContext(), "제목을 다시 입력해주세요",
							Toast.LENGTH_LONG).show();
				} else {

					Make_Sound(-1);

					try {
						fs.writeToFile(name + ".mid", 1);
					} catch (IOException e) {
						e.printStackTrace();
					}

					Toast.makeText(getBaseContext(), "저장되었습니다",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		alert.setPositiveButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}

		});

		alert.show();

	}

	// Channel, Track

	// 02 채널/트랙 뷰
	private void Add_Channel(int index) {

		if (ll_channel.getChildCount() < 10) {
			// 채널은 트랙(을 감싸는 레이아웃) 과 함께 만들어진다
			// 채널 부분
			final LinearLayout ll_ch_x = new LinearLayout(MainActivity.this);
			final LinearLayout ll_ch_y = new LinearLayout(MainActivity.this);
			ll_ch_y.setOrientation(LinearLayout.VERTICAL);
			final LinearLayout ll_ch_z = new LinearLayout(MainActivity.this);

			final TextView vol = new TextView(MainActivity.this);
			final TextView plus = new TextView(MainActivity.this);
			final TextView minus = new TextView(MainActivity.this);
			final TextView ch = new TextView(MainActivity.this);

			ll_ch_x.addView(ch); // 가운데 텍스트뷰를 넣는다
			ll_ch_x.addView(vol); // 가운데 텍스트뷰를 넣는다
			ll_ch_y.addView(plus); // 가운데 텍스트뷰를 넣는다
			ll_ch_y.addView(minus); // 가운데 텍스트뷰를 넣는다
			ll_ch_z.addView(ll_ch_x);
			ll_ch_z.addView(ll_ch_y);
			ll_channel.addView(ll_ch_z, index);
			ll_ch_y.setGravity(Gravity.RIGHT);
			ll_ch_x.setGravity(Gravity.CENTER);
			ll_ch_z.setGravity(Gravity.CENTER);

			ch.setTextColor(Color.WHITE);
			vol.setTextColor(Color.GREEN);
			vol.setBackgroundColor(Color.GRAY);
			plus.setText("+");
			minus.setText("-");
			ch.setWidth(170);
			vol.setWidth(80);
			plus.setWidth(50);
			minus.setWidth(50);
			ch.setHeight(122);
			plus.setHeight(61);
			minus.setHeight(61);
			ch.setTextSize(15);
			vol.setTextSize(15);
			plus.setTextSize(35);
			minus.setTextSize(45);
			plus.setPadding(-35, -35, -35, -45);
			minus.setPadding(-45, -65, -45, -55);
			ch.setGravity(Gravity.CENTER);
			vol.setGravity(Gravity.CENTER);
			plus.setGravity(Gravity.CENTER);
			minus.setGravity(Gravity.CENTER);

			// 트랙(감싸는) 부분
			LinearLayout ll_t = new LinearLayout(MainActivity.this);
			int width = LayoutParams.WRAP_CONTENT;
			int height = LayoutParams.WRAP_CONTENT;
			LayoutParams params = new LayoutParams(width, height);
			ll_t.setLayoutParams(params);

			ll_tracks.addView(ll_t, index);

			int[] a = new int[2];
			int r = 0, g = 0, b = 0, inst = 0;
			int play = 0;

			for (int i = 0; i < 2; i++) {
				Random random = new Random();
				r = random.nextInt(256);
				g = random.nextInt(256);
				b = random.nextInt(256);
				inst = random.nextInt(129);
				play = random.nextInt(19)+1;
			}

			GradientDrawable gd1 = new GradientDrawable();
			a[0] = Color.argb(120, r, g, b);
			a[1] = Color.argb(240, r, g, b);
			gd1.setColors(a);
			gd1.setStroke(4, Color.rgb(r, g, b));

			ll_ch_z.setBackground(gd1);
			
			for(int i = 0; i < Inst_Order.size(); i ++){
				if(INT(Inst_Order.get(i)) == inst){
					
				}
			}

			
			String key_s = String.valueOf(Hash_Key);
			Hash_Order.add(index, key_s);
			Inst_Order.add(index, Random_Inst());
			Vol_Order.add(index, "6");
			Hash_Key++;
			Track_State = 0;

			listMap.put(key_s, new ArrayList<String>());
			ArrayList<String> list = listMap.get(key_s);

			list.clear();
			list.add(chordresult);
			list.add(play+"-"+play+"-"+play+"-"+play);
			list.add("4-4-4-4");
			list.add("0-0-0-0");

			for (int i = 0; i < 6; i++)
				list.add("0");

			Track_Color.add(index, String.valueOf(Color.rgb(r, g, b)));
			Add_Track(ll_t, 0);
			Shadow_CNT(index, 0);

			ch.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					{
						int c_p = ll_channel.indexOfChild(ll_ch_z);

						if (c_p != Channel_Pressed)
							Channel_State = 0;

						Channel_Pressed = c_p;
						Track_Pressed = (10 * c_p) + 0;

						if(ch.getCurrentTextColor() == Color.BLACK){
							Channel_Edit();
						}
						
						Shadow_CNT(c_p, 0);
				}}
			});
			
			vol.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {

					if (vol.getText().equals("ON")) {
						vol.setText("OFF");
						vol.setTextColor(Color.RED);
					}

					else if (vol.getText().equals("OFF")) {
						vol.setText("ON");
						vol.setTextColor(Color.GREEN);
					}

				}
			});

			plus.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {

					int c_p = ll_channel.indexOfChild(ll_ch_z);
					if (c_p != Channel_Pressed) {
						Channel_State = 0;
					}

					Channel_Pressed = c_p;
					Channel_State++;
					Add_Channel(c_p + Channel_State);
				}
			});

			minus.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {

					int c_p = ll_channel.indexOfChild(ll_ch_z);

					if (((TextView) ((ViewGroup) ((ViewGroup) ll_channel
							.getChildAt(c_p)).getChildAt(0)).getChildAt(0))
							.getCurrentTextColor() == Color.BLACK) {

						if (c_p == 0 && ll_channel.getChildCount() > 1)
							Shadow_CNT(1, 0);
						else
							Shadow_CNT(0, 0);
					}

					Channel_State = 0;
					Channel_Pressed = 0;
					listMap.remove(Hash_Order.get(c_p));
					Track_Color.remove(c_p);
					Hash_Order.remove(c_p);
					Inst_Order.remove(c_p);
					Vol_Order.remove(c_p);
					
					ll_channel.removeViews(c_p, 1);
					ll_tracks.removeViews(c_p, 1);
					F5cnt(cnttxt);

					if (ll_channel.getChildCount() == 0) {
						Track_Pressed = 0;
						Track_State = 0;
						Channel_Pressed = 0;
						Channel_State = 0;
						exp_cnt.setVisibility(View.VISIBLE);
					}

				}
			});
			
			F5cnt(cnttxt);
			vol.setText("ON");
		}

		else {
			Toast toast = Toast.makeText(this, "만들 수 있는 최대 갯수입니다.",
					Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private void Add_Track(final LinearLayout L, int index) {

		final LinearLayout ll_t_x = new LinearLayout(MainActivity.this);
		final LinearLayout ll_t_y = new LinearLayout(MainActivity.this);
		ll_t_y.setOrientation(LinearLayout.VERTICAL);
		final LinearLayout ll_t_z = new LinearLayout(MainActivity.this);

		final TextView track = new TextView(MainActivity.this);
		final TextView plus = new TextView(MainActivity.this);
		final TextView minus = new TextView(MainActivity.this);

		ll_t_x.addView(track); // 가운데 텍스트뷰를 넣는다
		ll_t_y.addView(plus); // 가운데 텍스트뷰를 넣는다
		ll_t_y.addView(minus); // 가운데 텍스트뷰를 넣는다
		ll_t_z.addView(ll_t_x);
		ll_t_z.addView(ll_t_y);
		L.addView(ll_t_z, index);

		int ioc = ll_tracks.indexOfChild(L);
		int tc = Integer.parseInt(Track_Color.get(ioc));

		GradientDrawable gd2 = new GradientDrawable();
		gd2.setColor(tc);
		gd2.setStroke(5, tc, 5, 5);
		gd2.setCornerRadius((float) 3.0);
		ll_t_x.setBackground(gd2);

		GradientDrawable gd3 = new GradientDrawable();
		gd3.setColor(tc);
		gd3.setStroke(5, tc, 5, 5);
		gd3.setCornerRadius((float) 3.0);
		ll_t_y.setBackground(gd3);

		track.setTextSize(10);
		track.setTextColor(Color.WHITE);
		plus.setText("+");
		minus.setText("-");
		track.setWidth(180);
		plus.setWidth(60);
		minus.setWidth(60);
		track.setHeight(122);
		plus.setHeight(61);
		minus.setHeight(61);
		plus.setTextSize(35);
		minus.setTextSize(45);
		plus.setPadding(-35, -35, -35, -35);
		minus.setPadding(-45, -65, -45, -45);
		track.setGravity(Gravity.CENTER);
		plus.setGravity(Gravity.CENTER);
		minus.setGravity(Gravity.CENTER);

		F5cnt(cnttxt);
		
		plus.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (L.getChildCount() < 10) {
					int c_p = ll_tracks.indexOfChild(L);
					if ((10 * c_p) + L.indexOfChild(ll_t_z) != Track_Pressed)
						Track_State = 0;

					Track_Pressed = (10 * c_p) + L.indexOfChild(ll_t_z);
					
					final ArrayList<String> list = listMap.get(Hash_Order.get(c_p));

					Random random = new Random();
					final int play = random.nextInt(19)+1;
					
					if(L.indexOfChild(ll_t_z) == 0){
						
						AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

						alert.setTitle("어느 곳에 트랙을 추가하시겠습니까?");
						alert.setNegativeButton("앞", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								Track_State = 0;
								list.add(0, chordresult);
								list.add(1, play+"-"+play+"-"+play+"-"+play);
								list.add(2, "4-4-4-4");
								list.add(3, "0-0-0-0");

								for (int i = 0; i < 6; i++)
									list.add(4 + i, "0");

								Add_Track(L, 0);
							}
						});
						alert.setPositiveButton("뒤", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {

								Track_State++;
								int next_dest = L.indexOfChild(ll_t_z) + Track_State;
								list.add((next_dest * 10), chordresult);
								list.add((next_dest * 10 + 1), play+"-"+play+"-"+play+"-"+play);
								list.add((next_dest * 10 + 2), "4-4-4-4");
								list.add((next_dest * 10 + 3), "0-0-0-0");

								for (int i = 0; i < 6; i++)
									list.add((next_dest * 10 + (4 + i)), "0");

								Add_Track(L, next_dest);
							}
						});

						alert.show();
					}
					else{

						Track_State++;
						int next_dest = L.indexOfChild(ll_t_z) + Track_State;
						list.add((next_dest * 10), chordresult);
						list.add((next_dest * 10 + 1), play+"-"+play+"-"+play+"-"+play);
						list.add((next_dest * 10 + 2), "4-4-4-4");
						list.add((next_dest * 10 + 3), "0-0-0-0");

						for (int i = 0; i < 6; i++)
							list.add((next_dest * 10 + (4 + i)), "0");

						Add_Track(L, next_dest);
					}
					
					
				}

				else {
					Toast toast = Toast.makeText(MainActivity.this,
							"만들 수 있는 최대 갯수입니다.", Toast.LENGTH_SHORT);
					toast.show();
				}
			}
		});

		minus.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int c_p = ll_tracks.indexOfChild(L);
				int dest = L.indexOfChild(ll_t_z);

				if (((TextView) ((ViewGroup) ((ViewGroup) ((ViewGroup) ll_tracks
						.getChildAt(c_p)).getChildAt(dest)).getChildAt(0))
						.getChildAt(0)).getCurrentTextColor() == Color.BLACK) {

					if (c_p == 0 && ((ViewGroup) ll_tracks.getChildAt(0)).getChildCount() == 1 && ll_channel.getChildCount() > 1)
						Shadow_CNT(1, 0);

					else
						Shadow_CNT(0, 0);

				}

				ArrayList<String> list = listMap.get(Hash_Order.get(c_p));

				for (int i = 0; i < 10; i++)
					list.remove(dest * 10);

				L.removeViews(dest, 1);
				Track_State = 0;
				F5cnt(cnttxt);

				if (L.getChildCount() == 0) {
					Channel_Pressed = 0;
					Channel_State = 0;
					listMap.remove(Hash_Order.get(c_p));
					Track_Color.remove(c_p);
					Hash_Order.remove(c_p);
					Inst_Order.remove(c_p);
					Vol_Order.remove(c_p);

					ll_channel.removeViews(c_p, 1);
					ll_tracks.removeViews(c_p, 1);
					F5cnt(cnttxt);

					if (ll_channel.getChildCount() == 0) {
						Track_Pressed = 0;
						Track_State = 0;
						Channel_Pressed = 0;
						Channel_State = 0;
						exp_cnt.setVisibility(View.VISIBLE);
					}
				}
			}
		});

		track.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				int c_p = ll_tracks.indexOfChild(L);
				int t_p = L.indexOfChild(ll_t_z);
				
				if ((10 * c_p) + t_p != Track_Pressed)
					Track_State = 0;
				
				Track_Pressed = (10 * c_p) + t_p;

				
				if(track.getCurrentTextColor() == Color.BLACK){
					Track_Edit();
				}
				Shadow_CNT(c_p, t_p);
				
				if(track.getText().equals("정보보기")){
					String key_s = String.valueOf(Hash_Order.get(c_p));
					ArrayList<String> list = listMap.get(key_s);
					
					String play = null, time = null, octave = null;
					
					//1,2,3에 대한 조건
					for(int t = 1; t <=3; t++){
						
						String [] s = list.get((t_p*10)+t).split("-");
						
						if(s[0].equals(s[1]) && s[1].equals(s[2]) && s[2].equals(s[3])){
							if(t == 1)
								play = (String) Play[INT(s[0])];
							if(t == 2)
								time = (String) Time[INT(s[0])];
							if(t == 3)
								octave = (String) Oct[INT(s[0])];
						}
						
						else{
							if(t == 1)
								play = "반주 " + list.get((t_p*10)+t);
							if(t == 2)
								time = "박자 " + list.get((t_p*10)+t);
							if(t == 3)
								octave = "옥타브 " + list.get((t_p*10)+t);
						}
							
					}
					
					String total =	 play + "\r\n" +
									 time + "\r\n" +
									 octave + "\r\n" +
									 Tempo[INT(list.get((t_p*10)+4))+2] + "\r\n" +
									 Key[INT(list.get((t_p*10)+5))+11] + "\r\n" +
									 Loop[INT(list.get((t_p*10)+6))];

					Toast.makeText(getBaseContext(), total, Toast.LENGTH_LONG)
							.show();
					
				}
			}
		});

	}


	// DB

	private void Db_Save() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("저장하기");

		alert.setNegativeButton("새로 저장", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Db_Save_New();
			}
		});

		alert.setNeutralButton("덮어 쓰기", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Db_Save_Re();
			}
		});

		alert.setPositiveButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});

		alert.show();

	}

	private void Db_Save_New() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		final DBHandler dbhandler = DBHandler.open(this);

		alert.setTitle("새로 저장");
		alert.setMessage("프로젝트 제목을 입력해주세요");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		dbhandler.selectAll();

		alert.setView(input);

		alert.setNegativeButton("확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				String name = input.getText().toString();

				if (name.equals("")) {
					Toast.makeText(getBaseContext(), "제목을 다시 입력해주세요",
							Toast.LENGTH_LONG).show();
				} else {

					Now_P = name;
					String format = new String("yyyyMMddHHmmss");
					SimpleDateFormat sdf = new SimpleDateFormat(format,
							Locale.KOREA);
					name += "/" + sdf.format(new Date());
					// 해쉬를 하나의 스트링으로 합치기

					String save = "";
					for (int i = 0; i < ll_channel.getChildCount(); i++) {

						ArrayList<String> list = listMap.get(Hash_Order.get(i));

						for (int t = 0; t < list.size(); t++) {
							save += list.get(t);
							if (t < list.size() - 1)
								save += "!";
						}

						if (i < ll_channel.getChildCount() - 1)
							save += "N";
					}

					save += "&";

					// 색상,악기,볼륨(채널설정),전체설정값 넣기

					for (int i = 0; i < ll_channel.getChildCount(); i++) {
						save += Inst_Order.get(i);
						if (i < ll_channel.getChildCount() - 1)
							save += "!";
					}

					save += "@";

					for (int i = 0; i < ll_channel.getChildCount(); i++) {
						save += Vol_Order.get(i);
						if (i < ll_channel.getChildCount() - 1)
							save += "!";
					}

					save += "@";

					for (int i = 0; i < 3; i++) {
						save += String.valueOf(all_setting[i]);
						if (i < 2)
							save += "!";
					}

					if (DB_Count == 0) {
						dbhandler.insert(name, save);
						dbhandler.insert(name, save);
						DB.add(name);
						DB_Count++;
					}

					else {
						dbhandler.insert(name, save);
						DB.add(name);
					}

					Toast.makeText(getBaseContext(), "저장 되었습니다",
							Toast.LENGTH_LONG).show();
				}

			}
		});

		alert.setPositiveButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}

		});

		alert.show();

	}

	private void Db_Save_Re() {

		final DBHandler dbhandler = DBHandler.open(this);
		final Cursor c = dbhandler.selectAll();

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("덮어쓰기");

		alert.setSingleChoiceItems(DB.toArray(new String[DB.size()]), 0,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

					}
				});

		alert.setNegativeButton("확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				int index = ((AlertDialog) dialog).getListView()
						.getCheckedItemPosition();
				c.moveToFirst();
				c.moveToPosition(index + 1);
				int over = c.getInt(c.getColumnIndex("_id"));
				Now_P = c.getString(c.getColumnIndex("_name"));

				String format = new String("yyyyMMddHHmmss");
				SimpleDateFormat sdf = new SimpleDateFormat(format,
						Locale.KOREA);
				String re_name = Now_P + "/" + sdf.format(new Date());

				String save = "";
				for (int i = 0; i < ll_channel.getChildCount(); i++) {

					ArrayList<String> list = listMap.get(Hash_Order.get(i));

					for (int t = 0; t < list.size(); t++) {
						save += list.get(t);
						if (t < list.size() - 1)
							save += "!";
					}

					if (i < ll_channel.getChildCount() - 1)
						save += "N";
				}

				save += "&";

				// 악기,볼륨(채널설정),전체설정값 넣기

				for (int i = 0; i < ll_channel.getChildCount(); i++) {
					save += Inst_Order.get(i);
					if (i < ll_channel.getChildCount() - 1)
						save += "!";
				}

				save += "@";

				for (int i = 0; i < ll_channel.getChildCount(); i++) {
					save += Vol_Order.get(i);
					if (i < ll_channel.getChildCount() - 1)
						save += "!";
				}

				save += "@";

				for (int i = 0; i < 3; i++) {
					save += String.valueOf(all_setting[i]);
					if (i < 2)
						save += "!";
				}

				dbhandler.re_insert(over, save);
				DB.set(index, re_name);

				Toast.makeText(getBaseContext(), "저장 되었습니다", Toast.LENGTH_LONG)
						.show();
			}
		});

		alert.setPositiveButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		});

		alert.show();
	}

	private void Db_Delete() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("삭제하기");
		// alert.setMessage("");

		// Set an EditText view to get user input

		alert.setNegativeButton("개별 삭제", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Db_Delete_One();
			}
		});

		alert.setNeutralButton("전체 삭제", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Db_Delete_All();
			}
		});

		alert.setPositiveButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});

		alert.show();

	}

	private void Db_Delete_One() {

		final DBHandler dbhandler = DBHandler.open(this);
		final Cursor c = dbhandler.selectAll();

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("개별 삭제");

		alert.setSingleChoiceItems(DB.toArray(new String[DB.size()]), 0,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

					}
				});

		alert.setNegativeButton("확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				int index = ((AlertDialog) dialog).getListView()
						.getCheckedItemPosition();
				c.moveToFirst();
				c.moveToPosition(index + 1);
				int del = c.getInt(c.getColumnIndex("_id"));
				dbhandler.delete(del);
				DB.remove(index);

				Toast.makeText(getBaseContext(), "삭제 되었습니다", Toast.LENGTH_LONG)
						.show();
			}
		});

		alert.setPositiveButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		});

		alert.show();

	}

	private void Db_Delete_All() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		final DBHandler dbhandler = DBHandler.open(this);

		alert.setTitle("전체 삭제");
		alert.setMessage("진행하시겠습니까?");

		// Set an EditText view to get user input
		dbhandler.selectAll();

		alert.setNegativeButton("확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				DB_Count = 0;
				DB.clear();
				dbhandler.deleteAll();
				dbhandler.close();
				Toast.makeText(getBaseContext(), "전체 삭제 되었습니다",
						Toast.LENGTH_LONG).show();
			}
		});

		alert.setPositiveButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}

		});

		alert.show();

	}

	private void Db_Contents() {

		DBHandler dbhandler = DBHandler.open(this);
		final Cursor c = dbhandler.selectAll();

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("불러오기");

		alert.setSingleChoiceItems(DB.toArray(new String[DB.size()]), 0,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

					}
				});

		alert.setNegativeButton("확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				int index = ((AlertDialog) dialog).getListView()
						.getCheckedItemPosition();

				c.moveToFirst();
				c.moveToPosition(index + 1);

				String save = c.getString(c.getColumnIndex("_contents"));
				Now_P = c.getString(c.getColumnIndex("_name"));

				// Zero 쪼갤 시 2개로 나뉘어 진다.
				String[] Zero = save.split("&");

				String[] First = Zero[0].split("N");
				String[] Second;
				String[] Third = Zero[1].split("@");

				listMap.clear();
				ll_channel.removeAllViews();
				ll_tracks.removeAllViews();

				setReset();

				String[] chord = First[0].split("!");
				String load_chord = chord[0];
				String[] re_chord = load_chord.split("-");

				for (int c = 0; c < 4; c++) {
					Layout_Chord_State(re_chord[c]);
					Count++;
				}

				Go_Next_Activity();
				Count = 5;
				mc1_1.setVisibility(View.GONE);
				mc1_2.setVisibility(View.VISIBLE);
				mc4_1.setVisibility(View.GONE);
				mc4_2.setVisibility(View.VISIBLE);
				select_view.setText("코드");

				for (int i = 0; i < First.length; i++) {
					Add_Channel(i);
					Second = First[i].split("!");
					listMap.put(String.valueOf(i), new ArrayList<String>());

					for (int t = 0; t < Second.length; t++) {
						if (t > 9 && t % 10 == 0) {
							Add_Track((LinearLayout) ll_tracks.getChildAt(i), 0);
						}
					}
				}

				listMap.clear();

				// 제대로 값 넣어주는 부분
				for (int i = 0; i < First.length; i++) {

					Second = First[i].split("!");
					listMap.put(String.valueOf(i), new ArrayList<String>());
					ArrayList<String> list = listMap.get(String.valueOf(i));

					for (int t = 0; t < Second.length; t++) {
						list.add(Second[t]);

						if (t % 10 == 1 && Second[t].equals("0-0-0-0")) {

							((ViewGroup) ((ViewGroup) ll_tracks.getChildAt(i))
									.getChildAt((t / 10))).getChildAt(0)
									.setBackgroundColor(Color.TRANSPARENT);

							((ViewGroup) ((ViewGroup) ll_tracks.getChildAt(i))
									.getChildAt((t / 10))).getChildAt(1)
									.setBackgroundColor(Color.TRANSPARENT);

							GradientDrawable gd5 = new GradientDrawable();
							gd5.setColor(Color.argb(255, 255, 255, 255));
							gd5.setStroke(5,
									Integer.parseInt(Track_Color.get(i)), 5, 5);
							gd5.setCornerRadius((float) 3.0);
							((ViewGroup) ll_tracks.getChildAt(i)).getChildAt(
									(t / 10)).setBackground(gd5);

						}

						if (t % 10 == 0 ) {
								int tempo = INT(Second[t+4]);
								int loop = INT(Second[t+6]);
								Change_Size_Track(i, t / 10, tempo, loop);
						}
					}
				}

				String[] i_o = Third[0].split("!");
				String[] v_o = Third[1].split("!");
				String[] a_s = Third[2].split("!");

				for (int i = 0; i < ll_channel.getChildCount(); i++) {
					Inst_Order.set(i, i_o[i]);
					Vol_Order.set(i, v_o[i]);
				}

				for (int i = 0; i < 3; i++) {
					all_setting[i] = Integer.parseInt(a_s[i]);
				}

			}
		});

		alert.setPositiveButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		});

		alert.show();

	}

	// 코드 생성,수정, 기족 곡 알아보기

	// 코드 생성 뷰
	private void Layout_Chord_State(String A) {

		// 이 곳에서 코드에 대해 개별적으로 생성할 수 있는 부분이 만들어짐
		// 따라서 여기서 ArrayList에 값을 넣는다.

		if (Count == 0) {
			mc2.setVisibility(View.GONE);
			chordresult = A;
			textarray = (A.replaceAll("/", "")).replaceAll("0", "");
		} else {
			TextView bar = new TextView(MainActivity.this);
			Make_TextView(mc3, bar, 10 + Count, 30, 0, 0, 0, 0);
			bar.setText(" - ");
			chordresult += "-" + A;
			textarray += "-" + (A.replaceAll("/", "")).replaceAll("0", "");
		}

		final TextView chord_n = new TextView(MainActivity.this);
		Make_TextView(mc3, chord_n, 1 + Count, 30, 0, 0, 0, 0);
		chord_n.setText((A.replaceAll("/", "")).replaceAll("0", ""));
		
		for(int i = 0; i < chordlist.size(); i++){
			if(chordlist.get(i).equals(chordresult)){
				y_result.add(singerlist.get(i) + " - " +titlelist.get(i));
			}
		}
		
	}

	private void setButton() {
		First_Button();
		N_Button();
	}

	private void First_Button() {
		Count = 0;
		final String A = "";
		Text(RandomText, A);
		Make_Button(RandomText);
	}

	private void N_Button() {
		try {
			for (int i = 0; i < codeBt.length; i++) {
				final String A = RandomText.get(i);

				codeBt[i].setOnTouchListener(new View.OnTouchListener() {
					public boolean onTouch(View v, MotionEvent event) {
						int action = event.getAction();
						int downPointerIndex = -1;

						if (action == MotionEvent.ACTION_DOWN)
							downPointerIndex = 0;

						if (downPointerIndex >= 0) {
							Individual_Sound(A);
							Layout_Chord_State(A);
							Text(newRandomText, A);

							if (newRandomText.size() == 0) {
								Go_Next_Activity();
							} else
								Make_Button(newRandomText);
						}
						return false;
					}
				});
			}
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
		}
	}

	private void Text(ArrayList<String> r, String A) {
		r.clear();
		Search_Chord(r, A);

		if (r.equals(newRandomText)) {
			Count++;
			RandomText.clear();
		}

		HashSet<String> hs = new HashSet<String>(r);
		Iterator<String> it = hs.iterator();
		r.clear();
		while (it.hasNext())
			r.add(it.next());

		Random(r);
	}

	private void Random(ArrayList<String> r) {
		Random ra = new Random();
		for (int i = 0; i < r.size(); i++) {
			int rv = ra.nextInt(r.size());
			r.add(r.get(rv));
			r.remove(rv);
		}
	}

	private void Search_Chord(ArrayList<String> r, String A) {
		if (r.equals(RandomText)) {
			for (int i = 0; i < chordlist.size(); i++) {
				chordsearch.set(i, "O");
				String[] B = chordlist.get(i).split("-");
				RandomText.add(B[Count]);
			}
		}

		else if (r.equals(newRandomText)) {
			for (int j = 0; j < chordlist.size(); j++) {
				String[] B = chordlist.get(j).split("-");
				if (chordsearch.get(j).equals("O") && B[Count].equals(A)
						&& Count + 1 < B.length) {
					newRandomText.add(B[Count + 1]);
				} else {
					chordsearch.set(j, "X");
				}
			}

		}
	}

	private void Make_Button(ArrayList<String> r) {
		
		if (r.equals(newRandomText)) {
			for (int i = 0; i < codeBt.length; i++)
				mc1.removeView(codeBt[i]);
		}

		for (int i = 0; i < r.size(); i++) {
			if (r.size() > 4)
				r.remove(r.size() - 1);
		}
		
		if(Count==0){
			r.remove(r.size() - 1);
			r.remove(r.size() - 1);
		}

		Collections.sort(r);
		codeBt = new Button[r.size()];

		for (int i = 0; i < codeBt.length; i++) {
			RandomText.add(r.get(i));

			codeBt[i] = new Button(MainActivity.this);
			codeBt[i].setId(i);
			codeBt[i].setText((r.get(i).replaceAll("/", ""))
					.replaceAll("0", ""));
			codeBt[i].setTextSize(15);
			codeBt[i].setBackgroundResource(R.drawable.button);
			codeBt[i].setTextColor(Color.WHITE);

			mc1.addView(codeBt[i]);
			ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(
					codeBt[i].getLayoutParams());
			margin.setMargins(10, 0, 10, 0);
			codeBt[i].setLayoutParams(new LinearLayout.LayoutParams(margin));
		}

		if (r.equals(newRandomText))
			N_Button();
			

	}

	private void Go_Next_Activity() {
		for (int i = 0; i < codeBt.length; i++) {
			mc1.removeView(codeBt[i]);
		}
		
		mc1_1.setVisibility(View.VISIBLE);
	}

	private void Caution_Clear_Chord() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("코드 변경");
		alert.setMessage("주의! 저장된 코드는 초기화됩니다");

		alert.setNegativeButton("확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Clear_Chord();
			}
		});

		alert.setPositiveButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});

		alert.show();

	}

	private void Caution_Time_Signature() {

		String key_s = String.valueOf(Hash_Order.get(v_t_cs));
		final ArrayList<String> list = listMap.get(key_s);

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("지정된 박자의 합이 16이어야 합니다!");
		alert.setMessage("코드의 모든 박자를 4/4로 맞추시겠습니까?");

		alert.setNegativeButton("확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				list.set(v_t_ts + 2, "4-4-4-4");
				inf_ts = list.get(v_t_ts + 2).split("-");
				value_op3.setText("4/4");
				n_time = 0;

				Toast.makeText(getBaseContext(), "적용 되었습니다", Toast.LENGTH_LONG)
						.show();
			}
		});

		alert.setPositiveButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				Toast.makeText(getBaseContext(), "각 코드의 박자를 다시 설정해주세요", Toast.LENGTH_LONG)
						.show();

			}
		});

		alert.show();

	}

	private void Track_Edit(){
		if (ll_tracks.getChildCount() > 0) {
			s_c = -1;
			v_t_cs = Track_Pressed / 10;
			v_t_ts = (Track_Pressed % 10) * 10;

			String key_s = String.valueOf(Hash_Order.get(v_t_cs));
			ArrayList<String> list = listMap.get(key_s);

			inf_ch = list.get(v_t_ts).split("-");
			inf_ps = list.get(v_t_ts + 1).split("-");
			inf_ts = list.get(v_t_ts + 2).split("-");
			inf_oc = list.get(v_t_ts + 3).split("-");

			// 코드 옵션이므로
			op2.setText("빠르기");
			op3.setText("전조");
			op4.setText("반복");

			value_op2.setText(Tempo[INT(list.get(v_t_ts + 4)) + 2]);
			value_op3.setTextSize(20);
			value_op3.setText(Key[INT(list.get(v_t_ts + 5)) + 11]);
			value_op4.setText(Loop[INT(list.get(v_t_ts + 6))]);

			// 나머지 화면 설정
			shap.setText("코드");
			shchn.setText("채널" + (v_t_cs + 1));
			shtrk.setText("트랙" + ((v_t_ts / 10) + 1));

			make_chord.setVisibility(View.GONE);
			exp_cnt.setVisibility(View.GONE);
			cnt.setVisibility(View.GONE);
			edit_chord.setVisibility(View.VISIBLE);
			confirm_t.setVisibility(View.INVISIBLE);
			menu.setVisibility(View.GONE);
			select_view.setText("트랙 설정");
			View_State = 2;
			//Double_Count = 0;
			Shadow_CO(shtrk);
		}
	}
	
	private void Channel_Edit(){
		if (ll_tracks.getChildCount() > 0) {
			s_c = -1;
			v_t_cs = Track_Pressed / 10;
			v_t_ts = (Track_Pressed % 10) * 10;

			String key_s = String.valueOf(Hash_Order.get(v_t_cs));
			ArrayList<String> list = listMap.get(key_s);

			inf_ch = list.get(v_t_ts).split("-");
			inf_ps = list.get(v_t_ts + 1).split("-");
			inf_ts = list.get(v_t_ts + 2).split("-");
			inf_oc = list.get(v_t_ts + 3).split("-");

			s_c = -1;
			shap.setText("코드");
			shchn.setText("채널" + (v_t_cs + 1));
			shtrk.setText("트랙" + ((v_t_ts / 10) + 1));

			value_op2.setTextSize(15);
			value_op3.setTextSize(25);
			left_op4.setText("");
			right_op4.setText("");

			op2.setText("악기");
			op3.setText("음량");
			op4.setText("텍스트");
			value_op2.setText(Inst[INT(Inst_Order.get(v_t_cs))]);
			value_op3.setText(Vol[INT(Vol_Order.get(v_t_cs))]);
			
			if(cnttxt == 0)
				value_op4.setText("채널/트랙");

			else if(cnttxt == 1)
				value_op4.setText("악기/정보");
			
			select_view.setText("채널 설정");
			cnt.setVisibility(View.GONE);
			edit_chord.setVisibility(View.VISIBLE);
			confirm_t.setVisibility(View.INVISIBLE);
			View_State = 2;
			Shadow_CO(shchn);
		}
	}
	
	private void You_Tube(){
		youtube = YouTubeIntents.createSearchIntent(this, USER_ID);
	}
	
	// 기타 편의를 위한 함수
	private void Make_TextView(LinearLayout L, TextView T, int I, int S,
			int m1, int m2, int m3, int m4) {
		L.addView(T);
		T.setId(I);
		T.setTextSize(S); // TypedValue.COMPLEX_UNIT_DIP
		T.setGravity(Gravity.CENTER);
		ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(
				T.getLayoutParams());
		margin.setMargins(m1, m2, m3, m4);
		T.setLayoutParams(new LinearLayout.LayoutParams(margin));
	}
	private int INT(String S) {
		int num = Integer.parseInt(S);
		return num;
	}
	private void Change_Size_Track(int ch, int tr, int t, int l) {
		if (t < 0) {
			t = t * 2;
		}
		int s = (180 / 3) * (3 - t);
		LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(s
				+ ((s + 60) * l), 122);

		((ViewGroup) ((ViewGroup) ll_tracks.getChildAt(ch)).getChildAt(tr))
				.getChildAt(0).setLayoutParams(Params1);
	}
	private void Paint_Track(ArrayList<String> list) {
		((ViewGroup) ((ViewGroup) ll_tracks.getChildAt(v_t_cs))
				.getChildAt((v_t_ts / 10))).getChildAt(0).setBackgroundColor(
				Color.TRANSPARENT);

		((ViewGroup) ((ViewGroup) ll_tracks.getChildAt(v_t_cs))
				.getChildAt((v_t_ts / 10))).getChildAt(1).setBackgroundColor(
				Color.TRANSPARENT);

		if (list.get(v_t_ts + 1).equals("0-0-0-0")) {
			GradientDrawable gd = new GradientDrawable();
			gd.setColor(Color.argb(255, 255, 255, 255));
			gd.setStroke(5, Integer.parseInt(Track_Color.get(v_t_cs)), 5, 5);
			gd.setCornerRadius((float) 3.0);
			((ViewGroup) ll_tracks.getChildAt(v_t_cs))
					.getChildAt((v_t_ts / 10)).setBackground(gd);

		}

		else {
			GradientDrawable gd2 = new GradientDrawable();
			gd2.setColor(Integer.parseInt(Track_Color.get(v_t_cs)));
			gd2.setStroke(5, Integer.parseInt(Track_Color.get(v_t_cs)), 5, 5);
			gd2.setCornerRadius((float) 3.0);

			((ViewGroup) ((ViewGroup) ll_tracks.getChildAt(v_t_cs))
					.getChildAt((v_t_ts / 10))).getChildAt(0)
					.setBackground(gd2);

			GradientDrawable gd3 = new GradientDrawable();
			gd3.setColor(Integer.parseInt(Track_Color.get(v_t_cs)));
			gd3.setStroke(5, Integer.parseInt(Track_Color.get(v_t_cs)), 5, 5);
			gd3.setCornerRadius((float) 3.0);

			((ViewGroup) ((ViewGroup) ll_tracks.getChildAt(v_t_cs))
					.getChildAt((v_t_ts / 10))).getChildAt(1)
					.setBackground(gd3);

		}
	}
	private void Clear_Chord() {
		
		make_chord.setVisibility(View.VISIBLE);
		mc1_1.setVisibility(View.GONE);
		mc1_2.setVisibility(View.GONE);
		mc2.setVisibility(View.VISIBLE);
		mc4_1.setVisibility(View.VISIBLE);
		mc4_2.setVisibility(View.GONE);
		
		
		//for (int i = 0; i < codeBt.length; i++)
		//	mc1.removeView(codeBt[i]);
		
		if (Count > 0) {
			for (int i = 0; i < Count; i++)
				mc3.removeView((TextView) findViewById(i + 1));
			for (int i = 10; i < Count + 9; i++)
				mc3.removeView((TextView) findViewById(i + 1));
		}

		
		setButton();
		
		
		

	}
	private void Shadow_CNT(int c, int t) {

		// 다 지우기
		for (int i = 0; i < ll_tracks.getChildCount(); i++) {

			((TextView) ((ViewGroup) ((ViewGroup) ll_channel.getChildAt(i))
					.getChildAt(0)).getChildAt(0)).setTextColor(Color.WHITE);
			((TextView) ((ViewGroup) ((ViewGroup) ll_channel.getChildAt(i))
					.getChildAt(0)).getChildAt(0)).setShadowLayer(0, 0, 0,
					Color.WHITE);

			for (int j = 0; j < ((ViewGroup) ll_tracks.getChildAt(i))
					.getChildCount(); j++) {

				((TextView) ((ViewGroup) ((ViewGroup) ((ViewGroup) ll_tracks
						.getChildAt(i)).getChildAt(j)).getChildAt(0))
						.getChildAt(0)).setTextColor(Color.WHITE);

				((TextView) ((ViewGroup) ((ViewGroup) ((ViewGroup) ll_tracks
						.getChildAt(i)).getChildAt(j)).getChildAt(0))
						.getChildAt(0)).setShadowLayer(0, 0, 0, Color.WHITE);
			}
		}

		((TextView) ((ViewGroup) ((ViewGroup) ((ViewGroup) ll_tracks
				.getChildAt(c)).getChildAt(t)).getChildAt(0)).getChildAt(0))
				.setTextColor(Color.BLACK);

		((TextView) ((ViewGroup) ((ViewGroup) ((ViewGroup) ll_tracks
				.getChildAt(c)).getChildAt(t)).getChildAt(0)).getChildAt(0))
				.setShadowLayer(2, 0, 0, Color.WHITE);

		((TextView) ((ViewGroup) ((ViewGroup) ll_channel.getChildAt(c))
				.getChildAt(0)).getChildAt(0)).setTextColor(Color.BLACK);
		((TextView) ((ViewGroup) ((ViewGroup) ll_channel.getChildAt(c))
				.getChildAt(0)).getChildAt(0)).setShadowLayer(2, 0, 0,
				Color.WHITE);

	}
	private void Exception_Time_Signature() {

		int[] ets = new int[4];
		int sum = 0;

		for (int i = 0; i < 4; i++) {
			ets[i] = INT(inf_ts[i]);
			sum += ets[i];
		}

		if (sum != 16)
			n_time = 1;

		else
			n_time = 0;

	}
	private void Shadow_CO(TextView t) {
		// 다 지우기

		shap.setTextColor(Color.BLACK);
		shtrk.setTextColor(Color.BLACK);
		shchn.setTextColor(Color.BLACK);
		shall.setTextColor(Color.BLACK);

		shap.setShadowLayer(0, 0, 0, Color.WHITE);
		shtrk.setShadowLayer(0, 0, 0, Color.WHITE);
		shchn.setShadowLayer(0, 0, 0, Color.WHITE);
		shall.setShadowLayer(0, 0, 0, Color.WHITE);

		if (t != null) {
			t.setTextColor(INT(Track_Color.get(v_t_cs)));
			t.setShadowLayer(1, 0, 0, Color.GRAY);
		}

	}
	private String Random_Inst(){

		int inst = 0;

		for (int i = 0; i < 1; i++) {
			Random random = new Random();
			inst = random.nextInt(129);
		}
		
		for(int i = 0; i < Inst_Order.size(); i ++){
			if(INT(Inst_Order.get(i)) == inst)
				Random_Inst();
		}
		
		return String.valueOf(inst);
	}
	private void Click_Op(final TextView V, final String T, final CharSequence[] C,
			final ArrayList<String> S, final int CORT, final int Add){

		String [] temp;
		int C_I = 0;
		C_I = CORT;
		
		if(S != null){
			if(T.equals("반주 설정") || T.equals("박자 설정") || T.equals("옥타브 설정") ){
				temp = S.get(CORT).split("-");
				C_I = INT(temp[s_c]);
			}
			
			else
				C_I =  INT(S.get(CORT));
		}
		
			AlertDialog.Builder alert = new AlertDialog.Builder(
					MainActivity.this);
			alert.setTitle(T);
			alert.setSingleChoiceItems(C,C_I+Add,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							indexop = ((AlertDialog) dialog)
									.getListView()
									.getCheckedItemPosition();
						
						}
					});

			alert.setNegativeButton("확인",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

							int tof = 0;
							
							if(S != null) {
								
								if((!T.equals("반주 설정") || !T.equals("박자 설정") 
										|| !T.equals("옥타브 설정"))){
									
									if(T.equals("악기 설정")){
										for (int i = 0; i < Inst_Order.size(); i++){
											if(indexop == INT(Inst_Order.get(i))){
												if(indexop == 0){
												tof = 1;
												}
											}	
										}
										
										if(tof == 0)
											S.set(CORT,String.valueOf(indexop-Add));
										
										else if (tof == 1){
											Toast.makeText(getBaseContext(), 
													"드럼은 하나의 채널에서만 사용할 수 있습니다, " +
													"다른 악기를 선택해주세요", Toast.LENGTH_LONG)
											.show();
										}	
									}
									
									else
										S.set(CORT,String.valueOf(indexop-Add));
								}
								
								if(T.equals("반주 설정")){
									inf_ps[s_c] = String.valueOf(indexop-Add);

									String n_v_s = "";
									for (int i = 0; i < 4; i++) {
										n_v_s += inf_ps[i];
										if (i < 3)
											n_v_s += "-";
									}

									S.set(CORT, n_v_s);
									Paint_Track(S); 
								}
								
								
								
								if(T.equals("박자 설정")){
									inf_ts[s_c] = String.valueOf(indexop-Add);

									String n_v_s = "";
									for (int i = 0; i < 4; i++) {
										n_v_s += inf_ts[i];
										if (i < 3)
											n_v_s += "-";
									}
									S.set(CORT, n_v_s);

									Exception_Time_Signature();
								}
								
								
								if(T.equals("옥타브 설정")){
									inf_oc[s_c] = String.valueOf(indexop-Add);

									String n_v_s = "";
									for (int i = 0; i < 4; i++) {
										n_v_s += inf_oc[i];
										if (i < 3)
											n_v_s += "-";
									}

										S.set(CORT, n_v_s);
								}
								
					
							}
							
						

							else if(T.equals("전체 빠르기 설정")){
								all_setting[0] = indexop-Add;

								if(all_setting[0] >= 0)
									fs.header[13] = 64 + (all_setting[0]*2);
								else if (all_setting[0] < 0)
									fs.header[13] = 64 + (all_setting[0]);
							}

							else if(T.equals("전체 전조 설정"))
								all_setting[1] = indexop-Add;

							else if(T.equals("전체 반복 설정"))
								all_setting[2] = indexop-Add;
							
							
							if(T.equals("빠르기 설정"))
								Change_Size_Track(v_t_cs, v_t_ts / 10, 
										INT(S.get(v_t_ts+4)),  INT(S.get(v_t_ts+6)));

							if(tof != 1){
								V.setText(C[indexop]);
								Option_Sound(v_t_cs, v_t_ts/10);
							}
						}
					});

			alert.setPositiveButton("취소",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					});

			alert.show();
		
	}
	private void F5cnt(int state) {

		if (state == 0) {
			for (int i = 0; i < ll_tracks.getChildCount(); i++) {

				((TextView) ((ViewGroup) ((ViewGroup) ll_channel.getChildAt(i))
						.getChildAt(0)).getChildAt(0)).setText("채널" + (i + 1));
				((TextView) ((ViewGroup) ((ViewGroup) ll_channel.getChildAt(i))
						.getChildAt(0)).getChildAt(0)).setTextSize(30);

				for (int j = 0; j < ((ViewGroup) ll_tracks.getChildAt(i))
						.getChildCount(); j++) {

					((TextView) ((ViewGroup) ((ViewGroup) ((ViewGroup) ll_tracks
							.getChildAt(i)).getChildAt(j)).getChildAt(0))
							.getChildAt(0)).setText("트랙" + (j + 1));

					((TextView) ((ViewGroup) ((ViewGroup) ((ViewGroup) ll_tracks
							.getChildAt(i)).getChildAt(j)).getChildAt(0))
							.getChildAt(0)).setTextSize(20);
				}
			}
		}
		
		else if (state == 1){
			for (int i = 0; i < ll_tracks.getChildCount(); i++) {

				((TextView) ((ViewGroup) ((ViewGroup) ll_channel.getChildAt(i))
						.getChildAt(0)).getChildAt(0)).setText(Inst[INT(Inst_Order.get(i))] 
								+"\r\n음량: " + Vol[INT(Vol_Order.get(i))]);
				
				((TextView) ((ViewGroup) ((ViewGroup) ll_channel.getChildAt(i))
						.getChildAt(0)).getChildAt(0)).setTextSize(15);

				for (int j = 0; j < ((ViewGroup) ll_tracks.getChildAt(i))
						.getChildCount(); j++) {
					

					String key_s = String.valueOf(Hash_Order.get(i));
					ArrayList<String> list = listMap.get(key_s);
					
					String play = null, time = null, octave = null;
					
					//1,2,3에 대한 조건
					for(int t = 1; t <=3; t++){
						String [] s = list.get((j*10)+t).split("-");
						
						if(s[0].equals(s[1]) && s[1].equals(s[2]) && s[2].equals(s[3])){
							if(t == 1){
								if(Inst_Order.get(i).equals("0"))
									play = (String) Drum[INT(s[0])];
								if(!Inst_Order.get(i).equals("0"))
									play = (String) Play[INT(s[0])];
							}
							if(t == 2)
								time = (String) Time[INT(s[0])];
							if(t == 3)
								octave = (String) Oct[INT(s[0])];
						}
						
						else{
							if(t == 1)
								play = "반주 " + list.get((j*10)+t);
							if(t == 2)
								time = "박자 " + list.get((j*10)+t);
							if(t == 3)
								octave = "옥타브 " + list.get((j*10)+t);
						}
							
					}
					
					String total = "";
					
					if(list.get((j*10)+4).equals("2") && list.get((j*10)+6).equals("0")){
						total = "정보보기";					}
					
					else{
						total =	 play + "\r\n" +
									 time + "\r\n" +
									 octave + "\r\n" +
									 Tempo[INT(list.get((j*10)+4))+2] + "\r\n" +
									 Key[INT(list.get((j*10)+5))+11] + "\r\n" +
									 Loop[INT(list.get((j*10)+6))];
					}
					
					

					((TextView) ((ViewGroup) ((ViewGroup) ((ViewGroup) ll_tracks
							.getChildAt(i)).getChildAt(j)).getChildAt(0))
							.getChildAt(0)).setText(total);

					((TextView) ((ViewGroup) ((ViewGroup) ((ViewGroup) ll_tracks
							.getChildAt(i)).getChildAt(j)).getChildAt(0))
							.getChildAt(0)).setTextSize(10);
				}
			}
		}
	}
	private int[] Sort(){
		
		int result[] = new int[2];
		result[0] = 0; result[1] = 0;
		int track_sum = 0;
		int tempo = 0;
		int loop = 0;

		//40, 30, 20, 15, 10
		for(int i = 0; i < ll_channel.getChildCount(); i++){
			track_sum = 0;
			String key_s = String.valueOf(Hash_Order.get(i));
			ArrayList<String> list = listMap.get(key_s);
			
			for (int t = 0; t < list.size() / 10; t++) {
				tempo = Integer.parseInt(list.get(4 + (10 * t)));
				loop = Integer.parseInt(list.get(6 + (10 * t)));
				
				if(tempo == -2)
					tempo = 40;
				if(tempo == -1)
					tempo = 30;
				if(tempo == 0)
					tempo = 20;
				if(tempo == 1)
					tempo = 15;
				if(tempo == 2)
					tempo = 10;
				
				track_sum += tempo * (loop+1); 
			}

			
			if(track_sum > result[0]){
				result[0] = track_sum;
				result[1] = i;
			}
		}	
			
		return result;
	}
	
	// 악보
	private void Make_Score() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		String[] seperate = Now_P.split("/");

		alert.setTitle("악보 출력");
		alert.setMessage("악보 제목을 입력해주세요");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		input.setText(seperate[0]);
		
		alert.setView(input);
		alert.setNegativeButton("확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				String name = input.getText().toString();

				if (name.equals("")) {
					Toast.makeText(getBaseContext(), "제목을 다시 입력해주세요",
							Toast.LENGTH_LONG).show();
				} else {
					

					if(Score == 0){
						int[] data = { -1, 4, 0, 0, 0, 0, 0 };
						ps.style(fs, 0, "C/0/0/0/0", data);
						try {
							fs.writeToFile(name+".mid", 0);
							Create_Score(name, "Test");
						} catch (IOException e) {
							e.printStackTrace();
						}

						Score++;
					}


					for(int i = 0; i < ll_channel.getChildCount(); i++){
						
						Make_Sound(i);


					try {
						fs.writeToFile(name+".mid", 0);
						Create_Score(name, name + " " + Inst[INT(Inst_Order.get(i))]);
					} catch (IOException e) {
						e.printStackTrace();
					}


					}

					Toast.makeText(getBaseContext(), "저장 되었습니다",
							Toast.LENGTH_LONG).show();
					
				}

			}
		});

		alert.setPositiveButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}

		});

		alert.show();
	}

	private void Create_Score(String Midi_Name, String Score_Name) {

		ClefSymbol.LoadImages(this);
		TimeSigSymbol.LoadImages(this);
		MidiPlayer.LoadImages(this);

		String Filename = Midi_Name+".mid";
		//String dir = getDir("", MODE_PRIVATE).getAbsolutePath();
		//File file = new File(dir, Filename);
		
		String dir = getDir("", Context.MODE_PRIVATE).getAbsolutePath();
		File file = new File(dir + File.separator + Filename);

		byte[] data = null;

		try {
			FileInputStream fis = new FileInputStream(file);
			int readcount = (int) file.length();
			data = new byte[readcount];
			fis.read(data);
			fis.close();
			midifile = new MidiFile(data, Score_Name);
		} catch (Exception e) {
			e.printStackTrace();
		}

		options = new MidiOptions(midifile);
		createView();
		createSheetMusic(options);
		saveAsImages(Score_Name);

	}

	private void createView() {
		layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		player = new MidiPlayer(this);
		piano = new Piano(this);
		layout.addView(player);
		layout.addView(piano);
		layout.setVisibility(View.GONE);
	}

	private void createSheetMusic(MidiOptions options) {
		sheet = new SheetMusic(this);
		sheet.init(midifile, options);
		sheet.setPlayer(player);
		layout.addView(sheet);
		sheet.callOnDraw();
	}

	private void saveAsImages(String name) {
		String filename = name;
		//try {
			//filename = URLEncoder.encode(name, "UTF8");
		//} catch (UnsupportedEncodingException e) {
		//}
		if (!options.scrollVert) {
			options.scrollVert = true;
			createSheetMusic(options);
		}
		try {
			int numpages = sheet.GetTotalPages();
			for (int page = 1; page <= numpages; page++) {
				Bitmap image = Bitmap.createBitmap(SheetMusic.PageWidth + 40,
						SheetMusic.PageHeight + 40, Bitmap.Config.ARGB_8888);
				Canvas imageCanvas = new Canvas(image);
				sheet.DrawPage(imageCanvas, page);
				File path = Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
				File file = new File(path, "" + filename + page + ".png");
				path.mkdirs();
				OutputStream stream = new FileOutputStream(file);
				image.compress(Bitmap.CompressFormat.PNG, 0, stream);
				image = null;
				stream.close();

				MediaScannerConnection.scanFile(this,
						new String[] { file.toString() }, null, null);
			}
		} catch (IOException e) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Error saving image to file "
					+ Environment.DIRECTORY_PICTURES + filename + ".png");
			builder.setCancelable(false);
			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
						}
					});
			AlertDialog alert = builder.create();
			alert.show();
		}
	}
	
	
	@Override
	public boolean onKeyDown(int KeyCode, KeyEvent event) //뒤로버튼 두번 누르면 종료되는 것.
	{
		back_Handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
			if (msg.what == 0) {
			// back_Check 값 초기화
			back_Check = false;
			}}};
		
		super.onKeyDown(KeyCode, event);
		if (event.getAction() == KeyEvent.ACTION_DOWN) 
		{
				switch (KeyCode) 
				{
					case KeyEvent.KEYCODE_BACK: // 뒤로 키와 같은 기능을 한다.
						if(help == "qa23ws" && !back_Check )
						{
							if(Temp.equals("채널/트랙"))
							{
								if (ll_channel.getChildCount() == 0) {
									exp_cnt.setVisibility(View.VISIBLE);
									cnt.setVisibility(View.VISIBLE);
									lower.setVisibility(View.VISIBLE);
								}
					    		cnt.setVisibility(View.VISIBLE);
					    		lower.setVisibility(View.VISIBLE);
					    		
					    		
							}
							else if(Temp.equals("메뉴"))
							{
					    		menu.setVisibility(View.VISIBLE);
					    		lower.setVisibility(View.VISIBLE);
							}
							else if(Temp.equals("코드"))
							{
								make_chord.setVisibility(View.VISIBLE);
					    		lower.setVisibility(View.VISIBLE);
							}
							else if(Temp.equals("전체 설정") || Temp.equals("코드 설정") || Temp.equals("채널 설정") || Temp.equals("트랙 설정"))
							{
								edit_chord.setVisibility(View.VISIBLE);
					    	    lower.setVisibility(View.VISIBLE);
							}
							main.setBackground(null);
							main.setBackgroundColor(Color.parseColor("#FFFFFF"));
							help = "1q2w3e";
							back_Handler.sendEmptyMessageDelayed(0, 200);
							return false;
						}
						else if (help == "1q2w3e" && !back_Check) {
							// 버튼클릭시 true
							back_Check = true;
							Toast.makeText(this,"한번 더 누르면 앱이 종료 됩니다.", Toast.LENGTH_SHORT).show();
							// Handler 호출 (0.5초 이후 back_Check 값 false)
							back_Handler.sendEmptyMessageDelayed(0, 1000);
							return false;
						} else {
						
							// 종료이벤트 or 기타이벤트 or 페이지이동 응용
						}
						return true;
				}
			}
		return false;
	}
				@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		menu.setQwertyMode(true);
		
		SubMenu sm = menu.addSubMenu(0, 0, 0, "도움말");
		MenuItem m = menu.add(0, 1, 1, "만든이");
		
		return true;
	}
	@SuppressWarnings("deprecation")
	@Override  
	public boolean onOptionsItemSelected(MenuItem item) {  
	    
		if (item.getItemId() == 0 ) {
	    	if(select_view.getText().equals("채널/트랙"))
	    	{
	    		if (ll_channel.getChildCount() == 0) 
	    			main.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_channel0));
	    		else
	    			main.setBackgroundDrawable(getResources().getDrawable(R.drawable.channel0));
	    		
	    		cnt.setVisibility(View.GONE);
	    		exp_cnt.setVisibility(View.GONE);
	    		lower.setVisibility(View.GONE);
	    		Temp = (String) select_view.getText();
	    		help = "qa23ws";
	    	}
	    	else if(select_view.getText().equals("메뉴"))
	    	{
	    		main.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_help0));
	    		menu.setVisibility(View.GONE);
	    		lower.setVisibility(View.GONE);
	    		Temp = (String) select_view.getText();
	    		help = "qa23ws";
	    	}
	    		
	    	else if(select_view.getText().equals("전체 설정"))
	    	{
	    	    main.setBackgroundDrawable(getResources().getDrawable(R.drawable.modify_total_help0));
	    	    edit_chord.setVisibility(View.GONE);
	    	    lower.setVisibility(View.GONE);
	    	    Temp = (String) select_view.getText();
	    		help = "qa23ws";
	    	}
	    		
	    	else if(select_view.getText().equals("코드 설정"))
	    	{
	    		main.setBackgroundDrawable(getResources().getDrawable(R.drawable.modify_chord_help0));
	    		edit_chord.setVisibility(View.GONE);
	    	    lower.setVisibility(View.GONE);
	    	    Temp = (String) select_view.getText();
	    		help = "qa23ws";
	    	}
	    		
	    	else if(select_view.getText().equals("트랙 설정"))
	    	{
	    		main.setBackgroundDrawable(getResources().getDrawable(R.drawable.modify_track_help0));
	    		edit_chord.setVisibility(View.GONE);
	    	    lower.setVisibility(View.GONE);
	    	    Temp = (String) select_view.getText();
	    		help = "qa23ws";
	    	}
	    		
	    	else if(select_view.getText().equals("채널 설정"))
	    	{
	    		main.setBackgroundDrawable(getResources().getDrawable(R.drawable.modify_channel_help0));
	    		edit_chord.setVisibility(View.GONE);
	    	    lower.setVisibility(View.GONE);
	    	    Temp = (String) select_view.getText();
	    		help = "qa23ws";
	    	}
	    		
	    	else if(select_view.getText().equals("코드"))
	    	{
	    		if(Count == 5)
	    		{
	    			main.setBackgroundDrawable(getResources().getDrawable(R.drawable.chord_help0));
	    		}
	    		else if(Count == 0)
	    			main.setBackgroundDrawable(getResources().getDrawable(R.drawable.chord_first_help0));
	    		
	    		make_chord.setVisibility(View.GONE);
	    		lower.setVisibility(View.GONE);
	    		Temp = (String) select_view.getText();
	    		help = "qa23ws";
	    	}
	    	
	    	
	    		
	        return true;  
	    }  
		
		else if (item.getItemId() == 1 ) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle("모두의작곡 v1.5 개발자");
			
			alert.setMessage("\r\n디자인 및 튜토리얼: 이동규\r\n\r\n\r\n" +
					"미디 프로그래밍: 나동희\r\n\r\n\r\n" +
					"데이터베이스: 이승현\r\n\r\n\r\n" + 
					"파일 출력: 강석규\r\n");	
			alert.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				}
			});

			alert.show();

	        return true;  
		}
		
	    else   
	        return false;  
	} 

}
