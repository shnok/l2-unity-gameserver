package com.shnok.javaserver.gameserver.data.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import com.shnok.javaserver.commons.data.StatSet;
import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.pool.ConnectionPool;
import com.shnok.javaserver.commons.pool.ThreadPool;
import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.sql.ClanTable;
import com.shnok.javaserver.gameserver.data.sql.PlayerInfoTable;
import com.shnok.javaserver.gameserver.enums.CabalType;
import com.shnok.javaserver.gameserver.enums.FestivalType;
import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.MessageType;
import com.shnok.javaserver.gameserver.enums.RestartType;
import com.shnok.javaserver.gameserver.enums.SayType;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.instance.FestivalMonster;
import com.shnok.javaserver.gameserver.model.group.Party;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.model.spawn.Spawn;
import com.shnok.javaserver.gameserver.model.zone.type.PeaceZone;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.CreatureSay;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.MagicSkillUse;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

public class FestivalOfDarknessManager
{
	private static final CLogger LOGGER = new CLogger(FestivalOfDarknessManager.class.getName());
	
	private static final String RESTORE_FESTIVAL = "SELECT festivalId, cabal, cycle, date, score, members FROM seven_signs_festival";
	private static final String RESTORE_FESTIVAL_2 = "SELECT festival_cycle, accumulated_bonus0, accumulated_bonus1, accumulated_bonus2, accumulated_bonus3, accumulated_bonus4 FROM seven_signs_status WHERE id=0";
	private static final String INSERT_OR_UPDATE_FESTIVAL = "INSERT INTO seven_signs_festival (festivalId, cabal, cycle, date, score, members) VALUES (?,?,?,?,?,?) ON DUPLICATE KEY UPDATE date = VALUES(date), score = VALUES(score), members = VALUES(members)";
	private static final String GET_CLAN_NAME = "SELECT clan_name FROM clan_data WHERE clan_id = (SELECT clanid FROM characters WHERE char_name = ?)";
	
	/**
	 * These length settings are important! :) All times are relative to the ELAPSED time (in ms) since a festival begins. Festival manager start is the time after the server starts to begin the first festival cycle. The cycle length should ideally be at least 2x longer than the festival length.
	 * This allows ample time for players to sign-up to participate in the festival. The intermission is the time between the festival participants being moved to the "arenas" and the spawning of the first set of mobs. The monster swarm time is the time before the monsters swarm to the center of the
	 * arena, after they are spawned. The chest spawn time is for when the bonus festival chests spawn, usually towards the end of the festival.
	 */
	public static final long FESTIVAL_SIGNUP_TIME = Config.FESTIVAL_CYCLE_LENGTH - Config.FESTIVAL_LENGTH - 60000;
	
	// Key Constants \\
	private static final int FESTIVAL_MAX_OFFSET_X = 230;
	private static final int FESTIVAL_MAX_OFFSET_Y = 230;
	private static final int FESTIVAL_DEFAULT_RESPAWN = 60; // Specify in seconds!
	
	public static final int FESTIVAL_COUNT = 5;
	
	public static final int FESTIVAL_OFFERING_ID = 5901;
	public static final int FESTIVAL_OFFERING_VALUE = 5;
	
	// ////////////////////// \\\\\\\\\\\\\\\\\\\\\\\\\\
	/*
	 * The following contains all the necessary spawn data for: - Player Start Locations - Witches - Monsters - Chests All data is given by: X, Y, Z (coords), Heading, NPC ID (if necessary) This may be moved externally in time, but the data should not change.
	 */
	public static final int[][] FESTIVAL_DAWN_PLAYER_SPAWNS =
	{
		{
			-79187,
			113186,
			-4895,
			0
		}, // 31 and below
		{
			-75918,
			110137,
			-4895,
			0
		}, // 42 and below
		{
			-73835,
			111969,
			-4895,
			0
		}, // 53 and below
		{
			-76170,
			113804,
			-4895,
			0
		}, // 64 and below
		{
			-78927,
			109528,
			-4895,
			0
		}
		// No level limit
	};
	
	public static final int[][] FESTIVAL_DUSK_PLAYER_SPAWNS =
	{
		{
			-77200,
			88966,
			-5151,
			0
		}, // 31 and below
		{
			-76941,
			85307,
			-5151,
			0
		}, // 42 and below
		{
			-74855,
			87135,
			-5151,
			0
		}, // 53 and below
		{
			-80208,
			88222,
			-5151,
			0
		}, // 64 and below
		{
			-79954,
			84697,
			-5151,
			0
		}
		// No level limit
	};
	
	protected static final int[][] FESTIVAL_DAWN_WITCH_SPAWNS =
	{
		{
			-79183,
			113052,
			-4891,
			0,
			31132
		}, // 31 and below
		{
			-75916,
			110270,
			-4891,
			0,
			31133
		}, // 42 and below
		{
			-73979,
			111970,
			-4891,
			0,
			31134
		}, // 53 and below
		{
			-76174,
			113663,
			-4891,
			0,
			31135
		}, // 64 and below
		{
			-78930,
			109664,
			-4891,
			0,
			31136
		}
		// No level limit
	};
	
	protected static final int[][] FESTIVAL_DUSK_WITCH_SPAWNS =
	{
		{
			-77199,
			88830,
			-5147,
			0,
			31142
		}, // 31 and below
		{
			-76942,
			85438,
			-5147,
			0,
			31143
		}, // 42 and below
		{
			-74990,
			87135,
			-5147,
			0,
			31144
		}, // 53 and below
		{
			-80207,
			88222,
			-5147,
			0,
			31145
		}, // 64 and below
		{
			-79952,
			84833,
			-5147,
			0,
			31146
		}
		// No level limit
	};
	
	protected static final int[][][] FESTIVAL_DAWN_PRIMARY_SPAWNS =
	{
		{
			/* Level 31 and Below - Offering of the Branded */
			{
				-78537,
				113839,
				-4895,
				-1,
				18009
			},
			{
				-78466,
				113852,
				-4895,
				-1,
				18010
			},
			{
				-78509,
				113899,
				-4895,
				-1,
				18010
			},
			
			{
				-78481,
				112557,
				-4895,
				-1,
				18009
			},
			{
				-78559,
				112504,
				-4895,
				-1,
				18010
			},
			{
				-78489,
				112494,
				-4895,
				-1,
				18010
			},
			
			{
				-79803,
				112543,
				-4895,
				-1,
				18012
			},
			{
				-79854,
				112492,
				-4895,
				-1,
				18013
			},
			{
				-79886,
				112557,
				-4895,
				-1,
				18014
			},
			
			{
				-79821,
				113811,
				-4895,
				-1,
				18015
			},
			{
				-79857,
				113896,
				-4895,
				-1,
				18017
			},
			{
				-79878,
				113816,
				-4895,
				-1,
				18018
			},
			
			// Archers and Marksmen \\
			{
				-79190,
				113660,
				-4895,
				-1,
				18011
			},
			{
				-78710,
				113188,
				-4895,
				-1,
				18011
			},
			{
				-79190,
				112730,
				-4895,
				-1,
				18016
			},
			{
				-79656,
				113188,
				-4895,
				-1,
				18016
			}
		},
		{
			/* Level 42 and Below - Apostate Offering */
			{
				-76558,
				110784,
				-4895,
				-1,
				18019
			},
			{
				-76607,
				110815,
				-4895,
				-1,
				18020
			}, // South West
			{
				-76559,
				110820,
				-4895,
				-1,
				18020
			},
			
			{
				-75277,
				110792,
				-4895,
				-1,
				18019
			},
			{
				-75225,
				110801,
				-4895,
				-1,
				18020
			}, // South East
			{
				-75262,
				110832,
				-4895,
				-1,
				18020
			},
			
			{
				-75249,
				109441,
				-4895,
				-1,
				18022
			},
			{
				-75278,
				109495,
				-4895,
				-1,
				18023
			}, // North East
			{
				-75223,
				109489,
				-4895,
				-1,
				18024
			},
			
			{
				-76556,
				109490,
				-4895,
				-1,
				18025
			},
			{
				-76607,
				109469,
				-4895,
				-1,
				18027
			}, // North West
			{
				-76561,
				109450,
				-4895,
				-1,
				18028
			},
			
			// Archers and Marksmen \\
			{
				-76399,
				110144,
				-4895,
				-1,
				18021
			},
			{
				-75912,
				110606,
				-4895,
				-1,
				18021
			},
			{
				-75444,
				110144,
				-4895,
				-1,
				18026
			},
			{
				-75930,
				109665,
				-4895,
				-1,
				18026
			}
		},
		{
			/* Level 53 and Below - Witch's Offering */
			{
				-73184,
				111319,
				-4895,
				-1,
				18029
			},
			{
				-73135,
				111294,
				-4895,
				-1,
				18030
			}, // South West
			{
				-73185,
				111281,
				-4895,
				-1,
				18030
			},
			
			{
				-74477,
				111321,
				-4895,
				-1,
				18029
			},
			{
				-74523,
				111293,
				-4895,
				-1,
				18030
			}, // South East
			{
				-74481,
				111280,
				-4895,
				-1,
				18030
			},
			
			{
				-74489,
				112604,
				-4895,
				-1,
				18032
			},
			{
				-74491,
				112660,
				-4895,
				-1,
				18033
			}, // North East
			{
				-74527,
				112629,
				-4895,
				-1,
				18034
			},
			
			{
				-73197,
				112621,
				-4895,
				-1,
				18035
			},
			{
				-73142,
				112631,
				-4895,
				-1,
				18037
			}, // North West
			{
				-73182,
				112656,
				-4895,
				-1,
				18038
			},
			
			// Archers and Marksmen \\
			{
				-73834,
				112430,
				-4895,
				-1,
				18031
			},
			{
				-74299,
				111959,
				-4895,
				-1,
				18031
			},
			{
				-73841,
				111491,
				-4895,
				-1,
				18036
			},
			{
				-73363,
				111959,
				-4895,
				-1,
				18036
			}
		},
		{
			/* Level 64 and Below - Dark Omen Offering */
			{
				-75543,
				114461,
				-4895,
				-1,
				18039
			},
			{
				-75514,
				114493,
				-4895,
				-1,
				18040
			}, // South West
			{
				-75488,
				114456,
				-4895,
				-1,
				18040
			},
			
			{
				-75521,
				113158,
				-4895,
				-1,
				18039
			},
			{
				-75504,
				113110,
				-4895,
				-1,
				18040
			}, // South East
			{
				-75489,
				113142,
				-4895,
				-1,
				18040
			},
			
			{
				-76809,
				113143,
				-4895,
				-1,
				18042
			},
			{
				-76860,
				113138,
				-4895,
				-1,
				18043
			}, // North East
			{
				-76831,
				113112,
				-4895,
				-1,
				18044
			},
			
			{
				-76831,
				114441,
				-4895,
				-1,
				18045
			},
			{
				-76840,
				114490,
				-4895,
				-1,
				18047
			}, // North West
			{
				-76864,
				114455,
				-4895,
				-1,
				18048
			},
			
			// Archers and Marksmen \\
			{
				-75703,
				113797,
				-4895,
				-1,
				18041
			},
			{
				-76180,
				114263,
				-4895,
				-1,
				18041
			},
			{
				-76639,
				113797,
				-4895,
				-1,
				18046
			},
			{
				-76180,
				113337,
				-4895,
				-1,
				18046
			}
		},
		{
			/* No Level Limit - Offering of Forbidden Path */
			{
				-79576,
				108881,
				-4895,
				-1,
				18049
			},
			{
				-79592,
				108835,
				-4895,
				-1,
				18050
			}, // South West
			{
				-79614,
				108871,
				-4895,
				-1,
				18050
			},
			
			{
				-79586,
				110171,
				-4895,
				-1,
				18049
			},
			{
				-79589,
				110216,
				-4895,
				-1,
				18050
			}, // South East
			{
				-79620,
				110177,
				-4895,
				-1,
				18050
			},
			
			{
				-78825,
				110182,
				-4895,
				-1,
				18052
			},
			{
				-78238,
				110182,
				-4895,
				-1,
				18053
			}, // North East
			{
				-78266,
				110218,
				-4895,
				-1,
				18054
			},
			
			{
				-78275,
				108883,
				-4895,
				-1,
				18055
			},
			{
				-78267,
				108839,
				-4895,
				-1,
				18057
			}, // North West
			{
				-78241,
				108871,
				-4895,
				-1,
				18058
			},
			
			// Archers and Marksmen \\
			{
				-79394,
				109538,
				-4895,
				-1,
				18051
			},
			{
				-78929,
				109992,
				-4895,
				-1,
				18051
			},
			{
				-78454,
				109538,
				-4895,
				-1,
				18056
			},
			{
				-78929,
				109053,
				-4895,
				-1,
				18056
			}
		}
	};
	
