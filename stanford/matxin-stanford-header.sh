#java -cp .:matxin:matxin/stanford-corenlp-2012-04-09.jar:matxin/xom.jar:matxin/stanford-corenlp-2012-04-09-models.jar:matxin/joda-time.jar matxin.StanfordAnalyzer -chunkRules ../matxin-eng.chunk_rules.dat

$JAVA -cp $CLASSPATH matxin.StanfordAnalyzer $@
