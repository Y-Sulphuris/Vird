package com.ydo4ki.brougham.lang;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Sulphuris
 * @since 4/9/2025 9:38 AM
 */
@Getter
public final class FunctionCall {
	private final FunctionImpl function;
	private final ConversionRule cast_result;
	private final ConversionRule[] implicit_cast_calls;
	private final int castsCount;
	
	public FunctionCall(ConcreteFunction function, ConversionRule castResult, ConversionRule[] implicitCasts) {
		this.function = function.asFunctionImpl();
		cast_result = castResult;
		implicit_cast_calls = implicitCasts;
		int casts = 0;
		for (ConversionRule implicitCast : implicitCasts) {
			if (implicitCast != null) casts++;
		}
		this.castsCount = casts;
	}
	
	public static FunctionCall makeCall(Scope caller, ConcreteFunction function, TypeRef expectedType, TypeRef[] argsTypes) {
		ConversionRule return_type_cast = null;
		TypeRef returnType = function.getRawType().getReturnType();
		if (expectedType != null) {
			if (returnType != null && !returnType.isCompatibleWith(caller, expectedType)) {
//				if (amIaCastFunction) return null;
				ConversionRule cast = caller.resolveConversionRule(new ConversionRule.ConversionTypes(expectedType, returnType));
				if (cast == null) return null;
				return_type_cast = cast;
			}
		}
		
		TypeRef[] params = function.getRawType().getParams();
		ConversionRule[] casts = new ConversionRule[argsTypes.length];
		for (int i = 0; i < argsTypes.length; i++) {
			TypeRef param;
			if (i >= params.length && function.getRawType().isVarargFunction()) {
				param = params[params.length-1];
			} else {
				param = params[i];
			}
			if (param.isCompatibleWith(caller, argsTypes[i])) continue; // cast = null (not needed)
			else {
				ConversionRule cast;
				TypeRef neededReturnType = param;
				TypeRef inputWeHave = argsTypes[i];
				if (/*amIaCastFunction &&*/ Objects.equals(neededReturnType, expectedType)) {
					cast = null;
				} else {
					cast = caller.resolveConversionRule(new ConversionRule.ConversionTypes(neededReturnType, inputWeHave));
				}
				if (cast == null) return null;
				casts[i] = cast;
			}
		}
		return new FunctionCall(function, return_type_cast, casts);
	}
	
	public boolean needsResultCast() {
		return cast_result != null;
	}
	
	public TypeRef getReturnType() {
		if (needsResultCast()) return cast_result.getReturnType();
		return function.getRawType().getReturnType();
	}
	
	public Val invoke(Scope caller, Val[] args) {
		for (int i = 0; i < args.length; i++) {
			ConversionRule cast = implicit_cast_calls[i];
			if (cast == null) continue;
			args[i] = cast.invoke(caller, args[i]);
		}
		Val result = function.invoke(caller, args);
		if (cast_result != null) result = cast_result.invoke(caller, result);
		return result;
	}
	
	@Override
	public String toString() {
		return "FunctionCall{" +
				"function=" + function +
				", cast_result=" + cast_result +
				", implicit_cast_calls=" + Arrays.toString(implicit_cast_calls) +
				'}';
	}
	
	public boolean isExactMatch() {
		return castsCount == 0 && cast_result == null;
	}
}
