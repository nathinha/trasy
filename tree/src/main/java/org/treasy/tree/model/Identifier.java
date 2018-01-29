package org.treasy.tree.model;

public class Identifier {

    private Long id;

    public Identifier() {
    }

    public Identifier(Long id) {
	this.setId(id);
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }
}
