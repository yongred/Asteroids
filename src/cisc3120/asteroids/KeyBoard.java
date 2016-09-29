package cisc3120.asteroids;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class KeyBoard implements KeyListener {
	
	Asteroids Ast;
	
	public KeyBoard(Asteroids Ast)
	{
		this.Ast= Ast;
	}
	
	public void keyPressed(KeyEvent e)
	{
		Ast.keyPressed(e);
	}
	
	public void keyReleased(KeyEvent e)
	{
		Ast.keyReleased(e);
	}
	
	public void keyTyped(KeyEvent e)
	{
		
	}

	
}
