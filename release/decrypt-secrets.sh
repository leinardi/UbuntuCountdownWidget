#!/bin/bash

#
# Ubuntu Countdown Widget
# Copyright (C) 2023 Roberto Leinardi
#
# This program is free software: you can redistribute it and/or modify it under the terms
# of the GNU General Public License as published by the Free Software Foundation,
# either version 3 of the License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful, but WITHOUT ANY
# WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
# A PARTICULAR PURPOSE. See the GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License along with this
# program.  If not, see <http://www.gnu.org/licenses/>.
#

decrypt() {
  PASSPHRASE=$1
  INPUT=$2
  OUTPUT=$3
  gpg --quiet --batch --yes --decrypt --passphrase="$PASSPHRASE" --output $OUTPUT $INPUT
}

if [[ ! -z "$ENCRYPT_KEY" ]]; then
  # Decrypt Release key
  decrypt ${ENCRYPT_KEY} release/app-release.gpg release/app-release.jks
  # Decrypt Play Store key
  decrypt ${ENCRYPT_KEY} release/play-account.gpg release/play-account.json
else
  echo "ENCRYPT_KEY is empty"
fi
