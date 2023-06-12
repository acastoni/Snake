import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Main {
	public static void main(String[] args) {
		// Create a new game window
		new GameWindow();
	}
}

class GameWindow extends JFrame {

	// Initialize the game board
	public GameWindow() {
		add(new GameBoard());
		setResizable(false);
		pack();

		setTitle("Snake Game");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 setVisible(true);
	}
}

class GameBoard extends JPanel implements ActionListener {

	// Game variables
	private final int size = 720;
	private final int unit_size = 20;
	private final int game_units = (size*size) / (unit_size*unit_size);
	private final int delay = 75;
	private final int[] x = new int[game_units];
	private final int[] y = new int[game_units];
	private int body_parts = 6;
	private int apples_eaten;
	private int apple_x;
	private int apple_y;
	private char direction = 'R';
	private boolean running = false;
	private Timer timer;

	// Constructor
	public GameBoard() {
		setPreferredSize(new Dimension(size, size));
		setFocusable(true);
		setBackground(Color.BLACK);
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					if(direction != 'R') direction = 'L';
					break;
				case KeyEvent.VK_RIGHT:
					if(direction != 'L') direction = 'R';
					break;
				case KeyEvent.VK_UP:
					if(direction != 'D') direction = 'U';
					break;
				case KeyEvent.VK_DOWN:
					if(direction != 'U') direction = 'D';
					break;
				}
			}
		});
		startGame();
	}

	public void newApple() {
	    // Create new apple at a random location
	    apple_x = (int) (Math.random() * (size / unit_size)) * unit_size;
	    apple_y = (int) (Math.random() * (size / unit_size)) * unit_size;
	}
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(delay, this);
		timer.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
		if(running) {
			// Draw apple
			g.setColor(Color.RED);
			g.fillOval(apple_x, apple_y, unit_size, unit_size);

			// Draw snake
			for(int i = 0; i < body_parts; i++) {
				if(i == 0) {
					g.setColor(Color.MAGENTA);
					g.fillRect(x[i], y[i], unit_size, unit_size);
				}
				else {
					g.setColor(new Color(138, 43, 226));
					g.fillRect(x[i], y[i], unit_size, unit_size);
				}
			}

			// Draw score
			g.setColor(Color.RED);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			g.drawString("Score: " + apples_eaten, size/2, g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
	}

	public void move() {
		for(int i = body_parts; i > 0; i--) {
			x[i] = x[(i - 1)];
			y[i] = y[(i - 1)];
		}

		switch(direction) {
		case 'U':
			y[0] = y[0] - unit_size;
			break;
		case 'D':
			y[0] = y[0] + unit_size;
			break;
		case 'L':
			x[0] = x[0] - unit_size;
			break;
		case 'R':
			x[0] = x[0] + unit_size;
			break;
		}
	}

	public void checkApple() {
		if((x[0] == apple_x) && (y[0] == apple_y)) {
			body_parts++;
			apples_eaten++;
			newApple();
		}
	}

	public void checkCollisions() {
		// Check if head collides with body
		for(int i = body_parts; i > 0; i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}

		// Check if head touches left border
		if(x[0] < 0) running = false;

		// Check if head touches right border
		if(x[0] > size) running = false;

		// Check if head touches top border
		if(y[0] < 0) running = false;

		// Check if head touches bottom border
		if(y[0] > size) running = false;

		// Stop timer
		if(!running) timer.stop();
	}

	public void gameOver(Graphics g) {
		// Game over text
		g.setColor(Color.RED);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		g.drawString("Game Over", size/2, size/2);

		// Display score
		g.setFont(new Font("Ink Free", Font.BOLD, 20));
		g.drawString("Apples eaten: " + apples_eaten, size/2, size/2 + 30);
	}

	@Override
	
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
}