	protected static final int[][][] FESTIVAL_DUSK_PRIMARY_SPAWNS =
	{
		{
			/* Level 31 and Below - Offering of the Branded */
			{
				-76542,
				89653,
				-5151,
				-1,
				18009
			},
			{
				-76509,
				89637,
				-5151,
				-1,
				18010
			},
			{
				-76548,
				89614,
				-5151,
				-1,
				18010
			},
			
			{
				-76539,
				88326,
				-5151,
				-1,
				18009
			},
			{
				-76512,
				88289,
				-5151,
				-1,
				18010
			},
			{
				-76546,
				88287,
				-5151,
				-1,
				18010
			},
			
			{
				-77879,
				88308,
				-5151,
				-1,
				18012
			},
			{
				-77886,
				88310,
				-5151,
				-1,
				18013
			},
			{
				-77879,
				88278,
				-5151,
				-1,
				18014
			},
			
			{
				-77857,
				89605,
				-5151,
				-1,
				18015
			},
			{
				-77858,
				89658,
				-5151,
				-1,
				18017
			},
			{
				-77891,
				89633,
				-5151,
				-1,
				18018
			},
			
			// Archers and Marksmen \\
			{
				-76728,
				88962,
				-5151,
				-1,
				18011
			},
			{
				-77194,
				88494,
				-5151,
				-1,
				18011
			},
			{
				-77660,
				88896,
				-5151,
				-1,
				18016
			},
			{
				-77195,
				89438,
				-5151,
				-1,
				18016
			}
		},
		{
			/* Level 42 and Below - Apostate's Offering */
			{
				-77585,
				84650,
				-5151,
				-1,
				18019
			},
			{
				-77628,
				84643,
				-5151,
				-1,
				18020
			},
			{
				-77607,
				84613,
				-5151,
				-1,
				18020
			},
			
			{
				-76603,
				85946,
				-5151,
				-1,
				18019
			},
			{
				-77606,
				85994,
				-5151,
				-1,
				18020
			},
			{
				-77638,
				85959,
				-5151,
				-1,
				18020
			},
			
			{
				-76301,
				85960,
				-5151,
				-1,
				18022
			},
			{
				-76257,
				85972,
				-5151,
				-1,
				18023
			},
			{
				-76286,
				85992,
				-5151,
				-1,
				18024
			},
			
			{
				-76281,
				84667,
				-5151,
				-1,
				18025
			},
			{
				-76291,
				84611,
				-5151,
				-1,
				18027
			},
			{
				-76257,
				84616,
				-5151,
				-1,
				18028
			},
			
			// Archers and Marksmen \\
			{
				-77419,
				85307,
				-5151,
				-1,
				18021
			},
			{
				-76952,
				85768,
				-5151,
				-1,
				18021
			},
			{
				-76477,
				85312,
				-5151,
				-1,
				18026
			},
			{
				-76942,
				84832,
				-5151,
				-1,
				18026
			}
		},
		{
			/* Level 53 and Below - Witch's Offering */
			{
				-74211,
				86494,
				-5151,
				-1,
				18029
			},
			{
				-74200,
				86449,
				-5151,
				-1,
				18030
			},
			{
				-74167,
				86464,
				-5151,
				-1,
				18030
			},
			
			{
				-75495,
				86482,
				-5151,
				-1,
				18029
			},
			{
				-75540,
				86473,
				-5151,
				-1,
				18030
			},
			{
				-75509,
				86445,
				-5151,
				-1,
				18030
			},
			
			{
				-75509,
				87775,
				-5151,
				-1,
				18032
			},
			{
				-75518,
				87826,
				-5151,
				-1,
				18033
			},
			{
				-75542,
				87780,
				-5151,
				-1,
				18034
			},
			
			{
				-74214,
				87789,
				-5151,
				-1,
				18035
			},
			{
				-74169,
				87801,
				-5151,
				-1,
				18037
			},
			{
				-74198,
				87827,
				-5151,
				-1,
				18038
			},
			
			// Archers and Marksmen \\
			{
				-75324,
				87135,
				-5151,
				-1,
				18031
			},
			{
				-74852,
				87606,
				-5151,
				-1,
				18031
			},
			{
				-74388,
				87146,
				-5151,
				-1,
				18036
			},
			{
				-74856,
				86663,
				-5151,
				-1,
				18036
			}
		},
		{
			/* Level 64 and Below - Dark Omen Offering */
			{
				-79560,
				89007,
				-5151,
				-1,
				18039
			},
			{
				-79521,
				89016,
				-5151,
				-1,
				18040
			},
			{
				-79544,
				89047,
				-5151,
				-1,
				18040
			},
			
			{
				-79552,
				87717,
				-5151,
				-1,
				18039
			},
			{
				-79552,
				87673,
				-5151,
				-1,
				18040
			},
			{
				-79510,
				87702,
				-5151,
				-1,
				18040
			},
			
			{
				-80866,
				87719,
				-5151,
				-1,
				18042
			},
			{
				-80897,
				87689,
				-5151,
				-1,
				18043
			},
			{
				-80850,
				87685,
				-5151,
				-1,
				18044
			},
			
			{
				-80848,
				89013,
				-5151,
				-1,
				18045
			},
			{
				-80887,
				89051,
				-5151,
				-1,
				18047
			},
			{
				-80891,
				89004,
				-5151,
				-1,
				18048
			},
			
			// Archers and Marksmen \\
			{
				-80205,
				87895,
				-5151,
				-1,
				18041
			},
			{
				-80674,
				88350,
				-5151,
				-1,
				18041
			},
			{
				-80209,
				88833,
				-5151,
				-1,
				18046
			},
			{
				-79743,
				88364,
				-5151,
				-1,
				18046
			}
		},
		{
			/* No Level Limit - Offering of Forbidden Path */
			{
				-80624,
				84060,
				-5151,
				-1,
				18049
			},
			{
				-80621,
				84007,
				-5151,
				-1,
				18050
			},
			{
				-80590,
				84039,
				-5151,
				-1,
				18050
			},
			
			{
				-80605,
				85349,
				-5151,
				-1,
				18049
			},
			{
				-80639,
				85363,
				-5151,
				-1,
				18050
			},
			{
				-80611,
				85385,
				-5151,
				-1,
				18050
			},
			
			{
				-79311,
				85353,
				-5151,
				-1,
				18052
			},
			{
				-79277,
				85384,
				-5151,
				-1,
				18053
			},
			{
				-79273,
				85539,
				-5151,
				-1,
				18054
			},
			
			{
				-79297,
				84054,
				-5151,
				-1,
				18055
			},
			{
				-79285,
				84006,
				-5151,
				-1,
				18057
			},
			{
				-79260,
				84040,
				-5151,
				-1,
				18058
			},
			
			// Archers and Marksmen \\
			{
				-79945,
				85171,
				-5151,
				-1,
				18051
			},
			{
				-79489,
				84707,
				-5151,
				-1,
				18051
			},
			{
				-79952,
				84222,
				-5151,
				-1,
				18056
			},
			{
				-80423,
				84703,
				-5151,
				-1,
				18056
			}
		}
	};
	
