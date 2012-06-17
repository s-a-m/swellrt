#!/bin/bash

# This script will start the Wave in a Box server.
#

# Make sure the config file exists.
if [ ! -e server.config ]; then
  echo "You need to copy server.config.example to server.config and edit it. Or run: 'ant -f server-config.xml' to generate the file automatically."
  exit 1
fi

# The version of Wave in a Box, extracted from the build.properties file
WAVEINABOX_VERSION=`sed "s/[\\t ]*=[\\t ]*/=/g" build.properties | grep ^waveinabox.version= | cut -f2 -d=`

. process-script-args.sh

exec java $DEBUG_FLAGS \
  -Djava.util.logging.config.file=logging.properties \
  -Dorg.eclipse.jetty.LEVEL=INFO \
  -Djava.security.auth.login.config=jaas.config \
  -Dwave.server.config=server.config \
  -jar dist/waveinabox-server-$WAVEINABOX_VERSION.jar
#  -DVERBOSE \
#  -Djava.util.logging.ConsoleHandler.level=ALL \
#  -Djava.util.logging.FileHandler.level=ALL \
