package com.ydo4ki.vird.base;

import com.ydo4ki.vird.lang.Scope;
import com.ydo4ki.vird.lang.constraint.AndConstraint;
import com.ydo4ki.vird.lang.constraint.FreeConstraint;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Sulphuris
 * @since 4/9/2025 12:26 PM
 */
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class TypeRef implements Val {
	private final Type baseType;
	private final boolean vararg;
	private final Constraint constraint;
	
	TypeRef(Type baseType, boolean vararg) {
		this(baseType, vararg, FreeConstraint.INSTANCE);
	}
	
	public TypeRef also(Constraint constraints) {
		return new TypeRef(baseType, vararg, AndConstraint.of(this.constraint, constraints));
	}
	
	public boolean matches(Scope scope, Val val) {
		if (!val.getRawType().equals(baseType)) return false;
		return constraint.test(scope, val);
	}
	public boolean isCompatibleWith(Scope scope, TypeRef other) {
		if (other == null) return true;
		if (!this.baseType.equals(other.baseType)) return false;
		return this.constraint.implies(scope, other.constraint);
	}
	
	@Override
	public String toString() {
		String str = baseType.toString();
		if (vararg) str += "...";
		if (constraint != FreeConstraint.INSTANCE) str += " requires " + constraint;
		return str;
	}
	
	public static final Type TYPE = new Type() {
		@Override
		public String toString() {
			return "TypeRef";
		}
	};
	
	@Override
	public Type getRawType() {
		return TYPE;
	}
}
