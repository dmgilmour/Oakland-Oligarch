import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Random;
import game.OaklandOligarchy;
import game.Game;

public class playerNumTest {
	@Test
	public void testPlay2()
	{
		assertEquals((OaklandOligarchy.generatePlayers(2)).length, 2);
	}
	
	public void testRoll()
	{
		long timeMillis = System.currentTimeMillis(); 
		assertEquals(Game.roll(timeMillis), -1);
		Game g = new Game(null);
		Random rand = new Random(timeMillis);
		int roll = rand.nextInt(6) + rand.nextInt(6) + 2;
		assertEquals(g.roll(timeMillis), roll);
	}
}
