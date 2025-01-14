# l2-unity-gameserver

This repository is modified version of ACIS L2J for the project [l2-unity](https://gitlab.com/shnok/l2-unity).

## Current features

It contains all of the features of an interlude server. It only comes down to if a feature is handled by the client or not.

## How to use?

- Download and install [MariaDB](https://downloads.mariadb.org/).
- Create the DB *l2unity*.
- Donwnload clone both [l2-unity-gameserver](https://github.com/shnok/l2-unity-gameserver) and [l2-unity-loginserver](https://github.com/shnok/l2-unity-loginserver) repositories.
- Update the mysqlBinPath, DB user and password values in the *gameserver/db/tools/database_installer.bat* file.
- Execute the bat file to create the tables.
- Open both repositories with with [intellij](https://www.jetbrains.com/idea/download/download-thanks.html?platform=windows&code=IIC).
- To start the server do a *gradle run* on both the loginserver and gameserver.
