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
		p2 = new Player(1, 50, "dummy");
		prop = new Property("nowhere", 50, 20);
		prop.setOwner(p2);
		prop.unmortgage();
	}

	// Test that mortgaging a property updates the "mortgaged" value
	@Test
	public void Mortgage_Standard_Valid() {
		setup();
		prop.setOwner(p1);
		prop.mortgage();
		assertTrue(prop.getMortgaged());
	}

	// Test that a valid mortgage will pay the owner the proper amount
	@Test
	public void Mortgage_Standard_IsPaid() {
		setup();
		prop.setOwner(p1);
		prop.mortgage();
		assertEquals(p1.getMoney(), 125);
	}

	// Test that unmortgaging a property updates the "mortgaged" value
	@Test
	public void Unmortgage_Standard_Valid() {
		setup();
		prop.setOwner(p1);
		prop.mortgage();
		prop.unmortgage();
		assertFalse(prop.getMortgaged());
	}

	// Test that a valid unmortgage will charge the owner the proper amount
	@Test
	public void Unmortgage_Standard_IsCharged() {
		setup();
		prop.setOwner(p1);
		prop.mortgage();
		prop.unmortgage();
		assertEquals(p1.getMoney(), 75);
	}

	// Test that attempting to mortgage an already mortgaged property does 
	// nothing
	@Test
	public void Mortgage_AlreadyMortgaged_NotPaid() {
		setup();
		prop.setOwner(p1);
		prop.setMortgaged(true);
		prop.mortgage();
		assertEquals(p1.getMoney(), 100);
	}

	// Test that attempting to unmortgage an already unmortgaged property does
	// nothing
	@Test
	public void Unmortgage_AlreadyUnmortgaged_NotCharged() {
		setup();
		prop.setOwner(p1);
		prop.setMortgaged(false);
		prop.unmortgage();
		assertEquals(p1.getMoney(), 100);
	}

	// Tests that one can unmortgage a property with exactly the price requested
	@Test
	public void Unmortgage_MoneyEqualsPrice_Valid() {
		setup();
		prop.setOwner(p2);
		prop.setMortgaged(true);
		prop.unmortgage();
		assertFalse(prop.getMortgaged());
	}

	// Tests that a player is properly charged for unmortgaging a property with
	// price equal to their remaining money
	@Test
	public void Unmortgage_MoneyEqualsPrice_IsCharged() {
		setup();
		prop.setOwner(p2);
		prop.setMortgaged(true);
		prop.unmortgage();
		assertEquals(p2.getMoney(), 0);
	}

	// Tests that if a player cannot afford to unmortgage a property it will
	// stay mortgaged
	@Test
	public void Unmortgage_TooExpensive_Invalid() {
		setup();
		prop.setOwner(p2);
		prop.setMortgaged(true);
		prop.unmortgage();
		assertTrue(prop.getMortgaged());
	}

	// Tests that if a player cannot afford to unmortgage a property they will
	// not be charged
	@Test
	public void Unmortgage_TooExpensive_NotCharged() {
		setup();
		prop.setOwner(p2);
		prop.setMortgaged(true);
		prop.unmortgage();
		assertEquals(p2.getMoney(), 50);
	}
}
