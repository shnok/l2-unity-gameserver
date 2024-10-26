package com.shnok.javaserver.gameserver.network;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.commons.logging.CLogger;

/**
 * NpcStringId implementation
 */
public final class NpcStringId
{
	@NpcString(id = 1, message = "Hello! I am %s. Ha-ha, you are %s? Hee hee hee hee")
	public static NpcStringId ID_1;
	
	@NpcString(id = 2, message = "%s--%s--%s--%s//%s Hee hee giggle")
	public static NpcStringId ID_2;
	
	@NpcString(id = 3, message = "Opening")
	public static NpcStringId ID_3;
	
	@NpcString(id = 4, message = "Suspended")
	public static NpcStringId ID_4;
	
	@NpcString(id = 5, message = "%s/%s %s payment next working costs")
	public static NpcStringId ID_5;
	
	@NpcString(id = 6, message = "<font color=\"FFAABB\">%s</font> Adena /%s Day(s)")
	public static NpcStringId ID_6;
	
	@NpcString(id = 7, message = "%s<a action=\"bypass -h menu_select?ask=-270&reply=%s000\">Deactivate</a>%s")
	public static NpcStringId ID_7;
	
	@NpcString(id = 8, message = "Stage")
	public static NpcStringId ID_8;
	
	@NpcString(id = 9, message = "Stage %s")
	public static NpcStringId ID_9;
	
	@NpcString(id = 10, message = "%s%%")
	public static NpcStringId ID_10;
	
	@NpcString(id = 101, message = "Letters of Love")
	public static NpcStringId ID_101;
	
	@NpcString(id = 102, message = "Letters of Love (In Progress)")
	public static NpcStringId ID_102;
	
	@NpcString(id = 103, message = "Letters of Love (Done)")
	public static NpcStringId ID_103;
	
	@NpcString(id = 174, message = "I shall put you in a never-ending nightmare!")
	public static NpcStringId ID_174;
	
	@NpcString(id = 175, message = "My soul belongs to Icarus...")
	public static NpcStringId ID_175;
	
	@NpcString(id = 201, message = "What Women Want")
	public static NpcStringId ID_201;
	
	@NpcString(id = 202, message = "What Women Want (In Progress)")
	public static NpcStringId ID_202;
	
	@NpcString(id = 203, message = "What Women Want (Done)")
	public static NpcStringId ID_203;
	
	@NpcString(id = 301, message = "Will the Seal be Broken?")
	public static NpcStringId ID_301;
	
	@NpcString(id = 302, message = "Will the Seal be Broken? (In Progress)")
	public static NpcStringId ID_302;
	
	@NpcString(id = 303, message = "Will the Seal be Broken? (Done)")
	public static NpcStringId ID_303;
	
	@NpcString(id = 401, message = "Long Live the Pa'agrio Lord")
	public static NpcStringId ID_401;
	
	@NpcString(id = 402, message = "Long Live the Pa'agrio Lord (In Progress)")
	public static NpcStringId ID_402;
	
	@NpcString(id = 403, message = "Long Live the Pa'agrio Lord (Done)")
	public static NpcStringId ID_403;
	
	@NpcString(id = 501, message = "Miner's Favor")
	public static NpcStringId ID_501;
	
	@NpcString(id = 502, message = "Miner's Favor (In Progress)")
	public static NpcStringId ID_502;
	
	@NpcString(id = 503, message = "Miner's Favor (Done)")
	public static NpcStringId ID_503;
	
	@NpcString(id = 601, message = "Step Into the Future")
	public static NpcStringId ID_601;
	
	@NpcString(id = 602, message = "Step Into the Future (In Progress)")
	public static NpcStringId ID_602;
	
	@NpcString(id = 603, message = "Step Into the Future (Done)")
	public static NpcStringId ID_603;
	
	@NpcString(id = 701, message = "A Trip Begins")
	public static NpcStringId ID_701;
	
	@NpcString(id = 702, message = "A Trip Begins (In Progress)")
	public static NpcStringId ID_702;
	
	@NpcString(id = 703, message = "A Trip Begins (Done)")
	public static NpcStringId ID_703;
	
	@NpcString(id = 801, message = "An Adventure Begins")
	public static NpcStringId ID_801;
	
	@NpcString(id = 802, message = "An Adventure Begins (In Progress)")
	public static NpcStringId ID_802;
	
	@NpcString(id = 803, message = "An Adventure Begins (Done)")
	public static NpcStringId ID_803;
	
	@NpcString(id = 901, message = "Into the City of Humans")
	public static NpcStringId ID_901;
	
	@NpcString(id = 902, message = "Into the City of Humans (In Progress)")
	public static NpcStringId ID_902;
	
	@NpcString(id = 903, message = "Into the City of Humans (Done)")
	public static NpcStringId ID_903;
	
	@NpcString(id = 1001, message = "Into the World")
	public static NpcStringId ID_1001;
	
	@NpcString(id = 1002, message = "Into the World (In Progress)")
	public static NpcStringId ID_1002;
	
	@NpcString(id = 1003, message = "Into the World (Done)")
	public static NpcStringId ID_1003;
	
	@NpcString(id = 1101, message = "Secret meeting with Ketra Orcs")
	public static NpcStringId ID_1101;
	
	@NpcString(id = 1102, message = "Secret Meeting with Ketra Orcs (In Progress)")
	public static NpcStringId ID_1102;
	
	@NpcString(id = 1103, message = "Secret meeting with Ketra Orcs (Done)")
	public static NpcStringId ID_1103;
	
	@NpcString(id = 1201, message = "Secret meeting with Varka Silenos")
	public static NpcStringId ID_1201;
	
	@NpcString(id = 1202, message = "Secret Meeting with Varka Silenos (In Progress)")
	public static NpcStringId ID_1202;
	
	@NpcString(id = 1203, message = "Secret meeting with Varka Silenos (Done)")
	public static NpcStringId ID_1203;
	
	@NpcString(id = 1301, message = "Parcel Delivery")
	public static NpcStringId ID_1301;
	
	@NpcString(id = 1302, message = "Parcel Delivery (In Progress)")
	public static NpcStringId ID_1302;
	
	@NpcString(id = 1303, message = "Parcel Delivery (Done)")
	public static NpcStringId ID_1303;
	
	@NpcString(id = 1401, message = "Whereabouts of the Archaeologist")
	public static NpcStringId ID_1401;
	
	@NpcString(id = 1402, message = "Whereabouts of the Archaeologist (In Progress)")
	public static NpcStringId ID_1402;
	
	@NpcString(id = 1403, message = "Whereabouts of the Archaeologist (Done)")
	public static NpcStringId ID_1403;
	
	@NpcString(id = 1501, message = "Sweet Whispers")
	public static NpcStringId ID_1501;
	
	@NpcString(id = 1502, message = "Sweet Whispers (In Progress)")
	public static NpcStringId ID_1502;
	
	@NpcString(id = 1503, message = "Sweet Whispers (Done)")
	public static NpcStringId ID_1503;
	
	@NpcString(id = 1601, message = "The Coming Darkness")
	public static NpcStringId ID_1601;
	
	@NpcString(id = 1602, message = "The Coming Darkness (In Progress)")
	public static NpcStringId ID_1602;
	
	@NpcString(id = 1603, message = "The Coming Darkness (Done)")
	public static NpcStringId ID_1603;
	
	@NpcString(id = 1701, message = "Light and Darkness")
	public static NpcStringId ID_1701;
	
	@NpcString(id = 1702, message = "Light and Darkness (In Progress)")
	public static NpcStringId ID_1702;
	
	@NpcString(id = 1703, message = "Light and Darkness (Done)")
	public static NpcStringId ID_1703;
	
	@NpcString(id = 1801, message = "Meeting with the Golden Ram")
	public static NpcStringId ID_1801;
	
	@NpcString(id = 1802, message = "Meeting with the Golden Ram (In Progress)")
	public static NpcStringId ID_1802;
	
	@NpcString(id = 1803, message = "Meeting with the Golden Ram (Done)")
	public static NpcStringId ID_1803;
	
	@NpcString(id = 1901, message = "Go to the Pastureland")
	public static NpcStringId ID_1901;
	
	@NpcString(id = 1902, message = "Go to the Pastureland (In Progress)")
	public static NpcStringId ID_1902;
	
	@NpcString(id = 1903, message = "Go to the Pastureland (Done)")
	public static NpcStringId ID_1903;
	
	@NpcString(id = 2001, message = "Bring Up With Love")
	public static NpcStringId ID_2001;
	
	@NpcString(id = 2002, message = "Bring Up With Love (In Progress)")
	public static NpcStringId ID_2002;
	
	@NpcString(id = 2003, message = "Bring Up With Love (Done)")
	public static NpcStringId ID_2003;
	
	@NpcString(id = 2004, message = "What did you just do to me?")
	public static NpcStringId ID_2004;
	
	@NpcString(id = 2005, message = "Are you trying to tame me? Don't do that!")
	public static NpcStringId ID_2005;
	
	@NpcString(id = 2006, message = "Don't give such a thing.  You can endanger yourself!")
	public static NpcStringId ID_2006;
	
	@NpcString(id = 2007, message = "Yuck!  What is this?  It tastes terrible!")
	public static NpcStringId ID_2007;
	
	@NpcString(id = 2008, message = "I'm hungry.  Give me a little more, please.")
	public static NpcStringId ID_2008;
	
	@NpcString(id = 2009, message = "What is this?  Is this edible?")
	public static NpcStringId ID_2009;
	
	@NpcString(id = 2010, message = "Don't worry about me.")
	public static NpcStringId ID_2010;
	
	@NpcString(id = 2011, message = "Thank you.  That was delicious!")
	public static NpcStringId ID_2011;
	
	@NpcString(id = 2012, message = "I think I am starting to like you!")
	public static NpcStringId ID_2012;
	
	@NpcString(id = 2013, message = "Eeeeek!  Eeeeek!")
	public static NpcStringId ID_2013;
	
	@NpcString(id = 2014, message = "Don't keep trying to tame me.  I don't want to be tamed.")
	public static NpcStringId ID_2014;
	
	@NpcString(id = 2015, message = "It is just food to me.  Although it may also be your hand.")
	public static NpcStringId ID_2015;
	
	@NpcString(id = 2016, message = "If I keep eating like this, won't I become fat?  *chomp chomp*")
	public static NpcStringId ID_2016;
	
	@NpcString(id = 2017, message = "Why do you keep feeding me?")
	public static NpcStringId ID_2017;
	
	@NpcString(id = 2018, message = "Don't trust me.  I'm afraid I may betray you later.")
	public static NpcStringId ID_2018;
	
	@NpcString(id = 2019, message = "Grrrrr....")
	public static NpcStringId ID_2019;
	
	@NpcString(id = 2020, message = "You brought this upon yourself...!")
	public static NpcStringId ID_2020;
	
	@NpcString(id = 2021, message = "I feel strange...!  I keep having these evil thoughts...!")
	public static NpcStringId ID_2021;
	
	@NpcString(id = 2022, message = "Alas... so this is how it all ends...")
	public static NpcStringId ID_2022;
	
	@NpcString(id = 2023, message = "I don't feel so good... Oh, my mind is very troubled...!")
	public static NpcStringId ID_2023;
	
	@NpcString(id = 2024, message = "%s, so what do you think it is like to be tamed?")
	public static NpcStringId ID_2024;
	
	@NpcString(id = 2025, message = "%s, whenever I see spice, I think I will miss your hand that used to feed it to me.")
	public static NpcStringId ID_2025;
	
	@NpcString(id = 2026, message = "%s, don't go to the village.  I don't have the strength to follow you.")
	public static NpcStringId ID_2026;
	
	@NpcString(id = 2027, message = "Thank you for trusting me, %s. I hope I will be helpful to you.")
	public static NpcStringId ID_2027;
	
	@NpcString(id = 2028, message = "%s, will I be able to help you?")
	public static NpcStringId ID_2028;
	
	@NpcString(id = 2029, message = "I guess it's just my animal magnetism.")
	public static NpcStringId ID_2029;
	
	@NpcString(id = 2030, message = "Too much spicy food makes me sweat like a beast.")
	public static NpcStringId ID_2030;
	
	@NpcString(id = 2031, message = "Animals need love too.")
	public static NpcStringId ID_2031;
	
	@NpcString(id = 2032, message = "What'd I miss? What'd I miss?")
	public static NpcStringId ID_2032;
	
	@NpcString(id = 2033, message = "I just know before this is over, I'm gonna need a lot of serious therapy.")
	public static NpcStringId ID_2033;
	
	@NpcString(id = 2034, message = "I sense great wisdom in you... I'm an animal, and I got instincts.")
	public static NpcStringId ID_2034;
	
	@NpcString(id = 2035, message = "Remember, I'm here to help.")
	public static NpcStringId ID_2035;
	
	@NpcString(id = 2036, message = "Are we there yet?")
	public static NpcStringId ID_2036;
	
	@NpcString(id = 2037, message = "That really made me feel good to see that.")
	public static NpcStringId ID_2037;
	
	@NpcString(id = 2038, message = "Oh, no, no, no, Nooooo!")
	public static NpcStringId ID_2038;
	
	@NpcString(id = 2101, message = "Hidden Truth")
	public static NpcStringId ID_2101;
	
	@NpcString(id = 2102, message = "Hidden Truth (In Progress)")
	public static NpcStringId ID_2102;
	
	@NpcString(id = 2103, message = "Hidden Truth (Done)")
	public static NpcStringId ID_2103;
	
	@NpcString(id = 2150, message = "Who awoke me?")
	public static NpcStringId ID_2150;
	
	@NpcString(id = 2151, message = "My master has instructed me to be your guide, %s.")
	public static NpcStringId ID_2151;
	
	@NpcString(id = 2152, message = "Please check this bookcase, %s.")
	public static NpcStringId ID_2152;
	
	@NpcString(id = 2201, message = "Tragedy in Von Hellmann Forest")
	public static NpcStringId ID_2201;
	
	@NpcString(id = 2202, message = "Tragedy in Von Hellmann Forest (In Progress)")
	public static NpcStringId ID_2202;
	
	@NpcString(id = 2203, message = "Tragedy in Von Hellmann Forest (Done)")
	public static NpcStringId ID_2203;
	
	@NpcString(id = 2250, message = "Did you call me, %s?")
	public static NpcStringId ID_2250;
	
	@NpcString(id = 2251, message = "I'm confused! Maybe it's time to go back.")
	public static NpcStringId ID_2251;
	
	@NpcString(id = 2301, message = "Lidia's Heart")
	public static NpcStringId ID_2301;
	
	@NpcString(id = 2302, message = "Lidia's Heart (In Progress)")
	public static NpcStringId ID_2302;
	
	@NpcString(id = 2303, message = "Lidia's Heart (Done)")
	public static NpcStringId ID_2303;
	
	@NpcString(id = 2401, message = "Inhabitants of the Forest of the Dead")
	public static NpcStringId ID_2401;
	
	@NpcString(id = 2402, message = "Inhabitants of the Forest of the Dead (In Progress)")
	public static NpcStringId ID_2402;
	
	@NpcString(id = 2403, message = "Inhabitants of the Forest of the Dead (Done)")
	public static NpcStringId ID_2403;
	
	@NpcString(id = 2450, message = "That sign!")
	public static NpcStringId ID_2450;
	
	@NpcString(id = 2501, message = "Hiding Behind the Truth")
	public static NpcStringId ID_2501;
	
	@NpcString(id = 2502, message = "Hiding Behind the Truth (In Progress)")
	public static NpcStringId ID_2502;
	
	@NpcString(id = 2503, message = "Hiding Behind the Truth (Done)")
	public static NpcStringId ID_2503;
	
	@NpcString(id = 2550, message = "That box was sealed by my master, %s! Don't touch it!")
	public static NpcStringId ID_2550;
	
	@NpcString(id = 2551, message = "You've ended my immortal life! You're protected by the feudal lord, aren't you?")
	public static NpcStringId ID_2551;
	
	@NpcString(id = 2601, message = "Tired of Waiting")
	public static NpcStringId ID_2601;
	
	@NpcString(id = 2602, message = "Tired of Waiting (In Progress)")
	public static NpcStringId ID_2602;
	
	@NpcString(id = 2603, message = "Tired of Waiting (Done)")
	public static NpcStringId ID_2603;
	
	@NpcString(id = 2701, message = "Chest Caught with a Bait of Wind")
	public static NpcStringId ID_2701;
	
	@NpcString(id = 2702, message = "Chest Caught with a Bait of Wind (In Progress)")
	public static NpcStringId ID_2702;
	
	@NpcString(id = 2703, message = "Chest Caught with a Bait of Wind (Done)")
	public static NpcStringId ID_2703;
	
	@NpcString(id = 2801, message = "Chest Caught with a Bait of Icy Air")
	public static NpcStringId ID_2801;
	
	@NpcString(id = 2802, message = "Chest Caught with a Bait of Icy Air (In Progress)")
	public static NpcStringId ID_2802;
	
	@NpcString(id = 2803, message = "Chest Caught with a Bait of Icy Air (Done)")
	public static NpcStringId ID_2803;
	
	@NpcString(id = 2901, message = "Chest Caught with a Bait of Earth")
	public static NpcStringId ID_2901;
	
	@NpcString(id = 2902, message = "Chest Caught with a Bait of Earth (In Progress)")
	public static NpcStringId ID_2902;
	
	@NpcString(id = 2903, message = "Chest Caught with a Bait of Earth (Done)")
	public static NpcStringId ID_2903;
	
	@NpcString(id = 3001, message = "Chest Caught with a Bait of Flame")
	public static NpcStringId ID_3001;
	
	@NpcString(id = 3002, message = "Chest Caught with a Bait of Flame (In Progress)")
	public static NpcStringId ID_3002;
	
	@NpcString(id = 3003, message = "Chest Caught with a Bait of Flame (Done)")
	public static NpcStringId ID_3003;
	
	@NpcString(id = 3101, message = "Secret Buried in the Swamp")
	public static NpcStringId ID_3101;
	
	@NpcString(id = 3102, message = "Secret Buried in the Swamp (In Progress)")
	public static NpcStringId ID_3102;
	
	@NpcString(id = 3103, message = "Secret Buried in the Swamp (Done)")
	public static NpcStringId ID_3103;
	
	@NpcString(id = 3201, message = "An Obvious Lie")
	public static NpcStringId ID_3201;
	
	@NpcString(id = 3202, message = "An Obvious Lie (In Progress)")
	public static NpcStringId ID_3202;
	
	@NpcString(id = 3203, message = "An Obvious Lie (Done)")
	public static NpcStringId ID_3203;
	
	@NpcString(id = 3301, message = "Make a Pair of Dress Shoes")
	public static NpcStringId ID_3301;
	
	@NpcString(id = 3302, message = "Make a Pair of Dress Shoes (In Progress)")
	public static NpcStringId ID_3302;
	
	@NpcString(id = 3303, message = "Make a Pair of Dress Shoes (Done)")
	public static NpcStringId ID_3303;
	
	@NpcString(id = 3401, message = "In Search of Cloth")
	public static NpcStringId ID_3401;
	
	@NpcString(id = 3402, message = "In Search of Cloth (In Progress)")
	public static NpcStringId ID_3402;
	
	@NpcString(id = 3403, message = "In Search of Cloth (Done)")
	public static NpcStringId ID_3403;
	
	@NpcString(id = 3501, message = "Find Glittering Jewelry")
	public static NpcStringId ID_3501;
	
	@NpcString(id = 3502, message = "Find Glittering Jewelry (In Progress)")
	public static NpcStringId ID_3502;
	
	@NpcString(id = 3503, message = "Find Glittering Jewelry (Done)")
	public static NpcStringId ID_3503;
	
	@NpcString(id = 3601, message = "Make a Sewing Kit")
	public static NpcStringId ID_3601;
	
	@NpcString(id = 3602, message = "Make a Sewing Kit (In Progress)")
	public static NpcStringId ID_3602;
	
	@NpcString(id = 3603, message = "Make a Sewing Kit (Done)")
	public static NpcStringId ID_3603;
	
	@NpcString(id = 3701, message = "Make Formal Wear")
	public static NpcStringId ID_3701;
	
	@NpcString(id = 3702, message = "Make Formal Wear (In Progress)")
	public static NpcStringId ID_3702;
	
	@NpcString(id = 3703, message = "Make Formal Wear (Done)")
	public static NpcStringId ID_3703;
	
	@NpcString(id = 3801, message = "Dragon Fangs")
	public static NpcStringId ID_3801;
	
	@NpcString(id = 3802, message = "Dragon Fangs (In Progress)")
	public static NpcStringId ID_3802;
	
	@NpcString(id = 3803, message = "Dragon Fangs (Done)")
	public static NpcStringId ID_3803;
	
	@NpcString(id = 3901, message = "Red-eyed Invader")
	public static NpcStringId ID_3901;
	
	@NpcString(id = 3902, message = "Red-eyed Invader (In Progress)")
	public static NpcStringId ID_3902;
	
	@NpcString(id = 3903, message = "Red-eyed Invader (Done)")
	public static NpcStringId ID_3903;
	
	@NpcString(id = 4001, message = "A Special Order")
	public static NpcStringId ID_4001;
	
	@NpcString(id = 4002, message = "A Special Order (In Progress)")
	public static NpcStringId ID_4002;
	
	@NpcString(id = 4003, message = "A Special Order (Done)")
	public static NpcStringId ID_4003;
	
	@NpcString(id = 4101, message = "The Adventurer's Challenge")
	public static NpcStringId ID_4101;
	
	@NpcString(id = 4102, message = "The Adventurer's Challenge (In Progress)")
	public static NpcStringId ID_4102;
	
	@NpcString(id = 4103, message = "The Adventurer's Challenge (Done)")
	public static NpcStringId ID_4103;
	
	@NpcString(id = 4201, message = "Help the Uncle!")
	public static NpcStringId ID_4201;
	
	@NpcString(id = 4202, message = "Help the Uncle! (In Progress)")
	public static NpcStringId ID_4202;
	
	@NpcString(id = 4203, message = "Help the Uncle! (Done)")
	public static NpcStringId ID_4203;
	
	@NpcString(id = 4301, message = "Help the Sister!")
	public static NpcStringId ID_4301;
	
	@NpcString(id = 4302, message = "Help the Sister! (In Progress)")
	public static NpcStringId ID_4302;
	
	@NpcString(id = 4303, message = "Help the Sister! (Done)")
	public static NpcStringId ID_4303;
	
	@NpcString(id = 4401, message = "Help the Son!")
	public static NpcStringId ID_4401;
	
	@NpcString(id = 4402, message = "Help the Son! (In Progress)")
	public static NpcStringId ID_4402;
	
	@NpcString(id = 4403, message = "Help the Son! (Done)")
	public static NpcStringId ID_4403;
	
	@NpcString(id = 4501, message = "To Talking Island")
	public static NpcStringId ID_4501;
	
	@NpcString(id = 4502, message = "To Talking Island (In Progress)")
	public static NpcStringId ID_4502;
	
	@NpcString(id = 4503, message = "To Talking Island (Done)")
	public static NpcStringId ID_4503;
	
	@NpcString(id = 4601, message = "Once More In the Arms of the Mother Tree")
	public static NpcStringId ID_4601;
	
	@NpcString(id = 4602, message = "Once More In the Arms of the Mother Tree (In Progress)")
	public static NpcStringId ID_4602;
	
	@NpcString(id = 4603, message = "Once More In the Arms of the Mother Tree (Done)")
	public static NpcStringId ID_4603;
	
	@NpcString(id = 4701, message = "Into the Dark Elven Forest")
	public static NpcStringId ID_4701;
	
	@NpcString(id = 4702, message = "Into the Dark Elven Forest (In Progress)")
	public static NpcStringId ID_4702;
	
	@NpcString(id = 4703, message = "Into the Dark Elven Forest (Done)")
	public static NpcStringId ID_4703;
	
	@NpcString(id = 4801, message = "To the Immortal Plateau")
	public static NpcStringId ID_4801;
	
	@NpcString(id = 4802, message = "To the Immortal Plateau (In Progress)")
	public static NpcStringId ID_4802;
	
	@NpcString(id = 4803, message = "To the Immortal Plateau (Done)")
	public static NpcStringId ID_4803;
	
	@NpcString(id = 4901, message = "The Road Home")
	public static NpcStringId ID_4901;
	
	@NpcString(id = 4902, message = "The Road Home (In Progress)")
	public static NpcStringId ID_4902;
	
	@NpcString(id = 4903, message = "The Road Home (Done)")
	public static NpcStringId ID_4903;
	
	@NpcString(id = 5001, message = "Lanosco's Special Bait")
	public static NpcStringId ID_5001;
	
	@NpcString(id = 5002, message = "Lanosco's Special Bait (In Progress)")
	public static NpcStringId ID_5002;
	
	@NpcString(id = 5003, message = "Lanosco's Special Bait (Done)")
	public static NpcStringId ID_5003;
	
	@NpcString(id = 5101, message = "O'Fulle's Special Bait")
	public static NpcStringId ID_5101;
	
	@NpcString(id = 5102, message = "O'Fulle's Special Bait (In Progress)")
	public static NpcStringId ID_5102;
	
	@NpcString(id = 5103, message = "O'Fulle's Special Bait (Done)")
	public static NpcStringId ID_5103;
	
	@NpcString(id = 5201, message = "Willie's Special Bait")
	public static NpcStringId ID_5201;
	
	@NpcString(id = 5202, message = "Willie's Special Bait (In Progress)")
	public static NpcStringId ID_5202;
	
	@NpcString(id = 5203, message = "Willie's Special Bait (Done)")
	public static NpcStringId ID_5203;
	
	@NpcString(id = 5301, message = "Linnaeus' Special Bait")
	public static NpcStringId ID_5301;
	
	@NpcString(id = 5302, message = "Linnaeus' Special Bait (In Progress)")
	public static NpcStringId ID_5302;
	
	@NpcString(id = 5303, message = "Linnaeus' Special Bait (Done)")
	public static NpcStringId ID_5303;
	
	@NpcString(id = 6001, message = "Good Work's Reward")
	public static NpcStringId ID_6001;
	
	@NpcString(id = 6002, message = "Good Work's Reward (In Progress)")
	public static NpcStringId ID_6002;
	
	@NpcString(id = 6003, message = "Good Work's Reward (Done)")
	public static NpcStringId ID_6003;
	
	@NpcString(id = 6101, message = "Law Enforcement")
	public static NpcStringId ID_6101;
	
	@NpcString(id = 6102, message = "Law Enforcement (In Progress)")
	public static NpcStringId ID_6102;
	
	@NpcString(id = 6103, message = "Law Enforcement (Done)")
	public static NpcStringId ID_6103;
	
	@NpcString(id = 6201, message = "Path of the Trooper")
	public static NpcStringId ID_6201;
	
	@NpcString(id = 6202, message = "Path of the Trooper (In Progress)")
	public static NpcStringId ID_6202;
	
	@NpcString(id = 6203, message = "Path of the Trooper (Done)")
	public static NpcStringId ID_6203;
	
	@NpcString(id = 6301, message = "Path of the Warder")
	public static NpcStringId ID_6301;
	
	@NpcString(id = 6302, message = "Path of the Warder (In Progress)")
	public static NpcStringId ID_6302;
	
	@NpcString(id = 6303, message = "Path of the Warder (Done)")
	public static NpcStringId ID_6303;
	
	@NpcString(id = 6401, message = "Certified Berserker")
	public static NpcStringId ID_6401;
	
	@NpcString(id = 6402, message = "Certified Berserker (In Progress)")
	public static NpcStringId ID_6402;
	
	@NpcString(id = 6403, message = "Certified Berserker (Done)")
	public static NpcStringId ID_6403;
	
	@NpcString(id = 6501, message = "Certified Soul Breaker")
	public static NpcStringId ID_6501;
	
	@NpcString(id = 6502, message = "Certified Soul Breaker (In Progress)")
	public static NpcStringId ID_6502;
	
	@NpcString(id = 6503, message = "Certified Soul Breaker (Done)")
	public static NpcStringId ID_6503;
	
	@NpcString(id = 6601, message = "Certified Arbalester")
	public static NpcStringId ID_6601;
	
	@NpcString(id = 6602, message = "Certified Arbalester (In Progress)")
	public static NpcStringId ID_6602;
	
	@NpcString(id = 6603, message = "Certified Arbalester (Done)")
	public static NpcStringId ID_6603;
	
	@NpcString(id = 6701, message = "Saga of the Doombringer")
	public static NpcStringId ID_6701;
	
	@NpcString(id = 6702, message = "Saga of the Doombringer (In Progress)")
	public static NpcStringId ID_6702;
	
	@NpcString(id = 6703, message = "Saga of the Doombringer (Done)")
	public static NpcStringId ID_6703;
	
	@NpcString(id = 6801, message = "Saga of the Soul Hound")
	public static NpcStringId ID_6801;
	
	@NpcString(id = 6802, message = "Saga of the Soul Hound (In Progress)")
	public static NpcStringId ID_6802;
	
	@NpcString(id = 6803, message = "Saga of the Soul Hound (Done)")
	public static NpcStringId ID_6803;
	
	@NpcString(id = 6901, message = "Saga of the Trickster")
	public static NpcStringId ID_6901;
	
	@NpcString(id = 6902, message = "Saga of the Trickster (In Progress)")
	public static NpcStringId ID_6902;
	
	@NpcString(id = 6903, message = "Saga of the Trickster (Done)")
	public static NpcStringId ID_6903;
	
	@NpcString(id = 7001, message = "Saga of the Phoenix Knight")
	public static NpcStringId ID_7001;
	
	@NpcString(id = 7002, message = "Saga of the Phoenix Knight (In Progress)")
	public static NpcStringId ID_7002;
	
	@NpcString(id = 7003, message = "Saga of the Phoenix Knight (Done)")
	public static NpcStringId ID_7003;
	
	@NpcString(id = 7050, message = "I came here, %s, to prove my strength. I'll kill you!")
	public static NpcStringId ID_7050;
	
	@NpcString(id = 7051, message = "Ha! You failed! Are you ready to quit?")
	public static NpcStringId ID_7051;
	
	@NpcString(id = 7052, message = "I'm the strongest! I lost everything to win!")
	public static NpcStringId ID_7052;
	
	@NpcString(id = 7053, message = "%s! Are you doing this because they're Halisha's minions?")
	public static NpcStringId ID_7053;
	
	@NpcString(id = 7054, message = "My spirit is released from this shell. I'm getting close to Halisha...")
	public static NpcStringId ID_7054;
	
	@NpcString(id = 7055, message = "Mind your own business!")
	public static NpcStringId ID_7055;
	
	@NpcString(id = 7056, message = "This fight is a waste of time. Goodbye!")
	public static NpcStringId ID_7056;
	
	@NpcString(id = 7057, message = "Every cloud has a silver lining, don't you think?")
	public static NpcStringId ID_7057;
	
	@NpcString(id = 7058, message = "%s! Don't listen to this demon.")
	public static NpcStringId ID_7058;
	
	@NpcString(id = 7059, message = "%s! Hesitation betrays your heart. Fight!")
	public static NpcStringId ID_7059;
	
	@NpcString(id = 7060, message = "%s! Isn't protecting somebody worthy? Isn't that justice?")
	public static NpcStringId ID_7060;
	
	@NpcString(id = 7061, message = "I have urgent business! I gotta go.")
	public static NpcStringId ID_7061;
	
	@NpcString(id = 7062, message = "Are my efforts in vain? Is this the end?")
	public static NpcStringId ID_7062;
	
	@NpcString(id = 7063, message = "Goodbye, friend. I hope to see you again.")
	public static NpcStringId ID_7063;
	
	@NpcString(id = 7064, message = "Knights are always foolish!")
	public static NpcStringId ID_7064;
	
	@NpcString(id = 7065, message = "I'll show mercy on you for now.")
	public static NpcStringId ID_7065;
	
	@NpcString(id = 7066, message = "%s! Your justice is just hypocrisy if you give up on what you've promised to protect.")
	public static NpcStringId ID_7066;
	
	@NpcString(id = 7067, message = "%s...Don't think you've won! Your dark shadow will always follow you...hypocrite!")
	public static NpcStringId ID_7067;
	
	@NpcString(id = 7101, message = "Saga of Eva's Templar")
	public static NpcStringId ID_7101;
	
	@NpcString(id = 7102, message = "Saga of Eva's Templar (In Progress)")
	public static NpcStringId ID_7102;
	
	@NpcString(id = 7103, message = "Saga of Eva's Templar (Done)")
	public static NpcStringId ID_7103;
	
	@NpcString(id = 7150, message = "A temple knight guards the Mother Tree! %s! Has Human contact made you forget that?")
	public static NpcStringId ID_7150;
	
	@NpcString(id = 7151, message = "I must stop. Remember, the ones you're protecting will someday defeat the Elves.")
	public static NpcStringId ID_7151;
	
	@NpcString(id = 7152, message = "What! That will just strengthen the enemy!")
	public static NpcStringId ID_7152;
	
	@NpcString(id = 7153, message = "You dare to disturb the order of the shrine! Die, %s!")
	public static NpcStringId ID_7153;
	
	@NpcString(id = 7154, message = "My spirit is releasing from this shell. I'm getting close to Halisha...")
	public static NpcStringId ID_7154;
	
	@NpcString(id = 7155, message = "Mind your own business!")
	public static NpcStringId ID_7155;
	
	@NpcString(id = 7156, message = "This is a waste of time. Goodbye!")
	public static NpcStringId ID_7156;
	
	@NpcString(id = 7157, message = "I'm not like my brother Panacea. Ghost of the past, begone!")
	public static NpcStringId ID_7157;
	
	@NpcString(id = 7158, message = "The Elves no longer rule! Help me, %s!")
	public static NpcStringId ID_7158;
	
	@NpcString(id = 7159, message = "Don't give up, %s! He' a demon from the past!")
	public static NpcStringId ID_7159;
	
	@NpcString(id = 7160, message = "Be proud, %s. We protect this world together.")
	public static NpcStringId ID_7160;
	
	@NpcString(id = 7161, message = "I have to go. I've got some business to take care of.")
	public static NpcStringId ID_7161;
	
	@NpcString(id = 7162, message = "Ugh! Don't let him get away!")
	public static NpcStringId ID_7162;
	
	@NpcString(id = 7163, message = "Don't forget your pride. You're protecting the world!")
	public static NpcStringId ID_7163;
	
	@NpcString(id = 7164, message = "Ha, ha, ha!...")
	public static NpcStringId ID_7164;
	
	@NpcString(id = 7165, message = "Kuh, huh...")
	public static NpcStringId ID_7165;
	
	@NpcString(id = 7166, message = "Aah! Kuh...%s!...Kuh, huh...")
	public static NpcStringId ID_7166;
	
	@NpcString(id = 7167, message = "%s!...Re... mem... Ugh...Uh...")
	public static NpcStringId ID_7167;
	
	@NpcString(id = 7168, message = "Must...Retreat... Too...Strong. Defeat...by...retaining...and...Mo...Hacker, ....! Fight...Defeat...It...Fight...Defeat...It...")
	public static NpcStringId ID_7168;
	
	@NpcString(id = 7169, message = "...Goodness! %s you are still looking? %s ... Not just to whom the victory. Only personnel involved in the fighting are eligible to share in the victory.")
	public static NpcStringId ID_7169;
	
	@NpcString(id = 7170, message = "Why do you interfere others' battles? This is a waste of time.. Say goodbye...!...That is the enemy")
	public static NpcStringId ID_7170;
	
	@NpcString(id = 7201, message = "Saga of the Sword Muse")
	public static NpcStringId ID_7201;
	
	@NpcString(id = 7202, message = "Saga of the Sword Muse (In Progress)")
	public static NpcStringId ID_7202;
	
	@NpcString(id = 7203, message = "Saga of the Sword Muse (Done)")
	public static NpcStringId ID_7203;
	
	@NpcString(id = 7250, message = "%s, You'd better listen.")
	public static NpcStringId ID_7250;
	
	@NpcString(id = 7251, message = "Huh? It's curtain time! I won't get any money.")
	public static NpcStringId ID_7251;
	
	@NpcString(id = 7252, message = "Ugh...It can't be...!")
	public static NpcStringId ID_7252;
	
	@NpcString(id = 7253, message = "You dare to disturb the order of the shrine! Die, %s!")
	public static NpcStringId ID_7253;
	
	@NpcString(id = 7254, message = "My spirit is released from this shell. I'm getting close to Halisha...")
	public static NpcStringId ID_7254;
	
	@NpcString(id = 7255, message = "Mind your own business!")
	public static NpcStringId ID_7255;
	
	@NpcString(id = 7256, message = "This is a waste of time. Goodbye!")
	public static NpcStringId ID_7256;
	
	@NpcString(id = 7257, message = "You won't get away this time, Narcissus!")
	public static NpcStringId ID_7257;
	
	@NpcString(id = 7258, message = "%s! Help me!")
	public static NpcStringId ID_7258;
	
	@NpcString(id = 7259, message = "You must be aware of your audience when singing, %s!")
	public static NpcStringId ID_7259;
	
	@NpcString(id = 7260, message = "You must work harder to be victorious, %s.")
	public static NpcStringId ID_7260;
	
	@NpcString(id = 7261, message = "My song is over, I must go. Goodbye!")
	public static NpcStringId ID_7261;
	
	@NpcString(id = 7262, message = "How could I miss!")
	public static NpcStringId ID_7262;
	
	@NpcString(id = 7263, message = "Don't forget. Song comes with harmony.")
	public static NpcStringId ID_7263;
	
	@NpcString(id = 7264, message = "Sing. Everyone will listen.")
	public static NpcStringId ID_7264;
	
	@NpcString(id = 7265, message = "You don't deserve my blessing.")
	public static NpcStringId ID_7265;
	
	@NpcString(id = 7266, message = "Do you reject my blessing, %s?")
	public static NpcStringId ID_7266;
	
	@NpcString(id = 7267, message = "But why, %s. Everyone would praise you!")
	public static NpcStringId ID_7267;
	
	@NpcString(id = 7301, message = "Saga of the Duelist")
	public static NpcStringId ID_7301;
	
	@NpcString(id = 7302, message = "Saga of the Duelist (In Progress)")
	public static NpcStringId ID_7302;
	
	@NpcString(id = 7303, message = "Saga of the Duelist (Done)")
	public static NpcStringId ID_7303;
	
	@NpcString(id = 7350, message = "%s! Attack me? I'm immortal! I'm unrivaled!")
	public static NpcStringId ID_7350;
	
	@NpcString(id = 7351, message = "Ha! I'm immortal. This scar will soon heal. You'll die next time.")
	public static NpcStringId ID_7351;
	
	@NpcString(id = 7352, message = "Metellus! You promised me an immortal life! How could I, Swordmaster Iron, lose?")
	public static NpcStringId ID_7352;
	
	@NpcString(id = 7353, message = "You dare to disturb the order of the shrine! Die, %s!")
	public static NpcStringId ID_7353;
	
	@NpcString(id = 7354, message = "My spirit is released from this shell. I'm getting close to Halisha...")
	public static NpcStringId ID_7354;
	
	@NpcString(id = 7355, message = "Mind your own business!")
	public static NpcStringId ID_7355;
	
	@NpcString(id = 7356, message = "This is a waste of time... Goodbye!")
	public static NpcStringId ID_7356;
	
	@NpcString(id = 7357, message = "Fallen Angel? It's worth trying.")
	public static NpcStringId ID_7357;
	
	@NpcString(id = 7358, message = "Hey %s! Why don't you join? It's your best shot!")
	public static NpcStringId ID_7358;
	
	@NpcString(id = 7359, message = "Are you interested in immortal life, %s? It's too boring for me.")
	public static NpcStringId ID_7359;
	
	@NpcString(id = 7360, message = "Excellent, %s! Show me what you learned when your life was on the line!")
	public static NpcStringId ID_7360;
	
	@NpcString(id = 7361, message = "I have to go take a break.")
	public static NpcStringId ID_7361;
	
	@NpcString(id = 7362, message = "You missed?! What a fool you are!")
	public static NpcStringId ID_7362;
	
	@NpcString(id = 7363, message = "Fighting without risk, training without danger and growing without hardship will only lead to an inflated ego. Don't forget.")
	public static NpcStringId ID_7363;
	
	@NpcString(id = 7364, message = "Do you want an immortal life?")
	public static NpcStringId ID_7364;
	
	@NpcString(id = 7365, message = "Think it over. An immortal life and assured victory...")
	public static NpcStringId ID_7365;
	
	@NpcString(id = 7366, message = "That's good, %s! Do you want my blessing to improve your skills?")
	public static NpcStringId ID_7366;
	
	@NpcString(id = 7367, message = "%s! Why do you reject guaranteed victory?")
	public static NpcStringId ID_7367;
	
	@NpcString(id = 7401, message = "Saga of the Dreadnought")
	public static NpcStringId ID_7401;
	
	@NpcString(id = 7402, message = "Saga of the Dreadnought (In Progress)")
	public static NpcStringId ID_7402;
	
	@NpcString(id = 7403, message = "Saga of the Dreadnought (Done)")
	public static NpcStringId ID_7403;
	
	@NpcString(id = 7450, message = "In the name of gods, I punish you, %s! You can't rival us all, no matter how strong you think you are!")
	public static NpcStringId ID_7450;
	
	@NpcString(id = 7451, message = "I have to stop for now, but I'll defeat the power of the dragon yet!")
	public static NpcStringId ID_7451;
	
	@NpcString(id = 7452, message = "It is...The power that shouldn't live!")
	public static NpcStringId ID_7452;
	
	@NpcString(id = 7453, message = "You dare to disturb the order of the shrine! Die, %s!")
	public static NpcStringId ID_7453;
	
	@NpcString(id = 7454, message = "My spirit is released from this shell. I'm getting close to Halisha...")
	public static NpcStringId ID_7454;
	
	@NpcString(id = 7455, message = "Mind your own business!")
	public static NpcStringId ID_7455;
	
	@NpcString(id = 7456, message = "This is a waste of time. Goodbye!")
	public static NpcStringId ID_7456;
	
	@NpcString(id = 7457, message = "Isn't it unwise for an angel to interfere in Human affairs?")
	public static NpcStringId ID_7457;
	
	@NpcString(id = 7458, message = "This is tough! %s, would you help me?")
	public static NpcStringId ID_7458;
	
	@NpcString(id = 7459, message = "%s! He's keeping an eye on all those in succession to the blood of dragons, including you!")
	public static NpcStringId ID_7459;
	
	@NpcString(id = 7460, message = "Attack the rear, %s! If I'm killed, you're next!")
	public static NpcStringId ID_7460;
	
	@NpcString(id = 7461, message = "I can't stay any longer. There are too many eyes on us. Farewell!")
	public static NpcStringId ID_7461;
	
	@NpcString(id = 7462, message = "Get away? You're still alive. But...")
	public static NpcStringId ID_7462;
	
	@NpcString(id = 7463, message = "If we can't avoid this fight, we'll need great strength. It's the only way to peace.")
	public static NpcStringId ID_7463;
	
	@NpcString(id = 7464, message = "Warlord, you'll never learn the techniques of the dragon!")
	public static NpcStringId ID_7464;
	
	@NpcString(id = 7465, message = "Hey, why bother with this. Isn't it your mother's business?")
	public static NpcStringId ID_7465;
	
	@NpcString(id = 7466, message = "%s! Are you against your mother's wishes? You're not worthy of the secrets of Shilen's children!")
	public static NpcStringId ID_7466;
	
	@NpcString(id = 7467, message = "Excellent technique, %s. Unfortunately, you're the one destined for tragedy!")
	public static NpcStringId ID_7467;
	
	@NpcString(id = 7501, message = "Saga of the Titan")
	public static NpcStringId ID_7501;
	
	@NpcString(id = 7502, message = "Saga of the Titan (In Progress)")
	public static NpcStringId ID_7502;
	
	@NpcString(id = 7503, message = "Saga of the Titan (Done)")
	public static NpcStringId ID_7503;
	
	@NpcString(id = 7550, message = "%s! You may follow me, but an Orc is no match for my giant's strength!")
	public static NpcStringId ID_7550;
	
	@NpcString(id = 7551, message = "Kuh...My body fails..This is the end!")
	public static NpcStringId ID_7551;
	
	@NpcString(id = 7552, message = "How could I lose with the powers of a giant? Aargh!!!")
	public static NpcStringId ID_7552;
	
	@NpcString(id = 7553, message = "You dare to disturb the order of the shrine! Die, %s!")
	public static NpcStringId ID_7553;
	
	@NpcString(id = 7554, message = "My spirit is released from this shell. I'm getting close to Halisha...")
	public static NpcStringId ID_7554;
	
	@NpcString(id = 7555, message = "Mind your own business!")
	public static NpcStringId ID_7555;
	
	@NpcString(id = 7556, message = "This is a waste of time. Goodbye!")
	public static NpcStringId ID_7556;
	
	@NpcString(id = 7557, message = "That's the enemy.")
	public static NpcStringId ID_7557;
	
	@NpcString(id = 7558, message = "%s! Why are you staring?")
	public static NpcStringId ID_7558;
	
	@NpcString(id = 7559, message = "%s. Nothing risked, nothing gained. Only those who fight enjoy the spoils of victory.")
	public static NpcStringId ID_7559;
	
	@NpcString(id = 7560, message = "A sword isn't jewelry, %s. Don't you agree?")
	public static NpcStringId ID_7560;
	
	@NpcString(id = 7561, message = "With no fight, I have no reason to stay.")
	public static NpcStringId ID_7561;
	
	@NpcString(id = 7562, message = "Miss...")
	public static NpcStringId ID_7562;
	
	@NpcString(id = 7563, message = "Pick your battles wisely, or you'll regret it.")
	public static NpcStringId ID_7563;
	
	@NpcString(id = 7564, message = "What a fool to challenge the giant of the Oroka tribe!")
	public static NpcStringId ID_7564;
	
	@NpcString(id = 7565, message = "Running low on steam. I must withdraw.")
	public static NpcStringId ID_7565;
	
	@NpcString(id = 7566, message = "%s. You're the one who defeated Guardian Muhark!")
	public static NpcStringId ID_7566;
	
	@NpcString(id = 7567, message = "%s....! I must succeed...")
	public static NpcStringId ID_7567;
	
	@NpcString(id = 7601, message = "Saga of the Grand Khavatari")
	public static NpcStringId ID_7601;
	
	@NpcString(id = 7602, message = "Saga of the Grand Khavatari (In Progress)")
	public static NpcStringId ID_7602;
	
	@NpcString(id = 7603, message = "Saga of the Grand Khavatari (Done)")
	public static NpcStringId ID_7603;
	
	@NpcString(id = 7650, message = "%s... Would you fight Uruz, who has reached the power of Azira?")
	public static NpcStringId ID_7650;
	
	@NpcString(id = 7651, message = "I can't handle the power of Azira yet. First...")
	public static NpcStringId ID_7651;
	
	@NpcString(id = 7652, message = "This can't be happening! I have the power of Azira! How could I fall so easily?")
	public static NpcStringId ID_7652;
	
	@NpcString(id = 7653, message = "You dare to disturb the order of the shrine! Die, %s!")
	public static NpcStringId ID_7653;
	
	@NpcString(id = 7654, message = "My spirit is released from this shell. I'm getting close to Halisha...")
	public static NpcStringId ID_7654;
	
	@NpcString(id = 7655, message = "Mind your own business!")
	public static NpcStringId ID_7655;
	
	@NpcString(id = 7656, message = "This is a waste of time. Goodbye!")
	public static NpcStringId ID_7656;
	
	@NpcString(id = 7657, message = "Azira, born from the Evil Flame, I'll kill you with my bare hands!")
	public static NpcStringId ID_7657;
	
	@NpcString(id = 7658, message = "%s! In the name of Khavatari Hubai, strike this evil with your fists!")
	public static NpcStringId ID_7658;
	
	@NpcString(id = 7659, message = "%s! Attack from both sides! Hit hard!")
	public static NpcStringId ID_7659;
	
	@NpcString(id = 7660, message = "%s! Strike with all your heart. It must work.")
	public static NpcStringId ID_7660;
	
	@NpcString(id = 7661, message = "Damn! It's time to go. Until next time.")
	public static NpcStringId ID_7661;
	
	@NpcString(id = 7662, message = "Evil spirit of flame! I won't give up!")
	public static NpcStringId ID_7662;
	
	@NpcString(id = 7663, message = "My fist works even on the evil spirit. Don't forget!")
	public static NpcStringId ID_7663;
	
	@NpcString(id = 7664, message = "Foolish Khavatari...Do you think your power will work on me? I'm the source of your martial art!")
	public static NpcStringId ID_7664;
	
	@NpcString(id = 7665, message = "No more games...")
	public static NpcStringId ID_7665;
	
	@NpcString(id = 7666, message = "%s...Are you next after Khavatari? Do you know who I am?")
	public static NpcStringId ID_7666;
	
	@NpcString(id = 7667, message = "%s...Kashu. Not a bad attack. I can't hold on much longer.")
	public static NpcStringId ID_7667;
	
	@NpcString(id = 7701, message = "Saga of the Dominator")
	public static NpcStringId ID_7701;
	
	@NpcString(id = 7702, message = "Saga of the Dominator (In Progress)")
	public static NpcStringId ID_7702;
	
	@NpcString(id = 7703, message = "Saga of the Dominator (Done)")
	public static NpcStringId ID_7703;
	
	@NpcString(id = 7750, message = "%s, Akkan, you can't be my rival! I'll kill everything! I'll be the king!")
	public static NpcStringId ID_7750;
	
	@NpcString(id = 7751, message = "Ha! I'll show mercy on you this time. I know well of your technique!")
	public static NpcStringId ID_7751;
	
	@NpcString(id = 7752, message = "I have in me the blood of a king! How could I lose?!")
	public static NpcStringId ID_7752;
	
	@NpcString(id = 7753, message = "You dare to disturb the order of the shrine! Die, %s!")
	public static NpcStringId ID_7753;
	
	@NpcString(id = 7754, message = "My spirit is released from this shell. I'm getting close to Halisha...")
	public static NpcStringId ID_7754;
	
	@NpcString(id = 7755, message = "Mind your own business!")
	public static NpcStringId ID_7755;
	
	@NpcString(id = 7756, message = "This is a waste of time. Goodbye!")
	public static NpcStringId ID_7756;
	
	@NpcString(id = 7757, message = "Are you....tyrant!")
	public static NpcStringId ID_7757;
	
	@NpcString(id = 7758, message = "You're not a king! You're just a tyrant! %s, we must fight together!")
	public static NpcStringId ID_7758;
	
	@NpcString(id = 7759, message = "Such rule is ruining the country! %s, I can't bear this tyranny any longer!")
	public static NpcStringId ID_7759;
	
	@NpcString(id = 7760, message = "%s, leaders must always resist tyranny!")
	public static NpcStringId ID_7760;
	
	@NpcString(id = 7761, message = "I stayed too long. I'll be punished for being away so long.")
	public static NpcStringId ID_7761;
	
	@NpcString(id = 7762, message = "He got away, Dammit! We must catch this dark soul!")
	public static NpcStringId ID_7762;
	
	@NpcString(id = 7763, message = "What is a king? What must one do to be a good king? Think it over.")
	public static NpcStringId ID_7763;
	
	@NpcString(id = 7764, message = "Kneel down before me! Foolish people!")
	public static NpcStringId ID_7764;
	
	@NpcString(id = 7765, message = "It's time for the king to leave! Bow your head and say goodbye!")
	public static NpcStringId ID_7765;
	
	@NpcString(id = 7766, message = "%s! You dare to fight me? A king's power must be great to rule!")
	public static NpcStringId ID_7766;
	
	@NpcString(id = 7767, message = "You would fight the king, %s? Traitor!")
	public static NpcStringId ID_7767;
	
	@NpcString(id = 7801, message = "Saga of the Doomcryer")
	public static NpcStringId ID_7801;
	
	@NpcString(id = 7802, message = "Saga of the Doomcryer (In Progress)")
	public static NpcStringId ID_7802;
	
	@NpcString(id = 7803, message = "Saga of the Doomcryer (Done)")
	public static NpcStringId ID_7803;
	
	@NpcString(id = 7850, message = "Tejakar Sharuhi! %s, I'll show you the power of Sharuhi Mouth Mudaha!")
	public static NpcStringId ID_7850;
	
	@NpcString(id = 7851, message = "Aaargh! My soul won't keep quiet. Now I must take my leave.")
	public static NpcStringId ID_7851;
	
	@NpcString(id = 7852, message = "No, Sharuhi. You're soul is mine!")
	public static NpcStringId ID_7852;
	
	@NpcString(id = 7853, message = "You dare to disturb the order of the shrine! Die, %s!")
	public static NpcStringId ID_7853;
	
	@NpcString(id = 7854, message = "My spirit is released from this shell. I'm getting close to Halisha...")
	public static NpcStringId ID_7854;
	
	@NpcString(id = 7855, message = "Mind your own business!")
	public static NpcStringId ID_7855;
	
	@NpcString(id = 7856, message = "This is a waste of time. Goodbye!")
	public static NpcStringId ID_7856;
	
	@NpcString(id = 7857, message = "Tejakar Oroca! Tejakar Duda-Mara!")
	public static NpcStringId ID_7857;
	
	@NpcString(id = 7858, message = "%s, we must fight this soul together to prevent an everlasting winter!")
	public static NpcStringId ID_7858;
	
	@NpcString(id = 7859, message = "%s! The soul responds to you. May your attack quiet him!")
	public static NpcStringId ID_7859;
	
	@NpcString(id = 7860, message = "%s! Calm Sharuhi! He doesn't listen to me anymore.")
	public static NpcStringId ID_7860;
	
	@NpcString(id = 7861, message = "It's time to go! May the eternal flame bless you!")
	public static NpcStringId ID_7861;
	
	@NpcString(id = 7862, message = "He left...That's too bad..Too bad...")
	public static NpcStringId ID_7862;
	
	@NpcString(id = 7863, message = "Don't forget your strong will now!")
	public static NpcStringId ID_7863;
	
	@NpcString(id = 7864, message = "Ha! Nobody will rule over me anymore!")
	public static NpcStringId ID_7864;
	
	@NpcString(id = 7865, message = "Freedom... freedom... freedom!")
	public static NpcStringId ID_7865;
	
	@NpcString(id = 7866, message = "%s, You released me, but you also want to catch me. Ha!")
	public static NpcStringId ID_7866;
	
	@NpcString(id = 7867, message = "...%s...Me?....All right...I'll help you.")
	public static NpcStringId ID_7867;
	
	@NpcString(id = 7901, message = "Saga of the Adventurer")
	public static NpcStringId ID_7901;
	
	@NpcString(id = 7902, message = "Saga of the Adventurer (In Progress)")
	public static NpcStringId ID_7902;
	
	@NpcString(id = 7903, message = "Saga of the Adventurer (Done)")
	public static NpcStringId ID_7903;
	
	@NpcString(id = 7950, message = "Get out of here! This place is forbidden by god.")
	public static NpcStringId ID_7950;
	
	@NpcString(id = 7951, message = "Einhasad is calling me.")
	public static NpcStringId ID_7951;
	
	@NpcString(id = 7952, message = "You killed me! Aren't you afraid of god's curse?")
	public static NpcStringId ID_7952;
	
	@NpcString(id = 7953, message = "You bother my minions, %s. Do you want to die?")
	public static NpcStringId ID_7953;
	
	@NpcString(id = 7954, message = "What the hell... I lost.")
	public static NpcStringId ID_7954;
	
	@NpcString(id = 7955, message = "Who are you? Why are you interfering in our business?")
	public static NpcStringId ID_7955;
	
	@NpcString(id = 7956, message = "You're strong. I'll get you next time!")
	public static NpcStringId ID_7956;
	
	@NpcString(id = 7957, message = "We meet again. I'll have you this time!")
	public static NpcStringId ID_7957;
	
	@NpcString(id = 7958, message = "A worthy opponent. %s. Help me!")
	public static NpcStringId ID_7958;
	
	@NpcString(id = 7959, message = "%s! Hurry before he gets away!")
	public static NpcStringId ID_7959;
	
	@NpcString(id = 7960, message = "I'll kill you!")
	public static NpcStringId ID_7960;
	
	@NpcString(id = 7961, message = "Why don't you fight me someday?")
	public static NpcStringId ID_7961;
	
	@NpcString(id = 7962, message = "I missed again. Dammit!")
	public static NpcStringId ID_7962;
	
	@NpcString(id = 7963, message = "I'm sure we'll meet again someday.")
	public static NpcStringId ID_7963;
	
	@NpcString(id = 7964, message = "Curse those who defy the gods!")
	public static NpcStringId ID_7964;
	
	@NpcString(id = 7965, message = "Einhasad is calling me.")
	public static NpcStringId ID_7965;
	
	@NpcString(id = 7966, message = "You would fight me, a messenger of the gods?")
	public static NpcStringId ID_7966;
	
	@NpcString(id = 7967, message = "%s! I won't forget you.")
	public static NpcStringId ID_7967;
	
	@NpcString(id = 8001, message = "Saga of the Wind Rider")
	public static NpcStringId ID_8001;
	
	@NpcString(id = 8002, message = "Saga of the Wind Rider (In Progress)")
	public static NpcStringId ID_8002;
	
	@NpcString(id = 8003, message = "Saga of the Wind Rider (Done)")
	public static NpcStringId ID_8003;
	
	@NpcString(id = 8050, message = "%s! How could you desecrate a holy place?")
	public static NpcStringId ID_8050;
	
	@NpcString(id = 8051, message = "Leave before you are severely punished!")
	public static NpcStringId ID_8051;
	
	@NpcString(id = 8052, message = "Einhasad, don't give up on me!")
	public static NpcStringId ID_8052;
	
	@NpcString(id = 8053, message = "%s, so you're the one who's looking for me?")
	public static NpcStringId ID_8053;
	
	@NpcString(id = 8054, message = "A mere mortal has defeated me!")
	public static NpcStringId ID_8054;
	
	@NpcString(id = 8055, message = "How cowardly to intrude in other people's business.")
	public static NpcStringId ID_8055;
	
	@NpcString(id = 8056, message = "Time is up.")
	public static NpcStringId ID_8056;
	
	@NpcString(id = 8057, message = "I'll kill you with my sword!")
	public static NpcStringId ID_8057;
	
	@NpcString(id = 8058, message = "Help me!")
	public static NpcStringId ID_8058;
	
	@NpcString(id = 8059, message = "Don't miss!")
	public static NpcStringId ID_8059;
	
	@NpcString(id = 8060, message = "Keep pushing!")
	public static NpcStringId ID_8060;
	
	@NpcString(id = 8061, message = "I'll get him. You'll have your revenge.")
	public static NpcStringId ID_8061;
	
	@NpcString(id = 8062, message = "I missed him again. I'll kill you.")
	public static NpcStringId ID_8062;
	
	@NpcString(id = 8063, message = "I must follow him.")
	public static NpcStringId ID_8063;
	
	@NpcString(id = 8064, message = "Curse those who defy the gods!")
	public static NpcStringId ID_8064;
	
	@NpcString(id = 8065, message = "Einhasad is calling me.")
	public static NpcStringId ID_8065;
	
	@NpcString(id = 8066, message = "You would fight me, a messenger of the gods?")
	public static NpcStringId ID_8066;
	
	@NpcString(id = 8067, message = "%s! I won't forget you.")
	public static NpcStringId ID_8067;
	
	@NpcString(id = 8101, message = "Saga of the Ghost Hunter")
	public static NpcStringId ID_8101;
	
	@NpcString(id = 8102, message = "Saga of the Ghost Hunter (In Progress)")
	public static NpcStringId ID_8102;
	
	@NpcString(id = 8103, message = "Saga of the Ghost Hunter (Done)")
	public static NpcStringId ID_8103;
	
	@NpcString(id = 8150, message = "%s, you should leave if you fear god's wrath!")
	public static NpcStringId ID_8150;
	
	@NpcString(id = 8151, message = "What's going on?")
	public static NpcStringId ID_8151;
	
	@NpcString(id = 8152, message = "I'll see you again!")
	public static NpcStringId ID_8152;
	
	@NpcString(id = 8153, message = "Who are you? Why are you bothering my minions?")
	public static NpcStringId ID_8153;
	
	@NpcString(id = 8154, message = "No way!!!")
	public static NpcStringId ID_8154;
	
	@NpcString(id = 8155, message = "Why are you sticking your nose in our business?")
	public static NpcStringId ID_8155;
	
	@NpcString(id = 8156, message = "Who are you? How can a creature from the netherworld be so powerful?")
	public static NpcStringId ID_8156;
	
	@NpcString(id = 8157, message = "Is this the end?")
	public static NpcStringId ID_8157;
	
	@NpcString(id = 8158, message = "Show me what you're made of. Kill him!")
	public static NpcStringId ID_8158;
	
	@NpcString(id = 8159, message = "You think you can get him with that?")
	public static NpcStringId ID_8159;
	
	@NpcString(id = 8160, message = "Pull yourself together! He's trying to get away.")
	public static NpcStringId ID_8160;
	
	@NpcString(id = 8161, message = "Tell the Black Cat that I got his paid back.")
	public static NpcStringId ID_8161;
	
	@NpcString(id = 8162, message = "Black Cat, he'll blame me.")
	public static NpcStringId ID_8162;
	
	@NpcString(id = 8163, message = "I gotta' go now.")
	public static NpcStringId ID_8163;
	
	@NpcString(id = 8164, message = "Curse those who defy the gods!")
	public static NpcStringId ID_8164;
	
	@NpcString(id = 8165, message = "Einhasad is calling me.")
	public static NpcStringId ID_8165;
	
	@NpcString(id = 8166, message = "I'll kill you in the name of god.")
	public static NpcStringId ID_8166;
	
	@NpcString(id = 8167, message = "%s! See you later.")
	public static NpcStringId ID_8167;
	
	@NpcString(id = 8201, message = "Saga of the Sagittarius")
	public static NpcStringId ID_8201;
	
	@NpcString(id = 8202, message = "Saga of the Sagittarius (In Progress)")
	public static NpcStringId ID_8202;
	
	@NpcString(id = 8203, message = "Saga of the Sagittarius (Done)")
	public static NpcStringId ID_8203;
	
	@NpcString(id = 8250, message = "%s! How could you desecrate a holy place?")
	public static NpcStringId ID_8250;
	
	@NpcString(id = 8251, message = "Get out before you're punished!")
	public static NpcStringId ID_8251;
	
	@NpcString(id = 8252, message = "Einhasad, please don't give up on me!")
	public static NpcStringId ID_8252;
	
	@NpcString(id = 8253, message = "%s, are you looking for me?")
	public static NpcStringId ID_8253;
	
	@NpcString(id = 8254, message = "A mere mortal is killing me!")
	public static NpcStringId ID_8254;
	
	@NpcString(id = 8255, message = "Mind your own business!")
	public static NpcStringId ID_8255;
	
	@NpcString(id = 8256, message = "Mortal, don't you recognize my greatness?")
	public static NpcStringId ID_8256;
	
	@NpcString(id = 8257, message = "I'll get you this time.")
	public static NpcStringId ID_8257;
	
	@NpcString(id = 8258, message = "I'll never forget the taste of his steel, %s! Let's fight him together!")
	public static NpcStringId ID_8258;
	
	@NpcString(id = 8259, message = "%s! Pull yourself together. We'll miss him!")
	public static NpcStringId ID_8259;
	
	@NpcString(id = 8260, message = "%s! He's trying to get away.")
	public static NpcStringId ID_8260;
	
	@NpcString(id = 8261, message = "I missed again! Next time...")
	public static NpcStringId ID_8261;
	
	@NpcString(id = 8262, message = "Dammit! Failed again!")
	public static NpcStringId ID_8262;
	
	@NpcString(id = 8263, message = "I gotta' go now.")
	public static NpcStringId ID_8263;
	
	@NpcString(id = 8264, message = "Curse those who defy the gods!")
	public static NpcStringId ID_8264;
	
	@NpcString(id = 8265, message = "Einhasad is calling me.")
	public static NpcStringId ID_8265;
	
	@NpcString(id = 8266, message = "You would fight me, a messenger of the gods?")
	public static NpcStringId ID_8266;
	
	@NpcString(id = 8267, message = "%s! I won't forget you.")
	public static NpcStringId ID_8267;
	
	@NpcString(id = 8301, message = "Saga of the Moonlight Sentinel")
	public static NpcStringId ID_8301;
	
	@NpcString(id = 8302, message = "Saga of the Moonlight Sentinel (In Progress)")
	public static NpcStringId ID_8302;
	
	@NpcString(id = 8303, message = "Saga of the Moonlight Sentinel (Done)")
	public static NpcStringId ID_8303;
	
	@NpcString(id = 8350, message = "%s! How could you desecrate a holy place?")
	public static NpcStringId ID_8350;
	
	@NpcString(id = 8351, message = "Get out before you're punished!")
	public static NpcStringId ID_8351;
	
	@NpcString(id = 8352, message = "Einhasad, don't give up on me!")
	public static NpcStringId ID_8352;
	
	@NpcString(id = 8353, message = "You are the one who's looking for me, %s?")
	public static NpcStringId ID_8353;
	
	@NpcString(id = 8354, message = "A mere mortal has killed me!")
	public static NpcStringId ID_8354;
	
	@NpcString(id = 8355, message = "Who are you? This is none of your business!")
	public static NpcStringId ID_8355;
	
	@NpcString(id = 8356, message = "Mortal, don't you recognize my greatness?")
	public static NpcStringId ID_8356;
	
	@NpcString(id = 8357, message = "I'll get you this time.")
	public static NpcStringId ID_8357;
	
	@NpcString(id = 8358, message = "I'll never forget the taste of his steel, %s! Let's fight him together!")
	public static NpcStringId ID_8358;
	
	@NpcString(id = 8359, message = "%s! Pull yourself together.")
	public static NpcStringId ID_8359;
	
	@NpcString(id = 8360, message = "%s! He'll get away!")
	public static NpcStringId ID_8360;
	
	@NpcString(id = 8361, message = "I missed again! Next time...")
	public static NpcStringId ID_8361;
	
	@NpcString(id = 8362, message = "Dammit! Failed again!")
	public static NpcStringId ID_8362;
	
	@NpcString(id = 8363, message = "I gotta' go now.")
	public static NpcStringId ID_8363;
	
	@NpcString(id = 8364, message = "Curse those who defy the gods!")
	public static NpcStringId ID_8364;
	
	@NpcString(id = 8365, message = "Einhasad is calling me.")
	public static NpcStringId ID_8365;
	
	@NpcString(id = 8366, message = "You would fight me, a messenger of the gods?")
	public static NpcStringId ID_8366;
	
	@NpcString(id = 8367, message = "%s! I won't forget you.")
	public static NpcStringId ID_8367;
	
	@NpcString(id = 8401, message = "Saga of the Ghost Sentinel")
	public static NpcStringId ID_8401;
	
	@NpcString(id = 8402, message = "Saga of the Ghost Sentinel (In Progress)")
	public static NpcStringId ID_8402;
	
	@NpcString(id = 8403, message = "Saga of the Ghost Sentinel (Done)")
	public static NpcStringId ID_8403;
	
	@NpcString(id = 8450, message = "%s! How could you desecrate a holy place?")
	public static NpcStringId ID_8450;
	
	@NpcString(id = 8451, message = "Get out before you're punished!")
	public static NpcStringId ID_8451;
	
	@NpcString(id = 8452, message = "Einhasad, please don't forsake me!")
	public static NpcStringId ID_8452;
	
	@NpcString(id = 8453, message = "Looking for me, %s?")
	public static NpcStringId ID_8453;
	
	@NpcString(id = 8454, message = "A mere mortal is killing me!")
	public static NpcStringId ID_8454;
	
	@NpcString(id = 8455, message = "Who are you? This is none of your business!")
	public static NpcStringId ID_8455;
	
	@NpcString(id = 8456, message = "Mortal! Don't you recognize my greatness?")
	public static NpcStringId ID_8456;
	
	@NpcString(id = 8457, message = "I'll get you this time.")
	public static NpcStringId ID_8457;
	
	@NpcString(id = 8458, message = "I'll never forget the taste of his steel, %s! Let's fight him together!")
	public static NpcStringId ID_8458;
	
	@NpcString(id = 8459, message = "%s! Pull yourself together!")
	public static NpcStringId ID_8459;
	
	@NpcString(id = 8460, message = "%s! He'll get away!")
	public static NpcStringId ID_8460;
	
	@NpcString(id = 8461, message = "I missed again! Next time...")
	public static NpcStringId ID_8461;
	
	@NpcString(id = 8462, message = "Dammit! Failed again!")
	public static NpcStringId ID_8462;
	
	@NpcString(id = 8463, message = "I gotta' go now.")
	public static NpcStringId ID_8463;
	
	@NpcString(id = 8464, message = "Curse those who defy the gods!")
	public static NpcStringId ID_8464;
	
	@NpcString(id = 8465, message = "Einhasad is calling me.")
	public static NpcStringId ID_8465;
	
	@NpcString(id = 8466, message = "You would fight me, a messenger of the gods?")
	public static NpcStringId ID_8466;
	
	@NpcString(id = 8467, message = "%s! I won't forget you.")
	public static NpcStringId ID_8467;
	
	@NpcString(id = 8501, message = "Saga of the Cardinal")
	public static NpcStringId ID_8501;
	
	@NpcString(id = 8502, message = "Saga of the Cardinal (In Progress)")
	public static NpcStringId ID_8502;
	
	@NpcString(id = 8503, message = "Saga of the Cardinal (Done)")
	public static NpcStringId ID_8503;
	
	@NpcString(id = 8550, message = "%s! Bishop, how foolish to go against the will of god!")
	public static NpcStringId ID_8550;
	
	@NpcString(id = 8551, message = "Your faith is stronger than I thought. I'll pay you back next time.")
	public static NpcStringId ID_8551;
	
	@NpcString(id = 8552, message = "Tanakia! Forgive me. I couldn't fulfill your dream!")
	public static NpcStringId ID_8552;
	
	@NpcString(id = 8553, message = "%s, you are the won who's been bothering my minions?")
	public static NpcStringId ID_8553;
	
	@NpcString(id = 8554, message = "Damn! You've beaten me.")
	public static NpcStringId ID_8554;
	
	@NpcString(id = 8555, message = "Who are you? This isn't your business, coward.")
	public static NpcStringId ID_8555;
	
	@NpcString(id = 8556, message = "How weak. I'll forgive you this time because you made me laugh.")
	public static NpcStringId ID_8556;
	
	@NpcString(id = 8557, message = "You are stronger than I thought, but I'm no weakling!")
	public static NpcStringId ID_8557;
	
	@NpcString(id = 8558, message = "He's got a tough shell. %s! Let's fight together and crack his skull!")
	public static NpcStringId ID_8558;
	
	@NpcString(id = 8559, message = "%s! Pull yourself together.")
	public static NpcStringId ID_8559;
	
	@NpcString(id = 8560, message = "%s! We won't beat him unless we give it our all. Come on!")
	public static NpcStringId ID_8560;
	
	@NpcString(id = 8561, message = "I'll follow him.")
	public static NpcStringId ID_8561;
	
	@NpcString(id = 8562, message = "I missed again! He's hard to follow.")
	public static NpcStringId ID_8562;
	
	@NpcString(id = 8563, message = "We'll see what the future brings.")
	public static NpcStringId ID_8563;
	
	@NpcString(id = 8564, message = "For Shilen!")
	public static NpcStringId ID_8564;
	
	@NpcString(id = 8565, message = "I'll be back. I'll deal with you then.")
	public static NpcStringId ID_8565;
	
	@NpcString(id = 8566, message = "%s! Are you going to fight me?")
	public static NpcStringId ID_8566;
	
	@NpcString(id = 8567, message = "%s! I'll pay you back. I won't forget you.")
	public static NpcStringId ID_8567;
	
	@NpcString(id = 8601, message = "Saga of the Hierophant")
	public static NpcStringId ID_8601;
	
	@NpcString(id = 8602, message = "Saga of the Hierophant (In Progress)")
	public static NpcStringId ID_8602;
	
	@NpcString(id = 8603, message = "Saga of the Hierophant (Done)")
	public static NpcStringId ID_8603;
	
	@NpcString(id = 8650, message = "%s! Prophet, how foolish to go against the will of god!")
	public static NpcStringId ID_8650;
	
	@NpcString(id = 8651, message = "Your faith is stronger than I thought. I'll deal with you next time.")
	public static NpcStringId ID_8651;
	
	@NpcString(id = 8652, message = "Tanakia! Forgive me. I couldn't fulfill your dream!")
	public static NpcStringId ID_8652;
	
	@NpcString(id = 8653, message = "Are you the one who's been bothering my minions, %s?")
	public static NpcStringId ID_8653;
	
	@NpcString(id = 8654, message = "Damn! I can't believe I've been beaten by you!")
	public static NpcStringId ID_8654;
	
	@NpcString(id = 8655, message = "Who are you? This is none of your business, coward.")
	public static NpcStringId ID_8655;
	
	@NpcString(id = 8656, message = "How weak. I'll forgive you this time because you made me laugh.")
	public static NpcStringId ID_8656;
	
	@NpcString(id = 8657, message = "I'll destroy the darkness surrounding the world with the power of light!")
	public static NpcStringId ID_8657;
	
	@NpcString(id = 8658, message = "%s! Fight the Fallen Angel with me. Show the true power of light!")
	public static NpcStringId ID_8658;
	
	@NpcString(id = 8659, message = "%s! Go! We must stop fighting here.")
	public static NpcStringId ID_8659;
	
	@NpcString(id = 8660, message = "We mustn't lose, %s! Pull yourself together!")
	public static NpcStringId ID_8660;
	
	@NpcString(id = 8661, message = "We'll meet again if fate wills it.")
	public static NpcStringId ID_8661;
	
	@NpcString(id = 8662, message = "I'll follow the cowardly devil.")
	public static NpcStringId ID_8662;
	
	@NpcString(id = 8663, message = "We'll meet again if fate wills it.")
	public static NpcStringId ID_8663;
	
	@NpcString(id = 8664, message = "For Shilen!")
	public static NpcStringId ID_8664;
	
	@NpcString(id = 8665, message = "I'll be back. I'll deal with you then.")
	public static NpcStringId ID_8665;
	
	@NpcString(id = 8666, message = "%s! Are you going to fight me?")
	public static NpcStringId ID_8666;
	
	@NpcString(id = 8667, message = "%s! I'll pay you back. I won't forget you.")
	public static NpcStringId ID_8667;
	
	@NpcString(id = 8701, message = "Saga of Eva's Saint")
	public static NpcStringId ID_8701;
	
	@NpcString(id = 8702, message = "Saga of Eva's Saint (In Progress)")
	public static NpcStringId ID_8702;
	
	@NpcString(id = 8703, message = "Saga of Eva's Saint (Done)")
	public static NpcStringId ID_8703;
	
	@NpcString(id = 8750, message = "%s! Elder, it's foolish of you to go against the will of the gods.")
	public static NpcStringId ID_8750;
	
	@NpcString(id = 8751, message = "Your faith is stronger than I thought. I'll pay you back next time.")
	public static NpcStringId ID_8751;
	
	@NpcString(id = 8752, message = "Tanakia! Forgive me. I couldn't fulfill your dream!")
	public static NpcStringId ID_8752;
	
	@NpcString(id = 8753, message = "Are you the one who's been bothering my minions, %s?")
	public static NpcStringId ID_8753;
	
	@NpcString(id = 8754, message = "Damn! I can't believe I've been beaten by you.")
	public static NpcStringId ID_8754;
	
	@NpcString(id = 8755, message = "Who are you? This is none of your business, coward.")
	public static NpcStringId ID_8755;
	
	@NpcString(id = 8756, message = "How weak. I'll forgive you this time because you made me laugh.")
	public static NpcStringId ID_8756;
	
	@NpcString(id = 8757, message = "You're stronger than I thought, but I'm no weakling either!")
	public static NpcStringId ID_8757;
	
	@NpcString(id = 8758, message = "He's got a tough shell. %s! Let's fight together and crack his skull!")
	public static NpcStringId ID_8758;
	
	@NpcString(id = 8759, message = "%s! Pull yourself together.")
	public static NpcStringId ID_8759;
	
	@NpcString(id = 8760, message = "%s! We'll never win unless we give it our all. Come on!")
	public static NpcStringId ID_8760;
	
	@NpcString(id = 8761, message = "I'll follow him.")
	public static NpcStringId ID_8761;
	
	@NpcString(id = 8762, message = "I missed again! He's hard to follow.")
	public static NpcStringId ID_8762;
	
	@NpcString(id = 8763, message = "We'll see what the future brings.")
	public static NpcStringId ID_8763;
	
	@NpcString(id = 8764, message = "For Shilen!")
	public static NpcStringId ID_8764;
	
	@NpcString(id = 8765, message = "I'll be back. I'll deal with you then.")
	public static NpcStringId ID_8765;
	
	@NpcString(id = 8766, message = "%s! Are you going to fight me?")
	public static NpcStringId ID_8766;
	
	@NpcString(id = 8767, message = "%s! I'll pay you back. I won't forget you.")
	public static NpcStringId ID_8767;
	
	@NpcString(id = 8801, message = "Saga of the Archmage")
	public static NpcStringId ID_8801;
	
	@NpcString(id = 8802, message = "Saga of the Archmage (In Progress)")
	public static NpcStringId ID_8802;
	
	@NpcString(id = 8803, message = "Saga of the Archmage (Done)")
	public static NpcStringId ID_8803;
	
	@NpcString(id = 8850, message = "Are you %s? Oh! I have the Resonance Amulet!")
	public static NpcStringId ID_8850;
	
	@NpcString(id = 8851, message = "You're feistier than I thought! I'll quit here for today.")
	public static NpcStringId ID_8851;
	
	@NpcString(id = 8852, message = "Aaargh! I can't believe I lost...")
	public static NpcStringId ID_8852;
	
	@NpcString(id = 8853, message = "Are you the one who's been bothering my minions, %s?")
	public static NpcStringId ID_8853;
	
	@NpcString(id = 8854, message = "Yikes! You're tough!")
	public static NpcStringId ID_8854;
	
	@NpcString(id = 8855, message = "Why do you interfere in other people's business...")
	public static NpcStringId ID_8855;
	
	@NpcString(id = 8856, message = "I'll stop here for today.")
	public static NpcStringId ID_8856;
	
	@NpcString(id = 8857, message = "I won't miss you this time!")
	public static NpcStringId ID_8857;
	
	@NpcString(id = 8858, message = "Dammit! This is too hard by myself... %s! Give me a hand!")
	public static NpcStringId ID_8858;
	
	@NpcString(id = 8859, message = "%s! Hurry up, we'll miss him.")
	public static NpcStringId ID_8859;
	
	@NpcString(id = 8860, message = "%s! Come on. Hurry up!")
	public static NpcStringId ID_8860;
	
	@NpcString(id = 8861, message = "I gotta' go follow him.")
	public static NpcStringId ID_8861;
	
	@NpcString(id = 8862, message = "Hey, quit running! Stop!")
	public static NpcStringId ID_8862;
	
	@NpcString(id = 8863, message = "See you next time~")
	public static NpcStringId ID_8863;
	
	@NpcString(id = 8864, message = "What? Think you can get in my way?")
	public static NpcStringId ID_8864;
	
	@NpcString(id = 8865, message = "You are so weak. I gotta' go now!")
	public static NpcStringId ID_8865;
	
	@NpcString(id = 8866, message = "%s! Good. I'll help you.")
	public static NpcStringId ID_8866;
	
	@NpcString(id = 8867, message = "%s, you're stronger than I thought. See you next time.")
	public static NpcStringId ID_8867;
	
	@NpcString(id = 8901, message = "Saga of the Mystic Muse")
	public static NpcStringId ID_8901;
	
	@NpcString(id = 8902, message = "Saga of the Mystic Muse (In Progress)")
	public static NpcStringId ID_8902;
	
	@NpcString(id = 8903, message = "Saga of the Mystic Muse (Done)")
	public static NpcStringId ID_8903;
	
	@NpcString(id = 8950, message = "Are you %s? Oh! I have the Resonance Amulet!")
	public static NpcStringId ID_8950;
	
	@NpcString(id = 8951, message = "You're feistier than I thought! I'll stop here today.")
	public static NpcStringId ID_8951;
	
	@NpcString(id = 8952, message = "Aargh! I can't believe I lost...")
	public static NpcStringId ID_8952;
	
	@NpcString(id = 8953, message = "Are you the one who's been bothering my minions, %s?")
	public static NpcStringId ID_8953;
	
	@NpcString(id = 8954, message = "Yikes! You're tough!")
	public static NpcStringId ID_8954;
	
	@NpcString(id = 8955, message = "Mind your own business!")
	public static NpcStringId ID_8955;
	
	@NpcString(id = 8956, message = "I'll stop here today.")
	public static NpcStringId ID_8956;
	
	@NpcString(id = 8957, message = "I'll get you this time!")
	public static NpcStringId ID_8957;
	
	@NpcString(id = 8958, message = "Damn! It's too much by myself...%s! Give me a hand!")
	public static NpcStringId ID_8958;
	
	@NpcString(id = 8959, message = "%s! Hurry, we'll miss him.")
	public static NpcStringId ID_8959;
	
	@NpcString(id = 8960, message = "%s! Hurry, please!")
	public static NpcStringId ID_8960;
	
	@NpcString(id = 8961, message = "I gotta' go follow him now.")
	public static NpcStringId ID_8961;
	
	@NpcString(id = 8962, message = "Are you running away? Stop!")
	public static NpcStringId ID_8962;
	
	@NpcString(id = 8963, message = "See you next time~")
	public static NpcStringId ID_8963;
	
	@NpcString(id = 8964, message = "Do you think you can stop me?")
	public static NpcStringId ID_8964;
	
	@NpcString(id = 8965, message = "You're so weak. I gotta' go now...")
	public static NpcStringId ID_8965;
	
	@NpcString(id = 8966, message = "You're %s! Good. I'll help you.")
	public static NpcStringId ID_8966;
	
	@NpcString(id = 8967, message = "%s! You're stronger than I thought. See you next time.")
	public static NpcStringId ID_8967;
	
	@NpcString(id = 9001, message = "Saga of the Storm Screamer")
	public static NpcStringId ID_9001;
	
	@NpcString(id = 9002, message = "Saga of the Storm Screamer (In Progress)")
	public static NpcStringId ID_9002;
	
	@NpcString(id = 9003, message = "Saga of the Storm Screamer (Done)")
	public static NpcStringId ID_9003;
	
	@NpcString(id = 9050, message = "Are you %s? Oh! I have a Resonance Amulet!")
	public static NpcStringId ID_9050;
	
	@NpcString(id = 9051, message = "Hey, you're more tenacious than I thought! I'll stop here today.")
	public static NpcStringId ID_9051;
	
	@NpcString(id = 9052, message = "Aargh! I can't believe I lost...")
	public static NpcStringId ID_9052;
	
	@NpcString(id = 9053, message = "Are you the one who's been bothering my minions, %s?")
	public static NpcStringId ID_9053;
	
	@NpcString(id = 9054, message = "Yikes! You're tough!")
	public static NpcStringId ID_9054;
	
	@NpcString(id = 9055, message = "Mind your own business!")
	public static NpcStringId ID_9055;
	
	@NpcString(id = 9056, message = "I'll stop here today.")
	public static NpcStringId ID_9056;
	
	@NpcString(id = 9057, message = "I won't miss you this time!")
	public static NpcStringId ID_9057;
	
	@NpcString(id = 9058, message = "Dammit! I can't do this alone, %s! Give me a hand!")
	public static NpcStringId ID_9058;
	
	@NpcString(id = 9059, message = "%s! Hurry or we'll miss him.")
	public static NpcStringId ID_9059;
	
	@NpcString(id = 9060, message = "%s! Hurry up!")
	public static NpcStringId ID_9060;
	
	@NpcString(id = 9061, message = "I gotta' follow him now.")
	public static NpcStringId ID_9061;
	
	@NpcString(id = 9062, message = "Hey, are you running? Stop!")
	public static NpcStringId ID_9062;
	
	@NpcString(id = 9063, message = "See you next time~")
	public static NpcStringId ID_9063;
	
	@NpcString(id = 9064, message = "Do you think you can stop me?")
	public static NpcStringId ID_9064;
	
	@NpcString(id = 9065, message = "You're so weak. I gotta' go now...")
	public static NpcStringId ID_9065;
	
	@NpcString(id = 9066, message = "Oh! You're %s! Good. I'll help you.")
	public static NpcStringId ID_9066;
	
	@NpcString(id = 9067, message = "%s. You're stronger than I thought. See you next time.")
	public static NpcStringId ID_9067;
	
	@NpcString(id = 9101, message = "Saga of the Arcana Lord")
	public static NpcStringId ID_9101;
	
	@NpcString(id = 9102, message = "Saga of the Arcana Lord (In Progress)")
	public static NpcStringId ID_9102;
	
	@NpcString(id = 9103, message = "Saga of the Arcana Lord (Done)")
	public static NpcStringId ID_9103;
	
	@NpcString(id = 9150, message = "You carouse with evil spirits, %s! You're not worthy of the holy wisdom!")
	public static NpcStringId ID_9150;
	
	@NpcString(id = 9151, message = "You're so stubborn! I can't boss you around any more, can I?")
	public static NpcStringId ID_9151;
	
	@NpcString(id = 9152, message = "How could it happen? Defeated by a Human!")
	public static NpcStringId ID_9152;
	
	@NpcString(id = 9153, message = "You dare to disturb the order of the shrine! Die, %s!")
	public static NpcStringId ID_9153;
	
	@NpcString(id = 9154, message = "My spirit is released from this shell. I'm getting close to Halisha...")
	public static NpcStringId ID_9154;
	
	@NpcString(id = 9155, message = "Mind your own business!")
	public static NpcStringId ID_9155;
	
	@NpcString(id = 9156, message = "This is a waste of time. Goodbye!")
	public static NpcStringId ID_9156;
	
	@NpcString(id = 9157, message = "My master sent me here! I'll give you a hand.")
	public static NpcStringId ID_9157;
	
	@NpcString(id = 9158, message = "Meow~! Master %s, help me!")
	public static NpcStringId ID_9158;
	
	@NpcString(id = 9159, message = "Master %s. Punish him so he can't bother Belinda!")
	public static NpcStringId ID_9159;
	
	@NpcString(id = 9160, message = "Master %s, We'll miss him!")
	public static NpcStringId ID_9160;
	
	@NpcString(id = 9161, message = "Meow~! My master is calling. Meow! I gotta' go now~!")
	public static NpcStringId ID_9161;
	
	@NpcString(id = 9162, message = "Meow~! I missed him. Meow!")
	public static NpcStringId ID_9162;
	
	@NpcString(id = 9163, message = "Good luck! Meow~! I gotta' go now.")
	public static NpcStringId ID_9163;
	
	@NpcString(id = 9164, message = "Curiosity killed the cat? I'll show you!")
	public static NpcStringId ID_9164;
	
	@NpcString(id = 9165, message = "That's all for today...!")
	public static NpcStringId ID_9165;
	
	@NpcString(id = 9166, message = "Are you trying to take Belinda from me, %s? I'll show you!")
	public static NpcStringId ID_9166;
	
	@NpcString(id = 9167, message = "Belinda! I love you! Yikes!!!")
	public static NpcStringId ID_9167;
	
	@NpcString(id = 9201, message = "Saga of the Elemental Master")
	public static NpcStringId ID_9201;
	
	@NpcString(id = 9202, message = "Saga of the Elemental Master (In Progress)")
	public static NpcStringId ID_9202;
	
	@NpcString(id = 9203, message = "Saga of the Elemental Master (Done)")
	public static NpcStringId ID_9203;
	
	@NpcString(id = 9250, message = "You carouse with evil spirits, %s! You're not worthy of the holy wisdom!")
	public static NpcStringId ID_9250;
	
	@NpcString(id = 9251, message = "You're stubborn as a mule! Guess I can't boss you around any more!")
	public static NpcStringId ID_9251;
	
	@NpcString(id = 9252, message = "How could it be?...Defeated by an Elf!")
	public static NpcStringId ID_9252;
	
	@NpcString(id = 9253, message = "You dare to disturb the order of the shrine! Die, %s!")
	public static NpcStringId ID_9253;
	
	@NpcString(id = 9254, message = "My spirit is released from this shell. I'm getting close to Halisha...")
	public static NpcStringId ID_9254;
	
	@NpcString(id = 9255, message = "Mind your own business!")
	public static NpcStringId ID_9255;
	
	@NpcString(id = 9256, message = "This is a waste of time. Goodbye!")
	public static NpcStringId ID_9256;
	
	@NpcString(id = 9257, message = "I came to help you. It's the will of Radyss.")
	public static NpcStringId ID_9257;
	
	@NpcString(id = 9258, message = "%s! Fight with me!")
	public static NpcStringId ID_9258;
	
	@NpcString(id = 9259, message = "%s! We must defeat him!")
	public static NpcStringId ID_9259;
	
	@NpcString(id = 9260, message = "%s. There's no time. We must defeat him!")
	public static NpcStringId ID_9260;
	
	@NpcString(id = 9261, message = "Radyss is calling me. I gotta' go now.")
	public static NpcStringId ID_9261;
	
	@NpcString(id = 9262, message = "I was unable to avenge my brother.")
	public static NpcStringId ID_9262;
	
	@NpcString(id = 9263, message = "May you be blessed.")
	public static NpcStringId ID_9263;
	
	@NpcString(id = 9264, message = "The proud, repent! The foolish, awaken! Sinners, die!")
	public static NpcStringId ID_9264;
	
	@NpcString(id = 9265, message = "Hell's master is calling. Atonement will have to wait!")
	public static NpcStringId ID_9265;
	
	@NpcString(id = 9266, message = "%s, I'll remember your name, heathen.")
	public static NpcStringId ID_9266;
	
	@NpcString(id = 9267, message = "I won't forget the name of one who doesn't obey holy judgment, %s!")
	public static NpcStringId ID_9267;
	
	@NpcString(id = 9301, message = "Saga of the Spectral Master")
	public static NpcStringId ID_9301;
	
	@NpcString(id = 9302, message = "Saga of the Spectral Master (In Progress)")
	public static NpcStringId ID_9302;
	
	@NpcString(id = 9303, message = "Saga of the Spectral Master (Done)")
	public static NpcStringId ID_9303;
	
	@NpcString(id = 9350, message = "You carouse with evil spirits, %s! You're not worthy of the holy wisdom!")
	public static NpcStringId ID_9350;
	
	@NpcString(id = 9351, message = "You're stubborn as a mule! I guess I can't boss you around any more!")
	public static NpcStringId ID_9351;
	
	@NpcString(id = 9352, message = "Could it be...? Defeated by a Dark Elf!")
	public static NpcStringId ID_9352;
	
	@NpcString(id = 9353, message = "You dare to disturb the order of the shrine! Die, %s!")
	public static NpcStringId ID_9353;
	
	@NpcString(id = 9354, message = "My spirit is released from this shell. I'm getting close to Halisha...")
	public static NpcStringId ID_9354;
	
	@NpcString(id = 9355, message = "Mind your own business!")
	public static NpcStringId ID_9355;
	
	@NpcString(id = 9356, message = "This is a waste of time. Goodbye!")
	public static NpcStringId ID_9356;
	
	@NpcString(id = 9357, message = "Shadow Summoner, I came here to help you.")
	public static NpcStringId ID_9357;
	
	@NpcString(id = 9358, message = "Shadow Summoner, %s! Fight with me!")
	public static NpcStringId ID_9358;
	
	@NpcString(id = 9359, message = "%s, You'll die if you don't kill him!")
	public static NpcStringId ID_9359;
	
	@NpcString(id = 9360, message = "Hurry, %s! Don't miss him!")
	public static NpcStringId ID_9360;
	
	@NpcString(id = 9361, message = "I can't hold on any longer...")
	public static NpcStringId ID_9361;
	
	@NpcString(id = 9362, message = "After all that...I missed him...")
	public static NpcStringId ID_9362;
	
	@NpcString(id = 9363, message = "Shadow summoner! May you be blessed!")
	public static NpcStringId ID_9363;
	
	@NpcString(id = 9364, message = "My master sent me here to kill you!")
	public static NpcStringId ID_9364;
	
	@NpcString(id = 9365, message = "The shadow is calling me...")
	public static NpcStringId ID_9365;
	
	@NpcString(id = 9366, message = "%s, you want to die early? I'll send you to the darkness!")
	public static NpcStringId ID_9366;
	
	@NpcString(id = 9367, message = "You deal in darkness, %s! I'll pay you back.")
	public static NpcStringId ID_9367;
	
	@NpcString(id = 9401, message = "Saga of the Soultaker")
	public static NpcStringId ID_9401;
	
	@NpcString(id = 9402, message = "Saga of the Soultaker (In Progress)")
	public static NpcStringId ID_9402;
	
	@NpcString(id = 9403, message = "Saga of the Soultaker (Done)")
	public static NpcStringId ID_9403;
	
	@NpcString(id = 9450, message = "You're %s? I won't be like Hindemith!")
	public static NpcStringId ID_9450;
	
	@NpcString(id = 9451, message = "You're feistier than I thought! I'll stop here for today.")
	public static NpcStringId ID_9451;
	
	@NpcString(id = 9452, message = "Aargh! I can't believe I lost...")
	public static NpcStringId ID_9452;
	
	@NpcString(id = 9453, message = "Are you the one who is bothering my minions, %s?")
	public static NpcStringId ID_9453;
	
	@NpcString(id = 9454, message = "Yikes! You're tough!")
	public static NpcStringId ID_9454;
	
	@NpcString(id = 9455, message = "Mind your own business!")
	public static NpcStringId ID_9455;
	
	@NpcString(id = 9456, message = "I'll stop here for today.")
	public static NpcStringId ID_9456;
	
	@NpcString(id = 9457, message = "I can't let you commune with Tablet of Vision! Give me the Resonance Amulet!")
	public static NpcStringId ID_9457;
	
	@NpcString(id = 9458, message = "Dammit! I can't do this alone, %s! Give me a hand!")
	public static NpcStringId ID_9458;
	
	@NpcString(id = 9459, message = "%s! Hurry or we'll miss him.")
	public static NpcStringId ID_9459;
	
	@NpcString(id = 9460, message = "%s! Please hurry!")
	public static NpcStringId ID_9460;
	
	@NpcString(id = 9461, message = "I must follow him now.")
	public static NpcStringId ID_9461;
	
	@NpcString(id = 9462, message = "Are you running? Stop!")
	public static NpcStringId ID_9462;
	
	@NpcString(id = 9463, message = "See you next time.")
	public static NpcStringId ID_9463;
	
	@NpcString(id = 9464, message = "Are you betraying me? I thought something was wrong...I'll stop here.")
	public static NpcStringId ID_9464;
	
	@NpcString(id = 9465, message = "I gotta' go now.")
	public static NpcStringId ID_9465;
	
	@NpcString(id = 9466, message = "You're %s? Even two of you can't stop me!")
	public static NpcStringId ID_9466;
	
	@NpcString(id = 9467, message = "Dammit! My Resonance Amulet...%s, I'll never forget to pay you back.")
	public static NpcStringId ID_9467;
	
	@NpcString(id = 9501, message = "Saga of the Hell Knight")
	public static NpcStringId ID_9501;
	
	@NpcString(id = 9502, message = "Saga of the Hell Knight (In Progress)")
	public static NpcStringId ID_9502;
	
	@NpcString(id = 9503, message = "Saga of the Hell Knight (Done)")
	public static NpcStringId ID_9503;
	
	@NpcString(id = 9550, message = "Are you... %s? I won't be like Waldstein!")
	public static NpcStringId ID_9550;
	
	@NpcString(id = 9551, message = "You're feistier than I thought! I'll stop here for today.")
	public static NpcStringId ID_9551;
	
	@NpcString(id = 9552, message = "Yikes! I can't believe I lost...")
	public static NpcStringId ID_9552;
	
	@NpcString(id = 9553, message = "Are you the one bothering my minions, %s?")
	public static NpcStringId ID_9553;
	
	@NpcString(id = 9554, message = "Yikes! You're tough!")
	public static NpcStringId ID_9554;
	
	@NpcString(id = 9555, message = "Mind your own business!")
	public static NpcStringId ID_9555;
	
	@NpcString(id = 9556, message = "I'll stop here for today.")
	public static NpcStringId ID_9556;
	
	@NpcString(id = 9557, message = "You can't commune with the Tablet of Vision! Give me the Resonance Amulet!")
	public static NpcStringId ID_9557;
	
	@NpcString(id = 9558, message = "Dammit! I can't do this alone, %s! Give me a hand!")
	public static NpcStringId ID_9558;
	
	@NpcString(id = 9559, message = "%s! Hurry or we'll miss him.")
	public static NpcStringId ID_9559;
	
	@NpcString(id = 9560, message = "%s! Please hurry!")
	public static NpcStringId ID_9560;
	
	@NpcString(id = 9561, message = "I gotta' go follow him.")
	public static NpcStringId ID_9561;
	
	@NpcString(id = 9562, message = "Are you running? Stop!")
	public static NpcStringId ID_9562;
	
	@NpcString(id = 9563, message = "See you next time.")
	public static NpcStringId ID_9563;
	
	@NpcString(id = 9564, message = "Are you betraying me? I thought something was wrong...I'll stop here.")
	public static NpcStringId ID_9564;
	
	@NpcString(id = 9565, message = "I gotta go now...")
	public static NpcStringId ID_9565;
	
	@NpcString(id = 9566, message = "You're... %s? Even two of you can't stop me!")
	public static NpcStringId ID_9566;
	
	@NpcString(id = 9567, message = "Dammit! My Resonance Amulet...%s, I'll never forget this.")
	public static NpcStringId ID_9567;
	
	@NpcString(id = 9601, message = "Saga of the Spectral Dancer")
	public static NpcStringId ID_9601;
	
	@NpcString(id = 9602, message = "Saga of the Spectral Dancer (In Progress)")
	public static NpcStringId ID_9602;
	
	@NpcString(id = 9603, message = "Saga of the Spectral Dancer (Done)")
	public static NpcStringId ID_9603;
	
	@NpcString(id = 9650, message = "You're %s? I'll kill you for Hallate!")
	public static NpcStringId ID_9650;
	
	@NpcString(id = 9651, message = "You're tougher than I thought, but you still can't rival me!")
	public static NpcStringId ID_9651;
	
	@NpcString(id = 9652, message = "Hallate! Forgive me! I can't help you.")
	public static NpcStringId ID_9652;
	
	@NpcString(id = 9653, message = "Are you the one who's been bothering my minions, %s?")
	public static NpcStringId ID_9653;
	
	@NpcString(id = 9654, message = "Dammit! I can't believe you beat me!")
	public static NpcStringId ID_9654;
	
	@NpcString(id = 9655, message = "Who are you? Mind your own business, coward.")
	public static NpcStringId ID_9655;
	
	@NpcString(id = 9656, message = "How weak. I'll forgive you this time because you made me laugh.")
	public static NpcStringId ID_9656;
	
	@NpcString(id = 9657, message = "Purgatory Lord, I won't fail this time.")
	public static NpcStringId ID_9657;
	
	@NpcString(id = 9658, message = "%s! Now's the time to put your training to the test!")
	public static NpcStringId ID_9658;
	
	@NpcString(id = 9659, message = "%s! Your sword skills can't be that bad.")
	public static NpcStringId ID_9659;
	
	@NpcString(id = 9660, message = "%s! Show your strength!")
	public static NpcStringId ID_9660;
	
	@NpcString(id = 9661, message = "I have some pressing business. I have to go.")
	public static NpcStringId ID_9661;
	
	@NpcString(id = 9662, message = "I missed him! Dammit.")
	public static NpcStringId ID_9662;
	
	@NpcString(id = 9663, message = "Try again sometime.")
	public static NpcStringId ID_9663;
	
	@NpcString(id = 9664, message = "I'll kill anyone who gets in my way!")
	public static NpcStringId ID_9664;
	
	@NpcString(id = 9665, message = "This is pathetic! You make me laugh.")
	public static NpcStringId ID_9665;
	
	@NpcString(id = 9666, message = "%s! Are you trying to get in my way?")
	public static NpcStringId ID_9666;
	
	@NpcString(id = 9667, message = "%s! When I come back, I'll kill you.")
	public static NpcStringId ID_9667;
	
	@NpcString(id = 9701, message = "Saga of the Shillien Templar")
	public static NpcStringId ID_9701;
	
	@NpcString(id = 9702, message = "Saga of the Shillien Templar (In Progress)")
	public static NpcStringId ID_9702;
	
	@NpcString(id = 9703, message = "Saga of the Shillien Templar (Done)")
	public static NpcStringId ID_9703;
	
	@NpcString(id = 9750, message = "%s? Wake up! Time to die!")
	public static NpcStringId ID_9750;
	
	@NpcString(id = 9751, message = "You're tougher than I thought!  I'll be back!")
	public static NpcStringId ID_9751;
	
	@NpcString(id = 9752, message = "I lost? It can't be!")
	public static NpcStringId ID_9752;
	
	@NpcString(id = 9753, message = "Are you the one who's been bothering my minions, %s?")
	public static NpcStringId ID_9753;
	
	@NpcString(id = 9754, message = "Dammit! I can't believe you beat me!")
	public static NpcStringId ID_9754;
	
	@NpcString(id = 9755, message = "Who are you? Mind your own business, coward.")
	public static NpcStringId ID_9755;
	
	@NpcString(id = 9756, message = "How weak. I'll forgive you this time because you made me laugh.")
	public static NpcStringId ID_9756;
	
	@NpcString(id = 9757, message = "You're a cunning fiend. I won't fail again.")
	public static NpcStringId ID_9757;
	
	@NpcString(id = 9758, message = "%s! It's after you! Fight!")
	public static NpcStringId ID_9758;
	
	@NpcString(id = 9759, message = "%s! You have to fight better than that if you expect to survive!")
	public static NpcStringId ID_9759;
	
	@NpcString(id = 9760, message = "%s! Pull yourself together. Fight!")
	public static NpcStringId ID_9760;
	
	@NpcString(id = 9761, message = "I'll catch the cunning fiend.")
	public static NpcStringId ID_9761;
	
	@NpcString(id = 9762, message = "I missed him again! He's clever!")
	public static NpcStringId ID_9762;
	
	@NpcString(id = 9763, message = "Don't cower like a puppy next time!")
	public static NpcStringId ID_9763;
	
	@NpcString(id = 9764, message = "I have only one goal. Get out of my way.")
	public static NpcStringId ID_9764;
	
	@NpcString(id = 9765, message = "Just wait. You'll get yours!")
	public static NpcStringId ID_9765;
	
	@NpcString(id = 9766, message = "%s! You're a coward, aren't you!")
	public static NpcStringId ID_9766;
	
	@NpcString(id = 9767, message = "%s! I'll kill you next time.")
	public static NpcStringId ID_9767;
	
	@NpcString(id = 9801, message = "Saga of the Shillien Saint")
	public static NpcStringId ID_9801;
	
	@NpcString(id = 9802, message = "Saga of the Shillien Saint (In Progress)")
	public static NpcStringId ID_9802;
	
	@NpcString(id = 9803, message = "Saga of the Shillien Saint (Done)")
	public static NpcStringId ID_9803;
	
	@NpcString(id = 9850, message = "%s! How foolish to act against the will of god.")
	public static NpcStringId ID_9850;
	
	@NpcString(id = 9851, message = "Your faith is stronger than I thought. I'll get you next time.")
	public static NpcStringId ID_9851;
	
	@NpcString(id = 9852, message = "Tanakia, forgive me! I couldn't fulfill your dream!")
	public static NpcStringId ID_9852;
	
	@NpcString(id = 9853, message = "Are you the one who's been bothering my minions, %s?")
	public static NpcStringId ID_9853;
	
	@NpcString(id = 9854, message = "Dammit! I can't believe you beat me!")
	public static NpcStringId ID_9854;
	
	@NpcString(id = 9855, message = "Who are you? Mind your own business, you coward!")
	public static NpcStringId ID_9855;
	
	@NpcString(id = 9856, message = "How weak. I'll forgive you this time because you made me laugh.")
	public static NpcStringId ID_9856;
	
	@NpcString(id = 9857, message = "Tanakia, your lie has already been exposed!")
	public static NpcStringId ID_9857;
	
	@NpcString(id = 9858, message = "%s! Help me overcome this.")
	public static NpcStringId ID_9858;
	
	@NpcString(id = 9859, message = "%s! We can't defeat Tanakia this way.")
	public static NpcStringId ID_9859;
	
	@NpcString(id = 9860, message = "%s! Here's our chance. Don't squander the opportunity!")
	public static NpcStringId ID_9860;
	
	@NpcString(id = 9861, message = "Goodbye. We'll meet again if fate allows.")
	public static NpcStringId ID_9861;
	
	@NpcString(id = 9862, message = "I'll follow Tanakia to correct this falsehood.")
	public static NpcStringId ID_9862;
	
	@NpcString(id = 9863, message = "I'll be back if fate allows.")
	public static NpcStringId ID_9863;
	
	@NpcString(id = 9864, message = "For Shilen!")
	public static NpcStringId ID_9864;
	
	@NpcString(id = 9865, message = "I'll be back. You'll pay.")
	public static NpcStringId ID_9865;
	
	@NpcString(id = 9866, message = "%s! Are you trying to spoil my plan?")
	public static NpcStringId ID_9866;
	
	@NpcString(id = 9867, message = "%s! I won't forget you. You'll pay.")
	public static NpcStringId ID_9867;
	
	@NpcString(id = 9901, message = "Saga of the Fortune Seeker")
	public static NpcStringId ID_9901;
	
	@NpcString(id = 9902, message = "Saga of the Fortune Seeker (In Progress)")
	public static NpcStringId ID_9902;
	
	@NpcString(id = 9903, message = "Saga of the Fortune Seeker (Done)")
	public static NpcStringId ID_9903;
	
	@NpcString(id = 9950, message = "%s, You have an affinity for dangerous ideas. Are you ready to die?")
	public static NpcStringId ID_9950;
	
	@NpcString(id = 9951, message = "My time is up...")
	public static NpcStringId ID_9951;
	
	@NpcString(id = 9952, message = "I can't believe I must kneel to a Human!")
	public static NpcStringId ID_9952;
	
	@NpcString(id = 9953, message = "You dare to disturb the order of the shrine! Die, %s!")
	public static NpcStringId ID_9953;
	
	@NpcString(id = 9954, message = "My spirit is released from this shell. I'm getting close to Halisha...")
	public static NpcStringId ID_9954;
	
	@NpcString(id = 9955, message = "Mind your own business!")
	public static NpcStringId ID_9955;
	
	@NpcString(id = 9956, message = "This is a waste of time. Goodbye!")
	public static NpcStringId ID_9956;
	
	@NpcString(id = 9957, message = "Minervia! What's the matter?")
	public static NpcStringId ID_9957;
	
	@NpcString(id = 9958, message = "The princess is in danger. Why are you staring?")
	public static NpcStringId ID_9958;
	
	@NpcString(id = 9959, message = "Master %s! Come on, Hurry up!")
	public static NpcStringId ID_9959;
	
	@NpcString(id = 9960, message = "We can't fail! Master %s, Pull yourself together!")
	public static NpcStringId ID_9960;
	
	@NpcString(id = 9961, message = "What am I doing... I gotta' go! Goodbye.")
	public static NpcStringId ID_9961;
	
	@NpcString(id = 9962, message = "Dammit! I missed...!")
	public static NpcStringId ID_9962;
	
	@NpcString(id = 9963, message = "Sorry, but I must say goodbye again... Good luck to you!")
	public static NpcStringId ID_9963;
	
	@NpcString(id = 9964, message = "I can't yield the secret of the tablet!")
	public static NpcStringId ID_9964;
	
	@NpcString(id = 9965, message = "I'll stop here for now...")
	public static NpcStringId ID_9965;
	
	@NpcString(id = 9966, message = "%s, you dared to leave scar on my face! I'll kill you!!!")
	public static NpcStringId ID_9966;
	
	@NpcString(id = 9967, message = "%s, I won't forget your name...Ha!")
	public static NpcStringId ID_9967;
	
	@NpcString(id = 10001, message = "Saga of the Maestro")
	public static NpcStringId ID_10001;
	
	@NpcString(id = 10002, message = "Saga of the Maestro (In Progress)")
	public static NpcStringId ID_10002;
	
	@NpcString(id = 10003, message = "Saga of the Maestro (Done)")
	public static NpcStringId ID_10003;
	
	@NpcString(id = 10050, message = "%s? You have an affinity for bad ideas. Are you ready to die?")
	public static NpcStringId ID_10050;
	
	@NpcString(id = 10051, message = "My time is up...")
	public static NpcStringId ID_10051;
	
	@NpcString(id = 10052, message = "I can't believe I must kneel before a Human!")
	public static NpcStringId ID_10052;
	
	@NpcString(id = 10053, message = "You dare to disturb the order of the shrine! Die, %s!")
	public static NpcStringId ID_10053;
	
	@NpcString(id = 10054, message = "My spirit is released from this shell. I'm getting close to Halisha...")
	public static NpcStringId ID_10054;
	
	@NpcString(id = 10055, message = "Mind your own business!")
	public static NpcStringId ID_10055;
	
	@NpcString(id = 10056, message = "This is a waste of time. Goodbye!")
	public static NpcStringId ID_10056;
	
	@NpcString(id = 10057, message = "You thief! Give me the Resonance Amulet!")
	public static NpcStringId ID_10057;
	
	@NpcString(id = 10058, message = "Ugh! %s, Help me!")
	public static NpcStringId ID_10058;
	
	@NpcString(id = 10059, message = "%s. Please, help me! Together we can beat him.")
	public static NpcStringId ID_10059;
	
	@NpcString(id = 10060, message = "%s! Are you going to let a guild member die?")
	public static NpcStringId ID_10060;
	
	@NpcString(id = 10061, message = "I'm sorry, but I gotta' go first!")
	public static NpcStringId ID_10061;
	
	@NpcString(id = 10062, message = "Aaaah! I couldn't get the Resonance Amulet.")
	public static NpcStringId ID_10062;
	
	@NpcString(id = 10063, message = "Take care! I gotta' go now~!")
	public static NpcStringId ID_10063;
	
	@NpcString(id = 10064, message = "I'm sorry, but it's my job to kill you now!")
	public static NpcStringId ID_10064;
	
	@NpcString(id = 10065, message = "What a waste of time!")
	public static NpcStringId ID_10065;
	
	@NpcString(id = 10066, message = "%s! How could you do this? I'll kill you!")
	public static NpcStringId ID_10066;
	
	@NpcString(id = 10067, message = "%s! I'll pay you back!")
	public static NpcStringId ID_10067;
	
	@NpcString(id = 10068, message = "Why don't you just die?!")
	public static NpcStringId ID_10068;
	
	@NpcString(id = 10069, message = "Taste the sting of Level 5 Spoil!")
	public static NpcStringId ID_10069;
	
	@NpcString(id = 10070, message = "The item is already inside you...")
	public static NpcStringId ID_10070;
	
	@NpcString(id = 10071, message = "This potion you're making me drink is worth its weight in gold!")
	public static NpcStringId ID_10071;
	
	@NpcString(id = 10072, message = "This potion is prepared from the ground gall of a bear. Be careful - it packs quite a punch!")
	public static NpcStringId ID_10072;
	
	@NpcString(id = 10073, message = "How can you use a potion on a newbie...")
	public static NpcStringId ID_10073;
	
	@NpcString(id = 10075, message = "Dear %s, may the blessings of Einhasad be with you always.")
	public static NpcStringId ID_10075;
	
	@NpcString(id = 10076, message = "Dear brother %s, follow the path of light with me...")
	public static NpcStringId ID_10076;
	
	@NpcString(id = 10077, message = "%s, why would you choose the path of darkness?!")
	public static NpcStringId ID_10077;
	
	@NpcString(id = 10078, message = "%s!  How dare you defy the will of Einhasad!")
	public static NpcStringId ID_10078;
	
	@NpcString(id = 10079, message = "The door to the 3rd floor of the altar is now open.")
	public static NpcStringId ID_10079;
	
	@NpcString(id = 10101, message = "Sword of Solidarity")
	public static NpcStringId ID_10101;
	
	@NpcString(id = 10102, message = "Sword of Solidarity (In Progress)")
	public static NpcStringId ID_10102;
	
	@NpcString(id = 10103, message = "Sword of Solidarity (Done)")
	public static NpcStringId ID_10103;
	
	@NpcString(id = 10201, message = "Sea of Spores Fever")
	public static NpcStringId ID_10201;
	
	@NpcString(id = 10202, message = "Sea of Spores Fever (In Progress)")
	public static NpcStringId ID_10202;
	
	@NpcString(id = 10203, message = "Sea of Spores Fever (Done)")
	public static NpcStringId ID_10203;
	
	@NpcString(id = 10301, message = "Spirit of Craftsman")
	public static NpcStringId ID_10301;
	
	@NpcString(id = 10302, message = "Spirit of Craftsman (In Progress)")
	public static NpcStringId ID_10302;
	
	@NpcString(id = 10303, message = "Spirit of Craftsman (Done)")
	public static NpcStringId ID_10303;
	
	@NpcString(id = 10401, message = "Spirit of Mirrors")
	public static NpcStringId ID_10401;
	
	@NpcString(id = 10402, message = "Spirit of Mirrors (In Progress)")
	public static NpcStringId ID_10402;
	
	@NpcString(id = 10403, message = "Spirit of Mirrors (Done)")
	public static NpcStringId ID_10403;
	
	@NpcString(id = 10501, message = "Skirmish with the Orcs")
	public static NpcStringId ID_10501;
	
	@NpcString(id = 10502, message = "Skirmish with the Orcs (In Progress)")
	public static NpcStringId ID_10502;
	
	@NpcString(id = 10503, message = "Skirmish with the Orcs (Done)")
	public static NpcStringId ID_10503;
	
	@NpcString(id = 10601, message = "Forgotten Truth")
	public static NpcStringId ID_10601;
	
	@NpcString(id = 10602, message = "Forgotten Truth (In Progress)")
	public static NpcStringId ID_10602;
	
	@NpcString(id = 10603, message = "Forgotten Truth (Done)")
	public static NpcStringId ID_10603;
	
	@NpcString(id = 10701, message = "Merciless Punishment")
	public static NpcStringId ID_10701;
	
	@NpcString(id = 10702, message = "Merciless Punishment (In Progress)")
	public static NpcStringId ID_10702;
	
	@NpcString(id = 10703, message = "Merciless Punishment (Done)")
	public static NpcStringId ID_10703;
	
	@NpcString(id = 10801, message = "Jumble, Tumble, Diamond Fuss")
	public static NpcStringId ID_10801;
	
	@NpcString(id = 10802, message = "Jumble, Tumble, Diamond Fuss (In Progress)")
	public static NpcStringId ID_10802;
	
	@NpcString(id = 10803, message = "Jumble, Tumble, Diamond Fuss (Done)")
	public static NpcStringId ID_10803;
	
	@NpcString(id = 10901, message = "In Search of the Nest")
	public static NpcStringId ID_10901;
	
	@NpcString(id = 10902, message = "In Search of the Nest (In Progress)")
	public static NpcStringId ID_10902;
	
	@NpcString(id = 10903, message = "In Search of the Nest (Done)")
	public static NpcStringId ID_10903;
	
	@NpcString(id = 11001, message = "To the Primeval Isle")
	public static NpcStringId ID_11001;
	
	@NpcString(id = 11002, message = "To the Primeval Isle (In Progress)")
	public static NpcStringId ID_11002;
	
	@NpcString(id = 11003, message = "To the Primeval Isle (Done)")
	public static NpcStringId ID_11003;
	
	@NpcString(id = 11101, message = "Elrokian Hunters")
	public static NpcStringId ID_11101;
	
	@NpcString(id = 11102, message = "Elrokian Hunter's Proof (In Progress)")
	public static NpcStringId ID_11102;
	
	@NpcString(id = 11103, message = "Elrokian Hunter's Proof (Done)")
	public static NpcStringId ID_11103;
	
	@NpcString(id = 11201, message = "Walk of Fate")
	public static NpcStringId ID_11201;
	
	@NpcString(id = 11202, message = "Walk of Fate (In Progress)")
	public static NpcStringId ID_11202;
	
	@NpcString(id = 11203, message = "Walk of Fate (Done)")
	public static NpcStringId ID_11203;
	
	@NpcString(id = 11301, message = "Status of the Beacon Tower")
	public static NpcStringId ID_11301;
	
	@NpcString(id = 11302, message = "Status of the Beacon Tower (In Progress)")
	public static NpcStringId ID_11302;
	
	@NpcString(id = 11303, message = "Status of the Beacon Tower (Done)")
	public static NpcStringId ID_11303;
	
	@NpcString(id = 11401, message = "Resurrection of an Old Manager")
	public static NpcStringId ID_11401;
	
	@NpcString(id = 11402, message = "Resurrection of an Old Manager (In Progress)")
	public static NpcStringId ID_11402;
	
	@NpcString(id = 11403, message = "Resurrection of an Old Manager (Done)")
	public static NpcStringId ID_11403;
	
	@NpcString(id = 11450, message = "You, %s, you attacked Wendy. Prepare to die!")
	public static NpcStringId ID_11450;
	
	@NpcString(id = 11451, message = "%s, your enemy was driven out. I will now withdraw and await your next command.")
	public static NpcStringId ID_11451;
	
	@NpcString(id = 11452, message = "This enemy is far too powerful for me to fight. I must withdraw.")
	public static NpcStringId ID_11452;
	
	@NpcString(id = 11453, message = "The radio signal detector is responding. # A suspicious pile of stones catches your eye.")
	public static NpcStringId ID_11453;
	
	@NpcString(id = 11501, message = "The Other Side of Truth")
	public static NpcStringId ID_11501;
	
	@NpcString(id = 11502, message = "The Other Side of Truth (In Progress)")
	public static NpcStringId ID_11502;
	
	@NpcString(id = 11503, message = "The Other Side of Truth (Done)")
	public static NpcStringId ID_11503;
	
	@NpcString(id = 11550, message = "This looks like the right place...")
	public static NpcStringId ID_11550;
	
	@NpcString(id = 11551, message = "I see someone. Is this fate?")
	public static NpcStringId ID_11551;
	
	@NpcString(id = 11552, message = "We meet again.")
	public static NpcStringId ID_11552;
	
	@NpcString(id = 11553, message = "Don't bother trying to find out more about me. Follow your own destiny.")
	public static NpcStringId ID_11553;
	
	@NpcString(id = 11601, message = "Beyond the Hills of Winter")
	public static NpcStringId ID_11601;
	
	@NpcString(id = 11602, message = "Beyond the Hills of Winter (In Progress)")
	public static NpcStringId ID_11602;
	
	@NpcString(id = 11603, message = "Beyond the Hills of Winter (Done)")
	public static NpcStringId ID_11603;
	
	@NpcString(id = 11701, message = "The Ocean of Distant Stars")
	public static NpcStringId ID_11701;
	
	@NpcString(id = 11702, message = "The Ocean of Distant Stars (In Progress)")
	public static NpcStringId ID_11702;
	
	@NpcString(id = 11703, message = "The Ocean of Distant Stars (Done)")
	public static NpcStringId ID_11703;
	
	@NpcString(id = 11801, message = "To Lead and Be Led")
	public static NpcStringId ID_11801;
	
	@NpcString(id = 11802, message = "To Lead and Be Led (In Progress)")
	public static NpcStringId ID_11802;
	
	@NpcString(id = 11803, message = "To Lead and Be Led (Done)")
	public static NpcStringId ID_11803;
	
	@NpcString(id = 11804, message = "To Lead and Be Led (Sponsor)")
	public static NpcStringId ID_11804;
	
	@NpcString(id = 11901, message = "Last Imperial Prince")
	public static NpcStringId ID_11901;
	
	@NpcString(id = 11902, message = "Last Imperial Prince (In Progress)")
	public static NpcStringId ID_11902;
	
	@NpcString(id = 11903, message = "Last Imperial Prince (Done)")
	public static NpcStringId ID_11903;
	
	@NpcString(id = 12001, message = "Pavel's Last Research")
	public static NpcStringId ID_12001;
	
	@NpcString(id = 12002, message = "Pavel's Last Research (In Progress)")
	public static NpcStringId ID_12002;
	
	@NpcString(id = 12003, message = "Pavel's Last Research (Done)")
	public static NpcStringId ID_12003;
	
	@NpcString(id = 12101, message = "Pavel the Giant")
	public static NpcStringId ID_12101;
	
	@NpcString(id = 12102, message = "Pavel the Giant (In Progress)")
	public static NpcStringId ID_12102;
	
	@NpcString(id = 12103, message = "Pavel the Giant (Done)")
	public static NpcStringId ID_12103;
	
	@NpcString(id = 12201, message = "Ominous News")
	public static NpcStringId ID_12201;
	
	@NpcString(id = 12202, message = "Ominous News (In Progress)")
	public static NpcStringId ID_12202;
	
	@NpcString(id = 12203, message = "Ominous News (Done)")
	public static NpcStringId ID_12203;
	
	@NpcString(id = 12301, message = "The Leader and the Follower")
	public static NpcStringId ID_12301;
	
	@NpcString(id = 12302, message = "The Leader and the Follower (In Progress)")
	public static NpcStringId ID_12302;
	
	@NpcString(id = 12303, message = "The Leader and the Follower (Done)")
	public static NpcStringId ID_12303;
	
	@NpcString(id = 12304, message = "The Leader and the Follower (Sponsor)")
	public static NpcStringId ID_12304;
	
	@NpcString(id = 12401, message = "Meeting the Elroki")
	public static NpcStringId ID_12401;
	
	@NpcString(id = 12402, message = "Meeting the Elroki (In Progress)")
	public static NpcStringId ID_12402;
	
	@NpcString(id = 12403, message = "Meeting the Elroki (Done)")
	public static NpcStringId ID_12403;
	
	@NpcString(id = 12501, message = "The Name of Evil 1")
	public static NpcStringId ID_12501;
	
	@NpcString(id = 12502, message = "The Name of Evil 1 (In Progress)")
	public static NpcStringId ID_12502;
	
	@NpcString(id = 12503, message = "The Name of Evil 1 (Done)")
	public static NpcStringId ID_12503;
	
	@NpcString(id = 12601, message = "The Name of Evil 2")
	public static NpcStringId ID_12601;
	
	@NpcString(id = 12602, message = "The Name of Evil 2 (In Progress)")
	public static NpcStringId ID_12602;
	
	@NpcString(id = 12603, message = "The Name of Evil 2 (Done)")
	public static NpcStringId ID_12603;
	
	@NpcString(id = 12701, message = "Kamael: A Window to the Future")
	public static NpcStringId ID_12701;
	
	@NpcString(id = 12702, message = "Kamael: A Window to the Future (In Progress)")
	public static NpcStringId ID_12702;
	
	@NpcString(id = 12703, message = "Kamael: A Window to the Future (Done)")
	public static NpcStringId ID_12703;
	
	@NpcString(id = 12801, message = "Pailaka - Song of Ice and Fire")
	public static NpcStringId ID_12801;
	
	@NpcString(id = 12802, message = "Pailaka - Song of Ice and Fire (In Progress)")
	public static NpcStringId ID_12802;
	
	@NpcString(id = 12803, message = "Pailaka - Song of Ice and Fire (Done)")
	public static NpcStringId ID_12803;
	
	@NpcString(id = 12901, message = "Pailaka - Devil's Legacy")
	public static NpcStringId ID_12901;
	
	@NpcString(id = 12902, message = "Pailaka - Devil's Legacy (In Progress)")
	public static NpcStringId ID_12902;
	
	@NpcString(id = 12903, message = "Pailaka - Devil's Legacy (Done)")
	public static NpcStringId ID_12903;
	
	@NpcString(id = 13001, message = "Path to Hellbound")
	public static NpcStringId ID_13001;
	
	@NpcString(id = 13002, message = "Path to Hellbound (In Progress)")
	public static NpcStringId ID_13002;
	
	@NpcString(id = 13003, message = "Path to Hellbound (Done)")
	public static NpcStringId ID_13003;
	
	@NpcString(id = 13031, message = "Oink oink! This hurts too much! Are you sure you're trying to cure me? You're not very good at this!")
	public static NpcStringId ID_13031;
	
	@NpcString(id = 13032, message = "Oink oink! Nooo! I don't want to go back to normal! I look better with this body. What have you done?!")
	public static NpcStringId ID_13032;
	
	@NpcString(id = 13033, message = "You cured me! Thanks a lot! Oink oink!")
	public static NpcStringId ID_13033;
	
	@NpcString(id = 13101, message = "Bird in a Cage")
	public static NpcStringId ID_13101;
	
	@NpcString(id = 13102, message = "Bird in a Cage (In Progress)")
	public static NpcStringId ID_13102;
	
	@NpcString(id = 13103, message = "Bird in a Cage (Done)")
	public static NpcStringId ID_13103;
	
	@NpcString(id = 13201, message = "Curiosity of a Matras")
	public static NpcStringId ID_13201;
	
	@NpcString(id = 13202, message = "Curiosity of a Matras (In Progress)")
	public static NpcStringId ID_13202;
	
	@NpcString(id = 13203, message = "Curiosity of a Matras (Done)")
	public static NpcStringId ID_13203;
	
	@NpcString(id = 13301, message = "That's Bloody Hot!")
	public static NpcStringId ID_13301;
	
	@NpcString(id = 13302, message = "That's Bloody Hot! (In Progress)")
	public static NpcStringId ID_13302;
	
	@NpcString(id = 13303, message = "That's Bloody Hot! (Done)")
	public static NpcStringId ID_13303;
	
	@NpcString(id = 13401, message = "Temple Missionary")
	public static NpcStringId ID_13401;
	
	@NpcString(id = 13402, message = "Temple Missionary (In Progress)")
	public static NpcStringId ID_13402;
	
	@NpcString(id = 13403, message = "Temple Missionary (Done)")
	public static NpcStringId ID_13403;
	
	@NpcString(id = 13501, message = "Temple Executor")
	public static NpcStringId ID_13501;
	
	@NpcString(id = 13502, message = "Temple Executor (In Progress)")
	public static NpcStringId ID_13502;
	
	@NpcString(id = 13503, message = "Temple Executor (Done)")
	public static NpcStringId ID_13503;
	
	@NpcString(id = 13601, message = "More Than Meets The Eye")
	public static NpcStringId ID_13601;
	
	@NpcString(id = 13602, message = "More Than Meets The Eye (In Progress)")
	public static NpcStringId ID_13602;
	
	@NpcString(id = 13603, message = "More Than Meets The Eye (Done)")
	public static NpcStringId ID_13603;
	
	@NpcString(id = 13701, message = "Temple Champion - 1")
	public static NpcStringId ID_13701;
	
	@NpcString(id = 13702, message = "Temple Champion - 1 (In Progress)")
	public static NpcStringId ID_13702;
	
	@NpcString(id = 13703, message = "Temple Champion - 1 (Done)")
	public static NpcStringId ID_13703;
	
	@NpcString(id = 13801, message = "Temple Champion - 2")
	public static NpcStringId ID_13801;
	
	@NpcString(id = 13802, message = "Temple Champion - 2 (In Progress)")
	public static NpcStringId ID_13802;
	
	@NpcString(id = 13803, message = "Temple Champion - 2 (Done)")
	public static NpcStringId ID_13803;
	
	@NpcString(id = 13901, message = "Shadow Fox - 1")
	public static NpcStringId ID_13901;
	
	@NpcString(id = 13902, message = "Shadow Fox - 1 (In Progress)")
	public static NpcStringId ID_13902;
	
	@NpcString(id = 13903, message = "Shadow Fox - 1 (Done)")
	public static NpcStringId ID_13903;
	
	@NpcString(id = 14000, message = "%s, you have passed Formal Wear quest!")
	public static NpcStringId ID_14000;
	
	@NpcString(id = 14001, message = "%s! How Dare you stop Beleth's.... man... die....")
	public static NpcStringId ID_14001;
	
	@NpcString(id = 14002, message = "Goodness! I no longer sense a battle there now.")
	public static NpcStringId ID_14002;
	
	@NpcString(id = 14003, message = "Must...Retreat... Too...Strong.")
	public static NpcStringId ID_14003;
	
	@NpcString(id = 14004, message = "%s! How Dare you stop Eranus.... man... die....")
	public static NpcStringId ID_14004;
	
	@NpcString(id = 14005, message = "a,%s purchased a clan item, reducing the Clan Reputation by %s points.")
	public static NpcStringId ID_14005;
	
	@NpcString(id = 14101, message = "Shadow Fox - 3")
	public static NpcStringId ID_14101;
	
	@NpcString(id = 14102, message = "Shadow Fox - 3 (In Progress)")
	public static NpcStringId ID_14102;
	
	@NpcString(id = 14103, message = "Shadow Fox - 3 (Done)")
	public static NpcStringId ID_14103;
	
	@NpcString(id = 14201, message = "Fallen Angel - Request of Dawn")
	public static NpcStringId ID_14201;
	
	@NpcString(id = 14202, message = "Fallen Angel - Request of Dawn (In Progress)")
	public static NpcStringId ID_14202;
	
	@NpcString(id = 14203, message = "Fallen Angel - Request of Dawn (Done)")
	public static NpcStringId ID_14203;
	
	@NpcString(id = 14204, message = "Fallen Angel - Select")
	public static NpcStringId ID_14204;
	
	@NpcString(id = 14301, message = "Fallen Angel - Request of Dusk")
	public static NpcStringId ID_14301;
	
	@NpcString(id = 14302, message = "Fallen Angel - Request of Dusk (In Progress)")
	public static NpcStringId ID_14302;
	
	@NpcString(id = 14303, message = "Fallen Angel - Request of Dusk (Done)")
	public static NpcStringId ID_14303;
	
	@NpcString(id = 14401, message = "Pailaka - Injured Dragon")
	public static NpcStringId ID_14401;
	
	@NpcString(id = 14402, message = "Pailaka - Injured Dragon (In Progress)")
	public static NpcStringId ID_14402;
	
	@NpcString(id = 14403, message = "Pailaka - Injured Dragon (Done)")
	public static NpcStringId ID_14403;
	
	@NpcString(id = 14701, message = "Path to Becoming an Elite Mercenary")
	public static NpcStringId ID_14701;
	
	@NpcString(id = 14702, message = "Path to Becoming an Elite Mercenary (In Progress)")
	public static NpcStringId ID_14702;
	
	@NpcString(id = 14703, message = "Path to Becoming an Elite Mercenary (Done)")
	public static NpcStringId ID_14703;
	
	@NpcString(id = 14801, message = "Path to Becoming an Exalted Mercenary")
	public static NpcStringId ID_14801;
	
	@NpcString(id = 14802, message = "Path to Becoming an Exalted Mercenary (In Progress)")
	public static NpcStringId ID_14802;
	
	@NpcString(id = 14803, message = "Path to Becoming an Exalted Mercenary (Done)")
	public static NpcStringId ID_14803;
	
	@NpcString(id = 15101, message = "Cure for Fever")
	public static NpcStringId ID_15101;
	
	@NpcString(id = 15102, message = "Cure for Fever (In Progress)")
	public static NpcStringId ID_15102;
	
	@NpcString(id = 15103, message = "Cure for Fever (Done)")
	public static NpcStringId ID_15103;
	
	@NpcString(id = 15201, message = "Shards of Golem")
	public static NpcStringId ID_15201;
	
	@NpcString(id = 15202, message = "Shards of Golem (In Progress)")
	public static NpcStringId ID_15202;
	
	@NpcString(id = 15203, message = "Shards of Golem (Done)")
	public static NpcStringId ID_15203;
	
	@NpcString(id = 15301, message = "Deliver Goods")
	public static NpcStringId ID_15301;
	
	@NpcString(id = 15302, message = "Deliver Goods (In Progress)")
	public static NpcStringId ID_15302;
	
	@NpcString(id = 15303, message = "Deliver Goods (Done)")
	public static NpcStringId ID_15303;
	
	@NpcString(id = 15401, message = "Sacrifice to the Sea")
	public static NpcStringId ID_15401;
	
	@NpcString(id = 15402, message = "Sacrifice to the Sea (In Progress)")
	public static NpcStringId ID_15402;
	
	@NpcString(id = 15403, message = "Sacrifice to the Sea (Done)")
	public static NpcStringId ID_15403;
	
	@NpcString(id = 15501, message = "Find Sir Windawood")
	public static NpcStringId ID_15501;
	
	@NpcString(id = 15502, message = "Find Sir Windawood (In Progress)")
	public static NpcStringId ID_15502;
	
	@NpcString(id = 15503, message = "Find Sir Windawood (Done)")
	public static NpcStringId ID_15503;
	
	@NpcString(id = 15601, message = "Millennium Love")
	public static NpcStringId ID_15601;
	
	@NpcString(id = 15602, message = "Millennium Love (In Progress)")
	public static NpcStringId ID_15602;
	
	@NpcString(id = 15603, message = "Millennium Love (Done)")
	public static NpcStringId ID_15603;
	
	@NpcString(id = 15701, message = "Recover Smuggled Goods")
	public static NpcStringId ID_15701;
	
	@NpcString(id = 15702, message = "Recover Smuggled Goods (In Progress)")
	public static NpcStringId ID_15702;
	
	@NpcString(id = 15703, message = "Recover Smuggled Goods (Done)")
	public static NpcStringId ID_15703;
	
	@NpcString(id = 15801, message = "Seed of Evil")
	public static NpcStringId ID_15801;
	
	@NpcString(id = 15802, message = "Seed of Evil (In Progress)")
	public static NpcStringId ID_15802;
	
	@NpcString(id = 15803, message = "Seed of Evil (Done)")
	public static NpcStringId ID_15803;
	
	@NpcString(id = 15804, message = "... How dare you challenge me!")
	public static NpcStringId ID_15804;
	
	@NpcString(id = 15805, message = "The power of Lord Beleth rules the whole world...!")
	public static NpcStringId ID_15805;
	
	@NpcString(id = 15901, message = "Protect the Water Source")
	public static NpcStringId ID_15901;
	
	@NpcString(id = 15902, message = "Protect the Water Source (In Progress)")
	public static NpcStringId ID_15902;
	
	@NpcString(id = 15903, message = "Protect the Water Source (Done)")
	public static NpcStringId ID_15903;
	
	@NpcString(id = 16001, message = "Nerupa's Request")
	public static NpcStringId ID_16001;
	
	@NpcString(id = 16002, message = "Nerupa's Request (In Progress)")
	public static NpcStringId ID_16002;
	
	@NpcString(id = 16003, message = "Nerupa's Request (Done)")
	public static NpcStringId ID_16003;
	
	@NpcString(id = 16101, message = "Fruit of the Mother Tree")
	public static NpcStringId ID_16101;
	
	@NpcString(id = 16102, message = "Fruit of the Mother Tree (In Progress)")
	public static NpcStringId ID_16102;
	
	@NpcString(id = 16103, message = "Fruit of the Mother Tree (Done)")
	public static NpcStringId ID_16103;
	
	@NpcString(id = 16201, message = "Curse of the Fortress")
	public static NpcStringId ID_16201;
	
	@NpcString(id = 16202, message = "Curse of the Fortress (In Progress)")
	public static NpcStringId ID_16202;
	
	@NpcString(id = 16203, message = "Curse of the Fortress (Done)")
	public static NpcStringId ID_16203;
	
	@NpcString(id = 16301, message = "Legacy of the Poet")
	public static NpcStringId ID_16301;
	
	@NpcString(id = 16302, message = "Legacy of the Poet (In Progress)")
	public static NpcStringId ID_16302;
	
	@NpcString(id = 16303, message = "Legacy of the Poet (Done)")
	public static NpcStringId ID_16303;
	
	@NpcString(id = 16401, message = "Blood Fiend")
	public static NpcStringId ID_16401;
	
	@NpcString(id = 16402, message = "Blood Fiend (In Progress)")
	public static NpcStringId ID_16402;
	
	@NpcString(id = 16403, message = "Blood Fiend (Done)")
	public static NpcStringId ID_16403;
	
	@NpcString(id = 16404, message = "I will taste your blood!")
	public static NpcStringId ID_16404;
	
	@NpcString(id = 16405, message = "I have fulfilled my contract with Trader Creamees.")
	public static NpcStringId ID_16405;
	
	@NpcString(id = 16501, message = "Shilen's Hunt")
	public static NpcStringId ID_16501;
	
	@NpcString(id = 16502, message = "Shilen's Hunt (In Progress)")
	public static NpcStringId ID_16502;
	
	@NpcString(id = 16503, message = "Shilen's Hunt (Done)")
	public static NpcStringId ID_16503;
	
	@NpcString(id = 16601, message = "Mass of Darkness")
	public static NpcStringId ID_16601;
	
	@NpcString(id = 16602, message = "Mass of Darkness (In Progress)")
	public static NpcStringId ID_16602;
	
	@NpcString(id = 16603, message = "Mass of Darkness (Done)")
	public static NpcStringId ID_16603;
	
	@NpcString(id = 16701, message = "Dwarven Kinship")
	public static NpcStringId ID_16701;
	
	@NpcString(id = 16702, message = "Dwarven Kinship (In Progress)")
	public static NpcStringId ID_16702;
	
	@NpcString(id = 16703, message = "Dwarven Kinship (Done)")
	public static NpcStringId ID_16703;
	
	@NpcString(id = 16801, message = "Deliver Supplies")
	public static NpcStringId ID_16801;
	
	@NpcString(id = 16802, message = "Deliver Supplies (In Progress)")
	public static NpcStringId ID_16802;
	
	@NpcString(id = 16803, message = "Deliver Supplies (Done)")
	public static NpcStringId ID_16803;
	
	@NpcString(id = 16901, message = "Offspring of Nightmares")
	public static NpcStringId ID_16901;
	
	@NpcString(id = 16902, message = "Offspring of Nightmares (In Progress)")
	public static NpcStringId ID_16902;
	
	@NpcString(id = 16903, message = "Offspring of Nightmares (Done)")
	public static NpcStringId ID_16903;
	
	@NpcString(id = 17001, message = "Dangerous Seduction")
	public static NpcStringId ID_17001;
	
	@NpcString(id = 17002, message = "Dangerous Seduction (In Progress)")
	public static NpcStringId ID_17002;
	
	@NpcString(id = 17003, message = "Dangerous Seduction (Done)")
	public static NpcStringId ID_17003;
	
	@NpcString(id = 17004, message = "I'll cast you into an eternal nightmare!")
	public static NpcStringId ID_17004;
	
	@NpcString(id = 17005, message = "Send my soul to Lich King Icarus...")
	public static NpcStringId ID_17005;
	
	@NpcString(id = 17101, message = "Acts of Evil")
	public static NpcStringId ID_17101;
	
	@NpcString(id = 17102, message = "Acts of Evil (In Progress)")
	public static NpcStringId ID_17102;
	
	@NpcString(id = 17103, message = "Acts of Evil (Done)")
	public static NpcStringId ID_17103;
	
	@NpcString(id = 17151, message = "You should consider going back...")
	public static NpcStringId ID_17151;
	
	@NpcString(id = 17201, message = "New Horizons")
	public static NpcStringId ID_17201;
	
	@NpcString(id = 17202, message = "New Horizons (In Progress)")
	public static NpcStringId ID_17202;
	
	@NpcString(id = 17203, message = "New Horizons (Done)")
	public static NpcStringId ID_17203;
	
	@NpcString(id = 17301, message = "To the Isle of Souls")
	public static NpcStringId ID_17301;
	
	@NpcString(id = 17302, message = "To the Isle of Souls (In Progress)")
	public static NpcStringId ID_17302;
	
	@NpcString(id = 17303, message = "To the Isle of Souls (Done)")
	public static NpcStringId ID_17303;
	
	@NpcString(id = 17401, message = "Supply Check")
	public static NpcStringId ID_17401;
	
	@NpcString(id = 17402, message = "Supply Check (In Progress)")
	public static NpcStringId ID_17402;
	
	@NpcString(id = 17403, message = "Supply Check (Done)")
	public static NpcStringId ID_17403;
	
	@NpcString(id = 17501, message = "The Way of the Warrior")
	public static NpcStringId ID_17501;
	
	@NpcString(id = 17502, message = "The Way of the Warrior (In Progress)")
	public static NpcStringId ID_17502;
	
	@NpcString(id = 17503, message = "The Way of the Warrior (Done)")
	public static NpcStringId ID_17503;
	
	@NpcString(id = 17601, message = "Steps for Honor")
	public static NpcStringId ID_17601;
	
	@NpcString(id = 17602, message = "Steps for Honor (In Progress)")
	public static NpcStringId ID_17602;
	
	@NpcString(id = 17603, message = "Steps for Honor (Done)")
	public static NpcStringId ID_17603;
	
	@NpcString(id = 17801, message = "Iconic Trinity")
	public static NpcStringId ID_17801;
	
	@NpcString(id = 17802, message = "Iconic Trinity (In Progress)")
	public static NpcStringId ID_17802;
	
	@NpcString(id = 17803, message = "Iconic Trinity (Done)")
	public static NpcStringId ID_17803;
	
	@NpcString(id = 17901, message = "Into the Large Cavern")
	public static NpcStringId ID_17901;
	
	@NpcString(id = 17902, message = "Into the Large Cavern (In Progress)")
	public static NpcStringId ID_17902;
	
	@NpcString(id = 17903, message = "Into the Large Cavern (Done)")
	public static NpcStringId ID_17903;
	
	@NpcString(id = 18201, message = "New Recruits")
	public static NpcStringId ID_18201;
	
	@NpcString(id = 18202, message = "New Recruits (In Progress)")
	public static NpcStringId ID_18202;
	
	@NpcString(id = 18203, message = "New Recruits (Done)")
	public static NpcStringId ID_18203;
	
	@NpcString(id = 18301, message = "Relics Exploration")
	public static NpcStringId ID_18301;
	
	@NpcString(id = 18302, message = "Relics Exploration (In Progress)")
	public static NpcStringId ID_18302;
	
	@NpcString(id = 18303, message = "Relics Exploration (Done)")
	public static NpcStringId ID_18303;
	
	@NpcString(id = 18401, message = "Art of Persuasion")
	public static NpcStringId ID_18401;
	
	@NpcString(id = 18402, message = "Art of Persuasion (In Progress)")
	public static NpcStringId ID_18402;
	
	@NpcString(id = 18403, message = "Art of Persuasion (Done)")
	public static NpcStringId ID_18403;
	
	@NpcString(id = 18404, message = "Nikola's Cooperation")
	public static NpcStringId ID_18404;
	
	@NpcString(id = 18451, message = "Intruder Alert! The alarm will self-destruct in 2 minutes.")
	public static NpcStringId ID_18451;
	
	@NpcString(id = 18452, message = "The alarm will self-destruct in 60 seconds. Enter passcode to override.")
	public static NpcStringId ID_18452;
	
	@NpcString(id = 18453, message = "The alarm will self-destruct in 30 seconds. Enter passcode to override.")
	public static NpcStringId ID_18453;
	
	@NpcString(id = 18454, message = "The alarm will self-destruct in 10 seconds. Enter passcode to override.")
	public static NpcStringId ID_18454;
	
	@NpcString(id = 18455, message = "Recorder crushed.")
	public static NpcStringId ID_18455;
	
	@NpcString(id = 18501, message = "Nikola's Cooperation")
	public static NpcStringId ID_18501;
	
	@NpcString(id = 18502, message = "Nikola's Cooperation (In Progress)")
	public static NpcStringId ID_18502;
	
	@NpcString(id = 18503, message = "Nikola's Cooperation (Done)")
	public static NpcStringId ID_18503;
	
	@NpcString(id = 18551, message = "Intruder Alert! The alarm will self-destruct in 2 minutes.")
	public static NpcStringId ID_18551;
	
	@NpcString(id = 18552, message = "The alarm will self-destruct in 60 seconds. Please evacuate immediately!")
	public static NpcStringId ID_18552;
	
	@NpcString(id = 18553, message = "The alarm will self-destruct in 30 seconds. Please evacuate immediately!")
	public static NpcStringId ID_18553;
	
	@NpcString(id = 18554, message = "The alarm will self-destruct in 10 seconds. Please evacuate immediately!")
	public static NpcStringId ID_18554;
	
	@NpcString(id = 18601, message = "Contract Execution")
	public static NpcStringId ID_18601;
	
	@NpcString(id = 18602, message = "Contract Execution (In Progress)")
	public static NpcStringId ID_18602;
	
	@NpcString(id = 18603, message = "Contract Execution (Done)")
	public static NpcStringId ID_18603;
	
	@NpcString(id = 18701, message = "Nikola's Heart")
	public static NpcStringId ID_18701;
	
	@NpcString(id = 18702, message = "Nikola's Heart (In Progress)")
	public static NpcStringId ID_18702;
	
	@NpcString(id = 18703, message = "Nikola's Heart (Done)")
	public static NpcStringId ID_18703;
	
	@NpcString(id = 18801, message = "Seal Removal")
	public static NpcStringId ID_18801;
	
	@NpcString(id = 18802, message = "Seal Removal (In Progress)")
	public static NpcStringId ID_18802;
	
	@NpcString(id = 18803, message = "Seal Removal (Done)")
	public static NpcStringId ID_18803;
	
	@NpcString(id = 18901, message = "Contract Completion")
	public static NpcStringId ID_18901;
	
	@NpcString(id = 18902, message = "Contract Completion (In Progress)")
	public static NpcStringId ID_18902;
	
	@NpcString(id = 18903, message = "Contract Completion (Done)")
	public static NpcStringId ID_18903;
	
	@NpcString(id = 19001, message = "Lost Dream")
	public static NpcStringId ID_19001;
	
	@NpcString(id = 19002, message = "Lost Dream (In Progress)")
	public static NpcStringId ID_19002;
	
	@NpcString(id = 19003, message = "Lost Dream (Done)")
	public static NpcStringId ID_19003;
	
	@NpcString(id = 19101, message = "Vain Conclusion")
	public static NpcStringId ID_19101;
	
	@NpcString(id = 19102, message = "Vain Conclusion (In Progress)")
	public static NpcStringId ID_19102;
	
	@NpcString(id = 19103, message = "Vain Conclusion (Done)")
	public static NpcStringId ID_19103;
	
	@NpcString(id = 19201, message = "Seven Signs, Series of Doubt")
	public static NpcStringId ID_19201;
	
	@NpcString(id = 19202, message = "Seven Signs, Series of Doubt (In Progress)")
	public static NpcStringId ID_19202;
	
	@NpcString(id = 19203, message = "Seven Signs, Series of Doubt (Done)")
	public static NpcStringId ID_19203;
	
	@NpcString(id = 19301, message = "Seven Signs, Dying Message")
	public static NpcStringId ID_19301;
	
	@NpcString(id = 19302, message = "Seven Signs, Dying Message (In Progress)")
	public static NpcStringId ID_19302;
	
	@NpcString(id = 19303, message = "Seven Signs, Dying Message (Done)")
	public static NpcStringId ID_19303;
	
	@NpcString(id = 19304, message = "%s! You are not the owner of that item.")
	public static NpcStringId ID_19304;
	
	@NpcString(id = 19305, message = "Next time, you will not escape!")
	public static NpcStringId ID_19305;
	
	@NpcString(id = 19306, message = "%s! You may have won this time... But next time, I will surely capture you!")
	public static NpcStringId ID_19306;
	
	@NpcString(id = 19401, message = "Seven Signs, Mammon's Contract")
	public static NpcStringId ID_19401;
	
	@NpcString(id = 19402, message = "Seven Signs, Mammon's Contract (In Progress)")
	public static NpcStringId ID_19402;
	
	@NpcString(id = 19403, message = "Seven Signs, Mammon's Contract (Done)")
	public static NpcStringId ID_19403;
	
	@NpcString(id = 19501, message = "Seven Signs, Secret Ritual of the Priests")
	public static NpcStringId ID_19501;
	
	@NpcString(id = 19502, message = "Seven Signs, Secret Ritual of the Priests (In Progress)")
	public static NpcStringId ID_19502;
	
	@NpcString(id = 19503, message = "Seven Signs, Secret Ritual of the Priests (Done)")
	public static NpcStringId ID_19503;
	
	@NpcString(id = 19504, message = "Intruder! Protect the Priests of Dawn!")
	public static NpcStringId ID_19504;
	
	@NpcString(id = 19505, message = "Who are you?! A new face like you can't approach this place!")
	public static NpcStringId ID_19505;
	
	@NpcString(id = 19506, message = "How dare you intrude with that transformation! Get lost!")
	public static NpcStringId ID_19506;
	
	@NpcString(id = 19601, message = "Seven Signs, Seal of the Emperor")
	public static NpcStringId ID_19601;
	
	@NpcString(id = 19602, message = "Seven Signs, Seal of the Emperor (In Progress)")
	public static NpcStringId ID_19602;
	
	@NpcString(id = 19603, message = "Seven Signs, Seal of the Emperor (Done)")
	public static NpcStringId ID_19603;
	
	@NpcString(id = 19701, message = "Seven Signs, the Sacred Book of Seal")
	public static NpcStringId ID_19701;
	
	@NpcString(id = 19702, message = "Seven Signs, the Sacred Book of Seal (In Progress)")
	public static NpcStringId ID_19702;
	
	@NpcString(id = 19703, message = "Seven Signs, the Sacred Book of Seal (Done)")
	public static NpcStringId ID_19703;
	
	@NpcString(id = 19801, message = "Seven Signs, Embryo")
	public static NpcStringId ID_19801;
	
	@NpcString(id = 19802, message = "Seven Signs, Embryo (In Progress)")
	public static NpcStringId ID_19802;
	
	@NpcString(id = 19803, message = "Seven Signs, Embryo (Done)")
	public static NpcStringId ID_19803;
	
	@NpcString(id = 19804, message = "Death to the enemies of the Lords of Dawn!!!")
	public static NpcStringId ID_19804;
	
	@NpcString(id = 20101, message = "Fighter's Tutorial")
	public static NpcStringId ID_20101;
	
	@NpcString(id = 20102, message = "Fighter's Tutorial (In Progress)")
	public static NpcStringId ID_20102;
	
	@NpcString(id = 20103, message = "Fighter's Tutorial (Done)")
	public static NpcStringId ID_20103;
	
	@NpcString(id = 20201, message = "Mystic's Tutorial")
	public static NpcStringId ID_20201;
	
	@NpcString(id = 20202, message = "Mystic's Tutorial (In Progress)")
	public static NpcStringId ID_20202;
	
	@NpcString(id = 20203, message = "Mystic's Tutorial (Done)")
	public static NpcStringId ID_20203;
	
	@NpcString(id = 20301, message = "Elf's Tutorial")
	public static NpcStringId ID_20301;
	
	@NpcString(id = 20302, message = "Elf's Tutorial (In Progress)")
	public static NpcStringId ID_20302;
	
	@NpcString(id = 20303, message = "Elf's Tutorial (Done)")
	public static NpcStringId ID_20303;
	
	@NpcString(id = 20401, message = "Dark Elf's Tutorial")
	public static NpcStringId ID_20401;
	
	@NpcString(id = 20402, message = "Dark Elf's Tutorial (In Progress)")
	public static NpcStringId ID_20402;
	
	@NpcString(id = 20403, message = "Dark Elf's Tutorial (Done)")
	public static NpcStringId ID_20403;
	
	@NpcString(id = 20501, message = "Orc's Tutorial")
	public static NpcStringId ID_20501;
	
	@NpcString(id = 20502, message = "Orc's Tutorial (In Progress)")
	public static NpcStringId ID_20502;
	
	@NpcString(id = 20503, message = "Orc's Tutorial (Done)")
	public static NpcStringId ID_20503;
	
	@NpcString(id = 20601, message = "Dwarf's Tutorial")
	public static NpcStringId ID_20601;
	
	@NpcString(id = 20602, message = "Dwarf's Tutorial (In Progress)")
	public static NpcStringId ID_20602;
	
	@NpcString(id = 20603, message = "Dwarf's Tutorial (Done)")
	public static NpcStringId ID_20603;
	
	@NpcString(id = 21101, message = "Trial of the Challenger")
	public static NpcStringId ID_21101;
	
	@NpcString(id = 21102, message = "Trial of the Challenger (In Progress)")
	public static NpcStringId ID_21102;
	
	@NpcString(id = 21103, message = "Trial of the Challenger (Done)")
	public static NpcStringId ID_21103;
	
	@NpcString(id = 21201, message = "Trial of Duty")
	public static NpcStringId ID_21201;
	
	@NpcString(id = 21202, message = "Trial of Duty (In Progress)")
	public static NpcStringId ID_21202;
	
	@NpcString(id = 21203, message = "Trial of Duty (Done)")
	public static NpcStringId ID_21203;
	
	@NpcString(id = 21301, message = "Trial of the Seeker")
	public static NpcStringId ID_21301;
	
	@NpcString(id = 21302, message = "Trial of the Seeker (In Progress)")
	public static NpcStringId ID_21302;
	
	@NpcString(id = 21303, message = "Trial of the Seeker (Done)")
	public static NpcStringId ID_21303;
	
	@NpcString(id = 21401, message = "Trial of the Scholar")
	public static NpcStringId ID_21401;
	
	@NpcString(id = 21402, message = "Trial of the Scholar (In Progress)")
	public static NpcStringId ID_21402;
	
	@NpcString(id = 21403, message = "Trial of the Scholar (Done)")
	public static NpcStringId ID_21403;
	
	@NpcString(id = 21501, message = "Trial of the Pilgrim")
	public static NpcStringId ID_21501;
	
	@NpcString(id = 21502, message = "Trial of the Pilgrim (In Progress)")
	public static NpcStringId ID_21502;
	
	@NpcString(id = 21503, message = "Trial of the Pilgrim (Done)")
	public static NpcStringId ID_21503;
	
	@NpcString(id = 21601, message = "Trial of the Guildsman")
	public static NpcStringId ID_21601;
	
	@NpcString(id = 21602, message = "Trial of the Guildsman (In Progress)")
	public static NpcStringId ID_21602;
	
	@NpcString(id = 21603, message = "Trial of the Guildsman (Done)")
	public static NpcStringId ID_21603;
	
	@NpcString(id = 21701, message = "Testimony of Trust")
	public static NpcStringId ID_21701;
	
	@NpcString(id = 21702, message = "Testimony of Trust (In Progress)")
	public static NpcStringId ID_21702;
	
	@NpcString(id = 21703, message = "Testimony of Trust (Done)")
	public static NpcStringId ID_21703;
	
	@NpcString(id = 21801, message = "Testimony of Life")
	public static NpcStringId ID_21801;
	
	@NpcString(id = 21802, message = "Testimony of Life (In Progress)")
	public static NpcStringId ID_21802;
	
	@NpcString(id = 21803, message = "Testimony of Life (Done)")
	public static NpcStringId ID_21803;
	
	@NpcString(id = 21901, message = "Testimony of Fate")
	public static NpcStringId ID_21901;
	
	@NpcString(id = 21902, message = "Testimony of Fate (In Progress)")
	public static NpcStringId ID_21902;
	
	@NpcString(id = 21903, message = "Testimony of Fate (Done)")
	public static NpcStringId ID_21903;
	
	@NpcString(id = 22001, message = "Testimony of Glory")
	public static NpcStringId ID_22001;
	
	@NpcString(id = 22002, message = "Testimony of Glory (In Progress)")
	public static NpcStringId ID_22002;
	
	@NpcString(id = 22003, message = "Testimony of Glory (Done)")
	public static NpcStringId ID_22003;
	
	@NpcString(id = 22051, message = "Is it a lackey of Kakai?!")
	public static NpcStringId ID_22051;
	
	@NpcString(id = 22052, message = "Too late!")
	public static NpcStringId ID_22052;
	
	@NpcString(id = 22053, message = "Is it a lackey of Kakai?!")
	public static NpcStringId ID_22053;
	
	@NpcString(id = 22054, message = "Too late!")
	public static NpcStringId ID_22054;
	
	@NpcString(id = 22055, message = "How regretful! Unjust dishonor!")
	public static NpcStringId ID_22055;
	
	@NpcString(id = 22056, message = "I'll get revenge someday!!")
	public static NpcStringId ID_22056;
	
	@NpcString(id = 22057, message = "Indignant and unfair death!")
	public static NpcStringId ID_22057;
	
	@NpcString(id = 22101, message = "Testimony of Prosperity")
	public static NpcStringId ID_22101;
	
	@NpcString(id = 22102, message = "Testimony of Prosperity (In Progress)")
	public static NpcStringId ID_22102;
	
	@NpcString(id = 22103, message = "Testimony of Prosperity (Done)")
	public static NpcStringId ID_22103;
	
	@NpcString(id = 22201, message = "Test of the Duelist")
	public static NpcStringId ID_22201;
	
	@NpcString(id = 22202, message = "Test of the Duelist (In Progress)")
	public static NpcStringId ID_22202;
	
	@NpcString(id = 22203, message = "Test of the Duelist (Done)")
	public static NpcStringId ID_22203;
	
	@NpcString(id = 22301, message = "Test of the Champion")
	public static NpcStringId ID_22301;
	
	@NpcString(id = 22302, message = "Test of the Champion (In Progress)")
	public static NpcStringId ID_22302;
	
	@NpcString(id = 22303, message = "Test of the Champion (Done)")
	public static NpcStringId ID_22303;
	
	@NpcString(id = 22401, message = "Test of Sagittarius")
	public static NpcStringId ID_22401;
	
	@NpcString(id = 22402, message = "Test of Sagittarius (In Progress)")
	public static NpcStringId ID_22402;
	
	@NpcString(id = 22403, message = "Test of Sagittarius (Done)")
	public static NpcStringId ID_22403;
	
	@NpcString(id = 22501, message = "Test of the Searcher")
	public static NpcStringId ID_22501;
	
	@NpcString(id = 22502, message = "Test of the Searcher (In Progress)")
	public static NpcStringId ID_22502;
	
	@NpcString(id = 22503, message = "Test of the Searcher (Done)")
	public static NpcStringId ID_22503;
	
	@NpcString(id = 22601, message = "Test of the Healer")
	public static NpcStringId ID_22601;
	
	@NpcString(id = 22602, message = "Test of the Healer (In Progress)")
	public static NpcStringId ID_22602;
	
	@NpcString(id = 22603, message = "Test of the Healer (Done)")
	public static NpcStringId ID_22603;
	
	@NpcString(id = 22701, message = "Test of the Reformer")
	public static NpcStringId ID_22701;
	
	@NpcString(id = 22702, message = "Test of the Reformer (In Progress)")
	public static NpcStringId ID_22702;
	
	@NpcString(id = 22703, message = "Test of the Reformer (Done)")
	public static NpcStringId ID_22703;
	
	@NpcString(id = 22719, message = "The concealed truth will always be revealed...!")
	public static NpcStringId ID_22719;
	
	@NpcString(id = 22720, message = "Cowardly guy!")
	public static NpcStringId ID_22720;
	
	@NpcString(id = 22801, message = "Test of Magus")
	public static NpcStringId ID_22801;
	
	@NpcString(id = 22802, message = "Test of Magus (In Progress)")
	public static NpcStringId ID_22802;
	
	@NpcString(id = 22803, message = "Test of Magus (Done)")
	public static NpcStringId ID_22803;
	
	@NpcString(id = 22819, message = "I am a tree of nothing... a tree... that knows where to return...")
	public static NpcStringId ID_22819;
	
	@NpcString(id = 22820, message = "I am a creature that shows the truth of the place deep in my heart...")
	public static NpcStringId ID_22820;
	
	@NpcString(id = 22821, message = "I am a mirror of darkness... a virtual image of darkness...")
	public static NpcStringId ID_22821;
	
	@NpcString(id = 22901, message = "Test of Witchcraft")
	public static NpcStringId ID_22901;
	
	@NpcString(id = 22902, message = "Test of Witchcraft (In Progress)")
	public static NpcStringId ID_22902;
	
	@NpcString(id = 22903, message = "Test of Witchcraft (Done)")
	public static NpcStringId ID_22903;
	
	@NpcString(id = 22933, message = "I absolutely cannot give it to you! It is my precious jewel!")
	public static NpcStringId ID_22933;
	
	@NpcString(id = 22934, message = "I'll take your lives later!")
	public static NpcStringId ID_22934;
	
	@NpcString(id = 22935, message = "That sword is really...!")
	public static NpcStringId ID_22935;
	
	@NpcString(id = 22936, message = "No! I haven't completely finished the command for destruction and slaughter yet!!!")
	public static NpcStringId ID_22936;
	
	@NpcString(id = 22937, message = "How dare you wake me!  Now you shall die!")
	public static NpcStringId ID_22937;
	
	@NpcString(id = 23001, message = "Test of the Summoner")
	public static NpcStringId ID_23001;
	
	@NpcString(id = 23002, message = "Test of the Summoner (In Progress)")
	public static NpcStringId ID_23002;
	
	@NpcString(id = 23003, message = "Test of the Summoner (Done)")
	public static NpcStringId ID_23003;
	
	@NpcString(id = 23060, message = "START DUEL")
	public static NpcStringId ID_23060;
	
	@NpcString(id = 23061, message = "RULE VIOLATION")
	public static NpcStringId ID_23061;
	
	@NpcString(id = 23062, message = "I LOSE")
	public static NpcStringId ID_23062;
	
	@NpcString(id = 23063, message = "Whhiisshh!")
	public static NpcStringId ID_23063;
	
	@NpcString(id = 23064, message = "Rule violation!")
	public static NpcStringId ID_23064;
	
	@NpcString(id = 23065, message = "I'm sorry, Lord!")
	public static NpcStringId ID_23065;
	
	@NpcString(id = 23066, message = "Whish! Fight!")
	public static NpcStringId ID_23066;
	
	@NpcString(id = 23067, message = "Rule violation!")
	public static NpcStringId ID_23067;
	
	@NpcString(id = 23068, message = "Lost! Sorry, Lord!")
	public static NpcStringId ID_23068;
	
	@NpcString(id = 23069, message = "START DUEL")
	public static NpcStringId ID_23069;
	
	@NpcString(id = 23070, message = "RULE VIOLATION")
	public static NpcStringId ID_23070;
	
	@NpcString(id = 23071, message = "I LOSE")
	public static NpcStringId ID_23071;
	
	@NpcString(id = 23072, message = "So shall we start?!")
	public static NpcStringId ID_23072;
	
	@NpcString(id = 23073, message = "Rule violation!!!")
	public static NpcStringId ID_23073;
	
	@NpcString(id = 23074, message = "Ugh! I lost...!")
	public static NpcStringId ID_23074;
	
	@NpcString(id = 23075, message = "I'll walk all over you!")
	public static NpcStringId ID_23075;
	
	@NpcString(id = 23076, message = "Rule violation!!!")
	public static NpcStringId ID_23076;
	
	@NpcString(id = 23077, message = "Ugh! Can this be happening?!")
	public static NpcStringId ID_23077;
	
	@NpcString(id = 23078, message = "It's the natural result!")
	public static NpcStringId ID_23078;
	
	@NpcString(id = 23079, message = "Ho, ho! I win!")
	public static NpcStringId ID_23079;
	
	@NpcString(id = 23080, message = "I WIN")
	public static NpcStringId ID_23080;
	
	@NpcString(id = 23081, message = "Whish! I won!")
	public static NpcStringId ID_23081;
	
	@NpcString(id = 23082, message = "Whhiisshh!")
	public static NpcStringId ID_23082;
	
	@NpcString(id = 23083, message = "I WIN")
	public static NpcStringId ID_23083;
	
	@NpcString(id = 23101, message = "Test of the Maestro")
	public static NpcStringId ID_23101;
	
	@NpcString(id = 23102, message = "Test of the Maestro (In Progress)")
	public static NpcStringId ID_23102;
	
	@NpcString(id = 23103, message = "Test of the Maestro (Done)")
	public static NpcStringId ID_23103;
	
	@NpcString(id = 23201, message = "Test of the Lord")
	public static NpcStringId ID_23201;
	
	@NpcString(id = 23202, message = "Test of the Lord (In Progress)")
	public static NpcStringId ID_23202;
	
	@NpcString(id = 23203, message = "Test of the Lord (Done)")
	public static NpcStringId ID_23203;
	
	@NpcString(id = 23301, message = "Test of the War Spirit")
	public static NpcStringId ID_23301;
	
	@NpcString(id = 23302, message = "Test of the War Spirit (In Progress)")
	public static NpcStringId ID_23302;
	
	@NpcString(id = 23303, message = "Test of the War Spirit (Done)")
	public static NpcStringId ID_23303;
	
	@NpcString(id = 23401, message = "Fate's Whisper")
	public static NpcStringId ID_23401;
	
	@NpcString(id = 23402, message = "Fate's Whisper (In Progress)")
	public static NpcStringId ID_23402;
	
	@NpcString(id = 23403, message = "Fate's Whisper (Done)")
	public static NpcStringId ID_23403;
	
	@NpcString(id = 23434, message = "Who dares to try and steal my noble blood?")
	public static NpcStringId ID_23434;
	
	@NpcString(id = 23501, message = "Mimir's Elixir")
	public static NpcStringId ID_23501;
	
	@NpcString(id = 23502, message = "Mimir's Elixir (In Progress)")
	public static NpcStringId ID_23502;
	
	@NpcString(id = 23503, message = "Mimir's Elixir (Done)")
	public static NpcStringId ID_23503;
	
	@NpcString(id = 23601, message = "Seeds of Chaos")
	public static NpcStringId ID_23601;
	
	@NpcString(id = 23602, message = "Seeds of Chaos (In Progress)")
	public static NpcStringId ID_23602;
	
	@NpcString(id = 23603, message = "Seeds of Chaos (Done)")
	public static NpcStringId ID_23603;
	
	@NpcString(id = 23701, message = "Winds of Change")
	public static NpcStringId ID_23701;
	
	@NpcString(id = 23702, message = "Winds of Change (In Progress)")
	public static NpcStringId ID_23702;
	
	@NpcString(id = 23703, message = "Winds of Change (Done)")
	public static NpcStringId ID_23703;
	
	@NpcString(id = 23801, message = "Success/Failure of Business")
	public static NpcStringId ID_23801;
	
	@NpcString(id = 23802, message = "Success/Failure of Business (In Progress)")
	public static NpcStringId ID_23802;
	
	@NpcString(id = 23803, message = "Success/Failure of Business (Done)")
	public static NpcStringId ID_23803;
	
	@NpcString(id = 23901, message = "Won't You Join Us?")
	public static NpcStringId ID_23901;
	
	@NpcString(id = 23902, message = "Won't You Join Us? (In Progress)")
	public static NpcStringId ID_23902;
	
	@NpcString(id = 23903, message = "Won't You Join Us? (Done)")
	public static NpcStringId ID_23903;
	
	@NpcString(id = 24001, message = "I'm the Only One You Can Trust")
	public static NpcStringId ID_24001;
	
	@NpcString(id = 24002, message = "I'm the Only One You Can Trust (In Progress)")
	public static NpcStringId ID_24002;
	
	@NpcString(id = 24003, message = "I'm the Only One You Can Trust (Done)")
	public static NpcStringId ID_24003;
	
	@NpcString(id = 24101, message = "Path of the Noblesse, Precious Soul - 1")
	public static NpcStringId ID_24101;
	
	@NpcString(id = 24102, message = "Path of the Noblesse, Precious Soul - 1 (In Progress)")
	public static NpcStringId ID_24102;
	
	@NpcString(id = 24103, message = "Path of the Noblesse, Precious Soul - 1 (Done)")
	public static NpcStringId ID_24103;
	
	@NpcString(id = 24201, message = "Path of the Noblesse, Precious Soul - 2")
	public static NpcStringId ID_24201;
	
	@NpcString(id = 24202, message = "Path of the Noblesse, Precious Soul - 2 (In Progress)")
	public static NpcStringId ID_24202;
	
	@NpcString(id = 24203, message = "Path of the Noblesse, Precious Soul - 2 (Done)")
	public static NpcStringId ID_24203;
	
	@NpcString(id = 24601, message = "Path of the Noblesse, Precious Soul - 3")
	public static NpcStringId ID_24601;
	
	@NpcString(id = 24602, message = "Path of the Noblesse, Precious Soul - 3 (In Progress)")
	public static NpcStringId ID_24602;
	
	@NpcString(id = 24603, message = "Path of the Noblesse, Precious Soul - 3 (Done)")
	public static NpcStringId ID_24603;
	
	@NpcString(id = 24701, message = "Path of the Noblesse, Precious Soul - 4")
	public static NpcStringId ID_24701;
	
	@NpcString(id = 24702, message = "Path of the Noblesse, Precious Soul - 4 (In Progress)")
	public static NpcStringId ID_24702;
	
	@NpcString(id = 24703, message = "Path of the Noblesse, Precious Soul - 4 (Done)")
	public static NpcStringId ID_24703;
	
	@NpcString(id = 24901, message = "Poisoned Plains of the Lizardmen")
	public static NpcStringId ID_24901;
	
	@NpcString(id = 24902, message = "Poisoned Plains of the Lizardmen (In Progress)")
	public static NpcStringId ID_24902;
	
	@NpcString(id = 24903, message = "Poisoned Plains of the Lizardmen (Done)")
	public static NpcStringId ID_24903;
	
	@NpcString(id = 25001, message = "Watch What You Eat")
	public static NpcStringId ID_25001;
	
	@NpcString(id = 25002, message = "Watch What You Eat (In Progress)")
	public static NpcStringId ID_25002;
	
	@NpcString(id = 25003, message = "Watch What You Eat (Done)")
	public static NpcStringId ID_25003;
	
	@NpcString(id = 25101, message = "No Secrets")
	public static NpcStringId ID_25101;
	
	@NpcString(id = 25102, message = "No Secrets (In Progress)")
	public static NpcStringId ID_25102;
	
	@NpcString(id = 25103, message = "No Secrets (Done)")
	public static NpcStringId ID_25103;
	
	@NpcString(id = 25201, message = "It Smells Delicious!")
	public static NpcStringId ID_25201;
	
	@NpcString(id = 25202, message = "It Smells Delicious! (In Progress)")
	public static NpcStringId ID_25202;
	
	@NpcString(id = 25203, message = "It Smells Delicious! (Done)")
	public static NpcStringId ID_25203;
	
	@NpcString(id = 25401, message = "Legendary Tales")
	public static NpcStringId ID_25401;
	
	@NpcString(id = 25402, message = "Legendary Tales (In Progress)")
	public static NpcStringId ID_25402;
	
	@NpcString(id = 25403, message = "Legendary Tales (Done)")
	public static NpcStringId ID_25403;
	
	@NpcString(id = 25701, message = "The Guard is Busy")
	public static NpcStringId ID_25701;
	
	@NpcString(id = 25702, message = "The Guard is Busy (In Progress)")
	public static NpcStringId ID_25702;
	
	@NpcString(id = 25703, message = "The Guard is Busy (Done)")
	public static NpcStringId ID_25703;
	
	@NpcString(id = 25801, message = "Bring Wolf Pelts")
	public static NpcStringId ID_25801;
	
	@NpcString(id = 25802, message = "Bring Wolf Pelts (In Progress)")
	public static NpcStringId ID_25802;
	
	@NpcString(id = 25901, message = "Rancher's Plea")
	public static NpcStringId ID_25901;
	
	@NpcString(id = 25902, message = "Rancher's Plea (In Progress)")
	public static NpcStringId ID_25902;
	
	@NpcString(id = 26001, message = "Hunt the Orcs")
	public static NpcStringId ID_26001;
	
	@NpcString(id = 26002, message = "Hunt the Orcs (In Progress)")
	public static NpcStringId ID_26002;
	
	@NpcString(id = 26101, message = "Collector's Dream")
	public static NpcStringId ID_26101;
	
	@NpcString(id = 26102, message = "Collector's Dream (In Progress)")
	public static NpcStringId ID_26102;
	
	@NpcString(id = 26201, message = "Trade with the Ivory Tower")
	public static NpcStringId ID_26201;
	
	@NpcString(id = 26202, message = "Trade with the Ivory Tower (In Progress)")
	public static NpcStringId ID_26202;
	
	@NpcString(id = 26301, message = "Orc Subjugation")
	public static NpcStringId ID_26301;
	
	@NpcString(id = 26302, message = "Orc Subjugation (In Progress)")
	public static NpcStringId ID_26302;
	
	@NpcString(id = 26401, message = "Keen Claws")
	public static NpcStringId ID_26401;
	
	@NpcString(id = 26402, message = "Keen Claws (In Progress)")
	public static NpcStringId ID_26402;
	
	@NpcString(id = 26501, message = "Chains of Slavery")
	public static NpcStringId ID_26501;
	
	@NpcString(id = 26502, message = "Chains of Slavery (In Progress)")
	public static NpcStringId ID_26502;
	
	@NpcString(id = 26601, message = "Pleas of Pixies")
	public static NpcStringId ID_26601;
	
	@NpcString(id = 26602, message = "Pleas of Pixies (In Progress)")
	public static NpcStringId ID_26602;
	
	@NpcString(id = 26701, message = "Wrath of Verdure")
	public static NpcStringId ID_26701;
	
	@NpcString(id = 26702, message = "Wrath of Verdure (In Progress)")
	public static NpcStringId ID_26702;
	
	@NpcString(id = 26801, message = "Traces of Evil")
	public static NpcStringId ID_26801;
	
	@NpcString(id = 26802, message = "Traces of Evil (In Progress)")
	public static NpcStringId ID_26802;
	
	@NpcString(id = 26901, message = "Invention Ambition")
	public static NpcStringId ID_26901;
	
	@NpcString(id = 26902, message = "Invention Ambition (In Progress)")
	public static NpcStringId ID_26902;
	
	@NpcString(id = 27001, message = "The One Who Ends Silence")
	public static NpcStringId ID_27001;
	
	@NpcString(id = 27002, message = "The One Who Ends Silence (In Progress)")
	public static NpcStringId ID_27002;
	
	@NpcString(id = 27101, message = "Proof of Valor")
	public static NpcStringId ID_27101;
	
	@NpcString(id = 27102, message = "Proof of Valor (In Progress)")
	public static NpcStringId ID_27102;
	
	@NpcString(id = 27201, message = "Wrath of Ancestors")
	public static NpcStringId ID_27201;
	
	@NpcString(id = 27202, message = "Wrath of Ancestors (In Progress)")
	public static NpcStringId ID_27202;
	
	@NpcString(id = 27301, message = "Invaders of the Holy Land")
	public static NpcStringId ID_27301;
	
	@NpcString(id = 27302, message = "Invaders of the Holy Land (In Progress)")
	public static NpcStringId ID_27302;
	
	@NpcString(id = 27401, message = "Skirmish with the Werewolves")
	public static NpcStringId ID_27401;
	
	@NpcString(id = 27402, message = "Skirmish with the Werewolves (In Progress)")
	public static NpcStringId ID_27402;
	
	@NpcString(id = 27501, message = "Dark Winged Spies")
	public static NpcStringId ID_27501;
	
	@NpcString(id = 27502, message = "Dark Winged Spies (In Progress)")
	public static NpcStringId ID_27502;
	
	@NpcString(id = 27601, message = "Totem of the Hestui")
	public static NpcStringId ID_27601;
	
	@NpcString(id = 27602, message = "Totem of the Hestui (In Progress)")
	public static NpcStringId ID_27602;
	
	@NpcString(id = 27701, message = "Gatekeeper's Offering")
	public static NpcStringId ID_27701;
	
	@NpcString(id = 27702, message = "Gatekeeper's Offering (In Progress)")
	public static NpcStringId ID_27702;
	
	@NpcString(id = 27801, message = "Home Security")
	public static NpcStringId ID_27801;
	
	@NpcString(id = 27802, message = "Home Security (In Progress)")
	public static NpcStringId ID_27802;
	
	@NpcString(id = 27901, message = "Target of Opportunity")
	public static NpcStringId ID_27901;
	
	@NpcString(id = 27902, message = "Target of Opportunity (In Progress)")
	public static NpcStringId ID_27902;
	
	@NpcString(id = 28001, message = "The Food Chain")
	public static NpcStringId ID_28001;
	
	@NpcString(id = 28002, message = "The Food Chain (In Progress)")
	public static NpcStringId ID_28002;
	
	@NpcString(id = 28101, message = "Head for the Hills!")
	public static NpcStringId ID_28101;
	
	@NpcString(id = 28102, message = "Head for the Hills! (In Progress)")
	public static NpcStringId ID_28102;
	
	@NpcString(id = 28301, message = "The Few, The Proud, The Brave")
	public static NpcStringId ID_28301;
	
	@NpcString(id = 28302, message = "The Few, The Proud, The Brave (In Progress)")
	public static NpcStringId ID_28302;
	
	@NpcString(id = 28401, message = "Muertos Hunting")
	public static NpcStringId ID_28401;
	
	@NpcString(id = 28402, message = "Muertos Hunting (In Progress)")
	public static NpcStringId ID_28402;
	
	@NpcString(id = 28601, message = "Fabulous Feathers")
	public static NpcStringId ID_28601;
	
	@NpcString(id = 28602, message = "Fabulous Feathers (In Progress)")
	public static NpcStringId ID_28602;
	
	@NpcString(id = 28701, message = "Figuring It Out!")
	public static NpcStringId ID_28701;
	
	@NpcString(id = 28702, message = "Figuring It Out! (In Progress)")
	public static NpcStringId ID_28702;
	
	@NpcString(id = 28801, message = "Handle With Care")
	public static NpcStringId ID_28801;
	
	@NpcString(id = 28802, message = "Handle With Care (In Progress)")
	public static NpcStringId ID_28802;
	
	@NpcString(id = 28901, message = "No More Soup For You")
	public static NpcStringId ID_28901;
	
	@NpcString(id = 28902, message = "No More Soup For You (In Progress)")
	public static NpcStringId ID_28902;
	
	@NpcString(id = 29001, message = "Threat Removal")
	public static NpcStringId ID_29001;
	
	@NpcString(id = 29002, message = "Threat Removal (In Progress)")
	public static NpcStringId ID_29002;
	
	@NpcString(id = 29101, message = "Revenge of the Redbonnet")
	public static NpcStringId ID_29101;
	
	@NpcString(id = 29102, message = "Revenge of the Redbonnet (In Progress)")
	public static NpcStringId ID_29102;
	
	@NpcString(id = 29201, message = "Brigands Sweep")
	public static NpcStringId ID_29201;
	
	@NpcString(id = 29202, message = "Brigands Sweep (In Progress)")
	public static NpcStringId ID_29202;
	
	@NpcString(id = 29301, message = "The Hidden Veins")
	public static NpcStringId ID_29301;
	
	@NpcString(id = 29302, message = "The Hidden Veins (In Progress)")
	public static NpcStringId ID_29302;
	
	@NpcString(id = 29401, message = "Covert Business")
	public static NpcStringId ID_29401;
	
	@NpcString(id = 29402, message = "Covert Business (In Progress)")
	public static NpcStringId ID_29402;
	
	@NpcString(id = 29501, message = "Dreaming of the Skies")
	public static NpcStringId ID_29501;
	
	@NpcString(id = 29502, message = "Dreaming of the Skies (In Progress)")
	public static NpcStringId ID_29502;
	
	@NpcString(id = 29601, message = "Tarantula's Spider Silk")
	public static NpcStringId ID_29601;
	
	@NpcString(id = 29602, message = "Tarantula's Spider Silk (In Progress)")
	public static NpcStringId ID_29602;
	
	@NpcString(id = 29701, message = "Gatekeeper's Favor")
	public static NpcStringId ID_29701;
	
	@NpcString(id = 29702, message = "Gatekeeper's Favor (In Progress)")
	public static NpcStringId ID_29702;
	
	@NpcString(id = 29801, message = "Lizardmen's Conspiracy")
	public static NpcStringId ID_29801;
	
	@NpcString(id = 29802, message = "Lizardmen's Conspiracy (In Progress)")
	public static NpcStringId ID_29802;
	
	@NpcString(id = 29901, message = "Gather Ingredients for Pie")
	public static NpcStringId ID_29901;
	
	@NpcString(id = 29902, message = "Gather Ingredients for Pie (In Progress)")
	public static NpcStringId ID_29902;
	
	@NpcString(id = 30001, message = "Hunting Leto Lizardman")
	public static NpcStringId ID_30001;
	
	@NpcString(id = 30002, message = "Hunting Leto Lizardman (In Progress)")
	public static NpcStringId ID_30002;
	
	@NpcString(id = 30301, message = "Collect Arrowheads")
	public static NpcStringId ID_30301;
	
	@NpcString(id = 30302, message = "Collect Arrowheads (In Progress)")
	public static NpcStringId ID_30302;
	
	@NpcString(id = 30601, message = "Crystals of Fire and Ice")
	public static NpcStringId ID_30601;
	
	@NpcString(id = 30602, message = "Crystals of Fire and Ice (In Progress)")
	public static NpcStringId ID_30602;
	
	@NpcString(id = 30701, message = "Control Device of the Giants")
	public static NpcStringId ID_30701;
	
	@NpcString(id = 30702, message = "Control Device of the Giants (In Progress)")
	public static NpcStringId ID_30702;
	
	@NpcString(id = 30801, message = "Reed Field Maintenance")
	public static NpcStringId ID_30801;
	
	@NpcString(id = 30802, message = "Reed Field Maintenance (In Progress)")
	public static NpcStringId ID_30802;
	
	@NpcString(id = 30901, message = "For a Good Cause")
	public static NpcStringId ID_30901;
	
	@NpcString(id = 30902, message = "For a Good Cause (In Progress)")
	public static NpcStringId ID_30902;
	
	@NpcString(id = 31001, message = "Only What Remains")
	public static NpcStringId ID_31001;
	
	@NpcString(id = 31002, message = "Only What Remains (In Progress)")
	public static NpcStringId ID_31002;
	
	@NpcString(id = 31101, message = "Expulsion of Evil Spirits")
	public static NpcStringId ID_31101;
	
	@NpcString(id = 31102, message = "Expulsion of Evil Spirits (In Progress)")
	public static NpcStringId ID_31102;
	
	@NpcString(id = 31201, message = "Take Advantage of the Crisis!")
	public static NpcStringId ID_31201;
	
	@NpcString(id = 31202, message = "Take Advantage of the Crisis! (In Progress)")
	public static NpcStringId ID_31202;
	
	@NpcString(id = 31301, message = "Collect Spores")
	public static NpcStringId ID_31301;
	
	@NpcString(id = 31302, message = "Collect Spores (In Progress)")
	public static NpcStringId ID_31302;
	
	@NpcString(id = 31601, message = "Destroy Plague Carriers")
	public static NpcStringId ID_31601;
	
	@NpcString(id = 31602, message = "Destroy Plague Carriers (In Progress)")
	public static NpcStringId ID_31602;
	
	@NpcString(id = 31603, message = "Why do you oppress us so?")
	public static NpcStringId ID_31603;
	
	@NpcString(id = 31701, message = "Catch the Wind")
	public static NpcStringId ID_31701;
	
	@NpcString(id = 31702, message = "Catch the Wind (In Progress)")
	public static NpcStringId ID_31702;
	
	@NpcString(id = 31901, message = "Scent of Death")
	public static NpcStringId ID_31901;
	
	@NpcString(id = 31902, message = "Scent of Death (In Progress)")
	public static NpcStringId ID_31902;
	
	@NpcString(id = 32001, message = "Bones Tell the Future")
	public static NpcStringId ID_32001;
	
	@NpcString(id = 32002, message = "Bones Tell the Future (In Progress)")
	public static NpcStringId ID_32002;
	
	@NpcString(id = 32401, message = "Sweetest Venom")
	public static NpcStringId ID_32401;
	
	@NpcString(id = 32402, message = "Sweetest Venom (In Progress)")
	public static NpcStringId ID_32402;
	
	@NpcString(id = 32501, message = "Grim Collector")
	public static NpcStringId ID_32501;
	
	@NpcString(id = 32502, message = "Grim Collector (In Progress)")
	public static NpcStringId ID_32502;
	
	@NpcString(id = 32601, message = "Vanquish Remnants")
	public static NpcStringId ID_32601;
	
	@NpcString(id = 32602, message = "Vanquish Remnants (In Progress)")
	public static NpcStringId ID_32602;
	
	@NpcString(id = 32701, message = "Recover the Farmland")
	public static NpcStringId ID_32701;
	
	@NpcString(id = 32702, message = "Recover the Farmland (In Progress)")
	public static NpcStringId ID_32702;
	
	@NpcString(id = 32801, message = "Sense for Business")
	public static NpcStringId ID_32801;
	
	@NpcString(id = 32802, message = "Sense for Business (In Progress)")
	public static NpcStringId ID_32802;
	
	@NpcString(id = 32901, message = "Curiosity of a Dwarf")
	public static NpcStringId ID_32901;
	
	@NpcString(id = 32902, message = "Curiosity of a Dwarf (In Progress)")
	public static NpcStringId ID_32902;
	
	@NpcString(id = 33001, message = "Adept of Taste")
	public static NpcStringId ID_33001;
	
	@NpcString(id = 33002, message = "Adept of Taste (In Progress)")
	public static NpcStringId ID_33002;
	
	@NpcString(id = 33101, message = "Arrow of Vengeance")
	public static NpcStringId ID_33101;
	
	@NpcString(id = 33102, message = "Arrow of Vengeance (In Progress)")
	public static NpcStringId ID_33102;
	
	@NpcString(id = 33301, message = "Hunt of the Black Lion")
	public static NpcStringId ID_33301;
	
	@NpcString(id = 33302, message = "Hunt of the Black Lion (In Progress)")
	public static NpcStringId ID_33302;
	
	@NpcString(id = 33401, message = "The Wishing Potion")
	public static NpcStringId ID_33401;
	
	@NpcString(id = 33402, message = "The Wishing Potion (In Progress)")
	public static NpcStringId ID_33402;
	
	@NpcString(id = 33403, message = "The Wishing Potion (Done)")
	public static NpcStringId ID_33403;
	
	@NpcString(id = 33409, message = "Don't interrupt my rest again")
	public static NpcStringId ID_33409;
	
	@NpcString(id = 33410, message = "You're a great devil now...")
	public static NpcStringId ID_33410;
	
	@NpcString(id = 33411, message = "Oh, it's not an opponent of mine. Ha, ha, ha!")
	public static NpcStringId ID_33411;
	
	@NpcString(id = 33412, message = "Oh... Great Demon King...")
	public static NpcStringId ID_33412;
	
	@NpcString(id = 33413, message = "Revenge is Overlord Ramsebalius of the evil world!")
	public static NpcStringId ID_33413;
	
	@NpcString(id = 33414, message = "Bonaparterius, Abyss King, will punish you")
	public static NpcStringId ID_33414;
	
	@NpcString(id = 33415, message = "OK, everybody pray fervently!")
	public static NpcStringId ID_33415;
	
	@NpcString(id = 33416, message = "Both hands to heaven! Everybody yell together!")
	public static NpcStringId ID_33416;
	
	@NpcString(id = 33417, message = "One! Two! May your dreams come true!")
	public static NpcStringId ID_33417;
	
	@NpcString(id = 33418, message = "Who killed my underling devil?")
	public static NpcStringId ID_33418;
	
	@NpcString(id = 33420, message = "I will make your love come true~ love, love, love~")
	public static NpcStringId ID_33420;
	
	@NpcString(id = 33421, message = "I have wisdom in me. I am the box of wisdom!")
	public static NpcStringId ID_33421;
	
	@NpcString(id = 33422, message = "Oh, oh, oh!")
	public static NpcStringId ID_33422;
	
	@NpcString(id = 33423, message = "Do you want us to love you? Oh.")
	public static NpcStringId ID_33423;
	
	@NpcString(id = 33424, message = "Who is calling the Lord of Darkness!")
	public static NpcStringId ID_33424;
	
	@NpcString(id = 33425, message = "I am a great empire, Bonaparterius!")
	public static NpcStringId ID_33425;
	
	@NpcString(id = 33426, message = "Let your head down before the Lord!")
	public static NpcStringId ID_33426;
	
	@NpcString(id = 33501, message = "The Song of the Hunter")
	public static NpcStringId ID_33501;
	
	@NpcString(id = 33502, message = "The Song of the Hunter  (In Progress)")
	public static NpcStringId ID_33502;
	
	@NpcString(id = 33511, message = "We'll take the property of the ancient empire!")
	public static NpcStringId ID_33511;
	
	@NpcString(id = 33512, message = "Show me the pretty sparkling things! They're all mine!")
	public static NpcStringId ID_33512;
	
	@NpcString(id = 33513, message = "Pretty good!")
	public static NpcStringId ID_33513;
	
	@NpcString(id = 33520, message = "<a action=\"bypass -h menu_select?ask=335&reply=5\">C: 40 Totems of Kadesh</a><br>")
	public static NpcStringId ID_33520;
	
	@NpcString(id = 33521, message = "<a action=\"bypass -h menu_select?ask=335&reply=6\">C: 50 Jade Necklaces of Timak</a><br>")
	public static NpcStringId ID_33521;
	
	@NpcString(id = 33522, message = "<a action=\"bypass -h menu_select?ask=335&reply=7\">C: 50 Enchanted Golem Shards</a><br>")
	public static NpcStringId ID_33522;
	
	@NpcString(id = 33523, message = "<a action=\"bypass -h menu_select?ask=335&reply=8\">C: 30 Pieces Monster Eye Meat</a><br>")
	public static NpcStringId ID_33523;
	
	@NpcString(id = 33524, message = "<a action=\"bypass -h menu_select?ask=335&reply=9\">C: 40 Eggs of Dire Wyrm</a><br>")
	public static NpcStringId ID_33524;
	
	@NpcString(id = 33525, message = "<a action=\"bypass -h menu_select?ask=335&reply=10\">C: 100 Claws of Guardian Basilisk</a><br>")
	public static NpcStringId ID_33525;
	
	@NpcString(id = 33526, message = "<a action=\"bypass -h menu_select?ask=335&reply=11\">C: 50 Revenant Chains </a><br>")
	public static NpcStringId ID_33526;
	
	@NpcString(id = 33527, message = "<a action=\"bypass -h menu_select?ask=335&reply=12\">C: 30 Windsus Tusks</a><br>")
	public static NpcStringId ID_33527;
	
	@NpcString(id = 33528, message = "<a action=\"bypass -h menu_select?ask=335&reply=13\">C: 100 Skulls of Grandis</a><br>")
	public static NpcStringId ID_33528;
	
	@NpcString(id = 33529, message = "<a action=\"bypass -h menu_select?ask=335&reply=14\">C: 50 Taik Obsidian Amulets</a><br>")
	public static NpcStringId ID_33529;
	
	@NpcString(id = 33530, message = "<a action=\"bypass -h menu_select?ask=335&reply=15\">C: 30 Heads of Karul Bugbear</a><br>")
	public static NpcStringId ID_33530;
	
	@NpcString(id = 33531, message = "<a action=\"bypass -h menu_select?ask=335&reply=16\">C: 40 Ivory Charms of Tamlin</a><br>")
	public static NpcStringId ID_33531;
	
	@NpcString(id = 33532, message = "<a action=\"bypass -h menu_select?ask=335&reply=17\">B: Situation Preparation - Leto Chief</a><br>")
	public static NpcStringId ID_33532;
	
	@NpcString(id = 33533, message = "<a action=\"bypass -h menu_select?ask=335&reply=18\">B: 50 Enchanted Gargoyle Horns</a><br>")
	public static NpcStringId ID_33533;
	
	@NpcString(id = 33534, message = "<a action=\"bypass -h menu_select?ask=335&reply=19\">B: 50 Coiled Serpent Totems</a><br>")
	public static NpcStringId ID_33534;
	
	@NpcString(id = 33535, message = "<a action=\"bypass -h menu_select?ask=335&reply=20\">B: Situation Preparation - Sorcerer Catch of Leto</a><br>")
	public static NpcStringId ID_33535;
	
	@NpcString(id = 33536, message = "<a action=\"bypass -h menu_select?ask=335&reply=21\">B: Situation Preparation - Timak Raider Kaikee</a><br>")
	public static NpcStringId ID_33536;
	
	@NpcString(id = 33537, message = "<a action=\"bypass -h menu_select?ask=335&reply=22\">B: 30 Kronbe Venom Sacs</a><br>")
	public static NpcStringId ID_33537;
	
	@NpcString(id = 33538, message = "<a action=\"bypass -h menu_select?ask=335&reply=23\">A: 30 Charms of Eva</a><br>")
	public static NpcStringId ID_33538;
	
	@NpcString(id = 33539, message = "<a action=\"bypass -h menu_select?ask=335&reply=24\">A: Titan's Tablet</a><br>")
	public static NpcStringId ID_33539;
	
	@NpcString(id = 33540, message = "<a action=\"bypass -h menu_select?ask=335&reply=25\">A: Book of Shunaiman</a><br>")
	public static NpcStringId ID_33540;
	
	@NpcString(id = 33541, message = "<a action=\"bypass -h menu_select?ask=335&reply=26\">C: 40 Rotted Tree Spores</a><br>")
	public static NpcStringId ID_33541;
	
	@NpcString(id = 33542, message = "<a action=\"bypass -h menu_select?ask=335&reply=27\">C: 40 Trisalim Venom Sacs</a><br>")
	public static NpcStringId ID_33542;
	
	@NpcString(id = 33543, message = "<a action=\"bypass -h menu_select?ask=335&reply=28\">C: 50 Totems of Taik Orc</a><br>")
	public static NpcStringId ID_33543;
	
	@NpcString(id = 33544, message = "<a action=\"bypass -h menu_select?ask=335&reply=29\">C: 40 Harit Barbed Necklaces</a><br>")
	public static NpcStringId ID_33544;
	
	@NpcString(id = 33545, message = "<a action=\"bypass -h menu_select?ask=335&reply=30\">C: 20 Coins of Ancient Empire</a><br>")
	public static NpcStringId ID_33545;
	
	@NpcString(id = 33546, message = "<a action=\"bypass -h menu_select?ask=335&reply=31\">C: 30 Skins of Farkran</a><br>")
	public static NpcStringId ID_33546;
	
	@NpcString(id = 33547, message = "<a action=\"bypass -h menu_select?ask=335&reply=32\">C: 40 Tempest Shards</a><br>")
	public static NpcStringId ID_33547;
	
	@NpcString(id = 33548, message = "<a action=\"bypass -h menu_select?ask=335&reply=36\">C: 30 Vanor Silenos Manes</a><br>")
	public static NpcStringId ID_33548;
	
	@NpcString(id = 33549, message = "<a action=\"bypass -h menu_select?ask=335&reply=34\">C: 40 Manes of Pan Ruem</a><br>")
	public static NpcStringId ID_33549;
	
	@NpcString(id = 33550, message = "<a action=\"bypass -h menu_select?ask=335&reply=35\">C: hamadryad shards</a><br>")
	public static NpcStringId ID_33550;
	
	@NpcString(id = 33551, message = "<a action=\"bypass -h menu_select?ask=335&reply=36\">C: 30 Manes of Vanor Silenos</a><br>")
	public static NpcStringId ID_33551;
	
	@NpcString(id = 33552, message = "<a action=\"bypass -h menu_select?ask=335&reply=37\">C: 30 Totems of Talk Bugbears</a><br>")
	public static NpcStringId ID_33552;
	
	@NpcString(id = 33553, message = "<a action=\"bypass -h menu_select?ask=335&reply=38\">B: Situation Preparation - Overlord Okun of Timak</a><br>")
	public static NpcStringId ID_33553;
	
	@NpcString(id = 33554, message = "<a action=\"bypass -h menu_select?ask=335&reply=39\">B: Situation Preparation - Overlord Kakran of Taik</a><br>")
	public static NpcStringId ID_33554;
	
	@NpcString(id = 33555, message = "<a action=\"bypass -h menu_select?ask=335&reply=40\">B: 40 Narcissus Soulstones</a><br>")
	public static NpcStringId ID_33555;
	
	@NpcString(id = 33556, message = "<a action=\"bypass -h menu_select?ask=335&reply=41\">B: 20 Eyes of Deprived</a><br>")
	public static NpcStringId ID_33556;
	
	@NpcString(id = 33557, message = "<a action=\"bypass -h menu_select?ask=335&reply=42\">B: 20 Unicorn Horns</a><br>")
	public static NpcStringId ID_33557;
	
	@NpcString(id = 33558, message = "<a action=\"bypass -h menu_select?ask=335&reply=43\">B: Kerunos's Gold Mane</a><br>")
	public static NpcStringId ID_33558;
	
	@NpcString(id = 33601, message = "Coins of Magic")
	public static NpcStringId ID_33601;
	
	@NpcString(id = 33602, message = "Coins of Magic (In Progress)")
	public static NpcStringId ID_33602;
	
	@NpcString(id = 33701, message = "Audience with the Land Dragon")
	public static NpcStringId ID_33701;
	
	@NpcString(id = 33702, message = "Audience with the Land Dragon (In Progress)")
	public static NpcStringId ID_33702;
	
	@NpcString(id = 33801, message = "Alligator Hunter")
	public static NpcStringId ID_33801;
	
	@NpcString(id = 33802, message = "Alligator Hunter (In Progress)")
	public static NpcStringId ID_33802;
	
	@NpcString(id = 34001, message = "Subjugation of Lizardmen")
	public static NpcStringId ID_34001;
	
	@NpcString(id = 34002, message = "Subjugation of Lizardmen (In Progress)")
	public static NpcStringId ID_34002;
	
	@NpcString(id = 34101, message = "Hunting for Wild Beasts")
	public static NpcStringId ID_34101;
	
	@NpcString(id = 34102, message = "Hunting for Wild Beasts (In Progress)")
	public static NpcStringId ID_34102;
	
	@NpcString(id = 34201, message = "Subjugation of Lizardmen")
	public static NpcStringId ID_34201;
	
	@NpcString(id = 34202, message = "Subjugation of Lizardmen (In Progress)")
	public static NpcStringId ID_34202;
	
	@NpcString(id = 34203, message = "Subjugation of Lizardmen (Done)")
	public static NpcStringId ID_34203;
	
	@NpcString(id = 34301, message = "Under the Shadow of the Ivory Tower")
	public static NpcStringId ID_34301;
	
	@NpcString(id = 34302, message = "Under the Shadow of the Ivory Tower (In Progress)")
	public static NpcStringId ID_34302;
	
	@NpcString(id = 34401, message = "1000 Years, the End of Lamentation")
	public static NpcStringId ID_34401;
	
	@NpcString(id = 34402, message = "1000 Years, the End of Lamentation (In Progress)")
	public static NpcStringId ID_34402;
	
	@NpcString(id = 34501, message = "Method to Raise the Dead")
	public static NpcStringId ID_34501;
	
	@NpcString(id = 34502, message = "Method to Raise the Dead (In Progress)")
	public static NpcStringId ID_34502;
	
	@NpcString(id = 34701, message = "Go Get the Calculator")
	public static NpcStringId ID_34701;
	
	@NpcString(id = 34702, message = "Go Get the Calculator (In Progress)")
	public static NpcStringId ID_34702;
	
	@NpcString(id = 34801, message = "An Arrogant Search")
	public static NpcStringId ID_34801;
	
	@NpcString(id = 34802, message = "An Arrogant Search (In Progress)")
	public static NpcStringId ID_34802;
	
	@NpcString(id = 34830, message = "Ha, that was fun! If you wish to find the key, search the corpse.")
	public static NpcStringId ID_34830;
	
	@NpcString(id = 34831, message = "I have the key. Why don't you come and take it?")
	public static NpcStringId ID_34831;
	
	@NpcString(id = 34832, message = "You fools will get what's coming to you!")
	public static NpcStringId ID_34832;
	
	@NpcString(id = 34833, message = "Sorry about this, but I must kill you now.")
	public static NpcStringId ID_34833;
	
	@NpcString(id = 34834, message = "I have the key. Why don't you come and take it?")
	public static NpcStringId ID_34834;
	
	@NpcString(id = 34835, message = "You guys wouldn't know... the seven seals are...  Arrrgh!")
	public static NpcStringId ID_34835;
	
	@NpcString(id = 34836, message = "I shall drench this mountain with your blood!")
	public static NpcStringId ID_34836;
	
	@NpcString(id = 34837, message = "That doesn't belong to you. Don't touch it!")
	public static NpcStringId ID_34837;
	
	@NpcString(id = 34838, message = "Get out of my sight, you infidels!")
	public static NpcStringId ID_34838;
	
	@NpcString(id = 34839, message = "We don't have any further business to discuss... Have you searched the corpse for the key?")
	public static NpcStringId ID_34839;
	
	@NpcString(id = 35001, message = "Enhance Your Weapon")
	public static NpcStringId ID_35001;
	
	@NpcString(id = 35002, message = "Enhance Your Weapon (In Progress)")
	public static NpcStringId ID_35002;
	
	@NpcString(id = 35051, message = "%s has earned a stage %s Blue Soul Crystal.")
	public static NpcStringId ID_35051;
	
	@NpcString(id = 35052, message = "%s has earned a stage %s Red Soul Crystal.")
	public static NpcStringId ID_35052;
	
	@NpcString(id = 35053, message = "%s has earned a stage %s Green Soul Crystal.")
	public static NpcStringId ID_35053;
	
	@NpcString(id = 35101, message = "Black Swan")
	public static NpcStringId ID_35101;
	
	@NpcString(id = 35102, message = "Black Swan (In Progress)")
	public static NpcStringId ID_35102;
	
	@NpcString(id = 35201, message = "Help Rood Raise A New Pet!")
	public static NpcStringId ID_35201;
	
	@NpcString(id = 35202, message = "Help Rood Raise A New Pet! (In Progress)")
	public static NpcStringId ID_35202;
	
	@NpcString(id = 35301, message = "Power of Darkness")
	public static NpcStringId ID_35301;
	
	@NpcString(id = 35302, message = "Power of Darkness (In Progress)")
	public static NpcStringId ID_35302;
	
	@NpcString(id = 35401, message = "Conquest of Alligator Island")
	public static NpcStringId ID_35401;
	
	@NpcString(id = 35402, message = "Conquest of Alligator Island (In Progress)")
	public static NpcStringId ID_35402;
	
	@NpcString(id = 35501, message = "Family Honor")
	public static NpcStringId ID_35501;
	
	@NpcString(id = 35502, message = "Family Honor (In Progress)")
	public static NpcStringId ID_35502;
	
	@NpcString(id = 35601, message = "Dig Up the Sea of Spores!")
	public static NpcStringId ID_35601;
	
	@NpcString(id = 35602, message = "Dig Up the Sea of Spores! (In Progress)")
	public static NpcStringId ID_35602;
	
	@NpcString(id = 35701, message = "Warehouse Keeper's Ambition")
	public static NpcStringId ID_35701;
	
	@NpcString(id = 35702, message = "Warehouse Keeper's Ambition (In Progress)")
	public static NpcStringId ID_35702;
	
	@NpcString(id = 35801, message = "Illegitimate Child of a Goddess")
	public static NpcStringId ID_35801;
	
	@NpcString(id = 35802, message = "Illegitimate Child of a Goddess (In Progress)")
	public static NpcStringId ID_35802;
	
	@NpcString(id = 35901, message = "For a Sleepless Deadman")
	public static NpcStringId ID_35901;
	
	@NpcString(id = 35902, message = "For a Sleepless Deadman (In Progress)")
	public static NpcStringId ID_35902;
	
	@NpcString(id = 36001, message = "Plunder the Supplies")
	public static NpcStringId ID_36001;
	
	@NpcString(id = 36002, message = "Plunder the Supplies (In Progress)")
	public static NpcStringId ID_36002;
	
	@NpcString(id = 36101, message = "Plunder Their Supplies")
	public static NpcStringId ID_36101;
	
	@NpcString(id = 36201, message = "Bard's Mandolin")
	public static NpcStringId ID_36201;
	
	@NpcString(id = 36202, message = "Bard's Mandolin (In Progress)")
	public static NpcStringId ID_36202;
	
	@NpcString(id = 36301, message = "Sorrowful Sound of Flute")
	public static NpcStringId ID_36301;
	
	@NpcString(id = 36302, message = "Sorrowful Sound of Flute (In Progress)")
	public static NpcStringId ID_36302;
	
	@NpcString(id = 36401, message = "Jovial Accordion")
	public static NpcStringId ID_36401;
	
	@NpcString(id = 36402, message = "Jovial Accordion (In Progress)")
	public static NpcStringId ID_36402;
	
	@NpcString(id = 36501, message = "Demon's Legacy")
	public static NpcStringId ID_36501;
	
	@NpcString(id = 36502, message = "Demon's Legacy (In Progress)")
	public static NpcStringId ID_36502;
	
	@NpcString(id = 36601, message = "Silver Haired Shaman")
	public static NpcStringId ID_36601;
	
	@NpcString(id = 36602, message = "Silver Haired Shaman (In Progress)")
	public static NpcStringId ID_36602;
	
	@NpcString(id = 36701, message = "Electrifying Recharge!")
	public static NpcStringId ID_36701;
	
	@NpcString(id = 36702, message = "Electrifying Recharge! (In Progress)")
	public static NpcStringId ID_36702;
	
	@NpcString(id = 36801, message = "Trespassing into the Holy Ground")
	public static NpcStringId ID_36801;
	
	@NpcString(id = 36802, message = "Trespassing into the Holy Ground (In Progress)")
	public static NpcStringId ID_36802;
	
	@NpcString(id = 36901, message = "Collector of Jewels")
	public static NpcStringId ID_36901;
	
	@NpcString(id = 36902, message = "Collector of Jewels (In Progress)")
	public static NpcStringId ID_36902;
	
	@NpcString(id = 37001, message = "An Elder Sows Seeds")
	public static NpcStringId ID_37001;
	
	@NpcString(id = 37002, message = "An Elder Sows Seeds (In Progress)")
	public static NpcStringId ID_37002;
	
	@NpcString(id = 37101, message = "Shrieks of Ghosts")
	public static NpcStringId ID_37101;
	
	@NpcString(id = 37102, message = "Shrieks of Ghosts")
	public static NpcStringId ID_37102;
	
	@NpcString(id = 37201, message = "Legacy of Insolence")
	public static NpcStringId ID_37201;
	
	@NpcString(id = 37202, message = "Legacy of Insolence (In Progress)")
	public static NpcStringId ID_37202;
	
	@NpcString(id = 37301, message = "Supplier of Reagents")
	public static NpcStringId ID_37301;
	
	@NpcString(id = 37302, message = "Supplier of Reagents (In Progress)")
	public static NpcStringId ID_37302;
	
	@NpcString(id = 37401, message = "Whisper of Dreams - Part 1")
	public static NpcStringId ID_37401;
	
	@NpcString(id = 37402, message = "Whisper of Dreams - Part 1 (In Progress)")
	public static NpcStringId ID_37402;
	
	@NpcString(id = 37501, message = "Whisper of Dreams - Part 2")
	public static NpcStringId ID_37501;
	
	@NpcString(id = 37502, message = "Whisper of Dreams - Part 2 (In Progress)")
	public static NpcStringId ID_37502;
	
	@NpcString(id = 37601, message = "Exploration of the Giants' Cave - Part 1")
	public static NpcStringId ID_37601;
	
	@NpcString(id = 37602, message = "Exploration of the Giants' Cave - Part 1 (In Progress)")
	public static NpcStringId ID_37602;
	
	@NpcString(id = 37701, message = "Exploration of the Giants' Cave - Part 2")
	public static NpcStringId ID_37701;
	
	@NpcString(id = 37702, message = "Exploration of the Giants' Cave - Part 2 (In Progress)")
	public static NpcStringId ID_37702;
	
	@NpcString(id = 37801, message = "Grand Feast")
	public static NpcStringId ID_37801;
	
	@NpcString(id = 37802, message = "Grand Feast (In Progress)")
	public static NpcStringId ID_37802;
	
	@NpcString(id = 37901, message = "Fantasy Wine")
	public static NpcStringId ID_37901;
	
	@NpcString(id = 37902, message = "Fantasy Wine (In Progress)")
	public static NpcStringId ID_37902;
	
	@NpcString(id = 38001, message = "Bring Out the Flavor of Ingredients!")
	public static NpcStringId ID_38001;
	
	@NpcString(id = 38002, message = "Bring Out the Flavor of Ingredients! (In Progress)")
	public static NpcStringId ID_38002;
	
	@NpcString(id = 38101, message = "Let's Become a Royal Member!")
	public static NpcStringId ID_38101;
	
	@NpcString(id = 38102, message = "Let's Become a Royal Member! (In Progress)")
	public static NpcStringId ID_38102;
	
	@NpcString(id = 38201, message = "Kail's Magic Coin")
	public static NpcStringId ID_38201;
	
	@NpcString(id = 38202, message = "Kail's Magic Coin (In Progress)")
	public static NpcStringId ID_38202;
	
	@NpcString(id = 38301, message = "Treasure Hunt")
	public static NpcStringId ID_38301;
	
	@NpcString(id = 38302, message = "Treasure Hunt (In Progress)")
	public static NpcStringId ID_38302;
	
	@NpcString(id = 38401, message = "Warehouse Keeper's Pastime")
	public static NpcStringId ID_38401;
	
	@NpcString(id = 38402, message = "Warehouse Keeper's Pastime (In Progress)")
	public static NpcStringId ID_38402;
	
	@NpcString(id = 38451, message = "Slot %s: %s")
	public static NpcStringId ID_38451;
	
	@NpcString(id = 38501, message = "Yoke of the Past")
	public static NpcStringId ID_38501;
	
	@NpcString(id = 38502, message = "Yoke of the Past (In Progress)")
	public static NpcStringId ID_38502;
	
	@NpcString(id = 38601, message = "Stolen Dignity")
	public static NpcStringId ID_38601;
	
	@NpcString(id = 38602, message = "Stolen Dignity (In Progress)")
	public static NpcStringId ID_38602;
	
	@NpcString(id = 40001, message = "Long Live the Paagrio Lord")
	public static NpcStringId ID_40001;
	
	@NpcString(id = 40002, message = "Long Live the Paagrio Lord (In Progress)")
	public static NpcStringId ID_40002;
	
	@NpcString(id = 40003, message = "Long Live the Paagrio Lord (Done)")
	public static NpcStringId ID_40003;
	
	@NpcString(id = 40101, message = "Path of the Warrior")
	public static NpcStringId ID_40101;
	
	@NpcString(id = 40102, message = "Path of the Warrior (In Progress)")
	public static NpcStringId ID_40102;
	
	@NpcString(id = 40201, message = "Path of the Human Knight")
	public static NpcStringId ID_40201;
	
	@NpcString(id = 40202, message = "Path of the Human Knight (In Progress)")
	public static NpcStringId ID_40202;
	
	@NpcString(id = 40301, message = "Path of the Rogue")
	public static NpcStringId ID_40301;
	
	@NpcString(id = 40302, message = "Path of the Rogue (In Progress)")
	public static NpcStringId ID_40302;
	
	@NpcString(id = 40306, message = "You childish fool, do you think you can catch me?")
	public static NpcStringId ID_40306;
	
	@NpcString(id = 40307, message = "I must do something about this shameful incident...")
	public static NpcStringId ID_40307;
	
	@NpcString(id = 40308, message = "What, do you dare to challenge me!")
	public static NpcStringId ID_40308;
	
	@NpcString(id = 40309, message = "The red-eyed thieves will revenge!")
	public static NpcStringId ID_40309;
	
	@NpcString(id = 40310, message = "Go ahead, you child!")
	public static NpcStringId ID_40310;
	
	@NpcString(id = 40311, message = "My friends are sure to revenge!")
	public static NpcStringId ID_40311;
	
	@NpcString(id = 40312, message = "You ruthless fool, I will show you what real fighting is all about!")
	public static NpcStringId ID_40312;
	
	@NpcString(id = 40313, message = "Ahh, how can it end like this... it is not fair!")
	public static NpcStringId ID_40313;
	
	@NpcString(id = 40401, message = "Path of the Human Wizard")
	public static NpcStringId ID_40401;
	
	@NpcString(id = 40402, message = "Path of the Human Wizard (In Progress)")
	public static NpcStringId ID_40402;
	
	@NpcString(id = 40501, message = "Path of the Cleric")
	public static NpcStringId ID_40501;
	
	@NpcString(id = 40502, message = "Path of the Cleric (In Progress)")
	public static NpcStringId ID_40502;
	
	@NpcString(id = 40601, message = "Path of the Elven Knight")
	public static NpcStringId ID_40601;
	
	@NpcString(id = 40602, message = "Path of the Elven Knight (In Progress)")
	public static NpcStringId ID_40602;
	
	@NpcString(id = 40701, message = "Path of the Elven Scout")
	public static NpcStringId ID_40701;
	
	@NpcString(id = 40702, message = "Path of the Elven Scout (In Progress)")
	public static NpcStringId ID_40702;
	
	@NpcString(id = 40801, message = "Path of the Elven Wizard")
	public static NpcStringId ID_40801;
	
	@NpcString(id = 40802, message = "Path of the Elven Wizard (In Progress)")
	public static NpcStringId ID_40802;
	
	@NpcString(id = 40901, message = "Path of the Elven Oracle")
	public static NpcStringId ID_40901;
	
	@NpcString(id = 40902, message = "Path of the Elven Oracle (In Progress)")
	public static NpcStringId ID_40902;
	
	@NpcString(id = 40909, message = "The sacred flame is ours!")
	public static NpcStringId ID_40909;
	
	@NpcString(id = 40910, message = "Arrghh...we shall never.. surrender...")
	public static NpcStringId ID_40910;
	
	@NpcString(id = 40911, message = "The sacred flame is ours")
	public static NpcStringId ID_40911;
	
	@NpcString(id = 40912, message = "The sacred flame is ours")
	public static NpcStringId ID_40912;
	
	@NpcString(id = 40913, message = "As you wish, master!")
	public static NpcStringId ID_40913;
	
	@NpcString(id = 41001, message = "Path of the Palus Knight")
	public static NpcStringId ID_41001;
	
	@NpcString(id = 41002, message = "Path of the Palus Knight (In Progress)")
	public static NpcStringId ID_41002;
	
	@NpcString(id = 41101, message = "Path of the Assassin")
	public static NpcStringId ID_41101;
	
	@NpcString(id = 41102, message = "Path of the Assassin (In Progress)")
	public static NpcStringId ID_41102;
	
	@NpcString(id = 41201, message = "Path of the Dark Wizard")
	public static NpcStringId ID_41201;
	
	@NpcString(id = 41202, message = "Path of the Dark Wizard (In Progress)")
	public static NpcStringId ID_41202;
	
	@NpcString(id = 41301, message = "Path of the Shillien Oracle")
	public static NpcStringId ID_41301;
	
	@NpcString(id = 41302, message = "Path of the Shillien Oracle (In Progress)")
	public static NpcStringId ID_41302;
	
	@NpcString(id = 41401, message = "Path of the Orc Raider")
	public static NpcStringId ID_41401;
	
	@NpcString(id = 41402, message = "Path of the Orc Raider (In Progress)")
	public static NpcStringId ID_41402;
	
	@NpcString(id = 41501, message = "Path of the Orc Monk")
	public static NpcStringId ID_41501;
	
	@NpcString(id = 41502, message = "Path of the Orc Monk (In Progress)")
	public static NpcStringId ID_41502;
	
	@NpcString(id = 41601, message = "Path of the Orc Shaman")
	public static NpcStringId ID_41601;
	
	@NpcString(id = 41602, message = "Path of the Orc Shaman (In Progress)")
	public static NpcStringId ID_41602;
	
	@NpcString(id = 41701, message = "Path of the Scavenger")
	public static NpcStringId ID_41701;
	
	@NpcString(id = 41702, message = "Path of the Scavenger (In Progress)")
	public static NpcStringId ID_41702;
	
	@NpcString(id = 41801, message = "Path of the Artisan")
	public static NpcStringId ID_41801;
	
	@NpcString(id = 41802, message = "Path of the Artisan (In Progress)")
	public static NpcStringId ID_41802;
	
	@NpcString(id = 41901, message = "Get a Pet")
	public static NpcStringId ID_41901;
	
	@NpcString(id = 41902, message = "Get a Pet (In Progress)")
	public static NpcStringId ID_41902;
	
	@NpcString(id = 42001, message = "Little Wing")
	public static NpcStringId ID_42001;
	
	@NpcString(id = 42002, message = "Little Wing (In Progress)")
	public static NpcStringId ID_42002;
	
	@NpcString(id = 42046, message = "Hey! Everybody watch the eggs!")
	public static NpcStringId ID_42046;
	
	@NpcString(id = 42047, message = "I thought I'd caught one share... Whew!")
	public static NpcStringId ID_42047;
	
	@NpcString(id = 42048, message = "The stone... the Elven stone... broke...")
	public static NpcStringId ID_42048;
	
	@NpcString(id = 42049, message = "If the eggs get taken, we're dead!")
	public static NpcStringId ID_42049;
	
	@NpcString(id = 42101, message = "Little Wing's Big Adventure")
	public static NpcStringId ID_42101;
	
	@NpcString(id = 42102, message = "Little Wing's Big Adventure (In Progress)")
	public static NpcStringId ID_42102;
	
	@NpcString(id = 42111, message = "Give me a Fairy Leaf...!")
	public static NpcStringId ID_42111;
	
	@NpcString(id = 42112, message = "Why do you bother me again?")
	public static NpcStringId ID_42112;
	
	@NpcString(id = 42113, message = "Hey, you've already drunk the essence of wind!")
	public static NpcStringId ID_42113;
	
	@NpcString(id = 42114, message = "Leave now, before you incur the wrath of the guardian ghost...")
	public static NpcStringId ID_42114;
	
	@NpcString(id = 42115, message = "Hey, you've already drunk the essence of a star!")
	public static NpcStringId ID_42115;
	
	@NpcString(id = 42116, message = "Hey, you've already drunk the essence of dusk!")
	public static NpcStringId ID_42116;
	
	@NpcString(id = 42117, message = "Hey, you've already drunk the essence of the abyss!")
	public static NpcStringId ID_42117;
	
	@NpcString(id = 42118, message = "We must protect the fairy tree!")
	public static NpcStringId ID_42118;
	
	@NpcString(id = 42119, message = "Get out of the sacred tree, you scoundrels!")
	public static NpcStringId ID_42119;
	
	@NpcString(id = 42120, message = "Death to the thieves of the pure water of the world!")
	public static NpcStringId ID_42120;
	
	@NpcString(id = 42201, message = "Repent Your Sins")
	public static NpcStringId ID_42201;
	
	@NpcString(id = 42202, message = "Repent Your Sins (In Progress)")
	public static NpcStringId ID_42202;
	
	@NpcString(id = 42231, message = "Hey, it seems like you need my help, doesn't it?")
	public static NpcStringId ID_42231;
	
	@NpcString(id = 42232, message = "Almost got it... Ouch! Stop!  Damn these bloody manacles!")
	public static NpcStringId ID_42232;
	
	@NpcString(id = 42233, message = "Oh, that smarts!")
	public static NpcStringId ID_42233;
	
	@NpcString(id = 42234, message = "Hey, master! Pay attention! I'm dying over here!")
	public static NpcStringId ID_42234;
	
	@NpcString(id = 42235, message = "What have I done to deserve this?")
	public static NpcStringId ID_42235;
	
	@NpcString(id = 42236, message = "Oh, this is just great! What are you going to do now?")
	public static NpcStringId ID_42236;
	
	@NpcString(id = 42237, message = "You inconsiderate moron! Can't you even take care of little old me?!")
	public static NpcStringId ID_42237;
	
	@NpcString(id = 42238, message = "Oh no! The man who eats one's sins has died! Penitence is further away~!")
	public static NpcStringId ID_42238;
	
	@NpcString(id = 42239, message = "Using a special skill here could trigger a bloodbath!")
	public static NpcStringId ID_42239;
	
	@NpcString(id = 42240, message = "Hey, what do you expect of me?")
	public static NpcStringId ID_42240;
	
	@NpcString(id = 42241, message = "Ugggggh! Push! It's not coming out!")
	public static NpcStringId ID_42241;
	
	@NpcString(id = 42242, message = "Ah, I missed the mark!")
	public static NpcStringId ID_42242;
	
	@NpcString(id = 42243, message = "Yawwwwn! It's so boring here. We should go and find some action!")
	public static NpcStringId ID_42243;
	
	@NpcString(id = 42244, message = "Hey, if you continue to waste time you will never finish your penance!")
	public static NpcStringId ID_42244;
	
	@NpcString(id = 42245, message = "I know you don't like me. The feeling is mutual!")
	public static NpcStringId ID_42245;
	
	@NpcString(id = 42246, message = "I need a drink.")
	public static NpcStringId ID_42246;
	
	@NpcString(id = 42247, message = "Oh, this is dragging on too long...  At this rate I won't make it home before the seven seals are broken.")
	public static NpcStringId ID_42247;
	
	@NpcString(id = 42301, message = "Take Your Best Shot")
	public static NpcStringId ID_42301;
	
	@NpcString(id = 42302, message = "Take Your Best Shot (In Progress)")
	public static NpcStringId ID_42302;
	
	@NpcString(id = 42601, message = "Quest for Fishing shot")
	public static NpcStringId ID_42601;
	
	@NpcString(id = 42602, message = "Quest for Fishing shot (In Progress)")
	public static NpcStringId ID_42602;
	
	@NpcString(id = 43101, message = "Wedding March")
	public static NpcStringId ID_43101;
	
	@NpcString(id = 43102, message = "Wedding March (In Progress)")
	public static NpcStringId ID_43102;
	
	@NpcString(id = 43201, message = "Birthday Party Song")
	public static NpcStringId ID_43201;
	
	@NpcString(id = 43202, message = "Birthday Party Song (In Progress)")
	public static NpcStringId ID_43202;
	
	@NpcString(id = 45001, message = "Grave Robber Rescue")
	public static NpcStringId ID_45001;
	
	@NpcString(id = 45002, message = "Grave Robber Rescue (In Progress)")
	public static NpcStringId ID_45002;
	
	@NpcString(id = 45003, message = "Grave Robber Rescue (Done)")
	public static NpcStringId ID_45003;
	
	@NpcString(id = 45101, message = "Lucien's Altar")
	public static NpcStringId ID_45101;
	
	@NpcString(id = 45102, message = "Lucien's Altar (In Progress)")
	public static NpcStringId ID_45102;
	
	@NpcString(id = 45103, message = "Lucien's Altar (Done)")
	public static NpcStringId ID_45103;
	
	@NpcString(id = 45201, message = "Finding the Lost Soldiers")
	public static NpcStringId ID_45201;
	
	@NpcString(id = 45202, message = "Finding the Lost Soldiers (In Progress)")
	public static NpcStringId ID_45202;
	
	@NpcString(id = 45203, message = "Finding the Lost Soldiers (Done)")
	public static NpcStringId ID_45203;
	
	@NpcString(id = 45301, message = "Not Strong Enough Alone")
	public static NpcStringId ID_45301;
	
	@NpcString(id = 45302, message = "Not Strong Enough Alone (In Progress)")
	public static NpcStringId ID_45302;
	
	@NpcString(id = 45303, message = "Not Strong Enough Alone (Done)")
	public static NpcStringId ID_45303;
	
	@NpcString(id = 45401, message = "Completely Lost")
	public static NpcStringId ID_45401;
	
	@NpcString(id = 45402, message = "Completely Lost  (In Progress)")
	public static NpcStringId ID_45402;
	
	@NpcString(id = 45403, message = "Completely Lost  (Done)")
	public static NpcStringId ID_45403;
	
	@NpcString(id = 45501, message = "Wings of Sand")
	public static NpcStringId ID_45501;
	
	@NpcString(id = 45502, message = "Wings of Sand (In Progress)")
	public static NpcStringId ID_45502;
	
	@NpcString(id = 45503, message = "Wings of Sand (Done)")
	public static NpcStringId ID_45503;
	
	@NpcString(id = 45601, message = "Don't Know, Don't Care")
	public static NpcStringId ID_45601;
	
	@NpcString(id = 45602, message = "Don't Know, Don't Care (In Progress)")
	public static NpcStringId ID_45602;
	
	@NpcString(id = 45603, message = "Don't Know, Don't Care (Done)")
	public static NpcStringId ID_45603;
	
	@NpcString(id = 45701, message = "Lost and Found")
	public static NpcStringId ID_45701;
	
	@NpcString(id = 45702, message = "Lost and Found (In Progress)")
	public static NpcStringId ID_45702;
	
	@NpcString(id = 45703, message = "Lost and Found (Done)")
	public static NpcStringId ID_45703;
	
	@NpcString(id = 45801, message = "Perfect Form")
	public static NpcStringId ID_45801;
	
	@NpcString(id = 45802, message = "Perfect Form (In Progress)")
	public static NpcStringId ID_45802;
	
	@NpcString(id = 45803, message = "Perfect Form (Done)")
	public static NpcStringId ID_45803;
	
	@NpcString(id = 46101, message = "Rumble in the Base")
	public static NpcStringId ID_46101;
	
	@NpcString(id = 46102, message = "Rumble in the Base (In Progress)")
	public static NpcStringId ID_46102;
	
	@NpcString(id = 46103, message = "Rumble in the Base (Done)")
	public static NpcStringId ID_46103;
	
	@NpcString(id = 46301, message = "I Must Be a Genius")
	public static NpcStringId ID_46301;
	
	@NpcString(id = 46302, message = "I Must Be a Genius (In Progress)")
	public static NpcStringId ID_46302;
	
	@NpcString(id = 46303, message = "I Must Be a Genius (Done)")
	public static NpcStringId ID_46303;
	
	@NpcString(id = 46401, message = "Oath")
	public static NpcStringId ID_46401;
	
	@NpcString(id = 46402, message = "Oath (In Progress)")
	public static NpcStringId ID_46402;
	
	@NpcString(id = 46403, message = "Oath (Done)")
	public static NpcStringId ID_46403;
	
	@NpcString(id = 50001, message = "Miner's Favor")
	public static NpcStringId ID_50001;
	
	@NpcString(id = 50002, message = "Miner's Favor (In Progress)")
	public static NpcStringId ID_50002;
	
	@NpcString(id = 50003, message = "Miner's Favor (Done)")
	public static NpcStringId ID_50003;
	
	@NpcString(id = 50101, message = "Proof of Clan Alliance")
	public static NpcStringId ID_50101;
	
	@NpcString(id = 50102, message = "Proof of Clan Alliance (In Progress)")
	public static NpcStringId ID_50102;
	
	@NpcString(id = 50110, message = "########## BINGO! ##########")
	public static NpcStringId ID_50110;
	
	@NpcString(id = 50301, message = "Pursuit of Clan Ambition!")
	public static NpcStringId ID_50301;
	
	@NpcString(id = 50302, message = "Pursuit of Clan Ambition! (In Progress)")
	public static NpcStringId ID_50302;
	
	@NpcString(id = 50338, message = "Blood and honor!")
	public static NpcStringId ID_50338;
	
	@NpcString(id = 50339, message = "Curse of the gods on the one that defiles the property of the empire!")
	public static NpcStringId ID_50339;
	
	@NpcString(id = 50340, message = "War and death!")
	public static NpcStringId ID_50340;
	
	@NpcString(id = 50341, message = "Ambition and power!")
	public static NpcStringId ID_50341;
	
	@NpcString(id = 50401, message = "Competition for the Bandit Stronghold")
	public static NpcStringId ID_50401;
	
	@NpcString(id = 50402, message = "Competition for the Bandit Stronghold (In Progress)")
	public static NpcStringId ID_50402;
	
	@NpcString(id = 50501, message = "Offering Dedicated to Shilen")
	public static NpcStringId ID_50501;
	
	@NpcString(id = 50502, message = "Offering Dedicated to Shilen (In Progress)")
	public static NpcStringId ID_50502;
	
	@NpcString(id = 50503, message = "%s has won the main event for players under level %s, and earned %s points!")
	public static NpcStringId ID_50503;
	
	@NpcString(id = 50504, message = "%s has earned %s points in the main event for unlimited levels.")
	public static NpcStringId ID_50504;
	
	@NpcString(id = 50701, message = "Into the Chaos")
	public static NpcStringId ID_50701;
	
	@NpcString(id = 50702, message = "Into the Chaos  (In Progress)")
	public static NpcStringId ID_50702;
	
	@NpcString(id = 50801, message = "A Clan's Reputation")
	public static NpcStringId ID_50801;
	
	@NpcString(id = 50802, message = "A Clan's Reputation (In Progress)")
	public static NpcStringId ID_50802;
	
	@NpcString(id = 50901, message = "The Clan's Prestige")
	public static NpcStringId ID_50901;
	
	@NpcString(id = 50902, message = "The Clan's Prestige (In Progress)")
	public static NpcStringId ID_50902;
	
	@NpcString(id = 51001, message = "A Clan's Reputation")
	public static NpcStringId ID_51001;
	
	@NpcString(id = 51002, message = "A Clan's Reputation (In Progress)")
	public static NpcStringId ID_51002;
	
	@NpcString(id = 51101, message = "Awl Under Foot")
	public static NpcStringId ID_51101;
	
	@NpcString(id = 51102, message = "Awl Under Foot (In Progress)")
	public static NpcStringId ID_51102;
	
	@NpcString(id = 51201, message = "Hidden Blade")
	public static NpcStringId ID_51201;
	
	@NpcString(id = 51202, message = "Hidden Blade (In Progress)")
	public static NpcStringId ID_51202;
	
	@NpcString(id = 55101, message = "Olympiad Starter")
	public static NpcStringId ID_55101;
	
	@NpcString(id = 55102, message = "Olympiad Starter (In Progress)")
	public static NpcStringId ID_55102;
	
	@NpcString(id = 55103, message = "Olympiad Starter (Done)")
	public static NpcStringId ID_55103;
	
	@NpcString(id = 55201, message = "Olympiad Veteran")
	public static NpcStringId ID_55201;
	
	@NpcString(id = 55202, message = "Olympiad Veteran (In Progress)")
	public static NpcStringId ID_55202;
	
	@NpcString(id = 55203, message = "Olympiad Veteran (Done)")
	public static NpcStringId ID_55203;
	
	@NpcString(id = 55301, message = "Olympiad Undefeated")
	public static NpcStringId ID_55301;
	
	@NpcString(id = 55302, message = "Olympiad Undefeated (In Progress)")
	public static NpcStringId ID_55302;
	
	@NpcString(id = 55303, message = "Olympiad Undefeated (Done)")
	public static NpcStringId ID_55303;
	
	@NpcString(id = 60101, message = "Watching Eyes")
	public static NpcStringId ID_60101;
	
	@NpcString(id = 60102, message = "Watching Eyes (In Progress)")
	public static NpcStringId ID_60102;
	
	@NpcString(id = 60201, message = "Shadow of Light")
	public static NpcStringId ID_60201;
	
	@NpcString(id = 60202, message = "Shadow of Light (In Progress)")
	public static NpcStringId ID_60202;
	
	@NpcString(id = 60301, message = "Daimon the White-Eyed - Part 1")
	public static NpcStringId ID_60301;
	
	@NpcString(id = 60302, message = "Daimon the White-Eyed - Part 1 (In Progress)")
	public static NpcStringId ID_60302;
	
	@NpcString(id = 60401, message = "Daimon the White-Eyed - Part 2")
	public static NpcStringId ID_60401;
	
	@NpcString(id = 60402, message = "Daimon the White-Eyed - Part 2 (In Progress)")
	public static NpcStringId ID_60402;
	
	@NpcString(id = 60403, message = "Who is calling me?")
	public static NpcStringId ID_60403;
	
	@NpcString(id = 60404, message = "Can light exist without darkness?")
	public static NpcStringId ID_60404;
	
	@NpcString(id = 60501, message = "Alliance with Ketra Orcs")
	public static NpcStringId ID_60501;
	
	@NpcString(id = 60502, message = "Alliance with Ketra Orcs (In Progress)")
	public static NpcStringId ID_60502;
	
	@NpcString(id = 60601, message = "War with Varka Silenos")
	public static NpcStringId ID_60601;
	
	@NpcString(id = 60602, message = "War with Varka Silenos (In Progress)")
	public static NpcStringId ID_60602;
	
	@NpcString(id = 60701, message = "Prove your courage! (Ketra)")
	public static NpcStringId ID_60701;
	
	@NpcString(id = 60702, message = "Prove your courage! (Ketra) (In Progress)")
	public static NpcStringId ID_60702;
	
	@NpcString(id = 60801, message = "Slay the enemy commander! (Ketra)")
	public static NpcStringId ID_60801;
	
	@NpcString(id = 60802, message = "Slay the enemy commander! (Ketra) (In Progress)")
	public static NpcStringId ID_60802;
	
	@NpcString(id = 60901, message = "Magical Power of Water - Part 1")
	public static NpcStringId ID_60901;
	
	@NpcString(id = 60902, message = "Magical Power of Water - Part 1 (In Progress)")
	public static NpcStringId ID_60902;
	
	@NpcString(id = 60903, message = "You can't avoid the eyes of Udan!")
	public static NpcStringId ID_60903;
	
	@NpcString(id = 60904, message = "Udan has already seen your face!")
	public static NpcStringId ID_60904;
	
	@NpcString(id = 61001, message = "Magical Power of Water - Part 2")
	public static NpcStringId ID_61001;
	
	@NpcString(id = 61002, message = "Magical Power of Water - Part 2 (In Progress)")
	public static NpcStringId ID_61002;
	
	@NpcString(id = 61050, message = "The magical power of water comes from the power of storm and hail!  If you dare to confront it, only death will await you!")
	public static NpcStringId ID_61050;
	
	@NpcString(id = 61051, message = "The power of constraint is getting weaker.  Your ritual has failed!")
	public static NpcStringId ID_61051;
	
	@NpcString(id = 61101, message = "Alliance with Varka Silenos")
	public static NpcStringId ID_61101;
	
	@NpcString(id = 61102, message = "Alliance with Varka Silenos (In Progress)")
	public static NpcStringId ID_61102;
	
	@NpcString(id = 61201, message = "War with Ketra Orcs")
	public static NpcStringId ID_61201;
	
	@NpcString(id = 61202, message = "War with Ketra Orcs (In Progress)")
	public static NpcStringId ID_61202;
	
	@NpcString(id = 61301, message = "Prove Your Courage! (Varka)")
	public static NpcStringId ID_61301;
	
	@NpcString(id = 61302, message = "Prove Your Courage! (Varka) (In Progress)")
	public static NpcStringId ID_61302;
	
	@NpcString(id = 61401, message = "Slay the Enemy Commander! (Varka)")
	public static NpcStringId ID_61401;
	
	@NpcString(id = 61402, message = "Slay the Enemy Commander! (Varka) (In Progress)")
	public static NpcStringId ID_61402;
	
	@NpcString(id = 61501, message = "Magical Power of Fire - Part 1")
	public static NpcStringId ID_61501;
	
	@NpcString(id = 61502, message = "Magical Power of Fire - Part 1 (In Progress)")
	public static NpcStringId ID_61502;
	
	@NpcString(id = 61503, message = "You can't avoid the eyes of Asefa!")
	public static NpcStringId ID_61503;
	
	@NpcString(id = 61504, message = "Asefa has already seen your face!")
	public static NpcStringId ID_61504;
	
	@NpcString(id = 61601, message = "Magical Power of Fire - Part 2")
	public static NpcStringId ID_61601;
	
	@NpcString(id = 61602, message = "Magical Power of Fire - Part 2 (In Progress)")
	public static NpcStringId ID_61602;
	
	@NpcString(id = 61650, message = "The magical power of fire is also the power of flames and lava!  If you dare to confront it, only death will await you!")
	public static NpcStringId ID_61650;
	
	@NpcString(id = 61651, message = "The power of constraint is getting weaker.  Your ritual has failed!")
	public static NpcStringId ID_61651;
	
	@NpcString(id = 61701, message = "Gather the Flames")
	public static NpcStringId ID_61701;
	
	@NpcString(id = 61702, message = "Gather the Flames (In Progress)")
	public static NpcStringId ID_61702;
	
	@NpcString(id = 61801, message = "Into the flames")
	public static NpcStringId ID_61801;
	
	@NpcString(id = 61802, message = "Into the flames (In Progress)")
	public static NpcStringId ID_61802;
	
	@NpcString(id = 61901, message = "Relics of the Old Empire")
	public static NpcStringId ID_61901;
	
	@NpcString(id = 61902, message = "Relics of the Old Empire (In Progress)")
	public static NpcStringId ID_61902;
	
	@NpcString(id = 62001, message = "Four Goblets")
	public static NpcStringId ID_62001;
	
	@NpcString(id = 62002, message = "Four Goblets (In Progress)")
	public static NpcStringId ID_62002;
	
	@NpcString(id = 62101, message = "Egg Delivery")
	public static NpcStringId ID_62101;
	
	@NpcString(id = 62102, message = "Egg Delivery (In Progress)")
	public static NpcStringId ID_62102;
	
	@NpcString(id = 62201, message = "Specialty Liquor Delivery")
	public static NpcStringId ID_62201;
	
	@NpcString(id = 62202, message = "Specialty Liquor Delivery (In Progress)")
	public static NpcStringId ID_62202;
	
	@NpcString(id = 62301, message = "The Finest Food")
	public static NpcStringId ID_62301;
	
	@NpcString(id = 62302, message = "The Finest Food (In Progress)")
	public static NpcStringId ID_62302;
	
	@NpcString(id = 62401, message = "The Finest Ingredients - Part 1")
	public static NpcStringId ID_62401;
	
	@NpcString(id = 62402, message = "The Finest Ingredients - Part 1 (In Progress)")
	public static NpcStringId ID_62402;
	
	@NpcString(id = 62501, message = "The Finest Ingredients - Part 2")
	public static NpcStringId ID_62501;
	
	@NpcString(id = 62502, message = "The Finest Ingredients - Part 2 (In Progress)")
	public static NpcStringId ID_62502;
	
	@NpcString(id = 62503, message = "I smell something delicious...")
	public static NpcStringId ID_62503;
	
	@NpcString(id = 62504, message = "Oooh!")
	public static NpcStringId ID_62504;
	
	@NpcString(id = 62601, message = "A Dark Twilight")
	public static NpcStringId ID_62601;
	
	@NpcString(id = 62602, message = "A Dark Twilight (In Progress)")
	public static NpcStringId ID_62602;
	
	@NpcString(id = 62701, message = "Heart in Search of Power")
	public static NpcStringId ID_62701;
	
	@NpcString(id = 62702, message = "Heart in Search of Power (In Progress)")
	public static NpcStringId ID_62702;
	
	@NpcString(id = 62801, message = "Hunt of the Golden Ram Mercenary Force")
	public static NpcStringId ID_62801;
	
	@NpcString(id = 62802, message = "Hunt of the Golden Ram Mercenary Force (In Progress)")
	public static NpcStringId ID_62802;
	
	@NpcString(id = 62901, message = "Clean up the Swamp of Screams")
	public static NpcStringId ID_62901;
	
	@NpcString(id = 62902, message = "Clean up the Swamp of Screams (In Progress)")
	public static NpcStringId ID_62902;
	
	@NpcString(id = 63101, message = "Delicious Top Choice Meat")
	public static NpcStringId ID_63101;
	
	@NpcString(id = 63102, message = "Delicious Top Choice Meat (In Progress)")
	public static NpcStringId ID_63102;
	
	@NpcString(id = 63201, message = "Necromancer's Request")
	public static NpcStringId ID_63201;
	
	@NpcString(id = 63202, message = "Necromancer's Request (In Progress)")
	public static NpcStringId ID_63202;
	
	@NpcString(id = 63301, message = "In the Forgotten Village")
	public static NpcStringId ID_63301;
	
	@NpcString(id = 63302, message = "In the Forgotten Village (In Progress)")
	public static NpcStringId ID_63302;
	
	@NpcString(id = 63401, message = "In Search of Fragments of Dimension")
	public static NpcStringId ID_63401;
	
	@NpcString(id = 63402, message = "In Search of Fragments of Dimension (In Progress)")
	public static NpcStringId ID_63402;
	
	@NpcString(id = 63501, message = "Into the Dimensional Rift")
	public static NpcStringId ID_63501;
	
	@NpcString(id = 63502, message = "Into the Dimensional Rift (In Progress)")
	public static NpcStringId ID_63502;
	
	@NpcString(id = 63601, message = "Truth Beyond the Gate")
	public static NpcStringId ID_63601;
	
	@NpcString(id = 63602, message = "Truth Beyond the Gate (In Progress)")
	public static NpcStringId ID_63602;
	
	@NpcString(id = 63701, message = "Through the Gate Once More")
	public static NpcStringId ID_63701;
	
	@NpcString(id = 63702, message = "Through the Gate Once More (In Progress)")
	public static NpcStringId ID_63702;
	
	@NpcString(id = 63801, message = "Seekers of the Holy Grail")
	public static NpcStringId ID_63801;
	
	@NpcString(id = 63802, message = "Seekers of the Holy Grail (In Progress)")
	public static NpcStringId ID_63802;
	
	@NpcString(id = 63901, message = "Guardians of the Holy Grail")
	public static NpcStringId ID_63901;
	
	@NpcString(id = 63902, message = "Guardians of the Holy Grail (In Progress)")
	public static NpcStringId ID_63902;
	
	@NpcString(id = 63903, message = "Guardians of the Holy Grail (Done)")
	public static NpcStringId ID_63903;
	
	@NpcString(id = 64001, message = "The Zero Hour")
	public static NpcStringId ID_64001;
	
	@NpcString(id = 64002, message = "The Zero Hour (In Progress)")
	public static NpcStringId ID_64002;
	
	@NpcString(id = 64003, message = "The Zero Hour (Done)")
	public static NpcStringId ID_64003;
	
	@NpcString(id = 64101, message = "Sailren's Charge!")
	public static NpcStringId ID_64101;
	
	@NpcString(id = 64102, message = "Sailren's Charge! (In Progress)")
	public static NpcStringId ID_64102;
	
	@NpcString(id = 64201, message = "A Powerful Primeval Creature")
	public static NpcStringId ID_64201;
	
	@NpcString(id = 64202, message = "A Powerful Primeval Creature (In Progress)")
	public static NpcStringId ID_64202;
	
	@NpcString(id = 64301, message = "Rise and Fall of the Elroki Tribe")
	public static NpcStringId ID_64301;
	
	@NpcString(id = 64302, message = "Rise and Fall of the Elroki Tribe (In Progress)")
	public static NpcStringId ID_64302;
	
	@NpcString(id = 64401, message = "Grave Robber Annihilation")
	public static NpcStringId ID_64401;
	
	@NpcString(id = 64402, message = "Grave Robber Annihilation (In Progress)")
	public static NpcStringId ID_64402;
	
	@NpcString(id = 64501, message = "Ghosts of Batur")
	public static NpcStringId ID_64501;
	
	@NpcString(id = 64502, message = "Ghosts of Batur (In Progress)")
	public static NpcStringId ID_64502;
	
	@NpcString(id = 64601, message = "Signs of Revolt")
	public static NpcStringId ID_64601;
	
	@NpcString(id = 64602, message = "Signs of Revolt (In Progress)")
	public static NpcStringId ID_64602;
	
	@NpcString(id = 64701, message = "Influx of Machines")
	public static NpcStringId ID_64701;
	
	@NpcString(id = 64702, message = "Influx of Machines (In Progress)")
	public static NpcStringId ID_64702;
	
	@NpcString(id = 64801, message = "An Ice Merchant's Dream")
	public static NpcStringId ID_64801;
	
	@NpcString(id = 64802, message = "An Ice Merchant's Dream (In Progress)")
	public static NpcStringId ID_64802;
	
	@NpcString(id = 64901, message = "A Looter and a Railroad Man")
	public static NpcStringId ID_64901;
	
	@NpcString(id = 64902, message = "A Looter and a Railroad Man (In Progress)")
	public static NpcStringId ID_64902;
	
	@NpcString(id = 65001, message = "A Broken Dream")
	public static NpcStringId ID_65001;
	
	@NpcString(id = 65002, message = "A Broken Dream (In Progress)")
	public static NpcStringId ID_65002;
	
	@NpcString(id = 65101, message = "Runaway Youth")
	public static NpcStringId ID_65101;
	
	@NpcString(id = 65102, message = "Runaway Youth (In Progress)")
	public static NpcStringId ID_65102;
	
	@NpcString(id = 65201, message = "An Aged Ex-Adventurer")
	public static NpcStringId ID_65201;
	
	@NpcString(id = 65202, message = "An Aged Ex-Adventurer (In Progress)")
	public static NpcStringId ID_65202;
	
	@NpcString(id = 65301, message = "Wild Maiden")
	public static NpcStringId ID_65301;
	
	@NpcString(id = 65302, message = "Wild Maiden (In Progress)")
	public static NpcStringId ID_65302;
	
	@NpcString(id = 65401, message = "Journey to a Settlement")
	public static NpcStringId ID_65401;
	
	@NpcString(id = 65402, message = "Journey to a Settlement (In Progress)")
	public static NpcStringId ID_65402;
	
	@NpcString(id = 65501, message = "A Grand Plan for Taming Wild Beasts")
	public static NpcStringId ID_65501;
	
	@NpcString(id = 65502, message = "A Grand Plan for Taming Wild Beasts (In Progress)")
	public static NpcStringId ID_65502;
	
	@NpcString(id = 65901, message = "I'd Rather Be Collecting Fairy Breath")
	public static NpcStringId ID_65901;
	
	@NpcString(id = 65902, message = "I'd Rather Be Collecting Fairy Breath (In Progress)")
	public static NpcStringId ID_65902;
	
	@NpcString(id = 66001, message = "Aiding the Floran Village.")
	public static NpcStringId ID_66001;
	
	@NpcString(id = 66002, message = "Aiding the Floran Village (In Progress)")
	public static NpcStringId ID_66002;
	
	@NpcString(id = 66101, message = "Making the Harvest Grounds Safe")
	public static NpcStringId ID_66101;
	
	@NpcString(id = 66102, message = "Making the Harvest Grounds Safe (In Progress)")
	public static NpcStringId ID_66102;
	
	@NpcString(id = 66103, message = "Making the Harvest Grounds Safe (Done)")
	public static NpcStringId ID_66103;
	
	@NpcString(id = 66201, message = "A Game of Cards")
	public static NpcStringId ID_66201;
	
	@NpcString(id = 66202, message = "A Game of Cards (In Progress)")
	public static NpcStringId ID_66202;
	
	@NpcString(id = 66300, message = "No such card")
	public static NpcStringId ID_66300;
	
	@NpcString(id = 66301, message = "Seductive Whispers")
	public static NpcStringId ID_66301;
	
	@NpcString(id = 66302, message = "Seductive Whispers (In Progress)")
	public static NpcStringId ID_66302;
	
	@NpcString(id = 66311, message = "<font color=\"ff453d\"> Sun Card: 1 </font>")
	public static NpcStringId ID_66311;
	
	@NpcString(id = 66312, message = "<font color=\"ff453d\"> Sun Card: 2 </font>")
	public static NpcStringId ID_66312;
	
	@NpcString(id = 66313, message = "<font color=\"ff453d\"> Sun Card: 3 </font>")
	public static NpcStringId ID_66313;
	
	@NpcString(id = 66314, message = "<font color=\"ff453d\"> Sun Card: 4 </font>")
	public static NpcStringId ID_66314;
	
	@NpcString(id = 66315, message = "<font color=\"ff453d\"> Sun Card: 5 </font>")
	public static NpcStringId ID_66315;
	
	@NpcString(id = 66321, message = "<font color=\"fff802\"> Moon Card: 1 </font>")
	public static NpcStringId ID_66321;
	
	@NpcString(id = 66322, message = "<font color=\"fff802\"> Moon Card: 2 </font>")
	public static NpcStringId ID_66322;
	
	@NpcString(id = 66323, message = "<font color=\"fff802\"> Moon Card: 3 </font>")
	public static NpcStringId ID_66323;
	
	@NpcString(id = 66324, message = "<font color=\"fff802\"> Moon Card: 4 </font>")
	public static NpcStringId ID_66324;
	
	@NpcString(id = 66325, message = "<font color=\"fff802\"> Moon Card: 5 </font>")
	public static NpcStringId ID_66325;
	
	@NpcString(id = 68801, message = "Defeat the Elrokian Raiders!")
	public static NpcStringId ID_68801;
	
	@NpcString(id = 68802, message = "Defeat the Elrokian Raiders! (In Progress)")
	public static NpcStringId ID_68802;
	
	@NpcString(id = 69001, message = "Jude's Request")
	public static NpcStringId ID_69001;
	
	@NpcString(id = 69002, message = "Jude's Request (In Progress)")
	public static NpcStringId ID_69002;
	
	@NpcString(id = 69101, message = "Matras' Suspicious Request")
	public static NpcStringId ID_69101;
	
	@NpcString(id = 69102, message = "Matras' Suspicious Request (In Progress)")
	public static NpcStringId ID_69102;
	
	@NpcString(id = 69201, message = "How to Oppose Evil")
	public static NpcStringId ID_69201;
	
	@NpcString(id = 69202, message = "How to Oppose Evil (In Progress)")
	public static NpcStringId ID_69202;
	
	@NpcString(id = 69301, message = "Defeating Dragonkin Remnants")
	public static NpcStringId ID_69301;
	
	@NpcString(id = 69302, message = "Defeating Dragonkin Remnants (In Progress)")
	public static NpcStringId ID_69302;
	
	@NpcString(id = 69401, message = "Break Through the Hall of Suffering")
	public static NpcStringId ID_69401;
	
	@NpcString(id = 69402, message = "Break Through the Hall of Suffering (In Progress)")
	public static NpcStringId ID_69402;
	
	@NpcString(id = 69501, message = "Defend the Hall of Suffering")
	public static NpcStringId ID_69501;
	
	@NpcString(id = 69502, message = "Defend the Hall of Suffering (In Progress)")
	public static NpcStringId ID_69502;
	
	@NpcString(id = 69601, message = "Conquer the Hall of Erosion")
	public static NpcStringId ID_69601;
	
	@NpcString(id = 69602, message = "Conquer the Hall of Erosion (In Progress)")
	public static NpcStringId ID_69602;
	
	@NpcString(id = 69701, message = "Defend the Hall of Erosion")
	public static NpcStringId ID_69701;
	
	@NpcString(id = 69702, message = "Defend the Hall of Erosion (In Progress)")
	public static NpcStringId ID_69702;
	
	@NpcString(id = 69801, message = "Block the Lord's Escape")
	public static NpcStringId ID_69801;
	
	@NpcString(id = 69802, message = "Block the Lord's Escape (In Progress)")
	public static NpcStringId ID_69802;
	
	@NpcString(id = 69901, message = "Guardian of the Skies")
	public static NpcStringId ID_69901;
	
	@NpcString(id = 69902, message = "Guardian of the Skies (In Progress)")
	public static NpcStringId ID_69902;
	
	@NpcString(id = 70001, message = "Cursed Life")
	public static NpcStringId ID_70001;
	
	@NpcString(id = 70002, message = "Cursed Life (In Progress)")
	public static NpcStringId ID_70002;
	
	@NpcString(id = 70101, message = "Proof of Existence")
	public static NpcStringId ID_70101;
	
	@NpcString(id = 70102, message = "Proof of Existence (In Progress)")
	public static NpcStringId ID_70102;
	
	@NpcString(id = 70201, message = "A Trap for Revenge")
	public static NpcStringId ID_70201;
	
	@NpcString(id = 70202, message = "A Trap for Revenge (In Progress)")
	public static NpcStringId ID_70202;
	
	@NpcString(id = 70801, message = "Path to Becoming a Lord - Gludio")
	public static NpcStringId ID_70801;
	
	@NpcString(id = 70802, message = "Path to Becoming a Lord - Gludio (In Progress)")
	public static NpcStringId ID_70802;
	
	@NpcString(id = 70851, message = "Have you completed your preparations to become a lord?")
	public static NpcStringId ID_70851;
	
	@NpcString(id = 70852, message = "%s. Now, depart!")
	public static NpcStringId ID_70852;
	
	@NpcString(id = 70853, message = "Go find Saius!")
	public static NpcStringId ID_70853;
	
	@NpcString(id = 70854, message = "Listen, you villagers. Our liege, who will soon become a lord, has defeated the Headless Knight. You can now rest easy!")
	public static NpcStringId ID_70854;
	
	@NpcString(id = 70855, message = "%s! Do you dare defy my subordinates?")
	public static NpcStringId ID_70855;
	
	@NpcString(id = 70856, message = "Does my mission to block the supplies end here?")
	public static NpcStringId ID_70856;
	
	@NpcString(id = 70859, message = "%s has become lord of the Town of Gludio. Long may he reign!")
	public static NpcStringId ID_70859;
	
	@NpcString(id = 71001, message = "Path to Becoming a Lord - Giran")
	public static NpcStringId ID_71001;
	
	@NpcString(id = 71002, message = "Path to Becoming a Lord - Giran (In Progress)")
	public static NpcStringId ID_71002;
	
	@NpcString(id = 71051, message = "Have you completed your preparations to become a lord?")
	public static NpcStringId ID_71051;
	
	@NpcString(id = 71052, message = "It's the enemy! No mercy!")
	public static NpcStringId ID_71052;
	
	@NpcString(id = 71053, message = "What are you doing? We are still superior!")
	public static NpcStringId ID_71053;
	
	@NpcString(id = 71054, message = "How infuriating! This enemy...")
	public static NpcStringId ID_71054;
	
	@NpcString(id = 71059, message = "%s has become the lord of the Town of Giran. May there be glory in the territory of Giran!")
	public static NpcStringId ID_71059;
	
	@NpcString(id = 71101, message = "Path to Becoming a Lord - Innadril")
	public static NpcStringId ID_71101;
	
	@NpcString(id = 71102, message = "Path to Becoming a Lord - Innadril (In Progress)")
	public static NpcStringId ID_71102;
	
	@NpcString(id = 71151, message = "My liege! Where are you?")
	public static NpcStringId ID_71151;
	
	@NpcString(id = 71152, message = "%s. Now, depart!")
	public static NpcStringId ID_71152;
	
	@NpcString(id = 71159, message = "%s has become the lord of the Town of Innadril. May there be glory in the territory of Innadril!")
	public static NpcStringId ID_71159;
	
	@NpcString(id = 71201, message = "Path to Becoming a Lord - Oren")
	public static NpcStringId ID_71201;
	
	@NpcString(id = 71202, message = "Path to Becoming a Lord - Oren (In Progress)")
	public static NpcStringId ID_71202;
	
	@NpcString(id = 71251, message = "Have you completed your preparations to become a lord?")
	public static NpcStringId ID_71251;
	
	@NpcString(id = 71252, message = "You have found all the Nebulite Orbs!")
	public static NpcStringId ID_71252;
	
	@NpcString(id = 71259, message = "%s has become the lord of the Town of Oren. May there be glory in the territory of Oren!")
	public static NpcStringId ID_71259;
	
	@NpcString(id = 71301, message = "Path to Becoming a Lord - Aden")
	public static NpcStringId ID_71301;
	
	@NpcString(id = 71302, message = "Path to Becoming a Lord - Aden (In Progress)")
	public static NpcStringId ID_71302;
	
	@NpcString(id = 71351, message = "%s has become the lord of the Town of Aden. May there be glory in the territory of Aden!")
	public static NpcStringId ID_71351;
	
	@NpcString(id = 71401, message = "Path to Becoming a Lord - Schuttgart")
	public static NpcStringId ID_71401;
	
	@NpcString(id = 71402, message = "Path to Becoming a Lord - Schuttgart (In Progress)")
	public static NpcStringId ID_71402;
	
	@NpcString(id = 71451, message = "Have you completed your preparations to become a lord?")
	public static NpcStringId ID_71451;
	
	@NpcString(id = 71459, message = "%s has become the lord of the Town of Schuttgart. May there be glory in the territory of Schuttgart!")
	public static NpcStringId ID_71459;
	
	@NpcString(id = 71501, message = "Path to Becoming a Lord - Goddard")
	public static NpcStringId ID_71501;
	
	@NpcString(id = 71502, message = "Path to Becoming a Lord - Goddard (In Progress)")
	public static NpcStringId ID_71502;
	
	@NpcString(id = 71551, message = "%s, I will remember you.")
	public static NpcStringId ID_71551;
	
	@NpcString(id = 71601, message = "Path to Becoming a Lord - Rune")
	public static NpcStringId ID_71601;
	
	@NpcString(id = 71602, message = "Path to Becoming a Lord - Rune (In Progress)")
	public static NpcStringId ID_71602;
	
	@NpcString(id = 71652, message = "%s. Now, depart!")
	public static NpcStringId ID_71652;
	
	@NpcString(id = 71653, message = "Frederick is looking for you, my liege.")
	public static NpcStringId ID_71653;
	
	@NpcString(id = 71654, message = "Ho ho! Did you think you could really stop trading with us?")
	public static NpcStringId ID_71654;
	
	@NpcString(id = 71655, message = "You have charged into the temple.")
	public static NpcStringId ID_71655;
	
	@NpcString(id = 71656, message = "You are in the midst of dealing with the heretic of Heretic Temple.")
	public static NpcStringId ID_71656;
	
	@NpcString(id = 71657, message = "The Heretic Temple is descending into chaos.")
	public static NpcStringId ID_71657;
	
	@NpcString(id = 71659, message = "%s has become the lord of the Town of Rune. May there be glory in the territory of Rune!")
	public static NpcStringId ID_71659;
	
	@NpcString(id = 71701, message = "For the Sake of the Territory - Gludio")
	public static NpcStringId ID_71701;
	
	@NpcString(id = 71702, message = "For the Sake of the Territory - Gludio (In Progress)")
	public static NpcStringId ID_71702;
	
	@NpcString(id = 71751, message = "%s! Raise your weapons for the sake of the territory!")
	public static NpcStringId ID_71751;
	
	@NpcString(id = 71752, message = "%s! The war is over. Lower your sword for the sake of the future.")
	public static NpcStringId ID_71752;
	
	@NpcString(id = 71753, message = "%s Territory Badge(s) and %s Adenas")
	public static NpcStringId ID_71753;
	
	@NpcString(id = 71754, message = "90 Territory Badges, %s score(s) in Individual Fame and %s Adenas")
	public static NpcStringId ID_71754;
	
	@NpcString(id = 71755, message = "90 Territory Badges, 450 scores in Individual Fame and %s Adenas")
	public static NpcStringId ID_71755;
	
	@NpcString(id = 71801, message = "For the Sake of the Territory - Dion")
	public static NpcStringId ID_71801;
	
	@NpcString(id = 71802, message = "For the Sake of the Territory - Dion (In Progress)")
	public static NpcStringId ID_71802;
	
	@NpcString(id = 71901, message = "For the Sake of the Territory - Giran")
	public static NpcStringId ID_71901;
	
	@NpcString(id = 71902, message = "For the Sake of the Territory - Giran (In Progress)")
	public static NpcStringId ID_71902;
	
	@NpcString(id = 72001, message = "For the Sake of the Territory - Innadril")
	public static NpcStringId ID_72001;
	
	@NpcString(id = 72002, message = "For the Sake of the Territory - Innadril (In Progress)")
	public static NpcStringId ID_72002;
	
	@NpcString(id = 72101, message = "For the Sake of the Territory - Oren")
	public static NpcStringId ID_72101;
	
	@NpcString(id = 72102, message = "For the Sake of the Territory - Oren (In Progress)")
	public static NpcStringId ID_72102;
	
	@NpcString(id = 72201, message = "For the Sake of the Territory - Aden")
	public static NpcStringId ID_72201;
	
	@NpcString(id = 72202, message = "For the Sake of the Territory - Aden (In Progress)")
	public static NpcStringId ID_72202;
	
	@NpcString(id = 72301, message = "For the Sake of the Territory - Schuttgart")
	public static NpcStringId ID_72301;
	
	@NpcString(id = 72302, message = "For the Sake of the Territory - Schuttgart (In Progress)")
	public static NpcStringId ID_72302;
	
	@NpcString(id = 72401, message = "For the Sake of the Territory - Goddard")
	public static NpcStringId ID_72401;
	
	@NpcString(id = 72402, message = "For the Sake of the Territory - Goddard (In Progress)")
	public static NpcStringId ID_72402;
	
	@NpcString(id = 72501, message = "For the Sake of the Territory - Rune")
	public static NpcStringId ID_72501;
	
	@NpcString(id = 72502, message = "For the Sake of the Territory - Rune (In Progress)")
	public static NpcStringId ID_72502;
	
	@NpcString(id = 72601, message = "Light within the Darkness")
	public static NpcStringId ID_72601;
	
	@NpcString(id = 72602, message = "Light within the Darkness (In Progress)")
	public static NpcStringId ID_72602;
	
	@NpcString(id = 72701, message = "Hope within the Darkness")
	public static NpcStringId ID_72701;
	
	@NpcString(id = 72702, message = "Hope within the Darkness (In Progress)")
	public static NpcStringId ID_72702;
	
	@NpcString(id = 72901, message = "Protect the Territory Catapult!")
	public static NpcStringId ID_72901;
	
	@NpcString(id = 72902, message = "Protect the Territory Catapult! (In Progress)")
	public static NpcStringId ID_72902;
	
	@NpcString(id = 90101, message = "How a Lavasaurus is Made")
	public static NpcStringId ID_90101;
	
	@NpcString(id = 90102, message = "How a Lavasaurus is Made (In Progress)")
	public static NpcStringId ID_90102;
	
	@NpcString(id = 90103, message = "How a Lavasaurus is Made (Done)")
	public static NpcStringId ID_90103;
	
	@NpcString(id = 90201, message = "Reclaim Our Era")
	public static NpcStringId ID_90201;
	
	@NpcString(id = 90202, message = "Reclaim Our Era (In Progress)")
	public static NpcStringId ID_90202;
	
	@NpcString(id = 90203, message = "Reclaim Our Era (Done)")
	public static NpcStringId ID_90203;
	
	@NpcString(id = 90301, message = "The Call of Antharas")
	public static NpcStringId ID_90301;
	
	@NpcString(id = 90302, message = "The Call of Antharas (In Progress)")
	public static NpcStringId ID_90302;
	
	@NpcString(id = 90303, message = "The Call of Antharas (Done)")
	public static NpcStringId ID_90303;
	
	@NpcString(id = 90401, message = "Dragon Trophy - Antharas")
	public static NpcStringId ID_90401;
	
	@NpcString(id = 90402, message = "Dragon Trophy - Antharas (In Progress)")
	public static NpcStringId ID_90402;
	
	@NpcString(id = 90403, message = "Dragon Trophy - Antharas (Done)")
	public static NpcStringId ID_90403;
	
	@NpcString(id = 90501, message = "Refined Dragon Blood")
	public static NpcStringId ID_90501;
	
	@NpcString(id = 90502, message = "Refined Dragon Blood (In Progress)")
	public static NpcStringId ID_90502;
	
	@NpcString(id = 90503, message = "Refined Dragon Blood (Done)")
	public static NpcStringId ID_90503;
	
	@NpcString(id = 90601, message = "The Call of Valakas")
	public static NpcStringId ID_90601;
	
	@NpcString(id = 90602, message = "The Call of Valakas (In Progress)")
	public static NpcStringId ID_90602;
	
	@NpcString(id = 90603, message = "The Call of Valakas (Done)")
	public static NpcStringId ID_90603;
	
	@NpcString(id = 90701, message = "Dragon Trophy - Valakas")
	public static NpcStringId ID_90701;
	
	@NpcString(id = 90702, message = "Dragon Trophy - Valakas (In Progress)")
	public static NpcStringId ID_90702;
	
	@NpcString(id = 90703, message = "Dragon Trophy - Valakas (Done)")
	public static NpcStringId ID_90703;
	
	@NpcString(id = 99201, message = "Broken ring, part 2")
	public static NpcStringId ID_99201;
	
	@NpcString(id = 99202, message = "Broken ring, part 2 (In Progress)")
	public static NpcStringId ID_99202;
	
	@NpcString(id = 99203, message = "Broken ring, part 2 (Done)")
	public static NpcStringId ID_99203;
	
	@NpcString(id = 99301, message = "Broken ring, part 1")
	public static NpcStringId ID_99301;
	
	@NpcString(id = 99302, message = "Broken ring, part 1 (In Progress)")
	public static NpcStringId ID_99302;
	
	@NpcString(id = 99303, message = "Broken ring, part 1 (Done)")
	public static NpcStringId ID_99303;
	
	@NpcString(id = 99601, message = "Tra la la... Today, I'm going to make another fun-filled trip.  I wonder what I should look for this time...")
	public static NpcStringId ID_99601;
	
	@NpcString(id = 99700, message = "What's this? Why am I being disturbed?")
	public static NpcStringId ID_99700;
	
	@NpcString(id = 99701, message = "Ta-da! Here I am!")
	public static NpcStringId ID_99701;
	
	@NpcString(id = 99702, message = "What are you looking at?")
	public static NpcStringId ID_99702;
	
	@NpcString(id = 99703, message = "If you give me nectar, this little squash will grow up quickly!")
	public static NpcStringId ID_99703;
	
	@NpcString(id = 99704, message = "Are you my mommy?")
	public static NpcStringId ID_99704;
	
	@NpcString(id = 99705, message = "Fancy meeting you here!")
	public static NpcStringId ID_99705;
	
	@NpcString(id = 99706, message = "Are you afraid of the big-bad squash?")
	public static NpcStringId ID_99706;
	
	@NpcString(id = 99707, message = "Impressive, aren't I?")
	public static NpcStringId ID_99707;
	
	@NpcString(id = 99708, message = "Obey me!!")
	public static NpcStringId ID_99708;
	
	@NpcString(id = 99709, message = "Raise me well and you'll be rewarded. Neglect me and suffer the consequences!")
	public static NpcStringId ID_99709;
	
	@NpcString(id = 99710, message = "Transform!")
	public static NpcStringId ID_99710;
	
	@NpcString(id = 99711, message = "I feel different?")
	public static NpcStringId ID_99711;
	
	@NpcString(id = 99712, message = "I'm bigger now! Bring it on!")
	public static NpcStringId ID_99712;
	
	@NpcString(id = 99713, message = "I'm not a kid anymore!")
	public static NpcStringId ID_99713;
	
	@NpcString(id = 99714, message = "Big time!")
	public static NpcStringId ID_99714;
	
	@NpcString(id = 99715, message = "Good luck!")
	public static NpcStringId ID_99715;
	
	@NpcString(id = 99716, message = "I'm all grown up now!")
	public static NpcStringId ID_99716;
	
	@NpcString(id = 99717, message = "If you let me go, I'll be your best friend.")
	public static NpcStringId ID_99717;
	
	@NpcString(id = 99718, message = "I'm chuck full of goodness!")
	public static NpcStringId ID_99718;
	
	@NpcString(id = 99719, message = "Good job! Now what are you going to do?")
	public static NpcStringId ID_99719;
	
	@NpcString(id = 99720, message = "Keep it coming!")
	public static NpcStringId ID_99720;
	
	@NpcString(id = 99721, message = "That's what I'm talking about!")
	public static NpcStringId ID_99721;
	
	@NpcString(id = 99722, message = "May I have some more?")
	public static NpcStringId ID_99722;
	
	@NpcString(id = 99723, message = "That hit the spot!")
	public static NpcStringId ID_99723;
	
	@NpcString(id = 99724, message = "I feel special!")
	public static NpcStringId ID_99724;
	
	@NpcString(id = 99725, message = "I think it's working!")
	public static NpcStringId ID_99725;
	
	@NpcString(id = 99726, message = "You DO understand!")
	public static NpcStringId ID_99726;
	
	@NpcString(id = 99727, message = "Yuck! What is this? Ha Ha just kidding!")
	public static NpcStringId ID_99727;
	
	@NpcString(id = 99728, message = "A total of five and I'll be twice as alive!")
	public static NpcStringId ID_99728;
	
	@NpcString(id = 99729, message = "Nectar is sublime!")
	public static NpcStringId ID_99729;
	
	@NpcString(id = 99730, message = "You call that a hit?")
	public static NpcStringId ID_99730;
	
	@NpcString(id = 99731, message = "Why are you hitting me? Ouch, stop it! Give me nectar!")
	public static NpcStringId ID_99731;
	
	@NpcString(id = 99732, message = "Stop or I'll wilt!")
	public static NpcStringId ID_99732;
	
	@NpcString(id = 99733, message = "I'm not fully grown yet! Oh well, do what you will. I'll fade away without nectar anyway!")
	public static NpcStringId ID_99733;
	
	@NpcString(id = 99734, message = "Go ahead and hit me again. It won't do you any good!")
	public static NpcStringId ID_99734;
	
	@NpcString(id = 99735, message = "Woe is me! I'm wilting!")
	public static NpcStringId ID_99735;
	
	@NpcString(id = 99736, message = "I'm not fully grown yet! How about some nectar to ease my pain?")
	public static NpcStringId ID_99736;
	
	@NpcString(id = 99737, message = "The end is near!")
	public static NpcStringId ID_99737;
	
	@NpcString(id = 99738, message = "Pretty please... with sugar on top, give me some nectar!")
	public static NpcStringId ID_99738;
	
	@NpcString(id = 99739, message = "If I die without nectar, you'll get nothing.")
	public static NpcStringId ID_99739;
	
	@NpcString(id = 99740, message = "I'm feeling better! Another thirty seconds and I'll be out of here!")
	public static NpcStringId ID_99740;
	
	@NpcString(id = 99741, message = "Twenty seconds and it's ciao, baby!")
	public static NpcStringId ID_99741;
	
	@NpcString(id = 99742, message = "Woohoo, just ten seconds left! nine... eight... seven!")
	public static NpcStringId ID_99742;
	
	@NpcString(id = 99743, message = "Give me nectar or I'll be gone in two minutes!")
	public static NpcStringId ID_99743;
	
	@NpcString(id = 99744, message = "Give me nectar or I'll be gone in one minute!")
	public static NpcStringId ID_99744;
	
	@NpcString(id = 99745, message = "So long suckers!")
	public static NpcStringId ID_99745;
	
	@NpcString(id = 99746, message = "I'm out of here!")
	public static NpcStringId ID_99746;
	
	@NpcString(id = 99747, message = "I must be going! Have fun everybody!")
	public static NpcStringId ID_99747;
	
	@NpcString(id = 99748, message = "Time is up! Put your weapons down!")
	public static NpcStringId ID_99748;
	
	@NpcString(id = 99749, message = "Good for me, bad for you!")
	public static NpcStringId ID_99749;
	
	@NpcString(id = 99750, message = "Soundtastic!")
	public static NpcStringId ID_99750;
	
	@NpcString(id = 99751, message = "I can sing along if you like?")
	public static NpcStringId ID_99751;
	
	@NpcString(id = 99752, message = "I think you need some backup!")
	public static NpcStringId ID_99752;
	
	@NpcString(id = 99753, message = "Keep up that rhythm and you'll be a star!")
	public static NpcStringId ID_99753;
	
	@NpcString(id = 99754, message = "My heart yearns for more music.")
	public static NpcStringId ID_99754;
	
	@NpcString(id = 99755, message = "You're out of tune again!")
	public static NpcStringId ID_99755;
	
	@NpcString(id = 99756, message = "This is awful!")
	public static NpcStringId ID_99756;
	
	@NpcString(id = 99757, message = "I think I broke something!")
	public static NpcStringId ID_99757;
	
	@NpcString(id = 99758, message = "What a lovely melody! Play it again!")
	public static NpcStringId ID_99758;
	
	@NpcString(id = 99759, message = "Music to my, uh... ears!")
	public static NpcStringId ID_99759;
	
	@NpcString(id = 99760, message = "You need music lessons!")
	public static NpcStringId ID_99760;
	
	@NpcString(id = 99761, message = "I can't hear you!")
	public static NpcStringId ID_99761;
	
	@NpcString(id = 99762, message = "You can't hurt me like that!")
	public static NpcStringId ID_99762;
	
	@NpcString(id = 99763, message = "I'm stronger than you are!")
	public static NpcStringId ID_99763;
	
	@NpcString(id = 99764, message = "No music? I'm out of here!")
	public static NpcStringId ID_99764;
	
	@NpcString(id = 99765, message = "That racket is getting on my nerves! Tone it down a bit!")
	public static NpcStringId ID_99765;
	
	@NpcString(id = 99766, message = "You can only hurt me through music!")
	public static NpcStringId ID_99766;
	
	@NpcString(id = 99767, message = "Only musical instruments can hurt me! Nothing else!")
	public static NpcStringId ID_99767;
	
	@NpcString(id = 99768, message = "Your skills are impressive, but sadly, useless...")
	public static NpcStringId ID_99768;
	
	@NpcString(id = 99769, message = "Catch a Chrono for me please.")
	public static NpcStringId ID_99769;
	
	@NpcString(id = 99770, message = "You got me!")
	public static NpcStringId ID_99770;
	
	@NpcString(id = 99771, message = "Now look at what you've done!")
	public static NpcStringId ID_99771;
	
	@NpcString(id = 99772, message = "You win!")
	public static NpcStringId ID_99772;
	
	@NpcString(id = 99773, message = "Squashed......")
	public static NpcStringId ID_99773;
	
	@NpcString(id = 99774, message = "Don't tell anyone!")
	public static NpcStringId ID_99774;
	
	@NpcString(id = 99775, message = "Gross! My guts are coming out!")
	public static NpcStringId ID_99775;
	
	@NpcString(id = 99776, message = "Take it and go.")
	public static NpcStringId ID_99776;
	
	@NpcString(id = 99777, message = "I should've left when I could!")
	public static NpcStringId ID_99777;
	
	@NpcString(id = 99778, message = "Now look what you have done!")
	public static NpcStringId ID_99778;
	
	@NpcString(id = 99779, message = "I feel dirty!")
	public static NpcStringId ID_99779;
	
	@NpcString(id = 99780, message = "Better luck next time!")
	public static NpcStringId ID_99780;
	
	@NpcString(id = 99781, message = "Nice shot!")
	public static NpcStringId ID_99781;
	
	@NpcString(id = 99782, message = "I'm not afraid of you!")
	public static NpcStringId ID_99782;
	
	@NpcString(id = 99783, message = "If I knew this was going to happen, I would have stayed home!")
	public static NpcStringId ID_99783;
	
	@NpcString(id = 99784, message = "Try harder or I'm out of here!")
	public static NpcStringId ID_99784;
	
	@NpcString(id = 99785, message = "I'm tougher than I look!")
	public static NpcStringId ID_99785;
	
	@NpcString(id = 99786, message = "Good strike!")
	public static NpcStringId ID_99786;
	
	@NpcString(id = 99787, message = "Oh my gourd!")
	public static NpcStringId ID_99787;
	
	@NpcString(id = 99788, message = "That's all you've got?")
	public static NpcStringId ID_99788;
	
	@NpcString(id = 99789, message = "Why me?")
	public static NpcStringId ID_99789;
	
	@NpcString(id = 99790, message = "Bring me nectar!")
	public static NpcStringId ID_99790;
	
	@NpcString(id = 99791, message = "I must have nectar to grow!")
	public static NpcStringId ID_99791;
	
	@NpcString(id = 99792, message = "Give me some nectar quickly or you'll get nothing!")
	public static NpcStringId ID_99792;
	
	@NpcString(id = 99793, message = "Please give me some nectar! I'm hungry!")
	public static NpcStringId ID_99793;
	
	@NpcString(id = 99794, message = "Nectar please!")
	public static NpcStringId ID_99794;
	
	@NpcString(id = 99795, message = "Nectar will make me grow quickly!")
	public static NpcStringId ID_99795;
	
	@NpcString(id = 99796, message = "Don't you want a bigger squash? Give me some nectar and I'll grow much larger!")
	public static NpcStringId ID_99796;
	
	@NpcString(id = 99797, message = "If you raise me well, you'll get prizes! Or not...")
	public static NpcStringId ID_99797;
	
	@NpcString(id = 99798, message = "You are here for the stuff, eh? Well it's mine, all mine!")
	public static NpcStringId ID_99798;
	
	@NpcString(id = 99799, message = "Trust me... give me nectar and I'll become a giant squash!")
	public static NpcStringId ID_99799;
	
	@NpcString(id = 526701, message = "Journey to Gracia")
	public static NpcStringId ID_526701;
	
	@NpcString(id = 526702, message = "Journey to Gracia (In Progress)")
	public static NpcStringId ID_526702;
	
	@NpcString(id = 526703, message = "Journey to Gracia (Done)")
	public static NpcStringId ID_526703;
	
	@NpcString(id = 526801, message = "To the Seed of Infinity")
	public static NpcStringId ID_526801;
	
	@NpcString(id = 526802, message = "To the Seed of Infinity (In Progress)")
	public static NpcStringId ID_526802;
	
	@NpcString(id = 526803, message = "To the Seed of Infinity (Done)")
	public static NpcStringId ID_526803;
	
	@NpcString(id = 526901, message = "To the Seed of Destruction")
	public static NpcStringId ID_526901;
	
	@NpcString(id = 526902, message = "To the Seed of Destruction (In Progress)")
	public static NpcStringId ID_526902;
	
	@NpcString(id = 526903, message = "To the Seed of Destruction (Done)")
	public static NpcStringId ID_526903;
	
	@NpcString(id = 527001, message = "Birth of the Seed")
	public static NpcStringId ID_527001;
	
	@NpcString(id = 527002, message = "Birth of the Seed (In Progress)")
	public static NpcStringId ID_527002;
	
	@NpcString(id = 527003, message = "Birth of the Seed (Done)")
	public static NpcStringId ID_527003;
	
	@NpcString(id = 527101, message = "The Enveloping Darkness")
	public static NpcStringId ID_527101;
	
	@NpcString(id = 527102, message = "The Enveloping Darkness (In Progress)")
	public static NpcStringId ID_527102;
	
	@NpcString(id = 527103, message = "The Enveloping Darkness (Done)")
	public static NpcStringId ID_527103;
	
	@NpcString(id = 527201, message = "Light Fragment")
	public static NpcStringId ID_527201;
	
	@NpcString(id = 527202, message = "Light Fragment (In Progress)")
	public static NpcStringId ID_527202;
	
	@NpcString(id = 527203, message = "Light Fragment (Done)")
	public static NpcStringId ID_527203;
	
	@NpcString(id = 527301, message = "Good Day to Fly")
	public static NpcStringId ID_527301;
	
	@NpcString(id = 527302, message = "Good Day to Fly (In Progress)")
	public static NpcStringId ID_527302;
	
	@NpcString(id = 527303, message = "Good Day to Fly (Done)")
	public static NpcStringId ID_527303;
	
	@NpcString(id = 527401, message = "Collecting in the Air")
	public static NpcStringId ID_527401;
	
	@NpcString(id = 527402, message = "Collecting in the Air (In Progress)")
	public static NpcStringId ID_527402;
	
	@NpcString(id = 527403, message = "Collecting in the Air (Done)")
	public static NpcStringId ID_527403;
	
	@NpcString(id = 527501, message = "Containing the Attribute Power")
	public static NpcStringId ID_527501;
	
	@NpcString(id = 527502, message = "Containing the Attribute Power (In Progress)")
	public static NpcStringId ID_527502;
	
	@NpcString(id = 527503, message = "Containing the Attribute Power (Done)")
	public static NpcStringId ID_527503;
	
	@NpcString(id = 527601, message = "Mutated Kaneus - Gludio")
	public static NpcStringId ID_527601;
	
	@NpcString(id = 527602, message = "Mutated Kaneus - Gludio (In Progress)")
	public static NpcStringId ID_527602;
	
	@NpcString(id = 527603, message = "Mutated Kaneus - Gludio (Done)")
	public static NpcStringId ID_527603;
	
	@NpcString(id = 527701, message = "Mutated Kaneus - Dion")
	public static NpcStringId ID_527701;
	
	@NpcString(id = 527702, message = "Mutated Kaneus - Dion (In Progress)")
	public static NpcStringId ID_527702;
	
	@NpcString(id = 527703, message = "Mutated Kaneus - Dion (Done)")
	public static NpcStringId ID_527703;
	
	@NpcString(id = 527801, message = "Mutated Kaneus - Heine")
	public static NpcStringId ID_527801;
	
	@NpcString(id = 527802, message = "Mutated Kaneus - Heine (In Progress)")
	public static NpcStringId ID_527802;
	
	@NpcString(id = 527803, message = "Mutated Kaneus - Heine (Done)")
	public static NpcStringId ID_527803;
	
	@NpcString(id = 527901, message = "Mutated Kaneus - Oren")
	public static NpcStringId ID_527901;
	
	@NpcString(id = 527902, message = "Mutated Kaneus - Oren (In Progress)")
	public static NpcStringId ID_527902;
	
	@NpcString(id = 527903, message = "Mutated Kaneus - Oren (Done)")
	public static NpcStringId ID_527903;
	
	@NpcString(id = 528001, message = "Mutated Kaneus - Schuttgart")
	public static NpcStringId ID_528001;
	
	@NpcString(id = 528002, message = "Mutated Kaneus - Schuttgart (In Progress)")
	public static NpcStringId ID_528002;
	
	@NpcString(id = 528003, message = "Mutated Kaneus - Schuttgart (Done)")
	public static NpcStringId ID_528003;
	
	@NpcString(id = 528101, message = "Mutated Kaneus - Rune")
	public static NpcStringId ID_528101;
	
	@NpcString(id = 528102, message = "Mutated Kaneus - Rune (In Progress)")
	public static NpcStringId ID_528102;
	
	@NpcString(id = 528103, message = "Mutated Kaneus - Rune (Done)")
	public static NpcStringId ID_528103;
	
	@NpcString(id = 528201, message = "To the Seed of Annihilation")
	public static NpcStringId ID_528201;
	
	@NpcString(id = 528202, message = "To the Seed of Annihilation (In Progress)")
	public static NpcStringId ID_528202;
	
	@NpcString(id = 528203, message = "To the Seed of Annihilation (Done)")
	public static NpcStringId ID_528203;
	
	@NpcString(id = 528301, message = "Request of Ice Merchant")
	public static NpcStringId ID_528301;
	
	@NpcString(id = 528302, message = "Request of Ice Merchant (In Progress)")
	public static NpcStringId ID_528302;
	
	@NpcString(id = 528303, message = "Request of Ice Merchant (Done)")
	public static NpcStringId ID_528303;
	
	@NpcString(id = 528401, message = "Acquisition of Divine Sword")
	public static NpcStringId ID_528401;
	
	@NpcString(id = 528402, message = "Acquisition of Divine Sword (In Progress)")
	public static NpcStringId ID_528402;
	
	@NpcString(id = 528403, message = "Acquisition of Divine Sword (Done)")
	public static NpcStringId ID_528403;
	
	@NpcString(id = 528501, message = "Meeting Sirra")
	public static NpcStringId ID_528501;
	
	@NpcString(id = 528502, message = "Meeting Sirra (In Progress)")
	public static NpcStringId ID_528502;
	
	@NpcString(id = 528503, message = "Meeting Sirra (Done)")
	public static NpcStringId ID_528503;
	
	@NpcString(id = 528601, message = "Reunion with Sirra")
	public static NpcStringId ID_528601;
	
	@NpcString(id = 528602, message = "Reunion with Sirra (In Progress)")
	public static NpcStringId ID_528602;
	
	@NpcString(id = 528603, message = "Reunion with Sirra (Done)")
	public static NpcStringId ID_528603;
	
	@NpcString(id = 528701, message = "Story of Those Left")
	public static NpcStringId ID_528701;
	
	@NpcString(id = 528702, message = "Story of Those Left (In Progress)")
	public static NpcStringId ID_528702;
	
	@NpcString(id = 528703, message = "Story of Those Left (Done)")
	public static NpcStringId ID_528703;
	
	@NpcString(id = 528801, message = "Secret Mission")
	public static NpcStringId ID_528801;
	
	@NpcString(id = 528802, message = "Secret Mission (In Progress)")
	public static NpcStringId ID_528802;
	
	@NpcString(id = 528803, message = "Secret Mission (Done)")
	public static NpcStringId ID_528803;
	
	@NpcString(id = 528901, message = "Fade to Black")
	public static NpcStringId ID_528901;
	
	@NpcString(id = 528902, message = "Fade to Black (In Progress)")
	public static NpcStringId ID_528902;
	
	@NpcString(id = 528903, message = "Fade to Black (Done)")
	public static NpcStringId ID_528903;
	
	@NpcString(id = 529001, message = "Land Dragon Conqueror")
	public static NpcStringId ID_529001;
	
	@NpcString(id = 529002, message = "Land Dragon Conqueror (In Progress)")
	public static NpcStringId ID_529002;
	
	@NpcString(id = 529003, message = "Land Dragon Conqueror (Done)")
	public static NpcStringId ID_529003;
	
	@NpcString(id = 529101, message = "Fire Dragon Destroyer")
	public static NpcStringId ID_529101;
	
	@NpcString(id = 529102, message = "Fire Dragon Destroyer (In Progress)")
	public static NpcStringId ID_529102;
	
	@NpcString(id = 529103, message = "Fire Dragon Destroyer (Done)")
	public static NpcStringId ID_529103;
	
	@NpcString(id = 529201, message = "Seven Signs, Girl of Doubt")
	public static NpcStringId ID_529201;
	
	@NpcString(id = 529202, message = "Seven Signs, Girl of Doubt (In Progress)")
	public static NpcStringId ID_529202;
	
	@NpcString(id = 529203, message = "Seven Signs, Girl of Doubt (Done)")
	public static NpcStringId ID_529203;
	
	@NpcString(id = 529301, message = "Seven Signs, Forbidden Book of the Elmore-Aden Kingdom")
	public static NpcStringId ID_529301;
	
	@NpcString(id = 529302, message = "Seven Signs, Forbidden Book of the Elmore-Aden Kingdom (In Progress)")
	public static NpcStringId ID_529302;
	
	@NpcString(id = 529303, message = "Seven Signs, Forbidden Book of the Elmore-Aden Kingdom (Done)")
	public static NpcStringId ID_529303;
	
	@NpcString(id = 529401, message = "Seven Signs, To the Monastery of Silence")
	public static NpcStringId ID_529401;
	
	@NpcString(id = 529402, message = "Seven Signs, To the Monastery of Silence (In Progress)")
	public static NpcStringId ID_529402;
	
	@NpcString(id = 529403, message = "Seven Signs, To the Monastery of Silence (Done)")
	public static NpcStringId ID_529403;
	
	@NpcString(id = 529501, message = "Seven Signs, Solina Tomb")
	public static NpcStringId ID_529501;
	
	@NpcString(id = 529502, message = "Seven Signs, Solina Tomb (In Progress)")
	public static NpcStringId ID_529502;
	
	@NpcString(id = 529503, message = "Seven Signs, Solina Tomb (Done)")
	public static NpcStringId ID_529503;
	
	@NpcString(id = 529601, message = "Seven Signs, One Who Seeks the Power of the Seal")
	public static NpcStringId ID_529601;
	
	@NpcString(id = 529602, message = "Seven Signs, One Who Seeks the Power of the Seal (In Progress)")
	public static NpcStringId ID_529602;
	
	@NpcString(id = 529603, message = "Seven Signs, One Who Seeks the Power of the Seal (Done)")
	public static NpcStringId ID_529603;
	
	@NpcString(id = 550401, message = "Jewel of Antharas")
	public static NpcStringId ID_550401;
	
	@NpcString(id = 550402, message = "Jewel of Antharas (In Progress)")
	public static NpcStringId ID_550402;
	
	@NpcString(id = 550403, message = "Jewel of Antharas (Done)")
	public static NpcStringId ID_550403;
	
	@NpcString(id = 550501, message = "Jewel of Valakas")
	public static NpcStringId ID_550501;
	
	@NpcString(id = 550502, message = "Jewel of Valakas (In Progress)")
	public static NpcStringId ID_550502;
	
	@NpcString(id = 550503, message = "Jewel of Valakas (Done)")
	public static NpcStringId ID_550503;
	
	@NpcString(id = 1000001, message = "A non-permitted target has been discovered.")
	public static NpcStringId ID_1000001;
	
	@NpcString(id = 1000002, message = "Intruder removal system initiated.")
	public static NpcStringId ID_1000002;
	
	@NpcString(id = 1000003, message = "Removing intruders.")
	public static NpcStringId ID_1000003;
	
	@NpcString(id = 1000004, message = "A fatal error has occurred.")
	public static NpcStringId ID_1000004;
	
	@NpcString(id = 1000005, message = "System is being shut down...")
	public static NpcStringId ID_1000005;
	
	@NpcString(id = 1000006, message = "......")
	public static NpcStringId ID_1000006;
	
	@NpcString(id = 1000007, message = "We shall see about that!")
	public static NpcStringId ID_1000007;
	
	@NpcString(id = 1000008, message = "I will definitely repay this humiliation!")
	public static NpcStringId ID_1000008;
	
	@NpcString(id = 1000009, message = "Retreat!")
	public static NpcStringId ID_1000009;
	
	@NpcString(id = 1000010, message = "Tactical retreat!")
	public static NpcStringId ID_1000010;
	
	@NpcString(id = 1000011, message = "Mass fleeing!")
	public static NpcStringId ID_1000011;
	
	@NpcString(id = 1000012, message = "It's stronger than expected!")
	public static NpcStringId ID_1000012;
	
	@NpcString(id = 1000013, message = "I'll kill you next time!")
	public static NpcStringId ID_1000013;
	
	@NpcString(id = 1000014, message = "I'll definitely kill you next time!")
	public static NpcStringId ID_1000014;
	
	@NpcString(id = 1000015, message = "Oh! How strong!")
	public static NpcStringId ID_1000015;
	
	@NpcString(id = 1000016, message = "Invader!")
	public static NpcStringId ID_1000016;
	
	@NpcString(id = 1000017, message = "There is no reason for you to kill me! I have nothing you need!")
	public static NpcStringId ID_1000017;
	
	@NpcString(id = 1000018, message = "Someday you will pay!")
	public static NpcStringId ID_1000018;
	
	@NpcString(id = 1000019, message = "I won't just stand still while you hit me.")
	public static NpcStringId ID_1000019;
	
	@NpcString(id = 1000020, message = "Stop hitting!")
	public static NpcStringId ID_1000020;
	
	@NpcString(id = 1000021, message = "It hurts to the bone!")
	public static NpcStringId ID_1000021;
	
	@NpcString(id = 1000022, message = "Am I the neighborhood drum for beating!")
	public static NpcStringId ID_1000022;
	
	@NpcString(id = 1000023, message = "Follow me if you want!")
	public static NpcStringId ID_1000023;
	
	@NpcString(id = 1000024, message = "Surrender!")
	public static NpcStringId ID_1000024;
	
	@NpcString(id = 1000025, message = "Oh, I'm dead!")
	public static NpcStringId ID_1000025;
	
	@NpcString(id = 1000026, message = "I'll be back!")
	public static NpcStringId ID_1000026;
	
	@NpcString(id = 1000027, message = "I'll give you ten million adena if you let me live!")
	public static NpcStringId ID_1000027;
	
	@NpcString(id = 1000028, message = "%s. Stop kidding yourself about your own powerlessness!")
	public static NpcStringId ID_1000028;
	
	@NpcString(id = 1000029, message = "%s. I'll make you feel what true fear is!")
	public static NpcStringId ID_1000029;
	
	@NpcString(id = 1000030, message = "You're really stupid to have challenged me. %s! Get ready!")
	public static NpcStringId ID_1000030;
	
	@NpcString(id = 1000031, message = "%s. Do you think that's going to work?!")
	public static NpcStringId ID_1000031;
	
	@NpcString(id = 1000032, message = "I will definitely reclaim my honor which has been tarnished!")
	public static NpcStringId ID_1000032;
	
	@NpcString(id = 1000033, message = "Show me the wrath of the knight whose honor has been downtrodden!")
	public static NpcStringId ID_1000033;
	
	@NpcString(id = 1000034, message = "Death to the hypocrite!")
	public static NpcStringId ID_1000034;
	
	@NpcString(id = 1000035, message = "I'll never sleep until I've shed my dishonor!")
	public static NpcStringId ID_1000035;
	
	@NpcString(id = 1000036, message = "I'm here for the ones that are cursing the world!")
	public static NpcStringId ID_1000036;
	
	@NpcString(id = 1000037, message = "I'll turn you into a malignant spirit!")
	public static NpcStringId ID_1000037;
	
	@NpcString(id = 1000038, message = "I'll curse you with the power of revenge and hate!")
	public static NpcStringId ID_1000038;
	
	@NpcString(id = 1000039, message = "For the glory of Gracia!")
	public static NpcStringId ID_1000039;
	
	@NpcString(id = 1000040, message = "Do you dare pit your power against me?")
	public static NpcStringId ID_1000040;
	
	@NpcString(id = 1000041, message = "I... I am defeated!!!")
	public static NpcStringId ID_1000041;
	
	@NpcString(id = 1000042, message = "I am conveying the will of Nurka! Everybody get out of my way!")
	public static NpcStringId ID_1000042;
	
	@NpcString(id = 1000043, message = "Those who stand against me shall die horribly!")
	public static NpcStringId ID_1000043;
	
	@NpcString(id = 1000044, message = "Do you dare to block my way?!")
	public static NpcStringId ID_1000044;
	
	@NpcString(id = 1000045, message = "My comrades will get revenge!")
	public static NpcStringId ID_1000045;
	
	@NpcString(id = 1000046, message = "You heathen blasphemers of this holy place will be punished!")
	public static NpcStringId ID_1000046;
	
	@NpcString(id = 1000047, message = "Step forward, you worthless creatures who challenge my authority!")
	public static NpcStringId ID_1000047;
	
	@NpcString(id = 1000048, message = "My creator... The unchanging faithfulness to my master...")
	public static NpcStringId ID_1000048;
	
	@NpcString(id = 1000049, message = "Master of the tower... My master... master... Where is he?")
	public static NpcStringId ID_1000049;
	
	@NpcString(id = 1000050, message = "I AM THE ONE CARRYING OUT THE WILL OF CORE.")
	public static NpcStringId ID_1000050;
	
	@NpcString(id = 1000051, message = "DESTROY THE INVADER.")
	public static NpcStringId ID_1000051;
	
	@NpcString(id = 1000052, message = "STRANGE CONDITION - DOESN'T WORK")
	public static NpcStringId ID_1000052;
	
	@NpcString(id = 1000053, message = "According to the command of Beleth... I'm going to observe you guys!")
	public static NpcStringId ID_1000053;
	
	@NpcString(id = 1000054, message = "You people make me sick! No sense of loyalty whatsoever!")
	public static NpcStringId ID_1000054;
	
	@NpcString(id = 1000055, message = "A challenge against me is the same as a challenge against Beleth...")
	public static NpcStringId ID_1000055;
	
	@NpcString(id = 1000056, message = "Beleth is always watching over you guys!")
	public static NpcStringId ID_1000056;
	
	@NpcString(id = 1000057, message = "That was really close! Antharas opened its eyes!")
	public static NpcStringId ID_1000057;
	
	@NpcString(id = 1000058, message = "You who disobey the will of Antharas! Die!")
	public static NpcStringId ID_1000058;
	
	@NpcString(id = 1000059, message = "Antharas has taken my life!")
	public static NpcStringId ID_1000059;
	
	@NpcString(id = 1000060, message = "I crossed back over the marshlands of death to reclaim the treasure!")
	public static NpcStringId ID_1000060;
	
	@NpcString(id = 1000061, message = "Bring over and surrender your precious gold treasure to me!")
	public static NpcStringId ID_1000061;
	
	@NpcString(id = 1000062, message = "I'll kill you in an instant!")
	public static NpcStringId ID_1000062;
	
	@NpcString(id = 1000063, message = "No! The treasure is still..!")
	public static NpcStringId ID_1000063;
	
	@NpcString(id = 1000064, message = "Invaders of Dragon Valley will never live to return!")
	public static NpcStringId ID_1000064;
	
	@NpcString(id = 1000065, message = "I am the guardian that honors the command of Antharas to watch over this place!")
	public static NpcStringId ID_1000065;
	
	@NpcString(id = 1000066, message = "You've set foot in Dragon Valley without permission! The penalty is death!")
	public static NpcStringId ID_1000066;
	
	@NpcString(id = 1000067, message = "Antharas has taken my life!")
	public static NpcStringId ID_1000067;
	
	@NpcString(id = 1000068, message = "The joy of killing! The ecstasy of looting! Hey guys, let's have a go at it again!")
	public static NpcStringId ID_1000068;
	
	@NpcString(id = 1000069, message = "There really are still lots of folks in the world without fear! I'll teach you a lesson!")
	public static NpcStringId ID_1000069;
	
	@NpcString(id = 1000070, message = "If you hand over everything you've got, I'll at least spare your life!")
	public static NpcStringId ID_1000070;
	
	@NpcString(id = 1000071, message = "Kneel down before one such as this!")
	public static NpcStringId ID_1000071;
	
	@NpcString(id = 1000072, message = "Honor the master's wishes and punish all the invaders!")
	public static NpcStringId ID_1000072;
	
	@NpcString(id = 1000073, message = "Follow the master's wishes and punish the invaders!")
	public static NpcStringId ID_1000073;
	
	@NpcString(id = 1000074, message = "Death is nothing more than a momentary rest...")
	public static NpcStringId ID_1000074;
	
	@NpcString(id = 1000075, message = "Listen! This is the end of the human era! Antharas has awoken!")
	public static NpcStringId ID_1000075;
	
	@NpcString(id = 1000076, message = "Present the lives of four people to Antharas!")
	public static NpcStringId ID_1000076;
	
	@NpcString(id = 1000077, message = "This is unbelievable! How could I have lost to one so inferior to myself?")
	public static NpcStringId ID_1000077;
	
	@NpcString(id = 1000078, message = "I carry the power of darkness and have returned from the abyss.")
	public static NpcStringId ID_1000078;
	
	@NpcString(id = 1000079, message = "It's detestable.")
	public static NpcStringId ID_1000079;
	
	@NpcString(id = 1000080, message = "I finally find rest...")
	public static NpcStringId ID_1000080;
	
	@NpcString(id = 1000081, message = "Glory to Orfen!")
	public static NpcStringId ID_1000081;
	
	@NpcString(id = 1000082, message = "In the name of Orfen, I can never forgive you who are invading this place!")
	public static NpcStringId ID_1000082;
	
	@NpcString(id = 1000083, message = "I'll make you pay the price for fearlessly entering Orfen's land!")
	public static NpcStringId ID_1000083;
	
	@NpcString(id = 1000084, message = "Even if you disappear into nothingness, you will still face the life-long suffering of the curse that I have given you.")
	public static NpcStringId ID_1000084;
	
	@NpcString(id = 1000085, message = "I'll stand against anyone that makes light of the sacred place of the Elves!")
	public static NpcStringId ID_1000085;
	
	@NpcString(id = 1000086, message = "I will kill with my own hands anyone that defiles our home!")
	public static NpcStringId ID_1000086;
	
	@NpcString(id = 1000087, message = "My brothers will never rest until we push you and your gang out of this valley!")
	public static NpcStringId ID_1000087;
	
	@NpcString(id = 1000088, message = "Until the day of destruction of Hestui!")
	public static NpcStringId ID_1000088;
	
	@NpcString(id = 1000089, message = "If any intrepid Orcs remain, attack them!")
	public static NpcStringId ID_1000089;
	
	@NpcString(id = 1000090, message = "I'll break your windpipe!")
	public static NpcStringId ID_1000090;
	
	@NpcString(id = 1000091, message = "Is revenge a failure?!")
	public static NpcStringId ID_1000091;
	
	@NpcString(id = 1000092, message = "The sparkling mithril of the dwarves and their pretty treasures! I'll get them all!")
	public static NpcStringId ID_1000092;
	
	@NpcString(id = 1000093, message = "Where are all the dreadful dwarves and their sparkling things?")
	public static NpcStringId ID_1000093;
	
	@NpcString(id = 1000094, message = "Hand over your pretty treasures!")
	public static NpcStringId ID_1000094;
	
	@NpcString(id = 1000095, message = "Hey! You should have run away!")
	public static NpcStringId ID_1000095;
	
	@NpcString(id = 1000096, message = "DESTRUCTION - EXTINCTION - SLAUGHTER - COLLAPSE! DESTRUCTION - EXTINCTION - SLAUGHTER - COLLAPSE!")
	public static NpcStringId ID_1000096;
	
	@NpcString(id = 1000097, message = "Destruction! Destruction! Destruction! Destruction!")
	public static NpcStringId ID_1000097;
	
	@NpcString(id = 1000098, message = "Destruction! Destruction! Destruction...")
	public static NpcStringId ID_1000098;
	
	@NpcString(id = 1000099, message = "Ta-da! Uthanka has returned!")
	public static NpcStringId ID_1000099;
	
	@NpcString(id = 1000100, message = "Wah, ha, ha, ha! Uthanka has taken over this island today!")
	public static NpcStringId ID_1000100;
	
	@NpcString(id = 1000101, message = "Whew! He's quite a guy!")
	public static NpcStringId ID_1000101;
	
	@NpcString(id = 1000102, message = "How exasperating and unfair to have things happen in such a meaningless way like this...")
	public static NpcStringId ID_1000102;
	
	@NpcString(id = 1000103, message = "This world should be filled with fear and sadness...")
	public static NpcStringId ID_1000103;
	
	@NpcString(id = 1000104, message = "I won't forgive the world that cursed me!")
	public static NpcStringId ID_1000104;
	
	@NpcString(id = 1000105, message = "I'll make everyone feel the same suffering as me!")
	public static NpcStringId ID_1000105;
	
	@NpcString(id = 1000106, message = "I'll give you a curse that you'll never be able to remove forever!")
	public static NpcStringId ID_1000106;
	
	@NpcString(id = 1000107, message = "I'll get revenge on you who slaughtered my compatriots!")
	public static NpcStringId ID_1000107;
	
	@NpcString(id = 1000108, message = "Those who are afraid should get away and those who are brave should fight!")
	public static NpcStringId ID_1000108;
	
	@NpcString(id = 1000109, message = "I've got power from Beleth so do you think I'll be easily defeated?!")
	public static NpcStringId ID_1000109;
	
	@NpcString(id = 1000110, message = "I am leaving now, but soon someone will come who will teach you all a lesson!")
	public static NpcStringId ID_1000110;
	
	@NpcString(id = 1000111, message = "Hey guys, let's make a round of our territory!")
	public static NpcStringId ID_1000111;
	
	@NpcString(id = 1000112, message = "The rumor is that there are wild, uncivilized ruffians who have recently arrived in my territory.")
	public static NpcStringId ID_1000112;
	
	@NpcString(id = 1000113, message = "Do you know who I am?! I am Sirocco! Everyone, attack!")
	public static NpcStringId ID_1000113;
	
	@NpcString(id = 1000114, message = "What's just happened?! The invincible Sirocco was defeated by someone like you?!")
	public static NpcStringId ID_1000114;
	
	@NpcString(id = 1000115, message = "Oh, I'm really hungry...")
	public static NpcStringId ID_1000115;
	
	@NpcString(id = 1000116, message = "I smell food. Ooh...")
	public static NpcStringId ID_1000116;
	
	@NpcString(id = 1000117, message = "Ooh...")
	public static NpcStringId ID_1000117;
	
	@NpcString(id = 1000118, message = "What does honey of this place taste like?!")
	public static NpcStringId ID_1000118;
	
	@NpcString(id = 1000119, message = "Give me some sweet, delicious golden honey!")
	public static NpcStringId ID_1000119;
	
	@NpcString(id = 1000120, message = "If you give me some honey, I'll at least spare your life...")
	public static NpcStringId ID_1000120;
	
	@NpcString(id = 1000121, message = "Only for lack of honey did I lose to the likes of you.")
	public static NpcStringId ID_1000121;
	
	@NpcString(id = 1000122, message = "Where is the traitor Kuroboros!?")
	public static NpcStringId ID_1000122;
	
	@NpcString(id = 1000123, message = "Look in every nook and cranny around here!")
	public static NpcStringId ID_1000123;
	
	@NpcString(id = 1000124, message = "Are you Lackey of Kuroboros?! I'll knock you out in one shot!")
	public static NpcStringId ID_1000124;
	
	@NpcString(id = 1000125, message = "He just closed his eyes without disposing of the traitor... How unfair!")
	public static NpcStringId ID_1000125;
	
	@NpcString(id = 1000126, message = "Hell for unbelievers in Kuroboros!")
	public static NpcStringId ID_1000126;
	
	@NpcString(id = 1000127, message = "The person that does not believe in Kuroboros, his life will soon become hell!")
	public static NpcStringId ID_1000127;
	
	@NpcString(id = 1000128, message = "The lackey of that demented devil, the servant of a false god! I'll send that fool straight to hell!")
	public static NpcStringId ID_1000128;
	
	@NpcString(id = 1000129, message = "Uh... I'm not dying; I'm just disappearing for a moment... I'll resurrect again!")
	public static NpcStringId ID_1000129;
	
	@NpcString(id = 1000130, message = "Hail to Kuroboros, the founder of our religion!")
	public static NpcStringId ID_1000130;
	
	@NpcString(id = 1000131, message = "Only those who believe in Patriarch Kuroboros shall receive salvation!")
	public static NpcStringId ID_1000131;
	
	@NpcString(id = 1000132, message = "Are you the ones that Sharuk has incited?! You also should trust in Kuroboros and be saved!")
	public static NpcStringId ID_1000132;
	
	@NpcString(id = 1000133, message = "Kuroboros will punish you.")
	public static NpcStringId ID_1000133;
	
	@NpcString(id = 1000134, message = "You who have beautiful spirits that shine brightly! I have returned!")
	public static NpcStringId ID_1000134;
	
	@NpcString(id = 1000135, message = "You that are weary and exhausted... Entrust your souls to me.")
	public static NpcStringId ID_1000135;
	
	@NpcString(id = 1000136, message = "The color of your soul is very attractive.")
	public static NpcStringId ID_1000136;
	
	@NpcString(id = 1000137, message = "Those of you who live! Do you know how beautiful your souls are?!")
	public static NpcStringId ID_1000137;
	
	@NpcString(id = 1000138, message = "It... will... kill... everyone...")
	public static NpcStringId ID_1000138;
	
	@NpcString(id = 1000139, message = "I'm... so... lonely...")
	public static NpcStringId ID_1000139;
	
	@NpcString(id = 1000140, message = "My... enemy...!")
	public static NpcStringId ID_1000140;
	
	@NpcString(id = 1000141, message = "... Now... I'm not so lonely!")
	public static NpcStringId ID_1000141;
	
	@NpcString(id = 1000142, message = "I will never forgive the Pixy Murika... that is trying to... kill us!")
	public static NpcStringId ID_1000142;
	
	@NpcString(id = 1000143, message = "Attack all the dull and stupid followers of Murika!")
	public static NpcStringId ID_1000143;
	
	@NpcString(id = 1000144, message = "I didn't have any idea about such ambitions!")
	public static NpcStringId ID_1000144;
	
	@NpcString(id = 1000145, message = "This is not the end... It's just the beginning.")
	public static NpcStringId ID_1000145;
	
	@NpcString(id = 1000146, message = "Hey... Shall we have some fun for the first time in a long while?...")
	public static NpcStringId ID_1000146;
	
	@NpcString(id = 1000147, message = "There've been some things going around like crazy here recently...")
	public static NpcStringId ID_1000147;
	
	@NpcString(id = 1000148, message = "Hey! Do you know who I am? I am Malex, Herald of Dagoniel! Attack!")
	public static NpcStringId ID_1000148;
	
	@NpcString(id = 1000149, message = "What's just happened?! The invincible Malex just lost to the likes of you?!")
	public static NpcStringId ID_1000149;
	
	@NpcString(id = 1000150, message = "It's something repeated in a vain life...")
	public static NpcStringId ID_1000150;
	
	@NpcString(id = 1000151, message = "Shake in fear, all you who value your lives!")
	public static NpcStringId ID_1000151;
	
	@NpcString(id = 1000152, message = "I'll make you feel suffering like a flame that is never extinguished!")
	public static NpcStringId ID_1000152;
	
	@NpcString(id = 1000153, message = "Back to the dirt...")
	public static NpcStringId ID_1000153;
	
	@NpcString(id = 1000154, message = "Hail Varika!!")
	public static NpcStringId ID_1000154;
	
	@NpcString(id = 1000155, message = "Nobody can stop us!")
	public static NpcStringId ID_1000155;
	
	@NpcString(id = 1000156, message = "You move slowly!")
	public static NpcStringId ID_1000156;
	
	@NpcString(id = 1000157, message = "Varika! Go first!")
	public static NpcStringId ID_1000157;
	
	@NpcString(id = 1000158, message = "Where am I? Who am I?")
	public static NpcStringId ID_1000158;
	
	@NpcString(id = 1000159, message = "Uh... My head hurts like it's going to burst! Who am I?")
	public static NpcStringId ID_1000159;
	
	@NpcString(id = 1000160, message = "You jerk. You're a devil! You're a devil to have made me like this!")
	public static NpcStringId ID_1000160;
	
	@NpcString(id = 1000161, message = "Where am I? What happened? Thank you!")
	public static NpcStringId ID_1000161;
	
	@NpcString(id = 1000162, message = "Ukru Master!")
	public static NpcStringId ID_1000162;
	
	@NpcString(id = 1000163, message = "Are you Matu?")
	public static NpcStringId ID_1000163;
	
	@NpcString(id = 1000164, message = "Marak! Tubarin! Sabaracha!")
	public static NpcStringId ID_1000164;
	
	@NpcString(id = 1000165, message = "Pa'agrio Tama!")
	public static NpcStringId ID_1000165;
	
	@NpcString(id = 1000166, message = "Accept the will of Icarus!")
	public static NpcStringId ID_1000166;
	
	@NpcString(id = 1000167, message = "The people who are blocking my way will not be forgiven...")
	public static NpcStringId ID_1000167;
	
	@NpcString(id = 1000168, message = "You are scum.")
	public static NpcStringId ID_1000168;
	
	@NpcString(id = 1000169, message = "You lack power.")
	public static NpcStringId ID_1000169;
	
	@NpcString(id = 1000170, message = "Return")
	public static NpcStringId ID_1000170;
	
	@NpcString(id = 1000171, message = "Adena has been transferred.")
	public static NpcStringId ID_1000171;
	
	@NpcString(id = 1000172, message = "Event Number")
	public static NpcStringId ID_1000172;
	
	@NpcString(id = 1000173, message = "First Prize")
	public static NpcStringId ID_1000173;
	
	@NpcString(id = 1000174, message = "Second prize")
	public static NpcStringId ID_1000174;
	
	@NpcString(id = 1000175, message = "Third prize")
	public static NpcStringId ID_1000175;
	
	@NpcString(id = 1000176, message = "Fourth prize")
	public static NpcStringId ID_1000176;
	
	@NpcString(id = 1000177, message = "There has been no winning lottery ticket.")
	public static NpcStringId ID_1000177;
	
	@NpcString(id = 1000178, message = "The most recent winning lottery numbers")
	public static NpcStringId ID_1000178;
	
	@NpcString(id = 1000179, message = "Your lucky numbers have been selected above.")
	public static NpcStringId ID_1000179;
	
	@NpcString(id = 1000180, message = "I wonder who it is that is lurking about..")
	public static NpcStringId ID_1000180;
	
	@NpcString(id = 1000181, message = "Sacred magical research is conducted here.")
	public static NpcStringId ID_1000181;
	
	@NpcString(id = 1000182, message = "Behold the awesome power of magic!")
	public static NpcStringId ID_1000182;
	
	@NpcString(id = 1000183, message = "Your powers are impressive but you must not annoy our high level sorcerer.")
	public static NpcStringId ID_1000183;
	
	@NpcString(id = 1000184, message = "I am Barda, master of the Bandit Stronghold!")
	public static NpcStringId ID_1000184;
	
	@NpcString(id = 1000185, message = "I, Master Barda, once owned that stronghold,")
	public static NpcStringId ID_1000185;
	
	@NpcString(id = 1000186, message = "Ah, very interesting!")
	public static NpcStringId ID_1000186;
	
	@NpcString(id = 1000187, message = "You are more powerful than you appear. We'll meet again!")
	public static NpcStringId ID_1000187;
	
	@NpcString(id = 1000188, message = "You filthy sorcerers disgust me!")
	public static NpcStringId ID_1000188;
	
	@NpcString(id = 1000189, message = "Why would you build a tower in our territory?")
	public static NpcStringId ID_1000189;
	
	@NpcString(id = 1000190, message = "Are you part of that evil gang of sorcerers?")
	public static NpcStringId ID_1000190;
	
	@NpcString(id = 1000191, message = "That is why I don't bother with anyone below the level of sorcerer.")
	public static NpcStringId ID_1000191;
	
	@NpcString(id = 1000192, message = "Ah, another beautiful day!")
	public static NpcStringId ID_1000192;
	
	@NpcString(id = 1000193, message = "Our specialties are arson and looting.")
	public static NpcStringId ID_1000193;
	
	@NpcString(id = 1000194, message = "You will leave empty-handed!")
	public static NpcStringId ID_1000194;
	
	@NpcString(id = 1000195, message = "Ah, so you admire my treasure, do you? Try finding it! Ha!")
	public static NpcStringId ID_1000195;
	
	@NpcString(id = 1000196, message = "Is everybody listening? Sirion has come back. Everyone chant and bow...")
	public static NpcStringId ID_1000196;
	
	@NpcString(id = 1000197, message = "Bow down, you worthless humans!")
	public static NpcStringId ID_1000197;
	
	@NpcString(id = 1000198, message = "Very tacky!")
	public static NpcStringId ID_1000198;
	
	@NpcString(id = 1000199, message = "Don't think that you are invincible just because you defeated me!")
	public static NpcStringId ID_1000199;
	
	@NpcString(id = 1000200, message = "The material desires of mortals are ultimately meaningless.")
	public static NpcStringId ID_1000200;
	
	@NpcString(id = 1000201, message = "Do not forget the reason the Tower of Insolence collapsed.")
	public static NpcStringId ID_1000201;
	
	@NpcString(id = 1000202, message = "You humans are all alike, full of greed and avarice!")
	public static NpcStringId ID_1000202;
	
	@NpcString(id = 1000203, message = "All for nothing,")
	public static NpcStringId ID_1000203;
	
	@NpcString(id = 1000204, message = "What are all these people doing here?")
	public static NpcStringId ID_1000204;
	
	@NpcString(id = 1000205, message = "I must find the secret of eternal life, here among these rotten angels!")
	public static NpcStringId ID_1000205;
	
	@NpcString(id = 1000206, message = "Do you also seek the secret of immortality?")
	public static NpcStringId ID_1000206;
	
	@NpcString(id = 1000207, message = "I shall never reveal my secrets!")
	public static NpcStringId ID_1000207;
	
	@NpcString(id = 1000208, message = "Who dares enter this place?")
	public static NpcStringId ID_1000208;
	
	@NpcString(id = 1000209, message = "This is no place for humans! You must leave immediately.")
	public static NpcStringId ID_1000209;
	
	@NpcString(id = 1000210, message = "You poor creatures! Too stupid to realize your own ignorance!")
	public static NpcStringId ID_1000210;
	
	@NpcString(id = 1000211, message = "You mustn't go there!")
	public static NpcStringId ID_1000211;
	
	@NpcString(id = 1000212, message = "Who dares disturb this marsh?")
	public static NpcStringId ID_1000212;
	
	@NpcString(id = 1000213, message = "The humans must not be allowed to destroy the marshland for their greedy purposes.")
	public static NpcStringId ID_1000213;
	
	@NpcString(id = 1000214, message = "You are a brave man...")
	public static NpcStringId ID_1000214;
	
	@NpcString(id = 1000215, message = "You idiots! Some day you shall also be gone!")
	public static NpcStringId ID_1000215;
	
	@NpcString(id = 1000216, message = "Someone has entered the forest...")
	public static NpcStringId ID_1000216;
	
	@NpcString(id = 1000217, message = "The forest is very quiet and peaceful.")
	public static NpcStringId ID_1000217;
	
	@NpcString(id = 1000218, message = "Stay here in this wonderful forest!")
	public static NpcStringId ID_1000218;
	
	@NpcString(id = 1000219, message = "My... my souls...")
	public static NpcStringId ID_1000219;
	
	@NpcString(id = 1000220, message = "This forest is a dangerous place.")
	public static NpcStringId ID_1000220;
	
	@NpcString(id = 1000221, message = "Unless you leave this forest immediately you are bound to run into serious trouble.")
	public static NpcStringId ID_1000221;
	
	@NpcString(id = 1000222, message = "Leave now!")
	public static NpcStringId ID_1000222;
	
	@NpcString(id = 1000223, message = "Why do you ignore my warning?")
	public static NpcStringId ID_1000223;
	
	@NpcString(id = 1000224, message = "Harits of the world... I bring you peace!")
	public static NpcStringId ID_1000224;
	
	@NpcString(id = 1000225, message = "Harits! Be courageous!")
	public static NpcStringId ID_1000225;
	
	@NpcString(id = 1000226, message = "I shall eat your still-beating heart!.")
	public static NpcStringId ID_1000226;
	
	@NpcString(id = 1000227, message = "Harits! Keep faith until the day I return... Never lose hope!")
	public static NpcStringId ID_1000227;
	
	@NpcString(id = 1000228, message = "Even the giants are gone! There's nothing left to be afraid of now!")
	public static NpcStringId ID_1000228;
	
	@NpcString(id = 1000229, message = "Have you heard of the giants? Their downfall was inevitable!")
	public static NpcStringId ID_1000229;
	
	@NpcString(id = 1000230, message = "What nerve! Do you dare to challenge me?")
	public static NpcStringId ID_1000230;
	
	@NpcString(id = 1000231, message = "You are as evil as the giants...")
	public static NpcStringId ID_1000231;
	
	@NpcString(id = 1000232, message = "This dungeon is still in good condition!")
	public static NpcStringId ID_1000232;
	
	@NpcString(id = 1000233, message = "This place is spectacular, wouldn't you say?")
	public static NpcStringId ID_1000233;
	
	@NpcString(id = 1000234, message = "You are very brave warriors!")
	public static NpcStringId ID_1000234;
	
	@NpcString(id = 1000235, message = "Are the giants truly gone for good?")
	public static NpcStringId ID_1000235;
	
	@NpcString(id = 1000236, message = "These graves are good.")
	public static NpcStringId ID_1000236;
	
	@NpcString(id = 1000237, message = "Gold and silver are meaningless to a dead man!")
	public static NpcStringId ID_1000237;
	
	@NpcString(id = 1000238, message = "Why would those corrupt aristocrats bury such useful things?")
	public static NpcStringId ID_1000238;
	
	@NpcString(id = 1000239, message = "You filthy pig! Eat and be merry now that you have shirked your responsibilities!")
	public static NpcStringId ID_1000239;
	
	@NpcString(id = 1000240, message = "Those thugs! It would be too merciful to rip them apart and chew them up one at a time!")
	public static NpcStringId ID_1000240;
	
	@NpcString(id = 1000241, message = "You accursed scoundrels!")
	public static NpcStringId ID_1000241;
	
	@NpcString(id = 1000242, message = "Hmm, could this be the assassin sent by those idiots from Aden?")
	public static NpcStringId ID_1000242;
	
	@NpcString(id = 1000243, message = "I shall curse your name with my last breath!")
	public static NpcStringId ID_1000243;
	
	@NpcString(id = 1000244, message = "My beloved Lord Shilen.")
	public static NpcStringId ID_1000244;
	
	@NpcString(id = 1000245, message = "I must break the seal and release Lord Shilen as soon as possible...")
	public static NpcStringId ID_1000245;
	
	@NpcString(id = 1000246, message = "You shall taste the vengeance of Lord Shilen!")
	public static NpcStringId ID_1000246;
	
	@NpcString(id = 1000247, message = "Lord Shilen... some day... you will accomplish... this mission...")
	public static NpcStringId ID_1000247;
	
	@NpcString(id = 1000248, message = "Towards immortality...")
	public static NpcStringId ID_1000248;
	
	@NpcString(id = 1000249, message = "He who desires immortality... Come unto me.")
	public static NpcStringId ID_1000249;
	
	@NpcString(id = 1000250, message = "You shall be sacrificed to gain my immortality!")
	public static NpcStringId ID_1000250;
	
	@NpcString(id = 1000251, message = "Eternal life in front of my eyes... I have collapsed in such a worthless way like this...")
	public static NpcStringId ID_1000251;
	
	@NpcString(id = 1000252, message = "Zaken, you are a cowardly cur!")
	public static NpcStringId ID_1000252;
	
	@NpcString(id = 1000253, message = "You are immortal, aren't you, Zaken?")
	public static NpcStringId ID_1000253;
	
	@NpcString(id = 1000254, message = "Please return my body to me.")
	public static NpcStringId ID_1000254;
	
	@NpcString(id = 1000255, message = "Finally... will I be able to rest?")
	public static NpcStringId ID_1000255;
	
	@NpcString(id = 1000256, message = "What is all that racket?")
	public static NpcStringId ID_1000256;
	
	@NpcString(id = 1000257, message = "Master Gildor does not like to be disturbed.")
	public static NpcStringId ID_1000257;
	
	@NpcString(id = 1000258, message = "Please, just hold it down...")
	public static NpcStringId ID_1000258;
	
	@NpcString(id = 1000259, message = "If you disturb Master Gildor I won't be able to help you.")
	public static NpcStringId ID_1000259;
	
	@NpcString(id = 1000260, message = "Who dares approach?")
	public static NpcStringId ID_1000260;
	
	@NpcString(id = 1000261, message = "These reeds are my territory...")
	public static NpcStringId ID_1000261;
	
	@NpcString(id = 1000262, message = "You fools! Today you shall learn a lesson!")
	public static NpcStringId ID_1000262;
	
	@NpcString(id = 1000263, message = "The past goes by... Is a new era beginning?...")
	public static NpcStringId ID_1000263;
	
	@NpcString(id = 1000264, message = "This is the garden of Eva.")
	public static NpcStringId ID_1000264;
	
	@NpcString(id = 1000265, message = "The garden of Eva is a sacred place.")
	public static NpcStringId ID_1000265;
	
	@NpcString(id = 1000266, message = "Do you mean to insult Eva?")
	public static NpcStringId ID_1000266;
	
	@NpcString(id = 1000267, message = "How rude!  Eva's love is available to all, even to an ill-mannered lout like yourself!")
	public static NpcStringId ID_1000267;
	
	@NpcString(id = 1000268, message = "This place once belonged to Lord Shilen.")
	public static NpcStringId ID_1000268;
	
	@NpcString(id = 1000269, message = "Leave this palace to us, spirits of Eva.")
	public static NpcStringId ID_1000269;
	
	@NpcString(id = 1000270, message = "Why are you getting in our way?")
	public static NpcStringId ID_1000270;
	
	@NpcString(id = 1000271, message = "Shilen... our Shilen!")
	public static NpcStringId ID_1000271;
	
	@NpcString(id = 1000272, message = "All who fear of Fafurion... Leave this place at once!")
	public static NpcStringId ID_1000272;
	
	@NpcString(id = 1000273, message = "You are being punished in the name of Fafurion!")
	public static NpcStringId ID_1000273;
	
	@NpcString(id = 1000274, message = "Oh, master... please forgive your humble servant...")
	public static NpcStringId ID_1000274;
	
	@NpcString(id = 1000275, message = "Prepare to die, foreign invaders! I am Gustav, the eternal ruler of this fortress and I have taken up my sword to repel thee!")
	public static NpcStringId ID_1000275;
	
	@NpcString(id = 1000276, message = "Glory to Aden, the Kingdom of the Lion! Glory to Sir Gustav, our immortal lord!")
	public static NpcStringId ID_1000276;
	
	@NpcString(id = 1000277, message = "Soldiers of Gustav, go forth and destroy the invaders!")
	public static NpcStringId ID_1000277;
	
	@NpcString(id = 1000278, message = "This is unbelievable! Have I really been defeated? I shall return and take your head!")
	public static NpcStringId ID_1000278;
	
	@NpcString(id = 1000279, message = "Could it be that I have reached my end? I cannot die without honor, without the permission of Sir Gustav!")
	public static NpcStringId ID_1000279;
	
	@NpcString(id = 1000280, message = "Ah, the bitter taste of defeat... I fear my torments are not over...")
	public static NpcStringId ID_1000280;
	
	@NpcString(id = 1000281, message = "I follow the will of Fafurion.")
	public static NpcStringId ID_1000281;
	
	@NpcString(id = 1000282, message = "Tickets for the Lucky Lottery are now on sale!")
	public static NpcStringId ID_1000282;
	
	@NpcString(id = 1000283, message = "The Lucky Lottery drawing is about to begin!")
	public static NpcStringId ID_1000283;
	
	@NpcString(id = 1000284, message = "The winning numbers for Lucky Lottery %s are %s. Congratulations to the winners!")
	public static NpcStringId ID_1000284;
	
	@NpcString(id = 1000285, message = "You're too young to play Lucky Lottery!")
	public static NpcStringId ID_1000285;
	
	@NpcString(id = 1000286, message = "%s! Watch your back!!!")
	public static NpcStringId ID_1000286;
	
	@NpcString(id = 1000287, message = "%s! Come on, I'll take you on!")
	public static NpcStringId ID_1000287;
	
	@NpcString(id = 1000288, message = "%s! How dare you interrupt our fight! Hey guys, help!")
	public static NpcStringId ID_1000288;
	
	@NpcString(id = 1000289, message = "I'll help you! I'm no coward!")
	public static NpcStringId ID_1000289;
	
	@NpcString(id = 1000290, message = "Dear ultimate power!!!")
	public static NpcStringId ID_1000290;
	
	@NpcString(id = 1000291, message = "Everybody! Attack %s!")
	public static NpcStringId ID_1000291;
	
	@NpcString(id = 1000292, message = "I will follow your order.")
	public static NpcStringId ID_1000292;
	
	@NpcString(id = 1000293, message = "Bet you didn't expect this!")
	public static NpcStringId ID_1000293;
	
	@NpcString(id = 1000294, message = "Come out, you children of darkness!")
	public static NpcStringId ID_1000294;
	
	@NpcString(id = 1000295, message = "Summon party members!")
	public static NpcStringId ID_1000295;
	
	@NpcString(id = 1000296, message = "Master! Did you call me?")
	public static NpcStringId ID_1000296;
	
	@NpcString(id = 1000297, message = "You idiot!")
	public static NpcStringId ID_1000297;
	
	@NpcString(id = 1000298, message = "What about this?")
	public static NpcStringId ID_1000298;
	
	@NpcString(id = 1000299, message = "Very impressive %s! This is the last!")
	public static NpcStringId ID_1000299;
	
	@NpcString(id = 1000300, message = "Dawn")
	public static NpcStringId ID_1000300;
	
	@NpcString(id = 1000301, message = "Dusk")
	public static NpcStringId ID_1000301;
	
	@NpcString(id = 1000302, message = "Nothingness")
	public static NpcStringId ID_1000302;
	
	@NpcString(id = 1000303, message = "This world will soon be annihilated!")
	public static NpcStringId ID_1000303;
	
	@NpcString(id = 1000304, message = "A curse upon you!")
	public static NpcStringId ID_1000304;
	
	@NpcString(id = 1000305, message = "The day of judgment is near!")
	public static NpcStringId ID_1000305;
	
	@NpcString(id = 1000306, message = "I bestow upon you a blessing!")
	public static NpcStringId ID_1000306;
	
	@NpcString(id = 1000307, message = "The first rule of fighting is to start by killing the weak ones!")
	public static NpcStringId ID_1000307;
	
	@NpcString(id = 1000308, message = "Adena")
	public static NpcStringId ID_1000308;
	
	@NpcString(id = 1000309, message = "Ancient Adena")
	public static NpcStringId ID_1000309;
	
	@NpcString(id = 1000310, message = "Dusk")
	public static NpcStringId ID_1000310;
	
	@NpcString(id = 1000311, message = "Dawn")
	public static NpcStringId ID_1000311;
	
	@NpcString(id = 1000312, message = "Level 31 or lower")
	public static NpcStringId ID_1000312;
	
	@NpcString(id = 1000313, message = "Level 42 or lower")
	public static NpcStringId ID_1000313;
	
	@NpcString(id = 1000314, message = "Level 53 or lower")
	public static NpcStringId ID_1000314;
	
	@NpcString(id = 1000315, message = "Level 64 or lower")
	public static NpcStringId ID_1000315;
	
	@NpcString(id = 1000316, message = "No Level Limit")
	public static NpcStringId ID_1000316;
	
	@NpcString(id = 1000317, message = "The main event will start in 2 minutes. Please register now.")
	public static NpcStringId ID_1000317;
	
	@NpcString(id = 1000318, message = "The main event is now starting.")
	public static NpcStringId ID_1000318;
	
	@NpcString(id = 1000319, message = "The main event will close in 5 minutes.")
	public static NpcStringId ID_1000319;
	
	@NpcString(id = 1000320, message = "The main event will finish in 2 minutes. Please prepare for the next game.")
	public static NpcStringId ID_1000320;
	
	@NpcString(id = 1000321, message = "The amount of SSQ contribution has increased by %s.")
	public static NpcStringId ID_1000321;
	
	@NpcString(id = 1000322, message = "No record exists")
	public static NpcStringId ID_1000322;
	
	@NpcString(id = 1000324, message = "<a action=\"bypass -h menu_select?ask=-2&reply=324\">Gladiator</a><br>")
	public static NpcStringId ID_1000324;
	
	@NpcString(id = 1000325, message = "<a action=\"bypass -h menu_select?ask=-2&reply=325\">Warlord</a><br>")
	public static NpcStringId ID_1000325;
	
	@NpcString(id = 1000327, message = "<a action=\"bypass -h menu_select?ask=-2&reply=327\">Paladin</a><br>")
	public static NpcStringId ID_1000327;
	
	@NpcString(id = 1000328, message = "<a action=\"bypass -h menu_select?ask=-2&reply=328\">Dark Avenger</a><br>")
	public static NpcStringId ID_1000328;
	
	@NpcString(id = 1000330, message = "<a action=\"bypass -h menu_select?ask=-2&reply=330\">Treasure Hunter</a><br>")
	public static NpcStringId ID_1000330;
	
	@NpcString(id = 1000331, message = "<a action=\"bypass -h menu_select?ask=-2&reply=331\">Hawkeye</a><br>")
	public static NpcStringId ID_1000331;
	
	@NpcString(id = 1000334, message = "<a action=\"bypass -h menu_select?ask=-2&reply=334\">Sorcerer</a><br>")
	public static NpcStringId ID_1000334;
	
	@NpcString(id = 1000335, message = "<a action=\"bypass -h menu_select?ask=-2&reply=335\">Necromancer</a><br>")
	public static NpcStringId ID_1000335;
	
	@NpcString(id = 1000336, message = "<a action=\"bypass -h menu_select?ask=-2&reply=336\">Warlock</a><br>")
	public static NpcStringId ID_1000336;
	
	@NpcString(id = 1000338, message = "<a action=\"bypass -h menu_select?ask=-2&reply=338\">Bishop</a><br>")
	public static NpcStringId ID_1000338;
	
	@NpcString(id = 1000339, message = "<a action=\"bypass -h menu_select?ask=-2&reply=339\">Prophet</a><br>")
	public static NpcStringId ID_1000339;
	
	@NpcString(id = 1000342, message = "<a action=\"bypass -h menu_select?ask=-2&reply=342\">Temple Knight</a><br>")
	public static NpcStringId ID_1000342;
	
	@NpcString(id = 1000343, message = "<a action=\"bypass -h menu_select?ask=-2&reply=343\">Swordsinger</a><br>")
	public static NpcStringId ID_1000343;
	
	@NpcString(id = 1000345, message = "<a action=\"bypass -h menu_select?ask=-2&reply=345\">Plainswalker</a><br>")
	public static NpcStringId ID_1000345;
	
	@NpcString(id = 1000346, message = "<a action=\"bypass -h menu_select?ask=-2&reply=346\">Silver Ranger</a><br>")
	public static NpcStringId ID_1000346;
	
	@NpcString(id = 1000349, message = "<a action=\"bypass -h menu_select?ask=-2&reply=349\">Spellsinger</a><br>")
	public static NpcStringId ID_1000349;
	
	@NpcString(id = 1000350, message = "<a action=\"bypass -h menu_select?ask=-2&reply=350\">Elemental Summoner</a><br>")
	public static NpcStringId ID_1000350;
	
	@NpcString(id = 1000352, message = "<a action=\"bypass -h menu_select?ask=-2&reply=352\">Elven Elder</a><br>")
	public static NpcStringId ID_1000352;
	
	@NpcString(id = 1000355, message = "<a action=\"bypass -h menu_select?ask=-2&reply=355\">Shillien Knight</a><br>")
	public static NpcStringId ID_1000355;
	
	@NpcString(id = 1000356, message = "<a action=\"bypass -h menu_select?ask=-2&reply=356\">Bladedancer</a><br>")
	public static NpcStringId ID_1000356;
	
	@NpcString(id = 1000358, message = "<a action=\"bypass -h menu_select?ask=-2&reply=358\">Abyssal Walker</a><br>")
	public static NpcStringId ID_1000358;
	
	@NpcString(id = 1000359, message = "<a action=\"bypass -h menu_select?ask=-2&reply=359\">Phantom Ranger</a><br>")
	public static NpcStringId ID_1000359;
	
	@NpcString(id = 1000362, message = "<a action=\"bypass -h menu_select?ask=-2&reply=362\">Spellhowler</a><br>")
	public static NpcStringId ID_1000362;
	
	@NpcString(id = 1000363, message = "<a action=\"bypass -h menu_select?ask=-2&reply=363\">Phantom Summoner</a><br>")
	public static NpcStringId ID_1000363;
	
	@NpcString(id = 1000365, message = "<a action=\"bypass -h menu_select?ask=-2&reply=365\">Shillien Elder</a><br>")
	public static NpcStringId ID_1000365;
	
	@NpcString(id = 1000368, message = "<a action=\"bypass -h menu_select?ask=-2&reply=368\">Destroyer</a><br>")
	public static NpcStringId ID_1000368;
	
	@NpcString(id = 1000370, message = "<a action=\"bypass -h menu_select?ask=-2&reply=370\">Tyrant</a><br>")
	public static NpcStringId ID_1000370;
	
	@NpcString(id = 1000373, message = "<a action=\"bypass -h menu_select?ask=-2&reply=373\">Overlord</a><br>")
	public static NpcStringId ID_1000373;
	
	@NpcString(id = 1000374, message = "<a action=\"bypass -h menu_select?ask=-2&reply=374\">Warcryer</a><br>")
	public static NpcStringId ID_1000374;
	
	@NpcString(id = 1000377, message = "<a action=\"bypass -h menu_select?ask=-2&reply=377\">Bounty Hunter</a><br>")
	public static NpcStringId ID_1000377;
	
	@NpcString(id = 1000379, message = "<a action=\"bypass -h menu_select?ask=-2&reply=379\">Warsmith</a><br>")
	public static NpcStringId ID_1000379;
	
	@NpcString(id = 1000380, message = "That will do! I'll move you to the outside soon.")
	public static NpcStringId ID_1000380;
	
	@NpcString(id = 1000381, message = "%s! Watch your back!")
	public static NpcStringId ID_1000381;
	
	@NpcString(id = 1000382, message = "Your rear is practically unguarded!")
	public static NpcStringId ID_1000382;
	
	@NpcString(id = 1000383, message = "How dare you turn your back on me!")
	public static NpcStringId ID_1000383;
	
	@NpcString(id = 1000384, message = "%s! I'll deal with you myself!")
	public static NpcStringId ID_1000384;
	
	@NpcString(id = 1000385, message = "%s! This is personal!")
	public static NpcStringId ID_1000385;
	
	@NpcString(id = 1000386, message = "%s! Leave us alone! This is between us!")
	public static NpcStringId ID_1000386;
	
	@NpcString(id = 1000387, message = "%s! Killing you will be a pleasure!")
	public static NpcStringId ID_1000387;
	
	@NpcString(id = 1000388, message = "%s! Hey! We're having a duel here!")
	public static NpcStringId ID_1000388;
	
	@NpcString(id = 1000389, message = "The duel is over! Attack!")
	public static NpcStringId ID_1000389;
	
	@NpcString(id = 1000390, message = "Foul! Kill the coward!")
	public static NpcStringId ID_1000390;
	
	@NpcString(id = 1000391, message = "How dare you interrupt a sacred duel! You must be taught a lesson!")
	public static NpcStringId ID_1000391;
	
	@NpcString(id = 1000392, message = "Die, you coward!")
	public static NpcStringId ID_1000392;
	
	@NpcString(id = 1000393, message = "What are you looking at?")
	public static NpcStringId ID_1000393;
	
	@NpcString(id = 1000394, message = "Kill the coward!")
	public static NpcStringId ID_1000394;
	
	@NpcString(id = 1000395, message = "I never thought I'd use this against a novice!")
	public static NpcStringId ID_1000395;
	
	@NpcString(id = 1000396, message = "You won't take me down easily.")
	public static NpcStringId ID_1000396;
	
	@NpcString(id = 1000397, message = "The battle has just begun!")
	public static NpcStringId ID_1000397;
	
	@NpcString(id = 1000398, message = "Kill %s first!")
	public static NpcStringId ID_1000398;
	
	@NpcString(id = 1000399, message = "Attack %s!")
	public static NpcStringId ID_1000399;
	
	@NpcString(id = 1000400, message = "Attack! Attack!")
	public static NpcStringId ID_1000400;
	
	@NpcString(id = 1000401, message = "Over here!")
	public static NpcStringId ID_1000401;
	
	@NpcString(id = 1000402, message = "Roger!")
	public static NpcStringId ID_1000402;
	
	@NpcString(id = 1000403, message = "Show yourselves!")
	public static NpcStringId ID_1000403;
	
	@NpcString(id = 1000404, message = "Forces of darkness! Follow me!")
	public static NpcStringId ID_1000404;
	
	@NpcString(id = 1000405, message = "Destroy the enemy, my brothers!")
	public static NpcStringId ID_1000405;
	
	@NpcString(id = 1000406, message = "Now the fun starts!")
	public static NpcStringId ID_1000406;
	
	@NpcString(id = 1000407, message = "Enough fooling around. Get ready to die!")
	public static NpcStringId ID_1000407;
	
	@NpcString(id = 1000408, message = "You idiot! I've just been toying with you!")
	public static NpcStringId ID_1000408;
	
	@NpcString(id = 1000409, message = "Witness my true power!")
	public static NpcStringId ID_1000409;
	
	@NpcString(id = 1000410, message = "Now the battle begins!")
	public static NpcStringId ID_1000410;
	
	@NpcString(id = 1000411, message = "I must admit, no one makes my blood boil quite like you do!")
	public static NpcStringId ID_1000411;
	
	@NpcString(id = 1000412, message = "You have more skill than I thought!")
	public static NpcStringId ID_1000412;
	
	@NpcString(id = 1000413, message = "I'll double my strength!")
	public static NpcStringId ID_1000413;
	
	@NpcString(id = 1000414, message = "Prepare to die!")
	public static NpcStringId ID_1000414;
	
	@NpcString(id = 1000415, message = "All is lost! Prepare to meet the goddess of death!")
	public static NpcStringId ID_1000415;
	
	@NpcString(id = 1000416, message = "All is lost! The prophecy of destruction has been fulfilled!")
	public static NpcStringId ID_1000416;
	
	@NpcString(id = 1000417, message = "The end of time has come! The prophecy of destruction has been fulfilled!")
	public static NpcStringId ID_1000417;
	
	@NpcString(id = 1000418, message = "%s! You bring an ill wind!")
	public static NpcStringId ID_1000418;
	
	@NpcString(id = 1000419, message = "%s! You might as well give up!")
	public static NpcStringId ID_1000419;
	
	@NpcString(id = 1000420, message = "You don't have any hope! Your end has come!")
	public static NpcStringId ID_1000420;
	
	@NpcString(id = 1000421, message = "The prophecy of darkness has been fulfilled!")
	public static NpcStringId ID_1000421;
	
	@NpcString(id = 1000422, message = "As foretold in the prophecy of darkness, the era of chaos has begun!")
	public static NpcStringId ID_1000422;
	
	@NpcString(id = 1000423, message = "The prophecy of darkness has come to pass!")
	public static NpcStringId ID_1000423;
	
	@NpcString(id = 1000424, message = "%s! I give you the blessing of prophecy!")
	public static NpcStringId ID_1000424;
	
	@NpcString(id = 1000425, message = "%s! I bestow upon you the authority of the abyss!")
	public static NpcStringId ID_1000425;
	
	@NpcString(id = 1000426, message = "Herald of the new era, open your eyes!")
	public static NpcStringId ID_1000426;
	
	@NpcString(id = 1000427, message = "Remember, kill the weaklings first!")
	public static NpcStringId ID_1000427;
	
	@NpcString(id = 1000428, message = "Prepare to die, maggot!")
	public static NpcStringId ID_1000428;
	
	@NpcString(id = 1000429, message = "That will do. Prepare to be released!")
	public static NpcStringId ID_1000429;
	
	@NpcString(id = 1000430, message = "Draw")
	public static NpcStringId ID_1000430;
	
	@NpcString(id = 1000431, message = "Rulers of the seal! I bring you wondrous gifts!")
	public static NpcStringId ID_1000431;
	
	@NpcString(id = 1000432, message = "Rulers of the seal! I have some excellent weapons to show you!")
	public static NpcStringId ID_1000432;
	
	@NpcString(id = 1000433, message = "I've been so busy lately, in addition to planning my trip!")
	public static NpcStringId ID_1000433;
	
	@NpcString(id = 1000434, message = "Your treatment of weaklings is unforgivable!")
	public static NpcStringId ID_1000434;
	
	@NpcString(id = 1000435, message = "I'm here to help you! Hi yah!")
	public static NpcStringId ID_1000435;
	
	@NpcString(id = 1000436, message = "Justice will be served!")
	public static NpcStringId ID_1000436;
	
	@NpcString(id = 1000437, message = "On to immortal glory!")
	public static NpcStringId ID_1000437;
	
	@NpcString(id = 1000438, message = "Justice always avenges the powerless!")
	public static NpcStringId ID_1000438;
	
	@NpcString(id = 1000439, message = "Are you hurt? Hang in there, we've almost got them!")
	public static NpcStringId ID_1000439;
	
	@NpcString(id = 1000440, message = "Why should I tell you my name, you creep!?")
	public static NpcStringId ID_1000440;
	
	@NpcString(id = 1000441, message = "Registration possible.")
	public static NpcStringId ID_1000441;
	
	@NpcString(id = 1000442, message = "Minute")
	public static NpcStringId ID_1000442;
	
	@NpcString(id = 1000443, message = "The defenders of %s castle will be teleported to the inner castle.")
	public static NpcStringId ID_1000443;
	
	@NpcString(id = 1000444, message = "Sunday")
	public static NpcStringId ID_1000444;
	
	@NpcString(id = 1000445, message = "Monday")
	public static NpcStringId ID_1000445;
	
	@NpcString(id = 1000446, message = "Tuesday")
	public static NpcStringId ID_1000446;
	
	@NpcString(id = 1000447, message = "Wednesday")
	public static NpcStringId ID_1000447;
	
	@NpcString(id = 1000448, message = "Thursday")
	public static NpcStringId ID_1000448;
	
	@NpcString(id = 1000449, message = "Friday")
	public static NpcStringId ID_1000449;
	
	@NpcString(id = 1000450, message = "Saturday")
	public static NpcStringId ID_1000450;
	
	@NpcString(id = 1000451, message = "Poem")
	public static NpcStringId ID_1000451;
	
	@NpcString(id = 1000452, message = "It's a good day to die! Welcome to hell, maggot!")
	public static NpcStringId ID_1000452;
	
	@NpcString(id = 1000453, message = "The Festival of Darkness will end in two minutes.")
	public static NpcStringId ID_1000453;
	
	@NpcString(id = 1000454, message = "Noblesse gate pass")
	public static NpcStringId ID_1000454;
	
	@NpcString(id = 1000455, message = "Currently")
	public static NpcStringId ID_1000455;
	
	@NpcString(id = 1000456, message = "minutes, have passed")
	public static NpcStringId ID_1000456;
	
	@NpcString(id = 1000457, message = "Game over. The teleport will appear momentarily.")
	public static NpcStringId ID_1000457;
	
	@NpcString(id = 1000458, message = "You, who are like the slugs crawling on the ground. The generosity and greatness of me, Daimon the White-Eyed is endless! Ha Ha Ha!")
	public static NpcStringId ID_1000458;
	
	@NpcString(id = 1000459, message = "If you want to be the opponent of me, Daimon the White-Eyed, you should at least have the basic skills~!")
	public static NpcStringId ID_1000459;
	
	@NpcString(id = 1000460, message = "You stupid creatures that are bound to the earth. You are suffering too much while dragging your fat, heavy bodies.  I, Daimon, will lighten your burden.")
	public static NpcStringId ID_1000460;
	
	@NpcString(id = 1000461, message = "A weak and stupid tribe like yours doesn't deserve to be my enemy! Bwa ha ha ha!")
	public static NpcStringId ID_1000461;
	
	@NpcString(id = 1000462, message = "You dare to invade the territory of Daimon, the White-Eyed!  Now, you will pay the price for your action!")
	public static NpcStringId ID_1000462;
	
	@NpcString(id = 1000463, message = "This is the grace of Daimon the White-Eyed, the great Monster Eye Lord! Ha Ha Ha!")
	public static NpcStringId ID_1000463;
	
	@NpcString(id = 1000464, message = "%s! You have become a Hero of Duelists. Congratulations!")
	public static NpcStringId ID_1000464;
	
	@NpcString(id = 1000465, message = "%s! You have become a Hero of Dreadnoughts. Congratulations!")
	public static NpcStringId ID_1000465;
	
	@NpcString(id = 1000466, message = "%s! You have become a Hero of Phoenix Knights. Congratulations!")
	public static NpcStringId ID_1000466;
	
	@NpcString(id = 1000467, message = "%s! You have become a Hero of Hell Knights. Congratulations!")
	public static NpcStringId ID_1000467;
	
	@NpcString(id = 1000468, message = "%s! You have become a Sagittarius Hero. Congratulations!")
	public static NpcStringId ID_1000468;
	
	@NpcString(id = 1000469, message = "%s! You have become a Hero of Adventurers. Congratulations!")
	public static NpcStringId ID_1000469;
	
	@NpcString(id = 1000470, message = "%s! You have become a Hero of Archmages. Congratulations!")
	public static NpcStringId ID_1000470;
	
	@NpcString(id = 1000471, message = "%s! You have become a Hero of Soultakers. Congratulations!")
	public static NpcStringId ID_1000471;
	
	@NpcString(id = 1000472, message = "%s! You have become a Hero of Arcana Lords. Congratulations!")
	public static NpcStringId ID_1000472;
	
	@NpcString(id = 1000473, message = "%s! You have become a Hero of Cardinals. Congratulations!")
	public static NpcStringId ID_1000473;
	
	@NpcString(id = 1000474, message = "%s! You have become a Hero of Hierophants. Congratulations!")
	public static NpcStringId ID_1000474;
	
	@NpcString(id = 1000475, message = "%s! You have become a Hero of Eva's Templars. Congratulations!")
	public static NpcStringId ID_1000475;
	
	@NpcString(id = 1000476, message = "%s! You have become a Hero of Sword Muses. Congratulations!")
	public static NpcStringId ID_1000476;
	
	@NpcString(id = 1000477, message = "%s! You have become a Hero of Wind Riders. Congratulations!")
	public static NpcStringId ID_1000477;
	
	@NpcString(id = 1000478, message = "%s! You have become a Hero of Moonlight Sentinels. Congratulations!")
	public static NpcStringId ID_1000478;
	
	@NpcString(id = 1000479, message = "%s! You have become a Hero of Mystic Muses. Congratulations!")
	public static NpcStringId ID_1000479;
	
	@NpcString(id = 1000480, message = "%s! You have become a Hero of Elemental Masters. Congratulations!")
	public static NpcStringId ID_1000480;
	
	@NpcString(id = 1000481, message = "%s! You have become a Hero of Eva's Saints. Congratulations!")
	public static NpcStringId ID_1000481;
	
	@NpcString(id = 1000482, message = "%s! You have become a Hero of the Shillien Templars. Congratulations!")
	public static NpcStringId ID_1000482;
	
	@NpcString(id = 1000483, message = "%s! You have become a Hero of Spectral Dancers. Congratulations!")
	public static NpcStringId ID_1000483;
	
	@NpcString(id = 1000484, message = "%s! You have become a Hero of Ghost Hunters. Congratulations!")
	public static NpcStringId ID_1000484;
	
	@NpcString(id = 1000485, message = "%s! You have become a Hero of Ghost Sentinels. Congratulations!")
	public static NpcStringId ID_1000485;
	
	@NpcString(id = 1000486, message = "%s! You have become a Hero of Storm Screamers. Congratulations!")
	public static NpcStringId ID_1000486;
	
	@NpcString(id = 1000487, message = "%s! You have become a Hero of Spectral Masters. Congratulations!")
	public static NpcStringId ID_1000487;
	
	@NpcString(id = 1000488, message = "%s! You have become a Hero of the Shillien Saints. Congratulations!")
	public static NpcStringId ID_1000488;
	
	@NpcString(id = 1000489, message = "%s! You have become a Hero of Titans. Congratulations!")
	public static NpcStringId ID_1000489;
	
	@NpcString(id = 1000490, message = "%s! You have become a Hero of Grand Khavataris. Congratulations!")
	public static NpcStringId ID_1000490;
	
	@NpcString(id = 1000491, message = "%s! You have become a Hero of Dominators. Congratulations!")
	public static NpcStringId ID_1000491;
	
	@NpcString(id = 1000492, message = "%s! You have become a Hero of Doomcryers. Congratulations!")
	public static NpcStringId ID_1000492;
	
	@NpcString(id = 1000493, message = "%s! You have become a Hero of Fortune Seekers. Congratulations!")
	public static NpcStringId ID_1000493;
	
	@NpcString(id = 1000494, message = "%s! You have become a Hero of Maestros. Congratulations!")
	public static NpcStringId ID_1000494;
	
	@NpcString(id = 1000495, message = "**unregistered**")
	public static NpcStringId ID_1000495;
	
	@NpcString(id = 1000500, message = "You may now enter the Sepulcher.")
	public static NpcStringId ID_1000500;
	
	@NpcString(id = 1000501, message = "If you place your hand on the stone statue in front of each sepulcher, you will be able to enter.")
	public static NpcStringId ID_1000501;
	
	@NpcString(id = 1000502, message = "The monsters have spawned!")
	public static NpcStringId ID_1000502;
	
	@NpcString(id = 1000503, message = "Thank you for saving me.")
	public static NpcStringId ID_1000503;
	
	@NpcString(id = 1000504, message = "Fewer than %s")
	public static NpcStringId ID_1000504;
	
	@NpcString(id = 1000505, message = "More than %s")
	public static NpcStringId ID_1000505;
	
	@NpcString(id = 1000507, message = "Competition")
	public static NpcStringId ID_1000507;
	
	@NpcString(id = 1000508, message = "Seal Validation")
	public static NpcStringId ID_1000508;
	
	@NpcString(id = 1000509, message = "Preparation")
	public static NpcStringId ID_1000509;
	
	@NpcString(id = 1000510, message = "Dusk")
	public static NpcStringId ID_1000510;
	
	@NpcString(id = 1000511, message = "Dawn")
	public static NpcStringId ID_1000511;
	
	@NpcString(id = 1000512, message = "No Owner")
	public static NpcStringId ID_1000512;
	
	@NpcString(id = 1000513, message = "This place is dangerous, %s. Please turn back.")
	public static NpcStringId ID_1000513;
	
	@NpcString(id = 1000514, message = "Who disturbs my sacred sleep?")
	public static NpcStringId ID_1000514;
	
	@NpcString(id = 1000515, message = "Begone, thief! Let our bones rest in peace.")
	public static NpcStringId ID_1000515;
	
	@NpcString(id = 1000516, message = "Leave us be, Hestui scum!")
	public static NpcStringId ID_1000516;
	
	@NpcString(id = 1000517, message = "Thieving Kakai, may bloodbugs gnaw you in your sleep!")
	public static NpcStringId ID_1000517;
	
	@NpcString(id = 1000518, message = "Newbie Travel Token")
	public static NpcStringId ID_1000518;
	
	@NpcString(id = 1000519, message = "Arrogant fool! You dare to challenge me, the Ruler of Flames? Here is your reward!")
	public static NpcStringId ID_1000519;
	
	@NpcString(id = 1000520, message = "You cannot hope to defeat me with your meager strength.")
	public static NpcStringId ID_1000520;
	
	@NpcString(id = 1000521, message = "Not even the gods themselves could touch me. But you, %s, you dare challenge me?! Ignorant mortal!")
	public static NpcStringId ID_1000521;
	
	@NpcString(id = 1000522, message = "Requiem of Hatred")
	public static NpcStringId ID_1000522;
	
	@NpcString(id = 1000523, message = "Fugue of Jubilation")
	public static NpcStringId ID_1000523;
	
	@NpcString(id = 1000524, message = "Frenetic Toccata")
	public static NpcStringId ID_1000524;
	
	@NpcString(id = 1000525, message = "Hypnotic Mazurka")
	public static NpcStringId ID_1000525;
	
	@NpcString(id = 1000526, message = "Mournful Chorale Prelude")
	public static NpcStringId ID_1000526;
	
	@NpcString(id = 1000527, message = "Rondo of Solitude")
	public static NpcStringId ID_1000527;
	
	@NpcString(id = 1001000, message = "The Kingdom of Aden")
	public static NpcStringId ID_1001000;
	
	@NpcString(id = 1001001, message = "Gludio")
	public static NpcStringId ID_1001001;
	
	@NpcString(id = 1001002, message = "Dion")
	public static NpcStringId ID_1001002;
	
	@NpcString(id = 1001003, message = "Giran")
	public static NpcStringId ID_1001003;
	
	@NpcString(id = 1001004, message = "Oren")
	public static NpcStringId ID_1001004;
	
	@NpcString(id = 1001005, message = "Aden")
	public static NpcStringId ID_1001005;
	
	@NpcString(id = 1001006, message = "Innadril")
	public static NpcStringId ID_1001006;
	
	@NpcString(id = 1001007, message = "Goddard")
	public static NpcStringId ID_1001007;
	
	@NpcString(id = 1001008, message = "Rune")
	public static NpcStringId ID_1001008;
	
	@NpcString(id = 1001009, message = "Schuttgart")
	public static NpcStringId ID_1001009;
	
	@NpcString(id = 1001100, message = "The Kingdom of Elmore")
	public static NpcStringId ID_1001100;
	
	@NpcString(id = 1010001, message = "Talking Island Village")
	public static NpcStringId ID_1010001;
	
	@NpcString(id = 1010002, message = "The Elven Village")
	public static NpcStringId ID_1010002;
	
	@NpcString(id = 1010003, message = "The Dark Elf Village")
	public static NpcStringId ID_1010003;
	
	@NpcString(id = 1010004, message = "The Village of Gludin")
	public static NpcStringId ID_1010004;
	
	@NpcString(id = 1010005, message = "The Town of Gludio")
	public static NpcStringId ID_1010005;
	
	@NpcString(id = 1010006, message = "The Town of Dion")
	public static NpcStringId ID_1010006;
	
	@NpcString(id = 1010007, message = "The Town of Giran")
	public static NpcStringId ID_1010007;
	
	@NpcString(id = 1010008, message = "Orc Village")
	public static NpcStringId ID_1010008;
	
	@NpcString(id = 1010009, message = "Dwarven Village")
	public static NpcStringId ID_1010009;
	
	@NpcString(id = 1010010, message = "The Southern Part of the Dark Elven Forest")
	public static NpcStringId ID_1010010;
	
	@NpcString(id = 1010011, message = "The Northeast Coast")
	public static NpcStringId ID_1010011;
	
	@NpcString(id = 1010012, message = "The Southern Entrance to the Wastelands")
	public static NpcStringId ID_1010012;
	
	@NpcString(id = 1010013, message = "Town of Oren")
	public static NpcStringId ID_1010013;
	
	@NpcString(id = 1010014, message = "Ivory Tower")
	public static NpcStringId ID_1010014;
	
	@NpcString(id = 1010015, message = "1st Floor Ivory Tower Lobby")
	public static NpcStringId ID_1010015;
	
	@NpcString(id = 1010016, message = "Underground Shopping Area")
	public static NpcStringId ID_1010016;
	
	@NpcString(id = 1010017, message = "2nd Floor Human Wizard Guild")
	public static NpcStringId ID_1010017;
	
	@NpcString(id = 1010018, message = "3rd Floor Elven Wizard Guild")
	public static NpcStringId ID_1010018;
	
	@NpcString(id = 1010019, message = "4th Floor Dark Wizard Guild")
	public static NpcStringId ID_1010019;
	
	@NpcString(id = 1010020, message = "Hunters Village")
	public static NpcStringId ID_1010020;
	
	@NpcString(id = 1010021, message = "Giran Harbor")
	public static NpcStringId ID_1010021;
	
	@NpcString(id = 1010022, message = "Hardin's Private Academy")
	public static NpcStringId ID_1010022;
	
	@NpcString(id = 1010023, message = "Town of Aden")
	public static NpcStringId ID_1010023;
	
	@NpcString(id = 1010024, message = "Village Square")
	public static NpcStringId ID_1010024;
	
	@NpcString(id = 1010025, message = "North Gate Entrance")
	public static NpcStringId ID_1010025;
	
	@NpcString(id = 1010026, message = "East Gate Entrance")
	public static NpcStringId ID_1010026;
	
	@NpcString(id = 1010027, message = "West Gate Entrance")
	public static NpcStringId ID_1010027;
	
	@NpcString(id = 1010028, message = "South Gate Entrance")
	public static NpcStringId ID_1010028;
	
	@NpcString(id = 1010029, message = "Entrance to Turek Orc Camp")
	public static NpcStringId ID_1010029;
	
	@NpcString(id = 1010030, message = "Entrance to Forgotten Temple")
	public static NpcStringId ID_1010030;
	
	@NpcString(id = 1010031, message = "Entrance to the Wastelands")
	public static NpcStringId ID_1010031;
	
	@NpcString(id = 1010032, message = "Entrance to Abandoned Camp")
	public static NpcStringId ID_1010032;
	
	@NpcString(id = 1010033, message = "Entrance to Cruma Marshlands")
	public static NpcStringId ID_1010033;
	
	@NpcString(id = 1010034, message = "Entrance to Execution Grounds")
	public static NpcStringId ID_1010034;
	
	@NpcString(id = 1010035, message = "Entrance to Partisan Hideaway")
	public static NpcStringId ID_1010035;
	
	@NpcString(id = 1010036, message = "Entrance to Floran Village")
	public static NpcStringId ID_1010036;
	
	@NpcString(id = 1010037, message = "Neutral Zone")
	public static NpcStringId ID_1010037;
	
	@NpcString(id = 1010038, message = "Western Road of Giran")
	public static NpcStringId ID_1010038;
	
	@NpcString(id = 1010039, message = "Eastern Road of Gludin Village")
	public static NpcStringId ID_1010039;
	
	@NpcString(id = 1010040, message = "Entrance to Partisan Hideaway")
	public static NpcStringId ID_1010040;
	
	@NpcString(id = 1010041, message = "Entrance to Cruma Tower")
	public static NpcStringId ID_1010041;
	
	@NpcString(id = 1010042, message = "Death Pass")
	public static NpcStringId ID_1010042;
	
	@NpcString(id = 1010043, message = "Dark Forest")
	public static NpcStringId ID_1010043;
	
	@NpcString(id = 1010044, message = "Black Rock Hill")
	public static NpcStringId ID_1010044;
	
	@NpcString(id = 1010045, message = "Center of Immortal plateau")
	public static NpcStringId ID_1010045;
	
	@NpcString(id = 1010046, message = "Southern Immortal Plateau")
	public static NpcStringId ID_1010046;
	
	@NpcString(id = 1010047, message = "Southern Immortal Plateau Mountains")
	public static NpcStringId ID_1010047;
	
	@NpcString(id = 1010048, message = "Frozen Waterfalls")
	public static NpcStringId ID_1010048;
	
	@NpcString(id = 1010049, message = "Heine")
	public static NpcStringId ID_1010049;
	
	@NpcString(id = 1010050, message = "Tower of Insolence - 1st Floor")
	public static NpcStringId ID_1010050;
	
	@NpcString(id = 1010051, message = "Tower of Insolence - 5th Floor")
	public static NpcStringId ID_1010051;
	
	@NpcString(id = 1010052, message = "Tower of Insolence - 10th Floor")
	public static NpcStringId ID_1010052;
	
	@NpcString(id = 1010053, message = "Coliseum near Aden Castle Town")
	public static NpcStringId ID_1010053;
	
	@NpcString(id = 1010054, message = "Monster Derby Track")
	public static NpcStringId ID_1010054;
	
	@NpcString(id = 1010055, message = "Border Outpost")
	public static NpcStringId ID_1010055;
	
	@NpcString(id = 1010056, message = "Entrance to the Sea of Spores")
	public static NpcStringId ID_1010056;
	
	@NpcString(id = 1010057, message = "Ancient Battleground")
	public static NpcStringId ID_1010057;
	
	@NpcString(id = 1010058, message = "Entrance to Silent Valley")
	public static NpcStringId ID_1010058;
	
	@NpcString(id = 1010059, message = "Entrance to Tower of Insolence")
	public static NpcStringId ID_1010059;
	
	@NpcString(id = 1010060, message = "Blazing Swamp")
	public static NpcStringId ID_1010060;
	
	@NpcString(id = 1010061, message = "Entrance to Cemetery")
	public static NpcStringId ID_1010061;
	
	@NpcString(id = 1010062, message = "Entrance to Giants Cave")
	public static NpcStringId ID_1010062;
	
	@NpcString(id = 1010063, message = "Entrance to Forest of Mirrors")
	public static NpcStringId ID_1010063;
	
	@NpcString(id = 1010064, message = "The Forbidden Gateway")
	public static NpcStringId ID_1010064;
	
	@NpcString(id = 1010065, message = "Entrance to Tower of Insolence")
	public static NpcStringId ID_1010065;
	
	@NpcString(id = 1010066, message = "Tanor Canyon")
	public static NpcStringId ID_1010066;
	
	@NpcString(id = 1010067, message = "Dragon Valley")
	public static NpcStringId ID_1010067;
	
	@NpcString(id = 1010068, message = "Oracle of Dawn")
	public static NpcStringId ID_1010068;
	
	@NpcString(id = 1010069, message = "Oracle of Dusk")
	public static NpcStringId ID_1010069;
	
	@NpcString(id = 1010070, message = "Necropolis of Sacrifice")
	public static NpcStringId ID_1010070;
	
	@NpcString(id = 1010071, message = "The Pilgrim's Necropolis")
	public static NpcStringId ID_1010071;
	
	@NpcString(id = 1010072, message = "Necropolis of Worship")
	public static NpcStringId ID_1010072;
	
	@NpcString(id = 1010073, message = "The Patriot's Necropolis")
	public static NpcStringId ID_1010073;
	
	@NpcString(id = 1010074, message = "Necropolis of Devotion")
	public static NpcStringId ID_1010074;
	
	@NpcString(id = 1010075, message = "Necropolis of Martyrdom")
	public static NpcStringId ID_1010075;
	
	@NpcString(id = 1010076, message = "The Disciple's Necropolis")
	public static NpcStringId ID_1010076;
	
	@NpcString(id = 1010077, message = "The Saint's Necropolis")
	public static NpcStringId ID_1010077;
	
	@NpcString(id = 1010078, message = "The Catacomb of the Heretic")
	public static NpcStringId ID_1010078;
	
	@NpcString(id = 1010079, message = "Catacomb of the Branded")
	public static NpcStringId ID_1010079;
	
	@NpcString(id = 1010080, message = "Catacomb of the Apostate")
	public static NpcStringId ID_1010080;
	
	@NpcString(id = 1010081, message = "Catacomb of the Witch")
	public static NpcStringId ID_1010081;
	
	@NpcString(id = 1010082, message = "Catacomb of Dark Omens")
	public static NpcStringId ID_1010082;
	
	@NpcString(id = 1010083, message = "Catacomb of the Forbidden Path")
	public static NpcStringId ID_1010083;
	
	@NpcString(id = 1010084, message = "Entrance to the Ruins of Agony")
	public static NpcStringId ID_1010084;
	
	@NpcString(id = 1010085, message = "Entrance to the Ruins of Despair")
	public static NpcStringId ID_1010085;
	
	@NpcString(id = 1010086, message = "Entrance to the Ant Nest")
	public static NpcStringId ID_1010086;
	
	@NpcString(id = 1010087, message = "Southern part of Dion")
	public static NpcStringId ID_1010087;
	
	@NpcString(id = 1010088, message = "Entrance to Dragon Valley")
	public static NpcStringId ID_1010088;
	
	@NpcString(id = 1010089, message = "Field of Silence")
	public static NpcStringId ID_1010089;
	
	@NpcString(id = 1010090, message = "Western path Field of Whispers")
	public static NpcStringId ID_1010090;
	
	@NpcString(id = 1010091, message = "Entrance to Alligator Island")
	public static NpcStringId ID_1010091;
	
	@NpcString(id = 1010092, message = "Southern part of Oren")
	public static NpcStringId ID_1010092;
	
	@NpcString(id = 1010093, message = "Entrance to the Bandit Stronghold")
	public static NpcStringId ID_1010093;
	
	@NpcString(id = 1010094, message = "Windy Hill")
	public static NpcStringId ID_1010094;
	
	@NpcString(id = 1010095, message = "Orc Barracks")
	public static NpcStringId ID_1010095;
	
	@NpcString(id = 1010096, message = "Fellmere Harvesting Grounds")
	public static NpcStringId ID_1010096;
	
	@NpcString(id = 1010097, message = "Ruins of Agony")
	public static NpcStringId ID_1010097;
	
	@NpcString(id = 1010098, message = "Abandoned Camp")
	public static NpcStringId ID_1010098;
	
	@NpcString(id = 1010099, message = "Red Rock Ridge")
	public static NpcStringId ID_1010099;
	
	@NpcString(id = 1010100, message = "Necropolis of Sacrifice")
	public static NpcStringId ID_1010100;
	
	@NpcString(id = 1010101, message = "Ruins of Despair")
	public static NpcStringId ID_1010101;
	
	@NpcString(id = 1010102, message = "Windawood Manor")
	public static NpcStringId ID_1010102;
	
	@NpcString(id = 1010103, message = "North Entrance to the Ant Nest")
	public static NpcStringId ID_1010103;
	
	@NpcString(id = 1010104, message = "West Entrance to the Ant Nest")
	public static NpcStringId ID_1010104;
	
	@NpcString(id = 1010105, message = "South Entrance to the Ant Nest")
	public static NpcStringId ID_1010105;
	
	@NpcString(id = 1010106, message = "Forgotten Temple")
	public static NpcStringId ID_1010106;
	
	@NpcString(id = 1010107, message = "Southern entrance to Ant Nest")
	public static NpcStringId ID_1010107;
	
	@NpcString(id = 1010108, message = "East Gate of Ant Nest")
	public static NpcStringId ID_1010108;
	
	@NpcString(id = 1010109, message = "West Gate of Ant Nest")
	public static NpcStringId ID_1010109;
	
	@NpcString(id = 1010110, message = "Cruma Marshland")
	public static NpcStringId ID_1010110;
	
	@NpcString(id = 1010111, message = "Plains of Dion")
	public static NpcStringId ID_1010111;
	
	@NpcString(id = 1010112, message = "Bee Hive")
	public static NpcStringId ID_1010112;
	
	@NpcString(id = 1010113, message = "Partisan Hideaway")
	public static NpcStringId ID_1010113;
	
	@NpcString(id = 1010114, message = "Execution Grounds")
	public static NpcStringId ID_1010114;
	
	@NpcString(id = 1010115, message = "Giran Territory")
	public static NpcStringId ID_1010115;
	
	@NpcString(id = 1010116, message = "Cruma Tower")
	public static NpcStringId ID_1010116;
	
	@NpcString(id = 1010117, message = "Death Pass")
	public static NpcStringId ID_1010117;
	
	@NpcString(id = 1010118, message = "Breka's Stronghold")
	public static NpcStringId ID_1010118;
	
	@NpcString(id = 1010119, message = "Yageken garden")
	public static NpcStringId ID_1010119;
	
	@NpcString(id = 1010120, message = "Antharas Nest")
	public static NpcStringId ID_1010120;
	
	@NpcString(id = 1010121, message = "Sea of Spores")
	public static NpcStringId ID_1010121;
	
	@NpcString(id = 1010122, message = "Forest of Outlaws")
	public static NpcStringId ID_1010122;
	
	@NpcString(id = 1010123, message = "Forest of Evil Spirits and Ivory Tower")
	public static NpcStringId ID_1010123;
	
	@NpcString(id = 1010124, message = "Timak Outpost")
	public static NpcStringId ID_1010124;
	
	@NpcString(id = 1010125, message = "Oren territory")
	public static NpcStringId ID_1010125;
	
	@NpcString(id = 1010126, message = "to put under house arrest master shack")
	public static NpcStringId ID_1010126;
	
	@NpcString(id = 1010127, message = "Ancient Battleground")
	public static NpcStringId ID_1010127;
	
	@NpcString(id = 1010128, message = "North Elven valley entrance")
	public static NpcStringId ID_1010128;
	
	@NpcString(id = 1010129, message = "South Elven valley entrance")
	public static NpcStringId ID_1010129;
	
	@NpcString(id = 1010130, message = "Hunters Valley")
	public static NpcStringId ID_1010130;
	
	@NpcString(id = 1010131, message = "Western Entrance of Blazing Swamp")
	public static NpcStringId ID_1010131;
	
	@NpcString(id = 1010132, message = "Eastern Entrance of Blazing Swamp")
	public static NpcStringId ID_1010132;
	
	@NpcString(id = 1010133, message = "Plains of Glory")
	public static NpcStringId ID_1010133;
	
	@NpcString(id = 1010134, message = "War-Torn Plains")
	public static NpcStringId ID_1010134;
	
	@NpcString(id = 1010135, message = "Northern entrance to Forest of Mirrors")
	public static NpcStringId ID_1010135;
	
	@NpcString(id = 1010136, message = "The Front of Anghel Waterfall")
	public static NpcStringId ID_1010136;
	
	@NpcString(id = 1010137, message = "South of the Fields of Massacre")
	public static NpcStringId ID_1010137;
	
	@NpcString(id = 1010138, message = "North of the Fields of Massacre")
	public static NpcStringId ID_1010138;
	
	@NpcString(id = 1010139, message = "Northern entrance to Cemetery")
	public static NpcStringId ID_1010139;
	
	@NpcString(id = 1010140, message = "Southern entrance to Cemetery")
	public static NpcStringId ID_1010140;
	
	@NpcString(id = 1010141, message = "Western entrance to Cemetery")
	public static NpcStringId ID_1010141;
	
	@NpcString(id = 1010142, message = "Entrance of the Forbidden Gateway")
	public static NpcStringId ID_1010142;
	
	@NpcString(id = 1010143, message = "Forsaken Plains")
	public static NpcStringId ID_1010143;
	
	@NpcString(id = 1010144, message = "Tower of Insolence")
	public static NpcStringId ID_1010144;
	
	@NpcString(id = 1010145, message = "The Giant's Cave")
	public static NpcStringId ID_1010145;
	
	@NpcString(id = 1010146, message = "Northern Region of Field of Silence")
	public static NpcStringId ID_1010146;
	
	@NpcString(id = 1010147, message = "Western side of Field of Silence")
	public static NpcStringId ID_1010147;
	
	@NpcString(id = 1010148, message = "Field of Whispers East Region")
	public static NpcStringId ID_1010148;
	
	@NpcString(id = 1010149, message = "Field of Whispers West Region")
	public static NpcStringId ID_1010149;
	
	@NpcString(id = 1010150, message = "Alligator Island")
	public static NpcStringId ID_1010150;
	
	@NpcString(id = 1010151, message = "Alligator Beach")
	public static NpcStringId ID_1010151;
	
	@NpcString(id = 1010152, message = "Devil's Isle")
	public static NpcStringId ID_1010152;
	
	@NpcString(id = 1010153, message = "Garden of Eva")
	public static NpcStringId ID_1010153;
	
	@NpcString(id = 1010154, message = "Talking Island Village")
	public static NpcStringId ID_1010154;
	
	@NpcString(id = 1010155, message = "Elven village")
	public static NpcStringId ID_1010155;
	
	@NpcString(id = 1010156, message = "Dark Elven village")
	public static NpcStringId ID_1010156;
	
	@NpcString(id = 1010157, message = "Orc village")
	public static NpcStringId ID_1010157;
	
	@NpcString(id = 1010158, message = "Dwarf village")
	public static NpcStringId ID_1010158;
	
	@NpcString(id = 1010159, message = "Scenic Deck of Iris Lake")
	public static NpcStringId ID_1010159;
	
	@NpcString(id = 1010160, message = "Altar of Rites")
	public static NpcStringId ID_1010160;
	
	@NpcString(id = 1010161, message = "Dark Waterfall")
	public static NpcStringId ID_1010161;
	
	@NpcString(id = 1010162, message = "Neutral Zone")
	public static NpcStringId ID_1010162;
	
	@NpcString(id = 1010163, message = "Catacomb of Dark Omens")
	public static NpcStringId ID_1010163;
	
	@NpcString(id = 1010164, message = "Swampland")
	public static NpcStringId ID_1010164;
	
	@NpcString(id = 1010165, message = "Black Rock Hill")
	public static NpcStringId ID_1010165;
	
	@NpcString(id = 1010166, message = "Spider Nest")
	public static NpcStringId ID_1010166;
	
	@NpcString(id = 1010167, message = "Elven forest")
	public static NpcStringId ID_1010167;
	
	@NpcString(id = 1010168, message = "Obelisk of Victory")
	public static NpcStringId ID_1010168;
	
	@NpcString(id = 1010169, message = "North part of Talking Island")
	public static NpcStringId ID_1010169;
	
	@NpcString(id = 1010170, message = "South part of Talking Island")
	public static NpcStringId ID_1010170;
	
	@NpcString(id = 1010171, message = "Looting Area of Evil Spirits")
	public static NpcStringId ID_1010171;
	
	@NpcString(id = 1010172, message = "Maille Lizardman Barracks")
	public static NpcStringId ID_1010172;
	
	@NpcString(id = 1010173, message = "Ruins of Agony crossroads")
	public static NpcStringId ID_1010173;
	
	@NpcString(id = 1010174, message = "Ruins of Despair crossroads")
	public static NpcStringId ID_1010174;
	
	@NpcString(id = 1010175, message = "Windmill Hill")
	public static NpcStringId ID_1010175;
	
	@NpcString(id = 1010176, message = "Forgotten Temple")
	public static NpcStringId ID_1010176;
	
	@NpcString(id = 1010177, message = "Floran Agricultural Area")
	public static NpcStringId ID_1010177;
	
	@NpcString(id = 1010178, message = "Dion Territory")
	public static NpcStringId ID_1010178;
	
	@NpcString(id = 1010179, message = "Plains of the Lizardmen")
	public static NpcStringId ID_1010179;
	
	@NpcString(id = 1010180, message = "Forest of Evil Spirits")
	public static NpcStringId ID_1010180;
	
	@NpcString(id = 1010181, message = "Fields of Massacre")
	public static NpcStringId ID_1010181;
	
	@NpcString(id = 1010182, message = "Silent Valley")
	public static NpcStringId ID_1010182;
	
	@NpcString(id = 1010183, message = "Nothern part of Immortal Plateau")
	public static NpcStringId ID_1010183;
	
	@NpcString(id = 1010184, message = "Southern Part of North Immortal Plateau")
	public static NpcStringId ID_1010184;
	
	@NpcString(id = 1010185, message = "North of Southern Immortal Plateau")
	public static NpcStringId ID_1010185;
	
	@NpcString(id = 1010186, message = "Southern Part of South Immortal Plateau")
	public static NpcStringId ID_1010186;
	
	@NpcString(id = 1010187, message = "West Mining Region")
	public static NpcStringId ID_1010187;
	
	@NpcString(id = 1010188, message = "west of the Mining Region")
	public static NpcStringId ID_1010188;
	
	@NpcString(id = 1010189, message = "East of the Mining Region")
	public static NpcStringId ID_1010189;
	
	@NpcString(id = 1010190, message = "Entrance to Abandoned Mine")
	public static NpcStringId ID_1010190;
	
	@NpcString(id = 1010191, message = "Entrance to Mithril Mines")
	public static NpcStringId ID_1010191;
	
	@NpcString(id = 1010192, message = "Disciples Necropolis")
	public static NpcStringId ID_1010192;
	
	@NpcString(id = 1010193, message = "Tower of Insolence - 3rd Floor")
	public static NpcStringId ID_1010193;
	
	@NpcString(id = 1010194, message = "Tower of Insolence - 5th Floor")
	public static NpcStringId ID_1010194;
	
	@NpcString(id = 1010195, message = "Tower of Insolence - 7th Floor")
	public static NpcStringId ID_1010195;
	
	@NpcString(id = 1010196, message = "Tower of Insolence - 10th Floor")
	public static NpcStringId ID_1010196;
	
	@NpcString(id = 1010197, message = "Tower of Insolence - 13th Floor")
	public static NpcStringId ID_1010197;
	
	@NpcString(id = 1010198, message = "Garden of Eva")
	public static NpcStringId ID_1010198;
	
	@NpcString(id = 1010199, message = "Goddard Castle Town")
	public static NpcStringId ID_1010199;
	
	@NpcString(id = 1010200, message = "Town of Aden")
	public static NpcStringId ID_1010200;
	
	@NpcString(id = 1010201, message = "A delivery for Mr. Lector? Very good!")
	public static NpcStringId ID_1010201;
	
	@NpcString(id = 1010202, message = "I need a break!")
	public static NpcStringId ID_1010202;
	
	@NpcString(id = 1010203, message = "Hello, Mr. Lector! Long time no see, Mr. Jackson!")
	public static NpcStringId ID_1010203;
	
	@NpcString(id = 1010204, message = "Lulu!")
	public static NpcStringId ID_1010204;
	
	@NpcString(id = 1010205, message = "Where has he gone?")
	public static NpcStringId ID_1010205;
	
	@NpcString(id = 1010206, message = "Have you seen Windawood?")
	public static NpcStringId ID_1010206;
	
	@NpcString(id = 1010207, message = "Where did he go?")
	public static NpcStringId ID_1010207;
	
	@NpcString(id = 1010208, message = "The Mother Tree is slowly dying.")
	public static NpcStringId ID_1010208;
	
	@NpcString(id = 1010209, message = "How can we save the Mother Tree?")
	public static NpcStringId ID_1010209;
	
	@NpcString(id = 1010210, message = "The Mother Tree is always so gorgeous!")
	public static NpcStringId ID_1010210;
	
	@NpcString(id = 1010211, message = "Lady Mirabel, may the peace of the lake be with you!")
	public static NpcStringId ID_1010211;
	
	@NpcString(id = 1010212, message = "You're a hard worker, Rayla!")
	public static NpcStringId ID_1010212;
	
	@NpcString(id = 1010213, message = "You're a hard worker!")
	public static NpcStringId ID_1010213;
	
	@NpcString(id = 1010214, message = "The mass of darkness will start in a couple of days. Pay more attention to the guard!")
	public static NpcStringId ID_1010214;
	
	@NpcString(id = 1010215, message = "Have you seen Torocco today?")
	public static NpcStringId ID_1010215;
	
	@NpcString(id = 1010216, message = "Have you seen Torocco?")
	public static NpcStringId ID_1010216;
	
	@NpcString(id = 1010217, message = "Where is that fool hiding?")
	public static NpcStringId ID_1010217;
	
	@NpcString(id = 1010218, message = "Care to go a round?")
	public static NpcStringId ID_1010218;
	
	@NpcString(id = 1010219, message = "Have a nice day, Mr. Garita and Mion!")
	public static NpcStringId ID_1010219;
	
	@NpcString(id = 1010220, message = "Mr. Lid, Murdoc, and Airy! How are you doing?")
	public static NpcStringId ID_1010220;
	
	@NpcString(id = 1010221, message = "Greyclaw Kutus (lv23)")
	public static NpcStringId ID_1010221;
	
	@NpcString(id = 1010222, message = "Turek Mercenary Captain (lv30)")
	public static NpcStringId ID_1010222;
	
	@NpcString(id = 1010223, message = "Retreat Spider Cletu (lv42)")
	public static NpcStringId ID_1010223;
	
	@NpcString(id = 1010224, message = "Furious Thieles (lv55)")
	public static NpcStringId ID_1010224;
	
	@NpcString(id = 1010225, message = "Ghost of Peasant Leader (lv50)")
	public static NpcStringId ID_1010225;
	
	@NpcString(id = 1010226, message = "The 3rd Underwater Guardian (lv60)")
	public static NpcStringId ID_1010226;
	
	@NpcString(id = 1010227, message = "Pan Dryad (lv25)")
	public static NpcStringId ID_1010227;
	
	@NpcString(id = 1010228, message = "Breka Warlock Pastu (lv34)")
	public static NpcStringId ID_1010228;
	
	@NpcString(id = 1010229, message = "Stakato Queen Zyrnna (lv34)")
	public static NpcStringId ID_1010229;
	
	@NpcString(id = 1010230, message = "Katu Van Leader Atui (lv49)")
	public static NpcStringId ID_1010230;
	
	@NpcString(id = 1010231, message = "Atraiban (lv53)")
	public static NpcStringId ID_1010231;
	
	@NpcString(id = 1010232, message = "Eva's Guardian Millenu (lv65)")
	public static NpcStringId ID_1010232;
	
	@NpcString(id = 1010233, message = "Shilen's Messenger Cabrio (lv70)")
	public static NpcStringId ID_1010233;
	
	@NpcString(id = 1010234, message = "Tirak (lv28)")
	public static NpcStringId ID_1010234;
	
	@NpcString(id = 1010235, message = "Remmel (lv35)")
	public static NpcStringId ID_1010235;
	
	@NpcString(id = 1010236, message = "Barion (lv47)")
	public static NpcStringId ID_1010236;
	
	@NpcString(id = 1010237, message = "Karte (lv49)")
	public static NpcStringId ID_1010237;
	
	@NpcString(id = 1010238, message = "Verfa (lv51)")
	public static NpcStringId ID_1010238;
	
	@NpcString(id = 1010239, message = "Rahha (lv65)")
	public static NpcStringId ID_1010239;
	
	@NpcString(id = 1010240, message = "Kernon (lv75)")
	public static NpcStringId ID_1010240;
	
	@NpcString(id = 1010241, message = "Biconne of Blue Sky (lv45)")
	public static NpcStringId ID_1010241;
	
	@NpcString(id = 1010242, message = "Unrequited Kael (lv24)")
	public static NpcStringId ID_1010242;
	
	@NpcString(id = 1010243, message = "Chertuba of Great Soul (lv35)")
	public static NpcStringId ID_1010243;
	
	@NpcString(id = 1010244, message = "Wizard of Storm Teruk (lv40)")
	public static NpcStringId ID_1010244;
	
	@NpcString(id = 1010245, message = "Captain of Red Flag Shaka (lv52)")
	public static NpcStringId ID_1010245;
	
	@NpcString(id = 1010246, message = "Enchanted Forest Watcher Ruell (lv55)")
	public static NpcStringId ID_1010246;
	
	@NpcString(id = 1010247, message = "Bloody Priest Rudelto (lv69)")
	public static NpcStringId ID_1010247;
	
	@NpcString(id = 1010248, message = "Princess Molrang (lv25)")
	public static NpcStringId ID_1010248;
	
	@NpcString(id = 1010249, message = "Cat's Eye Bandit (lv30)")
	public static NpcStringId ID_1010249;
	
	@NpcString(id = 1010250, message = "Leader of Cat Gang (lv39)")
	public static NpcStringId ID_1010250;
	
	@NpcString(id = 1010251, message = "Timak Orc Chief Ranger (lv44)")
	public static NpcStringId ID_1010251;
	
	@NpcString(id = 1010252, message = "Crazy Mechanic Golem (lv43)")
	public static NpcStringId ID_1010252;
	
	@NpcString(id = 1010253, message = "Soulless Wild Boar (lv59)")
	public static NpcStringId ID_1010253;
	
	@NpcString(id = 1010254, message = "Korim (lv70)")
	public static NpcStringId ID_1010254;
	
	@NpcString(id = 1010255, message = "Elf Renoa (lv29)")
	public static NpcStringId ID_1010255;
	
	@NpcString(id = 1010256, message = "Sejarr's Servitor (lv35)")
	public static NpcStringId ID_1010256;
	
	@NpcString(id = 1010257, message = "Rotten Tree Repiro (lv44)")
	public static NpcStringId ID_1010257;
	
	@NpcString(id = 1010258, message = "Shacram (lv45)")
	public static NpcStringId ID_1010258;
	
	@NpcString(id = 1010259, message = "Sorcerer Isirr (lv55)")
	public static NpcStringId ID_1010259;
	
	@NpcString(id = 1010260, message = "Ghost of the Well Lidia (lv63)")
	public static NpcStringId ID_1010260;
	
	@NpcString(id = 1010261, message = "Antharas Priest Cloe (lv74)")
	public static NpcStringId ID_1010261;
	
	@NpcString(id = 1010262, message = "Agent of Beres, Meana (lv30)")
	public static NpcStringId ID_1010262;
	
	@NpcString(id = 1010263, message = "Icarus Sample 1 (lv40)")
	public static NpcStringId ID_1010263;
	
	@NpcString(id = 1010264, message = "Warden of the Execution Ground, Guilotine (lv35)")
	public static NpcStringId ID_1010264;
	
	@NpcString(id = 1010265, message = "Messenger of Fairy Queen Berun (lv50)")
	public static NpcStringId ID_1010265;
	
	@NpcString(id = 1010266, message = "Refugee Hopeful Leo (lv56)")
	public static NpcStringId ID_1010266;
	
	@NpcString(id = 1010267, message = "Fierce Tiger King Angel (lv65)")
	public static NpcStringId ID_1010267;
	
	@NpcString(id = 1010268, message = "Longhorn Golkonda (lv79)")
	public static NpcStringId ID_1010268;
	
	@NpcString(id = 1010269, message = "Langk Matriarch Rashkos (lv24)")
	public static NpcStringId ID_1010269;
	
	@NpcString(id = 1010270, message = "Vuku Grand Seer Gharmash (lv33)")
	public static NpcStringId ID_1010270;
	
	@NpcString(id = 1010271, message = "Carnage Lord Gato (lv50)")
	public static NpcStringId ID_1010271;
	
	@NpcString(id = 1010272, message = "Leto Chief Talkin (lv40)")
	public static NpcStringId ID_1010272;
	
	@NpcString(id = 1010273, message = "Beleth's Seer Sephia (lv55)")
	public static NpcStringId ID_1010273;
	
	@NpcString(id = 1010274, message = "Hekaton Prime (lv65)")
	public static NpcStringId ID_1010274;
	
	@NpcString(id = 1010275, message = "Fire of Wrath Shuriel (lv79)")
	public static NpcStringId ID_1010275;
	
	@NpcString(id = 1010276, message = "Serpent Demon Bifrons (lv21)")
	public static NpcStringId ID_1010276;
	
	@NpcString(id = 1010277, message = "Zombie Lord Crowl (lv25)")
	public static NpcStringId ID_1010277;
	
	@NpcString(id = 1010278, message = "Flame Lord Shadar (lv35)")
	public static NpcStringId ID_1010278;
	
	@NpcString(id = 1010279, message = "Shaman King Selu (lv40)")
	public static NpcStringId ID_1010279;
	
	@NpcString(id = 1010280, message = "King Tarlk (lv48)")
	public static NpcStringId ID_1010280;
	
	@NpcString(id = 1010281, message = "Unicorn Paniel (lv54)")
	public static NpcStringId ID_1010281;
	
	@NpcString(id = 1010282, message = "Giant Marpanak (lv60)")
	public static NpcStringId ID_1010282;
	
	@NpcString(id = 1010283, message = "Roaring Skylancer (lv70)")
	public static NpcStringId ID_1010283;
	
	@NpcString(id = 1010284, message = "Ikuntai (lv25)")
	public static NpcStringId ID_1010284;
	
	@NpcString(id = 1010285, message = "Ragraman (lv30)")
	public static NpcStringId ID_1010285;
	
	@NpcString(id = 1010286, message = "Lizardmen Leader Hellion (lv38)")
	public static NpcStringId ID_1010286;
	
	@NpcString(id = 1010287, message = "Tiger King Karuta (lv45)")
	public static NpcStringId ID_1010287;
	
	@NpcString(id = 1010288, message = "Witch Wimere (lv55)")
	public static NpcStringId ID_1010288;
	
	@NpcString(id = 1010289, message = "Hatos (lv60)")
	public static NpcStringId ID_1010289;
	
	@NpcString(id = 1010290, message = "Demon Kurikups (lv65)")
	public static NpcStringId ID_1010290;
	
	@NpcString(id = 1010291, message = "Tasaba Patriarch Hellena (lv35)")
	public static NpcStringId ID_1010291;
	
	@NpcString(id = 1010292, message = "Apepi (lv30)")
	public static NpcStringId ID_1010292;
	
	@NpcString(id = 1010293, message = "Cronos's Servitor Mumu (lv34)")
	public static NpcStringId ID_1010293;
	
	@NpcString(id = 1010294, message = "Earth Protector Panathen (lv43)")
	public static NpcStringId ID_1010294;
	
	@NpcString(id = 1010295, message = "Fafurion's Herald Lokness (lv70)")
	public static NpcStringId ID_1010295;
	
	@NpcString(id = 1010296, message = "Water Dragon Seer Sheshark (lv72)")
	public static NpcStringId ID_1010296;
	
	@NpcString(id = 1010297, message = "Krokian Padisha Sobekk (lv74)")
	public static NpcStringId ID_1010297;
	
	@NpcString(id = 1010298, message = "Ocean Flame Ashakiel (lv76)")
	public static NpcStringId ID_1010298;
	
	@NpcString(id = 1010299, message = "Water Couatle Ateka (lv40)")
	public static NpcStringId ID_1010299;
	
	@NpcString(id = 1010300, message = "Sebek (lv36)")
	public static NpcStringId ID_1010300;
	
	@NpcString(id = 1010301, message = "Fafurion's Page Sika (lv40)")
	public static NpcStringId ID_1010301;
	
	@NpcString(id = 1010302, message = "Cursed Clara (lv50)")
	public static NpcStringId ID_1010302;
	
	@NpcString(id = 1010303, message = "Death Lord Hallate (lv73)")
	public static NpcStringId ID_1010303;
	
	@NpcString(id = 1010304, message = "Soul Collector Acheron (lv35)")
	public static NpcStringId ID_1010304;
	
	@NpcString(id = 1010305, message = "Roaring Lord Kastor (lv62)")
	public static NpcStringId ID_1010305;
	
	@NpcString(id = 1010306, message = "Storm Winged Naga (lv75)")
	public static NpcStringId ID_1010306;
	
	@NpcString(id = 1010307, message = "Timak Seer Ragoth (lv57)")
	public static NpcStringId ID_1010307;
	
	@NpcString(id = 1010308, message = "Spirit of Andras, the Betrayer (lv69)")
	public static NpcStringId ID_1010308;
	
	@NpcString(id = 1010309, message = "Ancient Weird Drake (lv65)")
	public static NpcStringId ID_1010309;
	
	@NpcString(id = 1010310, message = "Vanor Chief Kandra (lv72)")
	public static NpcStringId ID_1010310;
	
	@NpcString(id = 1010311, message = "Nightmare Drake (lv67)")
	public static NpcStringId ID_1010311;
	
	@NpcString(id = 1010312, message = "Harit Hero Tamash (lv55)")
	public static NpcStringId ID_1010312;
	
	@NpcString(id = 1010313, message = "Last Lesser Giant Olkuth (lv75)")
	public static NpcStringId ID_1010313;
	
	@NpcString(id = 1010314, message = "Last Lesser Giant Glaki (lv78)")
	public static NpcStringId ID_1010314;
	
	@NpcString(id = 1010315, message = "Doom Blade Tanatos (lv72)")
	public static NpcStringId ID_1010315;
	
	@NpcString(id = 1010316, message = "Vermilion, Blood Tree (lv75)")
	public static NpcStringId ID_1010316;
	
	@NpcString(id = 1010317, message = "Palibati Queen Themis (lv70)")
	public static NpcStringId ID_1010317;
	
	@NpcString(id = 1010318, message = "Gargoyle Lord Tiphon (lv65)")
	public static NpcStringId ID_1010318;
	
	@NpcString(id = 1010319, message = "Taik High Prefect Arak (lv60)")
	public static NpcStringId ID_1010319;
	
	@NpcString(id = 1010320, message = "Zaken's Butcher Krantz (lv55)")
	public static NpcStringId ID_1010320;
	
	@NpcString(id = 1010321, message = "Iron Giant Totem (lv45)")
	public static NpcStringId ID_1010321;
	
	@NpcString(id = 1010322, message = "Malruk's Witch Sekina (lv67)")
	public static NpcStringId ID_1010322;
	
	@NpcString(id = 1010323, message = "Bloody Empress Decarbia (lv75)")
	public static NpcStringId ID_1010323;
	
	@NpcString(id = 1010324, message = "Beast Lord Behemoth (lv70)")
	public static NpcStringId ID_1010324;
	
	@NpcString(id = 1010325, message = "Partisan Leader Talakin (lv28)")
	public static NpcStringId ID_1010325;
	
	@NpcString(id = 1010326, message = "Carnamakos (lv56)")
	public static NpcStringId ID_1010326;
	
	@NpcString(id = 1010327, message = "Death Lord Ipos (lv75)")
	public static NpcStringId ID_1010327;
	
	@NpcString(id = 1010328, message = "Lilith's Witch Marilion (lv50)")
	public static NpcStringId ID_1010328;
	
	@NpcString(id = 1010329, message = "Pagan Watcher Cerberon (lv55)")
	public static NpcStringId ID_1010329;
	
	@NpcString(id = 1010330, message = "Anakim's Nemesis Zakaron (lv70)")
	public static NpcStringId ID_1010330;
	
	@NpcString(id = 1010331, message = "Death Lord Shax (lv75)")
	public static NpcStringId ID_1010331;
	
	@NpcString(id = 1010332, message = "Guardian Deity of Hot Springs Hestia (lv78)")
	public static NpcStringId ID_1010332;
	
	@NpcString(id = 1010333, message = "Icicle Emperor Bumbalump (lv74)")
	public static NpcStringId ID_1010333;
	
	@NpcString(id = 1010334, message = "Ketra's Hero Hekaton (lv80)")
	public static NpcStringId ID_1010334;
	
	@NpcString(id = 1010335, message = "Ketra's Commander Tayr (lv84)")
	public static NpcStringId ID_1010335;
	
	@NpcString(id = 1010336, message = "Ketra's Chief Brakki (lv87)")
	public static NpcStringId ID_1010336;
	
	@NpcString(id = 1010337, message = "Hero of Varka Shadith (lv80)")
	public static NpcStringId ID_1010337;
	
	@NpcString(id = 1010338, message = "Varka's Commander Mos (lv84)")
	public static NpcStringId ID_1010338;
	
	@NpcString(id = 1010339, message = "Varka's Chief Horus (lv87)")
	public static NpcStringId ID_1010339;
	
	@NpcString(id = 1010340, message = "Ember (lv85)")
	public static NpcStringId ID_1010340;
	
	@NpcString(id = 1010341, message = "Demon's Agent Falston (lv66)")
	public static NpcStringId ID_1010341;
	
	@NpcString(id = 1010342, message = "Flame of Shine Barakiel (lv70)")
	public static NpcStringId ID_1010342;
	
	@NpcString(id = 1010343, message = "Eilhalder Von Hellman (lv71)")
	public static NpcStringId ID_1010343;
	
	@NpcString(id = 1010344, message = "Giant Wasteland Basilisk (lv30)")
	public static NpcStringId ID_1010344;
	
	@NpcString(id = 1010345, message = "Gargoyle Lord Sirocco (lv35)")
	public static NpcStringId ID_1010345;
	
	@NpcString(id = 1010346, message = "Sukar Wererat Chief (lv21)")
	public static NpcStringId ID_1010346;
	
	@NpcString(id = 1010347, message = "Tiger Hornet (lv26)")
	public static NpcStringId ID_1010347;
	
	@NpcString(id = 1010348, message = "Tracker Leader Sharuk (lv23)")
	public static NpcStringId ID_1010348;
	
	@NpcString(id = 1010349, message = "Patriarch Kuroboros (lv26)")
	public static NpcStringId ID_1010349;
	
	@NpcString(id = 1010350, message = "Kuroboros' Priest (lv23)")
	public static NpcStringId ID_1010350;
	
	@NpcString(id = 1010351, message = "Soul Scavenger (lv25)")
	public static NpcStringId ID_1010351;
	
	@NpcString(id = 1010352, message = "Discarded Guardian (lv20)")
	public static NpcStringId ID_1010352;
	
	@NpcString(id = 1010353, message = "Malex Herald of Dagoniel (lv21)")
	public static NpcStringId ID_1010353;
	
	@NpcString(id = 1010354, message = "Zombie Lord Farakelsus (lv20)")
	public static NpcStringId ID_1010354;
	
	@NpcString(id = 1010355, message = "Madness Beast (lv20)")
	public static NpcStringId ID_1010355;
	
	@NpcString(id = 1010356, message = "Kaysha Herald of Icarus (lv21)")
	public static NpcStringId ID_1010356;
	
	@NpcString(id = 1010357, message = "Revenant of Sir Calibus (lv34)")
	public static NpcStringId ID_1010357;
	
	@NpcString(id = 1010358, message = "Evil Spirit Tempest (lv36)")
	public static NpcStringId ID_1010358;
	
	@NpcString(id = 1010359, message = "Red Eye Captain Trakia (lv35)")
	public static NpcStringId ID_1010359;
	
	@NpcString(id = 1010360, message = "Nurka's Messenger (lv33)")
	public static NpcStringId ID_1010360;
	
	@NpcString(id = 1010361, message = "Captain of Queen's Royal Guards (lv32)")
	public static NpcStringId ID_1010361;
	
	@NpcString(id = 1010362, message = "Premo Prime (lv38)")
	public static NpcStringId ID_1010362;
	
	@NpcString(id = 1010363, message = "Archon Suscepter (lv45)")
	public static NpcStringId ID_1010363;
	
	@NpcString(id = 1010364, message = "Eye of Beleth (lv35)")
	public static NpcStringId ID_1010364;
	
	@NpcString(id = 1010365, message = "Skyla (lv32)")
	public static NpcStringId ID_1010365;
	
	@NpcString(id = 1010366, message = "Corsair Captain Kylon (lv33)")
	public static NpcStringId ID_1010366;
	
	@NpcString(id = 1010367, message = "Lord Ishka (lv60)")
	public static NpcStringId ID_1010367;
	
	@NpcString(id = 1010368, message = "Road Scavenger Leader (lv40)")
	public static NpcStringId ID_1010368;
	
	@NpcString(id = 1010369, message = "Necrosentinel Royal Guard (lv47)")
	public static NpcStringId ID_1010369;
	
	@NpcString(id = 1010370, message = "Nakondas (lv35)")
	public static NpcStringId ID_1010370;
	
	@NpcString(id = 1010371, message = "Dread Avenger Kraven (lv44)")
	public static NpcStringId ID_1010371;
	
	@NpcString(id = 1010372, message = "Orfen's Handmaiden (lv48)")
	public static NpcStringId ID_1010372;
	
	@NpcString(id = 1010373, message = "Fairy Queen Timiniel (lv56)")
	public static NpcStringId ID_1010373;
	
	@NpcString(id = 1010374, message = "Betrayer of Urutu Freki (lv25)")
	public static NpcStringId ID_1010374;
	
	@NpcString(id = 1010375, message = "Mammon Collector Talos (lv25)")
	public static NpcStringId ID_1010375;
	
	@NpcString(id = 1010376, message = "Flamestone Golem (lv44)")
	public static NpcStringId ID_1010376;
	
	@NpcString(id = 1010377, message = "Bandit Leader Barda (lv55)")
	public static NpcStringId ID_1010377;
	
	@NpcString(id = 1010378, message = "Timak Orc Gosmos (lv45)")
	public static NpcStringId ID_1010378;
	
	@NpcString(id = 1010379, message = "Thief Kelbar (lv44)")
	public static NpcStringId ID_1010379;
	
	@NpcString(id = 1010380, message = "Evil Spirit Cyrion (lv45)")
	public static NpcStringId ID_1010380;
	
	@NpcString(id = 1010381, message = "Enmity Ghost Ramdal (lv65)")
	public static NpcStringId ID_1010381;
	
	@NpcString(id = 1010382, message = "Immortal Savior Mardil (lv71)")
	public static NpcStringId ID_1010382;
	
	@NpcString(id = 1010383, message = "Cherub Galaxia (lv78)")
	public static NpcStringId ID_1010383;
	
	@NpcString(id = 1010384, message = "Meanas Anor (lv70)")
	public static NpcStringId ID_1010384;
	
	@NpcString(id = 1010385, message = "Mirror of Oblivion (lv49)")
	public static NpcStringId ID_1010385;
	
	@NpcString(id = 1010386, message = "Deadman Ereve (lv51)")
	public static NpcStringId ID_1010386;
	
	@NpcString(id = 1010387, message = "Harit Guardian Garangky (lv56)")
	public static NpcStringId ID_1010387;
	
	@NpcString(id = 1010388, message = "Gorgolos (lv64)")
	public static NpcStringId ID_1010388;
	
	@NpcString(id = 1010389, message = "Last Titan Utenus (lv66)")
	public static NpcStringId ID_1010389;
	
	@NpcString(id = 1010390, message = "Grave Robber Kim (lv52)")
	public static NpcStringId ID_1010390;
	
	@NpcString(id = 1010391, message = "Ghost Knight Kabed (lv55)")
	public static NpcStringId ID_1010391;
	
	@NpcString(id = 1010392, message = "Shilen's Priest Hisilrome (lv65)")
	public static NpcStringId ID_1010392;
	
	@NpcString(id = 1010393, message = "Magus Kenishee (lv53)")
	public static NpcStringId ID_1010393;
	
	@NpcString(id = 1010394, message = "Zaken's Chief Mate Tillion (lv50)")
	public static NpcStringId ID_1010394;
	
	@NpcString(id = 1010395, message = "Water Spirit Lian (lv40)")
	public static NpcStringId ID_1010395;
	
	@NpcString(id = 1010396, message = "Gwindorr (lv40)")
	public static NpcStringId ID_1010396;
	
	@NpcString(id = 1010397, message = "Eva's Spirit Niniel (lv55)")
	public static NpcStringId ID_1010397;
	
	@NpcString(id = 1010398, message = "Fafurion's Envoy Pingolpin (lv52)")
	public static NpcStringId ID_1010398;
	
	@NpcString(id = 1010399, message = "Fafurion's Henchman Istary (lv45)")
	public static NpcStringId ID_1010399;
	
	@NpcString(id = 1010400, message = "Croak, Croak! Food like %s in this place?!")
	public static NpcStringId ID_1010400;
	
	@NpcString(id = 1010401, message = "%s, How lucky I am!")
	public static NpcStringId ID_1010401;
	
	@NpcString(id = 1010402, message = "Pray that you caught a wrong fish %s!")
	public static NpcStringId ID_1010402;
	
	@NpcString(id = 1010403, message = "Do you know what a frog tastes like?")
	public static NpcStringId ID_1010403;
	
	@NpcString(id = 1010404, message = "I will show you the power of a frog!")
	public static NpcStringId ID_1010404;
	
	@NpcString(id = 1010405, message = "I will swallow at a mouthful!")
	public static NpcStringId ID_1010405;
	
	@NpcString(id = 1010406, message = "Ugh, no chance.  How could this elder pass away like this!")
	public static NpcStringId ID_1010406;
	
	@NpcString(id = 1010407, message = "Croak! Croak! A frog is dying!")
	public static NpcStringId ID_1010407;
	
	@NpcString(id = 1010408, message = "A frog tastes bad!  Yuck!")
	public static NpcStringId ID_1010408;
	
	@NpcString(id = 1010409, message = "Kaak! %s, What are you doing now?")
	public static NpcStringId ID_1010409;
	
	@NpcString(id = 1010410, message = "Huh, %s You pierced into the body of the Spirit with a needle. Are you ready?")
	public static NpcStringId ID_1010410;
	
	@NpcString(id = 1010411, message = "Ooh %s That's you. But, no lady is pleased with this savage invitation!")
	public static NpcStringId ID_1010411;
	
	@NpcString(id = 1010412, message = "You made me angry!")
	public static NpcStringId ID_1010412;
	
	@NpcString(id = 1010413, message = "It is but a scratch!  Is that all you can do?!")
	public static NpcStringId ID_1010413;
	
	@NpcString(id = 1010414, message = "Feel my pain!")
	public static NpcStringId ID_1010414;
	
	@NpcString(id = 1010415, message = "I'll get you for this!")
	public static NpcStringId ID_1010415;
	
	@NpcString(id = 1010416, message = "I will tell fish not to take your bait!")
	public static NpcStringId ID_1010416;
	
	@NpcString(id = 1010417, message = "You bothered such a weak spirit...Huh, Huh")
	public static NpcStringId ID_1010417;
	
	@NpcString(id = 1010418, message = "Ke, ke, ke..%s...I'm eating..Ke..")
	public static NpcStringId ID_1010418;
	
	@NpcString(id = 1010419, message = "Kuh..Ooh..%s..Enmity...Fish....")
	public static NpcStringId ID_1010419;
	
	@NpcString(id = 1010420, message = "%s...? Huh... Huh..huh...")
	public static NpcStringId ID_1010420;
	
	@NpcString(id = 1010421, message = "Ke, ke, ke, Rakul! Spin! Eh, eh, eh!")
	public static NpcStringId ID_1010421;
	
	@NpcString(id = 1010422, message = "Ah! Fafurion! Ah! Ah!")
	public static NpcStringId ID_1010422;
	
	@NpcString(id = 1010423, message = "Rakul! Rakul! Ra-kul!")
	public static NpcStringId ID_1010423;
	
	@NpcString(id = 1010424, message = "Eh..Enmity...Fish...")
	public static NpcStringId ID_1010424;
	
	@NpcString(id = 1010425, message = "I won't be eaten up...Kah, ah, ah")
	public static NpcStringId ID_1010425;
	
	@NpcString(id = 1010426, message = "Cough! Rakul! Cough, Cough! Keh...")
	public static NpcStringId ID_1010426;
	
	@NpcString(id = 1010427, message = "Glory to Fafurion! Death to %s!")
	public static NpcStringId ID_1010427;
	
	@NpcString(id = 1010428, message = "%s! You are the one who bothered my poor fish!")
	public static NpcStringId ID_1010428;
	
	@NpcString(id = 1010429, message = "Fafurion! A curse upon %s!")
	public static NpcStringId ID_1010429;
	
	@NpcString(id = 1010430, message = "Giant Special Attack!")
	public static NpcStringId ID_1010430;
	
	@NpcString(id = 1010431, message = "Know the enmity of fish!")
	public static NpcStringId ID_1010431;
	
	@NpcString(id = 1010432, message = "I will show you the power of a spear!")
	public static NpcStringId ID_1010432;
	
	@NpcString(id = 1010433, message = "Glory to Fafurion!")
	public static NpcStringId ID_1010433;
	
	@NpcString(id = 1010434, message = "Yipes!")
	public static NpcStringId ID_1010434;
	
	@NpcString(id = 1010435, message = "An old soldier does not die..! but just disappear...!")
	public static NpcStringId ID_1010435;
	
	@NpcString(id = 1010436, message = "%s, Take my challenge, the knight of water!")
	public static NpcStringId ID_1010436;
	
	@NpcString(id = 1010437, message = "Discover %s in the treasure chest of fish!")
	public static NpcStringId ID_1010437;
	
	@NpcString(id = 1010438, message = "%s, I took your bait!")
	public static NpcStringId ID_1010438;
	
	@NpcString(id = 1010439, message = "I will show you spearmanship used in Dragon King's Palace!")
	public static NpcStringId ID_1010439;
	
	@NpcString(id = 1010440, message = "This is the last gift I give you!")
	public static NpcStringId ID_1010440;
	
	@NpcString(id = 1010441, message = "Your bait was too delicious! Now, I will kill you!")
	public static NpcStringId ID_1010441;
	
	@NpcString(id = 1010442, message = "What a regret! The enmity of my brethren!")
	public static NpcStringId ID_1010442;
	
	@NpcString(id = 1010443, message = "I'll pay you back! Somebody will have my revenge!")
	public static NpcStringId ID_1010443;
	
	@NpcString(id = 1010444, message = "Cough... But, I won't be eaten up by you...!")
	public static NpcStringId ID_1010444;
	
	@NpcString(id = 1010445, message = "....? %s... I will kill you...")
	public static NpcStringId ID_1010445;
	
	@NpcString(id = 1010446, message = "%s... How could you catch me from the deep sea...")
	public static NpcStringId ID_1010446;
	
	@NpcString(id = 1010447, message = "%s... Do you think I am a fish?")
	public static NpcStringId ID_1010447;
	
	@NpcString(id = 1010448, message = "Ebibibi~")
	public static NpcStringId ID_1010448;
	
	@NpcString(id = 1010449, message = "He, he, he. Do you want me to roast you well?")
	public static NpcStringId ID_1010449;
	
	@NpcString(id = 1010450, message = "You didn't keep your eyes on me because I come from the sea?")
	public static NpcStringId ID_1010450;
	
	@NpcString(id = 1010451, message = "Eeek... I feel sick...yow...!")
	public static NpcStringId ID_1010451;
	
	@NpcString(id = 1010452, message = "I have failed...")
	public static NpcStringId ID_1010452;
	
	@NpcString(id = 1010453, message = "Activity of life... Is stopped... Chizifc...")
	public static NpcStringId ID_1010453;
	
	@NpcString(id = 1010454, message = "Growling! %s~ Growling!")
	public static NpcStringId ID_1010454;
	
	@NpcString(id = 1010455, message = "I can smell %s..!")
	public static NpcStringId ID_1010455;
	
	@NpcString(id = 1010456, message = "Looks delicious, %s!")
	public static NpcStringId ID_1010456;
	
	@NpcString(id = 1010457, message = "I will catch you!")
	public static NpcStringId ID_1010457;
	
	@NpcString(id = 1010458, message = "Uah, ah, ah, I couldn't eat anything for a long time!")
	public static NpcStringId ID_1010458;
	
	@NpcString(id = 1010459, message = "I can swallow you at a mouthful!")
	public static NpcStringId ID_1010459;
	
	@NpcString(id = 1010460, message = "What?!  I am defeated by the prey!")
	public static NpcStringId ID_1010460;
	
	@NpcString(id = 1010461, message = "You are my food! I have to kill you!")
	public static NpcStringId ID_1010461;
	
	@NpcString(id = 1010462, message = "I can't believe I am eaten up by my prey...  Gah!")
	public static NpcStringId ID_1010462;
	
	@NpcString(id = 1010463, message = "....You caught me, %s...?")
	public static NpcStringId ID_1010463;
	
	@NpcString(id = 1010464, message = "You're lucky to have even seen me, %s.")
	public static NpcStringId ID_1010464;
	
	@NpcString(id = 1010465, message = "%s, you can't leave here alive. Give up.")
	public static NpcStringId ID_1010465;
	
	@NpcString(id = 1010466, message = "I will show you the power of the deep sea!")
	public static NpcStringId ID_1010466;
	
	@NpcString(id = 1010467, message = "I will break the fishing pole!")
	public static NpcStringId ID_1010467;
	
	@NpcString(id = 1010468, message = "Your corpse will be good food for me!")
	public static NpcStringId ID_1010468;
	
	@NpcString(id = 1010469, message = "You are a good fisherman.")
	public static NpcStringId ID_1010469;
	
	@NpcString(id = 1010470, message = "Aren't you afraid of Fafurion?!")
	public static NpcStringId ID_1010470;
	
	@NpcString(id = 1010471, message = "You are excellent....")
	public static NpcStringId ID_1010471;
	
	@NpcString(id = 1010472, message = "The Poison device has been activated.")
	public static NpcStringId ID_1010472;
	
	@NpcString(id = 1010473, message = "The P. Atk. reduction device will be activated in 1 minute.")
	public static NpcStringId ID_1010473;
	
	@NpcString(id = 1010474, message = "The Defense reduction device will be activated in 2 minutes.")
	public static NpcStringId ID_1010474;
	
	@NpcString(id = 1010475, message = "The HP regeneration reduction device will be activated in 3 minutes.")
	public static NpcStringId ID_1010475;
	
	@NpcString(id = 1010476, message = "The P. Atk. reduction device has been activated.")
	public static NpcStringId ID_1010476;
	
	@NpcString(id = 1010477, message = "The Defense reduction device has been activated.")
	public static NpcStringId ID_1010477;
	
	@NpcString(id = 1010478, message = "The HP regeneration reduction device has been activated.")
	public static NpcStringId ID_1010478;
	
	@NpcString(id = 1010479, message = "The poison device has now been destroyed.")
	public static NpcStringId ID_1010479;
	
	@NpcString(id = 1010480, message = "The P. Atk. reduction device has now been destroyed.")
	public static NpcStringId ID_1010480;
	
	@NpcString(id = 1010481, message = "The Defense reduction device has been destroyed.")
	public static NpcStringId ID_1010481;
	
	@NpcString(id = 1010482, message = "The HP regeneration reduction device will be activated in 3 minutes.")
	public static NpcStringId ID_1010482;
	
	@NpcString(id = 1010483, message = "%s! Help me!!")
	public static NpcStringId ID_1010483;
	
	@NpcString(id = 1010484, message = "Help me!!")
	public static NpcStringId ID_1010484;
	
	@NpcString(id = 1010485, message = "Entrance to the Cave of Trials")
	public static NpcStringId ID_1010485;
	
	@NpcString(id = 1010486, message = "Elven ruins interior")
	public static NpcStringId ID_1010486;
	
	@NpcString(id = 1010487, message = "Elven ruins entrance")
	public static NpcStringId ID_1010487;
	
	@NpcString(id = 1010488, message = "Hardins Academy entrance")
	public static NpcStringId ID_1010488;
	
	@NpcString(id = 1010489, message = "Center of Hardyn's Academy")
	public static NpcStringId ID_1010489;
	
	@NpcString(id = 1010490, message = "Entrance to the Elven Fortress")
	public static NpcStringId ID_1010490;
	
	@NpcString(id = 1010491, message = "Varka Silenos Stronghold")
	public static NpcStringId ID_1010491;
	
	@NpcString(id = 1010492, message = "Ketra Orc Outpost")
	public static NpcStringId ID_1010492;
	
	@NpcString(id = 1010493, message = "Rune Castle Town trade union")
	public static NpcStringId ID_1010493;
	
	@NpcString(id = 1010494, message = "Rune Castle Town temple")
	public static NpcStringId ID_1010494;
	
	@NpcString(id = 1010495, message = "Rune Castle Town store")
	public static NpcStringId ID_1010495;
	
	@NpcString(id = 1010496, message = "Entrance to Forest of the Dead")
	public static NpcStringId ID_1010496;
	
	@NpcString(id = 1010497, message = "Entrance to Swamp of Screams")
	public static NpcStringId ID_1010497;
	
	@NpcString(id = 1010498, message = "Entrance to Forgotten Temple")
	public static NpcStringId ID_1010498;
	
	@NpcString(id = 1010499, message = "Center of the Forgotten Temple")
	public static NpcStringId ID_1010499;
	
	@NpcString(id = 1010500, message = "Entrance to Cruma Tower")
	public static NpcStringId ID_1010500;
	
	@NpcString(id = 1010501, message = "Cruma Tower 1st Floor")
	public static NpcStringId ID_1010501;
	
	@NpcString(id = 1010502, message = "Cruma Tower 2nd Floor")
	public static NpcStringId ID_1010502;
	
	@NpcString(id = 1010503, message = "Cruma Tower 3rd Floor")
	public static NpcStringId ID_1010503;
	
	@NpcString(id = 1010504, message = "Entrance to Devils Island")
	public static NpcStringId ID_1010504;
	
	@NpcString(id = 1010505, message = "Skyshadow Meadow")
	public static NpcStringId ID_1010505;
	
	@NpcString(id = 1010506, message = "Arena near Gludin")
	public static NpcStringId ID_1010506;
	
	@NpcString(id = 1010507, message = "Arena near Giran Castle Town")
	public static NpcStringId ID_1010507;
	
	@NpcString(id = 1010508, message = "Entrance to Lair of Antharas")
	public static NpcStringId ID_1010508;
	
	@NpcString(id = 1010509, message = "Lair of Antharas Cave 1")
	public static NpcStringId ID_1010509;
	
	@NpcString(id = 1010510, message = "Lair of Antharas Cave 2")
	public static NpcStringId ID_1010510;
	
	@NpcString(id = 1010511, message = "Lair of Antharas Cave Bridge")
	public static NpcStringId ID_1010511;
	
	@NpcString(id = 1010512, message = "Lair of Antharas Heart")
	public static NpcStringId ID_1010512;
	
	@NpcString(id = 1010513, message = "Eastern side of Field of Silence")
	public static NpcStringId ID_1010513;
	
	@NpcString(id = 1010514, message = "Western side of Field of Silence")
	public static NpcStringId ID_1010514;
	
	@NpcString(id = 1010515, message = "East side of Field of Silence")
	public static NpcStringId ID_1010515;
	
	@NpcString(id = 1010516, message = "West side of Field of Silence")
	public static NpcStringId ID_1010516;
	
	@NpcString(id = 1010517, message = "Eva's water garden entrance")
	public static NpcStringId ID_1010517;
	
	@NpcString(id = 1010518, message = "Alligator island entrance")
	public static NpcStringId ID_1010518;
	
	@NpcString(id = 1010519, message = "Alligator island sand beach")
	public static NpcStringId ID_1010519;
	
	@NpcString(id = 1010520, message = "North alligator island")
	public static NpcStringId ID_1010520;
	
	@NpcString(id = 1010521, message = "Alligator island central")
	public static NpcStringId ID_1010521;
	
	@NpcString(id = 1010522, message = "Eva's water garden 2")
	public static NpcStringId ID_1010522;
	
	@NpcString(id = 1010523, message = "Eva's water garden 3")
	public static NpcStringId ID_1010523;
	
	@NpcString(id = 1010524, message = "Eva's water garden 4")
	public static NpcStringId ID_1010524;
	
	@NpcString(id = 1010525, message = "Eva's water garden 5")
	public static NpcStringId ID_1010525;
	
	@NpcString(id = 1010526, message = "Enter to Garden of Eva")
	public static NpcStringId ID_1010526;
	
	@NpcString(id = 1010527, message = "The Four Sepulchers")
	public static NpcStringId ID_1010527;
	
	@NpcString(id = 1010528, message = "Imperial Tomb")
	public static NpcStringId ID_1010528;
	
	@NpcString(id = 1010529, message = "Shrine of the Loyal")
	public static NpcStringId ID_1010529;
	
	@NpcString(id = 1010530, message = "Entrance to Forge of the Gods")
	public static NpcStringId ID_1010530;
	
	@NpcString(id = 1010531, message = "Forge of the Gods, Upper Level")
	public static NpcStringId ID_1010531;
	
	@NpcString(id = 1010532, message = "Forge of the Gods, Lower Level")
	public static NpcStringId ID_1010532;
	
	@NpcString(id = 1010533, message = "Wall of Argos entrance")
	public static NpcStringId ID_1010533;
	
	@NpcString(id = 1010534, message = "Varka Silenos Outpost")
	public static NpcStringId ID_1010534;
	
	@NpcString(id = 1010535, message = "Center of Ketra Orc Outpost")
	public static NpcStringId ID_1010535;
	
	@NpcString(id = 1010536, message = "Hot Spring entrance")
	public static NpcStringId ID_1010536;
	
	@NpcString(id = 1010537, message = "Beast Farm")
	public static NpcStringId ID_1010537;
	
	@NpcString(id = 1010538, message = "Entrance to Saint Valley")
	public static NpcStringId ID_1010538;
	
	@NpcString(id = 1010539, message = "Cursed Village")
	public static NpcStringId ID_1010539;
	
	@NpcString(id = 1010540, message = "South Entrance to the Beast Farm")
	public static NpcStringId ID_1010540;
	
	@NpcString(id = 1010541, message = "Eastern Entrance to the Beast Farm")
	public static NpcStringId ID_1010541;
	
	@NpcString(id = 1010542, message = "Western Entrance to the Beast Farm")
	public static NpcStringId ID_1010542;
	
	@NpcString(id = 1010543, message = "Devils Pass")
	public static NpcStringId ID_1010543;
	
	@NpcString(id = 1010544, message = "Swamp of Screams")
	public static NpcStringId ID_1010544;
	
	@NpcString(id = 1010545, message = "East Swamp of Screams")
	public static NpcStringId ID_1010545;
	
	@NpcString(id = 1010546, message = "Entrance to Saint valley")
	public static NpcStringId ID_1010546;
	
	@NpcString(id = 1010547, message = "Aden Border Outpost")
	public static NpcStringId ID_1010547;
	
	@NpcString(id = 1010548, message = "Oren Border Outpost")
	public static NpcStringId ID_1010548;
	
	@NpcString(id = 1010549, message = "Garden of Beasts")
	public static NpcStringId ID_1010549;
	
	@NpcString(id = 1010550, message = "Devils Pass")
	public static NpcStringId ID_1010550;
	
	@NpcString(id = 1010551, message = "Is equipped ammunition.")
	public static NpcStringId ID_1010551;
	
	@NpcString(id = 1010552, message = "From the point to begin.")
	public static NpcStringId ID_1010552;
	
	@NpcString(id = 1010553, message = "Giant cave entrance")
	public static NpcStringId ID_1010553;
	
	@NpcString(id = 1010554, message = "Giant cave -")
	public static NpcStringId ID_1010554;
	
	@NpcString(id = 1010555, message = "Giant cave lower")
	public static NpcStringId ID_1010555;
	
	@NpcString(id = 1010556, message = "Immortal Plateau, Central Region")
	public static NpcStringId ID_1010556;
	
	@NpcString(id = 1010557, message = "Elven Ruins")
	public static NpcStringId ID_1010557;
	
	@NpcString(id = 1010558, message = "Singing Waterfall")
	public static NpcStringId ID_1010558;
	
	@NpcString(id = 1010559, message = "Western Territory of Talking Island")
	public static NpcStringId ID_1010559;
	
	@NpcString(id = 1010560, message = "Entrance to Elven Fortress")
	public static NpcStringId ID_1010560;
	
	@NpcString(id = 1010561, message = "Parade of monasteries")
	public static NpcStringId ID_1010561;
	
	@NpcString(id = 1010562, message = "Gludin villages ancient port")
	public static NpcStringId ID_1010562;
	
	@NpcString(id = 1010563, message = "Schilling's courtyard")
	public static NpcStringId ID_1010563;
	
	@NpcString(id = 1010564, message = "Inducement Law")
	public static NpcStringId ID_1010564;
	
	@NpcString(id = 1010565, message = "Lament marsh")
	public static NpcStringId ID_1010565;
	
	@NpcString(id = 1010566, message = "Ants")
	public static NpcStringId ID_1010566;
	
	@NpcString(id = 1010567, message = "Demon Island")
	public static NpcStringId ID_1010567;
	
	@NpcString(id = 1010568, message = "Wall of Argos")
	public static NpcStringId ID_1010568;
	
	@NpcString(id = 1010569, message = "Den of Evil")
	public static NpcStringId ID_1010569;
	
	@NpcString(id = 1010570, message = "Ice Merchant Cabin")
	public static NpcStringId ID_1010570;
	
	@NpcString(id = 1010571, message = "Crypts of Disgrace")
	public static NpcStringId ID_1010571;
	
	@NpcString(id = 1010572, message = "Plunderous Plains")
	public static NpcStringId ID_1010572;
	
	@NpcString(id = 1010573, message = "Pavel Ruins")
	public static NpcStringId ID_1010573;
	
	@NpcString(id = 1010574, message = "Town of Schuttgart")
	public static NpcStringId ID_1010574;
	
	@NpcString(id = 1010575, message = "Monastery of Silence")
	public static NpcStringId ID_1010575;
	
	@NpcString(id = 1010576, message = "Monastery of Silence: Rear Gate")
	public static NpcStringId ID_1010576;
	
	@NpcString(id = 1010577, message = "Stakato Nest")
	public static NpcStringId ID_1010577;
	
	@NpcString(id = 1010578, message = "How dare you trespass into my territory! Have you no fear?")
	public static NpcStringId ID_1010578;
	
	@NpcString(id = 1010579, message = "Fools! Why haven't you fled yet? Prepare to learn a lesson!")
	public static NpcStringId ID_1010579;
	
	@NpcString(id = 1010580, message = "Bwah-ha-ha! Your doom is at hand! Behold the Ultra Secret Super Weapon!")
	public static NpcStringId ID_1010580;
	
	@NpcString(id = 1010581, message = "Foolish, insignificant creatures! How dare you challenge me!")
	public static NpcStringId ID_1010581;
	
	@NpcString(id = 1010582, message = "I see that none will challenge me now!")
	public static NpcStringId ID_1010582;
	
	@NpcString(id = 1010583, message = "Urggh! You will pay dearly for this insult.")
	public static NpcStringId ID_1010583;
	
	@NpcString(id = 1010584, message = "What? You haven't even two pennies to rub together? Harumph!")
	public static NpcStringId ID_1010584;
	
	@NpcString(id = 1010585, message = "Forest of Mirrors")
	public static NpcStringId ID_1010585;
	
	@NpcString(id = 1010586, message = "The Center of the Forest of Mirrors")
	public static NpcStringId ID_1010586;
	
	@NpcString(id = 1010587, message = "Field of Silence (Western Section)")
	public static NpcStringId ID_1010587;
	
	@NpcString(id = 1010588, message = "Sky Wagon Relic")
	public static NpcStringId ID_1010588;
	
	@NpcString(id = 1010589, message = "Dark Forest")
	public static NpcStringId ID_1010589;
	
	@NpcString(id = 1010590, message = "The Center of the Dark Forest")
	public static NpcStringId ID_1010590;
	
	@NpcString(id = 1010591, message = "Grave Robber Hideout")
	public static NpcStringId ID_1010591;
	
	@NpcString(id = 1010592, message = "Forest of the Dead")
	public static NpcStringId ID_1010592;
	
	@NpcString(id = 1010593, message = "The Center of the Forest of the Dead")
	public static NpcStringId ID_1010593;
	
	@NpcString(id = 1010594, message = "Mithril Mines")
	public static NpcStringId ID_1010594;
	
	@NpcString(id = 1010595, message = "The Center of the Mithril Mines")
	public static NpcStringId ID_1010595;
	
	@NpcString(id = 1010596, message = "Abandoned Coal Mines")
	public static NpcStringId ID_1010596;
	
	@NpcString(id = 1010597, message = "The Center of the Abandoned Coal Mines")
	public static NpcStringId ID_1010597;
	
	@NpcString(id = 1010598, message = "Immortal Plateau, Western Region")
	public static NpcStringId ID_1010598;
	
	@NpcString(id = 1010599, message = "Bee Hive")
	public static NpcStringId ID_1010599;
	
	@NpcString(id = 1010600, message = "Valley of Saints")
	public static NpcStringId ID_1010600;
	
	@NpcString(id = 1010601, message = "The Center of the Valley of Saints")
	public static NpcStringId ID_1010601;
	
	@NpcString(id = 1010602, message = "Field of Whispers (Eastern Section)")
	public static NpcStringId ID_1010602;
	
	@NpcString(id = 1010603, message = "Cave of Trials")
	public static NpcStringId ID_1010603;
	
	@NpcString(id = 1010604, message = "Seal of Shilen")
	public static NpcStringId ID_1010604;
	
	@NpcString(id = 1010605, message = "The Center of the Wall of Argos")
	public static NpcStringId ID_1010605;
	
	@NpcString(id = 1010606, message = "The Center of Alligator Island")
	public static NpcStringId ID_1010606;
	
	@NpcString(id = 1010607, message = "Anghel Waterfall")
	public static NpcStringId ID_1010607;
	
	@NpcString(id = 1010608, message = "Center of the Elven Ruins")
	public static NpcStringId ID_1010608;
	
	@NpcString(id = 1010609, message = "Hot Springs")
	public static NpcStringId ID_1010609;
	
	@NpcString(id = 1010610, message = "The Center of the Hot Springs")
	public static NpcStringId ID_1010610;
	
	@NpcString(id = 1010611, message = "The Center of Dragon Valley")
	public static NpcStringId ID_1010611;
	
	@NpcString(id = 1010612, message = "Neutral Zone")
	public static NpcStringId ID_1010612;
	
	@NpcString(id = 1010613, message = "The Center of the Neutral Zone")
	public static NpcStringId ID_1010613;
	
	@NpcString(id = 1010614, message = "Cruma Marshlands")
	public static NpcStringId ID_1010614;
	
	@NpcString(id = 1010615, message = "The Center of the Cruma Marshlands")
	public static NpcStringId ID_1010615;
	
	@NpcString(id = 1010616, message = "Timak Outpost")
	public static NpcStringId ID_1010616;
	
	@NpcString(id = 1010617, message = "The Center of the Enchanted Valley")
	public static NpcStringId ID_1010617;
	
	@NpcString(id = 1010618, message = "Enchanted Valley, Southern Region")
	public static NpcStringId ID_1010618;
	
	@NpcString(id = 1010619, message = "Enchanted Valley, Northern Region")
	public static NpcStringId ID_1010619;
	
	@NpcString(id = 1010620, message = "Frost Lake")
	public static NpcStringId ID_1010620;
	
	@NpcString(id = 1010621, message = "Wastelands")
	public static NpcStringId ID_1010621;
	
	@NpcString(id = 1010622, message = "Wastelands, Western Region")
	public static NpcStringId ID_1010622;
	
	@NpcString(id = 1010623, message = "Who dares to covet the throne of our castle! Leave immediately or you will pay the price of your audacity with your very own blood!")
	public static NpcStringId ID_1010623;
	
	@NpcString(id = 1010624, message = "Hmm, those who are not of the bloodline are coming this way to take over the castle?! Humph! The bitter grudges of the dead. You must not make light of their power!")
	public static NpcStringId ID_1010624;
	
	@NpcString(id = 1010625, message = "Aargh...! If I die, then the magic force field of blood will...!")
	public static NpcStringId ID_1010625;
	
	@NpcString(id = 1010626, message = "It's not over yet... It won't be... over... like this... Never...")
	public static NpcStringId ID_1010626;
	
	@NpcString(id = 1010627, message = "Oooh! Who poured nectar on my head while I was sleeping?")
	public static NpcStringId ID_1010627;
	
	@NpcString(id = 1010628, message = "Please wait a moment.")
	public static NpcStringId ID_1010628;
	
	@NpcString(id = 1010629, message = "The word you need this time is %s.")
	public static NpcStringId ID_1010629;
	
	@NpcString(id = 1010630, message = "Intruders! Sound the alarm!")
	public static NpcStringId ID_1010630;
	
	@NpcString(id = 1010631, message = "De-activate the alarm.")
	public static NpcStringId ID_1010631;
	
	@NpcString(id = 1010632, message = "Oh no! The defenses have failed. It is too dangerous to remain inside the castle. Flee! Every man for himself!")
	public static NpcStringId ID_1010632;
	
	@NpcString(id = 1010633, message = "The game has begun. Participants, prepare to learn an important word.")
	public static NpcStringId ID_1010633;
	
	@NpcString(id = 1010634, message = "%s team's jackpot has %s percent of its HP remaining.")
	public static NpcStringId ID_1010634;
	
	@NpcString(id = 1010635, message = "Undecided")
	public static NpcStringId ID_1010635;
	
	@NpcString(id = 1010636, message = "Heh Heh... I see that the feast has begun! Be wary! The curse of the Hellmann family has poisoned this land!")
	public static NpcStringId ID_1010636;
	
	@NpcString(id = 1010637, message = "Arise, my faithful servants! You, my people who have inherited the blood.  It is the calling of my daughter.  The feast of blood will now begin!")
	public static NpcStringId ID_1010637;
	
	@NpcString(id = 1010638, message = "I'll let you have the throne of the Lord of the Forest for now...!  Get drunk with your victory...!  And while you wait with fear and trembling under the pressure of your ill-gotten position, just you wait for the return of the true Lord...!  Bwa ha ha ha ha...!")
	public static NpcStringId ID_1010638;
	
	@NpcString(id = 1010639, message = "Grarr! For the next 2 minutes or so, the game arena are will be cleaned. Throw any items you don't need to the floor now.")
	public static NpcStringId ID_1010639;
	
	@NpcString(id = 1010640, message = "Grarr! %s team is using the hot springs sulfur on the opponent's camp.")
	public static NpcStringId ID_1010640;
	
	@NpcString(id = 1010641, message = "Grarr! %s team is attempting to steal the jackpot.")
	public static NpcStringId ID_1010641;
	
	@NpcString(id = 1010642, message = "** Vacant Seat **")
	public static NpcStringId ID_1010642;
	
	@NpcString(id = 1010643, message = "%s minute(s) are remaining.")
	public static NpcStringId ID_1010643;
	
	@NpcString(id = 1010644, message = "How dare you ruin the performance of the Dark Choir... Unforgivable!")
	public static NpcStringId ID_1010644;
	
	@NpcString(id = 1010645, message = "Get rid of the invaders who interrupt the performance of the Dark Choir!")
	public static NpcStringId ID_1010645;
	
	@NpcString(id = 1010646, message = "Don't you hear the music of death? Reveal the horror of the Dark Choir!")
	public static NpcStringId ID_1010646;
	
	@NpcString(id = 1010647, message = "The Immortal Plateau")
	public static NpcStringId ID_1010647;
	
	@NpcString(id = 1010648, message = "Kamael Village")
	public static NpcStringId ID_1010648;
	
	@NpcString(id = 1010649, message = "Isle of Souls Base")
	public static NpcStringId ID_1010649;
	
	@NpcString(id = 1010650, message = "Golden Hills Base")
	public static NpcStringId ID_1010650;
	
	@NpcString(id = 1010651, message = "Mimir's Forest Base")
	public static NpcStringId ID_1010651;
	
	@NpcString(id = 1010652, message = "Isle of Souls Harbor")
	public static NpcStringId ID_1010652;
	
	@NpcString(id = 1010653, message = "Stronghold I")
	public static NpcStringId ID_1010653;
	
	@NpcString(id = 1010654, message = "Stronghold II")
	public static NpcStringId ID_1010654;
	
	@NpcString(id = 1010655, message = "Stronghold III")
	public static NpcStringId ID_1010655;
	
	@NpcString(id = 1010656, message = "Fortress West Gate")
	public static NpcStringId ID_1010656;
	
	@NpcString(id = 1010657, message = "Fortress East Gate")
	public static NpcStringId ID_1010657;
	
	@NpcString(id = 1010658, message = "Fortress North Gate")
	public static NpcStringId ID_1010658;
	
	@NpcString(id = 1010659, message = "Fortress South Gate")
	public static NpcStringId ID_1010659;
	
	@NpcString(id = 1010660, message = "Front of the Valley Fortress")
	public static NpcStringId ID_1010660;
	
	@NpcString(id = 1010661, message = "Goddard Town Square")
	public static NpcStringId ID_1010661;
	
	@NpcString(id = 1010662, message = "Front of the Goddard Castle Gate")
	public static NpcStringId ID_1010662;
	
	@NpcString(id = 1010663, message = "Gludio Town Square")
	public static NpcStringId ID_1010663;
	
	@NpcString(id = 1010664, message = "Front of the Gludio Castle Gate")
	public static NpcStringId ID_1010664;
	
	@NpcString(id = 1010665, message = "Giran Town Square")
	public static NpcStringId ID_1010665;
	
	@NpcString(id = 1010666, message = "Front of the Giran Castle Gate")
	public static NpcStringId ID_1010666;
	
	@NpcString(id = 1010667, message = "Front of the Southern Fortress")
	public static NpcStringId ID_1010667;
	
	@NpcString(id = 1010668, message = "Front of the Swamp Fortress")
	public static NpcStringId ID_1010668;
	
	@NpcString(id = 1010669, message = "Dion Town Square")
	public static NpcStringId ID_1010669;
	
	@NpcString(id = 1010670, message = "Front of the Dion Castle Gate")
	public static NpcStringId ID_1010670;
	
	@NpcString(id = 1010671, message = "Rune Town Square")
	public static NpcStringId ID_1010671;
	
	@NpcString(id = 1010672, message = "Front of the Rune Castle Gate")
	public static NpcStringId ID_1010672;
	
	@NpcString(id = 1010673, message = "Front of the White Sand Fortress")
	public static NpcStringId ID_1010673;
	
	@NpcString(id = 1010674, message = "Front of the Basin Fortress")
	public static NpcStringId ID_1010674;
	
	@NpcString(id = 1010675, message = "Front of the Ivory Fortress")
	public static NpcStringId ID_1010675;
	
	@NpcString(id = 1010676, message = "Schuttgart Town Square")
	public static NpcStringId ID_1010676;
	
	@NpcString(id = 1010677, message = "Front of the Schuttgart Castle Gate")
	public static NpcStringId ID_1010677;
	
	@NpcString(id = 1010678, message = "Aden Town Square")
	public static NpcStringId ID_1010678;
	
	@NpcString(id = 1010679, message = "Front of the Aden Castle Gate")
	public static NpcStringId ID_1010679;
	
	@NpcString(id = 1010680, message = "Front of the Shanty Fortress")
	public static NpcStringId ID_1010680;
	
	@NpcString(id = 1010681, message = "Oren Town Square")
	public static NpcStringId ID_1010681;
	
	@NpcString(id = 1010682, message = "Front of the Oren Castle Gate")
	public static NpcStringId ID_1010682;
	
	@NpcString(id = 1010683, message = "Front of the Archaic Fortress")
	public static NpcStringId ID_1010683;
	
	@NpcString(id = 1010684, message = "Front of the Innadril Castle Gate")
	public static NpcStringId ID_1010684;
	
	@NpcString(id = 1010685, message = "Front of the Border Fortress")
	public static NpcStringId ID_1010685;
	
	@NpcString(id = 1010686, message = "Heine Town Square")
	public static NpcStringId ID_1010686;
	
	@NpcString(id = 1010687, message = "Front of the Hive Fortress")
	public static NpcStringId ID_1010687;
	
	@NpcString(id = 1010688, message = "Front of the Narsell Fortress")
	public static NpcStringId ID_1010688;
	
	@NpcString(id = 1010689, message = "Front of the Gludio Castle")
	public static NpcStringId ID_1010689;
	
	@NpcString(id = 1010690, message = "Front of the Dion Castle")
	public static NpcStringId ID_1010690;
	
	@NpcString(id = 1010691, message = "Front of the Giran Castle")
	public static NpcStringId ID_1010691;
	
	@NpcString(id = 1010692, message = "Front of the Oren Castle")
	public static NpcStringId ID_1010692;
	
	@NpcString(id = 1010693, message = "Front of the Aden Castle")
	public static NpcStringId ID_1010693;
	
	@NpcString(id = 1010694, message = "Front of the Innadril Castle")
	public static NpcStringId ID_1010694;
	
	@NpcString(id = 1010695, message = "Front of the Goddard Castle")
	public static NpcStringId ID_1010695;
	
	@NpcString(id = 1010696, message = "Front of the Rune Castle")
	public static NpcStringId ID_1010696;
	
	@NpcString(id = 1010697, message = "Front of the Schuttgart Castle")
	public static NpcStringId ID_1010697;
	
	@NpcString(id = 1010698, message = "Primeval Isle Wharf")
	public static NpcStringId ID_1010698;
	
	@NpcString(id = 1010699, message = "Isle of Prayer")
	public static NpcStringId ID_1010699;
	
	@NpcString(id = 1010700, message = "Mithril Mines Western Entrance")
	public static NpcStringId ID_1010700;
	
	@NpcString(id = 1010701, message = "Mithril Mines Eastern Entrance")
	public static NpcStringId ID_1010701;
	
	@NpcString(id = 1010702, message = "The Giant's Cave Upper Layer")
	public static NpcStringId ID_1010702;
	
	@NpcString(id = 1010703, message = "The Giant's Cave Lower Layer")
	public static NpcStringId ID_1010703;
	
	@NpcString(id = 1010704, message = "Field of Silence Center")
	public static NpcStringId ID_1010704;
	
	@NpcString(id = 1010705, message = "Field of Whispers Center")
	public static NpcStringId ID_1010705;
	
	@NpcString(id = 1010706, message = "Shyeed's Cavern")
	public static NpcStringId ID_1010706;
	
	@NpcString(id = 1010707, message = "Sel Mahum Training Grounds (South Gate)")
	public static NpcStringId ID_1010707;
	
	@NpcString(id = 1010708, message = "Sel Mahum Training Grounds (Center)")
	public static NpcStringId ID_1010708;
	
	@NpcString(id = 1010709, message = "Seed of Infinity Dock")
	public static NpcStringId ID_1010709;
	
	@NpcString(id = 1010710, message = "Seed of Destruction Dock")
	public static NpcStringId ID_1010710;
	
	@NpcString(id = 1010711, message = "Seed of Annihilation Dock")
	public static NpcStringId ID_1010711;
	
	@NpcString(id = 1010712, message = "Town of Aden Einhasad Temple Priest Wood")
	public static NpcStringId ID_1010712;
	
	@NpcString(id = 1010713, message = "Hunter's Village Separated Soul Front")
	public static NpcStringId ID_1010713;
	
	@NpcString(id = 1022122, message = "Brother %s, move your weapon away!!")
	public static NpcStringId ID_1022122;
	
	@NpcString(id = 1110001, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">Can be used for item transportation.</a><br>")
	public static NpcStringId ID_1110001;
	
	@NpcString(id = 1110002, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">Can help during hunting by assisting in attacks.</a><br>")
	public static NpcStringId ID_1110002;
	
	@NpcString(id = 1110003, message = "<a action=\"bypass -h menu_select?ask=419&reply=1\">Can be sent to the village to buy items.</a><br>")
	public static NpcStringId ID_1110003;
	
	@NpcString(id = 1110004, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">Can be traded or sold to a new owner for adena.</a><br>")
	public static NpcStringId ID_1110004;
	
	@NpcString(id = 1110005, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">None of the above.</a><br>")
	public static NpcStringId ID_1110005;
	
	@NpcString(id = 1110006, message = "<a action=\"bypass -h menu_select?ask=419&reply=1\">When taking down a monster, always have a pet's company.</a><br>")
	public static NpcStringId ID_1110006;
	
	@NpcString(id = 1110007, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">Tell your pet to pick up items.</a><br>")
	public static NpcStringId ID_1110007;
	
	@NpcString(id = 1110008, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">Tell your pet to attack monsters first.</a><br>")
	public static NpcStringId ID_1110008;
	
	@NpcString(id = 1110009, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">Let your pet do what it wants.</a><br>")
	public static NpcStringId ID_1110009;
	
	@NpcString(id = 1110010, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">None of the above.</a><br>")
	public static NpcStringId ID_1110010;
	
	@NpcString(id = 1110011, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">10 minutes</a><br>")
	public static NpcStringId ID_1110011;
	
	@NpcString(id = 1110012, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">15 minutes</a><br>")
	public static NpcStringId ID_1110012;
	
	@NpcString(id = 1110013, message = "<a action=\"bypass -h menu_select?ask=419&reply=1\">20 minutes</a><br>")
	public static NpcStringId ID_1110013;
	
	@NpcString(id = 1110014, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">25 minutes</a><br>")
	public static NpcStringId ID_1110014;
	
	@NpcString(id = 1110015, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">None of the above.</a><br>")
	public static NpcStringId ID_1110015;
	
	@NpcString(id = 1110016, message = "<a action=\"bypass -h menu_select?ask=419&reply=1\">Dire Wolf</a><br>")
	public static NpcStringId ID_1110016;
	
	@NpcString(id = 1110017, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">Air Wolf</a><br>")
	public static NpcStringId ID_1110017;
	
	@NpcString(id = 1110018, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">Turek Wolf</a><br>")
	public static NpcStringId ID_1110018;
	
	@NpcString(id = 1110019, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">Kasha Wolf</a><br>")
	public static NpcStringId ID_1110019;
	
	@NpcString(id = 1110020, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">None of the above.</a><br>")
	public static NpcStringId ID_1110020;
	
	@NpcString(id = 1110021, message = "<a action=\"bypass -h menu_select?ask=419&reply=1\">It's tail is always pointing straight down.</a><br>")
	public static NpcStringId ID_1110021;
	
	@NpcString(id = 1110022, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">It's tail is always curled up.</a><br>")
	public static NpcStringId ID_1110022;
	
	@NpcString(id = 1110023, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">It's tail is always wagging back and forth.</a><br>")
	public static NpcStringId ID_1110023;
	
	@NpcString(id = 1110024, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">What are you talking about?! A wolf doesn't have a tail.</a><br>")
	public static NpcStringId ID_1110024;
	
	@NpcString(id = 1110025, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">None of the above.</a><br>")
	public static NpcStringId ID_1110025;
	
	@NpcString(id = 1110026, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">Raccoon</a><br>")
	public static NpcStringId ID_1110026;
	
	@NpcString(id = 1110027, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">Jackal</a><br>")
	public static NpcStringId ID_1110027;
	
	@NpcString(id = 1110028, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">Fox</a><br>")
	public static NpcStringId ID_1110028;
	
	@NpcString(id = 1110029, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">Shepherd Dog</a><br>")
	public static NpcStringId ID_1110029;
	
	@NpcString(id = 1110030, message = "<a action=\"bypass -h menu_select?ask=419&reply=1\">None of the above.</a><br>")
	public static NpcStringId ID_1110030;
	
	@NpcString(id = 1110031, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">1.4 km</a><br>")
	public static NpcStringId ID_1110031;
	
	@NpcString(id = 1110032, message = "<a action=\"bypass -h menu_select?ask=419&reply=1\">2.4 km</a><br>")
	public static NpcStringId ID_1110032;
	
	@NpcString(id = 1110033, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">3.4 km</a><br>")
	public static NpcStringId ID_1110033;
	
	@NpcString(id = 1110034, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">4.4 km</a><br>")
	public static NpcStringId ID_1110034;
	
	@NpcString(id = 1110035, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">None of the above.</a><br>")
	public static NpcStringId ID_1110035;
	
	@NpcString(id = 1110036, message = "<a action=\"bypass -h menu_select?ask=419&reply=1\">Male</a><br>")
	public static NpcStringId ID_1110036;
	
	@NpcString(id = 1110037, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">Female</a><br>")
	public static NpcStringId ID_1110037;
	
	@NpcString(id = 1110038, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">A baby that was born last year</a><br>")
	public static NpcStringId ID_1110038;
	
	@NpcString(id = 1110039, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">A baby that was born two years ago</a><br>")
	public static NpcStringId ID_1110039;
	
	@NpcString(id = 1110040, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">None of the above.</a><br>")
	public static NpcStringId ID_1110040;
	
	@NpcString(id = 1110041, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">Goat</a><br>")
	public static NpcStringId ID_1110041;
	
	@NpcString(id = 1110042, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">Meat of a dead animal</a><br>")
	public static NpcStringId ID_1110042;
	
	@NpcString(id = 1110043, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">Berries</a><br>")
	public static NpcStringId ID_1110043;
	
	@NpcString(id = 1110044, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">Wild Bird</a><br>")
	public static NpcStringId ID_1110044;
	
	@NpcString(id = 1110045, message = "<a action=\"bypass -h menu_select?ask=419&reply=1\">None of the above.</a><br>")
	public static NpcStringId ID_1110045;
	
	@NpcString(id = 1110046, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">Breeding season is January-February.</a><br>")
	public static NpcStringId ID_1110046;
	
	@NpcString(id = 1110047, message = "<a action=\"bypass -h menu_select?ask=419&reply=1\">Pregnancy is nine months.</a><br>")
	public static NpcStringId ID_1110047;
	
	@NpcString(id = 1110048, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">Babies are born in April-June.</a><br>")
	public static NpcStringId ID_1110048;
	
	@NpcString(id = 1110049, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">Has up to ten offspring at one time.</a><br>")
	public static NpcStringId ID_1110049;
	
	@NpcString(id = 1110050, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">None of the above.</a><br>")
	public static NpcStringId ID_1110050;
	
	@NpcString(id = 1110051, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">3-6 years</a><br>")
	public static NpcStringId ID_1110051;
	
	@NpcString(id = 1110052, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">6-9 years</a><br>")
	public static NpcStringId ID_1110052;
	
	@NpcString(id = 1110053, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">9-12 years</a><br>")
	public static NpcStringId ID_1110053;
	
	@NpcString(id = 1110054, message = "<a action=\"bypass -h menu_select?ask=419&reply=1\">12-15 years</a><br>")
	public static NpcStringId ID_1110054;
	
	@NpcString(id = 1110055, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">None of the above.</a><br>")
	public static NpcStringId ID_1110055;
	
	@NpcString(id = 1110056, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">Wolves gather and move in groups of 7-13 animals.</a><br>")
	public static NpcStringId ID_1110056;
	
	@NpcString(id = 1110057, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">Wolves can eat a whole calf in one sitting.</a><br>")
	public static NpcStringId ID_1110057;
	
	@NpcString(id = 1110058, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">If they have water, wolves can live for 5-6 days without eating anything.</a><br>")
	public static NpcStringId ID_1110058;
	
	@NpcString(id = 1110059, message = "<a action=\"bypass -h menu_select?ask=419&reply=1\">A pregnant wolf makes its home in a wide open place to have its babies.</a><br>")
	public static NpcStringId ID_1110059;
	
	@NpcString(id = 1110060, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">None of the above.</a><br>")
	public static NpcStringId ID_1110060;
	
	@NpcString(id = 1110061, message = "<a action=\"bypass -h menu_select?ask=419&reply=1\">A grown wolf is still not as heavy as a fully-grown male adult human.</a><br>")
	public static NpcStringId ID_1110061;
	
	@NpcString(id = 1110062, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">A wolf changes into a werewolf during a full-moon.</a><br>")
	public static NpcStringId ID_1110062;
	
	@NpcString(id = 1110063, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">The color of a wolf's fur is the same as the place where it lives.</a><br>")
	public static NpcStringId ID_1110063;
	
	@NpcString(id = 1110064, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">A wolf enjoys eating Dwarves.</a><br>")
	public static NpcStringId ID_1110064;
	
	@NpcString(id = 1110065, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">None of the above.</a><br>")
	public static NpcStringId ID_1110065;
	
	@NpcString(id = 1110066, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">Talking Island - Wolf</a><br>")
	public static NpcStringId ID_1110066;
	
	@NpcString(id = 1110067, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">Dark Forest - Ashen Wolf</a><br>")
	public static NpcStringId ID_1110067;
	
	@NpcString(id = 1110068, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">Elven Forest - Gray Wolf</a><br>")
	public static NpcStringId ID_1110068;
	
	@NpcString(id = 1110069, message = "<a action=\"bypass -h menu_select?ask=419&reply=1\">Orc - Black Wolf</a><br>")
	public static NpcStringId ID_1110069;
	
	@NpcString(id = 1110070, message = "<a action=\"bypass -h menu_select?ask=419&reply=0\">None of the above.</a><br>")
	public static NpcStringId ID_1110070;
	
	@NpcString(id = 1110071, message = "... is the process of standing up.")
	public static NpcStringId ID_1110071;
	
	@NpcString(id = 1110072, message = "... is the process of sitting down.")
	public static NpcStringId ID_1110072;
	
	@NpcString(id = 1110073, message = "It is possible to use a skill while sitting down.")
	public static NpcStringId ID_1110073;
	
	@NpcString(id = 1110074, message = "...is out of range.")
	public static NpcStringId ID_1110074;
	
	@NpcString(id = 1110075, message = "<a action=\"bypass -h menu_select?ask=255&reply=3\" msg=\"811;Monster Derby\">Teleport to Monster Derby (Free)</a><br>")
	public static NpcStringId ID_1110075;
	
	@NpcString(id = 1111002, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Gladiator</a><br>")
	public static NpcStringId ID_1111002;
	
	@NpcString(id = 1111003, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Warlord</a><br>")
	public static NpcStringId ID_1111003;
	
	@NpcString(id = 1111005, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Paladin</a><br>")
	public static NpcStringId ID_1111005;
	
	@NpcString(id = 1111006, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Dark Avenger</a><br>")
	public static NpcStringId ID_1111006;
	
	@NpcString(id = 1111008, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Treasure Hunter</a><br>")
	public static NpcStringId ID_1111008;
	
	@NpcString(id = 1111009, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Hawkeye</a><br>")
	public static NpcStringId ID_1111009;
	
	@NpcString(id = 1111012, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Sorcerer</a><br>")
	public static NpcStringId ID_1111012;
	
	@NpcString(id = 1111013, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Necromancer</a><br>")
	public static NpcStringId ID_1111013;
	
	@NpcString(id = 1111014, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Warlock</a><br>")
	public static NpcStringId ID_1111014;
	
	@NpcString(id = 1111016, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Bishop</a><br>")
	public static NpcStringId ID_1111016;
	
	@NpcString(id = 1111017, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Prophet</a><br>")
	public static NpcStringId ID_1111017;
	
	@NpcString(id = 1111020, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Temple Knight</a><br>")
	public static NpcStringId ID_1111020;
	
	@NpcString(id = 1111021, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Sword Singer</a><br>")
	public static NpcStringId ID_1111021;
	
	@NpcString(id = 1111023, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Plains Walker</a><br>")
	public static NpcStringId ID_1111023;
	
	@NpcString(id = 1111024, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Silver Ranger</a><br>")
	public static NpcStringId ID_1111024;
	
	@NpcString(id = 1111027, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Spellsinger</a><br>")
	public static NpcStringId ID_1111027;
	
	@NpcString(id = 1111028, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Elemental Summoner</a><br>")
	public static NpcStringId ID_1111028;
	
	@NpcString(id = 1111030, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Elven Elder</a><br>")
	public static NpcStringId ID_1111030;
	
	@NpcString(id = 1111033, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Shillien Knight</a><br>")
	public static NpcStringId ID_1111033;
	
	@NpcString(id = 1111034, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Bladedancer</a><br>")
	public static NpcStringId ID_1111034;
	
	@NpcString(id = 1111036, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Abyss Walker</a><br>")
	public static NpcStringId ID_1111036;
	
	@NpcString(id = 1111037, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Phantom Ranger</a><br>")
	public static NpcStringId ID_1111037;
	
	@NpcString(id = 1111040, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Spellhowler</a><br>")
	public static NpcStringId ID_1111040;
	
	@NpcString(id = 1111041, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Phantom Summoner</a><br>")
	public static NpcStringId ID_1111041;
	
	@NpcString(id = 1111043, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Shillien Elder</a><br>")
	public static NpcStringId ID_1111043;
	
	@NpcString(id = 1111046, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Destroyer</a><br>")
	public static NpcStringId ID_1111046;
	
	@NpcString(id = 1111048, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Tyrant</a><br>")
	public static NpcStringId ID_1111048;
	
	@NpcString(id = 1111051, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Overlord</a><br>")
	public static NpcStringId ID_1111051;
	
	@NpcString(id = 1111052, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Warcryer</a><br>")
	public static NpcStringId ID_1111052;
	
	@NpcString(id = 1111055, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Bounty Hunter</a><br>")
	public static NpcStringId ID_1111055;
	
	@NpcString(id = 1111057, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Warsmith</a><br>")
	public static NpcStringId ID_1111057;
	
	@NpcString(id = 1111058, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Duelist</a><br>")
	public static NpcStringId ID_1111058;
	
	@NpcString(id = 1111059, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Dreadnought</a><br>")
	public static NpcStringId ID_1111059;
	
	@NpcString(id = 1111060, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Phoenix Knight</a><br>")
	public static NpcStringId ID_1111060;
	
	@NpcString(id = 1111061, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Hell Knight</a><br>")
	public static NpcStringId ID_1111061;
	
	@NpcString(id = 1111062, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Sagittarius</a><br>")
	public static NpcStringId ID_1111062;
	
	@NpcString(id = 1111063, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Adventurer</a><br>")
	public static NpcStringId ID_1111063;
	
	@NpcString(id = 1111064, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Archmage</a><br>")
	public static NpcStringId ID_1111064;
	
	@NpcString(id = 1111065, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Soultaker</a><br>")
	public static NpcStringId ID_1111065;
	
	@NpcString(id = 1111066, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Arcana Lord</a><br>")
	public static NpcStringId ID_1111066;
	
	@NpcString(id = 1111067, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Cardinal</a><br>")
	public static NpcStringId ID_1111067;
	
	@NpcString(id = 1111068, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Hierophant</a><br>")
	public static NpcStringId ID_1111068;
	
	@NpcString(id = 1111069, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Eva's Templar</a><br>")
	public static NpcStringId ID_1111069;
	
	@NpcString(id = 1111070, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Sword Muse</a><br>")
	public static NpcStringId ID_1111070;
	
	@NpcString(id = 1111071, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Wind Rider</a><br>")
	public static NpcStringId ID_1111071;
	
	@NpcString(id = 1111072, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Moonlight Sentinel</a><br>")
	public static NpcStringId ID_1111072;
	
	@NpcString(id = 1111073, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Mystic Muse</a><br>")
	public static NpcStringId ID_1111073;
	
	@NpcString(id = 1111074, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Elemental Master</a><br>")
	public static NpcStringId ID_1111074;
	
	@NpcString(id = 1111075, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Eva's Saint</a><br>")
	public static NpcStringId ID_1111075;
	
	@NpcString(id = 1111076, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Shillien Templar</a><br>")
	public static NpcStringId ID_1111076;
	
	@NpcString(id = 1111077, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Spectral Dancer</a><br>")
	public static NpcStringId ID_1111077;
	
	@NpcString(id = 1111078, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Ghost Hunter</a><br>")
	public static NpcStringId ID_1111078;
	
	@NpcString(id = 1111079, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Ghost Sentinel</a><br>")
	public static NpcStringId ID_1111079;
	
	@NpcString(id = 1111080, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Storm Screamer</a><br>")
	public static NpcStringId ID_1111080;
	
	@NpcString(id = 1111081, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Spectral Master</a><br>")
	public static NpcStringId ID_1111081;
	
	@NpcString(id = 1111082, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Shillien Saint</a><br>")
	public static NpcStringId ID_1111082;
	
	@NpcString(id = 1111083, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Titan</a><br>")
	public static NpcStringId ID_1111083;
	
	@NpcString(id = 1111084, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Grand Khavatari</a><br>")
	public static NpcStringId ID_1111084;
	
	@NpcString(id = 1111085, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Dominator</a><br>")
	public static NpcStringId ID_1111085;
	
	@NpcString(id = 1111086, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Doom Cryer</a><br>")
	public static NpcStringId ID_1111086;
	
	@NpcString(id = 1111087, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Fortune Seeker</a><br>")
	public static NpcStringId ID_1111087;
	
	@NpcString(id = 1111088, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=1\">Maestro</a><br>")
	public static NpcStringId ID_1111088;
	
	@NpcString(id = 1112002, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Gladiator</a><br>")
	public static NpcStringId ID_1112002;
	
	@NpcString(id = 1112003, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Warlord</a><br>")
	public static NpcStringId ID_1112003;
	
	@NpcString(id = 1112005, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Paladin</a><br>")
	public static NpcStringId ID_1112005;
	
	@NpcString(id = 1112006, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Dark Avenger</a><br>")
	public static NpcStringId ID_1112006;
	
	@NpcString(id = 1112008, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Treasure Hunter</a><br>")
	public static NpcStringId ID_1112008;
	
	@NpcString(id = 1112009, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Hawkeye</a><br>")
	public static NpcStringId ID_1112009;
	
	@NpcString(id = 1112012, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Sorcerer</a><br>")
	public static NpcStringId ID_1112012;
	
	@NpcString(id = 1112013, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Necromancer</a><br>")
	public static NpcStringId ID_1112013;
	
	@NpcString(id = 1112014, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Warlock</a><br>")
	public static NpcStringId ID_1112014;
	
	@NpcString(id = 1112016, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Bishop</a><br>")
	public static NpcStringId ID_1112016;
	
	@NpcString(id = 1112017, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Prophet</a><br>")
	public static NpcStringId ID_1112017;
	
	@NpcString(id = 1112020, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Temple Knight</a><br>")
	public static NpcStringId ID_1112020;
	
	@NpcString(id = 1112021, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Sword Singer</a><br>")
	public static NpcStringId ID_1112021;
	
	@NpcString(id = 1112023, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Plains Walker</a><br>")
	public static NpcStringId ID_1112023;
	
	@NpcString(id = 1112024, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Silver Ranger</a><br>")
	public static NpcStringId ID_1112024;
	
	@NpcString(id = 1112027, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Spellsinger</a><br>")
	public static NpcStringId ID_1112027;
	
	@NpcString(id = 1112028, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Elemental Summoner</a><br>")
	public static NpcStringId ID_1112028;
	
	@NpcString(id = 1112030, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Elven Elder</a><br>")
	public static NpcStringId ID_1112030;
	
	@NpcString(id = 1112033, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Shillien Knight</a><br>")
	public static NpcStringId ID_1112033;
	
	@NpcString(id = 1112034, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Bladedancer</a><br>")
	public static NpcStringId ID_1112034;
	
	@NpcString(id = 1112036, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Abyss Walker</a><br>")
	public static NpcStringId ID_1112036;
	
	@NpcString(id = 1112037, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Phantom Ranger</a><br>")
	public static NpcStringId ID_1112037;
	
	@NpcString(id = 1112040, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Spellhowler</a><br>")
	public static NpcStringId ID_1112040;
	
	@NpcString(id = 1112041, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Phantom Summoner</a><br>")
	public static NpcStringId ID_1112041;
	
	@NpcString(id = 1112043, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Shillien Elder</a><br>")
	public static NpcStringId ID_1112043;
	
	@NpcString(id = 1112046, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Destroyer</a><br>")
	public static NpcStringId ID_1112046;
	
	@NpcString(id = 1112048, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Tyrant</a><br>")
	public static NpcStringId ID_1112048;
	
	@NpcString(id = 1112051, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Overlord</a><br>")
	public static NpcStringId ID_1112051;
	
	@NpcString(id = 1112052, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Warcryer</a><br>")
	public static NpcStringId ID_1112052;
	
	@NpcString(id = 1112055, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Bounty Hunter</a><br>")
	public static NpcStringId ID_1112055;
	
	@NpcString(id = 1112057, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Warsmith</a><br>")
	public static NpcStringId ID_1112057;
	
	@NpcString(id = 1112058, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Duelist</a><br>")
	public static NpcStringId ID_1112058;
	
	@NpcString(id = 1112059, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Dreadnought</a><br>")
	public static NpcStringId ID_1112059;
	
	@NpcString(id = 1112060, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Phoenix Knight</a><br>")
	public static NpcStringId ID_1112060;
	
	@NpcString(id = 1112061, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Hell Knight</a><br>")
	public static NpcStringId ID_1112061;
	
	@NpcString(id = 1112062, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Sagittarius</a><br>")
	public static NpcStringId ID_1112062;
	
	@NpcString(id = 1112063, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Adventurer</a><br>")
	public static NpcStringId ID_1112063;
	
	@NpcString(id = 1112064, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Archmage</a><br>")
	public static NpcStringId ID_1112064;
	
	@NpcString(id = 1112065, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Soultaker</a><br>")
	public static NpcStringId ID_1112065;
	
	@NpcString(id = 1112066, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Arcana Lord</a><br>")
	public static NpcStringId ID_1112066;
	
	@NpcString(id = 1112067, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Cardinal</a><br>")
	public static NpcStringId ID_1112067;
	
	@NpcString(id = 1112068, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Hierophant</a><br>")
	public static NpcStringId ID_1112068;
	
	@NpcString(id = 1112069, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Eva's Templar</a><br>")
	public static NpcStringId ID_1112069;
	
	@NpcString(id = 1112070, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Sword Muse</a><br>")
	public static NpcStringId ID_1112070;
	
	@NpcString(id = 1112071, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Wind Rider</a><br>")
	public static NpcStringId ID_1112071;
	
	@NpcString(id = 1112072, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Moonlight Sentinel</a><br>")
	public static NpcStringId ID_1112072;
	
	@NpcString(id = 1112073, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Mystic Muse</a><br>")
	public static NpcStringId ID_1112073;
	
	@NpcString(id = 1112074, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Elemental Master</a><br>")
	public static NpcStringId ID_1112074;
	
	@NpcString(id = 1112075, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Eva's Saint</a><br>")
	public static NpcStringId ID_1112075;
	
	@NpcString(id = 1112076, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Shillien Templar</a><br>")
	public static NpcStringId ID_1112076;
	
	@NpcString(id = 1112077, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Spectral Dancer</a><br>")
	public static NpcStringId ID_1112077;
	
	@NpcString(id = 1112078, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Ghost Hunter</a><br>")
	public static NpcStringId ID_1112078;
	
	@NpcString(id = 1112079, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Ghost Sentinel</a><br>")
	public static NpcStringId ID_1112079;
	
	@NpcString(id = 1112080, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Storm Screamer</a><br>")
	public static NpcStringId ID_1112080;
	
	@NpcString(id = 1112081, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Spectral Master</a><br>")
	public static NpcStringId ID_1112081;
	
	@NpcString(id = 1112082, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Shillien Saint</a><br>")
	public static NpcStringId ID_1112082;
	
	@NpcString(id = 1112083, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Titan</a><br>")
	public static NpcStringId ID_1112083;
	
	@NpcString(id = 1112084, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Grand Khavatari</a><br>")
	public static NpcStringId ID_1112084;
	
	@NpcString(id = 1112085, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Dominator</a><br>")
	public static NpcStringId ID_1112085;
	
	@NpcString(id = 1112086, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Doom Cryer</a><br>")
	public static NpcStringId ID_1112086;
	
	@NpcString(id = 1112087, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Fortune Seeker</a><br>")
	public static NpcStringId ID_1112087;
	
	@NpcString(id = 1112088, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=2\">Maestro</a><br>")
	public static NpcStringId ID_1112088;
	
	@NpcString(id = 1113002, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Gladiator</a><br>")
	public static NpcStringId ID_1113002;
	
	@NpcString(id = 1113003, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Warlord</a><br>")
	public static NpcStringId ID_1113003;
	
	@NpcString(id = 1113005, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Paladin</a><br>")
	public static NpcStringId ID_1113005;
	
	@NpcString(id = 1113006, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Dark Avenger</a><br>")
	public static NpcStringId ID_1113006;
	
	@NpcString(id = 1113008, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Treasure Hunter</a><br>")
	public static NpcStringId ID_1113008;
	
	@NpcString(id = 1113009, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Hawkeye</a><br>")
	public static NpcStringId ID_1113009;
	
	@NpcString(id = 1113012, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Sorcerer</a><br>")
	public static NpcStringId ID_1113012;
	
	@NpcString(id = 1113013, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Necromancer</a><br>")
	public static NpcStringId ID_1113013;
	
	@NpcString(id = 1113014, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Warlock</a><br>")
	public static NpcStringId ID_1113014;
	
	@NpcString(id = 1113016, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Bishop</a><br>")
	public static NpcStringId ID_1113016;
	
	@NpcString(id = 1113017, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Prophet</a><br>")
	public static NpcStringId ID_1113017;
	
	@NpcString(id = 1113020, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Temple Knight</a><br>")
	public static NpcStringId ID_1113020;
	
	@NpcString(id = 1113021, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Sword Singer</a><br>")
	public static NpcStringId ID_1113021;
	
	@NpcString(id = 1113023, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Plains Walker</a><br>")
	public static NpcStringId ID_1113023;
	
	@NpcString(id = 1113024, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Silver Ranger</a><br>")
	public static NpcStringId ID_1113024;
	
	@NpcString(id = 1113027, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Spellsinger</a><br>")
	public static NpcStringId ID_1113027;
	
	@NpcString(id = 1113028, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Elemental Summoner</a><br>")
	public static NpcStringId ID_1113028;
	
	@NpcString(id = 1113030, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Elven Elder</a><br>")
	public static NpcStringId ID_1113030;
	
	@NpcString(id = 1113033, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Shillien Knight</a><br>")
	public static NpcStringId ID_1113033;
	
	@NpcString(id = 1113034, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Bladedancer</a><br>")
	public static NpcStringId ID_1113034;
	
	@NpcString(id = 1113036, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Abyss Walker</a><br>")
	public static NpcStringId ID_1113036;
	
	@NpcString(id = 1113037, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Phantom Ranger</a><br>")
	public static NpcStringId ID_1113037;
	
	@NpcString(id = 1113040, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Spellhowler</a><br>")
	public static NpcStringId ID_1113040;
	
	@NpcString(id = 1113041, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Phantom Summoner</a><br>")
	public static NpcStringId ID_1113041;
	
	@NpcString(id = 1113043, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Shillien Elder</a><br>")
	public static NpcStringId ID_1113043;
	
	@NpcString(id = 1113046, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Destroyer</a><br>")
	public static NpcStringId ID_1113046;
	
	@NpcString(id = 1113048, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Tyrant</a><br>")
	public static NpcStringId ID_1113048;
	
	@NpcString(id = 1113051, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Overlord</a><br>")
	public static NpcStringId ID_1113051;
	
	@NpcString(id = 1113052, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Warcryer</a><br>")
	public static NpcStringId ID_1113052;
	
	@NpcString(id = 1113055, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Bounty Hunter</a><br>")
	public static NpcStringId ID_1113055;
	
	@NpcString(id = 1113057, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Warsmith</a><br>")
	public static NpcStringId ID_1113057;
	
	@NpcString(id = 1113058, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Duelist</a><br>")
	public static NpcStringId ID_1113058;
	
	@NpcString(id = 1113059, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Dreadnought</a><br>")
	public static NpcStringId ID_1113059;
	
	@NpcString(id = 1113060, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Phoenix Knight</a><br>")
	public static NpcStringId ID_1113060;
	
	@NpcString(id = 1113061, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Hell Knight</a><br>")
	public static NpcStringId ID_1113061;
	
	@NpcString(id = 1113062, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Sagittarius</a><br>")
	public static NpcStringId ID_1113062;
	
	@NpcString(id = 1113063, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Adventurer</a><br>")
	public static NpcStringId ID_1113063;
	
	@NpcString(id = 1113064, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Archmage</a><br>")
	public static NpcStringId ID_1113064;
	
	@NpcString(id = 1113065, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Soultaker</a><br>")
	public static NpcStringId ID_1113065;
	
	@NpcString(id = 1113066, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Arcana Lord</a><br>")
	public static NpcStringId ID_1113066;
	
	@NpcString(id = 1113067, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Cardinal</a><br>")
	public static NpcStringId ID_1113067;
	
	@NpcString(id = 1113068, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Hierophant</a><br>")
	public static NpcStringId ID_1113068;
	
	@NpcString(id = 1113069, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Eva's Templar</a><br>")
	public static NpcStringId ID_1113069;
	
	@NpcString(id = 1113070, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Sword Muse</a><br>")
	public static NpcStringId ID_1113070;
	
	@NpcString(id = 1113071, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Wind Rider</a><br>")
	public static NpcStringId ID_1113071;
	
	@NpcString(id = 1113072, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Moonlight Sentinel</a><br>")
	public static NpcStringId ID_1113072;
	
	@NpcString(id = 1113073, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Mystic Muse</a><br>")
	public static NpcStringId ID_1113073;
	
	@NpcString(id = 1113074, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Elemental Master</a><br>")
	public static NpcStringId ID_1113074;
	
	@NpcString(id = 1113075, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Eva's Saint</a><br>")
	public static NpcStringId ID_1113075;
	
	@NpcString(id = 1113076, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Shillien Templar</a><br>")
	public static NpcStringId ID_1113076;
	
	@NpcString(id = 1113077, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Spectral Dancer</a><br>")
	public static NpcStringId ID_1113077;
	
	@NpcString(id = 1113078, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Ghost Hunter</a><br>")
	public static NpcStringId ID_1113078;
	
	@NpcString(id = 1113079, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Ghost Sentinel</a><br>")
	public static NpcStringId ID_1113079;
	
	@NpcString(id = 1113080, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Storm Screamer</a><br>")
	public static NpcStringId ID_1113080;
	
	@NpcString(id = 1113081, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Spectral Master</a><br>")
	public static NpcStringId ID_1113081;
	
	@NpcString(id = 1113082, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Shillien Saint</a><br>")
	public static NpcStringId ID_1113082;
	
	@NpcString(id = 1113083, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Titan</a><br>")
	public static NpcStringId ID_1113083;
	
	@NpcString(id = 1113084, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Grand Khavatari</a><br>")
	public static NpcStringId ID_1113084;
	
	@NpcString(id = 1113085, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Dominator</a><br>")
	public static NpcStringId ID_1113085;
	
	@NpcString(id = 1113086, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Doom Cryer</a><br>")
	public static NpcStringId ID_1113086;
	
	@NpcString(id = 1113087, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Fortune Seeker</a><br>")
	public static NpcStringId ID_1113087;
	
	@NpcString(id = 1113088, message = "<a action=\"bypass -h menu_select?ask=-2&reply=41&state=3\">Maestro</a><br>")
	public static NpcStringId ID_1113088;
	
	@NpcString(id = 1114088, message = "<a action=\"bypass -h menu_select?ask=-2&reply=380\">Duelist</a><br>")
	public static NpcStringId ID_1114088;
	
	@NpcString(id = 1114089, message = "<a action=\"bypass -h menu_select?ask=-2&reply=381\">Dreadnought</a><br>")
	public static NpcStringId ID_1114089;
	
	@NpcString(id = 1114090, message = "<a action=\"bypass -h menu_select?ask=-2&reply=382\">Phoenix Knight</a><br>")
	public static NpcStringId ID_1114090;
	
	@NpcString(id = 1114091, message = "<a action=\"bypass -h menu_select?ask=-2&reply=383\">Hell Knight</a><br>")
	public static NpcStringId ID_1114091;
	
	@NpcString(id = 1114092, message = "<a action=\"bypass -h menu_select?ask=-2&reply=384\">Sagittarius</a><br>")
	public static NpcStringId ID_1114092;
	
	@NpcString(id = 1114093, message = "<a action=\"bypass -h menu_select?ask=-2&reply=385\">Adventurer</a><br>")
	public static NpcStringId ID_1114093;
	
	@NpcString(id = 1114094, message = "<a action=\"bypass -h menu_select?ask=-2&reply=386\">Archmage</a><br>")
	public static NpcStringId ID_1114094;
	
	@NpcString(id = 1114095, message = "<a action=\"bypass -h menu_select?ask=-2&reply=387\">Soultaker</a><br>")
	public static NpcStringId ID_1114095;
	
	@NpcString(id = 1114096, message = "<a action=\"bypass -h menu_select?ask=-2&reply=388\">Arcana Lord</a><br>")
	public static NpcStringId ID_1114096;
	
	@NpcString(id = 1114097, message = "<a action=\"bypass -h menu_select?ask=-2&reply=389\">Cardinal</a><br>")
	public static NpcStringId ID_1114097;
	
	@NpcString(id = 1114098, message = "<a action=\"bypass -h menu_select?ask=-2&reply=390\">Hierophant</a><br>")
	public static NpcStringId ID_1114098;
	
	@NpcString(id = 1114099, message = "<a action=\"bypass -h menu_select?ask=-2&reply=391\">Eva's Templar</a><br>")
	public static NpcStringId ID_1114099;
	
	@NpcString(id = 1114100, message = "<a action=\"bypass -h menu_select?ask=-2&reply=392\">Sword Muse</a><br>")
	public static NpcStringId ID_1114100;
	
	@NpcString(id = 1114101, message = "<a action=\"bypass -h menu_select?ask=-2&reply=393\">Wind Rider</a><br>")
	public static NpcStringId ID_1114101;
	
	@NpcString(id = 1114102, message = "<a action=\"bypass -h menu_select?ask=-2&reply=394\">Moonlight Sentinel</a><br>")
	public static NpcStringId ID_1114102;
	
	@NpcString(id = 1114103, message = "<a action=\"bypass -h menu_select?ask=-2&reply=395\">Mystic Muse</a><br>")
	public static NpcStringId ID_1114103;
	
	@NpcString(id = 1114104, message = "<a action=\"bypass -h menu_select?ask=-2&reply=396\">Elemental Master</a><br>")
	public static NpcStringId ID_1114104;
	
	@NpcString(id = 1114105, message = "<a action=\"bypass -h menu_select?ask=-2&reply=397\">Eva's Saint</a><br>")
	public static NpcStringId ID_1114105;
	
	@NpcString(id = 1114106, message = "<a action=\"bypass -h menu_select?ask=-2&reply=398\">Shillien Templar</a><br>")
	public static NpcStringId ID_1114106;
	
	@NpcString(id = 1114107, message = "<a action=\"bypass -h menu_select?ask=-2&reply=399\">Spectral Dancer</a><br>")
	public static NpcStringId ID_1114107;
	
	@NpcString(id = 1114108, message = "<a action=\"bypass -h menu_select?ask=-2&reply=400\">Ghost Hunter</a><br>")
	public static NpcStringId ID_1114108;
	
	@NpcString(id = 1114109, message = "<a action=\"bypass -h menu_select?ask=-2&reply=401\">Ghost Sentinel</a><br>")
	public static NpcStringId ID_1114109;
	
	@NpcString(id = 1114110, message = "<a action=\"bypass -h menu_select?ask=-2&reply=402\">Storm Screamer</a><br>")
	public static NpcStringId ID_1114110;
	
	@NpcString(id = 1114111, message = "<a action=\"bypass -h menu_select?ask=-2&reply=403\">Spectral Master</a><br>")
	public static NpcStringId ID_1114111;
	
	@NpcString(id = 1114112, message = "<a action=\"bypass -h menu_select?ask=-2&reply=404\">Shillien Saint</a><br>")
	public static NpcStringId ID_1114112;
	
	@NpcString(id = 1114113, message = "<a action=\"bypass -h menu_select?ask=-2&reply=405\">Titan</a><br>")
	public static NpcStringId ID_1114113;
	
	@NpcString(id = 1114114, message = "<a action=\"bypass -h menu_select?ask=-2&reply=406\">Grand Khavatari</a><br>")
	public static NpcStringId ID_1114114;
	
	@NpcString(id = 1114115, message = "<a action=\"bypass -h menu_select?ask=-2&reply=407\">Dominator</a><br>")
	public static NpcStringId ID_1114115;
	
	@NpcString(id = 1114116, message = "<a action=\"bypass -h menu_select?ask=-2&reply=408\">Doomcryer</a><br>")
	public static NpcStringId ID_1114116;
	
	@NpcString(id = 1114117, message = "<a action=\"bypass -h menu_select?ask=-2&reply=409\">Fortune Seeker</a><br>")
	public static NpcStringId ID_1114117;
	
	@NpcString(id = 1114118, message = "<a action=\"bypass -h menu_select?ask=-2&reply=410\">Maestro</a><br>")
	public static NpcStringId ID_1114118;
	
	@NpcString(id = 1121000, message = "Steward: Please wait a moment.")
	public static NpcStringId ID_1121000;
	
	@NpcString(id = 1121001, message = "Steward: Please restore the Queen's former appearance!")
	public static NpcStringId ID_1121001;
	
	@NpcString(id = 1121002, message = "Steward: Waste no time!  Please hurry!")
	public static NpcStringId ID_1121002;
	
	@NpcString(id = 1121003, message = "Steward: Was it indeed too much to ask...")
	public static NpcStringId ID_1121003;
	
	@NpcString(id = 1121004, message = "Freya: Heathens! Feel my chill!")
	public static NpcStringId ID_1121004;
	
	@NpcString(id = 1121005, message = "Attention please! The gates will be closing shortly. All visitors to the Queen's Castle should leave immediately.")
	public static NpcStringId ID_1121005;
	
	@NpcString(id = 1121006, message = "You cannot carry a weapon without authorization!")
	public static NpcStringId ID_1121006;
	
	@NpcString(id = 1121007, message = "Are you trying to deceive me?  I'm disappointed.")
	public static NpcStringId ID_1121007;
	
	@NpcString(id = 1121008, message = "30 minutes remain.")
	public static NpcStringId ID_1121008;
	
	@NpcString(id = 1121009, message = "20 minutes remain.")
	public static NpcStringId ID_1121009;
	
	@NpcString(id = 1200001, message = "Chilly Coda")
	public static NpcStringId ID_1200001;
	
	@NpcString(id = 1200002, message = "Burning Coda")
	public static NpcStringId ID_1200002;
	
	@NpcString(id = 1200003, message = "Blue Coda")
	public static NpcStringId ID_1200003;
	
	@NpcString(id = 1200004, message = "Red Coda")
	public static NpcStringId ID_1200004;
	
	@NpcString(id = 1200005, message = "Golden Coda")
	public static NpcStringId ID_1200005;
	
	@NpcString(id = 1200006, message = "Desert Coda")
	public static NpcStringId ID_1200006;
	
	@NpcString(id = 1200007, message = "Lute Coda")
	public static NpcStringId ID_1200007;
	
	@NpcString(id = 1200008, message = "Twin Coda")
	public static NpcStringId ID_1200008;
	
	@NpcString(id = 1200009, message = "Dark Coda")
	public static NpcStringId ID_1200009;
	
	@NpcString(id = 1200010, message = "Shining Coda")
	public static NpcStringId ID_1200010;
	
	@NpcString(id = 1200011, message = "Chilly Cobol")
	public static NpcStringId ID_1200011;
	
	@NpcString(id = 1200012, message = "Burning Cobol")
	public static NpcStringId ID_1200012;
	
	@NpcString(id = 1200013, message = "Blue Cobol")
	public static NpcStringId ID_1200013;
	
	@NpcString(id = 1200014, message = "Red Cobol")
	public static NpcStringId ID_1200014;
	
	@NpcString(id = 1200015, message = "Golden Cobol")
	public static NpcStringId ID_1200015;
	
	@NpcString(id = 1200016, message = "Desert Cobol")
	public static NpcStringId ID_1200016;
	
	@NpcString(id = 1200017, message = "Sea Cobol")
	public static NpcStringId ID_1200017;
	
	@NpcString(id = 1200018, message = "Thorn Cobol")
	public static NpcStringId ID_1200018;
	
	@NpcString(id = 1200019, message = "Dapple Cobol")
	public static NpcStringId ID_1200019;
	
	@NpcString(id = 1200020, message = "Great Cobol")
	public static NpcStringId ID_1200020;
	
	@NpcString(id = 1200021, message = "Chilly Codran")
	public static NpcStringId ID_1200021;
	
	@NpcString(id = 1200022, message = "Burning Codran")
	public static NpcStringId ID_1200022;
	
	@NpcString(id = 1200023, message = "Blue Codran")
	public static NpcStringId ID_1200023;
	
	@NpcString(id = 1200024, message = "Red Codran")
	public static NpcStringId ID_1200024;
	
	@NpcString(id = 1200025, message = "Dapple Codran")
	public static NpcStringId ID_1200025;
	
	@NpcString(id = 1200026, message = "Desert Codran")
	public static NpcStringId ID_1200026;
	
	@NpcString(id = 1200027, message = "Sea Codran")
	public static NpcStringId ID_1200027;
	
	@NpcString(id = 1200028, message = "Twin Codran")
	public static NpcStringId ID_1200028;
	
	@NpcString(id = 1200029, message = "Thorn Codran")
	public static NpcStringId ID_1200029;
	
	@NpcString(id = 1200030, message = "Great Codran")
	public static NpcStringId ID_1200030;
	
	@NpcString(id = 1200031, message = "Alternative Dark Coda")
	public static NpcStringId ID_1200031;
	
	@NpcString(id = 1200032, message = "Alternative Red Coda")
	public static NpcStringId ID_1200032;
	
	@NpcString(id = 1200033, message = "Alternative Chilly Coda")
	public static NpcStringId ID_1200033;
	
	@NpcString(id = 1200034, message = "Alternative Blue Coda")
	public static NpcStringId ID_1200034;
	
	@NpcString(id = 1200035, message = "Alternative Golden Coda")
	public static NpcStringId ID_1200035;
	
	@NpcString(id = 1200036, message = "Alternative Lute Coda")
	public static NpcStringId ID_1200036;
	
	@NpcString(id = 1200037, message = "Alternative Desert Coda")
	public static NpcStringId ID_1200037;
	
	@NpcString(id = 1200038, message = "Alternative Red Cobol")
	public static NpcStringId ID_1200038;
	
	@NpcString(id = 1200039, message = "Alternative Chilly Cobol")
	public static NpcStringId ID_1200039;
	
	@NpcString(id = 1200040, message = "Alternative Blue Cobol")
	public static NpcStringId ID_1200040;
	
	@NpcString(id = 1200041, message = "Alternative Thorn Cobol")
	public static NpcStringId ID_1200041;
	
	@NpcString(id = 1200042, message = "Alternative Golden Cobol")
	public static NpcStringId ID_1200042;
	
	@NpcString(id = 1200043, message = "Alternative Great Cobol")
	public static NpcStringId ID_1200043;
	
	@NpcString(id = 1200044, message = "Alternative Red Codran")
	public static NpcStringId ID_1200044;
	
	@NpcString(id = 1200045, message = "Alternative Sea Codran")
	public static NpcStringId ID_1200045;
	
	@NpcString(id = 1200046, message = "Alternative Chilly Codran")
	public static NpcStringId ID_1200046;
	
	@NpcString(id = 1200047, message = "Alternative Blue Codran")
	public static NpcStringId ID_1200047;
	
	@NpcString(id = 1200048, message = "Alternative Twin Codran")
	public static NpcStringId ID_1200048;
	
	@NpcString(id = 1200049, message = "Alternative Great Codran")
	public static NpcStringId ID_1200049;
	
	@NpcString(id = 1200050, message = "Alternative Desert Codran")
	public static NpcStringId ID_1200050;
	
	@NpcString(id = 1303100, message = "Apiga")
	public static NpcStringId ID_1303100;
	
	@NpcString(id = 1303101, message = "Oink oink! This hurts too much! Are you sure you're trying to cure me? You're not very good at this!")
	public static NpcStringId ID_1303101;
	
	@NpcString(id = 1303102, message = "Oink oink! Nooo! I don't want to go back to normal! I look better with this body. What have you done?!")
	public static NpcStringId ID_1303102;
	
	@NpcString(id = 1303103, message = "Oink oink! Nooo! I don't want to go back to normal! I look better with this body. What have you done?!")
	public static NpcStringId ID_1303103;
	
	@NpcString(id = 1303104, message = "You cured me! Thanks a lot! Oink oink!")
	public static NpcStringId ID_1303104;
	
	@NpcString(id = 1800001, message = "(Queen Ant)  # %s's Command Channel has looting rights.")
	public static NpcStringId ID_1800001;
	
	@NpcString(id = 1800002, message = "(Core)  # %s's Command Channel has looting rights.")
	public static NpcStringId ID_1800002;
	
	@NpcString(id = 1800003, message = "(Orfen)  # %s's Command Channel has looting rights.")
	public static NpcStringId ID_1800003;
	
	@NpcString(id = 1800004, message = "(Zaken)  # %s's Command Channel has looting rights.")
	public static NpcStringId ID_1800004;
	
	@NpcString(id = 1800005, message = "(Queen Ant) Looting rules are no longer active.")
	public static NpcStringId ID_1800005;
	
	@NpcString(id = 1800006, message = "(Core) Looting rules are no longer active.")
	public static NpcStringId ID_1800006;
	
	@NpcString(id = 1800007, message = "(Orfen) Looting rules are no longer active.")
	public static NpcStringId ID_1800007;
	
	@NpcString(id = 1800008, message = "(Zaken) Looting rules are no longer active.")
	public static NpcStringId ID_1800008;
	
	@NpcString(id = 1800009, message = "# %s's Command Channel has looting rights.")
	public static NpcStringId ID_1800009;
	
	@NpcString(id = 1800010, message = "Looting rules are no longer active.")
	public static NpcStringId ID_1800010;
	
	@NpcString(id = 1800011, message = "Our master now comes to claim our vengeance. Soon you will all be nothing more than dirt...")
	public static NpcStringId ID_1800011;
	
	@NpcString(id = 1800012, message = "Death to those who challenge the Lords of Dawn!")
	public static NpcStringId ID_1800012;
	
	@NpcString(id = 1800013, message = "Death to those who challenge the Lord!")
	public static NpcStringId ID_1800013;
	
	@NpcString(id = 1800014, message = "Oink oink! Pigs can do it too! Antharas-Surpassing Super Powered Pig Stun! How do like them apples?")
	public static NpcStringId ID_1800014;
	
	@NpcString(id = 1800015, message = "Oink oink! Take that! Valakas-Terrorizing Ultra Pig Fear! Ha ha!")
	public static NpcStringId ID_1800015;
	
	@NpcString(id = 1800016, message = "Oink oink! Go away! Stop bothering me!")
	public static NpcStringId ID_1800016;
	
	@NpcString(id = 1800017, message = "Oink oink! Pigs of the world unite! Let's show them our strength!")
	public static NpcStringId ID_1800017;
	
	@NpcString(id = 1800018, message = "You healed me. Thanks a lot! Oink oink!")
	public static NpcStringId ID_1800018;
	
	@NpcString(id = 1800019, message = "Oink oink! This treatment hurts too much! Are you sure that you're trying to heal me?")
	public static NpcStringId ID_1800019;
	
	@NpcString(id = 1800020, message = "Oink oink! Transform with Moon Crystal Prism Power!")
	public static NpcStringId ID_1800020;
	
	@NpcString(id = 1800021, message = "Oink oink! Nooo! I don't want to go back to normal!")
	public static NpcStringId ID_1800021;
	
	@NpcString(id = 1800022, message = "Oink oink! I'm rich, so I've got plenty to share! Thanks!")
	public static NpcStringId ID_1800022;
	
	@NpcString(id = 1800023, message = "It's a weapon surrounded by an ominous aura. I'll discard it because it may be dangerous. Miss...!")
	public static NpcStringId ID_1800023;
	
	@NpcString(id = 1800024, message = "Thank you for saving me from the clutches of evil!")
	public static NpcStringId ID_1800024;
	
	@NpcString(id = 1800025, message = "That is it for today...let's retreat. Everyone pull back!")
	public static NpcStringId ID_1800025;
	
	@NpcString(id = 1800026, message = "Thank you for the rescue. It's a small gift.")
	public static NpcStringId ID_1800026;
	
	@NpcString(id = 1800138, message = "Squeak! This will be stronger than the stun the pig used last time!")
	public static NpcStringId ID_1800138;
	
	@NpcString(id = 1800139, message = "Squeak! Here it goes! Extremely scary, even to Valakas!")
	public static NpcStringId ID_1800139;
	
	@NpcString(id = 1800140, message = "Squeak! Go away! Leave us alone!")
	public static NpcStringId ID_1800140;
	
	@NpcString(id = 1800141, message = "Squeak! Guys, gather up! Let's show our power!")
	public static NpcStringId ID_1800141;
	
	@NpcString(id = 1800142, message = "It's not like I'm giving this because I'm grateful~ Squeak!")
	public static NpcStringId ID_1800142;
	
	@NpcString(id = 1800143, message = "Squeak! Even if it is treatment, my bottom hurts so much!")
	public static NpcStringId ID_1800143;
	
	@NpcString(id = 1800144, message = "Squeak! Transform to Moon Crystal Prism Power!")
	public static NpcStringId ID_1800144;
	
	@NpcString(id = 1800145, message = "Squeak! Oh, no! I don't want to turn back again...")
	public static NpcStringId ID_1800145;
	
	@NpcString(id = 1800146, message = "Squeak! I'm specially giving you a lot since I'm rich. Thank you")
	public static NpcStringId ID_1800146;
	
	@NpcString(id = 1800147, message = "Oink-oink! Rage is boiling up inside of me! Power! Infinite power!!")
	public static NpcStringId ID_1800147;
	
	@NpcString(id = 1800148, message = "Oink-oink! I'm really furious right now!")
	public static NpcStringId ID_1800148;
	
	@NpcString(id = 1800149, message = "Squeak! Rage is boiling up inside of me! Power! Infinite power!!")
	public static NpcStringId ID_1800149;
	
	@NpcString(id = 1800150, message = "Squeak! I'm really furious right now!!")
	public static NpcStringId ID_1800150;
	
	@NpcString(id = 1811000, message = "Fighter")
	public static NpcStringId ID_1811000;
	
	@NpcString(id = 1811001, message = "Warrior")
	public static NpcStringId ID_1811001;
	
	@NpcString(id = 1811002, message = "Gladiator")
	public static NpcStringId ID_1811002;
	
	@NpcString(id = 1811003, message = "Warlord")
	public static NpcStringId ID_1811003;
	
	@NpcString(id = 1811004, message = "Knight")
	public static NpcStringId ID_1811004;
	
	@NpcString(id = 1811005, message = "Paladin")
	public static NpcStringId ID_1811005;
	
	@NpcString(id = 1811006, message = "Dark Avenger")
	public static NpcStringId ID_1811006;
	
	@NpcString(id = 1811007, message = "Rogue")
	public static NpcStringId ID_1811007;
	
	@NpcString(id = 1811008, message = "Treasure Hunter")
	public static NpcStringId ID_1811008;
	
	@NpcString(id = 1811009, message = "Hawkeye")
	public static NpcStringId ID_1811009;
	
	@NpcString(id = 1811010, message = "Mage")
	public static NpcStringId ID_1811010;
	
	@NpcString(id = 1811011, message = "Wizard")
	public static NpcStringId ID_1811011;
	
	@NpcString(id = 1811012, message = "Sorcerer")
	public static NpcStringId ID_1811012;
	
	@NpcString(id = 1811013, message = "Necromancer")
	public static NpcStringId ID_1811013;
	
	@NpcString(id = 1811014, message = "Warlock")
	public static NpcStringId ID_1811014;
	
	@NpcString(id = 1811015, message = "Cleric")
	public static NpcStringId ID_1811015;
	
	@NpcString(id = 1811016, message = "Bishop")
	public static NpcStringId ID_1811016;
	
	@NpcString(id = 1811017, message = "Prophet")
	public static NpcStringId ID_1811017;
	
	@NpcString(id = 1811018, message = "Elven Fighter")
	public static NpcStringId ID_1811018;
	
	@NpcString(id = 1811019, message = "Elven Knight")
	public static NpcStringId ID_1811019;
	
	@NpcString(id = 1811020, message = "Temple Knight")
	public static NpcStringId ID_1811020;
	
	@NpcString(id = 1811021, message = "Sword Singer")
	public static NpcStringId ID_1811021;
	
	@NpcString(id = 1811022, message = "Elven Scout")
	public static NpcStringId ID_1811022;
	
	@NpcString(id = 1811023, message = "Plain Walker")
	public static NpcStringId ID_1811023;
	
	@NpcString(id = 1811024, message = "Silver Ranger")
	public static NpcStringId ID_1811024;
	
	@NpcString(id = 1811025, message = "Elven Mage")
	public static NpcStringId ID_1811025;
	
	@NpcString(id = 1811026, message = "Elven Wizard")
	public static NpcStringId ID_1811026;
	
	@NpcString(id = 1811027, message = "Spell Singer")
	public static NpcStringId ID_1811027;
	
	@NpcString(id = 1811028, message = "Elemental Summoner")
	public static NpcStringId ID_1811028;
	
	@NpcString(id = 1811029, message = "Oracle")
	public static NpcStringId ID_1811029;
	
	@NpcString(id = 1811030, message = "Elder")
	public static NpcStringId ID_1811030;
	
	@NpcString(id = 1811031, message = "Dark Fighter")
	public static NpcStringId ID_1811031;
	
	@NpcString(id = 1811032, message = "Palace Knight")
	public static NpcStringId ID_1811032;
	
	@NpcString(id = 1811033, message = "Shillien Knight")
	public static NpcStringId ID_1811033;
	
	@NpcString(id = 1811034, message = "Blade Dancer")
	public static NpcStringId ID_1811034;
	
	@NpcString(id = 1811035, message = "Assassin")
	public static NpcStringId ID_1811035;
	
	@NpcString(id = 1811036, message = "Abyss Walker")
	public static NpcStringId ID_1811036;
	
	@NpcString(id = 1811037, message = "Phantom Ranger")
	public static NpcStringId ID_1811037;
	
	@NpcString(id = 1811038, message = "Dark Mage")
	public static NpcStringId ID_1811038;
	
	@NpcString(id = 1811039, message = "Dark Wizard")
	public static NpcStringId ID_1811039;
	
	@NpcString(id = 1811040, message = "Spellhowler")
	public static NpcStringId ID_1811040;
	
	@NpcString(id = 1811041, message = "Phantom Summoner")
	public static NpcStringId ID_1811041;
	
	@NpcString(id = 1811042, message = "Shillien Oracle")
	public static NpcStringId ID_1811042;
	
	@NpcString(id = 1811043, message = "Shillien Elder")
	public static NpcStringId ID_1811043;
	
	@NpcString(id = 1811044, message = "Orc Fighter")
	public static NpcStringId ID_1811044;
	
	@NpcString(id = 1811045, message = "Orc Raider")
	public static NpcStringId ID_1811045;
	
	@NpcString(id = 1811046, message = "Destroyer")
	public static NpcStringId ID_1811046;
	
	@NpcString(id = 1811047, message = "Orc Monk")
	public static NpcStringId ID_1811047;
	
	@NpcString(id = 1811048, message = "Tyrant")
	public static NpcStringId ID_1811048;
	
	@NpcString(id = 1811049, message = "Orc Mage")
	public static NpcStringId ID_1811049;
	
	@NpcString(id = 1811050, message = "Orc Shaman")
	public static NpcStringId ID_1811050;
	
	@NpcString(id = 1811051, message = "Overlord")
	public static NpcStringId ID_1811051;
	
	@NpcString(id = 1811052, message = "Warcryer")
	public static NpcStringId ID_1811052;
	
	@NpcString(id = 1811053, message = "Dwarven Fighter")
	public static NpcStringId ID_1811053;
	
	@NpcString(id = 1811054, message = "Scavenger")
	public static NpcStringId ID_1811054;
	
	@NpcString(id = 1811055, message = "Bounty Hunter")
	public static NpcStringId ID_1811055;
	
	@NpcString(id = 1811056, message = "Artisan")
	public static NpcStringId ID_1811056;
	
	@NpcString(id = 1811057, message = "Warsmith")
	public static NpcStringId ID_1811057;
	
	@NpcString(id = 1811088, message = "Duelist")
	public static NpcStringId ID_1811088;
	
	@NpcString(id = 1811089, message = "Dreadnought")
	public static NpcStringId ID_1811089;
	
	@NpcString(id = 1811090, message = "Phoenix Knight")
	public static NpcStringId ID_1811090;
	
	@NpcString(id = 1811091, message = "Hell Knight")
	public static NpcStringId ID_1811091;
	
	@NpcString(id = 1811092, message = "Sagittarius")
	public static NpcStringId ID_1811092;
	
	@NpcString(id = 1811093, message = "Adventurer")
	public static NpcStringId ID_1811093;
	
	@NpcString(id = 1811094, message = "Archmage")
	public static NpcStringId ID_1811094;
	
	@NpcString(id = 1811095, message = "Soultaker")
	public static NpcStringId ID_1811095;
	
	@NpcString(id = 1811096, message = "Arcana Lord")
	public static NpcStringId ID_1811096;
	
	@NpcString(id = 1811097, message = "Cardinal")
	public static NpcStringId ID_1811097;
	
	@NpcString(id = 1811098, message = "Hierophant")
	public static NpcStringId ID_1811098;
	
	@NpcString(id = 1811099, message = "Eva's Templar")
	public static NpcStringId ID_1811099;
	
	@NpcString(id = 1811100, message = "Sword Muse")
	public static NpcStringId ID_1811100;
	
	@NpcString(id = 1811101, message = "Wind Rider")
	public static NpcStringId ID_1811101;
	
	@NpcString(id = 1811102, message = "Moonlight Sentinel")
	public static NpcStringId ID_1811102;
	
	@NpcString(id = 1811103, message = "Mystic Muse")
	public static NpcStringId ID_1811103;
	
	@NpcString(id = 1811104, message = "Elemental Master")
	public static NpcStringId ID_1811104;
	
	@NpcString(id = 1811105, message = "Eva's Saint")
	public static NpcStringId ID_1811105;
	
	@NpcString(id = 1811106, message = "Shillien Templar")
	public static NpcStringId ID_1811106;
	
	@NpcString(id = 1811107, message = "Spectral Dancer")
	public static NpcStringId ID_1811107;
	
	@NpcString(id = 1811108, message = "Ghost Hunter")
	public static NpcStringId ID_1811108;
	
	@NpcString(id = 1811109, message = "Ghost Sentinel")
	public static NpcStringId ID_1811109;
	
	@NpcString(id = 1811110, message = "Storm Screamer")
	public static NpcStringId ID_1811110;
	
	@NpcString(id = 1811111, message = "Spectral Master")
	public static NpcStringId ID_1811111;
	
	@NpcString(id = 1811112, message = "Shillien Saint")
	public static NpcStringId ID_1811112;
	
	@NpcString(id = 1811113, message = "Titan")
	public static NpcStringId ID_1811113;
	
	@NpcString(id = 1811114, message = "Grand Khavatari")
	public static NpcStringId ID_1811114;
	
	@NpcString(id = 1811115, message = "Dominator")
	public static NpcStringId ID_1811115;
	
	@NpcString(id = 1811116, message = "Doom Cryer")
	public static NpcStringId ID_1811116;
	
	@NpcString(id = 1811117, message = "Fortune Seeker")
	public static NpcStringId ID_1811117;
	
	@NpcString(id = 1811118, message = "Maestro")
	public static NpcStringId ID_1811118;
	
	@NpcString(id = 1900177, message = "<a action=\"bypass -h menu_select?ask=-4&reply=%s\">%s</a><br>")
	public static NpcStringId ID_1900177;
	
	private static final CLogger LOGGER = new CLogger(NpcStringId.class.getName());
	
	private static final String PARAMETER = "%s";
	
	private static final Map<Integer, NpcStringId> VALUES = new HashMap<>();
	
	static
	{
		// build fast lookup table
		for (Field field : NpcStringId.class.getDeclaredFields())
		{
			final int mod = field.getModifiers();
			if (Modifier.isStatic(mod) && Modifier.isPublic(mod) && field.getType().equals(NpcStringId.class) && field.isAnnotationPresent(NpcString.class))
			{
				final NpcString annotation = field.getAnnotationsByType(NpcString.class)[0];
				final NpcStringId nsId = new NpcStringId(annotation);
				try
				{
					field.set(null, nsId);
					VALUES.put(nsId.getId(), nsId);
				}
				catch (Exception e)
				{
					LOGGER.warn("Failed setting field \"{}\".", field.getName(), e);
				}
			}
		}
	}
	
	public static NpcStringId get(int id)
	{
		final NpcStringId nsi = VALUES.get(id);
		if (nsi == null)
		{
			LOGGER.warn("Message id {} does not exist.", id);
			return new NpcStringId(id);
		}
		
		return nsi;
	}
	
	public static String getNpcMessage(int id)
	{
		return get(id).getMessage();
	}
	
	public static String getNpcMessage(int id, Object... params)
	{
		return get(id).getMessage(params);
	}
	
	private final int _id;
	private final String _message;
	private final byte _paramCount;
	
	protected NpcStringId(int id)
	{
		_id = id;
		_message = "";
		_paramCount = 0;
	}
	
	protected NpcStringId(NpcString annotation)
	{
		_id = annotation.id();
		_message = annotation.message();
		_paramCount = (byte) (_message.split(PARAMETER, -1).length - 1);
	}
	
	public final int getId()
	{
		return _id;
	}
	
	public final String getMessage()
	{
		if (_paramCount > 0)
			LOGGER.warn("Wrong parameter count for {}, {} required.", this, _paramCount);
		
		return _message;
	}
	
	public final String getMessage(Object... params)
	{
		String message = _message;
		
		for (Object param : params)
			message = message.replaceFirst(PARAMETER, param.toString());
		
		if (message.indexOf(PARAMETER) > 0)
			LOGGER.warn("Wrong parameter count for {}, {} required and {} given.", this, _paramCount, params.length);
		
		return message;
	}
	
	public final byte getParamCount()
	{
		return _paramCount;
	}
	
	@Override
	public final String toString()
	{
		return "NS[" + _id + ": " + _message + "]";
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface NpcString
	{
		int id();
		
		String message();
	}
}