	protected static final int[][][] FESTIVAL_DAWN_SECONDARY_SPAWNS =
	{
		{
			/* 31 and Below */
			{
				-78757,
				112834,
				-4895,
				-1,
				18016
			},
			{
				-78581,
				112834,
				-4895,
				-1,
				18016
			},
			
			{
				-78822,
				112526,
				-4895,
				-1,
				18011
			},
			{
				-78822,
				113702,
				-4895,
				-1,
				18011
			},
			{
				-78822,
				113874,
				-4895,
				-1,
				18011
			},
			
			{
				-79524,
				113546,
				-4895,
				-1,
				18011
			},
			{
				-79693,
				113546,
				-4895,
				-1,
				18011
			},
			{
				-79858,
				113546,
				-4895,
				-1,
				18011
			},
			
			{
				-79545,
				112757,
				-4895,
				-1,
				18016
			},
			{
				-79545,
				112586,
				-4895,
				-1,
				18016
			},
		},
		{
			/* 42 and Below */
			{
				-75565,
				110580,
				-4895,
				-1,
				18026
			},
			{
				-75565,
				110740,
				-4895,
				-1,
				18026
			},
			
			{
				-75577,
				109776,
				-4895,
				-1,
				18021
			},
			{
				-75413,
				109776,
				-4895,
				-1,
				18021
			},
			{
				-75237,
				109776,
				-4895,
				-1,
				18021
			},
			
			{
				-76274,
				109468,
				-4895,
				-1,
				18021
			},
			{
				-76274,
				109635,
				-4895,
				-1,
				18021
			},
			{
				-76274,
				109795,
				-4895,
				-1,
				18021
			},
			
			{
				-76351,
				110500,
				-4895,
				-1,
				18056
			},
			{
				-76528,
				110500,
				-4895,
				-1,
				18056
			},
		},
		{
			/* 53 and Below */
			{
				-74191,
				111527,
				-4895,
				-1,
				18036
			},
			{
				-74191,
				111362,
				-4895,
				-1,
				18036
			},
			
			{
				-73495,
				111611,
				-4895,
				-1,
				18031
			},
			{
				-73327,
				111611,
				-4895,
				-1,
				18031
			},
			{
				-73154,
				111611,
				-4895,
				-1,
				18031
			},
			
			{
				-73473,
				112301,
				-4895,
				-1,
				18031
			},
			{
				-73473,
				112475,
				-4895,
				-1,
				18031
			},
			{
				-73473,
				112649,
				-4895,
				-1,
				18031
			},
			
			{
				-74270,
				112326,
				-4895,
				-1,
				18036
			},
			{
				-74443,
				112326,
				-4895,
				-1,
				18036
			},
		},
		{
			/* 64 and Below */
			{
				-75738,
				113439,
				-4895,
				-1,
				18046
			},
			{
				-75571,
				113439,
				-4895,
				-1,
				18046
			},
			
			{
				-75824,
				114141,
				-4895,
				-1,
				18041
			},
			{
				-75824,
				114309,
				-4895,
				-1,
				18041
			},
			{
				-75824,
				114477,
				-4895,
				-1,
				18041
			},
			
			{
				-76513,
				114158,
				-4895,
				-1,
				18041
			},
			{
				-76683,
				114158,
				-4895,
				-1,
				18041
			},
			{
				-76857,
				114158,
				-4895,
				-1,
				18041
			},
			
			{
				-76535,
				113357,
				-4895,
				-1,
				18056
			},
			{
				-76535,
				113190,
				-4895,
				-1,
				18056
			},
		},
		{
			/* No Level Limit */
			{
				-79350,
				109894,
				-4895,
				-1,
				18056
			},
			{
				-79534,
				109894,
				-4895,
				-1,
				18056
			},
			
			{
				-79285,
				109187,
				-4895,
				-1,
				18051
			},
			{
				-79285,
				109019,
				-4895,
				-1,
				18051
			},
			{
				-79285,
				108860,
				-4895,
				-1,
				18051
			},
			
			{
				-78587,
				109172,
				-4895,
				-1,
				18051
			},
			{
				-78415,
				109172,
				-4895,
				-1,
				18051
			},
			{
				-78249,
				109172,
				-4895,
				-1,
				18051
			},
			
			{
				-78575,
				109961,
				-4895,
				-1,
				18056
			},
			{
				-78575,
				110130,
				-4895,
				-1,
				18056
			},
		}
	};
	
	protected static final int[][][] FESTIVAL_DUSK_SECONDARY_SPAWNS =
	{
		{
			/* 31 and Below */
			{
				-76844,
				89304,
				-5151,
				-1,
				18011
			},
			{
				-76844,
				89479,
				-5151,
				-1,
				18011
			},
			{
				-76844,
				89649,
				-5151,
				-1,
				18011
			},
			
			{
				-77544,
				89326,
				-5151,
				-1,
				18011
			},
			{
				-77716,
				89326,
				-5151,
				-1,
				18011
			},
			{
				-77881,
				89326,
				-5151,
				-1,
				18011
			},
			
			{
				-77561,
				88530,
				-5151,
				-1,
				18016
			},
			{
				-77561,
				88364,
				-5151,
				-1,
				18016
			},
			
			{
				-76762,
				88615,
				-5151,
				-1,
				18016
			},
			{
				-76594,
				88615,
				-5151,
				-1,
				18016
			},
		},
		{
			/* 42 and Below */
			{
				-77307,
				84969,
				-5151,
				-1,
				18021
			},
			{
				-77307,
				84795,
				-5151,
				-1,
				18021
			},
			{
				-77307,
				84623,
				-5151,
				-1,
				18021
			},
			
			{
				-76614,
				84944,
				-5151,
				-1,
				18021
			},
			{
				-76433,
				84944,
				-5151,
				-1,
				18021
			},
			{
				-7626 - 1,
				84944,
				-5151,
				-1,
				18021
			},
			
			{
				-76594,
				85745,
				-5151,
				-1,
				18026
			},
			{
				-76594,
				85910,
				-5151,
				-1,
				18026
			},
			
			{
				-77384,
				85660,
				-5151,
				-1,
				18026
			},
			{
				-77555,
				85660,
				-5151,
				-1,
				18026
			},
		},
		{
			/* 53 and Below */
			{
				-74517,
				86782,
				-5151,
				-1,
				18031
			},
			{
				-74344,
				86782,
				-5151,
				-1,
				18031
			},
			{
				-74185,
				86782,
				-5151,
				-1,
				18031
			},
			
			{
				-74496,
				87464,
				-5151,
				-1,
				18031
			},
			{
				-74496,
				87636,
				-5151,
				-1,
				18031
			},
			{
				-74496,
				87815,
				-5151,
				-1,
				18031
			},
			
			{
				-75298,
				87497,
				-5151,
				-1,
				18036
			},
			{
				-75460,
				87497,
				-5151,
				-1,
				18036
			},
			
			{
				-75219,
				86712,
				-5151,
				-1,
				18036
			},
			{
				-75219,
				86531,
				-5151,
				-1,
				18036
			},
		},
		{
			/* 64 and Below */
			{
				-79851,
				88703,
				-5151,
				-1,
				18041
			},
			{
				-79851,
				88868,
				-5151,
				-1,
				18041
			},
			{
				-79851,
				89040,
				-5151,
				-1,
				18041
			},
			
			{
				-80548,
				88722,
				-5151,
				-1,
				18041
			},
			{
				-80711,
				88722,
				-5151,
				-1,
				18041
			},
			{
				-80883,
				88722,
				-5151,
				-1,
				18041
			},
			
			{
				-80565,
				87916,
				-5151,
				-1,
				18046
			},
			{
				-80565,
				87752,
				-5151,
				-1,
				18046
			},
			
			{
				-79779,
				87996,
				-5151,
				-1,
				18046
			},
			{
				-79613,
				87996,
				-5151,
				-1,
				18046
			},
		},
		{
			/* No Level Limit */
			{
				-79271,
				84330,
				-5151,
				-1,
				18051
			},
			{
				-79448,
				84330,
				-5151,
				-1,
				18051
			},
			{
				-79601,
				84330,
				-5151,
				-1,
				18051
			},
			
			{
				-80311,
				84367,
				-5151,
				-1,
				18051
			},
			{
				-80311,
				84196,
				-5151,
				-1,
				18051
			},
			{
				-80311,
				84015,
				-5151,
				-1,
				18051
			},
			
			{
				-80556,
				85049,
				-5151,
				-1,
				18056
			},
			{
				-80384,
				85049,
				-5151,
				-1,
				18056
			},
			
			{
				-79598,
				85127,
				-5151,
				-1,
				18056
			},
			{
				-79598,
				85303,
				-5151,
				-1,
				18056
			},
		}
	};
	
