import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

public class Horse implements IPiece {
    /**
     * x souradnice
     */
    private int[] x = {2, 2, 4, 2, 5, 3, 2, 4, 6, 8, 8, 6, 8, 8, 2};

    /**
     * y souradnice
     */
    private int[] y = {9, 7, 7, 6, 3, 4, 3, 1, 1, 3, 6, 7, 7, 9, 9};

    public int xPosition, yPosition;

    public Color color;

    public Horse(int xPosition, int yPosition, Color color) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.color = color;
    }

    @Override
    public void show(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        double oneUnit = 0.1;
        double r = 0.65 * oneUnit;

        Ellipse2D.Double horseEye = new Ellipse2D.Double(((4 * oneUnit) + xPosition) - r / 2, ((2 * oneUnit) + yPosition) - r / 2, r, r);

        Path2D horse = new Path2D.Double();

        horse.moveTo((x[0] * oneUnit) + xPosition, (y[0] * oneUnit) + yPosition);

        for(int i = 1; i < x.length; i ++) {
            horse.lineTo((x[i] * oneUnit) + xPosition, (y[i] * oneUnit) + yPosition);
        }

        g2d.setColor(Color.BLACK);
        g2d.fill(horseEye);

        g2d.setColor(color);
        g2d.fill(horse);
    }

    @Override
    public void movement(Square[][] board, IPiece[][] pieces) {
        int[] direction = {1, -1};
        int forwardX = 1;
        int forwardY = 2;

        for(int d : direction) {
            for(int i = 0; i < 2; i ++) {
                if(getxPosition() + forwardX >= 0 && getxPosition() + forwardX < 8 &&
                        getyPosition() + forwardY >= 0 && getyPosition() + forwardY < 8) {

                    if(getColor().equals(Color.BLACK)) {
                        if(isThereEnemy(pieces, getxPosition() + forwardX, getyPosition() + forwardY, Color.WHITE)) {
                            board[getxPosition() + forwardX][getyPosition() + forwardY].setActive(true);
                        }
                    } else {
                        if(isThereEnemy(pieces, getxPosition() + forwardX, getyPosition() + forwardY, Color.BLACK)) {
                            board[getxPosition() + forwardX][getyPosition() + forwardY].setActive(true);
                        }
                    }
                }

                if(getyPosition() - forwardY >= 0 && getxPosition() + forwardX >= 0 &&
                        getxPosition() + forwardX < 8) {

                    if(getColor().equals(Color.BLACK)) {
                        if(isThereEnemy(pieces, getxPosition() + forwardX, getyPosition() - forwardY, Color.WHITE)) {
                            board[getxPosition() + forwardX][getyPosition() - forwardY].setActive(true);
                        }
                    } else {
                        if(isThereEnemy(pieces, getxPosition() + forwardX, getyPosition() - forwardY, Color.BLACK)) {
                            board[getxPosition() + forwardX][getyPosition() - forwardY].setActive(true);
                        }
                    }
                }

                if(d != -1) {
                    forwardX++;
                } else {
                    forwardX--;
                }
                forwardY--;
            }

            forwardX = -1;
            forwardY = 2;
        }
    }

    /**
     * Zjisti, zda se na pozici x a y nachazi souper, ci je pole prazdne
     * @param x				x souradnice, kde se ma hledat souper nebo prazdne pole
     * @param y				y souradnice, kde se ma hledat souper nebo prazdne pole
     * @param enemyColor	barva soupere, ktereho je treba najit
     * @return		true, 	pokud je pole prazdne, nebo se na nem nachazi souper
     * 				false,	pokud se na poli nachazi souper
     */
    private boolean isThereEnemy(IPiece[][] pieces, int x, int y, Color enemyColor) {
        boolean enemy = false;

        if(pieces[x][y] != null && pieces[x][y].getColor().equals(enemyColor)) {
            enemy = true;
        } else if(pieces[x][y] == null) {
            enemy = true;
        }

        return enemy;
    }

    @Override
    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    @Override
    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public int getxPosition() {
        return this.xPosition;
    }

    @Override
    public int getyPosition() {
        return this.yPosition;
    }

    @Override
    public Color getColor() {
        return this.color;
    }
}
