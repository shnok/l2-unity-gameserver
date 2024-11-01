@echo off

REM ############################################
REM ## You can change here your own DB params ##
REM ############################################
REM MYSQL BIN PATH
set mysqlBinPath=C:\Program Files\MariaDB 11.5\bin

REM LOGINSERVER
set LSUSER=root
set LSPASS=
set LSDB=l2unity
set LSHOST=localhost

REM GAMESERVER
set GSUSER=root
set GSPASS=
set GSDB=l2unity
set GSHOST=localhost
REM ############################################

set mysqldumpPath="%mysqlBinPath%\mysqldump"
set mysqlPath="%mysqlBinPath%\mysql"

echo.
echo.                        aCis database installation
echo.                        __________________________
echo.
echo OPTIONS : (f) full install, it will destroy all (need validation).
echo           (s) skip characters data, it will install only static server tables.
echo.
:asklogin
set loginprompt=x
set /p loginprompt=Installation type: (f) full, (s) skip or (q) quit? 
if /i %loginprompt%==f goto fullinstall
if /i %loginprompt%==s goto lskip
if /i %loginprompt%==q goto end
goto asklogin

REM ############################################
:fullinstall

:validation
set jaja=x
set /p jaja=Are you sure to delete all databases, even characters (y/n) ? 
if /i %jaja%==y goto destruction
if /i %jaja%==n goto lskip
goto validation

:destruction
echo.
echo.
echo Deleting characters-related tables.
%mysqlPath% -h %LSHOST% -u %LSUSER% --password=%LSPASS% -D %LSDB% < full_install.sql
echo Done.
echo.
echo Installing empty character-related tables.
%mysqlPath% -h %LSHOST% -u %LSUSER% --password=%LSPASS% -D %LSDB% < ../sql/accounts.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/auctions.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/augmentations.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/bbs_favorite.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/bbs_forum.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/bbs_mail.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/bbs_post.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/bbs_topic.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/bookmarks.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/buffer_schemes.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/buylists.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/castle_doorupgrade.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/castle_manor_procure.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/castle_manor_production.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/castle_trapupgrade.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/character_hennas.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/character_macroses.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/character_memo.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/character_quests.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/character_raid_points.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/character_recipebook.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/character_recommends.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/character_relations.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/character_shortcuts.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/character_skills.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/character_skills_save.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/character_subclasses.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/characters.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/clan_data.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/clan_privs.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/clan_skills.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/clan_subpledges.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/clan_wars.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/clanhall.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/clanhall_flagwar_attackers.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/clanhall_flagwar_members.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/clanhall_flagwar_owner_npcs.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/clanhall_functions.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/clanhall_siege_attackers.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/cursed_weapons.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/fishing_championship.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/games.sql
%mysqlPath% -h %LSHOST% -u %LSUSER% --password=%LSPASS% -D %LSDB% < ../sql/gameservers.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/grandboss_list.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/heroes_diary.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/heroes.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/items.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/items_on_ground.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/mdt_bets.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/mdt_history.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/mods_wedding.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/olympiad_fights.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/olympiad_nobles_eom.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/olympiad_nobles.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/petition.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/petition_message.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/pets.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/rainbowsprings_attacker_list.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/server_memo.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/seven_signs.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/seven_signs_festival.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/seven_signs_status.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/siege_clans.sql

echo Done.
echo.

REM ############################################
:lskip
echo Deleting server tables.
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < gs_install.sql
echo Done.
echo.
echo Installing server tables.
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/castle.sql
%mysqlPath% -h %GSHOST% -u %GSUSER% --password=%GSPASS% -D %GSDB% < ../sql/spawn_data.sql
REM ############################################
:end
echo.
echo Was fast, isn't it ?
pause
