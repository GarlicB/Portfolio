package com.main;

public class PlayStyle {

	String[] originapb = new String[] { "C", "D", "E", "F", "G", "A", "B" };
	String[] transpose = new String[] { "C/0", "C/#", "D/0", "D/#", "E/0",
			"F/0", "F/#", "G/0", "G/#", "A/0", "A/#", "B/0" };

	String[] b;
	int temp;
	int f, f_1, f_2;
	int[] n = { 0, 2, 4, 5, 7, 9, 11 };
	int[] note = new int[4];
	int[] time;

	public void style(MakeMidi fs, int c, String a, int[] d) {

		b = a.split("/");

		if (d[3] > 0) {
			d[3] = d[3] * 2;
		}

			setting_chord(d[2], d[4], d[6]);
			
			if (c == 9 && d[0] >= 100) {
				note[0] = 42;
				note[1] = 35;
				note[2] = 0;
				note[3] = 0;
			}
		

		switch (d[0]) {

		case -1:
			fs.multi_note(127, note, 97, c);
			break;

		case 0:
			style_000(fs, c, d);
			break;

		case 1:
			style_001(fs, c, d);
			break;

		case 2:
			style_002(fs, c, d);
			break;

		case 3:
			style_003(fs, c, d);
			break;

		case 4:
			style_004(fs, c, d);
			break;

		case 5:
			style_005(fs, c, d);
			break;

		case 6:
			style_006(fs, c, d);
			break;

		case 7:
			style_007(fs, c, d);
			break;

		case 8:
			style_008(fs, c, d);
			break;

		case 9:
			style_009(fs, c, d);
			break;

		case 10:
			style_010(fs, c, d);
			break;

		case 11:
			style_011(fs, c, d);
			break;
			
		case 12:
			style_012(fs, c, d);
			break;
			
		case 13:
			style_013(fs, c, d);
			break;
			
		case 14:
			style_014(fs, c, d);
			break;
			
		case 15:
			style_015(fs, c, d);
			break;
			
		case 16:
			style_016(fs, c, d);
			break;
			
		case 17:
			style_017(fs, c, d);
			break;
			
		case 18:
			style_018(fs, c, d);
			break;
			
		case 19:
			style_019(fs, c, d);
			break;
			
		case 20:
			style_020(fs, c, d);
			break;

		// Drum Set!
		case 100:
			style_100(fs, c, d);
			break;

		case 101:
			style_101(fs, c, d);
			break;

		case 102:
			style_102(fs, c, d);
			break;

		case 103:
			style_103(fs, c, d);
			break;

		case 104:
			style_104(fs, c, d);
			break;

		case 105:
			style_105(fs, c, d);
			break;

		case 106:
			style_106(fs, c, d);
			break;
			
		case 107:
			style_107(fs, c, d);
			break;

		case 108:
			style_108(fs, c, d);
			break;

		case 109:
			style_109(fs, c, d);
			break;

		case 110:
			style_110(fs, c, d);
			break;

		case 111:
			style_111(fs, c, d);
			break;
			
		case 112:
			style_112(fs, c, d);
			break;

		case 113:
			style_113(fs, c, d);
			break;

		case 114:
			style_114(fs, c, d);
			break;

		case 115:
			style_115(fs, c, d);
			break;

		case 116:
			style_116(fs, c, d);
			break;
			
		case 117:
			style_117(fs, c, d);
			break;

		case 118:
			style_118(fs, c, d);
			break;

		case 119:
			style_119(fs, c, d);
			break;

		case 120:
			style_120(fs, c, d);
			break;


		}

	}

