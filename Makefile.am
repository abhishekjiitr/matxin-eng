LANG1=eng
BASENAME=matxin-eng

@ap_include@ 

SUBDIRS = stanford

TARGETS_COMMON=$(LANG1).autogen.bin \
	       $(LANG1).chunk_rules.dat \
	       $(LANG1).gnx.bin

#
#	ANALYSIS
#

$(LANG1).chunk_rules.dat: $(BASENAME.chunk_rules.dat
	cp $< $@

#
#	GENERATION
#

$(LANG1).autogen.bin: $(BASENAME).$(LANG1).dix
	lt-comp rl $< $@

$(LANG1).gnx.bin: $(BASENAME).$(LANG1).gnx
	matxin-preprocess-generate $< $@

matxinengdir = $(prefix)/share/matxin/matxin-eng

matxineng_DATA = $(TARGETS_COMMON) $(BASENAME).chunk_rules.dat

EXTRA_DIST = modes.xml

noinst_DATA=modes/$(LANG1)-analyser.mode
