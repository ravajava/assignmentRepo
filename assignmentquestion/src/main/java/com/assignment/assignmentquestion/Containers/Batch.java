package com.assignment.assignmentquestion.Containers;

import java.util.List;

public class Batch {
    public Batch(int batchID, List<Record> recordList)
    {
        this.batchID = batchID;
        this.recordList = recordList;
    }

    private int batchID;
    private List<Record> recordList;

    public int getBatchID() {
        return batchID;
    }

    public List<Record> getRecordList() {
        return recordList;
    }
}
