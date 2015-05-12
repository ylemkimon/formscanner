#######################################################################
#
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

echo "Starting FormScanner installation"
sleep 1

JAVA_PATH=$(which java)
echo "Found java in $JAVA_PATH"
sleep 1

INSTALL_DIR=$PWD
echo "Found FormScanner in $INSTALL_DIR"
sleep 1

FS_EXECUTABLE=$(ls lib/formscanner-main*)
echo "Found FormScanner executable: $FS_EXECUTABLE"
sleep 1

sleep 1
echo "#!/bin/sh" > $INSTALL_DIR/run.sh
echo "$JAVA_PATH -jar $FS_EXECUTABLE" >> $INSTALL_DIR/run.sh
chmod +x $INSTALL_DIR/run.sh
echo "Installation finished"
echo
echo "**********************************************************"
echo "*                                                        *"
echo "*           Type ./run.sh to start FormScanner.          *"
echo "*                                                        *"
echo "**********************************************************"

