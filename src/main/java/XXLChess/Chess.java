package XXLChess;

import java.util.ArrayList;

public class Chess extends Shape{
    /**
     * The Chess's row
     */
    private int row;
    /**
     * The Chess's column
     */
    private int column;
    /**
     * The Chess's type
     */
    private String type;
    /**
     * The Chess's color, 0 is white, 1 is black
     */
    private int color;
    /**
     * Whether the Chess move to left side
     */
    private boolean moveLeft;
    /**
     * Whether the Chess move to right side
     */
    private boolean moveRight;
    /**
     * Whether the Chess move to Up
     */

    private boolean moveUp;
    /**
     * Whether the Chess move to Down
     */
    private boolean moveDown;
    /**
     * The Chess's move target in x-coordinate
     */
    private int targetX;
    /**
     * The Chess's move target in y-coordinate
     */
    private int targetY;
    /**
     * The Chess's move speed in x-coordinate
     */
    private double speedX;
    /**
     * The Chess's move speed in y-coordinate
     */
    private double speedY;
    /**
     * The Chess's move speed
     */
    private double speed;
    /**
     * The Chess's value
     */
    private double value;

    private double maxTime;
    /**
     * The Chess's move area
     */
    private ArrayList<int[]> moveArea;
    /**
     * The Chess's attack area
     */
    private  ArrayList<int[]> attackArea;
    /**
     * Whether the chess has been moved
     */
    private boolean firstMove;
    /**
     * Creates a new Chess object.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param type The type
     * @param color The color
     * @param value The value
     * @param speed The speed
     * @param maxTime The max time of chess to move
     */
    public Chess(int x, int y,String type, int color, double value,double speed,double maxTime) {
        super(x, y);
        this.row = x/48;
        this.column = y/48;
        this.type = type;
        this.value = value;
        this.color = color;
        this.moveLeft = false;
        this.moveRight = false;
        this.moveUp = false;
        this.moveDown = false;
        this.speed = speed;
        this.speedX = speed;
        this.speedY = speed;
        this.maxTime = maxTime * 60; // transfer to sketch
        this.firstMove = false;
        this.moveArea= new ArrayList<int[]>();
        this.attackArea= new ArrayList<int[]>();

    }

    /**
     * The function to move Chess
     */
    @Override
    public void tick() {
        if(this.moveLeft==true){
            this.x += speedX;
            if(this.targetX>=this.x){
                this.x = this.targetX;
                this.moveLeft = false;
            }
        }
        if(this.moveRight==true){
            this.x += speedX;
            if(this.targetX<=this.x){
                this.x = this.targetX;
                this.moveRight = false;
            }
        }
        if(this.moveUp==true){
            this.y += speedY;
            if(this.targetY>=this.y){
                this.y = this.targetY;
                this.moveUp = false;
            }
        }
        if(this.moveDown==true){
            this.y += speedY;
            if(this.targetY<=this.y){
                this.y = this.targetY;
                this.moveDown = false;
            }
        }
    }
    public void mouseClicked(){

    }
    /**
     * Gets the Row of Chess
     * @return The row of chess
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Calculate the move and attack area of a pawn chess
     */
    public void pawnMove(ArrayList<Chess>chessList){
        this.moveArea= new ArrayList<int[]>();
        this.attackArea= new ArrayList<int[]>();
        if(this.column!=0&&this.color==0){
            boolean left = false;
            boolean right = false;
            boolean cannotMove = false;
            int[] tempList = new int[]{this.row, this.column-1};
            int[] tempList2 = new int[]{this.row, this.column-2};
            int[] leftAttack = new int[2];
            if(this.row!=0){
                leftAttack = new int[]{this.row-1, this.column-1};
                left = true;
            }
            int[] rightAttack= new int[2];
            if(this.row!=13){
                rightAttack = new int[]{this.row+1, this.column-1};
                right = true;
            }
            this.moveArea.add(tempList);
            if(getFirstMove()==false){
                this.moveArea.add(tempList2);
            }
            for(Chess oppChess:chessList){
                if(tempList[0]==oppChess.getRow()&&tempList[1]==oppChess.getColumn()){
                    this.moveArea.remove(tempList);
                    cannotMove = true;
                }
                if(getFirstMove()==false){
                    if((tempList2[0]==oppChess.getRow()&&tempList2[1]==oppChess.getColumn()) || cannotMove == true){
                        this.moveArea.remove(tempList2);
                    }
                }
                if(oppChess.getColor()!=color){
                    if(leftAttack[0]==oppChess.getRow()&&leftAttack[1]==oppChess.getColumn()&& left==true){
                        this.attackArea.add(leftAttack);
                    }
                    if(rightAttack[0]==oppChess.getRow()&&rightAttack[1]==oppChess.getColumn()&& right==true){
                        this.attackArea.add(rightAttack);
                    }
                }

            }

        }else if(this.column!=13 && this.color==1){
            boolean left = false;
            boolean right = false;
            boolean cannotMove = false;
            int[] tempList = new int[]{this.row, this.column+1};
            int[] tempList2 = new int[]{this.row, this.column+2};
            int[] leftAttack = new int[2];
            if(this.row!=0){
                leftAttack = new int[]{this.row-1, this.column+1};
                left = true;
            }
            int[] rightAttack= new int[2];
            if(this.row!=13){
                rightAttack = new int[]{this.row+1, this.column+1};
                right = true;
            }

            this.moveArea.add(tempList);
            if(getFirstMove()==false){
                this.moveArea.add(tempList2);
            }
            for(Chess oppChess:chessList){
                if(tempList[0]==oppChess.getRow()&&tempList[1]==oppChess.getColumn()){
                    this.moveArea.remove(tempList);
                    cannotMove = true;
                }
                if(getFirstMove()==false){
                    if((tempList2[0]==oppChess.getRow()&&tempList2[1]==oppChess.getColumn()) || cannotMove == true){
                        this.moveArea.remove(tempList2);
                    }
                }
                if(oppChess.getColor()!=color){
                    if(leftAttack[0]==oppChess.getRow()&&leftAttack[1]==oppChess.getColumn()&& left==true){
                        this.attackArea.add(leftAttack);
                    }
                    if(rightAttack[0]==oppChess.getRow()&&rightAttack[1]==oppChess.getColumn()&& right==true){
                        this.attackArea.add(rightAttack);
                    }
                }

            }
        }
    }

