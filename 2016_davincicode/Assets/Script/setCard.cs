using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using UnityEngine.UI;

public class setCard : MonoBehaviour
{

    public GameObject[] allCase = new GameObject[26]; //흑,벡,조커 0~26의 오브젝트를 관리하는 배열
    public GameObject[] sC = new GameObject[4]; // 트라이앵글, 순서대로 Player, Enemy, guessLeft, guessRight
    public GameObject guessView;
    public GameObject guessCard;
    public Button goCard;
    public Text goCardText;
    public Text gameRule;
    public Text cST;

    private GameObject target;

    private List<int> curposList = new List<int>(); // 현재 ?로 되어있는 카드들, 뽑힌 것은 Remove 처리
    private List<int> mybw = new List<int>(); // My Black/White List
    private List<int> enemybw = new List<int>(); // Enemy Black/White List
    private SortedDictionary<int, List<int>> enemyStrategy = new SortedDictionary<int, List<int>>();
    private SortedDictionary<int, List<int>> subs = new SortedDictionary<int, List<int>>();

    private Vector2 pos;
    private RaycastHit2D hit;

    private int randomResult;
    private int goCardNum = -100;
    private int enemyChoice;
    private int playerCount = 0;
    private int enemyCount = 0;
    //,내수명,적수명 -> 게임종료, 적이알고있는 내카드(리스트),
    private float enemyTime = 0f;

    static public bool[] gameStep = new bool[5]; //내거픽, 상대꺼픽, 상대꺼맞추기, 결과
    private bool playerTurn = false;
    private bool final= false;
    private bool final_2 = true;


    void Start()
    {
        Screen.SetResolution(400, 640, false);

        for (int i = 0; i < 26; i++)
        {
            if (i < 5)
                gameStep[i] = false;

            //모든 카드 랜덤한 위치와 각도로 화면에 뿌림
            allCase[i].transform.position =
                new Vector3(Random.Range(-14f, 15f), Random.Range(-14f, 15f), 0f);
            allCase[i].transform.Rotate(0f, 0f, Random.Range(0f, 181f));

            //검, 흰 오브젝트를 ?로 변경
            if (i % 2 != 0)
                changeSprite(allCase, null, i, "27", 0);
            else
                changeSprite(allCase, null, i, "26", 0);

            curposList.Add(i); //current possible list; 현재 남아있는 경우의 수
        }

        autoPick(mybw, 4); // Player
        autoPick(enemybw, 4); // Enemy

        gameRule.text = "Pick Card";
    }

    void Update()
    {
        if (enemyCount > 0 && playerCount > 0) //게임은 둘 중 하나의 카운트가 0이 되면 끝난다
        {
            if (playerTurn.Equals(true)) //내 차례
            {
                if (Input.GetMouseButtonDown(0))
                {
                    pos = Camera.main.ScreenToWorldPoint(Input.mousePosition);
                    hit = Physics2D.Raycast(pos, Vector2.zero, 0f);

                    //01. 내 카드를 고른다
                    if (gameStep[0].Equals(false))
                    {
                        if (hit.collider != null && hit.collider.gameObject.layer == 10)
                        {
                            target = hit.collider.gameObject;
                            gameStepForAll(0, mybw, target, int.Parse(target.name));
                        }}

                    //02-1. 적 카드를 고른다
                    if (gameStep[1].Equals(true))
                    {
                        if (hit.collider != null && hit.collider.gameObject.layer == 9)
                        {
                            target = hit.collider.gameObject;
                            gameStepForAll(1, null, target, 0);
                        }}

                    //03. ?인 카드를 고른다 (터치다운함수 포함)
                    if (gameStep[3].Equals(true))
                        gameStepForAll(3, null, null, 0);

                    //04-2. 맞은 경우 Correct라 뜨고 guessCard를 눌러 또 한다
                    if (gameStep[4].Equals(true))
                    {
                        if (hit.collider != null && hit.collider.gameObject == guessCard && gameRule.text == "Correct!")
                        {
                            closeGuessViewGraphic(1);
                        }}}

                //02-2. 적 카드를 고를 때 나오는 그래픽 실행 함수
                if (gameStep[2] == true)
                    gameStepForAll(2, null, null, 0);
            }

            else //적 차례, 나중에 코루틴화 하기
            {
                enemyTime += Time.deltaTime;

                if (gameStep[0].Equals(false) && enemyTime >= 0.5f)
                {
                    enemyChoice = curposList[Random.Range(0, curposList.Count)];
                    gameStepForAll(0, enemybw, allCase[enemyChoice], enemyChoice);
                }

                if (gameStep[1].Equals(true) && enemyTime >= 1f)
                {
                    enemyAlgorithm();
                    gameStepForAll(1, null, allCase[enemyChoice], 0);
                }

                if (gameStep[2].Equals(true) && enemyTime >= 1.5f)
                    gameStepForAll(2, null, null, 0);

                if (gameStep[3].Equals(true) && enemyTime >= 2f)
                    gameStepForAll(3, null, null, 0);

                if (gameStep[4].Equals(true) && enemyTime >= 2.5f)
                    gameStepForAll(4, null, null, int.Parse(guessCard.name));
            }
        }
        else
        {
            if (enemyCount.Equals(0))
                gameRule.text = "Player Win!";
            else if (playerCount.Equals(0))
                gameRule.text = "Enemy Win!";

        }
    }

