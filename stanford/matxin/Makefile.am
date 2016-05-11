
#java -cp .:matxin:matxin/stanford-corenlp-2012-04-09.jar:matxin/xom.jar:matxin/stanford-corenlp-2012-04-09-models.jar:matxin/joda-time.jar matxin.StanfordAnalyzer -chunkRules ../matxin-eng.chunk_rules.dat

#libxom-java

all: stanford-corenlp-2012-04-09.jar  StanfordAnalyzer.class ChunkRule.class
	javac -cp stanford-corenlp-2012-04-09.jar StanfordAnalyzer.java ChunkRule.java

StanfordAnalyzer.class: stanford-corenlp-2012-04-09.jar ChunkRule.class
	javac -cp stanford-corenlp-2012-04-09.jar StanfordAnalyzer.java ChunkRule.java

ChunkRule.class: stanford-corenlp-2012-04-09.jar 
	javac -cp stanford-corenlp-2012-04-09.jar ChunkRule.java

matxinengdir=$(prefix)/share/matxin/matxin-eng/matxin

javadir=$(prefix)/share/java

java_DATA=joda-time.jar \
	stanford-corenlp-2012-04-09.jar \
	stanford-corenlp-2012-04-09-models.jar \
	xom.jar

matxineng_DATA=StanfordAnalyzer.class ChunkRule.class
