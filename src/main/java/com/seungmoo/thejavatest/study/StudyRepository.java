package com.seungmoo.thejavatest.study;

import com.seungmoo.thejavatest.test.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Long> {
}
