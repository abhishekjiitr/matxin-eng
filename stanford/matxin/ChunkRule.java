package matxin;

import java.io.*;
import java.util.*;

public class ChunkRule {
    private String head="";
    private String child="";
    private String rel="";
    private String type="";
    
    public ChunkRule(String line) {
	String[] elements= line.split("\t");
	
	if (elements.length == 4) {
	    type=elements[0];
	    head=elements[1];
	    child=elements[2];
	    rel=elements[3];
	}
	else
	    System.err.println("Formatu ezegokia: '" + line + "'");
    }
    
    public ChunkRule(String headText, String childText, String relText, String typeText) {
	head=headText;
	child=childText;
	rel=relText;
	type=typeText;
    }
    
    public String apply(String head_text, String child_text, String rel_text) {
	if (head_text.matches(this.head) && child_text.matches(this.child) && rel_text.matches(this.rel))
	    return this.type;
	else
	    return "";
    }
}
