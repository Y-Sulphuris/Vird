package com.ydo4ki.vird.lang.constraint;

import com.ydo4ki.vird.base.ExprList;
import com.ydo4ki.vird.base.Val;
import com.ydo4ki.vird.lang.LangValidationException;
import com.ydo4ki.vird.lang.Scope;
import com.ydo4ki.vird.lang.ValidatedValCall;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
public final class EqualityConstraint implements Constraint {
	@NonNull
	private final Val expected;
	
	@Override
	public boolean test(Scope scope, Val value) {
		return value.equals(expected);
	}
	
	@Override
	public boolean implies(Scope scope, Constraint other) {
		return other.test(scope, expected);
	}
	
	@Override
	public ValidatedValCall getInvocationConstraint(Scope scope, ExprList f) throws LangValidationException {
		return expected.invocation(scope, f);
	}
	
	@Override
	public String toString() {
		return "Equals(" + expected + ")";
	}
	
}
