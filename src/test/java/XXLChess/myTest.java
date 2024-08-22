package XXLChess;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import XXLChess.App;

//import static XXLChess.App.rookMove;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class myTest {
    /**
     * Test the creat and return functions of two Chess
     */
    @Test
    public void chessCreateTest(){
        Chess chess = new Chess(1,1,"pawn",1,2,6,1);
        Chess chess2 = new Chess(624,624,"rook",0,9.5,6,1);
        assertEquals(0,chess.getRow());
        assertEquals(0,chess.getColumn());
        assertEquals("pawn",chess.getType());
        assertEquals(1,chess.getColor());
        assertEquals(2,chess.getValue());
        System.out.println("chessCreate test1 pass");

        assertEquals(13,chess2.getRow());
        assertEquals(13,chess2.getColumn());
        assertEquals("rook",chess2.getType());
        assertEquals(0,chess2.getColor());
        assertEquals(9.5,chess2.getValue());
        System.out.println("chessCreate test2 pass");
    }
    /**
     * Test the initialize function of app, mainly test whether it can read file
     * and put chess at the same position
     */
    @Test
    public void initTest(){
        App app = new App();
        app.chessList = new ArrayList<Chess>();
        String line = "RNBHCGAKGCEBNP";
        String line2 = "rnbhcgakgcebnp";
        String[] str = line.split("");
        String[] str2 = line2.split("");
        List<String> boardLine;
        boardLine = Arrays.asList(str);
        int row = 0;
        for(String s: boardLine){
            app.putChess(s,row,0);
            row+=1;
        }
        row = 0;
        boardLine = Arrays.asList(str2);
        for(String s: boardLine){
            app.putChess(s,row,13);
            row+=1;
        }
        assertEquals(28,app.chessList.size());
    }
    /**
     * Test the typeJudge function of App class
     */
    @Test
    public void typeJudgeTest(){
        App app = new App();
        app.chessList = new ArrayList<Chess>();
        String line = "rnbhcgakgcebnp";
        String[] str = line.split("");
        List<String> boardLine;
        boardLine = Arrays.asList(str);
        int row = 0;
        for(String s: boardLine){
            app.putChess(s,row,0);
            row+=1;
        }
        assertEquals(14,app.chessList.size());

        app.moveArea = new ArrayList<int[]>();
        app.attackArea = new ArrayList<int[]>();
        for(Chess chess:app.chessList){
            app.typeJudge(chess,0);
        }
        assertEquals(14,app.chessList.size());
    }
    /**
     * Test the move function of pawn
     */
    @Test
    public void PawnMoveAreaTest(){
        Chess chess = new Chess(624,624,"pawn",0,2,6,1);
        ArrayList<Chess> chessList = new ArrayList<Chess>();
        chessList.add(chess);
        chess.pawnMove(chessList);
        assertEquals(2,chess.getMoveArea().size());
        System.out.println("PawnMoveAreaTest pass");
    }
    /**
     * Test the attack area of pawn
     */
    @Test
    public void PawnAttackAreaTest(){
        App app = new App();
        app.chessList = new ArrayList<Chess>();
        Chess chess = new Chess(336,624,"pawn",0,2,6,1);
        Chess oppchess1 = new Chess(288,576,"pawn",1,2,6,1);
        Chess oppchess2 = new Chess(384,576,"pawn",1,2,6,1);
        app.chessList.add(chess);
        app.chessList.add(oppchess1);
        app.chessList.add(oppchess2);
        app.moveArea = new ArrayList<int[]>();
        app.attackArea = new ArrayList<int[]>();
        app.typeJudge(chess,0);
        assertEquals(2,app.attackArea.size());
        System.out.println("PawnAttackAreaTest pass");
    }
    /**
     * Test the move function of rook
     */
    @Test
    public void RookMoveAreaTest(){
        Chess chess = new Chess(336,336,"rook",0,9.5,6,1);
        //Chess chess2 = new Chess(624,624,"rook",1,9.5,6);
        ArrayList<Chess> chessList = new ArrayList<Chess>();
        chessList.add(chess);
        chess.rookMove(chessList);
        assertEquals(26,chess.getMoveArea().size());
        System.out.println("PawnMoveAreaTest pass");
    }
    /**
     * Test the attack area of rook
     */
    @Test
    public void RookAttackAreaTest(){
        App app = new App();
        app.chessList = new ArrayList<Chess>();
        Chess chess = new Chess(336,336,"rook",0,9.5,6,1);
        Chess oppchess1 = new Chess(336,576,"pawn",1,2,6,1);
        Chess oppchess2 = new Chess(576,336,"pawn",1,2,6,1);
        Chess oppchess3 = new Chess(336,0,"pawn",1,2,6,1);
        Chess oppchess4 = new Chess(0,336,"pawn",1,2,6,1);
        app.chessList.add(chess);
        app.chessList.add(oppchess1);
        app.chessList.add(oppchess2);
        app.chessList.add(oppchess3);
        app.chessList.add(oppchess4);
        app.moveArea = new ArrayList<int[]>();
        app.attackArea = new ArrayList<int[]>();
        app.typeJudge(chess,0);
        assertEquals(4,app.attackArea.size());
        System.out.println("PawnAttackAreaTest pass");
    }
    /**
     * Test the move function of knight
     */
    @Test
    public void knightMoveTest(){
        Chess chess = new Chess(336,336,"knight",0,1,6,1);
        ArrayList<Chess> chessList = new ArrayList<Chess>();
        chessList.add(chess);
        chess.knightMove(chessList);
        assertEquals(8,chess.getMoveArea().size());
        System.out.println("knightMoveTest pass");
    }
    /**
     * Test the attack area of knight
     */
    @Test
    public void knightAttackAreaTest(){
        App app = new App();
        app.chessList = new ArrayList<Chess>();
        Chess chess = new Chess(336,336,"knight",0,1,6,1);
        int[][] knightArea = {{1, 2},{2,1},{-1,2},{-2,1},{-1,-2},{-2,-1},{1,-2},{2,-1}};
        int[] tempList = new int[2];
        for(int[] direct:knightArea){
            Chess oppchess = new Chess(chess.getX()+direct[0]*48,chess.getY()+direct[1]*48,"pawn",1,2,6,1);
            app.chessList.add(oppchess);
        }
        app.chessList.add(chess);
        app.moveArea = new ArrayList<int[]>();
        app.attackArea = new ArrayList<int[]>();
        app.typeJudge(chess,0);
        assertEquals(8,app.attackArea.size());
        System.out.println("knightAttackAreaTest pass");
    }
    /**
     * Test the move function of bishop
     */
    @Test
    public void bishopMoveTest(){
        Chess chess = new Chess(336,336,"bishop",0,1,6,1);
        App app = new App();
        app.chessList = new ArrayList<Chess>();
        app.chessList.add(chess);
        app.moveArea = new ArrayList<int[]>();
        app.attackArea = new ArrayList<int[]>();
        app.typeJudge(chess,0);
        assertEquals(25,app.moveArea.size());
        System.out.println("bishopMoveTest pass");
    }
    /**
     * Test the attack area of bishop
     */
    @Test
    public void bishopAttackAreaTest(){
        App app = new App();
        app.chessList = new ArrayList<Chess>();
        Chess chess = new Chess(336,336,"bishop",0,1,6,1);
        int[][] Area = {{3, 3},{-3,3},{2,-2},{-4,-4}};
        int[] tempList = new int[2];
        for(int[] direct:Area){
            Chess oppchess = new Chess(chess.getX()+direct[0]*48,chess.getY()+direct[1]*48,"pawn",1,2,6,1);
            app.chessList.add(oppchess);
        }
        app.chessList.add(chess);
        app.moveArea = new ArrayList<int[]>();
        app.attackArea = new ArrayList<int[]>();
        app.typeJudge(chess,0);
        assertEquals(4,app.attackArea.size());
        System.out.println("bishopAttackAreaTest pass");
    }
    /**
     * Test the move function of cannon
     */
    @Test
    public void cannonMoveTest(){
        Chess chess = new Chess(336,336,"cannon",0,1,6,1);
        App app = new App();
        app.chessList = new ArrayList<Chess>();
        app.chessList.add(chess);
        app.moveArea = new ArrayList<int[]>();
        app.attackArea = new ArrayList<int[]>();
        app.typeJudge(chess,0);
        assertEquals(12,app.moveArea.size());
        System.out.println("bishopMoveTest pass");
    }
    /**
     * Test the timeAdd function of App
     */
    @Test
    public void timeAddTest(){
        App app = new App();
        app.player_seconds = 59;
        app.player_min = 0;
        app.player_increment = 2;
        app.playerTimeAdd();
        assertEquals(1,app.player_min);
        assertEquals(1,app.player_seconds);
        app.enemy_seconds = 59;
        app.enemy_min = 0;
        app.enemy_increment = 2;
        app.enemyTimeAdd();
        assertEquals(1,app.enemy_min);
        assertEquals(1,app.enemy_seconds);
        System.out.println("timeAddTest pass");
    }
    /**
     * Initialize the position of king
     */
    public void setKing(App app){
        app.initialize();
        app.chessList = new ArrayList<Chess>();
        app.king1 = new Chess(336,624,"king",0,999,6,1);
        app.king2 = new Chess(336,0,"king",1,999,6,1);
        app.chessList.add(app.king1);
        app.chessList.add(app.king2);
        app.playerChosenColor = 0;
        app.enemyColor = 1;

    }
    /**
     * Test the enemyMove() function and its first scenario: Move randomly
     */
    @Test
    public void enemyNormalMoveTest(){
        App app = new App();
        this.setKing(app);
        for(int i = 0;i<=13;i++){
            Chess pawn1 = new Chess(i*48,576,"pawn",0,2,6,1);
            Chess pawn2 = new Chess(i*48,48,"pawn",1,2,6,1);
            app.chessList.add(pawn1);
            app.chessList.add(pawn2);
        }
        app.enemyMove();
        assertEquals(1,app.enemyChess.getColor());
        System.out.println("enemyNormalMove pass");
    }
    /**
     * Test the enemyMove() function and its second scenario: attack higher chess of player
     */
    @Test
    public void enemyMoveToAttackTest(){
        App app = new App();
        this.setKing(app);
        for(int i = 0;i<=13;i++){
            Chess pawn1 = new Chess(i*48,576,"pawn",0,2,6,1);
            app.chessList.add(pawn1);
        }
        Chess rook1= new Chess(6*48,384,"rook",0,5.25,6,1);
        Chess queen1= new Chess(8*48,384,"queen",0,9.5,6,1);
        Chess chosenPawn= new Chess(7*48,7*48,"pawn",1,1,6,1);
        app.chessList.add(rook1);
        app.chessList.add(queen1);
        app.chessList.add(chosenPawn);
        int originalQueenRow = queen1.getRow();
        int originalQueenColumn = queen1.getColumn();
        app.enemyMove();
        assertEquals(originalQueenRow,app.enemyChess.getRow());
        assertEquals(originalQueenColumn,app.enemyChess.getColumn());
        System.out.println("enemyMoveToAttackTest pass");

    }
    /**
     * Test the enemyMove() function and
     * its third scenario: move higher value chess from being attack
     */
    @Test
    public void enemyMoveFromThreatenTest(){
        App app = new App();
        this.setKing(app);
        for(int i = 0;i<=13;i++){
            Chess pawn1 = new Chess(i*48,576,"pawn",0,2,6,1);
            app.chessList.add(pawn1);
        }
        Chess rook1= new Chess(6*48,336,"rook",1,5.25,6,1);
        Chess queen1= new Chess(8*48,336,"queen",1,9.5,6,1);
        Chess chosenPawn= new Chess(7*48,384,"pawn",0,1,6,1);
        app.chessList.add(rook1);
        app.chessList.add(queen1);
        app.chessList.add(chosenPawn);
        app.enemyMove();
        assertEquals("queen",app.enemyChess.getType());
        System.out.println("enemyMoveFromThreatenTest pass");

    }
    /**
     * Test the enemyMove() function and
     * its third scenario but chess is king
     */
    @Test
    public void enemyMoveKingFromThreatenTest(){
        App app = new App();
        this.setKing(app);
        Chess rook1= new Chess(6*48,336,"rook",1,5.25,6,1);
        Chess rook2= new Chess(7*48,288,"rook",0,5.25,6,1);
        Chess queen1= new Chess(8*48,336,"queen",1,9.5,6,1);
        Chess chosenPawn= new Chess(7*48,384,"pawn",0,1,6,1);
        app.chessList.add(rook1);
        app.chessList.add(rook2);
        app.chessList.add(queen1);
        app.chessList.add(chosenPawn);
        app.enemyMove();
        assertEquals("king",app.enemyChess.getType());
        System.out.println("enemyMoveKingFromThreatenTest pass");

    }
    /**
     * Test the check() function
     */
    @Test
    public void enemyCheckTest(){
        App app = new App();
        this.setKing(app);
        Chess rook= new Chess(7*48,288,"rook",0,5.25,6,1);
        app.chessList.add(rook);
        app.check(app.king2,1);
        assertEquals(true,app.enemyIsCheck);
        System.out.println("enemyCheckTest pass");

    }
    /**
     * Test the enemyMove() function and
     * its fourth scenario: move to check player's king
     */
    @Test
    public void enemyMoveToCheckTest(){
        App app = new App();
        this.setKing(app);
        for(int i = 0;i<=13;i++){
            Chess pawn1 = new Chess(i*48,48,"pawn",1,2,6,1);
            app.chessList.add(pawn1);
        }
        Chess queen= new Chess(0,96,"queen",1,5.25,6,1);
        app.chessList.add(queen);
        app.enemyMove();
        assertEquals("queen",app.enemyChess.getType());
        assertEquals(9,app.enemyChess.getRow());
        System.out.println("enemyMoveToCheckTest pass");

    }
    /**
     * Test the enemyMove() function and
     * its fifth scenario: avoid king attacking chess if it will cause check
     */
    @Test
    public void enemySafeAttackTest(){
        App app = new App();
        this.setKing(app);
        Chess pawn1 = new Chess(384,48,"pawn",0,2,6,1);
        Chess pawn2 = new Chess(432,96,"pawn",0,2,6,1);
        app.chessList.add(pawn1);
        app.chessList.add(pawn2);
        app.enemyMove();
        app.playerChessList =  new ArrayList<Chess>();
        for(Chess chess:app.chessList){
            if(chess.getColor()==0){
                app.playerChessList.add(chess);
            }
        }
        assertEquals(3,app.playerChessList.size());
        System.out.println("enemySafeAttackTest pass");
    }
    /**
     * Create two AI and let them play oppositely
     */
    @Test
    public void theFinalLoopTest(){
        App app = new App();
        app.chessList = new ArrayList<Chess>();
        String line = "RNBHCGAKGCEBNP";
        String line2 = "rnbhcgakgcebnp";
        String[] str = line.split("");
        String[] str2 = line2.split("");
        List<String> boardLine;
        boardLine = Arrays.asList(str);
        int row = 0;
        for(String s: boardLine){
            app.putChess(s,row,0);
            row+=1;
        }
        row = 0;
        boardLine = Arrays.asList(str2);
        for(String s: boardLine){
            app.putChess(s,row,13);
            row+=1;
        }
        app.enemyColor = 0;
        while(app.enemyWin==false){
            app.enemyMove();
            app.enemyColor = 1 - app.enemyColor;
        }
        System.out.println("theFinalLoopTest pass");
    }
}
