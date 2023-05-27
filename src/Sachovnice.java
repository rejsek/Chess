import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.Timer;
import java.util.List;
import java.util.ArrayList;
import java.util.TimerTask;

record Pair<A, B>(A first, B second) {}
/**
 * Trida zabyvajici se vytvorenim sachovnice a veskere logiky podle pravidel sachu
 *
 * @author Daniel Riess
 */
public class Sachovnice extends JPanel{
	/**
	 * Objekt tridy AffineTransform
	 */
	private AffineTransform transform;

	/**
	 * Pole reprezentujici jednotlive figurky a jejich pozice
	 */
	private IPiece [][] pieces = new IPiece[8][8];

	/**
	 * Pole reprezentujici jednotlice ctverecky
	 */
	private Square[][] board = new Square[8][8];

	private int originalX;
	private int originalY;

	/**
	 *
	 */
	private int mensi;

	/**
	 * Zvolena figurka
	 */
	private IPiece currentPiece;

	/**
	 * Pesci, kteri maji nastaveno, ze lze s nimi enPassant
	 */
	private Pawn[] enPassantablePawns;

	/**
	 * Barva pro cerne figurky
	 */
	private Color blackPiece = Color.BLACK;

	/**
	 * Barva pro bile figurky
	 */
	private Color whitePiece = Color.WHITE;

	/**
	 * Cerny na tahu
	 */
	private boolean blackTurn = false;

	/**
	 * Bily na tahu
	 */
	private boolean whiteTurn = true;

