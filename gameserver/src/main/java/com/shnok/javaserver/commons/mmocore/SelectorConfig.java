package com.shnok.javaserver.commons.mmocore;

public final class SelectorConfig
{
	public int READ_BUFFER_SIZE = 64 * 1024;
	public int WRITE_BUFFER_SIZE = 64 * 1024;
	
	public int HELPER_BUFFER_COUNT = 20;
	public int HELPER_BUFFER_SIZE = 64 * 1024;
	
	/**
	 * Server will try to send MAX_SEND_PER_PASS packets per socket write call<br>
	 * however it may send less if the write buffer was filled before achieving this value.
	 */
	public int MAX_SEND_PER_PASS = 10;
	
	/**
	 * Server will try to read MAX_READ_PER_PASS packets per socket read call<br>
	 * however it may read less if the read buffer was empty before achieving this value.
	 */
	public int MAX_READ_PER_PASS = 10;
	
	/**
	 * Defines how much time (in milis) should the selector sleep, an higher value increases throughput but also increases latency(to a max of the sleep value itself).<BR>
	 * Also an extremely high value(usually > 100) will decrease throughput due to the server not doing enough sends per second (depends on max sends per pass).<BR>
	 * <BR>
	 * Recommended values:<BR>
	 * 1 for minimal latency.<BR>
	 * 10-30 for an latency/troughput trade-off based on your needs.<BR>
	 */
	public int SLEEP_TIME = 10;
	
	/**
	 * Used to enable/disable TCP_NODELAY which disable/enable Nagle's algorithm.<BR>
	 * <BR>
	 * Nagle's algorithm try to conserve bandwidth by minimizing the number of segments that are sent. When applications wish to decrease network latency and increase performance, they can disable Nagle's algorithm (that is enable TCP_NODELAY). Data will be sent earlier, at the cost of an increase
	 * in bandwidth consumption. The Nagle's algorithm is described in RFC 896.<BR>
	 * <BR>
	 * Summary, data will be sent earlier, thus lowering the ping, at the cost of a small increase in bandwidth consumption.
	 */
	public boolean TCP_NODELAY = true;
}