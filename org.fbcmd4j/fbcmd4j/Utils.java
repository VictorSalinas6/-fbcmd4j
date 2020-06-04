package org.fbcmd4j;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.function.BiConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;
import facebook4j.Post;

public class Utils 
{
	private static final Logger logger = LogManager.getLogger(Utils.class);
	public static Properties loadConfigFile(String folder_name, String file_name) 
			throws IOException 
	{
		Properties properties = new Properties();
		Path configFile = Paths.get(folder_name, file_name);
		properties.load(Files.newInputStream(configFile));
		BiConsumer<Object, Object> emptyProperty = (k, v) -> 
		{
			if(((String)v).isEmpty())
				logger.info("Empty propertie '" + k + "' ");
		};
		properties.forEach(emptyProperty);
		return properties;
	}


	public static Facebook configFacebook(Properties properties) 
	{
		Facebook facebook_instance = new FacebookFactory().getInstance();
		facebook_instance.setOAuthAppId(properties.getProperty("oauth.appId"), properties.getProperty("oauth.appSecret"));
		facebook_instance.setOAuthPermissions(properties.getProperty("oauth.permissions"));
		facebook_instance.setOAuthAccessToken(new AccessToken(properties.getProperty("oauth.accessToken"), null));
		return facebook_instance;
	}
	
	public static void printPost(Post posts_) 
	{
		if(posts_.getStory() != null)
		{
			System.out.println("Story: " + posts_.getStory());
		}
		if(posts_.getMessage() != null)
		{
			System.out.println("Mensaje: " + posts_.getMessage());
		}
		System.out.println(". . . . . . . . . . . . . . . . . .");
	}
	
	public static void postStatus(String msg, Facebook fb) {
		try 
		{
			fb.postStatusMessage(msg);
		} 
		catch (FacebookException e) 
		{
			logger.error(e);
		}		
	}
	
	public static void postLink(String link, Facebook fb) {
		try 
		{
			fb.postLink(new URL(link));
		} 
		catch (MalformedURLException e) 
		{
			logger.error(e);
		} 
		catch (FacebookException e) 
		{
		logger.error(e);
		}
	}
	
	public static String Save_Post(String fileName, List<Post> posts) 
	{
		File file = new File(fileName + ".txt");
		try 
		{
			if(!file.exists()) 
			{ 
				file.createNewFile(); 
			}
		
		FileOutputStream f = new FileOutputStream(file);
  		
		for (Post p : posts) 
		{
			String m = "";
			if(p.getStory() != null)
			m += "Story: " + p.getStory() + "\n";
			if(p.getMessage() != null)
			m += "Mensaje: " + p.getMessage() + "\n" + ". . . . . . . . . . . . . . . . . . . .\n";
			f.write(m.getBytes());	
		}
			f.close();
			logger.info("Filepath name: '" + file.getName() + "'.");
			System.out.println("Filepath name '" + file.getName() + "'.");
		} 
		
		catch (IOException ex) {logger.error(ex);
	}	
        return file.getName();
  }	
}
