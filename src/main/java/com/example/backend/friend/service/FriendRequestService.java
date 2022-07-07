package com.example.backend.friend.service;

import com.example.backend.exception.CustomException;
import com.example.backend.exception.ErrorCode;
import com.example.backend.friend.domain.FriendRequest;
import com.example.backend.friend.dto.FriendRequestDto;
import com.example.backend.friend.repository.FriendRequestRepository;
import com.example.backend.todo.dto.TodoResponseDto;
import com.example.backend.user.domain.User;
import com.example.backend.user.dto.UserResponseDto;
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

    @Transactional
    public String requestFriend(FriendRequestDto requestDto, String email) {

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        User userTo = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(
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
                this.accept(user.getEmail(), userTo.getEmail());
                return "요청을 수락했습니다";
            }
        }

        FriendRequest request = new FriendRequest(user, userTo);

        friendRequestRepository.save(request);
        return "친구 요청을 보냈습니다";
    }

    @Transactional
    public void accept(String userEmail, String requestEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        User userTo = userRepository.findByEmail(requestEmail).orElseThrow(
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
    public void reject(String userEmail, String requestEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        User userTo = userRepository.findByEmail(requestEmail).orElseThrow(
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
    public void delete(String userEmail, String requestEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        User userTo = userRepository.findByEmail(requestEmail).orElseThrow(
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

    public List<UserResponseDto> getList(String email) {
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
}
