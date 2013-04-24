#!/bin/bash
javac -Xlint:deprecation -cp "./class:./jars/craftbukkit.jar" -d "./class" "./src/com/hybris/bukkit/headblocks/HeadBlocks.java" "./src/com/hybris/bukkit/headblocks/HeadBlocksExecutor.java"
cd ./class
jar cvf "HeadBlocks.jar" ./plugin.yml ./com/
mv HeadBlocks.jar ../jars/
