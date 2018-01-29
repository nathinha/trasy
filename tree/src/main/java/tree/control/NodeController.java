package tree.control;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tree.model.Node;
import tree.util.HibernateUtil;

@RestController
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

    @RequestMapping(method = RequestMethod.POST, path = "/node")
    public Long addNode(@RequestParam(value = "code", required = false) String code, //
	    @RequestParam(value = "description", required = false) String description, //
	    @RequestParam(value = "parentId", required = false) Long parentId, //
	    @RequestParam(value = "detail", required = false) String detail) {

	Long id = null;

	Session session = HibernateUtil.getSessionFactory().openSession();
	Transaction tx = null;

	Node node = new Node(code, description, parentId, detail);

	try {
	    if (parentId == null && hasRoot()) {
		return id;
	    }

	    tx = session.beginTransaction();
	    id = (Long) session.save(node);
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
	    addChild(parentId, id);
	}

	return id;
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/node")
    public Long updateNode(@RequestParam(value = "id") Long id, //
	    @RequestParam(value = "code", required = false) String code, //
	    @RequestParam(value = "description", required = false) String description, //
	    @RequestParam(value = "parentId", required = false) Long parentId, //
	    @RequestParam(value = "detail", required = false) String detail) {

	Long childId = null;

	Session session = HibernateUtil.getSessionFactory().openSession();
	Transaction tx = null;

	if (id == null) {
	    return childId;
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

    @RequestMapping(method = RequestMethod.GET, path = "/node")
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

    @RequestMapping(method = RequestMethod.GET, path = "/node/{id}")
    public List<Node> getChildren(@PathVariable Long id) {
	Session session = HibernateUtil.getSessionFactory().openSession();
	Node node = session.get(Node.class, id);
	session.close();

	return node.getChildren();
    }
}
