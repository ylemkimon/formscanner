# FormScanner - Free OMR Software
# 
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or 
# (at your option) any later version.
# 
# This program is distributed in the hope that it will be useful, 
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with this program. If not, see http://www.gnu.org/licenses
#
####################################################################### 

#!/bin/sh

JAVA_PATH=$(readlink -f $(which java))
INSTALL_DIR=$PWD
FS_EXECUTABLE=$(ls $INSTALL_DIR/lib/formscanner-main*)

echo "#!/bin/sh" > $INSTALL_DIR/bin/run.sh
echo "$JAVA_PATH -jar -DFormScanner_HOME=$INSTALL_DIR -DFormScanner_VERSION=0.11-SNAPSHOT $FS_EXECUTABLE" >> $INSTALL_DIR/bin/run.sh
chmod +x $INSTALL_DIR/bin/run.sh
