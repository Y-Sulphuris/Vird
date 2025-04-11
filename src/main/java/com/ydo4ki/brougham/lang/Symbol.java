package com.ydo4ki.brougham.lang;

import com.ydo4ki.brougham.Location;
import com.ydo4ki.brougham.Source;

import java.util.Objects;

/**
 * @since 4/7/2025 10:33 PM
 * @author Sulphuris
 */
public final class Symbol implements Val {
	private final Location location;
	private final DList parent;
	private final String value;
	
	public Symbol(Location location, DList parent, String value) {
		this.location = location;
		this.parent = parent;
		this.value = value;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public DList getParent() {
		return parent;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public Type getRawType() {
		return SymbolType.instance;
	}
	
	@Override
	public String toString() {
		return value;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		Symbol symbol = (Symbol) o;
		return Objects.equals(value, symbol.value);
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}
