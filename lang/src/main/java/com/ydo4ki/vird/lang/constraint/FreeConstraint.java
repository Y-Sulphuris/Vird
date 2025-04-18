package com.ydo4ki.vird.lang.constraint;

import com.ydo4ki.vird.base.Constraint;
import com.ydo4ki.vird.lang.Scope;
import com.ydo4ki.vird.base.Val;

public final class FreeConstraint implements Constraint {
	public static final FreeConstraint INSTANCE = new FreeConstraint();
	
	private FreeConstraint() {}
	
	@Override
	public boolean test(Scope scope, Val value) {
		return true;
	}
	
	@Override
	public boolean implies(Scope scope, Constraint other) {
		return true;
	}
	
	@Override
	public String toString() {
		return "Free";
	}
}
