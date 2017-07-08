import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import game.Player;
import game.Property;

public class playerTest {	

	@Before
	public void setup() throws Exception {
		Player p1 = new Player(0, 2000, "P1", null);
	}


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
	
	@Test
	public void testRent1()			//Tests a successfully rent payment
	{
		Player p1 = new Player(0, 0, "P1", null);
		Player p2 = new Player(0, 10, "P2", null);
		Property prop1 = new Property("Boardwalk", 0, 10);
		prop1.setOwner(p1);
		assertEquals(p2.payRent(prop1), true);
		assertEquals(p2.getMoney(), 0);
		assertEquals(p1.getMoney(), 10);
	}
	
	@Test
	public void testRent2()			//Tests a failed rent payment
	{
		Player p1 = new Player(0, 0, "P1", null);
		Player p2 = new Player(0, 0, "P2", null);
		Property prop1 = new Property("Boardwalk", 0, 10);
		prop1.setOwner(p1);
		assertEquals(p2.payRent(prop1), false);
		assertEquals(p2.getMoney(), 0);
		assertEquals(p1.getMoney(), 0);
	}
}
