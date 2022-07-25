package com.example.backend.chat.service;

import com.example.backend.chat.domain.ChatRoom;
import com.example.backend.chat.domain.Participant;
import com.example.backend.chat.repository.ChatRoomRepository;
import com.example.backend.chat.repository.ParticipantRepository;
import com.example.backend.exception.CustomException;
import com.example.backend.exception.ErrorCode;
import com.example.backend.msg.MsgEnum;
import com.example.backend.user.domain.User;
import com.example.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

// StompHandler 에 ChatMessageService DI 시 The dependencies of some of the beans in the application context form a cycle 오류
// 원인 : ChatMessageService 에는 SimpMessageSendingOperations 가 선언되어 있음
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService2 {

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOperations;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOperations;

    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final ChatRoomRepository chatRoomRepository;

    public String getRoomId(Optional<String> simpDestination) {
        log.info("chat.service.ChatMessageService2.getRoomId()");
        if (simpDestination.isEmpty()) {
            throw new CustomException(ErrorCode.SUBSCRIBE_ERROR);
        }
        int lastIndex = simpDestination.get().lastIndexOf('/');
        if (lastIndex != -1) {
            return simpDestination.get().substring(lastIndex + 1);
        } else {
            return "";
        }
    }

    public Long getParticipantCount(String roomId) {
        log.info("chat.service.ChatMessageService2.getParticipant()");
        return Long.parseLong(Optional.ofNullable(valueOperations.get(MsgEnum.COUNT_PARTICIPANT.getMsg() + "_" + roomId)).orElse("0"));
    }

    public Long plusParticipantCount(String roomId) {
        log.info("chat.service.ChatMessageService2.plusParticipantCount");
        return Optional.ofNullable(valueOperations.increment(MsgEnum.COUNT_PARTICIPANT.getMsg() + "_" + roomId)).orElse(0L);
    }

    public Long minusParticipantCount(String roomId) {
        log.info("chat.service.ChatMessageService2.minusParticipantCount");
        return Optional.ofNullable(valueOperations.decrement(MsgEnum.COUNT_PARTICIPANT.getMsg() + "_" + roomId)).orElse(0L);
    }

    public void mapSessionAndParticipant(String email, String roomId, String sessionId) {
        log.info("chat.service.ChatMessageService2.mapSessionAndParticipant");
        Participant participant = this.findParticipant(email, roomId);
        hashOperations.put(MsgEnum.SESSION_PARTICIPANT_MAPPING.getMsg(), sessionId, participant.getId().toString());
    }

    @Transactional
    public Participant findParticipant(String email, String roomId) {
        log.info("chat.service.ChatMessageService2.findParticipant");
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new CustomException(ErrorCode.ROOM_NOT_FOUND)
        );
        return participantRepository.findByUserAndChatRoom(user, room).orElseThrow(
                () -> new CustomException(ErrorCode.PARTICIPANT_NOT_FOUND)
        );
    }

    public String exitParticipant(String sessionId) {
        log.info("chat.service.ChatMessageService2.exitParticipant");
        String Id = hashOperations.get(MsgEnum.SESSION_PARTICIPANT_MAPPING.getMsg(), sessionId);
        if (Id != null) {
            Long participantId = Long.parseLong(Id);
            return this.changeExitTime(participantId);
        }
        return null;
    }

    @Transactional
    public String changeExitTime(Long id) {
        log.info("chat.service.ChatMessageService2.changeExitTime");
        Participant participant = participantRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.PARTICIPANT_NOT_FOUND)
        );
        participant.exit();
        return participant.getChatRoom().getRoomId();
    }
}