	protected void style_000(MakeMidi fs, int c, int[] d) {

		time = time(4, 48, d[1], d[3]);

		fs.single_note(time[0], note[0], 0, c);
		fs.single_note(time[1], note[1], 0, c);
		fs.single_note(time[2], note[2], 0, c);
		fs.single_note(time[3], note[3], 0, c);
	}
	protected void style_001(MakeMidi fs, int c, int[] d) {

		time = time(16, MakeMidi.T, d[1], d[3]);

		f_1 = 12;
		if (!b[4].equals("0")) {
			f = 3;
			f_1 = 0;
			note[0] += 12;
			note[1] += 12;
		}
		
		//fs.pedal(0, 67, c);
		fs.multi_note(time[0], note, d[5] + 7, c);
		fs.single_note(time[1], note[(1 + f) % 4], d[5] + 2, c);
		fs.single_note(time[2], note[(0 + f) % 4], d[5] + 4, c);
		fs.single_note(time[3], note[(1 + f) % 4], d[5] + 2, c);
		if (!b[4].equals("0")) {
			note[1] -= 12;
			note[2] += 12;
		}

		for (int i = 0; i < 3; i++) {
			fs.single_note(time[(4 * (i + 1)) + 0], note[1] - f_1, d[5] - 3, c);
			fs.single_note(time[(4 * (i + 1)) + 1], note[2] - 12, d[5] - 1, c);
			fs.single_note(time[(4 * (i + 1)) + 2], note[(0 + f) % 4],
					d[5] + 1, c);
			fs.single_note(time[(4 * (i + 1)) + 3], note[(1 + f) % 4],
					d[5] + 3, c);
		}

		//fs.pedal(0, 0, c);

	}
	protected void style_002(MakeMidi fs, int c, int[] d) {
		time = time(12, MakeMidi.T + 4, d[1], d[3]);

		note[0] += 12;

		if (!b[4].equals("0"))
			note[0] = 0;
		else
			note[3] = 0;

		for (int i = 0; i < 4; i++) {

			fs.multi_note(time[(3 * i) + 0], note, d[5], c);
			fs.single_note(time[(3 * i) + 1], note[2], d[5], c);
			fs.single_note(time[(3 * i) + 2], note[1], d[5], c);
		}
	}
	protected void style_003(MakeMidi fs, int c, int[] d) {
		time = time(8, MakeMidi.T * 2, d[1], d[3]);

		f_1 = 12;
		f_2 = 0;

		if (!b[4].equals("0")) {
			note[3] -= 12;
			f = 3;
			f_1 = 0;
			f_2 = -1;
		}
		fs.single_note(time[0], note[(0 + f) % 4], d[5], c);
		fs.single_note(time[1], note[(1 + f) % 4], d[5], c);
		fs.single_note(time[2], note[(2 + f) % 4], d[5], c);
		fs.single_note(time[3], note[(1 + f) % 4], d[5], c);
		fs.single_note(time[4], note[(0 + f + f_2) % 4] + f_1, d[5], c);
		fs.single_note(time[5], note[(2 + f) % 4], d[5], c);
		fs.single_note(time[6], note[(1 + f) % 4], d[5], c);
		fs.single_note(time[7], note[(2 + f) % 4], d[5], c);
	}
	protected void style_004(MakeMidi fs, int c, int[] d) {
		time = time(8, MakeMidi.T * 2, d[1], d[3]);

		if (!b[4].equals("0")) {
			note[3] -= 12;
			note[2] -= 12;
			f = 3;
			f_1 = 2;
		}
		for (int i = 0; i < 2; i++) {
			fs.single_note(time[(i * 4) + 0], note[(0 + f) % 4], d[5], c);
			fs.single_note(time[(i * 4) + 1], note[(1 + f) % 4], d[5], c);
			fs.single_note(time[(i * 4) + 2], note[(2 + f) % 4], d[5], c);
			fs.single_note(time[(i * 4) + 3], note[(0 + f_1) % 4] + 12, d[5], c);
		}
	}
	protected void style_005(MakeMidi fs, int c, int[] d) {
		time = time(8, MakeMidi.T * 2, d[1], d[3]);

		if (!b[4].equals("0")) {
			note[0] -= 12;
			note[3] -= 24;
			f = 3;
			f_1 = -12;
		}
		for (int i = 0; i < 2; i++) {
			fs.single_note(time[(i * 4) + 0], note[(1 + f) % 4] + 12, d[5], c);
			fs.single_note(time[(i * 4) + 1], note[(0 + f) % 4] + 12, d[5], c);
			fs.single_note(time[(i * 4) + 2], note[(2) % 4] + f_1, d[5], c);
			fs.single_note(time[(i * 4) + 3], note[(1) % 4] + f_1, d[5], c);
		}
	}
	protected void style_006(MakeMidi fs, int c, int[] d) {
		time = time(8, MakeMidi.T * 2, d[1], d[3]);

		f_1 = 12;
		fs.single_note(time[0], note[0], d[5], c);
		if (!b[4].equals("0")) {
			f = 2;
			f_1 -= 12;
		}
		fs.single_note(time[1], note[(0 + f) % 3] + f_1, d[5], c);
		fs.single_note(time[2], note[(2 + f) % 3], d[5], c);
		fs.single_note(time[3], note[(1 + f) % 3], d[5], c);
		fs.single_note(time[4], note[(1 + f) % 4] + f_1, d[5], c);
		fs.single_note(time[5], note[(2 + f) % 3], d[5], c);
		fs.single_note(time[6], note[(1 + f) % 3], d[5], c);
		fs.single_note(time[7], note[(0 + f) % 3] + f_1, d[5], c);
	}
	protected void style_007(MakeMidi fs, int c, int[] d) {
		time = time(8, MakeMidi.T * 2, d[1], d[3]);

		f_1 = 12;
		if (!b[4].equals("0")) {
			f = 3;
			f_1 -= 12;
		}
		fs.single_note(time[0], note[(0)], d[5], c);
		fs.single_note(time[1], note[(1)], d[5], c);
		fs.single_note(time[2], note[(2)], d[5], c);
		fs.single_note(time[3], note[(0 + f) % 4] + f_1, d[5], c);
		fs.single_note(time[4], note[(1 + f) % 4] + 12, d[5], c);
		fs.single_note(time[5], note[(2)], d[5], c);
		fs.single_note(time[6], note[(0 + f) % 4] + f_1, d[5], c);
		fs.single_note(time[7], note[(1 + f) % 4] + 12, d[5], c);
	}
	protected void style_008(MakeMidi fs, int c, int[] d) {
		time = time(8, MakeMidi.T * 2, d[1], d[3]);

		f_2 = 12;
		if (!b[4].equals("0")) {
			note[3] -= 12;
			f = 3;
			f_1 = 2;
			f_2 = 0;
		}
		fs.single_note(time[0] + time[1], note[(0)], d[5], c);
		fs.single_note(time[2], note[(0 + f) % 4], d[5], c);
		fs.single_note(time[3] + time[4], note[(2 + f) % 4], d[5], c);
		fs.single_note(time[5], note[(1 + f) % 4], d[5], c);
		fs.single_note(time[6] + time[7], note[(0 + f_1)] + f_2, d[5], c);
	}
	protected void style_009(MakeMidi fs, int c, int[] d) {
		time = time(8, MakeMidi.T * 2, d[1], d[3]);

		f_1 = 12;

		if (!b[4].equals("0")) {
			note[3] -= 12;
			f = 2;
			f_2 = 3;
			f_1 = 0;
		}

		fs.single_note(time[0], note[(0 + f) % 3] + f_1, d[5], c);
		fs.single_note(time[1], note[(2 + f) % 3], d[5], c);
		fs.single_note(time[2], note[(1 + f) % 3], d[5], c);
		fs.single_note(time[3], note[(0 + f_2)], d[5], c);
		fs.single_note(time[4], note[(1 + f) % 3], d[5], c);
		fs.single_note(time[5], note[(2 + f) % 3], d[5], c);
		fs.single_note(time[6], note[(0 + f) % 3] + f_1, d[5], c);
		fs.single_note(time[7], note[(2 + f) % 3], d[5], c);
	}
	protected void style_010(MakeMidi fs, int c, int[] d) {
		time = time(8, MakeMidi.T * 2, d[1], d[3]);

		f_1 = 12;

		if (!b[4].equals("0")) {
			note[3] -= 12;
			f = 3;
			f_2 = 2;
			f_1 = 0;
		}

		fs.single_note(time[0], note[(0 + f) % 4], d[5], c);
		fs.single_note(time[1], note[(1 + f) % 4], d[5], c);
		fs.single_note(time[2], note[(2 + f) % 4], d[5], c);
		fs.single_note(time[3], note[(0 + f_2)] + f_1, d[5], c);
		fs.single_note(time[4], note[(2 + f) % 4], d[5], c);
		fs.single_note(time[5], note[(1 + f) % 4], d[5], c);
		fs.single_note(time[6], note[(0 + f) % 4], d[5], c);
		fs.single_note(time[7], note[(1 + f) % 4], d[5], c);
	}
	protected void style_011(MakeMidi fs, int c, int[] d) {
		
		time = time(4, MakeMidi.T*4, d[1], d[3]);

		if (b[4].equals("0"))
			note[3] = 0;
		
		//fs.pedal(0, 67, c);
		fs.multi_note(time[0]+time[1]/4, note, d[5], c);
		fs.multi_note(time[1]/4*3, note, 0, c);
		fs.multi_note(time[2], note, 0, c);
		fs.multi_note(time[3], note, 0, c);
		//fs.pedal(0, 0, c);
		
	}
	protected void style_012(MakeMidi fs, int c, int[] d) {

		time = time(4, MakeMidi.T*4, d[1], d[3]);
		if (b[4].equals("0"))
			note[3] = 0;
		
		//fs.pedal(0, 67, c);
		fs.multi_note(time[0]+time[1]/4, note, d[5], c);
		fs.multi_note(time[1]/4*3, note, 0, c);
		fs.multi_note(time[2]+time[3]/4, note, d[5]-20, c);
		fs.multi_note(time[3]/4*3, note, 0, c);
		//fs.pedal(0, 0, c);

		
	}
	protected void style_013(MakeMidi fs, int c, int[] d) {
		time = time(4, MakeMidi.T*4, d[1], d[3]);
		//fs.pedal(0, 67, c);
		fs.multi_note(time[0], note, d[5], c);
		fs.multi_note(time[1], note, d[5]-10, c);
		f = note[0];
		note[0] = 0;
		fs.multi_note(time[2], note, d[5]-10, c);
		note[0] = f;
		fs.multi_note(time[3], note, d[5]-10, c);
		//fs.pedal(0, 0, c);
		
	}
	protected void style_014(MakeMidi fs, int c, int[] d) {
		time = time(8, MakeMidi.T*2, d[1], d[3]);
		//fs.pedal(0, 67, c);
		fs.multi_note(time[0], note, d[5], c);
		fs.multi_note(time[1], note, d[5]-15, c);
		fs.multi_note(time[2], note, d[5]-15, c);
		fs.multi_note(time[3], note, d[5]-15, c);
		fs.multi_note(time[4], note, d[5]-5, c);
		fs.multi_note(time[5], note, d[5]-15, c);
		fs.multi_note(time[6], note, d[5]-15, c);
		fs.multi_note(time[7], note, d[5]-15, c);
		//fs.pedal(0, 0, c);
		
	}
	protected void style_015(MakeMidi fs, int c, int[] d) {
		time = time(8, MakeMidi.T*2, d[1], d[3]);

		//fs.pedal(0, 67, c);
		for(int i = 0; i < 2; i ++){
		fs.multi_note(time[(4*i)+0]+time[(4*i)+1], note, d[5], c);
		fs.multi_note(time[(4*i)+2], note, d[5], c);
		fs.single_note(time[(4*i)+3], note[0], d[5], c);
		}
		//fs.pedal(0, 0, c);
		
	}
	protected void style_016(MakeMidi fs, int c, int[] d) {
		time = time(8, MakeMidi.T*2, d[1], d[3]);
		if (b[4].equals("0"))
			note[3] = 0;
		
		//fs.pedal(0, 67, c);
		fs.multi_note(time[0]+time[1], note, d[5], c);

		for(int i = 0; i < 3; i ++){
		fs.multi_note(time[(2*i)+1], note, d[5], c);
		fs.single_note(time[(2*i)+2], note[0], d[5], c);
		}
		//fs.pedal(0, 0, c);
		
		
	}
	protected void style_017(MakeMidi fs, int c, int[] d) {
		
		time = time(8, MakeMidi.T*2, d[1], d[3]);
		//fs.pedal(0, 67, c);

		fs.multi_note(time[0], note, d[5], c);
		fs.multi_note(time[1]+time[2], note, d[5], c);

		fs.multi_note(time[3], note, d[5], c);
		fs.multi_note(time[4], note, d[5], c);
		fs.multi_note(time[5] + time[6], note, d[5], c);
		
		fs.multi_note(time[7], note, d[5], c);

		//fs.pedal(0, 0, c);
		
		
	}
	protected void style_018(MakeMidi fs, int c, int[] d) {

		time = time(8, MakeMidi.T*2, d[1], d[3]);
		//fs.pedal(0, 67, c);

		fs.multi_note(time[0], note, d[5], c);
		fs.multi_note(time[1], note, d[5], c);
		fs.multi_note(time[2] +time[3], note, d[5], c);
		fs.multi_note(time[4] + time[5]/2, note, d[5], c);
		fs.multi_note(time[5]/2+time[6]+time[7], note, d[5], c);

		//fs.pedal(0, 0, c);
		
		
	}
	protected void style_019(MakeMidi fs, int c, int[] d) {

		time = time(4, MakeMidi.T*4, d[1], d[3]);
		//fs.pedal(0, 67, c);

		fs.multi_note(time[0]/2, note, d[5], c);
		fs.multi_note(time[0]/2+time[1]/2, note, d[5], c);
		fs.multi_note(time[1]/2+time[2]/2, note, d[5], c);
		fs.multi_note(time[2]/2, note, d[5], c);
		fs.multi_note(time[3], note, d[5], c);

		//fs.pedal(0, 0, c);
		
	}
	protected void style_020(MakeMidi fs, int c, int[] d) {

		time = time(8, MakeMidi.T*2, d[1], d[3]);
		if (b[4].equals("0"))
			note[3] = 0;
		
	//	fs.pedal(0, 67, c);

		fs.single_note(time[0], note[0], d[5], c);
		fs.single_note(time[1], note[1], d[5], c);
		fs.multi_note(time[2], note, d[5], c);
		fs.single_note(time[3], note[0], d[5], c);
		fs.single_note(time[4], note[1], d[5], c);
		fs.single_note(time[5], note[2], d[5], c);
		fs.multi_note(time[6], note, d[5], c);
		fs.single_note(time[7], note[0], d[5], c);

	//	fs.pedal(0, 0, c);
		
	}
	protected void style_100(MakeMidi fs, int c, int[] d) {
		time = time(4, 48, d[1], d[3]);

		fs.single_note(time[0], note[0], 0, c);
		fs.single_note(time[1], note[1], 0, c);
		fs.single_note(time[2], note[2], 0, c);
		fs.single_note(time[3], note[3], 0, c);
	}
	protected void style_101(MakeMidi fs, int c, int[] d) {

		time = time(8, 24, d[1], d[3]);

		fs.multi_note(time[0], note, d[5], 9);
		note[1] = 0;
		fs.multi_note(time[1], note, d[5], 9);
		note[2] = 38;
		fs.multi_note(time[2], note, d[5], 9);
		note[1] = 35;
		note[2] = 0;
		fs.multi_note(time[3], note, d[5], 9);
		fs.multi_note(time[4], note, d[5], 9);
		note[1] = 0;
		fs.multi_note(time[5], note, d[5], 9);
		note[2] = 38;
		fs.multi_note(time[6], note, d[5], 9);
		note[2] = 0;
		fs.multi_note(time[7], note, d[5], 9);
		note[1] = 35;

	}
	protected void style_102(MakeMidi fs, int c, int[] d) {

		time = time(8, 24, d[1], d[3]);

		fs.multi_note(time[0], note, d[5], 9);
		note[1] = 0;
		fs.multi_note(time[1], note, d[5], 9);
		fs.multi_note(time[2], note, d[5], 9);
		note[1] = 35;
		fs.multi_note(time[3], note, d[5], 9);
		fs.multi_note(time[4], note, d[5], 9);
		note[1] = 0;
		fs.multi_note(time[5], note, d[5], 9);
		note[2] = 38;
		fs.multi_note(time[6], note, d[5], 9);
		note[2] = 0;
		fs.multi_note(time[7], note, d[5], 9);
		note[1] = 35;
	}
	protected void style_103(MakeMidi fs, int c, int[] d) {

		time = time(8, 24, d[1], d[3]);

		fs.multi_note(time[0], note, d[5], 9);
		note[1] = 0;
		fs.multi_note(time[1], note, d[5], 9);
		fs.multi_note(time[2], note, d[5], 9);
		note[1] = 35;
		fs.multi_note(time[3], note, d[5], 9);
		note[2] = 38;
		fs.multi_note(time[4], note, d[5], 9);
		note[1] = 0;
		note[2] = 0;
		fs.multi_note(time[5], note, d[5], 9);
		fs.multi_note(time[6], note, d[5], 9);
		fs.multi_note(time[7], note, d[5], 9);
		note[1] = 35;
	}
	protected void style_104(MakeMidi fs, int c, int[] d) {

		time = time(8, 24, d[1], d[3]);

		fs.multi_note(time[0], note, d[5], 9);
		note[1] = 0;
		note[2] = 38;
		fs.multi_note(time[1], note, d[5], 9);
		note[2] = 0;
		fs.multi_note(time[2], note, d[5], 9);
		note[1] = 35;
		fs.multi_note(time[3], note, d[5], 9);
		fs.multi_note(time[4], note, d[5], 9);
		note[1] = 0;
		fs.multi_note(time[5], note, d[5], 9);
		note[2] = 38;
		fs.multi_note(time[6], note, d[5], 9);
		note[2] = 0;
		fs.multi_note(time[7], note, d[5], 9);
		note[1] = 35;
	}
	protected void style_105(MakeMidi fs, int c, int[] d) {

		time = time(8, 24, d[1], d[3]);

		fs.multi_note(time[0], note, d[5], 9);
		note[1] = 0;
		fs.multi_note(time[1] / 2, note, d[5], 9);
		note[0] = 0;
		note[2] = 38;
		fs.multi_note(time[1] / 2, note, d[5], 9);
		note[0] = 42;
		note[2] = 0;
		fs.multi_note(time[2], note, d[5], 9);
		note[1] = 35;
		fs.multi_note(time[3], note, d[5], 9);
		fs.multi_note(time[4], note, d[5], 9);
		note[1] = 0;
		fs.multi_note(time[5], note, d[5], 9);
		note[2] = 38;
		fs.multi_note(time[6], note, d[5], 9);
		note[2] = 0;
		fs.multi_note(time[7], note, d[5], 9);
		note[1] = 35;
	}
	protected void style_106(MakeMidi fs, int c, int[] d) {

		time = time(8, 24, d[1], d[3]);

		fs.multi_note(time[0], note, d[5], 9);
		note[1] = 0;
		fs.multi_note(time[1] / 2, note, d[5], 9);
		note[0] = 0;
		note[2] = 38;
		fs.multi_note(time[1] / 2, note, d[5], 9);
		note[0] = 42;
		fs.multi_note(time[2], note, d[5], 9);
		note[2] = 0;
		note[1] = 35;
		fs.multi_note(time[3], note, d[5], 9);
		fs.multi_note(time[4], note, d[5], 9);
		note[1] = 0;
		fs.multi_note(time[5], note, d[5], 9);
		note[2] = 38;
		fs.multi_note(time[6] / 2, note, d[5], 9);
		note[0] = 0;
		fs.multi_note(time[6] / 2, note, d[5], 9);
		note[0] = 42;
		note[2] = 0;
		fs.multi_note(time[7], note, d[5], 9);
		note[1] = 35;
	}
	protected void style_107(MakeMidi fs, int c, int[] d) {

		time = time(8, 24, d[1], d[3]);
		//클로즈드 하이햇, 베이스드럼, 0 , 0
		//24 16 8
		note[1] = 44;
		fs.single_note(time[0], note[0], d[5], 9);
		
		fs.multi_note((time[1]/4) * 3, note, d[5], 9);
		fs.single_note((time[1]/4), note[0], d[5], 9);
		fs.single_note(time[2], note[0], d[5], 9);
		
		fs.multi_note((time[3]/4) * 3, note, d[5], 9);
		fs.single_note((time[3]/4) , note[0], d[5], 9);
		fs.single_note(time[4], note[0], d[5], 9);
		
		fs.multi_note((time[5]/4) * 3, note, d[5], 9);
		fs.single_note((time[5]/4), note[0], d[5], 9);
		fs.single_note(time[6], note[0], d[5], 9);
		
		fs.multi_note((time[7]/4) * 3, note, d[5], 9);
		fs.single_note((time[7]/4), note[0], d[5], 9);
		
	}
	protected void style_108(MakeMidi fs, int c, int[] d) {
		time = time(4, 48, d[1], d[3]);
		//클로즈드 하이햇42, 베이스드럼35, 0 , 0
		//24 16 8

		for(int i = 0; i < 3; i++){
		fs.multi_note(time[i]/4, note, d[5], 9);
		fs.single_note(time[i]/4, note[0], d[5], 9);
		note[1] = 38;
		fs.multi_note(time[i]/4, note, d[5], 9);
		fs.single_note(time[i]/4, note[0], d[5], 9);
		note[1] = 35;
		}

		fs.multi_note(time[3]/4, note, d[5], 9);
		fs.multi_note(time[3]/4, note, d[5], 9);
		note[1] = 38;
		fs.multi_note(time[3]/4, note, d[5], 9);
		fs.single_note(time[3]/4, note[0], d[5], 9);
	}
	protected void style_109(MakeMidi fs, int c, int[] d) {

		time = time(4, 48, d[1], d[3]);
		
		for(int i = 0; i <2; i++){
		fs.multi_note(time[0+i]/8, note, d[5], 9);
		fs.single_note(time[0+i]/8, note[0], d[5], 9);
		fs.single_note(time[0+i]/8, note[0], d[5], 9);
		fs.single_note(time[0+i]/8, note[0], d[5], 9);
		note[1] = 38;
		fs.single_note(time[0+i]/8, note[1], d[5], 9);
		fs.single_note(time[0+i]/8, note[0], d[5], 9);
		fs.single_note(time[0+i]/8, note[0], d[5], 9);
		fs.single_note(time[0+i]/8, note[0], d[5], 9);
		note[1] = 35;
		}

		fs.multi_note(time[2]/8, note, d[5], 9);
		fs.single_note(time[2]/8, note[0], d[5], 9);
		fs.single_note(time[2]/8, note[0], d[5], 9);
		fs.single_note(time[2]/8, note[0], d[5], 9);
		note[1] = 38;
		fs.single_note(time[2]/8, note[1], d[5], 9);
		fs.single_note(time[2]/8, note[0], d[5], 9);
		fs.single_note(time[2]/8, note[0], d[5], 9);
		note[1] = 35;
		fs.multi_note(time[2]/8, note, d[5], 9);
		
		fs.multi_note(time[3]/8, note, d[5], 9);
		fs.single_note(time[3]/8, note[0], d[5], 9);
		fs.single_note(time[3]/8, note[0], d[5], 9);
		fs.single_note(time[3]/8, note[0], d[5], 9);
		note[1] = 38;
		fs.single_note(time[3]/8, note[1], d[5], 9);
		fs.single_note(time[3]/8, note[0], d[5], 9);
		note[0] = 46;
		fs.single_note(time[3]/4, note[0], d[5], 9);
		
		
	}
	protected void style_110(MakeMidi fs, int c, int[] d) {
		time = time(8, 24, d[1], d[3]);
		//클로즈드 하이햇42, 베이스드럼35, 0 , 0
		//24 16 8
		note[0] = 50;
		fs.multi_note(time[0], note, d[5], 9);
		note[1] = 38;
		fs.multi_note(time[1]/2, note, d[5], 9);
		note[1] = 35;
		fs.single_note(time[1]/2, note[1], d[5], 9);
		fs.multi_note(time[2], note, d[5], 9);
		note[1] = 38;
		fs.multi_note(time[3], note, d[5], 9);
		
		note[1] = 35;
		fs.multi_note(time[4], note, d[5], 9);
		note[1] = 38;
		fs.multi_note(time[5]/2, note, d[5], 9);
		note[1] = 35;
		fs.single_note(time[5]/2, note[1], d[5], 9);
		fs.multi_note(time[6]/2, note, d[5], 9);
		fs.single_note(time[6]/2, note[1], d[5], 9);
		note[1] = 38;
		fs.multi_note(time[7]/2, note, d[5], 9);
		note[1] = 35;
		fs.single_note(time[7]/2, note[1], d[5], 9);
	}
	protected void style_111(MakeMidi fs, int c, int[] d) {

		time = time(4, 48, d[1], d[3]);
		
		fs.multi_note(time[0]/8, note, d[5], 9);
		fs.single_note(time[0]/8, note[0], d[5], 9);
		fs.multi_note(time[0]/8, note, d[5], 9);
		fs.single_note(time[0]/8, note[0], d[5], 9);
	
		note[1] = 38;
		fs.multi_note(time[0]/8, note, d[5], 9);
		note[1] = 35;
		fs.multi_note(time[0]/8, note, d[5], 9);
		fs.single_note(time[0]/8, note[0], d[5], 9);
		fs.single_note(time[0]/8, note[0], d[5], 9);
		
		fs.multi_note(time[1]/8, note, d[5], 9);
		fs.single_note(time[1]/8, note[0], d[5], 9);
		fs.single_note(time[1]/8, note[0], d[5], 9);
		fs.single_note(time[1]/8, note[0], d[5], 9);
		
		note[1] = 38;
		fs.multi_note(time[1]/8, note, d[5], 9);
		fs.single_note(time[1]/8, note[0], d[5], 9);
		fs.single_note(time[1]/8, note[0], d[5], 9);
		note[0] = 46;
		note[1] = 35;
		fs.multi_note(time[1]/8, note, d[5], 9);
		
		note[0] = 42;
		fs.multi_note(time[2]/8, note, d[5], 9);
		fs.single_note(time[2]/8, note[0], d[5], 9);
		fs.multi_note(time[2]/8, note, d[5], 9);
		fs.single_note(time[2]/8, note[0], d[5], 9);

		note[1] = 38;
		fs.multi_note(time[2]/8, note, d[5], 9);
		note[1] = 35;
		fs.multi_note(time[2]/8, note, d[5], 9);
		fs.single_note(time[2]/8, note[0], d[5], 9);
		fs.single_note(time[2]/8, note[0], d[5], 9);
		
		fs.multi_note(time[3]/8, note, d[5], 9);
		fs.single_note(time[3]/8, note[0], d[5], 9);
		fs.single_note(time[3]/8, note[0], d[5], 9);
		fs.single_note(time[3]/8, note[0], d[5], 9);

		note[1] = 38;
		fs.multi_note(time[3]/8, note, d[5], 9);
		fs.single_note(time[3]/8, note[0], d[5], 9);
		fs.single_note(time[3]/8, note[0], d[5], 9);
		note[1] = 35;
		fs.multi_note(time[3]/8, note, d[5], 9);
		
	}
	protected void style_112(MakeMidi fs, int c, int[] d) {

		time = time(4, 48, d[1], d[3]);

		fs.multi_note(time[0]/3, note, d[5], 9);
		fs.single_note(time[0]/3, note[0], d[5], 9);
		note[0] = 50;
		fs.single_note(time[0]/3, note[0], d[5], 9);
		note[0] = 42;
		note[1] = 38;
		
		fs.multi_note(time[1]/3, note, d[5], 9);
		fs.single_note(time[1]/3, note[0], d[5], 9);
		note[1] = 35;
		fs.multi_note(time[1]/3, note, d[5], 9);
		
		fs.multi_note(time[2]/3, note, d[5], 9);
		fs.single_note(time[2]/3, note[0], d[5], 9);
		fs.single_note(time[2]/3, note[0], d[5], 9);
		
		note[1] = 38;
		fs.multi_note(time[3]/3, note, d[5], 9);
		fs.single_note(time[3]/3, note[0], d[5], 9);
		note[1] = 35;
		fs.multi_note(time[3]/3, note, d[5], 9);
		
	}
	protected void style_113(MakeMidi fs, int c, int[] d) {

		time = time(4, 48, d[1], d[3]);
		note[2] = 38;
		
		for(int i = 0; i < 2; i ++){
		fs.multi_note(time[i]/4, note, d[5], 9);
		fs.multi_note(time[i]/4, note, d[5], 9);
		
		note[0] = 0;
		fs.multi_note(time[i]/8, note, d[5], 9);
		note[1] = 0;
		
		fs.single_note(time[i]/8, note[2], d[5], 9);
		fs.single_note(time[i]/8, note[2], d[5], 9);
		fs.single_note(time[i]/8, note[2], d[5], 9);
		
		note[0] = 42;
		note[1] = 35;
		}
		
		fs.multi_note(time[2]/4, note, d[5], 9);
		fs.multi_note(time[2]/4, note, d[5], 9);
		
		note[0] = 50;
		fs.single_note(time[2]/4, note[0], d[5], 9);
		note[0] = 42;
		note[2] = 0;
		fs.multi_note(time[2]/4+time[3]/4, note, d[5], 9);
		note[2] = 38;
		fs.single_note(time[3]/8, note[2], d[5], 9);
		fs.single_note(time[3]/8, note[2], d[5], 9);
		
		fs.single_note(time[3]/8, note[2], d[5], 9);
		fs.single_note(time[3]/8, note[2], d[5], 9);
		fs.single_note(time[3]/4, note[2], d[5], 9);
		
	}
	protected void style_114(MakeMidi fs, int c, int[] d) {

		time = time(4, 48, d[1], d[3]);
		
		
		fs.multi_note(time[0]/4, note, d[5], 9);
		fs.single_note(time[0]/4, note[0], d[5], 9);
		note[2] = 38;
		note[3] = 61;
		fs.multi_note(time[0]/4, note, d[5], 9);
		fs.single_note(time[0]/4, note[0], d[5], 9);
		
		note[2] = 0;
		fs.multi_note(time[1]/4, note, d[5], 9);
		fs.multi_note(time[1]/4, note, d[5], 9);
		note[2] = 38;
		note[1] = 0;
		fs.multi_note(time[1]/4, note, d[5], 9);
		note[1] = 35;
		note[2] = 0;
		fs.multi_note(time[1]/4, note, d[5], 9);
		
		note[3] = 0;
		note[2] = 0;
		fs.multi_note(time[2]/8, note, d[5], 9);
		fs.single_note(time[2]/8, note[0], d[5], 9);
		note[3] = 56;
		note[1] = 0;
		fs.multi_note(time[2]/8, note, d[5], 9);
		note[3] = 0;
		note[1] = 35;
		fs.multi_note(time[2]/8, note, d[5], 9);
		fs.single_note(time[2]/8, note[0], d[5], 9);
		fs.multi_note(time[2]/8, note, d[5], 9);
		note[3] = 56;
		fs.multi_note(time[2]/8, note, d[5], 9);
		fs.single_note(time[2]/8, note[0], d[5], 9);
		
		note[3] = 0;
		fs.multi_note(time[3]/8, note, d[5], 9);
		fs.single_note(time[3]/8, note[0], d[5], 9);
		note[1] = 0;
		note[3] = 56;
		fs.multi_note(time[3]/8, note, d[5], 9);
		fs.multi_note(time[3]/8, note, d[5], 9);
		fs.single_note(time[3]/8, note[0], d[5], 9);
		fs.single_note(time[3]/8, note[0], d[5], 9);
		fs.multi_note(time[3]/8, note, d[5], 9);
		note[3] = 44;
		fs.multi_note(time[3]/8, note, d[5], 9);
		
	}
	protected void style_115(MakeMidi fs, int c, int[] d) {
		time = time(4, 48, d[1], d[3]);
		
		for (int i = 0; i < 2; i++) {
			note[2] = 69;
			fs.multi_note(time[(i * 2) + 0] / 8, note, d[5], 9);
			fs.single_note(time[(i * 2) + 0] / 8, note[0], d[5], 9);
			fs.single_note(time[(i * 2) + 0] / 8, note[0], d[5], 9);
			fs.multi_note(time[(i * 2) + 0] / 8, note, d[5], 9);
			note[0] = 44; note[1] = 38; note[2] = 0; note[3] = 58;
			fs.multi_note(time[(i * 2) + 0] / 8, note, d[5], 9);
			note[0] = 42;
			fs.single_note(time[(i * 2) + 0] / 8, note[0], d[5], 9);
			note[1] = 35; note[2] = 69; note[3] = 0;
			fs.multi_note(time[(i * 2) + 0] / 8, note, d[5], 9);
			fs.single_note(time[(i * 2) + 0] / 8, note[0], d[5], 9);

			fs.single_note(time[(i * 2) + 1] / 8, note[0], d[5], 9);
			fs.single_note(time[(i * 2) + 1] / 8, note[0], d[5], 9);
			fs.multi_note(time[(i * 2) + 1] / 8, note, d[5], 9);
			fs.single_note(time[(i * 2) + 1] / 8, note[0], d[5], 9);
			note[0] = 44; note[1] = 38; note[2] = 0; note[3] = 58;
			fs.multi_note(time[(i * 2) + 1] / 8, note, d[5], 9);
			note[0] = 42;
			fs.single_note(time[(i * 2) + 1] / 8, note[0], d[5], 9);
			fs.single_note(time[(i * 2) + 1] / 8, note[0], d[5], 9);
			fs.single_note(time[(i * 2) + 1] / 8, note[0], d[5], 9);
			note[0] = 42; note[1] = 35; note[2] = 69; note[3] = 0;
		}
	
	}
	protected void style_116(MakeMidi fs, int c, int[] d) {
		time = time(4, 48, d[1], d[3]);
		note[2] = 40;
		fs.multi_note(time[0]/4, note, d[5], 9);
		fs.single_note(time[0]/4, note[0], d[5], 9);
		fs.single_note(time[0]/4, note[0], d[5], 9);
		fs.multi_note(time[0]/4, note, d[5], 9);
		
		note[2] = 0;
		fs.multi_note(time[1]/4, note, d[5], 9);
		fs.single_note(time[1]/4, note[0], d[5], 9);
		note[1] = 0;
		note[2] = 40;
		fs.multi_note(time[1]/4, note, d[5], 9);
		fs.single_note(time[1]/4, note[0], d[5], 9);
		
		note[1] = 35; note[2] = 0;
		fs.multi_note(time[2]/4, note, d[5], 9);
		fs.single_note(time[2]/4, note[0], d[5], 9);
		note[1] = 40;
		fs.multi_note(time[2]/4, note, d[5], 9);
		note[1] = 35;
		fs.multi_note(time[2]/4, note, d[5], 9);
		
		fs.multi_note(time[3]/4, note, d[5], 9);
		note[1] = 40;
		fs.multi_note(time[3]/4, note, d[5], 9);
		fs.single_note(time[3]/4, note[0], d[5], 9);
		fs.single_note(time[3]/4, note[0], d[5], 9);
		
	}
	protected void style_117(MakeMidi fs, int c, int[] d) {

		time = time(4, 48, d[1], d[3]);
		
		for(int i = 0; i < 2; i ++){
		fs.multi_note(time[i]/8 * 3, note, d[5], 9);
		note[1] = 0; note[2] = 38;
		fs.multi_note(time[i]/8, note, d[5], 9);
		note[1] = 35;
		fs.multi_note(time[i]/8 * 3, note, d[5], 9);
		fs.single_note(time[i]/8, note[0], d[5], 9);
		note[2] = 0;
		}
		
		note[2] = 38; note[0] = 0;
		fs.multi_note(time[2]/8 * 3, note, d[5], 9);
		fs.single_note(time[2]/8, note[2], d[5], 9);
		fs.multi_note(time[2]/8 * 3, note, d[5], 9);
		fs.single_note(time[2]/8, note[2], d[5], 9);
		
		fs.multi_note(time[3]/4, note, d[5], 9);
		

		fs.single_note(time[3]/4 + time[3]/2, 0, d[5], 9);

	}
	protected void style_118(MakeMidi fs, int c, int[] d) {
		time = time(4, 48, d[1], d[3]);
		
		fs.multi_note(time[0]/4 , note, d[5], 9);
		fs.single_note(time[0]/4, note[0], d[5], 9);
		note[1] = 38;
		fs.multi_note(time[0]/4 , note, d[5], 9);
		note[1] = 35;
		fs.multi_note(time[0]/4 , note, d[5], 9);
		
		note[0] = 38;
		fs.multi_note(time[1]/8 , note, d[5], 9);
		note[0] = 42;
		fs.single_note(time[1]/8, note[0], d[5], 9);
		note[0] = 38;
		fs.single_note(time[1]/4, note[0], d[5], 9);
		note[0] = 42;
		fs.single_note(time[1]/8, note[0], d[5], 9);
		note[0] = 38;
		fs.single_note(time[1]/8, note[0], d[5], 9);
		fs.single_note(time[1]/8, note[1], d[5], 9);
		note[0] = 42;
		fs.single_note(time[1]/8, note[0], d[5], 9);
		
		fs.multi_note(time[2]/8 , note, d[5], 9);
		fs.single_note(time[2]/8, note[0], d[5], 9);
		fs.single_note(time[2]/8, note[0], d[5], 9);
		fs.single_note(time[2]/8, note[0], d[5], 9);
		fs.single_note(time[2]/8, note[0], d[5], 9);
		fs.single_note(time[2]/8, note[0], d[5], 9);
		fs.single_note(time[2]/8, note[0], d[5], 9);
		fs.single_note(time[2]/8, note[1], d[5], 9);
		
		fs.single_note(time[3]/8, note[0], d[5], 9);
		fs.single_note(time[3]/8, note[1], d[5], 9);
		fs.single_note(time[3]/8, note[0], d[5], 9);
		fs.single_note(time[3]/8, note[0], d[5], 9);
		fs.single_note(time[3]/8, note[0], d[5], 9);
		fs.single_note(time[3]/8, note[0], d[5], 9);
		fs.multi_note(time[3]/8 , note, d[5], 9);
		fs.single_note(time[3]/8, note[0], d[5], 9);
		
	}
	protected void style_119(MakeMidi fs, int c, int[] d) {

		time = time(4, 48, d[1], d[3]);
		
		note[0] = 57;
		fs.multi_note(time[0]/2 , note, d[5], 9);
		note[0] = 42; note[1] = 38;
		fs.multi_note(time[0]/2 , note, d[5], 9);
		note[1] = 35;
		fs.multi_note(time[1]/4 , note, d[5], 9);
 		fs.single_note(time[1]/4, note[1], d[5], 9); 
 		note[1] = 38;
		fs.multi_note(time[1]/2 , note, d[5], 9);
		note[1] = 35;
		fs.multi_note(time[2]/2 , note, d[5], 9);
 		note[1] = 38;
		fs.multi_note(time[2]/2 , note, d[5], 9);
		note[1] = 35;
 		fs.single_note(time[3]/4, note[1], d[5], 9);
 		fs.single_note(time[3]/4, note[0], d[5], 9);
 		fs.single_note(time[3]/4, note[1], d[5], 9);
 		note[1] = 38;
		fs.multi_note(time[3]/4 , note, d[5], 9);
		
	}
	protected void style_120(MakeMidi fs, int c, int[] d) {

		time = time(4, 48, d[1], d[3]);
		
		for(int i = 0; i < 4; i++){
		note[0] = 44; //pedal
		note[1] = 64;
		fs.multi_note(time[i]/4 , note, d[5], 9);
		fs.multi_note(time[i]/4 , note, d[5], 9);
		
		note[0] = 42;
		note[1] = 62;
		fs.multi_note(time[i]/6 , note, d[5], 9);
		note[0] = 44;
		fs.single_note(time[i]/6 , note[0], d[5], 9);
		note[1] = 63;
		fs.multi_note(time[i]/6 , note, d[5], 9);
		}
		
	}

	
	protected void setting_chord(int octave, int key, int all_key) {

		int k = 0;
		f = 0;
		f_1 = 0;
		f_2 = 0;
		note[0] = 36 + octave;
		note[1] = 40 + octave;
		note[2] = 43 + octave;
		note[3] = 0;

		for (int t = 0; t < 12; t++) {
			if (transpose[t].equals(b[0] + "/" + b[1]))
				k = t;
		}

		if (k + key < 0)
			key += 12;

		String[] new_chord = transpose[(k + key) % 12].split("/");
		b[0] = new_chord[0];
		b[1] = new_chord[1];

		// 코드 찾아냄
		for (int i = 0; i < 7; i++) {
			if (originapb[i].equals(b[0]))
				temp = i;
		}

		// 코드 분석 (7처리 제대로 하기)
		for (int i = 0; i < 3; i++)
			note[i] += n[temp];

		if (b[1].equals("#")) {
			for (int i = 0; i <= 3; i++)
				note[i] += 1;
		}

		else if (b[1].equals("♭")) {
			for (int i = 0; i <= 3; i++)
				note[i] -= 1;
		}

		if (b[2].equals("m"))
			note[1] -= 1;
		
		if (b[4].equals("6"))
			note[3] += (n[temp] + 45 + octave);
		

		if (b[4].equals("7")){
			note[3] += (n[temp] + 46 + octave);
			if (b[2].equals("M")){
				note[3] ++;
			}
		}
		
		if (b[4].equals("9"))
			note[3] += (n[temp] + 50 + octave);
		
		if (b[4].equals("11"))
			note[3] += (n[temp] + 41 + octave);
	}
	protected int[] time(int size, int div, int t, int d) {

		int[] Time = new int[size];
		d = (48 / size) * d;
		// 리밋제한 할당량
		int limit = 48 * t;
		// 초기화
		for (int i = 0; i < Time.length; i++)
			Time[i] = 0;
		// 리밋 값만큼 박자 넣기
		for (int i = 0; i < limit / div; i++) {
			Time[i] = div + d;
		}

		return Time;

	}

}
