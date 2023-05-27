import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Path2D;

/**
 * Trida slouzi pro vytvoreni figurky Kral
 * prislusne barvy na pozici x a y
 *
 * @author Daniel Riess
 */
public class King implements IPiece {
    /**
     * x souradnice
     */
    private int[] x = {2, 2, 3, 1, 9, 7, 8, 8, 2, 3, 7}; //posldeni dve x souradnice stredy kruhu

    /**
     * y souradnice
     */
    private int[] y = {9, 7, 7, 4, 4, 7, 7, 9, 9, 4, 4}; //posledni dve y souradnice stredu kruhu

    /**
     * x souradnice krize
     */
    private double[] crossX = {4.5, 4.5, 4, 4, 4.5, 4.5, 5.5, 5.5, 6, 6, 5.5, 5.5, 4.5};

    /**
     * y souradnice krize
     */
    private double[] crossY = {3, 2, 2, 1.5, 1.5, 1, 1, 1.5, 1.5, 2, 2, 3, 3};

    /**
     * vysec x souradnice
     */
    private int[] subtractX = {2, 3, 4, 4, 6, 6, 7, 8};

    /**
     * vysec y souradnice
     */
    private int[] subtractY = {4, 5, 5, 4, 4, 5, 5, 4};

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
     * Konstruktor
     * @param x         x souradnice
     * @param y         y souradnice
     * @param color     barva figurky
     */
    public King(int x, int y, Color color) {
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
        double bigR = 4.10 * oneUnit;
        double smallR = 2 * oneUnit;

        Arc2D.Double bigCircleLeft = new Arc2D.Double(((x[x.length - 2] * oneUnit) + xPosition) - bigR / 2, ((y[y.length - 2] * oneUnit) + yPosition) - bigR / 2, bigR, bigR, 0, 180, Arc2D.OPEN);
        Arc2D.Double bigCircleRight = new Arc2D.Double(((x[x.length - 1] * oneUnit) + xPosition) - bigR / 2, ((y[y.length - 1] * oneUnit) + yPosition) - bigR / 2, bigR, bigR, 0, 180, Arc2D.OPEN);

        Arc2D.Double smallCircleLeft = new Arc2D.Double(((x[x.length - 2] * oneUnit) + xPosition) - smallR / 2, ((y[y.length - 2] * oneUnit) + yPosition) - smallR / 2, smallR, smallR, 0, 180, Arc2D.OPEN);
        Arc2D.Double smallCircleRight = new Arc2D.Double(((x[x.length - 1] * oneUnit) + xPosition) - smallR / 2, ((y[y.length - 1] * oneUnit) + yPosition) - smallR / 2, smallR, smallR, 0, 180, Arc2D.OPEN);

        /*-------KRIZ-------*/
        Path2D cross = new Path2D.Double();

        cross.moveTo((crossX[0] * oneUnit) + xPosition, (crossY[0] * oneUnit) + yPosition);

        for(int i = 1; i < crossX.length; i ++) {
            cross.lineTo((crossX[i] * oneUnit) + xPosition, (crossY[i] * oneUnit) + yPosition);
        }
        /*-------KRIZ-------*/

        Path2D king = new Path2D.Double();

        king.moveTo((x[0] * oneUnit) + xPosition, (y[0] * oneUnit) + yPosition);

        for(int i = 1; i < x.length - 2; i ++) {
            king.lineTo((x[i] * oneUnit) + xPosition, (y[i] * oneUnit) + yPosition);
        }

        Path2D substractLeft = new Path2D.Double();
        Path2D substractRight = new Path2D.Double();

        substractLeft.moveTo((subtractX[0] * oneUnit) + xPosition, (subtractY[0] * oneUnit) + yPosition);

        for(int i = 1; i <= 3; i ++) {
            substractLeft.lineTo((subtractX[i] * oneUnit) + xPosition, (subtractY[i] * oneUnit) + yPosition);
        }

        substractRight.moveTo((subtractX[4] * oneUnit) + xPosition, (subtractY[4] * oneUnit) + yPosition);

        for(int i = 5; i < subtractX.length; i ++) {
            substractRight.lineTo((subtractX[i] * oneUnit) + xPosition, (subtractY[i] * oneUnit) + yPosition);
        }

        Area obj = new Area(king);
        obj.add(new Area(bigCircleLeft));
        obj.add(new Area(bigCircleRight));

        Area leftSubstriction = new Area(substractLeft);
        leftSubstriction.add(new Area(smallCircleLeft));

        Area rightSubstriction = new Area(substractRight);
        rightSubstriction.add(new Area(smallCircleRight));

        Area finalKing = new Area(obj);
        finalKing.subtract(leftSubstriction);
        finalKing.subtract(rightSubstriction);

        g2d.setColor(color);
        g2d.fill(cross);
        g2d.fill(finalKing);
    }

    /**
     * Zobrazi pristupne tahy podle pravidel
     * @param board             sachovnice
     * @param pieces            pole figurek
     */
    @Override
    public void movement(Square[][] board, IPiece[][] pieces) {
        int[] direction = {-1, 0, 1};

        for(int y : direction) {
            for(int x : direction) {
                if(getxPosition() + x >= 0 && getxPosition() + x < 8 &&
                        getyPosition() + y >= 0 && getyPosition() + y < 8) {

                    if (pieces[(int)getxPosition() + x][(int)getyPosition() + y] == null) {
                        board[(int)getxPosition() + x][(int)getyPosition() + y].setActive(true);
                    } else if(!pieces[(int)getxPosition() + x][(int)getyPosition() + y].getColor().equals(color)) {
                        board[(int)getxPosition() + x][(int)getyPosition() + y].setActive(true);
                    }


                    //----------          Rosada - vpravo         ----------//
                    if(firstStep && pieces[7][(int)getyPosition()] instanceof Tower && ((Tower) pieces[7][(int)getyPosition()]).getFirstStep()) {
                        if (pieces[4][(int) getyPosition()] == null && pieces[5][(int) getyPosition()] == null && pieces[6][(int) getyPosition()] == null) {
                            board[(int) getxPosition() + 2][(int) getyPosition()].setActive(true);
                        }
                    }

                    //----------          Rosada - vlevo         ----------//
                    if(firstStep && pieces[0][(int)getyPosition()] instanceof Tower && ((Tower) pieces[0][(int)getyPosition()]).getFirstStep()) {
                        if (pieces[2][(int) getyPosition()] == null && pieces[1][(int) getyPosition()] == null) {
                            board[(int) getxPosition() - 2][(int) getyPosition()].setActive(true);
                        }
                    }
                }
            }
        }
    }

    /**
     * Nastavi novou x pozici figurky
     * @param xPosition     nova x pozice figurky
     */
    @Override
    public void setxPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    /**
     * Nastavi novou y pozici figuky
     * @param yPosition     nova y pozice figurky
     */
    @Override
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
    @Override
    public double getxPosition() {
        return this.xPosition;
    }

    /**
     * Vrati y pozici figurky
     * @return      y pozice figurky
     */
    @Override
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