	protected static final int[][][] FESTIVAL_DAWN_CHEST_SPAWNS =
	{
		{
			/* Level 31 and Below */
			{
				-78999,
				112957,
				-4927,
				-1,
				18109
			},
			{
				-79153,
				112873,
				-4927,
				-1,
				18109
			},
			{
				-79256,
				112873,
				-4927,
				-1,
				18109
			},
			{
				-79368,
				112957,
				-4927,
				-1,
				18109
			},
			
			{
				-79481,
				113124,
				-4927,
				-1,
				18109
			},
			{
				-79481,
				113275,
				-4927,
				-1,
				18109
			},
			
			{
				-79364,
				113398,
				-4927,
				-1,
				18109
			},
			{
				-79213,
				113500,
				-4927,
				-1,
				18109
			},
			{
				-79099,
				113500,
				-4927,
				-1,
				18109
			},
			{
				-78960,
				113398,
				-4927,
				-1,
				18109
			},
			
			{
				-78882,
				113235,
				-4927,
				-1,
				18109
			},
			{
				-78882,
				113099,
				-4927,
				-1,
				18109
			},
		},
		{
			/* Level 42 and Below */
			{
				-76119,
				110383,
				-4927,
				-1,
				18110
			},
			{
				-75980,
				110442,
				-4927,
				-1,
				18110
			},
			{
				-75848,
				110442,
				-4927,
				-1,
				18110
			},
			{
				-75720,
				110383,
				-4927,
				-1,
				18110
			},
			
			{
				-75625,
				110195,
				-4927,
				-1,
				18110
			},
			{
				-75625,
				110063,
				-4927,
				-1,
				18110
			},
			
			{
				-75722,
				109908,
				-4927,
				-1,
				18110
			},
			{
				-75863,
				109832,
				-4927,
				-1,
				18110
			},
			{
				-75989,
				109832,
				-4927,
				-1,
				18110
			},
			{
				-76130,
				109908,
				-4927,
				-1,
				18110
			},
			
			{
				-76230,
				110079,
				-4927,
				-1,
				18110
			},
			{
				-76230,
				110215,
				-4927,
				-1,
				18110
			},
		},
		{
			/* Level 53 and Below */
			{
				-74055,
				111781,
				-4927,
				-1,
				18111
			},
			{
				-74144,
				111938,
				-4927,
				-1,
				18111
			},
			{
				-74144,
				112075,
				-4927,
				-1,
				18111
			},
			{
				-74055,
				112173,
				-4927,
				-1,
				18111
			},
			
			{
				-73885,
				112289,
				-4927,
				-1,
				18111
			},
			{
				-73756,
				112289,
				-4927,
				-1,
				18111
			},
			
			{
				-73574,
				112141,
				-4927,
				-1,
				18111
			},
			{
				-73511,
				112040,
				-4927,
				-1,
				18111
			},
			{
				-73511,
				111912,
				-4927,
				-1,
				18111
			},
			{
				-73574,
				111772,
				-4927,
				-1,
				18111
			},
			
			{
				-73767,
				111669,
				-4927,
				-1,
				18111
			},
			{
				-73899,
				111669,
				-4927,
				-1,
				18111
			},
		},
		{
			/* Level 64 and Below */
			{
				-76008,
				113566,
				-4927,
				-1,
				18112
			},
			{
				-76159,
				113485,
				-4927,
				-1,
				18112
			},
			{
				-76267,
				113485,
				-4927,
				-1,
				18112
			},
			{
				-76386,
				113566,
				-4927,
				-1,
				18112
			},
			
			{
				-76482,
				113748,
				-4927,
				-1,
				18112
			},
			{
				-76482,
				113885,
				-4927,
				-1,
				18112
			},
			
			{
				-76371,
				114029,
				-4927,
				-1,
				18112
			},
			{
				-76220,
				114118,
				-4927,
				-1,
				18112
			},
			{
				-76092,
				114118,
				-4927,
				-1,
				18112
			},
			{
				-75975,
				114029,
				-4927,
				-1,
				18112
			},
			
			{
				-75861,
				11385 - 1,
				-4927,
				-1,
				18112
			},
			{
				-75861,
				113713,
				-4927,
				-1,
				18112
			},
		},
		{
			/* No Level Limit */
			{
				-79100,
				109782,
				-4927,
				-1,
				18113
			},
			{
				-78962,
				109853,
				-4927,
				-1,
				18113
			},
			{
				-78851,
				109853,
				-4927,
				-1,
				18113
			},
			{
				-78721,
				109782,
				-4927,
				-1,
				18113
			},
			
			{
				-78615,
				109596,
				-4927,
				-1,
				18113
			},
			{
				-78615,
				109453,
				-4927,
				-1,
				18113
			},
			
			{
				-78746,
				109300,
				-4927,
				-1,
				18113
			},
			{
				-78881,
				109203,
				-4927,
				-1,
				18113
			},
			{
				-79027,
				109203,
				-4927,
				-1,
				18113
			},
			{
				-79159,
				109300,
				-4927,
				-1,
				18113
			},
			
			{
				-79240,
				109480,
				-4927,
				-1,
				18113
			},
			{
				-79240,
				109615,
				-4927,
				-1,
				18113
			},
		}
	};
	
	protected static final int[][][] FESTIVAL_DUSK_CHEST_SPAWNS =
	{
		{
			/* Level 31 and Below */
			{
				-77016,
				88726,
				-5183,
				-1,
				18114
			},
			{
				-77136,
				88646,
				-5183,
				-1,
				18114
			},
			{
				-77247,
				88646,
				-5183,
				-1,
				18114
			},
			{
				-77380,
				88726,
				-5183,
				-1,
				18114
			},
			
			{
				-77512,
				88883,
				-5183,
				-1,
				18114
			},
			{
				-77512,
				89053,
				-5183,
				-1,
				18114
			},
			
			{
				-77378,
				89287,
				-5183,
				-1,
				18114
			},
			{
				-77254,
				89238,
				-5183,
				-1,
				18114
			},
			{
				-77095,
				89238,
				-5183,
				-1,
				18114
			},
			{
				-76996,
				89287,
				-5183,
				-1,
				18114
			},
			
			{
				-76901,
				89025,
				-5183,
				-1,
				18114
			},
			{
				-76901,
				88891,
				-5183,
				-1,
				18114
			},
		},
		{
			/* Level 42 and Below */
			{
				-77128,
				85553,
				-5183,
				-1,
				18115
			},
			{
				-77036,
				85594,
				-5183,
				-1,
				18115
			},
			{
				-76919,
				85594,
				-5183,
				-1,
				18115
			},
			{
				-76755,
				85553,
				-5183,
				-1,
				18115
			},
			
			{
				-76635,
				85392,
				-5183,
				-1,
				18115
			},
			{
				-76635,
				85216,
				-5183,
				-1,
				18115
			},
			
			{
				-76761,
				85025,
				-5183,
				-1,
				18115
			},
			{
				-76908,
				85004,
				-5183,
				-1,
				18115
			},
			{
				-77041,
				85004,
				-5183,
				-1,
				18115
			},
			{
				-77138,
				85025,
				-5183,
				-1,
				18115
			},
			
			{
				-77268,
				85219,
				-5183,
				-1,
				18115
			},
			{
				-77268,
				85410,
				-5183,
				-1,
				18115
			},
		},
		{
			/* Level 53 and Below */
			{
				-75150,
				87303,
				-5183,
				-1,
				18116
			},
			{
				-75150,
				87175,
				-5183,
				-1,
				18116
			},
			{
				-75150,
				87175,
				-5183,
				-1,
				18116
			},
			{
				-75150,
				87303,
				-5183,
				-1,
				18116
			},
			
			{
				-74943,
				87433,
				-5183,
				-1,
				18116
			},
			{
				-74767,
				87433,
				-5183,
				-1,
				18116
			},
			
			{
				-74556,
				87306,
				-5183,
				-1,
				18116
			},
			{
				-74556,
				87184,
				-5183,
				-1,
				18116
			},
			{
				-74556,
				87184,
				-5183,
				-1,
				18116
			},
			{
				-74556,
				87306,
				-5183,
				-1,
				18116
			},
			
			{
				-74757,
				86830,
				-5183,
				-1,
				18116
			},
			{
				-74927,
				86830,
				-5183,
				-1,
				18116
			},
		},
		{
			/* Level 64 and Below */
			{
				-80010,
				88128,
				-5183,
				-1,
				18117
			},
			{
				-80113,
				88066,
				-5183,
				-1,
				18117
			},
			{
				-80220,
				88066,
				-5183,
				-1,
				18117
			},
			{
				-80359,
				88128,
				-5183,
				-1,
				18117
			},
			
			{
				-80467,
				88267,
				-5183,
				-1,
				18117
			},
			{
				-80467,
				88436,
				-5183,
				-1,
				18117
			},
			
			{
				-80381,
				88639,
				-5183,
				-1,
				18117
			},
			{
				-80278,
				88577,
				-5183,
				-1,
				18117
			},
			{
				-80142,
				88577,
				-5183,
				-1,
				18117
			},
			{
				-80028,
				88639,
				-5183,
				-1,
				18117
			},
			
			{
				-79915,
				88466,
				-5183,
				-1,
				18117
			},
			{
				-79915,
				88322,
				-5183,
				-1,
				18117
			},
		},
		{
			/* No Level Limit */
			{
				-80153,
				84947,
				-5183,
				-1,
				18118
			},
			{
				-80003,
				84962,
				-5183,
				-1,
				18118
			},
			{
				-79848,
				84962,
				-5183,
				-1,
				18118
			},
			{
				-79742,
				84947,
				-5183,
				-1,
				18118
			},
			
			{
				-79668,
				84772,
				-5183,
				-1,
				18118
			},
			{
				-79668,
				84619,
				-5183,
				-1,
				18118
			},
			
			{
				-79772,
				84471,
				-5183,
				-1,
				18118
			},
			{
				-79888,
				84414,
				-5183,
				-1,
				18118
			},
			{
				-80023,
				84414,
				-5183,
				-1,
				18118
			},
			{
				-80166,
				84471,
				-5183,
				-1,
				18118
			},
			
			{
				-80253,
				84600,
				-5183,
				-1,
				18118
			},
			{
				-80253,
				84780,
				-5183,
				-1,
				18118
			},
		}
	};
	
	// ////////////////////// \\\\\\\\\\\\\\\\\\\\\\\\\\
	
	protected FestivalManager _managerInstance;
	protected ScheduledFuture<?> _managerScheduledTask;
	
	protected int _signsCycle = SevenSignsManager.getInstance().getCurrentCycle();
	protected int _festivalCycle;
	protected long _nextFestivalCycleStart;
	protected long _nextFestivalStart;
	protected boolean _festivalInitialized;
	protected boolean _festivalInProgress;
	protected List<Integer> _accumulatedBonuses = new ArrayList<>();
	
	boolean _noPartyRegister;
	private List<PeaceZone> _dawnPeace;
	private List<PeaceZone> _duskPeace;
	
	protected Map<Integer, List<Integer>> _dawnFestivalParticipants = new HashMap<>();
	protected Map<Integer, List<Integer>> _duskFestivalParticipants = new HashMap<>();
	
	protected Map<Integer, List<Integer>> _dawnPreviousParticipants = new HashMap<>();
	protected Map<Integer, List<Integer>> _duskPreviousParticipants = new HashMap<>();
	
	private Map<Integer, Integer> _dawnFestivalScores = new HashMap<>();
	private Map<Integer, Integer> _duskFestivalScores = new HashMap<>();
	
	private Map<Integer, Map<Integer, StatSet>> _festivalData = new ConcurrentHashMap<>();
	
	protected FestivalOfDarknessManager()
	{
		restoreFestivalData();
		
		if (SevenSignsManager.getInstance().isSealValidationPeriod())
		{
			LOGGER.info("Seven Signs Festival initialization was bypassed due to Seal Validation being under effect.");
			return;
		}
		
		startFestivalManager();
	}
	
	/**
	 * Returns true if the monster ID given is of an archer/marksman type.
	 * @param npcId
	 * @return boolean isArcher
	 */
	protected static final boolean isFestivalArcher(int npcId)
	{
		if (npcId < 18009 || npcId > 18108)
			return false;
		
		int identifier = npcId % 10;
		return (identifier == 4 || identifier == 9);
	}
	
