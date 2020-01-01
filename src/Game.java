import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.Timer;

public class Game extends JFrame implements KeyListener, ActionListener {
		// Set game screen
		final int SCREEN_WIDTH = 400;
		final int SCREEN_HEIGHT = 400;
		
		// ball init position
		final int INIT_Y_POS = SCREEN_HEIGHT / 2;
		final int INIT_X_POS = SCREEN_WIDTH / 2;

		// Set pad size
		final int PAD_WIDTH = 20;
		final int PAD_HEIGHT = 100;
		final int PLAYER_NUM = 2;
		final int PAD_OFFSET = 10;

		// Reset ball timer
		Timer ballTimer;
		
		// ball speed
		int ballSpeedX = 1, ballSpeedY = 1;
		final int DELAY_MS = 10;
		
		// ball position
		int ballPosX = INIT_X_POS;
		int ballPosY = INIT_Y_POS;
		
		// ball size
		final int BALL_RADIUS = 20;

		// player speed
		int playerSpeedY = 20;
		
		// player pad position
		int[] playerPosX = new int[PLAYER_NUM];
		int[] playerPosY = new int[PLAYER_NUM];
		
		// playerScore
		int[] playerScore = new int[PLAYER_NUM];

	public Game() {
        setTitle("¹CÀ¸°òÂ¦-¥â¥ã²y");
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        addKeyListener(this);

        initPlayerPos();
        initPlayerScore();
        initBallTimer();
	}
	
	private void initPlayerPos() {
        for(int i = 0; i < PLAYER_NUM; i++) {
        	playerPosY[i] = INIT_Y_POS;
        }
        playerPosX[0] = PAD_WIDTH - PAD_OFFSET;
        playerPosX[1] = SCREEN_WIDTH - PAD_WIDTH - PAD_OFFSET;
	}

	private void initPlayerScore() {
        for(int i = 0; i < PLAYER_NUM; i++) {
        	playerScore[i] = 0;
        }
	}
	
	private void initBallTimer() {
        ballTimer = new Timer(DELAY_MS, this);
        ballTimer.setInitialDelay(190);
        ballTimer.start();
	}
	
    public void update(Graphics g) { 
        this.paint(g); 
    } 
 
    public void paint(Graphics g) { 
        super.paint(g);

        drawPlayerPad(g);
        drawBall(g);
        drawScore(g);
    }

    private void drawPlayerPad(Graphics g) {
    	g.setColor(Color.GREEN);
        g.fillRect(playerPosX[0], playerPosY[0], PAD_WIDTH, PAD_HEIGHT);
        g.setColor(Color.BLUE);
        g.fillRect(playerPosX[1], playerPosY[1], PAD_WIDTH, PAD_HEIGHT);
    }
    
    private void drawBall(Graphics g) {
    	g.setColor(Color.CYAN);
    	g.fillOval(ballPosX, ballPosY, BALL_RADIUS, BALL_RADIUS);
    }

    private void drawScore(Graphics g) {
    	g.setColor(Color.BLACK);
    	g.drawString("P1:" + playerScore[0], playerPosX[0], 50);
    	g.drawString("P2:" + playerScore[1], playerPosX[1]-50, 50);
    }

	public static void main(String[] args) {
		new Game().show();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		// Move the player on the left
		if( key == KeyEvent.VK_UP )
			playerPosY[0] -= playerSpeedY;
		
		if( key == KeyEvent.VK_DOWN )
			playerPosY[0] += playerSpeedY;

		// Move the player on the right
		if( key == KeyEvent.VK_W )
			playerPosY[1] -= playerSpeedY;
		
		if( key == KeyEvent.VK_X )
			playerPosY[1] += playerSpeedY;

		checkPadPosRange();
		repaint();
	}

	// checkPadPosRange
	private void checkPadPosRange() {
		for(int i = 0; i < PLAYER_NUM; i++) {
			if( playerPosY[i] < 0)	playerPosY[i] = 0;
			if( playerPosY[i] > SCREEN_HEIGHT - PAD_HEIGHT) playerPosY[i] = SCREEN_HEIGHT - PAD_HEIGHT;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ballPosX += ballSpeedX;
		ballPosY += ballSpeedY;
		
		// whether ball touch boundary
    	if( ballPosX >= SCREEN_WIDTH - BALL_RADIUS || ballPosX <= 0 )
    	{
    		ballSpeedX = -ballSpeedX;
    		
    		if( ballPosX <= 0 ) {
    			playerScore[1]++;
    		} else {
    			playerScore[0]++;
    		}
    	}
    	
    	if( ballPosY >= SCREEN_HEIGHT - BALL_RADIUS || ballPosY <= BALL_RADIUS) ballSpeedY = -ballSpeedY;

    	// whether ball touch left pad
    	if( ballPosX <= playerPosX[0] + PAD_WIDTH && ballPosX >= playerPosX[0] &&
    		ballPosY <= playerPosY[0] + PAD_HEIGHT && ballPosY >= playerPosY[0] )
    		ballSpeedX = -ballSpeedX;
    	
    	// whether ball touch right pad
    	if( ballPosX <= playerPosX[1] - BALL_RADIUS + PAD_WIDTH && ballPosX >= playerPosX[1] - BALL_RADIUS &&
    		ballPosY <= playerPosY[1] + PAD_HEIGHT && ballPosY >= playerPosY[1] )
    		ballSpeedX = -ballSpeedX;

    	this.repaint();
	}
}