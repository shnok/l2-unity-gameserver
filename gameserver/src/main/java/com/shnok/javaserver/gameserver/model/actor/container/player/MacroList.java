package com.shnok.javaserver.gameserver.model.actor.container.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

import com.shnok.javaserver.commons.lang.StringUtil;
import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.pool.ConnectionPool;

import com.shnok.javaserver.gameserver.enums.ShortcutType;
import com.shnok.javaserver.gameserver.model.Macro;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.records.MacroCmd;
import com.shnok.javaserver.gameserver.network.serverpackets.SendMacroList;

/**
 * An ordered container holding {@link Macro}s of a {@link Player}.
 */
public class MacroList extends LinkedHashMap<Integer, Macro>
{
	private static final long serialVersionUID = 1L;
	
	private static final CLogger LOGGER = new CLogger(MacroList.class.getName());
	
	private static final String INSERT_OR_UPDATE_MACRO = "INSERT INTO character_macroses (char_obj_id,id,icon,name,descr,acronym,commands) VALUES (?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE icon=VALUES(icon),name=VALUES(name),descr=VALUES(descr),acronym=VALUES(acronym),commands=VALUES(commands)";
	private static final String DELETE_MACRO = "DELETE FROM character_macroses WHERE char_obj_id=? AND id=?";
	private static final String LOAD_MACROS = "SELECT char_obj_id, id, icon, name, descr, acronym, commands FROM character_macroses WHERE char_obj_id=?";
	
	private final Player _owner;
	
	private int _revision;
	private int _macroId;
	
	public MacroList(Player owner)
	{
		_owner = owner;
		_revision = 1;
		_macroId = 1000;
	}
	
	public int getRevision()
	{
		return _revision;
	}
	
	/**
	 * Add a {@link Macro} to this {@link MacroList}.
	 * @param macro : The {@link Macro} to add.
	 */
	public void registerMacro(Macro macro)
	{
		// Compute a proper Macro id.
		if (macro.id == 0)
		{
			macro.id = _macroId++;
			
			while (get(macro.id) != null)
				macro.id = _macroId++;
		}
		
		// Add or replace the Macro.
		put(macro.id, macro);
		
		// Save the Macro into db.
		registerMacroInDb(macro);
		
		// Refresh the Macro window.
		sendUpdate();
	}
	
	/**
	 * Delete the {@link Macro} corresponding to the id from this {@link MacroList}.
	 * @param id : The id of the {@link Macro} to delete.
	 */
	public void deleteMacro(int id)
	{
		final Macro macro = remove(id);
		if (macro == null)
			return;
		
		// Delete the Macro from db.
		deleteMacroFromDb(macro);
		
		// Delete all existing shortcuts refering to this Macro id.
		_owner.getShortcutList().deleteShortcuts(id, ShortcutType.MACRO);
		
		// Refresh the Macro window.
		sendUpdate();
	}
	
	/**
	 * Refresh the {@link Macro}s window. Used on onEnterWorld.
	 */
	public void sendUpdate()
	{
		_revision++;
		
		if (isEmpty())
			_owner.sendPacket(new SendMacroList(_revision, size(), null));
		else
		{
			for (Macro macro : values())
				_owner.sendPacket(new SendMacroList(_revision, size(), macro));
		}
	}
	
	/**
	 * Save the given {@link Macro} to the database.
	 * @param macro : The {@link Macro} to save.
	 */
	private void registerMacroInDb(Macro macro)
	{
		final StringBuilder sb = new StringBuilder(300);
		for (MacroCmd cmd : macro.commands)
		{
			StringUtil.append(sb, cmd.type(), ",", cmd.d1(), ",", cmd.d2());
			if (cmd.cmd() != null && cmd.cmd().length() > 0)
				StringUtil.append(sb, ",", cmd.cmd());
			
			sb.append(';');
		}
		
		if (sb.length() > 255)
			sb.setLength(255);
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(INSERT_OR_UPDATE_MACRO))
		{
			ps.setInt(1, _owner.getObjectId());
			ps.setInt(2, macro.id);
			ps.setInt(3, macro.icon);
			ps.setString(4, macro.name);
			ps.setString(5, macro.descr);
			ps.setString(6, macro.acronym);
			ps.setString(7, sb.toString());
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't store macro.", e);
		}
	}
	
	/**
	 * Delete the given {@link Macro} from the database.
	 * @param macro : The {@link Macro} to delete.
	 */
	private void deleteMacroFromDb(Macro macro)
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(DELETE_MACRO))
		{
			ps.setInt(1, _owner.getObjectId());
			ps.setInt(2, macro.id);
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't delete macro.", e);
		}
	}
	
	/**
	 * Restore {@link Macro}s associated to the {@link Player} owner.
	 */
	public void restore()
	{
		super.clear();
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(LOAD_MACROS))
		{
			ps.setInt(1, _owner.getObjectId());
			
			try (ResultSet rs = ps.executeQuery())
			{
				while (rs.next())
				{
					final int id = rs.getInt("id");
					final int icon = rs.getInt("icon");
					final String name = rs.getString("name");
					final String descr = rs.getString("descr");
					final String acronym = rs.getString("acronym");
					
					final List<MacroCmd> commands = new ArrayList<>();
					final StringTokenizer st1 = new StringTokenizer(rs.getString("commands"), ";");
					
					while (st1.hasMoreTokens())
					{
						final StringTokenizer st = new StringTokenizer(st1.nextToken(), ",");
						if (st.countTokens() < 3)
							continue;
						
						final int type = Integer.parseInt(st.nextToken());
						final int d1 = Integer.parseInt(st.nextToken());
						final int d2 = Integer.parseInt(st.nextToken());
						
						String cmd = "";
						if (st.hasMoreTokens())
							cmd = st.nextToken();
						
						final MacroCmd mcmd = new MacroCmd(commands.size(), type, d1, d2, cmd);
						commands.add(mcmd);
					}
					
					final Macro macro = new Macro(id, icon, name, descr, acronym, commands.toArray(new MacroCmd[commands.size()]));
					put(macro.id, macro);
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't load macros.", e);
		}
	}
}