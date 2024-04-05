package com.example.api.alarm;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AlarmMapper {

    //todo 자신의 알람리스트
    List<Alarm> getAlarmList(long memberId);
    void creatAlarm(Alarm alarm);
}
