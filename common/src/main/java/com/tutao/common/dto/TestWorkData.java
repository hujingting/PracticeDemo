package com.tutao.common.dto;

import java.util.List;

/**
 * Created by jingting on 2018/1/29.
 */

public class TestWorkData extends BaseDto{

    private WorkDataTest data;

    public WorkDataTest getData() {
        return data;
    }

    public void setData(WorkDataTest data) {
        this.data = data;
    }

    public static class WorkDataTest {
        List<WorkDto> works;

        public List<WorkDto> getWorks() {
            return works;
        }

        public void setWorks(List<WorkDto> works) {
            this.works = works;
        }
    }
}
