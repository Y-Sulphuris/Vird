package com.ydo4ki.brougham;

import com.ydo4ki.brougham.lang.*;
import com.ydo4ki.brougham.lib.Std;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Sulphuris
 * @since 4/10/2025 4:04 PM
 */
@Getter
public class Interpreter {
	private final Scope program = new Scope(null);
	
	public Interpreter() {
		Std.setup(program);
	}
	
	public Val next(String in) throws IOException {
		return evaluate(program, null, new Parser().read(program, new Source.OfString(in)));
	}
	
	public Val next(BufferedReader in) throws IOException {
		return evaluate(program, null, new Parser().read(program, new Source.Raw(in)));
	}
	
	public Val next(Source in) throws IOException {
		Val parsed = new Parser().read(program, in);
		if (parsed == null) throw new EOFException();
		return evaluate(program, null, parsed);
	}
	
	public static Val evaluate(Scope caller, TypeRef expectedType, Val val) {
		Objects.requireNonNull(val, "sho?");
		if (expectedType != null && expectedType.matches(caller, val)) return val;
		if (val instanceof DList)
			return Objects.requireNonNull(
					evaluate_function(caller, expectedType, (DList) val),
					"Cannot evaluate function: " + val
			);
		if (val instanceof Symbol)
			return Objects.requireNonNull(
					resolve((Symbol) val),
					"Cannot resolve symbol: " + val
			);
		if (expectedType != null) {
			// maybe find a cast function... idk
		}
		return val;
	}
	
	public static FunctionCall get_function_call(Scope caller, TypeRef expectedType, DList f) {
		Val functionId = f.getElements().get(0);
		Val function = evaluate(caller, null, functionId);
		if (!(function instanceof FunctionSet)) {
			f.getLocation().print(System.err);
			throw new ThisIsNotTheBookClubException("Function not found: " + functionId);
		}
		FunctionSet func = ((FunctionSet) function);
		
		final Val[] args;
		{
			List<Val> args0 = new ArrayList<>(f.getElements());
			args0.remove(0);
			args = args0.toArray(new Val[0]);
		}
		FunctionCall call = func.makeCall(f, expectedType, args);
		if (call == null) {
			f.getLocation().print(System.err);
			throw new IllegalArgumentException("Function not found: " + functionId +
					Arrays.stream(args).map(Val::getType).collect(Collectors.toList()) +
					" (for " + Arrays.toString(args) + ")");
		}
		return call;
	}
	
	private static Val evaluate_function(Scope caller, TypeRef expectedType, DList f) {
		final Val[] args;
		{
			List<Val> args0 = new ArrayList<>(f.getElements());
			args0.remove(0);
			args = args0.toArray(new Val[0]);
		}
		return get_function_call(caller, expectedType, f).invoke(f, args);
	}
	
	private static Val resolve(Symbol name) {
		return name.getParent().resolve(name.getValue());
	}
	
	private FunctionCall function_call(Scope caller, FunctionImpl function, TypeRef expectedType, Val[] args) {
		FunctionCall call = FunctionCall.makeCall(caller, function, expectedType, Arrays.stream(args).map(Val::getType).toArray(TypeRef[]::new));
		if (call == null) {
			throw new IllegalArgumentException("Function not found: " + Arrays.stream(args).map(Val::getType).collect(Collectors.toList()));
		}
		return call;
	}
}
