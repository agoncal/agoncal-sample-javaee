#!/usr/bin/env bash

JBOSS_HOME=~/Tools/JBoss/wildfly-10.0.0.Final
JBOSS_CLI=$JBOSS_HOME/bin/jboss-cli.sh

$JBOSS_CLI --connect --command="deploy --force cdbookstore/target/cdbookstore.war"
$JBOSS_CLI --connect --command="deploy --force topbooks/target/topbooks.war"
$JBOSS_CLI --connect --command="deploy --force topcds/target/topcds.war"