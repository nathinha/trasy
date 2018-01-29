package org.treasy.tree.control;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test0_getRoot() throws Exception {
	this.mockMvc.perform(get("/node")).andExpect(status().isOk()).andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void test1_addNode() throws Exception {

	MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
	params.add("code", "1");
	params.add("description", "1");
	params.add("parentId", null);
	params.add("detail", "1");

	this.mockMvc.perform(post("/node").params(params)).andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(Matchers.is(1)));
    }

    @Test
    public void test1_getChildren() throws Exception {
	this.mockMvc.perform(get("/node/1")).andExpect(status().isOk()).andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void test1_getRoot() throws Exception {
	this.mockMvc.perform(get("/node")).andExpect(status().isOk())
		.andExpect(jsonPath("$[0].id").value(Matchers.is(1)));
    }

    @Test
    public void test2_addNode() throws Exception {

	MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
	params.add("code", "2");
	params.add("description", "2");
	params.add("parentId", "1");
	params.add("detail", "2");

	this.mockMvc.perform(post("/node").params(params)).andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(Matchers.is(2)));
    }

    @Test
    public void test2_getChildren_1() throws Exception {
	this.mockMvc.perform(get("/node/1")).andExpect(status().isOk())
		.andExpect(jsonPath("$[0].id").value(Matchers.is(2)));
    }

    @Test
    public void test2_getChildren_2() throws Exception {
	this.mockMvc.perform(get("/node/2")).andExpect(status().isOk()).andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void test3_addNode() throws Exception {

	MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
	params.add("code", "3");
	params.add("description", "3");
	params.add("parentId", "1");
	params.add("detail", "3");

	this.mockMvc.perform(post("/node").params(params)).andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(Matchers.is(3)));
    }

    @Test
    public void test3_updateNode() throws Exception {

	MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
	params.add("id", "2");
	params.add("code", "4");
	params.add("description", "4");
	params.add("parentId", "3");
	params.add("detail", "4");

	this.mockMvc.perform(put("/node").params(params)).andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(Matchers.is(2)));
    }
}