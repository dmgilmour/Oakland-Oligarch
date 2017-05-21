*	Spaces
    * Objects?
    * Stored in an array corresponding to location on board?
  	*	Properties can have nice Booleans like purchased, mortgaged, an enum for player who owns
  	*	Because landing on a space will only do one thing, after rolling just do board[player_location].activate();
    *	So properties, for instance, will: collect money, let you buy them, do nothing and will be determined within the “location” object
*	Players
    *	Objects?
    *	Arrayed objects?
    *	With nice Booleans like in_jail, strings like name, ints like money and get out of jail cards, a fancy list of all properties that will point to property objects (which will also point back to them which may be hard to deal with if a property is sold
*	States of a turn
    *	Stored in a state machine
        *	Do things
        *	Roll Dice
        *	Stuff happens to you
            *	You can do things during
        *	Do things
*	Doubles
    *	Stored as an int and as a Boolean
*	Jail
    *	Boolean in_jail
    *	Location change
    *	Check for in_jail at the beginning of each turn
*	Cards
    *	Not really going to be real
    *	When a chance/community chest location is landed on and activated, just call a random “card” which will be a function
    *	Stack of function pointers?
       *	That way we can do a random shuffle
       *	And remove them
*	Monopolies
    *	Some sort of event tracker
       *	Checked each time a property is obtained
       *	Location.checkMonopoly()
        *	Location will contain an int for what set it belongs to
*	Houses and Hotels
    *	Needs to be tied to player in case of being sold
        *	Then again you could just force them to be resolved before the underlying property is sold or mortgaged
    *	Need to maintain equal number of houses on each property
        *	Event tracker
*	Pass Go
    *	If location > MAX_LOC : location %= MAX_LOC, money += 200
*	Go to nearest [railroad, utility, etc.]
    *	Track spaces by int, keep a set of all 
*	Free Parking
    *	Globalish int with all of the money stored so far
*	Means of getting out of jail
    *	Either store number of turns and enumerate each turn
    *	Or store turn entered jail and do math
*	Selling Property
