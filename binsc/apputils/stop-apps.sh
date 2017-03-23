#!/bin/bash

echo '...Stopping Data Producer...'
jps -l | grep "org.codehaus.plexus.classworlds.launcher.Launcher" | cut -d " " -f 1 | xargs kill -9

sleep 1m

echo '...Stopping Spark Streaming App...'
jps -l | grep "sbt-launch" | cut -d " " -f 1 | xargs kill -9

sleep 1m

echo '...End..'
