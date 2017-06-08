import org.junit.Test;
import static org.junit.Assert.*;
import game.OaklandOligarchy;

public class playerNumTest {
	@Test
	public void testPlay2()
	{
		assertEquals((OaklandOligarchy.generatePlayers(2)).length, 2);
	}
}