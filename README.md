Game
====

This project uses a variety of dependent libraries and tools:

Client:
* Ivy - http://ant.apache.org/ivy/ -  (Dependency resolver)
* LWJGL - http://www.lwjgl.org/ - (Compile & Runtime)
* Nifty-GUI - http://nifty-gui.lessvoid.com// - (Compile & Runtime)
* Google Protocol Buffers (Protobuf) - https://code.google.com/p/protobuf/ - (Compile & runtime)
* Netty - http://netty.io/ - (Compile & runtime)
* Log4J - http://logging.apache.org/log4j/1.2/ - (Compile & runtime)

Server
* Ivy - http://ant.apache.org/ivy/ -  (Dependency resolver)
* Google Protocol Buffers (Protobuf) - https://code.google.com/p/protobuf/ - (Compile & runtime)
* Netty - http://netty.io/ - (Compile and runtime)
* Spring Framework (Core, Beans & Context) - http://www.springsource.org/ - (Compile & runtime)
* Hibernate - http://www.hibernate.org/ - (Compile & runtime)
* Commons CLI - http://commons.apache.org/proper/commons-cli/ (Compile & runtime)
* Project Voldemort - http://www.project-voldemort.com/voldemort/ - (Compile & runtime)
* MySQL driver - http://www.mysql.com/products/connector/ - (Currently actually unused, GPL license)
* Log4J - http://logging.apache.org/log4j/1.2/ - (Compile & runtime)

Build notes:
* Note that Voldemort (and it's sub-dependencies) are currently in the repository because I can't find Voldemort on Maven Central.
* LWJGL's libraries are also statically included because they have native libraries and there's no transparent solution for this in Ivy. If you're building on something else but Windows you'll want to change the referenced native library to the correct folder.
* Nifty's libraries are statically included because it isn't in Maven Central. It's dependencies (EventBus and XPP3) are in Ivy however.
* The rest are downloaded from Maven Central.