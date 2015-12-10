package chessbook.service;


import java.lang.reflect.Type;
import java.util.Date;
import java.util.Optional;

import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import chessbook.lichess.model.GameList;
import chessbook.lichess.model.LiChessGame;
import chessbook.lichess.model.LiChessUser;


public class LiChessService {
	//SERVICE INFO AND ENDPOINTS
	private static final String URL_BASE = "http://en.lichess.org";
	private static final String API_USER = "/api/user/";
	private static final String API_USERS = "/api/user";
	private static final String API_GAMES = "/api/game";
	private static final String API_GAME = "/api/game/";
	private static final String API_EXPORT_GAME_PGN = "/game/export/";
	private static final String PARAM_PAGE_SIZE = "nb";
	private static final String PARAM_USERS_TEAM = "team";
	private static final String PARAM_USERNAME = "username";
	private static final String PARAM_RATED = "rated";
	private static final String PARAM_ANALYZED = "analysed";
	private static final String PARAM_WITH_ANALYSIS = "with_analysis";
	private static final String PARAM_WITH_MOVES = "with_moves";
	private static final String PARAM_WITH_OPENING ="with_opening";
	private static final String PARAM_WITH_FENS = "with_fens";
	
	
	
	//TODO 
	//Have basic service call example.
	//need to return data objects that can be stored instead of just JSON string.
	//add call to validate username 
	//maybe pop up an info IS THIS YOU???
	
	
	public static void getUserJsonString(String username) throws RuntimeException{

		Client client = Client.create();
		WebResource resource = client.resource(URL_BASE + API_USER + username);
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		String output = response.getEntity(String.class);
		System.out.println("Output from Server .... \n");
		System.out.println(output);

	}
	
	public static LiChessUser getUser(String username) {

		Client client = Client.create();
		WebResource resource = client.resource(URL_BASE + API_USER + username);
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		if (response.getStatus() != 200) {
			return null;
		}
		String output = response.getEntity(String.class);
		System.out.println("Output from Server .... \n");
		System.out.println(output);
		Gson g = new Gson();
		
		LiChessUser user = g.fromJson(output, LiChessUser.class);
					
		return user;

	}
	
	//TODO getGames
	public static void printUsersGames(String username){
		Client client = Client.create();
		WebResource resource = client.resource(URL_BASE + API_GAMES).queryParam(PARAM_USERNAME, username);
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		String output = response.getEntity(String.class);
		System.out.println("Output from Server .... \n");
		System.out.println(output);
		
		
	}
	
	public static GameList getUsersGames(String username){
		//this will just get 10 games right now. 
		//sent email to api owner to try and get paging
		//otherwise will just gank all games
		Client client = Client.create();
		WebResource resource = client.resource(URL_BASE + API_GAMES).queryParam(PARAM_USERNAME, username);
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		String output = response.getEntity(String.class);
		System.out.println("Output from Server .... \n");
		System.out.println(output);
		// Creates the json object which will manage the information received 
		GsonBuilder builder = new GsonBuilder(); 

		// Register an adapter to manage the date types as long values 
		builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() { 
			public Date deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2)
				throws JsonParseException {
				return new Date(json.getAsJsonPrimitive().getAsLong());
		} 
		});
		Gson g = builder.create();
		GameList games = g.fromJson(output,GameList.class);
		return games;
	}
	
	private static GameList getXRatedGamesForUser(String username, Long numberOfGames){
		Client client = Client.create();
		WebResource resource = client.resource(URL_BASE + API_GAMES).queryParam(PARAM_USERNAME, username).
						queryParam(PARAM_PAGE_SIZE, numberOfGames.toString()).
						queryParam(PARAM_RATED, "1").queryParam(PARAM_WITH_OPENING, "1");
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		String output = response.getEntity(String.class);
		System.out.println("Output from Server .... \n");
		System.out.println(output);
		// Creates the json object which will manage the information received 
		GsonBuilder builder = new GsonBuilder(); 

		// Register an adapter to manage the date types as long values 
		builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() { 
			public Date deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2)
				throws JsonParseException {
				return new Date(json.getAsJsonPrimitive().getAsLong());
		} 
		});
		Gson g = builder.create();
		GameList games = g.fromJson(output,GameList.class);
		return games;
		
	}
	
	public static GameList getAllRatedGamesForUser(LiChessUser user){
		return getXRatedGamesForUser(user.getUsername(), user.numberOfRatedGames());
	}
	
	public static GameList getRecentRatedGamesForUser(LiChessUser user,Long number){
		
		return number != null ? getXRatedGamesForUser(user.getUsername(), number):
			getXRatedGamesForUser(user.getUsername(), 20L);
	}
	
	//TODO get Game
	
	

}

