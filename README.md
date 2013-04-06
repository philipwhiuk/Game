Game
====

This project uses a variety of dependent libraries and tools:

Client:
* Ivy - http://ant.apache.org/ivy/ -  (Dependency resolver)
* LWJGL - http://www.lwjgl.org/ - (Compile time & Runtime)
* Google Protocol Buffers (Protobuf) - https://code.google.com/p/protobuf/ - (Compile time and runtime)
* Netty - http://netty.io/ - (Compile and runtime)
* Log4J

Server
* Ivy - http://ant.apache.org/ivy/ -  (Dependency resolver)
* Google Protocol Buffers (Protobuf) - https://code.google.com/p/protobuf/ - (Compile time and runtime)
* Netty - http://netty.io/ - (Compile and runtime)
* Spring Framework (Core, Beans & Context)
* Hibernate - http://www.hibernate.org/ - (Compile & Runtime)
* Commons CLI - http://commons.apache.org/proper/commons-cli/ (Compile & runtime)
* Project Voldemort - http://www.project-voldemort.com/voldemort/ - (Compile & runtime)
* MySQL driver - http://www.mysql.com/products/connector/ - (Currently actually unused, GPL license)

Build notes:
* Note that Voldemort (and it's sub-depedencies) are currently in the repository because I can't find Voldemort on Maven Central.
* LWJGL's libraries are also statically included because they have native libraries and there's no transparent solution for this in Ivy. If you're building on something else but Linux you'll want to change the referenced native library to the correct folder.
* The rest are downloaded from Maven Central.