    public void setYourChoice()
    {
        

        //04-1a. GO! 일때, 버튼을 누르면 우선 STOP으로 바뀐다
        if (goCardText.text.Equals("GO!") && int.Parse(guessCard.name) < 26)
        {
            goCardText.text = "STOP";
            gameStepForAll(4, null, null, ((goCardNum) * 2 + (int.Parse(sC[1].name) % 2)));
            gameStep[3] = false;
            gameStep[4] = true;
        }

        //04-1b. STOP일때, 버튼을 누르면 턴 종료
        else if (goCardText.text.Equals("STOP"))
        {
            closeGuessViewGraphic(0);
            playerTurn = false;
        }
    }

    void autoPick(List<int> PorEList, int pickVol)
    {
        int i = 0, bnw = Random.Range(0, 5);

        for (i = 0; i < pickVol; i++)
        {
            randomResult = Random.Range(0, curposList.Count);

            if (pickVol.Equals(4))
            {
                if (i < bnw)
                {
                    while (curposList[randomResult] % 2 == 1)
                        randomResult = Random.Range(0, curposList.Count);
                }
                else if (i >= bnw)
                {
                    while (curposList[randomResult] % 2 == 0)
                        randomResult = Random.Range(0, curposList.Count);
                }
            }
            addCardInList(PorEList, curposList[randomResult]);
        }
        displayListGraphic(PorEList);
    }
    void addCardInList(List<int> PorEList, int pickNum)
    {
        if (PorEList.Equals(mybw))
        {
            changeSprite(allCase, null, pickNum, pickNum.ToString(), 8); //layer 8로 변경 포함
            enemyStrategy.Add(pickNum, new List<int> { });
            playerCount++;
        }

        else
        {
            allCase[pickNum].layer = 9;
            enemyCount++;
        }

        PorEList.Add(pickNum);
        curposList.Remove(pickNum); //현재 뽑힌 수를 픽킹가능 대상 범위에서 제거한다

    }
    void displayListGraphic(List<int> PorEList)
    {
        int PorE, i, j = 0, k = 0;

        PorEList.Sort();

        if (PorEList.Equals(mybw))
            PorE = -1;
        else
            PorE = 1;

        for (i = 0; i < PorEList.Count; i++)
        {

            allCase[PorEList[i]].transform.position =
                new Vector3(PorE * 13.8f + (PorE * -2.5f * j), PorE * 18f - (PorE * 4f * k), 0);
            allCase[PorEList[i]].transform.rotation = Quaternion.Euler(0f, 0f, 0f);

            j++;

            if ((i + 1) % 12 == 0)
            {
                j = 0;
                k++;
            }
        }


        //우선 ?에 대해 범위폭이 좁은게 있나 없나 본다
        //없으면 걍 랜덤으로 아무거나 찔러본다
        //저 위치의 애들, -1,-2,-3 으로, 안되는 숫자들 써놓음..
        //-1,3
        //판이 바뀌었을때 Temp를 만들고 -의 순서를 다시
        //어쨌든 그 수가 이제 죽거나 내가 맞췄을 경우 -n에 대한증거는 다 지운다
        //
    }
    void enemyAlgorithm()
    {
        //-------------------적알고리즘-------------------//
        int algoresult_1 = 100;
        int algoresult_2 = 0;

        for (int i = 0; i < mybw.Count; i++)
        {

            int min = 0, max = 25;

            if (allCase[mybw[i]].layer != 11)
            {

                subs.Add(mybw[i], new List<int> { });

                for (int j = i - 1; j >= 0; j--)
                {
                    if (allCase[mybw[j]].layer == 11 && mybw[j] > min)
                        min = mybw[j];
                }

                for (int j = i + 1; j < mybw.Count; j++)
                {
                    if (allCase[mybw[j]].layer == 11 && mybw[j] < max)
                        max = mybw[j];
                }

                string a = min +", " + max + "\n흑백삭제: "; ///////////////////////////////////////////////

                //min부터 max까지의 범위를 더한다.
                for (int k = min; k <= max; k++)
                    subs[mybw[i]].Add(k);

                //흑/백을 구분해 아닌 것 삭제
                if (mybw[i] % 2 == 0)
                {
                    for (int j = 0; j < subs[mybw[i]].Count; j++)
                    {
                        if (subs[mybw[i]][j] % 2 != 0)
                        {
                            a += subs[mybw[i]][j] + "/"; ///////////////////////////////////////////////
                            subs[mybw[i]].RemoveAt(j);
                        }
                    }}

                else if (mybw[i] % 2 != 0)
                {
                    for (int j = 0; j < subs[mybw[i]].Count; j++)
                    {
                        if (subs[mybw[i]][j] % 2 == 0)
                        {
                            a += subs[mybw[i]][j] + "/"; ///////////////////////////////////////////////
                            subs[mybw[i]].RemoveAt(j);
                        }
                    }}

                a += "\n내꺼삭제: "; ///////////////////////////////////////////////

                //범위 내의 적 카드를 지운다
                for (int j = 0; j < enemybw.Count; j++)
                {
                    if (subs[mybw[i]].Contains(enemybw[j]))
                    {
                        a += enemybw[j] + "/";
                        subs[mybw[i]].Remove(enemybw[j]);
                    }
                }

                a += "\n증거삭제: ";

                //여기서 ? 수에 대해 틀렸던 증거가 있으면 그것도 지운다
                if (enemyStrategy[mybw[i]].Count > 0)
                {
                    for (int j = 0; j < enemyStrategy[mybw[i]].Count; j++)
                    {
                        if (subs[mybw[i]].Contains(enemyStrategy[mybw[i]][j]))
                        {
                            a += enemyStrategy[mybw[i]][j] + "/";
                            subs[mybw[i]].Remove(enemyStrategy[mybw[i]][j]);
                        }}}


                a += "\n죽은적삭제: ";

                for (int j = 0; j < mybw.Count; j++)
                {
                    if (allCase[mybw[j]].layer == 11 && subs[mybw[i]].Contains(mybw[j]))
                    {
                        a += mybw[j] + "/";
                        subs[mybw[i]].Remove(mybw[j]);
                       }
                }


                    //카드확인
                    a += "\n카드확인: ";

                for (int j = 0; j < subs[mybw[i]].Count; j++)
                    a += subs[mybw[i]][j] + "/";
                Debug.Log(a);
                
                if (subs[mybw[i]].Count < algoresult_1)
                {
                    algoresult_1 = subs[mybw[i]].Count;
                    algoresult_2 = mybw[i];
                }}}
        
        enemyChoice = algoresult_2;
    }
    void gameStepForAll(int a, List<int> b, GameObject c, int d)
    {
        switch (a)
        {
            case 0:

                addCardInList(b, d);
                displayListGraphic(b); // 소팅과 그래픽배치
                pickTriangleGraphic(sC[0], c); //고른 카드 위에 초록색 세모가 위에 그려진다
                gameRule.text = "Select Enemy Card";
                gameStep[0] = true;
                gameStep[1] = true;
                break;

            case 1:
                pickTriangleGraphic(sC[1], c);
                fSetupGuessGraphic(c); // 흑/백이냐에 따라 다음 단계 인터페이스가 달라진다
                gameStep[1] = false;
                gameStep[2] = true;
                break;

            case 2:
                StartCoroutine(sSetupGuessGraphic());

                if (gameRule.transform.localPosition.y >= 80)
                {
                    gameStep[2] = false;
                    gameStep[3] = true;
                }
                break;

            case 3:
                if (playerTurn.Equals(true))
                {
                    if (hit.collider != null && hit.collider.gameObject == sC[2])
                        changeNumPM(0, 13, -1);

                    else if (hit.collider != null && hit.collider.gameObject == sC[3])
                        changeNumPM(12, -1, 1);
                }

                else if (playerTurn.Equals(false))
                {
                    guessCard.name = (Random.Range(0, 13) * 2 + (int.Parse(sC[1].name) % 2)).ToString();
                    changeSprite(null, guessCard, 0, guessCard.name, 0);

                    if (enemyTime >= 2.5f)
                    {
                        guessCard.name = subs[enemyChoice][Random.Range(0, subs[enemyChoice].Count)].ToString();
                        changeSprite(null, guessCard, 0, guessCard.name, 0);

                        string enemyResult;
                        enemyResult = "조사카드: " + enemyChoice + ", 범위: ";

                        for (int i = 0; i < subs[enemyChoice].Count; i++)
                            enemyResult += subs[enemyChoice][i] +"/";

                        enemyResult += "선택수: " + guessCard.name;
                        Debug.Log(enemyResult);

                        gameStep[3] = false;
                        gameStep[4] = true;

                    }
                }
                break;


            case 4:
                
                if (playerTurn.Equals(true))
                {
                    if (d == int.Parse(sC[1].name))
                    {
                        gameRule.text = "Correct!";
                        changeSprite(allCase, null, int.Parse(sC[1].name), sC[1].name, 11);
                        enemyCount--;
                    }

                    else
                    {
                        gameRule.text = "Wrong...";
                        changeSprite(allCase, null, int.Parse(sC[0].name), sC[0].name, 11);
                        playerCount--;
                        enemyStrategy.Remove(int.Parse(sC[0].name));
                    }
                }

                if (playerTurn.Equals(false))
                {
                    if (final_2 == true)
                    {
                        if (d == int.Parse(sC[1].name))
                        {
                            gameRule.text = "Correct!";
                            changeSprite(allCase, null, int.Parse(sC[1].name), sC[1].name, 11);
                            playerCount--;
                            enemyStrategy.Remove(int.Parse(sC[0].name));

                            if (playerCount != 0 && enemyCount != 0)
                            {
                                subs.Clear();
                                enemyAlgorithm();

                                if (subs[enemyChoice].Count < 3)
                                    final = false;
                                else
                                    final = true;
                            }

                        }

                        else
                        {
                            gameRule.text = "Wrong...";
                            enemyStrategy[int.Parse(sC[1].name)].Add(d); // [i] 내가 고른 곳의 순서
                            changeSprite(allCase, null, int.Parse(sC[0].name), sC[0].name, 11);
                            enemyCount--;
                            final = true;
                        }
                        final_2 = false;
                    }


                    if (enemyTime >= 3.3f)
                    {
                        if(final == false)
                            closeGuessViewGraphic(1);
                        else if(final == true)
                            closeGuessViewGraphic(0);
                        
                        playerTurn = final;
                    }

                }

                break;

            default: // case가 지정되지 않은 기본값
                break;
        }

        if (a < 2)
            target = null;

    }
    void changeSprite(GameObject[] a, GameObject a_1, int b, string c, int d)
    {

        if (d != 0)
            a[b].layer = d;

        if (a_1 == null)
            a[b].GetComponent<SpriteRenderer>().sprite = Resources.Load(c, typeof(Sprite)) as Sprite;

        if (a == null)
            a_1.GetComponent<SpriteRenderer>().sprite = Resources.Load(c, typeof(Sprite)) as Sprite;


    }
    void pickTriangleGraphic(GameObject selectSide, GameObject pickSideCard)
    {
        selectSide.transform.position =
            new Vector3(pickSideCard.transform.position.x, pickSideCard.transform.position.y + 3f, 0f);
        selectSide.name = pickSideCard.name;
    }
    void fSetupGuessGraphic(GameObject target)
    {
        if (int.Parse(target.name) % 2 == 0)
        {
            goCard.image.color = Color.black;
            goCardText.color = Color.white;
            changeSprite(null, guessCard, 0, "26", 0);
            changeSprite(sC, null, 2, "guessBCard", 0);
            changeSprite(sC, null, 3, "guessBCard", 0);
        }

        else
        {
            changeSprite(null, guessCard, 0, "27", 0);
            changeSprite(sC, null, 2, "guessWCard", 0);
            changeSprite(sC, null, 3, "guessWCard", 0);
        }
    }
    IEnumerator sSetupGuessGraphic()
    {
        gameRule.text = "Guess Number";
        gameRule.transform.Translate(new Vector3(0f, 3f, 0f));
        guessView.transform.localScale += new Vector3(0.25f, 0.3f, 0f);
        guessCard.transform.localScale += new Vector3(0.08f, 0.08f, 0f);

        if (playerTurn.Equals(true))
        {
            goCard.transform.Translate(new Vector3(0f, -2.9f, 0f));
            goCard.transform.localScale += new Vector3(0.025f, 0.025f, 0f);
            sC[2].transform.localScale += new Vector3(0.08f, 0.08f, 0f);
            sC[2].transform.Translate(new Vector3(0f, -0.2f, 0f));
            sC[3].transform.localScale += new Vector3(0.08f, 0.08f, 0f);
            sC[3].transform.Translate(new Vector3(0f, -0.2f, 0f));
        }

        yield return new WaitForSeconds(0.0f);
    }

