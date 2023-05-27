import javax.swing.*;
import java.awt.*;

/**
 * Hlavni trida, slouzi ke spusteni cele aplikace
 *
 * @author Daniel Riess
 */
public class Main extends JFrame{
	/**
	 * Vstupni bod programu
	 * @param args		hodnoty prikazove radky
	 */
	public static void main(String[] args){
		new Main().setVisible(true);
	}

	/**
	 * Hlavni metoda aplikace, vytvori okno
	 */
	public Main(){
		setSize(800,600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().add(new Sachovnice());
	}
}
