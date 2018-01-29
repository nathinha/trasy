package org.treasy.tree.control;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hibernate.Session;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.treasy.tree.model.Identifier;
import org.treasy.tree.model.Node;
import org.treasy.tree.util.HibernateUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NodeControllerTests {

    private static NodeController ctrl;

    @BeforeClass
    public static void setUp() throws Exception {

	ctrl = new NodeController();
    }

    @AfterClass
    public static void tearDown() throws Exception {

	Session session = HibernateUtil.getSessionFactory().openSession();
	session.createNativeQuery("DROP TABLE TREE");
	session.close();
    }

    @Test
    public void test1_AddNode() throws Exception {

	Identifier nodeId = ctrl.addNode("Node 1", "Description 1", null, "Detail 1");

	Session session = HibernateUtil.getSessionFactory().openSession();
	Node node = session.get(Node.class, nodeId.getId());
	session.close();

	assertTrue(node.getCode().equals("Node 1"));
	assertTrue(node.getDescription().equals("Description 1"));
	assertTrue(node.getDetail().equals("Detail 1"));
    }

    @Test
    public void test2_AddNode() throws Exception {

	Identifier nodeId = ctrl.addNode("Node 2", "Description 2", null, "Detail 2");
	assertNull(nodeId);
    }

    @Test
    public void test3_GetRoot() throws Exception {

	List<Node> root = ctrl.getRoot();
	assertEquals(1, root.size());

	Identifier nodeId = ctrl.addNode("Node 2", "Description 2", 1L, "Detail 2");
	assertNotNull(nodeId);
	nodeId = ctrl.addNode("Node 3", "Description 3", 1L, "Detail 3");
	assertNotNull(nodeId);
	nodeId = ctrl.addNode("Node 4", "Description 4", 2L, "Detail 4");
	assertNotNull(nodeId);
	nodeId = ctrl.addNode("Node 5", "Description 5", 2L, "Detail 5");
	assertNotNull(nodeId);

	root = ctrl.getRoot();
	assertEquals(1, root.size());
    }

    @Test
    public void test4_UpdateNode() throws Exception {

	Identifier nodeId = ctrl.updateNode(2L, "Node 6", "Description 6", null, "Detail 6");
	Session session = HibernateUtil.getSessionFactory().openSession();
	Node node = session.get(Node.class, nodeId.getId());
	session.close();

	assertTrue(node.getCode().equals("Node 6"));
	assertTrue(node.getDescription().equals("Description 6"));
	assertTrue(node.getParentId().equals(1L));
	assertTrue(node.getDetail().equals("Detail 6"));
    }

    @Test
    public void test5_UpdateNode() throws Exception {

	Identifier nodeId = ctrl.updateNode(3L, null, null, 5L, null);
	Session session = HibernateUtil.getSessionFactory().openSession();
	Node node = session.get(Node.class, nodeId.getId());
	session.close();

	assertTrue(node.getCode().equals("Node 3"));
	assertTrue(node.getDescription().equals("Description 3"));
	assertTrue(node.getParentId().equals(5L));
	assertTrue(node.getDetail().equals("Detail 3"));
    }

    @Test
    public void test6_GetChildren() throws Exception {

	List<Node> children = ctrl.getChildren(3L);
	assertEquals(children.size(), 0);

	children = ctrl.getChildren(2L);
	assertEquals(children.size(), 2);
    }
}
