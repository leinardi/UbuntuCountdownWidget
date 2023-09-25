#!/usr/bin/env bash

cat app/build/outputs/app_versioning/release/version_name.txt <(echo -n "+") app/build/outputs/app_versioning/release/version_code.txt > version.txt && cat version.txt
