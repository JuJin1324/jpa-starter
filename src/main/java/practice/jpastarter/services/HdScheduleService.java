package practice.jpastarter.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.jpastarter.dtos.ScheduleDto;
import practice.jpastarter.exceptions.ResourceNotFoundException;
import practice.jpastarter.models.delete.hard.HdMember;
import practice.jpastarter.models.delete.hard.HdSchedule;
import practice.jpastarter.repositories.delete.hard.HdMemberRepository;
import practice.jpastarter.repositories.delete.hard.HdScheduleMemberRepository;
import practice.jpastarter.repositories.delete.hard.HdScheduleRepository;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2021/11/06
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HdScheduleService implements ScheduleService {
    private final HdScheduleRepository       scheduleRepository;
    private final HdScheduleMemberRepository scheduleMemberRepository;
    private final HdMemberRepository         memberRepository;

    /**
     * 일정 생성
     *
     * @return Schedule ID
     */
    @Transactional
    @Override
    public Long createSchedule(String title, ZonedDateTime startTimeKST, ZonedDateTime endTimeKST, List<Long> memberIds) {
        List<HdMember> members = memberRepository.findAllById(memberIds);
        HdSchedule schedule = HdSchedule.newSchedule(title, startTimeKST, endTimeKST, members.toArray(new HdMember[0]));
        scheduleRepository.save(schedule);
        return schedule.getId();
    }

    /**
     * 일정 갱신
     */
    @Transactional
    @Override
    public void updateSchedule(Long scheduleId, String title, ZonedDateTime startTimeKST, ZonedDateTime endTimeKST, List<Long> memberIds) {
        List<HdMember> members = memberRepository.findAllById(memberIds);
        HdSchedule schedule = scheduleRepository.findWithAllById(scheduleId)
                .orElseThrow(ResourceNotFoundException::new);
        schedule.update(title, startTimeKST, endTimeKST, members.toArray(new HdMember[0]));
    }

    @Override
    public ScheduleDto getSingleSchedule(Long scheduleId) {
        return scheduleRepository.findWithAllById(scheduleId)
                .map(ScheduleDto::new)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    @Override
    public void deleteMembersInSchedule(Long scheduleId, List<Long> memberIds) {
        HdSchedule schedule = scheduleRepository.findWithAllById(scheduleId)
                .orElseThrow(ResourceNotFoundException::new);
        memberIds.forEach(memberId ->
                schedule.findScheduleMember(memberId)
                        .ifPresent(scheduleMember -> {
                            schedule.getScheduleMembers().remove(scheduleMember);
                            scheduleMemberRepository.delete(scheduleMember);
                        }));
    }
}