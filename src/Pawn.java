import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

/**
 * Trida slouzi pro vytvoreni figurky Pesak
 *
 * @author Daniel Riess
 */
public class Pawn implements IPiece {
	/**
	 * x souradnice
	 */
	private int[] x = {3, 3, 7, 7, 3, 3, 5, 7};

	/**
	 * y souradnice
	 */
	private int[] y = {9, 8, 8, 9, 9, 8, 4, 8};

	/**
	 * x pozice, y pozice
	 */
	public double xPosition, yPosition;

	/**
	 * Barva
	 */
	public Color color;

	/**
	 * Prvni tah
	 */
	public boolean firstWalk = true;

	public boolean twoStepsOnFirst = false;

	public boolean enPassant = false;

	public Pawn[] enPassantablePawns = new Pawn[2];
	/**
	 * Konstuktor
	 * @param x			x souradnice figurky
	 * @param y			y souradnice figurky
	 * @param color		barva figurky
	 */
	public Pawn(int x,int y, Color color){
		this.xPosition = x;
		this.yPosition = y;
		this.color = color;
	}

	/**
	 * Metoda pro zobrazeni figurky
	 * @param g2d       objekt tridy Graphics2D
	 */
	@Override
	public void show(Graphics2D g2d){
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		double oneUnit = 0.1;
		double r = 3 * oneUnit;

		Ellipse2D.Double pawnHead = new Ellipse2D.Double(((5 * oneUnit) + xPosition) - r / 2, ((4 * oneUnit) + yPosition - r / 2), r, r);

		Path2D pawnBody = new Path2D.Double();

		pawnBody.moveTo((x[0] * oneUnit) + xPosition, ((y[0] * oneUnit) + yPosition));

		for(int i = 1; i < x.length; i ++) {
			pawnBody.lineTo((x[i] * oneUnit) + xPosition, (y[i] * oneUnit) + yPosition);
		}
		g2d.setStroke(new BasicStroke(2));

		g2d.setColor(color);
		g2d.fill(pawnBody);
		g2d.fill(pawnHead);
	}

	/**
	 * Zobrazi pristupne tahy podle pravidel
	 * @param board             sachovnice
	 * @param pieces            pole figurek
	 */
	@Override
	public void movement(Square[][] board, IPiece[][] pieces) {
		int direction = (color.equals(Color.WHITE)) ? -1 : 1;
		Color enemyColor = (color.equals(Color.WHITE)) ? Color.BLACK : Color.WHITE;

		if (firstWalk) {
			if (pieces[(int)getxPosition()][(int)getyPosition() + direction] == null && pieces[(int)getxPosition()][(int)getyPosition() + (2 * direction)] == null) {
				for (int i = 1; i <= 2; i++) {
					board[(int)getxPosition()][(int)getyPosition() + (i * direction)].setActive(true);
				}
			} else {
				board[(int)getxPosition()][(int)getyPosition() + direction].setActive(true);
			}

			showEnemy(pieces, board, direction, enemyColor);
		} else {
			showEnPassant(pieces, board);

			if (pieces[(int)getxPosition()][(int)getyPosition() + direction] == null) {
				board[(int)getxPosition()][(int)getyPosition() + direction].setActive(true);
			}

			showEnemy(pieces, board, direction, enemyColor);
		}
	}

	/**
	 * Metoda zjisti, zda se v okoli figurky nenachazi souper
	 * @param pieces		pole figurek
	 * @param x				x souradnice okoli
	 * @param y				y souradnice okoli
	 * @param color			barva hledaneho soupere
	 * @return			true, pokud je v okoli nalezen souper
	 * 					false, pokud v okoli neni nalezen souper
	 */
	private boolean checkEnemy(IPiece[][] pieces, int x, int y, Color color) {
		boolean isThereEnemy = false;

		if(pieces[x][y] != null && pieces[x][y].getColor().equals(color)) {
			isThereEnemy = true;
		}

		return isThereEnemy;
	}

