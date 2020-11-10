package com.guarda.ethereum.sapling.tree;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guarda.ethereum.ZCashException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import timber.log.Timber;

import static com.guarda.ethereum.sapling.tree.SaplingMerkleTree.SAPLING_INCREMENTAL_MERKLE_TREE_DEPTH;


public class IncrementalWitness {
    private SaplingMerkleTree tree;
    private List<String> filled;
    private SaplingMerkleTree cursor;
    private int cursor_depth;


    public IncrementalWitness(SaplingMerkleTree tree) {
        this.tree = tree;
        filled = new ArrayList<>();
        cursor_depth = 0;
        cursor = null;
    }

    public static IncrementalWitness fromJson(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<IncrementalWitness>(){}.getType();
        return gson.fromJson(json, type);
    }

    public static String toJson(IncrementalWitness iw) {
        Gson gson = new Gson();
        return gson.toJson(iw);
    }

    public MerklePath path() {
        try {
            return tree.path(partialPath());
        } catch (ZCashException e) {
            e.printStackTrace();
            Timber.d("IncrementalWitness path e=%s", e.getMessage());
        }
        return null;
    }

    private PathFiller partialPath() {
        LinkedList<String> uncles = new LinkedList<>(filled);
        if (cursor != null) {
            try {
                uncles.add(cursor.root(cursor_depth, new PathFiller()));
            } catch (ZCashException e) {
                e.printStackTrace();
                Timber.d("IncrementalWitness partialPath e=%s", e.getMessage());
            }
        }
        return new PathFiller(uncles);
    }

    public int position() {
        return tree.size() - 1;
    }

    public void append(String hash) throws ZCashException {
        append_inner(hash, SAPLING_INCREMENTAL_MERKLE_TREE_DEPTH);
//        append_inner(hash, INCREMENTAL_MERKLE_TREE_DEPTH_TESTING);
    }

    public void append_inner(String hash, int depth) throws ZCashException {
        if (cursor != null) {
            cursor.append(hash);

            if (cursor.isComplete(cursor_depth)) {
                Timber.d("cursor.isComplete(cursor_depth) %d", cursor_depth);
                filled.add(cursor.root(cursor_depth, new PathFiller()));
                cursor = null;
            }
        } else {
            cursor_depth = tree.nextDepth(filled.size());
            Timber.d("append_inner cursor == null cursor_depth=%d", cursor_depth);
            if (cursor_depth >= depth) {
                Timber.e("IncrementalWitness append_inner cursor_depth >= depth cd=%d", cursor_depth);
            }

            if (cursor_depth == 0) {
                filled.add(hash);
            } else {
                cursor = new SaplingMerkleTree();
                cursor.append(hash);
            }
        }
    }

    public String root() {
        try {
//            return tree.root(INCREMENTAL_MERKLE_TREE_DEPTH_TESTING, partialPath());
            return tree.root(SAPLING_INCREMENTAL_MERKLE_TREE_DEPTH, partialPath());
        } catch (ZCashException e) {
            e.printStackTrace();
            Timber.e("SaplingMerkleTree root() e=%s", e.getMessage());
        }
        return "";
    }

    public String element() throws ZCashException {
        return tree.last();
    }

    @Override
    public String toString() {
        return "IncrementalWitness{" +
                "tree=" + tree +
                ", filled=" + filled +
                ", cursor=" + cursor +
                ", cursor_depth=" + cursor_depth +
                '}';
    }
}
