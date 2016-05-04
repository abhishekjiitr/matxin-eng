#                                               -*- Autoconf -*-
# Process this file with autoconf to produce a configure script.

AC_PREREQ(2.59)
AC_INIT([matxin-eng], [1.5.0])
PACKAGE=matxin-eng

GENERIC_MAJOR_VERSION=1
GENERIC_MINOR_VERSION=5
GENERIC_MICRO_VERSION=0

GENERIC_RELEASE=$GENERIC_MAJOR_VERSION.$GENERIC_MINOR_VERSION
GENERIC_RELEASE=$GENERIC_MAJOR_VERSION.$GENERIC_MINOR_VERSION.$GENERIC_MICRO_VERSION
AC_SUBST(GENERIC_RELEASE)
AC_SUBST(GENERIC_VERSION)


m4_define([required_lttoolbox_version], [3.2.0])
m4_define([required_matxin_version], [1.4.0])

AC_CONFIG_FILES([
                 Makefile
		 matxin-eng.pc
		])

AC_OUTPUT

#
# Print summary for the selected configuration
#
echo
echo "**  $PACKAGE_NAME $PACKAGE_VERSION - selected build configuration  **"
echo
echo "Install prefix ................... : $prefix"
echo
echo "Build architecture ............... : $ARCH"
echo "Operating System ................. : $OSTYPE"
echo
echo "Now type 'make', followed by 'make install' as root."
echo "If there are any problems, read the INSTALL file."
echo


