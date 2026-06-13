package com.shnok.javaserver.accountmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import com.shnok.javaserver.commons.crypt.BCrypt;
import com.shnok.javaserver.commons.pool.ConnectionPool;

import com.shnok.javaserver.Config;

public class SQLAccountManager
{
	private static final String INSERT_OR_UPDATE_ACCOUNT = "INSERT INTO accounts(login, password, access_level) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE password = VALUES(password), access_level = VALUES(access_level)";
	private static final String UPDATE_ACCOUNT_LEVEL = "UPDATE accounts SET access_level = ? WHERE login = ?";
	private static final String DELETE_ACCOUNT = "DELETE FROM accounts WHERE login = ?";
	
	private static String _uname = "";
	private static String _pass = "";
	private static String _level = "";
	private static String _mode = "";
	
	public static void main(String[] args)
	{
		Config.loadAccountManager();
		
		ConnectionPool.init();
		
		try (Scanner _scn = new Scanner(System.in))
		{
			while (true)
			{
				System.out.println("Please choose an option:");
				System.out.println();
				System.out.println("1 - Create new account or update existing one (change pass and access level)");
				System.out.println("2 - Change access level");
				System.out.println("3 - Delete existing account");
				System.out.println("4 - List accounts and access levels");
				System.out.println("5 - Exit");
				
				while (!(_mode.equals("1") || _mode.equals("2") || _mode.equals("3") || _mode.equals("4") || _mode.equals("5")))
				{
					System.out.print("Your choice: ");
					_mode = _scn.next();
				}
				
				if (_mode.equals("1") || _mode.equals("2") || _mode.equals("3"))
				{
					while (_uname.trim().length() == 0)
					{
						System.out.print("Username: ");
						_uname = _scn.next().toLowerCase();
					}
					
					if (_mode.equals("1"))
					{
						while (_pass.trim().length() == 0)
						{
							System.out.print("Password: ");
							_pass = _scn.next();
						}
					}
					
					if (_mode.equals("1") || _mode.equals("2"))
					{
						while (_level.trim().length() == 0)
						{
							System.out.print("Access level: ");
							_level = _scn.next();
						}
					}
				}
				
				if (_mode.equals("1"))
				{
					// Add or Update
					addOrUpdateAccount(_uname.trim(), _pass.trim(), _level.trim());
				}
				else if (_mode.equals("2"))
				{
					// Change Level
					changeAccountLevel(_uname.trim(), _level.trim());
				}
				else if (_mode.equals("3"))
				{
					// Delete
					System.out.print("WARNING: This will not delete the gameserver data (characters, items, etc..)");
					System.out.print(" it will only delete the account login server data.");
					System.out.println();
					System.out.print("Do you really want to delete this account? Y/N: ");
					
					String yesno = _scn.next();
					if ((yesno != null) && yesno.equalsIgnoreCase("Y"))
					{
						deleteAccount(_uname.trim());
					}
					else
					{
						System.out.println("Deletion cancelled.");
					}
				}
				else if (_mode.equals("4"))
				{
					// List
					_mode = "";
					System.out.println();
					System.out.println("Please choose a listing mode:");
					System.out.println();
					System.out.println("1 - Banned accounts only (accessLevel < 0)");
					System.out.println("2 - GM/privileged accounts (accessLevel > 0");
					System.out.println("3 - Regular accounts only (accessLevel = 0)");
					System.out.println("4 - List all");
					
					while (!(_mode.equals("1") || _mode.equals("2") || _mode.equals("3") || _mode.equals("4")))
					{
						System.out.print("Your choice: ");
						_mode = _scn.next();
					}
					System.out.println();
					printAccInfo(_mode);
				}
				else if (_mode.equals("5"))
				{
					System.exit(0);
				}
				
				_uname = "";
				_pass = "";
				_level = "";
				_mode = "";
				System.out.println();
			}
		}
	}
	
	private static void printAccInfo(String m)
	{
		int count = 0;
		String q = "SELECT login, access_level FROM accounts ";
		if (m.equals("1"))
		{
			q = q.concat("WHERE access_level < 0");
		}
		else if (m.equals("2"))
		{
			q = q.concat("WHERE access_level > 0");
		}
		else if (m.equals("3"))
		{
			q = q.concat("WHERE access_level = 0");
		}
		q = q.concat(" ORDER BY login ASC");
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(q);
			ResultSet rset = ps.executeQuery())
		{
			while (rset.next())
			{
				System.out.println(rset.getString("login") + " -> " + rset.getInt("access_level"));
				count++;
			}
			
			System.out.println("Displayed accounts: " + count);
		}
		catch (Exception e)
		{
			System.out.println("There was error while displaying accounts:");
			System.out.println(e.getMessage());
		}
	}
	
	private static void addOrUpdateAccount(String account, String password, String level)
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(INSERT_OR_UPDATE_ACCOUNT))
		{
			// Hash the password with default (10) salt
			final String hashed = BCrypt.hashPw(password);
			
			ps.setString(1, account);
			ps.setString(2, hashed);
			ps.setString(3, level);
			
			if (ps.executeUpdate() > 0)
			{
				System.out.println("Account " + account + " has been created or updated");
			}
			else
			{
				System.out.println("Account " + account + " doesn't exist");
			}
		}
		catch (Exception e)
		{
			System.out.println("There was error while adding/updating account:");
			System.out.println(e.getMessage());
		}
	}
	
	private static void changeAccountLevel(String account, String level)
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_ACCOUNT_LEVEL))
		{
			ps.setString(1, level);
			ps.setString(2, account);
			if (ps.executeUpdate() > 0)
			{
				System.out.println("Account " + account + " has been updated");
			}
			else
			{
				System.out.println("Account " + account + " doesn't exist");
			}
		}
		catch (Exception e)
		{
			System.out.println("There was error while updating account:");
			System.out.println(e.getMessage());
		}
	}
	
	private static void deleteAccount(String account)
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(DELETE_ACCOUNT))
		{
			ps.setString(1, account);
			if (ps.executeUpdate() > 0)
			{
				System.out.println("Account " + account + " has been deleted");
			}
			else
			{
				System.out.println("Account " + account + " doesn't exist");
			}
		}
		catch (Exception e)
		{
			System.out.println("There was error while deleting account:");
			System.out.println(e.getMessage());
		}
	}
}