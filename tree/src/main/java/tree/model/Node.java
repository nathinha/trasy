package tree.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "TREE")
public class Node {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CODE")
    private String code;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PARENTID")
    private Long parentId;

    @Column(name = "DETAIL")
    private String detail;

    @Column(name = "CHILDREN")
    @OneToMany(targetEntity = Node.class, fetch = FetchType.EAGER)
    @JoinColumn(updatable = true)
    private List<Node> children;

    public Node() {
	this.children = new ArrayList<Node>();
    }

    public Node(String code, String description, Long parentId, String detail) {
	this.code = code;
	this.description = description;
	this.parentId = parentId;
	this.detail = detail;
	this.children = new ArrayList<Node>();
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getDetail() {
	return detail;
    }

    public void setDetail(String detail) {
	this.detail = detail;
    }

    public Long getParentId() {
	return parentId;
    }

    public void setParentId(Long parentId) {
	this.parentId = parentId;
    }

    public List<Node> getChildren() {
	return children;
    }

    public boolean hasChildren() {
	return !this.children.isEmpty();
    }
}