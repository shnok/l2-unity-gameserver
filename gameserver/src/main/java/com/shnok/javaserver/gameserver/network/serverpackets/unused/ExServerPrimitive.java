package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import java.awt.Color;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.shnok.javaserver.commons.geometry.basic.Line2D;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * A packet used to draw points and lines on client.<br>
 * <br>
 * <b>Note:</b><br>
 * Names in points and lines are bugged they will appear even when not looking at them.<br>
 * Packet must be sent using sendTo() method, otherwise nested packets are not sent.
 */
public class ExServerPrimitive extends L2GameServerPacket
{
	private static final int MAX_SIZE = 16000;
	
	private final Set<Point> _points = ConcurrentHashMap.newKeySet();
	private final Set<Line> _lines = ConcurrentHashMap.newKeySet();
	
	private final String _name;
	
	private final int _x;
	private final int _y;
	private final int _z;
	private final int _index;
	
	private int _size = 0;
	private ExServerPrimitive _next;
	
	/**
	 * @param name : An unique name this will be used to replace lines if second packet is sent.
	 * @param location : The {@link Player}'s enter world {@link Location}.
	 */
	public ExServerPrimitive(String name, Location location)
	{
		_name = name;
		_x = location.getX();
		_y = location.getY();
		_z = location.getZ();
		_index = 0;
	}
	
	/**
	 * Constructor of a dummy packet as a simple container. Should not be send out.
	 */
	public ExServerPrimitive()
	{
		_name = null;
		_x = 0;
		_y = 0;
		_z = 0;
		_index = -1;
	}
	
	/**
	 * Constructor of a following packet. Used, when parent packet is out of capacity.
	 * @param parent : A parent {@link ExServerPrimitive} packet used for nesting.
	 */
	public ExServerPrimitive(ExServerPrimitive parent)
	{
		_name = parent._name;
		_x = parent._x;
		_y = parent._y;
		_z = parent._z;
		_index = parent._index + 1;
	}
	
	/**
	 * Add a point to the {@link ExServerPrimitive} ; create next {@link ExServerPrimitive} if out of capacity.
	 * @param point : Added {@link Point}.
	 */
	private void addPoint(Point point)
	{
		// Check size and add point, if free capacity (or add if dummy packet).
		if (_size < MAX_SIZE || _index < 0)
		{
			_size += point.size();
			_points.add(point);
			return;
		}
		
		// Capacity not enough, create next packet (if needed).
		if (_next == null)
			_next = new ExServerPrimitive(this);
		
		// Add point to next packet.
		_next.addPoint(point);
	}
	
	/**
	 * Add a point to be displayed on client.
	 * @param name : The {@link String} name that will be displayed over the point.
	 * @param color : The used {@link Color}.
	 * @param isNameColored : If true, the name will be colored.
	 * @param x : The x coordinate for this point.
	 * @param y : The y coordinate for this point.
	 * @param z : The z coordinate for this point.
	 */
	public void addPoint(String name, Color color, boolean isNameColored, int x, int y, int z)
	{
		addPoint(new Point(name, color, isNameColored, x, y, z));
	}
	
	/**
	 * Add a point to be displayed on client.
	 * @param name : The {@link String} name that will be displayed over the point.
	 * @param color : The used {@link Color}.
	 * @param isNameColored : If true, the name will be colored.
	 * @param loc : The {@link Location} to take coordinates for this point.
	 */
	public void addPoint(String name, Color color, boolean isNameColored, Location loc)
	{
		addPoint(name, color, isNameColored, loc.getX(), loc.getY(), loc.getZ());
	}
	
	/**
	 * Add a point to be displayed on client.
	 * @param color : The used {@link Color}.
	 * @param x : The x coordinate for this point.
	 * @param y : The y coordinate for this point.
	 * @param z : The z coordinate for this point.
	 */
	public void addPoint(Color color, int x, int y, int z)
	{
		addPoint("", color, false, x, y, z);
	}
	
