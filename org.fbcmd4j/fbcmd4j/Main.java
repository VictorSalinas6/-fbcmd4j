package org.fbcmd4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.Post;
import facebook4j.ResponseList;

public class Main {
	static final Logger logger = LogManager.getLogger(Main.class);

	private static final String CONFIG_DIR = "config";
	private static final String CONFIG_FILE = "fbcmd4j.properties";
	private static int seleccion = 0;

	public static void main(String[] args) 
	{
		logger.info("Iniciando app");
		Facebook fb =  null;
		Properties props = null;

		try 
		{
			props = Utils.loadConfigFile(CONFIG_DIR, CONFIG_FILE);
		} catch (IOException ex) 
		{
			logger.error(ex);
		}
	
		try 
		{
			Scanner scanner = new Scanner(System.in);
			while(true) 
			{
				fb = Utils.configFacebook(props);
				System.out.println("Facebook Console Client \n");
				System.out.println("(1) NewsFeed");
				System.out.println("(2) Wall");
				System.out.println("(3) Post post");
				System.out.println("(4) Post Url");
				System.out.println("(5) Log out");
				System.out.println("Select and option:");	
				
				try {
					seleccion= scanner.nextInt();
					scanner.nextLine();
					switch (seleccion) 
					{
					case 1:
						System.out.println("-- Show NewsFeed --");
						ResponseList<Post> newsFeed = fb.getFeed();
						for (Post p : newsFeed) 
						{
							Utils.printPost(p);
						}
						 Save_Facebook("NewsFeed", newsFeed,scanner);
						break;
					case 2:
						System.out.println("-- Show Wall --");
						ResponseList<Post> wall = fb.getPosts();
						for (Post p : wall) 
						{
							Utils.printPost(p);
						}		
						 Save_Facebook("Wall", wall, scanner);
						break;
					case 3:
						System.out.println("-- Create Post --: ");
						String estado = scanner.nextLine();
						Utils.postStatus(estado, fb);
						break;
					case 4:
						System.out.println("-- Post Url --: ");
						String link = scanner.nextLine();
						Utils.postLink(link, fb);
						break;
					case 5:
						System.out.println("Log out");
						System.exit(0);
						break;
					default:
						logger.error("Opción inválida");
						break;
					}
				} 
				catch (InputMismatchException e) 
				{
					System.out.println("Ocurrió un error, revisar log.");
					logger.error("Opción inválida. %s. \n", e.getClass());
				} 
				catch (FacebookException e) 
				{
					System.out.println("Ocurrió un error, revisar log.");
					logger.error(e.getErrorMessage());
				} 
				catch (Exception e) 
				{
					System.out.println("Ocurrió un error, revisar log.");
					logger.error(e);
				}
				System.out.println();
			}
		} 
		catch (Exception ex) {
			logger.error(ex);
		}
	}
	
	
	public static void Save_Facebook(String fileName, ResponseList<Post> posts, Scanner scanner) 
	{
		System.out.println("¿Quieres guardar lo mostrado en un archivo txt?");
		String seleccion= scanner.nextLine();
		if (seleccion.contains("Si") || seleccion.contains("si"))
		{
		List<Post> post = new ArrayList<>();
		int num = 0;
		while(num <= 0) 
		{
			try 
			{
			System.out.println("¿Cuantas lineas quieres guaradar?");
			num = Integer.parseInt(scanner.nextLine());					
			if(num <= 0) 
			{
				System.out.println("Ingrese un numero valido!!!!!");
			} 
			else 
			{
				for(int i = 0; i<num; i++)
				{
					if(i>posts.size()-1) break;
					post.add(posts.get(i));
					}
				}
			} 
			catch(NumberFormatException e) 
			{
				logger.error(e);
			}
		}
	Utils.Save_Post(fileName, post);
	}
  }
}
