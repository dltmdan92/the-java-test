package com.seungmoo.thejavatest.study;

import com.seungmoo.thejavatest.member.Member;
import com.seungmoo.thejavatest.member.MemberService;
import com.seungmoo.thejavatest.test.Study;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * Mockito 소개
 * Mock: 진짜 객체와 비슷하게 동작하지만 프로그래머가 직접 그 객체의 행동을 관리하는 객체.
 * Mockito: Mock 객체를 쉽게 만들고 관리하고 검증할 수 있는 방법을 제공한다.
 *
 * <Mock 객체 Stubbing>
 * 모든 Mock 객체의 행동
 * •	Null을 리턴한다. (Optional 타입은 Optional.empty 리턴)
 * •	Primitive 타입은 기본 Primitive 값.
 * •	콜렉션은 비어있는 콜렉션.
 * •	Void 메소드는 예외를 던지지 않고 아무런 일도 발생하지 않는다.
 *
 * Mock 객체를 조작해서
 * •	특정한 매개변수를 받은 경우 특정한 값을 리턴하거나 예뢰를 던지도록 만들 수 있다.
 * •	How about some stubbing?
 * •	Argument matchers
 * •	Void 메소드 특정 매개변수를 받거나 호출된 경우 예외를 발생 시킬 수 있다.
 * •	Subbing void methods with exceptions
 * •	메소드가 동일한 매개변수로 여러번 호출될 때 각기 다르게 행동호도록 조작할 수도 있다.
 *
 */
@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

    // 클래스에서 MockitoExtenseion을 선언해주고 @Mock annotation을 셋팅하면 Mock 객체를 선언할 수 있다.
    /*
    @Mock
    MemberService memberService;

    @Mock
    StudyRepository studyRepository;
    */

    // Mock 객체를 파라미터 화 해서 만들어 줄 수 도 있다. (MockitoExtension 필요)
    // 이렇게 하면 각 테스트 별로 Mock 객체를 주입해 줄 수 있다.
    @Test
    void createStudyService(@Mock MemberService memberService,
                            @Mock StudyRepository studyRepository) {
        /*
        MemberService memberService = new MemberService() {
            @Override
            public Optional<Member> findById(Long memberId) {
                return Optional.empty();
            }
        };*/
        /*
        StudyRepository studyRepository = new StudyRepository() {
            @Override
            public List<Study> findAll() {
                return null;
            }

            @Override
            public List<Study> findAll(Sort sort) {
                return null;
            }

            @Override
            public List<Study> findAllById(Iterable<Long> iterable) {
                return null;
            }

            @Override
            public <S extends Study> List<S> saveAll(Iterable<S> iterable) {
                return null;
            }

            @Override
            public void flush() {

            }

            @Override
            public <S extends Study> S saveAndFlush(S s) {
                return null;
            }

            @Override
            public void deleteInBatch(Iterable<Study> iterable) {

            }

            @Override
            public void deleteAllInBatch() {

            }

            @Override
            public Study getOne(Long aLong) {
                return null;
            }

            @Override
            public <S extends Study> List<S> findAll(Example<S> example) {
                return null;
            }

            @Override
            public <S extends Study> List<S> findAll(Example<S> example, Sort sort) {
                return null;
            }

            @Override
            public Page<Study> findAll(Pageable pageable) {
                return null;
            }

            @Override
            public <S extends Study> S save(S s) {
                return null;
            }

            @Override
            public Optional<Study> findById(Long aLong) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(Long aLong) {
                return false;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(Long aLong) {

            }

            @Override
            public void delete(Study study) {

            }

            @Override
            public void deleteAll(Iterable<? extends Study> iterable) {

            }

            @Override
            public void deleteAll() {

            }

            @Override
            public <S extends Study> Optional<S> findOne(Example<S> example) {
                return Optional.empty();
            }

            @Override
            public <S extends Study> Page<S> findAll(Example<S> example, Pageable pageable) {
                return null;
            }

            @Override
            public <S extends Study> long count(Example<S> example) {
                return 0;
            }

            @Override
            public <S extends Study> boolean exists(Example<S> example) {
                return false;
            }
        };*/

        // 위의 장황한 코드들은 Mockito를 사용해서 간결화 할 수 있다. (Mockito로 Mock 객체 만들기)
        // 위에서 이거 말고 Annotation으로도 Mock 객체를 만들 수 있다.
        /*
        MemberService memberService = Mockito.mock(MemberService.class);
        StudyRepository studyRepository = Mockito.mock(StudyRepository.class);
        */

        memberService.validate(1L); // Mock객체의 void 메서드는 아무일도 일어나지 않는다.

        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        Member member = new Member();
        member.setId(1L);
        member.setEmail("seungmoo@email.com");

        // Mock 객체를 Stubbing 한다.
        // 1L라는 인자로 findById를 호출해야 member 객체를 Return 하라를 미리 셋팅해두었다.
        when(memberService.findById(1L)).thenReturn(Optional.of(member));

        // 아무 파라미터나 받아도 이게 수행된다. argument matcher를 통해서
        when(memberService.findById(any())).thenReturn(Optional.of(member));

        Study study = new Study(10, "java");

        // 위에서 Stubbing 한 내용이 수행된다. Mock 객체 memberService에 Mock stubbing을 설정해놨음
        studyService.createNewStudy(1L, study);

        // return하는 메서드 : 1L 로 호출하면 Exception 호출 한다.
        when(memberService.findById(1L)).thenThrow(new RuntimeException());
        // void 메서드 : 1L 로 호출하면 Exception 호출
        doThrow(new IllegalArgumentException()).when(memberService).validate(1L);
        //memberService.validate(1L); // 위의 Stubbing 때문에 예외 발생!!
        memberService.validate(2L);

        // 각각 3번 호출할 때마다, 다르게 Stubbing 해준다.
        when(memberService.findById(any()))
                .thenReturn(Optional.of(member))
                .thenThrow(new RuntimeException())
                .thenReturn(Optional.empty());

        // 첫 번째 호출
        Optional<Member> byId = memberService.findById(1L);
        assertEquals("seungmoo@email.com", byId.get().getEmail());

        // 두 번째 호출
        assertThrows(RuntimeException.class, () -> {
            memberService.findById(2L);
        });

        // 세 번째 호출
        assertEquals(Optional.empty(), memberService.findById(3L));

    }

    @DisplayName("스터빙 연습문제")
    @Test
    void studyMockitoStubbing(@Mock MemberService memberService,
                              @Mock StudyRepository studyRepository) {

        StudyService studyService = new StudyService(memberService, studyRepository);

        Study study = new Study(10, "테스트");

        Member member = new Member();
        member.setId(1L);
        member.setEmail("seungmoo@email.com");

        // TODO memberService 객체에 findById 메소드를 1L 값으로 호출하면 Optional.of(member) 객체를 리턴하도록 Stubbing
        when(memberService.findById(1L)).thenReturn(Optional.of(member));

        // TODO studyRepository 객체에 save 메소드를 study 객체로 호출하면 study 객체 그대로 리턴하도록 Stubbing
        when(studyRepository.save(study)).thenReturn(study);

        // 여기서 위의 Stubbing이 돌아야 createNewStudy에서 findById 메서드 돌렸을 때 객체가 리턴될 것임.
        studyService.createNewStudy(1L, study);

        assertNotNull(study.getOwner());
        assertEquals(member, study.getOwner());

    }
}