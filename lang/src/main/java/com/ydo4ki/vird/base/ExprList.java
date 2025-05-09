package com.ydo4ki.vird.base;

import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sulphuris
 * @since 4/8/2025 8:24 PM
 */
@Getter
public abstract class ExprList extends Expr implements Iterable<Expr> {
	
	private final List<Expr> elements;
	
	ExprList(Location location, List<Expr> elements) {
		super(location);
		this.elements = elements;
	}
	
	public static ExprList of(Location location, BracketsType bracketsType, List<Expr> elements) {
		switch (bracketsType) {
			case ROUND:
				return new Round(location, elements);
			case BRACES:
				return new Braces(location, elements);
			case SQUARE:
				return new Square(location, elements);
		}
		throw new NullPointerException("bracketsType is null");
	}
	
	public static final class Round extends ExprList {
		Round(Location location, List<Expr> elements) {
			super(location, elements);
		}
		
		@Override
		public BracketsType getBracketsType() {
			return BracketsType.ROUND;
		}
	}
	public static final class Square extends ExprList {
		Square(Location location, List<Expr> elements) {
			super(location, elements);
		}
		
		@Override
		public BracketsType getBracketsType() {
			return BracketsType.SQUARE;
		}
	}
	public static final class Braces extends ExprList {
		Braces(Location location, List<Expr> elements) {
			super(location, elements);
		}
		
		@Override
		public BracketsType getBracketsType() {
			return BracketsType.BRACES;
		}
	}
	
	public abstract BracketsType getBracketsType();
	
	public List<Expr> getElements() {
		return new ArrayList<>(elements);
	}
	
	public int size() {
		return elements.size();
	}
	
	public Expr get(int index) {
		return elements.get(index);
	}
	
	@Override
	public String toString() {
		return getBracketsType().open + elements.stream().map(Val::toString).collect(Collectors.joining(" ")) + getBracketsType().close;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		ExprList exprList = (ExprList) o;
		return Objects.equals(elements, exprList.elements);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getBracketsType(), elements);
	}
	
	@Override
	public Iterator<Expr> iterator() {
		return elements.iterator();
	}
}
