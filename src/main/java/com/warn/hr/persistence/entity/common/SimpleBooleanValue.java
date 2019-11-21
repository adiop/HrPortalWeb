package com.warn.hr.persistence.entity.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Data
public class SimpleBooleanValue {
    private Boolean value;
}
