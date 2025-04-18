package com.ydo4ki.vird.base;

import com.ydo4ki.vird.lang.constraint.AndConstraint;
import com.ydo4ki.vird.lang.constraint.DListBracketsConstraint;
import com.ydo4ki.vird.lang.constraint.InstanceOfConstraint;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sulphuris
 * @since 4/8/2025 8:24 PM
 */
@Getter
public final class ExprList extends Expr implements Iterable<Expr> {
	
	public static TypeRef TYPE(BracketsType type) {
		return TYPE.ref(
				AndConstraint.of(
						new InstanceOfConstraint(ExprList.class),
						new DListBracketsConstraint(type)
				)
		);
	}
	
	private final BracketsType bracketsType;
	private final List<Expr> elements;
	
	public ExprList(Location location, BracketsType bracketsType, List<Expr> elements) {
		super(location);
		this.bracketsType = bracketsType;
		this.elements = elements;
	}
	
	public List<Expr> getElements() {
		return new ArrayList<>(elements);
	}
	
	public int elementsCount() {
		return elements.size();
	}
	
	public Expr getElement(int index) {
		return elements.get(index);
	}
	
	@Override
	public String toString() {
		return bracketsType.open + elements.stream().map(Val::toString).collect(Collectors.joining(" ")) + bracketsType.close;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		ExprList exprList = (ExprList) o;
		return bracketsType == exprList.bracketsType && Objects.equals(elements, exprList.elements);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(bracketsType, elements);
	}
	
	@Override
	public Iterator<Expr> iterator() {
		return elements.iterator();
	}
}
