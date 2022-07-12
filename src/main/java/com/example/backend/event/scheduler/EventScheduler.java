package com.example.backend.event.scheduler;

import com.example.backend.todo.domain.Todo;
import com.example.backend.todo.repository.TodoRepository;
import com.example.backend.user.domain.User;
import com.example.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventScheduler {

    private final UserRepository userRepository;
    private final TodoRepository todoRepository;

    //초, 분, 시, 일, 월, 요
    //요일에서 0과 7은 일요일이며, 1부터 월요일이고 6이 토요일이다.
//    매일 00:00:05 마다 반복
//    @Scheduled(cron = "5 0 0 * * ?")
    @Scheduled(cron = "*/10 * * * * ?")
    public void eventJob() {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        log.info("Event Job Start : " + sdf1.format(new Date()));

        //어제 날짜 구하기
        Calendar cal = Calendar.getInstance();
        cal.add(cal.DATE, -1); //날짜를 하루 뺀다.
        String yesterday = sdf2.format(cal.getTime());
        System.out.println(yesterday);

        List<User> users = userRepository.findAll();
//        for (User user: users) {
//
//        }

        List<Todo> todoList = todoRepository.findAllByUsersAndTodoDate(users, yesterday);


        System.out.println(users.size());
        System.out.println(todoList.size());




    }
}
