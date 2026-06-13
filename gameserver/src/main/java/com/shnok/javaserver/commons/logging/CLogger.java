package com.shnok.javaserver.commons.logging;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.shnok.javaserver.commons.lang.StringReplacer;

/**
 * Wraps the regular {@link Logger} to handle slf4j features, notably {} replacement.<br>
 * <br>
 * The current values should be used :
 * <ul>
 * <li>debug (Level.FINE): debug purposes (end replacement for Config.DEBUG).</li>
 * <li>info (Level.INFO) : send regular informations to the console.</li>
 * <li>warn (Level.WARNING): report failed integrity checks.</li>
 * <li>error (Level.SEVERE): report an issue involving data loss / leading to unexpected server behavior.</li>
 * </ul>
 */
public final class CLogger
{
	private final Logger _logger;
	
	public CLogger(String name)
	{
		_logger = Logger.getLogger(name);
	}
	
	private void log0(Level level, StackTraceElement caller, Object message, Throwable exception)
	{
		if (!_logger.isLoggable(level))
			return;
		
		if (caller == null)
			caller = new Throwable().getStackTrace()[2];
		
		_logger.logp(level, caller.getClassName(), caller.getMethodName(), String.valueOf(message), exception);
	}
	
	private void log0(Level level, StackTraceElement caller, Object message, Throwable exception, Object... args)
	{
		if (!_logger.isLoggable(level))
			return;
		
		if (caller == null)
			caller = new Throwable().getStackTrace()[2];
		
		_logger.logp(level, caller.getClassName(), caller.getMethodName(), format(String.valueOf(message), args), exception);
	}
	
	public void log(LogRecord logRecord)
	{
		_logger.log(logRecord);
	}
	
	/**
	 * Logs a message with Level.FINE.
	 * @param message : The object to log.
	 */
	public void debug(Object message)
	{
		log0(Level.FINE, null, message, null);
	}
	
	/**
	 * Logs a message with Level.FINE.
	 * @param message : The object to log.
	 * @param args : The passed arguments, used to format the message.
	 */
	public void debug(Object message, Object... args)
	{
		log0(Level.FINE, null, message, null, args);
	}
	
	/**
	 * Logs a message with Level.FINE.
	 * @param message : The object to log.
	 * @param exception : Log the caught exception.
	 */
	public void debug(Object message, Throwable exception)
	{
		log0(Level.FINE, null, message, exception);
	}
	
	/**
	 * Logs a message with Level.FINE.
	 * @param message : The object to log.
	 * @param exception : Log the caught exception.
	 * @param args : The passed arguments, used to format the message.
	 */
	public void debug(Object message, Throwable exception, Object... args)
	{
		log0(Level.FINE, null, message, exception, args);
	}
	
	/**
	 * Logs a message with Level.INFO.
	 * @param message : The object to log.
	 */
	public void info(Object message)
	{
		log0(Level.INFO, null, message, null);
	}
	
	/**
	 * Logs a message with Level.INFO.
	 * @param message : The object to log.
	 * @param args : The passed arguments, used to format the message.
	 */
	public void info(Object message, Object... args)
	{
		log0(Level.INFO, null, message, null, args);
	}
	
	/**
	 * Logs a message with Level.INFO.
	 * @param message : The object to log.
	 * @param exception : Log the caught exception.
	 */
	public void info(Object message, Throwable exception)
	{
		log0(Level.INFO, null, message, exception);
	}
	
	/**
	 * Logs a message with Level.INFO.
	 * @param message : The object to log.
	 * @param exception : Log the caught exception.
	 * @param args : The passed arguments, used to format the message.
	 */
	public void info(Object message, Throwable exception, Object... args)
	{
		log0(Level.INFO, null, message, exception, args);
	}
	
	/**
	 * Logs a message with Level.WARNING.
	 * @param message : The object to log.
	 */
	public void warn(Object message)
	{
		log0(Level.WARNING, null, message, null);
	}
	
	/**
	 * Logs a message with Level.WARNING.
	 * @param message : The object to log.
	 * @param args : The passed arguments, used to format the message.
	 */
	public void warn(Object message, Object... args)
	{
		log0(Level.WARNING, null, message, null, args);
	}
	
	/**
	 * Logs a message with Level.WARNING.
	 * @param message : The object to log.
	 * @param exception : Log the caught exception.
	 */
	public void warn(Object message, Throwable exception)
	{
		log0(Level.WARNING, null, message, exception);
	}
	
	/**
	 * Logs a message with Level.WARNING.
	 * @param message : The object to log.
	 * @param exception : Log the caught exception.
	 * @param args : The passed arguments, used to format the message.
	 */
	public void warn(Object message, Throwable exception, Object... args)
	{
		log0(Level.WARNING, null, message, exception, args);
	}
	
	/**
	 * Logs a message with Level.SEVERE.
	 * @param message : The object to log.
	 */
	public void error(Object message)
	{
		log0(Level.SEVERE, null, message, null);
	}
	
	/**
	 * Logs a message with Level.SEVERE.
	 * @param message : The object to log.
	 * @param args : The passed arguments, used to format the message.
	 */
	public void error(Object message, Object... args)
	{
		log0(Level.SEVERE, null, message, null, args);
	}
	
	/**
	 * Logs a message with Level.SEVERE.
	 * @param message : The object to log.
	 * @param exception : Log the caught exception.
	 */
	public void error(Object message, Throwable exception)
	{
		log0(Level.SEVERE, null, message, exception);
	}
	
	/**
	 * Logs a message with Level.SEVERE.
	 * @param message : The object to log.
	 * @param exception : Log the caught exception.
	 * @param args : The passed arguments, used to format the message.
	 */
	public void error(Object message, Throwable exception, Object... args)
	{
		log0(Level.SEVERE, null, message, exception, args);
	}
	
	/**
	 * Format the message, allowing to use {} as parameter. Avoid to generate String concatenation.
	 * @param message : the Object (String) message to format.
	 * @param args : the arguments to pass.
	 * @return a formatted String.
	 */
	private static final String format(String message, Object... args)
	{
		if (args == null || args.length == 0)
			return message;
		
		final StringReplacer sr = new StringReplacer(message);
		sr.replaceAll(args);
		return sr.toString();
	}
}