	/**
	 * Add a point to be displayed on client.
	 * @param color : The used {@link Color}.
	 * @param loc : The {@link Location} to take coordinates for this point.
	 */
	public void addPoint(Color color, Location loc)
	{
		addPoint("", color, false, loc.getX(), loc.getY(), loc.getZ());
	}
	
	/**
	 * Add a line to the {@link ExServerPrimitive} ; create next {@link ExServerPrimitive} if out of capacity.
	 * @param line : Added {@link Line}.
	 */
	private void addLine(Line line)
	{
		// Check size and add line, if free capacity (or add if dummy packet).
		if (_size < MAX_SIZE || _index < 0)
		{
			_size += line.size();
			_lines.add(line);
			return;
		}
		
		// Capacity not enough, create next packet (if needed).
		if (_next == null)
			_next = new ExServerPrimitive(this);
		
		// Add line to next packet.
		_next.addLine(line);
	}
	
	/**
	 * Add a line to be displayed on client.
	 * @param name : The {@link String} name that will be displayed over the point.
	 * @param color : The used {@link Color}.
	 * @param isNameColored : If true, the name will be colored.
	 * @param x : The x coordinate for this line start point.
	 * @param y : The y coordinate for this line start point.
	 * @param z : The z coordinate for this line start point.
	 * @param x2 : The x coordinate for this line end point.
	 * @param y2 : The y coordinate for this line end point.
	 * @param z2 : The z coordinate for this line end point.
	 */
	public void addLine(String name, Color color, boolean isNameColored, int x, int y, int z, int x2, int y2, int z2)
	{
		addLine(new Line(name, color, isNameColored, x, y, z, x2, y2, z2));
	}
	
	/**
	 * Add a line to be displayed on client.
	 * @param name : The {@link String} name that will be displayed over the point.
	 * @param color : The used {@link Color}.
	 * @param isNameColored : If true, the name will be colored.
	 * @param loc : The {@link Location} to take coordinates for this line start point.
	 * @param x2 : The x coordinate for this line end point.
	 * @param y2 : The y coordinate for this line end point.
	 * @param z2 : The z coordinate for this line end point.
	 */
	public void addLine(String name, Color color, boolean isNameColored, Location loc, int x2, int y2, int z2)
	{
		addLine(name, color, isNameColored, loc.getX(), loc.getY(), loc.getZ(), x2, y2, z2);
	}
	
	/**
	 * Add a line to be displayed on client.
	 * @param name : The {@link String} name that will be displayed over the point.
	 * @param color : The used {@link Color}.
	 * @param isNameColored : If true, the name will be colored.
	 * @param line : The {@link Line2D} to take coordinates.
	 * @param z : The z axis used to draw the line.
	 */
	public void addLine(String name, Color color, boolean isNameColored, Line2D line, int z)
	{
		addLine(name, color, isNameColored, line.getP1x(), line.getP1y(), z, line.getP2x(), line.getP2y(), z);
	}
	
	/**
	 * Add a line to be displayed on client.
	 * @param name : The {@link String} name that will be displayed over the point.
	 * @param color : The used {@link Color}.
	 * @param isNameColored : If true, the name will be colored.
	 * @param x : The x coordinate for this line end point.
	 * @param y : The y coordinate for this line end point.
	 * @param z : The z coordinate for this line end point.
	 * @param loc : The {@link Location} to take coordinates for this line end point.
	 */
	public void addLine(String name, Color color, boolean isNameColored, int x, int y, int z, Location loc)
	{
		addLine(name, color, isNameColored, x, y, z, loc.getX(), loc.getY(), loc.getZ());
	}
	
	/**
	 * Add a line to be displayed on client.
	 * @param name : The {@link String} name that will be displayed over the point.
	 * @param color : The used {@link Color}.
	 * @param isNameColored : If true, the name will be colored.
	 * @param loc : The {@link Location} to take coordinates for this line start point.
	 * @param loc2 : The {@link Location} to take coordinates for this line end point.
	 */
	public void addLine(String name, Color color, boolean isNameColored, Location loc, Location loc2)
	{
		addLine(name, color, isNameColored, loc.getX(), loc.getY(), loc.getZ(), loc2.getX(), loc2.getY(), loc2.getZ());
	}
	