	/**
	 * Returns true if the monster ID given is a festival chest.
	 * @param npcId
	 * @return boolean isChest
	 */
	protected static final boolean isFestivalChest(int npcId)
	{
		return (npcId < 18109 || npcId > 18118);
	}
	
	/**
	 * Primarily used to terminate the Festival Manager, when the Seven Signs period changes.
	 * @return ScheduledFuture festManagerScheduler
	 */
	public final ScheduledFuture<?> getFestivalManagerSchedule()
	{
		if (_managerScheduledTask == null)
			startFestivalManager();
		
		return _managerScheduledTask;
	}
	
	/**
	 * Used to start the Festival Manager, if the current period is not Seal Validation.
	 */
	public void startFestivalManager()
	{
		// Start the Festival Manager for the first time after the server has started at the specified time, then invoke it automatically after every cycle.
		FestivalManager fm = new FestivalManager();
		
		setNextFestivalStart(Config.FESTIVAL_MANAGER_START + FESTIVAL_SIGNUP_TIME);
		_managerScheduledTask = ThreadPool.scheduleAtFixedRate(fm, Config.FESTIVAL_MANAGER_START, Config.FESTIVAL_CYCLE_LENGTH);
		
		LOGGER.info("The first Festival of Darkness cycle begins in {} minute(s).", (Config.FESTIVAL_MANAGER_START / 60000));
	}
	
	/**
	 * Restores saved festival data, basic settings from the properties file and past high score data from the database.
	 */
	protected void restoreFestivalData()
	{
		try (Connection con = ConnectionPool.getConnection())
		{
			try (PreparedStatement ps = con.prepareStatement(RESTORE_FESTIVAL);
				ResultSet rs = ps.executeQuery())
			{
				while (rs.next())
				{
					int festivalCycle = rs.getInt("cycle");
					int festivalId = rs.getInt("festivalId");
					final String cabal = rs.getString("cabal");
					
					final StatSet set = new StatSet();
					set.set("festivalId", festivalId);
					set.set("cabal", Enum.valueOf(CabalType.class, cabal));
					set.set("cycle", festivalCycle);
					set.set("date", rs.getString("date"));
					set.set("score", rs.getInt("score"));
					set.set("members", rs.getString("members"));
					
					if (cabal.equalsIgnoreCase("dawn"))
						festivalId += FESTIVAL_COUNT;
					
					final Map<Integer, StatSet> map = _festivalData.computeIfAbsent(festivalCycle, m -> new HashMap<>());
					map.put(festivalId, set);
				}
			}
			
			try (PreparedStatement ps = con.prepareStatement(RESTORE_FESTIVAL_2);
				ResultSet rs = ps.executeQuery())
			{
				while (rs.next())
				{
					_festivalCycle = rs.getInt("festival_cycle");
					
					for (int i = 0; i < FESTIVAL_COUNT; i++)
						_accumulatedBonuses.add(i, rs.getInt("accumulated_bonus" + i));
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't load Seven Signs Festival data.", e);
		}
	}
	
	/**
	 * Stores current festival data, basic settings to the properties file and past high score data to the database.<BR>
	 * <BR>
	 * If updateSettings = true, then all Seven Signs data is updated in the database.
	 * @param updateSettings if true, will save Seven Signs status aswell.
	 */
	public void saveFestivalData(boolean updateSettings)
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(INSERT_OR_UPDATE_FESTIVAL))
		{
			for (Map<Integer, StatSet> map : _festivalData.values())
			{
				for (StatSet set : map.values())
				{
					ps.setInt(1, set.getInteger("festivalId"));
					ps.setString(2, set.getString("cabal"));
					ps.setInt(3, set.getInteger("cycle"));
					ps.setLong(4, set.getLong("date"));
					ps.setInt(5, set.getInteger("score"));
					ps.setString(6, set.getString("members"));
					ps.addBatch();
				}
			}
			ps.executeBatch();
			
			// Updates Seven Signs DB data also, so call only if really necessary.
			if (updateSettings)
				SevenSignsManager.getInstance().saveSevenSignsStatus();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't save Seven Signs Festival data.", e);
		}
	}
	
	public void rewardHighestRanked()
	{
		for (int i = 0; i < FESTIVAL_COUNT; i++)
		{
			final StatSet set = getOverallHighestScoreData(i);
			if (set != null)
			{
				for (String playerName : set.getString("members").split(","))
					addReputationPointsForPartyMemberClan(playerName);
			}
		}
	}
	
	private static void addReputationPointsForPartyMemberClan(String playerName)
	{
		final Player player = World.getInstance().getPlayer(playerName);
		if (player != null)
		{
			if (player.getClan() != null)
			{
				player.getClan().addReputationScore(100);
				player.getClan().broadcastToMembers(SystemMessage.getSystemMessage(SystemMessageId.CLAN_MEMBER_S1_WAS_IN_HIGHEST_RANKED_PARTY_IN_FESTIVAL_OF_DARKNESS_AND_GAINED_S2_REPUTATION).addString(playerName).addNumber(100));
			}
		}
		else
		{
			try (Connection con = ConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(GET_CLAN_NAME))
			{
				ps.setString(1, playerName);
				
				try (ResultSet rs = ps.executeQuery())
				{
					if (rs.next())
					{
						final String clanName = rs.getString("clan_name");
						if (clanName != null)
						{
							final Clan clan = ClanTable.getInstance().getClanByName(clanName);
							if (clan != null)
							{
								clan.addReputationScore(100);
								clan.broadcastToMembers(SystemMessage.getSystemMessage(SystemMessageId.CLAN_MEMBER_S1_WAS_IN_HIGHEST_RANKED_PARTY_IN_FESTIVAL_OF_DARKNESS_AND_GAINED_S2_REPUTATION).addString(playerName).addNumber(100));
							}
						}
					}
				}
			}
			catch (Exception e)
			{
				LOGGER.error("Couldn't get clan name of {}.", e, playerName);
			}
		}
	}
	
	/**
	 * Used to reset all festival data at the beginning of a new quest event period.
	 * @param updateSettings
	 */
	public void resetFestivalData(boolean updateSettings)
	{
		_festivalCycle = 0;
		_signsCycle = SevenSignsManager.getInstance().getCurrentCycle();
		
		// Set all accumulated bonuses back to 0.
		for (int i = 0; i < FESTIVAL_COUNT; i++)
			_accumulatedBonuses.set(i, 0);
		
		_dawnFestivalParticipants.clear();
		_duskFestivalParticipants.clear();
		
		_dawnPreviousParticipants.clear();
		_duskPreviousParticipants.clear();
		
		_dawnFestivalScores.clear();
		_duskFestivalScores.clear();
		
		// Set up a new data set for the current cycle of festivals
		final Map<Integer, StatSet> map = new HashMap<>();
		
		for (int i = 0; i < FESTIVAL_COUNT * 2; i++)
		{
			int festivalId = i;
			
			if (i >= FESTIVAL_COUNT)
				festivalId -= FESTIVAL_COUNT;
			
			// Create a new StatSet with "default" data for Dusk
			final StatSet set = new StatSet();
			set.set("festivalId", festivalId);
			set.set("cycle", _signsCycle);
			set.set("date", "0");
			set.set("score", 0);
			set.set("members", "");
			set.set("cabal", (i >= FESTIVAL_COUNT) ? CabalType.DAWN : CabalType.DUSK);
			
			map.put(i, set);
		}
		
		// Add the newly created cycle data to the existing festival data, and subsequently save it to the database.
		_festivalData.put(_signsCycle, map);
		
		saveFestivalData(updateSettings);
		
		// Remove any unused blood offerings from online players.
		for (Player player : World.getInstance().getPlayers())
		{
			ItemInstance bloodOfferings = player.getInventory().getItemByItemId(FESTIVAL_OFFERING_ID);
			if (bloodOfferings != null)
				player.destroyItem(bloodOfferings, false);
		}
		
		LOGGER.info("Reinitialized Seven Signs Festival for next competition period.");
	}
	
	public final int getCurrentFestivalCycle()
	{
		return _festivalCycle;
	}
	
	public final boolean isFestivalInitialized()
	{
		return _festivalInitialized;
	}
	
	public final boolean isFestivalInProgress()
	{
		return _festivalInProgress;
	}
	
	public void setNextCycleStart()
	{
		_nextFestivalCycleStart = System.currentTimeMillis() + Config.FESTIVAL_CYCLE_LENGTH;
	}
	
	public void setNextFestivalStart(long milliFromNow)
	{
		_nextFestivalStart = System.currentTimeMillis() + milliFromNow;
	}
	
	public final int getMinsToNextCycle()
	{
		if (SevenSignsManager.getInstance().isSealValidationPeriod())
			return -1;
		
		return Math.round((_nextFestivalCycleStart - System.currentTimeMillis()) / 60000);
	}
	
	public final int getMinsToNextFestival()
	{
		if (SevenSignsManager.getInstance().isSealValidationPeriod())
			return -1;
		
		return Math.round((_nextFestivalStart - System.currentTimeMillis()) / 60000) + 1;
	}
	
	public final String getTimeToNextFestivalStr()
	{
		if (SevenSignsManager.getInstance().isSealValidationPeriod())
			return "<font color=\"FF0000\">This is the Seal Validation period. Festivals will resume next week.</font>";
		
		return "<font color=\"FF0000\">The next festival will begin in " + getMinsToNextFestival() + " minute(s).</font>";
	}
	
	/**
	 * Returns the current festival ID and oracle ID that the specified player is in, but will return the default of {-1, -1} if the player is not found as a participant.
	 * @param player
	 * @return int[] playerFestivalInfo
	 */
	public final int[] getFestivalForPlayer(Player player)
	{
		int[] playerFestivalInfo =
		{
			-1,
			-1
		};
		int festivalId = 0;
		
		while (festivalId < FESTIVAL_COUNT)
		{
			List<Integer> participants = _dawnFestivalParticipants.get(festivalId);
			
			// If there are no participants in this festival, move on to the next.
			if (participants != null && participants.contains(player.getObjectId()))
			{
				playerFestivalInfo[0] = CabalType.DAWN.ordinal();
				playerFestivalInfo[1] = festivalId;
				
				return playerFestivalInfo;
			}
			
			participants = _duskFestivalParticipants.get(++festivalId);
			
			if (participants != null && participants.contains(player.getObjectId()))
			{
				playerFestivalInfo[0] = CabalType.DUSK.ordinal();
				playerFestivalInfo[1] = festivalId;
				
				return playerFestivalInfo;
			}
			
			festivalId++;
		}
		
		// Return default data if the player is not found as a participant.
		return playerFestivalInfo;
	}
	
