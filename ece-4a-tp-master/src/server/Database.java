package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import shared.Pokemon;

/**
 * This class represents the server database.
 * In this project, it will simply provides an API for the server to interact with the file system.
 * @author strift
 *
 */

public class Database {

	/**
	 * The name of the file used to store the data
	 */
	private File file;
	
	/**
	 * Constructor
	 * @param fileName the name of the file used to store the data
	 */
	public Database(String fileName) {
		this.file = new File(fileName);
	}
	
	/**
	 * Load the list of Pokemons stored inside the file and returns it
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Pokemon> loadPokemons() throws IOException, ClassNotFoundException {
		ArrayList<Pokemon> data = new ArrayList<Pokemon>();

		// This checks if the file actually exists
		if(this.file.exists() && !this.file.isDirectory()) { 
			/*
			 * TODO
			 * Classes you can use:
			 * - ArrayList<Pokemon>
			 * - FileInputStream
			 * - ObjectInputStream
			 */
			// Variables pour lire le fichier
			FileInputStream fis = new FileInputStream(this.file.getName());
			ObjectInputStream objectIn = new ObjectInputStream(fis);
            
			// On ajoute les Pokemons du fichier dans data (Java capable de serializer des objets)
            data = (ArrayList<Pokemon>) objectIn.readObject();
            
            // On ferme le fichier
		    objectIn.close();
		} else {
			System.out.println("Le fichier de sauvegarde n'existe pas.");
		}
		
		System.out.println(data.size() + " Pokémon(s) chargé(s) depuis la sauvegarde.");
		return data;
	}
	
	/**
	 * Save the list of Pokémons received in parameters
	 * @param data the list of Pokémons
	 * @throws IOException 
	 */
	public void savePokemons(ArrayList<Pokemon> data) throws IOException {
		/*
		 * TODO
		 * Classes you can use:
		 * - FileOutputStream
		 * - ObjectOutputStream
		 */
		// Variables
		FileOutputStream fos = new FileOutputStream(this.file.getName());
		ObjectOutputStream objectOutput = new ObjectOutputStream(fos);
		
		// Sauvegarde serializable de l'objet data
		objectOutput.writeObject(data);
		
		// On ferme
		objectOutput.close();
		
		System.out.println("Sauvegarde effectuée... " + data.size() + " Pokémon(s) en banque.");
	}
}
