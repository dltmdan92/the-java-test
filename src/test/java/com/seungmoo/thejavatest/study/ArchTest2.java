package com.seungmoo.thejavatest.study;

import com.seungmoo.thejavatest.TheJavaTestApplication;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

// ArchUnit 테스트 코드를 Annotaion 사용해서 작성 해보자
// 코드가 깔끔해진다는 장점, BUT DisplayName을 적어주지 못한다는 단점이 존재!!

/**
 * test 메서드를 생성하지 않았는데 어떻게 테스트가 실행되는가??
 * --> ArchUnit의 Extension 기능이 JUnit과는 별도의 엔진을 돌려서 테스트를 실행한다.
 */
@AnalyzeClasses(packagesOf = TheJavaTestApplication.class)
public class ArchTest2 {

    // TODO ..domain.. 패키지에 있는 클래스는 ..study.., ..member.., ..domain에서 참조 가능.
    @ArchTest
    ArchRule domainPackageRule = classes().that().resideInAPackage("..domain..")
            .should().onlyBeAccessed().byClassesThat().resideInAnyPackage("..study..", "..member..", "..domain..");

    // TODO (반대로) ..domain.. 패키지는 ..member.. 패키지를 참조하지 못한다.
    @ArchTest
    ArchRule memberPackageRule = noClasses().that().resideInAPackage("..domain..")
            .should().accessClassesThat().resideInAPackage("..member..");

    // TODO ..study.. 패키지에 있는 클래스는 ..study.. 에서만 참조 가능.
    @ArchTest
    ArchRule studyPackageRule = noClasses().that().resideOutsideOfPackage("..study..")
            .should().accessClassesThat().resideInAPackage("..study..");

    // TODO 순환 참조 없어야 한다.
    @ArchTest
    ArchRule sliceRule = slices().matching("..thejavatest.(*)..")
            .should().beFreeOfCycles();

}
