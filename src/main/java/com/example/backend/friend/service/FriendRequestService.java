package com.example.backend.friend.service;

import com.example.backend.character.domain.Characters;
import com.example.backend.character.dto.CharacterResponseDto;
import com.example.backend.character.repository.CharacterRepository;
import com.example.backend.character.service.CharacterService;
import com.example.backend.exception.CustomException;
import com.example.backend.exception.ErrorCode;
import com.example.backend.friend.domain.FriendRequest;
import com.example.backend.friend.dto.FriendRequestDto;
import com.example.backend.friend.repository.FriendRequestRepository;
import com.example.backend.notification.domain.Type;
import com.example.backend.notification.dto.NotificationRequestDto;
import com.example.backend.notification.service.EmitterService;
import com.example.backend.notification.service.NotificationService;
import com.example.backend.todo.domain.Todo;
import com.example.backend.todo.dto.response.TodoResponseDto;
import com.example.backend.todo.repository.TodoRepository;
import com.example.backend.user.domain.PublicScope;
import com.example.backend.user.domain.User;
import com.example.backend.user.dto.UserResponseDto;
import com.example.backend.user.dto.UserTodoResponseDto;
import com.example.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final CharacterService characterService;
    private final CharacterRepository characterRepository;
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;
    private final NotificationService notificationService;


    @Transactional
    public String requestFriend(FriendRequestDto requestDto, String email) {

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        User userTo = userRepository.findByUsername(requestDto.getNick()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        String message;

        // 나에게 보내는 요청 예외처리
        if (user == userTo) {
            throw new CustomException(ErrorCode.SELF_REQUEST);
        }

        // if (요청을 보낸적이 있는가) else if (친구로 등록 되어 있는가)
        for (FriendRequest friendRequest : friendRequestRepository.findAllByUserFromUserSeq(user.getUserSeq())) {
            if (friendRequest.getUserTo() == userTo && !friendRequest.isState()) {
                throw new CustomException(ErrorCode.EXISTING_REQUEST);
            } else if (friendRequest.getUserTo() == userTo && friendRequest.isState()) {
                throw new CustomException(ErrorCode.EXISTING_FRIEND);
            }
        }

        // 상대가 보낸 요청에 대한 수락인가
        for (FriendRequest friendRequest : friendRequestRepository.findAllByUserFromUserSeq(userTo.getUserSeq())) {
            if (friendRequest.getUserTo() == user && !friendRequest.isState()) {
                this.requestAccept(user.getEmail(), userTo.getEmail());
                return "요청을 수락했습니다";
            }
        }

        // 요청 받는 상대에게 알림 전송
        NotificationRequestDto notificationRequestDto = new NotificationRequestDto(
                Type.친구,
                user.getUsername()+"님이 "+userTo.getUsername()+"님께 친구 신청을 하셨습니다! 친구 페이지로 이동하셔서 수락 또는 거절 하실 수 있어요!"
        );

        notificationService.sendNotification(userTo.getUserSeq(), notificationRequestDto);

        friendRequestRepository.save(new FriendRequest(user, userTo));
        return "친구 요청을 보냈습니다";
    }

    @Transactional
    public void accept(String userEmail, FriendRequestDto requestDto) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        User friend = userRepository.findByUsername(requestDto.getNick()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        // 요청이 온 적 없는데 수락하는 경우
        FriendRequest f = friendRequestRepository.findRelation(friend, user).orElseThrow(
                () -> new CustomException(ErrorCode.NO_FRIEND_REQUEST_FROM)
        );
        // 친구인데 수락하는 경우
        if (f.isState()) {
            throw new CustomException(ErrorCode.EXISTING_FRIEND);
        }
        // 나도 request 보냄 ( state = true  인 상태로 )
        FriendRequest request = new FriendRequest(user, friend, true);
        // 상대방 state true 로 변경
        for (FriendRequest friendRequest : friendRequestRepository.findAllByUserFromUserSeq(friend.getUserSeq())) {
            if (friendRequest.getUserTo() == user && !friendRequest.isState()) {
                friendRequest.linked();
            }
        }

        // 처음 요청 보낸 사람에게 알림 전송
        NotificationRequestDto notificationRequestDto = new NotificationRequestDto(
                Type.친구,
                user.getUsername()+"님이 친구 요청을 수락하셨습니다."
        );
        notificationService.sendNotification(friend.getUserSeq(), notificationRequestDto);

        friendRequestRepository.save(request);
    }

    // 위 메서드에서 FriendRequestDto requestDto -> String nick 로 변환 ( 해당 클래스에서 사용 하는 메서드 )
    @Transactional
    public void requestAccept(String userEmail, String nick) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        User userTo = userRepository.findByUsername(nick).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        // 예외처리에 걸리지 않는 경우 나도 request 보냄 ( state = true  인 상태로 )
        friendRequestRepository.save(new FriendRequest(user, userTo, true));
        // 상대방 state true 로 변경
        for (FriendRequest friendRequest : friendRequestRepository.findAllByUserFromUserSeq(userTo.getUserSeq())) {
            if (friendRequest.getUserTo() == user && !friendRequest.isState()) {
                friendRequest.linked();
            }
        }
    }

    @Transactional
    public void reject(String userEmail, FriendRequestDto requestDto) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        User friend = userRepository.findByUsername(requestDto.getNick()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        // 요청이 온 적 없는데 거절하는 경우 예외 처리
        FriendRequest f = friendRequestRepository.findRelation(friend, user).orElseThrow(
                () -> new CustomException(ErrorCode.NO_FRIEND_REQUEST_FROM)
        );
        // 이미 친구인데 거절하는 경우
        if (f.isState()) {
            throw new CustomException(ErrorCode.EXISTING_FRIEND);
        }
        // 상대방의 request 삭제
        for (FriendRequest friendRequest : friendRequestRepository.findAllByUserFromUserSeq(friend.getUserSeq())) {
            if (friendRequest.getUserTo() == user && !friendRequest.isState()) {
                friendRequestRepository.delete(friendRequest);
            }
        }

        // 처음 요청 보낸 사람에게 알림 전송
        NotificationRequestDto notificationRequestDto = new NotificationRequestDto(
                Type.친구,
                user.getUsername()+"님이 친구 요청을 거절하셨습니다."
        );
        notificationService.sendNotification(friend.getUserSeq(), notificationRequestDto);
    }

    @Transactional
    public void delete(String userEmail, FriendRequestDto requestDto) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        User friend = userRepository.findByUsername(requestDto.getNick()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        FriendRequest f = friendRequestRepository.findRelation(user, friend).orElseThrow(
                () -> new CustomException(ErrorCode.NO_FRIEND_REQUEST_TO)
        );
        // 친구가 아니고 요청을 받은 경우
        if (!f.isState()) {
            throw new CustomException(ErrorCode.NOT_FRIEND);
        }
        // 상대방 요청 삭제
        for (FriendRequest friendRequest : friendRequestRepository.findAllByUserFromUserSeq(friend.getUserSeq())) {
            if (friendRequest.getUserTo() == user && friendRequest.isState()) {
                friendRequestRepository.delete(friendRequest);
            }
        }
        // 내 요청 삭제
        for (FriendRequest friendRequest : friendRequestRepository.findAllByUserFromUserSeq(user.getUserSeq())) {
            if (friendRequest.getUserTo() == friend && friendRequest.isState()) {
                friendRequestRepository.delete(friendRequest);
            }
        }
    }

    public List<UserResponseDto> getFriendList(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        List<UserResponseDto> responseDtoList = new ArrayList<>();
        for (FriendRequest friendRequest : friendRequestRepository.findAllByUserFromUserSeq(user.getUserSeq())) {
            if (friendRequest.isState()) {
                Characters character = characterRepository.findById(friendRequest.getUserTo().getUserSeq()).orElseThrow(
                        () -> new CustomException(ErrorCode.CHARACTER_NOT_FOUND)
                );
                UserResponseDto responseDto = new UserResponseDto(friendRequest.getUserTo(), character);
                responseDtoList.add(responseDto);
            }
        }
        return responseDtoList;
    }

    public List<UserResponseDto> getRequestList(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        List<UserResponseDto> responseDtoList = new ArrayList<>();
        for (FriendRequest friendRequest : friendRequestRepository.findAllByUserToUserSeq(user.getUserSeq())) {
            if (!friendRequest.isState()) {
                Characters c = characterRepository.findById(friendRequest.getUserFrom().getUserSeq()).orElseThrow(
                        () -> new CustomException(ErrorCode.CHARACTER_NOT_FOUND)
                );
                UserResponseDto responseDto = new UserResponseDto(friendRequest.getUserFrom(), c);
                responseDtoList.add(responseDto);
            }
        }
        return responseDtoList;
    }

    @Transactional
    public UserTodoResponseDto getFriendPage(String email, String nickFriend) {

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        User userFriend = userRepository.findByUsername(nickFriend).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        CharacterResponseDto characterResponseDto = characterService.getCharacterInfo(userFriend.getEmail());
        // 공개 범위 및 친구 관계 확인
        List<TodoResponseDto> responseDtoList = new ArrayList<>();
        if (userFriend.getPublicScope() == PublicScope.FRIEND) {
            // 친구인지 확인
            Optional<FriendRequest> f = friendRequestRepository.findRelation(user, userFriend);
            Optional<FriendRequest> f2 = friendRequestRepository.findRelation(userFriend, user);
            if (f.isEmpty() || f2.isEmpty()) {
                return new UserTodoResponseDto(userFriend, responseDtoList, characterResponseDto);
            } else {
                // 오늘의 todo필요
                List<Todo> todoList = todoRepository.findAllByTodoDate(userFriend);
                for (Todo t : todoList) {
                    responseDtoList.add(new TodoResponseDto(t));
                }
                return new UserTodoResponseDto(userFriend, responseDtoList, characterResponseDto);
            }
        } else if (userFriend.getPublicScope() == PublicScope.NONE) {
            return new UserTodoResponseDto(userFriend, responseDtoList, characterResponseDto);
        } else {
            // 오늘의 todo필요
            List<Todo> todoList = todoRepository.findAllByTodoDate(userFriend);
            for (Todo t : todoList) {
                responseDtoList.add(new TodoResponseDto(t));
            }
            return new UserTodoResponseDto(userFriend, responseDtoList, characterResponseDto);
        }
    }

    public void cancel(String userEmail, FriendRequestDto requestDto) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        User userTo = userRepository.findByUsername(requestDto.getNick()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        // 요청을 보낸 적이 있는가?
        // 없다면 에러
        // 있는데 이미 친구이면 에러
        // 있고 친구도 아니면 요청 삭제
        FriendRequest friendRequest = friendRequestRepository.findRelation(user, userTo).orElseThrow(
                () -> new CustomException(ErrorCode.NO_FRIEND_REQUEST_TO)
        );
        if (!friendRequest.isState()) {
            throw new CustomException(ErrorCode.EXISTING_FRIEND);
        } else {
            friendRequestRepository.delete(friendRequest);
        }
    }
}