	/**
	 * Add a line to be displayed on client.
	 * @param color : The used {@link Color}.
	 * @param x : The x coordinate for this line start point.
	 * @param y : The y coordinate for this line start point.
	 * @param z : The z coordinate for this line start point.
	 * @param x2 : The x coordinate for this line end point.
	 * @param y2 : The y coordinate for this line end point.
	 * @param z2 : The z coordinate for this line end point.
	 */
	public void addLine(Color color, int x, int y, int z, int x2, int y2, int z2)
	{
		addLine("", color, false, x, y, z, x2, y2, z2);
	}
	
	/**
	 * Add a line to be displayed on client.
	 * @param color : The used {@link Color}.
	 * @param loc : The {@link Location} to take coordinates for this line start point.
	 * @param x2 : The x coordinate for this line end point.
	 * @param y2 : The y coordinate for this line end point.
	 * @param z2 : The z coordinate for this line end point.
	 */
	public void addLine(Color color, Location loc, int x2, int y2, int z2)
	{
		addLine("", color, false, loc.getX(), loc.getY(), loc.getZ(), x2, y2, z2);
	}
	
	/**
	 * Add a line to be displayed on client.
	 * @param color : The used {@link Color}.
	 * @param x : The x coordinate for this line end point.
	 * @param y : The y coordinate for this line end point.
	 * @param z : The z coordinate for this line end point.
	 * @param loc : The {@link Location} to take coordinates for this line end point.
	 */
	public void addLine(Color color, int x, int y, int z, Location loc)
	{
		addLine("", color, false, x, y, z, loc.getX(), loc.getY(), loc.getZ());
	}
	
	/**
	 * Add a line to be displayed on client.
	 * @param color : The used {@link Color}.
	 * @param loc : The {@link Location} to take coordinates for this line start point.
	 * @param loc2 : The {@link Location} to take coordinates for this line end point.
	 */
	public void addLine(Color color, Location loc, Location loc2)
	{
		addLine("", color, false, loc.getX(), loc.getY(), loc.getZ(), loc2.getX(), loc2.getY(), loc2.getZ());
	}
	
	/**
	 * Add a line to be displayed on client.
	 * @param color : The used {@link Color}.
	 * @param line : The {@link Line2D} to take coordinates.
	 * @param z : The z axis used to draw the line.
	 */
	public void addLine(Color color, Line2D line, int z)
	{
		addLine("", color, false, line.getP1x(), line.getP1y(), z, line.getP2x(), line.getP2y(), z);
	}
	
	/**
	 * Add a rectangle to be displayed on client.
	 * @param name : The {@link String} name that will be displayed over the point.
	 * @param color : The used {@link Color}.
	 * @param isNameColored : If true, the name will be colored.
	 * @param x : The x coordinate of one corner.
	 * @param y : The y coordinate of one corner.
	 * @param x2 : The x coordinate of opposite corner.
	 * @param y2 : The y coordinate of opposite corner.
	 * @param z : The z coordinate of all corners.
	 */
	public void addRectangle(String name, Color color, boolean isNameColored, int x, int y, int x2, int y2, int z)
	{
		addLine(name, color, isNameColored, x, y, z, x, y2, z);
		addLine(name, color, isNameColored, x2, y2, z, x, y2, z);
		addLine(name, color, isNameColored, x2, y2, z, x2, y, z);
		addLine(name, color, isNameColored, x, y, z, x2, y, z);
	}
	
	/**
	 * Add a rectangle to be displayed on client.
	 * @param name : The {@link String} name that will be displayed over the point.
	 * @param color : The used {@link Color}.
	 * @param isNameColored : If true, the name will be colored.
	 * @param loc : The {@link Location} of one corner, the height of all corners is taken from this {@link Location}.
	 * @param loc2 : The {@link Location} of opposite corner.
	 */
	public void addRectangle(String name, Color color, boolean isNameColored, Location loc, Location loc2)
	{
		addRectangle(name, color, isNameColored, loc.getX(), loc.getY(), loc2.getX(), loc2.getY(), loc.getZ());
	}
	
