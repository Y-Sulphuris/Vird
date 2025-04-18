package com.ydo4ki.vird.lang.constraint;

import com.ydo4ki.vird.base.Constraint;
import com.ydo4ki.vird.lang.Scope;
import com.ydo4ki.vird.base.Val;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class OrConstraint implements Constraint {
	private final Set<Constraint> constraints;
	
	public static OrConstraint of(Constraint... constraints) {
		return new OrConstraint(Arrays.stream(constraints).collect(Collectors.toSet()));
	}
	
	@Override
	public boolean test(Scope scope, Val value) {
		return constraints.stream().anyMatch(c -> c.test(scope, value));
	}
	
	@Override
	public boolean implies(Scope scope, Constraint other) {
		return constraints.stream().allMatch(c -> c.implies(scope, other));
	}
	
	@Override
	public String toString() {
		return "Or" + constraints;
	}
}
