package XXLChess;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;


import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.event.MouseEvent;

import javax.sound.sampled.*;

public class App extends PApplet {
    public static final int SPRITESIZE = 480;
    public static final int CELLSIZE = 48;
    public static final int SIDEBAR = 120;
    public static final int BOARD_WIDTH = 14;
    public static int WIDTH = 792;
    public static int HEIGHT = 672;
    public static final int FPS = 60;
    public String configPath = "config.json";
    /**
     * The Coordinate of the chess last move
     */
    public int[] CurrentCood;
    /**
     * The past coordinate of the chess last move
     */
    public int[] PastCood;
    /**
     * The White King object
     */
    public Chess king1; // the white king
    /**
     * The Black King object
     */
    public Chess king2; // the black king
    /**
     * The ArrayList of all Chess available
     */
    public ArrayList<Chess> chessList;
    /**
     * The ArrayList of all Chess available of enemy
     */
    public ArrayList<Chess> enemyChessList;
    /**
     * The ArrayList of all Chess available of player
     */
    public ArrayList<Chess> playerChessList;
    /**
     * The Boolean of whether player choose a Chess
     */
    public boolean chessChosen;
    /**
     * The Row of mouse
     */
    public int mouseRow;
    /**
     * The Column of mouse
     */
    public int mouseColumn;
    /**
     * The Row of mouse click
     */
    public int clickedRow;
    /**
     * The Column of mouse click
     */
    public int clickedColumn;
    /**
     * Remaining seconds of player
     */
    public int  player_seconds;
    /**
     * Time increment of player
     */
    public int  player_increment;
    /**
     * Remaining minutes of player
     */
    public int player_min;
    /**
     * Sketch of player to calculate a second
     */
    public int player_sketch;
    /**
     * Boolean of whether player is time_out
     */
    public boolean playerTimeOut;
    /**
     * Remaining seconds of enemy
     */
    public int enemy_seconds;
    /**
     * Time increment of enemy
     */
    public int enemy_increment;
    /**
     * Remaining minutes of penemy
     */
    public int enemy_min;
    /**
     * Sketch of enemy to calculate a second
     */
    public int enemy_sketch;
    /**
     * Boolean of whether enemy is time_out
     */
    public boolean enemyTimeOut;
    /**
     * The Color of player choose, 0 is white, 1 is black
     */
    public int playerChosenColor;
    /**
     * The max time for chess to move
     */
    public double maxTime;
    /**
     * The Chess object player choose
     */
    public Chess ChosenChess;
    /**
     * The Chess object enemy choose
     */
    public Chess enemyChess;
    /**
     * An abandoned boolean variable, in order to prevent bugs, I remain it here
     */
    public boolean blackKingIsDanger;
    /**
     * An abandoned boolean variable, in order to prevent bugs, I remain it here
     */
    public boolean whiteKingIsDanger;
    /**
     * Boolean of whether game is over
     */
    public boolean gameOver;
    /**
     * Boolean of whether it is enemy's turn
     */
    public boolean isEnemyTurn;
    /**
     * Boolean of player's king is checked
     */
    public boolean playerIsCheck;
    /**
     * Boolean of whether player win the game
     */
    public boolean playerWin;
    /**
     * Boolean of whether enemy win the game
     */
    public boolean enemyWin;
    /**
     * Boolean of enemy's king is checked
     */
    public boolean enemyIsCheck;
    /**
     * Boolean of whether enemy should move
     */
    public boolean enemyShouldMove;
    /**
     * The Color of enemy
     */
    public int enemyColor;
    /**
     * The seconds of warning time has been passed
     */
    public int warnTimes;
    /**
     * The boolean of whether player should be warned
     */
    public boolean warning;
    /**
     * The boolean of whether sound of end has been played because of time out
     */
    public boolean timeWinSound;
    /**
     * The speed of chess movement
     */
    public double speed;
    /**
     * The Arraylist of move area of chess
     */
    public ArrayList<int[]> moveArea;
    /**
     * The Arraylist of attack area of chess
     */
    public ArrayList<int[]> attackArea;
    /**
     * The Arraylist of safe area of chess
     */
    public ArrayList<int[]> safeArea;
    /**
     * The Arraylist of white board location
     */
    public ArrayList<int[]> whiteBoard;
    /**
     * The Arraylist of brown board location
     */
    public ArrayList<int[]> brownBoard;
    public Scanner scanner;

