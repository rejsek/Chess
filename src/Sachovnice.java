import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

public class Sachovnice extends JPanel{
	/**
	 * Objekt tridy AffineTransform
	 */
	private AffineTransform transform;

	/**
	 * Pole reprezentujici jednotlive figurky a jejich pozice
	 */
	private IPiece[][] pieces = new IPiece[8][8];

	/**
	 * Pole reprezentujici jednotlice ctverecky
	 */
	private Square[][] board = new Square[8][8];

	/**
	 *
	 */
	private int mensi;

	/**
	 * Zvolena figurka
	 */
	private IPiece currentPiece;

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

		//JOptionPane.showMessageDialog(null, "Bílý začíná", null, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Metoda pro veskera vykresleni
	 * @param g  the <code>Graphics</code> context in which to paint
	 */
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		mensi = Math.min(getWidth(),getHeight());
		int posun = (getWidth()-getHeight())/2;

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
						int xPosition = currentPiece.getxPosition();
						int yPosition = currentPiece.getyPosition();

						if(xPosition != x || yPosition != y) {
							if(currentPiece.getColor().equals(blackPiece)) {
								blackTurn = false;
								whiteTurn = true;
							} else {
								blackTurn = true;
								whiteTurn = false;
							}

							pieces[currentPiece.getxPosition()][currentPiece.getyPosition()] = null;

							currentPiece.setxPosition(x);
							currentPiece.setyPosition(y);

							if(currentPiece instanceof Pawn) {
								if(currentPiece.getyPosition() == 3 && currentPiece.getColor().equals(blackPiece) && ((Pawn) currentPiece).getFirstWalk()) {
									((Pawn) currentPiece).setTwoStepsOnFirst(true);
									enPassantablePawns = ((Pawn) currentPiece).getEnPassantablePawns();
									((Pawn) currentPiece).enPassant(pieces);
								} else {
									((Pawn) currentPiece).setEnPassant(false);
									((Pawn) currentPiece).setTwoStepsOnFirst(false);
								}

								if(currentPiece.getyPosition() == 4 && currentPiece.getColor().equals(whitePiece) && ((Pawn) currentPiece).getFirstWalk()) {
									((Pawn) currentPiece).setTwoStepsOnFirst(true);
									enPassantablePawns = ((Pawn) currentPiece).getEnPassantablePawns();
									((Pawn) currentPiece).enPassant(pieces);
								} else {
									((Pawn) currentPiece).setEnPassant(false);
									((Pawn) currentPiece).setTwoStepsOnFirst(false);
								}
							} else {
								for(Pawn i : enPassantablePawns) {
									if(i != null) {
										System.out.println(i.getxPosition() + ", " + i.getyPosition());
										i.setEnPassant(false);
									}
								}
							}

							pieces[x][y] = currentPiece;

							if(currentPiece instanceof Pawn) {
								removePawnEnPassant(x, y);

								((Pawn) currentPiece).setFirstWalk(false);
							}
						}
					}
				}
			}
		}
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
		if(currentPiece != null) {
			Queen queen = new Queen(currentPiece.getxPosition(), currentPiece.getyPosition(), null);

			if (currentPiece instanceof Pawn) {
				if (currentPiece.getyPosition() == 7 || currentPiece.getyPosition() == 0) {
					pieces[currentPiece.getxPosition()][currentPiece.getyPosition()] = null;
					queen.setColor(currentPiece.getColor());

					pieces[currentPiece.getxPosition()][currentPiece.getyPosition()] = queen;
				}
			}
		}
	}
}
