Dance Server
===
The Dance Server aims to emulate the behaviour of the original DANCE! ONLINE game.

## Table of contents
- [Building](#building)
- [Versioning](#versioning)
- [Running](#running)
- [Scripts](#scripts)
- [Structure](#structure)
  - [./src](#src)
  - [./bot](#bot)
  - [./database](#database)
  - [./editor](#editor)
    - [./editor/itemlist](#editoritemlist)
    - [./editor/songcrypto](#editorsongcrypto)
    - [./editor/stepfile](#editorstepfile)
  - [./library](#library)
  - [./log](#log)
  - [./server](#server)
    - [./server/channel](#serverchannel)
    - [./server/chat](#serverchat)
    - [./server/game](#servergame)
    - [./server/group](#servergroup)
    - [./server/inventar](#serverinventar)
    - [./server/lobby](#serverlobby)
    - [./server/mail](#servermail)
    - [./server/packet](#serverpacket)
    - [./server/query](#serverquery)
    - [./server/room](#serverroom)
    - [./server/shop](#servershop)
    - [./server/song](#serversong)
    - [./server/tcp](#servertcp)
    - [./server/wedding](#serverwedding)
- [Best Practise](#best-practise)
- [Further Thoughts / Bigger Goals](#further-thoughts-bigger-goals)
- [Developer Environment](#developer-environment)
- [Links](#links)

## Building

Gradle is used as the build system.  
If no local gradle installation exists you can use the gradle wrapper to build the project.  
To use the wrapper run 'gradlew' instead of the 'gradle'-command.  
Further details: [https://docs.gradle.org/current/userguide/gradle_wrapper.html](https://docs.gradle.org/current/userguide/gradle_wrapper.html)

Building the project:
```bash
gradle build
# without gradle installation
gradlew build
```

Building a single executable .jar-file:
```bash
gradle fatJar
# without gradle installation
gradlew fatJar
```

Building a single executable .jar-file without version:
```bash
gradle fatJar -Pserver
# without gradle installation
gradlew fatJar -Pserver
```

## Versioning

The version number depends on the git commit.
The command `git describe --tags` is used to determine the version number.
It is made up of the latest `git tag` along with the `number of commits after that tag` and the `short commit hash` of
the commit the release is based on.

Ex:   
```
{ProjectName}-bundle-{Version/GitTag}-{Number of commits after tag}-{Short git hash of commit}.jar   
dance-bundle-1.0-1-g588a3f9.jar
```


## Running

Running Server:
```bash
gradle run
# without gradle installation
gradlew run
```

Running Server from fatJar:
```bash
# GUI
java -jar dance-bundle-1.0.jar
# No GUI, just server
java -jar dance-bundle-1.0.jar server 
```

## Scripts

Deletes all files that are ignored by git:
```bash
# unix
./clean.sh 
# windows
./clean.bat 
```

Structure
===
The Server is written in a modular way, that allows to exchange and reuse components easily.  

## [/src](./src/main/java/net/arrowgene/dance)

Provides the entry point to launching the server and all related tools.  
It processes the arguments and launches the appropriate components.  
Following arguments are implemented:  
- server: Launches only the server

If no arguments are passed the graphical user interface will be launched.

Only the graphical components who depend on the server-project are included.
All other user interfaces can be found in the editor-project.
Adding new graphical components is easy by extending the 'EditorFrame'-class, this provides all information on how a window is displayed.

## [/bot](./bot/src/main/java/net/arrowgene/dance/bot)
Automated client to interact with the game-world via server.

TODO:
- Interact with server by using existing protocol (as client)
- Extended Bot, knows the server instance, overrides methods to be spawned as a thread by server
- join Rooms, play games, be fair :)
- ability to spawn bot for playing by players (think lover-mode is missing a player)


## [/database](./database/src/main/java/net/arrowgene/dance/database)  
Backend to store persistent data.

Function:
- Provides an interface to implement different storage solutions in the future.

Ensure:
- Save to DB when creating new entities who require to have an 'id'-property.
- Save to DB on critical operations (e.g. change of 'coins'-property)

## [/editor](./editor/src/main/java/net/arrowgene/dance/editor)  
Contains graphical user interface components to manage tasks.

TODO:
- "songlist.dat"-File editor
- "datas.sac"-File editor
- "Add/Remove Song"-function (create entry in songlist.dat & DB, produce all necessary client files)

#### [/editor/itemlist](./editor/src/main/java/net/arrowgene/dance/editor/itemlist)  
Read/ Write 'iteminfo.dat'

Function:
- View all items
- Edit item
- Add new item
- Export to Database

#### [/editor/songcrypto](./editor/src/main/java/net/arrowgene/dance/editor/songcrypto)  

#### [/editor/stepfile](./editor/src/main/java/net/arrowgene/dance/editor/stepfile)  
Stepfile Editor

Function:
- Read / Write '*.gn'-files
- Display properties of stepfile (notes, difficulty)
- Visualise steps

## [/library](./library/src/main/java/net/arrowgene/dance/library)  
Includes shared functions that are used in multiple projects.

Function:
- Database Models
- Classes for client file reading/altering/writing
- Utility Classes

## [/log](./log/src/main/java/net/arrowgene/dance/log)  
Handles log messages.

Function:
- Different Log Levels
- Scheduled writing to file
- Printing to console

## [/server](./server/src/main/java/net/arrowgene/dance/server)
Accepts clients and handles requests.

#### [/server/channel](./server/src/main/java/net/arrowgene/dance/server/channel)    
Displays a list of channels available to join.

TODO:
- Test synchronisation of players (channel status)
- Implement the special competition channel

#### [/server/chat](./server/src/main/java/net/arrowgene/dance/server/chat)  
Lobby, Room and Private messages are handled.

Function:
- Easy to extend by middleware
- Commands
- Filtering bad words
- Logging of chat messages

TODO:
- Banner Messages (Scrolling top of screen)
- World Messages (Shouting with the speaker item)

#### [/server/game](./server/src/main/java/net/arrowgene/dance/server/game)   
Manages starting a game, handling scores

#### [/server/group](./server/src/main/java/net/arrowgene/dance/server/group)  
Creates new groups and keeps track of exist ones.

#### [server/inventar](./server/src/main/java/net/arrowgene/dance/server/inventar)  
Manages items a player owns.

#### [/server/lobby](./server/src/main/java/net/arrowgene/dance/server/lobby)  
Manages the players inside a lobby, and keeps track of rooms.

#### [/server/mail](./server/src/main/java/net/arrowgene/dance/server/mail)  
System to send and receive in game mails.

#### [/server/packet](./server/src/main/java/net/arrowgene/dance/server/packet)  
Handling of individual client requests and building the answer.

- [/packet/builder](./server/src/main/java/net/arrowgene/dance/server/packet/builder)  
PacketBuilders are Factories that help building the packet.  
The Factory should only ask for the minimum necessary input to build a packet.
- [/packet/handle](./server/src/main/java/net/arrowgene/dance/server/packet/handle)  
Packet Handlers are Classes that handle an incoming request, and craft the response.  

TODO:
- Implement missing packet functionality.

#### [/server/query](./server/src/main/java/net/arrowgene/dance/server/query)  
Interface to talk with external clients

TODO:
- remove dependency of 'com.sun.net.httpserver.HttpServer'

#### [/server/room](./server/src/main/java/net/arrowgene/dance/server/room)  
Handles room state, keeps track of the players inside

TODO:
- Sync (Test joining / leaving)

#### [/server/shop](./server/src/main/java/net/arrowgene/dance/server/shop)  
Handles shopping actions (buy / sell)

TODO:
- Implement missing features

#### [/server/song](./server/src/main/java/net/arrowgene/dance/server/song)  
Manages available songs

#### [/server/tcp](./server/src/main/java/net/arrowgene/dance/server/tcp)  
Underlying system to handle TCP connections

TODO:
- Testing of different implementations / multiple clients / load
- Finding best implementation

#### [/server/wedding](./server/src/main/java/net/arrowgene/dance/server/wedding)  
Lets two people marry, displaying their characters in each others profile.

Dependencies
===
- org.xerial:sqlite-jdbc:3.20.1

Best Practise
===
- Own the Code: extract solutions, discard libraries
- Reduce Inheritance: Favor composition over inheritance
- Reduce Inheritance: Don't target the Interface (ie: declare varables as List<> instead of ArrayList<>) until necessary
- Reduce Database Access: Manage high read/write access in memory, write back when crashing/closing (Except critical transactions, money, etc.)
- Annotate functions with Javadoc (http://blog.joda.org/2012/11/javadoc-coding-standards.html)

Further Thoughts / Bigger Goals
===

## File Source- / Storage- / Management-System
Duplicated information is stored in more than one place. For example the properties of items are stored in a file (iteminfo.dat) and inside the database. 
It need to be ensured that those files are always synchronised. 
Therefor a source needs to be set on which can be relied that it contains the latest up to date state of the information. 
A system need to be put in place that can produce all necessary files based on the source.

1. Define a source ("master"-source) that holds the latest version of the information / where information are stored and updated
2. Build a system that can produce all files from the "master"-source
3. Version these files, so clients can always be sure to be synchronised with the servers "master"-source
4. If the "master"-source is updated, the server needs to be shut down, a version needs to be bumped, and when clients connect they need to update in order to be synchron with the "master"-source again

## Inspector:
- A new project called "inspector"
- Operates as a proxy and lists all incoming / outgoing packets
- Provide inspection of the packets (display all known and unknwon properties with name and value)

Developer Environment
===
Steps to setup an development environment with various IDEs.

## Visual Studio Code

- [Language Support for Java(TM) by Red Hat](https://marketplace.visualstudio.com/items?itemName=redhat.java)
- [Troubleshooting](https://github.com/redhat-developer/vscode-java/wiki/Troubleshooting)

## IntelliJ


Links
===
- [Arrowgene](https://arrowgene.net)
- [Markdown-Cheatsheet](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet)
- SDO-X (Malaysia) [http://sdox.cibmall.net/](http://sdox.cibmall.net/)

# Create the best Dance! Online Server :)

# Remarks
- When a client disconnects, at some points we still try to write to the socket (maybe test if alive or smth?)

