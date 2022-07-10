package com.example.backend.friend.service;

import com.example.backend.exception.CustomException;
import com.example.backend.exception.ErrorCode;
import com.example.backend.friend.domain.FriendRequest;
import com.example.backend.friend.dto.FriendRequestDto;
import com.example.backend.friend.repository.FriendRequestRepository;
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

@Service
@RequiredArgsConstructor
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;

    @Transactional
    public String requestFriend(FriendRequestDto requestDto, String email) {

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        User userTo = userRepository.findByUsername(requestDto.getNick()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

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

        FriendRequest request = new FriendRequest(user, userTo);

        friendRequestRepository.save(request);
        return "친구 요청을 보냈습니다";
    }

    @Transactional
    public void accept(String userEmail, FriendRequestDto requestDto) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        User userTo = userRepository.findByUsername(requestDto.getNick()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        // 나도 request 보냄 ( state = true  인 상태로 )
        FriendRequest request = new FriendRequest(user, userTo, true);
        // 상대방 state true 로 변경
        for (FriendRequest friendRequest : friendRequestRepository.findAllByUserFromUserSeq(userTo.getUserSeq())) {
            if (friendRequest.getUserTo() == user && !friendRequest.isState()) {
                friendRequest.linked();
            }
        }
        friendRequestRepository.save(request);
    }

    @Transactional
    public void requestAccept(String userEmail, String nick) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        User userTo = userRepository.findByUsername(nick).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        // 나도 request 보냄 ( state = true  인 상태로 )
        FriendRequest request = new FriendRequest(user, userTo, true);
        // 상대방 state true 로 변경
        for (FriendRequest friendRequest : friendRequestRepository.findAllByUserFromUserSeq(userTo.getUserSeq())) {
            if (friendRequest.getUserTo() == user && !friendRequest.isState()) {
                friendRequest.linked();
            }
        }
        friendRequestRepository.save(request);
    }

    @Transactional
    public void reject(String userEmail, FriendRequestDto requestDto) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        User userTo = userRepository.findByUsername(requestDto.getNick()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        // 상대방의 request 삭제
        for (FriendRequest friendRequest : friendRequestRepository.findAllByUserFromUserSeq(userTo.getUserSeq())) {
            if (friendRequest.getUserTo() == user && !friendRequest.isState()) {
                friendRequestRepository.delete(friendRequest);
            }
        }
    }

    @Transactional
    public void delete(String userEmail, FriendRequestDto requestDto) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        User userTo = userRepository.findByUsername(requestDto.getNick()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        // 상대방 요청 삭제
        for (FriendRequest friendRequest : friendRequestRepository.findAllByUserFromUserSeq(userTo.getUserSeq())) {
            if (friendRequest.getUserTo() == user && friendRequest.isState()) {
                friendRequestRepository.delete(friendRequest);
            }
        }
        // 내 요청 삭제
        for (FriendRequest friendRequest : friendRequestRepository.findAllByUserFromUserSeq(user.getUserSeq())) {
            if (friendRequest.getUserTo() == userTo && friendRequest.isState()) {
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
                UserResponseDto responseDto = new UserResponseDto(friendRequest.getUserTo());
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
                UserResponseDto responseDto = new UserResponseDto(friendRequest.getUserFrom());
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
        // 공개 범위 및 친구 관계 확인
        if (userFriend.getPublicScope() == PublicScope.FRIEND) {
            // 친구인지 확인
            FriendRequest f = friendRequestRepository.findRelation(user, userFriend);
            if (f == null) {
                return new UserTodoResponseDto(userFriend, null);
            } else if (f.isState()) {
                // 오늘의 todo필요
                List<Todo> todoList = todoRepository.findAllByTodoDate(userFriend);
                List<TodoResponseDto> responseDtoList = new ArrayList<>();
                for (Todo t : todoList) {
                    responseDtoList.add(new TodoResponseDto(t));
                }
                return new UserTodoResponseDto(userFriend, responseDtoList);
            }
            else {
                return new UserTodoResponseDto(userFriend, null);
            }
        } else if (userFriend.getPublicScope() == PublicScope.NONE) {
            return new UserTodoResponseDto(userFriend, null);
        } else {
            // 오늘의 todo필요
            List<Todo> todoList = todoRepository.findAllByTodoDate(userFriend);
            List<TodoResponseDto> responseDtoList = new ArrayList<>();
            for (Todo t : todoList) {
                responseDtoList.add(new TodoResponseDto(t));
            }
            return new UserTodoResponseDto(userFriend, responseDtoList);
        }
    }
}
