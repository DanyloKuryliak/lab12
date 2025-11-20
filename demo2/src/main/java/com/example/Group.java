package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Group {
    private final List<Signature> signatures;
    private final Map<String, Object> header;
    private final String groupId;

    public Group(Signature... signatures) {
        this.signatures = new ArrayList<>();
        this.header = new HashMap<>();
        this.groupId = UUID.randomUUID().toString();
        
        for (Signature sig : signatures) {
            this.signatures.add(sig);
        }
    }

    public Group(List<Signature> signatures) {
        this.signatures = new ArrayList<>(signatures);
        this.header = new HashMap<>();
        this.groupId = UUID.randomUUID().toString();
    }

    public List<Object> apply() {
        List<Object> results = new ArrayList<>();
        
        for (Signature signature : signatures) {
            applyGroupStamping(signature);
            Object result = signature.apply();
            results.add(result);
        }
        
        return results;
    }

    private void applyGroupStamping(Signature signature) {
        if (!signature.hasStamp("groupId")) {
            signature.stamp("groupId", groupId);
        }
        
        for (Map.Entry<String, Object> entry : header.entrySet()) {
            if (!signature.hasStamp(entry.getKey())) {
                signature.stamp(entry.getKey(), entry.getValue());
            }
        }
    }

    public void stamp(String key, Object value) {
        header.put(key, value);
    }

    public Object getStamp(String key) {
        return header.get(key);
    }

    public Map<String, Object> getHeader() {
        return new HashMap<>(header);
    }

    public String getGroupId() {
        return groupId;
    }

    public List<Signature> getSignatures() {
        return new ArrayList<>(signatures);
    }

    public void addSignature(Signature signature) {
        signatures.add(signature);
    }

    public int size() {
        return signatures.size();
    }
}

