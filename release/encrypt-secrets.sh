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

encrypt() {
  PASSPHRASE=$1
  INPUT=$2
  OUTPUT=$3
  gpg --batch --yes --passphrase="$PASSPHRASE" --cipher-algo AES256 --symmetric --output "$OUTPUT" "$INPUT"
}

if [[ -n "$ENCRYPT_KEY" ]]; then
  # Encrypt Release key
  encrypt "${ENCRYPT_KEY}" release/app-release.jks release/app-release.gpg
  # Encrypt Play Store key
  encrypt "${ENCRYPT_KEY}" release/play-account.json release/play-account.gpg
else
  echo "ENCRYPT_KEY is empty"
fi
