@echo off
javac -Xlint:deprecation -cp "./class;./jars/craftbukkit.jar" -d "./class" ./src/com/hybris/bukkit/headblocks/*.java
cd ./class
jar cvf "HeadBlocks.jar" ./plugin.yml ./com/
move /Y HeadBlocks.jar ../jars/
pause
