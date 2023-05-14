import java.awt.*;
import java.awt.geom.Path2D;

public class Tower implements IPiece {
    /**
     * x souradnice
     */
    private int[] x = {2, 2, 3, 4, 3, 2, 3, 3, 4, 4, 6, 6, 7, 7, 8, 7, 6, 7, 8, 8, 2};
    /**
     * y souradnice
     */
    private int[] y = {9, 7, 7, 3, 3, 1, 1, 2, 2, 1, 1, 2, 2, 1, 1, 3, 3, 7, 7, 9, 9};

    public int xPosition, yPosition;

    public Color color;

    public Tower(int x, int y, Color color) {
        this.xPosition = x;
        this.yPosition = y;
        this.color = color;
    }

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

    @Override
    public void movement(Square[][] board, IPiece[][] pieces) {
        int[] directionsX = {1, -1, 0, 0};
        int[] directionsY = {0, 0, 1, -1};

        for(int i = 0; i < 4; i ++) {
            int x = getxPosition();
            int y = getyPosition();

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
}
