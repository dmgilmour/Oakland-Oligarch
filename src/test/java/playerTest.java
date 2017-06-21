import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import game.Player;
import game.Property;

public class playerTest {	
	@Test
	public void testBuy()
	{
		Player p1 = new Player(0, 2000, "P1", null);
		Property prop1 = new Property("Boardwalk", 1000, 4000);
		Property prop2 = new Property("Boardwalk", 1000, 4000);
		Property prop3 = new Property("Boardwalk", 1000, 4000);
		assertEquals(true, p1.buy(prop1));							//Test property cost < money
		assertEquals(p1.getMoney(), 1000);
		assertEquals(false, p1.buy(prop1));							//Test buying an owned property
		assertEquals(p1.getMoney(), 1000);
		assertEquals(true, p1.buy(prop2));							//Test propety cost == money
		assertEquals(p1.getMoney(), 0);
		assertEquals(false, p1.buy(prop3));							//Test property cost > money
		assertEquals(p1.getMoney(), 0);
		assertEquals(p1.getProperties().size(), 2);					//Test that properties are stored
	}
	
	
}