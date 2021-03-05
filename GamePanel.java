import java.awt.*;
import java.awt.event.*;
import java.security.PublicKey;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {
	
	static final int GAME_WIDTH = 1000;
	static final int GAME_HEIGHT = (int)(GAME_WIDTH * (0.5555));
	static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH,GAME_HEIGHT);
	static final int BALLS_DIAMETER = 20;
	static final int PADDLE_WIDTH = 25;
	static final int PADDLE_HEIGHT = 100;
	Thread gameThread;
	Image image;
	Graphics graphics;
	Random random;
	Paddle paddle1;
	Paddle paddle2;
	Balls ball;
	Score score; 
	
	GamePanel() {
		newPaddles();
		newBall();
		score = new Score(GAME_WIDTH,GAME_HEIGHT);
		this.setFocusable(true);
		this.addKeyListener(new AL());
		this.setPreferredSize(SCREEN_SIZE);
		
		gameThread = new Thread(this);
		gameThread.start();
		
	}
	public void newBall() {
		random = new Random();
		ball = new Balls((GAME_WIDTH/2)-(BALLS_DIAMETER/2),random.nextInt(GAME_HEIGHT-BALLS_DIAMETER),BALLS_DIAMETER,BALLS_DIAMETER);
	
	}
	
	public void newPaddles() {
		paddle1 = new Paddle(0,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,1);
		paddle2 = new Paddle(GAME_WIDTH-PADDLE_WIDTH,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,2);
	
	}
	
	public void paint(Graphics g) {
		 image = createImage(getWidth(),getHeight());
		 graphics = image.getGraphics();
		 draw(graphics);
		 g.drawImage(image,0,0,this);
		 
	}
	public void draw(Graphics g) {
		paddle1.draw(g);
		paddle2.draw(g);
		ball.draw(g);
		score.draw(g);
	
	}
	public void move() {
		paddle1.move();
		paddle2.move();
		ball.move();
		
	}
	public void checkCollision() {
		// bounce the ball off top and bottom window edges
		
		if(ball.y <= 0)
			ball.setYDirection(-ball.yVelocity);
		if(ball.y >= GAME_HEIGHT-BALLS_DIAMETER)
			ball.setYDirection(-ball.yVelocity);
		
		// bounce ball off paddles 
		
		// paddle 1
		if(ball.intersects(paddle1)) {
			ball.xVelocity = Math.abs(ball.xVelocity);
			ball.xVelocity++; // for more difficulty xball_speed_++
			if(ball.yVelocity>0)
				ball.yVelocity++; //for more difficulty yball_speed_++
			else
				ball.yVelocity--;
			ball.setXDirection(ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
			
		}
		
		// paddle 2 
		if(ball.intersects(paddle2)) {
			ball.xVelocity = Math.abs(ball.xVelocity);
			ball.xVelocity++; // for more difficulty xball_speed_++
			if(ball.yVelocity>0)
				ball.yVelocity++; //for more difficulty yball_speed_++
			else
				ball.yVelocity--;
			ball.setXDirection(-ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
			
		}
		
		// check if paddles still in game window
		//paddle 1 (x,y) testing
		
		if(paddle1.y <= 0)
			paddle1.y = 0;
		if(paddle1.y >= GAME_HEIGHT-PADDLE_HEIGHT)
			paddle1.y = GAME_HEIGHT-PADDLE_HEIGHT;
		
		if(paddle1.x <= 0)
			paddle1.x = 0;
		if(paddle1.x >= (GAME_WIDTH / 2)-PADDLE_WIDTH*2)
			paddle1.x = (GAME_WIDTH / 2)-PADDLE_WIDTH*2;
		
		//paddle 2 (x,y) testing
		if(paddle2.y <= 0)
			paddle2.y = 0;
		if(paddle2.y >= GAME_HEIGHT-PADDLE_HEIGHT)
			paddle2.y = GAME_HEIGHT-PADDLE_HEIGHT;
		
		if(paddle2.x <= (GAME_WIDTH / 2)+PADDLE_WIDTH)
			paddle2.x = (GAME_WIDTH / 2)+PADDLE_WIDTH;
		if(paddle2.x >= GAME_WIDTH-PADDLE_WIDTH)
			paddle2.x = GAME_WIDTH-PADDLE_WIDTH;
		
		
		// give a player +1 point and create a ball and paddles
		if(ball.x <= 0) {
			score.player2++;
			newPaddles();
			newBall();
			//System.out.println("Player 2 : "+score.player2);
		}
		if(ball.x >= GAME_WIDTH-BALLS_DIAMETER) {
			score.player1++;
			newPaddles();
			newBall();
			//System.out.println("Player 1 : "+score.player1);
		}
		
		
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000/amountOfTicks;
		double delta = 0;
		while(true) {
			long now = System.nanoTime();
			delta += (now -lastTime)/ns;
			lastTime = now;
			if (delta >= 1) {
				move();
				checkCollision();
				repaint();
				delta--;
				System.out.println("game is running ; )");
			}			
		}	
	}
	
	public class AL extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			paddle1.keyPressed(e);
			paddle2.keyPressed(e);
			
		}
		public void keyReleased(KeyEvent e) {
			paddle1.keyReleased(e);
			paddle2.keyReleased(e);
				
		}
	}	
}