    /**
     * Calculate the move and attack area of a rook chess
     */
    public void rookMove(ArrayList<Chess>chessList){
        this.moveArea= new ArrayList<int[]>();
        this.attackArea= new ArrayList<int[]>();
        int[] tempList;
        int up = -1;
        int down = 14;
        int left = -1;
        int right = 14;
        boolean upIsEnemy = false;
        boolean downIsEnemy = false;
        boolean leftIsEnemy = false;
        boolean rightIsEnemy = false;
        for(Chess oppChess:chessList){
            if(oppChess.getRow()==this.row&&oppChess.getColumn()<this.column){
                if(oppChess.getColumn()>up){
                    upIsEnemy = false;
                    up = oppChess.getColumn();
                    if(oppChess.getColor()!=color){
                        upIsEnemy = true;
                    }
                }
            }
            if(oppChess.getRow()==this.row&&oppChess.getColumn()>this.column){
                if(oppChess.getColumn()<down){
                    downIsEnemy = false;
                    down = oppChess.getColumn();
                    if(oppChess.getColor()!=color){
                        downIsEnemy = true;
                    }

                }
            }
            if(oppChess.getColumn()==this.column&&oppChess.getRow()<this.row){
                if(oppChess.getRow()>left){
                    leftIsEnemy = false;
                    left = oppChess.getRow();
                    if(oppChess.getColor()!=color){
                        leftIsEnemy = true;
                    }

                }
            }
            if(oppChess.getColumn()==this.column&&oppChess.getRow()>this.row){
                if(oppChess.getRow()<right){
                    rightIsEnemy = false;
                    right = oppChess.getRow();
                    if(oppChess.getColor()!=color){
                        rightIsEnemy = true;
                    }

                }
            }
        }

        if(up!= this.column-1){
            for(int i = up + 1;i<this.column;i+=1){
                tempList = new int[2];
                tempList[0] = this.row;
                tempList[1] = i;
                this.moveArea.add(tempList);
            }
        }
        if(down!= this.column+1){
            for(int i = down - 1;i>this.column;i-=1){
                tempList = new int[2];
                tempList[0] = this.row;
                tempList[1] = i;
                this.moveArea.add(tempList);
            }
        }
        if(left!=this.row-1){
            for(int i = left + 1;i<this.row;i+=1){
                tempList = new int[2];
                tempList[0] = i;
                tempList[1] = this.column;
                this.moveArea.add(tempList);
            }
        }
        if(right!=this.row+1){
            for(int i = right - 1;i>this.row;i-=1){
                tempList = new int[2];
                tempList[0] = i;
                tempList[1] = this.column;
                this.moveArea.add(tempList);
            }
        }
        if(upIsEnemy){
            this.attackArea.add(new int[]{this.row,up});
        }
        if(downIsEnemy){
            this.attackArea.add(new int[]{this.row,down});
        }
        if(leftIsEnemy){ // y same x not same
            this.attackArea.add(new int[]{left,this.column});
        }
        if(rightIsEnemy){
            this.attackArea.add(new int[]{right,this.column});
        }
    }
    /**
     * Calculate the move and attack area of a cannon chess
     */
    public void cannonMove(ArrayList<Chess>chessList){
        this.moveArea= new ArrayList<int[]>();
        this.attackArea= new ArrayList<int[]>();
        int[][] cannonArea = {{0,1},{0,2},{1,0},{2,0},{0,-1},{0,-2},{-1,0},{-2,0},{-4,0},{4,0},{0,4},{0,-4}};
        int[] tempList = new int[2];
        for(int[] direct:cannonArea){
            tempList[0]=this.row+direct[0];
            tempList[1]=this.column+direct[1];
            if(tempList[0]<=13 && tempList[0]>=0 && tempList[1]<=13 && tempList[1]>=0){
                this.moveArea.add(tempList);
                for(Chess oppchess:chessList){
                    if(oppchess.getRow()==tempList[0]&&oppchess.getColumn()==tempList[1]){
                        this.moveArea.remove(tempList);
                        if(oppchess.getColor()!=color){
                            this.attackArea.add(tempList);
                        }
                    }
                }
            }
            tempList = new int[2];
        }
    }
    /**
     * Calculate the move and attack area of a knight chess
     */
    public void knightMove(ArrayList<Chess>chessList){
        this.moveArea= new ArrayList<int[]>();
        this.attackArea= new ArrayList<int[]>();
        int[][] knightArea = {{1, 2},{2,1},{-1,2},{-2,1},{-1,-2},{-2,-1},{1,-2},{2,-1}};
        int[] tempList = new int[2];
        for(int[] direct:knightArea){
            tempList[0]=this.row+direct[0];
            tempList[1]=this.column+direct[1];
            if(tempList[0]<=13 && tempList[0]>=0 && tempList[1]<=13 && tempList[1]>=0){
                this.moveArea.add(tempList);
                for(Chess oppchess:chessList){
                    if(oppchess.getRow()==tempList[0]&&oppchess.getColumn()==tempList[1]){
                        this.moveArea.remove(tempList);
                        if(oppchess.getColor()!=color){
                            this.attackArea.add(tempList);
                        }
                    }
                }
            }
            tempList = new int[2];
        }
    }
    /**
     * Calculate the move and attack area of a camel chess
     */
    public void camelMove(ArrayList<Chess>chessList){
        this.moveArea= new ArrayList<int[]>();
        this.attackArea= new ArrayList<int[]>();
        int[][] knightArea = {{1, 3},{3,1},{-1,3},{-3,1},{-1,-3},{-3,-1},{1,-3},{3,-1}};
        int[] tempList = new int[2];
        for(int[] direct:knightArea){
            tempList[0]=this.row+direct[0];
            tempList[1]=this.column+direct[1];
            if(tempList[0]<=13 && tempList[0]>=0 && tempList[1]<=13 && tempList[1]>=0){
                this.moveArea.add(tempList);
                for(Chess oppchess:chessList){
                    if(oppchess.getRow()==tempList[0]&&oppchess.getColumn()==tempList[1]){
                        this.moveArea.remove(tempList);
                        if(oppchess.getColor()!=color){
                            this.attackArea.add(tempList);
                        }
                    }
                }
            }
            tempList = new int[2];
        }
    }

