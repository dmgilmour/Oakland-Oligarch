import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import game.Player;
import game.Property;

public class PlayerTest {

	Player p1;
	Player p2;
	Player p3;
	Player p4;

	Property prop1;
	Property prop2;
	Property prop3;

	public void setup() {
		prop1 = new Property("Boardwalk", 1000, 400);
		prop2 = new Property("Boardwalk", 2000, 400);
		prop3 = new Property("Boardwalk", 2000, 1500);

		p1 = new Player(0, 2000, "P1");
		p2 = new Player(1, 1500, "P2");
		p3 = new Player(2, 1000, "P3");
		p4 = new Player(3, 500, "P4");

		p4.addProperty(prop3);
	}

	// Test that buying a property a player can afford will let the player buy
	@Test
	public void buyStandardValidated() {
		setup();
	}
	
	// Test that buying a property a player can afford will charge the player
	// the proper amount
	@Test
	public void buyStandardCharged() {
		setup();
		p1.buy(prop1);
		assertEquals(p1.getMoney(), 1000);
	}

	// Test that buying a property a player can afford will add the property to
	// their property list
	@Test
	public void buyStandardIsStored() {
		setup();
		p1.buy(prop1);
		assertEquals(p1.getProperties().get(0), prop1);
	}

	// Tests that attempting to buy an already owned property is not allowed
	@Test
	public void buyOwnedPropertyValidated() {
		setup();
		assertFalse(p1.buy(prop3));
	}
	
	// Tests that attempting to buy a property with price exactly equal to the
	// player's current money is allowed
	@Test
	public void buyMoneyEqualsPriceValidated() {
		setup();
		assertTrue(p1.buy(prop2));
	}

	// Tests that attempting to buy a property with price exactly equal to the
	// player's current money charges them properly and leaves them with $0
	@Test
	public void buyMoneyEqualsPriceCharged() {
		setup();
		p1.buy(prop2);
		assertEquals(p1.getMoney(), 0);
	}
		
	// Tests that attempting to buy a property with price exactly equal to the
	// player's current money stores the property in their property list
	@Test
	public void buyMoneyEqualsPriceIsStored() {
		setup();
		p1.buy(prop2);
		assertEquals(p1.getProperties().get(0), prop2);
	}

	// Tests that attempting to buy a property that the player cannot afford is
	// not allowed
	@Test
	public void buyTooExpensiveValidated() {
		setup();
		assertFalse(p2.buy(prop2));
	}

	// Tests that attempting to buy a property that the player cannot afford
	// will not charge them
	@Test
	public void buyTooExpensiveCharged() {
		setup();
		p2.buy(prop2);
		System.err.println(p2.getMoney());
		assertEquals(p2.getMoney(), 1500);
	}
		
	// Tests that attempting to buy a property that the player cannot afford 
	// will not add the property to their propertylist
	@Test
	public void buyTooExpensiveIsStored() {
		setup();
		p2.buy(prop2);
		System.err.println(p2.getProperties().size());
		assertNull(p2.getProperties());
	}
		
	// Tests that a player can successfully pay a rent they can afford
	@Test
	public void rentStandardValidate() {
		setup();
		assertTrue(p1.payRent(prop3));
	}

	// Tests that a player is successfully charged the correct amount of rent
	// for a property they can afford
	@Test
	public void rentStandardCharged() {
		setup();
		p1.payRent(prop3);
		assertEquals(p1.getMoney(), 500);
	}

	// Tests that money for a successful rent payment goes to the owner of the
	// property
	@Test
	public void rentStandardOwnerPaid() {
		setup();
		p1.payRent(prop3);
		assertEquals(p4.getMoney(), 2000);
	}

	// Tests that a player can sucessfully pay a rent that is exactly equal to 
	// their current money
	@Test 
	public void rentMoneyEqualsRentValidate() {
		setup();
		assertTrue(p2.payRent(prop3));
	}

	// Tests that a player is charged and left with $0 for a rent that is
	// exactly equal to their current money
	@Test
	public void rentMoneyEqualsRentCharged() {
		setup();
		p2.payRent(prop3);
		assertEquals(p2.getMoney(), 0);
	}

	// Tests that the property owner is paid for rent charged to a player that
	// has exactly enough money to afford it
	@Test
	public void rentMoneyEqualsRentOwnerPaid() {
		setup();
		p2.payRent(prop3);
		assertEquals(p4.getMoney(), 2000);
	}

	// Tests that a player is not validated when attempting to pay a rent they
	// cannot afford
	@Test 
	public void rentTooExpensiveValidate() {
		setup();
		assertFalse(p3.payRent(prop3));
	}

	// Tests that a player is (or isn't) charged the amount of the rent when
	// they cannot afford it
	// FIX WHEN CHARGE BEHAVIOR IS DETERMINED
	@Test
	public void rentTooExpensiveCharged() {
		setup();
		p3.payRent(prop3);
		assertEquals(p1.getMoney(), 1000);
	}

	// Tests that a property's owner is not given money for rent that a player
	// cannot afford
	@Test
	public void rentTooExpensiveOwnerPaid() {
		setup();
		p3.payRent(prop3);
		assertEquals(p4.getMoney(), 500);
	}

	// Tests that a rent payment is not validated when the property they're
	// paying rent on is unowned
	@Test
	public void rentUnownedPropertyValidate() {
		setup();
		assertFalse(p1.payRent(prop2));
	}
}
