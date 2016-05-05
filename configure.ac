#                                               -*- Autoconf -*-
# Process this file with autoconf to produce a configure script.

AC_PREREQ(2.59)
AC_INIT([matxin-eng], [1.5.0])
PACKAGE=matxin-eng

AC_CONFIG_MACRO_DIRS([m4])

GENERIC_MAJOR_VERSION=1
GENERIC_MINOR_VERSION=5
GENERIC_MICRO_VERSION=0

GENERIC_RELEASE=$GENERIC_MAJOR_VERSION.$GENERIC_MINOR_VERSION
GENERIC_RELEASE=$GENERIC_MAJOR_VERSION.$GENERIC_MINOR_VERSION.$GENERIC_MICRO_VERSION
AC_SUBST(GENERIC_RELEASE)
AC_SUBST(GENERIC_VERSION)

AM_INIT_AUTOMAKE


m4_define([required_lttoolbox_version], [3.2.0])
m4_define([required_matxin_version], [1.4.0])

AC_PATH_PROG(BASH, bash, no)
if test x$ac_cv_path_BASH = x
then
  AC_MSG_ERROR([bash not found. You may want to adjust your PATH.])
fi
if test x$ac_cv_path_BASH = xno
then
  AC_MSG_ERROR([bash not found. You may want to adjust your PATH.])
fi

AC_CHECK_FILE(stanford/matxin/stanford-corenlp-2012-04-09.jar, HAS_CORENLP=1, HAS_CORENLP=0)
AC_CHECK_FILE(stanford/matxin/stanford-corenlp-2012-04-09-models.jar, HAS_CORENLP=1, HAS_CORENLP=0)

if test $HAS_CORENLP = 0
then
  AC_MSG_ERROR([You have not got stanford-corenlp-2012-04-09.jar.
                Try running: scripts/download-corenlp.sh
               ])
  
fi

AX_PROG_JAVAC
AX_PROG_JAVA

PKG_CHECK_MODULES(MATXIN, [dnl
 lttoolbox >= required_lttoolbox_version dnl
 matxin >= required_matxin_version], CPPFLAGS="$CPPFLAGS $MATXIN_CPPFLAGS $MATXIN_CFLAGS"; LIBS="$LIBS")



AP_MKINCLUDE

AC_CONFIG_FILES([
                 Makefile
                 stanford/Makefile
                 stanford/matxin/Makefile
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


