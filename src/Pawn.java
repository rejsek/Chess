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
	public int xPosition, yPosition;

	/**
	 * Barva
	 */
	public Color color;

	/**
	 * Prvni tah
	 */
	public boolean firstWalk = true;

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
			if (pieces[getxPosition()][getyPosition() + direction] == null && pieces[getxPosition()][getyPosition() + (2 * direction)] == null) {
				for (int i = 1; i <= 2; i++) {
					board[getxPosition()][getyPosition() + (i * direction)].setActive(true);
				}
			}

			showEnemy(pieces, board, direction, enemyColor);
		} else {
			if (pieces[getxPosition()][getyPosition() + direction] == null) {
				board[getxPosition()][getyPosition() + direction].setActive(true);
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
			if (checkEnemy(pieces, getxPosition() + 1, getyPosition() + direction, enemyColor)) {
				board[getxPosition() + 1][getyPosition() + direction].setActive(true);
			}
		}

		if (getxPosition() - 1 >= 0 && getxPosition() - 1 < 8 && getyPosition() + direction >= 0 && getyPosition() + direction < 8) {
			if (checkEnemy(pieces, getxPosition() - 1, getyPosition() + direction, enemyColor)) {
				board[getxPosition() - 1][getyPosition() + direction].setActive(true);
			}
		}
	}

	public void setxPosition(int xPosition) {
		this.xPosition = xPosition;
	}

	public void setyPosition(int yPosition) {
		this.yPosition = yPosition;
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
	}

	public void setFirstWalk(boolean firstWalk) {
		this.firstWalk = firstWalk;
	}

	public int getxPosition() {
		return this.xPosition;
	}

	public int getyPosition() {
		return this.yPosition;
	}

	@Override
	public Color getColor() {
		return this.color;
	}

	public boolean getFirstWalk() {
		return this.firstWalk;
	}
}
