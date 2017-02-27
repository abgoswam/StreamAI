#!/bin/bash
# Run first time through to setup the environment variables, example data, and start the services 
echo '*** MAKE SURE YOU RUN THIS ONLY ONCE ***'

echo '...Sourcing StreamAI-specific Env Variables...'
# source /root/streamai/config/bash/streamai.bashrc

echo '...Configuring Services Before Starting...'
echo ''
echo '********************************************'
echo '* Please Be Patient and Ignore All Errors! *'
echo '********************************************'
echo ''

$SCRIPTS_HOME/setup/config-services-before-starting.sh

echo '...Start All Services...'
$SCRIPTS_HOME/service/start-all-services.sh

echo '...Create Examples Data Sources...'
$SCRIPTS_HOME/setup/create-example-datasources.sh

echo '...Show Exported Variables...'
export

echo '...Show Running Java Processes...'
jps -l

echo ''
echo '********************************'
echo '*** All Services Running OK! ***'
echo '********************************'
echo ''
