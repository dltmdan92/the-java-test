# TestContainers를 사용해서 test 시작할 때 도커컨테이너를 띄우고, test가 끝나면 docker container를 내려주도록 해보자.
# TestContainers 를 사용하면 아래 처럼 script를 작성할 필요도 없고, 테스트용 properties 설정 및, Terminal에서 컨테이너를 띄우고 내리는 작업이 없다.
# 단점 : 테스트가 느려진다.

## 개발용 DB
docker run -p 5432:5432 --name springdata -e POSTGRES_USER=seungmoo -e POSTGRES_PASSWORD=1568919am! -e POSTGRES_DB=springdata -d postgres

## 테스트용 DB
docker run -p 15432:5432 --name springdata-test -e POSTGRES_USER=seungmoo -e POSTGRES_PASSWORD=1568919am! -e POSTGRES_DB=springdata_test -d postgres