	public final boolean isParticipant(Player player)
	{
		if (SevenSignsManager.getInstance().isSealValidationPeriod())
			return false;
		
		if (_managerInstance == null)
			return false;
		
		for (List<Integer> participants : _dawnFestivalParticipants.values())
			if (participants != null && participants.contains(player.getObjectId()))
				return true;
			
		for (List<Integer> participants : _duskFestivalParticipants.values())
			if (participants != null && participants.contains(player.getObjectId()))
				return true;
			
		return false;
	}
	
	public final List<Integer> getParticipants(CabalType oracle, int festivalId)
	{
		if (oracle == CabalType.DAWN)
			return _dawnFestivalParticipants.get(festivalId);
		
		return _duskFestivalParticipants.get(festivalId);
	}
	
	public final List<Integer> getPreviousParticipants(CabalType oracle, int festivalId)
	{
		if (oracle == CabalType.DAWN)
			return _dawnPreviousParticipants.get(festivalId);
		
		return _duskPreviousParticipants.get(festivalId);
	}
	
	public void setParticipants(CabalType oracle, int festivalId, Party festivalParty)
	{
		List<Integer> participants = null;
		
		if (festivalParty != null)
		{
			participants = new ArrayList<>(festivalParty.getMembersCount());
			for (Player player : festivalParty.getMembers())
				participants.add(player.getObjectId());
		}
		
		if (oracle == CabalType.DAWN)
			_dawnFestivalParticipants.put(festivalId, participants);
		else
			_duskFestivalParticipants.put(festivalId, participants);
	}
	
	public void updateParticipants(Player player, Party festivalParty)
	{
		if (!isParticipant(player))
			return;
		
		final int[] playerFestInfo = getFestivalForPlayer(player);
		final CabalType oracle = CabalType.VALUES[playerFestInfo[0]];
		final int festivalId = playerFestInfo[1];
		
		if (festivalId > -1)
		{
			if (_festivalInitialized)
			{
				L2DarknessFestival festivalInst = _managerInstance.getFestivalInstance(oracle, festivalId);
				
				// leader has left
				if (festivalParty == null)
				{
					for (int partyMemberObjId : getParticipants(oracle, festivalId))
					{
						Player partyMember = World.getInstance().getPlayer(partyMemberObjId);
						if (partyMember == null)
							continue;
						
						festivalInst.relocatePlayer(partyMember, true);
					}
				}
				else
					festivalInst.relocatePlayer(player, true);
			}
			
			setParticipants(oracle, festivalId, festivalParty);
			
			// Check on disconnect if min player in party
			if (festivalParty != null && festivalParty.getMembersCount() < Config.FESTIVAL_MIN_PLAYER)
			{
				updateParticipants(player, null); // under minimum count
				festivalParty.removePartyMember(player, MessageType.EXPELLED);
			}
		}
	}
	
	public final int getFinalScore(CabalType oracle, int festivalId)
	{
		if (oracle == CabalType.DAWN)
			return _dawnFestivalScores.get(festivalId);
		
		return _duskFestivalScores.get(festivalId);
	}
	
	public final int getHighestScore(CabalType oracle, int festivalId)
	{
		return getHighestScoreData(oracle, festivalId).getInteger("score");
	}
	
	/**
	 * @param oracle : The {@link CabalType} to test.
	 * @param festivalId : The festival id to test.
	 * @return The {@link StatSet} containing the highest score <b>this cycle</b> for the the specified {@link CabalType} and festival id.
	 */
	public final StatSet getHighestScoreData(CabalType oracle, int festivalId)
	{
		int offsetId = festivalId;
		if (oracle == CabalType.DAWN)
			offsetId += 5;
		
		return _festivalData.get(_signsCycle).get(offsetId);
	}
	
	/**
	 * @param festivalId : The festival id to test.
	 * @return The {@link StatSet} containing the highest ever recorded score data for the specified festival id.
	 */
	public final StatSet getOverallHighestScoreData(int festivalId)
	{
		StatSet set = null;
		int highestScore = 0;
		
		for (Map<Integer, StatSet> map : _festivalData.values())
		{
			for (StatSet setToTest : map.values())
			{
				int currFestID = setToTest.getInteger("festivalId");
				int festivalScore = setToTest.getInteger("score");
				
				if (currFestID != festivalId)
					continue;
				
				if (festivalScore > highestScore)
				{
					highestScore = festivalScore;
					set = setToTest;
				}
			}
		}
		
		return set;
	}
	
	/**
	 * Set the final score details for the last participants of the specified festival data. Returns <b>true</b> if the score is higher than that previously recorded <b>this cycle</b>.
	 * @param player
	 * @param oracle
	 * @param festival
	 * @param offeringScore
	 * @return boolean isHighestScore
	 */
	public boolean setFinalScore(Player player, CabalType oracle, FestivalType festival, int offeringScore)
	{
		final int festivalId = festival.ordinal();
		
		int currDawnHighScore = getHighestScore(CabalType.DAWN, festivalId);
		int currDuskHighScore = getHighestScore(CabalType.DUSK, festivalId);
		
		int thisCabalHighScore = 0;
		int otherCabalHighScore = 0;
		
		if (oracle == CabalType.DAWN)
		{
			thisCabalHighScore = currDawnHighScore;
			otherCabalHighScore = currDuskHighScore;
			
			_dawnFestivalScores.put(festivalId, offeringScore);
		}
		else
		{
			thisCabalHighScore = currDuskHighScore;
			otherCabalHighScore = currDawnHighScore;
			
			_duskFestivalScores.put(festivalId, offeringScore);
		}
		
		StatSet set = getHighestScoreData(oracle, festivalId);
		
		// Check if this is the highest score for this level range so far for the player's cabal.
		if (offeringScore > thisCabalHighScore)
		{
			// If the current score is greater than that for the other cabal,
			// then they already have the points from this festival.
			if (thisCabalHighScore < otherCabalHighScore)
				return false;
			
			final List<String> partyMembers = new ArrayList<>();
			for (int partyMember : getPreviousParticipants(oracle, festivalId))
				partyMembers.add(PlayerInfoTable.getInstance().getPlayerName(partyMember));
			
			// Update the highest scores and party list.
			set.set("date", String.valueOf(System.currentTimeMillis()));
			set.set("score", offeringScore);
			set.set("members", String.join(",", partyMembers));
			
			// Add the festival score to the cabal' score.
			SevenSignsManager.getInstance().addFestivalScore(oracle, festival.getMaxScore());
			
			saveFestivalData(true);
			
			return true;
		}
		
		return false;
	}
	
	public final int getAccumulatedBonus(int festivalId)
	{
		return _accumulatedBonuses.get(festivalId);
	}
	
	public final int getTotalAccumulatedBonus()
	{
		int totalAccumBonus = 0;
		
		for (int accumBonus : _accumulatedBonuses)
			totalAccumBonus += accumBonus;
		
		return totalAccumBonus;
	}
	
	public void addAccumulatedBonus(int festivalId, int stoneType, int stoneAmount)
	{
		int eachStoneBonus = 0;
		
		switch (stoneType)
		{
			case SevenSignsManager.SEAL_STONE_BLUE_ID:
				eachStoneBonus = SevenSignsManager.SEAL_STONE_BLUE_VALUE;
				break;
			case SevenSignsManager.SEAL_STONE_GREEN_ID:
				eachStoneBonus = SevenSignsManager.SEAL_STONE_GREEN_VALUE;
				break;
			case SevenSignsManager.SEAL_STONE_RED_ID:
				eachStoneBonus = SevenSignsManager.SEAL_STONE_RED_VALUE;
				break;
		}
		
		int newTotalBonus = _accumulatedBonuses.get(festivalId) + (stoneAmount * eachStoneBonus);
		_accumulatedBonuses.set(festivalId, newTotalBonus);
	}
	
	/**
	 * Calculate and return the proportion of the accumulated bonus for the festival where the player was in the winning party, if the winning party's cabal won the event. The accumulated bonus is then updated, with the player's share deducted.
	 * @param player
	 * @return playerBonus (the share of the bonus for the party)
	 */
	public final int distribAccumulatedBonus(Player player)
	{
		if (SevenSignsManager.getInstance().getPlayerCabal(player.getObjectId()) != SevenSignsManager.getInstance().getWinningCabal())
			return 0;
		
		final Map<Integer, StatSet> map = _festivalData.get(_signsCycle);
		if (map == null)
			return 0;
		
		final String playerName = player.getName();
		
		int playerBonus = 0;
		for (StatSet set : map.values())
		{
			final String members = set.getString("members");
			if (members.indexOf(playerName) > -1)
			{
				final int festivalId = set.getInteger("festivalId");
				final int numPartyMembers = members.split(",").length;
				final int totalAccumBonus = _accumulatedBonuses.get(festivalId);
				
				playerBonus = totalAccumBonus / numPartyMembers;
				_accumulatedBonuses.set(festivalId, totalAccumBonus - playerBonus);
				break;
			}
		}
		
		return playerBonus;
	}
	
	/**
	 * Basically a wrapper-call to signal to increase the challenge of the specified festival.
	 * @param oracle
	 * @param festivalId
	 * @return boolean isChalIncreased
	 */
	public final boolean increaseChallenge(CabalType oracle, int festivalId)
	{
		return _managerInstance.getFestivalInstance(oracle, festivalId).increaseChallenge();
	}
	
	/**
	 * Add zone for use with announcements in the oracles.
	 * @param zone : Zone to be added.
	 * @param dawn : Is dawn zone.
	 */
	public void addPeaceZone(PeaceZone zone, boolean dawn)
	{
		if (dawn)
		{
			if (_dawnPeace == null)
				_dawnPeace = new ArrayList<>(2);
			
			if (!_dawnPeace.contains(zone))
				_dawnPeace.add(zone);
		}
		else
		{
			if (_duskPeace == null)
				_duskPeace = new ArrayList<>(2);
			
			if (!_duskPeace.contains(zone))
				_duskPeace.add(zone);
		}
	}
	
