package com.wit.farmo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OuterMainObj {
    MainObj main;
    Rain rain = new Rain(0.1);
}
