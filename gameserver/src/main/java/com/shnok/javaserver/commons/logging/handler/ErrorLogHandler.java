package com.shnok.javaserver.commons.logging.handler;

import java.io.IOException;
import java.util.logging.FileHandler;

public class ErrorLogHandler extends FileHandler
{
	public ErrorLogHandler() throws IOException, SecurityException
	{
		super();
	}
}