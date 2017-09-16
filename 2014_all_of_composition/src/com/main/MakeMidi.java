package com.main;

import java.io.*;
import java.util.*;

import android.content.Context;
import android.os.Environment;

public class MakeMidi {

	private Context Midi;

	static final int S = 4;
	static final int Q = 8;
	static final int T = 12;
	static final int C = 16;
	static final int M = 32;

	static final int header[] = new int[] { 0x4d, 0x54, 0x68, 0x64, // MTHd
			0x00, 0x00, 0x00, 0x06, // fix value
			0x00, 0x01, // multi-track format
			0x00, 0x11, // one track +...
			0x00, 0x40, // 80 ticks per quarter
			//
	};

	static final int h_closed[] = new int[] { 0x4d, 0x54, 0x72, 0x6B
	//
	};

	static final int footer[] = new int[] { 0x01, 0xFF, 0x2F, 0x00 };

	static final int tempoEvent[] = new int[] { 0x00, 0xFF, 0x51, 0x03, 0x0F,
			0x42, 
			0x40 // Default 1 million usec per crotchet
	};

	static final int keySigEvent[] = new int[] { 0x00, 0xFF, 0x59, 
		0x02, 
		0x00, // C 
		0 // major
	};

	static final int timeSigEvent[] = new int[] { 0x00, 0xFF, 0x58, 0x04, 
			0x04, // numerator
			0x02, // denominator (2==4, because it's a power of 2)
			0x18, // ticks per click (not used)
			0x08 // 32nd notes per crotchet
	};

	 protected Vector<int[]>[]  playEvents;
	
	@SuppressWarnings("unchecked")
	public MakeMidi(Context Midi) {
		super();
		
		playEvents = (Vector<int[]>[]) new Vector[16]; 
		
		for(int i = 0; i < playEvents.length; i++){ 
			playEvents[i] = new Vector<int[]>(); 
		}
		 
		
		this.Midi = Midi;
	}

	public void channel_setting(Vector<int[]> E, FileOutputStream fos)
			throws IOException {

		fos.write(intArrayToByteArray(h_closed));

		int size;

		if (E == null)
			size = tempoEvent.length + keySigEvent.length + timeSigEvent.length
					+ footer.length;

		else {
			size = footer.length;
			for (int i = 0; i < E.size(); i++)
				size += E.elementAt(i).length;
		}

		int high = size / 256;
		int low = size - (high * 256);
		fos.write((byte) 0);
		fos.write((byte) 0);
		fos.write((byte) high);
		fos.write((byte) low);

		if (E == null) {
			fos.write(intArrayToByteArray(tempoEvent));
			fos.write(intArrayToByteArray(keySigEvent));
			fos.write(intArrayToByteArray(timeSigEvent));
		}

		else {
			for (int i = 0; i < E.size(); i++)
				fos.write(intArrayToByteArray(E.elementAt(i)));
		}

		fos.write(intArrayToByteArray(footer));

	}	

	public void writeToFile(String Filename, int p) throws IOException {

		String dir = Midi.getDir("", Context.MODE_PRIVATE).getAbsolutePath();
		File file = new File(dir + File.separator + Filename);
		
		if(p == 1){
		file = new File(
				Environment.getExternalStoragePublicDirectory
				(Environment.DIRECTORY_MUSIC), Filename);
		}
		
		if (!file.exists())
			file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);

		fos.write(intArrayToByteArray(header));

		 channel_setting(null, fos); 
		 for(int i = 0; i < 16; i++)
		 channel_setting(playEvents[i], fos);

		fos.close();
	}
	
	protected static byte[] intArrayToByteArray(int[] ints) {
		int l = ints.length;
		byte[] out = new byte[ints.length];
		for (int i = 0; i < l; i++) {
			out[i] = (byte) ints[i];
		}
		return out;
	}

	public void noteOn(int delta, int note, int velocity, int channel) {
		int[] data = new int[4];
		data[0] = delta;
		data[1] = 0x90+channel;
		data[2] = note;
		data[3] = velocity;
		playEvents[channel].add(data);
	}

	public void noteOff(int delta, int note, int velocity, int channel) {
		int[] data = new int[4];
		data[0] = delta;
		data[1] = 0x80+channel;
		data[2] = note;
		data[3] = velocity;
		playEvents[channel].add(data);
	}
	
	public void single_note(int delta, int note, int velocity, int channel) {
		if(delta > 0){
		//pedal(0, 67, channel);
		noteOn(0,note,velocity, channel);
		noteOff(delta,note,velocity, channel);
		}
	}
	
	public void multi_note(int delta, int note[], int velocity, int channel) {

		
		if(delta > 0){
			
		if(note[0] > 0)
		noteOn(0,note[0],velocity, channel);
		
		if(note[1] > 0)
			noteOn(0, note[1], velocity, channel);
		if(note[2] > 0)
			noteOn(0, note[2], velocity, channel);
		if(note[3] > 0)
			noteOn(0, note[3], velocity, channel);

		noteOff(delta,note[0],velocity, channel);
		
		if(note[1] > 0)
			noteOff(0, note[1], velocity, channel);
		if(note[2] > 0)
			noteOff(0, note[2], velocity, channel);
		if(note[3] > 0)
			noteOff(0, note[3], velocity, channel);
		
		}
	}
	
	public void end(int channel) {
		noteOn(0,0,0, channel);
		noteOff(127,0,0, channel);
	}
	
	
	public void pedal(int delta, int velocity, int channel) {
		
		int[] data = new int[4];
		
		data[0] = delta;
		data[1] = 0xB0+channel;
		data[2] = 64;
		data[3] = velocity;
		
		playEvents[channel].add(data);
		
	}
	
	
	
	public void progChange(int prog, int channel) {
		int[] data = new int[3];
		data[0] = 0;
		data[1] = 0xC0+channel;
		data[2] = prog;
		playEvents[channel].add(data);
	}

	public void aftertouch(int note, int mount) {
		int[] data = new int[4];
		data[0] = 0;
		data[1] = 0xa0;
		data[2] = note;
		data[3] = mount;
		playEvents[0].add(data);
	}

	public void controller(int ctrnum, int value) {
		int[] data = new int[4];
		data[0] = 0;
		data[1] = 0xB0;
		data[2] = ctrnum;
		data[3] = value;
		playEvents[0].add(data);
	}

	public void pitchwheel(int fd, int sd) {
		int[] data = new int[4];
		data[0] = 0;
		data[1] = 0xE0;
		data[2] = fd;
		data[3] = sd;
		playEvents[0].add(data);
	}

	/*
	 * public void noteOnOffNow(int duration, int note, int velocity) {
	 * noteOn(0, note, velocity); noteOff(duration, note); }
	 * 
	 * public void noteSequenceFixedVelocity(int[] sequence, int velocity) {
	 * boolean lastWasRest = false; int restDelta = 0; for (int i = 0; i <
	 * sequence.length; i += 2) { int note = sequence[i]; int duration =
	 * sequence[i + 1]; if (note < 0) { // This is a rest restDelta += duration;
	 * lastWasRest = true; } else { // A note, not a rest if (lastWasRest) {
	 * noteOn(restDelta, note, velocity); noteOff(duration, note); } else {
	 * noteOn(0, note, velocity); noteOff(duration, note); } restDelta = 0;
	 * lastWasRest = false; } } }
	 */
}