#!/usr/bin/env bash

echo "start  bintrayUpload------------------------------"
gradlew clean build bintrayUpload -PbintrayUser=ilike -PbintrayKey=d6d13496e61fc40a68bf110103226e74821580d9  -PdryRun=true