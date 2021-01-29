package com.seungmoo.thejavatest.study;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.dependencies.SliceRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

public class ArchTests {

    @Test
    void packageDependencyTests() {
        // 우리 프로젝트의 root 패키지에서 부터 java class 들을 읽어온다.
        JavaClasses javaClasses = new ClassFileImporter().importPackages("com.seungmoo.thejavatest");

        /**
         * TODO ..domain.. 패키지에 있는 클래스는 ..study.., ..member.., ..domain에서 참조 가능.
         * TODO ..member.. 패키지에 있는 클래스는 ..study..와 ..member..에서만 참조 가능.
         * TODO (반대로) ..domain.. 패키지는 ..member.. 패키지를 참조하지 못한다.
         * TODO ..study.. 패키지에 있는 클래스는 ..study.. 에서만 참조 가능.
         * TODO 순환 참조 없어야 한다.
         */

        // TODO ..domain.. 패키지에 있는 클래스는 ..study.., ..member.., ..domain에서 참조 가능.
        ArchRule domainPackageRule = classes().that().resideInAPackage("..domain..")
                .should().onlyBeAccessed().byClassesThat().resideInAnyPackage("..study..", "..member..", "..domain..");
        domainPackageRule.check(javaClasses);

        // TODO (반대로) ..domain.. 패키지는 ..member.. 패키지를 참조하지 못한다.
        ArchRule memberPackageRule = noClasses().that().resideInAPackage("..domain..")
                .should().accessClassesThat().resideInAPackage("..member..");
        memberPackageRule.check(javaClasses);

        // TODO ..study.. 패키지에 있는 클래스는 ..study.. 에서만 참조 가능.
        ArchRule studyPackageRule = noClasses().that().resideOutsideOfPackage("..study..")
                .should().accessClassesThat().resideInAPackage("..study..");
        studyPackageRule.check(javaClasses);

        // TODO 순환 참조 없어야 한다.
        ArchRule sliceRule = slices().matching("..thejavatest.(*)..")
                .should().beFreeOfCycles();
        sliceRule.check(javaClasses);
    }

}