	/**
	 * Add a rectangle to be displayed on client.
	 * @param color : The used {@link Color}.
	 * @param x : The x coordinate of one corner.
	 * @param y : The y coordinate of one corner.
	 * @param x2 : The x coordinate of opposite corner.
	 * @param y2 : The y coordinate of opposite corner.
	 * @param z : The z coordinate of all corners.
	 */
	public void addRectangle(Color color, int x, int y, int x2, int y2, int z)
	{
		addRectangle("", color, false, x, y, x, y2, z);
	}
	
	/**
	 * Add a rectangle to be displayed on client.
	 * @param color : The used {@link Color}.
	 * @param loc : The {@link Location} of one corner, the height of all corners is taken from this {@link Location}.
	 * @param loc2 : The {@link Location} of opposite corner.
	 */
	public void addRectangle(Color color, Location loc, Location loc2)
	{
		addRectangle("", color, false, loc.getX(), loc.getY(), loc2.getX(), loc2.getY(), loc.getZ());
	}
	
	/**
	 * Add a square to be displayed on client.
	 * @param name : The {@link String} name that will be displayed over the point.
	 * @param color : The used {@link Color}.
	 * @param isNameColored : If true, the name will be colored.
	 * @param x : The x coordinate of one corner.
	 * @param y : The y coordinate of one corner.
	 * @param z : The z coordinate of all corners.
	 * @param size : The size of square (may be negative)
	 */
	public void addSquare(String name, Color color, boolean isNameColored, int x, int y, int z, int size)
	{
		int x2 = x + size;
		int y2 = y + size;
		addLine(name, color, isNameColored, x, y, z, x, y2, z);
		addLine(name, color, isNameColored, x2, y2, z, x, y2, z);
		addLine(name, color, isNameColored, x2, y2, z, x2, y, z);
		addLine(name, color, isNameColored, x, y, z, x2, y, z);
	}
	
	/**
	 * Add a square to be displayed on client.
	 * @param name : The {@link String} name that will be displayed over the point.
	 * @param color : The used {@link Color}.
	 * @param isNameColored : If true, the name will be colored.
	 * @param loc : The {@link Location} of one corner, the height of all corners is taken from this {@link Location}.
	 * @param size : The size of square (may be negative)
	 */
	public void addSquare(String name, Color color, boolean isNameColored, Location loc, int size)
	{
		addSquare(name, color, isNameColored, loc.getX(), loc.getY(), loc.getZ(), size);
	}
	
	/**
	 * Add a square to be displayed on client.
	 * @param color : The used {@link Color}.
	 * @param x : The x coordinate of one corner.
	 * @param y : The y coordinate of one corner.
	 * @param z : The z coordinate of all corners.
	 * @param size : The size of square (may be negative)
	 */
	public void addSquare(Color color, int x, int y, int z, int size)
	{
		addSquare("", color, false, x, y, z, size);
	}
	
	/**
	 * Add a square to be displayed on client.
	 * @param color : The used {@link Color}.
	 * @param loc : The {@link Location} of one corner, the height of all corners is taken from this {@link Location}.
	 * @param size : The size of square (may be negative)
	 */
	public void addSquare(Color color, Location loc, int size)
	{
		addSquare("", color, false, loc.getX(), loc.getY(), loc.getZ(), size);
	}
	
	/**
	 * Adds points and lines from existing {@link ExServerPrimitive} packet.
	 * @param esp : The {@link ExServerPrimitive} packet to use points and lines from.
	 */
	public void addAll(ExServerPrimitive esp)
	{
		for (Point p : esp._points)
			addPoint(p);
		for (Line l : esp._lines)
			addLine(l);
	}
	
	/**
	 * Reset both lines and points {@link List}s.
	 */
	public void reset()
	{
		_lines.clear();
		_points.clear();
		_size = 0;
		
		if (_next != null)
			_next.reset();
	}
	