    public PFont f;
    /**
     * Creates a new App object.
     */
    public App() {}
    /**
     * Setting of game
     */
    public void settings() {
        this.size(WIDTH, HEIGHT);
    }
    /**
     * Set up the ChessBoard
     */
    public void setChessBoard(){
        fill(246,218,183);
        for(int i = 0;i<672;i+=96){
            for(int j = 0;j<=672;j+=96){
                rect(i,j,48,48);
                this.whiteBoard.add(new int[]{i,j});
            }
        }
        for(int i = 48;i<672;i+=96){
            for(int j = 48;j<=672;j+=96){
                rect(i,j,48,48);
                this.whiteBoard.add(new int[]{i,j});
            }
        }
        fill(181,136,101);
        for(int i = 48;i<672;i+=96){
            for(int j = 0;j<=672;j+=96){
                rect(i,j,48,48);
                this.brownBoard.add(new int[]{i,j});
            }
        }
        for(int i = 0;i<672;i+=96){
            for(int j = 48;j<=672;j+=96){
                rect(i,j,48,48);
                this.brownBoard.add(new int[]{i,j});
            }
        }
    }
    /**
     * Create the Chess object with the coordinates input
     * @param str The input text which decide the type and color of chess
     * @param row The row of Chess
     * @param column The column of Chess
     */
    public void putChess(String str, int row, int column){
        if(str.equals("P")){
            Chess chess = new Chess(row*48,column*48,"pawn",1,1,this.speed,this.maxTime);
            this.chessList.add(chess);
        }
        if(str.equals("p")){
            Chess chess = new Chess(row*48,column*48,"pawn",0,1,this.speed,this.maxTime);
            this.chessList.add(chess);
        }
        if(str.equals("K")){
            this.king2 = new Chess(row*48,column*48,"king",1,999,this.speed,this.maxTime);
            this.chessList.add(this.king2);
        }
        if(str.equals("k")){
            this.king1 = new Chess(row*48,column*48,"king",0,999,this.speed,this.maxTime);
            this.chessList.add(this.king1);
        }
        if(str.equals("R")){
            Chess chess = new Chess(row*48,column*48,"rook",1,5.25,this.speed,this.maxTime);
            this.chessList.add(chess);
        }
        if(str.equals("r")){
            Chess chess = new Chess(row*48,column*48,"rook",0,5.25,this.speed,this.maxTime);
            this.chessList.add(chess);
        }
        if(str.equals("N")){
            Chess chess = new Chess(row*48,column*48,"knight",1,2,this.speed,this.maxTime);
            this.chessList.add(chess);
        }
        if(str.equals("n")){
            Chess chess = new Chess(row*48,column*48,"knight",0,2,this.speed,this.maxTime);
            this.chessList.add(chess);
        }
        if(str.equals("B")){
            Chess chess = new Chess(row*48,column*48,"bishop",1,3.625,this.speed,this.maxTime);
            this.chessList.add(chess);
        }
        if(str.equals("b")){
            Chess chess = new Chess(row*48,column*48,"bishop",0,3.625,this.speed,this.maxTime);
            this.chessList.add(chess);
        }
        if(str.equals("H")){
            Chess chess = new Chess(row*48,column*48,"archbishop",1,7.5,this.speed,this.maxTime);
            this.chessList.add(chess);
        }
        if(str.equals("h")){
            Chess chess = new Chess(row*48,column*48,"archbishop",0,7.5,this.speed,this.maxTime);
            this.chessList.add(chess);
        }
        if(str.equals("C")){
            Chess chess = new Chess(row*48,column*48,"camel",1,2,this.speed,this.maxTime);
            this.chessList.add(chess);
        }
        if(str.equals("c")){
            Chess chess = new Chess(row*48,column*48,"camel",0,2,this.speed,this.maxTime);
            this.chessList.add(chess);
        }
        if(str.equals("G")){
            Chess chess = new Chess(row*48,column*48,"guard",1,5,this.speed,this.maxTime);
            this.chessList.add(chess);
        }
        if(str.equals("g")){
            Chess chess = new Chess(row*48,column*48,"guard",0,5,this.speed,this.maxTime);
            this.chessList.add(chess);
        }
        if(str.equals("A")){
            Chess chess = new Chess(row*48,column*48,"amazon",1,12,this.speed,this.maxTime);
            this.chessList.add(chess);
        }
        if(str.equals("a")){
            Chess chess = new Chess(row*48,column*48,"amazon",0,12,this.speed,this.maxTime);
            this.chessList.add(chess);
        }
        if(str.equals("E")){
            Chess chess = new Chess(row*48,column*48,"chancellor",1,8.5,this.speed,this.maxTime);
            this.chessList.add(chess);
        }
        if(str.equals("e")){
            Chess chess = new Chess(row*48,column*48,"chancellor",0,8.5,this.speed,this.maxTime);
            this.chessList.add(chess);
        }
        if(str.equals("Q")){
            Chess chess = new Chess(row*48,column*48,"queen",1,9.5,this.speed,this.maxTime);
            this.chessList.add(chess);
        }
        if(str.equals("q")){
            Chess chess = new Chess(row*48,column*48,"queen",0,9.5,this.speed,this.maxTime);
            this.chessList.add(chess);
        }
        if(str.equals("V")){
            Chess chess = new Chess(row*48,column*48,"cannon",1,4.5,this.speed,this.maxTime);
            this.chessList.add(chess);
        }
        if(str.equals("v")){
            Chess chess = new Chess(row*48,column*48,"cannon",0,4.5,this.speed,this.maxTime);
            this.chessList.add(chess);
        }
    }
    /**
     * Initialize all the global variables when game start or restart
     */
    public void initialize(){
        this.chessList= new ArrayList<Chess>();
        this.moveArea= new ArrayList<int[]>();
        this.attackArea= new ArrayList<int[]>();
        this.safeArea= new ArrayList<int[]>();
        this.whiteBoard= new ArrayList<int[]>();
        this.brownBoard= new ArrayList<int[]>();
        this.enemyChessList= new ArrayList<Chess>();
        this.playerChessList = new ArrayList<Chess>();
        this.CurrentCood = new int[]{999,999};
        this.PastCood = new int[]{999,999};
        this.chessChosen = false;
        this.blackKingIsDanger = false;
        this.whiteKingIsDanger = false;
        this.enemyIsCheck =false;
        this.playerIsCheck =false;
        this.gameOver = false;
        this.isEnemyTurn = false;
        this.playerTimeOut = false;
        this.enemyTimeOut = false;
        this.playerChosenColor = 0;
        this.enemyColor = 1 - this.playerChosenColor;
        this.enemyShouldMove = false;
        this.enemyWin = false;
        this.playerWin = false;
        this.warning = false;
        this.timeWinSound = false;
        this.enemyChess = null;
        this.warnTimes = 0;
        JSONObject conf = loadJSONObject(new File(this.configPath));
        String fileName = conf.getString("layout");
        this.speed = conf.getDouble("piece_movement_speed");
        this.maxTime = conf.getDouble("max_movement_time");
        File file = new File(fileName);
        try {
            this.scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        String line = "";
        int column = 0;
        int row = 0;
        List<String> boardLine;
        while(this.scanner.hasNextLine()){
            line = this.scanner.nextLine();
            String[] str = line.split("");
            boardLine = Arrays.asList(str);
            for(String s: boardLine){
                this.putChess(s,row,column);
                row+=1;
            }
            line = "";
            column+=1;
            row = 0;

        }
        JSONObject arr = conf.getJSONObject("time_controls");
        JSONObject player = arr.getJSONObject("player");
        this.player_seconds = player.getInt("seconds");
        this.player_increment = player.getInt("increment");
        this.player_min = this.player_seconds/60;
        this.player_seconds = this.player_seconds % 60;
        this.player_sketch = 0;

        JSONObject cpu = arr.getJSONObject("cpu");
        this.enemy_seconds = cpu.getInt("seconds");
        this.enemy_increment = cpu.getInt("increment");
        this.enemy_min = this.enemy_seconds/60;
        this.enemy_seconds = this.enemy_seconds % 60;
        this.enemy_sketch = 0;
        String color = conf.getString("player_colour");
        if(color.equals("white")){
            this.playerChosenColor = 0;
        }else{
            this.playerChosenColor = 1;
        }
        this.enemyColor = 1 - this.playerChosenColor;


    }
    /**
     * The setup function will be called when game start or restart
     */
    public void setup() {
        this.frameRate(60.0F);
        this.initialize();
        this.soundJudge(3);

        this.f = createFont("Times New Roman" , 48);
        PImage tempImg = this.loadImage("src/main/resources/XXLChess/w-pawn.png");
        tempImg.resize(48,48);
        for(Chess chess:chessList){
            if(chess.getType().equals("pawn")&&chess.getColor()==0){
                chess.setSprite(tempImg);
            }
        }
        tempImg = this.loadImage("src/main/resources/XXLChess/b-pawn.png");
        tempImg.resize(48,48);
        for(Chess chess:chessList){
            if(chess.getType().equals("pawn")&&chess.getColor()==1){
                chess.setSprite(tempImg);
            }
        }
        tempImg = this.loadImage("src/main/resources/XXLChess/w-rook.png");
        tempImg.resize(48,48);
        for(Chess chess:chessList){
            if(chess.getType().equals("rook")&&chess.getColor()==0){
                chess.setSprite(tempImg);
            }
        }
        tempImg = this.loadImage("src/main/resources/XXLChess/b-rook.png");
        tempImg.resize(48,48);
        for(Chess chess:chessList){
            if(chess.getType().equals("rook")&&chess.getColor()==1){
                chess.setSprite(tempImg);
            }
        }
        tempImg = this.loadImage("src/main/resources/XXLChess/w-knight.png");
        tempImg.resize(48,48);
        for(Chess chess:chessList){
            if(chess.getType().equals("knight")&&chess.getColor()==0){
                chess.setSprite(tempImg);
            }
        }
        tempImg = this.loadImage("src/main/resources/XXLChess/b-knight.png");
        tempImg.resize(48,48);
        for(Chess chess:chessList){
            if(chess.getType().equals("knight")&&chess.getColor()==1){
                chess.setSprite(tempImg);
            }
        }
        tempImg = this.loadImage("src/main/resources/XXLChess/w-bishop.png");
        tempImg.resize(48,48);
        for(Chess chess:chessList){
            if(chess.getType().equals("bishop")&&chess.getColor()==0){
                chess.setSprite(tempImg);
            }
        }
        tempImg = this.loadImage("src/main/resources/XXLChess/b-bishop.png");
        tempImg.resize(48,48);
        for(Chess chess:chessList){
            if(chess.getType().equals("bishop")&&chess.getColor()==1){
                chess.setSprite(tempImg);
            }
        }
        tempImg = this.loadImage("src/main/resources/XXLChess/w-archbishop.png");
        tempImg.resize(48,48);
        for(Chess chess:chessList){
            if(chess.getType().equals("archbishop")&&chess.getColor()==0){
                chess.setSprite(tempImg);
            }
        }
        tempImg = this.loadImage("src/main/resources/XXLChess/b-archbishop.png");
        tempImg.resize(48,48);
        for(Chess chess:chessList){
            if(chess.getType().equals("archbishop")&&chess.getColor()==1){
                chess.setSprite(tempImg);
            }
        }
        tempImg = this.loadImage("src/main/resources/XXLChess/w-camel.png");
        tempImg.resize(48,48);
        for(Chess chess:chessList){
            if(chess.getType().equals("camel")&&chess.getColor()==0){
                chess.setSprite(tempImg);
            }
        }
        tempImg = this.loadImage("src/main/resources/XXLChess/b-camel.png");
        tempImg.resize(48,48);
        for(Chess chess:chessList){
            if(chess.getType().equals("camel")&&chess.getColor()==1){
                chess.setSprite(tempImg);
            }
        }
        tempImg = this.loadImage("src/main/resources/XXLChess/w-knight-king.png");
        tempImg.resize(48,48);
        for(Chess chess:chessList){
            if(chess.getType().equals("guard")&&chess.getColor()==0){
                chess.setSprite(tempImg);
            }
        }
        tempImg = this.loadImage("src/main/resources/XXLChess/b-knight-king.png");
        tempImg.resize(48,48);
        for(Chess chess:chessList){
            if(chess.getType().equals("guard")&&chess.getColor()==1){
                chess.setSprite(tempImg);
            }
        }
        tempImg = this.loadImage("src/main/resources/XXLChess/w-queen.png");
        tempImg.resize(48,48);
        for(Chess chess:chessList){
            if(chess.getType().equals("queen")&&chess.getColor()==0){
                chess.setSprite(tempImg);
            }
        }
        tempImg = this.loadImage("src/main/resources/XXLChess/b-queen.png");
        tempImg.resize(48,48);
        for(Chess chess:chessList){
            if(chess.getType().equals("queen")&&chess.getColor()==1){
                chess.setSprite(tempImg);
            }
        }
        tempImg = this.loadImage("src/main/resources/XXLChess/w-amazon.png");
        tempImg.resize(48,48);
        for(Chess chess:chessList){
            if(chess.getType().equals("amazon")&&chess.getColor()==0){
                chess.setSprite(tempImg);
            }
        }
        tempImg = this.loadImage("src/main/resources/XXLChess/b-amazon.png");
        tempImg.resize(48,48);
        for(Chess chess:chessList){
            if(chess.getType().equals("amazon")&&chess.getColor()==1){
                chess.setSprite(tempImg);
            }
        }
        tempImg = this.loadImage("src/main/resources/XXLChess/w-chancellor.png");
        tempImg.resize(48,48);
        for(Chess chess:chessList){
            if(chess.getType().equals("chancellor")&&chess.getColor()==0){
                chess.setSprite(tempImg);
            }
        }
        tempImg = this.loadImage("src/main/resources/XXLChess/b-chancellor.png");
        tempImg.resize(48,48);
        for(Chess chess:chessList){
            if(chess.getType().equals("chancellor")&&chess.getColor()==1){
                chess.setSprite(tempImg);
            }
        }
        tempImg = this.loadImage("src/main/resources/XXLChess/w-cannon.png");
        tempImg.resize(48,48);
        for(Chess chess:chessList){
            if(chess.getType().equals("cannon")&&chess.getColor()==0){
                chess.setSprite(tempImg);
            }
        }

        tempImg = this.loadImage("src/main/resources/XXLChess/b-cannon.png");
        tempImg.resize(48,48);
        for(Chess chess:chessList){
            if(chess.getType().equals("cannon")&&chess.getColor()==1){
                chess.setSprite(tempImg);
            }
        }

        tempImg = this.loadImage("src/main/resources/XXLChess/w-king.png");
        tempImg.resize(48,48);
        this.king1.setSprite(tempImg);
        tempImg = this.loadImage("src/main/resources/XXLChess/b-king.png");
        tempImg.resize(48,48);
        this.king2.setSprite(tempImg);


    }
    /**
     * The function will be called only when any of keyBoard is pressed
     */

    public void keyPressed() {
        if(key == 'r'){
            setup();
        }
        if(key == 'y'){
            if(this.gameOver==true){
                setup();
            }
        }
    }

    public void keyReleased() {
    }
    /**
     * The function to add the move and attack area of a rook Chess to the moveArea and attackArea
     * @param chess The Chess object to move or attack
     * @param color The Color of the Chess object
     */
    public void rookMove(Chess chess, int color){
        chess.rookMove(this.chessList);
        ArrayList<int[]> tempList = new ArrayList<int[]>();
        for(int[] place:chess.getMoveArea()){
            this.moveArea.add(place);
        }
        for(int[] place:chess.getAttackArea()){
            this.attackArea.add(place);
        }
    }
    /**
     * The function to add the move and attack area of a knight Chess to the moveArea and attackArea
     * @param chess The Chess object to move or attack
     * @param color The Color of the Chess object
     */
    public void knightMove(Chess chess,int color){
        chess.knightMove(this.chessList);
        ArrayList<int[]> tempList = new ArrayList<int[]>();
        for(int[] place:chess.getMoveArea()){
            this.moveArea.add(place);
        }
        for(int[] place:chess.getAttackArea()){
            this.attackArea.add(place);
        }
    }
    /**
     * The function to add the move and attack area of a cannon Chess to the moveArea and attackArea
     * @param chess The Chess object to move or attack
     * @param color The Color of the Chess object
     */
    public void cannonMove(Chess chess,int color){
        chess.cannonMove(this.chessList);
        ArrayList<int[]> tempList = new ArrayList<int[]>();
        for(int[] place:chess.getMoveArea()){
            this.moveArea.add(place);
        }
        for(int[] place:chess.getAttackArea()){
            this.attackArea.add(place);
        }
    }
    /**
     * The function to add the move and attack area of a camel Chess to the moveArea and attackArea
     * @param chess The Chess object to move or attack
     * @param color The Color of the Chess object
     */
    public void camelMove(Chess chess, int color){
        chess.camelMove(this.chessList);
        ArrayList<int[]> tempList = new ArrayList<int[]>();
        for(int[] place:chess.getMoveArea()){
            this.moveArea.add(place);
        }
        for(int[] place:chess.getAttackArea()){
            this.attackArea.add(place);
        }
    }
    /**
     * The function to add the move and attack area of a king Chess to the moveArea and attackArea
     * @param chess The Chess object to move or attack
     * @param color The Color of the Chess object
     */
    public void kingMove(Chess chess, int color){
        int[] tempList = new int[2];
        for(int i = -1;i<=1;i++){
            for(int j = -1;j<=1;j++){
                if(chess.getRow()+i>=0 && chess.getRow()+i<=13){
                    if(chess.getColumn()+j>=0 && chess.getColumn()+j<=13){
                        if(i!=0||j!=0){
                            tempList = new int[2];
                            tempList[0] = chess.getRow()+i;
                            tempList[1] = chess.getColumn()+j;
                            this.moveArea.add(tempList);
                            for(Chess oppChess:chessList){
                                if(tempList[0]==oppChess.getRow()&&tempList[1]==oppChess.getColumn()){
                                    this.moveArea.remove(tempList);
                                    if(oppChess.getColor()!=color){
                                        this.attackArea.add(tempList);
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }
    /**
     * The function to add the move and attack area of a bishop Chess to the moveArea and attackArea
     * @param chess The Chess object to move or attack
     * @param color The Color of the Chess object
     */
    public void bishopMove(Chess chess, int color){
        int[] tempList = new int[2];
        // up left
        int i = chess.getRow()-1;
        int j = chess.getColumn()-1;
        boolean block = false;
        while(i>=0&&j>=0&&block==false){
            tempList[0] = i;
            tempList[1] = j;
            this.moveArea.add(tempList);
            for(Chess oppChess:chessList) {
                if (oppChess.getRow() == i && oppChess.getColumn() == j) {
                    this.moveArea.remove(tempList);
                    if(oppChess.getColor()!=color){
                        this.attackArea.add(tempList);
                    }
                    block=true;
                }
            }
            i--;
            j--;
            tempList = new int[2];
        }
        // up right
        block = false;
        i = chess.getRow()+1;
        j = chess.getColumn()-1;
        while(i<=13&&j>=0&&block==false){
            tempList[0] = i;
            tempList[1] = j;
            this.moveArea.add(tempList);
            for(Chess oppChess:chessList) {
                if (oppChess.getRow() == i && oppChess.getColumn() == j) {
                    this.moveArea.remove(tempList);
                    if(oppChess.getColor()!=color){
                        this.attackArea.add(tempList);
                    }
                    block = true;
                }
            }
            i++;
            j--;
            tempList = new int[2];
        }
        // down left
        block = false;
        i = chess.getRow()-1;
        j = chess.getColumn()+1;
        while(i>=0&&j<=13&&block==false){
            tempList[0] = i;
            tempList[1] = j;
            this.moveArea.add(tempList);
            for(Chess oppChess:chessList) {
                if (oppChess.getRow() == i && oppChess.getColumn() == j) {
                    this.moveArea.remove(tempList);
                    if(oppChess.getColor()!=color){
                        this.attackArea.add(tempList);
                    }
                    block = true;
                }
            }
            i--;
            j++;
            tempList = new int[2];
        }
        //down right
        block =false;
        i = chess.getRow()+1;
        j = chess.getColumn()+1;
        while(i<=13&&j<=13&&block==false){
            tempList[0] = i;
            tempList[1] = j;
            this.moveArea.add(tempList);
            for(Chess oppChess:chessList) {
                if (oppChess.getRow() == i && oppChess.getColumn() == j) {
                    this.moveArea.remove(tempList);
                    if(oppChess.getColor()!=color){
                        this.attackArea.add(tempList);
                    }
                    block=true;
                }
            }
            i++;
            j++;
            tempList = new int[2];
        }
    }
    /**
     * The function to add the move and attack area of a pawn Chess to the moveArea and attackArea
     * @param chess The Chess object to move or attack
     * @param color The Color of the Chess object
     */
    public void pawnMove(Chess chess, int color){
        chess.pawnMove(this.chessList);
        ArrayList<int[]> tempList = new ArrayList<int[]>();
        for(int[] place:chess.getMoveArea()){
            this.moveArea.add(place);
        }
        for(int[] place:chess.getAttackArea()){
            this.attackArea.add(place);
        }
    }
    /**
     * The function to calculate whether the input king Chess is checked
     * @param king The king Chess to be checked
     * @param color The Color of the Chess object
     */
    public void check(Chess king, int color){

        this.moveArea = new ArrayList<int[]>();
        this.attackArea = new ArrayList<int[]>();
        for(Chess chess:this.chessList){
            if(chess.getColor()!=color){
                typeJudge(chess,chess.getColor());
            }
        }
        if(this.attackArea.size()>0){
            for(int[] tempList:this.attackArea){
                if(tempList[0] == king.getRow() && tempList[1] == king.getColumn()){
                    if(this.playerChosenColor != color){
                        this.enemyIsCheck = true;
                    }else{
                        this.playerIsCheck = true;

                    }
                }
            }
        }
        this.moveArea = new ArrayList<int[]>();
        this.attackArea = new ArrayList<int[]>();
    }
    /**
     * The function to change the input pawn Chess into a new Queen Chess
     * @param chess The Chess object to be changed
     * @param color The Color of the Chess object
     * @return A new Chess object with type of "queen"
     */
    public Chess pawnToQueen(Chess chess,int color){
        this.chessList.remove(chess);
        Chess newChess = new Chess(chess.getX(),chess.getY(),"queen",color,9.5,this.speed,this.maxTime);
        this.chessList.add(newChess);
        PImage tempImg = null;
        if(color==0){
            tempImg = this.loadImage("src/main/resources/XXLChess/w-queen.png");
            tempImg.resize(48,48);
        }else{
            tempImg = this.loadImage("src/main/resources/XXLChess/b-queen.png");
            tempImg.resize(48,48);
        }
        newChess.setSprite(tempImg);
        return newChess;
    }
    /**
     * The function add the player's time
     */
    public void playerTimeAdd(){
        if(this.player_seconds<60 - this.player_increment){
            this.player_seconds = this.player_seconds + this.player_increment;
        }else{
            this.player_seconds = this.player_seconds + this.player_increment;
            this.player_min = this.player_min + this.player_seconds / 60;
            this.player_seconds -= 60;
        }
    }
    /**
     * The function add the enemy's time
     */
    public void enemyTimeAdd(){
        if(this.enemy_seconds<60 - this.enemy_increment){
            this.enemy_seconds = this.enemy_seconds + this.enemy_increment;
        }else{
            this.enemy_seconds = this.enemy_seconds + this.enemy_increment;
            this.enemy_min = this.enemy_min + this.enemy_seconds / 60;
            this.enemy_seconds -= 60;
        }
    }
    /**
     * The function to play the sound in different scenario
     * @param type: The type of scenario, 0 is move, 1 is attack, 2 is king-check
     *              3 is game start, 4 is game over, 5 is let play defend his king
     */
    public void soundJudge(int type){
        File soundFile = null;
        if(type==0){
            soundFile = new File("src/main/resources/XXLChess/move.wav");
        }
        if(type==1){
            soundFile = new File("src/main/resources/XXLChess/attack.wav");
        }
        if(type==2){
            soundFile = new File("src/main/resources/XXLChess/check.wav");
        }
        if(type==3){
            soundFile = new File("src/main/resources/XXLChess/start.wav");
        }
        if(type==4){
            soundFile = new File("src/main/resources/XXLChess/end.wav");
        }
        if(type==5){
            soundFile = new File("src/main/resources/XXLChess/attention.wav");
        }
        if(soundFile.exists()){
            try {
                AudioInputStream am = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(am);
                clip.start();
            } catch (UnsupportedAudioFileException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * The function called when the mouse click
     * first click to choose the chess, second click to move the chess
     */
    public void mouseClicked(MouseEvent e) {
        if(this.gameOver==false) {
            if (this.chessChosen == false) {
                this.mouseRow = mouseX / 48;
                this.mouseColumn = mouseY / 48;
                for (Chess chess : chessList) {
                    if (chess.getRow() == mouseRow && chess.getColumn() == mouseColumn && chess.getColor() == this.playerChosenColor) {
                        if (chess.getType().equals("pawn")) {
                            this.pawnMove(chess, this.playerChosenColor);

                        } // end of move of pawn
                        if (chess.getType().equals("king")) {
                            this.kingMove(chess, this.playerChosenColor);
                        } // end of move of king
                        if (chess.getType().equals("rook")) {
                            this.rookMove(chess, this.playerChosenColor);
                        } // end of move of rook
                        if (chess.getType().equals("knight")) {
                            this.knightMove(chess, this.playerChosenColor);
                        } // end of move of knight
                        if (chess.getType().equals("cannon")) {
                            this.cannonMove(chess, this.playerChosenColor);
                        }
                        if (chess.getType().equals("bishop")) {
                            this.bishopMove(chess, this.playerChosenColor);
                        } // end of move of bishop
                        if (chess.getType().equals("queen")) {
                            this.rookMove(chess, this.playerChosenColor);
                            this.bishopMove(chess, this.playerChosenColor);
                        }// end of move of queen
                        if (chess.getType().equals("camel")) {
                            this.camelMove(chess, this.playerChosenColor);
                        } // end of move of camel
                        if (chess.getType().equals("archbishop")) {
                            this.knightMove(chess, this.playerChosenColor);
                            this.bishopMove(chess, this.playerChosenColor);
                        } // end of arch
                        if (chess.getType().equals("guard")) {
                            this.knightMove(chess, this.playerChosenColor);
                            this.kingMove(chess, this.playerChosenColor);
                        }
                        if (chess.getType().equals("amazon")) {
                            this.knightMove(chess, this.playerChosenColor);
                            this.rookMove(chess, this.playerChosenColor);
                            this.bishopMove(chess, this.playerChosenColor);
                        }
                        if (chess.getType().equals("chancellor")) {
                            this.knightMove(chess, this.playerChosenColor);
                            this.rookMove(chess, this.playerChosenColor);
                        }
                        this.chessChosen = true;
                        this.clickedRow = this.mouseRow;
                        this.clickedColumn = this.mouseColumn;
                        this.ChosenChess = chess;


                    }
                }
                if (this.chessChosen == true && this.moveArea.size() > 0 && (!this.ChosenChess.getType().equals("king"))) {
                    ArrayList<int[]> original_Attack = new ArrayList<int[]>();
                    ArrayList<int[]> original_Move = new ArrayList<int[]>();
                    for (int[] tempList : this.moveArea) {
                        original_Move.add(tempList);
                    }
                    for (int[] tempList : this.attackArea) {
                        original_Attack.add(tempList);
                    }
                    this.check(king1, 0);
                    this.moveArea = new ArrayList<int[]>();
                    for (int[] tempList : original_Move) {
                        this.moveArea.add(tempList);
                    }
                    if (this.playerIsCheck == true) {
                        ArrayList<int[]> mustMoveArea = new ArrayList<int[]>();
                        Chess tempchess = null;
                        for (int[] tempList : this.moveArea) {
                            this.playerIsCheck = false;
                            this.chessList.remove(ChosenChess);
                            tempchess = new Chess(tempList[0] * 48, tempList[1] * 48, ChosenChess.getType(), ChosenChess.getColor(), ChosenChess.getValue(), this.speed,this.maxTime);
                            this.chessList.add(tempchess);
                            if(playerChosenColor==0){
                                this.check(king1, 0);
                            }else{
                                this.check(king2, 1);
                            }
                            if (this.playerIsCheck == false) {
                                mustMoveArea.add(tempList);
                            }
                            this.chessList.remove(tempchess);
                            this.chessList.add(ChosenChess);
                        }
                        for (int[] tempList : mustMoveArea) {
                            this.moveArea.add(tempList);
                        }

                    }
                    this.attackArea = new ArrayList<int[]>();
                    for (int[] tempList : original_Attack) {
                        this.attackArea.add(tempList);
                    }
                }


                if (this.chessChosen == true && this.moveArea.size() > 0 && this.ChosenChess.getType().equals("king")) {
                    this.enemyChessList = new ArrayList<Chess>();
                    for (Chess chess : this.chessList) {
                        if (chess.getColor() == this.enemyColor) {
                            this.enemyChessList.add(chess);
                        }
                    }
                    this.moveArea = new ArrayList<int[]>();
                    this.attackArea = new ArrayList<int[]>();
                    ArrayList<int[]> safePlace = new ArrayList<int[]>();
                    for (Chess chess : this.enemyChessList) {
                        if (chess.getType().equals("pawn")) {
                            ArrayList<int[]> tempMoveArea = new ArrayList<int[]>();
                            for (int[] tempList : this.moveArea) {
                                tempMoveArea.add(tempList);
                            }
                            this.moveArea = new ArrayList<int[]>();
                            typeJudge(chess, this.enemyColor);
                            this.moveArea = new ArrayList<int[]>();
                            for (int[] tempList : tempMoveArea) {
                                this.moveArea.add(tempList);
                            }
                        } else {
                            typeJudge(chess, this.enemyColor);
                        }
                    }
                    for (int i = 0; i <= 13; i++) {
                        for (int j = 0; j <= 13; j++) {
                            int[] place = new int[]{i, j};
                            safePlace.add(place);
                            for (int[] tempList : attackArea) {
                                if (tempList[0] == i && tempList[1] == j) {
                                    safePlace.remove(place);
                                }
                            }
                            if (safePlace.size() > 0) {
                                for (int[] tempList : moveArea) {
                                    if (tempList[0] == i && tempList[1] == j) {
                                        safePlace.remove(place);
                                    }
                                }
                            }

                        }
                    }
                    this.moveArea = new ArrayList<int[]>();
                    this.attackArea = new ArrayList<int[]>();
                    typeJudge(this.ChosenChess, this.playerChosenColor);
                    ArrayList<int[]> shouldMovePlace = new ArrayList<int[]>();
                    for (int[] tempList : safePlace) {
                        for (int[] tempList2 : this.moveArea) {
                            if (tempList[0] == tempList2[0] && tempList[1] == tempList2[1]) {
                                shouldMovePlace.add(tempList2);
                            }
                        }
                    }
                    ArrayList<int[]> shouldMovePlace2 = new ArrayList<int[]>();
                    for (int[] tempList : shouldMovePlace) {
                        shouldMovePlace2.add(tempList);
                    }
                    Chess tempChess = null;
                    for (int[] place : shouldMovePlace2) {
                        this.moveArea = new ArrayList<int[]>();
                        this.attackArea = new ArrayList<int[]>();
                        if (this.playerChosenColor == 0) {
                            tempChess = new Chess(place[0] * 48, place[1] * 48, this.king1.getType(), this.playerChosenColor, 999, this.speed,this.maxTime);
                            this.chessList.remove(this.king1);
                        } else {
                            tempChess = new Chess(place[0] * 48, place[1] * 48, this.king2.getType(), this.playerChosenColor, 999, this.speed,this.maxTime);
                            this.chessList.remove(this.king2);
                        }
                        this.chessList.add(tempChess);
                        for (Chess chess : this.chessList) {
                            if (chess.getColor() == this.enemyColor) {
                                typeJudge(chess, this.enemyColor);
                            }
                        }

                        if (this.attackArea.size() > 0) {
                            for (int[] tempList : this.attackArea) {
                                if (tempList[0] == place[0] && tempList[1] == place[1]) {
                                    shouldMovePlace.remove(place);
                                }
                            }
                        }
                        this.chessList.remove(tempChess);
                        if (this.playerChosenColor == 0) {
                            this.chessList.add(this.king1);
                        } else {
                            this.chessList.add(this.king2);
                        }
                        this.moveArea = new ArrayList<int[]>();
                        this.attackArea = new ArrayList<int[]>();
                    }
                    typeJudge(this.ChosenChess, this.playerChosenColor);
                    this.moveArea = new ArrayList<int[]>();
                    if (shouldMovePlace.size() > 0) {
                        for (int[] tempList : shouldMovePlace) {
                            this.moveArea.add(tempList);
                        }
                    }
                }

                if (this.chessChosen == true && this.attackArea.size() > 0 && this.ChosenChess.getType().equals("king")) {
                    boolean tempBoolean = false;
                    if (this.playerChosenColor == 0) {
                        tempBoolean = this.whiteKingIsDanger;
                    } else {
                        tempBoolean = this.blackKingIsDanger;
                    }

                    this.enemyChessList = new ArrayList<Chess>();
                    for (Chess chess : this.chessList) {
                        if (chess.getColor() == this.enemyColor) {
                            this.enemyChessList.add(chess);
                        }
                    }

                    ArrayList<int[]> originMoveArea = new ArrayList<int[]>();
                    for (int[] tempList : this.moveArea) {
                        originMoveArea.add(tempList);
                    }
                    ArrayList<int[]> shouldAttackPlace = new ArrayList<int[]>();
                    for (int[] tempList : this.attackArea) {
                        shouldAttackPlace.add(tempList);
                    }
                    ArrayList<int[]> shouldAttackPlace2 = new ArrayList<int[]>();
                    for (int[] tempList : shouldAttackPlace) {
                        shouldAttackPlace2.add(tempList);
                    }

                    for (int[] place : shouldAttackPlace2) {
                        for (Chess oppchess : enemyChessList) {
                            if (oppchess.getRow() == place[0] && oppchess.getColumn() == place[1] && oppchess.getColor() == this.enemyColor) {
                                this.moveArea = new ArrayList<int[]>();
                                this.attackArea = new ArrayList<int[]>();
                                Chess tempChess = null;
                                this.chessList.remove(oppchess);
                                tempChess = new Chess(place[0] * 48, place[1] * 48, "king", this.playerChosenColor, 999, this.speed,this.maxTime);
                                this.chessList.add(tempChess);
                                for (Chess chess : this.chessList) {
                                    if (chess.getColor() == this.enemyColor) {
                                        typeJudge(chess, this.enemyColor);
                                    }
                                }
                                if (attackArea.size() > 0) {
                                    for (int[] tempList : attackArea) {
                                        if (tempList[0] == place[0] && tempList[1] == place[1]) {
                                            shouldAttackPlace.remove(place);
                                        }
                                    }
                                }
                                this.chessList.remove(tempChess);
                                this.chessList.add(oppchess);
                            }


                        }
                    }
                    this.moveArea = new ArrayList<int[]>();
                    this.attackArea = new ArrayList<int[]>();
                    for (int[] tempList : originMoveArea) {
                        this.moveArea.add(tempList);
                    }
                    if (shouldAttackPlace.size() > 0) {
                        for (int[] tempList : shouldAttackPlace) {
                            this.attackArea.add(tempList);
                        }
                    }
                    if (this.playerChosenColor == 0) {
                        this.whiteKingIsDanger = tempBoolean;
                    } else {
                        this.blackKingIsDanger = tempBoolean;
                    }
                }


            } else {
                boolean chooseSafe = false;
                this.mouseRow = mouseX / 48;
                this.mouseColumn = mouseY / 48;
                int[] mouseClick = new int[]{this.mouseRow, this.mouseColumn};
                    if (this.attackArea.size()>0) {
                    for (int[] target : this.attackArea) {
                        if (target[0] == mouseClick[0] && target[1] == mouseClick[1]) {
                            ArrayList<Chess> tempChessList = new ArrayList<Chess>();
                            for (Chess chess : this.chessList) {
                                tempChessList.add(chess);
                            }
                            for (Chess chess : tempChessList) {
                                if (target[0] == chess.getRow() && target[1] == chess.getColumn() && chess.getColor() != this.playerChosenColor) {
                                    chess.setX(999);
                                    chess.setY(999);
                                    chess.setSprite(null);
                                    if (chess.getType().equals("king")) {
                                        System.out.println("player win");
                                        this.playerWin = true;
                                        this.gameOver = true;
                                        this.soundJudge(4);
                                    }
                                    chessList.remove(chess);
                                }
                            }
                            if (this.ChosenChess.getType().equals("pawn") && target[1] == 7) {
                                this.ChosenChess = this.pawnToQueen(this.ChosenChess, this.playerChosenColor);
                            }
                            this.PastCood = new int[]{this.ChosenChess.getX(), this.ChosenChess.getY()};
                            this.CurrentCood = new int[]{this.mouseRow * 48, this.mouseColumn * 48};
                            this.ChosenChess.setX(this.mouseRow * 48);
                            this.ChosenChess.setY(this.mouseColumn * 48);

                            this.playerTimeAdd();
                            this.enemyShouldMove = true;
                            chooseSafe = true;
                            this.soundJudge(1);
                        }
                    }
                }
                if (this.moveArea.size() > 0) {
                    for (int[] target : this.moveArea) {
                        if (target[0] == mouseClick[0] && target[1] == mouseClick[1]) {
                            if (this.ChosenChess.getType().equals("pawn") && target[1] == 7) {
                                this.ChosenChess = this.pawnToQueen(this.ChosenChess, this.playerChosenColor);
                            }
                            this.PastCood = new int[]{this.ChosenChess.getX(), this.ChosenChess.getY()};
                            this.CurrentCood = new int[]{this.mouseRow * 48, this.mouseColumn * 48};
                            this.ChosenChess.setX(this.mouseRow * 48);
                            this.ChosenChess.setY(this.mouseColumn * 48);
                            if (this.ChosenChess.getFirstMove() == false) {
                                this.ChosenChess.setFirstMove(true);
                            }
                            this.playerTimeAdd();
                            this.enemyShouldMove = true;
                            chooseSafe = true;
                            this.soundJudge(0);
                        }
                    }
                }
                boolean notCheckSound = false;
                if(this.playerIsCheck == true){
                    if(this.warning==false){
                        if(chooseSafe==false){
                            this.warning = true;
                            this.soundJudge(5);
                            notCheckSound = true;
                        }
                    }
                }

                this.chessChosen = false;
                this.clickedColumn = 0;
                this.clickedRow = 0;
                this.ChosenChess = null;
                this.moveArea = new ArrayList<int[]>();
                this.attackArea = new ArrayList<int[]>();
                this.blackKingIsDanger = false;
                this.whiteKingIsDanger = false;
                this.playerIsCheck = false;
                this.enemyIsCheck = false;
                this.check(king1, 0);
                this.check(king2, 1);
                if((this.playerIsCheck == true||this.enemyIsCheck==true) && notCheckSound == false){
                    this.soundJudge(2);
                }
                if (this.playerIsCheck == true) {
                    boolean haveSafeMethod = false;
                    this.playerChessList = new ArrayList<Chess>();
                    this.enemyChessList = new ArrayList<Chess>();
                    for (Chess chess : this.chessList) {
                        if (chess.getColor() == this.playerChosenColor) {
                            this.playerChessList.add(chess);
                        } else {
                            this.enemyChessList.add(chess);
                        }
                    }
                    for (Chess chess : this.playerChessList) {
                        if (chess.getColor() == this.playerChosenColor) {
                            this.moveArea = new ArrayList<int[]>();
                            this.attackArea = new ArrayList<int[]>();
                            typeJudge(chess, this.playerChosenColor);
                            ArrayList<int[]> tempMoveArea = new ArrayList<int[]>();
                            ArrayList<int[]> tempAttackArea = new ArrayList<int[]>();
                            for (int[] tempList : moveArea) {
                                tempMoveArea.add(tempList);
                            }
                            for (int[] tempList : attackArea) {
                                tempAttackArea.add(tempList);
                            }
                            if (this.moveArea.size() > 0) {
                                for (int[] place : tempMoveArea) {
                                    this.playerIsCheck = false;
                                    this.chessList.remove(chess);
                                    Chess tempChess = new Chess(place[0] * 48, place[1] * 48, chess.getType(), chess.getColor(), chess.getValue(), this.speed,this.maxTime);
                                    this.chessList.add(tempChess);
                                    if (chess.getType().equals("king")) {
                                        this.check(tempChess, tempChess.getColor());
                                    } else {
                                        if (this.playerChosenColor == 0) {
                                            this.check(this.king1, 0);
                                        } else {
                                            this.check(this.king2, 1);
                                        }
                                    }
                                    if (this.playerIsCheck == false) {
                                        haveSafeMethod = true;
                                    }
                                    this.chessList.remove(tempChess);
                                    this.chessList.add(chess);
                                }
                            }
                            if (tempAttackArea.size() > 0) {
                                for (int[] place : tempAttackArea) {
                                    for (Chess oppchess : this.enemyChessList) {
                                        if (oppchess.getRow() == place[0] && oppchess.getColumn() == place[1] && oppchess.getColor() != this.playerChosenColor) {
                                            this.playerIsCheck = false;
                                            Chess tempChess = new Chess(place[0] * 48, place[1] * 48, chess.getType(), chess.getColor(), chess.getValue(), this.speed,this.maxTime);
                                            this.chessList.remove(oppchess);
                                            this.chessList.remove(chess);
                                            this.chessList.add(tempChess);
                                            if (chess.getType().equals("king")) {
                                                this.check(tempChess, tempChess.getColor());
                                            } else {
                                                if (this.playerChosenColor == 0) {
                                                    this.check(this.king1, 0);
                                                } else {
                                                    this.check(this.king2, 1);
                                                }
                                            }
                                            if (this.playerIsCheck == false) {
                                                haveSafeMethod = true;
                                            }
                                            this.chessList.remove(tempChess);
                                            this.chessList.add(chess);
                                            this.chessList.add(oppchess);
                                        }
                                    }
                                }
                            }


                        }
                    }
                    if (haveSafeMethod == false) {
                        this.gameOver = true;
                        this.enemyWin = true;
                        this.soundJudge(4);
                        System.out.println("Enemy win!");
                    }
                    this.playerIsCheck = true;
                }

                this.moveArea = new ArrayList<int[]>();
                this.attackArea = new ArrayList<int[]>();
            }

        }


    }
    /**
     * Adjust the move and attack area based on the type of chess
     * @param chess Chess object to be judged
     * @param color The color of Chess
     */

    public void typeJudge(Chess chess, int color){
        this.whiteKingIsDanger = false;
        this.blackKingIsDanger = false;
        if(chess.getType().equals("pawn")){
            this.pawnMove(chess, color);
        }
        if(chess.getType().equals("rook")){
            this.rookMove(chess,color);
        }
        if(chess.getType().equals("knight")){
            this.knightMove(chess,color);
        }
        if(chess.getType().equals("bishop")){
            this.bishopMove(chess,color);
        }
        if(chess.getType().equals("archbishop")){
            this.knightMove(chess,color);
            this.bishopMove(chess,color);
        }
        if(chess.getType().equals("camel")){
            this.camelMove(chess,color);
        }
        if(chess.getType().equals("guard")){
            this.knightMove(chess,color);
            this.kingMove(chess,color);
        }
        if(chess.getType().equals("amazon")){
            this.knightMove(chess,color);
            this.rookMove(chess,color);
            this.bishopMove(chess,color);
        }
        if(chess.getType().equals("king")){
            this.kingMove(chess,color);
        }
        if(chess.getType().equals("chancellor")){
            this.knightMove(chess,color);
            this.rookMove(chess,color);
        }
        if(chess.getType().equals("queen")){
            this.rookMove(chess,color);
            this.bishopMove(chess,color);
        }
        if(chess.getType().equals("cannon")){
            this.cannonMove(chess,color);
        }
    }
    /**
     * The function decide enemy choose which Chess and move to where
     */
    public void enemyMove(){
        this.isEnemyTurn = true;
        this.enemyChessList= new ArrayList<Chess>();
        ArrayList<Chess> attackByLow = new ArrayList<Chess>();
        double playerValue = 0;
        Map<Chess,ArrayList> threatMap = new HashMap<>();
        Map<Chess, Chess> hostMap = new HashMap<>();
        ArrayList<Chess> chessUnderAttack = new ArrayList<Chess>();
        Chess playerKing = null;
        if(this.playerChosenColor == 0){
            playerKing = this.king1;
        }else{
            playerKing = this.king2;
        }
        Chess attackChess = null;

        for(Chess chess: chessList){
            if (chess.getColor() == this.enemyColor){
                this.enemyChessList.add(chess);
            }
        }

        if(this.enemyChessList.size() > 0){
            Random ran = new Random();
            while(true){
                this.moveArea = new ArrayList<int[]>();
                this.attackArea = new ArrayList<int[]>();
                int index = ran.nextInt(this.enemyChessList.size());
                this.enemyChess = this.enemyChessList.get(index);
                typeJudge(this.enemyChess,this.enemyColor);
                if(this.moveArea.size()>0 || this.attackArea.size()>0){
                    break;
                }

            }
            this.moveArea = new ArrayList<int[]>();
            this.attackArea = new ArrayList<int[]>();

            this.playerChessList = new ArrayList<Chess>();
            for(Chess chess: chessList){
                if (chess.getColor() == this.playerChosenColor){
                    playerChessList.add(chess);
                }
            }

            ArrayList<Chess> waitMoveList= new ArrayList<Chess>();
            ArrayList<Chess> waitMoveToThreatenList= new ArrayList<Chess>();
            ArrayList<Chess> waitAttackList= new ArrayList<Chess>();
            ArrayList<Chess> waitAttackHighList= new ArrayList<Chess>();
            for(Chess chess:enemyChessList){
                this.typeJudge(chess,this.enemyColor);
                if(this.attackArea.size()>0){
                    for(Chess playerChess:this.playerChessList){
                        for(int[] tempList:this.attackArea){
                            if(tempList[0]==playerChess.getRow() && tempList[1] == playerChess.getColumn()){
                                if(playerChess.getValue() > chess.getValue()){
                                    if(!waitAttackHighList.contains(chess)){
                                        waitAttackHighList.add(chess);
                                    }
                                }
                            }
                        }
                    }
                    waitAttackList.add(chess);
                }
                if(this.moveArea.size()>0){
                    waitMoveList.add(chess);
                }
                this.moveArea = new ArrayList<int[]>();
                this.attackArea = new ArrayList<int[]>();
            }

            // Judge whether there is high-value chess under low-value chess's attack

            for(Chess chess:this.playerChessList){
                typeJudge(chess,this.playerChosenColor);
            }
            if(attackArea.size()>0){
                for(Chess chess:this.enemyChessList){
                    for(int[] tempList:attackArea){
                        if(chess.getRow()==tempList[0] && chess.getColumn()==tempList[1]){
                            chessUnderAttack.add(chess);
                        }
                    }
                }
                this.moveArea = new ArrayList<int[]>();
                this.attackArea = new ArrayList<int[]>();
                for(Chess chess:this.playerChessList){
                    typeJudge(chess,this.playerChosenColor);
                    if(attackArea.size()>0){
                        for(int[] tempList:attackArea){
                            for(Chess eChess:chessUnderAttack){
                                if(eChess.getRow() == tempList[0] && eChess.getColumn() == tempList[1]){
                                    hostMap.put(eChess,chess);
                                }
                            }
                        }
                        threatMap.put(chess,this.attackArea);
                    }
                    this.moveArea = new ArrayList<int[]>();
                    this.attackArea = new ArrayList<int[]>();
                }
                for(Chess key:hostMap.keySet()){
                    Chess oppChess = hostMap.get(key);
                    if(oppChess.getValue()<key.getValue()){
                        attackByLow.add(key);
                    }
                }

            }
            if(attackByLow.size()>0){
                double threatValue = 0;
                for(Chess chess:attackByLow){
                    if(chess.getValue()>threatValue){
                        this.moveArea = new ArrayList<int[]>();
                        this.attackArea = new ArrayList<int[]>();
                        typeJudge(chess,this.enemyColor);
                        if(this.moveArea.size()>0 || this.attackArea.size()>0){
                            this.enemyChess=chess;
                        }
                        threatValue = chess.getValue();
                    }
                }
            }
            this.moveArea = new ArrayList<int[]>();
            this.attackArea = new ArrayList<int[]>();
            boolean captureHighValue = false;
            if(waitAttackList.size()>0 && attackByLow.size()==0){
                double oppValue = 0;
                for(Chess chess:waitAttackList){
                    this.typeJudge(chess,this.enemyColor);
                    for(int[] tempList:this.attackArea){
                        for(Chess oppChess:this.chessList){
                            if(tempList[0] == oppChess.getRow() && tempList[1] == oppChess.getColumn() && oppChess.getColor()!=this.enemyColor){
                                if(oppChess.getValue()>oppValue){
                                    oppValue = oppChess.getValue();
                                    if(oppChess.getValue()>chess.getValue()){
                                        captureHighValue = true;
                                    }
                                    this.enemyChess = chess;
                                }
                            }
                        }
                    }
                    this.moveArea = new ArrayList<int[]>();
                    this.attackArea = new ArrayList<int[]>();
                }

            }
            boolean moveToCheck = false;
            ArrayList<Chess> checkList= new ArrayList<Chess>();
            if(waitMoveList.size()>0 && attackByLow.size() == 0 && captureHighValue == false){
                for(Chess chess:waitMoveList){
                    this.moveArea = new ArrayList<int[]>();
                    this.attackArea = new ArrayList<int[]>();
                    typeJudge(chess,this.enemyColor);
                    ArrayList<int[]> tempMoveArea = new ArrayList<int[]>();
                    ArrayList<int[]> tempAttackArea = new ArrayList<int[]>();
                    for(int[] tempList:moveArea){
                        tempMoveArea.add(tempList);
                    }

                    for(int[] place:tempMoveArea){
                        this.moveArea = new ArrayList<int[]>();
                        this.attackArea = new ArrayList<int[]>();
                        this.playerIsCheck =false;
                        Chess tempChess = new Chess(place[0]*48,place[1]*48, chess.getType(), this.enemyColor,chess.getValue(),this.speed,this.maxTime);
                        this.chessList.remove(chess);
                        this.chessList.add(tempChess);
                        typeJudge(tempChess,this.enemyColor);
                        if(playerChosenColor==0){
                            this.check(this.king1,0);
                        }else{
                            this.check(this.king2,1);
                        }
                        if(playerIsCheck==true){
                            checkList.add(chess);
                            moveToCheck = true;
                        }
                        this.chessList.remove(tempChess);
                        this.chessList.add(chess);

                    }
                    this.moveArea = new ArrayList<int[]>();
                    this.attackArea = new ArrayList<int[]>();
                }
                this.playerIsCheck =false;
            }
            if(moveToCheck==true){
                int index = ran.nextInt(checkList.size());
                this.enemyChess = checkList.get(index);
            }

            if(waitMoveList.size()>0 && attackByLow.size() == 0 && waitAttackList.size()==0 && moveToCheck == false){
                int index = ran.nextInt(waitMoveList.size());
                this.enemyChess = waitMoveList.get(index);
                this.moveArea = new ArrayList<int[]>();
                this.attackArea = new ArrayList<int[]>();
            }
            ArrayList<Chess> attackList= new ArrayList<Chess>();
            this.typeJudge(this.enemyChess, this.enemyColor);
            if(this.attackArea.size()>0 && this.moveArea.size()>0){
                Chess tempChess = null;
                double hValue = 0;
                ArrayList<int[]> tempAttackArea = new ArrayList<int[]>();
                ArrayList<int[]> tempAttackArea2 = new ArrayList<int[]>();
                for(int[] tempList: this.attackArea){
                    tempAttackArea.add(tempList);
                    tempAttackArea2.add(tempList);
                }
                for(int[] tempList: tempAttackArea2){
                    this.moveArea = new ArrayList<int[]>();
                    this.attackArea = new ArrayList<int[]>();
                    // If attack the chess, judge it whether it will be attacked by low-value chess
                    for(Chess chess:this.playerChessList){
                        if(tempList[0] == chess.getRow() && tempList[1] == chess.getColumn() && chess.getValue()<=this.enemyChess.getValue()){
                            this.chessList.remove(chess);
                            tempChess = new Chess(tempList[0]*48,tempList[1]*48,this.enemyChess.getType(), this.enemyColor,this.enemyChess.getValue(),this.speed,this.maxTime);
                            this.chessList.add(tempChess);
                            for(Chess playerChess:this.chessList){
                                if(playerChess.getColor()==this.playerChosenColor){
                                    typeJudge(playerChess,this.playerChosenColor);
                                    if(this.attackArea.size()>0){
                                        for(int[] tempList2:this.attackArea){
                                            if(tempList2[0] == tempChess.getRow() && tempList2[1] == tempChess.getColumn()){
                                                if(playerChess.getValue()<=this.enemyChess.getValue()){
                                                    tempAttackArea.remove(tempList);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            this.chessList.remove(tempChess);
                            this.chessList.add(chess);

                        }
                    }
                }
                this.moveArea = new ArrayList<int[]>();
                this.attackArea = new ArrayList<int[]>();
                this.typeJudge(this.enemyChess, this.enemyColor);
                if(this.attackArea.size()!=tempAttackArea.size()){
                    this.attackArea = new ArrayList<int[]>();
                    if(tempAttackArea.size()>0){
                        for(int[] tempList:tempAttackArea){
                            this.attackArea.add(tempList);
                        }
                    }
                }
            }


            if(this.attackArea.size()>0){
                for(int[] tempList : this.attackArea){
                    for(Chess chess : chessList){
                        if(tempList[0]==chess.getRow() && tempList[1] == chess.getColumn() && this.enemyColor!= chess.getColor()){
                            attackList.add(chess);
                        }
                    }
                }
                double value = 0;
                for(Chess chess : attackList){
                    if(chess.getValue()>value){
                        value = chess.getValue();
                    }
                }

                for(Chess chess : attackList){
                    if(chess.getValue() == value){
                        if(attackChess!=null){
                            double tempDist = Math.pow(attackChess.getRow()-playerKing.getRow(),2) + Math.pow(attackChess.getColumn()-playerKing.getColumn(),2);
                            double tempDist2 = Math.pow(chess.getRow()-playerKing.getRow(),2) + Math.pow(chess.getColumn()-playerKing.getColumn(),2);
                            if(tempDist>tempDist2){
                                attackChess = chess;
                            }
                        }else{
                            attackChess = chess;
                        }
                    }
                }
                int x = attackChess.getRow();
                int y = attackChess.getColumn();
                attackChess.setX(999);
                attackChess.setY(999);
                attackChess.setSprite(null);
                this.chessList.remove(attackChess);
                if(attackChess.getType().equals("king")){
                    System.out.println("enemy win");
                    this.enemyWin = true;
                    this.gameOver=true;
                }
                if(this.enemyChess.getType().equals("pawn") && y == 7){
                    this.enemyChess = this.pawnToQueen(this.enemyChess,this.enemyColor);
                }
                if(this.gameOver==false){
                    this.PastCood = new int[]{this.enemyChess.getX(),this.enemyChess.getY()};
                    this.enemyChess.setX(x * 48);
                    this.enemyChess.setY(y * 48);
                    this.CurrentCood = new int[]{x * 48,y * 48};
                }
                if(this.enemyChess.getType().equals("pawn") && this.enemyChess.getFirstMove() == false){
                    this.enemyChess.setFirstMove(true);
                }
                this.soundJudge(1);
            }else if(this.moveArea.size()>0){
                double dist = 99999;
                double tempDist = 0;
                int[] target = new int[2];
                if(attackByLow.size()>0){
                    if(attackByLow.contains(this.enemyChess)){
                        this.moveArea = new ArrayList<int[]>();
                        this.attackArea = new ArrayList<int[]>();
                        for(Chess chess:this.playerChessList){
                            typeJudge(chess,this.playerChosenColor);
                        }
                        ArrayList<int[]> safePlace = new ArrayList<int[]>();
                        for(int i = 0;i<=13;i++){
                            for(int j = 0 ;j<=13;j++){
                                int[] place = new int[]{i,j};
                                safePlace.add(place);
                                for(int[] tempList:attackArea){
                                    if(tempList[0] == i && tempList[1] == j){
                                        safePlace.remove(place);
                                    }
                                }
                                if(safePlace.size()>0){
                                    for(int[] tempList:moveArea){
                                        if(tempList[0] == i && tempList[1] == j){
                                            safePlace.remove(place);
                                        }
                                    }
                                }
                            }
                        }
                        this.moveArea = new ArrayList<int[]>();
                        this.attackArea = new ArrayList<int[]>();
                        typeJudge(this.enemyChess,this.enemyColor);
                        ArrayList<int[]> shouldMovePlace = new ArrayList<int[]>();
                        for(int[] tempList:safePlace){
                            for(int[] tempList2:this.moveArea){
                                if(tempList[0]==tempList2[0] && tempList[1]==tempList2[1]){
                                    shouldMovePlace.add(tempList2);
                                }
                            }
                        }
                        if(this.enemyChess.getType().equals("king") && shouldMovePlace.size()>0){
                            Iterator<int[]> itr = shouldMovePlace.iterator();
                            ArrayList<int[]> tempMovePlace = new ArrayList<int[]>(shouldMovePlace);
                            Chess tempChess = null;
                            for(int[] place:tempMovePlace){
                                this.moveArea = new ArrayList<int[]>();
                                this.attackArea = new ArrayList<int[]>();
                                if(enemyColor == 1){
                                    tempChess = new Chess(place[0]*48,place[1]*48,this.king2.getType(), enemyColor,999,this.speed,this.maxTime);
                                    this.chessList.remove(this.king2);
                                }else{
                                    tempChess = new Chess(place[0]*48,place[1]*48,this.king1.getType(), enemyColor,999,this.speed,this.maxTime);
                                    this.chessList.remove(this.king1);
                                }
                                this.chessList.add(tempChess);
                                for(Chess chess:this.playerChessList){
                                    typeJudge(chess,this.playerChosenColor);
                                }
                                if(this.attackArea.size()>0){
                                    for(int[] tempList:this.attackArea){
                                        if(tempList[0] == place[0] && tempList[1] == place[1] ){
                                            shouldMovePlace.remove(place);
                                        }
                                    }
                                }
                                this.chessList.remove(tempChess);
                                if(enemyColor == 1){
                                    this.chessList.add(this.king2);
                                }else{
                                    this.chessList.add(this.king1);
                                }
                            }
                        }
                        this.moveArea = new ArrayList<int[]>();
                        this.attackArea = new ArrayList<int[]>();
                        typeJudge(this.enemyChess,this.enemyColor);
                        if(shouldMovePlace.size()>0){
                            for(int[] tempList : shouldMovePlace){
                                tempDist = Math.pow(tempList[0]-playerKing.getRow(),2) + Math.pow(tempList[1]-playerKing.getColumn(),2) ;
                                if(tempDist<dist){
                                    dist = tempDist;
                                    target = tempList;
                                }
                            }
                        }else{
                            if(this.enemyChess.getType().equals("king")){
                                this.gameOver = true;
                                this.playerWin = true;
                                System.out.println("player win!");
                                this.soundJudge(4);
                            }
                            if(moveToCheck==false) {
                                for (int[] tempList : this.moveArea) {
                                    tempDist = Math.pow(tempList[0] - playerKing.getRow(), 2) + Math.pow(tempList[1] - playerKing.getColumn(), 2);
                                    if (tempDist < dist) {
                                        dist = tempDist;
                                        target = tempList;
                                    }
                                }
                            }else{
                                for (int[] tempList : this.moveArea) {
                                    tempDist = Math.pow(tempList[0] - playerKing.getRow(), 2) + Math.pow(tempList[1] - playerKing.getColumn(), 2);
                                    if (tempDist > dist) {
                                        dist = tempDist;
                                        target = tempList;
                                    }
                                }
                            }
                        }
                    }else{ // no chess threatened by low-value chess
                        this.moveArea = new ArrayList<int[]>();
                        this.attackArea = new ArrayList<int[]>();
                        ArrayList<int[]> safePlace = new ArrayList<int[]>();
                        for(Chess chess:this.playerChessList){
                            if(chess.getValue()<=this.enemyChess.getValue()){
                                typeJudge(chess,this.playerChosenColor);
                            }
                        }
                        for(int i = 0;i<=13;i++){
                            for(int j = 0 ;j<=13;j++){
                                int[] place = new int[]{i,j};
                                safePlace.add(place);
                                for(int[] tempList:attackArea){
                                    if(tempList[0] == i && tempList[1] == j){
                                        safePlace.remove(place);
                                    }
                                }
                                if(safePlace.size()>0){
                                    for(int[] tempList:moveArea){
                                        if(tempList[0] == i && tempList[1] == j){
                                            safePlace.remove(place);
                                        }
                                    }
                                }
                            }
                        }
                        this.moveArea = new ArrayList<int[]>();
                        this.attackArea = new ArrayList<int[]>();
                        typeJudge(this.enemyChess,this.enemyColor);
                        ArrayList<int[]> shouldMovePlace = new ArrayList<int[]>();
                        for(int[] tempList:safePlace){
                            for(int[] tempList2:this.moveArea){
                                if(tempList[0]==tempList2[0] && tempList[1]==tempList2[1]){
                                    shouldMovePlace.add(tempList2);
                                }
                            }
                        }

                        if(shouldMovePlace.size()>0){
                            if(moveToCheck==false) {
                                for (int[] tempList : shouldMovePlace) {
                                    tempDist = Math.pow(tempList[0] - playerKing.getRow(), 2) + Math.pow(tempList[1] - playerKing.getColumn(), 2);
                                    if (tempDist < dist) {
                                        dist = tempDist;
                                        target = tempList;
                                    }
                                }
                            }else{
                                for (int[] tempList : shouldMovePlace) {
                                    tempDist = Math.pow(tempList[0] - playerKing.getRow(), 2) + Math.pow(tempList[1] - playerKing.getColumn(), 2);
                                    if (tempDist > dist) {
                                        dist = tempDist;
                                        target = tempList;
                                    }
                                }
                            }
                        }else{
                            if(moveToCheck==false) {
                                for (int[] tempList : this.moveArea) {
                                    tempDist = Math.pow(tempList[0] - playerKing.getRow(), 2) + Math.pow(tempList[1] - playerKing.getColumn(), 2);
                                    if (tempDist < dist) {
                                        dist = tempDist;
                                        target = tempList;
                                    }
                                }
                            }else{
                                for (int[] tempList : this.moveArea) {
                                    tempDist = Math.pow(tempList[0] - playerKing.getRow(), 2) + Math.pow(tempList[1] - playerKing.getColumn(), 2);
                                    if (tempDist > dist) {
                                        dist = tempDist;
                                        target = tempList;
                                    }
                                }
                            }
                        }
                    }
                }else{
                    this.moveArea = new ArrayList<int[]>();
                    this.attackArea = new ArrayList<int[]>();
                    ArrayList<int[]> safePlace = new ArrayList<int[]>();
                    for(Chess chess:this.playerChessList){
                        if(chess.getValue()<=this.enemyChess.getValue()){
                            typeJudge(chess,this.playerChosenColor);
                        }
                    }
                    //This code is use for detect enemy pawn!
                    ArrayList<int[]> pawnAttackArea = new ArrayList<int[]>();
                    for(Chess chess:this.playerChessList){
                        if(chess.getType().equals("pawn")){
                            if(playerChosenColor==0){
                                if(chess.getRow()!=0){
                                    pawnAttackArea.add(new int[]{chess.getRow() - 1, chess.getColumn() - 1});
                                }
                                if(chess.getRow()!=13){
                                    pawnAttackArea.add(new int[]{chess.getRow() + 1, chess.getColumn() - 1});
                                }
                            }else{
                                if(chess.getRow()!=0){
                                    pawnAttackArea.add(new int[]{chess.getRow() - 1, chess.getColumn() + 1});
                                }
                                if(chess.getRow()!=13){
                                    pawnAttackArea.add(new int[]{chess.getRow() + 1, chess.getColumn() + 1});
                                }
                            }
                        }

                    }

                    for(int i = 0;i<=13;i++){
                        for(int j = 0 ;j<=13;j++){
                            int[] place = new int[]{i,j};
                            safePlace.add(place);
                            for(int[] tempList:attackArea){
                                if(tempList[0] == i && tempList[1] == j){
                                    safePlace.remove(place);
                                }
                            }
                            if(safePlace.size()>0){
                                for(int[] tempList:moveArea){
                                    if(tempList[0] == i && tempList[1] == j){
                                        safePlace.remove(place);
                                    }
                                }
                            }
                            if(pawnAttackArea.size()>0){
                                for(int[] tempList:pawnAttackArea){
                                    if(tempList[0] == i && tempList[1] == j){
                                        safePlace.remove(place);
                                    }
                                }
                            }
                        }
                    }
                    this.moveArea = new ArrayList<int[]>();
                    this.attackArea = new ArrayList<int[]>();
                    typeJudge(this.enemyChess,this.enemyColor);
                    ArrayList<int[]> shouldMovePlace = new ArrayList<int[]>();
                    for(int[] tempList:safePlace){
                        for(int[] tempList2:this.moveArea){
                            if(tempList[0]==tempList2[0] && tempList[1]==tempList2[1]){
                                shouldMovePlace.add(tempList2);
                            }
                        }
                    }
                    if(shouldMovePlace.size()>0){
                        for(int[] tempList : shouldMovePlace){
                            tempDist = Math.pow(tempList[0]-playerKing.getRow(),2) + Math.pow(tempList[1]-playerKing.getColumn(),2) ;
                            if(tempDist<dist){
                                dist = tempDist;
                                target = tempList;
                            }
                        }
                    }else{
                        for(int[] tempList : this.moveArea){
                            tempDist = Math.pow(tempList[0]-playerKing.getRow(),2) + Math.pow(tempList[1]-playerKing.getColumn(),2) ;
                            if(tempDist<dist){
                                dist = tempDist;
                                target = tempList;
                            }
                        }
                    }
                }
                if(this.enemyChess.getType().equals("pawn") && target[1] == 7){
                    this.enemyChess = this.pawnToQueen(this.enemyChess,this.enemyColor);
                }
                if(this.gameOver==false){
                    this.PastCood = new int[]{this.enemyChess.getX(),this.enemyChess.getY()};
                    this.enemyChess.setX(target[0] * 48);
                    this.enemyChess.setY(target[1]  * 48);
                    this.CurrentCood = new int[]{target[0] * 48,target[1]  * 48};
                    this.soundJudge(0);
                }
                if(this.enemyChess.getType().equals("pawn") && this.enemyChess.getFirstMove() == false){
                    this.enemyChess.setFirstMove(true);
                }
            }else{
                System.out.println("No move case");
            }
            this.moveArea = new ArrayList<int[]>();
            this.attackArea = new ArrayList<int[]>();
        }

        this.enemyTimeAdd();
        this.isEnemyTurn = false;
    }
    /**
     * The function called when the mouse drag
     */
    public void mouseDragged(MouseEvent e) {
    }
    /**
     * The function decide whether enemy should move
     */
    public void enemyMoveJudge(){
        this.playerChessList = new ArrayList<Chess>();
        for(Chess chess: chessList){
            if (chess.getColor() == this.playerChosenColor){
                playerChessList.add(chess);
            }
        }
        int inPlace = 0;
        for(Chess chess:this.playerChessList){
            if(chess.getX()==chess.getRow()*48 && chess.getY() == chess.getColumn() * 48){
                inPlace +=1;
            }
        }
        if(inPlace==this.playerChessList.size()){
            this.enemyMove();
            this.enemyShouldMove = false;
            this.playerIsCheck = false;
            this.enemyIsCheck = false;
            this.check(this.king1,0);
            this.check(this.king2,1);
            if(this.enemyIsCheck==true){
                this.gameOver = true;
                this.playerWin = true;
                this.soundJudge(4);
            }
            if(this.playerIsCheck == true){
                this.soundJudge(2);
            }
        }
    }


    /**
     * Draw function, to draw the scene of game
     */
    public void draw() {
        background(128,128,128);
        noStroke();
        setChessBoard();
        fill(153,153,0,150);
        rect(this.PastCood[0],this.PastCood[1],48,48);
        rect(this.CurrentCood[0],this.CurrentCood[1],48,48);
        stroke(255,255,255);
        if(this.chessChosen==true){
            fill(48,128,20);
            rect(this.clickedRow*48,this.clickedColumn*48,48,48);
            fill(51,161,201,180);
            for(int[] target:moveArea){
                rect(target[0]*48,target[1]*48,48,48);
            }
            fill(255,127,80);
            for(int[] target:attackArea){
                rect(target[0]*48,target[1]*48,48,48);
                if(target[0]==this.king1.getRow() && target[1] == this.king1.getColumn()){
                    fill(255,0,0);
                    rect(this.king1.getX(),this.king1.getY(),48,48);
                }
                if(target[0]==this.king2.getRow() && target[1] == this.king2.getColumn()){
                    fill(255,0,0);
                    rect(this.king2.getX(),this.king2.getY(),48,48);
                }
            }
        }
        if(this.enemyIsCheck==true || this.playerWin == true ){

            fill(255,0,0);
            if(this.playerChosenColor == 0){
                rect(this.king2.getX(),this.king2.getY(),48,48);
            }else{
                rect(this.king1.getX(),this.king1.getY(),48,48);
            }
        }
        if(this.playerIsCheck==true || this.enemyWin == true ){
            fill(255,0,0);
            if(this.playerChosenColor == 0){
                rect(this.king1.getX(),this.king1.getY(),48,48);
            }else{
                rect(this.king2.getX(),this.king2.getY(),48,48);
            }
        }
        if(this.warning==true && this.playerIsCheck == true && this.warnTimes<180){
            if(player_sketch<30){
                fill(255,192,203);
            }else{
                fill(255,0,0);
            }
            if(this.playerChosenColor == 0){
                rect(this.king1.getX(),this.king1.getY(),48,48);
            }else{
                rect(this.king2.getX(),this.king2.getY(),48,48);
            }
            this.warnTimes +=1;
            fill(255);
            this.f = createFont("Times New Roman" , 15);
            textFont(f);
            textAlign(CENTER,CENTER);
            text("You must defend ",730,400);
            text("your king! ",730,440);
        }else if(this.warnTimes>=60){
            this.warnTimes = 0;
            this.warning = false;
        }
        for(Chess chess:this.chessList){
            chess.tick();
            chess.draw(this);
        }
        if((this.playerIsCheck == true|| this.enemyIsCheck == true)&&this.gameOver==false){
            this.f = createFont("Times New Roman" , 36);
            fill(255);
            textFont(f);
            textAlign(CENTER,CENTER);
            text("Check!",730,324);
        }
        if(this.gameOver==false){
            if(this.isEnemyTurn==false){
                this.player_sketch ++;
            }else{
                this.enemy_sketch ++;
            }
        }
        if(this.player_sketch>=60){
            this.player_sketch = 0;
            if(this.player_seconds<=0){
                if(this.player_min>0){
                    this.player_min = this.player_min - 1;
                    this.player_seconds = 59;
                }else{
                    this.playerTimeOut = true;
                }
            }else{
                this.player_seconds = this.player_seconds - 1;
            }
        }

        if(this.playerTimeOut == false){
            this.f = createFont("Times New Roman" , 24);
            fill(255);
            textFont(f);
            textAlign(CENTER,CENTER);
            String time = null;
            if(player_seconds<10){
                time = this.player_min + " : 0" +this.player_seconds;
            }else{
                time = this.player_min + " : " +this.player_seconds;
            }
            if(playerChosenColor==0){
                text(time,735,600);
            }else{
                text(time,735,72);
            }

        }else{
            this.f = createFont("Times New Roman" , 24);
            fill(255);
            textFont(f);
            textAlign(CENTER,CENTER);
            String time = "0 : 00";
            if(playerChosenColor==0){
                text(time,735,600);
            }else{
                text(time,735,72);
            }
            this.f = createFont("Times New Roman" , 15);
            fill(255);
            textFont(f);
            textAlign(CENTER,CENTER);
            String win1 = "You lost on";
            String win2 = "Time!";
            text(win1,735,250);
            text(win2,735,280);
            this.gameOver = true;
            if(timeWinSound==false){
                this.soundJudge(4);
                this.timeWinSound=true;
            }
        }

        if(this.enemy_sketch>=60){
            this.enemy_sketch = 0;
            if(this.enemy_seconds<=0){
                if(this.enemy_min>0){
                    this.enemy_min = this.enemy_min - 1;
                    this.enemy_seconds = 59;
                }else{
                    this.enemyTimeOut = true;
                }
            }else{
                this.enemy_seconds = this.enemy_seconds - 1;
            }
        }
        if(this.playerWin==true){
            this.f = createFont("Times New Roman" , 15);
            fill(255);
            textFont(f);
            textAlign(CENTER,CENTER);
            String win1 = "You win by";
            String win2 = "checkmate!";
            text(win1,735,250);
            text(win2,735,280);
        }
        if(this.enemyWin==true){
            this.f = createFont("Times New Roman" , 15);
            fill(255);
            textFont(f);
            textAlign(CENTER,CENTER);
            String win1 = "You lost by";
            String win2 = "checkmate!";
            text(win1,735,250);
            text(win2,735,280);

        }

        if(this.enemyTimeOut == false){
            this.f = createFont("Times New Roman" , 24);
            fill(255);
            textFont(f);
            textAlign(CENTER,CENTER);
            String time = null;
            if(enemy_seconds<10){
                time = this.enemy_min + " : 0" +this.enemy_seconds;
            }else{
                time = this.enemy_min + " : " +this.enemy_seconds;
            }
            if(enemyColor==0){
                text(time,735,600);
            }else{
                text(time,735,72);
            }

        }else{
            this.f = createFont("Times New Roman" , 24);
            fill(255);
            textFont(f);
            textAlign(CENTER,CENTER);
            String time = "0 : 00";
            if(enemyColor==0){
                text(time,735,600);
            }else{
                text(time,735,72);
            }
            this.f = createFont("Times New Roman" , 15);
            fill(255);
            textFont(f);
            textAlign(CENTER,CENTER);
            String win1 = "You win on";
            String win2 = "Time!";
            text(win1,735,250);
            text(win2,735,280);
            this.gameOver = true;
            if(timeWinSound==false){
                this.soundJudge(4);
                this.timeWinSound=true;
            }

        }
        if(this.enemyShouldMove==true){
            this.enemyMoveJudge();
        }
        if(this.gameOver==true){
            this.f = createFont("Times New Roman" , 15);
            fill(255);
            textFont(f);
            textAlign(CENTER,CENTER);
            String str1 = "Press 'Y' to restart";
            String str2 = "the game";
            text(str1,735,350);
            text(str2,735,380);
        }

    }
    /**
     * The Main function
     */
    public static void main(String[] args) {
        PApplet.main("XXLChess.App");
    }
}