    /**
     * Gets the Column of Chess
     * @return The column of chess
     */
    public int getColumn() {
        return this.column;
    }


    /**
     * Gets the Type of Chess
     * @return The type of chess
     */
    public String getType(){
        return this.type;
    }
    /**
     * Set the chess's x-coordinate.
     *
     * @param x The new x
     */
    public void setX(int x){
        this.row=x/48;
        this.targetX = this.row * 48;
        this.moveLeft = false;
        this.moveRight = false;
        if(targetX<this.x){
            this.moveLeft = true;
        }else if(targetX>this.x){
            this.moveRight = true;
        }
        if(Math.abs(targetX - this.x) > maxTime * speed){
            this.speedX = (targetX-this.x)/maxTime;

        }else{
            if(this.moveRight){
                this.speedX = speed;
            }else if(this.moveLeft){
                this.speedX = -speed;
            }
        }
    }
    /**
     * Set the chess's y-coordinate.
     *
     * @param y The new y
     */
    public void setY(int y){
        this.column = y/48;
        this.targetY = this.column*48;
        this.moveUp = false;
        this.moveDown = false;
        if(targetY<this.y){
            this.moveUp = true;
        }else if(targetY>this.y){
            this.moveDown = true;
        }

        if(Math.abs(targetY - this.y) > maxTime * speed){
            this.speedY = (targetY-this.y)/maxTime;

        }else{
            if(this.moveDown){
                this.speedY = speed;
            }else if(this.moveUp){
                this.speedY = -speed;
            }
        }
    }
    /**
     * Get the Color of Chess
     * @return The color of chess
     */
    public int getColor(){
        return this.color;
    }
    /**
     * Get the Value of Chess
     * @return The value of chess
     */
    public double getValue(){return this.value;}
    /**
     * Get the FirstMove of Chess
     * @return The firstmove of chess
     */
    public boolean getFirstMove(){
        return this.firstMove;
    }
    /**
     * Set the chess's FirstMove
     *
     * @param n the new FirstMove
     */
    public void setFirstMove(boolean n){
        this.firstMove = n;
    }
    /**
     * Get the move area of Chess
     * @return The move area of chess
     */
    public ArrayList<int[]> getMoveArea() {
        return moveArea;
    }
    /**
     * Get the attackArea of Chess
     * @return The attack area of chess
     */
    public ArrayList<int[]> getAttackArea() {
        return attackArea;
    }
}