	/**
	 * Zvyrazni souperovi figurky, ktere je mozne vyhodit
	 * @param pieces			pole figurek
	 * @param board				pole ctverecku reprezentujici sachovnici
	 * @param direction			smer, kterym se figurka pohubuje
	 * @param enemyColor		barva souperovi figurky
	 */
	private void showEnemy(IPiece[][] pieces, Square[][] board, int direction, Color enemyColor) {
		if (getxPosition() + 1 >= 0 && getxPosition() + 1 < 8 && getyPosition() + direction >= 0 && getyPosition() + direction < 8) {
			if (checkEnemy(pieces, (int)getxPosition() + 1, (int)getyPosition() + direction, enemyColor)) {
				board[(int)getxPosition() + 1][(int)getyPosition() + direction].setActive(true);
			}
		}

		if (getxPosition() - 1 >= 0 && getxPosition() - 1 < 8 && getyPosition() + direction >= 0 && getyPosition() + direction < 8) {
			if (checkEnemy(pieces, (int)getxPosition() - 1, (int)getyPosition() + direction, enemyColor)) {
				board[(int)getxPosition() - 1][(int)getyPosition() + direction].setActive(true);
			}
		}
	}

	public void enPassant(IPiece[][] pieces) {
		if(twoStepsOnFirst) {
			if(getxPosition() - 1 >= 0 && getyPosition() >= 0 && getyPosition() < 8) {
				if (pieces[(int)getxPosition() - 1][(int)getyPosition()] != null) {
					((Pawn) pieces[(int)getxPosition() - 1][(int)getyPosition()]).setEnPassant(true);
					enPassantablePawns[0] = ((Pawn) pieces[(int)getxPosition() - 1][(int)getyPosition()]);
				}
			}

			if(getxPosition() + 1 < 8 && getyPosition() >= 0 && getyPosition() < 8) {
				if (pieces[(int)getxPosition() + 1][(int)getyPosition()] != null) {
					((Pawn) pieces[(int)getxPosition() + 1][(int)getyPosition()]).setEnPassant(true);
					enPassantablePawns[1] = ((Pawn) pieces[(int)getxPosition() + 1][(int)getyPosition()]);
				}
			}
		}
	}

	private void showEnPassant(IPiece[][] pieces, Square[][] board) {
		if(enPassant) {
			if (getxPosition() + 1 < 8 && getyPosition() + 1 < 8 && getyPosition() - 1 >= 0) {
				if (pieces[(int)getxPosition() + 1][(int)getyPosition()] != null && !pieces[(int)getxPosition() + 1][(int)getyPosition()].getColor().equals(color)) {
					if (color.equals(Color.BLACK)) {
						board[(int)getxPosition() + 1][(int)getyPosition() + 1].setActive(true);
					} else {
						board[(int)getxPosition() + 1][(int)getyPosition() - 1].setActive(true);
					}
				}
			}

			if(getxPosition() - 1 >= 0 && getyPosition() + 1 < 8 && getyPosition() - 1 >= 0) {
				if (pieces[(int)getxPosition() - 1][(int)getyPosition()] != null && !pieces[(int)getxPosition() - 1][(int)getyPosition()].getColor().equals(color)) {
					if (color.equals(Color.BLACK)) {
						board[(int)getxPosition() - 1][(int)getyPosition() + 1].setActive(true);
					} else {
						board[(int)getxPosition() - 1][(int)getyPosition() - 1].setActive(true);
					}
				}
			}
		}
	}

	public void setxPosition(double xPosition) {
		this.xPosition = xPosition;
	}

	public void setyPosition(double yPosition) {
		this.yPosition = yPosition;
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
	}

	public void setFirstWalk(boolean firstWalk) {
		this.firstWalk = firstWalk;
	}

	public void setTwoStepsOnFirst(boolean action) {
		this.twoStepsOnFirst = action;
	}

	public void setEnPassant(boolean enPassant) {
		this.enPassant = enPassant;
	}

	public double getxPosition() {
		return this.xPosition;
	}

	public double getyPosition() {
		return this.yPosition;
	}

	@Override
	public Color getColor() {
		return this.color;
	}

	public boolean getFirstWalk() {
		return this.firstWalk;
	}

	public boolean isTwoStepsOnFirst() {
		return this.twoStepsOnFirst;
	}

	public boolean getEnPassant() {
		return this.enPassant;
	}

	public Pawn[] getEnPassantablePawns() {
		return enPassantablePawns;
	}
}