	/**
	 * Used to send a "shout" message to all players currently present in an Oracle. Primarily used for Festival Guide and Witch related speech.
	 * @param senderName
	 * @param message
	 */
	public void sendMessageToAll(String senderName, String message)
	{
		// TODO get NPC instance and convert to NpcSay
		final CreatureSay cs = new CreatureSay(0, SayType.SHOUT, senderName, message);
		
		if (_dawnPeace != null)
			for (PeaceZone zone : _dawnPeace)
				zone.broadcastPacket(cs);
			
		if (_duskPeace != null)
			for (PeaceZone zone : _duskPeace)
				zone.broadcastPacket(cs);
	}
	
	/**
	 * The FestivalManager class is the main runner of all the festivals. It is used for easier integration and management of all running festivals.
	 * @author Tempy
	 */
	private class FestivalManager implements Runnable
	{
		protected Map<Integer, L2DarknessFestival> _festivalInstances;
		
		public FestivalManager()
		{
			_festivalInstances = new HashMap<>();
			_managerInstance = this;
			
			// Increment the cycle counter.
			_festivalCycle++;
			
			// Set the next start timers.
			setNextCycleStart();
			setNextFestivalStart(Config.FESTIVAL_CYCLE_LENGTH - FESTIVAL_SIGNUP_TIME);
		}
		
		@Override
		public synchronized void run()
		{
			// The manager shouldn't be running if Seal Validation is in effect.
			if (SevenSignsManager.getInstance().isSealValidationPeriod())
				return;
				
			// If the next period is due to start before the end of this
			// festival cycle, then don't run it.
			if (SevenSignsManager.getInstance().getMilliToPeriodChange() < Config.FESTIVAL_CYCLE_LENGTH)
				return;
			
			if (getMinsToNextFestival() == 2)
				sendMessageToAll("Festival Guide", "The main event will start in 2 minutes. Please register now.");
			
			// Stand by until the allowed signup period has elapsed.
			try
			{
				wait(FESTIVAL_SIGNUP_TIME);
			}
			catch (InterruptedException ie)
			{
				// Do nothing.
			}
			
			// Clear past participants, they can no longer register their score if not done so already.
			_dawnPreviousParticipants.clear();
			_duskPreviousParticipants.clear();
			
			// Get rid of random monsters that avoided deletion after last festival
			for (L2DarknessFestival festivalInst : _festivalInstances.values())
				festivalInst.unspawnMobs();
			
			// Start only if participants signed up
			_noPartyRegister = true;
			
			while (_noPartyRegister)
			{
				if ((_duskFestivalParticipants.isEmpty() && _dawnFestivalParticipants.isEmpty()))
				{
					try
					{
						setNextCycleStart();
						setNextFestivalStart(Config.FESTIVAL_CYCLE_LENGTH - FESTIVAL_SIGNUP_TIME);
						wait(Config.FESTIVAL_CYCLE_LENGTH - FESTIVAL_SIGNUP_TIME);
						for (L2DarknessFestival festivalInst : _festivalInstances.values())
						{
							if (!festivalInst._npcInsts.isEmpty())
								festivalInst.unspawnMobs();
						}
					}
					catch (InterruptedException ie)
					{
						// Do nothing.
					}
				}
				else
					_noPartyRegister = false;
			}
			
			/* INITIATION */
			// Set the festival timer to 0, as it is just beginning.
			long elapsedTime = 0;
			
			// Create the instances for the festivals in both Oracles,
			// but only if they have participants signed up for them.
			for (int i = 0; i < FESTIVAL_COUNT; i++)
			{
				if (_duskFestivalParticipants.get(i) != null)
					_festivalInstances.put(10 + i, new L2DarknessFestival(CabalType.DUSK, i));
				
				if (_dawnFestivalParticipants.get(i) != null)
					_festivalInstances.put(20 + i, new L2DarknessFestival(CabalType.DAWN, i));
			}
			
			// Prevent future signups while festival is in progress.
			_festivalInitialized = true;
			
			setNextFestivalStart(Config.FESTIVAL_CYCLE_LENGTH);
			sendMessageToAll("Festival Guide", "The main event is now starting.");
			
			// Stand by for a short length of time before starting the festival.
			try
			{
				wait(Config.FESTIVAL_FIRST_SPAWN);
			}
			catch (InterruptedException ie)
			{
				// Do nothing.
			}
			
			elapsedTime = Config.FESTIVAL_FIRST_SPAWN;
			
			// Participants can now opt to increase the challenge, if desired.
			_festivalInProgress = true;
			
			/* PROPOGATION */
			// Sequentially set all festivals to begin, spawn the Festival Witch and notify participants.
			for (L2DarknessFestival festivalInst : _festivalInstances.values())
			{
				festivalInst.festivalStart();
				festivalInst.sendMessageToParticipants("The main event is now starting.");
			}
			
			// After a short time period, move all idle spawns to the center of the arena.
			try
			{
				wait(Config.FESTIVAL_FIRST_SWARM - Config.FESTIVAL_FIRST_SPAWN);
			}
			catch (InterruptedException ie)
			{
				// Do nothing.
			}
			
			elapsedTime += Config.FESTIVAL_FIRST_SWARM - Config.FESTIVAL_FIRST_SPAWN;
			
			for (L2DarknessFestival festivalInst : _festivalInstances.values())
				festivalInst.moveMonstersToCenter();
			
			// Stand by until the time comes for the second spawn.
			try
			{
				wait(Config.FESTIVAL_SECOND_SPAWN - Config.FESTIVAL_FIRST_SWARM);
			}
			catch (InterruptedException ie)
			{
				// Do nothing.
			}
			
			// Spawn an extra set of monsters (archers) on the free platforms with
			// a faster respawn when killed.
			for (L2DarknessFestival festivalInst : _festivalInstances.values())
			{
				festivalInst.spawnFestivalMonsters(FESTIVAL_DEFAULT_RESPAWN / 2, 2);
				
				long end = (Config.FESTIVAL_LENGTH - Config.FESTIVAL_SECOND_SPAWN) / 60000;
				festivalInst.sendMessageToParticipants("The Festival of Darkness will end in " + end + " minute(s).");
			}
			
			elapsedTime += Config.FESTIVAL_SECOND_SPAWN - Config.FESTIVAL_FIRST_SWARM;
			
			// After another short time period, again move all idle spawns to the center of the arena.
			try
			{
				wait(Config.FESTIVAL_SECOND_SWARM - Config.FESTIVAL_SECOND_SPAWN);
			}
			catch (InterruptedException ie)
			{
				// Do nothing.
			}
			
			for (L2DarknessFestival festivalInst : _festivalInstances.values())
				festivalInst.moveMonstersToCenter();
			
			elapsedTime += Config.FESTIVAL_SECOND_SWARM - Config.FESTIVAL_SECOND_SPAWN;
			
			// Stand by until the time comes for the chests to be spawned.
			try
			{
				wait(Config.FESTIVAL_CHEST_SPAWN - Config.FESTIVAL_SECOND_SWARM);
			}
			catch (InterruptedException ie)
			{
				// Do nothing.
			}
			
			// Spawn the festival chests, which enable the team to gain greater rewards
			// for each chest they kill.
			for (L2DarknessFestival festivalInst : _festivalInstances.values())
			{
				festivalInst.spawnFestivalMonsters(FESTIVAL_DEFAULT_RESPAWN, 3);
				festivalInst.sendMessageToParticipants("The chests have spawned! Be quick, the festival will end soon."); // FIXME What is the correct npcString?
			}
			
			elapsedTime += Config.FESTIVAL_CHEST_SPAWN - Config.FESTIVAL_SECOND_SWARM;
			
			// Stand by and wait until it's time to end the festival.
			try
			{
				wait(Config.FESTIVAL_LENGTH - elapsedTime);
			}
			catch (InterruptedException ie)
			{
				// Do nothing.
			}
			
			// Participants can no longer opt to increase the challenge, as the festival will soon close.
			_festivalInProgress = false;
			
			/* TERMINATION */
			// Sequentially begin the ending sequence for all running festivals.
			for (L2DarknessFestival festivalInst : _festivalInstances.values())
				festivalInst.festivalEnd();
			
			// Clear the participants list for the next round of signups.
			_dawnFestivalParticipants.clear();
			_duskFestivalParticipants.clear();
			
			// Allow signups for the next festival cycle.
			_festivalInitialized = false;
			
			sendMessageToAll("Festival Witch", "That will do! I'll move you to the outside soon.");
		}
		
		/**
		 * Returns the running instance of a festival for the given Oracle and festivalID. <BR>
		 * A <B>null</B> value is returned if there are no participants in that festival.
		 * @param oracle
		 * @param festivalId
		 * @return L2DarknessFestival festivalInst
		 */
		public final L2DarknessFestival getFestivalInstance(CabalType oracle, int festivalId)
		{
			if (!isFestivalInitialized())
				return null;
			
			/*
			 * Compute the offset if a Dusk instance is required. ID: 0 1 2 3 4 Dusk 1: 10 11 12 13 14 Dawn 2: 20 21 22 23 24
			 */
			
			festivalId += (oracle == CabalType.DUSK) ? 10 : 20;
			return _festivalInstances.get(festivalId);
		}
	}
	
	/**
	 * Each running festival is represented by an L2DarknessFestival class. It contains all the spawn information and data for the running festival. All festivals are managed by the FestivalManager class, which must be initialized first.
	 * @author Tempy
	 */
	private class L2DarknessFestival
	{
		protected final CabalType _cabal;
		protected final int _levelRange;
		protected boolean _challengeIncreased;
		
		private FestivalSpawn _startLocation;
		private FestivalSpawn _witchSpawn;
		
		private Npc _witchInst;
		protected final List<FestivalMonster> _npcInsts;
		
		private List<Integer> _participants;
		private final Map<Integer, FestivalSpawn> _originalLocations;
		
