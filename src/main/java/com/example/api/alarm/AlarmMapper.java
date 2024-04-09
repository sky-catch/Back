package com.example.api.alarm;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AlarmMapper {

    //todo 알람읽기 만들기
    void readAlarm(long memberId);

    //todo 홈페이지 풀러올 때 사용
    List<GetAlarm> getAlarmList(long memberId);

    void creatAlarm(Alarm alarm);
    void creatReviewAlarm(Alarm alarm);
}
