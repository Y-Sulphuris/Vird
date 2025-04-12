package com.ydo4ki.brougham.lang;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sulphuris
 * @since 4/8/2025 1:13 PM
 */
public class FunctionSetImpl implements FunctionSet {
	private final Set<FunctionImpl> specificFunctions = new HashSet<>();
	
	public FunctionSetImpl(FunctionImpl... impl) {
		for (FunctionImpl function : impl) {
			addFunction(function);
		}
	}
	
	@Override
	public Type getRawType() {
		return FunctionSetType.instance;
	}
	
	public void addFunction(FunctionImpl function) {
		if (findImplByType(function.getRawType()) != null)
			throw new IllegalArgumentException("This types of arguments are already occupied " +
					Arrays.toString(function.getRawType().getParams()));
		specificFunctions.add(function);
	}
	
	public FunctionImpl findImplByType(FunctionType type) {
		for (FunctionImpl function : specificFunctions) {
			if (function.getRawType().equals(type)) return function;
		}
		return null;
	}
	
	public FunctionCall makeCall(Scope caller, TypeRef expectedType, TypeRef[] argsTypes) {
		return makeCall(specificFunctions, caller, expectedType, argsTypes);
	}
	
	
	private static FunctionCall makeCall(Collection<FunctionImpl> specificFunctions, Scope caller, TypeRef expectedType, TypeRef[] argsTypes) {
		
		List<FunctionCall> candidates = new ArrayList<>();
		List<FunctionImpl> potentialTemplates = new ArrayList<>();
		
		search:
		for (FunctionImpl function : specificFunctions) {
			
			FunctionCall call = FunctionCall.makeCall(caller, function, expectedType, argsTypes);
			if (call == null) {
				if (function.isTemplate())
					potentialTemplates.add(function);
				continue search;
			}
			if (call.isExactMatch()) return call;
			else candidates.add(call);
		}
		int minCasts = Integer.MAX_VALUE;
		for (FunctionCall candidate : candidates) {
			if (candidate.getCastsCount() < minCasts)
				minCasts = candidate.getCastsCount();
		}
		final int MinCasts = minCasts;
		candidates.removeIf(f -> f.getCastsCount() > MinCasts);
		List<FunctionCall> resultIsExact = candidates.stream()
				.filter(f -> !f.needsResultCast())
				.collect(Collectors.toList());
		if (resultIsExact.size() == 1) {
			return resultIsExact.get(0);
		}
		if (resultIsExact.isEmpty()) {
			if (candidates.size() == 1) {
				return candidates.get(0);
			}
			if (candidates.isEmpty()) {
				if (potentialTemplates.isEmpty()) return null;
				TypeRef[] typesOfArgsTypes = Arrays.stream(argsTypes).map(t -> t.getBaseType().getType()).toArray(TypeRef[]::new);
				FunctionCall templateCall = makeCall(potentialTemplates, caller, null, typesOfArgsTypes);
				if (templateCall == null) return null;
				Val[] rawTypes = Arrays.stream(argsTypes).map(TypeRef::getBaseType).toArray(Val[]::new);
				FunctionImpl toInvoke = (FunctionImpl)templateCall.invoke(caller, rawTypes);
				return FunctionCall.makeCall(caller, toInvoke, expectedType, argsTypes);
			}
			throw new IllegalArgumentException("Ambiguous call: " + candidates);
		}
		throw new IllegalArgumentException("Ambiguous call: " + resultIsExact);
	}
}

