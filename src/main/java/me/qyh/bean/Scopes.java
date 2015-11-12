package me.qyh.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.qyh.entity.Scope;
import me.qyh.utils.Validators;

public class Scopes {

	public static final Scopes ALL = new Scopes();
	public static final Scopes PUBLIC = new Scopes(Scope.PUBLIC);

	private Set<Scope> scopes = new HashSet<Scope>();

	public void removeScope(Scope scope) {
		scopes.remove(scope);
	}

	public void addScope(Scope scope) {
		scopes.add(scope);
	}

	public Set<Scope> getScopes() {
		return scopes;
	}

	public void setScopes(Set<Scope> scopes) {
		this.scopes = scopes;
	}

	public List<Scope> toList() {
		return new ArrayList<Scope>(scopes);
	}

	public Scopes(Scope... scopes) {
		Collections.addAll(this.scopes, scopes);
	}

	public boolean hasScope(Scope scope) {
		if (this.scopes.isEmpty()) {
			return true;
		}
		return scopes.contains(scope);
	}

	public boolean isEmpty() {
		return Validators.isEmptyOrNull(scopes);
	}

	public Scopes() {
	}

	public boolean contains(Scopes scopes) {
		if (scopes == null) {
			return false;
		}
		if (scopes.scopes.isEmpty() && this.scopes.isEmpty()) {
			return true;
		}
		for (Scope scope : scopes.scopes) {
			if (!hasScope(scope)) {
				return false;
			}
		}
		return true;
	}
}
