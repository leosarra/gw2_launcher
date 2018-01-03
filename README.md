# Custom Guild Wars 2 Launcher for ArcDPS' users

This is a custom launcher that is intended to keep Guild Wars 2 and ArcDPS fully functional.

## Getting Started


### Prerequisites

This application is written in Java and can only to be executed on a Windows x64 environment with a x64 version of Guild Wars 2.
It requires the [Java Runtime Environment](http://www.oracle.com/technetwork/java/javase/downloads/index.html) (JRE 9 and above is highly recommended) to be executed correctly.


### Features
- **Installer:** The launcher enables the user to install (and remove) ArcDPS with just one click.
- **Auto-update:** The d3d9.dll containing ArcDPS will always be up-to-date. Each time the launcher is executed it retrives the md5 checksum of the last avaiable version of ArcDPS, if it doesn't match a new updated version will be downloaded and installed.
- **Backup:** A backup version of the previously version of the addon will be stored in the Guild Wars 2 / bin64 directory with the name d3d9_old.dll.
- **Light on resources:** The updater doesn't run in background during the daily-use of your computer or during your play sessions. It runs only for the time needed to update the dll, usually around 1-2 seconds, at the start of Guild Wars 2 then it terminates automatically.
- **Configuration download:** Each time it checks if archdps.ini exists and and if it were not the launcher will ask if the user would like to automatically download an archdps.ini with default settings.
- **Fast-start:** The launcher comes with an autostart feature and one to hide the GUI. The update process will be completely invisible and will not require any additional input from the user.
- **Freedom of choice:** It provides the user the ability to decide to run GW2 with or without the addons at anytime.


### Setup

1. Place the Gw2-Launcher.exe in the Guild Wars 2 folder (same path of Gw2-64.exe).
2. Create a shortcut to the launcher and place it where you like (e.g, Desktop).
3. Execute the launcher the first time and change the settings the way you want.
4. Remember to use the shortcut everytime you want to launch Guild Wars 2.


### Download
- [For Windows x64](https://github.com/LithiumSR/gw2_launcher/releases)
	

### Issues
- This program isn't signed with a certificate so Windows could stop it's execution because it comes from "an unknown developer". You can fix that by changing "Smartscreen settings" in Windows Defender Security Center from "Block" to "Warn", this change will enable you to skip the warning.
- If you have a screen with an high DPI value and the UI of this custom launcher is not displayed correctly please install update your [Java Runtime Environment](http://www.oracle.com/technetwork/java/javase/downloads/index.html) (JRE 9 and above is required if you have an high DPI screen).
## Acknowledgments

* Huge thanks to deltaconnected for his work on ArcDPS and his perseverance in releasing updates :)