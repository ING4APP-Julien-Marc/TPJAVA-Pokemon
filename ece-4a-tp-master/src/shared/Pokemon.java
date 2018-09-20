package shared;

import java.io.Serializable;

/**
 * This class represents a Pokémon. You do not need to update this code.
 * @author strift
 *
 */
@SuppressWarnings("serial")
public class Pokemon implements Serializable {
	
	private String name;
	private int level;
	
	public Pokemon(String name, int level) {
		this.name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
		this.level = level;
	}
	
	public Pokemon(String name) {
		this.name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
		this.level = 1;
	}
	
	public String toString() {
		return this.name + " (niveau " + this.level + ")";  
	}

	// Getters et Setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
