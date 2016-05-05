LANG1=eng
BASENAME=matxin-eng

@ap_include@ 

SUBDIRS = stanford

$(LANG1).chunk_rules.dat: $(BASENAME).chunk_rules.dat
	cp $< $@

matxinengdir = $(prefix)/share/matxin/matxin-eng

matxineng_DATA = eng.chunk_rules.dat matxin-eng.semantic.dat

EXTRA_DIST = modes.xml

noinst_DATA=modes/$(LANG1)-analyser.mode
