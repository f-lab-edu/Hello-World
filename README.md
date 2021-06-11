# 🌐 Hello World!

- **Share Your Passion Through Languages** <br/> <br/>

![image](https://user-images.githubusercontent.com/68094005/116848654-02c3fc80-ac28-11eb-97c5-85a984ef7806.png)
(사진 출처: https://cudoo.com/blog/why-language-exchange-platforms-are-perfect-for-language-practice/)

## 🗺️ 개요 
언어 학습을 하면서 과거에 여러 언어 교환 플랫폼을 사용해 본 경험이 있습니다. 당시에는 무심결하게 넘어갔지만 개발 공부를 시작하면서 다시금 떠오른 생각이 **도대체 어떻게 전 세계에서 들어오는 수많은 트래픽을 견디면서 장애없이 서비스를 지속할 수 있을까?** 에 대한 부분이었습니다. 또한 **여러 사용자로부터 발생하는 수많은 데이터는 어떻게 보관하고 있는지**에 대한 부분도 궁금했습니다. 본 프로젝트는 이러한 궁금증과 언어 학습에 대한 개인적인 애정으로부터 시작하게 되었고, 전 세계 곳곳에 거주하고 있는 여러 언어 학습자들을 연결해주고 있는 다양한 언어교환 플랫폼을 모티브로 하고 있습니다. 

## 🗺️ 중점과제

#### 1. 기능구현에만 집중하지 않기

- 빠르게 구현하는 것보다 더 중요한 것이 작성하는 코드나 사용하는 기술을 `정확하게` 알고 사용하는 것이라고 생각합니다.

- 때문에 새로운 기술을 적용할 때 해당 기술에 대한 학습도 함께 진행하여 기초를 튼튼히 세워 적재적소에 필요한 기능을 배치하고, 필요하면 응용도 할 수 있는 능력을 키우고자 합니다.

- 작성하는 코드에 대해서는 예시를 보고 단순히 `copy&paste`하는 것이 아니라 깊이 생각하고 이해함으로써 완전히 내것으로 만들 수 있도록 노력할 필요가 있습니다.

#### 2. 항상 대용량 트래픽을 염두에 두기

- 프로젝트의 최종 목표는 `대용량 트래픽 속에서도 빠르고 안정적인 서비스`를 만드는 법을 배우는 것입니다.

- 따라서, 항상 `다수의 트래픽을 기본 전제`로 하여 섬세하게 설계해 나가고자 합니다. 

#### 3. 유연하고 확장성이 좋은 코드 작성

- 기술이 발달할 수록 비즈니스 환경도 빠르게 변화합니다. 이러한 변화 속에서 살아남기 위해서는 `유연하고 확장성이 좋은 깔끔한 코드를 작성하는 것을 목표`로 해야 합니다.

- 이를 달성하도록 도와주는 것이 바로 `객체지향 패러다임`입니다.

- 이러한 이유로 본 프로젝트에서는 `객체지향에 충실한 코드를 작성해보는 것`을 최우선 순위 중 하나로 두었습니다.

#### 4. 테스트 코드 작성에 충실하기

- 올바른 기능 구현을 위해 구조를 적절히 설계하고 이를 뒷받침 해줄 깔끔한 코드를 작성하는 것은 중요합니다. 그러나 이보다 더 중요한 것이 `구현한 기능을 제대로 테스트 하는 것`입니다.  

- 실제로 어플리케이션 서버를 구동해서 구현한 기능을 테스트 할 수 있지만 이 방법은 `명확한 한계`가 존재합니다.

```
예시
1. 해당 어플리케이션이 의존하는 서비스(ex. DB) 들이 사전에 구동 중이어야 함
2. 테스트 내용을 변경하기 위해서는 값을 변경한 뒤 컴파일, 빌드 등의 과정을 다시 거쳐야 하므로 시간 소요도 많이되고 번거로움
3. 가장 큰 문제는 `눈에 보이는 부분`만 확인이 가능하다는 점 
```

- 테스트 코드를 활용하면 여러 조건 하의 다양한 시나리오에서 실패, 성공을 `확실하게 확인`할 수 있습니다.   

- 또한, 테스트 코드는 구현한 기능이 의도대로 동작한다는 것을 일정 수준 이상 보장해 주는 역할을 합니다.

- 특히, 단위 테스트의 경우 다른 의존성을 `Stub으로 대체`하기 때문에 해당 컴포넌트들로 부터 발생한 문제의 전이로 인한 실패를 미연에 방지할 수 있고, 본 기능만 테스트 하므로 `실행속도가 매우 빨라` 언제든지 테스트를 부담 없이 실행해볼 수 있다는 장점이 있습니다.

- 그래서, 작성한 비즈니스 로직에 대한 단위테스트를 최대한 꼼꼼히 작성함으로써 테스트 커버리지를 높여 기능의 명확한 동작을 보장하고, 차후에 코드를 리팩토링하거나 CI를 도입하는데 있어서도 이러한 테스트 코드를 바탕으로 신속하고 정확하게 문제가 없음을 보증할 수 있도록 하는 것을 목표로 합니다.
 
## 🗺️ 사용기술
- [Java 11](https://docs.oracle.com/en/java/javase/11/)
- [Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [JUnit](https://junit.org/junit5/docs/current/user-guide/)
- [MySQL 8.0](https://dev.mysql.com/doc/refman/8.0/en/)
- [MyBatis](https://mybatis.org/mybatis-3/)
- [Redis](https://redis.io/documentation)
- [Kafka](https://kafka.apache.org/documentation/)
- [Jenkins](https://www.jenkins.io/doc/)
- [Firebase Cloud Messaging(FCM)](https://firebase.google.com/docs/cloud-messaging)
- [nGrinder](http://naver.github.io/ngrinder/)
- [pinpoint](https://github.com/pinpoint-apm/pinpoint)
- [Naver Cloud](https://www.navercloudcorp.com/)
- [Google Cloud Platform](https://console.cloud.google.com/compute/instances?hl=ko&orgonly=true&project=scientific-glow-309614&supportedpurview=organizationId)

## 🗺️ 프로젝트 전체 구성도
![image](https://user-images.githubusercontent.com/68094005/120600730-114e3f80-c484-11eb-9255-8ef7756afce2.png)

## 🗺️ DB ERD
![image](https://user-images.githubusercontent.com/68094005/120588923-0212c600-c473-11eb-90c7-aad73a9079a5.png)


## 🗺️ 기능정의
- [내용 살펴보기](https://github.com/f-lab-edu/Hello-World/wiki/01-Use-Case)

## 🗺️ 테크니컬 이슈와 해결과정
- [내용 살펴보기](https://github.com/f-lab-edu/Hello-World/wiki/04-Issues-&-Resolutions-(Blogs))