	/**
	 * Konstruktor
	 */
	public Sachovnice(){
		for(int x = 0; x < 8; x++){
			for(int y = 0; y < 8; y++){
				board[x][y] = new Square(x, y, null);
			}
		}

		setPieces();
		addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				try {
					selectedSquare(e);
				} catch (NoninvertibleTransformException ex) {
					ex.printStackTrace();
				}
				validMove();
				repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				releasedSquare(e);
				isOnEnd(e);
				repaint();
			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}
		});
	}

	/**
	 * Metoda pro veskera vykresleni
	 * @param g  the <code>Graphics</code> context in which to paint
	 */
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;

		mensi = Math.min(getWidth(),getHeight());
		int posun = (getWidth() - getHeight())/2;

		g2d.translate(Math.max(posun,0),Math.max(-posun,0));
		g2d.scale(mensi/8.,mensi/8.);
		transform = g2d.getTransform();

		drawSachovnice(g2d);
		drawPieces(g2d);
	}

	/**
	 * Metoda pro vykresleni figurek na sachovnici
	 * @param g2d		Objekt tridy Graphics2D
	 */
	private void drawPieces(Graphics2D g2d) {
		for(int x = 0; x < 8; x ++) {
			for(int y = 0; y < 8; y ++) {
				if(pieces[x][y] != null) {
					pieces[x][y].show(g2d);
				}
			}
		}
	}

	/**
	 * Metoda pro vykresleni sachovnice
	 * @param g2d		Objekt tridy Graphics2D
	 */
	public void drawSachovnice(Graphics2D g2d) {
		for(int x = 0; x < 8; x++){
			for(int y = 0; y < 8; y++){
				board[x][y].setColor(((x+y)%2)==1?new Color(86, 140, 150):new Color(238,238,210)); //Zelena -> 118,150,86
				board[x][y].showRectangle(g2d);
			}
		}
	}

	/**
	 * Metoda nastavi pocatecni pozici na sachovnici jednotlivym figurkam
	 */
	public void setPieces() {
		int index = 0;

		/*-------Cerne figurky-------*/
		for(int i = 0; i < 8; i++)
			pieces[i][1] = new Pawn(i,1, blackPiece);

		for(int i = 0; i < 2; i ++) {
			pieces[index][0] = new Tower(index, 0, blackPiece);

			index += 7;
		}

		index = 1;
		for(int i = 0; i < 2; i ++) {
			pieces[index][0] = new Horse(index, 0, blackPiece);

			index += 5;
		}

		index = 2;
		for(int i = 0; i < 2; i ++) {
			pieces[index][0] = new Bishop(index, 0, blackPiece);

			index += 3;
		}

		pieces[3][0] = new King(3, 0, blackPiece);

		pieces[4][0] = new Queen(4, 0, blackPiece);
		/*-------Cerne figurky-------*/


		/*-------Bile figurky-------*/
		for(int i = 0; i < 8; i++) {
			pieces[i][6] = new Pawn(i, 6, whitePiece);
		}

		index = 0;
		for(int i = 0; i < 2; i ++) {
			pieces[index][7] = new Tower(index, 7, whitePiece);

			index += 7;
		}

		index = 1;
		for(int i = 0; i < 2; i ++) {
			pieces[index][7] = new Horse(index, 7, whitePiece);

			index += 5;
		}

		index = 2;
		for(int i = 0; i < 2; i ++) {
			pieces[index][7] = new Bishop(index, 7, whitePiece);

			index += 3;
		}

		pieces[3][7] = new King(3, 7, whitePiece);

		pieces[4][7] = new Queen(4, 7, whitePiece);
		/*-------Bile figurky-------*/
	}

	/**
	 * Metoda zjisti jaky byl zvoleny ctverecek
	 * @param e
	 * @throws NoninvertibleTransformException
	 */
	private void selectedSquare(MouseEvent e) throws NoninvertibleTransformException {
		for(int x = 0; x < 8; x ++) {
			for(int y = 0; y < 8; y ++) {
				int squareSize = Math.min(getWidth()/8,getHeight()/8);
				int move = (getWidth()-getHeight())/2;
				int row = (e.getX() - Math.max(move,0)) / squareSize;
				int column = (e.getY() - Math.max(-move,0)) / squareSize;

				if(board[x][y].contains(row, column) && pieces[x][y] != null) {
					currentPiece = pieces[x][y];
					originalX = x;
					originalY = y;

					board[x][y].setActive(true);
				}
			}
		}
	}

	/**
	 * Metoda zjisti na kterem ctverecku na sachovnici byla pustena mys
	 * @param e
	 */
	private void releasedSquare(MouseEvent e) {
		for(int x = 0; x < 8; x ++) {
			for(int y = 0; y < 8; y ++) {
				int squareSize = Math.min(getWidth()/8,getHeight()/8);
				int move = (getWidth()-getHeight())/2;
				int row = (e.getX() - Math.max(move,0)) / squareSize;
				int column = (e.getY() -  Math.max(-move,0)) / squareSize;

				if(board[x][y].contains(row, column) && currentPiece != null) {
					if(board[x][y].getColor().equals(new Color(217, 70, 70, 171)) ) {
						int xPosition = (int)currentPiece.getxPosition();
						int yPosition = (int)currentPiece.getyPosition();
						int newX = x;
						int newY = y;

						if(xPosition != x || yPosition != y) {
							pieces[(int) currentPiece.getxPosition()][(int) currentPiece.getyPosition()] = null;

							currentPiece.setxPosition(x);
							currentPiece.setyPosition(y);

							IPiece original = pieces[x][y];
							pieces[x][y] = currentPiece;
							
							isGameOver();

							if(currentPiece.getColor().equals(blackPiece)) {
								blackTurn = false;
								whiteTurn = true;
							} else {
								blackTurn = true;
								whiteTurn = false;
							}

							if(isChecked(currentPiece.getColor())) {
								System.out.println("Bacha, kral je v ohrozeni");

								IPiece movedPiece = currentPiece;
								pieces[x][y] = null;
								movedPiece.setxPosition(originalX);
								movedPiece.setyPosition(originalY);

								pieces[originalX][originalY] = movedPiece;
								pieces[x][y] = original;

								if(currentPiece.getColor().equals(blackPiece)) {
									blackTurn = true;
									whiteTurn = false;
								} else {
									blackTurn = false;
									whiteTurn = true;
								}
							} else {
								enPassant();

								castling();

								animation(xPosition, yPosition, newX, newY);

								if (currentPiece instanceof King) {
									((King) currentPiece).setFirstStep(false);
								} else if (currentPiece instanceof Tower) {
									((Tower) currentPiece).setFirstStep(false);
								}

								if (currentPiece instanceof Pawn) {
									removePawnEnPassant(x, y);

									((Pawn) currentPiece).setFirstWalk(false);
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Metoda zjisti, zda se jedna o konec hry a zahlasi prislusny vysledek
	 */
	private void isGameOver() {
		if(whiteTurn) {
			if(!isThereAnyMove(blackPiece)) {
				if(isChecked(blackPiece)) {
					showResult("Hra skončila matem a vyhrál bílý");
				} else {
					showResult("Hra skončila patem a vyhrál bílý");
				}
			}
		} else {
			if(!isThereAnyMove(whitePiece)) {
				if(isChecked(whitePiece)) {
					showResult("Hra skončila matem a vyhrál černý");
				} else {
					showResult("Hra skončila patem a vyhrál černý");
				}
			}
		}
	}

	/**
	 * Metoda zobrazi vyherni okno s vysledkem hry
	 * @param message		text, ktery ma byt zobrazen
	 */
	private void showResult(String message) {
		String[] buttons = {"Restartovat hru", "Odejít ze hry"};

		int selectedButton = JOptionPane.showOptionDialog(null, message, "", JOptionPane.WARNING_MESSAGE, 0, null, buttons, buttons[0]);

		switch(selectedButton) {
			case 0:
				restart();
				break;

			case 1:
				System.exit(0);
				break;
		}
	}

	/**
	 * Metoda zjisti, zda se pro danou situaci nachazi nejaky validni tah
	 * @param color		barva, pro kterou se ma situace vyhodnotit
	 * @return			true, pokud se vyskytuje nejaky validni tah
	 * 					false, pokud se nevyskytuje nejaky validni tah
	 */
	private boolean isThereAnyMove(Color color) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (pieces[i][j] != null && pieces[i][j].getColor().equals(color)) {
					pieces[i][j].movement(board, pieces);
					List<Pair<Integer, Integer>> availableMoves = new ArrayList<>();
					for (int x = 0; x < 8; x++) {
						for (int y = 0; y < 8; y++) {
							if (board[x][y].getActive()) {
								availableMoves.add(new Pair<>(x, y));
							}
						}
					}

					for (Pair<Integer, Integer> move : availableMoves) {
						int x = move.first();
						int y = move.second();
						IPiece originalPiece = pieces[x][y];
						IPiece piece = pieces[i][j];

						// Make the move
						pieces[x][y] = piece;
						piece.setxPosition(x);
						piece.setyPosition(y);
						pieces[i][j] = null;

						// Check if the move removes the check
						if (!isChecked(color)) {
							// The move is valid
							undoMove(i, j, x, y, originalPiece);
							return true;
						}

						// Undo the move
						undoMove(i, j, x, y, originalPiece);
					}
				}
			}
		}

		return false;
	}

	private void undoMove(int i, int j, int x, int y, IPiece originalPiece) {
		IPiece piece = pieces[x][y];
		piece.setxPosition(i);
		piece.setyPosition(j);
		pieces[i][j] = piece;
		pieces[x][y] = originalPiece;
		if (originalPiece != null) {
			originalPiece.setxPosition(x);
			originalPiece.setyPosition(y);
		}
		board[x][y].setActive(false);
	}

	/**
	 * Metoda zjisti, zda je kral ohrozen
	 * @return		true, pokud je kral v ohrozeni
	 * 				false, pokud kral neni v ohrozeni
	 */
	private boolean isChecked(Color color) {
		boolean isChecked = false;

		for(int x = 0; x < 8; x ++) {
			for(int y = 0; y < 8; y ++) {
				if(pieces[x][y] != null && !pieces[x][y].getColor().equals(color)) {
					pieces[x][y].movement(board, pieces);
				}
			}
		}
		outer:
		for(int x = 0; x < 8; x ++) {
			for(int y = 0; y < 8; y ++) {
				if(pieces[x][y] != null && pieces[x][y] instanceof King && pieces[x][y].getColor().equals(color) && board[x][y].getActive()) {
					isChecked = true;
					break outer;
				}
			}
		}

		for(int x = 0; x < 8; x ++) {
			for(int y = 0; y < 8; y ++) {
				board[x][y].setActive(false);
			}
		}

		return isChecked;
	}

	/**
	 * Metoda pro vykonani enPassantu
	 */
	private void enPassant() {
		if(currentPiece instanceof Pawn) {
			if(currentPiece.getyPosition() == 3 && currentPiece.getColor().equals(blackPiece) && ((Pawn) currentPiece).getFirstWalk()) {
				((Pawn) currentPiece).setTwoStepsOnFirst(true);
				enPassantablePawns = ((Pawn) currentPiece).getEnPassantablePawns();
				((Pawn) currentPiece).enPassant(pieces);
			}

			if(currentPiece.getyPosition() == 4 && currentPiece.getColor().equals(whitePiece) && ((Pawn) currentPiece).getFirstWalk()) {
				((Pawn) currentPiece).setTwoStepsOnFirst(true);
				enPassantablePawns = ((Pawn) currentPiece).getEnPassantablePawns();
				((Pawn) currentPiece).enPassant(pieces);
			}
		} else {
			for(Pawn i : enPassantablePawns) {
				if(i != null) {
					i.setEnPassant(false);
				}
			}
		}
	}

	/**
	 * Metoda pro provedeni rosady
	 */
	private void castling() {
		if(currentPiece instanceof King && ((King) currentPiece).getFirstStep() && pieces[7][(int)currentPiece.getyPosition()] instanceof Tower && ((Tower) pieces[7][(int)currentPiece.getyPosition()]).getFirstStep()) {
			if (pieces[4][(int) currentPiece.getyPosition()] == null && pieces[5][(int) currentPiece.getyPosition()] instanceof King && pieces[6][(int) currentPiece.getyPosition()] == null) {
				((Tower)pieces[7][(int) currentPiece.getyPosition()]).setFirstStep(false);

				IPiece tower = pieces[7][(int) currentPiece.getyPosition()];
				tower.setxPosition(4);
				pieces[7][(int) currentPiece.getyPosition()] = tower;
			}
		}

		if(currentPiece instanceof King && ((King) currentPiece).getFirstStep() && pieces[0][(int)currentPiece.getyPosition()] instanceof Tower && ((Tower) pieces[0][(int)currentPiece.getyPosition()]).getFirstStep()) {
			if (pieces[3][(int) currentPiece.getyPosition()] == null && pieces[2][(int) currentPiece.getyPosition()] == null && pieces[1][(int) currentPiece.getyPosition()] instanceof King) {
				((Tower)pieces[0][(int) currentPiece.getyPosition()]).setFirstStep(false);
				pieces[0][(int) currentPiece.getyPosition()].setxPosition(2);

				IPiece tower = pieces[0][(int) currentPiece.getyPosition()];
				tower.setxPosition(2);
				pieces[0][(int) currentPiece.getyPosition()] = tower;
			}
		}
	}

	/**
	 * Za pomoci teto metody ma kazda figurka animovany tah po sachovnici
	 * @param xPosition		startovni x pozice figurky
	 * @param yPosition		startovni y pozice figurky
	 * @param newX			konecna x pozice figurky
	 * @param newY			konecna y pozice figurky
	 */
	private void animation(int xPosition, int yPosition, int newX, int newY) {
		IPiece animatedPiece = currentPiece;
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			int moveDistance = 500;
			int i = 0;
			@Override
			public void run() {
				i++;
				double t = (double)i / (double) moveDistance;
				animatedPiece.setxPosition(lerp(xPosition, newX, t));
				animatedPiece.setyPosition(lerp(yPosition, newY, t));

				if(t >= 1) {
					for(int x = 0; x < 8; x ++) {
						for(int y = 0; y < 8; y ++) {
							board[x][y].setActive(false);
						}
					}

					cancel();
				}

				repaint();
			}
		}, 0, 1);
	}

	/**
	 * Spocita novou souradnici na ktere se ma figurka zobrazit
	 * @param start		startovni pozice
	 * @param end		konecna pozice
	 * @param t			koeficien
	 * @return		nova pozice, na ktere se ma figurka zobrazit
	 */
	private double lerp(double start, double end, double t) {
		return start + t * (end - start);
	}

	/**
	 * Metoda provede odebrani pesce, pokud bude mozne brani mimochodem
	 * @param x		x pozice
	 * @param y		y pozice
	 */
	private void removePawnEnPassant(int x, int y) {
		if(((Pawn) currentPiece).getEnPassant()) {
			if(currentPiece.getColor().equals(blackPiece)) {
				pieces[x][y - 1] = null;
				((Pawn) currentPiece).setEnPassant(false);
			} else if(currentPiece.getColor().equals(whitePiece)) {
				pieces[x][y + 1] = null;
				((Pawn) currentPiece).setEnPassant(false);
			}
		}
	}

	/**
	 * Metoda ukaze pro aktualni figurku validni tahy
	 */
	private void validMove() {
		if(currentPiece != null) {
			if(blackTurn && currentPiece.getColor().equals(blackPiece)) {
				currentPiece.movement(board, pieces);
			}

			if(whiteTurn && currentPiece.getColor().equals(whitePiece)) {
				currentPiece.movement(board, pieces);
			}
		}
	}

	/**
	 * Metoda zjisti, zda byla mys pustena na konci sachovnice
	 * @param e
	 */
	private void isOnEnd(MouseEvent e) {
		if(currentPiece != null & currentPiece instanceof Pawn) {
			if (currentPiece.getyPosition() == 7 || currentPiece.getyPosition() == 0) {
				IPiece selectedPiece = null;
				String[] buttons = {"Kůň", "Střelec", "Věž", "Královna"};
				int returnPiece = JOptionPane.showOptionDialog(null, "Vyberte si figurku:", "", JOptionPane.WARNING_MESSAGE, 0, null, buttons, buttons[0]);

				switch(returnPiece) {
					case 0:
						selectedPiece = new Horse((int)currentPiece.getxPosition(), (int)currentPiece.getyPosition(), null);
						break;

					case 1:
						selectedPiece = new Bishop((int)currentPiece.getxPosition(), (int)currentPiece.getyPosition(), null);
						break;

					case 2:
						selectedPiece = new Tower((int)currentPiece.getxPosition(), (int)currentPiece.getyPosition(), null);
						break;

					case 3:
						selectedPiece = new Queen((int)currentPiece.getxPosition(), (int)currentPiece.getyPosition(), null);
						break;
				}

				pieces[(int)currentPiece.getxPosition()][(int)currentPiece.getyPosition()] = null;
				selectedPiece.setColor(currentPiece.getColor());

				pieces[(int)currentPiece.getxPosition()][(int)currentPiece.getyPosition()] = selectedPiece;
			}
		}
	}

	/**
	 * Metoda pro vyresetovani hry
	 */
	private void restart() {
		for(int x = 0; x < 8; x ++) {
			for(int y = 0; y < 8; y ++) {
				pieces[x][y] = null;
			}
		}

		whiteTurn = true;
		blackTurn = false;

		setPieces();
	}
}
