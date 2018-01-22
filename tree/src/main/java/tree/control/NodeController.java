package tree.control;

import java.security.InvalidParameterException;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;

import tree.model.Node;
import tree.util.HibernateUtil;

public class NodeController {

    private boolean hasRoot() {
	return !getRoot().isEmpty();
    }

    private void addChild(Long parentId, Long childId) {
	Session session = HibernateUtil.getSessionFactory().openSession();
	Transaction tx = null;
	try {
	    Node child = session.get(Node.class, childId);
	    Node parent = session.get(Node.class, parentId);

	    parent.getChildren().add(child);
	    tx = session.beginTransaction();
	    session.update(parent);
	    tx.commit();
	} catch (Exception e) {
	    if (tx != null) {
		tx.rollback();
	    }
	    throw e;
	} finally {
	    session.close();
	}
    }

    private void removeChild(Long parentId, Long childId) {
	Session session = HibernateUtil.getSessionFactory().openSession();
	Transaction tx = null;
	try {
	    Node child = session.get(Node.class, childId);
	    Node parent = session.get(Node.class, parentId);

	    parent.getChildren().remove(child);
	    tx = session.beginTransaction();
	    session.update(parent);
	    tx.commit();
	} catch (Exception e) {
	    if (tx != null) {
		tx.rollback();
	    }
	    throw e;
	} finally {
	    session.close();
	}
    }

    public Long addNode(String code, String description, Long parentId, String detail) {
	Long nodeId = null;

	Session session = HibernateUtil.getSessionFactory().openSession();
	Transaction tx = null;

	Node node = new Node(code, description, parentId, detail);

	try {
	    if (parentId == null && hasRoot()) {
		throw new InvalidParameterException("Root node already exists");
	    }

	    tx = session.beginTransaction();
	    nodeId = (Long) session.save(node);
	    tx.commit();

	} catch (Exception e) {
	    if (tx != null) {
		tx.rollback();
	    }
	    throw e;
	} finally {
	    session.close();
	}

	if (parentId != null) {
	    addChild(parentId, nodeId);
	}

	return nodeId;
    }

    public Long updateNode(Long id, String code, String description, Long parentId, String detail) {
	Long childId = null;

	Session session = HibernateUtil.getSessionFactory().openSession();
	Transaction tx = null;

	if (id == null) {
	    throw new InvalidParameterException("Need node ID to update it");
	}

	Node node = session.get(Node.class, id);

	if (code != null) {
	    node.setCode(code);
	}

	if (description != null) {
	    node.setDescription(description);
	}

	if (parentId != null) {
	    node.setParentId(parentId);
	}

	if (detail != null) {
	    node.setDetail(detail);
	}

	try {
	    tx = session.beginTransaction();
	    childId = (Long) session.save(node);
	    tx.commit();
	} catch (Exception e) {
	    if (tx != null) {
		tx.rollback();
	    }
	    throw e;
	} finally {
	    session.close();
	}

	if (parentId != null) {
	    Long oldParentId = node.getParentId();

	    removeChild(oldParentId, childId);
	    addChild(parentId, childId);
	}

	return childId;
    }

    public List<Node> getRoot() {
	Session session = HibernateUtil.getSessionFactory().openSession();

	CriteriaBuilder builder = session.getCriteriaBuilder();
	CriteriaQuery<Node> query = builder.createQuery(Node.class);

	Root<Node> nodeRoot = query.from(Node.class);
	query.select(nodeRoot).where(builder.isNull(nodeRoot.get("parentId")));
	List<Node> root = session.createQuery(query).getResultList();

	session.close();

	return root;
    }

    public List<Node> getChildren(Long id) {
	Session session = HibernateUtil.getSessionFactory().openSession();
	Node node = session.get(Node.class, id);
	session.close();

	return node.getChildren();
    }
}
