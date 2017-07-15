import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import game.Property;
import game.Player;

public class PropertyTest {	

	Player p1;
	Player p2;
	Property prop;

	public void setup() {
		p1 = new Player(0, 100, "dummy");
		p2 = new Player(0, 50, "dummy");
		prop = new Property("nowhere", 50, 20);
	}

	@Test
	public void testMortgageStandard() {
		setup();
		prop.setOwner(p1);
		prop.mortgage();
		assertTrue(prop.getMortgaged());
	}

	@Test
	public void testMortgagePayStandard() {
		setup();
		prop.setOwner(p1);
		prop.mortgage();
		assertEquals(p1.getMoney(), 125);
	}

	@Test
	public void testUnmortgageStandard() {
		setup();
		prop.setOwner(p1);
		prop.mortgage();
		prop.unmortgage();
		assertFalse(prop.getMortgaged());
	}

	@Test
	public void testUnmortgagePayStandard() {
		setup();
		prop.setOwner(p1);
		prop.mortgage();
		prop.unmortgage();
		assertEquals(p1.getMoney(), 75);
	}
}
