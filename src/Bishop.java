import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

public class Bishop implements IPiece {
    /**
     * x souradnice
     */
    private double[] x = {2, 2, 4, 2, 5, 8, 6, 8, 8, 2, 6.5, 5.5}; //koncove souradnice pro carku

    /**
     * y souradnice
     */
    private double[] y = {9, 7, 7, 4, 2, 4, 7, 7, 9, 9, 3, 4}; //koncove souradnice pro carku

    public int xPosition, yPosition;

    public Color color;

    public Bishop(int x, int y, Color color) {
        this.xPosition = x;
        this.yPosition = y;
        this.color = color;
    }

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

    @Override
    public void movement(Square[][] board, IPiece[][] pieces) {
        int[] direction = {1, -1};

        for(int x : direction) {
            for(int y : direction) {
                int forward = 1;

                while(getxPosition() + forward * x >= 0 && getxPosition() + forward * x <= 7 &&
                    getyPosition() + forward * y >= 0 && getyPosition() + forward * y <= 7) {

                    if(pieces[getxPosition() + forward * x][getyPosition() + forward * y] == null) {
                        board[getxPosition() + forward * x][getyPosition() + forward * y].setActive(true);
                    } else {
                        if(!pieces[getxPosition() + forward * x][getyPosition() + forward * y].getColor().equals(getColor())) {
                            board[getxPosition() + forward * x][getyPosition() + forward * y].setActive(true);
                        }

                        break;
                    }

                    forward ++;
                }
            }
        }
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
