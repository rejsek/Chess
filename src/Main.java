import javax.swing.*;
import java.awt.*;
public class Main extends JFrame{
	public static void main(String... args){
		new Main().setVisible(true);
	}
	public Main(){
		setSize(800,600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().add(new Sachovnice());
	}
}
