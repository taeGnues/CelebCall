# Trouble Shooting
프로젝트를 진행하면서 발생한 문제점들과 해결법 서술합니다.

## 최초 리뷰 피드백 반영
```
user_roles 사용자 역할이라는 도메인의 역할이 너무 작게 느껴집니다. 
단순하게 사용자 역할이라는 테이블을 별도 관리하도록 설계하신 이유가 무엇인가요? 
user 와 user_roles 의 관계가 1:1 이라고 한다면 더더욱 별도 테이블(도메인)을 만들기에는 도메인이 가진 역할이 너무 작지 않나 싶습니다. 

role 은 단순하게 사용자의 분류 체계를 갖기 위한 정도로 보이는데요. 
그렇다고 한다면 user domain 에 응집하여 관리하는 것이 좋을 것 같아요.

feed_like 에 대해서는 데이터가 많아질 경우를 대비하여 생각해보시면 좋겠네요. 
만일 하나의 게시글에 10만명이 좋아요를 누르면 어떻게 될까요? 

게시글을 entity 상에 load 할 때 10만개의 feed_like 를 fetch 하게 되니 성능상 문제가 될 것으로 보이네요. 
10만개 이상의 좋아요를 가진 게시글을 한번에 100개씩 조회한다고 한다면.. 
성능 부하가 더 심해지겠죠.
 
간단하게 개선점을 생각해볼만한 것 같은데 고민해보시고 개선해보면 좋겠네요.

댓글 좋아요 또한 마찬가지입니다. 댓글의 케이스는 더욱 심각하죠. 게시글 조회할 때 댓글 조회하고, 댓글에 속한 좋아요 조회하면.. 
성능이 더 나쁠 것으로 보입니다.

모든 도메인에 created_at / modified_at 과 같은 필드를 더 추가하여 
시간데이터에 대한 관리도 하면 좋겠네요. 기본적으로 언제 생성 수정되었는지 기록하는 것이 중요합니다.

최신순 정렬 기능도 보이는데, 시간 데이터가 없다면 정렬이 불가능하겠죠?

전체적으로 구현해야될 양이 조금 많긴 하네요 ^^; 진행하다가 일정상 어렵다면 필수와 선택 기능을 나누어 개발하시는 것도 좋겟네요. 
이번 프로젝트에서는 spring 에 대한 심도있는 이해를 하는 것에 우선순위를 두면 좋겠습니다~!
```
1. user - user_roles 일대일 관계 인점을 고려 -> user라는 하나의 도메인으로 정리
2. feed_like, comment_like 와 같이 10만개 이상의 좋아요 가진 게시글을 한번에 100개씩 조회 시 생기는 성능 부하 문제
  - feed_like, comment_like의 개수를 관리해주는 feed와 comment 도메인에 각각 like_count 라는 필드를 추가하자.
  - like의 경우, 개수만 전달해주면 될일이 많으므로 like_count만 수정해주면 된다. 
  - 등록된 게시글 정보의 경우, 자주 바뀌는 정보가 아니므로 redis를 통해 캐싱해준다.
4. created_at, modified_at으로 관리. -> 모든 도메인에 추가.
5. 주요 및 보조 기능으로 정리해서 프로젝트 진행 -> '신고' 기능은 제외해서 플젝 진행하기.

## Fetch Join의 이해
프록시 객체를 불러오고 사용 시, FetchType.LAZY를 했더라도 getUsername, getId와 같이 직접 접근하게 되면
프록시 초기화가 발생한다.

즉, 1개의 게시글을 조회할 때, 게시글을 작성한 User의 Username 필드도 필요하다면, 아래와 같은 상황이 발생한다.
```sql
select f.id, f.title, f.user_id from feed f ....; -- (1)
select u.id, u.username, u.email from user u where u.user_id = f.user_id -- (2)
```


https://velog.io/@j3beom/JPA-%ED%94%84%EB%A1%9D%EC%8B%9CProxy-%EA%B8%B0%EB%B3%B8

## QueryDSL 초기 설정하기
### Trouble
- querydsl의 설정은 다음과 같은 방식으로 진행한다.
https://blog.naver.com/innogrid/222725730056

- 구조 잡기
https://velog.io/@soyeon207/QueryDSL-Spring-Boot-%EC%97%90%EC%84%9C-QueryDSL-JPA-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0

- querydsl 사용이유
http://ssow93.tistory.com/69

- 오류 발생 시 :
https://lahezy.tistory.com/94

- 동적 정렬하기 : https://dingdingmin-back-end-developer.tistory.com/entry/Springboot-JPA-Querydsl-%EB%8F%99%EC%A0%81-%EC%A0%95%EB%A0%AC-OrderSpecifier

## PathVariable 설정 문제 (build and run - intellij로 설정 시)
### Trouble
querydsl를 사용하기 위해 기존 gradle 대신 intellij를 사용했다. 이때, 
```java
    @PatchMapping("/{groupname}")
    public ResponseEntity<String> updateGroupName(@PathVariable String groupname) {
        log.info("Updating group name {}", groupname);
        try{
            authService.updateGroupName(GroupName.valueOf(groupname));
        }catch (IllegalArgumentException e){
            throw new BaseException(ExceptionCode.WRONG_GROUPNAME);
        }
        return ResponseEntity.ok("그룹 가입에 성공하셨습니다.");
    }
```
위와 같이 patchvariable를 사용해도 적용이 안된다!

### Solution


설정 변경 적용을 위한 out 폴더 삭제 : https://sehyeona.tistory.com/32


## N+1 문제점 
처음 프로젝트 설계시 게시판 글 조회 시 댓글 목록도 함께 조회를 하고자 하였습니다. 그러자 한번 게시글 조회시 해당 게시글에 작성된 모든 댓글들도 함께 조회가 되었습니다.
그리고 이 부분들이 한번에 조회된 것이 아닌, 
- 게시글 조회 : 게시글에 작성된 댓글의 id가 함께 조회
- 각 댓글 조회 : 게시글 조회시 조회된 댓글 id를 통해 댓글 각각 조회
즉 댓글 갯수만큼의 조회가 더 발생하게 되었습니다. 이를 해결하고자 다음의 방법을 사용 & 시도해보았습니다

1. native query 작성
...

2. API 분리
...


궁금증 : 게시글 조회시 댓글도 함께 가져오는게 좋을까?
댓글 가져오는 API, 게시글 가져오는 API 분리하는게 좋지 않을까?

cascade로 게시글-댓글-댓글좋아요을 묶는게 바람직할까?
게시글의 삭제는 댓글, 댓글 좋아요 삭제로 생명주기가 묶이지만
댓글 삭제는 게시글의 삭제 이전에도 이루어질 수 있고,
댓글 좋아요 삭제 역시 댓글, 게시글의 삭제 이전에도 이뤄질 수 있다.