		protected L2DarknessFestival(CabalType cabal, int levelRange)
		{
			_cabal = cabal;
			_levelRange = levelRange;
			_originalLocations = new HashMap<>();
			_npcInsts = new ArrayList<>();
			
			if (cabal == CabalType.DAWN)
			{
				_participants = _dawnFestivalParticipants.get(levelRange);
				_witchSpawn = new FestivalSpawn(FESTIVAL_DAWN_WITCH_SPAWNS[levelRange]);
				_startLocation = new FestivalSpawn(FESTIVAL_DAWN_PLAYER_SPAWNS[levelRange]);
			}
			else
			{
				_participants = _duskFestivalParticipants.get(levelRange);
				_witchSpawn = new FestivalSpawn(FESTIVAL_DUSK_WITCH_SPAWNS[levelRange]);
				_startLocation = new FestivalSpawn(FESTIVAL_DUSK_PLAYER_SPAWNS[levelRange]);
			}
			
			// FOR TESTING!
			if (_participants == null)
				_participants = new ArrayList<>();
			
			festivalInit();
		}
		
		protected void festivalInit()
		{
			boolean isPositive;
			
			// Teleport all players to arena and notify them.
			if (_participants != null && !_participants.isEmpty())
			{
				for (int participantObjId : _participants)
				{
					Player participant = World.getInstance().getPlayer(participantObjId);
					if (participant == null)
						continue;
					
					_originalLocations.put(participantObjId, new FestivalSpawn(participant.getX(), participant.getY(), participant.getZ(), participant.getHeading()));
					
					// Randomize the spawn point around the specific centerpoint for each player.
					int x = _startLocation._x;
					int y = _startLocation._y;
					
					isPositive = (Rnd.get(2) == 1);
					
					if (isPositive)
					{
						x += Rnd.get(FESTIVAL_MAX_OFFSET_X);
						y += Rnd.get(FESTIVAL_MAX_OFFSET_Y);
					}
					else
					{
						x -= Rnd.get(FESTIVAL_MAX_OFFSET_X);
						y -= Rnd.get(FESTIVAL_MAX_OFFSET_Y);
					}
					
					participant.getAI().tryToIdle();
					participant.teleportTo(x, y, _startLocation._z, 20);
					
					// Remove all buffs from all participants on entry. Works like the skill Cancel.
					participant.stopAllEffectsExceptThoseThatLastThroughDeath();
					
					// Remove any stray blood offerings in inventory
					ItemInstance bloodOfferings = participant.getInventory().getItemByItemId(FESTIVAL_OFFERING_ID);
					if (bloodOfferings != null)
						participant.destroyItem(bloodOfferings, true);
				}
			}
			
			// Spawn the festival witch for this arena
			try
			{
				final Spawn spawn = new Spawn(_witchSpawn._npcId);
				spawn.setLoc(_witchSpawn._x, _witchSpawn._y, _witchSpawn._z, _witchSpawn._heading);
				spawn.setRespawnDelay(1);
				
				_witchInst = spawn.doSpawn(false);
			}
			catch (Exception e)
			{
				LOGGER.error("Couldn't properly spawn Festival Witch {}.", e, _witchSpawn._npcId);
			}
			
			// Make it appear as though the Witch has apparated there.
			MagicSkillUse msu = new MagicSkillUse(_witchInst, _witchInst, 2003, 1, 1, 0);
			_witchInst.broadcastPacket(msu);
			
			// And another one...:D
			msu = new MagicSkillUse(_witchInst, _witchInst, 2133, 1, 1, 0);
			_witchInst.broadcastPacket(msu);
			
			// Send a message to all participants from the witch.
			sendMessageToParticipants("The festival will begin in 2 minutes.");
		}
		
		protected void festivalStart()
		{
			spawnFestivalMonsters(FESTIVAL_DEFAULT_RESPAWN, 0);
		}
		
		protected void moveMonstersToCenter()
		{
			for (FestivalMonster festivalMob : _npcInsts)
			{
				if (festivalMob.isDead())
					continue;
				
				// Only move monsters that are idle or doing their usual functions.
				IntentionType currIntention = festivalMob.getAI().getCurrentIntention().getType();
				
				if (currIntention != IntentionType.IDLE)
					continue;
				
				int x = _startLocation._x;
				int y = _startLocation._y;
				
				if (Rnd.nextBoolean())
				{
					x += Rnd.get(FESTIVAL_MAX_OFFSET_X);
					y += Rnd.get(FESTIVAL_MAX_OFFSET_Y);
				}
				else
				{
					x -= Rnd.get(FESTIVAL_MAX_OFFSET_X);
					y -= Rnd.get(FESTIVAL_MAX_OFFSET_Y);
				}
				
				festivalMob.forceRunStance();
				festivalMob.getAI().addMoveToDesire(new Location(x, y, _startLocation._z), 1000000);
			}
		}
		
		/**
		 * Used to spawn monsters unique to the festival. <BR>
		 * Valid SpawnTypes:<BR>
		 * 0 - All Primary Monsters (starting monsters) <BR>
		 * 1 - Same as 0, but without archers/marksmen. (used for challenge increase) <BR>
		 * 2 - Secondary Monsters (archers) <BR>
		 * 3 - Festival Chests
		 * @param respawnDelay
		 * @param spawnType
		 */
		protected void spawnFestivalMonsters(int respawnDelay, int spawnType)
		{
			int[][] npcSpawns = null;
			
			switch (spawnType)
			{
				case 0, 1:
					npcSpawns = (_cabal == CabalType.DAWN) ? FESTIVAL_DAWN_PRIMARY_SPAWNS[_levelRange] : FESTIVAL_DUSK_PRIMARY_SPAWNS[_levelRange];
					break;
				
				case 2:
					npcSpawns = (_cabal == CabalType.DAWN) ? FESTIVAL_DAWN_SECONDARY_SPAWNS[_levelRange] : FESTIVAL_DUSK_SECONDARY_SPAWNS[_levelRange];
					break;
				
				case 3:
					npcSpawns = (_cabal == CabalType.DAWN) ? FESTIVAL_DAWN_CHEST_SPAWNS[_levelRange] : FESTIVAL_DUSK_CHEST_SPAWNS[_levelRange];
					break;
			}
			
			if (npcSpawns == null)
				return;
			
			for (int[] npcSpawn : npcSpawns)
			{
				FestivalSpawn currSpawn = new FestivalSpawn(npcSpawn);
				
				// Only spawn archers/marksmen if specified to do so.
				if (spawnType == 1 && isFestivalArcher(currSpawn._npcId))
					continue;
				
				try
				{
					final Spawn spawn = new Spawn(currSpawn._npcId);
					spawn.setLoc(currSpawn._x, currSpawn._y, currSpawn._z, Rnd.get(65536));
					spawn.setRespawnDelay(respawnDelay);
					
					FestivalMonster festivalMob = (FestivalMonster) spawn.doSpawn(false);
					
					// Set the offering bonus to 2x or 5x the amount per kill, if this spawn is part of an increased challenge or is a festival chest.
					if (spawnType == 1)
						festivalMob.setOfferingBonus(2);
					else if (spawnType == 3)
						festivalMob.setOfferingBonus(5);
					
					_npcInsts.add(festivalMob);
				}
				catch (Exception e)
				{
					LOGGER.error("Couldn't properly spawn Npc {}.", e, currSpawn._npcId);
				}
			}
		}
		
		protected boolean increaseChallenge()
		{
			if (_challengeIncreased)
				return false;
			
			// Set this flag to true to make sure that this can only be done once.
			_challengeIncreased = true;
			
			// Spawn more festival monsters, but this time with a twist.
			spawnFestivalMonsters(FESTIVAL_DEFAULT_RESPAWN, 1);
			return true;
		}
		
		public void sendMessageToParticipants(String message)
		{
			if (_participants != null && !_participants.isEmpty())
				_witchInst.broadcastNpcSay(message);
		}
		
		protected void festivalEnd()
		{
			if (_participants != null && !_participants.isEmpty())
			{
				for (int participantObjId : _participants)
				{
					Player participant = World.getInstance().getPlayer(participantObjId);
					if (participant == null)
						continue;
					
					relocatePlayer(participant, false);
					participant.sendMessage("The festival has ended. Your party leader must now register your score before the next festival takes place.");
				}
				
				if (_cabal == CabalType.DAWN)
					_dawnPreviousParticipants.put(_levelRange, _participants);
				else
					_duskPreviousParticipants.put(_levelRange, _participants);
			}
			_participants = null;
			
			unspawnMobs();
		}
		
		protected void unspawnMobs()
		{
			// Delete all the NPCs in the current festival arena.
			if (_witchInst != null)
				_witchInst.getSpawn().doDelete();
			
			if (_npcInsts != null)
				for (FestivalMonster monsterInst : _npcInsts)
					if (monsterInst != null)
						monsterInst.getSpawn().doDelete();
		}
		
		public void relocatePlayer(Player participant, boolean isRemoving)
		{
			if (participant == null)
				return;
			
			try
			{
				FestivalSpawn origPosition = _originalLocations.get(participant.getObjectId());
				
				if (isRemoving)
					_originalLocations.remove(participant.getObjectId());
				
				participant.getAI().tryToIdle();
				participant.teleportTo(origPosition._x, origPosition._y, origPosition._z, 20);
				participant.sendMessage("You have been removed from the festival arena.");
			}
			catch (Exception e)
			{
				// If an exception occurs, just move the player to the nearest town.
				participant.teleportTo(RestartType.TOWN);
				participant.sendMessage("You have been removed from the festival arena.");
			}
		}
	}
	
	private static class FestivalSpawn
	{
		protected final int _x;
		protected final int _y;
		protected final int _z;
		protected final int _heading;
		protected final int _npcId;
		
		protected FestivalSpawn(int x, int y, int z, int heading)
		{
			_x = x;
			_y = y;
			_z = z;
			
			// Generate a random heading if no positive one given.
			_heading = (heading < 0) ? Rnd.get(65536) : heading;
			
			_npcId = -1;
		}
		
		protected FestivalSpawn(int[] spawnData)
		{
			_x = spawnData[0];
			_y = spawnData[1];
			_z = spawnData[2];
			
			_heading = (spawnData[3] < 0) ? Rnd.get(65536) : spawnData[3];
			
			if (spawnData.length > 4)
				_npcId = spawnData[4];
			else
				_npcId = -1;
		}
	}
	
	public static FestivalOfDarknessManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final FestivalOfDarknessManager INSTANCE = new FestivalOfDarknessManager();
	}
}