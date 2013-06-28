# Together Workflow Editor 
# Copyright (C) 2011 Together Teamsolutions Co., Ltd.
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

#!/bin/bash
#
#
#

function help {
	echo
	echo $(basename $0) "-option=value ..."
	echo
	echo $(basename $0) "-option value ..."
	echo
	echo "Options:"
	echo " 
	jdkhome

	Example: configure -jdkhome /usr/java/jdk1.6.0_20

"

}

for n in $*
do
	case $n in
		--help | -help) help
			exit 0
		;;      
	esac            
done                    

cd $(dirname $0)        
                        
if
	[[ -r configure.properties ]]
then
	fname=/tmp/.configure.properties$$.tmp
	grep -v "^[ 	]*#" ./configure.properties |
		grep -v ^$ |
		sed -e 's/\./_DOT_/g' >$fname
	. $fname
	rm  $fname
fi

for n in $*             
do
	#if  the previous option needs an argument, assign it
                        
	if
		[[ -n $previous ]]
	then 
		eval "$previous=\$n"
		previous=
		continue
	fi
	optarg=$(expr "x$n" : 'x[^=]*=\(.*\)')
	case $n in
		-jdkhome | --jdkhome)		previous=jdk_DOT_dir;;
		-jdkhome=* | --jdkhome=*)	jdk_DOT_dir=$optarg;;

	-*)
		{
			echo "$0: Error: unrecognized option $n" >&2
			help
			{(exit 1); exit 1;};
		} ;;
	*=*)
		eval "$n"
	    ;;

	*)
	    {
		echo "$0: error: unrecognized param: $n." >&2
		help
		{ (exit 1); exit 1; };
	    } ;;
    esac
done

if test -n "$jdk_DOT_dir"; then
    jdk_DOT_dir=`echo $jdk_DOT_dir|sed -e 's|_DOT_|\.|g'`
    version=`echo $version|sed -e 's|_DOT_|\.|g'`
else
    if test -n "$JAVA_HOME"; then
      jdk_DOT_dir=$JAVA_HOME
    else
       which_java=`which java 2>/dev/null`
       if test -n "$which_java"; then
	   cd `dirname $which_java`/..
	   jdk_DOT_dir=$PWD
	   cd -
       else
	   {
	       echo "$0: error: JAVA_HOME not defined
   Try \`$0 --help' for more information." >&2
	       { (exit 1); exit 1; };
	   }
       fi
    fi
fi

if test -d $jdk_DOT_dir -a -x $jdk_DOT_dir/bin/java ; then
	prefix=${prefix:=/usr/local}
	prefix=$(echo $prefix | sed 's/_DOT_/./g')
fnameOld=/tmp/$$configure.properties.old
fname=/tmp/$$configure.properties
if
	[[ -r configure.properties ]]
then
cp -p ./configure.properties $fnameOld
sed "
/^[     ]*# Configured at/c\\
\# Configured at $(date)
/^[ 	]*jdk.dir=/c\\
jdk.dir=$jdk_DOT_dir
" $fnameOld >$fname

rm configure.properties
else
 touch $fnameOld
 touch $fname
fi

if
	! grep "^[ 	]*# Configured on" $fname >/dev/null
then
	echo "# Configured at "`date` >configure.properties
fi
sed  -e 's|_DOT_|\.|g' $fname >>configure.properties
(
	for pName in jdk.dir
	do
		value=$(echo $pName | sed "s/\./_DOT_/g")
		if
			 ! grep "^[      ]*$pName=" $fname >/dev/null
		then
			if
				eval [[ -n \$$value ]]
			then
				eval echo "$pName=\$$value"
			fi
		fi
	done
)|sed -e 's|_DOT_|\.|g'>>configure.properties
chmod a+r configure.properties
rm $fname $fnameOld

CLASSPATH=lib/ant.jar
CLASSPATH=$CLASSPATH:lib/ant-launcher.jar
CLASSPATH=$CLASSPATH:lib/xercesImpl.jar
CLASSPATH=$CLASSPATH:lib/xml-apis.jar
CLASSPATH=$CLASSPATH:$jdk_DOT_dir/lib/tools.jar
export CLASSPATH

JAVA_HOME=$jdk_DOT_dir
PATH=$jdk_DOT_dir/bin:$PATH
export PATH

chmod -R a+rw config
java org.apache.tools.ant.Main -v

else
    {
	echo "$0: error: JAVA_HOME=$jdk_DOT_dir not valid
Try \`$0 --help' for more information." >&2
	{ (exit 1); exit 1; };
    }
fi
