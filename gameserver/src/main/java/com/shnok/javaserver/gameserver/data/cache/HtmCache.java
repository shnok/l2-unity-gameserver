package com.shnok.javaserver.gameserver.data.cache;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.commons.logging.CLogger;

/**
 * A cache storing HTMs content.<br>
 * <br>
 * HTMs are loaded lazily, on request, then their {@link String} content can be retrieved using path hashcode.
 */
public class HtmCache
{
	private static final CLogger LOGGER = new CLogger(HtmCache.class.getName());
	
	private final Map<Integer, String> _htmCache = new HashMap<>();
	
	/**
	 * Clean the HTM cache.
	 */
	public void reload()
	{
		LOGGER.info("HtmCache has been cleared ({} entries).", _htmCache.size());
		
		_htmCache.clear();
	}
	
	/**
	 * Load and store the HTM file content.
	 * @param filePath : The path of the file to be cached.
	 * @return the content of the file under a {@link String}.
	 */
	private String loadFile(Path filePath)
	{
		try (BufferedReader br = Files.newBufferedReader(filePath, StandardCharsets.UTF_8))
		{
			final StringBuilder sb = new StringBuilder();
			
			String line;
			while ((line = br.readLine()) != null)
				sb.append(line).append('\n');
			
			final String content = sb.toString().replaceAll("\r\n", "\n");
			_htmCache.put(filePath.toString().replace("\\", "/").hashCode(), content);
			return content;
		}
		catch (Exception e)
		{
			LOGGER.error("Error caching HTM file.", e);
			return null;
		}
	}
	
	/**
	 * Check if an HTM exists and can be loaded. If so, it is loaded and stored.
	 * @param path : The path to the HTM.
	 * @return true if the HTM can be loaded.
	 */
	public boolean isLoadable(String path)
	{
		final Path filePath = Paths.get(path);
		if (!isValidHtmFile(filePath))
			return false;
		
		return loadFile(filePath) != null;
	}
	
	/**
	 * Return the HTM content given by filename. Test the cache first, then try to load the file if unsuccessful.
	 * @param path : The path to the HTM.
	 * @return the {@link String} content if filename exists, otherwise returns null.
	 */
	public String getHtm(String path)
	{
		if (path == null || path.isEmpty())
			return null;
		
		String content = _htmCache.get(path.hashCode());
		if (content == null)
		{
			final Path filePath = Paths.get(path);
			if (isValidHtmFile(filePath))
				content = loadFile(filePath);
		}
		return content;
	}
	
	/**
	 * Return content of html message given by filename. In case filename does not exist, returns notice.
	 * @param path : The path to the HTM.
	 * @return the {@link String} content if filename exists, otherwise returns formatted default message.
	 */
	public String getHtmForce(String path)
	{
		String content = getHtm(path);
		if (content == null)
		{
			content = "<html><body>My html is missing:<br>" + path + "</body></html>";
			LOGGER.warn("Following HTM {} is missing.", path);
		}
		
		return content;
	}
	
	/**
	 * Validate whether a file is an HTML file (.htm or .html).
	 * @param filePath : The path to the file.
	 * @return True if the file is a valid HTML file or false otherwise.
	 */
	private static boolean isValidHtmFile(Path filePath)
	{
		if (!Files.isRegularFile(filePath))
			return false;
		
		final String fileName = filePath.getFileName().toString().toLowerCase();
		return fileName.endsWith(".htm") || fileName.endsWith(".html");
	}
	
	public static HtmCache getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final HtmCache INSTANCE = new HtmCache();
	}
}