import javax.swing.*;
import java.awt.*;

/**
 * Trida slouzi k vytvoreni ctverce, predstavujici policko na sachovnici
 */
public class Square {

    /**
     * x souradnice
     */
    private int x;

    /**
     * y souradnice
     */
    private int y;

    /**
     * vleikost ctverce
     */
    private int size;

    /**
     * objekt tridy Rectangle
     */
    private Rectangle rectangle;
    /**
     * Jaky ctverec je aktivni
     */
    private boolean active = false;

    private boolean movable = false;

    private Color color;

    /**
     * Konstruktor nastavi ctverec sachovnice
     * @param x         x souradnice ctverce
     * @param y         y souradnice ctverce
     */
    public Square(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;

        this.rectangle = new Rectangle(this.x, this.y, 1, 1);
    }

    /**
     * Metoda vrati ctverec
     * @return  ctverec sachovnice
     */
    public Rectangle getRectangle() {
        return this.rectangle;
    }

    /**
     * Vykresli ctverec
     * @param g2d     objekt tridy Graphics2D
     */
    public void showRectangle(Graphics2D g2d) {
        if(active) {
            color = new Color(217, 70, 70, 171);
            g2d.setColor(color);
            setActive(false);
        }

        g2d.setColor(color);
        g2d.fillRect(this.x, this.y, 1, 1);
    }

    /**
     * Nastavi x souradnici
     * @param x     nova x souradnice
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Nastavi y souradnici
     * @param y     nova y souradnice
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Nastavi velikost ctverecku
     * @param size      nova velikost ctverecku
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Nastavi aktivni ctverec
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setMovable(boolean action) {
        this.movable = action;
    }

    /**
     * Vrati x souradnici ctverecku sachovnice
     * @return  x souradnice
     */
    public int getX() {
        return this.x;
    }

    /**
     * Vrati y souradnici ctverecku sachovnice
     * @return  y souradnice
     */
    public int getY() {
        return this.y;
    }

    public boolean getActive() {
        return this.active;
    }

    public Color getColor() {
        return this.color;
    }

    public boolean isMovable() {
        return this.movable;
    }

    /**
     * Zjisti, ktery ctverecek sachovnice byl zvolen
     * @param mouseX   the <i>x</i> coordinate of the point
     * @param mouseY   the <i>y</i> coordinate of the point
     * @return
     */
    public boolean contains(int mouseX, int mouseY) {
        if(mouseX == this.x && mouseY == this.y) {
            return true;
        }

        return false;
    }
}
