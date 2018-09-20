package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import shared.Request;
import shared.Pokemon;

/**
 * This class represents the server application, which is a Pokémon Bank.
 * It is a shared account: everyone's Pokémons are stored together.
 * @author strift
 *
 */
public class PokemonBank {
	
	public static final int SERVER_PORT = 3000;
	public static final String DB_FILE_NAME = "pokemons.db";
	
	/**
	 * The database instance
	 */
	private Database db;
	
	/**
	 * The ServerSocket instance
	 */
	private ServerSocket server;
	
	/**
	 * The Pokémons stored in memory
	 */
	private ArrayList<Pokemon> pokemons;
	
	/**
	 * Constructor
	 * @param port		the port on which the server should listen
	 * @param fileName	the name of the file used for the database
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public PokemonBank() throws IOException, ClassNotFoundException {
		/*
		 * TODO
		 * Here, you should initialize the Database and ServerSocket instances.
		 */
		// Initialisation Database
		db = new Database(DB_FILE_NAME);
		
		// Initialisation ServerSocket
		server = new ServerSocket(SERVER_PORT);

		System.out.println("Banque Pokémon (" + DB_FILE_NAME + ") démarrée sur le port " + SERVER_PORT);
		
		// Let's load all the Pokémons stored in database
		this.pokemons = this.db.loadPokemons();
		this.printState();
	}
	
	/**
	 * The main loop logic of your application goes there.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public void handleClient() throws IOException, ClassNotFoundException {
		System.out.println("En attente de connexion...");
		/*
		 * TODO
		 * Here, you should wait for a client to connect.
		 */
		// Attente d'une connexion
		Socket client = server.accept();
		System.out.println("Un client s'est connecte");
		
		/*
		 * TODO
		 * You will one stream to read and one to write.
		 * Classes you can use:
		 * - ObjectInputStream
		 * - ObjectOutputStream
		 * - BankOperation -> Request
		 */
		// Variables I/O
		ObjectInputStream objectInput = new ObjectInputStream(client.getInputStream());
		ObjectOutputStream objectOutput = new ObjectOutputStream(client.getOutputStream());
		
		// For as long as the client wants it
		boolean running = true;
		while (running) {
			/*
			 * TODO
			 * Here you should read the stream to retrieve a Request object
			 */
			// Attribution de l'enumeration Request selon le choix saisi
			Request request;
			request = (Request) objectInput.readObject();
			
			/*
			 * Note: the server will only respond with String objects.
			 */
			switch(request) {
			case LIST:
				System.out.println("Request: LIST");
				if (this.pokemons.size() == 0) {
					/*
					 * TODO
					 * There is no Pokémons, so just send a message to the client using the output stream.
					 */
					// Output no Pokemon
					objectOutput.writeObject("Il n'y a pas de Pokemon.");
					objectOutput.flush();
				} else {
					/*
					 * TODO
					 * Here you need to build a String containing the list of Pokémons, then write this String
					 * in the output stream.
					 * Classes you can use:
					 * - StringBuilder
					 * - String
					 * - the output stream
					 */
					// On charge les pokemon de la database + declaration du stringbuilder
					pokemons = db.loadPokemons();
                    StringBuilder sb = new StringBuilder();
                    
                    // Pour chaque pokemon, on l'ajoute
                    for (int i = 0; i < pokemons.size(); i++) {
                        sb.append(pokemons.get(i));
                        sb.append("\n");
                    }
                    
                    // On ecrit dans la liste String
                    objectOutput.writeObject(sb);
                    objectOutput.flush();
				}
				break;
				
			case STORE:
				System.out.println("Request: STORE");
				/*
				 * TODO
				 * If the client sent a STORE request, the next object in the stream should be a Pokémon.
				 * You need to retrieve that Pokémon and add it to the ArrayList.
				 */
				// On recupere l'objet Pokemon et on l'ajoute a la liste
				Pokemon pokemonAstocker = (Pokemon) objectInput.readObject();
				pokemons.add(pokemonAstocker);
				objectOutput.flush();
				
				/*
				 * TODO
				 * Then, send a message to the client so he knows his Pokémon is safe.
				 */
				// Affichage au client du message annoncant que le Pokemon a bien ete recu
				objectOutput.writeObject(pokemonAstocker.getName() + " (niveau " + pokemonAstocker.getLevel() + ") a bien ete recu.\n");
				objectOutput.flush();

				break;
				
			case CLOSE:
				System.out.println("Request: CLOSE");
				/*
				 * TODO
				 * Here, you should use the output stream to send a nice 'Au revoir !' message to the client. 
				 */
				
				// Fermeture de la connexion
				System.out.println("Fermeture de la connexion...");
				running = false;
				break;
			}
			this.printState();
		};
		
		/*
		 * TODO
		 * Now you can close both I/O streams, and the client socket.
		 */
		// Fermeture des I/O stream
		objectInput.close();
		objectOutput.close();
		
		// Fermeture du client socket
        client.close();
		/*
		 * TODO
		 * Now that everything is done, let's update the database.
		 */
        // On sauvegarde les Pokemons
		db.savePokemons(pokemons);
	}
	
	/**
	 * Print the current state of the bank
	 */
	private void printState() {
		System.out.print("[");
		for (int i = 0; i < this.pokemons.size(); i++) {
			if (i > 0) {
				System.out.print(", ");
			}
			System.out.print(this.pokemons.get(i));
		}
		System.out.println("]");
	}
	
	/**
	 * Stops the server.
	 * Note: This function will never be called in this project.
	 * @throws IOException 
	 */
	public void stop() throws IOException {
		this.server.close();
	}
}
