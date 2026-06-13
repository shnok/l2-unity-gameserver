package com.shnok.javaserver.gameserver.data.cache;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.commons.logging.CLogger;

import com.shnok.javaserver.gameserver.enums.CrestType;

/**
 * A cache storing clan crests under .dds format.<br>
 * <br>
 * Size integrity checks are made on crest save, deletion, get and also during server first load.
 */
public class CrestCache
{
	private static final CLogger LOGGER = new CLogger(CrestCache.class.getName());
	
	private static final String CRESTS_DIR = "data/crests/";
	
	private final Map<Integer, byte[]> _crests = new HashMap<>();
	
	public CrestCache()
	{
		load();
	}
	
	/**
	 * Initial method used to load crests data and store it in server memory.<br>
	 * <br>
	 * If a file doesn't meet integrity checks requirements, it is simply deleted.
	 */
	private final void load()
	{
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(CRESTS_DIR), new DdsFilter()))
		{
			for (Path filePath : stream)
			{
				final String fileName = filePath.getFileName().toString();
				
				// Load data on byte array.
				final byte[] data = Files.readAllBytes(filePath);
				
				// Test each crest type.
				for (CrestType type : CrestType.values())
				{
					// The crest type isn't matching, ignore it.
					if (!fileName.startsWith(type.getPrefix()))
						continue;
					
					// The data size isn't the required one, delete the file.
					if (data.length != type.getSize())
					{
						if (Files.deleteIfExists(filePath))
							LOGGER.warn("The data for crest {} is invalid. The crest has been deleted.", fileName);
					}
					// Feed the cache with crest id as key, and crest data as value.
					else
						_crests.put(Integer.valueOf(fileName.substring(type.getPrefix().length(), fileName.length() - 4)), data);
					
					break;
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error loading crest files.", e);
		}
		
		LOGGER.info("Loaded {} crests.", _crests.size());
	}
	
	/**
	 * Clean the crest cache, and reload it.
	 */
	public final void reload()
	{
		_crests.clear();
		
		load();
	}
	
	/**
	 * @param type : The {@link CrestType} to refer on. Size integrity check is made based on it.
	 * @param id : The crest id data to retrieve.
	 * @return a byte array or null if id wasn't found.
	 */
	public final byte[] getCrest(CrestType type, int id)
	{
		// Get crest data.
		byte[] data = _crests.get(id);
		
		// Crest data is not required type, return.
		if (data == null || data.length != type.getSize())
			return null;
		
		return data;
	}
	
	/**
	 * Remove the crest from both memory and file system.
	 * @param type : The {@link CrestType} to refer on. Size integrity check is made based on it.
	 * @param id : The crest id to delete.
	 */
	public final void removeCrest(CrestType type, int id)
	{
		// Get crest data.
		final byte[] data = _crests.get(id);
		if (data == null || data.length != type.getSize())
			return;
		
		// Remove from cache.
		_crests.remove(id);
		
		// Delete file.
		final Path filePath = Paths.get(CRESTS_DIR, type.getPrefix() + id + ".dds");
		try
		{
			Files.deleteIfExists(filePath);
		}
		catch (Exception e)
		{
			LOGGER.error("Error deleting crest file: {}.", e, filePath.getFileName());
		}
	}
	
	/**
	 * Store the crest as a physical file and in cache memory.
	 * @param type : The {@link CrestType} used to register the crest. Crest name uses it.
	 * @param id : The crest id to register this new crest.
	 * @param data : The crest data to store.
	 * @return true if the crest has been successfully saved, false otherwise.
	 */
	public final boolean saveCrest(CrestType type, int id, byte[] data)
	{
		// Create the file.
		final Path filePath = Paths.get(CRESTS_DIR, type.getPrefix() + id + ".dds");
		
		// Verify the data size integrity.
		if (data.length != type.getSize())
		{
			LOGGER.warn("The data for crest {} is invalid. Saving process is aborted.", filePath.getFileName());
			return false;
		}
		
		// Save the crest file with given data.
		try
		{
			Files.write(filePath, data);
		}
		catch (Exception e)
		{
			LOGGER.error("Error saving crest file: {}.", e, filePath.getFileName());
			return false;
		}
		
		// Feed the cache with crest data.
		_crests.put(id, data);
		
		return true;
	}
	
	private static class DdsFilter implements DirectoryStream.Filter<Path>
	{
		@Override
		public boolean accept(Path file)
		{
			final String fileName = file.getFileName().toString();
			
			return (fileName.startsWith("Crest_") || fileName.startsWith("LargeCrest_") || fileName.startsWith("AllyCrest_")) && fileName.endsWith(".dds");
		}
	}
	
	public static CrestCache getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final CrestCache INSTANCE = new CrestCache();
	}
}