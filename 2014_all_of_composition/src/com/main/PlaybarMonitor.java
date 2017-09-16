package com.main;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class PlaybarMonitor implements OnSeekBarChangeListener {
	
	private SeekBar seekBar;
	private MediaPlayer mediaPlayer;
	@SuppressWarnings("rawtypes")
	private ScheduledFuture scheduledFuture;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			monitor();
		}
	};
	
	public PlaybarMonitor (SeekBar seekBar , MediaPlayer mediaPlayer) {
		this.seekBar = seekBar;
		this.mediaPlayer = mediaPlayer;
		init();
	}
	
	private void init() {
		ScheduledExecutorService service =  Executors.newScheduledThreadPool(1);
		
		 scheduledFuture = 
		service.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				
				handler.sendMessage(handler.obtainMessage(0));
			}
			}, 	200,
				200,
				TimeUnit.MILLISECONDS);
	

		seekBar.setOnSeekBarChangeListener(this);
	}
	
	
	private void monitor() {
		try {
			if (mediaPlayer.isPlaying()) {
				final float duration = mediaPlayer.getDuration();
				final float position = mediaPlayer.getCurrentPosition();
				
				seekBar.setMax((int) duration);
				seekBar.setProgress((int) position);
				
				
				 mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
	                 public void onCompletion(MediaPlayer arg0) {
	                	 seekBar.setProgress ((0));
	            }});
				
			}
	
			 
		} catch (Exception e) {
			
		}
	}
	
	public void cancel() {
		scheduledFuture.cancel(true);		
		handler.removeMessages(0);
	}
	
	private void seekTo (int msec) {
		try {
			mediaPlayer.seekTo(msec);
		} catch (Exception e) {
		}
	}

	@Override
	public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
		if (arg2) {
			seekTo(progress);
		}
	}
	
	

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
	}
}
