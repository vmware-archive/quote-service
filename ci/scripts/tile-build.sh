#!/bin/sh -e

SOURCE_DIR=$1
TARGET_DIR=$2



#BIN_DIR="$( cd "${TILE_GEN_DIR}/bin" && pwd )"

#TILE="${BIN_DIR}/tile"

#HISTORY=`ls ${HISTORY_DIR}/tile-history-*.yml`
#if [ -n "${HISTORY}" ]; then
#	cp ${HISTORY} ${SOURCE_DIR}/tile-history.yml
#fi

#(cd ${SOURCE_DIR}; mvn package)

#VERSION=`grep '^version:' ${SOURCE_DIR}/tile-history.yml | sed 's/^version: //'`
#HISTORY="tile-history-${VERSION}.yml"

#cd ${SOURCE_DIR}
#CMD ["mvn", "package"]
#cp ${SOURCE_DIR}/target/*.jar ${TARGET_DIR}


#cp ${SOURCE_DIR}/tile-history.yml ${TARGET_DIR}/tile-history-${VERSION}.yml
