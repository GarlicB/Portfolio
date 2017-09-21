#include <gl/GLaux.h>
#include <stdio.h>
#include <glut.h>
#include <gl/glu.h>
#include "glm.h"
#include "math.h"
#include <Windows.h>
#include <mmsystem.h> //MCI헤더
#include "digitalv.h"
#pragma comment(lib,"winmm.lib") //MCI사용하기 위한 라이브러리
#include "atlstr.h"
#include <tchar.h>
MCI_OPEN_PARMS mciOpen;
MCI_PLAY_PARMS mciPlay;
int dwID_bgm; // 미디어 ID (하나의 미디어 콘텐츠 객체인 셈)
int dwID_eff_1;
int dwID_eff_2;
int dwID_eff_3;
static POINT	ptLastMousePosit;
static POINT	ptCurrentMousePosit;
static bool		bMousing;
//////////////////////////////////////
float g_fDistance = -5.0f;
float g_fSpinX    = 0.0f;
float g_fSpinY    = 0.0f, nY = 0;
float zoom_g_fSpinY = 0;
/////////////////////////////////////
int begin = 0;
int window_x, window_y;
/////////////////////////////////////
GLUquadricObj *obj = gluNewQuadric();
GLUquadricObj  *p;
GLMmodel* pmodel_sniper = NULL;
GLMmodel* pmodel_wall = NULL;
GLMmodel* pmodel = NULL;
///////////////////////////////////
#define Pi                  3.1415
#define MAX_COLOR           9
#define CLR_BLACK           0
#define CLR_WHITE           1
#define CLR_RED             2
#define CLR_GREEN           3
#define CLR_BLUE            4
#define CLR_YELLOW          5
#define CLR_MAGENTA         6
#define CLR_CYAN            7
#define CLR_ORANGE          8
#define MAX_COLOR           9
float g_aColor[MAX_COLOR][3] =
{
    { 0.0f, 0.0f, 0.0f },   // Black
    { 1.0f, 1.0f, 1.0f },   // White
    { 1.0f, 0.0f, 0.0f },   // Red
    { 0.0f, 1.0f, 0.0f },   // Green
    { 0.0f, 0.0f, 1.0f },   // Blue
    { 1.0f, 1.0f, 0.0f },   // Yellow
    { 1.0f, 0.0f, 1.0f },   // Magenta
    { 0.0f, 1.0f, 1.0f },   // Cyan
    { 1.0f, 0.6f, 0.0f }    // Orange
};
char g_szMsg[100];
///////////////////////////////////
GLdouble planex = 0;
GLdouble planey = 305;   // -> 320
GLdouble planez = 5;   // -65 or 65
GLfloat roll = 0;
GLfloat pitch = 0;
GLfloat yaw = 0;
///////////////////////////////////
GLdouble zoom_planex = 0;
GLdouble zoom_planey = 242;   // -> 320
GLdouble zoom_planez = 700;   // -65 or 65
GLfloat zoom_roll = 0;
GLfloat zoom_pitch = 0;
GLfloat zoom_yaw = 0;
////////////////////////////////////
GLint back_y = -100, back_z = 0;
GLint building_pos_z = -1000;
////////////////////////////////////
GLfloat gun_rot_x = 6.5;
GLfloat gun_rot_y = 191;
GLfloat gun_rot_z = 0;
//////////////////////////////////
GLfloat gun_x = 1;
GLfloat gun_z = -6;
////////////////////////////////////
GLfloat  mouse_move_z = 44;
GLfloat theta = 0;
GLint bx=-3, by=-2, bz=5;
//////////////////////////////////////////
GLfloat target_x = 0, target_y = 0, target_z = 0;
GLint target_state = 0;
GLint target_state_y = 0;
GLint target_state_z = 0;
GLint target_state_size = 0;
GLfloat rotate_x = 0;
GLfloat rotate_y = 0;
GLfloat rotate_z = 0; //추가
GLfloat target_size = 1;
GLint size_count = 0;
GLint bullet_count = 5;
GLint blink_count = 0;
GLint blink_on = 0;
GLint blink_max = 10;
GLfloat speed = 25;
GLint random_state = 0;
GLint random_count = 0;
//////////////////////////////////////////
GLint zoomin_mode = 1;
GLint fovy = 45;
//////////////////////////////////////////
int game_stage = 0;
int temp=game_stage;
GLfloat target_position[20][2];
GLfloat a,b;
GLint remain_time = 60;
GLint time_count = 40;
GLint rot_count = 0;
GLint over_count = 0;
GLint shoot = 0;
//////////////////////////////////////////
GLfloat obstacle_wall = 300;
GLfloat obstacle_cone = 300;
GLfloat obstacle_walls = 0;
GLfloat wall_0 = -180, wall_1 = 150, wall_2 = -70, wall_3 = 80;
GLfloat wall_state_0 = 1, wall_state_1 = 0, wall_state_2 = 1, wall_state_3 = 0;
GLfloat down_wall_0 = 260, down_wall_1 = 360, down_wall_2 = 460, down_wall_3 = 560, down_wall_4 = 660, down_wall_5 = 760;
GLint obstacle_walls_state = 1;
GLfloat obstacle_drum[10] = {300, 300, 300, 300, 300, 300, 300, 300, 300, 300};
////////////////////////////////////
#define TEXTURE_NUM 1
GLuint texture[TEXTURE_NUM];
char texture_name[TEXTURE_NUM][20]={"bbd.bmp"};
///////////////////////////////////////////
GLfloat text_y=1, text_z=-6;
GLuint tex[6]; 
/////////////////////////////
AUX_RGBImageRec * LoadBMP(char *Filename){
    FILE * File = NULL;
    if(!Filename) return NULL;
    File = fopen(Filename,"r");
    if(File) {
        fclose(File);
        return auxDIBImageLoad(Filename);	     // 파일로부터 메모리로
    }
    return NULL;
}
void LoadGLTextures( ) {  // Bitmap 이미지 5개를 호출하여 Texture 이미지로 변환한다.
     AUX_RGBImageRec *texRec[6];
     memset(texRec, 0, sizeof(void *)*6);                                                  

     if((texRec[0]=LoadBMP("bbd.bmp")) &&
       (texRec[1]=LoadBMP("2.bmp")) &&
       (texRec[2]=LoadBMP("3.bmp")) &&
       (texRec[3]=LoadBMP("4.bmp")) &&
       (texRec[4]=LoadBMP("5.bmp")) &&
       (texRec[5]=LoadBMP("WALL.bmp"))   ) {

          for(int i=0; i<6; i++) {
               glGenTextures(1, &tex[i]); 
               glBindTexture(GL_TEXTURE_2D, tex[i]);
               glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
               glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
               glTexImage2D(GL_TEXTURE_2D, 0, 3, texRec[i]->sizeX, texRec[i]->sizeY, 0,
                         GL_RGB, GL_UNSIGNED_BYTE, texRec[i]->data); 
          }
     }

     for(int i=0; i<6; i++) {                                                              
          if(texRec[i]) {
               if(texRec[i]->data) free(texRec[i]->data);
               free(texRec[i]);
          } 
     }

     glEnable(GL_TEXTURE_2D);
     glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
}
void MakeNoticeLists(void)
{
    GLuint base = glGenLists(256);
 
    for(int i = 0; i < 256; i++)
    {
        glNewList(base + i, GL_COMPILE);
        //glutBitmapCharacter(GLUT_BITMAP_8_BY_13, i);
        glutBitmapCharacter(GLUT_BITMAP_TIMES_ROMAN_24, i);
        glEndList();
    }
 
    glListBase(base);
}
///////////////////////////////////////////////////
void PilotView(GLdouble planex, GLdouble planey, GLdouble planez, GLfloat roll, GLfloat pitch, GLfloat yaw) {
	glRotatef(roll, 0.0, 0.0, 1.0);
	glRotatef(pitch, 0.0, 1.0, 0.0);
	glRotatef(yaw, 1.0, 0.0, 0.0);
	glTranslatef(-planex, -planey, -planez);
}
void SetCamera() {
	PilotView( planex,  planey,  planez,  roll,  pitch,  yaw);
}
void Set_Zoom_Camera() {
	PilotView( zoom_planex,  zoom_planey,  zoom_planez,  zoom_roll,  zoom_pitch,  zoom_yaw);
}
//////////////////////////////////
void PlayMusic(CString fileName)
{
mciOpen.lpstrElementName = fileName;//연주할 파일 이름
mciOpen.lpstrDeviceType = "mpegvideo";

mciSendCommand(NULL,MCI_OPEN,MCI_OPEN_ELEMENT|MCI_OPEN_TYPE,(DWORD)(LPVOID)&mciOpen); //MCI_OPEN 명령을 준다.
dwID_bgm = mciOpen.wDeviceID;    //열린 디바이스 아이디를 받는다.
mciSendCommand(dwID_bgm, MCI_PLAY, MCI_DGV_PLAY_REPEAT, (DWORD)&mciPlay);//연주시작(반복재생)
}
void StopMusic()
{
mciSendCommand(dwID_bgm, MCI_CLOSE, 0, NULL);//연주 종료
}
void PlayEffect_1(CString fileName) {
	mciOpen.lpstrElementName = fileName;//연주할 파일 이름
	mciOpen.lpstrDeviceType = "mpegvideo";

	mciSendCommand(NULL,MCI_OPEN,MCI_OPEN_ELEMENT|MCI_OPEN_TYPE,(DWORD)(LPVOID)&mciOpen);
	dwID_eff_1 = mciOpen.wDeviceID;    //열린 디바이스 아이디를 받는다.
	mciSendCommand(dwID_eff_1, MCI_PLAY, MCI_NOTIFY,(DWORD)&mciPlay);//연주시작
}
void StopEffect_1() {
	mciSendCommand(dwID_eff_1, MCI_CLOSE, 0, NULL);//연주 종료
}
void PlayEffect_2(CString fileName) {
	mciOpen.lpstrElementName = fileName;//연주할 파일 이름
	mciOpen.lpstrDeviceType = "mpegvideo";
	mciSendCommand(NULL,MCI_OPEN,MCI_OPEN_ELEMENT|MCI_OPEN_TYPE,(DWORD)(LPVOID)&mciOpen);
	dwID_eff_2 = mciOpen.wDeviceID;    //열린 디바이스 아이디를 받는다.
	mciSendCommand(dwID_eff_2, MCI_PLAY, MCI_NOTIFY,(DWORD)&mciPlay);//연주시작
}
void StopEffect_2() {
	mciSendCommand(dwID_eff_2, MCI_CLOSE, 0, NULL);//연주 종료
}
void PlayEffect_3(CString fileName) {
	mciOpen.lpstrElementName = fileName;//연주할 파일 이름
	mciOpen.lpstrDeviceType = "mpegvideo";
	mciSendCommand(NULL,MCI_OPEN,MCI_OPEN_ELEMENT|MCI_OPEN_TYPE,(DWORD)(LPVOID)&mciOpen);
	dwID_eff_3 = mciOpen.wDeviceID;    //열린 디바이스 아이디를 받는다.
	mciSendCommand(dwID_eff_3, MCI_PLAY, MCI_NOTIFY,(DWORD)&mciPlay);//연주시작
}
void StopEffect_3() {
	mciSendCommand(dwID_eff_3, MCI_CLOSE, 0, NULL);//연주 종료
}
///////////////////////////////////////////
void reset() {
	target_state = 0; target_state_y = 0; target_state_z = 0;
	target_x = 0; target_y = 0; target_z = 0;
	rotate_x = 0; rotate_y = 0; rotate_z = 0;
	target_size = 1; size_count = 0;
	blink_on = 0;
	obstacle_wall = 300;
	obstacle_cone = 300;
}
void DrawDrum();
void MyTimer(int Value) {
	if(time_count > 0) time_count--;
	else if(time_count == 0) {
		remain_time--;
		time_count = 40;
	}
	
	if(game_stage == 0) {
	}
	else if(game_stage == 1) {
		target_y = 1;
		rotate_x += 25;
	}
	else if(game_stage == 2) {
		if(target_x >= 25)           target_state = 0;
		else if(target_x <= -25)     target_state = 1;
		if(target_state == 1)        target_x += 5;
		else if(target_state == 0)   target_x -= 5;
	}
	else if(( game_stage - 2 ) % 12 == 1) {
		rotate_x += 25;
		target_y = 2;
		if(target_x >= 130)          target_state = 0;
		else if(target_x <= -130)    target_state = 1;
		if(target_state == 1)        target_x += 7;
		else if(target_state == 0)   target_x -= 7;
	}
	else if(( game_stage - 2 ) % 12 == 2) {
		rotate_x += 15;
		rotate_z += 15;
		if(target_x <= -200)         target_state = 1;
		else if(target_x >= 200)     target_state = 0;
		if(target_state == 1)        target_x += 5;
		else if(target_state == 0)   target_x -= 5;

		if(target_z >= 200)          target_state_z = 0;
		else if(target_z <= -200)    target_state_z = 1;
		if(target_state_z == 1)      target_z += 10;
		else if(target_state_z == 0) target_z -= 10;
	}
	else if(( game_stage - 2 ) % 12 == 3) {
		rotate_x += 5;
		rotate_y += 5;
		target_y = 1.5;
		target_x = 100 * cos(theta);
		target_z = 100 * sin(theta) + 100;
		theta += 0.1;
	}
	else if(( game_stage - 2 ) % 12 == 4) {
		rotate_x = 0;
		if(target_x == 200)          target_state = 0;
		else if(target_x == -200)    target_state = 1;
		if(target_state == 1)        target_x += 5;
		else if(target_state == 0)   target_x -= 5;

		if(target_y == 0)            target_state_y = 1;
		else if(target_y == 100)     target_state_y = 0;
		if(target_state_y == 1)      target_y += 10;
		else if(target_state_y == 0) target_y -= 10;
	}
	else if(( game_stage - 2 ) % 12 == 5) {
		target_y = 5.0;
		if(size_count == 0) {
			if(target_size > 1.2)           target_state_size = 0;
			else if(target_size <= 0.2) {
				size_count++;
				target_state_size = 1;
			}
			if(target_state_size == 1)      target_size += 0.07;
			else if(target_state_size == 0) target_size -= 0.07;
		}
		else if(size_count == 20) {
			size_count = 0;
		}
		else {
			size_count++;
		}
		if(target_x >= 100)         target_state = 0;
		else if(target_x <= -100)   target_state = 1;
		if(target_state == 1)       target_x += 4;
		else if(target_state == 0)  target_x -= 4;
	}
	else if(( game_stage - 2 ) % 12 == 6) {
		if (random_state == 0) {
			target_x = (float)(rand()%400) - 200;
			target_z = (float)(rand()%400) - 200;
			random_state = 1;
		}
		else if(random_state == 1) {
			if(random_count < 20) random_count++;
			else if(random_count == 20) {
				random_count = 0;
				random_state = 0;
			}
		}
	}
	else if(( game_stage - 2 ) % 12 == 7) {
		target_y = 1;
		if(target_x >= 200) {
			target_state = 0;
			if(target_z >= 250)           target_state_z = 1;
			else if(target_z <= 100)      target_state_z = 0;
			if(target_state_z == 1)       target_z -= 50;
			else if(target_state_z == 0 ) target_z += 50;
		}
		else if(target_x <= -200) {
			target_state = 1;
			if(target_z >= 250)           target_state_z = 1;
			else if(target_z <= 100)      target_state_z = 0;
			if(target_state_z == 1)       target_z -= 50;
			else if(target_state_z == 0 ) target_z += 50;
		}
		if(target_state == 1)      {
			rotate_y -= 10;
			target_x += 10;
		}
		else if(target_state == 0) {
			rotate_y += 10;
			target_x -= 10;
		}
	}
	else if(( game_stage - 2 ) % 12 == 8) {
		if(target_x >= 200)         target_state = 0;
		else if(target_x <= -200)   target_state = 1;
		if(target_state == 1)      target_x += 7;
		else if(target_state == 0) target_x -= 7;

		if(size_count == 0) {
			if(target_y <= 0) {
				target_state_y = 1;
				size_count++;
				target_y = -2;
			}
			else if(target_y >= 120)     target_state_y = 0;
			if(target_state_y == 1)      target_y += 10;
			else if(target_state_y == 0) target_y -= 10;
		}
		else if(size_count == 20) {
			size_count = 0;
		}
		else if(size_count < 20) {
			size_count++;
		}
	}
	else if(( game_stage - 2 ) % 12 == 9) {
		if(blink_count == blink_max) {
			blink_count = 0;
			if(blink_on == 0) {
				blink_max = 15;
				blink_on = 1;
			}
			else if(blink_on == 1) {
				blink_max = 20;
				blink_on = 0;
			}
		}
		else if(blink_count < blink_max) {
			blink_count++;
		}
		if(target_x >= 200)         target_state = 0;
		else if(target_x <= -200)   target_state = 1;
		if(target_state == 1)       target_x += 5;
		else if(target_state == 0)  target_x -= 5;
	}
	else if(( game_stage - 2 ) % 12 == 10) {
		rotate_x += 10;
		if(obstacle_walls <= 0) obstacle_walls_state = 0;
		else if(obstacle_walls >= 60) obstacle_walls_state = 1;
		if(obstacle_walls_state == 0) obstacle_walls += 1.5;
		else if(obstacle_walls_state == 1) obstacle_walls -= 1.5;

		if(target_x >= 200)         target_state = 0;
		else if(target_x <= -200)   target_state = 1;
		if(target_state == 1)       target_x += 7;
		else if(target_state == 0)  target_x -= 7;
	}
	else if(( game_stage - 2 ) % 12 == 11) {
		if   (wall_0 >= 200)        wall_state_0 = 0;
		else if(wall_0 <= -200)     wall_state_0 = 1;
		if   (wall_state_0 == 1)    wall_0 += 7;
		else if(wall_state_0 == 0)  wall_0 -= 7;

		if   (wall_1 >= 200)        wall_state_1 = 0;
		else if(wall_1 <= -200)     wall_state_1 = 1;
		if   (wall_state_1 == 1)    wall_1 += 10;
		else if(wall_state_1 == 0)  wall_1 -= 10;

		if   (wall_2 >= 200)        wall_state_2 = 0;
		else if(wall_2 <= -200)     wall_state_2 = 1;
		if   (wall_state_2 == 1)    wall_2 += 10;
		else if(wall_state_2 == 0)  wall_2 -= 10;

		if   (wall_3 >= 200)        wall_state_3 = 0;
		else if(wall_3 <= -200)     wall_state_3 = 1;
		if   (wall_state_3 == 1)    wall_3 += 8;
		else if(wall_state_3 == 0)  wall_3 -= 8;

		if(target_x >= 200)         target_state = 0;
		else if(target_x <= -200)   target_state = 1;
		if(target_state == 1)       target_x += 7;
		else if(target_state == 0)  target_x -= 7;
	}
	else if(( game_stage - 2 ) % 12 == 0) {
		down_wall_0 -= 5;
		if(down_wall_0 == -10) down_wall_0 = 650;
		down_wall_1 -= 5;
		if(down_wall_1 == -10) down_wall_1 = 650;
		down_wall_2 -= 5;
		if(down_wall_2 == -10) down_wall_2 = 650;
		down_wall_3 -= 5;
		if(down_wall_3 == -10) down_wall_3 = 650;
		down_wall_4 -= 5;
		if(down_wall_4 == -10) down_wall_4 = 650;
		down_wall_5 -= 5;
		if(down_wall_5 == -10) down_wall_5 = 650;

		if(target_x >= 200)         target_state = 0;
		else if(target_x <= -200)   target_state = 1;
		if(target_state == 1)       target_x += 7;
		else if(target_state == 0)  target_x -= 7;
	}
	glutPostRedisplay( );
	glutTimerFunc(speed, MyTimer, 1);
}
//////////////////////////////////////
void Stage() {
	target_position[0][0] = 0;		target_position[0][1] = 0;
	target_position[1][0] = 75;		target_position[1][1] = 80;
	target_position[2][0] = -200;	target_position[2][1] = 10;		
	target_position[3][0] = 40;		target_position[3][1] = 40;
	target_position[4][0] = 70;		target_position[4][1] = -20;
	target_position[5][0] = -30;	target_position[5][1] = -60;	
	target_position[6][0] = -20;	target_position[6][1] = 80;	
	target_position[7][0] = 60;		target_position[7][1] = -90;	
	target_position[8][0] = 10;		target_position[8][1] = 30;	
	target_position[9][0] = -80;	target_position[9][1] = 50;	
	target_position[10][10] = 0;    target_position[10][1] = 100;
}
//////////////////////////////////////
void DrawBuilding(GLint building_length, GLint building_height, GLint building_width) {
	glRotatef(90, 0, 1, 0);
    glBindTexture(GL_TEXTURE_2D, tex[0]);
	glBegin(GL_POLYGON);
       glTexCoord2f(0.0f, 0.0f); glVertex3f(-building_length, -building_height, -building_width); 
	   glTexCoord2f(0.0f, 1.0f); glVertex3f(-building_length, building_height, -building_width); 
	   glTexCoord2f(1.0f, 1.0f); glVertex3f(building_length, building_height, -building_width);   
	   glTexCoord2f(1.0f, 0.0f); glVertex3f(building_length, -building_height, -building_width);
    glEnd( );
	glBegin(GL_POLYGON);
         glTexCoord2f(0.0f, 0.0f); glVertex3f(-building_length, -building_height, building_width); 
		 glTexCoord2f(0.0f, 1.0f); glVertex3f(-building_length, building_height, building_width);
		 glTexCoord2f(1.0f, 1.0f); glVertex3f(building_length, building_height, building_width);   
		 glTexCoord2f(1.0f, 0.0f); glVertex3f(building_length, -building_height, building_width);
    glEnd( );
	glBegin(GL_POLYGON);
         glTexCoord2f(0.0f, 0.0f); glVertex3f(building_length, -building_height, building_width); 
		 glTexCoord2f(0.0f, 1.0f); glVertex3f(building_length, building_height, building_width); 
		 glTexCoord2f(1.0f, 1.0f); glVertex3f(building_length, building_height, -building_width); 
		 glTexCoord2f(1.0f, 0.0f); glVertex3f(building_length, -building_height, -building_width);
    glEnd( );
	glBegin(GL_POLYGON);
         glTexCoord2f(0.0f, 0.0f); glVertex3f(-building_length, -building_height, building_width); 
		 glTexCoord2f(0.0f, 1.0f); glVertex3f(building_length, -building_height, building_width);
		 glTexCoord2f(1.0f, 1.0f); glVertex3f(building_length, -building_height, -building_width); 
		 glTexCoord2f(1.0f, 0.0f); glVertex3f(-building_length, -building_height, -building_width);
    glEnd( );
	glBegin(GL_POLYGON);
         glTexCoord2f(0.32f, 0.0f); glVertex3f(-building_length, -building_height, building_width); 
		 glTexCoord2f(0.0f, 0.0f); glVertex3f(-building_length, -building_height, -building_width); 
		 glTexCoord2f(0.0f, 0.5f); glVertex3f(-building_length, building_height, -building_width); 
		 glTexCoord2f(0.32f, 0.5f); glVertex3f(-building_length, building_height, building_width);
    glEnd( );
	glBegin(GL_POLYGON);
       glTexCoord2f(0.32f, 0.0f); glVertex3f(-building_length, building_height, building_width); 
	   glTexCoord2f(0.32f, 0.50f); glVertex3f(building_length, building_height, building_width);
	   glTexCoord2f(0.68f, 0.50f); glVertex3f(building_length, building_height, -building_width); 
	   glTexCoord2f(0.68f, 0.0f); glVertex3f(-building_length, building_height, -building_width);
    glEnd( );

	glColor3f(0, 0, 0);
	glLineWidth(3);
	glBegin(GL_LINE_LOOP);
        glVertex3f(-building_length, -building_height, -building_width); glVertex3f(-building_length, building_height, -building_width); 
		glVertex3f(building_length, building_height, -building_width);   glVertex3f(building_length, -building_height, -building_width);
    glEnd( );
	glBegin(GL_LINE_LOOP);
        glVertex3f(-building_length, -building_height, building_width); glVertex3f(-building_length, building_height, building_width);
		glVertex3f(building_length, building_height, building_width);   glVertex3f(building_length, -building_height, building_width);
    glEnd( );
	glBegin(GL_LINE_LOOP);
        glVertex3f(building_length, -building_height, building_width); glVertex3f(building_length, building_height, building_width); 
		glVertex3f(building_length, building_height, -building_width); glVertex3f(building_length, -building_height, -building_width);
    glEnd( );
	glBegin(GL_LINE_LOOP);
        glVertex3f(-building_length, -building_height, building_width); glVertex3f(building_length, -building_height, building_width);
		glVertex3f(building_length, -building_height, -building_width); glVertex3f(-building_length, -building_height, -building_width);
    glEnd( );
	glBegin(GL_LINE_LOOP);
        glVertex3f(-building_length, -building_height, building_width); glVertex3f(-building_length, -building_height, -building_width); 
		glVertex3f(-building_length, building_height, -building_width); glVertex3f(-building_length, building_height, building_width);
    glEnd( );
	glBegin(GL_LINE_LOOP);
        glVertex3f(-building_length, building_height, building_width); glVertex3f(building_length, building_height, building_width);
		glVertex3f(building_length, building_height, -building_width); glVertex3f(-building_length, building_height, -building_width);
    glEnd( );
	glColor3f(0.8, 0.65, 0.23);
	glutPostRedisplay();
}
void Draw_Sniper(void) {
    if (!pmodel_sniper) {
        pmodel_sniper = glmReadOBJ("data/sniper.obj");
        if (!pmodel_sniper) exit(0);
        glmUnitize(pmodel_sniper);
        glmFacetNormals(pmodel_sniper);
        glmVertexNormals(pmodel_sniper, 90.0);
    }
    
    glmDraw(pmodel_sniper, GLM_SMOOTH | GLM_MATERIAL);
}
void Draw_Wall(GLint building_length, GLfloat building_height, GLfloat building_width) {
    glBindTexture(GL_TEXTURE_2D, tex[5]);

	glBegin(GL_POLYGON);
       glTexCoord2f(0.0f, 0.0f); glVertex3f(-building_length, -building_height, -building_width); 
	   glTexCoord2f(0.0f, 1.0f); glVertex3f(-building_length, building_height, -building_width); 
	   glTexCoord2f(1.0f, 1.0f); glVertex3f(building_length, building_height, -building_width);   
	   glTexCoord2f(1.0f, 0.0f); glVertex3f(building_length, -building_height, -building_width);
    glEnd( );
	
    glBindTexture(GL_TEXTURE_2D, tex[5]);
	glBegin(GL_POLYGON);
         glTexCoord2f(0.0f, 0.0f); glVertex3f(-building_length, -building_height, building_width); 
		 glTexCoord2f(0.0f, 1.0f); glVertex3f(-building_length, building_height, building_width);
		 glTexCoord2f(1.0f, 1.0f); glVertex3f(building_length, building_height, building_width);   
		 glTexCoord2f(1.0f, 0.0f); glVertex3f(building_length, -building_height, building_width);
    glEnd( );
	
    glBindTexture(GL_TEXTURE_2D, tex[5]);
	glBegin(GL_POLYGON);
         glTexCoord2f(0.0f, 0.0f); glVertex3f(building_length, -building_height, building_width); 
		 glTexCoord2f(0.0f, 1.0f); glVertex3f(building_length, building_height, building_width); 
		 glTexCoord2f(1.0f, 1.0f); glVertex3f(building_length, building_height, -building_width); 
		 glTexCoord2f(1.0f, 0.0f); glVertex3f(building_length, -building_height, -building_width);
    glEnd( );
	
    glBindTexture(GL_TEXTURE_2D, tex[5]);
	glBegin(GL_POLYGON);
         glTexCoord2f(0.0f, 0.0f); glVertex3f(-building_length, -building_height, building_width); 
		 glTexCoord2f(0.0f, 1.0f); glVertex3f(building_length, -building_height, building_width);
		 glTexCoord2f(1.0f, 1.0f); glVertex3f(building_length, -building_height, -building_width); 
		 glTexCoord2f(1.0f, 0.0f); glVertex3f(-building_length, -building_height, -building_width);
    glEnd( );
	
    glBindTexture(GL_TEXTURE_2D, tex[5]);
	glBegin(GL_POLYGON);
         glTexCoord2f(0.32f, 0.0f); glVertex3f(-building_length, -building_height, building_width); 
		 glTexCoord2f(0.0f, 0.0f); glVertex3f(-building_length, -building_height, -building_width); 
		 glTexCoord2f(0.0f, 0.5f); glVertex3f(-building_length, building_height, -building_width); 
		 glTexCoord2f(0.32f, 0.5f); glVertex3f(-building_length, building_height, building_width);
    glEnd( );
	
	glBindTexture(GL_TEXTURE_2D, tex[5]);
	glBegin(GL_POLYGON);
       glTexCoord2f(0.32f, 0.0f); glVertex3f(-building_length, building_height, building_width); 
	   glTexCoord2f(0.32f, 0.52f); glVertex3f(building_length, building_height, building_width);
	   glTexCoord2f(0.68f, 0.52f); glVertex3f(building_length, building_height, -building_width); 
	   glTexCoord2f(0.68f, 0.0f); glVertex3f(-building_length, building_height, -building_width);
    glEnd( );
}
void DrawSmile() {
	p=gluNewQuadric();
	gluQuadricDrawStyle(p, GL_FILL);
		if(bullet_count <= 1) glColor3f(1, 0, 0);
		else if(bullet_count <= 3) glColor3f(0.52, 0.89, 0.49);
		else glColor3f(1, 0.8, 0);
		glScalef(target_size, target_size, target_size);
		gluDisk(p, 0, 1.0, 100, 1); 
		gluCylinder (p, 1.0, 1.0, 0.25, 100, 100 );
		glTranslatef(0.0f, 0.0f, 0.25f);
		
		glTranslatef(0.0f, 0.0f, -0.25f);
		
		glColor3f(0,0.0,0);
		glPushMatrix();
			glTranslatef(0, -0.1, 0);
		glBegin(GL_TRIANGLE_FAN); // 동그라미
			for(float theta = 0; theta < 6.4; theta+=0.1){
				a = 0.1*cos(theta)-0.5;
				b = 0.1*sin(theta)+0.3;
				glVertex3f(a,b,0.3);  
			}	  
		glEnd();
		glBegin(GL_TRIANGLE_FAN); // 동그라미
			for(float theta = 0; theta < 6.4; theta+=0.1){
				a = 0.1*cos(theta)+0.5;
				b = 0.1*sin(theta)+0.3;
				glVertex3f(a,b,0.3);
			}
		glEnd();
		glPopMatrix();
		
		if(bullet_count <= 1) {
			glColor3f(0, 0, 0);
			glLineWidth(3);
			glPushMatrix();
				glTranslatef(-0.4, 0.3, 0);
				glBegin(GL_LINES);
					glVertex3f(-0.4, 0.3, 0.3);glVertex3f(0.2, 0, 0.3);
				glEnd();
			glPopMatrix();
			glPushMatrix();
				glTranslatef(0.4, 0.3, 0);
				glBegin(GL_LINES);
					glVertex3f(0.4, 0.3, 0.3);glVertex3f(-0.2, 0, 0.3);
				glEnd();
			glPopMatrix();
			glPushMatrix();
				glTranslatef(0, -0.5, 0);
				glBegin(GL_LINES);
					glVertex3f(0.4, -0.2, 0.3); glVertex3f(0, 0, 0.3);
					glVertex3f(0, 0, 0.3); glVertex3f(-0.4, -0.2, 0.3);
				glEnd();
			glPopMatrix();
		} 
		else if(bullet_count <= 3) {
			glColor3f(0, 0, 0);
			glLineWidth(3);
			glPushMatrix();
				glTranslatef(-0.5, 0.5, 0);
				glBegin(GL_LINES);
					glVertex3f(-0.3, 0, 0.3);glVertex3f(0.2, 0, 0.3);
				glEnd();
			glPopMatrix();
			glPushMatrix();
				glTranslatef(0.5, 0.5, 0);
				glBegin(GL_LINES);
					glVertex3f(-0.2, 0, 0.3);glVertex3f(0.3, 0, 0.3);
				glEnd();
			glPopMatrix();
			glPushMatrix();
				glTranslatef(0, -0.6, 0);
				glBegin(GL_LINES);
					glVertex3f(0.3, 0, 0.3);glVertex3f(-0.3, 0, 0.3);
				glEnd();
			glPopMatrix();
		}
		else {
			glColor3f(1,0.0,0);
			glBegin(GL_TRIANGLE_FAN); // 입
				for(float theta = 0; theta < 3.2; theta+=0.1){
					a = 0.4*cos(theta);
					b = 0.4*sin(theta)+0.4;
					glVertex3f(-a,-b,0.3);
				}
			glEnd();
		}


	glutPostRedisplay();
}
void Drawground(GLfloat building_length, GLfloat building_height, GLfloat building_width) {
	glBindTexture(GL_TEXTURE_2D, tex[2]);
		glBegin(GL_POLYGON);
       glTexCoord2f(0.0f, 0.0f); glVertex3f(-building_length, -building_height, -building_width); 
	   glTexCoord2f(0.0f, 1.0f); glVertex3f(-building_length, building_height, -building_width); 
	   glTexCoord2f(1.0f, 1.0f); glVertex3f(building_length, building_height, -building_width);   
	   glTexCoord2f(1.0f, 0.0f); glVertex3f(building_length, -building_height, -building_width);
    glEnd( );
	
     glBindTexture(GL_TEXTURE_2D, tex[3]);
	glBegin(GL_POLYGON);
         glTexCoord2f(0.0f, 0.0f); glVertex3f(building_length, -building_height, building_width); 
		 glTexCoord2f(0.0f, 1.0f); glVertex3f(building_length, building_height, building_width); 
		 glTexCoord2f(1.0f, 1.0f); glVertex3f(building_length, building_height, -building_width); 
		 glTexCoord2f(1.0f, 0.0f); glVertex3f(building_length, -building_height, -building_width);
    glEnd( );
	
	
     glBindTexture(GL_TEXTURE_2D, tex[1]);
	glBegin(GL_POLYGON);
         glTexCoord2f(0.0f, 0.0f); glVertex3f(-building_length, -building_height, building_width); 
		 glTexCoord2f(0.0f, 1.0f); glVertex3f(-building_length, -building_height, -building_width); 
		 glTexCoord2f(1.0f, 1.0f); glVertex3f(-building_length, building_height, -building_width); 
		 glTexCoord2f(1.0f, 0.0f); glVertex3f(-building_length, building_height, building_width);
    glEnd( );
	
     glBindTexture(GL_TEXTURE_2D, tex[4]);
	glBegin(GL_POLYGON);
       glTexCoord2f(0.0f, 0.0f); glVertex3f(-building_length, building_height, building_width); 
	   glTexCoord2f(0.0f, 1.0f); glVertex3f(building_length, building_height, building_width);
	   glTexCoord2f(1.0f, 1.0f); glVertex3f(building_length, building_height, -building_width); 
	   glTexCoord2f(1.0f, 0.0f); glVertex3f(-building_length, building_height, -building_width);
    glEnd( );
	glutPostRedisplay();
}
void DrawDrum() {
	p=gluNewQuadric();
	gluQuadricDrawStyle(p, GL_FILL);
	glColor3f(0.1,0.1,0.1);
	gluDisk(p, 0.0, 0.5, 100, 1); 

	glColor3f(0.1,0.1,0.1);
	gluCylinder (p, 0.5, 0.5, 0.25, 100, 100 );

	glTranslatef(0.0f, 0.0f, 0.25f);
	glColor3f(0.13,0.13,0.13);
	gluCylinder (p, 0.5, 0.5, 0.25, 100, 100 );

	glTranslatef(0.0f, 0.0f, 0.25f);
	glColor3f(0.16,0.16,0.16);
	gluCylinder (p, 0.5, 0.5, 0.25, 100, 100 );

	glTranslatef(0.0f, 0.0f, 0.25f);
	glColor3f(0.19,0.19,0.19);
	gluCylinder (p, 0.5, 0.5, 0.25, 100, 100 );

	glTranslatef(0.f, 0.0f, 0.25f);
	glColor3f(0.22,0.22,0.22);
	gluDisk(p, 0.0, 0.5, 100, 1); 
	gluCylinder (p, 0.5, 0.5, 0.25, 100, 100 );

	glTranslatef(0.29f, 0.0f, 0.25f);
	glColor3f(0.25,0.25,0.25);
	gluCylinder (p, 0.15, 0.15, 0.10, 100, 100 );

	glTranslatef(0.f, 0.0f, 0.10f);
	glColor3f(0.26,0.26,0.26);
	gluDisk(p, 0.15, 0.15, 100, 1); 
}
void DrawCone() {
	p=gluNewQuadric();
	gluQuadricDrawStyle(p, GL_FILL);
	glColor3f(1,0.0,0);
	gluDisk(p, 0.0, 0.6, 100, 1); 
	glColor3f(1,0.0,0);
	gluCylinder (p, 0.6, 0.35, 0.8, 100, 100 );
	glTranslatef(0.0f, 0.0f, 0.8f);
	glColor3f(1,1,1);
	gluCylinder (p, 0.35, 0.25, 0.3, 100, 100 );
	glTranslatef(0.0f, 0.0f, 0.3f);
	glColor3f(1,0,0);
	gluCylinder (p, 0.25, 0.0, 0.7, 100, 100 );
}
void DrawBullet(float x) {
	glPushMatrix();
		glScalef(0.75, 0.75, 0.75);
		glTranslatef(bx + x, by, bz);
		glTranslatef(0, 0, text_z);
		glScalef(0.4, 0.4, 0);
		glTranslatef(-9.7, -5.3, 0);
		glRotatef(-90,90,0,0);
		glColor3f(0.75,0.5,0);
		gluDisk(p, 0, 0.5, 100, 1); 
		glTranslatef(0.0f, 0.0f, 0.0f);
		gluCylinder (p, 0.5, 0.5, 0.25, 100, 100 );
		glTranslatef(0.0f, 0.0f, 0.25f);
		gluDisk(p, 0, 0.5, 100, 1);
		glColor3f(0.70,0.5,0);
		glTranslatef(0.0f, 0.0f, 0.0f);
		gluCylinder (p, 0.45, 0.45, 1.7, 100, 100 );
		glTranslatef(0.0f, 0.0f, 1.7f);
		glColor3f(0.5,0.5,0.5);
		gluCylinder (p, 0.45, 0.25, 0.2, 100, 100 );
		glTranslatef(0.0f, 0.0f, 0.2f);
		gluDisk(p, 0, 0.25, 100, 1);
	glPopMatrix();
}
void DrawGLScene()	{
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glMatrixMode( GL_MODELVIEW );
	glRotatef( -g_fSpinY, 1.0f, 0.0f, 0.0f );
	SetCamera();

	glTranslatef(0, 0, mouse_move_z);
	glInitNames();
	Stage();

	glColor3f(1,1,1);
	if (game_stage == 0) {
}
	else if (game_stage == 1) {
		glPushName(104);
		glPushMatrix();   
			if(obstacle_cone > 0) obstacle_cone -= 20;
			glTranslatef(60, obstacle_cone + 220, building_pos_z + 200);
			glRotatef(-90, 1, 0, 0);
			glScalef(40, 40, 40);
			DrawCone();
		glPopMatrix();
		glPopName();
	}
	else if(game_stage == 2) {
		glPushName(104);
		glPushMatrix();   
			if(obstacle_wall > 0) obstacle_wall -= 20;
			glTranslatef(-180, obstacle_wall + 240, building_pos_z + 150);
			glScalef(50, 50, 50);
			Draw_Wall(1, 0.5, 0.1);
		glPopMatrix();
		glPopName();
	}
	else if(( game_stage - 2 ) % 12 == 8) {
		glPushName(104);
		glPushMatrix();                   // Draw Wall
			glTranslatef(-170, 240, building_pos_z + 230);
			glScalef(40, 40, 40);
			Draw_Wall(2, 0.8, 0.1);
		glPopMatrix();
		glPushMatrix();                   // Draw Wall
			glTranslatef(-10, 240, building_pos_z + 230);
			glScalef(40, 40, 40);
			Draw_Wall(2, 0.8, 0.1);
		glPopMatrix();
		glPushMatrix();                   // Draw Wall
			glTranslatef(150, 240, building_pos_z + 230);
			glScalef(40, 40, 40);
			Draw_Wall(2, 0.8, 0.1);
		glPopMatrix();
		glPopName();
	}
	else if(( game_stage - 2 ) % 12 == 10) {
		glPushName(104);
		glPushMatrix();                   // Draw Wall
			glTranslatef(-170, obstacle_walls + 240, building_pos_z + 230);
			glScalef(40, 40, 40);
			Draw_Wall(1, 0.8, 0.1);
		glPopMatrix();
		glPushMatrix();                   // Draw Wall
			glTranslatef(-10, obstacle_walls + 240, building_pos_z + 230);
			glScalef(40, 40, 40);
			Draw_Wall(1, 0.8, 0.1);
		glPopMatrix();
		glPushMatrix();                   // Draw Wall
			glTranslatef(150, obstacle_walls + 240, building_pos_z + 230);
			glScalef(40, 40, 40);
			Draw_Wall(1, 0.8, 0.1);
		glPopMatrix();

		glPushMatrix();                   // Draw Wall
			glTranslatef(-90, 300 - obstacle_walls, building_pos_z + 230);
			glScalef(40, 40, 40);
			Draw_Wall(1, 0.8, 0.1);
		glPopMatrix();
		glPushMatrix();                   // Draw Wall
			glTranslatef(70, 300 - obstacle_walls, building_pos_z + 230);
			glScalef(40, 40, 40);
			Draw_Wall(1, 0.8, 0.1);
		glPopMatrix();
		glPopName();
	}
	else if(( game_stage - 2 ) % 12 == 11) {
		glPushName(104);
		glPushMatrix();                   // Draw Wall
			glTranslatef(wall_0, 240, building_pos_z + 240);
			glScalef(40, 40, 40);
			Draw_Wall(2.5, 0.8, 0.1);
		glPopMatrix();
		glPushMatrix();                   // Draw Wall
			glTranslatef(wall_1, 240, building_pos_z + 190);
			glScalef(40, 40, 40);
			Draw_Wall(2.3, 0.8, 0.1);
		glPopMatrix();
		glPushMatrix();                   // Draw Wall
			glTranslatef(wall_2, 240, building_pos_z + 140);
			glScalef(40, 40, 40);
			Draw_Wall(2, 0.8, 0.1);
		glPopMatrix();
		glPushMatrix();                   // Draw Wall
			glTranslatef(wall_3, 240, building_pos_z + 90);
			glScalef(40, 40, 40);
			Draw_Wall(1.7, 0.8, 0.1);
		glPopMatrix();
		glPopName();
	}
	else if(( game_stage - 2 ) % 12 == 0) {
		glPushName(104);
		glPushMatrix();                   // Draw Wall
			glTranslatef(-170, down_wall_0, building_pos_z + 150);
			glScalef(40, 40, 40);
			Draw_Wall(1, 1.3, 0.1);
		glPopMatrix();
		glPushMatrix();                   // Draw Wall
			glTranslatef(-170, down_wall_2, building_pos_z + 150);
			glScalef(40, 40, 40);
			Draw_Wall(1, 1.3, 0.1);
		glPopMatrix();
		glPushMatrix();                   // Draw Wall
			glTranslatef(-170, down_wall_4, building_pos_z + 150);
			glScalef(40, 40, 40);
			Draw_Wall(1, 1.3, 0.1);
		glPopMatrix();
		glPushMatrix();                   // Draw Wall
			glTranslatef(-170, down_wall_5, building_pos_z + 150);
			glScalef(40, 40, 40);
			Draw_Wall(1, 1.3, 0.1);
		glPopMatrix();
		//////////////////////////////////////
		glPushMatrix();                   // Draw Wall
			glTranslatef(-90, 50 + down_wall_0, building_pos_z + 150);
			glScalef(40, 40, 40);
			Draw_Wall(1, 1.3, 0.1);
		glPopMatrix();
		glPushMatrix();                   // Draw Wall
			glTranslatef(-90, 50 + down_wall_1, building_pos_z + 150);
			glScalef(40, 40, 40);
			Draw_Wall(1, 1.3, 0.1);
		glPopMatrix();glPushMatrix();                   // Draw Wall
			glTranslatef(-90, 50 + down_wall_3, building_pos_z + 150);
			glScalef(40, 40, 40);
			Draw_Wall(1, 1.3, 0.1);
		glPopMatrix();glPushMatrix();                   // Draw Wall
			glTranslatef(-90, 50 + down_wall_5, building_pos_z + 150);
			glScalef(40, 40, 40);
			Draw_Wall(1, 1.3, 0.1);
		glPopMatrix();
		//////////////////////////////////////
		glPushMatrix();                   // Draw Wall
			glTranslatef(-10, down_wall_0 - 40, building_pos_z + 150);
			glScalef(40, 40, 40);
			Draw_Wall(1, 1.3, 0.1);
		glPopMatrix();
		glPushMatrix();                   // Draw Wall
			glTranslatef(-10, down_wall_2 - 40, building_pos_z + 150);
			glScalef(40, 40, 40);
			Draw_Wall(1, 1.3, 0.1);
		glPopMatrix();
		glPushMatrix();                   // Draw Wall
			glTranslatef(-10, down_wall_3 - 40, building_pos_z + 150);
			glScalef(40, 40, 40);
			Draw_Wall(1, 1.3, 0.1);
		glPopMatrix();
		glPushMatrix();                   // Draw Wall
			glTranslatef(-10, down_wall_4 - 40, building_pos_z + 150);
			glScalef(40, 40, 40);
			Draw_Wall(1, 1.3, 0.1);
		glPopMatrix();
		glPushMatrix();                   // Draw Wall
			glTranslatef(-10, down_wall_5 - 40, building_pos_z + 150);
			glScalef(40, 40, 40);
			Draw_Wall(1, 1.3, 0.1);
		glPopMatrix();
		//////////////////////////////////////
	    glPushMatrix();                   // Draw Wall
			glTranslatef(70, 30 + down_wall_0, building_pos_z + 150);
			glScalef(40, 40, 40);
			Draw_Wall(1, 1.3, 0.1);
		glPopMatrix();
		glPushMatrix();                   // Draw Wall
			glTranslatef(70, 30 + down_wall_1, building_pos_z + 150);
			glScalef(40, 40, 40);
			Draw_Wall(1, 1.3, 0.1);
		glPopMatrix();
		glPushMatrix();                   // Draw Wall
			glTranslatef(70, 30 + down_wall_2, building_pos_z + 150);
			glScalef(40, 40, 40);
			Draw_Wall(1, 1.3, 0.1);
		glPopMatrix();
		glPushMatrix();                   // Draw Wall
			glTranslatef(70, 30 + down_wall_4, building_pos_z + 150);
			glScalef(40, 40, 40);
			Draw_Wall(1, 1.3, 0.1);
		glPopMatrix();
		glPushMatrix();                   // Draw Wall
			glTranslatef(70, 30 + down_wall_5, building_pos_z + 150);
			glScalef(40, 40, 40);
			Draw_Wall(1, 1.3, 0.1);
		glPopMatrix();
		//////////////////////////////////////
		glPushMatrix();                   // Draw Wall
			glTranslatef(150, down_wall_0 - 60, building_pos_z + 150);
			glScalef(40, 40, 40);
			Draw_Wall(1, 1.3, 0.1);
		glPopMatrix();
		glPushMatrix();                   // Draw Wall
			glTranslatef(150, down_wall_1 - 60, building_pos_z + 150);
			glScalef(40, 40, 40);
			Draw_Wall(1, 1.3, 0.1);
		glPopMatrix();
		glPushMatrix();                   // Draw Wall
			glTranslatef(150, down_wall_3 - 60, building_pos_z + 150);
			glScalef(40, 40, 40);
			Draw_Wall(1, 1.3, 0.1);
		glPopMatrix();
		glPushMatrix();                   // Draw Wall
			glTranslatef(150, down_wall_4 - 60, building_pos_z + 150);
			glScalef(40, 40, 40);
			Draw_Wall(1, 1.3, 0.1);
		glPopMatrix();
		glPushMatrix();                   // Draw Wall
			glTranslatef(150, down_wall_5 - 60, building_pos_z + 150);
			glScalef(40, 40, 40);
			Draw_Wall(1, 1.3, 0.1);
		glPopMatrix();
		//////////////////////////////////////
		glPopName();
	}
	glColor3f(1,1,1);
	glPushName(103);
	glPushMatrix();                   // Draw Target
		glTranslatef(target_position[game_stage][0] + target_x, 231 + target_y, building_pos_z + target_position[game_stage][1] + target_z);
		glRotatef(rotate_x, 0, 1, 0);
		glRotatef(rotate_y, 0, 0, 1);
		glRotatef(rotate_z, 1, 0, 0);
		glScalef(20, 20, 20);
		if(blink_on == 0) DrawSmile();
	glPopMatrix();
	glPopName();
		
	glColor3f(1, 1, 1);
	glPopName();// background
		glPushMatrix();	   
		glTranslatef(0, back_y, back_z);
		Drawground(1500, 1500, 1500);
	glPopMatrix();
	glColor3f(1, 1, 1);
	glPushName(101);
	glPushMatrix();   // Draw My Building
		glTranslatef(0, 0, 50);
		DrawBuilding(100, 300, 200);
	glPopMatrix();
	glPopName();

	glPushMatrix();	   // Draw Your Building
		glTranslatef(0, -300, building_pos_z);
		DrawBuilding(200, 500, 300);
	glPopMatrix();
	glLoadIdentity();
	
	/////////////////////////////////   Draw Gun Part

	glColor3f(0, 0, 0);
	float focus_size;
	if(zoomin_mode == 1) focus_size = 0.05;
	else if(zoomin_mode == 0) focus_size = 10;
	glPushName(100);
	glPushMatrix();                     // Draw Focus
		glTranslatef(0, 0, -5);
		glLineWidth(3);
		glBegin(GL_LINES);
			glVertex3f(focus_size, 0, 0);glVertex3f(-focus_size, 0, 0);
			glVertex3f(0, focus_size, 0);glVertex3f(0, -focus_size, 0);
		glEnd();
	glPopMatrix();
	glPopName();
	glEnable(GL_LIGHTING); 
	glPushMatrix();                    // Draw Sniper
		glTranslatef(gun_x, -2, gun_z);
		glRotatef(gun_rot_x, 1, 0, 0);
		glRotatef(gun_rot_y, 0, 1, 0);
		glRotatef(gun_rot_z, 0, 0, 1);
		glScalef(3, 3, 3);
		if(zoomin_mode == 1) Draw_Sniper();
	glPopMatrix();
	glDisable(GL_LIGHTING);
	glColor3f(0, 0, 0);
	glPushMatrix();
		glTranslatef(0, 0, gun_z);
		if(zoomin_mode == 0) gluDisk(obj, 0.5, 100, 100, 100);
	glPopMatrix();
	glColor3f(0.8, 0.65, 0.23);
    
	glPushMatrix();
        glLoadIdentity();
		glTranslatef(0, text_y, text_z);
        glColor3fv(g_aColor[CLR_RED]);
        glRasterPos2f(-4.25, 1.15);
        sprintf(g_szMsg, "* Game Time : %d", remain_time);
        glCallLists((GLint) strlen(g_szMsg), GL_BYTE, g_szMsg);

		glColor3fv(g_aColor[CLR_GREEN]);
		glRasterPos2f(-0.3, 0.05);
		if(temp!=game_stage){
			sprintf(g_szMsg, "Stage %d Clear!", game_stage);
			rot_count++;
			if(rot_count == 200){
				temp=game_stage;
				rot_count=0;
			}
			glCallLists((GLint) strlen(g_szMsg), GL_BYTE, g_szMsg);
		}
		
		glColor3fv(g_aColor[CLR_BLUE]);
		glRasterPos2f(-0.3, 0.2);
		if(bullet_count == 0 || remain_time==0){
			sprintf(g_szMsg, "Game Over");
			shoot = 1;
			over_count++;
			if(over_count == 300){
				exit(0);
			}
			glCallLists((GLint) strlen(g_szMsg), GL_BYTE, g_szMsg);
		}

		glColor3f(0.8, 0.65, 0.23);

		for(float i = bullet_count; i > 0; i -= 1) 
			DrawBullet(i / 2);
		
    glPopMatrix();
	glMatrixMode( GL_PROJECTION );
	glLoadIdentity( );
	gluPerspective (fovy, (GLfloat)window_x / (GLfloat)window_y, 0.1, 4000);

	glutPostRedisplay();
	glutSwapBuffers();
}
//////////////////////////////////////////
void ProcessSelect(GLuint index[64]) {
	switch(index[3]) {
		case 100: 
			PlayEffect_3("RD.mp3");
			PlayEffect_2("GUNSHOT.wma");
			break;
		case 101: 
			PlayEffect_3("RD.mp3");
		PlayEffect_2("GUNSHOT.wma");
			break;
		case 102: 
			StopEffect_2();
			break;
		case 103: 
			reset();
			PlayEffect_1("HEADSHOT.wma");
			PlayEffect_2("ED.WAV");
			PlayEffect_3("RD.mp3");
			remain_time=remain_time+2;
			bullet_count=bullet_count;
			game_stage=game_stage+1;
			break;
		case 104: 
			PlayEffect_3("RD.mp3");
			PlayEffect_2("GUNSHOT.wma");
			bullet_count=bullet_count-1;
			break;
		case 105: 
			PlayEffect_3("RD.mp3");
			PlayEffect_2("GUNSHOT.wma");
			bullet_count=bullet_count-1;
			break;
		default: 
			PlayEffect_3("RD.mp3");
			PlayEffect_2("GUNSHOT.wma");
			bullet_count=bullet_count-1;
			break;
	}
}
void SelectObjects(GLint x, GLint y) {
	GLuint selectBuff[64];
	GLint hits, viewport[4];

	glSelectBuffer(64, selectBuff);
	glGetIntegerv(GL_VIEWPORT, viewport);
	glMatrixMode(GL_PROJECTION);
	glPushMatrix();
		glRenderMode(GL_SELECT);
		glLoadIdentity();
		gluPickMatrix(x, viewport[3]-y, 0.01, 0.01, viewport);

		gluPerspective(fovy, 1.0, 0.1f, 4000);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		DrawGLScene();

		hits = glRenderMode(GL_RENDER);
		if(hits>0) ProcessSelect(selectBuff);
		glMatrixMode(GL_PROJECTION);
	glPopMatrix();
	glMatrixMode(GL_MODELVIEW);
	glutPostRedisplay();
}
///////////////////////////////////////////////////////
void Zoom_In() {
	if(zoomin_mode == 0) { // 줌인 모드
		zoomin_mode = 1;
		fovy = 45;
	}
	else if(zoomin_mode == 1) { // 줌아웃 모드
		zoomin_mode = 0;
		fovy = 10;
	}
	glutPostRedisplay();
}
////////////////////////////////////////////////////
void MyMouse(GLint Button, GLint State, GLint X, GLint Y) {
	if(Button == GLUT_LEFT_BUTTON && State==GLUT_DOWN && shoot == 0){
		StopEffect_1();
		StopEffect_2();
		StopEffect_3();
		SelectObjects(window_x / 2, window_y / 2);
		zoomin_mode = 1;
		fovy = 45;
	}
	if( Button == GLUT_RIGHT_BUTTON && State==GLUT_DOWN) {
		Zoom_In();
	}
	glutPostRedisplay();// 새로 추가
}
void MyMouseMove(GLint X, GLint Y) {
	///////////////////////////////////////////////////   pitch moving
	pitch = (7 * (float) X / 142 ) - 35;
	////////////////////////////////////////////////////  pitch -> planez moving
	if(pitch < 0) planez = pitch / 18 + 5;
	else if(pitch > 0) planez = -pitch / 18 + 5;
	else if(pitch == 0) planez = 5;
	////////////////////////////////////////////////////  yaw -> g_fSpinY moving
	g_fSpinY = -( (float)Y - 510) / 10;
	nY = Y; // 다음 움직임을 위해 현재 좌표를 저장
	///////////////////////////////////////////////////////
	mouse_move_z = -9 * (g_fSpinY - 3) / 23 + 40;
	glutPostRedisplay();
}
void MyKeyboard(unsigned char key, int x, int y) { 
	switch (key) {
		case 'a' : planex -= 10; if (planex <= -200 ) planex = -195;break;
		case 'd' : planex += 10; if (planex >= 200) planex = 195;break;
		case '1' :
			if(game_stage <= 0) game_stage = 0;
			reset();
			game_stage--;
			break;
		case '2' :
			game_stage++;
			reset();
			break;
		case '3' :
			bullet_count++;
			break;
	}
	glutPostRedisplay();
}
/////////////////////////////////////////////////////////////////
int InitGL() {
	glShadeModel(GL_SMOOTH);
	glClearColor(0, 0, 0, 0.5f);
	glClearDepth(1.0f);
	glEnable(GL_DEPTH_TEST);
	
	glBlendFunc(GL_SRC_ALPHA, GL_ONE);   
	glEnable(GL_COLOR_MATERIAL);
	MakeNoticeLists();
	glEnable(GL_LIGHT0);
	glDepthFunc(GL_LEQUAL);
	glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

	return TRUE;
}
void MyReshape( int w, int h ) {
    glViewport( 0, 0, (GLsizei)w, (GLsizei)h );
	window_x = w;
	window_y = h;
    glMatrixMode( GL_PROJECTION );
    glLoadIdentity( );

    gluPerspective (fovy, (GLfloat)w / (GLfloat)h, 0.1, 4000);
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
	glutPostRedisplay();
}
///////////////////////////////////////////////////////////
int main(int argc, char** argv) {
   glutInit(&argc, argv);
   glutInitDisplayMode (GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH);
   glutInitWindowSize (640, 480); 
   glutInitWindowPosition (100, 100);
   glutCreateWindow ("Sniping Game!");
   InitGL();
   
   glutDisplayFunc( DrawGLScene ); 
   glutReshapeFunc( MyReshape );
   glutMouseFunc(MyMouse);
   glutPassiveMotionFunc(MyMouseMove);
   glutKeyboardFunc(MyKeyboard);
   
   glutTimerFunc(speed, MyTimer, 1);
   LoadGLTextures();
   glEnable(GL_TEXTURE_2D);
   glClearDepth(1.0);
   glHint(GL_PERSPECTIVE_CORRECTION_HINT,GL_NICEST);
   PlayMusic("sniperbty.mp3");
   
   glutSetCursor(GLUT_CURSOR_NONE);
   glutMainLoop();
   return 0;   
}
