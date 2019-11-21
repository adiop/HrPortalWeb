package com.warn.hr.persistence.entity.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Data
public  class SimpleIntegerValue {

    private Integer value;

    public void add(Integer v) {
        value += v;
    }

    public void add(Long v) {
        value += v.intValue();
    }

}
