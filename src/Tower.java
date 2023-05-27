import java.awt.*;
import java.awt.geom.Path2D;

/**
 * Trida slouzi pro vytvoreni figurky Vez
 * prislusne barvy na pozici x a y
 *
 * @author Daniel Riess
 */
public class Tower implements IPiece {
    /**
     * x souradnice
     */
    private int[] x = {2, 2, 3, 4, 3, 2, 3, 3, 4, 4, 6, 6, 7, 7, 8, 7, 6, 7, 8, 8, 2};
    /**
     * y souradnice
     */
    private int[] y = {9, 7, 7, 3, 3, 1, 1, 2, 2, 1, 1, 2, 2, 1, 1, 3, 3, 7, 7, 9, 9};

    /**
     * x a y souradnice figurky
     */
    private double xPosition, yPosition;

    /**
     * Barva figurky
     */
    private Color color;

    /**
     * Prvni krok
     */
    private boolean firstStep = true;

    /**
     * Kosntruktor
     * @param x         x souradnice
     * @param y         y souradnice
     * @param color     barva figurky
     */
    public Tower(int x, int y, Color color) {
        this.xPosition = x;
        this.yPosition = y;
        this.color = color;
    }

    /**
     * Metoda pro zobrazeni figurky
     * @param g2d       objekt tridy Graphics2D
     */
    @Override
    public void show(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        double oneUnit = 0.1;

        Path2D tower = new Path2D.Double();

        tower.moveTo((x[0] * oneUnit) + xPosition, ((y[0] * oneUnit) + yPosition));

        for(int i = 1; i < x.length; i ++) {
            tower.lineTo((x[i] * oneUnit) + xPosition, (y[i] * oneUnit) + yPosition);
        }

        g2d.setColor(color);
        g2d.fill(tower);
    }

    /**
     * Zobrazi pristupne tahy podle pravidel
     * @param board             sachovnice
     * @param pieces            pole figurek
     */
    @Override
    public void movement(Square[][] board, IPiece[][] pieces) {
        int[] directionsX = {1, -1, 0, 0};
        int[] directionsY = {0, 0, 1, -1};

        for(int i = 0; i < 4; i ++) {
            int x = (int)getxPosition();
            int y = (int)getyPosition();

            while(true) {
                x += directionsX[i];
                y += directionsY[i];

                if(x < 0 || x > 7 || y < 0 || y > 7) {
                    break;
                }

                if(pieces[x][y] == null) {
                    board[x][y].setActive(true);
                } else {
                    if(!pieces[x][y].getColor().equals(color)) {
                        board[x][y].setActive(true);
                    }

                    break;
                }
            }
        }
    }

    /**
     * Nastavi novou x pozici figurky
     * @param xPosition     nova x pozice figurky
     */
    public void setxPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    /**
     * Nastavi novou y pozici figurky
     * @param yPosition     nova y pozice figurky
     */
    public void setyPosition(double yPosition) {
        this.yPosition = yPosition;
    }

    /**
     * Nastavi novou barvu figurky
     * @param color         nova barva figurky
     */
    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Nastavi prvni krok
     * @param action        novy prvni krok
     */
    public void setFirstStep(boolean action) {
        this.firstStep = action;
    }

    /**
     * Vrati x pozici figurky
     * @return      x pozice figurky
     */
    public double getxPosition() {
        return this.xPosition;
    }

    /**
     * Vrati y pozici figurky
     * @return      y pozice figurky
     */
    public double getyPosition() {
        return this.yPosition;
    }

    /**
     * Vrati barvu figurky
     * @return      barva figurky
     */
    @Override
    public Color getColor() {
        return this.color;
    }

    /**
     * Vrati, zda figurka udelala prvni krok
     * @return      prvni krok
     */
    public boolean getFirstStep() {
        return this.firstStep;
    }
}
