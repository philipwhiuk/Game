Game
====

This project uses a variety of dependent libraries and tools:

Client:
* Maven - http://maven.apache.org/ -  (Software Project Management)
* LWJGL - http://www.lwjgl.org/ - (Compile & Runtime)
* Nifty-GUI - http://nifty-gui.lessvoid.com// - (Compile & Runtime)
* Google Protocol Buffers (Protobuf) - https://code.google.com/p/protobuf/ - (Compile & runtime)
* Netty - http://netty.io/ - (Compile & runtime)
* Log4J - http://logging.apache.org/log4j/1.2/ - (Compile & runtime)

Server
* Maven - http://maven.apache.org/ -  (Software Project Management)
* Google Protocol Buffers (Protobuf) - https://code.google.com/p/protobuf/ - (Compile & runtime)
* Netty - http://netty.io/ - (Compile and runtime)
* Spring Framework (Core, Beans & Context) - http://www.springsource.org/ - (Compile & runtime)
* Hibernate - http://www.hibernate.org/ - (Compile & runtime)
* Commons CLI - http://commons.apache.org/proper/commons-cli/ (Compile & runtime)
* H2 driver - http://www.h2database.com/ - (Runtime & Driver library)
* Log4J - http://logging.apache.org/log4j/1.2/ - (Compile & runtime)
* Java Mail - http://www.oracle.com/technetwork/java/javamail/index.html - (Compile & runtime)

Build notes:
* To build, run `./build.sh` This will perform a series of `mvn clean install` commands
* Alternatively, open the projects in Eclipse using the committed project files.
* The server relies on a local MySQL database. The `install.sql` file provides the schema. In the long term a more formalised install process will be established.

Running it:
* `java -cp GameClient/target/GameClient-0.0.1-SNAPSHOT-jar-with-dependencies.jar -Djava.library.path=/full/path/to/native/macosx/ com.whiuk.philip.mmorpg.client.Main`
* `java -cp GameServer/target/GameServer-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.whiuk.philip.mmorpg.server.Main`
