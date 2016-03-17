import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.EmptyStackException;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Screen extends JPanel implements KeyListener, ActionListener {
	
	private static final long serialVersionUID = 1L;
	private final static int CELL_SIZE = 25;
	
	//Dimensões do labirinto
	private int width;
	private int height;
	
	private boolean[][] lab;
	
	// Sprites para os personagens do labirinto (quadrado verde, quadrado vermelho e quadrado roxo) 
	private Image imageEnd;
	private int endX;
	private int endY;
	
	private Image imageMe;
	private int meX;
	private int meY;
	
	private Image imagePc;
	private int pcX;
	private int pcY;
	
	// Pilha que será usada para guardar os Crumbs criados
	Stack<Crumb> stack = new Stack<>();
	
	//Clock para atualizar o script
	private int speed = 30;
	Timer timer = new Timer(speed, this);
	
	//Ler arquivo labyrinth.txt
	FileHandler fileHandler = new FileHandler();
	boolean[][] c = fileHandler.getC();
	
	public Screen(boolean[][] labyrinth) {
		this.c = labyrinth;
		this.width = this.c[0].length;
		this.height = this.c.length;
		
		this.lab = new boolean[this.height][this.width];
		for(int i=0; i < this.height; i++) {
			  for(int j=0; j < this.width; j++) {
				  this.lab[i][j]=this.c[i][j];
			  }
		}
		
		stack.push(new Crumb(0,0));
		
		setPreferredSize(new Dimension(this.width * CELL_SIZE, this.height * CELL_SIZE));
		
		endX = this.width * CELL_SIZE - CELL_SIZE;
		endY = this.height * CELL_SIZE - CELL_SIZE;
		meX  = 0;
		meY  = 0;
		pcX  = 0;
		pcY  = 0;
		
		imageMe = new ImageIcon(getClass().getResource("/img/me.png")).getImage();
		imagePc = new ImageIcon(getClass().getResource("/img/pc.png")).getImage();
		imageEnd = new ImageIcon(getClass().getResource("/img/end.png")).getImage();
		
		
		lab[pcY/CELL_SIZE][pcX/CELL_SIZE] = false;
		timer.start();
		
	}

	public void paintComponent(Graphics g) {
		for(int i = 0; i < this.height; i++) {
			int y = i * CELL_SIZE;

			for(int j = 0; j < this.width; j++) {
				int x = j * CELL_SIZE;

				if(!lab[i][j] && c[i][j]) {
					g.setColor(Color.ORANGE);
				}
				
				else if(c[i][j]) {
					g.setColor(Color.WHITE);
				}
				else {
					g.setColor(Color.BLACK);
				}

				g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
				g.drawImage(imageEnd, endX, endY, CELL_SIZE, CELL_SIZE, null);
				g.drawImage(imagePc, pcX, pcY, CELL_SIZE, CELL_SIZE, null);
				g.drawImage(imageMe, meX, meY, CELL_SIZE, CELL_SIZE, null);
			}
		}

    	getToolkit().sync();
    }
	
	@Override
	public void keyPressed(KeyEvent e) {
    	int key = e.getKeyCode();
    	
    	int x = meX / CELL_SIZE;
    	int y = meY / CELL_SIZE;

    	// Se a tecla apertada foi a seta para a esquerda...
    	if(key == KeyEvent.VK_LEFT) {
    		if(meX != 0 && c[y][x-1]) {
    			meX -= CELL_SIZE;
    			repaint();
    		}
    	}

    	// Se a tecla apertada foi a seta para a direita...
    	if(key == KeyEvent.VK_RIGHT) {
    		if(meX != (this.width - 1) && c[y][x+1]) {
    			meX += CELL_SIZE;
    			repaint();
    		}
    	}
    	
    	// Se a tecla apertada foi a seta para cima...
    	if(key == KeyEvent.VK_UP) {
    		if(meY != 0 && c[y-1][x]) {
    			meY -= CELL_SIZE;
    			repaint();
    		}
    	}
    	
    	// Se a tecla apertada foi a seta para baixo...
    	if(key == KeyEvent.VK_DOWN) {
    		if(meY != (this.height - 1) && c[y+1][x]) {
    			meY += CELL_SIZE;
    			repaint();
    		}
    	}
	}

    @Override
	public void keyReleased(KeyEvent event) {
    	
	}

	@Override
	public void keyTyped(KeyEvent event) {
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		int x = pcX / CELL_SIZE;
		int y = pcY / CELL_SIZE;
		try {
			if(pcX == endX && pcY == endY){
				System.out.println("Perdestes!");
				timer.stop();
			} else if(meX == endX && meY == endY) {
				System.out.println("Ganhastes!");
				timer.stop();
			}
			Crumb crumb = stack.peek();
					
			if(crumb.getPasses() == 0) {
				//Verifica casa acima do bot
				if(y > 0 && lab[y-1][x]) {
					lab[y][x] = false;
					y--;
					stack.push(new Crumb(x, y));
					repaint();
				} 
				crumb.incrementPasses();
			}
					
			else if(crumb.getPasses() == 1) {
				//Verifica casa à direita do bot
				if(x < (width - 1) && lab[y][x+1]) {
					lab[y][x] = false;
					x++;
					stack.push(new Crumb(x, y));
					repaint();
				}
				crumb.incrementPasses();
			}		
					
			else if(crumb.getPasses() == 2) {
				//Verifica casa abaixo do bot
				if(y < (height - 1) && lab[y+1][x]) {
					lab[y][x] = false;
					y++;
					stack.push(new Crumb(x, y));
					repaint();
				}
				crumb.incrementPasses();
			}
				
			else if(crumb.getPasses() == 3) {
				//Verifica casa à esquerda do bot
				if(x > 0 && lab[y][x-1]) {
					lab[y][x] = false;
					x--;
					stack.push(new Crumb(x, y));
					repaint();
				}
				crumb.incrementPasses();
			}
					
			else if(crumb.getPasses() == 4) {
				//Retira Crumb da lista
				lab[y][x] = false;
				stack.pop();
				x = stack.peek().getX();
				y = stack.peek().getY();
				repaint();
			}
			
			//Converter coordenadas para a escala da interface
			pcY = y * CELL_SIZE;
			pcX = x * CELL_SIZE;
			
		} catch(EmptyStackException e) {
			System.out.println("Acabou!");
		}
	}
}