import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Random;
import game.OaklandOligarchy;

public class playerNumTest {
	@Test
	public void testPlay2()
	{
		assertEquals((OaklandOligarchy.generatePlayers(2)).length, 2);
	}
	
	public void testMovePhase()
	{
		Long time = System.currentTimeMillis();
		Random rand = new Random(time);
		int roll = rand.nextInt(5) + rand.nextInt(5) + 2;
		assertEquals(OaklandOligarchy.movePhase(time)[1], roll);
	}
}