package com.ydo4ki.brougham.lang;

import com.ydo4ki.brougham.Location;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sulphuris
 * @since 4/8/2025 8:24 PM
 */
public final class DList implements Val {
	private Location location;
	private final DList parent;
	private final BracketsType bracketsType;
	private final List<Val> elements;
	
	private final Map<String, Val> definedSymbols = new HashMap<>();
	
	public DList(DList parent, BracketsType bracketsType, List<Val> elements) {
		this.location = new Location(null, 0, 0);
		this.parent = parent;
		this.bracketsType = bracketsType;
		this.elements = elements;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public DList getParent() {
		return parent;
	}
	
	public Val resolve(Symbol symbol) {
		Val dereferenced = definedSymbols.get(symbol.getValue());
		if (dereferenced != null) return dereferenced;
		return parent == null ? null : parent.resolve(symbol);
	}
	public FunctionSet resolveFunction(String name) {
		Val dereferenced = definedSymbols.get(name);
		if (dereferenced instanceof FunctionSet) return (FunctionSet)dereferenced;
		return parent == null ? null : parent.resolveFunction(name);
	}
	
	private FunctionSet resolveFunctionNoParents(String name) {
		Val dereferenced = definedSymbols.get(name);
		if (dereferenced instanceof FunctionSet) return (FunctionSet)dereferenced;
		return null;
	}
	public FunctionCall resolveFunctionImpl(String name, TypeRef returnType, Val[] args) {
		return resolveFunctionImpl(name, returnType, Arrays.stream(args).map(Val::getType).toArray(TypeRef[]::new));
	}
	public FunctionCall resolveFunctionImpl(String name, TypeRef returnType, TypeRef[] argTypes) {
//		System.out.println("## Resolving: " + name + Arrays.toString(argTypes) + " -> " + returnType);
		DList caller = this;
		while (true) {
			FunctionSet functionSet = caller.resolveFunctionNoParents(name);
			if (functionSet != null) {
				FunctionCall call = functionSet.findImplForArgs(caller, returnType, argTypes);
				if (call != null) return call;
			}
			caller = caller.parent;
			if (caller == null) return null;
		}
	}

//	public FunctionImpl resolveFunctionExact(Symbol symbol, FunctionType type) {
//		Val dereferenced = definedSymbols.get(symbol.getValue());
//		if (dereferenced instanceof FunctionSet) {
//			FunctionSet set = (FunctionSet) dereferenced;
//			FunctionImpl exact = set.findImplByType(type);
//			if (exact != null) return exact;
//		}
//		return parent == null ? null : parent.resolveFunctionExact(symbol, type);
//	}
	
	public void defineFunction(String name, FunctionImpl... function) {
		FunctionSet set = resolveFunction(name);
		if (set == null) {
			set = new FunctionSet(function);
			define(name, set);
		} else {
			for (FunctionImpl f : function) {
				set.addImpl(f);
			}
		}
	}
	public void define(String name, Val value) {
		if (definedSymbols.containsKey(name))
			throw new IllegalArgumentException(name + " is already defined");
		if (name.isEmpty() && value instanceof FunctionSet) ((FunctionSet)value).cast = true;
		definedSymbols.put(name, value);
	}
	
	public List<Val> getElements() {
		return elements;
	}
	
	public BracketsType getBracketsType() {
		return bracketsType;
	}
	
	@Override
	public Type getRawType() {
		return DListType.of(bracketsType);
	}
	
	@Override
	public String toString() {
		return "DList"+bracketsType.open + elements.stream().map(Val::toString).collect(Collectors.joining(" "))+bracketsType.close;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		DList dList = (DList) o;
		return bracketsType == dList.bracketsType && Objects.equals(elements, dList.elements);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(bracketsType, elements);
	}
}