	/**
	 * Send packet to the {@link Player}. If out of capacity, send more packets.
	 * @param player : The {@link Player} to send packet(s) to.
	 */
	public void sendTo(Player player)
	{
		// Packet is empty, add dummy points (happens at first packet only).
		if (_size == 0)
			addPoint(Color.WHITE, _x, _y, 16384);
		
		// Send packet.
		player.sendPacket(this);
		
		// No next packet, return.
		if (_next == null)
			return;
		
		// Check next packet.
		if (_next._size == 0)
		{
			// Next packet is empty, add dummy point.
			_next.addPoint(Color.WHITE, _x, _y, 16384);
			
			// Send packet and remove next packet.
			_next.sendTo(player);
			_next = null;
		}
		else
			// Next packet is not empty, send packet.
			_next.sendTo(player);
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x24);
		if (_index == 0)
			writeS(_name);
		else
			writeS(_name == null ? "null" + _index : _name + _index);
		writeD(_x);
		writeD(_y);
		writeD(_z);
		writeD(Integer.MAX_VALUE); // has to do something with display range and angle
		writeD(Integer.MAX_VALUE); // has to do something with display range and angle
		
		writeD(_points.size() + _lines.size());
		
		for (Point point : _points)
		{
			writeC(1); // Its the type in this case Point
			writeS(point.getName());
			int color = point.getColor();
			writeD((color >> 16) & 0xFF); // R
			writeD((color >> 8) & 0xFF); // G
			writeD(color & 0xFF); // B
			writeD(point.isNameColored() ? 1 : 0);
			writeD(point.getX());
			writeD(point.getY());
			writeD(point.getZ());
		}
		
		for (Line line : _lines)
		{
			writeC(2); // Its the type in this case Line
			writeS(line.getName());
			int color = line.getColor();
			writeD((color >> 16) & 0xFF); // R
			writeD((color >> 8) & 0xFF); // G
			writeD(color & 0xFF); // B
			writeD(line.isNameColored() ? 1 : 0);
			writeD(line.getX());
			writeD(line.getY());
			writeD(line.getZ());
			writeD(line.getX2());
			writeD(line.getY2());
			writeD(line.getZ2());
		}
	}
	
	private static class Point
	{
		protected final String _name;
		private final int _color;
		private final boolean _isNameColored;
		private final int _x;
		private final int _y;
		private final int _z;
		
		public Point(String name, Color color, boolean isNameColored, int x, int y, int z)
		{
			_name = name;
			_color = color.getRGB();
			_isNameColored = isNameColored;
			_x = x;
			_y = y;
			_z = z;
		}
		
		public String getName()
		{
			return _name;
		}
		
		public int getColor()
		{
			return _color;
		}
		
		public boolean isNameColored()
		{
			return _isNameColored;
		}
		
		public int getX()
		{
			return _x;
		}
		
		public int getY()
		{
			return _y;
		}
		
		public int getZ()
		{
			return _z;
		}
		
		public int size()
		{
			// 1 byte, string (2 bytes per character + 2 termination bytes), 7 integers (4 bytes)
			return _name == null ? 31 : 31 + 2 * _name.length();
		}
	}
	
	private static class Line extends Point
	{
		private final int _x2;
		private final int _y2;
		private final int _z2;
		
		public Line(String name, Color color, boolean isNameColored, int x, int y, int z, int x2, int y2, int z2)
		{
			super(name, color, isNameColored, x, y, z);
			_x2 = x2;
			_y2 = y2;
			_z2 = z2;
		}
		
		public int getX2()
		{
			return _x2;
		}
		
		public int getY2()
		{
			return _y2;
		}
		
		public int getZ2()
		{
			return _z2;
		}
		
		@Override
		public int size()
		{
			// 1 byte, string (2 bytes per character + 2 termination bytes), 10 integers (4 bytes)
			return _name == null ? 43 : 43 + 2 * _name.length();
		}
	}
}