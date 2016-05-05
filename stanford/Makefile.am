
SUBDIRS = matxin

bin_SCRIPTS = matxin-stanford

#java -cp .:matxin:matxin/stanford-corenlp-2012-04-09.jar:matxin/xom.jar:matxin/stanford-corenlp-2012-04-09-models.jar:matxin/joda-time.jar matxin.StanfordAnalyzer -chunkRules ../matxin-eng.chunk_rules.dat

jardir=$(prefix)/share/java

matxin-stanford: Makefile.am matxin-stanford-header.sh
	@echo "Creating matxin-stanford script"
	@echo "#!$(BASH)" > $@
	@echo "JAVA=$(JAVA)" >> $@
	@echo "CLASSPATH=$(prefix)/share/matxin/matxin-eng/:$(jardir)/stanford-corenlp-2012-04-09.jar:$(jardir)/xom.jar:$(jardir)/stanford-corenlp-2012-04-09-models.jar:$(jardir)/joda-time.jar" >> $@
	@cat matxin-stanford-header.sh >> $@
	@chmod a+x $@
