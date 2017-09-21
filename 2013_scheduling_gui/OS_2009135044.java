import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.*;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

class US { //유닉스 스케쥴링 프로세스 클래스
	public String mode; //모드
	public int priority=0; //우선순위
	public int time=0; //시간
	public int basepriority=0; //기본우선순위
	public int cpucount=0; //CPU 카운팅
}


public class OS_2009135044 {
	private static Scanner scan;
	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("UNIX Scheduling"); //프레임 제목
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage // 윈도우 창 로고 이미지
		("D:\\대학 생활\\3학년 1학기\\OS(운영체제)\\kut_logo.gif"));	
		
		//
		//1. GUI와 LOGIC 구현을 위한 기본틀짜기 및 클래스(배열) 생성
		//
		
		Random random = new Random(); //속성 값을 랜덤으로 받기 위한 랜덤값
			
		int a,b,i,j=0;	
		//a는 프로세스 호출할 수 입력
		//b는 프로세스 속성 값에 대해 랜덤 함수를 이용한 임의의 값 부여
		//i,j는 FOR문에 대한 변수들.
		int finish_count=0; // 프로세스 완료되는 수 카운팅
		int time_count=0; // 프로세스가 완료되기까지 걸리는 시간 카운팅
		int time_sum=0; // 프로세스가 완료되기까지 걸리는 시간의 합
		
		scan = new Scanner(System.in);
		a = scan.nextInt(); //프로세스 호출할 수 입력받는다.
		
		//입력 받은 해당 값만큼 각 항목에 배열을 생성한다
		US[] us = new US[a]; //프로세스 클래스
		JLabel [] label  = new JLabel[a]; // 프로세스 모드 이미지
		JLabel [] label_1  = new JLabel[a];// 프로세스 번호
		JPanel [] panel  = new JPanel[a]; // 각 프로세스 속성이 담길 패널
		JLabel [][] panel3_inside_label = new JLabel[a][3]; // 각 프로세스 속성을 출력할 라벨
	
		//배열 크기만큼 각 항목을 생성 
		for (i=0; i<us.length;i++){
			us[i] = new US();
			label[i] = new JLabel();
			label_1[i] = new JLabel(BorderLayout.CENTER);
			label_1[i].setBorder(new LineBorder(new Color(0, 0, 0), 1));
			panel[i] = new JPanel();
			panel[i].setBorder(new LineBorder(new Color(0, 0, 0), 1));
		}
		
		//패널3 (각 프로세스 속성)에 대한 이차원 배열 설정
		for (i=0; i<us.length;i++){
			for (j=0; j<3;j++)
			panel3_inside_label[i][j] = new JLabel(BorderLayout.CENTER);
		}
		
		
		//
		//2. 패널 틀 짜기
		//
		
		
		//그래픽 틀 생성 (콘텐트패인, 패널), 패널은 y축 정렬이 된다.
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS)); 
		
		//패널 1 상단- 프로그램 정보 패널
		JPanel panel1 = new JPanel(new BorderLayout());
		frame.getContentPane().add(panel1, BorderLayout.CENTER);
		panel1.setBorder(new LineBorder(new Color(0, 0, 0), 1));

		//패널 2 중단-그림 패널
		JPanel panel2 = new JPanel(new BorderLayout());
		frame.getContentPane().add(panel2, BorderLayout.CENTER);

		//패널 2_0 중단- 프로세스 번호 패널
		JPanel panel2_0 = new JPanel(new BorderLayout());
		frame.getContentPane().add(panel2_0, BorderLayout.CENTER);
		
		//패널 3 중단- 프로세스 정보 패널
		JPanel panel3 = new JPanel(new BorderLayout());
		frame.getContentPane().add(panel3, BorderLayout.CENTER);	
	
		//하단 라벨 "프로세스 시간 순서도"
		JLabel lblNewLabel_1 = new JLabel("프로세스 시간 순서도");
		lblNewLabel_1.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblNewLabel_1.setFont(new Font("함초롬바탕", Font.BOLD, 15));
		frame.getContentPane().add(lblNewLabel_1);
	
		//패널 4 하단 - 프로세스 시간 순서도 패널
		JPanel panel4 = new JPanel(new BorderLayout());
		frame.getContentPane().add(panel4);
		//패널 4_1 하단 - 프로세스 아이콘 시간별 생성 패널
		JPanel panel4_1 = new JPanel(new BorderLayout());
		panel4.add(panel4_1);

		//윈도우 창의 축소 방지를 위한 라벨 생성
		JLabel lblNewLabel = new JLabel("                  ");
		frame.getContentPane().add(lblNewLabel);
		
		
		//
		//3. 패널(1~3)까지의 내용 생성 (LOGIC 일부 구현)
		//
		
		
		//상단 패널(panel 1번) 내용 생성 (GUI)
		JLabel label5  = new JLabel();
		JLabel label6  = new JLabel();
		JLabel label7  = new JLabel();
		panel1.setLayout(new GridLayout(3,0));
		panel1.add(label5);
		panel1.add(label6);
		panel1.add(label7);
		label5.setText("실행 중인 프로세스: "+Integer.toString(a) + "\n");
		label6.setText("완료 된 프로세스: "+Integer.toString(finish_count) + "\n");
		label7.setText("총 실행 시간: "+Integer.toString(time_count) + "\n");
		label5.setFont(new Font("함초롬바탕", Font.BOLD, 15));
		label6.setFont(new Font("함초롬바탕", Font.BOLD, 15));
		label7.setFont(new Font("함초롬바탕", Font.BOLD, 15));
	


		//중단 패널(panel 2번, 2_0번, 3번) 내용 생성 (GUI, LOGIC)
		
		for (i=0; i<us.length;i++){ //초기 화면 생성 과정
			
			//중단 그림과 프로세스 번호에 대한 레이어 설정
			panel2.setLayout(new GridLayout(0,a));
			panel2_0.setLayout(new GridLayout(0,a));
			
			//커널 모드와 유저 모드를 랜덤으로 생성하기 위한 int형 변수 생성
			b = random.nextInt(2);
			
			//우선순위와 시간도 랜덤 값으로 받는다.
			//어떤 모드냐에 따라 중단에 생성되는 이미지가 다르다.
			//더불어 프로세스 번호도 출력.

			if(b == 0){ //커널
			us[i].mode = "Kernal";
			us[i].priority=  1 + random.nextInt(100);
			us[i].time =  1 + random.nextInt(3);
			us[i].basepriority = 40;
			
			panel2.add(label[i]);
			label[i].setIcon(new ImageIcon("D:\\대학 생활\\3학년 1학기\\OS(운영체제)\\2_k.jpg"));	
			
			panel2_0.add(label_1[i]);
			label_1[i].setText("Process" + (i+1));
			label_1[i].setFont(new Font("함초롬바탕", Font.BOLD, 15));
			label_1[i].setHorizontalAlignment(SwingConstants.CENTER);
			}

			else if(b == 1){ //사용자
			us[i].mode="User";
			us[i].priority=  1 + random.nextInt(100);
			us[i].time =   1 + random.nextInt(3);
			us[i].basepriority = 60;
			
			panel2.add(label[i]);
			label[i].setIcon(new ImageIcon("D:\\대학 생활\\3학년 1학기\\OS(운영체제)\\2_u.jpg"));
			
			panel2_0.add(label_1[i]);
			label_1[i].setText("Process" + (i+1));
			label_1[i].setFont(new Font("함초롬바탕", Font.BOLD, 15));
			label_1[i].setHorizontalAlignment(SwingConstants.CENTER);
			}		
			
			
			//중단 패널(panel 3번) 내용 생성 (GUI)
			panel3.setLayout(new GridLayout(0,a));
			
			//중단 패널에 입력한 값만큼 패널을 만들고 그 안에 각 프로세스의 속성 값들을 출력한다.
			panel3.add(panel[i]);
			panel[i].setLayout(new BoxLayout(panel[i], BoxLayout.Y_AXIS));
			
			panel[i].add(panel3_inside_label[i][0]);
			panel[i].add(panel3_inside_label[i][1]);
			panel[i].add(panel3_inside_label[i][2]);
			panel3_inside_label[i][0].setText("우선순위: "+Integer.toString(us[i].priority) + "\n");
			panel3_inside_label[i][1].setText("CPUCOUNT: "+Integer.toString(us[i].cpucount) + "\n");
			panel3_inside_label[i][2].setText("할당 시간: "+Integer.toString(us[i].time) + "초\n");			
			//글씨체 밑 문장 위치 설정

			panel3_inside_label[i][0].setFont(new Font("함초롬바탕", Font.BOLD, 15));
			panel3_inside_label[i][1].setFont(new Font("함초롬바탕", Font.BOLD, 15));
			panel3_inside_label[i][2].setFont(new Font("함초롬바탕", Font.BOLD, 15));
			panel3_inside_label[i][0].setAlignmentX(Component.CENTER_ALIGNMENT);
			panel3_inside_label[i][1].setAlignmentX(Component.CENTER_ALIGNMENT);
			panel3_inside_label[i][2].setAlignmentX(Component.CENTER_ALIGNMENT);
			//하단 패널의 그래픽 효과를 위해 시간의 합을 구한다.
			time_sum=time_sum+us[i].time;
		}


		//기타 GUI 설정, 사이즈 조절 금지
		frame.setLocation(100,350);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		//
		//4. 패널4 내용 생성 및 LOGIC을 구현
		//   유닉스 스케쥴링 개념을 소스코드화
		//   우선순위에 따른 경우를 통해 연산 실행
		//   매 단계마다 달라지는 패널들의 구성요소를 통한 시각적인 효과 연출
		
		
		// 커널 모드인 프로세스의 수 측정
		int k=0;
		for (i=0; i<a; i++){
			if(us[i].mode == "Kernal") {
				k++;
		}		
		}
		
		while (finish_count <= a){// 연산 시작

			//모든 프로세스가 완료되면 WHILE문 탈출
			if (finish_count ==a){	
			System.out.println("완료");
				break;
			}
			//매 시간마다 시간을 카운팅 하여 상단 패널 "총 실행 시간"에 값 출력
			time_count++;
			label7.setText("총 실행 시간: "+Integer.toString(time_count) + "초\n");
			
			//우선 순위를 받을 인자 생성, 우선순위를 가늠할 프로세스 개수만큼의 배열 생성
			int temp=0;
			int  [] order = new int [a];
		

		// 커널 모드인 프로세스가 다 실행될 때까지의 우선순위 구하는 알고리즘
		if (finish_count < k){
		for (i=0; i<a; i++){
			if(us[i].time != 0 && us[i].mode == "Kernal") { //할당 시간이 남은 동시에 '그것이 커널'이면
				order[i] = us[i].priority; //우선순위 배열에 값을 넣는다.
			}
			else //시간이 다된 프로세스나 커널이 아닌 프로세스는 
				order[i] = 1000; // 가지고있는 우선순위 값이 아닌 높은 값을 준다.
		}
		
		for (i=0; i<a; i++){ // 우선순위 배열의 최소값을 temp에 담는다.
				 if (order[temp] > order[i]){
						 temp=i;
					 }		
			}}
		
		//모든 커널 모드 프로세스 실행 이후 유저 모드 프로세스들 사이에서 우선순위 구하는 알고리즘 (설명 생략)
		else{

			for (i=0; i<a; i++){
				if(us[i].time != 0) {
					order[i] = us[i].priority;
				}
				else
					order[i] = 1000;
			}
			
			for (i=0; i<a; i++){
					 if (order[temp] > order[i]){
							 temp=i;
						 }		
				}
		}
		
		
			for (i=0; i < a ; i++){ // 프로세스 수만큼 돌리기
				if (i == temp && (us[i].cpucount==0 || us[i].cpucount!=0) && us[i].time !=0){ 
					//경우[1]: 우선순위값이 가장 낮은 실행 대상 프로세스, CPUCOUNT=0 
					// 이미 한번 실행이 되었던 상태일 경우

					//하단 패널(panel 4번) 내용 생성 (GUI)
					//이 경우는 하단 패널에 대해 시간 순서도를 위한 이미지 추가에 대한 레이어 설정을 해준다.
					//총 시간만큼 이미지가 채워지므로 앞서 구한 time_sum만큼 GridLayout 적용
					panel4_1.setLayout(new GridLayout(0,time_sum));

					JPanel panel4_k  = new JPanel();
					JLabel panel_label_4_k  = new JLabel();
					panel4_1.add(panel4_k);
					panel4_k.add(panel_label_4_k);
					
					// 논리 연산에 따른 속성 값과 그래픽 변화 출력
					
					if (us[i].mode == "Kernal"){
					//실행중의 이미지로 변경
					label[i].setIcon(new ImageIcon("D:\\대학 생활\\3학년 1학기\\OS(운영체제)\\0_k.jpg"));
					//프로세스 번호와 함께 시간에 대한 모드 이미지 하단부 출력
					panel4_k.setBackground(new Color(51, 204, 255));
					panel_label_4_k.setText(Integer.toString(temp+1));
					panel_label_4_k.setFont(new Font("굴림", Font.PLAIN, 12));
					}
					
					//유저 모드도 마찬가지로 경우를 적용시킨다.
					else if (us[i].mode == "User"){
					label[i].setIcon(new ImageIcon("D:\\대학 생활\\3학년 1학기\\OS(운영체제)\\0_u.jpg"));
					panel4_k.setBackground(new Color(0,255,0));
					panel_label_4_k.setText(Integer.toString(temp+1));
					panel_label_4_k.setFont(new Font("굴림", Font.PLAIN, 12));
					}
					
					//실행 프로세스에 대한 시각적인 효과 (배경색, 폰트색 변경)
					panel[i].setBackground(new Color(255, 255, 153));
					panel3_inside_label[i][1].setForeground(new Color(255, 0, 0));
					panel3_inside_label[i][0].setForeground(new Color(255, 0, 0));
					panel3_inside_label[i][2].setForeground(new Color(255, 0, 0));
					
					//실행 프로세스에 대한 시각적인 효과 (CPU COUNT 값 증가)
					for (int x=0;x<60;x++){
						us[i].cpucount = us[i].cpucount+1;
						panel3_inside_label[i][1].setText("CPUCOUNT: "+Integer.toString(us[i].cpucount) + "\n");
						try{
							Thread.sleep(18);
						}
						catch(InterruptedException ignore){}
					}
					
					//유닉스 스케쥴링 논리 연산 실행
						us[i].cpucount = us[i].cpucount/2;
						us[i].priority = us[i].basepriority +us[i].cpucount/2;
						us[i].time -= 1;	
					//실행 프로세스에 대한 시각적인 효과 (논리 연산 결과 값 출력)
						panel3_inside_label[i][0].setText("우선순위: "+Integer.toString(us[i].priority) + "\n");
						panel3_inside_label[i][2].setText("할당 시간: "+Integer.toString(us[i].time) + "\n");
						panel3_inside_label[i][1].setText("CPUCOUNT: "+Integer.toString(us[i].cpucount) + "\n");
						
					//연산이 끝나면 실행 대기 상태의 이미지를 다시 출력한다.
						if (us[i].mode == "Kernal"){
							label[i].setIcon(new ImageIcon("D:\\대학 생활\\3학년 1학기\\OS(운영체제)\\2_k.jpg"));
							}
							else if (us[i].mode == "User"){
							label[i].setIcon(new ImageIcon("D:\\대학 생활\\3학년 1학기\\OS(운영체제)\\2_u.jpg"));
							}
						
					//시간을 할당하는 이 연산의 경우 중, 프로세스가 완료되는 (시간이 0이 되는) 시점이 있다면
						if (us[i].time == 0)
						{	
						 finish_count++; // 완료된 프로세스의 수를 카운팅
						 
						//패널 상단부 프로그램이 정보 업데이트
						label5.setText("실행중인 프로세스: "+Integer.toString(a-finish_count) + "\n");
						label6.setText("완료된 프로세스: "+Integer.toString(finish_count) + "\n");
						
						//패널 중단부 프로세스에 속성에 대한 값 변경
						panel3_inside_label[i][1].setText("실행 완료");
						panel3_inside_label[i][0].setText(" ");
						panel3_inside_label[i][2].setText(" ");
						
						
						//실행이 완료된 프로세스에 대한 이미지 변경
						if (us[i].mode == "Kernal")
						label[i].setIcon(new ImageIcon("D:\\대학 생활\\3학년 1학기\\OS(운영체제)\\4_k.jpg"));
						else
						label[i].setIcon(new ImageIcon("D:\\대학 생활\\3학년 1학기\\OS(운영체제)\\4_u.jpg"));
						}
						
						panel[i].setBackground(new Color(240, 240, 240));
						panel3_inside_label[i][1].setForeground(new Color(0,0,0));
						panel3_inside_label[i][0].setForeground(new Color(0,0,0));
						panel3_inside_label[i][2].setForeground(new Color(0,0,0));
				}

				else if (i != temp && us[i].cpucount == 0 && us[i].time !=0){//2. 연산대상은 아니고, 실행도 안되었을 때 
					
					//변경 값이 없으므로 그대로 간다.
					try{
						Thread.sleep(1);
					}
					catch(InterruptedException ignore){}
				}

				else if(i != temp && us[i].cpucount != 0 && us[i].time !=0){//3. 연산대상은 아니지만, 이미 실행이 된 경우
					
					us[i].cpucount = us[i].cpucount/2;//CPUCOUNT 값 증가 없이 1/2 연산 실행
					panel3_inside_label[i][1].setText("CPUCOUNT: "+Integer.toString(us[i].cpucount) + "\n");
					
					us[i].priority = us[i].basepriority +us[i].cpucount/2;
					panel3_inside_label[i][0].setText("우선순위: "+Integer.toString(us[i].priority) + "\n");
					try{
						Thread.sleep(1);
					}
					catch(InterruptedException ignore){}

			}
				try{
					Thread.sleep(1);
				}
				catch(InterruptedException ignore){}
			}
			} // 부분 FOR문이 끝나면 딜레이
				
			try{
				Thread.sleep(1);
			}
			catch(InterruptedException ignore){} //전체 FOR문이 끝나면 딜레이
			}	//연산 끝	
	} //main, class

