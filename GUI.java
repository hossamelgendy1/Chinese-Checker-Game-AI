import java.util.*;
import java.util.concurrent.TimeUnit;
import java.net.URL;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.SpringLayout;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
//////////////////////////////////////////////////////////////////////////////
// GUI class
public class GUI extends JFrame {
    
	private JFrame frame;
	private Board board;
	//////////////////////////////////////////////////////////////////////////
	// main
    public static void main(String[] args) {
        new GUI();
    } // end of main

	//////////////////////////////////////////////////////////////////////////
    // GUI constructor
    public GUI() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
				board = new Board();
                frame = new JFrame("Chinese Checkers");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(board);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.setResizable(false);
            }
        });
    } // end of GUI constructor

	public void resetBoard(){
		frame.remove(board);
		board = new Board();
		frame.add(board);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}

    //////////////////////////////////////////////////////////////////////////
    // Board class
    public class Board extends JPanel {

    	private int BOARD_SIZE = 620;
    	private JLabel gb [] = new JLabel[121];
    	private JLabel modeLabels [] = new JLabel[4];
    	private JLabel gameStatus1;
    	private JLabel gameStatus2;
    	private JButton endTurn;

    	private JButton modes [] = new JButton[3];
    	private SpringLayout boardLayout;
    	
		private ArrayList<Integer> turnOrder;
		private HashMap<Integer, Integer> nextTurn;
    	
    	private int currPlayer;
    	private int gameMode; // 0 - 8

    	private boolean gameStarted;
    	private boolean gameOver;

    	private ImageIcon bkg;

		private ImageIcon o_grn, o_blk, o_wht, o_yel, o_blu, o_red, o_blank;
		private ImageIcon s_grn, s_blk, s_wht, s_yel, s_blu, s_red;
		private ImageIcon h_grn, h_blk, h_wht, h_yel, h_blu, h_red;
		private ImageIcon d_grn, d_blk, d_wht, d_yel, d_blu, d_red;

		private ImageIcon mode_0, mode_1, mode_2;

		private ImageIcon boardBKG;

		private BoardMouseHandler boardMouseHandler = new BoardMouseHandler();
		private OptionMouseHandler optionMouseHandler
										 = new OptionMouseHandler();

    	private int winner;

		private int startIdx = -1, endIdx = -1;
		private int[] computerMove;
		private ChineseChecker computerAI;
    	
    	//////////////////////////////////////////////////////////////////////
    	// Initialize all the ImageIcon objects with resources
    	public void initImageIcons() {

    		o_grn = new ImageIcon(getClass().getResource("images/o_green_34.png"));
    		o_blk = new ImageIcon(getClass().getResource("images/o_black_34.png"));
    		o_wht = new ImageIcon(getClass().getResource("images/o_white_34.png"));
    		o_yel = new ImageIcon(getClass().getResource("images/o_yellow_34.png"));
    		o_blu = new ImageIcon(getClass().getResource("images/o_blue_34.png"));
    		o_red = new ImageIcon(getClass().getResource("images/o_red_34.png"));
    		o_blank = new ImageIcon(getClass().getResource("images/o_blank_34.png"));

			s_grn = new ImageIcon(getClass().getResource("images/s_green_34.png"));
    		s_red = new ImageIcon(getClass().getResource("images/s_red_34.png"));
			s_blk = new ImageIcon(getClass().getResource("images/s_black_34.png"));
    		s_wht = new ImageIcon(getClass().getResource("images/s_white_34.png"));
			s_yel = new ImageIcon(getClass().getResource("images/s_yellow_34.png"));
    		s_blu = new ImageIcon(getClass().getResource("images/s_blue_34.png"));

			h_grn = new ImageIcon(getClass().getResource("images/h_green_34.png"));
    		h_red = new ImageIcon(getClass().getResource("images/h_red_34.png"));
			h_blk = new ImageIcon(getClass().getResource("images/h_black_34.png"));
    		h_wht = new ImageIcon(getClass().getResource("images/h_white_34.png"));
			h_yel = new ImageIcon(getClass().getResource("images/h_yellow_34.png"));
    		h_blu = new ImageIcon(getClass().getResource("images/h_blue_34.png"));

			d_grn = new ImageIcon(getClass().getResource("images/d_green_34.png"));
    		d_red = new ImageIcon(getClass().getResource("images/d_red_34.png"));
			d_blk = new ImageIcon(getClass().getResource("images/d_black_34.png"));
    		d_wht = new ImageIcon(getClass().getResource("images/d_white_34.png"));
			d_yel = new ImageIcon(getClass().getResource("images/d_yellow_34.png"));
    		d_blu = new ImageIcon(getClass().getResource("images/d_blue_34.png"));

			mode_0 = new ImageIcon(getClass().getResource("images/mode_0.jpg"));
			mode_1 = new ImageIcon(getClass().getResource("images/mode_1.jpg"));
			mode_2 = new ImageIcon(getClass().getResource("images/mode_2.jpg"));
    		
    		boardBKG = new ImageIcon(getClass().getResource("images/board_620.jpg"));
    		
    	} // end of initImageIcons
    	
    	//////////////////////////////////////////////////////////////////////
    	// Based on the gameMode integer passed in it builds 
    	public void setTurnOrder(int gameMode) {
			switch(gameMode) {

				/////////////////////
				// 2 Players
				case 0:
					currPlayer = 0;

					nextTurn.put(0, 3);
					nextTurn.put(3, 0);

					turnOrder.add(0);
					turnOrder.add(3);

					break;
				case 1:
					currPlayer = 0;

					nextTurn.put(0, 3);
					nextTurn.put(3, 0);

					turnOrder.add(0);
					turnOrder.add(3);

					break;
				case 2:
					currPlayer = 0;

					nextTurn.put(0, 3);
					nextTurn.put(3, 0);

					turnOrder.add(0);
					turnOrder.add(3);

					break;
			}
    	} // end of setTurnOrder

    	//////////////////////////////////////////////////////////////////////
    	// Initialize major game control variables
	    public void initGame() {
			computerAI = new ChineseChecker();

	    	gameOver = false;
	    	gameStarted = false;
	    	winner = -1;

	    	turnOrder = new ArrayList<Integer>();
	    	nextTurn = new HashMap<Integer, Integer>();
	    	
	    	bkg = boardBKG;

	    	buildTurnInfo();
	    	buildOptions();
	    	buildBoard();

	    } // end of initGame
	    
	    //////////////////////////////////////////////////////////////////////
	    // Board constructor 
	    public Board() {
	    	initImageIcons();

        	boardLayout = new SpringLayout();
	        setLayout(boardLayout);
	        initGame();

	    } // end of Board constructor

	    //////////////////////////////////////////////////////////////////////
	    // Shows the game options that intially show up at start of game
	    public void showOptions() {
	    	gameStatus1.setVisible(false);
	    	gameStatus2.setVisible(false);

	    	modeLabels[0].setVisible(true);
			modes[0].setVisible(true);
			modes[1].setVisible(true);
			modes[2].setVisible(true);

	    } // end of showOptions

	    //////////////////////////////////////////////////////////////////////
	    // Hides the game options that intially show up at start of game
	    public void hideOptions() {
	    	gameStatus1.setVisible(true);
	    	gameStatus2.setVisible(true);

	    	modeLabels[0].setVisible(false);
			modes[0].setVisible(false);
			modes[0].setVisible(false);
			modes[1].setVisible(false);
			modes[2].setVisible(false);

	    } // end of hideOptions


	    public void updatePlayer(int i) {
	    	switch(i) {
				case 0:	
					gameStatus1.setText("Your"); 
					gameStatus1.setForeground(Color.GREEN);
					break;
				case 3:
					gameStatus1.setText("Computer's");
					gameStatus1.setForeground(Color.RED);
					break;
				default: break;
			}
	    }
		//////////////////////////////////////////////////////////////////////
	    // Builds the turn information elements to be displayed
	    public void buildTurnInfo() {
	    	
	    	gameStatus1 = new JLabel();
	    	gameStatus2 = new JLabel();
	    	
        	gameStatus1.setFont(new Font("Tahoma", Font.BOLD, 30));
        	gameStatus2.setFont(new Font("Tahoma", Font.BOLD, 30));

			updatePlayer(currPlayer);
	    	gameStatus2.setText("Turn"); 

	    	add(gameStatus1);
	    	add(gameStatus2);

			boardLayout.putConstraint(SpringLayout.NORTH, gameStatus1, 
				10, SpringLayout.NORTH, this);
	        boardLayout.putConstraint(SpringLayout.WEST, gameStatus1, 
	        	10, SpringLayout.WEST, this);

			boardLayout.putConstraint(SpringLayout.NORTH, gameStatus2, 
				0, SpringLayout.SOUTH, gameStatus1);
	        boardLayout.putConstraint(SpringLayout.WEST, gameStatus2, 
	        	0, SpringLayout.WEST, gameStatus1);
			endTurn=new JButton("end turn");

	    	
	    	endTurn.setPreferredSize(new Dimension(135, 35));
        	endTurn.setFocusPainted(false);
        	endTurn.setMargin(new Insets(0,0,0,0));
        	endTurn.setFont(new Font("Tahoma", Font.BOLD, 25));
	    	endTurn.addMouseListener(boardMouseHandler);
	    	endTurn.setBackground(new Color(59, 89, 182));
    		endTurn.setForeground(Color.WHITE);

			boardLayout.putConstraint(SpringLayout.SOUTH, endTurn, 
				-10, SpringLayout.SOUTH, this);
	        boardLayout.putConstraint(SpringLayout.EAST, endTurn, 
	        	-10, SpringLayout.EAST, this);
	        
    		add(endTurn);

    		endTurn.setVisible(false);
	    	gameStatus1.setVisible(false);
	    	gameStatus2.setVisible(false);

	    } // end of buildTurnInfo

	    //////////////////////////////////////////////////////////////////////
	    // Builds the option objects for gameMode selection
	    public void buildOptions() {
			for (int i = 0; i < 3; i++) {
				modes[i] = new JButton();
				modes[i].setPreferredSize(new Dimension(25, 25));
				modes[i].setFocusPainted(false);
				modes[i].setMargin(new Insets(0,0,0,0));
				modes[i].setFont(new Font("Tahoma", Font.BOLD, 12));
				modes[i].addMouseListener(optionMouseHandler);
				modes[i].setBackground(new Color(59, 89, 182));
				modes[i].setForeground(Color.WHITE);
				add(modes[i]);
			}


				modeLabels[0] = new JLabel();
	    		modeLabels[0].setFont(new Font("Tahoma", Font.BOLD, 14));
	    		add(modeLabels[0]);


	    	//////////////////////////////////////////////////////////////////

			modeLabels[0].setText("Difficulty Levels:");

			boardLayout.putConstraint(SpringLayout.NORTH, modeLabels[0],
					15, SpringLayout.NORTH, this);
			boardLayout.putConstraint(SpringLayout.WEST, modeLabels[0],
					15, SpringLayout.WEST, this);

			modes[0].setText("1");
			modes[1].setText("2");
			modes[2].setText("3");

			boardLayout.putConstraint(SpringLayout.NORTH, modes[0],
					5, SpringLayout.SOUTH, modeLabels[0]);
			boardLayout.putConstraint(SpringLayout.WEST, modes[0],
					0, SpringLayout.WEST, modeLabels[0]);

			boardLayout.putConstraint(SpringLayout.NORTH, modes[1],
					0, SpringLayout.NORTH, modes[0]);
			boardLayout.putConstraint(SpringLayout.WEST, modes[1],
					5, SpringLayout.EAST, modes[0]);

			boardLayout.putConstraint(SpringLayout.NORTH, modes[2],
					0, SpringLayout.NORTH, modes[1]);
			boardLayout.putConstraint(SpringLayout.WEST, modes[2],
					5, SpringLayout.EAST, modes[1]);

	    } // end of buildOptions

	    //////////////////////////////////////////////////////////////////////
	    // builds the springlayout for the board
	    public void buildBoard() {

			// Initialize game board aka gb
        	for (int i = 0; i < gb.length; i++) {
        		gb[i] = new JLabel(pieceType(i));
            	gb[i].addMouseListener(boardMouseHandler);
        		add(gb[i]);
        	}

	        int SIZE = 34;
	        int BELOW = 0;
	        int RIGHT = 5;
	        int OFFSET = 20;

	        boardLayout.putConstraint(SpringLayout.NORTH, gb[0],
	         20, SpringLayout.WEST, this);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[0],
	         295, SpringLayout.WEST, this);
	        
	        boardLayout.putConstraint(SpringLayout.NORTH, gb[1],
	         BELOW, SpringLayout.SOUTH, gb[0]);
	        boardLayout.putConstraint(SpringLayout.NORTH, gb[2],
	         BELOW, SpringLayout.SOUTH, gb[0]);

	        boardLayout.putConstraint(SpringLayout.WEST, gb[1],
	         -OFFSET, SpringLayout.WEST, gb[0]);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[2],
	         RIGHT, SpringLayout.EAST, gb[1]);

	        boardLayout.putConstraint(SpringLayout.NORTH, gb[3],
	         BELOW, SpringLayout.SOUTH, gb[1]);
	        boardLayout.putConstraint(SpringLayout.NORTH, gb[4],
	         BELOW, SpringLayout.SOUTH, gb[1]);
	        boardLayout.putConstraint(SpringLayout.NORTH, gb[5],
	         BELOW, SpringLayout.SOUTH, gb[1]);

	        boardLayout.putConstraint(SpringLayout.WEST, gb[3],
	         -OFFSET, SpringLayout.WEST, gb[1]);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[4],
	         RIGHT, SpringLayout.EAST, gb[3]);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[5],
	         RIGHT, SpringLayout.EAST, gb[4]);
	        
	        boardLayout.putConstraint(SpringLayout.NORTH, gb[6],
	         BELOW, SpringLayout.SOUTH, gb[3]);
	        boardLayout.putConstraint(SpringLayout.NORTH, gb[7],
	         BELOW, SpringLayout.SOUTH, gb[3]);
	        boardLayout.putConstraint(SpringLayout.NORTH, gb[8],
	         BELOW, SpringLayout.SOUTH, gb[3]);
	        boardLayout.putConstraint(SpringLayout.NORTH, gb[9],
	         BELOW, SpringLayout.SOUTH, gb[3]);

	        boardLayout.putConstraint(SpringLayout.WEST, gb[6],
	         -OFFSET, SpringLayout.WEST, gb[3]);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[7],
	         RIGHT, SpringLayout.EAST, gb[6]);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[8],
	         RIGHT, SpringLayout.EAST, gb[7]);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[9],
	         RIGHT, SpringLayout.EAST, gb[8]);

	        for (int i = 10; i < 23; i++) {
	            boardLayout.putConstraint(SpringLayout.NORTH, gb[i],
	             BELOW, SpringLayout.SOUTH, gb[6]);    
	        }

	        boardLayout.putConstraint(SpringLayout.EAST, gb[10],
	         -RIGHT, SpringLayout.WEST, gb[11]);
	        boardLayout.putConstraint(SpringLayout.EAST, gb[11],
	         -RIGHT, SpringLayout.WEST, gb[12]);
	        boardLayout.putConstraint(SpringLayout.EAST, gb[12],
	         -RIGHT, SpringLayout.WEST, gb[13]);
	        boardLayout.putConstraint(SpringLayout.EAST, gb[13],
	         -RIGHT, SpringLayout.WEST, gb[14]);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[14],
	         -OFFSET, SpringLayout.WEST, gb[6]);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[15],
	         RIGHT, SpringLayout.EAST, gb[14]);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[16],
	         RIGHT, SpringLayout.EAST, gb[15]);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[17],
	         RIGHT, SpringLayout.EAST, gb[16]);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[18],
	         RIGHT, SpringLayout.EAST, gb[17]);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[19],
	         RIGHT, SpringLayout.EAST, gb[18]);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[20],
	         RIGHT, SpringLayout.EAST, gb[19]);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[21],
	         RIGHT, SpringLayout.EAST, gb[20]);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[22],
	         RIGHT, SpringLayout.EAST, gb[21]);

	        boardLayout.putConstraint(SpringLayout.NORTH, gb[23],
	         BELOW, SpringLayout.SOUTH, gb[10]);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[23],
	         OFFSET, SpringLayout.WEST, gb[10]);

	        for (int i = 24; i < 35; i++) {
	            boardLayout.putConstraint(SpringLayout.NORTH, gb[i],
	             BELOW, SpringLayout.SOUTH, gb[10]);    
	            boardLayout.putConstraint(SpringLayout.WEST, gb[i],
	             RIGHT, SpringLayout.EAST, gb[i-1]);
	        }
	        
	        boardLayout.putConstraint(SpringLayout.NORTH, gb[35],
	         BELOW, SpringLayout.SOUTH, gb[23]);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[35],
	         OFFSET, SpringLayout.WEST, gb[23]);

	        for (int i = 36; i < 46; i++) {
	            boardLayout.putConstraint(SpringLayout.NORTH, gb[i],
	             BELOW, SpringLayout.SOUTH, gb[23]);    
	            boardLayout.putConstraint(SpringLayout.WEST, gb[i],
	             RIGHT, SpringLayout.EAST, gb[i-1]);
	        }
	        
	        boardLayout.putConstraint(SpringLayout.NORTH, gb[46],
	         BELOW, SpringLayout.SOUTH, gb[35]);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[46],
	         OFFSET, SpringLayout.WEST, gb[35]);

	        for (int i = 47; i < 56; i++) {
	            boardLayout.putConstraint(SpringLayout.NORTH, gb[i],
	             BELOW, SpringLayout.SOUTH, gb[35]);    
	            boardLayout.putConstraint(SpringLayout.WEST, gb[i],
	             RIGHT, SpringLayout.EAST, gb[i-1]);
	        }

	        boardLayout.putConstraint(SpringLayout.NORTH, gb[56],
	         BELOW, SpringLayout.SOUTH, gb[46]);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[56],
	         OFFSET, SpringLayout.WEST, gb[46]);

	        for (int i = 57; i < 65; i++) {
	            boardLayout.putConstraint(SpringLayout.NORTH, gb[i],
	             BELOW, SpringLayout.SOUTH, gb[46]);    
	            boardLayout.putConstraint(SpringLayout.WEST, gb[i],
	             RIGHT, SpringLayout.EAST, gb[i-1]);
	        }

	        boardLayout.putConstraint(SpringLayout.NORTH, gb[65],
	         BELOW, SpringLayout.SOUTH, gb[56]);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[65],
	         -OFFSET, SpringLayout.WEST, gb[56]);

	        for (int i = 66; i < 75; i++) {
	            boardLayout.putConstraint(SpringLayout.NORTH, gb[i],
	             BELOW, SpringLayout.SOUTH, gb[56]);    
	            boardLayout.putConstraint(SpringLayout.WEST, gb[i],
	             RIGHT, SpringLayout.EAST, gb[i-1]);
	        }

	        boardLayout.putConstraint(SpringLayout.NORTH, gb[75],
	         BELOW, SpringLayout.SOUTH, gb[65]);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[75],
	         -OFFSET, SpringLayout.WEST, gb[65]);

	        for (int i = 76; i < 86; i++) {
	            boardLayout.putConstraint(SpringLayout.NORTH, gb[i],
	             BELOW, SpringLayout.SOUTH, gb[65]);    
	            boardLayout.putConstraint(SpringLayout.WEST, gb[i],
	             RIGHT, SpringLayout.EAST, gb[i-1]);
	        }

	        boardLayout.putConstraint(SpringLayout.NORTH, gb[86],
	         BELOW, SpringLayout.SOUTH, gb[75]);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[86],
	         -OFFSET, SpringLayout.WEST, gb[75]);

	        for (int i = 87; i < 98; i++) {
	            boardLayout.putConstraint(SpringLayout.NORTH, gb[i],
	             BELOW, SpringLayout.SOUTH, gb[75]);    
	            boardLayout.putConstraint(SpringLayout.WEST, gb[i],
	             RIGHT, SpringLayout.EAST, gb[i-1]);
	        }

	        boardLayout.putConstraint(SpringLayout.NORTH, gb[98],
	         BELOW, SpringLayout.SOUTH, gb[86]);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[98],
	         -OFFSET, SpringLayout.WEST, gb[86]);

	        for (int i = 99; i < 111; i++) {
	            boardLayout.putConstraint(SpringLayout.NORTH, gb[i],
	             BELOW, SpringLayout.SOUTH, gb[86]);    
	            boardLayout.putConstraint(SpringLayout.WEST, gb[i],
	             RIGHT, SpringLayout.EAST, gb[i-1]);
	        }

	        boardLayout.putConstraint(SpringLayout.NORTH, gb[111],
	         BELOW, SpringLayout.SOUTH, gb[102]);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[111],
	         OFFSET, SpringLayout.WEST, gb[102]);

	        for (int i = 112; i < 115; i++) {
	            boardLayout.putConstraint(SpringLayout.NORTH, gb[i],
	             BELOW, SpringLayout.SOUTH, gb[102]);    
	            boardLayout.putConstraint(SpringLayout.WEST, gb[i],
	             RIGHT, SpringLayout.EAST, gb[i-1]);
	        }

	        boardLayout.putConstraint(SpringLayout.NORTH, gb[115],
	         BELOW, SpringLayout.SOUTH, gb[111]);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[115],
	         OFFSET, SpringLayout.WEST, gb[111]);

	        for (int i = 116; i < 118; i++) {
	            boardLayout.putConstraint(SpringLayout.NORTH, gb[i],
	             BELOW, SpringLayout.SOUTH, gb[111]);    
	            boardLayout.putConstraint(SpringLayout.WEST, gb[i],
	             RIGHT, SpringLayout.EAST, gb[i-1]);
	        }

	        boardLayout.putConstraint(SpringLayout.NORTH, gb[118],
	         BELOW, SpringLayout.SOUTH, gb[115]);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[118],
	         OFFSET, SpringLayout.WEST, gb[115]);

	        boardLayout.putConstraint(SpringLayout.NORTH, gb[119],
	         BELOW, SpringLayout.SOUTH, gb[115]);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[119],
	         RIGHT, SpringLayout.EAST, gb[118]);

	        boardLayout.putConstraint(SpringLayout.NORTH, gb[120],
	         BELOW, SpringLayout.SOUTH, gb[118]);
	        boardLayout.putConstraint(SpringLayout.WEST, gb[120],
	         OFFSET, SpringLayout.WEST, gb[118]);
	    } // end of buildBoard
	    
	    //////////////////////////////////////////////////////////////////////
	    // sets the icons of the JLabels that match JLabel 'p'
	    public void showMoves(JLabel p) {
	    	for (int i = 0; i < gb.length; i++) {
	    		if((inRange(p, gb[i]) || canJump(p, gb[i])) && 
	    			p != gb[i] && !isMarble(gb[i])) {
	    			gb[i].setIcon(getImageIcon(getColorInt(p), 'h'));
	    		}
	    	}
	    } // end of showMoves

	    //////////////////////////////////////////////////////////////////////
	    // Hides all the highlighted options available to JLabel p
	    public void hideMoves(JLabel p) {
	    	// NEED TO ADD HIDE JUMPS TOO
	    	for (int i = 0; i < gb.length; i++) {
	    		if(isHighlight(gb[i])) {
	    			gb[i].setIcon(o_blank);
	    		}
	    	}
	    } // end of hideMoves

	    //////////////////////////////////////////////////////////////////////
	    // Returns true only if JLabel p is blank
	    public boolean isBlank(JLabel p) { 
	    	return 6 == getColorInt(p); 
	    } // end of isBlank

	   	//////////////////////////////////////////////////////////////////////
	    // Returns true only if JLabel p is a marble
	    public boolean isMarble(JLabel p) { 
	    	return 0 <= getColorInt(p) && 5 >= getColorInt(p); 
	    } // end of isMarble

	    //////////////////////////////////////////////////////////////////////
	    // Returns true only if JLabel p is a highlighted space
	    public boolean isHighlight(JLabel p) {
    		if(p.getIcon() == h_grn) return true;
    		if(p.getIcon() == h_red) return true;
			if(p.getIcon() == h_blk) return true;
    		if(p.getIcon() == h_blu) return true;
			if(p.getIcon() == h_wht) return true;
    		if(p.getIcon() == h_yel) return true;
	    	return false;
	    } // end of isHighlight
	    
	    //////////////////////////////////////////////////////////////////////
	    // Returns true only if JLabel p is a dotted space
	    public boolean isDot(JLabel p) {
    		if(p.getIcon() == d_grn) return true;
    		if(p.getIcon() == d_red) return true;
			if(p.getIcon() == d_blk) return true;
    		if(p.getIcon() == d_blu) return true;
			if(p.getIcon() == d_wht) return true;
    		if(p.getIcon() == d_yel) return true;
	    	return false;
	    } // end of isDot

	    //////////////////////////////////////////////////////////////////////
	    // returns true if e is 1 move distance from s
	    public boolean inRange(JLabel s, JLabel e) {
	    	return (Math.abs(s.getX() - e.getX()) 
	    		  + Math.abs(s.getY() - e.getY())) < 55;
	    } // end of inRange

	    //////////////////////////////////////////////////////////////////////
	    // returns true if currPlayer matches JLabel
	    public boolean isTurn(JLabel s) {
	    	if (getColorInt(s) >= 0 && getColorInt(s) <= 2 && currPlayer == 0)
	    		return true;
	    	return false;
	    } // end of isTurn

	    //////////////////////////////////////////////////////////////////////
	    // Returns true only if the component passed in is both
	    // a JLabel and also a marble
	    public boolean compIsMarble(Component c) {
			JLabel tmp;

			try { 
				tmp = ((JLabel) c); 
			} catch (ClassCastException e) { 
				return false; 
			}

			if (isMarble(tmp))
				return true;

			return false;			
	    } // end of compIsMarble

	    //////////////////////////////////////////////////////////////////////

 		//////////////////////////////////////////////////////////////////////
	    // returns true if e is 1 move distance from s
	   	public boolean canJump(JLabel s, JLabel e) {

	   		// Calculate the distance between s and e
	      	int total = Math.abs(s.getX() - e.getX()) 
	      			  + Math.abs(s.getY() - e.getY());

	    	int x = s.getX() - e.getX();
	    	int y = s.getY() - e.getY();

	    	// Determine the direction of jump
	    	//////////////////////////////////////////////////////////////////
	    	// direction can be 1, 2, 3, 4, 5 or 6
	    	int direction = 0;

	    	if (x > 0 && y > 0) direction = 1; 			// + +
	    	else if (x < 0 && y > 0) direction = 2;  	// - +
	    	else if (x < 0 && y < 0) direction = 3;  	// - -
			else if (x > 0 && y < 0) direction = 4;  	// + -
	    	else if (x > 0 && y == 0) direction = 5; 	// <--
	    	else if (x < 0 && y == 0) direction = 6; 	// -->
	    	
	    	// The code is confusing here because the pixels are not 
	    	// perfectly aligned as well as I would have liked but 
	    	// I was able to work around the issues in positioning
	    	if(total == 78 || total == 106 || total == 107 || total == 108) {
	    		switch (direction) {
	    			case 1: // + +
	    				if(compIsMarble(getComponentAt(s.getX() - 20,
	    				 s.getY() - 34))
	    				|| compIsMarble(getComponentAt(s.getX() - 19,
	    				 s.getY() - 34)))
	    					if (isBlank(e))
	    						return true;
	    				break;
	    			case 2: // - +
	    				if(compIsMarble(getComponentAt(s.getX() + 20, 
	    					s.getY() - 34))
	    				|| compIsMarble(getComponentAt(s.getX() + 19, 
	    					s.getY() - 34)))
	    					if (isBlank(e))
	    						return true;
	    				break;
	    			case 3: // - -
	    				if(compIsMarble(getComponentAt(s.getX() + 20, 
	    					s.getY() + 34))
	    				|| compIsMarble(getComponentAt(s.getX() + 19, 
	    					s.getY() + 34)))
	    					if (isBlank(e))
	    						return true;
	    				break;
	    			case 4: // + -
	    				if(compIsMarble(getComponentAt(s.getX() - 20, 
	    					s.getY() + 34))
	    				|| compIsMarble(getComponentAt(s.getX() - 19, 
	    					s.getY() + 34)))
	    					if (isBlank(e))
	    						return true;
	    				break;
	    			case 5: // <--
	    				if(compIsMarble(getComponentAt(s.getX() - 39, 
	    					s.getY())))
	    					if (isBlank(e))
	    						return true;
	    				break;
	    			case 6: // -->
	    				if(compIsMarble(getComponentAt(s.getX() + 39, 
	    					s.getY())))
	    					if (isBlank(e))
	    						return true;
	    				break;
	    			default:
	    				return false;
	    		}
	    	}
	    	
	    	return false;
	    } // end of canJump

	    //////////////////////////////////////////////////////////////////////
	    // returns true p has another jump move avaible
	   	public boolean canJumpAgain(JLabel p) {
	    	for (int i = 0; i < gb.length; i++)
	    		if(canJump(p, gb[i]))
	    			return true;
	    	return false;
	    } // end of canJumpAgain

	   	//////////////////////////////////////////////////////////////////////
	    // highlights only the jumpable spaces for JLabel p
	   	public void showJumps(JLabel p) {
	    	for (int i = 0; i < gb.length; i++) {
	    		if(canJump(p, gb[i]) && p != gb[i] && !isMarble(gb[i]))
	    			gb[i].setIcon(getImageIcon(getColorInt(p), 'h'));
	    	}
	    } // end of showJumps

	    //////////////////////////////////////////////////////////////////////
	    // returns an int to match p based on its original or select image
	    public int getColorInt(JLabel p) {
	    	if (p.getIcon() == o_grn || p.getIcon() == s_grn) return 0;
			if (p.getIcon() == o_blu || p.getIcon() == s_blu) return 1;
			if (p.getIcon() == o_blk || p.getIcon() == s_blk) return 2;
	    	if (p.getIcon() == o_red || p.getIcon() == s_red) return 3;
			if (p.getIcon() == o_yel || p.getIcon() == s_yel) return 4;
			if (p.getIcon() == o_wht || p.getIcon() == s_wht) return 5;
	    	if (p.getIcon() == o_blank) return 6;
	    	
	    	return 7;
	    } // end of getColorInt

	    //////////////////////////////////////////////////////////////////////
	    // returns the correct icon for the given index of the game board  
	    public ImageIcon pieceType(int index) {

		    int grn_arr [] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
			int blue_arr [] = {10,11,12,13,23,24,25,35,36,46};
			int black_arr [] = {65,75,76,86,87,88,98,99,100,101};
			int white_arr [] = {19,20,21,22,32,33,34,44,45,55};
			int yellow_arr [] = {74,84,85,95,96,97,107,108,109,110};
		    int red_arr [] = {111, 112, 113, 114, 115, 116, 117, 118, 119, 120};

		    for (int i = 0; i < 10; i++) {
		    	if(grn_arr[i] == index) 
		    		return o_grn;
		    	else if (red_arr[i] == index)
		    		return o_red;
				else if (blue_arr[i] == index)
		    		return o_blu;
				else if (black_arr[i] == index)
		    		return o_blk;
				else if (yellow_arr[i] == index)
		    		return o_yel;
				else if (white_arr[i] == index)
		    		return o_wht;
		    }

		    return o_blank;
	    } // end of pieceType
	    
	    //////////////////////////////////////////////////////////////////////
	    // returns ImageIcon type, color or mode based on passed i and c
	    public ImageIcon getImageIcon(int i, char c) {
	    	
			///////////////////////////////////////
	    	// Highlight
	    	if (c == 'h') {
	    		if (0 == i) return h_grn;
				else if (1 == i) return h_blu;
				else if (2 == i) return h_blk;
		    	else if (3 == i) return h_red;
				else if (4 == i) return h_yel;
				else if (5 == i) return h_wht;
		    }

			///////////////////////////////////////
		    // Dot
			if (c == 'd') {
				if (0 == i) return d_grn;
				else if (1 == i) return d_blu;
				else if (2 == i) return d_blk;
		    	else if (3 == i) return d_red;
				else if (4 == i) return d_yel;
				else if (5 == i) return d_wht;
			}

			///////////////////////////////////////
			// Select
			if (c == 's') {
				if (0 == i) return s_grn;
				else if (1 == i) return s_blu;
				else if (2 == i) return s_blk;
		    	else if (3 == i) return s_red;
				else if (4 == i) return s_yel;
				else if (5 == i) return s_wht;
			}

			///////////////////////////////////////
			// Original
			if (c == 'o') {
				if (0 == i) return o_grn;
				else if (1 == i) return o_blu;
				else if (2 == i) return o_blk;
		    	else if (3 == i) return o_red;
				else if (4 == i) return o_yel;
				else if (5 == i) return o_wht;
		    	else if (6 == i) return o_blank;
			}

			///////////////////////////////////////
			// Mode
			if (c == 'm') {
				if (0 == i) return mode_0;
				else if (2 == i) return mode_1;
				else if (3 == i) return mode_2;
			}

			///////////////////////////////////////

			return null;
	    } // end of getImageIcon

	    //////////////////////////////////////////////////////////////////////
	    // returns the original image for a passed in JLabel if
	    // p is a selected marble
	    public ImageIcon getMarble(JLabel p) {
	    	if (p.getIcon() == s_grn) return o_grn;
	    	if (p.getIcon() == s_red) return o_red;
			if (p.getIcon() == s_blk) return o_blk;
	    	if (p.getIcon() == s_blu) return o_blu;
			if (p.getIcon() == s_yel) return o_yel;
	    	if (p.getIcon() == s_wht) return o_wht;
	    	return null;
	    } // end of getMarble

	    //////////////////////////////////////////////////////////////////////
	    // performs variable changes based on gameMode and currPlayer
	    // also checks if the game is over based on last move
	  	public void turnOver() {

	  		// if the number of players exceeds 3 then the way
	  		// or is determined changes
			currPlayer = nextTurn.get(currPlayer);
	  		updatePlayer(currPlayer);

			if (currPlayer == 0) {//the previous move was from computer
				//map the chinese checker board indecies to GUI board idx
				int computerStart = map2dBoardIdx(computerMove[0],computerMove[1]);
				int computerEnd = map2dBoardIdx(computerMove[2], computerMove[3]);
				//make the computer's move in GUI
				moveMarble(computerStart,computerEnd);
				//check if computer has won 
				if (computerAI.isComputerWinner()){
					gameOver();
					return;
				}
			}  
			
			if (currPlayer == 3){ //the previous move was from human
				//tell chinese checker to move the marble of the human
				computerAI.move(startIdx, endIdx);
				if (computerAI.isPlayerWinner()){
					gameOver();
					return;
				}
				startIdx = -1;
				//give the turn to computer
				computerMove = computerAI.computerTurn();
				//switch turn
				turnOver();
			}
	  	} // end of turnOver

		private void gameOver(){
			gameOver = true;
			winner = currPlayer;
			if (winner == 3){
    			System.out.println("You Win!");
				gameStatus1.setText("You");
				gameStatus1.setForeground(Color.GREEN);
				gameStatus2.setText("win!");
			}
			else{
				System.out.println("Computer wins!");
				gameStatus1.setText("Computer");
				gameStatus1.setForeground(Color.RED);
				gameStatus2.setText("wins!");
			}
			endTurn.setText("Restart");
			endTurn.setVisible(true);
		}

		private void moveMarble(int fromIdx, int toIdx) {
			gb[toIdx].setIcon(getImageIcon(getColorInt(gb[fromIdx]),'o'));
			gb[fromIdx].setIcon(o_blank);
		}

		private int map2dBoardIdx(int i, int j) {
			int idx = 17 * i + j;
			for (int row = 0; row < i; row++) {
				idx -= computerAI.getDashesIn2dBoard()[row][0] + computerAI.getDashesIn2dBoard()[row][1];
			}
			idx -= computerAI.getDashesIn2dBoard()[i][0];
			return idx;
		}

		public int getIndexOf(JLabel p){
			for (int i = 0; i < gb.length; i++) {
				if (gb[i] == p)
					return i;
			}
			return -1;
		}
	  	//////////////////////////////////////////////////////////////////////
	    // paints the background image on the Board JPanel
	    @Override
		protected void paintComponent(Graphics g) {
    		super.paintComponent(g);
    		// Paints the background
        	g.drawImage(bkg.getImage(), 0, 0, null);
		}

	  	//////////////////////////////////////////////////////////////////////
	    // sets the size of the JPanel to match the size of board 
	    @Override
        public Dimension getPreferredSize() {
            return new Dimension(BOARD_SIZE, BOARD_SIZE);
        }

	    //////////////////////////////////////////////////////////////////////
	    // Mouse listener for board elements 
		class BoardMouseHandler implements MouseListener {

		 	private JLabel start;
		 	private JLabel end;
		 	private JLabel firstMoved;

		 	private boolean hasJumped = false;
			private boolean hasMoved = false;

	        // MOUSE PRESSED ON PIECE TO MOVE
	        public void mousePressed( MouseEvent event ) {
	        	if (event.getSource() != endTurn) {
		        	start = (JLabel) event.getSource();
		        	
		        	if (!gameOver && gameStarted && isTurn(start) 
		        		&& !isBlank(start) 
		        		&& isMarble((JLabel) event.getSource())
		        		&& firstMoved == null) {
	        		
	        			start.setIcon(getImageIcon(getColorInt(start), 's'));
	        			showMoves(start);
		        	}
		        	
		        	if (!gameOver && gameStarted && isTurn(start) 
		        		&& !isBlank(start) 
		        		&& isMarble((JLabel) event.getSource())
		        		&& firstMoved == start) {

		        		start.setIcon(getImageIcon(getColorInt(start), 's'));
			        	showJumps(start);
		        	}

					//save the startIdx
					if (startIdx == -1)
						startIdx = getIndexOf(start);
				}

	        	if (event.getSource() == endTurn) {
					if (!gameOver){
						hasJumped = false;
						hasMoved = false;
						firstMoved = null;
						endTurn.setVisible(false);
						turnOver();
					}
					else {
						endTurn.setVisible(false);
						endTurn.setText("end turn");
						gameStatus1.setVisible(false);
						gameStatus2.setVisible(false);
						gameStatus2.setText("Turn");
						resetBoard();
					}
				}

	        } 

	        // ENDING MOUSE RELEASE 
	        public void mouseReleased( MouseEvent event ) {
	        	boolean moveMade = false;

	        	try{
		        	//MOVES 
		        	if (isDot(end)) {
		        		end.setIcon(getMarble(start));
		        		moveMade = true;
						hasMoved = true;
		        		if (!inRange(start,end)) {
		        			hasJumped = true;
		        		}
		        		firstMoved = end;
						//save the endIdx
						endIdx = getIndexOf(end);
		        	}
		        } catch (NullPointerException e) { moveMade = false; }

	        	hideMoves(start);	        	

				if(!hasMoved){
					startIdx = -1;
				}
	        	// MOVE if labels are not the same, 
	        	// is blankspace and move is in range
	        	if (!moveMade) { // IF NO MOVE MADE RESET START TO ORIGINAL
	        		start.setIcon(getImageIcon(getColorInt(start), 'o'));
	        	} else { // ELSE SET START TO BLANK
	        		// if jump still available 
	        		if (canJumpAgain(end) && hasJumped) {
	        			hasJumped = true;

	        			start.setIcon(o_blank); // blank image
						endTurn.setVisible(true);

	        			start = end;
	        			end = null;
	        		} else {
	        			hasJumped = false;
						hasMoved = false;
	        			firstMoved = null;
		        		start.setIcon(o_blank); // blank image
						endTurn.setVisible(false);
		        		turnOver();
	        		}
	        	}
	        }

	        // SETS THE END POINT 
	        public void mouseEntered( MouseEvent event ) {
	        	if (event.getSource() != endTurn) {
					try {
						end = (JLabel) event.getSource();
						if (isHighlight((JLabel) event.getSource())) {
							end = (JLabel) event.getSource();
							end.setIcon(getImageIcon(currPlayer, 'd'));
						}
					} catch (Exception e) {
						
					}
		        }		
	        } 

	        // handle event when mouse exits area
	        public void mouseExited( MouseEvent event ) {
	        	if (event.getSource() != endTurn) {
					try {
						JLabel tmp = (JLabel) event.getSource();
						if (isDot(tmp))
							end.setIcon(getImageIcon(currPlayer, 'h'));
					} catch (Exception e) {
						// TODO: handle exception
					}
		        }
	        } 
	        
	        public void mouseClicked( MouseEvent event ) { }  

	    } // end of BoardMouseHandler class


	    //////////////////////////////////////////////////////////////////////
	    // Mouse listener for game option buttons
	    public class OptionMouseHandler implements MouseListener {

			public void mousePressed(MouseEvent e) {
				if (e.getSource() == modes[0]) {
					gameMode = 0;
					computerAI.setLevel(1);
					setTurnOrder(gameMode);
					gameStarted = true;
					hideOptions();
				}
				if (e.getSource() == modes[1]) {
					gameMode = 1;
					computerAI.setLevel(3);
					setTurnOrder(gameMode);
					gameStarted = true;
					hideOptions();
				}
				if (e.getSource() == modes[2]) {
					gameMode = 2;
					computerAI.setLevel(5);
					setTurnOrder(gameMode);
					gameStarted = true;
					hideOptions();
				}

			}

			public void mouseReleased(MouseEvent e) { }

			public void mouseEntered(MouseEvent e) {


					
				repaint();
			}

			public void mouseExited(MouseEvent e) {
				bkg = boardBKG;
				repaint();
			}

			public void mouseClicked(MouseEvent e) {

			}
		} // end of OptionMouseHandler class
	} // end of Board class
} // end of ChineseCheckers class

