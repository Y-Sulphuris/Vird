package com.ydo4ki.vird.ast.lexer;

import com.ydo4ki.vird.ast.Location;

import java.io.File;

public class Token {
	public final TokenType type;
	public final String text;
	public final Location location;

	public Token(TokenType type, String text, int startpos, int endpos, int line, File file, String source) {
		this.type = type;
		this.text = text;
		this.location = new Location(startpos,endpos,line,line,file,source);
	}
	public Token(TokenType type,int startpos,int endpos, int line, File file, String source) {
		this(type,"\0",startpos,endpos,line,file,source);
	}

	@Override
	public String toString() {
		return type + "(" +
			(text != null ?"'" + text + '\'' : "")+
			", startpos=" + location.getStartPos() +
			", endpos=" + location.getEndPos() +
			", line=" + location.getStartLine() +
			')';
	}
}
