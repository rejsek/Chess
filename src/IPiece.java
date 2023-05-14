import java.awt.*;

public interface IPiece {
    /**
     * Metoda pro zobrazeni figurky
     * @param g2d       objekt tridy Graphics2D
     */
    void show(Graphics2D g2d);

    /**
     * Zobrazi pristupne tahy podle pravidel
     * @param board             sachovnice
     * @param pieces            pole figurek
     */
    void movement(Square[][] board, IPiece[][] pieces);

    /**
     * Nastavi novou x pozici figurky
     * @param xPosition     nova x pozice figurky
     */
    void setxPosition(int xPosition);

    /**
     * Nastavi novou y pozici figurky
     * @param yPosition     nova y pozice figurky
     */
    void setyPosition(int yPosition);

    /**
     * Nastavi novou barvu figurky
     * @param color         nova barva figurky
     */
    void setColor(Color color);

    /**
     * Vrati x pozici figurky na sachovnici
     * @return  x pozice figurky
     */
    int getxPosition();

    /**
     * Vrati y pozici figurky na sachovnici
     * @return  y pozice figurky
     */
    int getyPosition();

    /**
     * Vrati barvu figurky
     * @return  barva figurky
     */
    Color getColor();
}
