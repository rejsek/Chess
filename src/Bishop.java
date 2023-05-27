import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

/**
 * Trida slouzi pro vytvoreni figurky Strelec
 * prislusne barvy na pozici x a y
 *
 * @author Daniel Riess
 */
public class Bishop implements IPiece {
    /**
     * x souradnice
     */
    private double[] x = {2, 2, 4, 2, 5, 8, 6, 8, 8, 2, 6.5, 5.5}; //koncove souradnice pro carku

    /**
     * y souradnice
     */
    private double[] y = {9, 7, 7, 4, 2, 4, 7, 7, 9, 9, 3, 4}; //koncove souradnice pro carku

    /**
     * x a y souradnice figurky
     */
    private double xPosition, yPosition;

    /**
     * Barva figurky
     */
    private Color color;

    /**
     * Konstruktor
     * @param x         x souradnice
     * @param y         y souradnice
     * @param color     barva figurky
     */
    public Bishop(int x, int y, Color color) {
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
        double r = 1.5 * oneUnit;

        Ellipse2D.Double bishopCircle = new Ellipse2D.Double(((x[4] * oneUnit) + xPosition) - r / 2, ((y[4] * oneUnit) + yPosition) - r / 2, r, r);

        Path2D bishop = new Path2D.Double();

        bishop.moveTo((x[0] * oneUnit) + xPosition, (y[0] * oneUnit) + yPosition);

        for(int i = 1; i < x.length - 2; i ++) {
            bishop.lineTo((x[i] * oneUnit) + xPosition, (y[i] * oneUnit) + yPosition);
        }

        g2d.setColor(color);
        g2d.fill(bishopCircle);
        g2d.fill(bishop);
    }

    /**
     * Zobrazi pristupne tahy podle pravidel
     * @param board             sachovnice
     * @param pieces            pole figurek
     */
    @Override
    public void movement(Square[][] board, IPiece[][] pieces) {
        int[] direction = {1, -1};

        for(int x : direction) {
            for(int y : direction) {
                int forward = 1;

                while(getxPosition() + forward * x >= 0 && getxPosition() + forward * x <= 7 &&
                    getyPosition() + forward * y >= 0 && getyPosition() + forward * y <= 7) {

                    if(pieces[(int)getxPosition() + forward * x][(int)getyPosition() + forward * y] == null) {
                        board[(int)getxPosition() + forward * x][(int)getyPosition() + forward * y].setActive(true);
                    } else {
                        if(!pieces[(int)getxPosition() + forward * x][(int)getyPosition() + forward * y].getColor().equals(getColor())) {
                            board[(int)getxPosition() + forward * x][(int)getyPosition() + forward * y].setActive(true);
                        }

                        break;
                    }

                    forward ++;
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
}
