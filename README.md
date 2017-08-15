# Custom Guild Wars 2 Launcher for ArcDPS' and BGDM's users

This is a custom launcher that is intended to keep Guild Wars 2 and ArcDPS and BGDM up-to-date and fully functional.

## Getting Started


### Prerequisites

This application is written in Java and can only to be executed on a Windows x64 environment with a x64 version of Guild Wars 2.
It requires the [Java Runtime Environment](https://www.java.com/it/download/) to be executed correctly.


### Features
- **Installer:** The launcher enables the user to install BGDM and ArcDPS with just one click.
- **Auto-update:** The d3d9.dll containing ArcDPS will always be up-to-date. Each time the launcher is executed it retrives the md5 checksum of the last avaiable version of ArcDPS, if it doesn't match a new updated version will be downloaded and installed.
- **Backup:** A backup version of the previously version of the addon will be stored in the Guild Wars 2 / bin64 directory with the name d3d9_old.dll.
- **Light on resources:** The updater doesn't run in background during the daily-use of your computer or during your play sessions. It runs only for the time needed to update the dll, usually around 1-2 seconds, at the start of Guild Wars 2 then it terminates automatically.
- **Configuration download:** Each time it checks if archdps.ini exists and and if it were not the launcher will ask if the user would like to automatically download an archdps.ini with default settings.
- **Fast-start:** The launcher comes with an autostart feature and one to hide the GUI. The update process will be completely invisible and will cause just a slightly delay before the launch of the official launcher.
- **Freedom of choice:** It provides the user the ability to decide to run GW2 with or without the addons at anytime.


### Setup

1. Place the Gw2-Launcher.exe in the Guild Wars 2 folder (same path of Gw2-64.exe).
2. Create a shortcut to the launcher and place it where you like (e.g, Desktop).
3. Execute the launcher the first time and change the settings the way you want.
4. Remember to use the shortcut everytime you want to lunch Guild Wars 2.

### Download
- [For Windows x64](https://www.dropbox.com/s/e83yvt4jtpdblew/Gw2-Launcher.exe?dl=0)
	

	
## Acknowledgments

* Huge thanks to deltaconnected and Bhagawan for their work and perseverance in releasing updates for ArcDPS and BGDM, respectively.
* Thanks to Lingala for the [Zip4J Library](http://www.lingala.net/zip4j/index.php)