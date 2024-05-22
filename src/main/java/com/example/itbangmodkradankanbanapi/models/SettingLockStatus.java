package com.example.itbangmodkradankanbanapi.models;

import java.util.Arrays;

public class SettingLockStatus {
    private final  static Integer[] LOCK_STATUSES_ID = {1,7};
    public static Boolean isLockStatusId(Integer id){
        return Arrays.asList(LOCK_STATUSES_ID).contains(id);
    }
}
