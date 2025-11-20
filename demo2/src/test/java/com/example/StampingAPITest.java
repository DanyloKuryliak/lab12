package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

public class StampingAPITest {

    @Test
    void testSignatureCreation() {
        Task task = new TestTask("testTask");
        Signature sig = new Signature(task, "arg1", "arg2");
        
        assertNotNull(sig.getId());
        assertEquals("testTask", sig.getTask().getName());
        assertEquals(2, sig.getArgs().length);
    }

    @Test
    void testSignatureStamping() {
        Task task = new TestTask("testTask");
        Signature sig = new Signature(task, "arg1");
        
        sig.stamp("key1", "value1");
        sig.stamp("key2", 42);
        
        assertTrue(sig.hasStamp("key1"));
        assertEquals("value1", sig.getStamp("key1"));
        assertEquals(42, sig.getStamp("key2"));
    }

    @Test
    void testGroupCreation() {
        Task task1 = new TestTask("task1");
        Task task2 = new TestTask("task2");
        
        Signature sig1 = new Signature(task1, "arg1");
        Signature sig2 = new Signature(task2, "arg2");
        
        Group group = new Group(sig1, sig2);
        
        assertNotNull(group.getGroupId());
        assertEquals(2, group.size());
    }

    @Test
    void testGroupStamping() {
        Task task1 = new TestTask("task1");
        Task task2 = new TestTask("task2");
        
        Signature sig1 = new Signature(task1, "arg1");
        Signature sig2 = new Signature(task2, "arg2");
        
        Group group = new Group(sig1, sig2);
        group.stamp("groupKey", "groupValue");
        
        assertEquals("groupValue", group.getStamp("groupKey"));
    }

    @Test
    void testGroupApplyAutoStamping() {
        Task task1 = new TestTask("task1");
        Task task2 = new TestTask("task2");
        
        Signature sig1 = new Signature(task1, "arg1");
        Signature sig2 = new Signature(task2, "arg2");
        
        Group group = new Group(sig1, sig2);
        String groupId = group.getGroupId();
        
        List<Object> results = group.apply();
        
        assertEquals(2, results.size());
        assertTrue(sig1.hasStamp("groupId"));
        assertEquals(groupId, sig1.getStamp("groupId"));
        assertTrue(sig2.hasStamp("groupId"));
        assertEquals(groupId, sig2.getStamp("groupId"));
    }

    @Test
    void testGroupHeaderStamping() {
        Task task = new TestTask("task1");
        Signature sig = new Signature(task, "arg1");
        
        Group group = new Group(sig);
        group.stamp("headerKey", "headerValue");
        
        List<Object> results = group.apply();
        
        assertEquals(1, results.size());
        assertTrue(sig.hasStamp("groupId"));
        assertTrue(sig.hasStamp("headerKey"));
        assertEquals("headerValue", sig.getStamp("headerKey"));
    }

    @Test
    void testSignatureClonePreservesStamps() {
        Task task = new TestTask("task1");
        Signature original = new Signature(task, "arg1");
        original.stamp("key1", "value1");
        
        Signature cloned = original.clone();
        
        assertTrue(cloned.hasStamp("key1"));
        assertEquals("value1", cloned.getStamp("key1"));
        assertNotSame(original, cloned);
    }

    static class TestTask implements Task {
        private final String name;
        private int callCount = 0;

        TestTask(String name) {
            this.name = name;
        }

        @Override
        public Object apply(Object... args) {
            callCount++;
            return "Result from " + name + " with " + args.length + " args (call #" + callCount + ")";
        }

        @Override
        public String getName() {
            return name;
        }
    }
}