    void changeNumPM(int a, int b, int c)
    {
        if (goCardNum == -100 && c == 1)
            goCardNum = 0;

        else if (goCardNum == -100 && c == -1)
            goCardNum = 12;

        else if (goCardNum != -100)
        {
            goCardNum = goCardNum + c;

            if (goCardNum == 13)
                goCardNum = 0;
            else if (goCardNum == -1)
                goCardNum = 12;

        }

        guessCard.name = ((goCardNum) * 2 + (int.Parse(sC[1].name) % 2)).ToString();
        guessCard.GetComponent<SpriteRenderer>().sprite =
           Resources.Load(guessCard.name, typeof(Sprite)) as Sprite;

    }
    void closeGuessViewGraphic(int a)
    {
        gameRule.transform.localPosition = new Vector3(0f, 0f, 0f);
        gameRule.text = "Pick Card";

        goCard.transform.localPosition = new Vector3(0f, 0f, 0f);
        goCard.transform.localScale = new Vector3(0f, 0f, 0f);
        goCardText.text = "GO!";

        guessView.transform.position = new Vector3(0f, 0f, -2f);
        guessView.transform.localScale = new Vector3(0f, 0f, 0f);

        guessCard.transform.position = new Vector3(0f, 0f, -4f);
        guessCard.transform.localScale = new Vector3(0f, 0f, 0f);

        sC[2].transform.position = new Vector3(0f, 0f, -4f);
        sC[2].transform.localScale = new Vector3(0f, 0f, 0f);

        sC[3].transform.position = new Vector3(0f, 0f, -4f);
        sC[3].transform.localScale = new Vector3(0f, 0f, 0f);

        subs.Clear();
        enemyTime = 0f;
        goCardNum = -100;
        target = null;
        final_2 = true;

        if (a == 1)
        {
            gameRule.text = "Select Enemy Card";
            gameStep[0] = true;
            gameStep[1] = true;
        }

        else if (a == 0)
        {
            sC[0].transform.position = new Vector3(-20f, 0f, 0f);
            sC[1].transform.position = new Vector3(-20f, 0f, 0f);
            gameStep[4] = false;
            gameStep[0] = false;
        }


        cST.text = "";
        
            for (int i = 0; i < mybw.Count; i++)
            {
                if (allCase[mybw[i]].layer == 11)
                    cST.text += "X";

                else
                    cST.text += "O";
            }
    }





}
