LANG1=eng
BASENAME=matxin-eng

@ap_include@ 

SUBDIRS = stanford

TARGETS_COMMON=$(LANG1).morph_gen.bin \
	       $(LANG1).intrachunk_order.dat 
	       $(LANG1).chunk_rules.dat 

$(LANG1).morph_gen.bin: $(BASENAME).morph_gen_mur.xml 
	lt-comp rl $< $@

$(LANG1).intrachunk_order.dat: $(BASENAME).intrachunk_order.dat
	cp $< $@

$(LANG1).chunk_rules.dat: $(BASENAME).chunk_rules.dat
	cp $< $@

matxinengdir = $(prefix)/share/matxin/matxin-eng

matxineng_DATA = $(TARGETS_COMMON) matxin-eng.semantic.dat

EXTRA_DIST = modes.xml

noinst_DATA=modes/$(LANG1)-analyser.mode
