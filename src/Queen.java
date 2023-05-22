import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

public class Queen implements IPiece {
    /**
     * x souradnice
     */
    private int[] x = {2, 2, 3, 2, 3, 5, 7, 8, 7, 8, 8, 2};

    /**
     * y souradnice
     */
    private int[] y = {9, 7, 7, 4, 5, 2, 5, 4, 7, 7, 9, 9};

    public double xPosition, yPosition;

    public Color color;

    public Queen(int xPosition, int yPosition, Color color) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.color = color;
    }

    public void show(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        double oneUnit = 0.1;
        double r = 1.5 * oneUnit;

        Ellipse2D.Double queenCircle1 = new Ellipse2D.Double(((x[3] * oneUnit) + xPosition) - r / 2, ((y[3] * oneUnit) + yPosition) - r / 2, r, r);
        Ellipse2D.Double queenCircle2 = new Ellipse2D.Double(((x[5] * oneUnit) + xPosition) - r / 2, ((y[5] * oneUnit) + yPosition) - r / 2, r, r);
        Ellipse2D.Double queenCircle3 = new Ellipse2D.Double(((x[7] * oneUnit) + xPosition) - r / 2, ((y[7] * oneUnit) + yPosition) - r / 2, r, r);

        Path2D queen = new Path2D.Double();

        queen.moveTo((x[0] * oneUnit + xPosition), (y[0] * oneUnit) + yPosition);

        for(int i = 1; i < x.length; i ++) {
            queen.lineTo((x[i] * oneUnit) + xPosition, (y[i] * oneUnit) + yPosition);
        }

        g2d.setColor(color);
        g2d.fill(queenCircle1);
        g2d.fill(queenCircle2);
        g2d.fill(queenCircle3);
        g2d.fill(queen);
    }

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

        int[] direction = {1, -1};

        for(int x : direction) {
            for(int y : direction) {
                int forward = 1;

                while(getxPosition() + forward * x >= 0 && getxPosition() + forward * x <= 7 &&
                        getyPosition() + forward * y >= 0 && getyPosition() + forward * y <= 7) {

                    int xForward = (int)getxPosition() + forward * x;
                    int yForward = (int)getyPosition() + forward * y;

                    if(pieces[xForward][yForward] == null) {
                        board[xForward][yForward].setActive(true);
                    } else {
                        if(!pieces[xForward][yForward].getColor().equals(color)) {
                            board[xForward][yForward].setActive(true);
                        }

                        break;
                    }

                    forward ++;
                }
            }
        }
    }

    @Override
    public void setxPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    @Override
    public void setyPosition(double yPosition) {
        this.yPosition = yPosition;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public double getxPosition() {
        return this.xPosition;
    }

    @Override
    public double getyPosition() {
        return this.yPosition;
    }

    @Override
    public Color getColor() {
        return this.color;
    }
}
