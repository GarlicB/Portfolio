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

class US { //���н� �����층 ���μ��� Ŭ����
	public String mode; //���
	public int priority=0; //�켱����
	public int time=0; //�ð�
	public int basepriority=0; //�⺻�켱����
	public int cpucount=0; //CPU ī����
}


public class OS_2009135044 {
	private static Scanner scan;
	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("UNIX Scheduling"); //������ ����
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage // ������ â �ΰ� �̹���
		("D:\\���� ��Ȱ\\3�г� 1�б�\\OS(�ü��)\\kut_logo.gif"));	
		
		//
		//1. GUI�� LOGIC ������ ���� �⺻Ʋ¥�� �� Ŭ����(�迭) ����
		//
		
		Random random = new Random(); //�Ӽ� ���� �������� �ޱ� ���� ������
			
		int a,b,i,j=0;	
		//a�� ���μ��� ȣ���� �� �Է�
		//b�� ���μ��� �Ӽ� ���� ���� ���� �Լ��� �̿��� ������ �� �ο�
		//i,j�� FOR���� ���� ������.
		int finish_count=0; // ���μ��� �Ϸ�Ǵ� �� ī����
		int time_count=0; // ���μ����� �Ϸ�Ǳ���� �ɸ��� �ð� ī����
		int time_sum=0; // ���μ����� �Ϸ�Ǳ���� �ɸ��� �ð��� ��
		
		scan = new Scanner(System.in);
		a = scan.nextInt(); //���μ��� ȣ���� �� �Է¹޴´�.
		
		//�Է� ���� �ش� ����ŭ �� �׸� �迭�� �����Ѵ�
		US[] us = new US[a]; //���μ��� Ŭ����
		JLabel [] label  = new JLabel[a]; // ���μ��� ��� �̹���
		JLabel [] label_1  = new JLabel[a];// ���μ��� ��ȣ
		JPanel [] panel  = new JPanel[a]; // �� ���μ��� �Ӽ��� ��� �г�
		JLabel [][] panel3_inside_label = new JLabel[a][3]; // �� ���μ��� �Ӽ��� ����� ��
	
		//�迭 ũ�⸸ŭ �� �׸��� ���� 
		for (i=0; i<us.length;i++){
			us[i] = new US();
			label[i] = new JLabel();
			label_1[i] = new JLabel(BorderLayout.CENTER);
			label_1[i].setBorder(new LineBorder(new Color(0, 0, 0), 1));
			panel[i] = new JPanel();
			panel[i].setBorder(new LineBorder(new Color(0, 0, 0), 1));
		}
		
		//�г�3 (�� ���μ��� �Ӽ�)�� ���� ������ �迭 ����
		for (i=0; i<us.length;i++){
			for (j=0; j<3;j++)
			panel3_inside_label[i][j] = new JLabel(BorderLayout.CENTER);
		}
		
		
		//
		//2. �г� Ʋ ¥��
		//
		
		
		//�׷��� Ʋ ���� (����Ʈ����, �г�), �г��� y�� ������ �ȴ�.
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS)); 
		
		//�г� 1 ���- ���α׷� ���� �г�
		JPanel panel1 = new JPanel(new BorderLayout());
		frame.getContentPane().add(panel1, BorderLayout.CENTER);
		panel1.setBorder(new LineBorder(new Color(0, 0, 0), 1));

		//�г� 2 �ߴ�-�׸� �г�
		JPanel panel2 = new JPanel(new BorderLayout());
		frame.getContentPane().add(panel2, BorderLayout.CENTER);

		//�г� 2_0 �ߴ�- ���μ��� ��ȣ �г�
		JPanel panel2_0 = new JPanel(new BorderLayout());
		frame.getContentPane().add(panel2_0, BorderLayout.CENTER);
		
		//�г� 3 �ߴ�- ���μ��� ���� �г�
		JPanel panel3 = new JPanel(new BorderLayout());
		frame.getContentPane().add(panel3, BorderLayout.CENTER);	
	
		//�ϴ� �� "���μ��� �ð� ������"
		JLabel lblNewLabel_1 = new JLabel("���μ��� �ð� ������");
		lblNewLabel_1.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblNewLabel_1.setFont(new Font("���ʷҹ���", Font.BOLD, 15));
		frame.getContentPane().add(lblNewLabel_1);
	
		//�г� 4 �ϴ� - ���μ��� �ð� ������ �г�
		JPanel panel4 = new JPanel(new BorderLayout());
		frame.getContentPane().add(panel4);
		//�г� 4_1 �ϴ� - ���μ��� ������ �ð��� ���� �г�
		JPanel panel4_1 = new JPanel(new BorderLayout());
		panel4.add(panel4_1);

		//������ â�� ��� ������ ���� �� ����
		JLabel lblNewLabel = new JLabel("                  ");
		frame.getContentPane().add(lblNewLabel);
		
		
		//
		//3. �г�(1~3)������ ���� ���� (LOGIC �Ϻ� ����)
		//
		
		
		//��� �г�(panel 1��) ���� ���� (GUI)
		JLabel label5  = new JLabel();
		JLabel label6  = new JLabel();
		JLabel label7  = new JLabel();
		panel1.setLayout(new GridLayout(3,0));
		panel1.add(label5);
		panel1.add(label6);
		panel1.add(label7);
		label5.setText("���� ���� ���μ���: "+Integer.toString(a) + "\n");
		label6.setText("�Ϸ� �� ���μ���: "+Integer.toString(finish_count) + "\n");
		label7.setText("�� ���� �ð�: "+Integer.toString(time_count) + "\n");
		label5.setFont(new Font("���ʷҹ���", Font.BOLD, 15));
		label6.setFont(new Font("���ʷҹ���", Font.BOLD, 15));
		label7.setFont(new Font("���ʷҹ���", Font.BOLD, 15));
	


		//�ߴ� �г�(panel 2��, 2_0��, 3��) ���� ���� (GUI, LOGIC)
		
		for (i=0; i<us.length;i++){ //�ʱ� ȭ�� ���� ����
			
			//�ߴ� �׸��� ���μ��� ��ȣ�� ���� ���̾� ����
			panel2.setLayout(new GridLayout(0,a));
			panel2_0.setLayout(new GridLayout(0,a));
			
			//Ŀ�� ���� ���� ��带 �������� �����ϱ� ���� int�� ���� ����
			b = random.nextInt(2);
			
			//�켱������ �ð��� ���� ������ �޴´�.
			//� ���Ŀ� ���� �ߴܿ� �����Ǵ� �̹����� �ٸ���.
			//���Ҿ� ���μ��� ��ȣ�� ���.

			if(b == 0){ //Ŀ��
			us[i].mode = "Kernal";
			us[i].priority=  1 + random.nextInt(100);
			us[i].time =  1 + random.nextInt(3);
			us[i].basepriority = 40;
			
			panel2.add(label[i]);
			label[i].setIcon(new ImageIcon("D:\\���� ��Ȱ\\3�г� 1�б�\\OS(�ü��)\\2_k.jpg"));	
			
			panel2_0.add(label_1[i]);
			label_1[i].setText("Process" + (i+1));
			label_1[i].setFont(new Font("���ʷҹ���", Font.BOLD, 15));
			label_1[i].setHorizontalAlignment(SwingConstants.CENTER);
			}

			else if(b == 1){ //�����
			us[i].mode="User";
			us[i].priority=  1 + random.nextInt(100);
			us[i].time =   1 + random.nextInt(3);
			us[i].basepriority = 60;
			
			panel2.add(label[i]);
			label[i].setIcon(new ImageIcon("D:\\���� ��Ȱ\\3�г� 1�б�\\OS(�ü��)\\2_u.jpg"));
			
			panel2_0.add(label_1[i]);
			label_1[i].setText("Process" + (i+1));
			label_1[i].setFont(new Font("���ʷҹ���", Font.BOLD, 15));
			label_1[i].setHorizontalAlignment(SwingConstants.CENTER);
			}		
			
			
			//�ߴ� �г�(panel 3��) ���� ���� (GUI)
			panel3.setLayout(new GridLayout(0,a));
			
			//�ߴ� �гο� �Է��� ����ŭ �г��� ����� �� �ȿ� �� ���μ����� �Ӽ� ������ ����Ѵ�.
			panel3.add(panel[i]);
			panel[i].setLayout(new BoxLayout(panel[i], BoxLayout.Y_AXIS));
			
			panel[i].add(panel3_inside_label[i][0]);
			panel[i].add(panel3_inside_label[i][1]);
			panel[i].add(panel3_inside_label[i][2]);
			panel3_inside_label[i][0].setText("�켱����: "+Integer.toString(us[i].priority) + "\n");
			panel3_inside_label[i][1].setText("CPUCOUNT: "+Integer.toString(us[i].cpucount) + "\n");
			panel3_inside_label[i][2].setText("�Ҵ� �ð�: "+Integer.toString(us[i].time) + "��\n");			
			//�۾�ü �� ���� ��ġ ����

			panel3_inside_label[i][0].setFont(new Font("���ʷҹ���", Font.BOLD, 15));
			panel3_inside_label[i][1].setFont(new Font("���ʷҹ���", Font.BOLD, 15));
			panel3_inside_label[i][2].setFont(new Font("���ʷҹ���", Font.BOLD, 15));
			panel3_inside_label[i][0].setAlignmentX(Component.CENTER_ALIGNMENT);
			panel3_inside_label[i][1].setAlignmentX(Component.CENTER_ALIGNMENT);
			panel3_inside_label[i][2].setAlignmentX(Component.CENTER_ALIGNMENT);
			//�ϴ� �г��� �׷��� ȿ���� ���� �ð��� ���� ���Ѵ�.
			time_sum=time_sum+us[i].time;
		}


		//��Ÿ GUI ����, ������ ���� ����
		frame.setLocation(100,350);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		//
		//4. �г�4 ���� ���� �� LOGIC�� ����
		//   ���н� �����층 ������ �ҽ��ڵ�ȭ
		//   �켱������ ���� ��츦 ���� ���� ����
		//   �� �ܰ踶�� �޶����� �гε��� ������Ҹ� ���� �ð����� ȿ�� ����
		
		
		// Ŀ�� ����� ���μ����� �� ����
		int k=0;
		for (i=0; i<a; i++){
			if(us[i].mode == "Kernal") {
				k++;
		}		
		}
		
		while (finish_count <= a){// ���� ����

			//��� ���μ����� �Ϸ�Ǹ� WHILE�� Ż��
			if (finish_count ==a){	
			System.out.println("�Ϸ�");
				break;
			}
			//�� �ð����� �ð��� ī���� �Ͽ� ��� �г� "�� ���� �ð�"�� �� ���
			time_count++;
			label7.setText("�� ���� �ð�: "+Integer.toString(time_count) + "��\n");
			
			//�켱 ������ ���� ���� ����, �켱������ ������ ���μ��� ������ŭ�� �迭 ����
			int temp=0;
			int  [] order = new int [a];
		

		// Ŀ�� ����� ���μ����� �� ����� �������� �켱���� ���ϴ� �˰���
		if (finish_count < k){
		for (i=0; i<a; i++){
			if(us[i].time != 0 && us[i].mode == "Kernal") { //�Ҵ� �ð��� ���� ���ÿ� '�װ��� Ŀ��'�̸�
				order[i] = us[i].priority; //�켱���� �迭�� ���� �ִ´�.
			}
			else //�ð��� �ٵ� ���μ����� Ŀ���� �ƴ� ���μ����� 
				order[i] = 1000; // �������ִ� �켱���� ���� �ƴ� ���� ���� �ش�.
		}
		
		for (i=0; i<a; i++){ // �켱���� �迭�� �ּҰ��� temp�� ��´�.
				 if (order[temp] > order[i]){
						 temp=i;
					 }		
			}}
		
		//��� Ŀ�� ��� ���μ��� ���� ���� ���� ��� ���μ����� ���̿��� �켱���� ���ϴ� �˰��� (���� ����)
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
		
		
			for (i=0; i < a ; i++){ // ���μ��� ����ŭ ������
				if (i == temp && (us[i].cpucount==0 || us[i].cpucount!=0) && us[i].time !=0){ 
					//���[1]: �켱�������� ���� ���� ���� ��� ���μ���, CPUCOUNT=0 
					// �̹� �ѹ� ������ �Ǿ��� ������ ���

					//�ϴ� �г�(panel 4��) ���� ���� (GUI)
					//�� ���� �ϴ� �гο� ���� �ð� �������� ���� �̹��� �߰��� ���� ���̾� ������ ���ش�.
					//�� �ð���ŭ �̹����� ä�����Ƿ� �ռ� ���� time_sum��ŭ GridLayout ����
					panel4_1.setLayout(new GridLayout(0,time_sum));

					JPanel panel4_k  = new JPanel();
					JLabel panel_label_4_k  = new JLabel();
					panel4_1.add(panel4_k);
					panel4_k.add(panel_label_4_k);
					
					// �� ���꿡 ���� �Ӽ� ���� �׷��� ��ȭ ���
					
					if (us[i].mode == "Kernal"){
					//�������� �̹����� ����
					label[i].setIcon(new ImageIcon("D:\\���� ��Ȱ\\3�г� 1�б�\\OS(�ü��)\\0_k.jpg"));
					//���μ��� ��ȣ�� �Բ� �ð��� ���� ��� �̹��� �ϴܺ� ���
					panel4_k.setBackground(new Color(51, 204, 255));
					panel_label_4_k.setText(Integer.toString(temp+1));
					panel_label_4_k.setFont(new Font("����", Font.PLAIN, 12));
					}
					
					//���� ��嵵 ���������� ��츦 �����Ų��.
					else if (us[i].mode == "User"){
					label[i].setIcon(new ImageIcon("D:\\���� ��Ȱ\\3�г� 1�б�\\OS(�ü��)\\0_u.jpg"));
					panel4_k.setBackground(new Color(0,255,0));
					panel_label_4_k.setText(Integer.toString(temp+1));
					panel_label_4_k.setFont(new Font("����", Font.PLAIN, 12));
					}
					
					//���� ���μ����� ���� �ð����� ȿ�� (����, ��Ʈ�� ����)
					panel[i].setBackground(new Color(255, 255, 153));
					panel3_inside_label[i][1].setForeground(new Color(255, 0, 0));
					panel3_inside_label[i][0].setForeground(new Color(255, 0, 0));
					panel3_inside_label[i][2].setForeground(new Color(255, 0, 0));
					
					//���� ���μ����� ���� �ð����� ȿ�� (CPU COUNT �� ����)
					for (int x=0;x<60;x++){
						us[i].cpucount = us[i].cpucount+1;
						panel3_inside_label[i][1].setText("CPUCOUNT: "+Integer.toString(us[i].cpucount) + "\n");
						try{
							Thread.sleep(18);
						}
						catch(InterruptedException ignore){}
					}
					
					//���н� �����층 �� ���� ����
						us[i].cpucount = us[i].cpucount/2;
						us[i].priority = us[i].basepriority +us[i].cpucount/2;
						us[i].time -= 1;	
					//���� ���μ����� ���� �ð����� ȿ�� (�� ���� ��� �� ���)
						panel3_inside_label[i][0].setText("�켱����: "+Integer.toString(us[i].priority) + "\n");
						panel3_inside_label[i][2].setText("�Ҵ� �ð�: "+Integer.toString(us[i].time) + "\n");
						panel3_inside_label[i][1].setText("CPUCOUNT: "+Integer.toString(us[i].cpucount) + "\n");
						
					//������ ������ ���� ��� ������ �̹����� �ٽ� ����Ѵ�.
						if (us[i].mode == "Kernal"){
							label[i].setIcon(new ImageIcon("D:\\���� ��Ȱ\\3�г� 1�б�\\OS(�ü��)\\2_k.jpg"));
							}
							else if (us[i].mode == "User"){
							label[i].setIcon(new ImageIcon("D:\\���� ��Ȱ\\3�г� 1�б�\\OS(�ü��)\\2_u.jpg"));
							}
						
					//�ð��� �Ҵ��ϴ� �� ������ ��� ��, ���μ����� �Ϸ�Ǵ� (�ð��� 0�� �Ǵ�) ������ �ִٸ�
						if (us[i].time == 0)
						{	
						 finish_count++; // �Ϸ�� ���μ����� ���� ī����
						 
						//�г� ��ܺ� ���α׷��� ���� ������Ʈ
						label5.setText("�������� ���μ���: "+Integer.toString(a-finish_count) + "\n");
						label6.setText("�Ϸ�� ���μ���: "+Integer.toString(finish_count) + "\n");
						
						//�г� �ߴܺ� ���μ����� �Ӽ��� ���� �� ����
						panel3_inside_label[i][1].setText("���� �Ϸ�");
						panel3_inside_label[i][0].setText(" ");
						panel3_inside_label[i][2].setText(" ");
						
						
						//������ �Ϸ�� ���μ����� ���� �̹��� ����
						if (us[i].mode == "Kernal")
						label[i].setIcon(new ImageIcon("D:\\���� ��Ȱ\\3�г� 1�б�\\OS(�ü��)\\4_k.jpg"));
						else
						label[i].setIcon(new ImageIcon("D:\\���� ��Ȱ\\3�г� 1�б�\\OS(�ü��)\\4_u.jpg"));
						}
						
						panel[i].setBackground(new Color(240, 240, 240));
						panel3_inside_label[i][1].setForeground(new Color(0,0,0));
						panel3_inside_label[i][0].setForeground(new Color(0,0,0));
						panel3_inside_label[i][2].setForeground(new Color(0,0,0));
				}

				else if (i != temp && us[i].cpucount == 0 && us[i].time !=0){//2. �������� �ƴϰ�, ���൵ �ȵǾ��� �� 
					
					//���� ���� �����Ƿ� �״�� ����.
					try{
						Thread.sleep(1);
					}
					catch(InterruptedException ignore){}
				}

				else if(i != temp && us[i].cpucount != 0 && us[i].time !=0){//3. �������� �ƴ�����, �̹� ������ �� ���
					
					us[i].cpucount = us[i].cpucount/2;//CPUCOUNT �� ���� ���� 1/2 ���� ����
					panel3_inside_label[i][1].setText("CPUCOUNT: "+Integer.toString(us[i].cpucount) + "\n");
					
					us[i].priority = us[i].basepriority +us[i].cpucount/2;
					panel3_inside_label[i][0].setText("�켱����: "+Integer.toString(us[i].priority) + "\n");
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
			} // �κ� FOR���� ������ ������
				
			try{
				Thread.sleep(1);
			}
			catch(InterruptedException ignore){} //��ü FOR���� ������ ������
			}	//���� ��	
	} //main, class

